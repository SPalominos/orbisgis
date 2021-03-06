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

package org.orbisgis.editorjdbc.jobs;

import java.awt.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import org.h2gis.utilities.JDBCUtilities;
import org.h2gis.utilities.TableLocation;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.corejdbc.CreateTable;
import org.orbisgis.corejdbc.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.sql.DataSource;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * A background job to create a source from a selection.
 *
 * @author ebocher
 */
public class CreateSourceFromSelection extends SwingWorkerPM {

        private static final I18n I18N = I18nFactory.getI18n(CreateSourceFromSelection.class);
        
        private static final Logger GUILOGGER = LoggerFactory.getLogger("gui." + CreateSourceFromSelection.class);
      
        private final DataSource dataSource;
        private final String tableName;
        private final Set<Long> selectedRows;
        private String newName;

        /**
         * Constructor used by the Map Editor.
         *
         * @param dataSource     Original DataSource
         * @param tableName The table identifier [[catalog.]schema.]table
         * @param selectedRows Selected Rows
         */
        public CreateSourceFromSelection(DataSource dataSource,
                Set<Long> selectedRows, String tableName) {
                this.dataSource = dataSource;
                this.selectedRows = selectedRows;
                this.tableName = tableName;
                setTaskName(I18N.tr("Create a datasource from the current selection"));
        }

        /**
         * Constructor used by the TableEditor.
         *
         * @param dataSource     Original DataSource
         * @param selectedRows Value of primary key to copy
         * @param newName      New name to use to register the DataSource
         */
        public CreateSourceFromSelection(DataSource dataSource,
                                         Set<Long> selectedRows, String tableName,
                                         String newName) {
            this(dataSource, selectedRows, tableName);
            this.newName = newName;
        }

        @Override
        protected Object doInBackground() throws Exception {
            try {
                CreateTable.createTableFromRowPkSelection(dataSource, tableName, selectedRows, newName, this.getProgressMonitor());
            } catch (SQLException e) {
                GUILOGGER.error("The selection cannot be created.", e);
                if(newName!=null && !newName.isEmpty()) {
                    try(Connection connection = dataSource.getConnection();
                        Statement st = connection.createStatement()) {
                        boolean isH2 = JDBCUtilities.isH2DataBase(connection.getMetaData());
                        st.execute("DROP TABLE IF EXISTS "+TableLocation.parse(newName, isH2).toString(isH2));
                    } catch (SQLException ex) {
                        GUILOGGER.error("Could not revert changes", e);
                    }
                }
            }
            return null;
        }

        /**
         * Show an input dialog that ask for destination table.
         * @param parent Parent component to attach Dialog
         * @param dataSource JDBC dataSource
         * @param sourceTable Base table name
         * @return Chosen table name
         * @throws SQLException If the table name check failed.
         */
        public static String showNewNameDialog(Component parent,
                                               DataSource dataSource,String sourceTable) throws SQLException {
            String newName = null;
            boolean inputAccepted = false;
            final String newNameMessage = I18n.marktr("New name for the datasource:");
            JLabel message = new JLabel(I18N.tr(newNameMessage));
            try(Connection connection = dataSource.getConnection()) {
                DatabaseMetaData meta = connection.getMetaData();
                boolean isH2 = JDBCUtilities.isH2DataBase(meta);
                while (!inputAccepted) {
                    newName = JOptionPane.showInputDialog(
                            parent,
                            message.getText(),
                            MetaData.getNewUniqueName(sourceTable, meta,
                                    TableLocation.capsIdentifier(I18N.tr("selection"), isH2)));
                    // Check if the user canceled the operation.
                    if (newName == null) {
                        // Just exit
                        inputAccepted = true;
                    } // The user clicked OK.
                    else {
                        // Check for an empty name.
                        if (newName.isEmpty()) {
                            message.setText(I18N.tr("You must enter a non-empty name.")
                                    + "\n" + I18N.tr(newNameMessage));
                        } // Check for a source that already exists with that name.
                        else if (JDBCUtilities.tableExists(connection, newName)) {
                            message.setText(I18N.tr("A datasource with that name already exists.")
                                    + "\n" + I18N.tr(newNameMessage));
                        } // The user entered a non-empty, unique name.
                        else {
                            inputAccepted = true;
                        }
                    }
                }
            }
            return newName;
        }
}
