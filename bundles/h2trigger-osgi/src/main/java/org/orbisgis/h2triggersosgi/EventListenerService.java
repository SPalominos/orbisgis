/**
 * OrbisGIS is a java GIS application dedicated to research in GIScience.
 * OrbisGIS is developed by the GIS group of the DECIDE team of the 
 * Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
 *
 * The GIS group of the DECIDE team is located at :
 *
 * Laboratoire Lab-STICC – CNRS UMR 6285
 * Equipe DECIDE
 * UNIVERSITÉ DE BRETAGNE-SUD
 * Institut Universitaire de Technologie de Vannes
 * 8, Rue Montaigne - BP 561 56017 Vannes Cedex
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
 * Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.h2triggersosgi;


import org.h2.api.DatabaseEventListener;
import org.h2.api.Trigger;
import org.h2.jdbcx.JdbcDataSource;
import org.h2gis.utilities.JDBCUtilities;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.corejdbc.StateEvent;
import org.orbisgis.h2triggers.H2DatabaseEventListener;
import org.orbisgis.h2triggers.H2Trigger;
import org.orbisgis.h2triggers.TriggerFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Register and listen to H2 Database event system.
 * @author Nicolas Fortin
 */
@Component(immediate = true)
public class EventListenerService implements DatabaseEventListener, TriggerFactory {
    private DataManager dataManager;
    private Logger logger = LoggerFactory.getLogger(EventListenerService.class);
    private Queue<StateEvent> eventStack = new LinkedBlockingQueue<>();
    private AtomicBoolean eventProcessRunning = new AtomicBoolean(false);

    private static boolean isLocalH2DataBase(DatabaseMetaData meta) throws SQLException {
        return JDBCUtilities.isH2DataBase(meta)
                && meta.getURL().startsWith("jdbc:h2:")
                && !meta.getURL().startsWith("jdbc:h2:tcp:/");
    }

    @Reference
    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        DataSource dataSource = dataManager.getDataSource();
        // Link
        try(Connection connection = dataSource.getConnection();
            Statement st = connection.createStatement()) {
            if(isLocalH2DataBase(connection.getMetaData())) {
                H2DatabaseEventListener.setDelegateDatabaseEventListener(this);
                // Change DATABASE_EVENT_LISTENER for this Database instance
                st.execute("SET DATABASE_EVENT_LISTENER '" + H2DatabaseEventListener.class.getName() + "'");
                // H2 Database properties are not serialised. Then in order to keep the event listener
                // the JDBC url connection have to be changed
                try {
                    if (dataSource instanceof JdbcDataSource || dataSource.isWrapperFor(JdbcDataSource.class)) {
                        JdbcDataSource jdbcDataSource;
                        if (dataSource instanceof JdbcDataSource) {
                            jdbcDataSource = (JdbcDataSource) dataSource;
                        } else {
                            jdbcDataSource = dataSource.unwrap(JdbcDataSource.class);
                        }
                        if (!jdbcDataSource.getURL().toUpperCase().contains("DATABASE_EVENT_LISTENER")) {
                            jdbcDataSource.setURL(jdbcDataSource.getURL() + ";DATABASE_EVENT_LISTENER='" + H2DatabaseEventListener.class.getName() + "'");
                        }
                    }
                } catch (Exception ex) {
                    logger.warn("Cannot change connection URL:\n" + ex.getLocalizedMessage(), ex);
                }
                H2Trigger.setTriggerFactory(this);
            }
        } catch (SQLException ex) {
            logger.error(ex.getLocalizedMessage(), ex);
        }
    }

    @Deactivate
    public void disable() {
        H2DatabaseEventListener.setDelegateDatabaseEventListener(null);
        H2Trigger.setTriggerFactory(null);
    }

    public void unsetDataManager(DataManager dataManager) {
        // Unlink
        try(Connection connection = dataManager.getDataSource().getConnection();
            Statement st = connection.createStatement()) {
            if(isLocalH2DataBase(connection.getMetaData())) {
                st.execute("SET DATABASE_EVENT_LISTENER ''");
                H2Trigger.setTriggerFactory(null);
            }
        } catch (SQLException ex) {
            // Ignore
        }
    }

    @Override
    public void init(String url) {
        // Not used
    }

    @Override
    public void opened() {
        // Not used
    }

    @Override
    public void exceptionThrown(SQLException e, String sql) {
        // Not used
    }

    @Override
    public void setProgress(int state, String name, int x, int max) {
        if (dataManager != null && state < StateEvent.DB_STATES.values().length) {
            // Do not fire the event in the H2 thread in order to not raise
            // org.h2.jdbc.JdbcSQLException: Timeout trying to lock table XXX
            StateEvent.DB_STATES stateEnum = StateEvent.DB_STATES.values()[state];
            eventStack.add(new StateEvent(stateEnum, name, x, max));
            if (!eventProcessRunning.getAndSet(true)) {
                //SwingUtilities.invokeLater();
                new StateEventProcess(dataManager, eventStack, eventProcessRunning).execute();
            }
        }
    }

    @Override
    public void closingDatabase() {
        // Not used
    }

    @Override
    public Trigger createTrigger(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
        if(dataManager != null) {
            TableTrigger trigger = new TableTrigger(dataManager);
            trigger.init(conn, schemaName, triggerName, tableName, before, type);
            return trigger;
        } else {
            return null;
        }
    }

    private static class StateEventProcess extends SwingWorker {
        private final DataManager dataManager;
        private final Queue<StateEvent> eventStack;
        private final AtomicBoolean stateEventProcessing;
        private static final int TIME_MAX_THREAD_ALIVE = 5000;
        private static final int SLEEP_TIME = 500;

        private StateEventProcess(DataManager dataManager, Queue<StateEvent> eventStack, AtomicBoolean stateEventProcessing) {
            this.dataManager = dataManager;
            this.eventStack = eventStack;
            this.stateEventProcessing = stateEventProcessing;
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                long begin = System.currentTimeMillis();
                while(!eventStack.isEmpty() || System.currentTimeMillis() - begin < TIME_MAX_THREAD_ALIVE) {
                    while(!eventStack.isEmpty()) {
                        dataManager.fireDatabaseProgression(eventStack.remove());
                    }
                    try {
                        Thread.sleep(SLEEP_TIME);
                    } catch (InterruptedException ex) {
                        break;
                    }
                }
            } finally {
                stateEventProcessing.set(false);
            }
            return null;
        }
    }
}
