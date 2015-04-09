/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
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

package org.orbisgis.docking.impl.internals;

import bibliothek.gui.dock.common.DefaultMultipleCDockable;
import bibliothek.gui.dock.common.MultipleCDockableFactory;
import org.orbisgis.docking.impl.edition.dialogs.SaveDocuments;
import org.orbisgis.sif.docking.DockingPanel;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;

import javax.swing.SwingUtilities;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A custom cdockable that contains a reference to the dockingPanel instance.
 */
public class CustomMultipleCDockable extends DefaultMultipleCDockable implements CustomPanelHolder {
    private DockingPanel dockingPanel;
    private AtomicBoolean changeParameter = new AtomicBoolean(false);

    public CustomMultipleCDockable(DockingPanel dockingPanel, MultipleCDockableFactory<?, ?> factory) {
        super(factory,dockingPanel.getComponent());
        this.dockingPanel = dockingPanel;
    }
    /**
     * 
     * @return a reference to the dockingPanel instance
     */
    @Override
    public DockingPanel getDockingPanel() {
        return dockingPanel;
    }

    /**
     * In order to veto the visiblity change, this method has to be overridden.
     * @param visible New state
     */
    @Override
    public void setVisible(boolean visible) {
        if(!changeParameter.getAndSet(true)) {
            try {
                if(!visible && dockingPanel instanceof EditorDockable) {
                    EditableElement editableElement = ((EditorDockable) dockingPanel).getEditableElement();
                    if(editableElement != null && editableElement.isModified()) {
                        List<EditableElement> modifiedElements = Arrays.asList(editableElement);
                        SaveDocuments.CHOICE userChoice = SaveDocuments.showModal(SwingUtilities.getWindowAncestor(dockingPanel.getComponent()), modifiedElements);
                        // If the user do not want to save the editable elements
                        // Then cancel the modifications
                        if (userChoice == SaveDocuments.CHOICE.SAVE_NONE) {
                            for (EditableElement element : modifiedElements) {
                                element.setModified(false);
                            }
                        } else if(userChoice == SaveDocuments.CHOICE.CANCEL) {
                            return;
                        }
                    }
                }
                if(dockingPanel.getDockingParameters().setVisible(visible)) {
                    // If not vetoed, propagate visible change.
                    super.setVisible(visible);
                }
            } finally {
                changeParameter.set(false);
            }
        }
    }
}
