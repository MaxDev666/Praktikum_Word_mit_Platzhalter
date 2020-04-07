package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

import javax.persistence.NoResultException;
import javax.xml.bind.JAXBContext;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
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
			String rueckgabeAusHinzufuegen = hinzufuegenVorlage(args[1]);
			System.out.println(rueckgabeAusHinzufuegen);
			break;
		case "get":
			String rueckgabeAusAnsehen = ansehenVorlage(args[1]);
			System.out.println(rueckgabeAusAnsehen);
			break;
		case "edit":
			String rueckgabeAusBearbeiten = bearbeitenVorlage(args[1], args[2]);
			System.out.println(rueckgabeAusBearbeiten);
			break;
		case "delete":
			String rueckgabeAusLoeschen = loeschenVorlage(args[1]);
			System.out.println(rueckgabeAusLoeschen);
			break;
		case "replace":
			String rueckgabeAusReplace = replacePlaceholder(args[1], args[2]);
			System.out.println(rueckgabeAusReplace);
			break;
		default:
			String noOption = "Es wurde keine Option ausgewählt";
			System.out.println(noOption);
		}
	}

	/*
	 * Es wird eine Vorlage zur Datenbank hinzugefügt. Anhand des Dateipfads wird
	 * der Dateiname ermittelt, der als ID verwendet wird. Anschließend werden der
	 * Dateipfad sowie die neue ID in der Vorlagen-Tabelle gespeichert.
	 */
	public static String hinzufuegenVorlage(String documentpath) {
		String lesenEintragErfolgreich = null;
		String hinzufügenEintragErfolgreich = null;

		File f = new File(documentpath);
		if (f.exists()) {
			// ID aus dem Dateinamen bilden
			String[] splitpath = documentpath.split(Pattern.quote("\\"));
			String id = splitpath[splitpath.length - 1];
			id = id.split("[.]")[0];
			lesenEintragErfolgreich = ansehenVorlage(id);
			if (lesenEintragErfolgreich == "Eintrag nicht vorhanden.") {
				hinzufügenEintragErfolgreich = DatenbankverbindungVorlage.addVorlage(id, documentpath);
			} else {
				hinzufügenEintragErfolgreich = "Eintrag bereits vorhanden, bitte ändern und nicht hinzufügen.";
			}
		} else {
			hinzufügenEintragErfolgreich = "Datei wurde nicht gefunden.";
		}
		return hinzufügenEintragErfolgreich;
	}

	/*
	 * Es wird eine Vorlage angezeigt. Dazu wird mithilfe der ID die Datenbank
	 * gelesen und der Pfad der Word-Datei zurückgegeben. Wenn kein Eintrag gefunden
	 * wird, wird ein Fehler zurückgegeben
	 */
	public static String ansehenVorlage(String id) {
		String lesenEintragErfolgreich = null;
		try {
			lesenEintragErfolgreich = DatenbankverbindungVorlage.getVorlage(id);
		} catch (NoResultException e) {
			lesenEintragErfolgreich = "Eintrag nicht vorhanden.";
		}
		return lesenEintragErfolgreich;
	}

	/*
	 * Je nachdem, ob der neue Pfad zur Word-Datei angegeben wurde oder nicht, wird
	 * die Vorlage nur ausgelesen oder der Dateipfad in der Datenbank angepasst.
	 * 
	 * Wenn kein neuer Pfad angegebe wurde, gehe ich davon aus, dass der User nicht
	 * weiß, wo die zu ändernde Vorlage liegt. Deshalb wird dabei nur nach einer
	 * Vorlage mit der angegebenen ID gesucht.
	 * 
	 * Wenn ein Dateipfad angegeben ist, wird nach dem Datenbankeintrag gesucht und
	 * bei dem gefundenen Eintrag der Dateipfad geändert
	 */
	public static String bearbeitenVorlage(String id, String newPath) {
		String rueckgabeAnMainMethode = null;
		String lesenEintragErfolgreich = null;
		if (newPath.trim().equals("")) {
			rueckgabeAnMainMethode = ansehenVorlage(id);
		} else {
			File f = new File(newPath);
			if (f.exists()) {
				lesenEintragErfolgreich = ansehenVorlage(id);
				if (lesenEintragErfolgreich == "Eintrag nicht vorhanden.") {
					rueckgabeAnMainMethode = "Eintrag zum Ändern nicht vorhanden, bitte prüfen und eventuell hinzufügen.";
				} else {
					rueckgabeAnMainMethode = DatenbankverbindungVorlage.changeVorlage(id, newPath);
				}
			}else {
				rueckgabeAnMainMethode = "Datei unter geändertem Dateipfad nicht vorhanden.";
			}
		}

		return rueckgabeAnMainMethode;
	}

	/*
	 * Es wird eine Vorlage, also der Datenbankeintrag und die Datei gelöscht. Dazu
	 * wird erst geprüft, ob der Eintrag vorhanden ist. Wenn dies der Fall ist, wird
	 * die Word-Datei und danach der Datenbankeintrag gelöscht
	 */
	public static String loeschenVorlage(String id) {

		String lesenEintragErfolgreich = ansehenVorlage(id);
		String rueckgabeAnMainMethode = null;
		if (lesenEintragErfolgreich != "Eintrag nicht vorhanden.") {
			File vorlage = new File(lesenEintragErfolgreich);
			if (vorlage.delete()) {
				rueckgabeAnMainMethode = vorlage.getName() + " auf dem Dateisystem und in DB gelöscht.";
				DatenbankverbindungVorlage.deleteVorlage(id);
			} else {
				rueckgabeAnMainMethode = "Datei auf dem Dateisystem konnte nicht gelöscht werden.";
			}
		}else {
			rueckgabeAnMainMethode = lesenEintragErfolgreich;
		}
		return rueckgabeAnMainMethode;
	}

	/*
	 * Es werden die Platzhalter und die Textbausteine durch richtige Werte ersetzt.
	 * Dazu wird als erstes der Dateipfad der Vorlage ausgewählt. Dann werden die
	 * Platzhalter, die momentan noch reiner Text sind, in Content-Control-Felder
	 * umgewandelt Danach werden die Content-Control-Felder durch die Werte in der
	 * angegeben XML-Datei ersetzt
	 */
	public static String replacePlaceholder(String id, String xmlfilepath) {

		String fehler = null;
		WordprocessingMLPackage wordMLPackage = null;
		FileInputStream xmlStream = null;
		String docxfilepath = ansehenVorlage(id);
		File xmlFile = new File(xmlfilepath);
		
		if (docxfilepath == "Eintrag nicht vorhanden.") {
			fehler = docxfilepath;
			return fehler;
		}
		String outputfilepath = docxfilepath.split("[.]")[0] + "_out." + docxfilepath.split("[.]")[1];
		
		if (!xmlFile.exists()) {
			fehler = "XML-Datei nicht vorhanden.";
			return fehler;
		}
		
		try {
			replace.FromVariableReplacement.main(docxfilepath, outputfilepath);
		} catch (Exception e) {
			fehler = "Platzhalter konnten nicht in Content Control Felder umgewandelt werden." + e.toString();
		}

		try {
			wordMLPackage = Docx4J.load(new File(outputfilepath));
		} catch (Docx4JException e) {
			fehler = "Die Word-Vorlage konnte nicht geladen werden." + e.toString();
		}

		try {
			xmlStream = new FileInputStream(new File(xmlfilepath));
		} catch (FileNotFoundException e) {
			fehler = "Der XML-Stream wurde nicht gestartet" + e.toString();
		}

		// Do the binding:
		// FLAG_NONE means that all the steps of the binding will be done,
		// otherwise you could pass a combination of the following flags:
		// FLAG_BIND_INSERT_XML: inject the passed XML into the document
		// FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope
		// handling)
		// FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the
		// content remains)
		// FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document

		try {
			Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE);
		} catch (Docx4JException e) {
			fehler = "Die XML-Datei konnte nicht mit der Word-Vorlage gebunden werden." + e.toString();
		}
		// If a document doesn't include the Opendope definitions, eg. the XPathPart,
		// then the only thing you can do is insert the xml
		// the example document binding-simple.docx doesn't have an XPathPart....
		// Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML |
		// Docx4J.FLAG_BIND_BIND_XML);
		try {
			Docx4J.save(wordMLPackage, new File(outputfilepath), Docx4J.FLAG_NONE);
			fehler = "Word-Dokument erfolgreich gespeichert.";
		} catch (Docx4JException e) {
			fehler = "Das fertige Word-Dokument konnte nicht gespeichert werden." + e.toString();
		}
		return fehler;
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
	 * public static String docxtoxml(String inputpath, String filetype) throws
	 * Exception { String inputfilepath = inputpath + filetype; String
	 * outputfilepath = inputpath + ".xml";
	 * 
	 * try { WordprocessingMLPackage wmlPackage = Docx4J.load(new
	 * java.io.File(inputfilepath)); Docx4J.save(wmlPackage, new
	 * File(outputfilepath), Docx4J.FLAG_SAVE_FLAT_XML);
	 * System.out.println("Saved: " + outputfilepath); } catch (Exception exc) {
	 * exc.printStackTrace(); throw new RuntimeException(exc); } return
	 * outputfilepath; }
	 */
	/*
	 * Umwandeln der XML-Datei in ein Word-Dokument Als erstes werden wieder die
	 * Pfade zusammengestellt. Dann wird die XML-Datei geladen und als Word-Dokument
	 * gespeichert. Dieses wird in dem Outputpfad abgelegt
	 */
	/*
	 * public static void xmltodocx(String inputpath, String filetype) throws
	 * Exception { String inputfilepath = inputpath + filetype; String
	 * outputfilepath = inputpath + ".docx";
	 * 
	 * try { WordprocessingMLPackage wmlPackage = Docx4J.load(new
	 * java.io.File(inputfilepath)); Docx4J.save(wmlPackage, new
	 * File(outputfilepath), Docx4J.FLAG_SAVE_ZIP_FILE);
	 * System.out.println("Saved: " + outputfilepath); } catch (Exception exc) {
	 * exc.printStackTrace(); throw new RuntimeException(exc); } }
	 */

}
