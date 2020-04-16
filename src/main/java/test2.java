/*
 *  Copyright 2012, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 

    You may obtain a copy of the License at 

        http://www.apache.org/licenses/LICENSE-2.0 

    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.

 */

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.docx4j.XmlUtils;
import org.docx4j.model.datastorage.OpenDoPEHandler;
import org.docx4j.model.sdt.QueryString;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.openpackaging.parts.opendope.ConditionsPart;
import org.docx4j.openpackaging.parts.opendope.XPathsPart;
import org.docx4j.utils.SingleTraversalUtilVisitorCallback;
import org.docx4j.utils.TraversalUtilVisitor;
import org.docx4j.wml.CTDataBinding;
import org.docx4j.wml.SdtElement;
import org.docx4j.wml.SdtPr;
import org.docx4j.wml.Tag;
import org.opendope.conditions.Condition;
import org.opendope.conditions.Xpathref;


/**
 * Show the content controls in the main document part
 * indenting as nested.  
 * 
 * This is helpful for understanding complex documents.
 * 
 * If there are OpenDoPE tags, display info about these.
 * 
 * @author jharrop
 *
 */
public class test2 {
	
	/*
	 * private static org.opendope.conditions.Conditions conditions; private static
	 * org.opendope.xpaths.Xpaths xPaths;
	 * 
	 * private static boolean SHOW_BINDING_ROLE_XPATH = true;
	 */
	
	public static void main(String[] args) throws Exception {
		

		
		String inputfilepath = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST_out.docx";
		
			
		WordprocessingMLPackage wordMLPackage  = WordprocessingMLPackage.load(new java.io.File(inputfilepath));
		
		MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();
/*		if (wordMLPackage.getMainDocumentPart().getXPathsPart() == null) {
			throw new Docx4JException("OpenDoPE XPaths part missing");
		} else {
			xPaths = wordMLPackage.getMainDocumentPart().getXPathsPart()
					.getJaxbElement();
			System.out.println(XmlUtils.marshaltoString(xPaths, true, true));
		}
*/		
		
		TraversalUtilContentControlVisitor visitor = new TraversalUtilContentControlVisitor();		
		IndentingVisitorCallback contentControlCallback = new IndentingVisitorCallback(visitor);
		
		visitor.callback = contentControlCallback; 
		
		contentControlCallback.walkJAXBElements(wordMLPackage.getMainDocumentPart().getJaxbElement() );
	}
	
	
	public static class TraversalUtilContentControlVisitor extends TraversalUtilVisitor<SdtElement> {
		
		IndentingVisitorCallback callback; // so we can get indentation and repeat context
		
