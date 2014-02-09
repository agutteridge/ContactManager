import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ContactManagerImpl {
	private LinkedHashSet<Contact> contactSet = null;
	private FutureMeeting futureList = null; //sorted list?

	@Test
	public void testAddNewContact(){
		ContactManagerImpl database = new ContactManagerImpl();
		database.addNewContact("sam", "he is someone");
		database.addNewContact("sam", "");
		String result = database.prettyPrint();
		System.out.println("CONTACT SET: " + result);
		assertEquals(result, "sam, 0, he is someone; sam, 0, ; "); //iterator does not return in same order!!!
	}

	public void addNewContact(String name, String notes) throws NullPointerException {
		if (contactSet == null){
			contactSet = new LinkedHashSet<Contact>();
		}

		int newId = 0;
		// while (getContacts(newId)){
		// 	newId++;
		// }

		try {
			if (name.equals(null) || notes.equals(null)){
				throw new NullPointerException();
			}
			Contact anotherContact = new ContactImpl(name, newId);
			anotherContact.addNotes(notes);
			contactSet.add(anotherContact);
		} catch (NullPointerException e) {
			System.out.println("name or notes are null");
			e.printStackTrace();
		}
	}

	// public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
	//     Contact[] contactArray = contactSet.toArray(new Contact[contactSet.size()]); //turn into array so that elements can be returned
	// 	Set<Contact> result = new LinkedHashSet<Contact>();

	// 	for (int i = 0; i < ids.length; i++) { //iterating through args
	// 		System.out.println("ARG BEING TESTED: " + ids[i]);
	// 	    for (int n = 0; n < contactArray.length; n++) { //iterating through contactSet
	// 	    	if (ids[i] == (contactArray[i].getId())) {
	// 	    		result.add(contactArray[i]);
	// 	    		System.out.println("found " + contactArray[i].getName());
	// 	    	}
	// 	    }	
	// 	}

	// 	if (result.isEmpty()){
	//     	throw new IllegalArgumentException("No matching IDs found.");			
	// 	}		
	// 	return result;
	// } 

	public Set<Contact> getContacts(String name){
		if (name == null) { 
	    	throw new NullPointerException(); //catch
	    }
		
		Iterator<Contact> iterator = contactSet.iterator();
		Set<Contact> result = new LinkedHashSet<Contact>();
		
		while (iterator.hasNext()){
			Contact person = iterator.next();
			if (person.getName().contains(name)){ //boolean test for whether name of Contact object contains name
				result.add(person); //how does it increment?
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
}