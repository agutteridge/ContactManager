import java.util.Calendar;
import java.util.Set;

public class FutureMeetingImpl implements FutureMeeting {
	private final int meetingID;
	private Calendar meetingDate;
	private Set<Contact> meetingContacts;

	public FutureMeetingImpl(Calendar date, int iD, Set<Contact> contacts){
		this.meetingID = iD;
		this.meetingDate = date;
		this.meetingContacts = contacts;
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
}

