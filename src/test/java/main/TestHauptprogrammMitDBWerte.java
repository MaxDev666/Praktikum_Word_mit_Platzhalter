package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import org.docx4j.openpackaging.parts.relationships.AlteredParts.Alteration;
import org.docx4j.org.apache.poi.OldFileFormatException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import datenbank.DatenbankverbindungVorlage;

class TestHauptprogrammMitDBWerte {

	static String id = "Vorlage5_Bescheid_BZST";
	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
	static String replaceFilepath = vorlagenpfad.split("[.]")[0] + "_out." + vorlagenpfad.split("[.]")[1];
	static File replacefile = new File(replaceFilepath);
	
	@BeforeAll
	public static void addVorlage() { 
		DatenbankverbindungVorlage.deleteVorlagen();
		DatenbankverbindungVorlage.addVorlage(id, vorlagenpfad);
		try {
			Files.copy(oldfile.toPath(),newfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void addVorlageToDBSecondTime() {
		String rueckgabe = Hauptprogramm.hinzufuegenVorlage("C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx");
		String expected = "Eintrag bereits vorhanden, bitte ändern und nicht hinzufügen.";
		assertNotNull(rueckgabe);
		assertEquals(expected, rueckgabe);
	}
	
	@Test
	public void getVorlagenpfad() {
		String expected = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
		assertEquals(expected, Hauptprogramm.ansehenVorlage(id));
	}
	
	@Test
	public void getVorlagenpfadFileNotExisting() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, Hauptprogramm.ansehenVorlage(idnew));
	}
	
	@Test
	public void getVorlagenpfadToEdit() {
		String expected = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
		assertEquals(expected, Hauptprogramm.bearbeitenVorlage(id, ""));
	}
	
	@Test
	public void getVorlagenpfadToEditNoEntity() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, Hauptprogramm.bearbeitenVorlage(idnew, ""));
	}
	
	@Test
	public void editVorlagenpfadNoNewFileExisting() {
		String newPath = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST_new.docx";
		String expected = "Datei unter geändertem Dateipfad nicht vorhanden.";
		assertEquals(expected, Hauptprogramm.bearbeitenVorlage(id, newPath));
	}
	
	@Test
	public void deleteVorlageWrongID() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, Hauptprogramm.loeschenVorlage(idnew));
	}
	
	@Test
	public void replacePlaceholder() {
		String xmlPfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST_out_a.xml";
		String expected = "Word-Dokument erfolgreich gespeichert.";
		assertEquals(expected, Hauptprogramm.replacePlaceholder(id, xmlPfad));
	}
	
	@Test
	public void replacePlaceholderWrongID() {
		String xmlPfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST_out_a.xml";
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, Hauptprogramm.replacePlaceholder(idnew, xmlPfad));	
	}
	
	@Test
	public void replacePlaceholderWrongXML() {
		String xmlPfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.xml";
		String expected = "XML-Datei nicht vorhanden.";
		assertEquals(expected, Hauptprogramm.replacePlaceholder(id, xmlPfad));	
	}
	
	@AfterAll
	public static void deleteEntries() {
		DatenbankverbindungVorlage.deleteVorlage(id);
		newfile.delete();
		replacefile.delete();
	}

}
