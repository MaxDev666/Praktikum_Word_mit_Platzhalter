package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import datenbank.DatenbankverbindungVorlage;

public class Hauptprogramm {

	public static JAXBContext context = org.docx4j.jaxb.Context.jc;

	public static void main(String[] args) throws Exception {
		// Zu Testzwecken, zum Kopieren des Testpfads
		// C:\Users\Maximilian
		// Hett\Desktop\Studium\ITZBund\Praktikuminternes\Test\Vorlage2_Bescheid_BZST.docx

		// Je nach übergebenen Parametern wird eine entsprechende Methode zur
		// Verarbeitung der Vorlage ausgeführt
		switch (args[0]) {
		case "add":
			String rueckgabe = hinzufuegenVorlage(args[1]);
			break;
		case "get":
			ansehenVorlage(args[1]);
			break;
		case "edit":
			bearbeitenVorlage(args[1], args[2]);
			break;
		case "delete":
			loeschenVorlage(args[1]);
			break;
		case "replace":
			replacePlaceholder(args[1], args[2]);
			break;
		default:
			String noOption ="Es wurde keine Option ausgewählt";
			System.out.println(noOption);	
		}
	}

	/*
	 * Es wird eine Vorlage zur Datenbank hinzugefügt.
	 * Anhand des Dateipfads wird der Dateiname ermittelt, der als ID verwendet wird.
	 * Anschließend werden der Dateipfad sowie die neue ID in der Vorlagen-Tabelle gespeichert.
	 */
	public static String hinzufuegenVorlage(String documentpath) throws Exception {
		String[] splitpath = documentpath.split(Pattern.quote("\\"));
		String id = splitpath[splitpath.length-1];
		id = id.split("[.]")[0];
		String rueckgabe = DatenbankverbindungVorlage.getVorlage(id);
		if (rueckgabe == "Eintrag nicht vorhanden") {
			DatenbankverbindungVorlage.addVorlage(id, documentpath);
			rueckgabe = "";
		}else {
			rueckgabe = "Eintrag vorhanden; bitte bearbeiten, nicht hinzufügen";
		}
		return rueckgabe;
	}

	/*
	 * Es wird eine Vorlage angezeigt. 
	 * Dazu wird mithilfe der ID die Datenbank gelesen und der Pfad der Word-Datei zurückgegeben.
	 * Wenn kein Eintrag gefunden wird, wird ein Fehler zurückgegeben
	 */
	public static String ansehenVorlage(String id) throws Exception {
		String rueckgabe = DatenbankverbindungVorlage.getVorlage(id);
		System.out.println(rueckgabe);
		return rueckgabe;
		/*
		 * String filepath = fullfilepath.split("[.]")[0]; String filetype = "." +
		 * fullfilepath.split("[.]")[1]; xmltodocx(filepath, filetype);
		 */
	}

	/*
	 * Je nachdem, ob der neue Pfad zur Word-Datei angegeben wurde oder nicht, 
	 * wird die Vorlage nur ausgelesen oder der Dateipfad in der Datenbank angepasst.
	 * 
	 * Wenn kein neuer Pfad angegebe wurde, gehe ich davon aus, 
	 * dass der User nicht weiß, wo die zu ändernde Vorlage liegt.
	 * Deshalb wird dabei nur nach einer Vorlage mit der angegebenen ID gesucht.
	 * 
	 * Wenn ein Dateipfad angegeben ist, wird nach dem Datenbankeintrag gesucht und 
	 * bei dem gefundenen Eintrag der Dateipfad geändert
	 */
	public static void bearbeitenVorlage(String id, String newPath) throws Exception {
		if (newPath.trim().equals("")) {
			ansehenVorlage(id);
		} else {
			String rueckgabe = DatenbankverbindungVorlage.getVorlage(id);
			if (rueckgabe == "Eintrag nicht vorhanden") {
				System.out.println(rueckgabe);
			}else {
				DatenbankverbindungVorlage.changeVorlage(id, newPath);
			}
		}
	}

