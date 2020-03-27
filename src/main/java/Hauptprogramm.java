
import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;

import org.checkerframework.checker.units.qual.m;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import datenbank.DatenbankverbindungVorlage;

public class Hauptprogramm {

	public static JAXBContext context = org.docx4j.jaxb.Context.jc;

	public static void main(String[] args) throws Exception {
		// Zu Testzwecken, zum Kopieren des Testpfads
		// C:\\Users\\Maximilian Hett\\Desktop\\Studium\\ITZBund\\Praktikuminternes\\Test\\Vorlage2_Bescheid_BZST.docx

		// Je nach übergebenen Parametern wird eine entsprechende Methode zur
		// Verarbeitung der Vorlage ausgeführt
		switch (args[0]) {
		case "add":
			hinzufuegenVorlage(args[1], args[2]);
			break;
		case "get":
			ansehenVorlage(args[1]);
			break;
		case "replace":
			replacePlaceholder(args[1], args[2]);
		}

	}

	/*
	 * Es wird eine Vorlage zur Datenbank hinzugefügt Dazu wird der übergebene Pfad
	 * der Word-Datei nach dem eigentlichen Pfad sowie dem Dateityp aufgeteilt. Dann
	 * wird aus der Word-Datei eine XML-Datei erzeugt und die auf dem Dateisystem
	 * abgelegt. Der Pfad zu diese Datei wird anschließend in der Datenbank
	 * gespeichert
	 */
	public static void hinzufuegenVorlage(String id, String fullfilepath) throws Exception {
		/*
		 * String filepath = fullfilepath.split("[.]")[0]; String filetype = "." +
		 * fullfilepath.split("[.]")[1]; String outputpath = docxtoxml(filepath,
		 * filetype);
		 */
		DatenbankverbindungVorlage db = new DatenbankverbindungVorlage();
		db.addVorlage(id, fullfilepath);
	}

	/*
	 * Es wird eine Vorlage angezeigt. Dazu wird mithilfe der ID die Datenbank
	 * gelesen und der Pfad der XML-Datei nach Pfad und Dateityp aufgeteilt.
	 * Anschließend wird die XML-Datei in ein Word-Dokument umgewandelt.
	 */
	public static void ansehenVorlage(String id) throws Exception {
		DatenbankverbindungVorlage db = new DatenbankverbindungVorlage();
		String fullfilepath = db.getVorlage(id);
		/*
		 * String filepath = fullfilepath.split("[.]")[0]; String filetype = "." +
		 * fullfilepath.split("[.]")[1]; xmltodocx(filepath, filetype);
		 */
	}

	public static void replacePlaceholder(String id, String xmlfilepath) throws Exception {
		
		DatenbankverbindungVorlage db = new DatenbankverbindungVorlage();
		String docxfilepath = db.getVorlage(id);
		String outputfilepath = docxfilepath.split("[.]")[0]+"_out." + docxfilepath.split("[.]")[1];
		System.out.println(docxfilepath);
		System.out.println(outputfilepath);
		replace.FromVariableReplacement.main(docxfilepath, outputfilepath);

		WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(outputfilepath));
		FileInputStream xmlStream = new FileInputStream(new File(xmlfilepath));

		// Do the binding:
		// FLAG_NONE means that all the steps of the binding will be done,
		// otherwise you could pass a combination of the following flags:
		// FLAG_BIND_INSERT_XML: inject the passed XML into the document
		// FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope
		// handling)
		// FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the
		// content remains)
		// FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document

		Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE);
		// If a document doesn't include the Opendope definitions, eg. the XPathPart,
		// then the only thing you can do is insert the xml
		// the example document binding-simple.docx doesn't have an XPathPart....
		// Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_BIND_INSERT_XML |
		// Docx4J.FLAG_BIND_BIND_XML);
		Docx4J.save(wordMLPackage, new File(outputfilepath), Docx4J.FLAG_NONE);
		System.out.println("Saved: " + outputfilepath);
	}

	/*
	 * Umwandeln des Word-Dokuments in eine XML-Datei Als erstes werden der
	 * Inputpfad und der Outputpfad zusammengebaut. Dabei heißt der Outputpfad
	 * genauso wie der Inputpfad, bis auf den Dateityp. Anschließend wird das
	 * Word-Dokument geladen und durch die save()-Methode in eine Flat OPC-XML
	 * umgewandelt. Diese wird dann in dem Outputpfad abgelegt und der Pfad
	 * zurückgegeben.
	 */
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

	/*
	 * Umwandeln der XML-Datei in ein Word-Dokument Als erstes werden wieder die
	 * Pfade zusammengestellt. Dann wird die XML-Datei geladen und als Word-Dokument
	 * gespeichert. Dieses wird in dem Outputpfad abgelegt
	 */
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
