
import java.io.File;

import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import datenbank.DatenbankverbindungVorlage;

public class Hauptprogramm {

	public static void main(String[] args) throws Exception {
		//C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage2_Bescheid_BZST.docx
		switch(args[0]) {
		case "add":
			hinzufuegenVorlage(args[1], args[2]);
			break;
		case "get":
			ansehenVorlage(args[1]);
			break;
		}
	}

	public static void hinzufuegenVorlage(String id, String fullfilepath) throws Exception {
		String filepath = fullfilepath.split("[.]")[0];
		String filetype = "."+fullfilepath.split("[.]")[1];		
		String outputpath = docxtoxml(filepath, filetype);
		DatenbankverbindungVorlage db = new DatenbankverbindungVorlage();
		db.addVorlage(id, outputpath);
	}
	
	public static void ansehenVorlage(String id) throws Exception {
		DatenbankverbindungVorlage db = new DatenbankverbindungVorlage();
		String fullfilepath = db.getVorlage(id);
		String filepath = fullfilepath.split("[.]")[0];
		String filetype = "."+fullfilepath.split("[.]")[1];
		xmltodocx(filepath, filetype);
	}
	
	public static String docxtoxml(String inputpath, String filetype) throws Exception {
		String inputfilepath = inputpath + filetype;
		String outputfilepath = inputpath + ".xml";
		
		try {
			WordprocessingMLPackage wmlPackage = Docx4J.load(new java.io.File(inputfilepath));
			Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_FLAT_XML);
			System.out.println("Saved: " + outputfilepath);
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
		return outputfilepath;
	}

	public static void xmltodocx(String inputpath, String filetype) throws Exception {
		String inputfilepath = inputpath + filetype;
		String outputfilepath = inputpath + ".docx";
		
		try {
			WordprocessingMLPackage wmlPackage = Docx4J.load(new java.io.File(inputfilepath));
			Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_ZIP_FILE);
			System.out.println("Saved: " + outputfilepath);
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
	}

}
