package de.jjprojects.lotus;

import java.util.Date;
import java.util.Properties;

import de.jjprojects.JJPException;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;

/**
 * @since 2009-07-03
 * @author Joerg Juenger; Copyright 2009 ff. JJ-Projects Joerg Juenger, Ebersberg, Germany
 *
 */
public abstract class NotesElement {

	public void dataFromLotusDoc (Document doc, Properties props) throws NotesException, JJPException {
	   if (null == props)
	      throw new JJPException("Properties are invalid (null)!");

		this.setDocID(doc.getItemValueString(props.getProperty("db_col_rowkey", "DocID")));
	};
	

	public String getDocID() {
		return DocID;
	}

	private void setDocID(String docID) {
		DocID = docID;
	}
	
	protected Date getItemDate (Document doc, String strItem) throws Exception {
		Item theItem = doc.getFirstItem(strItem);
		return  theItem.getDateTimeValue().toJavaDate();
	}
	
	private String DocID;
}
