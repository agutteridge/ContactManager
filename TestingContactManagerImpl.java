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

	@After
	public void tearDown(){
		test = null;
		System.out.println("***END OF TEST***");
		System.out.println("");				
	}

	@Test
	public void testGetContactsInts(){
		System.out.println("***TESTING: testGetContactsInts***");
		Set<Contact> tempSet = test.getContacts(1);
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 1, \r\n");
	}

	@Test
	public void testGetContactsIntsWithMultipleArgs(){
		System.out.println("***TESTING: testGetContactsIntsWithMultipleArgs***");
		Set<Contact> tempSet = test.getContacts(0,1);
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetContactsIntsWithZeroArgs(){
		System.out.println("***TESTING: testGetContactsIntsWithZeroArgs***");
		Set<Contact> tempSet = test.getContacts();			
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetContactsIntsWithInvalidID(){
		System.out.println("***TESTING: testGetContactsIntsWithInvalidID***");
		Set<Contact> tempSet = test.getContacts(0,1,2);		
	}

	@Test
	public void testGetContactsString(){
		System.out.println("***TESTING: testGetContactsString***");
		Set<Contact> tempSet = test.getContacts("Sam");
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");
	}

	@Test
	public void testGetContactsPartial(){
		System.out.println("***TESTING: testGetContactsPartial***");
		Set<Contact> tempSet = test.getContacts("Sa");
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, \r\n");		
	}

	@Test
	public void testGetContactsStringNoMatch(){
		System.out.println("***TESTING: testGetContactsStringNoMatch***");
		Set<Contact> tempSet = test.getContacts("Alice");
		String output = printSet(tempSet);
		assertEquals(output, "");
	}

	@Test(expected=NullPointerException.class)
	public void testGetContactsStringNull(){
		System.out.println("***TESTING: testGetContactsStringNull***");
		String str = null;
		Set<Contact> tempSet = test.getContacts(str);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingPastDate(){
		System.out.println("***TESTING: testAddFutureMeetingPastDate***");
		Set<Contact> set = new LinkedHashSet<Contact>();
		Contact alice = new ContactImpl("Alice", 0);
		set.add(alice);
		Calendar date = new GregorianCalendar(1970, 1, 1);
		test.addFutureMeeting(set, date);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorEmptyContactSet(){
		System.out.println("***TESTING: testConstructorEmptyContactSet***");
		Set<Contact> set = new LinkedHashSet<Contact>();
		Calendar date = new GregorianCalendar(2015, 1, 1);
		test.addFutureMeeting(set, date);
	}

	@Test
	public void testAddFutureMeeting(){
		System.out.println("***TESTING: testAddFutureMeeting***");
		Set<Contact> set = new LinkedHashSet<Contact>();
		set = test.getContacts("Sam");
		Calendar date = new GregorianCalendar(2015, 1, 1);
		test.addFutureMeeting(set, date);
		test.addFutureMeeting(set, date);
		String output = test.printMeetings();
		assertEquals(output, "20150101, 201501010, \r\n20150101, 201501010, \r\n");
	}

	//printing methods
	public String printSet(Set<Contact> set){
		Iterator<Contact> iterator = set.iterator();
		String result = "";

		while (iterator.hasNext()){
			Contact person = iterator.next();	
			result += person.getName() + ", " + person.getId() + ", " + person.getNotes() + "\r\n";
		}

		System.out.println(result);
		return result;
	}

	public String printList(List<Meeting> list){
		Iterator<Meeting> iterator = list.iterator();
		String result = "";

		while (iterator.hasNext()){	
			Meeting m = iterator.next();
			result += m.getDate() + ", " + m.getId() + ", " + printSet(m.getContacts()) 
				+ ", " + "\r\n";
		}

		System.out.println(result);
		return result;
	}
}