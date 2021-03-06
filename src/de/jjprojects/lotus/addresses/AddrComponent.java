/**
 * 
 */
package de.jjprojects.lotus.addresses;

import java.util.Properties;

import lotus.domino.Document;
import lotus.domino.NotesException;
import de.jjprojects.JJPException;
import de.jjprojects.util;
import de.jjprojects.lotus.NotesElement;

/**
 * @author Joerg Juenger; Copyright 2012 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 *
 */
public abstract class AddrComponent extends NotesElement {
	
	public void dataFromLotusDoc (Document doc, Properties props) throws NotesException, JJPException {
			super.dataFromLotusDoc (doc, props);
			
			if (null == props)
			   throw new JJPException("Properties are invalid (null)!");
			
			this.setEMail(doc.getItemValueString(props.getProperty("db_col_email", "AdrEMail")));
			this.setAddrKey(this.getDocID());
			String parentKey = doc.getItemValueString(props.getProperty("db_col_companykey", "AdrParentSearchKey"));
			if (null == parentKey)
				parentKey = this.getAddrKey();
			this.setCompanyKey(parentKey);
			this.setSirName(doc.getItemValueString(props.getProperty("db_col_lastname", "AdrName")));
			this.setFirstName(doc.getItemValueString(props.getProperty("db_col_firstname", "AdrFirstName")));
			this.setCompany(doc.getItemValueString(props.getProperty("db_col_company", "AdrOrganization")));
			this.setZip(doc.getItemValueString(props.getProperty("db_col_zip", "AdrZip")));
			this.setCity(doc.getItemValueString(props.getProperty("db_col_city", "AdrCity")));
			this.setPhone(doc.getItemValueString(props.getProperty("db_col_phone", "AdrPhoneComplete")));
			this.setMobile(doc.getItemValueString(props.getProperty("db_col_mobile", "AdrPhoneMobile")));
			this.setMobile(doc.getItemValueString(props.getProperty("db_col_country", "AdrPhoneCountry")));
			this.setMobile(doc.getItemValueString(props.getProperty("db_col_street", "AdrPhoneStreet")));
	}

	public String toString () {
		return new String ("Key: " + this.getAddrKey() + ", Name: " + getName() + ", Org: " + this.getCompany() + ", OrgKey: " 
		                   + this.getCompanyKey() + ", Phone:" + this.getPhone());
	}

	/**
	 * @param name the name to set
	 */
	protected void setSirName(String name) {
		sirName = name;
	}
	/**
	 * @param name the name to set
	 */
	protected void setFirstName(String name) {
		firstName = name;
	}
	/**
	 * @return the name
	 */
	public String getSirName() {
		return null == sirName ? "" : sirName;
	}

	/**
	 * @return the name
	 */
	public String getFirstName() {
	   return null == firstName ? "" : firstName;
}

	/**
	 * @return the name
	 */
	public String getName() {
		return getFirstName() + " " + this.getSirName();
}


	/**
	 * @param addrKey the addrKey to set
	 */
	protected void setAddrKey(String addrKey) {
		this.addrKey = addrKey;
	}


	/**
	 * @return the addrKey
	 */
	public String getCompanyKey() {
	     return null == companyKey ? "" : companyKey;
}

	/**
	 * @param addrKey the addrKey to set
	 */
	protected void setCompanyKey(String addrKey) {
		this.companyKey = addrKey;
	}


	/**
	 * @return the addrKey
	 */
	public String getAddrKey() {
	   assert null != addrKey : "unique address key is not allowed to be NULL!";
		return addrKey;
	}


	/**
	 * @param eMail the eMail to set
	 */
	protected void setEMail(String eMail) {
		this.eMail = stripEmail(eMail);
	}

	/**
	 * @return the eMail
	 */
	public String getEMail() {
      return null == eMail ? "" : eMail;
	}

	/**
	 * @param company the company to set
	 */
	protected void setCompany(String company) {
		Company = company;
	}

	/**
	 * @return the company
	 */
	public String getCompany() {
      return null == Company ? "" : Company;
	}
	/**
	 * @param city the city to set
	 */
	protected void setCity(String city) {
		strCity = city;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return  (null == strCity) ? "" : strCity;
	}

	/**
	 * @return the street
	 */
	public String getStreet() {
		return  (null == strStreet) ? "" : strStreet;
	}

	/**
	 * @param zip the street to set
	 */
	protected void setStreet(String street) {
		strStreet = street;
	}

	/**
	 * @param city the country to set
	 */
	protected void setCountry(String country) {
		strCountry = country;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return  (null == strCountry) ? "" : strCountry;
	}

	/**
	 * @param zip the zip to set
	 */
	protected void setZip(String zip) {
		strZip = zip;
	}

	/**
	 * @return the zip
	 */
	public String getZip() {
		return  (null == strZip) ? "" : strZip;
	}


	/**
	 * @param phone the phone to set
	 */
	protected void setPhone(String phone) {
		
		Phone = stripPhone(phone);
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
	     return  (null == Phone) ? "" : Phone;
}

	/**
	 * @param phone the mobile phone number to set
	 */
	protected void setMobile(String phone) {
		
		Mobile = stripPhone(phone);
	}

	/**
	 * @return the mobile phone number
	 */
	public String getMobile() {
      return  (null == Mobile) ? "" : Mobile;
}

	private String stripPhone(String s) {  
	    String good = " +0123456789";
	    return util.stripChars (good, s);
	}
	
	private String stripEmail(String s) {  
	    String good = " abcdefghijklmnopqrstuvwxyz���ABCDEFGHIJKLMNOPQRSTUVWXYZ���0123456789<>@._-";
	    return util.stripChars (good, s);
	}
	

	private String strZip;
	private String strCity;
	private String firstName;
	private String sirName;
	private String addrKey;
	private String companyKey;
	private String eMail;
	private String Company;
	private String Phone;
	private String Mobile;
	private String strStreet;
	private String strCountry;

}
