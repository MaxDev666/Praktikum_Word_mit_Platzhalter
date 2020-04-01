package replace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.docx4j.model.datastorage.migration.AbstractMigrator;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.parts.relationships.RelationshipsPart.AddPartBehaviour;
import org.docx4j.wml.RPr;
import org.opendope.xpaths.Xpaths;
import org.opendope.xpaths.Xpaths.Xpath.DataBinding;

public class AbstractMigratorUsingSimpleTextFormat extends AbstractMigrator{

	protected SimpleTextPart simpleTextPart;
	
	protected Map<String, String> keys = new HashMap<String, String>();
	
	protected void createParts(WordprocessingMLPackage pkgOut) throws InvalidFormatException {
		
		super.createParts(pkgOut);

		// Simple Text
		simpleTextPart = new SimpleTextPart(new PartName("/customXml/item1.xml")); 
		pkgOut.getMainDocumentPart().addTargetPart(simpleTextPart, AddPartBehaviour.RENAME_IF_NAME_EXISTS);
		storeItemID = addPropertiesPart(pkgOut, simpleTextPart, null);	
		simpleTextPart.setJaxbElement(new Vorlagentexte());
		
	}
	
	/**
	 * @param r
	 * @param replacementContent
	 * @param key
	 */
	protected void createContentControl(RPr rPr, List<Object> replacementContent,
			String key) {
		// Has it been encountered already?
		if (!keys.containsKey(key) ) {
			// add the part entries
			addPartEntries(key);
			keys.put(key,  key);
		} 
		
		super.createContentControl(rPr, replacementContent, key, 
				"/texte/text[@id='" + key +"']", 
				null);		
	}
	
	private void addPartEntries(String key) {
		
		// text
		Vorlagentext t = new Vorlagentext();
		t.setId(key);;
		t.setValue("${" + key + "}");
		simpleTextPart.getJaxbElement().getText().add(t);
		
		// XPath
		Xpaths.Xpath xp = new org.opendope.xpaths.ObjectFactory().createXpathsXpath();
		xp.setId(key);
		xp.setPrepopulate(false);
		xp.setRequired(false);
		xp.setType("string");
		DataBinding db = new org.opendope.xpaths.ObjectFactory().createXpathsXpathDataBinding();
		db.setXpath("/texte/text[@id='" + key +"']");
		//db.setPrefixMappings("xmlns:odt='http://opendope.org/texte'");
		db.setStoreItemID(storeItemID);
		xp.setDataBinding(db);
		xPathsPart.getJaxbElement().getXpath().add(xp);
		
	}
	
}