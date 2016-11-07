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
package org.orbisgis.coremap.process;

import com.vividsolutions.jts.geom.Envelope;
import org.slf4j.*;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.corejdbc.ReadTable;
import org.orbisgis.coremap.layerModel.MapContext;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;
import java.sql.SQLException;
import java.util.SortedSet;

/**
 * Fetch selection extent and apply it to the map context.
 * @author Nicolas Fortin
 */
public class ZoomToSelectedFeatures extends SwingWorkerPM {
        private static final Logger LOGGER = LoggerFactory.getLogger(ZoomToSelectedFeatures.class);
        protected final static I18n I18N = I18nFactory.getI18n(ZoomToSelectedFeatures.class);
        private DataManager dataManager;
        private String tableName;
        private SortedSet<Long> modelSelection;
        private MapContext mapContext;

        /**
         * Constructor.
         * @param dataManager data manager
         * @param tableName Table location
         * @param modelSelection Selected rows
         * @param mapContext Loaded map context
         */
        public ZoomToSelectedFeatures(DataManager dataManager, String tableName, SortedSet<Long> modelSelection, MapContext mapContext) {
            this.dataManager = dataManager;
            this.tableName = tableName;
            this.modelSelection = modelSelection;
            this.mapContext = mapContext;
            setTaskName(I18N.tr("Zoom to selection"));
        }

        @Override
        protected Object doInBackground() throws SQLException {
            try {
                Envelope selectionEnvelope = ReadTable.getTableSelectionEnvelope(dataManager, tableName, modelSelection, this.getProgressMonitor());
                if(selectionEnvelope!=null) {
                    mapContext.setBoundingBox(selectionEnvelope);
                }
            }catch (SQLException ex) {
                LOGGER.error(I18N.tr("Unable to establish the selection bounding box"),ex);
            }
            return null;
        }
}
