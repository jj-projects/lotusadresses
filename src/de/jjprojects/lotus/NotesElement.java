package de.jjprojects.lotus;

import java.util.Date;

import lotus.domino.Document;
import lotus.domino.Item;
import lotus.domino.NotesException;

/**
 * @since 2009-07-03
 * @author Joerg Juenger; Copyright 2009 ff. JJ-Projects Joerg Juenger, Munich, Germany
 *
 */
public abstract class NotesElement {

	public void dataFromLotusDoc (Document doc) throws NotesException {
		this.setDocID(doc.getItemValueString("DocID"));
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
