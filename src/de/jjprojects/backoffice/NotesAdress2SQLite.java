package de.jjprojects.backoffice;

import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Session;
import de.jjprojects.lotus.LotusConnector;
import de.jjprojects.lotus.addresses.Data_Contact;

/**
 * utility to read dochouse addresses and insert them into a SQLite database (e.g. to take them on an iPhone)
 * 
 * @author Joerg Juenger, JJ-Projects Joerg Juenger, Copyright 2012
 */
public class NotesAdress2SQLite extends LotusConnector {

	static Logger naLogger = Logger.getLogger("ag.fast.backoffice");

	/**
	 * main function to execute the application code
	 * @param argv array of command line arguments
	 * 		- optional properties file for Lotus connection data 
	 */
	public static void main(String argv[]) {

			naLogger.setLevel(Level.FINEST);

			NotesAdress2SQLite nA = new NotesAdress2SQLite();
			nA.mainLoop(argv);
	}

	/**
	 * runNotes - main functions of Notes related classes
	 */
	public void runNotes() {
		naLogger.entering("ag.fast.backoffice.DHAdress2SQLite", "runNotes");

		try {
			Session session = getSession();
			assert null != session : "session is invalid (null)";

			SQLiteAddressDB sqlDb = new SQLiteAddressDB ();
			assert null != sqlDb : "sqlDb is invalid (null)";
			
			sqlDb.createDB("Addresses.sqlite");
			
			Database dbAddr = getLotusDatabase ("Adressen");
			assert null != dbAddr : "Database is invalid (null)";
			boolean isOpen = dbAddr.open();
			assert true == isOpen : "database not open yet !";
			
			// start iteration through the dochouse address db and push them into the SQLite db
			DocumentCollection dcAddr = dbAddr.getAllDocuments();
			assert null != dcAddr : "document collection invalid (null)";

			Document doc = dcAddr.getFirstDocument();
			Data_Contact scContact = new Data_Contact ();
			assert null != scContact : "scContact is invalid (null)";
			int count = 0;
			while (null != doc && count < 200000) {
				String strKey = doc.getItemValueString("DocID");
				if (null != strKey) {
					scContact.dataFromLotusDoc (doc, this.getProperties());
					naLogger.info(scContact.toString());
					
					sqlDb.insertAddress(scContact);
				}
				doc.recycle();
				doc = dcAddr.getNextDocument();
				count++;
			} // end while (null != doc)
			
			dcAddr.recycle();
			dbAddr.recycle();
		} catch(Exception e) {

			e.printStackTrace();

		}	

		naLogger.exiting("ag.fast.backoffice.DHAdress2SQLite", "runNotes");
	}

}
