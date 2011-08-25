/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information. OrbisGIS is
 * distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.cnrs.fr/> CNRS FR 2488.
 *
 *
 * Team leader : Erwan BOCHER, scientific researcher,
 *
 * User support leader : Gwendall Petit, geomatic engineer.
 *
 * Previous computer developer : Pierre-Yves FADET, computer engineer, Thomas LEDUC, 
 * scientific researcher, Fernando GONZALEZ CORTES, computer engineer.
 *
 * Copyright (C) 2007 Erwan BOCHER, Fernando GONZALEZ CORTES, Thomas LEDUC
 *
 * Copyright (C) 2010 Erwan BOCHER, Alexis GUEGANNO, Maxence LAURENT, Antoine GOURLAY
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
 *
 * or contact directly:
 * info@orbisgis.org
 */
package org.gdms.data.types;

import org.gdms.data.values.Value;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Constraint indicating the type of the geometry: point, multilinestring, ...
 * 
 */
public class GeometryTypeConstraint extends AbstractIntConstraint {
	private static final String MULTI_POLYGON_TEXT = "MultiPolygon";

	private static final String POLYGON_TEXT = "Polygon";

	private static final String MULTI_LINESTRING_TEXT = "MultiLinestring";

	private static final String LINESTRING_TEXT = "Linestring";

	private static final String MULTI_POINT_TEXT = "MultiPoint";

	private static final String POINT_TEXT = "Point";

	private static final String GEOMETRYCOLLECTION_TEXT = "GeometryCollection";

	public static final int POINT = 10;

	public static final int MULTI_POINT = 12;

	public static final int LINESTRING = 14;

	public static final int MULTI_LINESTRING = 16;

	public static final int POLYGON = 18;

	public static final int MULTI_POLYGON = 20;

	public static final int GEOMETRY_COLLECTION = 22;

	public GeometryTypeConstraint(final int constraintValue) {
		super(constraintValue);
	}

	GeometryTypeConstraint(byte[] constraintBytes) {
		super(constraintBytes);
	}

        @Override
	public int getConstraintCode() {
		return Constraint.GEOMETRY_TYPE;
	}

        @Override
	public String check(Value value) {
		if (!value.isNull()) {
			final Geometry geom = value.getAsGeometry();
			final int st = getGeometryType(geom);
			if (!geom.isEmpty() && (st != constraintValue)) {
				return "The type of the geometry must be "
						+ getConstraintHumanValue() + ": "
						+ new WKTWriter(3).write(geom);
			}
		}
		return null;
	}

	/**
	 * 
	 * @return One of the following constant in geometry constraint: MIXED,
	 *         POINT,MULTI_POINT, LINESTRING,
	 *         MULTI_LINESTRING,POLYGON,MULTI_POLYGON
	 */

	public int getGeometryType() {
		return constraintValue;
	}

	/**
	 * Get the type of the geometry as a constraint. Returns -1 if the geometry
	 * type doesn't fit any of the possible constraints
	 * 
	 * @param geometry
	 * @return
	 */
	private static int getGeometryType(final Geometry geometry) {
		int type;

		if (geometry instanceof Point) {
			type = POINT;
		} else if (geometry instanceof MultiPoint) {
			type = MULTI_POINT;
		} else if (geometry instanceof Polygon) {
			type = POLYGON;
		} else if (geometry instanceof MultiPolygon) {
			type = MULTI_POLYGON;
		} else if (geometry instanceof LineString) {
			type = LINESTRING;
		} else if (geometry instanceof MultiLineString) {
			type = MULTI_LINESTRING;
		} else if (geometry instanceof GeometryCollection) {
			type = GEOMETRY_COLLECTION;
		} else {
			type = -1;
		}

		return type;
	}

	@Override
	public String[] getChoiceStrings() {
		return new String[] { POINT_TEXT, MULTI_POINT_TEXT, LINESTRING_TEXT,
				MULTI_LINESTRING_TEXT, POLYGON_TEXT, MULTI_POLYGON_TEXT,
				GEOMETRYCOLLECTION_TEXT };
	}

	@Override
	public int[] getChoiceCodes() {
		return new int[] { POINT, MULTI_POINT, LINESTRING, MULTI_LINESTRING,
				POLYGON, MULTI_POLYGON, GEOMETRY_COLLECTION };
	}

	@Override
	public int getType() {
		return CONSTRAINT_TYPE_CHOICE;
	}

	@Override
	public String getConstraintHumanValue() {
		switch (constraintValue) {
		case POINT:
			return POINT_TEXT;
		case MULTI_POINT:
			return MULTI_POINT_TEXT;
		case LINESTRING:
			return LINESTRING_TEXT;
		case MULTI_LINESTRING:
			return MULTI_LINESTRING_TEXT;
		case POLYGON:
			return POLYGON_TEXT;
		case MULTI_POLYGON:
			return MULTI_POLYGON_TEXT;
		case GEOMETRY_COLLECTION:
			return GEOMETRYCOLLECTION_TEXT;
		default:
			throw new IllegalStateException("bug!");
		}
	}

}