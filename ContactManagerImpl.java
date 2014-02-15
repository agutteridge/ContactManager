import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
 
public class ContactManagerImpl { //IMPLEMENTS CONTACT MANAGER
	private LinkedHashSet<Contact> contactSet = null;
	private FutureMeeting futureList = null; //sorted list?

	@Before
	public void setUp(){
		addNewContact("Sam", "he is someone");
		addNewContact("Sam", "");
	}

	@After
	public void tearDown(){
		this.contactSet = null;
		this.futureList = null;
	}

	// public int addFutureMeeting(Set<Contact> contacts, Calendar date){
	// 	//method
	// }

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

	@Test
	public void testAddNewContact(){
		String output = prettyPrint(contactSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");
	}

	public void addNewContact(String name, String notes) {
		if (this.contactSet == null){
			LinkedHashSet<Contact> temp = new LinkedHashSet<Contact>(); 
			this.contactSet = temp;
		}

		int newId = generateId(0);
		//perhaps add method to check whether contact w/ same name exists?
		//option 1: append notes
		//option 2: overwrite
		//option 3: add new
		//option 4: do not add new


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

	private int generateId(int id){ //would take a long time with 1000s contacts
		Iterator<Contact> iterator = contactSet.iterator();
		boolean unique = true;

		while (iterator.hasNext()){
			Contact person = iterator.next();	
			if (person.getId() == id){
				unique = false;
			}
		}

		if (unique){
			return id;
		} else {
			return generateId(id + 1);
		}
	}

	@Test
	public void testGetContactsInts(){
		Set<Contact> tempSet = getContacts(1);
		String output = prettyPrint(tempSet);
		System.out.println(output);		
		assertEquals(output, "Sam, 1, \r\n");
	}

	@Test
	public void testGetContactsIntsWithMultipleArgs(){
		Set<Contact> tempSet = getContacts(0, 1);
		String output = prettyPrint(tempSet);
		System.out.println(output);		
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetContactsIntsWithZeroArgs(){
		Set<Contact> tempSet = getContacts();			
	}

	public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
	    Contact[] contactArray = contactSet.toArray(new Contact[contactSet.size()]); //turn into array so that elements can be returned
		Set<Contact> result = new LinkedHashSet<Contact>();

		for (int i = 0; i < ids.length; i++) { //iterating through args
		    for (int n = 0; n < contactArray.length; n++) { //iterating through contactSet
		    	if (ids[i] == (contactArray[n].getId())) {
		    		result.add(contactArray[n]);
		    	}
		    }	
		}
		
		//exception thrown when ANY ideas do not correspond to Contact
		if (result.isEmpty()){
	    	throw new IllegalArgumentException("No matching IDs found.");			
		}		
		return result;
	} 

	@Test
	public void testGetContactsString(){
		Set<Contact> tempSet = getContacts("Sam");
		String output = prettyPrint(tempSet);
		System.out.println(output);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");
	}

	@Test
	public void testGetContactsStringNoMatch(){
		Set<Contact> tempSet = getContacts("Alice");
		String output = prettyPrint(tempSet);
		System.out.println(output);
		assertEquals(output, "");
	}

	@Test(expected=NullPointerException.class)
	public void testGetContactsStringNull(){
		String str = null;
		Set<Contact> tempSet = getContacts(str);
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
	* Printing method - for testing only?
	* 
	* @param Set to be printed
	* @return Formatted string of Contacts' names, IDs and Notes
	*/
	private String prettyPrint(Set<Contact> set){
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