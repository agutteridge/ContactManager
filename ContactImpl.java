/**
 * This class will need to contain methods to read data
 * input from a tab-delimited text file. This could also 
 * take place via a 'reader' class that will parse the 
 * ident ints etc. 
 * <p>
 * Each line/row should represent an instance of ContactImpl, 
 * and each column should represent a member field.
 */
public class ContactImpl implements Contact {
	private final String name;
	private final int ident;
	private String notes;

	public ContactImpl(String name){
		this.name = name;
		this.ident = generateID();
		this.notes = "";
		// writeContact();
	}

	/**
	 * calls method which assesses whether ID is unique or not
	 * by searching through ContactSet (recursive)
	 * while boolean is false, keep generating IDs
	 * MOVE TO CLASS WHERE CONTACTSET IS?
	 */
	public int generateID(){
		int randomNumber = (int)(Math.random()*1000);
		System.out.println("ID for " + this.name + ": " + randomNumber);
		return randomNumber;
	}

	public int getId(){
		return this.ident;
	}

	public String getName(){
		return this.name;
	}

	public String getNotes(){
		return this.notes;
	}

	public void addNotes(String note){
		if (this.notes.equals("")){
			this.notes = note;
		} else {
			this.notes += ", " + note;		
		}
		// writeContact();
	}

	/**
	 * saves addition of/changes to contact by writing to
	 * a tab-delimited file (Contacts.txt)
	 */
	public void writeContact(){
		//methods
	}
}