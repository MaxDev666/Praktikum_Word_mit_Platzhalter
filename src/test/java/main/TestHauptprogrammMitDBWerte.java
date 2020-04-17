package main;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
public class TestHauptprogrammMitDBWerte {

	/*
	 * Tests vom Hinzufügen, Lesen, Bearbeiten und Entfernen einer Vorlage sowei beim Ersetzen von Platzhaltern:
	 * Test 1: Fehler beim Hinzufügen einer schon vorhandenen Datei
	 * Test 2: erfolgreiches Lesen des Dateipfads
	 * Test 3: Fehler beim Lesen eines nicht vorhandenen Eintrags
	 * Test 4: erfolgreiches Herausfinden eines Dateipfads zum bearbeiten
	 * Test 5: Fehler beim Herausfinden eines Dateipfads zum bearbeiten, da Eintrag nicht vorhanden
	 * Test 6: Fehler beim Bearbeitungsversuch eines Dateipfads aufgrund des Fehlens der neuen Datei
	 * Test 7: Fehler beim Löschen eines nicht vorhandenen Eintrags
	 * Test 8: Erfolgreiches Ersetzen von Platzhaltern
	 * Test 9: Fehler beim Ersetzen von Platzhaltern, da DB-Eintrag der Vorlage nicht vorhanden
	 * Test 10: Fehler beim Ersetzen von Platzhaltern, da XMl-Datei nciht vorhanden
	 */
	
	static String id = "Vorlage5_Bescheid_BZST";
	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
	static String replaceFilepath = vorlagenpfad.split("[.]")[0] + "_out." + vorlagenpfad.split("[.]")[1];
	static File replacefile = new File(replaceFilepath);
	
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
	public void addVorlageToDBSecondTime() {
		String rueckgabe = vorlagenService.hinzufuegenVorlage("C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx");
		String expected = "Eintrag bereits vorhanden, bitte ändern und nicht hinzufügen.";
		assertNotNull(rueckgabe);
		assertEquals(expected, rueckgabe);
	}
	
	@Test
	public void getVorlagenpfad() {
		String expected = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
		assertEquals(expected, vorlagenService.ansehenVorlage(id));
	}
	
	@Test
	public void getVorlagenpfadFileNotExisting() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, vorlagenService.ansehenVorlage(idnew));
	}
	
	@Test
	public void getVorlagenpfadToEdit() {
		String expected = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
		assertEquals(expected, vorlagenService.bearbeitenVorlage(id, ""));
	}
	
	@Test
	public void getVorlagenpfadToEditNoEntity() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, vorlagenService.bearbeitenVorlage(idnew, ""));
	}
	
	@Test
	public void editVorlagenpfadNoNewFileExisting() {
		String newPath = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST_new.docx";
		String expected = "Datei unter geändertem Dateipfad nicht vorhanden.";
		assertEquals(expected, vorlagenService.bearbeitenVorlage(id, newPath));
	}
	
	@Test
	public void deleteVorlageWrongID() {
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, vorlagenService.loeschenVorlage(idnew));
	}
	
	@Test
	public void replacePlaceholder() {
		String xmlPfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST_out_a.xml";
		String expected = "Word-Dokument erfolgreich gespeichert.";
		assertEquals(expected, vorlagenService.replacePlaceholder(id, xmlPfad));
	}
	
	@Test
	public void replacePlaceholderWrongID() {
		String xmlPfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST_out_a.xml";
		String idnew = "Vorlage5_Bescheid_BZST_new";
		String expected = "Eintrag nicht vorhanden.";
		assertEquals(expected, vorlagenService.replacePlaceholder(idnew, xmlPfad));	
	}
	
	@Test
	public void replacePlaceholderWrongXML() {
		String xmlPfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.xml";
		String expected = "XML-Datei nicht vorhanden.";
		assertEquals(expected, vorlagenService.replacePlaceholder(id, xmlPfad));	
	}
	
	@AfterAll
	public static void deleteEntries() {
		vorlagenschrankRepo.deleteAll();
		newfile.delete();
		replacefile.delete();
	}

}
