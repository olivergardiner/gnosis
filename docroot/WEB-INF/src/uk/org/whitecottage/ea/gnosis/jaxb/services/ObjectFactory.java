//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.11.11 at 11:06:59 AM GMT 
//


package uk.org.whitecottage.ea.gnosis.jaxb.services;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the uk.org.whitecottage.ea.gnosis.jaxb.services package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Description_QNAME = new QName("", "description");
    private final static QName _DependencyReason_QNAME = new QName("", "dependencyReason");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: uk.org.whitecottage.ea.gnosis.jaxb.services
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceComponent }
     * 
     */
    public ServiceComponent createServiceComponent() {
        return new ServiceComponent();
    }

    /**
     * Create an instance of {@link ServiceElement }
     * 
     */
    public ServiceElement createServiceElement() {
        return new ServiceElement();
    }

    /**
     * Create an instance of {@link Dependency }
     * 
     */
    public Dependency createDependency() {
        return new Dependency();
    }

    /**
     * Create an instance of {@link ServiceArea }
     * 
     */
    public ServiceArea createServiceArea() {
        return new ServiceArea();
    }

    /**
     * Create an instance of {@link ProcurementGroup }
     * 
     */
    public ProcurementGroup createProcurementGroup() {
        return new ProcurementGroup();
    }

    /**
     * Create an instance of {@link TowerRef }
     * 
     */
    public TowerRef createTowerRef() {
        return new TowerRef();
    }

    /**
     * Create an instance of {@link Services }
     * 
     */
    public Services createServices() {
        return new Services();
    }

    /**
     * Create an instance of {@link Tower }
     * 
     */
    public Tower createTower() {
        return new Tower();
    }

    /**
     * Create an instance of {@link RecycleBin }
     * 
     */
    public RecycleBin createRecycleBin() {
        return new RecycleBin();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "description")
    public JAXBElement<String> createDescription(String value) {
        return new JAXBElement<String>(_Description_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dependencyReason")
    public JAXBElement<String> createDependencyReason(String value) {
        return new JAXBElement<String>(_DependencyReason_QNAME, String.class, null, value);
    }

}
