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

	public NewMeeting(Calendar date, int iDsuffix, Set<Contact> contacts, String notes){
		meetingDate = date;
		meetingContacts = contacts;
		meetingNotes = notes;

		String lastnumber = String.valueOf(iDsuffix); 
		String stringID = formatDate(date) + lastnumber;
		meetingID = Integer.parseInt(stringID);
	}

	/**
	* Adds a zero prefix to a month or day of number 1-9
	*
	* @param number to be prefixed with a zero
	* @return String of concatenated digits
	*/
	private String addZero(String num){
		if (num.length() < 2){
			num = "0" + num;
		}
		return num;
	}

	/**
	* Formatting date to String (also used in testing)
	* 
	* @return date in yyyymmdd format
	* @param date to convert into String and format
	*/
	public String formatDate(Calendar date){
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

		String result = year + addZero(month) + addZero(day); //concatenate year, month and day
		return result;
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

}