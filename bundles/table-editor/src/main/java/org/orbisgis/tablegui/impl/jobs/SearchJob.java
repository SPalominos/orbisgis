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
package org.orbisgis.tablegui.impl.jobs;

import java.awt.Rectangle;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.corejdbc.common.IntegerUnion;
import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.tableeditorapi.TableEditableElement;
import org.orbisgis.tablegui.impl.filters.TableSelectionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * @author Nicolas Fortin
 */
public class SearchJob extends SwingWorkerPM {
    protected final static I18n I18N = I18nFactory.getI18n(SearchJob.class);
    private TableSelectionFilter activeFilter;
    private JTable table;
    private TableEditableElement source;
    private AtomicBoolean filterRunning;
    private static final Logger LOGGER = LoggerFactory.getLogger("gui." + SearchJob.class);

    public SearchJob(TableSelectionFilter activeFilter, JTable table, TableEditableElement source,
                     AtomicBoolean filterRunning) {
        this.activeFilter = activeFilter;
        this.table = table;
        this.source = source;
        this.filterRunning = filterRunning;
        setTaskName(I18N.tr("Selecting rows"));
    }

    private void runFilter() {
        final ProgressMonitor pm = getProgressMonitor().startTask(3);
        //Launch filter initialisation
        try {
            activeFilter.initialize(pm, source);
        } catch (SQLException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
            return;
        }
        pm.progressTo(1);  // If filter does not handle progress monitor
        //Iterate on rows
        final IntegerUnion nextViewSelection = new IntegerUnion();
        int rowCount = table.getRowCount();
        ProgressMonitor viewUpdate = pm.startTask(I18N.tr("Read filter"), rowCount);
        for (int viewId = 0; viewId < rowCount; viewId++) {
            if (activeFilter.isSelected(table.getRowSorter().convertRowIndexToModel(viewId), source)) {
                nextViewSelection.add(viewId);
                viewUpdate.endTask();
                if (pm.isCancelled()) {
                    return;
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                // Update the table values
                List<Integer> ranges = nextViewSelection.getValueRanges();
                Iterator<Integer> intervals = ranges.iterator();
                try {
                    table.getSelectionModel().setValueIsAdjusting(true);
                    table.clearSelection();
                    ProgressMonitor swingPm = pm.startTask("Apply filter", ranges.size());
                    while (intervals.hasNext()) {
                        int begin = intervals.next();
                        int end = intervals.next();
                        table.addRowSelectionInterval(begin, end);
                        swingPm.endTask();
                        if (swingPm.isCancelled()) {
                            return;
                        }
                    }
                } finally {
                    table.getSelectionModel().setValueIsAdjusting(false);
                }
                if (!nextViewSelection.isEmpty()) {
                    scrollToRow(nextViewSelection.first(), table);
                }
            }
        });
    }

    @Override
    protected Object doInBackground() throws Exception {
        try {
            runFilter();
        } finally {
            filterRunning.set(false);
        }
        return null;
    }

    public static void scrollToRow(int modelRowId, JTable table) {
        Rectangle firstSelectedRow = table.getCellRect(modelRowId, 0, true);
        table.scrollRectToVisible(firstSelectedRow);
    }
}
