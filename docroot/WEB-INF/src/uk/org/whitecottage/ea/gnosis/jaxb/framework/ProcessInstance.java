//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.10.22 at 10:52:21 AM BST 
//


package uk.org.whitecottage.ea.gnosis.jaxb.framework;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}parent" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}predecessor" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataImpact" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="dataEntity" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="impact" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="process-id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="duration" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "parent",
    "predecessor",
    "dataImpact"
})
@XmlRootElement(name = "processInstance")
public class ProcessInstance {

    protected List<Parent> parent;
    protected List<Predecessor> predecessor;
    protected List<ProcessInstance.DataImpact> dataImpact;
    @XmlAttribute(name = "process-id", required = true)
    protected String processId;
    @XmlAttribute(name = "duration")
    @XmlSchemaType(name = "anySimpleType")
    protected String duration;

    /**
     * Gets the value of the parent property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parent property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parent }
     * 
     * 
     */
    public List<Parent> getParent() {
        if (parent == null) {
            parent = new ArrayList<Parent>();
        }
        return this.parent;
    }

    /**
     * Gets the value of the predecessor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the predecessor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPredecessor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Predecessor }
     * 
     * 
     */
    public List<Predecessor> getPredecessor() {
        if (predecessor == null) {
            predecessor = new ArrayList<Predecessor>();
        }
        return this.predecessor;
    }

    /**
     * Gets the value of the dataImpact property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataImpact property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataImpact().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessInstance.DataImpact }
     * 
     * 
     */
    public List<ProcessInstance.DataImpact> getDataImpact() {
        if (dataImpact == null) {
            dataImpact = new ArrayList<ProcessInstance.DataImpact>();
        }
        return this.dataImpact;
    }

    /**
     * Gets the value of the processId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessId() {
        return processId;
    }

    /**
     * Sets the value of the processId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessId(String value) {
        this.processId = value;
    }

    /**
     * Gets the value of the duration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDuration() {
        return duration;
    }

    /**
     * Sets the value of the duration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDuration(String value) {
        this.duration = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="dataEntity" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="impact" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DataImpact {

        @XmlAttribute(name = "dataEntity")
        protected String dataEntity;
        @XmlAttribute(name = "impact")
        protected String impact;

        /**
         * Gets the value of the dataEntity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDataEntity() {
            return dataEntity;
        }

        /**
         * Sets the value of the dataEntity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDataEntity(String value) {
            this.dataEntity = value;
        }

        /**
         * Gets the value of the impact property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getImpact() {
            return impact;
        }

        /**
         * Sets the value of the impact property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setImpact(String value) {
            this.impact = value;
        }

    }

}