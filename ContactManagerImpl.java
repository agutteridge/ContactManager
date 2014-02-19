import java.util.*;
 
public class ContactManagerImpl { //IMPLEMENTS CONTACT MANAGER
	private Set<Contact> contactSet = null;
	private List<Meeting> meetingList = null; //sorted list?

	public int addFutureMeeting(Set<Contact> contacts, Calendar date){
		Calendar today = Calendar.getInstance();
		if (date.before(today)){
			throw new IllegalArgumentException();
			System.out.println("Meeting is in the past.");
		} else {
			this.meetingDate = date;
		}

		if (contacts.isEmpty()){
			System.out.println("No contacts specified.");
			throw new IllegalArgumentException();			
		} else {
			checkContacts();
		}

		int meetingID = generateMeetingId();

		FutureMeeting meeting = new NewMeeting(date, meetingID, contacts);
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
		for (int i = 0; i < set.size(); i++) {
			try {
				int contactID = set.get(i).getId();
				getContacts(contactID);
			} catch (IllegalArgumentException ie) {
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
		System.out.println("Contact \"" + anotherContact.getName() + "\" assigned ID " + 
			anotherContact.getId() + " and added to the system.");
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
	* Generates IDs for dates (made unique in NewMeeting constructor)
	* @return integer suffix to be added to date in NewMeeting constructor
	* @param date of meeting
	* @param whether meeting date is in the future or not
	*/
	private int generateId(Calendar date, boolean isFuture){
		if(isFuture){
			try {
				List<FutureMeeting> futureList = getFutureMeetingList(date);
				return futureList.length() + 1;
			} catch (IllegalArgumentException ie){
				return 0;
			}
		} else {
			try {
				List<PastMeeting> pastList = getPastMeetingList(date);
				return pastList.length() + 1;
			} catch (IllegalArgumentException ie){
				return 0;
			}			
		}
	}

	public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
		Set<Contact> result = new LinkedHashSet<Contact>(); 

		for (int i = 0; i < ids.length; i++) { //iterating through args
		    for (int n = 0; n < contactSet.size(); n++) { //iterating through contactSet
		    	if (ids[i] == (contactSet.get(n).getId())) {
		    		result.add(contactSet.get(n));
		    	}
		    }
		}
		
		if (result.isEmpty()){
			System.out.println("No matching IDs found.");
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
		Set<Contact> result = new LinkedHashSet<Contact>(); //does this have to be instantiated?
		
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
	* Printing method
	* 
	* @param Set to be printed
	* @return Formatted string of Contacts' names, IDs and Notes
	*/
	public String prettyPrint(Set<Contact> set){
		Iterator<Contact> iterator = set.iterator();
		String result = "";

		while (iterator.hasNext()){
			Contact person = iterator.next();	
			result += person.getName() + ", " + person.getId() + ", " + person.getNotes() + "\r\n";
		}

		return result;
	}

	public void flush(){
		//method
	}
}