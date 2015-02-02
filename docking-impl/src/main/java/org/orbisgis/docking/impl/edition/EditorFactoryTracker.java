/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
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
package org.orbisgis.docking.impl.edition;

import org.orbisgis.sif.edition.EditorFactory;
import org.orbisgis.sif.edition.EditorManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;

/**
 * Show {@link EditorFactory} registered services.
 * When a bundle register the Service {@link EditorFactory}, this tracker catch the event
 * and register the provided {@link EditorFactory} instance to the {@link EditorManagerImpl}.
 * @author Nicolas Fortin
 */
public class EditorFactoryTracker extends ServiceTracker<EditorFactory, EditorFactory> {
    private static enum DP_EVT { ADDED, MODIFIED, REMOVED}
    private BundleContext hostContext;
    private EditorManager editorManager;
    private static final Logger LOGGER = LoggerFactory.getLogger(EditorFactoryTracker.class);

    /**
     * Constructor.
     * @param context Bundle context
     * @param editorManager Docking manager
     */
    public EditorFactoryTracker(BundleContext context, EditorManager editorManager) {
        super(context, EditorFactory.class, null);
        hostContext = context;
        this.editorManager = editorManager;
    }
    @Override
    public EditorFactory addingService(ServiceReference<EditorFactory> reference) {
        EditorFactory editorFactory = hostContext.getService(reference);
        processOperation(new DockingPanelOperation(editorManager,editorFactory, DP_EVT.ADDED));
        return editorFactory;
    }
    @Override
    public void modifiedService(ServiceReference<EditorFactory> reference, EditorFactory editorFactory) {

    }
    @Override
    public void removedService(ServiceReference<EditorFactory> reference, EditorFactory editorFactory) {
        processOperation(new DockingPanelOperation(editorManager,editorFactory, DP_EVT.REMOVED));
    }
    private void processOperation(DockingPanelOperation operation) {
        SwingUtilities.invokeLater(operation);
    }

    /**
     * Guaranty that the Add/Remove panel operation is done on swing thread.
     */
    private static class DockingPanelOperation implements Runnable {
        private EditorManager editorManager;
        private EditorFactory editorFactory;
        private DP_EVT operation;

        private DockingPanelOperation(EditorManager editorManager, EditorFactory editorFactory, DP_EVT operation) {
            this.editorManager = editorManager;
            this.editorFactory = editorFactory;
            this.operation = operation;
        }

        @Override
        public void run() {
            switch(operation) {
                case REMOVED:
                    editorManager.removeEditorFactory(editorFactory);
                    break;
                case ADDED:
                    editorManager.addEditorFactory(editorFactory);
                    break;
            }
        }
    }
}