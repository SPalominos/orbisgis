//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.12.03 at 12:56:18 PM CET 
//


package org.orbisgis.core.renderer.persistance.se;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GraphicStrokeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GraphicStrokeType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/se}StrokeType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/se}Graphic"/>
 *         &lt;element ref="{http://www.opengis.net/se}Length" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/se}RelativeOrientation" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GraphicStrokeType", propOrder = {
    "graphic",
    "length",
    "relativeOrientation"
})
public class GraphicStrokeType
    extends StrokeType
{

    @XmlElementRef(name = "Graphic", namespace = "http://www.opengis.net/se", type = JAXBElement.class)
    protected JAXBElement<? extends GraphicType> graphic;
    @XmlElement(name = "Length")
    protected ParameterValueType length;
    @XmlElement(name = "RelativeOrientation")
    protected RelativeOrientationType relativeOrientation;

    /**
     * Gets the value of the graphic property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link MarkGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CompositeGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PieChartType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AlternativeGraphicsType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExternalGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AxisChartType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GraphicType }{@code >}
     *     
     */
    public JAXBElement<? extends GraphicType> getGraphic() {
        return graphic;
    }

    /**
     * Sets the value of the graphic property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link MarkGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link CompositeGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link PieChartType }{@code >}
     *     {@link JAXBElement }{@code <}{@link TextGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AlternativeGraphicsType }{@code >}
     *     {@link JAXBElement }{@code <}{@link ExternalGraphicType }{@code >}
     *     {@link JAXBElement }{@code <}{@link AxisChartType }{@code >}
     *     {@link JAXBElement }{@code <}{@link GraphicType }{@code >}
     *     
     */
    public void setGraphic(JAXBElement<? extends GraphicType> value) {
        this.graphic = ((JAXBElement<? extends GraphicType> ) value);
    }

    /**
     * Gets the value of the length property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterValueType }
     *     
     */
    public ParameterValueType getLength() {
        return length;
    }

    /**
     * Sets the value of the length property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterValueType }
     *     
     */
    public void setLength(ParameterValueType value) {
        this.length = value;
    }

    /**
     * Gets the value of the relativeOrientation property.
     * 
     * @return
     *     possible object is
     *     {@link RelativeOrientationType }
     *     
     */
    public RelativeOrientationType getRelativeOrientation() {
        return relativeOrientation;
    }

    /**
     * Sets the value of the relativeOrientation property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelativeOrientationType }
     *     
     */
    public void setRelativeOrientation(RelativeOrientationType value) {
        this.relativeOrientation = value;
    }

}