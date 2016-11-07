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
package org.orbisgis.sqlconsole.ui;

import org.h2gis.utilities.JDBCUtilities;
import org.h2gis.utilities.TableLocation;
import org.orbisgis.editorjdbc.TransferableSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.TransferHandler;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * If a source is dropped into the console panel, it transfer table reference into a SQL Request.
 * @author Nicolas Fortin
 */
public class ScriptPanelTransferHandler extends TransferHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptPanelTransferHandler.class);
    private JTextArea textArea;
    private DataSource dataSource;

    public ScriptPanelTransferHandler(JTextArea textArea, DataSource dataSource) {
        this.textArea = textArea;
        this.dataSource = dataSource;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        for(DataFlavor dataFlavor : transferFlavors) {
            if(dataFlavor.isFlavorTextType() || dataFlavor.equals(TransferableSource.sourceFlavor)) {
                return true;
            }
        }
        return super.canImport(comp, transferFlavors);
    }

    @Override
    public boolean importData(JComponent comp, Transferable t) {
        if(t.isDataFlavorSupported(TransferableSource.sourceFlavor)) {
            try(Connection connection = dataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                boolean isH2 = JDBCUtilities.isH2DataBase(metaData);
                String[] sources = (String[]) t.getTransferData(TransferableSource.sourceFlavor);
                for(String source : sources) {
                    TableLocation table = TableLocation.parse(source);
                    // Fetch field names
                    try(ResultSet rs = metaData.getColumns(table.getCatalog(), table.getSchema(), table.getTable(), null)) {
                        Map<Integer, String> columns = new HashMap<>();
                        while(rs.next()) {
                            String fieldName = rs.getString("COLUMN_NAME");
                            columns.put(rs.getInt("ORDINAL_POSITION"), fieldName);
                        }
                        StringBuilder fields = new StringBuilder();
                        for(int colId : new TreeSet<>(columns.keySet())) {
                            String fieldName = columns.remove(colId);
                            fields.append(TableLocation.quoteIdentifier(fieldName, isH2));
                            if(!columns.isEmpty()) {
                                fields.append(", ");
                            }
                        }
                        textArea.append(String.format("SELECT %s FROM %s;\n",fields.toString(),table.toString(isH2)));
                    }
                }
                return true;
            } catch (Exception e) {
                LOGGER.error(e.getLocalizedMessage(), e);
                return false;
            }

        } else {
            return super.importData(comp, t);
        }
    }
}
