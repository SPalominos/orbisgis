//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.03 at 12:56:18 PM CET 
//


package org.orbisgis.core.renderer.persistance.se;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TextGraphicType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TextGraphicType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/se}GraphicType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se}UnitOfMeasure" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Transform" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}StyledLabel" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TextGraphicType", propOrder = {
    "unitOfMeasure",
    "transform",
    "styledLabel"
})
public class TextGraphicType
    extends GraphicType
{

    @XmlElement(name = "UnitOfMeasure")
    @XmlSchemaType(name = "anyURI")
    protected String unitOfMeasure;
    @XmlElement(name = "Transform")
    protected TransformType transform;
    @XmlElement(name = "StyledLabel")
    protected StyledLabelType styledLabel;

    /**
     * Gets the value of the unitOfMeasure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    /**
     * Sets the value of the unitOfMeasure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitOfMeasure(String value) {
        this.unitOfMeasure = value;
    }

    /**
     * Gets the value of the transform property.
     * 
     * @return
     *     possible object is
     *     {@link TransformType }
     *     
     */
    public TransformType getTransform() {
        return transform;
    }

    /**
     * Sets the value of the transform property.
     * 
     * @param value
     *     allowed object is
     *     {@link TransformType }
     *     
     */
    public void setTransform(TransformType value) {
        this.transform = value;
    }

    /**
     * Gets the value of the styledLabel property.
     * 
     * @return
     *     possible object is
     *     {@link StyledLabelType }
     *     
     */
    public StyledLabelType getStyledLabel() {
        return styledLabel;
    }

    /**
     * Sets the value of the styledLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link StyledLabelType }
     *     
     */
    public void setStyledLabel(StyledLabelType value) {
        this.styledLabel = value;
    }

}
