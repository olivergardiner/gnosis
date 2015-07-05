//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.06.30 at 04:02:41 PM BST 
//


package uk.org.whitecottage.visio.jaxb.visio2007;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Align_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Align_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/visio/2006/extension}Row_Type">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="AlignLeft" type="{http://schemas.microsoft.com/visio/2006/extension}AlignLeft_Type" minOccurs="0"/>
 *         &lt;element name="AlignCenter" type="{http://schemas.microsoft.com/visio/2006/extension}AlignCenter_Type" minOccurs="0"/>
 *         &lt;element name="AlignRight" type="{http://schemas.microsoft.com/visio/2006/extension}AlignRight_Type" minOccurs="0"/>
 *         &lt;element name="AlignTop" type="{http://schemas.microsoft.com/visio/2006/extension}AlignTop_Type" minOccurs="0"/>
 *         &lt;element name="AlignMiddle" type="{http://schemas.microsoft.com/visio/2006/extension}AlignMiddle_Type" minOccurs="0"/>
 *         &lt;element name="AlignBottom" type="{http://schemas.microsoft.com/visio/2006/extension}AlignBottom_Type" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Align_Type", propOrder = {
    "alignLeftOrAlignCenterOrAlignRight"
})
public class AlignType
    extends RowType
{

    @XmlElements({
        @XmlElement(name = "AlignLeft", type = AlignLeftType.class),
        @XmlElement(name = "AlignCenter", type = AlignCenterType.class),
        @XmlElement(name = "AlignRight", type = AlignRightType.class),
        @XmlElement(name = "AlignTop", type = AlignTopType.class),
        @XmlElement(name = "AlignMiddle", type = AlignMiddleType.class),
        @XmlElement(name = "AlignBottom", type = AlignBottomType.class)
    })
    protected List<CellType> alignLeftOrAlignCenterOrAlignRight;

    /**
     * Gets the value of the alignLeftOrAlignCenterOrAlignRight property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alignLeftOrAlignCenterOrAlignRight property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlignLeftOrAlignCenterOrAlignRight().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlignLeftType }
     * {@link AlignCenterType }
     * {@link AlignRightType }
     * {@link AlignTopType }
     * {@link AlignMiddleType }
     * {@link AlignBottomType }
     * 
     * 
     */
    public List<CellType> getAlignLeftOrAlignCenterOrAlignRight() {
        if (alignLeftOrAlignCenterOrAlignRight == null) {
            alignLeftOrAlignCenterOrAlignRight = new ArrayList<CellType>();
        }
        return this.alignLeftOrAlignCenterOrAlignRight;
    }

}
