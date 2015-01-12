//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.13 at 12:24:34 PM BST 
//


package uk.org.whitecottage.ea.gnosis.jaxb.portfolio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="status" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="debt-class" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="cost-to-fix" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="effort-to-fix" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "content"
})
@XmlRootElement(name = "enterpriseDebt")
public class EnterpriseDebt {

    @XmlValue
    protected String content;
    @XmlAttribute(name = "title", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String title;
    @XmlAttribute(name = "status", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String status;
    @XmlAttribute(name = "debt-class")
    @XmlSchemaType(name = "anySimpleType")
    protected String debtClass;
    @XmlAttribute(name = "cost-to-fix")
    @XmlSchemaType(name = "anySimpleType")
    protected String costToFix;
    @XmlAttribute(name = "effort-to-fix")
    protected String effortToFix;

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContent(String value) {
        this.content = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the debtClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDebtClass() {
        return debtClass;
    }

    /**
     * Sets the value of the debtClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebtClass(String value) {
        this.debtClass = value;
    }

    /**
     * Gets the value of the costToFix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCostToFix() {
        return costToFix;
    }

    /**
     * Sets the value of the costToFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCostToFix(String value) {
        this.costToFix = value;
    }

    /**
     * Gets the value of the effortToFix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEffortToFix() {
        return effortToFix;
    }

    /**
     * Sets the value of the effortToFix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEffortToFix(String value) {
        this.effortToFix = value;
    }

}
