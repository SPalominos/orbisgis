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
 * Copyright (C) 2015-2016 CNRS (Lab-STICC UMR CNRS 6285)
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

import org.h2gis.utilities.JDBCUtilities;
import org.h2gis.utilities.TableLocation;
import org.orbisgis.commons.progress.ProgressMonitor;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.sql.DataSource;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

/**
 * Methods that need write rights on database
 * @author Nicolas Fortin
 */
public class CreateTable {
    private static final int INSERT_BATCH_SIZE = 30;
    protected final static I18n I18N = I18nFactory.getI18n(CreateTable.class, Locale.getDefault(), I18nFactory.FALLBACK);

    /**
     * Create a temporary table that contains the provided collection of integers.
     * @param connection JDBC connection
     * @param pm Progress monitor
     * @param selectedRows Integer to add in temp table,elements must be unique as it will be added a primary key
     * @return The temporary table name with a column named ROWID
     * @throws java.sql.SQLException
     */
    public static String createIndexTempTable(Connection connection, ProgressMonitor pm, Collection<Long> selectedRows,String columnName,int insertBatchSize) throws SQLException {
        ProgressMonitor insertProgress = pm.startTask(selectedRows.size());
        // Populate the new source
        try(Statement st = connection.createStatement()) {
            // Create row id table
            String tempTableName = "CREATE_SOURCE";
            tempTableName = MetaData.getNewUniqueName(tempTableName, connection.getMetaData(), "");
            st.execute(String.format("CREATE LOCAL TEMPORARY TABLE %s("+columnName+" bigint primary key)", tempTableName));
            // Prepare insert statement
            PreparedStatement insertSt = connection.prepareStatement(String.format("INSERT INTO %s VALUES(?)", tempTableName));
            // Cancel insert
            PropertyChangeListener listener = EventHandler.create(PropertyChangeListener.class, insertSt, "cancel");
            insertProgress.addPropertyChangeListener(ProgressMonitor.PROP_CANCEL,
                    listener);
            try {
                int batchSize = 0;
                for (long sel : selectedRows){
                    insertSt.setLong(1, sel);
                    insertSt.addBatch();
                    batchSize++;
                    insertProgress.endTask();
                    if(batchSize >= insertBatchSize) {
                        batchSize = 0;
                        insertSt.executeBatch();
                    }
                    if(insertProgress.isCancelled()) {
                        break;
                    }
                }
                if(batchSize > 0) {
                    insertSt.executeBatch();
                }
            } finally {
                insertProgress.removePropertyChangeListener(listener);
            }
            return tempTableName;
        }
    }


    public static void createTableFromRowPkSelection(DataSource dataSource, String tableName, Set<Long> selectedRows,
                                                     String newName, ProgressMonitor pm) throws SQLException {
        // Populate the new source
        try(Connection connection = dataSource.getConnection();
            Statement st = connection.createStatement()) {
            DatabaseMetaData meta = connection.getMetaData();
            // Find an unique name to register
            if (newName == null) {
                newName = MetaData.getNewUniqueName(tableName,meta,"selection");
            }
            // Create row id table
            String tempTableName = CreateTable.createIndexTempTable(connection, pm, selectedRows,"ROWID", INSERT_BATCH_SIZE);
            PropertyChangeListener listener = EventHandler.create(PropertyChangeListener.class, st, "cancel");
            pm.addPropertyChangeListener(ProgressMonitor.PROP_CANCEL,
                    listener);
            // Copy content using pk
            String primaryKeyName = MetaData.getPkName(connection, tableName, true);
            StringBuilder pkEquality = new StringBuilder("a.%s = ");
            if (!primaryKeyName.equals(MetaData.POSTGRE_ROW_IDENTIFIER)) {
                pkEquality.append("b.ROWID");
            } else {
                pkEquality.append(MetaData.castLongToTid("b.ROWID"));
            }
            st.execute(String.format("CREATE TABLE %s AS SELECT a.* FROM %s a,%s b " +
                            "WHERE "+pkEquality, TableLocation.parse(newName),
                    TableLocation.parse(tableName),tempTableName, primaryKeyName));
            pm.removePropertyChangeListener(listener);
        }
    }
}
