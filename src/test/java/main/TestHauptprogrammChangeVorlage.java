package main;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import main.datenbankmodel.Vorlagenschrank;
import main.service.VorlageService;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TestHauptprogrammChangeVorlage {

	/*
	 * Tests zum bearbeiten des Vorlagenpfads
	 * Test 1: Erfolgreiches Bearbeiten des Vorlagenpfads in der DB
	 * Test 2: Fehler beim bearbeiten, da Eintrag nicht vorhanden 
	 */
	
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
	
	@Autowired
	private static VorlagenschrankRepository vorlagenschrankRepo;
	
	@Autowired
	private VorlageService vorlagenService;
	
	@BeforeAll
	public static void addVorlage() { 
		vorlagenschrankRepo.deleteAll();
		vorlagenschrankRepo.save(new Vorlagenschrank(id, vorlagenpfad));
		try {
			Files.copy(oldfile.toPath(),newfile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void EditVorlagenpfad() {
		String expected = "Ändern erfolgreich.";
		assertEquals(expected, vorlagenService.bearbeitenVorlage(id, newPath));
	}
	
	@Test
	public void editVorlagenpfadWrongID() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag zum Ändern nicht vorhanden, bitte prüfen und eventuell hinzufügen.";
		assertEquals(expected, vorlagenService.bearbeitenVorlage(idnew, newPath));
	}
	
	@AfterAll
	public static void deleteEntries() {
		vorlagenschrankRepo.deleteAll();
		f.delete();
		
		
	}


}
