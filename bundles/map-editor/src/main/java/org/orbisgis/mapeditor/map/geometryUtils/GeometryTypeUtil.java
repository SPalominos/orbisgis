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
package org.orbisgis.mapeditor.map.geometryUtils;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.orbisgis.mapeditor.map.geometryUtils.filter.CoordinateSequenceDimensionFilter;


/**
 * This utility class provides methods to test the actual type of a JTS {@link Geometry} object.
 * 
 * @author Erwan Bocher
 */
public final class GeometryTypeUtil {

    private static final GeometryFactory FACTORY = new GeometryFactory();
    public static final String POINT_GEOMETRY_TYPE;
    public static final String MULTIPOINT_GEOMETRY_TYPE;
    public static final String LINESTRING_GEOMETRY_TYPE;
    public static final String LINEARRING_GEOMETRY_TYPE;
    public static final String MULTILINESTRING_GEOMETRY_TYPE;
    public static final String POLYGON_GEOMETRY_TYPE;
    public static final String MULTIPOLYGON_GEOMETRY_TYPE;
    public static final String GEOMETRYCOLLECTION_GEOMETRY_TYPE;

    static {
        POINT_GEOMETRY_TYPE = FACTORY.createPoint(new Coordinate(0, 0)).getGeometryType();
        MULTIPOINT_GEOMETRY_TYPE = FACTORY.createMultiPoint(
                new Coordinate[]{new Coordinate(0, 0)}).getGeometryType();
        LineString ls = FACTORY.createLineString(new Coordinate[]{
                    new Coordinate(0, 0), new Coordinate(1, 0)});
        LINESTRING_GEOMETRY_TYPE = ls.getGeometryType();
        MULTILINESTRING_GEOMETRY_TYPE = FACTORY.createMultiLineString(
                new LineString[]{ls}).getGeometryType();
        LinearRing lr = FACTORY.createLinearRing(new Coordinate[]{
                    new Coordinate(0, 0), new Coordinate(1, 1),
                    new Coordinate(1, 0), new Coordinate(0, 0)});
        LINEARRING_GEOMETRY_TYPE = lr.getGeometryType();
        Polygon pol = FACTORY.createPolygon(lr, new LinearRing[0]);
        POLYGON_GEOMETRY_TYPE = pol.getGeometryType();
        MULTIPOLYGON_GEOMETRY_TYPE = FACTORY.createMultiPolygon(
                new Polygon[]{pol}).getGeometryType();
        GEOMETRYCOLLECTION_GEOMETRY_TYPE = FACTORY.createGeometryCollection(new Geometry[]{pol, ls}).getGeometryType();
    }

    /**
     * Tests if the geometry is a Point.
     * @param geometry
     * @return
     */
    public static boolean isPoint(Geometry geometry) {
        return geometry.getGeometryType().equals(POINT_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a LineString.
     * @param geometry
     * @return
     */
    public static boolean isLineString(Geometry geometry) {
        return geometry.getGeometryType().equals(LINESTRING_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a LinearRing.
     * @param geometry
     * @return
     */
    public static boolean isLinearRing(Geometry geometry) {
        return geometry.getGeometryType().equals(LINEARRING_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a Polygon.
     * @param geometry
     * @return
     */
    public static boolean isPolygon(Geometry geometry) {
        return geometry.getGeometryType().equals(POLYGON_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a MultiPoint.
     * @param geometry
     * @return
     */
    public static boolean isMultiPoint(Geometry geometry) {
        return geometry.getGeometryType().equals(MULTIPOINT_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a MultiPoint.
     * @param geometry
     * @return
     */
    public static boolean isMultiLineString(Geometry geometry) {
        return geometry.getGeometryType().equals(MULTILINESTRING_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a MultiPolygon.
     * @param geometry
     * @return
     */
    public static boolean isMultiPolygon(Geometry geometry) {
        return geometry.getGeometryType().equals(MULTIPOLYGON_GEOMETRY_TYPE);
    }

    /**
     * Tests if the geometry is a GeometryCollection.
     * @param geometry
     * @return
     */
    public static boolean isGeometryCollection(Geometry geometry) {
        return geometry.getGeometryType().equals(GEOMETRYCOLLECTION_GEOMETRY_TYPE);
    }

    /**
     * Returns true is a geometry contains at least one z value.
     * @param geom Instance of geometry
     * @return
     */
    public static boolean is25Geometry(Geometry geom) {
        CoordinateSequenceDimensionFilter cf = new CoordinateSequenceDimensionFilter();
        cf.setMAXDim(CoordinateSequenceDimensionFilter.XYZ);
        geom.apply(cf);
        return cf.getDimension() >= CoordinateSequenceDimensionFilter.XYZ;
    }

   

    /**
     * Returns the coordinate dimension
     * 2 for XY
     * 3 for XYZ
     * 4 for XYZM
     *
     * XYM is not allowed.
     * @param geom
     * @return
     */
    public static int getCoordinateDimension(Geometry geom) {
        CoordinateSequenceDimensionFilter cf = new CoordinateSequenceDimensionFilter();
        geom.apply(cf);
        return cf.getDimension();
    }

    /**
     * Returns true if the geometry type is a surface
     * @param geom 
     * @return  
     */
    public static boolean isSurface(Geometry geom) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Private constructor for utility class.
     */
    private GeometryTypeUtil() {
    }
}
