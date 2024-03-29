import java.util.Calendar;
import java.util.Set;

public class PastMeetingImpl implements PastMeeting {
	private final int meetingID;
	private Calendar meetingDate;
	private Set<Contact> meetingContacts;
	private String meetingNotes;

	public PastMeetingImpl(Calendar date, int iD, Set<Contact> contacts, String notes){
		this.meetingID = iD;
		this.meetingDate = date;
		this.meetingContacts = contacts;
		this.meetingNotes = notes;
	}

	public int getId(){
		return meetingID;
	}

	public Calendar getDate(){
		return meetingDate;
	}

	public Set<Contact> getContacts(){
		return meetingContacts;
	}

	public String getNotes(){
		return meetingNotes;
	}

	/**
	* Adds notes to a pastMeeting. 
	* 
	* When read from a file, PastMeetings with no notes are constructed with an empty string.
	* This method assumes that notes are always appended, not overwritten.
	*/
	public void addNotes(String note){
		if (this.meetingNotes == null){
			this.meetingNotes = note;
		} else {
			this.meetingNotes += ", " + note;		
		}
	}
}
