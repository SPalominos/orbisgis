//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.03 at 12:56:18 PM CET 
//


package org.orbisgis.core.renderer.persistance.se;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RelativeOrientationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="RelativeOrientationType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="normal"/>
 *     &lt;enumeration value="line"/>
 *     &lt;enumeration value="portrayal"/>
 *     &lt;enumeration value="normalUp"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "RelativeOrientationType")
@XmlEnum
public enum RelativeOrientationType {

    @XmlEnumValue("normal")
    NORMAL("normal"),
    @XmlEnumValue("line")
    LINE("line"),
    @XmlEnumValue("portrayal")
    PORTRAYAL("portrayal"),
    @XmlEnumValue("normalUp")
    NORMAL_UP("normalUp");
    private final String value;

    RelativeOrientationType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RelativeOrientationType fromValue(String v) {
        for (RelativeOrientationType c: RelativeOrientationType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
