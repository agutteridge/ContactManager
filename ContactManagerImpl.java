import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.*;

public class ContactManagerImpl {
	private HashSet<Contact> contactSet = null;
	private FutureMeeting futureList = null; //sorted list?

	public void addNewContact(String name, String notes){
		if (contactSet == null){
			contactSet = new HashSet<Contact>();
		}
		if (name == null || notes == null){
			throw new NullPointerException();
		}
		Contact anotherContact = new ContactImpl(name);
		anotherContact.addNotes(notes);
		contactSet.add(anotherContact);
	}

	public Set<Contact> getContacts(int... ids){ //allows arguments with any number of ints (including zero)
		if (ids.length == 0) { //if getContacts() is called
	    	throw new IllegalArgumentException("No IDs given.");
	    }

	    Contact[] contactArray = contactSet.toArray(new Contact[contactSet.size()]); //turn into array so that elements can be returned
		Set<Contact> result = new HashSet<Contact>(); 

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

	public Set<Contact> getContacts(String name){ //Repeating myself
		if (name == null) { 
	    	throw new NullPointerException();
	    }

	    Contact[] contactArray = contactSet.toArray(new Contact[0]); //turn into array so that elements can be returned
		Set<Contact> result = new HashSet<Contact>(); 

	    for (int n = 0; n < contactArray.length; n++) { //iterating through contactSet
	    	if (name.equals(contactArray[n].getName())) { //currently does not compare substrings!!!!!!!!!!
	    		result.add(contactArray[n]);
	    	}
	    }	

		if (result.isEmpty()){
	    	throw new IllegalArgumentException("No matching names found.");			
		}		
		return result;
	}
}