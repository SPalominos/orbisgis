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
 *    Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
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
package org.gdms.data.object;

import org.gdms.data.AbstractDataSourceDefinition;
import org.gdms.data.DataSource;
import org.gdms.data.DataSourceCreationException;
import org.gdms.data.DataSourceDefinition;
import org.gdms.driver.DriverException;
import org.gdms.driver.ObjectDriver;
import org.gdms.driver.ObjectReadWriteDriver;
import org.gdms.driver.ReadOnlyDriver;
import org.gdms.source.directory.DefinitionType;
import org.gdms.source.directory.ObjectDefinitionType;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ObjectSourceDefinition extends AbstractDataSourceDefinition {

	public ObjectDriver driver;

	public ObjectSourceDefinition(ObjectDriver driver) {
		this.driver = driver;
		setDriver(driver);
	}

	public DataSource createDataSource(String tableName)
			throws DataSourceCreationException {
		ObjectDataSourceAdapter ds;
		ds = new ObjectDataSourceAdapter(getSource(tableName), tableName,
				driver);
		return ds;
	}

	public void createDataSource(DataSource contents) throws DriverException {
		contents.open();
		try {
			((ObjectReadWriteDriver) driver).write(contents);
		} catch (DriverException e) {
			contents.cancel();
			throw e;
		}
		contents.cancel();
	}

	public DefinitionType getDefinition() {
		ObjectDefinitionType ret = new ObjectDefinitionType();
		ret.setClazz(driver.getClass().getCanonicalName());

		return ret;
	}

	public static DataSourceDefinition createFromXML(ObjectDefinitionType d)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String className = d.getClazz();
		ObjectDriver od = (ObjectDriver) Class.forName(className).newInstance();

		return new ObjectSourceDefinition(od);
	}

	@Override
	protected ReadOnlyDriver getDriverInstance() {
		return driver;
	}

	public ObjectDriver getObject() {
		return driver;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ObjectSourceDefinition) {
			ObjectSourceDefinition dsd = (ObjectSourceDefinition) obj;
			if (driver.equals(dsd.driver)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
