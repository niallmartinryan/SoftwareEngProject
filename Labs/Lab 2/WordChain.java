import java.util.Scanner;
import java.util.ArrayList;

public class WordChain{
	

	public static int numWords;
	public static ArrayList<String> words;

	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		// String input ="";
		numWords = scanner.nextInt();
		for(int i=0;i< numWords;i++ ){


			words.add((String)scanner.nextLine);
		}
		for(String word : words){

				System.out.println(word);
		}








	}

}







