import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TestingContactManagerImpl {
	private ContactManagerImpl test = new ContactManagerImpl();
	
	@Before
	public void setUp(){
		test.addNewContact("Sam", "he is someone");
		test.addNewContact("Sam", "");
	}

	@Test
	public void testGetContactsInts(){
		Set<Contact> tempSet = test.getContacts(1);
		String output = test.prettyPrint(tempSet);
		System.out.println(output);		
		assertEquals(output, "Sam, 1, \r\n");
	}

	@Test
	public void testGetContactsIntsWithMultipleArgs(){
		Set<Contact> tempSet = test.getContacts(0, 1);
		String output = test.prettyPrint(tempSet);
		System.out.println(output);		
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetContactsIntsWithZeroArgs(){
		Set<Contact> tempSet = test.getContacts();			
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetContactsIntsWithInvalidID(){
		Set<Contact> tempSet = test.getContacts(0,1,2);		
	}

	@Test
	public void testGetContactsString(){
		Set<Contact> tempSet = test.getContacts("Sam");
		String output = test.prettyPrint(tempSet);
		System.out.println(output);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");
	}

	@Test
	public void testGetContactsPartial(){
		Set<Contact> tempSet = test.getContacts("Sa");
		String output = test.prettyPrint(tempSet);
		System.out.println(output);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");		
	}

	@Test
	public void testGetContactsStringNoMatch(){
		Set<Contact> tempSet = test.getContacts("Alice");
		String output = test.prettyPrint(tempSet);
		System.out.println(output);
		assertEquals(output, "");
	}

	@Test(expected=NullPointerException.class)
	public void testGetContactsStringNull(){
		String str = null;
		Set<Contact> tempSet = test.getContacts(str);
	}

	//FOR ADDFUTUREMEETING 
	// @Test(expected=IllegalArgumentException.class)
	// public void testConstructorPastMeeting(){
	// 	Set<Contact> set = new LinkedHashSet<Contact>();
	// 	Contact alice = new ContactImpl("Alice", 0);
	// 	set.add(alice);		
	// 	Calendar date = new GregorianCalendar(1970, 1, 1);
	// 	test = new NewMeeting(date, set);
	// }

	//FOR ADDFUTUREMEETING 
	// @Test(expected=IllegalArgumentException.class)
	// public void testConstructorEmptyContactSet(){
	// 	Calendar date = new GregorianCalendar(1970, 1, 1);
	// 	test = new NewMeeting();
	// }

}