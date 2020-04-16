package main.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.regex.Pattern;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import main.dao.TextbausteinRepository;
import main.dao.VorlagenschrankRepository;
import main.datenbankmodel.Textbaustein;
import main.datenbankmodel.Vorlagenschrank;

@Service
public class VorlageService {

	@Autowired
	private VorlagenschrankRepository vorlagenschrankRepo;
	
	@Autowired
	private TextbausteinRepository textbausteinRepo;

	/*
	 * Es wird eine Vorlage zur Datenbank hinzugefügt. Anhand des Dateipfads wird
	 * der Dateiname ermittelt, der als ID verwendet wird. Anschließend werden der
	 * Dateipfad sowie die neue ID in der Vorlagen-Tabelle gespeichert.
	 */
	public String hinzufuegenVorlage(String documentpath) {
		String hinzufügenEintragErfolgreich = null;
		File f = new File(documentpath);
		if (f.exists()) {
			// ID aus dem Dateinamen bilden
			String[] splitpath = documentpath.split(Pattern.quote("\\"));
			String id = splitpath[splitpath.length - 1];
			id = id.split("[.]")[0];
			String entryFound = ansehenVorlage(id);
			if (entryFound == "Eintrag nicht vorhanden.") {
				vorlagenschrankRepo.save(new Vorlagenschrank(id, documentpath));
				hinzufügenEintragErfolgreich = "Hinzufügen erfolgreich " + documentpath;
			}else {
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
	public String ansehenVorlage(String id) {
		Optional<Vorlagenschrank> vorlage = vorlagenschrankRepo.findById(id);

		if (vorlage.isPresent()) {
			return vorlage.get().getVorlagenpfad();
		} else {
			return "Eintrag nicht vorhanden.";
		}
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
	public String bearbeitenVorlage(String id, String newPath) {
		String rueckgabeAnMainMethode = null;
		if (newPath.trim().equals("")) {
			rueckgabeAnMainMethode = ansehenVorlage(id);
		} else {

			File f = new File(newPath);
			if (f.exists()) {
				Optional<Vorlagenschrank> vorlage = vorlagenschrankRepo.findById(id);
				if (vorlage.isPresent()) {
					Vorlagenschrank newVorlage = vorlage.get();
					newVorlage.setVorlagenpfad(newPath);
					vorlagenschrankRepo.save(newVorlage);
					rueckgabeAnMainMethode = "Ändern auf " + newPath + " erfolgreich.";
				} else {
					rueckgabeAnMainMethode = "Eintrag zum Ändern nicht vorhanden, bitte prüfen und eventuell hinzufügen.";
				}
			} else {
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
	public String loeschenVorlage(String id) {
		String vorlagenpath = null;
		String rueckgabeAnMainMethode = null;
		vorlagenpath = ansehenVorlage(id);
		if (vorlagenpath != "Eintrag nicht vorhanden.") {
			File vorlage = new File(vorlagenpath);
			if (vorlage.delete()) {
				rueckgabeAnMainMethode = vorlage.getName() + " auf dem Dateisystem und in DB gelöscht.";
				vorlagenschrankRepo.deleteById(id);
			} else {
				rueckgabeAnMainMethode = "Datei auf dem Dateisystem konnte nicht gelöscht werden.";
			}
		}else {
			rueckgabeAnMainMethode = vorlagenpath;
		}
		return rueckgabeAnMainMethode;
	}
	
	
	/*
	 * Es wird eine Vorlage zur Datenbank hinzugefügt. Anhand des Dateipfads wird
	 * der Dateiname ermittelt, der als ID verwendet wird. Anschließend werden der
	 * Dateipfad sowie die neue ID in der Vorlagen-Tabelle gespeichert.
	 */
	public String hinzufuegenTextbaustein(String id, String text) {
		String hinzufügenEintragErfolgreich = null;
			// ID aus dem Dateinamen bilden
			String entryFound = ansehenTextbaustein(id);
			if (entryFound == "Eintrag nicht vorhanden.") {
				textbausteinRepo.save(new Textbaustein(id, text));
				hinzufügenEintragErfolgreich = "Hinzufügen erfolgreich";
			}else {
				hinzufügenEintragErfolgreich = "Eintrag bereits vorhanden, bitte ändern und nicht hinzufügen.";
			}
		return hinzufügenEintragErfolgreich;
	}

	/*
	 * Es wird eine Vorlage angezeigt. Dazu wird mithilfe der ID die Datenbank
	 * gelesen und der Pfad der Word-Datei zurückgegeben. Wenn kein Eintrag gefunden
	 * wird, wird ein Fehler zurückgegeben
	 */
	public String ansehenTextbaustein(String id) {
		Optional<Textbaustein> textbaustein = textbausteinRepo.findById(id);

		if (textbaustein.isPresent()) {
			return textbaustein.get().getText();
		} else {
			return "Eintrag nicht vorhanden.";
		}
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
	public String bearbeitenTextbaustein(String id, String newText) {
		String rueckgabeAnMainMethode = null;
				Optional<Textbaustein> textbaustein = textbausteinRepo.findById(id);
				if (textbaustein.isPresent()) {
					Textbaustein newTextbaustein = textbaustein.get();
					newTextbaustein.setText(newText);
					textbausteinRepo.save(newTextbaustein);
					rueckgabeAnMainMethode = "Ändern erfolgreich.";
				} else {
					rueckgabeAnMainMethode = "Eintrag zum Ändern nicht vorhanden, bitte prüfen und eventuell hinzufügen.";
				}
		return rueckgabeAnMainMethode;
	}

	/*
	 * Es wird eine Vorlage, also der Datenbankeintrag und die Datei gelöscht. Dazu
	 * wird erst geprüft, ob der Eintrag vorhanden ist. Wenn dies der Fall ist, wird
	 * die Word-Datei und danach der Datenbankeintrag gelöscht
	 */
	public String loeschenTextbauStein(String id) {
		String textbaustein = null;
		String rueckgabeAnMainMethode = null;
		textbaustein = ansehenTextbaustein(id);
		if (textbaustein != "Eintrag nicht vorhanden.") {
				textbausteinRepo.deleteById(id);
				rueckgabeAnMainMethode = "Eintrag gelöscht";
		}else {
			rueckgabeAnMainMethode = textbaustein;
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
	public String replacePlaceholder(String id, String xmlfilepath) {
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
		}
		catch (Exception e) { 
			fehler = "Platzhalter konnten nicht in Content Control Felder umgewandelt werden." + e.toString(); 
		}
	  
		try { 
			wordMLPackage = Docx4J.load(new File(outputfilepath)); 
		} 
		catch (Docx4JException e) { 
			fehler = "Die Word-Vorlage konnte nicht geladen werden." + e.toString(); 
		}
	  
		try { 
			xmlStream = new FileInputStream(new File(xmlfilepath)); 
		} 
		catch (FileNotFoundException e) { 
			fehler = "Der XML-Stream wurde nicht gestartet" + e.toString(); 
		}
	  
		// Do the binding: 
		// FLAG_NONE means that all the steps of the binding will be done, 
		// otherwise you could pass a combination of the following flags: 
		// FLAG_BIND_INSERT_XML: inject the passed XML into the document 
		// FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope handling) 
		// FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the content remains) 
		// FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document
	  
		try { 
			Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE); 
		} 
		catch (Docx4JException e) { 
			fehler = "Die XML-Datei konnte nicht mit der Word-Vorlage gebunden werden." + e.toString(); 
		} 
		// If a document doesn't include the Opendope definitions, eg. the XPathPart, 
		// then the only thing you can do is insert the xml 
		// the example document binding-simple.docx doesn't have an XPathPart.... 
		// Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML); 
		
		try { 
			Docx4J.save(wordMLPackage, new File(outputfilepath), Docx4J.FLAG_NONE); 
			fehler = "Word-Dokument erfolgreich gespeichert."; 
		} catch (Docx4JException e) {
			fehler = "Das fertige Word-Dokument konnte nicht gespeichert werden." + e.toString(); 
		} 
		return fehler; 
	}
}
