//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.30 at 04:02:41 PM BST 
//


package uk.org.whitecottage.visio.jaxb.visio2007;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DataRecordSets_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DataRecordSets_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataRecordSet" type="{http://schemas.microsoft.com/visio/2006/extension}DataRecordSet_Type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="NextID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="ActiveRecordsetID" type="{http://www.w3.org/2001/XMLSchema}unsignedLong" />
 *       &lt;attribute name="DataWindowOrder" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DataRecordSets_Type", propOrder = {
    "dataRecordSet"
})
public class DataRecordSetsType {

    @XmlElement(name = "DataRecordSet")
    protected List<DataRecordSetType> dataRecordSet;
    @XmlAttribute(name = "NextID")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger nextID;
    @XmlAttribute(name = "ActiveRecordsetID")
    @XmlSchemaType(name = "unsignedLong")
    protected BigInteger activeRecordsetID;
    @XmlAttribute(name = "DataWindowOrder")
    protected String dataWindowOrder;

    /**
     * Gets the value of the dataRecordSet property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dataRecordSet property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDataRecordSet().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DataRecordSetType }
     * 
     * 
     */
    public List<DataRecordSetType> getDataRecordSet() {
        if (dataRecordSet == null) {
            dataRecordSet = new ArrayList<DataRecordSetType>();
        }
        return this.dataRecordSet;
    }

    /**
     * Gets the value of the nextID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNextID() {
        return nextID;
    }

    /**
     * Sets the value of the nextID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNextID(BigInteger value) {
        this.nextID = value;
    }

    /**
     * Gets the value of the activeRecordsetID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getActiveRecordsetID() {
        return activeRecordsetID;
    }

    /**
     * Sets the value of the activeRecordsetID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setActiveRecordsetID(BigInteger value) {
        this.activeRecordsetID = value;
    }

    /**
     * Gets the value of the dataWindowOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataWindowOrder() {
        return dataWindowOrder;
    }

    /**
     * Sets the value of the dataWindowOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataWindowOrder(String value) {
        this.dataWindowOrder = value;
    }

}
