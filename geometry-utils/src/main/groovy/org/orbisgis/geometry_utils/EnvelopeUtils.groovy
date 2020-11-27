/*
 * Bundle Geomerty_Utils is part of the OrbisGIS platform
 *
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
 * OSM is distributed under LGPL 3 license.
 *
 * Copyright (C) 2020 CNRS (Lab-STICC UMR CNRS 6285)
 *
 *
 * OSM is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OSM is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * OSM. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.geometry_utils

import groovy.transform.Field
import org.cts.util.UTMUtils
import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Utility script used as extension module adding methods to Envelope class.
 *
 * @author Erwan Bocher (CNRS)
 * @author Sylvain PALOMINOS (UBS chaire GEOTERA 2020)
 */

private static final @Field GeometryFactory FACTORY = new GeometryFactory()

/**
 * Main AsType method allowing to convert Envelope into an other class.
 *
 * Supported classes : Polygon, Envelope
 * @param env Envelope to convert.
 * @param aClass Destination conversion class.
 * @return Instance of the given class from the Envelope.
 */
static Object asType(Envelope env, Class aClass) {
    switch(aClass) {
        case Polygon :
            return FACTORY.toGeometry(env)
        case Envelope :
            return env
    }
    return null
}

/**
 * Convert a coordinate array to a Bbox Envelope.
 *
 * @param collection Collection of values.
 * @param isLatLon   Indicates if the coordinates are latitude/longitude or not. False by default.
 * @return Envelope build from coordinates.
 */
static def toBbox(Collection<Number> collection, boolean isLatLon = false) {
    if (!collection) {
        error "The collection values cannot be null or empty"
        return null
    }
    if (!(collection instanceof Collection) && !collection.class.isArray()) {
        error "The collection values must be set as an array"
        return null
    }
    if(collection.size() != 4) {
        error("The collection must be defined with 4 values")
        return null
    }
    def minLong = collection[0], minLat = collection[1]
    def maxLong = collection[2], maxLat = collection[3]
    if (!isLatLon || (
            UTMUtils.isValidLatitude(minLat) && UTMUtils.isValidLatitude(maxLat)
            && UTMUtils.isValidLongitude(minLong) && UTMUtils.isValidLongitude(maxLong))) {
        return new Envelope(minLong, maxLong, minLat, maxLat)
    }
}