import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
 
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

		for (int i = 0; i < meetingList.size(); i++){
			int order = meeting.getDate().compareTo(meetingList.get(i).getDate());

			if (order == -1){
				meetingList.add(i, meeting); //inserts meeting at index i
				inserted = true;
				i = meetingList.size() + 1; //break out of for loop
			}
		}
		
		//Date is further in future than any current meetings, meeting added to the end.
		if (!inserted){
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
	private boolean checkContacts(Set<Contact> set){
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
		for (Meeting m : meetingList){
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

		for (Meeting m : fullList){
			if (m.getDate().after(today)){
				futureOnly.add(m);
			}
		}

		return futureOnly;
	}

	public List<Meeting> getFutureMeetingList(Calendar date){
		List<Meeting> result = new ArrayList<Meeting>();
		int length = meetingList.size();

		for (int i = length - 1; i >= 0; i--){
			Meeting m = meetingList.get(i);
			int relative = date.compareTo(m.getDate()); 
			if (relative > 0){
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

		for (Meeting m : meetingList){
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

		for (Meeting m : fullList){
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
		} else if (meetingToCast.getDate().after(today)){ 
			throw new IllegalStateException();
		}

		if (meetingToCast instanceof FutureMeeting){ //transfers fields to PastMeetingImpl constructor
			Calendar dateToTransfer = meetingToCast.getDate();
			Set<Contact> setToTransfer = meetingToCast.getContacts();

			Meeting meetingNowInPast = new PastMeetingImpl(dateToTransfer, id, setToTransfer, text);
			meetingList.remove(meetingToCast);
			insertInOrder(meetingNowInPast);
		} else {
			PastMeetingImpl m = (PastMeetingImpl) meetingToCast;
			m.addNotes(text);
		}
	}

	public void addNewContact(String name, String notes){
		if (contactSet == null){
			contactSet = new LinkedHashSet<Contact>();
		}

		int newId = generateContactId();

		try {
			if (name.equals(null) || notes.equals(null)){
				throw new NullPointerException();
			}
		} catch (NullPointerException e){
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

		for (int i = 0; i < ids.length; i++){ //iterating through args
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
		if (name == null){ 
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

	public void launch(){
		String filename = "." + File.separator + "contacts.txt";
		File f = new File(filename);

		if (!f.exists()){ 
			try {
				f.createNewFile();
				System.out.println("Creating new file?");
			} catch (IOException e){
				System.out.println("Could not create " + f.getName());
				e.printStackTrace();
			}
		} else {
			System.out.println("Copying.");
			copyOver(f);
		}		
	}

	public void flush(){
		String filename = "." + File.separator + "contacts.txt";
		File f = new File(filename);

		if (!f.exists()){
			System.out.println("DATABASE DOES NOT EXIST"); 
			//um one should have been made using Load in Main
		}

		try {
			FileWriter fw = new FileWriter(f.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			writeContacts(bw);
			writeMeetings(bw);
				try {
					bw.close();
				} catch (IOException ie) {
					System.out.println("Could not close.");
				}
		} catch (IOException ie){
			System.out.println("File could not be found.");
		}
	}

	private void writeContacts(BufferedWriter bw){
		Contact[] contactArray = contactSet.toArray(new Contact[contactSet.size()]);
		String line;

		for (int i = 0; i < contactArray.length; i++){
			Contact c = contactArray[i];
			String name = c.getName();
			String identString = String.valueOf(c.getId());
			String notes = c.getNotes(); 
			line = "C" + '\t' + name + '\t' + identString + '\t' + notes;
			try {
				System.out.println("WRITING: " + line);
				bw.write(line);
				bw.newLine();
			} catch (IOException e){
				//do nothing
			}
		}
	}

	private void writeMeetings(BufferedWriter bw){
		String line;

		for (Meeting m : meetingList){
			Calendar date = m.getDate();
			String dateString = formatDate(date);
			String identString = String.valueOf(m.getId());

			Set<Contact> meetingContacts = m.getContacts();
			Contact[] contactArray = meetingContacts.toArray(new Contact[meetingContacts.size()]);
			String contactIDs = "";
			for (Contact c : contactArray){
				contactIDs += String.valueOf(c.getId()) + ",";
			}

			String notes = "";
			if (m instanceof PastMeeting){
				PastMeetingImpl pastM = (PastMeetingImpl) m;
				notes = pastM.getNotes();
			}

			line = "M" + '\t' + dateString + '\t' + identString + '\t' + contactIDs + '\t' + notes;
			try {
				System.out.println("WRITING: " + line);
				bw.write(line);
				bw.newLine();	
			} catch (IOException e){
				//do nothing
			}
		}		
	}

	//formatting date
	private String formatDate(Calendar date){
		Date timeAndDate = date.getTime();
		SimpleDateFormat f = new SimpleDateFormat("ddMMyyyy HH:mm");
		return f.format(timeAndDate);
	}

	private void copyOver(File input){
		System.out.println("copyOver method started.");
		BufferedReader in = null;
		boolean isContact;
		try {
			in = new BufferedReader(new FileReader(input));
			String line;

			while ((line = in.readLine()) != null){
				String[] fields = line.split("\t"); 
				if (fields[0].equals("C")){
					loadContact(fields);
				} else if (fields[0].equals("M")){
					loadMeeting(fields);
				}
			}
		} catch (IOException e){
			System.out.println("I/O error.");
			e.printStackTrace();
		} finally {
			System.out.println("Contacts and Meetings loaded.");
			try {
				closeReader(in);
			} catch (Exception e){
			}
		}
	}

	public void loadContact(String[] fields){
		if (contactSet == null){
			contactSet = new LinkedHashSet<Contact>();
		}

		String name = fields[1];
		String identString = fields[2];
		int identInt = Integer.parseInt(identString);
		Contact c = new ContactImpl(name, identInt);
		if (fields.length == 4){
			c.addNotes(fields[3]);
		}
		contactSet.add(c);
		System.out.println("Added contact " + identInt);	
	}

	private void loadMeeting(String[] fields){
		if (meetingList == null){
			meetingList = new ArrayList<Meeting>();
		}	

		Calendar today = Calendar.getInstance();
		String dateString = fields[1]; //date in format "ddMMyyyy HH:mm"
		Calendar date = Calendar.getInstance();
		int year = Integer.parseInt(dateString.substring(4,8));
		int month = Integer.parseInt(dateString.substring(2,4));
		int day = Integer.parseInt(dateString.substring(0,2));
		int hours = Integer.parseInt(dateString.substring(9,11));
		int minutes = Integer.parseInt(dateString.substring(12,14));
		date.set(year, month, day, hours, minutes);

		String identString = fields[2];
		int identInt = Integer.parseInt(identString);

		String contactIDs = fields[3]; //IDs in format "1,2,3,4,"
		String[] identArrayString = identString.split(",");
		int[] identArrayInt = new int[identArrayString.length];

		//copying IDs into an array of integers
		for (int i = 0; i < identArrayString.length; i++) {
        	identArrayInt[i] = Integer.parseInt(identArrayString[i]);
		} 

		Set<Contact> contactsInMeeting = new LinkedHashSet<Contact>();
		contactsInMeeting = getContacts(identArrayInt);

		Meeting m;
		if (date.before(today)){
			String notes;
			if (fields.length == 5){
				notes = fields[4];
			} else {
				notes = "";
			}

			m = new PastMeetingImpl(date, identInt, contactsInMeeting, notes); 
			meetingList.add(m);
			System.out.println("Added meeting " + identInt);
		} else {
			m = new FutureMeetingImpl(date, identInt, contactsInMeeting); 			
			meetingList.add(m);
			System.out.println("Added meeting " + identInt);			
		}
	}

	private void closeReader(Reader reader){
		try {
			if (reader != null){
			reader.close();
			}
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public static void main(String[] args){

	}
}
