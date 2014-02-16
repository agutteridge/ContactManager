import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class TestingNewMeeting {
	private NewMeeting test = new NewMeeting();

	@Test
	public void testGenerateId(){ //unique IDs must be generated within ContactManagerImpl
		Calendar date1 = new GregorianCalendar(2014, 2, 15);
		int output = test.generateId(date1, 8);
		assertEquals(output, 201402158);
	}


}