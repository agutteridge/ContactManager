public class NewMeeting implements AllMeetings {
	//generateID
	private int meetingID;
	private Calendar meetingDate;
	private Set<Contact> meetingContacts;

	private String addZero(String num){
		if (num.length() == 1){
			num = "0" + num;
		}
		return num;
	}

	public int generateID(Calendar date, int uniqueNumber){
		if (uniqueNumber > 10){
			System.out.println("There are already 10 meetings scheduled for this date.");
			throw new IllegalArgumentException();
		}

		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		addZero(month);
		String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
		addZero(day);
		String lastnumber = String.valueOf(uniqueNumber); 

		String id = year + month + day + lastnumber; //concatenate year, month, day and meeting no.
		return id;	
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

}