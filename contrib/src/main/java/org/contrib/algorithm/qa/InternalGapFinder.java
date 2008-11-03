package org.contrib.algorithm.qa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.gdms.data.SpatialDataSourceDecorator;
import org.gdms.data.values.Value;
import org.gdms.data.values.ValueFactory;
import org.gdms.driver.DriverException;
import org.gdms.driver.memory.ObjectMemoryDriver;
import org.orbisgis.progress.IProgressMonitor;
import org.orbisgis.progress.ProgressMonitor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import com.vividsolutions.jts.operation.union.CascadedPolygonUnion;

public class InternalGapFinder {

	private SpatialDataSourceDecorator sds;

	private IProgressMonitor pm;

	private ObjectMemoryDriver driver;

	public InternalGapFinder(SpatialDataSourceDecorator sds) {
		this.sds = sds;
		findGaps();
	}

	public void findGaps() {

		try {

			IProgressMonitor pm = new ProgressMonitor("Gaps finders");
			pm.startTask("Read data");
			sds.open();

			long rowCount = sds.getRowCount();

			SpatialIndex spatialIndex = new STRtree(10);

			Collection geometries = new ArrayList<Geometry>();
			for (int i = 0; i < rowCount; i++) {
				Geometry geom = sds.getGeometry(i);

				pm.progressTo(i);
				if (geom instanceof GeometryCollection) {
					final int nbOfGeometries = geom.getNumGeometries();
					for (int j = 0; j < nbOfGeometries; j++) {
						Geometry simpleGeom = geom.getGeometryN(j);
						if (geom.getDimension() == 2) {
							geometries.add(simpleGeom);
						}

					}
				} else {

					if (geom.getDimension() == 2) {
						geometries.add(geom);

					}
				}

			}

			pm.endTask();

			pm.startTask("Gap processing");

			Geometry cascadedPolygonUnion = CascadedPolygonUnion
					.union(geometries);

			driver = new ObjectMemoryDriver(sds.getMetadata());
			final int spatialFieldIndex = sds.getSpatialFieldIndex();
			final Value[] fieldsValues = sds.getRow(spatialFieldIndex);
			sds.close();

			GeometryFactory gf = new GeometryFactory();

			int nbOfGeometries = cascadedPolygonUnion.getNumGeometries();
			for (int i = 0; i < nbOfGeometries; i++) {
				pm.progressTo(i);
				Geometry simpleGeom = cascadedPolygonUnion.getGeometryN(i);
				spatialIndex.insert(simpleGeom.getEnvelopeInternal(),
						simpleGeom);
			}

			pm.endTask();

			pm.startTask("Result saving");

			for (int i = 0; i < nbOfGeometries; i++) {
				Geometry simpleGeom = cascadedPolygonUnion.getGeometryN(i);
				Polygon poly = (Polygon) simpleGeom;
				pm.progressTo(i);
				if (poly.getNumInteriorRing() > 0) {
					for (int j = 0; j < poly.getNumInteriorRing(); j++) {
						Polygon result = gf.createPolygon(gf
								.createLinearRing(poly.getInteriorRingN(j)
										.getCoordinates()), null);

						List query = spatialIndex.query(result
								.getEnvelopeInternal());
						Geometry geomDiff = result;

						for (Iterator k = query.iterator(); k.hasNext();) {
							Geometry queryGeom = (Geometry) k.next();

							if (result.contains(queryGeom)) {
								geomDiff = result.difference(queryGeom);

							}
						}

						fieldsValues[spatialFieldIndex] = ValueFactory
								.createValue(geomDiff);
						driver.addValues(fieldsValues);

					}
				}

			}
			pm.endTask();
			spatialIndex = null;

		} catch (DriverException e) {
			e.printStackTrace();
		}

	}

	public ObjectMemoryDriver getObjectMemoryDriver() {
		return driver;
	}

}
