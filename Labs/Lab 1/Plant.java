import java.util.ArrayList;

public class Plant{

	public ArrayList<RiceBug> bugs;
	public int row;
	public int column;
	public boolean infected;
	public Plant(int row, int column){
		infected = false;
		bugs = new ArrayList<RiceBug>();
		this.row = row;
		this.column = column;
	}
	// sample functions
	public void addBug(RiceBug bug){
		bug.alive = true;
		bugs.add(bug);
	}
	public void removeBug(RiceBug bug){
		bugs.remove(bugs.indexOf(bug));
	}
	public int howManyBugs(){
		return bugs.size();
	}
	// public void clearPlant(){

		
	// }
	public boolean isInfected(){
		return infected;
	}





}