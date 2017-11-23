
import java.util.Scanner;
import java.lang.IllegalArgumentException;
import java.util.Stack;
import java.util.ArrayList;


public class soko {

    static int R, C, destR, destC;
    static int box;
    static int[][] arr;
    static Graph graph;
    static int vertices;
    static Stack stackVertix;
    static ArrayList<Integer> copyVertices;             // for moving through vertices

    static void read()throws Exception {
        Scanner sin = new Scanner(System.in);
        R = sin.nextInt();
        C = sin.nextInt();
        if(R ==0 && C==0){
            System.out.println("No graph inputted");
        }
        else{

        }
        vertices = R*C;
        arr = new int[R][C];
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                arr[i][j] = 0;
            }
        }
        sin.nextLine();
        for (int i = 0; i < R; i++) {
            String tmp = sin.nextLine();
            for (int j = 0; j < C; j++) {                
                char ch = tmp.charAt(j);
                switch (ch) {
                    case '#':
                        arr[i][j] = -1;
                        break;
                    case 'B':
                        arr[i][j] = 1;
                        box = getIndex(i,j);
                        break;
                    case 'D':
                        destR = i;
                        destC = j;
                    default:
                        arr[i][j] = 0;
                }
            }
        }
    }
    /*

    */
    static void createGraph(){
        graph = new Graph(vertices); 
        // System.out.println(vertices);
        for(int i=0;i<R;i++){
            for(int j=0; j<C; j++){
                if(isFree(i,j)){
                   //check up // (j-1>=0 && j<C)  &&
                    if(checkObstacleVert(i,j)){
                        graph.addEdge(getIndex(i-1,j), getIndex(i,j));
                        graph.addEdge(getIndex(i+1,j), getIndex(i,j));
                    }
                    if(checkObstacleHor(i,j)){
                        graph.addEdge(getIndex(i,j-1), getIndex(i,j));
                        graph.addEdge(getIndex(i,j+1), getIndex(i,j));
                    } 
                }
                
            }
        }
    }
    static int getIndex(int x, int y){
        return (x*(R-1) + y) ;
    }

    static boolean isFree(int x, int y){
        if(x>=0 && x< R && y>=0 && y< C){
            if(arr[x][y]==-1){
                return false;
            }
            // System.out.println("                                    x :" + x + "-" + "y :" + y + "--- true");
            return true;
        }
        else return false;
        
    }
    static boolean checkObstacleVert(int x, int y){
         if(isFree(x-1, y) && isFree(x+1,y)){
            // System.out.println("x :" + x + "-" + "y :" + y + "--- true");
            return true;
        }
        else return false;
    }
    static boolean checkObstacleHor(int x, int y){
        if(isFree(x,y-1) && isFree(x, y+1)){
            // System.out.println("x :" + x + "-" + "y :" + y + "--- true");
            return true;
        }
        else return false;
    }

    static int findShortest(){
        int dest= getIndex(destR, destC);
        copyVertices = new ArrayList<Integer>();
        int distance =0;
        boolean found =false;
        // System.out.println("box =" + box);
        stackVertix = new Stack();
        stackVertix.push(graph.edges()[box]);
        while(found==false){
            while(stackVertix.empty()==false){
                Bag<Integer> current =(Bag<Integer>)stackVertix.pop();
                for(Integer x : current){
                    if(x == dest){
                        found = true;
                    }
                    copyVertices.add(x);
                }
            }
            distance++;
            for(Integer vertex : copyVertices){
                stackVertix.push(graph.edges()[vertex]);
            }
            copyVertices.clear();
        }
        return distance;
        
    }

    
    public static void main(String[] args)throws Exception {
        read();
        createGraph();
        // System.out.print(graph.printEdges());
        System.out.println(findShortest());
    }
}