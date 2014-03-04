import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TestingContactManagerImpl {
	private ContactManagerImpl test = new ContactManagerImpl();
	
	@Before
	public void setUp(){
		test.addNewContact("Sam", "he is someone");
		test.addNewContact("Sam", "");
		Calendar date = Calendar.getInstance();
		date.set(2015, Calendar.JANUARY, 1);
		Set<Contact> tempSet = test.getContacts(0);
		test.addFutureMeeting(tempSet, date); 
	}

	@After
	public void tearDown(){
		test.flush();
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
		Set<Contact> tempSet = test.getContacts("Sam");
		Calendar date = Calendar.getInstance();
		date.set(1970, Calendar.JANUARY, 1);
		test.addFutureMeeting(tempSet, date);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingEmptyContactSet(){
		System.out.println("***TESTING: testAddFutureMeetingEmptyContactSet***");
		Set<Contact> tempSet = new LinkedHashSet<Contact>();
		Calendar date = Calendar.getInstance();
		date.set(2015, Calendar.JANUARY, 1);
		test.addFutureMeeting(tempSet, date);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddFutureMeetingUnknownContact(){
		System.out.println("***TESTING: testAddFutureMeetingUnknownContact***");
		Set<Contact> tempSet = new LinkedHashSet<Contact>();
		Contact alice = new ContactImpl("Alice", 0);
		tempSet.add(alice);
		Calendar date = Calendar.getInstance();
		date.set(2015, Calendar.JANUARY, 1);
		test.addFutureMeeting(tempSet, date);
	}

	@Test
	public void testAddFutureMeeting(){
		System.out.println("***TESTING: testAddFutureMeeting***");
		List<Meeting> tempList = test.getAllMeetings();
		String output = printMeetings(tempList);
		assertEquals(output, "201501, 0\r\n");
	}

	@Test
	public void testAddFutureMeetingContacts(){
		System.out.println("***TESTING: testAddFutureMeetingContacts***");
		Meeting m = test.getMeeting(0);
		Set<Contact> tempSet = m.getContacts();
		String output = printSet(tempSet); 
		assertEquals(output, "Sam, 0, he is someone\r\n");		
	}

	@Test
	public void testGetFutureMeeting(){
		System.out.println("***TESTING: testGetFutureMeeting***");
		Meeting m = test.getFutureMeeting(0);
		assertTrue(m instanceof FutureMeeting);
	}

	@Test
	public void testMeetingListIsOrdered(){
		System.out.println("***TESTING: testMeetingListIsOrdered***");
		Set<Contact> set1 = test.getContacts("Sam");
		Calendar date1 = Calendar.getInstance();
		date1.set(1970, Calendar.JANUARY, 1);
		test.addNewContact("Alice", "new contact");
		Set<Contact> set2 = test.getContacts("Alice");
		Calendar date2 = Calendar.getInstance();
		date2.set(2015, Calendar.APRIL, 20);
		test.addNewPastMeeting(set1, date1, "new meeting in the past.");
		test.addFutureMeeting(set2, date2);
		List<Meeting> tempList = test.getAllMeetings();
		String output = printMeetings(tempList);
		assertEquals(output, "197001, 1\r\n201501, 0\r\n2015320, 2\r\n");		
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeetinginPast(){
		System.out.println("***TESTING: testGetFutureMeetinginPast***");
		Set<Contact> set = test.getContacts("Sam");
		Calendar date = Calendar.getInstance();
		date.set(1970, Calendar.JANUARY, 1);
		test.addNewPastMeeting(set, date, "new meeting in the past.");
		test.getFutureMeeting(1);
	}

	@Test(expected=IllegalStateException.class)
	public void testAddMeetingNotes(){
		System.out.println("***TESTING: testAddNotes***");
		test.addMeetingNotes(0, "meeting now in past");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetPastMeetingFuture(){
		System.out.println("***TESTING: testGetPastMeetingFuture***");
		test.getPastMeeting(0);
	}

	@Test
	public void testGetPastMeeting(){
		System.out.println("***TESTING: testGetPastMeeting***");
		Calendar date = Calendar.getInstance();
		date.set(1970, Calendar.JANUARY, 1);
		Set<Contact> tempSet = test.getContacts(0);
		test.addNewPastMeeting(tempSet, date, "string");
		Meeting m = test.getPastMeeting(1);
		assertTrue(m instanceof PastMeeting);
	}

	@Test
	public void testGetMeetingNull(){
		System.out.println("***TESTING: testGetMeetingNull***");
		Meeting m = test.getMeeting(23);
		System.out.println(m);
		assertNull(m);
	}

	@Test
	public void testGetFutureMeetingListDate(){
		System.out.println("***TESTING: testGetFutureMeetingListDate***");
		Calendar date = test.getMeeting(0).getDate();
		List<Meeting> tempList = test.getFutureMeetingList(date);
		String output = printMeetings(tempList);
		assertEquals(output, "201501, 0\r\n");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeetingListContactUnknownContact(){
		System.out.println("***TESTING: testGetFutureMeetingListContactUnknownContact***");
		Contact alice = new ContactImpl("alice", 6);
		test.getFutureMeetingList(alice);
	}

	@Test
	public void testGetFutureMeetingListContact(){
		System.out.println("***TESTING: testGetFutureMeetingListContact***");
		Set<Contact> tempSet = test.getContacts(0);
		Contact[] contactArray = tempSet.toArray(new Contact[1]);
		List<Meeting> tempList = test.getFutureMeetingList(contactArray[0]);
		String output = printMeetings(tempList);
		assertEquals(output, "201501, 0\r\n");
	}

	@Test
	public void testSimpleLoad(){
		System.out.println("***TESTING: testSimpleLoad***");
		Set<Contact> set1 = test.getContacts("Sam");
		Calendar date1 = Calendar.getInstance();
		date1.set(1970, Calendar.JANUARY, 1);
		test.addNewContact("Alice", "new contact");
		Set<Contact> set2 = test.getContacts("Alice");
		Calendar date2 = Calendar.getInstance();
		date2.set(2015, Calendar.APRIL, 20);
		test.addNewPastMeeting(set1, date1, "new meeting in the past.");
		test.addFutureMeeting(set2, date2);
		test.flush();
		
		ContactManagerImpl newTest = new ContactManagerImpl();
		newTest.launch();
		List<Meeting> tempList = newTest.getAllMeetings();
		String output = printMeetings(tempList);
		assertEquals(output, "197001, 1\r\n201501, 0\r\n2015320, 2\r\n");
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

	/**
	* Formatting date to String (used in testing)
	* 
	* @return date in yyyymmdd format
	* @param date to convert into String and format
	*/
	public String formatDate(Calendar date){
		String year = String.valueOf(date.get(Calendar.YEAR));
		String month = String.valueOf(date.get(Calendar.MONTH));
		String day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));

		String result = year + month + day; //concatenate year, month and day
		return result;
	}


	/**
	* Printing method for meetings (does not print contacts of meeting)
	* 
	* @param Set to be printed
	* @return Formatted string of meeting IDs and Notes
	*/
	public String printMeetings(List<Meeting> list){
		Iterator<Meeting> iterator = list.iterator();
		String result = "";

		while (iterator.hasNext()){	
			Meeting m = iterator.next();
			result += formatDate(m.getDate()) + ", " + m.getId() + "\r\n";
		}

		System.out.println(result);
		return result;
	}

}
