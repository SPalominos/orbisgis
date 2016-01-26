/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE 
 * team located in University of South Brittany, Vannes.
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
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
package org.orbisgis.sif.components.actions.intern;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created by action commands to remove actions before disposing the control.
 * @author Nicolas Fortin
 */
public class RemoveActionControls implements PropertyChangeListener {
        // If set this action property will remove all component associated
        public final static String DELETED_PROPERTY = "DeletedAction";
        private Container container;
        private Component menuItem;
        /**
         * When DELETED_PROPERTY is put on action, remove menuItem from container.
         * @param container
         * @param menuItem 
         */
        public RemoveActionControls(Container container, Component menuItem) {
                this.container = container;
                this.menuItem = menuItem;
        }
        
        @Override
        public void propertyChange(PropertyChangeEvent pce) {
                if(pce.getPropertyName().equals(DELETED_PROPERTY)) {
                        container.remove(menuItem);
                        container.validate();
                }
        }

        /**
         * The linked container
         * @return Container instance
         */
        public Container getContainer() {
                return container;
        }
}