	/*
	 * Es wird eine Vorlage, also der Datenbankeintrag und die Datei gelöscht.
	 * Dazu wird erst geprüft, ob der Eintrag vorhanden ist.
	 * Wenn dies der Fall ist, wird die Word-Datei und danach der Datenbankeintrag gelöscht 
	 */
	public static void loeschenVorlage(String id) {
		String rueckgabe = DatenbankverbindungVorlage.getVorlage(id);
		if (rueckgabe == "Eintrag nicht vorhanden") {
			System.out.println(rueckgabe);
		}else {
			File vorlage = new File(rueckgabe);
			if (vorlage.delete()) {
				System.out.println(vorlage.getName() + " deleted");
				DatenbankverbindungVorlage.deleteVorlage(id);
			}else {
				System.out.println("failed"); 
			}
			/*
			 * try { Files.deleteIfExists(Paths.get(rueckgabe)); } catch(NoSuchFileException
			 * e) { System.out.println("No such file/directory exists"); }
			 * catch(DirectoryNotEmptyException e) {
			 * System.out.println("Directory is not empty."); } catch(IOException e) {
			 * System.out.println("Invalid permissions."); }
			 * 
			 * System.out.println("Deletion successful.");
			 */
		}
	}

	/*
	 * Es werden die Platzhalter und die Textbausteine durch richtige Werte ersetzt.
	 * Dazu wird als erstes der Dateipfad der Vorlage ausgewählt.
	 * Dann werden die Platzhalter, die momentan noch reiner Text sind, in Content-Control-Felder umgewandelt
	 * Danach werden die Content-Control-Felder durch die Werte in der angegeben XML-Datei ersetzt
	 */
	public static void replacePlaceholder(String id, String xmlfilepath) throws Exception {

		String docxfilepath = DatenbankverbindungVorlage.getVorlage(id);
		String outputfilepath = docxfilepath.split("[.]")[0] + "_out." + docxfilepath.split("[.]")[1];
		System.out.println(docxfilepath);
		System.out.println(outputfilepath);
		replace.FromVariableReplacement.main(docxfilepath, outputfilepath);

		WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(outputfilepath)); 
		FileInputStream xmlStream = new FileInputStream(new	File(xmlfilepath));

		// Do the binding: 
		// FLAG_NONE means that all the steps of the binding will be done, 
		// otherwise you could pass a combination of the following flags: 
		// FLAG_BIND_INSERT_XML: inject the passed XML into the document 
		// FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope handling) 
		// FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the content remains) 
		// FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document
		
		Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE); 
		// If a document doesn't include the Opendope definitions, eg. the XPathPart, 
		// then the only thing you can do is insert the xml 
		// the example document binding-simple.docx doesn't have an XPathPart.... 
		// Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML); 
		Docx4J.save(wordMLPackage, new File(outputfilepath), Docx4J.FLAG_NONE); 
		System.out.println("Saved: " + outputfilepath);
	}

	/*
	 * Umwandeln des Word-Dokuments in eine XML-Datei Als erstes werden der
	 * Inputpfad und der Outputpfad zusammengebaut. Dabei heißt der Outputpfad
	 * genauso wie der Inputpfad, bis auf den Dateityp. Anschließend wird das
	 * Word-Dokument geladen und durch die save()-Methode in eine Flat OPC-XML
	 * umgewandelt. Diese wird dann in dem Outputpfad abgelegt und der Pfad
	 * zurückgegeben.
	 */
	/*
	public static String docxtoxml(String inputpath, String filetype) throws Exception {
		String inputfilepath = inputpath + filetype;
		String outputfilepath = inputpath + ".xml";

		try {
			WordprocessingMLPackage wmlPackage = Docx4J.load(new java.io.File(inputfilepath));
			Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_FLAT_XML);
			System.out.println("Saved: " + outputfilepath);
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
		return outputfilepath;
	}
*/
	/*
	 * Umwandeln der XML-Datei in ein Word-Dokument Als erstes werden wieder die
	 * Pfade zusammengestellt. Dann wird die XML-Datei geladen und als Word-Dokument
	 * gespeichert. Dieses wird in dem Outputpfad abgelegt
	 */
	/*
	public static void xmltodocx(String inputpath, String filetype) throws Exception {
		String inputfilepath = inputpath + filetype;
		String outputfilepath = inputpath + ".docx";

		try {
			WordprocessingMLPackage wmlPackage = Docx4J.load(new java.io.File(inputfilepath));
			Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_ZIP_FILE);
			System.out.println("Saved: " + outputfilepath);
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
	}
	*/

}
