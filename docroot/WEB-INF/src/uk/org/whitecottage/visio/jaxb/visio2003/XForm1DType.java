//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.30 at 04:02:16 PM BST 
//


package uk.org.whitecottage.visio.jaxb.visio2003;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for XForm1D_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XForm1D_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/visio/2003/core}Row_Type">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="BeginX" type="{http://schemas.microsoft.com/visio/2003/core}BeginX_Type" minOccurs="0"/>
 *         &lt;element name="BeginY" type="{http://schemas.microsoft.com/visio/2003/core}BeginY_Type" minOccurs="0"/>
 *         &lt;element name="EndX" type="{http://schemas.microsoft.com/visio/2003/core}EndX_Type" minOccurs="0"/>
 *         &lt;element name="EndY" type="{http://schemas.microsoft.com/visio/2003/core}EndY_Type" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XForm1D_Type", propOrder = {
    "beginXOrBeginYOrEndX"
})
public class XForm1DType
    extends RowType
{

    @XmlElements({
        @XmlElement(name = "BeginX", type = BeginXType.class),
        @XmlElement(name = "BeginY", type = BeginYType.class),
        @XmlElement(name = "EndX", type = EndXType.class),
        @XmlElement(name = "EndY", type = EndYType.class)
    })
    protected List<CellType> beginXOrBeginYOrEndX;

    /**
     * Gets the value of the beginXOrBeginYOrEndX property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the beginXOrBeginYOrEndX property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBeginXOrBeginYOrEndX().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BeginXType }
     * {@link BeginYType }
     * {@link EndXType }
     * {@link EndYType }
     * 
     * 
     */
    public List<CellType> getBeginXOrBeginYOrEndX() {
        if (beginXOrBeginYOrEndX == null) {
            beginXOrBeginYOrEndX = new ArrayList<CellType>();
        }
        return this.beginXOrBeginYOrEndX;
    }

}