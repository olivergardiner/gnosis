//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.11 at 02:25:12 PM BST 
//


package uk.org.whitecottage.ea.gnosis.jaxb.framework;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{}description"/>
 *         &lt;element ref="{}capability" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}milestone" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="domain-id" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute ref="{}name use="required""/>
 *       &lt;attribute name="standardisation">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="diverse"/>
 *             &lt;enumeration value="replicated"/>
 *             &lt;enumeration value="coordinated"/>
 *             &lt;enumeration value="unified"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="value-chain" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "capability",
    "milestone"
})
@XmlRootElement(name = "technologyDomain")
public class TechnologyDomain {

    @XmlElement(required = true)
    protected String description;
    protected List<Capability> capability;
    protected List<Milestone> milestone;
    @XmlAttribute(name = "domain-id", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String domainId;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "standardisation")
    protected String standardisation;
    @XmlAttribute(name = "value-chain")
    @XmlSchemaType(name = "anySimpleType")
    protected String valueChain;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the capability property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the capability property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCapability().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Capability }
     * 
     * 
     */
    public List<Capability> getCapability() {
        if (capability == null) {
            capability = new ArrayList<Capability>();
        }
        return this.capability;
    }

    /**
     * Gets the value of the milestone property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the milestone property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMilestone().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Milestone }
     * 
     * 
     */
    public List<Milestone> getMilestone() {
        if (milestone == null) {
            milestone = new ArrayList<Milestone>();
        }
        return this.milestone;
    }

    /**
     * Gets the value of the domainId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainId() {
        return domainId;
    }

    /**
     * Sets the value of the domainId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainId(String value) {
        this.domainId = value;
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
     * Gets the value of the standardisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStandardisation() {
        return standardisation;
    }

    /**
     * Sets the value of the standardisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStandardisation(String value) {
        this.standardisation = value;
    }

    /**
     * Gets the value of the valueChain property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueChain() {
        return valueChain;
    }

    /**
     * Sets the value of the valueChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueChain(String value) {
        this.valueChain = value;
    }

}
