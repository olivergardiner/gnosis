//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.14 at 11:18:14 AM BST 
//


package uk.org.whitecottage.ea.gnosis.jaxb.roadmap;

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
 *         &lt;element ref="{}dependencyReason"/>
 *       &lt;/sequence>
 *       &lt;attribute name="project" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "dependencyReason"
})
@XmlRootElement(name = "projectDependency")
public class ProjectDependency {

    @XmlElement(required = true)
    protected Object dependencyReason;
    @XmlAttribute(name = "project", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String project;

    /**
     * Gets the value of the dependencyReason property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getDependencyReason() {
        return dependencyReason;
    }

    /**
     * Sets the value of the dependencyReason property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setDependencyReason(Object value) {
        this.dependencyReason = value;
    }

    /**
     * Gets the value of the project property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets the value of the project property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProject(String value) {
        this.project = value;
    }

}