//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.30 at 04:02:16 PM BST 
//


package uk.org.whitecottage.visio.jaxb.visio2003;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FaceName_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FaceName_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="Name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="UnicodeRanges" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="CharSets" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Panos" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Flags" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FaceName_Type")
public class FaceNameType {

    @XmlAttribute(name = "ID")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger id;
    @XmlAttribute(name = "Name")
    protected String name;
    @XmlAttribute(name = "UnicodeRanges")
    protected String unicodeRanges;
    @XmlAttribute(name = "CharSets")
    protected String charSets;
    @XmlAttribute(name = "Panos")
    protected String panos;
    @XmlAttribute(name = "Flags")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger flags;

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
     * Gets the value of the unicodeRanges property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnicodeRanges() {
        return unicodeRanges;
    }

    /**
     * Sets the value of the unicodeRanges property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnicodeRanges(String value) {
        this.unicodeRanges = value;
    }

    /**
     * Gets the value of the charSets property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCharSets() {
        return charSets;
    }

    /**
     * Sets the value of the charSets property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCharSets(String value) {
        this.charSets = value;
    }

    /**
     * Gets the value of the panos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPanos() {
        return panos;
    }

    /**
     * Sets the value of the panos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPanos(String value) {
        this.panos = value;
    }

    /**
     * Gets the value of the flags property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFlags() {
        return flags;
    }

    /**
     * Sets the value of the flags property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFlags(BigInteger value) {
        this.flags = value;
    }

}