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
 * <p>Java class for Event_Type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Event_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.microsoft.com/visio/2003/core}Row_Type">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="TheData" type="{http://schemas.microsoft.com/visio/2003/core}TheData_Type" minOccurs="0"/>
 *         &lt;element name="TheText" type="{http://schemas.microsoft.com/visio/2003/core}TheText_Type" minOccurs="0"/>
 *         &lt;element name="EventDblClick" type="{http://schemas.microsoft.com/visio/2003/core}EventDblClick_Type" minOccurs="0"/>
 *         &lt;element name="EventXFMod" type="{http://schemas.microsoft.com/visio/2003/core}EventXFMod_Type" minOccurs="0"/>
 *         &lt;element name="EventDrop" type="{http://schemas.microsoft.com/visio/2003/core}EventDrop_Type" minOccurs="0"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event_Type", propOrder = {
    "theDataOrTheTextOrEventDblClick"
})
public class EventType
    extends RowType
{

    @XmlElements({
        @XmlElement(name = "TheData", type = TheDataType.class),
        @XmlElement(name = "TheText", type = TheTextType.class),
        @XmlElement(name = "EventDblClick", type = EventDblClickType.class),
        @XmlElement(name = "EventXFMod", type = EventXFModType.class),
        @XmlElement(name = "EventDrop", type = EventDropType.class)
    })
    protected List<CellType> theDataOrTheTextOrEventDblClick;

    /**
     * Gets the value of the theDataOrTheTextOrEventDblClick property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the theDataOrTheTextOrEventDblClick property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTheDataOrTheTextOrEventDblClick().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TheDataType }
     * {@link TheTextType }
     * {@link EventDblClickType }
     * {@link EventXFModType }
     * {@link EventDropType }
     * 
     * 
     */
    public List<CellType> getTheDataOrTheTextOrEventDblClick() {
        if (theDataOrTheTextOrEventDblClick == null) {
            theDataOrTheTextOrEventDblClick = new ArrayList<CellType>();
        }
        return this.theDataOrTheTextOrEventDblClick;
    }

}
