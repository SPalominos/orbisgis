package org.gdms.driver;

import org.gdms.data.DataSourceFactory;
import org.gdms.data.metadata.Metadata;
import org.gdms.data.types.TypeDefinition;
import org.gdms.driver.driverManager.Driver;

public interface ReadOnlyDriver extends Driver, ReadAccess {

	/**
	 * Method to pass references to the driver
	 *
	 * @param dsf
	 */
	public void setDataSourceFactory(DataSourceFactory dsf);

	/**
	 * Gets the driver specific metadata
	 *
	 * @return
	 * @throws DriverException
	 */
	public Metadata getMetadata() throws DriverException;

	/**
	 * @return
	 * @throws DriverException
	 */
	public TypeDefinition[] getTypesDefinitions() throws DriverException;
}