// le000422

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;


public class WordGuessingGame {
	private static ArrayList<String> myWords = new ArrayList <String>(); // words from words.txt
	private static int countWords = 0; // number of words in words.txt
	private  static ArrayList<Integer> usedWords = new ArrayList<Integer>(); // words that have been used

	// get words from words.txt
	public static void getWordsList(String fileName) throws Exception{
		File f = new File(fileName);
		Scanner readFile = new Scanner(f);
		while (readFile.hasNext()){
			myWords.add(readFile.next());
			countWords++;
		}
		readFile.close();
	}

	public static String findingAWord() throws Exception{
		String word;
		int index;

		Random rand = new Random();
		boolean checkWord = false;

		// check to see if the target word has been used or contains 2 identical characters.
		do{
			index = rand.nextInt(countWords);
			word = myWords.get(index);
			checkWord = wordOk(word, "history.txt"); // method to check a word
		}while(checkWord == false);

		return word;
	}

	public static boolean wordOk(String word, String fileName) throws Exception{

		// check to see if a word has been used or not
		for (int i = 0; i < word.length()-1; i ++){
			for (int j = i +1; j < word.length(); j++){
				if (word.charAt(i) == word.charAt(j)){
					return false;
				}
			}
		}

		// check to see if a word has been used or not
		File f = new File(fileName);
		Scanner readFile = new Scanner(f);
		String usedWord;
		while(readFile.hasNext()){
			usedWord = readFile.nextLine();
			if (Character.isLowerCase(usedWord.charAt(0))){
				if (usedWord.equals(word)){
					return false;
				}
			}
		}
		return true;
	}

	// the game
	public static void guessAWord(String word, Scanner scan, ArrayList<String> green, ArrayList<String> yellow, ArrayList<String> gray) throws Exception{
		int guesses = 0;
		String userInput;
		ArrayList<String> guessWords = new ArrayList<String>(); // store each of the valid attemps of the player
		ArrayList<String> displayGuessWord = new ArrayList<String>(); // store the display of the attempts of the player.
		System.out.println("The target word is: "+ word); // the target word

		while(guesses != 6){
			System.out.print("Enter a guess: ");

			// check input: 5 characters
			userInput = scan.nextLine();
			while(userInput.length() != 5){
				System.out.print("Enter a 5-character word: ");
				userInput = scan.nextLine();
			}
			guesses++;
			guessWords.add(userInput.toUpperCase());

			// add character to suitable list
			for (int i = 0; i < 5; i++){
				if (word.indexOf(userInput.charAt(i)) != -1){
					if (word.charAt(i) == (userInput.charAt(i))){
						green.add(String.valueOf(userInput.charAt(i)).toUpperCase());
					}
					else {yellow.add(String.valueOf(userInput.charAt(i)).toUpperCase());}
				}
				else {gray.add(String.valueOf(userInput.charAt(i)).toUpperCase());}
			}

			// generate the display of the most recent attempt of the player
			String guessDisplay = displayGuesses(userInput,word);

			// add the display format to the display arraylist
			displayGuessWord.add(guessDisplay);

			// display the attempts and the keyboard
			for (int i = 0; i < displayGuessWord.size(); i++){
				System.out.print(displayGuessWord.get(i));
			}
			System.out.println();
			System.out.println(displayKeyboard(green, yellow, gray));

			if (userInput.equals(word)){
					System.out.println("Congratulations! The word is " + word);
					break;
				}

			if (guesses == 6) {System.out.println("Loser! The word is " + word);}
			
		}

		// add the game history to a file
		addHistory("history.txt", word, guessWords);
	}


	public static String displayGuesses(String guessWord, String targetWord){
		String display = "";
		final String ANSI_RESET = "\u001b[0m";
		final String ANSI_GREEN = "\u001B[42m";
		final String ANSI_YELLOW = "\033[0;103m";
		final String ANSI_GRAY = "\u001B[100m";
		
		String temp;

		for (int i = 0; i < 5; i++){
				if (targetWord.indexOf(guessWord.charAt(i)) != -1){
					if (guessWord.charAt(i) == (targetWord.charAt(i))){
						display = display + ANSI_GREEN + Character.toUpperCase(guessWord.charAt(i));
					}
					else {display = display + ANSI_YELLOW + Character.toUpperCase(guessWord.charAt(i));}
				}
				else {display = display + ANSI_GRAY + Character.toUpperCase(guessWord.charAt(i));}
			}

		display = display + ANSI_RESET + "\n";
		return display;
	}

	public static String displayKeyboard(ArrayList<String> green, ArrayList<String> yellow, ArrayList<String> gray){
		String keyboard = "ABCDEFGHI\nJKLMNOPQR\nSTUVWYZ";
		String result = "";
		String temp;
		final String ANSI_RESET = "\u001b[0m";
		final String ANSI_GREEN = "\u001B[42m";
		final String ANSI_YELLOW = "\033[0;103m";
		final String ANSI_GRAY = "\u001B[100m";

		for (int i = 0; i < keyboard.length(); i++){
			temp = "" + keyboard.charAt(i);
			if(green.contains(temp)){
				result = result + ANSI_GREEN + temp + ANSI_RESET;
			}
			else if(yellow.contains(temp)){
				result = result + ANSI_YELLOW + temp + ANSI_RESET;
			}
			else if (gray.contains(temp)){
				result = result + ANSI_GRAY + temp + ANSI_RESET;
			}
			else result = result + temp;
		}
		return result;
	}

	public static void addHistory(String fileName, String targetWord, ArrayList<String> attempts) throws Exception{
		BufferedWriter out = new BufferedWriter(new FileWriter(fileName, true));
		out.write(targetWord + "\n");
		for (int i = 0; i < attempts.size(); i++){
			out.write(attempts.get(i) + "\n");
		}
		out.close();
	}

	public static void main(String [] args) throws Exception{
		// create the history.txt
		File f = new File("history.txt");
		FileOutputStream fos = new FileOutputStream(f);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		bw.write("PLAYER'S HISTORY\n");
		bw.close();

		// get the words list
		getWordsList("words.txt");
		String targetWord;
		Scanner scan = new Scanner(System.in);
		String userInput;
		
		do{
			ArrayList<String> green = new ArrayList<String>();
			ArrayList<String> yellow = new ArrayList<String>();
			ArrayList<String> gray = new ArrayList<String>();

			// choose a word
			targetWord = findingAWord();

			// start a game
			guessAWord(targetWord, scan, green, yellow, gray);
			System.out.print("Continue? y/n: ");
			userInput = scan.nextLine();
		}while(userInput.equals("y") == true);
		scan.close();

		System.out.println("Goodbye!");
	}
}