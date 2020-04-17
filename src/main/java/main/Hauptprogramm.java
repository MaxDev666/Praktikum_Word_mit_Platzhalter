package main;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;

import org.checkerframework.checker.i18nformatter.qual.I18nFormat;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import main.service.VorlageService;
import replace.ReplaceTextbaustein;

@SpringBootApplication
public class Hauptprogramm implements CommandLineRunner{

	@Autowired
	VorlageService vorlageService;
	
	public static List<String> texte = null; 
	public static List<String> idList = null;
	public static List<String> xPathList = null;
	
	public Hauptprogramm(VorlageService vorlageService) {
		texte = new ArrayList<String>();
		idList = new ArrayList<String>();
		xPathList = new ArrayList<String>();
	}

	public static void main(String[] args) {
		SpringApplication.run(Hauptprogramm.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Je nach übergebenen Parametern wird eine entsprechende Methode zur
		// Verarbeitung der Vorlage ausgeführt
 		switch (args[0]) {
		case "addVorlage":
			String rueckgabeAusHinzufuegen = vorlageService.hinzufuegenVorlage(args[1]);
			System.out.println(rueckgabeAusHinzufuegen);
			break;
		case "getVorlage":
			String rueckgabeAusAnsehen = vorlageService.ansehenVorlage(args[1]);
			System.out.println(rueckgabeAusAnsehen);
			break;
		case "editVorlage":
			String rueckgabeAusBearbeiten = vorlageService.bearbeitenVorlage(args[1], args[2]);
			System.out.println(rueckgabeAusBearbeiten);
			break;
		case "deleteVorlage":
			String rueckgabeAusLoeschen = vorlageService.loeschenVorlage(args[1]);
			System.out.println(rueckgabeAusLoeschen);
			break;
		case "addTextbaustein":
			String rueckgabeAusHinzufuegenText = vorlageService.hinzufuegenTextbaustein(args[1], args[2]);
			System.out.println(rueckgabeAusHinzufuegenText);
			break;
		case "getTextbaustein":
			String rueckgabeAusAnsehenText = vorlageService.ansehenTextbaustein(args[1]);
			System.out.println(rueckgabeAusAnsehenText);
			break;
		case "editTextbaustein":
			String rueckgabeAusBearbeitenText = vorlageService.bearbeitenTextbaustein(args[1], args[2]);
			System.out.println(rueckgabeAusBearbeitenText);
			break;
		case "deleteTextbaustein":
			String rueckgabeAusLoeschenText = vorlageService.loeschenTextbauStein(args[1]);
			System.out.println(rueckgabeAusLoeschenText);
			break;
		case "replace":
			String rueckgabeAusReplace = vorlageService.replacePlaceholder(args[1], args[2]);
			System.out.println(rueckgabeAusReplace);
			break;
		case "test":
			/*Diese Auswahl dient momentan dem Test des Textbaustein-Ersetzens. 
			Dabei wird erst der XPath-Pfad des XML-Elements und daraus die ID herausgefunden.
			Anschließend wird mit der ID die Datenbank gelesen, um den Text des Textbausteins herauszufinden
			Dann wird der Wert des Elements durch den Text ausgetauscht*/
			String filepath = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST_out.docx";
			WordprocessingMLPackage wordMLPackage  = WordprocessingMLPackage.load(new java.io.File(filepath));
			Boolean error = false;
			ReplaceTextbaustein.getXpaths(filepath, wordMLPackage);
			for (int i = 0; i < idList.size(); i++) {
				String id = idList.get(i);
				if (id.substring(0, 2).equals("tb")) {
					texte.add(vorlageService.ansehenTextbaustein(idList.get(i)));
				}
			}
			System.out.println(xPathList.toString());
			System.out.println(idList.toString());
			System.out.println(texte.toString());

			for (int i = 0; i < xPathList.size(); i++) {

				if (texte.get(i)!="Eintrag nicht vorhanden.") {
					//ReplaceTextbaustein.addTextbausteine(texte, xPathList, filepath, wordMLPackage);
					System.out.println("Textbaustein ersetzt");
				}else {
					System.out.println((i+1)+". Text nicht vorhanden");
				}
			}

			break;
		default:
			//Fehler, wenn keine passende Option gewählt wurde.
			String noOption = "Es wurde keine Option ausgewählt";
			System.out.println(noOption);
		}

	}
	
}