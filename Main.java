import java.util.Scanner;

// extends ContactManager ???
// rename ContactManagerImpl ???
public class Main {
	
	public void contactImplTester(){
		ContactImpl alice = new ContactImpl("Alice");
		System.out.println(alice.getName());
		System.out.println(alice.getId());
		alice.addNotes("something something");
		alice.addNotes("and again");
		System.out.println(alice.getNotes());
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