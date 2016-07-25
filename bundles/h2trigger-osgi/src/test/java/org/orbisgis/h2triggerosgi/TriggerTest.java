package org.orbisgis.h2triggerosgi;

import org.h2gis.functions.factory.H2GISDBFactory;
import org.h2gis.utilities.SFSUtilities;
import org.junit.BeforeClass;
import org.junit.Test;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.corejdbc.DatabaseProgressionListener;
import org.orbisgis.corejdbc.StateEvent;
import org.orbisgis.corejdbc.TableEditEvent;
import org.orbisgis.corejdbc.TableEditListener;
import org.orbisgis.corejdbc.internal.DataManagerImpl;
import org.orbisgis.h2triggersosgi.EventListenerService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Nicolas Fortin
 */
public class TriggerTest {

    private static DataSource dataSource;


    @BeforeClass
    public static void tearUp() throws Exception {
        // Keep a connection alive to not close the DataBase on each unit test
        dataSource = H2GISDBFactory.createDataSource(TriggerTest.class.getSimpleName(), true);
    }

    @Test
    public void testListener() throws Exception {
        testListenerInternal(dataSource);
    }

    private void testListenerInternal(DataSource dataSourceParam) throws Exception {
        DataManager dataManager = new DataManagerImpl(dataSourceParam);
        LocalListener local = new LocalListener();
        dataManager.addDatabaseProgressionListener(local, StateEvent.DB_STATES.STATE_STATEMENT_END);
        EventListenerService evtServ = new EventListenerService();
        evtServ.setDataManager(dataManager);
        try(Connection connection = dataSourceParam.getConnection();
            Statement st = connection.createStatement()) {
            String query = "select * from GEOMETRY_COLUMNS";
            st.execute(query);
            Thread.sleep(1000);
            assertNotNull(local.getLastState());
            assertEquals(query, local.getLastState().getName());
        }
        dataManager.removeDatabaseProgressionListener(local);
        evtServ.disable();
        evtServ.unsetDataManager(dataManager);

    }

    @Test
    public void testTableTrigger() throws Exception {
        DataManager dataManager = new DataManagerImpl(dataSource);
        EventListenerService evtServ = new EventListenerService();
        evtServ.setDataManager(dataManager);
        EventStack tableEvents = new EventStack();
        try(Connection connection = dataSource.getConnection();
            Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS TEST");
            st.execute("CREATE TABLE TEST(ID serial)");
            // Install listener
            dataManager.addTableEditListener("TEST", tableEvents);
            assertTrue(tableEvents.getEvents().isEmpty());
            st.execute("INSERT INTO TEST VALUES (1)");
            Thread.sleep(1000);
            List<TableEditEvent> evts = tableEvents.getEvents();
            assertEquals(1, evts.size());
            assertNull(evts.get(0).getUndoableEdit());
            assertEquals("PUBLIC.TEST", evts.get(0).getTableName());
        } finally {
            dataManager.removeTableEditListener("TEST", tableEvents);
        }
        evtServ.disable();
        evtServ.unsetDataManager(dataManager);
    }

    @Test
    public void testListenerWithWrapper() throws Exception {
        testListenerInternal(SFSUtilities.wrapSpatialDataSource(dataSource));
    }

    private static class LocalListener implements DatabaseProgressionListener {
        private StateEvent lastState;

        @Override
        public void progressionUpdate(StateEvent state) {
            lastState = state;
        }

        public StateEvent getLastState() {
            return lastState;
        }
    }

    private static class EventStack implements TableEditListener {
        List<TableEditEvent> events = new LinkedList<>();
        @Override
        public void tableChange(TableEditEvent event) {
            events.add(event);
        }

        /**
         * @return List of events
         */
        public List<TableEditEvent> getEvents() {
            return events;
        }
    }
    private static class DummyRunnable implements Runnable {
        @Override
        public void run() {
        }
    }
}
