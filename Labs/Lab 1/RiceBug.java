public class RiceBug{
	
	public int strength;
	public char direction;		// dont know if this will stay as an int;
	public int[] location;
	public int bugsSlain;
	public int startTime;
	public boolean alive;



	public RiceBug( int [] location, int startTime ,char direction){
		this.location = new int[2];
		strength = 0;
		this.direction = direction;
		this.location[0] = location[0];
		this.location[1] = location[1];
		bugsSlain = 0;
		this.startTime = startTime;
		alive = false;
	}

	public int[] buglocation(){

		return null;	
	}
	public int getRow(){
		return location[0];
	}
	public int getCol(){
		return location[1];
	}
	







	
}