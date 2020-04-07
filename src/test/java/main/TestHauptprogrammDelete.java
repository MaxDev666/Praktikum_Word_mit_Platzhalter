package main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import datenbank.DatenbankverbindungVorlage;

class TestHauptprogrammDelete {

	static String id = "Vorlage5_Bescheid_BZST";
	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
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
	public void deleteVorlage() {
		String expected = "Vorlage5_Bescheid_BZST.docx auf dem Dateisystem und in DB gelöscht.";
		assertEquals(expected, Hauptprogramm.loeschenVorlage(id));
	}

}
