/**
 * 
 */
package de.jjprojects.lotus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.DbDirectory;
import lotus.domino.NotesException;
import lotus.domino.NotesFactory;
import lotus.domino.Session;

/**
 * General service provider calss for Lotus Notes Connections including service to open databases
 * @author Joerg Juenger; Copyright 2012 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 *
 */
public abstract class LotusConnector {

	static Logger naLogger = Logger.getLogger("de.jjprojects.lotus");

	private Properties props = new Properties ();

	/**
	 * the active Notes session
	 * @see lotus.domino.Session
	 */
	private Session sess = null;


	/**
	 * Class to connect to a Lotus Notes server; it creates a notes Session and opens database connections if needbe
	 * @author Joerg Juenger, JJ-Projects Joerg Juenger, Copyright 2012
	 */
	public LotusConnector() {
	}

	/**
	 * function to find the given database in the Notes Server
	 * @param dbName the database to be opened
	 * @return a reference to the database, NULL if no database found
	 */
	public Database getLotusDatabase(String dbName) {
		naLogger.entering("de.jjprojects.lotus.LotusConnector", "getDatabase");
		Database dbObj = null;
		String host = props.getProperty("lotushost");
		assert (null != host);
		assert (null != sess);
		try {
			DbDirectory dbDir = sess.getDbDirectory(host);
			assert (null != dbDir);
			naLogger.info("DBDir : " + dbDir.getName() + "\n ");
			dbObj = dbDir.getFirstDatabase(DbDirectory.DATABASE);
			while (null != dbObj) {
				// Operational code goes here
				if (dbObj.getTitle().equals(dbName))
					break;
				dbObj = dbDir.getNextDatabase();
			}

		} catch (NotesException e) {
			e.printStackTrace();
			naLogger.logp(Level.ALL, "LotusConnector", "getLotusDatabase", e.getMessage(), e);
		}
		naLogger.exiting("de.jjprojects.sc.lotus.LotusConnector", "getDatabase");

		return dbObj;
	}


	protected void finalize () {
		try {
			if (null != sess) {
				// release the session object
				sess.recycle();
				sess = null;
			}
			super.finalize();

		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			naLogger.finer("LotusConnector dies.");
			sess = null;
			props = null;
		}
	}


	protected Properties getProperties () {
		return props;
	}

	protected Session getSession () {
		return sess;
	}

	/**
	 * Main loop to be called in your applications main to execute the application code; <br>
	 * A valid Lotus Session is created at your service as well.<br>
	 * Afterwards runNotes is called, so make sure you implement that. 
	 * @param argv array of command line arguments
	 * 		- optional properties file for Lotus connection data 
	 */
	protected void mainLoop(String argv[]) {
		try {

			String file = null;
			if (1 <= argv.length)
				file = argv[0];
			this.loadProps(file);

			this.createSession(null);

			this.runNotes();
		} catch (Throwable eThrow) {
			// make sure we get the prints from the assertions in debug mode
			eThrow.printStackTrace();
			naLogger.logp(Level.ALL, "LotusConnector", "mainLoop", eThrow.getMessage(), eThrow);
		}
	}
	protected abstract void runNotes () ;

	private void createSession (Properties notesProps) {
		naLogger.entering("de.jjprojects.lotus.LotusConnector", "createSession");
		try {
			if (null != notesProps)
				props = notesProps;

			// create a session ...
			String host = props.getProperty("lotushost");
			String port = props.getProperty("lotusport");
			String user = props.getProperty("lotususer");
			String pwd = props.getProperty("lotuspwd");

			sess = NotesFactory.createSession(host + ":" + port, user, pwd);

		} catch (Exception e) {
			e.printStackTrace();
			naLogger.logp(Level.ALL, "LotusConnector", "createSession", e.getMessage(), e);
		} 
		naLogger.exiting("de.jjprojects.lotus.LotusConnector", "createSession");
	}

	private void loadProps (String propsFile) {
		boolean setDefaults = true;
		if (null != propsFile) {
			try {
				props.loadFromXML(new FileInputStream (propsFile));
				setDefaults = false;
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (setDefaults) {
			props.setProperty("lotushost", "127.0.0.1");
			// notesProps.setProperty("lotushost", "muc-domino-adm.fast-lta.intra");
			// protected String host = "muc-tvserver.fast-lta.intra";
			props.setProperty("lotusport", "63148");
			props.setProperty("lotususer", "user");
			props.setProperty("lotuspwd", "password");

		}
	}

}
