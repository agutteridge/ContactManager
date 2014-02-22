import java.util.*;
 
public class ContactManagerImpl { //IMPLEMENTS CONTACT MANAGER
	private Set<Contact> contactSet;
	private List<Meeting> meetingList; //sorted list?

	public int addFutureMeeting(Set<Contact> contacts, Calendar date){

		Calendar today = Calendar.getInstance();
		if (date.before(today)){
			System.out.println("Meeting is in the past.");
			throw new IllegalArgumentException();
		}

		if (contacts.isEmpty()){
			System.out.println("No contacts specified.");
			throw new IllegalArgumentException();			
		} else {
			if (!checkContacts(contacts)){
				throw new IllegalArgumentException();
			}
		}

		if (meetingList == null){
			meetingList = new ArrayList<Meeting>();
		}	

		int meetingID = 0; //generateMeetingId();

		FutureMeeting meeting = new NewMeeting(date, meetingID, contacts, "");
		meetingList.add(meeting);
		return meetingID;
	}

	/**
	* Returns true if all contacts in set correspond to real Contacts in contactSet,
	* false otherwise.
	* 
	* Catches IllegalArgumentException thrown from getContacts(int...)
	*
	* @return boolean true if all contacts valid.
	* @param set containing contacts being tested
	*/
	public boolean checkContacts(Set<Contact> set){
		Iterator<Contact> iterator = set.iterator();
		while (iterator.hasNext()){
			Contact person = iterator.next();
			if (!contactSet.contains(person)){ //contains() is a method in String
				System.out.println("Set of contacts is invalid.");
				return false;
			}
		}
		return true;
	}

	// public PastMeeting getPastMeeting(int id){
	// 	//method
	// }

	// public FutureMeeting getFutureMeeting(int id){
	// 	//method
	// }

	// public Meeting getMeeting(int id){
	// 	//method
	// }

	// public List<Meeting> getFutureMeetingList(Contact contact){
	// 	//method
	// }

	// public List<Meeting> getFutureMeetingList(Calendar date){
	// 	//method
	// }

	// public List<PastMeeting> getPastMeetingList(Contact contact){
	// 	//method
	// }

	// public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){
	// 	//method
	// }

	// public void addMeetingNotes(int id, String text){
	// 	//method
	// }

	public void addNewContact(String name, String notes) {
		if (this.contactSet == null){
			this.contactSet = new LinkedHashSet<Contact>();
		}

		int newId = generateId();

		try {
			if (name.equals(null) || notes.equals(null)){
				throw new NullPointerException();
			}
		} catch (NullPointerException e) {
			System.out.println("name or notes are null");
			e.printStackTrace();
		}

		Contact anotherContact = new ContactImpl(name, newId);
		anotherContact.addNotes(notes);
		contactSet.add(anotherContact);
	}

	/**
	* Generates unique IDs for contacts 
	* 
	* @return unique contact integer
	* @throws IllegalArgumentException if ID generated already exists
	*/
	private int generateId(){
		int uniqueID = contactSet.size();
		try {
			getContacts(uniqueID);
		} catch (IllegalArgumentException ie){
			//ID is unique
			return uniqueID;
		}
		
		//ID is not unique
		throw new IllegalArgumentException();
	}

	/**
	* Generates ID suffix for meetings. 
	* Appended to date when NewMeeting constructor is called.
	*
	* @return integer suffix to be added to date in NewMeeting constructor
	* @param date of meeting
	* @param whether meeting date is in the future or not
	*/
	// private int generateId(Calendar date, boolean isFuture){
	// 	if(isFuture){
	// 		try {
	// 			List<Meeting> futureList = new ArrayList<FutureMeeting>();
	// 			futureList = getFutureMeetingList(date);
	// 			return futureList.length() + 1;
	// 		} catch (IllegalArgumentException ie){
	// 			return 0;
	// 		}
	// 	} else {
	// 		try {
	// 			List<Meeting> pastList = new ArrayList<PastMeeting>();
	// 			futureList = getPastMeetingList(date);
	// 			return pastList.length() + 1;
	// 		} catch (IllegalArgumentException ie){
	// 			return 0;
	// 		}			
	// 	}
	// }

	public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
		Set<Contact> result = new LinkedHashSet<Contact>();

		for (int i = 0; i < ids.length; i++) { //iterating through args
			Iterator<Contact> iterator = contactSet.iterator();
			while (iterator.hasNext()){
				Contact person = iterator.next();
				if (person.getId() == ids[i]){ //contains() is a method in String
					result.add(person);
				}
			}
		}
		
		if (result.isEmpty()){
	    	throw new IllegalArgumentException();			
		} else if (result.size() != ids.length){
			System.out.println("Not all IDs were valid.");
	    	throw new IllegalArgumentException();			
		}
		
		return result;
	} 

	public Set<Contact> getContacts(String name){
		if (name == null) { 
	    	throw new NullPointerException();
	    }
		
		Iterator<Contact> iterator = contactSet.iterator();
		Set<Contact> result = new LinkedHashSet<Contact>();
		
		while (iterator.hasNext()){
			Contact person = iterator.next();
			if (person.getName().contains(name)){ //contains() is a method in String
				result.add(person);
			}
		}

		if (result.isEmpty()){
			System.out.println("No match found.");
		}		

		return result;
	}

	/**
	* Printing method for contacts
	* 
	* @param Set to be printed
	* @return Formatted string of Contacts' names, IDs and Notes
	*/
	public String printContacts(){
		Iterator<Contact> iterator = contactSet.iterator();
		String result = "";

		while (iterator.hasNext()){
			Contact person = iterator.next();	
			result += person.getName() + ", " + person.getId() + ", " + person.getNotes() + "\r\n";
		}

		System.out.println(result);
		return result;
	}

	/**
	* Printing method for meetings
	* 
	* @param Set to be printed
	* @return Formatted string of Contacts' names, IDs and Notes
	*/
	public String printMeetings(){
		Iterator<Meeting> iterator = meetingList.iterator();
		String result = "";

		while (iterator.hasNext()){	
			Meeting m = iterator.next();
			NewMeeting nm = new NewMeeting();
			String fd = nm.formatDate(m.getDate());
			result += fd + ", " + m.getId() + ", " + "\r\n";
		}

		System.out.println(result);
		return result;
	}

	public void flush(){
		//method
	}
}