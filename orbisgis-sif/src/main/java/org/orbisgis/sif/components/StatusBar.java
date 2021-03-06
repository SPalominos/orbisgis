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
package org.orbisgis.sif.components;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * Root class for all status bar in OrbisGIS.
 */
public class StatusBar extends JPanel {
        protected JPanel rightToolbar;
        protected JPanel centerToolBar;
        protected JPanel leftToolbar;
        private final int horizontalEmptyBorder;

        public StatusBar(int outerBarBorder, int horizontalEmptyBorder) {
                super(new BorderLayout());
                this.horizontalEmptyBorder = horizontalEmptyBorder;
                // Right
                rightToolbar = new JPanel();
                rightToolbar.setLayout(
                        new BoxLayout(rightToolbar, BoxLayout.X_AXIS));
                // Left
                leftToolbar = new JPanel();
                leftToolbar.setLayout(
                        new BoxLayout(leftToolbar, BoxLayout.X_AXIS));
                // Center
                centerToolBar = new JPanel();
                centerToolBar.setLayout(new BoxLayout(centerToolBar, BoxLayout.X_AXIS));
                
                setBorder(
                        BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),
                        BorderFactory.createEmptyBorder(outerBarBorder,
                        outerBarBorder, outerBarBorder, outerBarBorder)));
                add(rightToolbar, BorderLayout.EAST);
                add(leftToolbar, BorderLayout.WEST);
                add(centerToolBar,BorderLayout.CENTER);
        }
               
        /**
         * Append a component on the right or the left the status bar
         * @param component 
         * @param position SwingConstants.LEFT, CENTER OR RIGHT
         */
        public void addComponent(JComponent component, int position) {
                addComponent(component,position,true);
        }

        /**
         * Append a component on the right or the left the status bar
         * @param component 
         * @param position SwingConstants.LEFT, CENTER OR RIGHT
         * @param addSeparator Add a separator at the left of the component
         */
        public void addComponent(JComponent component, int position,boolean addSeparator) {
                if (position == SwingConstants.LEFT) {
                        addComponent(leftToolbar,component, addSeparator);
                } else if (position == SwingConstants.RIGHT) {
                        addComponent(rightToolbar,component, addSeparator);
                } else if(position == SwingConstants.CENTER) {
                        addComponent(centerToolBar,component,addSeparator);
                }
        }     
        
        /**
         * Append a component on the right status bar
         * @param component 
         * @param addSeparator Add a separator at the left of the component
         */
        private void addComponent(JPanel panel, JComponent component,boolean addSeparator) {
                if(addSeparator && panel.getComponentCount()!=0) {
                        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
                        panel.add(Box.createHorizontalStrut(horizontalEmptyBorder));
                        panel.add(separator);
                        panel.add(Box.createHorizontalStrut(horizontalEmptyBorder));
                }
                panel.add(component);                
        }

        /**
         * Remove component from status bar.
         * @param component Component to remove.
         */
        public void removeComponent(JComponent component) {
                leftToolbar.remove(component);
                centerToolBar.remove(component);
                rightToolbar.remove(component);
        }
}
