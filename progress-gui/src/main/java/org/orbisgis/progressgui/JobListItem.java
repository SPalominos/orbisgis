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
package org.orbisgis.progressgui;

import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.sif.common.ContainerItem;
import org.orbisgis.sif.common.ContainerItemProperties;

import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import javax.swing.SwingWorker;

/**
 * This list item is linked with a Job
 * @warning This component has a Timer running when the function listenToJob is 
 * called, always call the method dispose() when this instance is no longer used
 */

public class JobListItem extends ContainerItem<SwingWorker> {
        private static final long serialVersionUID = 1L;
        private SwingWorker job;
        private PropertyChangeListener listener =
                EventHandler.create(PropertyChangeListener.class,
                                    this,
                                    "updateJob");
        private JobListItemPanel itemPanel;

        public JobListItemPanel getItemPanel() {
                return itemPanel;
        }
                
        public JobListItem(SwingWorker job) {
                super(job, job.toString());
                this.job = job;
        }

        public JobListItem(SwingWorkerPM job) {
            super(job, job.getProgressMonitor().getCurrentTaskName());
            this.job = job;
        }

        /**
         * Update the list item on job changes and make the panel
         * @param simplifiedPanel Job displayed on a single line
         * @return 
         */
        public JobListItem listenToJob(boolean simplifiedPanel) {
            job.getPropertyChangeSupport().addPropertyChangeListener("progress", listener);
            itemPanel = new JobListItemPanel(job, simplifiedPanel);
            updateJob();
            return this;
        }
        /**
         * Stop listening to the job and the timer
         */
        public void dispose() {
                job.removePropertyChangeListener(listener);
        }
        
        /**
         * The user click on the cancel button
         */
        public void onCancel() {
            job.cancel(false);
        }
        
        /**
         * Read the job to update labels and controls
         */
        public void updateJob() {
                if(itemPanel!=null) {
                        itemPanel.readJob();
                        setLabel(itemPanel.getText());
                }   
        }

        /**
         * 
         * @return The associated Job
         */
        public SwingWorker getJob() {
                return job;
        }
}
