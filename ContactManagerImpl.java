import java.util.*;
 
public class ContactManagerImpl { //IMPLEMENTS CONTACT MANAGER
	private Set<Contact> contactSet;
	private List<Meeting> meetingList;

	public int addFutureMeeting(Set<Contact> contacts, Calendar date){
		Calendar today = Calendar.getInstance();
		if (date.before(today)){
			System.out.println("Meeting is in the past.");
			throw new IllegalArgumentException();
		}

		NewMeeting meeting = createMeeting(contacts, date, "");
		insertInOrder(meeting, true);
		return meeting.getId();
	}

	private NewMeeting createMeeting(Set<Contact> contacts, Calendar date, String notes) {
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

		int meetingID = generateMeetingId();

		NewMeeting meeting = new NewMeeting(date, meetingID, contacts, notes);
		return meeting;
	}

	/**
	* True if future, false for past!
	*/
	private boolean insertInOrder(NewMeeting meeting, boolean isFuture){

		boolean inserted = false;
		for (int i = 0; i < meetingList.size(); i++) {
			int order = meeting.getDate().compareTo(meetingList.get(i).getDate());

			if (order == -1){
				if (isFuture){
					System.out.println("CAST TO FUTURE");
					FutureMeeting m = (FutureMeeting) meeting;	
					meetingList.add(i, m); //inserts meeting at index i
				} else {
					System.out.println("CAST TO PAST");
					PastMeeting m = (PastMeeting) meeting;
					meetingList.add(i, m);
				}
				inserted = true;
				i = meetingList.size() + 1; //break out of for loop
			}
		}
		
		//Date is further in future than any current meetings, meeting added to the end.
		if (!inserted) {
			if (isFuture){
				FutureMeeting m = (FutureMeeting) meeting;	
				meetingList.add(m);
			} else {
				PastMeeting m = (PastMeeting) meeting;						
				meetingList.add(m);
			}
			inserted = true;
		}
		return inserted;
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
	private boolean checkContacts(Set<Contact> set){
		Iterator<Contact> iterator = set.iterator();
		while (iterator.hasNext()){
			Contact person = iterator.next();
			if (!contactSet.contains(person)){ //ADJUST!!!
				System.out.println("Set of contacts is invalid.");
				return false;
			} 
		}
		return true;
	}

	public PastMeeting getPastMeeting(int id){
		Meeting retrievedMeeting = getMeeting(id);

		if (retrievedMeeting == null){
			return null;
		}

		if (retrievedMeeting instanceof PastMeeting){
			PastMeeting m = (PastMeeting) retrievedMeeting;
			return m;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public FutureMeeting getFutureMeeting(int id){
		System.out.println("METHOD CALLED.");
		Meeting retrievedMeeting = getMeeting(id);

		if (retrievedMeeting == null){
			return null;
		}

		Class meetingClass = FutureMeeting.class;
 
		if (meetingClass.isInstance(retrievedMeeting)){
			System.out.println("INSTANCE OF.");			
			FutureMeeting m = (FutureMeeting) retrievedMeeting;
			return m;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Meeting getMeeting(int id){
		for (int i = 0; i < meetingList.size(); i++) {
			if (id == meetingList.get(i).getId()){
				return meetingList.get(i);
			}
		}
		return null;
	}

	// public List<Meeting> getFutureMeetingList(Contact contact){
	// 	//method
	// }

	public List<Meeting> getFutureMeetingList(Calendar date){
		List<Meeting> result = new ArrayList<Meeting>();
		int length = meetingList.size();

		for (int i = length - 1; i >= 0; i--) {
			int relative = date.compareTo(meetingList.get(i).getDate()); 
			if (relative > 0) {
				i = -1; //list is in order, therefore traversal not necessary after this point
			} else if (relative == 0){
				result.add(0, meetingList.get(i)); //inserts elements to the front
			}
		}
		return result;
	}

	/**
	* Retrieval of Meetings, using date as a parameter. Used for generating IDs.
	*
	* Calls getFutureMeetingList
	*/
	private List<Meeting> getMeetingList(Calendar date){
		return getFutureMeetingList(date);
	}

	// public List<PastMeeting> getPastMeetingList(Contact contact){
	// 	//method
	// }

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){
		Calendar today = Calendar.getInstance();
		if (date.after(today)){
			System.out.println("Meeting is in the past.");
			throw new IllegalArgumentException();
		}

		NewMeeting meeting = createMeeting(contacts, date, text);
		insertInOrder(meeting, false);
	}

	// public void addMeetingNotes(int id, String text){
	// 	//method
	// }

	public void addNewContact(String name, String notes) {
		if (this.contactSet == null){
			this.contactSet = new LinkedHashSet<Contact>();
		}

		int newId = generateContactId();

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
	* Generates unique IDs for contacts. Assumes deletion is impossible. 
	* 
	* @return unique contact integer
	* @throws IllegalArgumentException if ID generated already exists
	*/
	private int generateContactId(){
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
	* Generates unique IDs for contacts. Assumes deletion is impossible. 
	* 
	* @return unique meeting integer
	* @throws IllegalArgumentException if ID generated already exists
	*/
	private int generateMeetingId(){
		int uniqueID = meetingList.size();

		if (getMeeting(uniqueID) == null){
			return uniqueID;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	* For determining whether the date is in the future or not.
	* 
	* Gregorian Calendar objects are set to 12am by default, so meetings created for today
	* are assumed to be in the past.
	*
	* @param date to compare to today's date
	* @return boolean - true if date is in future, false otherwise
	*/
	private boolean isFuture(Calendar date){
		Calendar today = Calendar.getInstance();

		if (date.compareTo(today) == 1){
			return true;
		} else {
			return false;
		}
	}

	public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
		Set<Contact> result = new LinkedHashSet<Contact>();

		for (int i = 0; i < ids.length; i++) { //iterating through args
			Iterator<Contact> iterator = contactSet.iterator();
			while (iterator.hasNext()){
				Contact person = iterator.next();
				if (person.getId() == ids[i]){
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
	* Printing method for meetings (does not print contacts of meeting)
	* 
	* @param Set to be printed
	* @return Formatted string of meeting IDs and Notes
	*/
	public String printMeetings(){
		Iterator<Meeting> iterator = meetingList.iterator();
		String result = "";

		while (iterator.hasNext()){	
			Meeting m = iterator.next();
			NewMeeting nm = new NewMeeting();
			String fd = nm.formatDate(m.getDate());
			result += fd + ", " + m.getId() + "\r\n";
		}

		System.out.println(result);
		return result;
	}

	public void flush(){
		//method
	}
}