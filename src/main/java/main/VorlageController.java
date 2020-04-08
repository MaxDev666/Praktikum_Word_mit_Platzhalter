package main;

import org.apache.http.util.Args;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class VorlageController {

	@Autowired
	VorlageService vorlageService;
	
	// Je nach übergebenen Parametern wird eine entsprechende Methode zur
	// Verarbeitung der Vorlage ausgeführt
	public String hinzufuegenVorlage() {
		String rueckgabeAusHinzufuegen = vorlageService.hinzufuegenVorlage(documentpath);
		return rueckgabeAusHinzufuegen;
	}
	
	public String ansehenVorlage() {
		String rueckgabeAusAnsehen = vorlageService.ansehenVorlage(id);
		return rueckgabeAusAnsehen;
	}
	
	public String bearbeitenVorlageString() {
		String rueckgabeAusBearbeiten = vorlageService.bearbeitenVorlage(;, newPath)
		return rueckgabeAusBearbeiten;
	}
		
	switch (args[0]) {
			case "add":
				String rueckgabeAusHinzufuegen = VorlageService.System.out.println(rueckgabeAusHinzufuegen);
				break;
			case "get":
				String rueckgabeAusAnsehen = ansehenVorlage(args[1]);
				System.out.println(rueckgabeAusAnsehen);
				break;
			case "edit":
				String rueckgabeAusBearbeiten = bearbeitenVorlage(args[1], args[2]);
				System.out.println(rueckgabeAusBearbeiten);
				break;
			case "delete":
				String rueckgabeAusLoeschen = loeschenVorlage(args[1]);
				System.out.println(rueckgabeAusLoeschen);
				break;
			case "replace":
				String rueckgabeAusReplace = replacePlaceholder(args[1], args[2]);
				System.out.println(rueckgabeAusReplace);
				break;
			default:
				String noOption = "Es wurde keine Option ausgewählt";
				System.out.println(noOption);
			}
}
