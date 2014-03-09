import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TestingContactManagerImpl {
	private ContactManagerImpl test = new ContactManagerImpl();
	
	@Before
	public void setUp(){
		test.addNewContact("Sam", "he is someone");
		test.addNewContact("Sam", "second person");
		Calendar date = Calendar.getInstance();
		date.set(2015, 0, 1, 0, 0);
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
	public void testGetPastMeeting(){
		System.out.println("***TESTING: testGetPastMeeting***");
		Calendar date = Calendar.getInstance();
		date.set(1970, Calendar.JANUARY, 1);
		Set<Contact> tempSet = test.getContacts(0);
		test.addNewPastMeeting(tempSet, date, "string");
		Meeting m = test.getPastMeeting(1);
		assertTrue(m instanceof PastMeeting);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetPastMeetingFuture(){
		System.out.println("***TESTING: testGetPastMeetingFuture***");
		test.getPastMeeting(0);
	}

	@Test
	public void testGetPastMeetingNull(){
		System.out.println("***TESTING: testGetPastMeetingNull***");
		Meeting m = test.getPastMeeting(23);
		System.out.println(m);
		assertNull(m);
	}

	@Test
	public void testGetFutureMeeting(){
		System.out.println("***TESTING: testGetFutureMeeting***");
		Meeting m = test.getFutureMeeting(0);
		assertTrue(m instanceof FutureMeeting);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeetingIDinPast(){
		System.out.println("***TESTING: testGetFutureMeetingIDinPast***");
		Set<Contact> set = test.getContacts("Sam");
		Calendar date = Calendar.getInstance();
		date.set(1970, Calendar.JANUARY, 1);
		test.addNewPastMeeting(set, date, "new meeting in the past.");
		test.getFutureMeeting(1);
	}

	@Test
	public void testGetFutureMeetingNull(){
		System.out.println("***TESTING: testGetFutureMeetingNull***");
		Meeting m = test.getFutureMeeting(23);
		assertNull(m);
	}

	@Test
	public void testGetMeeting(){
		System.out.println("***TESTING: testGetMeeting***");
		Meeting m = test.getMeeting(0);
		String output = formatDate(m.getDate()) + ", " + m.getId();
		System.out.println(output);
		assertEquals(output, "201501, 0");
	}

	@Test
	public void testGetMeetingNull(){
		System.out.println("***TESTING: testGetMeetingNull***");
		Meeting m = test.getMeeting(23);
		System.out.println(m);
		assertNull(m);
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

	@Test(expected=IllegalArgumentException.class)
	public void testGetFutureMeetingListContactUnknownContact(){
		System.out.println("***TESTING: testGetFutureMeetingListContactUnknownContact***");
		Contact alice = new ContactImpl("alice", 6);
		test.getFutureMeetingList(alice);
	}

	@Test
	public void testGetFutureMeetingListContactEmpty(){
		System.out.println("***TESTING: testGetFutureMeetingListContactEmpty***");
		test.addNewContact("Alice", "new contact");		
		Set<Contact> set = test.getContacts("Alice");
		Contact[] contactArray = set.toArray(new Contact[1]);
		Contact alice = contactArray[0];
		List<Meeting> result = test.getFutureMeetingList(alice);
		String output = printMeetings(result);
		assertEquals(output, "");
	}

	@Test
	public void testGetFutureMeetingListDate(){
		System.out.println("***TESTING: testGetFutureMeetingListDate***");
		Set<Contact> set1 = test.getContacts("Sam");
		Calendar date1 = Calendar.getInstance();
		date1.set(2015, Calendar.JANUARY, 1, 12, 0);
		test.addNewContact("Alice", "new contact");
		Set<Contact> set2 = test.getContacts("Alice");
		Calendar date2 = Calendar.getInstance();
		date2.set(2015, Calendar.JANUARY, 1, 9, 0);
		test.addFutureMeeting(set1, date1);
		test.addFutureMeeting(set2, date2);
	
		Calendar searchDate = test.getMeeting(0).getDate();
		List<Meeting> tempList = test.getFutureMeetingList(searchDate);
		String output = printMeetings(tempList);
		assertEquals(output, "201501, 0\r\n201501, 2\r\n201501, 1\r\n");
	}

	//also tests addNewPastMeeting
	@Test
	public void testGetPastMeetingListContact(){
		System.out.println("***TESTING: testGetPastMeetingListContact***");
		Calendar date = Calendar.getInstance();
		date.set(1990, Calendar.APRIL, 29);
		Set<Contact> tempSet = test.getContacts(0);
		test.addNewPastMeeting(tempSet, date, "special day!"); 		
		Contact[] contactArray = tempSet.toArray(new Contact[1]);
		List<PastMeeting> tempList = test.getPastMeetingList(contactArray[0]);
		String output = printMeetings(tempList);
		assertEquals(output, "1990329, 1\r\n");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetPastMeetingListContactUnknownContact(){
		System.out.println("***TESTING: testGetPastMeetingListContactUnknownContact***");
		Contact alice = new ContactImpl("alice", 6);
		test.getPastMeetingList(alice);
	}

	@Test
	public void testGetPastMeetingListContactEmpty(){
		System.out.println("***TESTING: testGetPastMeetingListContactEmpty***");
		test.addNewContact("Alice", "new contact");		
		Set<Contact> set = test.getContacts("Alice");
		Contact[] contactArray = set.toArray(new Contact[1]);
		Contact alice = contactArray[0];
		List<PastMeeting> result = test.getPastMeetingList(alice);
		String output = printMeetings(result);
		assertEquals(output, "");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNewPastMeetingEmptyContactSet(){
		System.out.println("***TESTING: testAddNewPastMeetingEmptyContactSet***");
		Set<Contact> tempSet = new LinkedHashSet<Contact>();
		Calendar date = Calendar.getInstance();
		date.set(1990, Calendar.APRIL, 29);
		test.addNewPastMeeting(tempSet, date, "special day!");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddNewPastMeetingUnknownContact(){
		System.out.println("***TESTING: testAddNewPastMeetingUnknownContact***");
		Set<Contact> tempSet = new LinkedHashSet<Contact>();
		Contact alice = new ContactImpl("Alice", 0);
		tempSet.add(alice);
		Calendar date = Calendar.getInstance();
		date.set(1990, Calendar.APRIL, 29);
		test.addNewPastMeeting(tempSet, date, "special day!");
	}	

	@Test(expected=NullPointerException.class)
	public void testAddNewPastMeetingNullSet(){
		System.out.println("***TESTING: testAddNewPastMeetingNullSet***");
		Calendar date = Calendar.getInstance();
		date.set(1990, Calendar.APRIL, 29);
		Set<Contact> tempSet = null;
		test.addNewPastMeeting(tempSet, date, "special day!"); 		
	}

	@Test(expected=NullPointerException.class)
	public void testAddNewPastMeetingNullDate(){
		System.out.println("***TESTING: testAddNewPastMeetingNullDate***");
		Calendar date = null;
		Set<Contact> tempSet = test.getContacts(0);
		test.addNewPastMeeting(tempSet, date, "special day!"); 		
	}

	@Test(expected=NullPointerException.class)
	public void testAddNewPastMeetingNullNotes(){
		System.out.println("***TESTING: testAddNewPastMeetingNullNotes***");
		Calendar date = Calendar.getInstance();
		date.set(1990, Calendar.APRIL, 29);
		Set<Contact> tempSet = test.getContacts(0);
		test.addNewPastMeeting(tempSet, date, null); 		
	}

	@Test
	public void testAddMeetingNotes(){
		System.out.println("***TESTING: testAddMeetingNotes***");
		test.addNewContact("Alice", "new contact");
		Set<Contact> tempSet = test.getContacts("Alice");	
		Calendar date = Calendar.getInstance();
		int s = date.get(Calendar.SECOND);
		System.out.println(s);
		s = s + 1;
		date.set(Calendar.SECOND, s);
		int id = test.addFutureMeeting(tempSet, date);
		wait(date);
		test.addMeetingNotes(id, "meeting now in past");
		Meeting m = test.getPastMeeting(id);
		List<Meeting> tempList = test.getAllMeetings();
		String output = printMeetings(tempList);
		assertTrue(m instanceof PastMeeting);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testAddMeetingNotesMeetingNotExist(){
		System.out.println("***TESTING: testAddMeetingNotesMeetingNotExist***");
		test.addMeetingNotes(23, "meeting now in past");
	}

	@Test(expected=IllegalStateException.class)
	public void testAddMeetingNotesMeetingInFuture(){
		System.out.println("***TESTING: testAddMeetingNotesMeetingInFuture***");
		test.addNewContact("Alice", "new contact");
		Set<Contact> tempSet = test.getContacts("Alice");	
		Calendar date = Calendar.getInstance();
		date.set(2015, Calendar.JANUARY, 1);
		int id = test.addFutureMeeting(tempSet, date);
		test.addMeetingNotes(id, "yay");
	}

	@Test(expected=NullPointerException.class)
	public void testAddMeetingNotesNull(){
		System.out.println("***TESTING: testAddMeetingNotesNull***");
		test.addMeetingNotes(0, null);
	}

	@Test(expected=NullPointerException.class)
	public void testAddNewContactNullName(){
		System.out.println("***TESTING: testAddNewContactNullName***");
		test.addNewContact(null, "he is someone");
	}

	@Test(expected=NullPointerException.class)
	public void testAddNewContactNullNotes(){
		System.out.println("***TESTING: testAddNewContactNullNotes***");
		test.addNewContact("Sam", null);
	}

	@Test
	public void testGetContactsInts(){
		System.out.println("***TESTING: testGetContactsInts***");
		Set<Contact> tempSet = test.getContacts(1);
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 1, second person\r\n");
	}

	@Test
	public void testGetContactsIntsWithMultipleArgs(){
		System.out.println("***TESTING: testGetContactsIntsWithMultipleArgs***");
		Set<Contact> tempSet = test.getContacts(0,1);
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, second person\r\n");		
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
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, second person\r\n");
	}

	@Test
	public void testGetContactsPartial(){
		System.out.println("***TESTING: testGetContactsPartial***");
		Set<Contact> tempSet = test.getContacts("Sa");
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, second person\r\n");		
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

	@Test
	public void testDoubleLoadMeetings(){
		System.out.println("***TESTING: testDoubleLoadMeetings***");
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
		set1 = newTest.getContacts("Alice");
		date1 = Calendar.getInstance();
		date1.set(1999, Calendar.SEPTEMBER, 14);
		newTest.addNewContact("Sergio", "more marks plz.");
		set2 = newTest.getContacts("Sergio");
		date2 = Calendar.getInstance();
		date2.set(2016, Calendar.JUNE, 2);
		newTest.addNewPastMeeting(set1, date1, "meeting with Alice.");
		newTest.addFutureMeeting(set2, date2);
		newTest.flush();

		ContactManagerImpl finalTest = new ContactManagerImpl();
		finalTest.launch();				
		List<Meeting> tempList = finalTest.getAllMeetings();
		String output = printMeetings(tempList);
		assertEquals(output, "197001, 1\r\n1999814, 3\r\n201501, 0\r\n2015320, 2\r\n201652, 4\r\n");		
	}

	@Test
	public void testDoubleLoadContacts(){
		System.out.println("***TESTING: testDoubleLoadContacts***");
		test.addNewContact("Alice", ""); //testing empty notes String
		test.flush();
		
		ContactManagerImpl newTest = new ContactManagerImpl();
		newTest.launch();
		newTest.addNewContact("Sergio", "more marks plz.");
		newTest.flush();

		ContactManagerImpl finalTest = new ContactManagerImpl();
		finalTest.launch();				
		Set<Contact> tempSet = finalTest.getAllContacts();
		String output = printSet(tempSet);
		assertEquals(output, "Sam, 0, he is someone\r\nSam, 1, second person\r\nAlice, 2, \r\nSergio, 3, more marks plz.\r\n");	
	}

	//printing methods
	public String printSet(Set<Contact> set){
		Iterator<Contact> iterator = set.iterator();
		String result = "";

		while (iterator.hasNext()){
			Contact person = iterator.next();	
			result += person.getName() + ", " + person.getId() + ", " + person.getNotes() + "\r\n";
		}

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
	public String printMeetings(List<? extends Meeting> list){
		Iterator<? extends Meeting> iterator = list.iterator();
		String result = "";

		while (iterator.hasNext()){	
			Meeting m = iterator.next();
			result += formatDate(m.getDate()) + ", " + m.getId() + "\r\n";
		}

		return result;
	}

	private void wait(Calendar threshold){
		System.out.println("waiting until meeting is in the past...");
		Calendar now = Calendar.getInstance();
		while (now.before(threshold)){
			now = Calendar.getInstance();
		}
	}

}
