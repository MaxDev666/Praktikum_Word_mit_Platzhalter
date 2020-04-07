package main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datenbank.DatenbankverbindungVorlage;

class TestHauptprogrammChangeVorlage {

	static String id = "Vorlage5_Bescheid_BZST";
	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
	static String newPath = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST_new.docx";
	static File f = new File(newPath);
	
	public TestHauptprogrammChangeVorlage() {
		newfile.renameTo(f);
	}
	
	
	@BeforeAll
	public static void addVorlage() { 
		DatenbankverbindungVorlage.addVorlage(id, vorlagenpfad);
		try {
			Files.copy(oldfile.toPath(),newfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void EditVorlagenpfad() {
		String expected = "Ändern erfolgreich.";
		assertEquals(expected, Hauptprogramm.bearbeitenVorlage(id, newPath));
	}
	
	@Test
	public void editVorlagenpfadWrongID() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag zum Ändern nicht vorhanden, bitte prüfen und eventuell hinzufügen.";
		assertEquals(expected, Hauptprogramm.bearbeitenVorlage(idnew, newPath));
	}
	
	@AfterAll
	public static void deleteEntries() {
		DatenbankverbindungVorlage.deleteVorlage(id);
		f.delete();
		
		
	}


}
