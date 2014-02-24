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
	* Adds notes about the meeting
	* 
	* @param note to be added, if notes already exist then new String will be appended.
	*/
	public void addNotes(String note){
		if (this.meetingNotes == null || this.meetingNotes.equals("")){
			this.meetingNotes = note;
		} else {
			this.meetingNotes += ", " + note;		
		}
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
	public int compareTo(Object meeting){
		if (meeting == null){
			throw new NullPointerException();
		}

		if (meeting instanceof Meeting){
			throw new ClassCastException();
		} 

		Meeting m = (Meeting) meeting;

		return (meetingDate.compareTo(m.getDate()));
	}

	/**
	* Formatting date to String (used in testing)
	* 
	* @return date in yyyymmdd format
	* @param date to convert into String and format
	*/
	public String formatDate(Calendar date){
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

		String result = year + month + day; //concatenate year, month and day
		return result;
	}
}