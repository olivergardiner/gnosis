//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.03 at 12:07:19 PM GMT 
//


package uk.org.whitecottage.ea.gnosis.jaxb.applications;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}classification" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}ecosystem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}stage" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}migration" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}investment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}usedBy" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}description" minOccurs="0"/>
 *         &lt;element ref="{}notes" minOccurs="0"/>
 *         &lt;element ref="{}source" minOccurs="0"/>
 *         &lt;element ref="{}managingOrganisation" minOccurs="0"/>
 *         &lt;element ref="{}procurementTower" minOccurs="0"/>
 *         &lt;element ref="{}tag" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}comment" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="app-id" use="required" type="{http://www.w3.org/2001/XMLSchema}token" />
 *       &lt;attribute ref="{}name use="required""/>
 *       &lt;attribute name="owner" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="sensitivity" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="criticality" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="virtualisation">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="high"/>
 *             &lt;enumeration value="medium"/>
 *             &lt;enumeration value="low"/>
 *             &lt;enumeration value="unknown"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "classification",
    "ecosystem",
    "stage",
    "migration",
    "investment",
    "usedBy",
    "description",
    "notes",
    "source",
    "managingOrganisation",
    "procurementTower",
    "tag",
    "comment"
})
@XmlRootElement(name = "application")
public class Application {

    protected List<Classification> classification;
    protected List<Ecosystem> ecosystem;
    protected List<Stage> stage;
    protected List<Migration> migration;
    protected List<Investment> investment;
    protected List<UsedBy> usedBy;
    protected String description;
    protected String notes;
    protected String source;
    protected ManagingOrganisation managingOrganisation;
    protected String procurementTower;
    protected List<Tag> tag;
    protected List<Comment> comment;
    @XmlAttribute(name = "app-id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String appId;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "owner")
    protected String owner;
    @XmlAttribute(name = "sensitivity")
    protected String sensitivity;
    @XmlAttribute(name = "criticality")
    protected String criticality;
    @XmlAttribute(name = "virtualisation")
    protected String virtualisation;

    /**
     * Gets the value of the classification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Classification }
     * 
     * 
     */
    public List<Classification> getClassification() {
        if (classification == null) {
            classification = new ArrayList<Classification>();
        }
        return this.classification;
    }

    /**
     * Gets the value of the ecosystem property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ecosystem property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEcosystem().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ecosystem }
     * 
     * 
     */
    public List<Ecosystem> getEcosystem() {
        if (ecosystem == null) {
            ecosystem = new ArrayList<Ecosystem>();
        }
        return this.ecosystem;
    }

    /**
     * Gets the value of the stage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Stage }
     * 
     * 
     */
    public List<Stage> getStage() {
        if (stage == null) {
            stage = new ArrayList<Stage>();
        }
        return this.stage;
    }

    /**
     * Gets the value of the migration property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the migration property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMigration().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Migration }
     * 
     * 
     */
    public List<Migration> getMigration() {
        if (migration == null) {
            migration = new ArrayList<Migration>();
        }
        return this.migration;
    }

    /**
     * Gets the value of the investment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the investment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInvestment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Investment }
     * 
     * 
     */
    public List<Investment> getInvestment() {
        if (investment == null) {
            investment = new ArrayList<Investment>();
        }
        return this.investment;
    }

    /**
     * Gets the value of the usedBy property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the usedBy property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsedBy().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UsedBy }
     * 
     * 
     */
    public List<UsedBy> getUsedBy() {
        if (usedBy == null) {
            usedBy = new ArrayList<UsedBy>();
        }
        return this.usedBy;
    }

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
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSource(String value) {
        this.source = value;
    }

    /**
     * Gets the value of the managingOrganisation property.
     * 
     * @return
     *     possible object is
     *     {@link ManagingOrganisation }
     *     
     */
    public ManagingOrganisation getManagingOrganisation() {
        return managingOrganisation;
    }

    /**
     * Sets the value of the managingOrganisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagingOrganisation }
     *     
     */
    public void setManagingOrganisation(ManagingOrganisation value) {
        this.managingOrganisation = value;
    }

    /**
     * Gets the value of the procurementTower property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcurementTower() {
        return procurementTower;
    }

    /**
     * Sets the value of the procurementTower property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcurementTower(String value) {
        this.procurementTower = value;
    }

    /**
     * Gets the value of the tag property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tag property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTag().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Tag }
     * 
     * 
     */
    public List<Tag> getTag() {
        if (tag == null) {
            tag = new ArrayList<Tag>();
        }
        return this.tag;
    }

    /**
     * Gets the value of the comment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the comment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Comment }
     * 
     * 
     */
    public List<Comment> getComment() {
        if (comment == null) {
            comment = new ArrayList<Comment>();
        }
        return this.comment;
    }

    /**
     * Gets the value of the appId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppId() {
        return appId;
    }

    /**
     * Sets the value of the appId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppId(String value) {
        this.appId = value;
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
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOwner(String value) {
        this.owner = value;
    }

    /**
     * Gets the value of the sensitivity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSensitivity() {
        return sensitivity;
    }

    /**
     * Sets the value of the sensitivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSensitivity(String value) {
        this.sensitivity = value;
    }

    /**
     * Gets the value of the criticality property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriticality() {
        return criticality;
    }

    /**
     * Sets the value of the criticality property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriticality(String value) {
        this.criticality = value;
    }

    /**
     * Gets the value of the virtualisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVirtualisation() {
        return virtualisation;
    }

    /**
     * Sets the value of the virtualisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVirtualisation(String value) {
        this.virtualisation = value;
    }

}
