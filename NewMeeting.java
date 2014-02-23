import java.util.*;

public class NewMeeting extends AllMeetings {
	private int meetingID;
	private Calendar meetingDate;
	private Set<Contact> meetingContacts;
	private String meetingNotes;

	public NewMeeting(){
		//testing only
		System.out.println("Incorrect constructor used.");
	}

	public NewMeeting(Calendar date, int iD, Set<Contact> contacts, String notes){
		meetingID = iD;
		meetingDate = date;
		meetingContacts = contacts;
		meetingNotes = notes;
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
	* Compares this meeting with the specified meeting, using meetingDate. 
	*
	* @return A negative integer, zero, or a positive integer as this meeting is earlier than, 
	* on the same date as, or later than the specified meeting date.
	* @param The meeting that this meeting will be compared to.
	* @throws NullPointerException - if the specified meeting is null.
	* @throws ClassCastException - if the specified meeting's type prevents it from being compared.
	*/
	@Override
	public int compareTo(Meeting meeting){
		if (meeting == null){
			throw new NullPointerException();
		}

		if (!Meeting.instanceOf(meeting)){
			throw new ClassCastException();
		}

		return (meetingDate.compareTo(meeting.getDate()));
	}

}