/**
 * 
 */
package de.jjprojects.lotus.addresses;

/**
 * @author Joerg Juenger; Copyright 2009 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 *
 */
public class Data_Contact extends AddrComponent {

	/**
	 * 
	 */
	public Data_Contact() {
		// TODO Auto-generated constructor stub
	}
	
	public String getContact () {
		String strContact = new String (getCompany() + " - " + getEMail() + " - " + getPhone());
		if (! getName().isEmpty())
			strContact = getName() + " - " + strContact;
			
		return strContact;
	}
	
	public String toString () {
		return new String ("Contact ") + super.toString ();
	}
}
