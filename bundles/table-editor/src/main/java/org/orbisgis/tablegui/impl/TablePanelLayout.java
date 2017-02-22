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
package org.orbisgis.tablegui.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.corejdbc.common.LongUnion;
import org.orbisgis.sif.docking.DockingPanelLayout;
import org.orbisgis.sif.docking.XElement;
import org.orbisgis.tableeditorapi.TableEditableElement;
import org.orbisgis.tableeditorapi.TableEditableElementImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * When the application close and start this layout retrieve/save
 * the state of this window.
 */

public class TablePanelLayout implements DockingPanelLayout {
        private TableEditableElement tableEditableElement;
        private static final Logger LOGGER = LoggerFactory.getLogger(TablePanelLayout.class);
        private static final I18n I18N = I18nFactory.getI18n(TablePanelLayout.class);
        private static final int MAX_SELECTION_SERIALISATION_SIZE = 100;
        
        //Fields name (xml)
        private static final String PROP_DATA_SOURCE_NAME = "datasource";
        private static final String PROP_SELECTION = "selection";

        private DataManager dataManager;

        public TablePanelLayout(DataManager dataManager) {
                this.tableEditableElement = null;
                this.dataManager = dataManager;
        }

        
        
        public TablePanelLayout(TableEditableElement tableEditableElement) {
                this.tableEditableElement = tableEditableElement;
                this.dataManager = tableEditableElement.getDataManager();
        }

        public TableEditableElement getTableEditableElement() {
                return tableEditableElement;
        }
        
        @Override
        public void writeStream(DataOutputStream out) throws IOException {
                //DataSource
                out.writeUTF(tableEditableElement.getTableReference());
                //Selection
                writeSelection(out);
        }

        private void writeSelection(OutputStream out) throws IOException {
                ObjectOutputStream selectionOut = new ObjectOutputStream(out);
                //Do not save byte consuming selection
                LongUnion mergedSelection;
                Set<Long> selection = tableEditableElement.getSelection();
                if(selection instanceof LongUnion) {
                        mergedSelection = (LongUnion) selection;
                } else {
                        mergedSelection = new LongUnion(selection);
                }
                if(mergedSelection.getValueRanges().size()>MAX_SELECTION_SERIALISATION_SIZE) {
                        selectionOut.writeObject(new LongUnion());
                } else {
                        selectionOut.writeObject(mergedSelection);
                }
                selectionOut.flush();
                selectionOut.close();                
        }
        
        private LongUnion readSelection(InputStream in) {
                try {
                        ObjectInputStream selectionIn = new ObjectInputStream(in);
                        return (LongUnion)selectionIn.readObject();
                } catch (ClassNotFoundException | IOException ex) {
                        LOGGER.error(I18N.tr("Selection deserialisation failed"),ex);
                }
            return new LongUnion();
        }
        @Override
        public void readStream(DataInputStream in) throws IOException {
                //DataSource
                String dataSourceName = in.readUTF();
                tableEditableElement = new TableEditableElementImpl(
                readSelection(in),dataSourceName, dataManager);
        }
        
        @Override
        public void writeXML(XElement element) {
                element.addString(PROP_DATA_SOURCE_NAME,tableEditableElement.getTableReference());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                try {
                        writeSelection(bytes);
                } catch (IOException ex) {
                        LOGGER.error(I18N.tr("Selection serialisation failed"),ex);
                }                
                element.addByteArray(PROP_SELECTION, bytes.toByteArray());
        }

        @Override
        public void readXML(XElement element) {
                ByteArrayInputStream in = new ByteArrayInputStream(element.getByteArray(PROP_SELECTION));
                tableEditableElement = new TableEditableElementImpl(readSelection(in),
                                element.getString(PROP_DATA_SOURCE_NAME), dataManager);

        }
}
