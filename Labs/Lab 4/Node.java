import java.util.ArrayList;


public class Node{

	private int type;						// gold, trap , Displacement square
	private int displacementX;
	private int displacementY;

	public Node(int type){
		this.type = type;
	}
	public Node(int type, int x, int y){
		this.type = type;
		displacementX = x;
		displacementY = y;
	}
	public int type(){
		return type;
	}
	public int displacementX(){
		return displacementX;
	}
	public int displacementY(){
		return displacementY;
	}











}