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
package org.orbisgis.corejdbc;

import javax.sql.DataSource;
import javax.sql.rowset.RowSetFactory;
import java.net.URI;
import java.sql.SQLException;

/**
 * DataManager has been created in order to minimize the usage of JDBC transaction when the ResultSet is
 * frequently read or updated. It can also manage a local modification history, in order to listen modifications.
 */
public interface DataManager extends RowSetFactory {
    /**
     * Free DataManager instance resources
     */
    void dispose();

    /**
     * This method use the URI in order to upload or link a data source.
     * It can take time if the data has to be uploaded.
     * @param uri Source path
     * @return Table reference (can include schema and/or database)
     * @throws SQLException Error while transaction with JDBC
     */
    String registerDataSource(URI uri) throws SQLException;

    /**
     * @param tableReference Table reference [[catalog.]schema.]table
     * @return True if this table exists
     */
    boolean isTableExists(String tableReference) throws SQLException;

    /**
     * @return DataSource of this DataManager
     */
    DataSource getDataSource();

    /**
     * Same as {@link javax.sql.rowset.RowSetFactory#createJdbcRowSet()}
     * @return A RowSet that manage {@link TableEditListener}
     * @throws SQLException
     */
    public ReversibleRowSet createReversibleRowSet() throws SQLException;

    /**
     * @return A read only RowSet
     * @throws SQLException
     */
    public ReadRowSet createReadRowSet() throws SQLException;

    /**
     * @param table Table identifier [[catalog.]schema.]table
     * @return True if a table is currently monitored by this DataManager
     */
    boolean hasTableEditListener(String table);

    /**
     * Table update done through ReversibleRowSet will be fire through theses listeners
     * @param table Table identifier [[catalog.]schema.]table
     * @param listener Listener instance
     * @param addTrigger True, add a trigger on the Database if possible
     */
    void addTableEditListener(String table, TableEditListener listener, boolean addTrigger);

    /**
     * Table update done through ReversibleRowSet will be fire through theses listeners
     * @param table Table identifier [[catalog.]schema.]table
     * @param listener Listener instance
     */
    void addTableEditListener(String table, TableEditListener listener);

    /**
     * Remove registered listener
     * @param table Table identifier [[catalog.]schema.]table
     * @param listener Listener instance to remove
     */
    void removeTableEditListener(String table, TableEditListener listener);

    /**
     * Remove all listeners
     */
    void clearTableEditListener();

    /**
     * @param e Event to fire
     */
    void fireTableEditHappened(TableEditEvent e);

    /**
     * @param originalTableName Table name if not exists
     * @return Table name concatenated with "_1" if originalTableName already exists
     * @throws SQLException Exception while querying database
     */
    public String findUniqueTableName(String originalTableName) throws SQLException;

    /**
     * Fire event to DatabaseProgressionListener
     * @param event Database event
     */
    void fireDatabaseProgression(StateEvent event);

    /**
     * Add a listener in order to retrieve long process progression
     * @param listener Listener instance
     * @param state DB event identifier
     */
    void addDatabaseProgressionListener(DatabaseProgressionListener listener, StateEvent.DB_STATES state);

    /**
     * Remove the listener.
     * @param listener Listener instance
     */
    void removeDatabaseProgressionListener(DatabaseProgressionListener listener);
}
