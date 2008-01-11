/*
 * The GDMS library (Generic Datasources Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...). GDMS is produced  by the geomatic team of the IRSTV
 * Institute <http://www.irstv.cnrs.fr/>, CNRS FR 2488:
 *    Erwan BOCHER, scientific researcher,
 *    Thomas LEDUC, scientific researcher,
 *    Fernando GONZALES CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALES CORTES, Thomas LEDUC
 *
 * This file is part of GDMS.
 *
 * GDMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GDMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GDMS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult:
 *    <http://orbisgis.cerma.archi.fr/>
 *    <http://sourcesup.cru.fr/projects/orbisgis/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-developers/>
 *    <http://listes.cru.fr/sympa/info/orbisgis-users/>
 *
 * or contact directly:
 *    erwan.bocher _at_ ec-nantes.fr
 *    fergonco _at_ gmail.com
 *    thomas.leduc _at_ cerma.archi.fr
 */
package org.gdms.source;

import java.io.File;

import junit.framework.TestCase;

import org.gdms.DBTestSource;
import org.gdms.FileTestSource;
import org.gdms.SQLTestSource;
import org.gdms.SourceTest;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceFactory;
import org.gdms.data.db.DBSource;

public class ChecksumTest extends TestCase {

	private DataSourceFactory dsf;
	private SourceManager sm;

	public void testModifyingSourceOutsideFactory() throws Exception {
		File testFile = new File(SourceTest.internalData + "test.csv");
		String name = "file";
		FileTestSource fts = new FileTestSource(name, testFile
				.getAbsolutePath());
		fts.backup();
		sm.register(name, fts.getBackupFile());
		testModifyingSourceOutsideFactory(name, false);

		name = "db";
		DBSource testDB = new DBSource(null, 0, SourceTest.internalData
				+ "backup/testhsqldb", "sa", "", "gisapps", "jdbc:hsqldb:file");
		DBTestSource dbTestSource = new DBTestSource(name,
				"org.hsqldb.jdbcDriver", SourceTest.internalData
						+ "testhsqldb.sql", testDB);
		dbTestSource.backup();
		sm.register(name, testDB);
		testModifyingSourceOutsideFactory(name, false);

		name = "sql";
		String sql = "select count(id) from file;";
		SQLTestSource sts = new SQLTestSource(name, sql);
		sts.backup();
		sm.register(name, sql);
		testModifyingSourceOutsideFactory(name, true);

	}

	private synchronized void testModifyingSourceOutsideFactory(String name,
			boolean outdatedValue) throws Exception {
		assertTrue(sm.getSource(name).isUpToDate() == null);
		sm.saveStatus();
		assertTrue(sm.getSource(name).isUpToDate().booleanValue() == true);

		DataSource ds = SourceTest.dsf.getDataSource(name);
		ds.open();
		ds.deleteRow(0);
		if (outdatedValue) {
			ds.cancel();
		} else {
			// To change modification time
			wait(2000);
			ds.commit();
		}

		instantiateDSF();
		assertTrue(sm.getSource(name).isUpToDate().booleanValue() == outdatedValue);
	}

	public void testUpdateOnSave() throws Exception {
		File testFile = new File(SourceTest.internalData + "test.csv");
		String name = "file";
		FileTestSource fts = new FileTestSource(name, testFile
				.getAbsolutePath());
		fts.backup();
		sm.register(name, fts.getBackupFile());
		sm.saveStatus();

		modificationWithOtherFactory(fts.getBackupFile());

		instantiateDSF();
		assertTrue(sm.getSource(name).isUpToDate() == false);
		sm.saveStatus();
		instantiateDSF();
		assertTrue(sm.getSource(name).isUpToDate() == true);
	}

	private synchronized void modificationWithOtherFactory(File file)
			throws Exception {
		// Modification with another factory
		DataSource ds = SourceTest.dsf.getDataSource(file);
		ds.open();
		ds.deleteRow(0);
		wait(2000);
		ds.commit();
	}

	@Override
	protected void setUp() throws Exception {
		SourceTest.dsf.getSourceManager().removeAll();
		instantiateDSF();
		sm.removeAll();
	}

	private void instantiateDSF() {
		dsf = new DataSourceFactory(SourceTest.internalData
				+ "source-management");
		sm = dsf.getSourceManager();
	}
}
