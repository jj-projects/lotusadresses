package de.jjprojects.backoffice;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import lotus.domino.Database;
import lotus.domino.Document;
import lotus.domino.DocumentCollection;
import lotus.domino.Session;
import de.jjprojects.lotus.LotusConnector;
import de.jjprojects.lotus.addresses.Data_Contact;

/**
 utility to read IBM Lotus Notes addresses and insert them into a SQLite database (e.g. to take them on an iPhone)

 @author Copyright (C) 2012  JJ-Projects Joerg Juenger <BR>

<pre>
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 </pre>
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

      // get the actual time as version and file name
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
      nowStr = sdf.format(cal.getTime());
      // put the output file path together
      String outputDir = this.getProperties().getProperty("output_location", "/var/www/htdocs");
      outputFile = outputDir + "/" + nowStr;

      try {
         Session session = getSession();
         assert null != session : "session is invalid (null)";

         SQLiteAddressDB sqlDb = new SQLiteAddressDB ();
         assert null != sqlDb : "sqlDb is invalid (null)";

         sqlDb.createDB(outputFile, nowStr);

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
         while (null != doc && count < 5) {
            // get address document from Notes
            String strKey = doc.getItemValueString("DocID");
            if (null != strKey) {
               scContact.dataFromLotusDoc (doc, this.getProperties());
               naLogger.info(scContact.toString());

               // push contact data into sqlite
               sqlDb.insertAddress(scContact);
            }
            doc.recycle();
            doc = dcAddr.getNextDocument();
            count++;
         } // end while (null != doc)

         dcAddr.recycle();
         dbAddr.recycle();

         // output the php file with the correct filename included
         // to make sure the appropriate smart phone app can get it
         this.writePHPFile(nowStr);

      } catch(Exception e) {

         e.printStackTrace();

      }	 

      naLogger.exiting("ag.fast.backoffice.DHAdress2SQLite", "runNotes");
   }


   private void writePHPFile (String replacementStr) throws IOException {
      String filePath = this.getProperties().getProperty("php_template", "./db.php");
      FileReader phpFile = new FileReader (filePath);
      CharBuffer buff = CharBuffer.allocate (2000);
      buff.clear();
      phpFile.read(buff);
      String phpTemplate = new String (buff.array());
      phpTemplate = phpTemplate.replaceFirst("<db>", nowStr);
      naLogger.info(phpTemplate);
      String outputPath = this.getProperties().getProperty("output_location", "/var/www/htdocs")  + "/db.php";
      FileWriter outFile = new FileWriter(outputPath);
      assert null != outFile : "PHP File not allocated (null) !";
      naLogger.info("PHP Filepath: " + outputPath);
      PrintWriter out = new PrintWriter(outFile);
      assert null != out : "File Printer not allocated (null) !";
      out.println(phpTemplate);
      out.close();
      outFile.close();
   };

   private String nowStr;
   private String outputFile;
}
