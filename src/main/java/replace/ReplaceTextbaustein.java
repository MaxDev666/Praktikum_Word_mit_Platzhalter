package replace;

import java.util.List;

import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.model.datastorage.CustomXmlDataStorage;
import org.docx4j.model.datastorage.CustomXmlDataStorageImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.CustomXmlDataStoragePart;
import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.CTDataBinding;
import org.docx4j.wml.SdtElement;
import org.docx4j.wml.SdtPr;

import main.Hauptprogramm;

/* 
 * Diese Klasse dient dem Testen des Textbaustein-Ersetzens. 
 * Dabei werden erst durch getXpaths nacheinander zwei Klassen aufgerufen, 
 * die die XPath-Pfade und die IDs der Textbaustein-Elemente herausfinden und in Arraylisten speichern
 * Dann wird durch das HAuptprogramm die addTextbausteine-Methode aufgerufen, 
 * die das Ersetzen der Textbausteine durchführt   
 */
public class ReplaceTextbaustein {

	public static void getXpaths(String inputfilepath, WordprocessingMLPackage wordMLPackage) throws Docx4JException {

		TraversalUtilContentControlVisitor visitor = new TraversalUtilContentControlVisitor();		
		IndentingVisitorCallback contentControlCallback = new IndentingVisitorCallback(visitor);

		visitor.callback = contentControlCallback; 
		
		contentControlCallback.walkJAXBElements(wordMLPackage.getMainDocumentPart().getJaxbElement() );
		
		System.out.println(Hauptprogramm.xPathList.toString());
		System.out.println(Hauptprogramm.idList.toString());
	}
	
	public static void addTextbausteine(List<String> texte, List<String> xpaths, String inputfilepath, WordprocessingMLPackage wordMLPackage) throws Docx4JException {

		String outputfilepath = inputfilepath.split("[.]")[0] + "_test." + inputfilepath.split("[.]")[1];
		
		// Get the part
		// First we need to get the part.  There are a few different ways to do this.
		
		// If we know the itemId ..
		String itemId = "{A22AFF5C-1F0E-4A8E-99C6-EE3CEF90DED8}".toLowerCase();
		CustomXmlDataStoragePart customXmlDataStoragePart = (CustomXmlDataStoragePart) wordMLPackage.getCustomXmlDataStorageParts().get(itemId);
		// .. if you don't know the itemId (which you can get using ContentControlsPartsInfo), 
		// you could get the part by name, by type,
		// or you could find the item Id by looking in an SDT for w:storeItemID:
		//  <w:sdt>
		//    <w:sdtPr>
		//      <w:dataBinding w:prefixMappings="" w:xpath="/myxml[1]/element1[1]" 
		//					w:storeItemID="{5448916C-134B-45E6-B8FE-88CC1FFC17C3}"/>
		//    </w:sdtPr>
		//    <w:sdtContent>		
		
		// Get the contents		
		CustomXmlDataStorage customXmlDataStorage = customXmlDataStoragePart.getData();
			// In a real program what you might do is populate this with your own data.
			// You could replace the whole part (as is done in https://github.com/plutext/OpenDoPE-WAR/blob/master/webapp-simple/src/main/java/org/opendope/webapp/SubmitBoth.java ),
		    // or as we show below, just set some particular value
		
				
		// Change its contents.  Here we use XPath, but you could get the DOM document:
		//   ((CustomXmlDataStorageImpl)customXmlDataStorage).getDocument()
		// and do whatever ....
		/*for (int i = 0; i < xpaths.size(); i++) {
			String text = texte.get(i);
			System.out.println(text);
			String xpath = xpaths.get(i);
			System.out.println(xpath);*/
			((CustomXmlDataStorageImpl)customXmlDataStorage).setNodeValueAtXPath(xpaths.get(0), texte.get(0),
					"xmlns:oda='http://opendope.org/answers'"); // no prefix mappings required here, but that shows you how to do it
		//}

		System.out.println(XmlUtils.w3CDomNodeToString(customXmlDataStorage.getDocument()));
		
		// Apply the bindings
		BindingHandler bh = new BindingHandler(wordMLPackage);
		bh.applyBindings(wordMLPackage.getMainDocumentPart());
		
		// If you inspect the output, you should see your data in 2 places:
		// 1. the custom xml part 
		// 2. (more importantly) the main document part
		System.out.println(
				XmlUtils.marshaltoString(wordMLPackage.getMainDocumentPart().getJaxbElement(), true, true)
				);
		
		
		wordMLPackage.save(new java.io.File(outputfilepath) );
	}
	

	public static class TraversalUtilContentControlVisitor extends TraversalUtilVisitor<SdtElement> {

		IndentingVisitorCallback callback; // so we can get indentation and repeat context

		@Override
		public void apply(SdtElement element, Object parent, List<Object> siblings) {
			StringBuffer sb = new StringBuffer(); 
			SdtPr sdtPr = element.getSdtPr();
			if (sdtPr!=null) {				

				CTDataBinding binding = (CTDataBinding) XmlUtils.unwrap(sdtPr.getDataBinding());
				if (binding!=null) {
					sb.append(binding.getXpath() );
				}
			}
			int start = sb.indexOf("'");
			int end = sb.indexOf("'", start+1);
			String id = sb.substring(start+1, end);
			Hauptprogramm.idList.add(id);
			if (id.substring(0, 2).equals("tb")) {
				Hauptprogramm.xPathList.add(sb.toString());
			}
		}
	}

	public static class IndentingVisitorCallback extends SingleTraversalUtilVisitorCallback {
		public IndentingVisitorCallback(TraversalUtilVisitor visitor) {
			super(visitor);			
		}

		@Override
		public void walkJAXBElements(Object parent) {

			List children = getChildren(parent);
			if (children != null) {
				for (Object o : children) {
					o = XmlUtils.unwrap(o);
					this.apply(o, parent, children);
					if (this.shouldTraverse(o)) {
						walkJAXBElements(o);
					}
				}
			}
		}
	}

}

