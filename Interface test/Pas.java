import java.util.*;

public class Pas implements PastMeeting {
	private int id;
	private Calendar date;
	private Set<Contact> cs;
	private String notes;

	public Pas(int newID, Calendar newDate, Set<Contact> newCS, String newNotes){
		id = newID;
		date = newDate;
		cs = newCS;
		notes = newNotes;
	}

	public int getId(){
		return id;
	}

	public Calendar getDate(){
		return date;
	}

	public Set<Contact> getContacts(){
		return cs;
	}

	public String getNotes(){
		return notes;
	}

}