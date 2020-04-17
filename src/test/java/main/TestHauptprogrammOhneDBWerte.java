package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import main.dao.VorlagenschrankRepository;
import main.service.VorlageService;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestHauptprogrammOhneDBWerte {
	
	/*
	 * Test des Hinzufügens einer Vorlage: 
	 * Test 1: Es wird getestet, ob der Dateipfad einer Datei korrekt in der Db hinzugefügt wurde 
	 * Test 2: Es wird getestet, ob das Programm die fehlende Datei auf dem Dateisystem erkennt
	 */
	
	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
	@Autowired
	private static VorlagenschrankRepository vorlagenschrankRepo;
	
	@Autowired
	private VorlageService vorlagenService; 
	
	@BeforeAll
	public static void setup() {
		vorlagenschrankRepo.deleteAll();
		try {
			Files.copy(oldfile.toPath(),newfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void addVorlageToDBFirstTime() {
		String rueckgabe = vorlagenService.hinzufuegenVorlage(
				"C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx");
		String expected = "Hinzufügen erfolgreich C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
		assertNotNull(rueckgabe);
		assertEquals(expected, rueckgabe);

	}

	@Test
	public void addVorlageToDBNotExistingFile() {
		String rueckgabe = vorlagenService.hinzufuegenVorlage(
				"C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST_new.docx");
		String expected = "Datei wurde nicht gefunden.";
		assertNotNull(rueckgabe);
		assertEquals(expected, rueckgabe);
	}

	@AfterAll
	public static void deleteEntries() {
		vorlagenschrankRepo.deleteAll();
		newfile.delete();
	}

}
