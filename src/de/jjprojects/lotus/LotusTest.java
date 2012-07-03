/**
 * 
 */
package de.jjprojects.lotus;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.jjprojects.lotus.dochouse.Data_Contact;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Session;

/**
 * Test class housing test code for apps which need to connect to a Lotus Notes server
 * @author Joerg Juenger, JJ-Projects Joerg Juenger, Copyright 2012
 */
public class LotusTest extends LotusConnector{


	static Logger naLogger = Logger.getLogger("de.jjprojects.lotus");

	/**
	 * main function to execute the application code
	 * @param argv array of command line arguments
	 * 		- optional properties file for Lotus connection data 
	 */
	public static void main(String argv[]) {

		naLogger.setLevel(Level.FINEST);

		LotusTest nA = new LotusTest();
		nA.mainLoop(argv);
	}


	/**
	 * runNotes - main functions of Notes related classes
	 */
	protected void runNotes() {
		naLogger.entering("de.jjprojects.lotus.LotusTest", "runNotes");

		// Lotus Domino Test Code has to be placed here
		try {

			Session session = getSession();
			assert null != session : "session is invalid (null)";

			Database db = getLotusDatabase ("Verträge");
			assert null != db : "Database is invalid (null)";

			db.open();

			
				DocumentCollection dColl = db.search("DocID = \"" + "97B7BFADD5112C43C125751C00001072" + "\"");
				Document doc = dColl.getFirstDocument();
				if (null != doc) {
					naLogger.info(doc.toString());
					Data_Contact adr = new Data_Contact ();
					adr.dataFromLotusDoc(doc, this.getProperties());
					naLogger.info(adr.toString());
				}
			
			
			// Vertrag HSK 8D8B9C9131378DC6C12575D30052F6C0
			// Vertrag Mönchengladbach 40CF633B49DA9F36C1257617005FEB40
			
			/*
			Database db = getLotusDatabase ("Dokumente");
			assert null != db : "Database is invalid (null)";

			DHDocument doc = new DHDocument (db);

			doc.appendHeaderInfo();
			doc.appendAuthor("Joerg Juenger");

			String nowStr =  util.getDateTime4SQL(new Date());

			doc.appendTitle("Test Document JJR" + nowStr);
			doc.createBody("Dies ist der Dokument Test von JJR " + nowStr);

			// JJs key 97B7BFADD5112C43C125751C00001072 
			doc.appendAdrKey("97B7BFADD5112C43C125751C00001072");
			doc.appendAddress("Juenger, Joerg", "JJ-Projects Joerg Juenger", "80686","Muenchen");

			if (doc.save())
				System.out.println("Document has been saved");

			else
				System.out.println("Unable to save document");
				 */
			db.recycle();

		} catch(Exception e) {

			e.printStackTrace();
			naLogger.logp(Level.ALL, "LotusTest", "runNotes", e.getMessage(), e);

		}	
		naLogger.exiting("de.jjprojects.lotus.LotusTest", "runNotes");
	}

}


