/**
 * The GDMS library (Generic Datasource Management System)
 * is a middleware dedicated to the management of various kinds of
 * data-sources such as spatial vectorial data or alphanumeric. Based
 * on the JTS library and conform to the OGC simple feature access
 * specifications, it provides a complete and robust API to manipulate
 * in a SQL way remote DBMS (PostgreSQL, H2...) or flat files (.shp,
 * .csv...).
 *
 * Gdms is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV FR CNRS 2488
 *
 * This file is part of Gdms.
 *
 * Gdms is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Gdms is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Gdms. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 *
 * or contact directly:
 * info@orbisgis.org
 */
package org.gdms.geometryUtils;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequence;
import com.vividsolutions.jts.io.WKTReader;
import org.junit.Test;

import static org.junit.Assert.*;

import org.gdms.geometryUtils.filter.CoordinateSequenceDimensionFilter;

/**
 *
 * @author Erwan Bocher
 */
public class GeometryTypeUtilTest {

        public WKTReader wKTReader = new WKTReader();

        @Test
        public void testDimensionSequence() throws Exception {
                Geometry geom = wKTReader.read("POINT(0 0)");
                CoordinateSequenceDimensionFilter cd = new CoordinateSequenceDimensionFilter();
                geom.apply(cd);
                assertTrue(cd.getDimension() == 2);
                geom = wKTReader.read("LINESTRING(0 0, 0 0 1)");
                cd = new CoordinateSequenceDimensionFilter();
                geom.apply(cd);
                assertTrue(cd.getDimension() == 3);
        }

       
        /**
         * Test several geometries
         * @throws Exception
         */
        @Test
        public void testGeometryTypes() throws Exception {
                Geometry geom = wKTReader.read("POINT(0 0)");
                assertTrue(GeometryTypeUtil.isPoint(geom));
                geom = wKTReader.read("MULTIPOINT((0 0), (1 1))");
                assertTrue(GeometryTypeUtil.isMultiPoint(geom));
                geom = wKTReader.read("LINESTRING(0 0, 1 1)");
                assertTrue(GeometryTypeUtil.isLineString(geom));
                geom = wKTReader.read("MULTILINESTRING((0 0, 1 1),(0 0, 1 1))");
                assertTrue(GeometryTypeUtil.isMultiLineString(geom));
                geom = wKTReader.read("LINEARRING(0 0, 1 1, 2 2 , 0 0)");
                assertTrue(GeometryTypeUtil.isLinearRing(geom));
                geom = wKTReader.read("POLYGON (( 131 166, 131 266, 210 266, 210 166, 131 166 ))");
                assertTrue(GeometryTypeUtil.isPolygon(geom));
                geom = wKTReader.read("MULTIPOLYGON ((( 131 166, 131 266, 210 266, 210 166, 131 166 )), (( 267 144, 267 239, 362 239, 362 144, 267 144 )))");
                assertTrue(GeometryTypeUtil.isMultiPolygon(geom));

        }

        /**
         * Test if a geometry has a z value or not
         * @throws Exception
         */
        @Test
        public void testGeometryHasZ() throws Exception {
                Geometry geom = wKTReader.read("POINT(0 0)");
                assertFalse(GeometryTypeUtil.is25Geometry(geom));
                geom = wKTReader.read("MULTIPOINT((0 0) , (1 1 10))");
                assertTrue(GeometryTypeUtil.is25Geometry(geom));
                geom = wKTReader.read("LINESTRING(0 0 12, 1 1 12)");
                assertTrue(GeometryTypeUtil.is25Geometry(geom));
                geom = wKTReader.read("MULTILINESTRING((0 0, 1 1),(0 0, 1 1 1))");
                assertTrue(GeometryTypeUtil.is25Geometry(geom));
        }
        
        @Test
        public void regressionIssue1() throws Exception {
                PackedCoordinateSequence.Double seq = new PackedCoordinateSequence.Double(new double[]{1, 4}, 2);
                Point p = new Point(seq, new GeometryFactory());

                assertFalse(GeometryTypeUtil.is25Geometry(p));
        }
}
