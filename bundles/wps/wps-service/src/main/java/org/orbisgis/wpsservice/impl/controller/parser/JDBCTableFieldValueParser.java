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

package org.orbisgis.wpsservice.impl.controller.parser;

import net.opengis.ows._2.CodeType;
import net.opengis.wps._2_0.Format;
import net.opengis.wps._2_0.InputDescriptionType;
import net.opengis.wps._2_0.OutputDescriptionType;
import org.orbisgis.wpsgroovyapi.attributes.DescriptionTypeAttribute;
import org.orbisgis.wpsgroovyapi.attributes.JDBCTableFieldValueAttribute;
import org.orbisgis.wpsgroovyapi.attributes.InputAttribute;
import org.orbisgis.wpsservice.api.controller.parser.Parser;
import org.orbisgis.wpsservice.impl.controller.utils.FormatFactory;
import org.orbisgis.wpsservice.impl.controller.utils.ObjectAnnotationConverter;
import org.orbisgis.wpsservice.impl.model.JDBCTableFieldValue;
import org.orbisgis.wpsservice.impl.model.MalformedScriptException;
import org.orbisgis.wpsservice.impl.model.ObjectFactory;

import javax.xml.bind.JAXBElement;
import java.lang.reflect.Field;
import java.net.URI;

/**
 * Parser for the JDBCTableFieldValue input/output annotations.
 *
 * @author Sylvain PALOMINOS
 **/

public class JDBCTableFieldValueParser implements Parser {

    @Override
    public InputDescriptionType parseInput(Field f, Object defaultValue, URI processId) throws MalformedScriptException {
        //Instantiate the JDBCTableFieldValue object
        JDBCTableFieldValueAttribute JDBCTableFieldValueAttribute = f.getAnnotation(JDBCTableFieldValueAttribute.class);
        Format format = FormatFactory.getFormatFromExtension(FormatFactory.TEXT_EXTENSION);
        URI jdbcTableFieldUri;
        //If the jdbcTable attribute is not an URI, autoGenerate one.
        if(!JDBCTableFieldValueAttribute.jdbcTableFieldReference().contains(":")) {
            jdbcTableFieldUri = URI.create(processId + ":input:" + JDBCTableFieldValueAttribute.jdbcTableFieldReference());
        }
        //else, use it
        else {
            jdbcTableFieldUri = URI.create(JDBCTableFieldValueAttribute.jdbcTableFieldReference());
        }
        JDBCTableFieldValue jdbcTableFieldValue = ObjectAnnotationConverter.annotationToObject(JDBCTableFieldValueAttribute, format, jdbcTableFieldUri);
        if(defaultValue != null && defaultValue instanceof String[]) {
            jdbcTableFieldValue.setDefaultValues((String[])defaultValue);
        }

        //Instantiate the returned input
        InputDescriptionType input = new InputDescriptionType();
        JAXBElement<JDBCTableFieldValue> jaxbElement = new ObjectFactory().createJDBCTableFieldValue(jdbcTableFieldValue);
        input.setDataDescription(jaxbElement);

        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(InputAttribute.class), input);
        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(DescriptionTypeAttribute.class), input);

        if(input.getIdentifier() == null){
            CodeType codeType = new CodeType();
            codeType.setValue(processId+":input:"+f.getName());
            input.setIdentifier(codeType);
        }

        return input;
    }

    @Override
    public OutputDescriptionType parseOutput(Field f, URI processId) throws MalformedScriptException {
        //Instantiate the JDBCTableFieldValue object
        JDBCTableFieldValueAttribute JDBCTableFieldValueAttribute = f.getAnnotation(JDBCTableFieldValueAttribute.class);
        Format format = FormatFactory.getFormatFromExtension(FormatFactory.TEXT_EXTENSION);
        URI jdbcTableFieldUri;
        //If the jdbcTable attribute is not an URI, autoGenerate one.
        if(!JDBCTableFieldValueAttribute.jdbcTableFieldReference().contains(":")) {
            jdbcTableFieldUri = URI.create(processId + ":input:" + JDBCTableFieldValueAttribute.jdbcTableFieldReference());
        }
        //else, use it
        else {
            jdbcTableFieldUri = URI.create(JDBCTableFieldValueAttribute.jdbcTableFieldReference());
        }
        JDBCTableFieldValue JDBCTableFieldValue = ObjectAnnotationConverter.annotationToObject(JDBCTableFieldValueAttribute, format, jdbcTableFieldUri);

        //Instantiate the returned output
        OutputDescriptionType output = new OutputDescriptionType();
        JAXBElement<JDBCTableFieldValue> jaxbElement = new ObjectFactory().createJDBCTableFieldValue(JDBCTableFieldValue);
        output.setDataDescription(jaxbElement);

        ObjectAnnotationConverter.annotationToObject(f.getAnnotation(DescriptionTypeAttribute.class), output);

        if(output.getIdentifier() == null){
            CodeType codeType = new CodeType();
            codeType.setValue(processId+":output:"+f.getName());
            output.setIdentifier(codeType);
        }

        return output;
    }

    @Override
    public Class getAnnotation() {
        return JDBCTableFieldValueAttribute.class;
    }
}
