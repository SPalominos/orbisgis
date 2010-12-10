//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.03 at 12:56:18 PM CET 
//


package org.orbisgis.core.renderer.persistance.se;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PieChartType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PieChartType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/se}GraphicType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se}UnitOfMeasure" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Transform" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Radius" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}HoleRadius" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Stroke" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}Slice" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.opengis.net/se}PieSubtype"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PieChartType", propOrder = {
    "unitOfMeasure",
    "transform",
    "radius",
    "holeRadius",
    "stroke",
    "slice",
    "pieSubtype"
})
public class PieChartType
    extends GraphicType
{

    @XmlElement(name = "UnitOfMeasure")
    @XmlSchemaType(name = "anyURI")
    protected String unitOfMeasure;
    @XmlElement(name = "Transform")
    protected TransformType transform;
    @XmlElement(name = "Radius")
    protected ParameterValueType radius;
    @XmlElement(name = "HoleRadius")
    protected ParameterValueType holeRadius;
    @XmlElementRef(name = "Stroke", namespace = "http://www.opengis.net/se", type = JAXBElement.class)
    protected JAXBElement<? extends StrokeType> stroke;
    @XmlElement(name = "Slice", required = true)
    protected List<SliceType> slice;
    @XmlElement(name = "PieSubtype", required = true)
    protected PieSubtypeType pieSubtype;

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
     * Gets the value of the radius property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getRadius() {
        return radius;
    }

    /**
     * Sets the value of the radius property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setRadius(ParameterValueType value) {
        this.radius = value;
    }

    /**
     * Gets the value of the holeRadius property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getHoleRadius() {
        return holeRadius;
    }

    /**
     * Sets the value of the holeRadius property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setHoleRadius(ParameterValueType value) {
        this.holeRadius = value;
    }

    /**
     * Gets the value of the stroke property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link GraphicStrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextStrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PenStrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CompoundStrokeType }{@code >}
     *     
     */
    public JAXBElement<? extends StrokeType> getStroke() {
        return stroke;
    }

    /**
     * Sets the value of the stroke property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link GraphicStrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link StrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextStrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PenStrokeType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CompoundStrokeType }{@code >}
     *     
     */
    public void setStroke(JAXBElement<? extends StrokeType> value) {
        this.stroke = ((JAXBElement<? extends StrokeType> ) value);
    }

    /**
     * Gets the value of the slice property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the slice property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSlice().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SliceType }
     * 
     * 
     */
    public List<SliceType> getSlice() {
        if (slice == null) {
            slice = new ArrayList<SliceType>();
        }
        return this.slice;
    }

    /**
     * Gets the value of the pieSubtype property.
     * 
     * @return
     *     possible object is
     *     {@link PieSubtypeType }
     *     
     */
    public PieSubtypeType getPieSubtype() {
        return pieSubtype;
    }

    /**
     * Sets the value of the pieSubtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link PieSubtypeType }
     *     
     */
    public void setPieSubtype(PieSubtypeType value) {
        this.pieSubtype = value;
    }

}
