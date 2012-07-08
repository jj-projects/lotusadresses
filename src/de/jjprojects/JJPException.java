package de.jjprojects;
/**
 * basic exception class for all JJ-Projects specific exceptions;<br>
 * please derive all specific exceptions from this one !
 * @author Joerg Juenger; Copyright 2012 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 *
 */

public class JJPException extends Exception {

   public JJPException(String message) {
   
      this.message = message;
   }
   

   public String getMessage (String msg) {
      return message;
   }
   
   /**
    * 
    */
   private static final long serialVersionUID = 10000000001L;
   
   private String message;
}
