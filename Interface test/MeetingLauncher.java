import java.util.*;

public class MeetingLauncher {
	private List<Meeting> toyList;

	public void launch(){
		Calendar date1 = new GregorianCalendar(2015, 1, 1);
		Contact contact1 = new ContactImpl("Alice", 29);
		Set<Contact> set1 = new LinkedHashSet<Contact>();
		set1.add(contact1);
		Meeting meeting1 = new Fut(30, date1, set1);
		
		if(toyList == null){
			toyList = new ArrayList<Meeting>();
		}
		toyList.add(meeting1);

		System.out.println("Meeting 1 added: Future");
		System.out.println("Is Meeting 1 an instance of Fut? " + (meeting1 instanceof Fut));
		System.out.println("Is Meeting 1 an instance of Pas? " + (meeting1 instanceof Pas));
		System.out.println("");

		addNotes(0, "hello notes");
		System.out.println("Meeting 1 re-added as: Past");
		System.out.println("Is Meeting 1 (local) an instance of Fut? " + (meeting1 instanceof Fut));
		System.out.println("Is Meeting 1 (local) an instance of Pas? " + (meeting1 instanceof Pas));
		System.out.println("");

		meeting1 = toyList.get(0);
		System.out.println("Is Meeting 1 (field) an instance of Fut? " + (meeting1 instanceof Fut));
		System.out.println("Is Meeting 1 (field) an instance of Pas? " + (meeting1 instanceof Pas));
		System.out.println("");
	}

	private void addNotes(int id, String text){
		Meeting meetingToCast = toyList.get(0); 
		Calendar dateToTransfer = meetingToCast.getDate();
		Set<Contact> setToTransfer = meetingToCast.getContacts();

		Meeting meetingNowInPast = new Pas(id, dateToTransfer, setToTransfer, text);
		toyList.remove(meetingToCast);
		toyList.add(meetingNowInPast);
	}

	public static void main(String[] args) {
		MeetingLauncher ml = new MeetingLauncher();
		ml.launch();
	}
}