package replace;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.docx4j.fonts.substitutions.FontSubstitutions.Replace;

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
 *         &lt;element ref="{http://opendope.org/texte}text" maxOccurs="unbounded" minOccurs="0"/>>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 */
@XmlRootElement(name = "texte")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "http://itzbund.de/texte", name = "", propOrder = {
    "text"
})
@XmlSeeAlso({Vorlagentext.class})
public class Vorlagentexte {
	
	@XmlElement(name="text", type=Vorlagentext.class)
	protected List<Vorlagentext> text;

	/**
     * Gets the value of the text property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the answerOrRepeat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getText().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Text }
     * 
     * 
     */
    public List<Vorlagentext> getText() {
        if (text == null) {
            text = new ArrayList<Vorlagentext>();
        }
        return this.text;
    }

}


    

    


