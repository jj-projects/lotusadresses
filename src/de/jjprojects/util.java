package de.jjprojects;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * provides several static utility functions
 * 
 * @author Joerg Juenger; Copyright 2012 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 *
 */

public class util {
	static Logger naLogger = Logger.getLogger("ag.fast.util");

	/**
	   Replace characters having special meaning in regular expressions
	   with their escaped equivalents, preceded by a '\' character.
	  
	   <P>The escaped characters include :
	  <ul>
	  <li>.
	  <li>\
	  <li>"
	  <li>?, * , and +
	  <li>&
	  <li>:
	  <li>{ and }
	  <li>[ and ]
	  <li>( and )
	  <li>^ and $
	  </ul>
	  */
	  public static String forRegex(String aRegexFragment){
	    final StringBuilder result = new StringBuilder();

	    final StringCharacterIterator iterator = 
	      new StringCharacterIterator(aRegexFragment)
	    ;
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      /*
	       All literals need to have backslashes doubled.
	      */
	      if (character == '.') {
	        result.append("\\.");
	      }
	      else if (character == '\\') {
	        result.append("\\\\");
	      }
	      else if (character == '?') {
		    result.append("\\?");
		  }
	      else if (character == '*') {
	        result.append("\\*");
	      }
	      else if (character == '+') {
	        result.append("\\+");
	      }
	      else if (character == '&') {
	        result.append("\\&");
	      }
	      else if (character == ':') {
	        result.append("\\:");
	      }
	      else if (character == '{') {
	        result.append("\\{");
	      }
	      else if (character == '}') {
	        result.append("\\}");
	      }
	      else if (character == '[') {
	        result.append("\\[");
	      }
	      else if (character == ']') {
	        result.append("\\]");
	      }
	      else if (character == '(') {
	        result.append("\\(");
	      }
	      else if (character == ')') {
	        result.append("\\)");
	      }
	      else if (character == '^') {
	        result.append("\\^");
	      }
	      else if (character == '$') {
	        result.append("\\$");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	  
	  public static String stripChars(String goods, String s) {  
		    String good = goods;
		    String result = "";
		    if (null != s)
		    	for ( int i = 0; i < s.length(); i++ ) {
		    		if ( good.indexOf(s.charAt(i)) >= 0 )
		    			result += s.charAt(i);
		        }
		    return result;
		}
	  

	  public static String getDateTime4SQL(Date date) {
	        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        return dateFormat.format(date);
	    }
	  

	  public static String propertyFromFile (String fileName, String key) {
		  String value = null;
		  try{  
			  Properties pro = new Properties();
			  File f = new File(fileName + ".properties");
			  boolean fileExists = f.exists();
			  if(fileExists){
				  FileInputStream in = new FileInputStream(f);
				  pro.loadFromXML(in);
				  value = pro.getProperty(key);
			  }			  
		  }
		  catch(IOException e){
			  e.printStackTrace();
		  }
		  
		  return value;
	  }
	  
	  public static void propertyToFile (String fileName, String key, String value) {
		  try{  
			  Properties pro = new Properties();
			  File f = new File(fileName + ".properties");
			  boolean fileExists = f.exists();
			  if(!fileExists){
				  naLogger.finer ("File " + fileName + ".properties not found, will be created!");
				  
				  fileExists = f.createNewFile();
				  assert fileExists : "Properties file could not be created";
			  } else {
				  FileInputStream in = new FileInputStream(f);
				  pro.loadFromXML(in);
			  }
			  pro.setProperty(key, value);
			  pro.storeToXML(new FileOutputStream(fileName + ".properties"),null);
			  naLogger.finer ("File " + fileName + ".properties was saved!");
		  }
		  catch(IOException e){
			  e.printStackTrace();
		  }
	  }
}
