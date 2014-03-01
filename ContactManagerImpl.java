import java.util.*;
 
public class ContactManagerImpl implements ContactManager {
	private Set<Contact> contactSet;
	private List<Meeting> meetingList;

	public int addFutureMeeting(Set<Contact> contacts, Calendar date){
		Calendar today = Calendar.getInstance();
		if (date.before(today)){
			System.out.println("Meeting is in the past.");
			throw new IllegalArgumentException();
		}

		if (meetingList == null){
			meetingList = new ArrayList<Meeting>();
		}	

		try {
			checkContacts(contacts);
		} catch (IllegalArgumentException ie){
			throw new IllegalArgumentException();
		}

		int meetingID = generateMeetingId();
		
		Meeting meeting = new FutureMeetingImpl(date, meetingID, contacts);
		insertInOrder(meeting);
		return meetingID;
	}

	/**
	* Inserts meeting in list according to the Calendar date
	* 
	* @return true if element was inserted, false otherwise
	* @param meeting to be inserted
	*/
	private boolean insertInOrder(Meeting meeting){
		boolean inserted = false;

		for (int i = 0; i < meetingList.size(); i++) {
			int order = meeting.getDate().compareTo(meetingList.get(i).getDate());

			if (order == -1){
				meetingList.add(i, meeting); //inserts meeting at index i
				inserted = true;
				i = meetingList.size() + 1; //break out of for loop
			}
		}
		
		//Date is further in future than any current meetings, meeting added to the end.
		if (!inserted) {
			meetingList.add(meeting);
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
	private boolean checkContacts(Set<Contact> set) {
		if (set.isEmpty()){
			System.out.println("No contacts specified.");
			throw new IllegalArgumentException();
		} 		

		Iterator<Contact> iterator = set.iterator();
		while (iterator.hasNext()){
			Contact person = iterator.next();
			if (!contactSet.contains(person)){
				System.out.println("Set of contacts is invalid.");
				throw new IllegalArgumentException();
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
		Meeting retrievedMeeting = getMeeting(id);

		if (retrievedMeeting == null){
			return null;
		}
 
		if (retrievedMeeting instanceof FutureMeeting){
			FutureMeeting m = (FutureMeeting) retrievedMeeting;
			return m;
		} else {
			throw new IllegalArgumentException();
		}
	}

	public Meeting getMeeting(int id){
		for (Meeting m : meetingList) {
			if (id == m.getId()){
				return m;
			}
		}
		return null;
	}

	public List<Meeting> getFutureMeetingList(Contact contact){
		List<Meeting> fullList = getMeetingList(contact);
		List<Meeting> futureOnly = new ArrayList<Meeting>();
		Calendar today = Calendar.getInstance();

		for (Meeting m : fullList) {
			if (m.getDate().after(today)){
				futureOnly.add(m);
			}
		}

		return futureOnly;
	}

	public List<Meeting> getFutureMeetingList(Calendar date){
		List<Meeting> result = new ArrayList<Meeting>();
		int length = meetingList.size();

		for (int i = length - 1; i >= 0; i--) {
			Meeting m = meetingList.get(i);
			int relative = date.compareTo(m.getDate()); 
			if (relative > 0) {
				i = -1; //list is in order, therefore traversal not necessary after this point
			} else if (relative == 0){
				result.add(0, meetingList.get(i)); //inserts elements to the front
			}
		}
		return result;
	}

	/**
	* Called by both getFutureMeetingList(Contact) and getPastMeetingList(Contact)
	* 
	* @return List of all meetings, past and future, that the contact attended/will attend 
	* @param Contact to be searched for
	*/
	private List<Meeting> getMeetingList(Contact contact){
		if(!contactSet.contains(contact)){
			throw new IllegalArgumentException();
		}

		List<Meeting> result = new ArrayList<Meeting>();
		int length = meetingList.size();

		for (Meeting m : meetingList) {
			Set<Contact> set = m.getContacts();
			if (set.contains(contact)){
				result.add(m);
			}
		}
		return result;
	}

	public List<PastMeeting> getPastMeetingList(Contact contact){
		List<Meeting> fullList = getMeetingList(contact);
		List<PastMeeting> pastOnly = new ArrayList<PastMeeting>();

		for (Meeting m : fullList) {
			if (m instanceof PastMeeting){
				PastMeeting meeting = (PastMeeting) m;
				pastOnly.add(meeting);
			}
		}
		return pastOnly;
	}

	public void addNewPastMeeting(Set<Contact> contacts, Calendar date, String text){
		Calendar today = Calendar.getInstance();
		if (date.after(today)){
			System.out.println("Meeting is in the future.");
			throw new IllegalArgumentException();
		}

		int meetingID = generateMeetingId();

		try {
			checkContacts(contacts);
		} catch (IllegalArgumentException ie){
			throw new IllegalArgumentException();
		}

		Meeting meeting = new PastMeetingImpl(date, meetingID, contacts, text);
		insertInOrder(meeting);
	}

	public void addMeetingNotes(int id, String text){
		if (text == null){
			throw new NullPointerException();
		}

		Calendar today = Calendar.getInstance();

		FutureMeeting meetingToCast = getFutureMeeting(id); 
		if (meetingToCast == null){
			throw new IllegalArgumentException();
		} else if (meetingToCast.getDate().before(today)){
			throw new IllegalStateException();
		}

		Calendar dateToTransfer = meetingToCast.getDate();
		Set<Contact> setToTransfer = meetingToCast.getContacts();

		Meeting meetingNowInPast = new PastMeetingImpl(dateToTransfer, id, setToTransfer, text);
		meetingList.remove(meetingToCast);
		insertInOrder(meetingNowInPast);
	}

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

	public List<Meeting> getAllMeetings(){
		return meetingList;
	}

	public Set<Contact> getAllContacts(){
		return contactSet;
	}

	public void flush(){
		//method
	}
}
