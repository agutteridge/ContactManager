import java.util.*;

public class NewMeeting extends AllMeetings {
	private int meetingID;
	private Calendar meetingDate;
	private Set<Contact> meetingContacts;
	private String meetingNotes;

	private String addZero(String num){
		if (num.length() < 2){
			num = "0" + num;
		}
		return num;
	}

	public int generateId(Calendar date, int uniqueNumber){
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		month = addZero(month);
		String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		day = addZero(day);
		String lastnumber = String.valueOf(uniqueNumber); 

		String id = year + month + day + lastnumber; //concatenate year, month, day and meeting no.
		int intID = Integer.parseInt(id);
		return intID;	
	}

	public int getId(){
		return meetingID;
	}

	public Calendar getDate(){
		return meetingDate;
	}

	/**
	* Return the details of people that attended the meeting.
	*
	* The list contains a minimum of one contact (if there were
	* just two people: the user and the contact) and may contain an
	* arbitary number of them.
	*
	* @return the details of people that attended the meeting.
	*/
	public Set<Contact> getContacts(){
		return meetingContacts;
	}

	public String getNotes(){
		return meetingNotes;
	}

}