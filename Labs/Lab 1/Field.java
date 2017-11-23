public class Field{
	
	public Plant [][] plantArray;	
	public int duration;
	int row;
	int column;		

	public Field(int rows, int columns , int duration){			// size wont exceed 6x6
		plantArray = new Plant[rows][columns];
		for(int i=0;i<rows;i++){
			for(int j=0;j<columns;j++){
				plantArray[i][j] = new Plant(i,j);
			}
		}
		this.duration =  duration;
		this.row = rows;
		this.column = columns;
	}

	public void updateBugsLocation(RiceBug [] bugs){			// Dont think this should be here
		
	}
	public int [] getDimensions(){
		int [] array = {row,column};
		return array;
	}











	
}