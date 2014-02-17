import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TestingNewMeeting {
	private NewMeeting test;

	@Test
	public void testGenerateId(){ //unique IDs must be generated within ContactManagerImpl
		test = new NewMeeting();
		Calendar date1 = new GregorianCalendar(2014, 2, 15);
		int output = test.generateId(date1, 8);
		assertEquals(output, 201402158);
	}

	//THESE EXCEPTIONS SHOULD BE IN ADDFUTUREMEETING OF CONTACTMANAGERIMPL
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorPastMeeting(){
		Set<Contact> set = new LinkedHashSet<Contact>();
		Contact alice = new ContactImpl("Alice", 0);
		set.add(alice);		
		Calendar date = new GregorianCalendar(1970, 1, 1);
		test = new NewMeeting(date, set);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testConstructorEmptyContactSet(){
		Calendar date = new GregorianCalendar(1970, 1, 1);
		test = new NewMeeting();
	}
}