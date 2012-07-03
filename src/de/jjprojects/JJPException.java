package de.jjprojects;

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
