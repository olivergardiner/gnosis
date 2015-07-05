//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.30 at 04:02:16 PM BST 
//


package uk.org.whitecottage.visio.jaxb.visio2003;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Page_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Page_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="PageSheet" type="{http://schemas.microsoft.com/visio/2003/core}PageSheet_Type" minOccurs="0"/>
 *         &lt;element name="Shapes" type="{http://schemas.microsoft.com/visio/2003/core}Shapes_Type" minOccurs="0"/>
 *         &lt;element name="Connects" type="{http://schemas.microsoft.com/visio/2003/core}Connects_Type" minOccurs="0"/>
 *       &lt;/choice>
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="NameU" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Background" type="{http://schemas.microsoft.com/visio/2003/core}ISOBoolean" />
 *       &lt;attribute name="BackPage" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="ViewScale" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="ViewCenterX" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="ViewCenterY" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="ReviewerID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="AssociatedPage" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Page_Type", propOrder = {
    "pageSheetOrShapesOrConnects"
})
public class PageType {

    @XmlElements({
        @XmlElement(name = "PageSheet", type = PageSheetType.class),
        @XmlElement(name = "Shapes", type = ShapesType.class),
        @XmlElement(name = "Connects", type = ConnectsType.class)
    })
    protected List<Object> pageSheetOrShapesOrConnects;
    @XmlAttribute(name = "ID")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger id;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlAttribute(name = "NameU")
    protected String nameU;
    @XmlAttribute(name = "Background")
    protected BigInteger background;
    @XmlAttribute(name = "BackPage")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger backPage;
    @XmlAttribute(name = "ViewScale")
    protected Float viewScale;
    @XmlAttribute(name = "ViewCenterX")
    protected Float viewCenterX;
    @XmlAttribute(name = "ViewCenterY")
    protected Float viewCenterY;
    @XmlAttribute(name = "ReviewerID")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger reviewerID;
    @XmlAttribute(name = "AssociatedPage")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger associatedPage;

    /**
     * Gets the value of the pageSheetOrShapesOrConnects property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pageSheetOrShapesOrConnects property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPageSheetOrShapesOrConnects().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PageSheetType }
     * {@link ShapesType }
     * {@link ConnectsType }
     * 
     * 
     */
    public List<Object> getPageSheetOrShapesOrConnects() {
        if (pageSheetOrShapesOrConnects == null) {
            pageSheetOrShapesOrConnects = new ArrayList<Object>();
        }
        return this.pageSheetOrShapesOrConnects;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setID(BigInteger value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nameU property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameU() {
        return nameU;
    }

    /**
     * Sets the value of the nameU property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameU(String value) {
        this.nameU = value;
    }

    /**
     * Gets the value of the background property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBackground() {
        return background;
    }

    /**
     * Sets the value of the background property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBackground(BigInteger value) {
        this.background = value;
    }

    /**
     * Gets the value of the backPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBackPage() {
        return backPage;
    }

    /**
     * Sets the value of the backPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBackPage(BigInteger value) {
        this.backPage = value;
    }

    /**
     * Gets the value of the viewScale property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getViewScale() {
        return viewScale;
    }

    /**
     * Sets the value of the viewScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setViewScale(Float value) {
        this.viewScale = value;
    }

    /**
     * Gets the value of the viewCenterX property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getViewCenterX() {
        return viewCenterX;
    }

    /**
     * Sets the value of the viewCenterX property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setViewCenterX(Float value) {
        this.viewCenterX = value;
    }

    /**
     * Gets the value of the viewCenterY property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getViewCenterY() {
        return viewCenterY;
    }

    /**
     * Sets the value of the viewCenterY property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setViewCenterY(Float value) {
        this.viewCenterY = value;
    }

    /**
     * Gets the value of the reviewerID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getReviewerID() {
        return reviewerID;
    }

    /**
     * Sets the value of the reviewerID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setReviewerID(BigInteger value) {
        this.reviewerID = value;
    }

    /**
     * Gets the value of the associatedPage property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAssociatedPage() {
        return associatedPage;
    }

    /**
     * Sets the value of the associatedPage property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAssociatedPage(BigInteger value) {
        this.associatedPage = value;
    }

}
