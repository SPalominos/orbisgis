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

package org.orbisgis.geocatalogtree.impl.jobs;

import java.awt.Component;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.conf.RenderNameStyle;
import org.jooq.conf.Settings;
import org.jooq.impl.DSL;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.dbjobs.api.DatabaseView;
import org.orbisgis.geocatalogtree.impl.SQLMessageDialog;
import org.orbisgis.geocatalogtree.impl.nodes.TableAndField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Drop the selected columns on the same table
 * @author Erwan Bocher
 */
public class DropColumn extends SwingWorkerPM{
    
    private static final I18n I18N = I18nFactory.getI18n(DropColumn.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(DropColumn.class);
    private List<TableAndField> indexIdentifier;
    private DatabaseView dbView;
    private DataSource dataSource;

    public DropColumn(List<TableAndField> indexIdentifier, DatabaseView dbView, DataSource dataSource) {
        super(indexIdentifier.size());
        this.indexIdentifier = indexIdentifier;
        this.dbView = dbView;
        this.dataSource = dataSource;
    }

     @Override
    protected Object doInBackground() throws Exception {
        try(Connection connection = dataSource.getConnection();
            Statement st = connection.createStatement()) {
            String query = getSQLDropColumn(connection, indexIdentifier);
            LOGGER.info(I18N.tr("Execute drop index command:\n{0}", query));
            st.execute(query);
        } catch (SQLException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }
    
    /**
     * Return the SQL representation of drop column
     * @param connection
     * @param indexIdentifier
     * @return
     * @throws SQLException 
     */
    private static String getSQLDropColumn(Connection connection, List<TableAndField> indexIdentifier) throws SQLException {
        StringBuilder query = new StringBuilder();
        DSLContext dslContext = DSL.using(connection, new Settings().withRenderNameStyle(RenderNameStyle.AS_IS));
        for (TableAndField tableAndField : indexIdentifier) {
            if( query.length()>0 ) {
                query.append("\n");
            }
            query.append(dslContext.alterTable(tableAndField.getTableName()).drop(tableAndField.getFieldName()).getSQL());
            query.append(";");
        }
        return query.toString();
    }
    
    public static DropColumn onMenuDropColumn(DataSource dataSource, List<TableAndField> indexIdentifier, Component parentComponent, DatabaseView dbView) throws SQLException {
        if(indexIdentifier.isEmpty()) {
            return null;
        }
        String query;
        try(Connection connection = dataSource.getConnection()) {
           query = getSQLDropColumn(connection, indexIdentifier);
        }
        String message = I18N.tr("Are you sure to drop the selected column ?");
        SQLMessageDialog.CHOICE option = SQLMessageDialog.showModal(null,I18N.tr("Drop column"), message, query);
        
        if(option==SQLMessageDialog.CHOICE.OK){
            return new DropColumn(indexIdentifier, dbView, dataSource);
        }
        else{
            return null;
        }
    }
    
    @Override
    protected void done() {
        HashSet<String> tables = new HashSet<>();
        for (TableAndField tableAndField : indexIdentifier) {
            tables.add(tableAndField.getTableName());            
        }       
        dbView.onDatabaseUpdate(DatabaseView.DB_ENTITY.INDEX.toString(),
                tables.toArray(new String[tables.size()]));
    }

    @Override
    public String toString() {
        return I18N.tr("Dropping column");
    }
}
