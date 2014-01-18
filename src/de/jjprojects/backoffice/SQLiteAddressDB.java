package de.jjprojects.backoffice;

import java.io.File;
import java.util.logging.Logger;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.ISqlJetTransaction;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

import de.jjprojects.lotus.addresses.AddrComponent;
/**
 * create and fill a SQLITE database from de.jjprojects.lotus.addresses.AddrComponent 
 * 
 * @see de.jjprojects.lotus.addresses.AddrComponent
 * @author Joerg Juenger; Copyright 2012 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 */
public class SQLiteAddressDB {

   static Logger naLogger = Logger.getLogger("de.jjprojects.backoffice");

   private static final String TABLE_NAME = "addresses";
   private static final String VERSION_NAME = "version";

   private static final String FIRST_NAME_FIELD = "first_name";
   private static final String SIR_NAME_FIELD = "sir_name";
   private static final String COMPANY_FIELD = "company";
   private static final String CITY_FIELD = "city";
   private static final String PHONE_FIELD = "phone";
   private static final String MOBILE_FIELD = "mobile";
   private static final String EMAIL_FIELD = "email";
   private static final String DHKEY_FIELD = "dhkey";
   private static final String COMPKEY_FIELD = "companykey";
   private static final String COUNTRY_FIELD = "country";
   private static final String ZIP_FIELD = "zip";
   private static final String STREET_FIELD = "street";


   private static final String FULL_NAME_INDEX = "full_name_index";
   private static final String CITY_INDEX = "city_index";
   private static final String COMPANY_INDEX = "company_index";


	public void createDB (String dbName, String versionStr) {
		try {
			File dbFile = new File(dbName);
			dbFile.delete();

			// create database, table and two indices:
			db = SqlJetDb.open(dbFile, true);
			assert null != db : "db creation failed (db == null)!";

			// set DB option that have to be set before running any transactions: 
			db.getOptions().setAutovacuum(true);
			// set DB option that have to be set in a transaction: 
			db.runTransaction(new ISqlJetTransaction() {
				public Object run(SqlJetDb db) throws SqlJetException {
					db.getOptions().setUserVersion(1);
					return true;
				}
			}, SqlJetTransactionMode.WRITE);

			db.beginTransaction(SqlJetTransactionMode.WRITE);
			try {            
				String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" 
													+ DHKEY_FIELD + " TEXT NOT NULL PRIMARY KEY , " 
													+ SIR_NAME_FIELD + " TEXT NOT NULL, " 
													+ FIRST_NAME_FIELD + " TEXT NOT NULL, " 
													+ COMPANY_FIELD + " TEXT NOT NULL, " 
													+ PHONE_FIELD + " TEXT NOT NULL, " 
													+ MOBILE_FIELD + " TEXT NOT NULL, " 
													+ CITY_FIELD + " TEXT NOT NULL, " 
													+ EMAIL_FIELD + " TEXT NOT NULL, " 
													+ COMPKEY_FIELD + " TEXT NOT NULL, "
													+ COUNTRY_FIELD + " TEXT NOT NULL, " 
													+ ZIP_FIELD + " TEXT NOT NULL, " 
													+ STREET_FIELD + " TEXT NOT NULL)";

				String createFirstNameIndexQuery = "CREATE INDEX " + FULL_NAME_INDEX + " ON " + TABLE_NAME + "(" +  FIRST_NAME_FIELD + "," + SIR_NAME_FIELD + ")";
				String createCityIndexQuery = "CREATE INDEX " + CITY_INDEX + " ON " + TABLE_NAME + "(" +  CITY_FIELD + ")";
				String createCompanyIndexQuery = "CREATE INDEX " + COMPANY_INDEX + " ON " + TABLE_NAME + "(" +  COMPANY_FIELD + ")";
				String createVersionTableQuery = "CREATE TABLE version (version TEXT NOT NULL PRIMARY KEY)";

				naLogger.fine(">DB schema queries:");
				naLogger.fine(createTableQuery);
				naLogger.fine(createFirstNameIndexQuery);
				naLogger.fine(createCityIndexQuery);
				naLogger.fine(createCompanyIndexQuery);

				db.createTable(createTableQuery);
				db.createIndex(createFirstNameIndexQuery);
				db.createIndex(createCityIndexQuery);
				db.createIndex(createCompanyIndexQuery);

            db.createTable(createVersionTableQuery);
            boolean versionIsSet = this.insertVersion(versionStr);
            if (versionIsSet)
                naLogger.info("Version: " + versionStr);
            else
                naLogger.info("Version could not be set in database");
			} finally {
				db.commit();
			}
			// close DB and open it again (as part of example code)

			db.close();        
			db = SqlJetDb.open(dbFile, true);

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

   public boolean insertAddress (AddrComponent contact ) {
      if (null == db)
         return false;

      boolean result = false;
      try {
         // insert rows:
         db.beginTransaction(SqlJetTransactionMode.WRITE);
         try {
            ISqlJetTable table = db.getTable(TABLE_NAME);
            table.insert(contact.getAddrKey(), contact.getSirName(), contact.getFirstName(), 
                      contact.getCompany(), contact.getPhone(), contact.getMobile(), contact.getCity(), contact.getEMail(),
                      contact.getCompanyKey(), contact.getCountry(), contact.getZip(), contact.getStreet());
            result = true;
         } finally {
            db.commit();
         }
      } catch (SqlJetException e) {
         e.printStackTrace();
      }

      return result;
   }
   
   public boolean insertVersion (String versionStr) {
      if (null == db)
         return false;

      boolean result = false;
      try {
         // insert rows:
         db.beginTransaction(SqlJetTransactionMode.WRITE);
         try {
            ISqlJetTable table = db.getTable(VERSION_NAME);
            table.insert(versionStr);
            result = true;
         } finally {
            db.commit();
         }
      } catch (SqlJetException e) {
         e.printStackTrace();
      }

      return result;
   }
   
	protected void finalize () {
		try {
			if (null != db) 
				db.close();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			naLogger.finer("SQLiteAddressDB dies.");
			db = null;
		}
	}

	private SqlJetDb db = null;
}
