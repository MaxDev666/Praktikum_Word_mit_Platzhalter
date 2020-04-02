package main;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import main.Hauptprogramm;

class Hauptprogramm_Test {

	@Test
	void testAddVorlage() throws Exception {
		//assertTrue(Hauptprogramm.hinzufuegenVorlage("C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage2_Bescheid_BZST_out.docx")=="");
		assertTrue(Hauptprogramm.hinzufuegenVorlage("C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST.docx")=="Eintrag vorhanden; bitte bearbeiten, nicht hinzufügen");
	}

}


