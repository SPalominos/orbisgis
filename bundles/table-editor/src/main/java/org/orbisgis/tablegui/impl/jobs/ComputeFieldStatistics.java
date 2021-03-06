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
package org.orbisgis.tablegui.impl.jobs;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import java.util.Set;
import java.util.SortedSet;

import org.h2gis.utilities.JDBCUtilities;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.corejdbc.common.IntegerUnion;
import org.orbisgis.corejdbc.ReadTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * This job compute a numeric column statistics.
 * @author Nicolas Fortin
 */
public class ComputeFieldStatistics extends SwingWorkerPM {
    protected final static I18n I18N = I18nFactory.getI18n(ComputeFieldStatistics.class);
    private static final Logger LOGGER = LoggerFactory.getLogger("gui." + ComputeFieldStatistics.class);
    private Set<Integer> statisticsRowFilter;
    private DataSource ds;
    private String columnName;
    private String table;

    /**
     * Constructor
     *
     * @param statisticsRowFilter Row id filter (not primary key)
     * @param dataSource          JDBC Datasource
     * @param columnName          Column name
     * @param tableName           Table identifier
     */
    public ComputeFieldStatistics(Set<Integer> statisticsRowFilter, DataSource dataSource, String columnName,
                                  String tableName) {
        this.statisticsRowFilter = statisticsRowFilter;
        this.ds = dataSource;
        this.columnName = columnName;
        table = tableName;
        setTaskName(I18N.tr("Compute column statistics."));
    }

    @Override
    protected Object doInBackground() throws Exception {
        try {
            boolean doRowFiltering = !statisticsRowFilter.isEmpty();
            String[] stats;
            if (doRowFiltering) {
                SortedSet<Integer> sortedSet;
                if (statisticsRowFilter instanceof SortedSet) {
                    sortedSet = (SortedSet<Integer>) statisticsRowFilter;
                } else {
                    sortedSet = new IntegerUnion(statisticsRowFilter);
                }
                try (Connection connection = ds.getConnection()) {
                    stats = ReadTable.computeStatsLocal(connection, table, columnName, sortedSet, this.getProgressMonitor());
                }
            } else {
                try (Connection connection = ds.getConnection()) {
                    stats = ReadTable.computeStatsSQL(connection, table, columnName, this.getProgressMonitor());
                }
            }
            // Show table statistics
            StringBuilder message = new StringBuilder();
            message.append(I18N.tr("\nTable {0}, statistics of the column {1}.\n", table, columnName));
            message.append(I18N.tr("Row count : {0}\n", stats[ReadTable.STATS.COUNT.ordinal()]));
            message.append(I18N.tr("Minimum : {0}\n", stats[ReadTable.STATS.MIN.ordinal()]));
            message.append(I18N.tr("Maximum : {0}\n", stats[ReadTable.STATS.MAX.ordinal()]));
            message.append(I18N.tr("Sum : {0}\n", stats[ReadTable.STATS.SUM.ordinal()]));
            message.append(I18N.tr("Average : {0}\n", stats[ReadTable.STATS.AVG.ordinal()]));
            message.append(I18N.tr("Standard deviation : {0}\n", stats[ReadTable.STATS.STDDEV_SAMP.ordinal()]));
            LOGGER.info(message.toString());
        } catch (SQLException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }
}
