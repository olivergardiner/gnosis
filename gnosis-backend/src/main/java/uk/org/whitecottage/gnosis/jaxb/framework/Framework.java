//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.18 at 04:16:38 PM BST 
//


package uk.org.whitecottage.gnosis.jaxb.framework;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}businessContext"/&gt;
 *         &lt;element ref="{}businessOperatingModel"/&gt;
 *         &lt;element ref="{}businessApplications"/&gt;
 *         &lt;element ref="{}commonServices"/&gt;
 *         &lt;element ref="{}infrastructure"/&gt;
 *         &lt;element ref="{}valueChain"/&gt;
 *         &lt;element ref="{}recycleBin" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "businessContext",
    "businessOperatingModel",
    "businessApplications",
    "commonServices",
    "infrastructure",
    "valueChain",
    "recycleBin"
})
@XmlRootElement(name = "framework")
public class Framework
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(required = true)
    protected Object businessContext;
    @XmlElement(required = true)
    protected BusinessOperatingModel businessOperatingModel;
    @XmlElement(required = true)
    protected BusinessApplications businessApplications;
    @XmlElement(required = true)
    protected CommonServices commonServices;
    @XmlElement(required = true)
    protected Infrastructure infrastructure;
    @XmlElement(required = true)
    protected ValueChain valueChain;
    protected RecycleBin recycleBin;

    /**
     * Gets the value of the businessContext property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getBusinessContext() {
        return businessContext;
    }

    /**
     * Sets the value of the businessContext property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setBusinessContext(Object value) {
        this.businessContext = value;
    }

    /**
     * Gets the value of the businessOperatingModel property.
     * 
     * @return
     *     possible object is
     *     {@link BusinessOperatingModel }
     *     
     */
    public BusinessOperatingModel getBusinessOperatingModel() {
        return businessOperatingModel;
    }

    /**
     * Sets the value of the businessOperatingModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link BusinessOperatingModel }
     *     
     */
    public void setBusinessOperatingModel(BusinessOperatingModel value) {
        this.businessOperatingModel = value;
    }

    /**
     * Gets the value of the businessApplications property.
     * 
     * @return
     *     possible object is
     *     {@link BusinessApplications }
     *     
     */
    public BusinessApplications getBusinessApplications() {
        return businessApplications;
    }

    /**
     * Sets the value of the businessApplications property.
     * 
     * @param value
     *     allowed object is
     *     {@link BusinessApplications }
     *     
     */
    public void setBusinessApplications(BusinessApplications value) {
        this.businessApplications = value;
    }

    /**
     * Gets the value of the commonServices property.
     * 
     * @return
     *     possible object is
     *     {@link CommonServices }
     *     
     */
    public CommonServices getCommonServices() {
        return commonServices;
    }

    /**
     * Sets the value of the commonServices property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommonServices }
     *     
     */
    public void setCommonServices(CommonServices value) {
        this.commonServices = value;
    }

    /**
     * Gets the value of the infrastructure property.
     * 
     * @return
     *     possible object is
     *     {@link Infrastructure }
     *     
     */
    public Infrastructure getInfrastructure() {
        return infrastructure;
    }

    /**
     * Sets the value of the infrastructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Infrastructure }
     *     
     */
    public void setInfrastructure(Infrastructure value) {
        this.infrastructure = value;
    }

    /**
     * Gets the value of the valueChain property.
     * 
     * @return
     *     possible object is
     *     {@link ValueChain }
     *     
     */
    public ValueChain getValueChain() {
        return valueChain;
    }

    /**
     * Sets the value of the valueChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueChain }
     *     
     */
    public void setValueChain(ValueChain value) {
        this.valueChain = value;
    }

    /**
     * Gets the value of the recycleBin property.
     * 
     * @return
     *     possible object is
     *     {@link RecycleBin }
     *     
     */
    public RecycleBin getRecycleBin() {
        return recycleBin;
    }

    /**
     * Sets the value of the recycleBin property.
     * 
     * @param value
     *     allowed object is
     *     {@link RecycleBin }
     *     
     */
    public void setRecycleBin(RecycleBin value) {
        this.recycleBin = value;
    }

}