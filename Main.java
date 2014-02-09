import java.util.Scanner;

public class Main {
	
	//testing now moved to JUnit

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