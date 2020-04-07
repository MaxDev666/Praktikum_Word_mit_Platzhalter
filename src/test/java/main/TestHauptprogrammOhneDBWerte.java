package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datenbank.DatenbankverbindungVorlage;

class TestHauptprogrammOhneDBWerte {

	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
	@BeforeAll
	public static void setup() {
		DatenbankverbindungVorlage.deleteVorlagen();
		try {
			Files.copy(oldfile.toPath(),newfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addVorlageToDBFirstTime() {
		String rueckgabe = Hauptprogramm.hinzufuegenVorlage(
				"C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx");
		String expected = "Hinzufügen erfolgreich C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
		assertNotNull(rueckgabe);
		assertEquals(expected, rueckgabe);
	}

	@Test
	public void addVorlageToDBNotExistingFile() {
		String rueckgabe = Hauptprogramm.hinzufuegenVorlage(
				"C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST_new.docx");
		String expected = "Datei wurde nicht gefunden.";
		assertNotNull(rueckgabe);
		assertEquals(expected, rueckgabe);
	}

	@AfterAll
	public static void deleteEntries() {
		DatenbankverbindungVorlage.deleteVorlagen();
		newfile.delete();
	}

}
