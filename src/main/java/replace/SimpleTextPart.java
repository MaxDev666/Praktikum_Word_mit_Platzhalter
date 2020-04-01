package replace;

import javax.xml.bind.JAXBContext;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.opendope.JaxbCustomXmlDataStoragePart;

public class SimpleTextPart extends JaxbCustomXmlDataStoragePart<Vorlagentexte>{

	public SimpleTextPart(PartName partName) throws InvalidFormatException {
		super(partName);
		init();
	}

	public SimpleTextPart(PartName partName, JAXBContext jc) throws InvalidFormatException {
		super(partName, jc);
		init();
	}
	
	public static void main(String[] args) throws Docx4JException {

		SimpleTextPart sap = new SimpleTextPart(new PartName("/testing"));
		
		Vorlagentexte texte = new Vorlagentexte();
		Vorlagentext text = new Vorlagentext();
		texte.getText().add(text);
		
		text.setId("id1");
		text.setValue("myval");
		
		sap.setContents(texte);
		
		System.out.println(sap.getXML());
		
		String prefixMappings = "xmlns:t='http://itzbund.de/texte'";
		
		System.out.println(
				sap.xpathGetString("/t:texte/t:text", prefixMappings));
		
		sap.setNodeValueAtXPath("/t:texte/t:text", "foo", prefixMappings);
		
		System.out.println(
				sap.xpathGetString("/t:texte/t:text", prefixMappings));
		
	}
	
}

	
