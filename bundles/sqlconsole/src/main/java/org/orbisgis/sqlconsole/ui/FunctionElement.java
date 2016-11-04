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
import org.markdown4j.Markdown4jProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * A class to manage function name and type in the JList function
 * 
 * @author Erwan Bocher
 * @author Adam Gouge
 */
public class FunctionElement {
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionElement.class);
    private final String functionName;
    /**
     * @see DatabaseMetaData#procedureResultUnknown DatabaseMetaData#procedureNoResult DatabaseMetaData#procedureReturnsResult
     **/
    private final int functionType;
    private String description;
    private String toolTip;
    private String command;
    private DataSource dataSource;
    private Markdown4jProcessor mdProcessor = new Markdown4jProcessor();

    /**
     * @param functionName Function identifier
     * @param functionType Function type {@see DatabaseMetaData#getProcedures}
     * @param dataSource DataSource instance used to extract function attributes
     */
    FunctionElement(String functionName, int functionType, DataSource dataSource) {
        this.functionName = functionName;
        this.functionType = functionType;
        this.dataSource = dataSource;
    }

    /**     *
     * @param functionName Function identifier
     * @param functionType Function type {@see DatabaseMetaData#getProcedures}
     * @param description Function remarks
     * @param dataSource DataSource instance used to extract function attributes
     */
    public FunctionElement(String functionName, int functionType, String description, DataSource dataSource) {
        this.functionName = functionName;
        this.functionType = functionType;
        this.description = description;
        this.dataSource = dataSource;
    }

    @Override
    public String toString() {
        return functionName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public int getFunctionType() {
        return functionType;
    }

    /**
     * Get SQL function remarks (for use in the SQL console).
     * @return SQL function remarks
     */
    String getSQLRemarks() {
        if (description == null) {
            // Retrieve function ToolTip
            try(Connection connection = dataSource.getConnection()) {
                TableLocation functionLocation = TableLocation.parse(functionName);
                ResultSet functionData = connection.getMetaData().getProcedures(functionLocation.getCatalog(), functionLocation.getSchema(), functionLocation.getTable());
                if (functionData.next()) {
                    description = functionData.getString("REMARKS");
                }
                functionData.close();
            } catch (SQLException ex) {
                LOGGER.warn("Could not read function remarks");
            }
        }
        return description;
    }

    /**
     * Build HTML tooltip from SQL remarks.
     * @return HTML tooltip
     */
    String getToolTip() {
        if (toolTip == null) {
            final String sqlRemarks = getSQLRemarks();
            try {
                return mdProcessor.process(sqlRemarks);
            } catch (IOException e) {
                return sqlRemarks;
            }
        }
        return toolTip;
    }

    /**
     * @return SQL Command ex: UPPER( param1 VARCHAR )
     */
    String getSQLCommand() {
        if (command == null) {
            //Retrieve function ToolTip
            try (Connection connection = dataSource.getConnection()) {
                final DatabaseMetaData metaData = connection.getMetaData();
                try (ResultSet functionData = getFunctionData(metaData)) {
                    StringBuilder sb = new StringBuilder();
                    SortedMap<Integer, Signature> signatureMap;
                    if (JDBCUtilities.isH2DataBase(metaData)) {
                        signatureMap = getH2SignatureMap(functionData);
                    } else {
                        signatureMap = getPostGRESignatureMap(functionData);
                    }
                    buildString(sb, signatureMap);
                    command = sb.toString();
                }
            } catch (SQLException ex) {
                LOGGER.warn("Could not read function command");
            }
        }
        return command;
    }

    private ResultSet getFunctionData(DatabaseMetaData metaData) throws SQLException {
        TableLocation functionLocation = TableLocation.parse(functionName);
        return metaData.getProcedureColumns(
                functionLocation.getCatalog(null),
                functionLocation.getSchema(null),
                functionLocation.getTable(),
                null);
    }

    private SortedMap<Integer, Signature> getPostGRESignatureMap(ResultSet functionData) throws SQLException {
        Map<Integer, Signature> sigMap = new HashMap<>();
        int sigNumber = 0;
        while (functionData.next()) {
            final int position = functionData.getInt("ORDINAL_POSITION");
            final String columnName = functionData.getString("COLUMN_NAME");
            final String typeName = functionData.getString("TYPE_NAME");
            // PostGRE separates signatures by an ordinal position of 0
            // to indicate the return type.
            if (position == 0) {
                sigNumber++;
                // Lazy initialize
                if (sigMap.get(sigNumber) == null) {
                    sigMap.put(sigNumber, new Signature(typeName));
                }
            } // Any nonzero ordinal position represents an IN parameter.
            else {
                // Lazy initialize
                if (sigMap.get(sigNumber) == null) {
                    sigMap.put(sigNumber, new Signature());
                }
                sigMap.get(sigNumber).getInParams().put(position, columnName + ":" + typeName);
            }
        }
        final TreeMap<Integer, Signature> sortedMap = new TreeMap<>(new ValueComparator(sigMap));
        sortedMap.putAll(sigMap);
        return sortedMap;
    }

    private SortedMap<Integer, Signature> getH2SignatureMap(ResultSet functionData) throws SQLException {
        final int[] nAndM = getNumberOfSignaturesAndMaxParams();
        final int numberSignatures = nAndM[0];
        final int maxParams = nAndM[1];
        Map<Integer, Signature> sigMap = new HashMap<>();
        int sigNumber = 0;
        int oldPosition = 1;
        int prev = 1;
        while (functionData.next()) {
            final int position = functionData.getInt("ORDINAL_POSITION");
            final String typeName = functionData.getString("TYPE_NAME");
            if (position > oldPosition) {
                // Note: This test depends on signatures having parameter count
                // increasing by one. It will not work, for example, if there are
                // two signatures, one with length 2 and one with length 4.
                sigNumber = (position > (maxParams - numberSignatures + 1)) ? ++prev : 1;
            } else {
                sigNumber++;
            }
            oldPosition = position;
            if (!sigMap.containsKey(sigNumber)) {
                sigMap.put(sigNumber, new Signature());
            }
            sigMap.get(sigNumber).getInParams().put(position, typeName);
        }
        final TreeMap<Integer, Signature> sortedMap = new TreeMap<>(new ValueComparator(sigMap));
        sortedMap.putAll(sigMap);
        return sortedMap;
    }

    private int[] getNumberOfSignaturesAndMaxParams() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (ResultSet functionData = getFunctionData(connection.getMetaData())) {
                int oldPosition = 1;
                boolean foundNumberSignatures = false;
                int numberSignatures = -1;
                int maxParams = 0;
                int count = 0;
                while (functionData.next()) {
                    final int position = functionData.getInt("ORDINAL_POSITION");
                    final int columnType = functionData.getInt("COLUMN_TYPE");
                    if (columnType != DatabaseMetaData.procedureColumnReturn) {
                        if (position > maxParams) {
                            maxParams = position;
                        }
                        if (!foundNumberSignatures && position > oldPosition) {
                            numberSignatures = count;
                            foundNumberSignatures = true;
                        }
                        count++;
                        oldPosition = position;
                    }
                }
                return new int[]{numberSignatures, maxParams};
            }
        }
    }

    private void buildString(StringBuilder sb, SortedMap<Integer, Signature> signatureMap) {
        for (Signature s : signatureMap.values()) {
            if (!s.getInParams().isEmpty()) {
                if(s.getReturnType() != null) {
                    sb.append(s.getReturnType().toUpperCase().replace("RESULTSET", "TABLE"));
                    sb.append(" ");
                }
                sb.append(functionName).append("(");
                for (Map.Entry<Integer, String> entry : s.getInParams().entrySet()) {
                    if(entry.getKey() > 1) {
                        sb.append(", ");
                    }
                    if(entry.getKey() > 0) { // H2 ORDINAL_POSITION=0 for return type
                        sb.append(entry.getValue());
                    }
                }
                    sb.append(")\n");
            }
        }
        // Delete last newline character
        if(sb.length() > 1) {
            sb.delete(sb.length() - 1, sb.length());
        }
    }

    private class Signature {

        private SortedMap<Integer, String> inParams;
        private String returnType;

        private Signature() {
            this(null);
        }

        private Signature(String returnType) {
            inParams = new TreeMap<>();
            this.returnType = returnType;
        }

        public SortedMap<Integer, String> getInParams() {
            return inParams;
        }

        /**
         * @return The return type or null if none.
         */
        public String getReturnType() {
            return returnType;
        }
    }

    private class ValueComparator implements Comparator<Integer> {

        private Map<Integer, Signature> baseMap;

        private ValueComparator(Map<Integer, Signature> baseMap) {
            this.baseMap = baseMap;
        }

        @Override
        public int compare(Integer o1, Integer o2) {
            // We never return 0 so as to not merge keys.
            return baseMap.get(o1).getInParams().size() >= baseMap.get(o2).getInParams().size() ? 1 : -1;
        }
    }
}
