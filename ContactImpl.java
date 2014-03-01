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

	public ContactImpl(String name, int ident){
		this.name = name;
		this.ident = ident;
		this.notes = null;
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
		if (this.notes == null){
			this.notes = note;
		} else {
			this.notes += ", " + note;		
		}
	}
}
