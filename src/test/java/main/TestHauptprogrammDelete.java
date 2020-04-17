package main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
public class TestHauptprogrammDelete {

	/*
	 * Test des Löschen einer Vorlage aus der DB
	 * Test 1: Löschen der Vorlage aus der Db erfolgreich
	 */
	static String id = "Vorlage5_Bescheid_BZST";
	static String vorlagenpfad = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage5_Bescheid_BZST.docx";
	static String old_path = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx";
	static File newfile = new File(vorlagenpfad);
	static File oldfile = new File(old_path);
	
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
	public void deleteVorlage() {
		String expected = "Vorlage5_Bescheid_BZST.docx auf dem Dateisystem und in DB gelöscht.";
		assertEquals(expected, vorlagenService.loeschenVorlage(id));
	}

}
