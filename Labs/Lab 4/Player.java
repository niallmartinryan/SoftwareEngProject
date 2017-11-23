
public class Player{

	private int id;
	private int x;
	private int y;

	public Player(int id, int x, int y){
		this.id = id;
		this.x = x;
		this.y = y;
	}
	public int id(){
			return id;
	}
	public int x(){
			return x;
	}
	public int y(){
			return y;
	}
	public void x(int x){
			this.x = x; 
	}
	public void y(int y){
			this.y = y;
	}

}