import java.util.Scanner;
import java.lang.IllegalArgumentException;
import java.util.Stack;
import java.util.ArrayList;


public class huntForGold{

	static int M, N;
	static int P;
	static Node [][] maze;
	static Player [] players;
	static boolean winner;


	static int player(){
		winner = false;
		while(winner != true){
			for(int i=0;i<players.length;i++){
				Player currPlayer = players[i];

				if(maze[currPlayer.x()][currPlayer.y()].type() == 1){										// on gold position
					return currPlayer.id();
				}
				else if(maze[currPlayer.x()][currPlayer.y()].type() ==0){
					Node pos = maze[currPlayer.x()][currPlayer.y()];
					int newX = pos.displacementX() + currPlayer.x();
					int newY = pos.displacementY() + currPlayer.y();
					if(validPos(newX,newY)){						// shouldnt have to check this.. if Im doing it for the wrap
						currPlayer.x(newX);
						currPlayer.y(newY);
					}
					else{		// have to wrap it around ^^
						if(currPlayer.x()<0){
							return currPlayer.x() + M;
						}
						else if(currPlayer.x()>=M){
							return currPlayer.x() % M;
						}
						if(currPlayer.y()<0){
							return currPlayer.y() + N;
						}
						else if(currPlayer.y()>=N){
							return currPlayer.y() % N;
						}
					}

				}
				else{
					// currPlayer is currently trapped and will never move..
				}
			}
		}
		return -1; //should never get to here....
	}
	static boolean validPos(int x, int y){		// check this method....
		if(x<0 || y<0 || x>=M || y>= N){
			return false;
		}
		return true;
	}


	static void read()throws Exception{
		Scanner sin = new Scanner(System.in);
		M = sin.nextInt();
		N = sin.nextInt();
		if(M<=0 || N <=0 ){
			System.out.println("No graph detected");
		}
		maze = new Node[M][N];
		sin.nextLine();
		int k=0;											// track which character is being accessed, as number of cells doesnt match input chars
		for(int i=0;i<M;i++){
			String tmp = sin.nextLine();
			k=0;
			for(int j=0; j<N;j++){
				String [] parts = tmp.split(" ");
				for(int l =0; l<parts.length;l++){
				}
				char ch = parts[k].charAt(0);
				switch(ch){
					case 'D':
						int x = Integer.parseInt(parts[k+1]);
						int y = Integer.parseInt(parts[k+2]);
						// int x = Integer.getInteger(Character.toString(tmp.charAt(k+1)));
						// int y = Integer.getInteger(Character.toString(tmp.charAt(k+2)));
						k= k+2;
						maze[i][j] = new Node(0, x, y);														//	HERE!!!!
						break;
					case 'G':
						maze[i][j] = new Node(1);
						break;
					case 'T':
						maze[i][j] = new Node(2);
						break;
				}
				k++;
			}	
		}
		P = sin.nextInt();
		players = new Player[P];
		sin.nextLine();
		for(int i=0;i<P;i++){
			String tmp = sin.nextLine();								// 3 is the input...
			String [] parts = tmp.split(" ");
			int id = Integer.parseInt(parts[0]);
			int x = Integer.parseInt(parts[1]);
			int y = Integer.parseInt(parts[2]);
			players[i] = new Player(id,x,y);
		}
	}
	static void printMaze(){
		System.out.println("MAZE\n");
		for(int i=0;i<M;i++){
			for(int j=0;j<N;j++){
				System.out.print(maze[i][j].type() +" ");	
			}
			System.out.println("");
		}
	}


	public static void main(String []args)throws Exception{
		//testing methods..

		//
		read();

		System.out.println(player());

		//printMaze();

	}




}