		@Override
		public void apply(SdtElement element, Object parent, List<Object> siblings) {
			
			StringBuffer sb = new StringBuffer(); 

//			sb.append("\n"+"\n");
			
			SdtPr sdtPr = element.getSdtPr();
			if (sdtPr!=null) {				

				CTDataBinding binding = (CTDataBinding) XmlUtils.unwrap(sdtPr.getDataBinding());
				if (binding!=null) {
					sb.append(binding.getXpath() );
				}
				
				/*Tag tag = sdtPr.getTag();
				if (tag != null) {			
				
				HashMap<String, String> map = QueryString.parseQueryString(
						tag.getVal(), true);
				
				String conditionId = map.get(OpenDoPEHandler.BINDING_ROLE_CONDITIONAL);
				String repeatId = map.get(OpenDoPEHandler.BINDING_ROLE_REPEAT);
				String xp = map.get(OpenDoPEHandler.BINDING_ROLE_XPATH);
				
				String repeatContext = callback.getRepeatContext();*/
				
					/*
					 * if (repeatId!=null ) {
					 * 
					 * org.opendope.xpaths.Xpaths.Xpath xpath = XPathsPart.getXPathById(xPaths,
					 * repeatId);
					 * 
					 * if (xpath==null) { sb.append("\n"+callback.indent + "  " +
					 * "XPath specified in repeat '" + repeatId + "' is missing!"); } else { String
					 * path = xpath.getDataBinding().getXpath(); sb.append("\n"+callback.indent +
					 * "  " + xpath.getId() + ": " + path ); if (repeatContext!=null) { // check
					 * this repeat is properly nested String repeatContextPath =
					 * XPathsPart.getXPathById(xPaths, repeatContext).getDataBinding().getXpath();
					 * if (path.startsWith(repeatContextPath)) { sb.append("\n"+callback.indent +
					 * "  " + ".. OK"); } else { sb.append("\n"+callback.indent + "  " +
					 * ".. !? not in " + repeatContext + ": " + repeatContextPath); } } }
					 * System.out.println(sb.toString());
					 * 
					 * } else
					 */
					/*if (xp!=null) {
					
					if (SHOW_BINDING_ROLE_XPATH) {
						org.opendope.xpaths.Xpaths.Xpath xpath = XPathsPart.getXPathById(xPaths, xp);
						
						if (xpath==null) {
							sb.append("\n"+callback.indent + "  " + "XPath specified with id '" + xp + "' is missing!");
						} else {
							String path = xpath.getDataBinding().getXpath();
							sb.append("\n"+callback.indent + "  " +  xpath.getId() + ": " + path );
							if (repeatContext!=null) {
								String repeatContextPath = XPathsPart.getXPathById(xPaths, repeatContext).getDataBinding().getXpath();
								if (path.startsWith(repeatContextPath)) {
									sb.append("\n"+callback.indent + "  " + ".. OK"); 
								} else {
									sb.append("\n"+callback.indent + "  " + ".. ?? not in " + repeatContext + ": " + repeatContextPath); 							
								}
							}
						}*/
						System.out.println(sb.toString());
					}
					
				} /*else {
					sb.append("\n"+callback.indent + "  " + "Check me?");
					System.out.println(sb.toString());
					
				}*/
				
			}
	
	/*
	 * } }
	 */
	
	
	
	
	public static class IndentingVisitorCallback extends SingleTraversalUtilVisitorCallback {

		public IndentingVisitorCallback(TraversalUtilVisitor visitor) {
			super(visitor);			
		}
		
		//String indent = "";
		
		/*
		 * private LinkedList<String> repeatContext = new LinkedList<String>(); public
		 * String getRepeatContext() { return repeatContext.peek(); }
		 */
		
		@Override
		public void walkJAXBElements(Object parent) {
			
			
			// Add repeat context?
			//Object parentUnwrapped = XmlUtils.unwrap(parent);
			/*
			 * boolean mustPopRepeat = false; if (parentUnwrapped instanceof SdtElement) {
			 * Tag tag = ((SdtElement)parentUnwrapped).getSdtPr().getTag(); if (tag == null)
			 * { log.debug("no tag " + XmlUtils.marshaltoString(parentUnwrapped)); } else {
			 * HashMap<String, String> map = QueryString.parseQueryString(tag.getVal(),
			 * true); String repeatId = map.get(OpenDoPEHandler.BINDING_ROLE_REPEAT); if
			 * (repeatId!=null) { repeatContext.push(repeatId); mustPopRepeat = true; } } }
			 */
			
			List children = getChildren(parent);
			if (children != null) {
				/*
				 * String oldIndent = indent; if (parentUnwrapped instanceof SdtElement) {
				 * indent += "  "; }
				 */
				for (Object o : children) {
					// if its wrapped in javax.xml.bind.JAXBElement, get its
					// value; this is ok, provided the results of the Callback
					// won't be marshalled
					o = XmlUtils.unwrap(o);
					this.apply(o, parent, children);
					if (this.shouldTraverse(o)) {
						walkJAXBElements(o);
					}
				}
				//indent = oldIndent;
			}
			
			// Remove repeat context?
			/*
			 * if (mustPopRepeat) { repeatContext.pop(); }
			 */
			
		}
		
	}
}
	

