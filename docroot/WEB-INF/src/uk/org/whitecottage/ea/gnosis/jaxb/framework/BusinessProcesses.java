//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.26 at 03:22:39 PM BST 
//


package uk.org.whitecottage.ea.gnosis.jaxb.framework;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element ref="{}processDomain" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}processFlow" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "processDomain",
    "processFlow"
})
@XmlRootElement(name = "businessProcesses")
public class BusinessProcesses {

    protected List<ProcessDomain> processDomain;
    protected List<ProcessFlow> processFlow;

    /**
     * Gets the value of the processDomain property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processDomain property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessDomain().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessDomain }
     * 
     * 
     */
    public List<ProcessDomain> getProcessDomain() {
        if (processDomain == null) {
            processDomain = new ArrayList<ProcessDomain>();
        }
        return this.processDomain;
    }

    /**
     * Gets the value of the processFlow property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the processFlow property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcessFlow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ProcessFlow }
     * 
     * 
     */
    public List<ProcessFlow> getProcessFlow() {
        if (processFlow == null) {
            processFlow = new ArrayList<ProcessFlow>();
        }
        return this.processFlow;
    }

}
