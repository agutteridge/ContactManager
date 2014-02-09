import java.util.Scanner;

public class Main {
	
	public void contactImplTester(){
		ContactImpl alice = new ContactImpl("Alice");
		System.out.println(alice.getName());
		System.out.println(alice.getId());
		alice.addNotes("something something");
		alice.addNotes("and again");
		System.out.println(alice.getNotes());

		System.out.println("");
		System.out.println("Launching ContactManagerImpl tester...");
		contactManagerImplTester();
	}

	public void contactManagerImplTester(){
		ContactManagerImpl database = new ContactManagerImpl();
		database.addNewContact("sam", "he is someone");
		database.addNewContact("turtle", "he is a turtle");
		database.addNewContact("ghost", "transparent & friendly");
		database.getContacts(0,1,2,3,4,5,6,7,8,9,10);
		database.getContacts("sam");		
	}

	public static void main(String[] args) {
		Scanner in = new Scanner (System.in);
		Main launcher = new Main();
		boolean input = false;
		System.out.println("Please enter 'T' for testing module, and 'E' for execution module");

		while (!input){
			String command = in.nextLine();
			if (command.toUpperCase().equals("T")){
				input = true;
				launcher.contactImplTester();
			} else {
				System.out.print("Please re-enter your choice: ");
			}
		}
	}
}