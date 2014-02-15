import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;
 
public class ContactManagerImpl { //IMPLEMENTS CONTACT MANAGER
	private LinkedHashSet<Contact> contactSet;
	private FutureMeeting futureList = null; //sorted list?

	@Before 
	public void initialiseClass(){
		addNewContact("Sam", "he is someone");
		addNewContact("Sam", "");
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
		String output = prettyPrint();
		assertEquals(output, "Sam, 0, he is someone; Sam, 1, ; ");
	}

	public void addNewContact(String name, String notes) {
		if (this.contactSet == null){
			LinkedHashSet<Contact> temp = new LinkedHashSet<Contact>(); 
			this.contactSet = temp;
		}

		int newId = generateId(0);
		//perhaps add method to check whether contact w/ same notes & name exists?

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

	private int generateId(int id){
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

	public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
	    Contact[] contactArray = contactSet.toArray(new Contact[contactSet.size()]); //turn into array so that elements can be returned
		Set<Contact> result = new LinkedHashSet<Contact>();

		for (int i = 0; i < ids.length; i++) { //iterating through args
			System.out.println("ARG BEING TESTED: " + ids[i]);
		    for (int n = 0; n < contactArray.length; n++) { //iterating through contactSet
		    	if (ids[i] == (contactArray[i].getId())) {
		    		result.add(contactArray[i]);
		    		System.out.println("found " + contactArray[i].getName());
		    	}
		    }	
		}
		
		if (result.isEmpty()){
	    	throw new IllegalArgumentException("No matching IDs found.");			
		}		
		return result;
	} 

	public Set<Contact> getContacts(String name){
		try {
			if (name == null) { 
	    	throw new NullPointerException(); //catch
	    	}
	    } catch (NullPointerException e){
	    	System.out.println("name entered is null.");
	    	e.printStackTrace();
	    }
		
		Iterator<Contact> iterator = contactSet.iterator();
		Set<Contact> result = new LinkedHashSet<Contact>();
		
		while (iterator.hasNext()){
			Contact person = iterator.next();
			if (person.getName().contains(name)){ //contains() is a method in String
				result.add(person);
			}
		}

		try {
			if (result.isEmpty()){
		    	throw new IllegalArgumentException();			
			}		
		} catch (IllegalArgumentException e){
			System.out.println("No match found.");
			e.printStackTrace();
		}

		return result;
	}

	private String prettyPrint(){
		Iterator<Contact> iterator = contactSet.iterator();
		String result = "";

		while (iterator.hasNext()){
			Contact person = iterator.next();	
			result += person.getName() + ", " + person.getId() + ", " + person.getNotes() + "; ";
		}

		return result;
	}

	public void flush(){
		//method
	}
}