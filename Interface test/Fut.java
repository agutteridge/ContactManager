import java.util.*;

public class Fut implements FutureMeeting {
	private int id;
	private Calendar date;
	private Set<Contact> cs;

	public Fut(int newID, Calendar newDate, Set<Contact> newCS){
		id = newID;
		date = newDate;
		cs = newCS;
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
}
