
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import datenbank.DatenbankverbindungVorlage;

public class Hauptprogramm {

	public static void main(String[] args) throws Exception {
		
		String id = "Vorlage1";
		String inputpath = "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage1_Bescheid_BZST";
		String filetype = ".docx";
		String outputpath = docxtoxml(inputpath, filetype);
//		xmltodocx("C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\test.xml", 
//				  "C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\test.docx");
		
		DatenbankverbindungVorlage db = new DatenbankverbindungVorlage();
		db.addVorlage(id, outputpath);
		
	}

	public static String docxtoxml(String inputpath, String filetype) throws Exception {
		boolean save = true;
		
		String inputfilepath = inputpath+filetype;
		String outputfilepath = inputpath+".xml";
		
		WordprocessingMLPackage wmlPackage = Docx4J.load(new java.io.File(inputfilepath));

		try {
			if (save) {
				Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_FLAT_XML);
				System.out.println("Saved: " + outputfilepath);
			} else {
				Docx4J.save(wmlPackage, System.out, Docx4J.FLAG_SAVE_FLAT_XML);
			}
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
		return outputfilepath;
	}
	
	public static void xmltodocx(String inputfilepath, String outputfilepath) throws Exception{
		
		try {
			// First, load Flat OPC Package from file
			WordprocessingMLPackage wmlPackage = Docx4J.load(new File(inputfilepath));
			Docx4J.save(wmlPackage, new File(outputfilepath), Docx4J.FLAG_SAVE_ZIP_FILE);
			System.out.println("Saved: " + outputfilepath);
			
		} catch (Exception exc) {
			exc.printStackTrace();
			throw new RuntimeException(exc);
		}
		
	}

}
