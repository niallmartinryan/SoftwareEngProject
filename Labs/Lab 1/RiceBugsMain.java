import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class RiceBugsMain{

	public static RiceBug [] bugs;
	public static Field field;
	public static int currentDuration;
		//
	public	String currentLine;
	public static void main(String [] args){




		//URL path = RiceBugsMain.class.getResource("testfile.txt");
		//File f = new File(path.getFile());

		// Have to scrap because of commandLine input...
		// File f = new File("testfile.txt");
		// System.out.println(f);
		// try{
		// 	BufferedReader br = new BufferedReader(new FileReader(f));	// remember to edit code...
							
		// 	String input = "";	// This is the max size of the input.. <15 bugs.. a bug has 4 ints 4*14 +4 = 60
		// 	while((currentLine = br.readLine()) != null){
		// 		input += currentLine + " ";
		// 	}
		// 	String[] tokens = input.split(" ");
		// 	int k=0;								// dont need to print these out anymore..
		// 	for(String token : tokens){
		// 		System.out.println(k+ " : " + token);
		// 		k++;
		// 	}
		// 	field = new Field(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
		// 	System.out.println("amount of bugs :" + tokens[3]);
		// 	bugs = new RiceBug[Integer.parseInt(tokens[3])];
		// 	int[] bugLocationTemp = new int[2]; 

		// 	for(int i=3, j=0; j< bugs.length; i+=4, j++){								// adds all bugs.. intialisation
		// 		System.out.println("i :"+ i);
		// 		bugLocationTemp[0] = Integer.parseInt(tokens[i+1]);
		// 		bugLocationTemp[1] = Integer.parseInt(tokens[i+2]);
		// 		bugs[j] = new RiceBug(bugLocationTemp,Integer.parseInt(tokens[i+3]),tokens[i+4].charAt(0));
		// 	}



		// 	System.out.println("\n"+ input +"\n");
		// 	// create the array of riceBugs
		// }
		// catch(FileNotFoundException ex){
		// 	System.out.println("exception : " + ex);
		// }
		// catch(IOException ioex){
		// 	System.out.println("exception : "+ ioex);
		// }



		Scanner scanner = new Scanner(System.in);
		String input = "";
		String tempInput = "";
		int count = 0;											// This is to wait until the amount of bugs has been inputted
		int inputTerminate = -1;
		boolean endOfInput = false;

		while( endOfInput != true ){
			tempInput = scanner.nextLine();
				input += tempInput + " ";
			if(count == inputTerminate){	
				endOfInput = true;
			}
			else{
				if(count ==2 && inputTerminate == -1){				// '2' correct line for the input of amount of bugs -- SO it doesnt enter this if statement
																	// after the value for bugs has been added
					count =0;										// going to re-use count variable to count the amount of bugs
					inputTerminate = Integer.parseInt(tempInput);	// grab the amount of bugs
				}
				count++;
			}
		}
		// String input = "6 6 25 5 2 3 10 H 1 1 5 E 1 2 6 D 3 3 18 A 4 5 2 E";
		String [] tokens = input.split(" ");
		field = new Field(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
		bugs = new RiceBug[Integer.parseInt(tokens[3])];
		int[] bugLocationTemp = new int[2];

		for(int i=3, j=0; j< bugs.length; i+=4, j++){								// adds all bugs.. intialisation

			bugLocationTemp[0] = Integer.parseInt(tokens[i+1]);
			bugLocationTemp[1] = Integer.parseInt(tokens[i+2]);
			bugs[j] = new RiceBug(bugLocationTemp,Integer.parseInt(tokens[i+3]),tokens[i+4].charAt(0));
		}
		simulation();
		output();

		//input finished












	}
	// Each phase should be a method that can be called in RiceBugsMain..
	// If a bug is intialised.. it will infect and not move...
	public static void simulation(){
		currentDuration = 0;
		while(currentDuration != field.duration){
			move();
			spawn();
			killBugs();
			fightPhase();
			infect();
			// increaseStrength();
			currentDuration++;
		}
	}
	public static void output(){
		int nonInfected =0;
		int bugsAlive =0;
		for(int i=0 ;i<field.row;i++){
			for(int j=0;j<field.column;j++){
				if(field.plantArray[i][j].isInfected()==false){
					nonInfected++;
				}
			}
		}
		for(int i=0;i<bugs.length;i++){
			if(bugs[i].alive ==true){
				bugsAlive++;
			}
		}
		System.out.println(nonInfected + " " + bugsAlive + "\n");

	}
	public static void move(){
		for(int i=0 ;i<bugs.length; i++){
			if(bugs[i].alive == true){
				moveBug(bugs[i]);
			}
		}
	}

	public static void spawn(){
		for(int i=0;i<bugs.length;i++){
			RiceBug currBug = bugs[i];
			if(currentDuration==currBug.startTime){
				field.plantArray[currBug.location[0]][currBug.location[1]].addBug(currBug);	// Adds the bug to the plant
			}
		}

	}
	public static void killBugs(){														// This method kills bugs that are outside of the field
		for(int i=0;i<bugs.length;i++){													//(run into a barrier)
			RiceBug currBug = bugs[i];
			if(currBug.alive == true){
				if(currBug.location[0]<0 || currBug.location[0]>=field.row || currBug.location[1]<0 || currBug.location[1]>= field.column){
					currBug.alive = false;
				}
			}
		}
	}
	public static void fightPhase(){
		for(int i=0;i<bugs.length;i++){
			RiceBug bug = bugs[i];
			if(bug.alive == true){
				if(field.plantArray[bug.location[0]][bug.location[1]].howManyBugs()>1){		// should make a var for these locations..
					fight(field.plantArray[bug.location[0]][bug.location[1]]);
				}
			}
		}
	}
	public static void fight(Plant plant){
		boolean split = false;
		boolean newHighest = false;
		ArrayList<RiceBug> bugs = plant.bugs;
		ArrayList<RiceBug> topBugs = new ArrayList<RiceBug>();
		RiceBug currentHighest = bugs.get(0);
		for(int i=1;i<bugs.size();i++){
			if(currentHighest.strength<bugs.get(i).strength){
				currentHighest.alive = false;
				plant.removeBug(currentHighest);
				currentHighest = bugs.get(i);
				newHighest = true;
				for(int j=0;j<topBugs.size();j++){
					plant.removeBug(topBugs.get(i));
				}
				topBugs.clear();
				split=false;
			}
			else if(currentHighest.strength!=bugs.get(i).strength){
				bugs.get(i).alive = false;
				plant.removeBug(bugs.get(i));
			}
			else if(currentHighest.strength== bugs.get(i).strength){
			 	topBugs.add(bugs.get(i));
			 	split = true;
			 }
		}
		// if(split==true){		// this means there is a split for the bug with the highest strength
		// }
	}
	public static void infect(){
		for(int i=0;i<bugs.length;i++){
			RiceBug currBug = bugs[i];
			if(bugs[i].alive == true){
				Plant currPlant = field.plantArray[currBug.getRow()][currBug.getCol()];
				if(currPlant.isInfected() == false){
					increaseStrength(currPlant);
					currPlant.infected = true;
				}
			}
		}
	}
	public static void increaseStrength(Plant plant){							// increases strength of all bugs in this location
		ArrayList<RiceBug> bugs = plant.bugs;
		for(int i=0;i<bugs.size();i++){
			bugs.get(i).strength++;
		}

	}

	public static void moveBug(RiceBug bug){

		char direction = bug.direction;
		field.plantArray[bug.location[0]][bug.location[1]].removeBug(bug);
		switch(direction){

			case 'A':
				bug.location[0]--;
				bug.location[1]--;
				break;
			case 'B':
				bug.location[0]--;
				break;
			case 'C':
				bug.location[0]--;
				bug.location[1]++;
			case 'D':
				bug.location[1]--;
				break;
			case 'E':
				bug.location[1]++;
				break;
			case 'F':
				bug.location[0]++;
				bug.location[1]--;
				break;
			case 'G':
				bug.location[0]++;
				break;
			case 'H':
				bug.location[0]++;
				bug.location[1]++;
				break;
		}	
		if(bug.location[0]<0 || bug.location[0]>=field.row || bug.location[1]<0 || bug.location[1]>= field.column){

		}
		else{
			field.plantArray[bug.location[0]][bug.location[1]].addBug(bug);
		}	
		
	}



}