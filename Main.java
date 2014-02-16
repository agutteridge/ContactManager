import java.util.Scanner;
import java.util.*;

public class Main {
	
	//testing now moved to JUnit

	public void contactImplTester(){
		ContactManagerImpl cmi = new ContactManagerImpl();
		cmi.addNewContact("Sam", "he is someone");
		cmi.addNewContact("Sam", "");
		Set<Contact> tempSet = cmi.getContacts("Sam");
		String output = cmi.prettyPrint(tempSet);
		System.out.println(output);
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