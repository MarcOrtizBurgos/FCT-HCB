
package jcadafalchproject.JocPenjat;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author jcadafalch
 */
public class Penjat {

    private final static int MAXIM_ERRORS = 6;
    private final static Scanner sc = new Scanner(System.in);
    private final static Random rand = new Random();

    // private method that choose a word randomly
    private static String chooseWord() {
        String[] arrWords = {"ATACAR", "CALFREDS", "APROPAR", "FUNCIONARI",
            "MESURAR", "OFICIAL", "SIGNATURA", "LLEGENDA", "PARAFANGS", "SIGNATURA"};

        int chooseWord = rand.nextInt(arrWords.length);
        return arrWords[chooseWord];
    }
    
    // private method that check if the letter has been repeated
    private static boolean repeatedLetter(char c, String s) {
        boolean repeatWord = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                repeatWord = true;
            }
        }

        return repeatWord;
    }

    public static void main(String[] args) {
        boolean newGame = true; 
        
        // loop that will still working unless the user wants to stop playing
        do {
            boolean finishRound = false, yesNoAnswer = false;
            int errors = 0, wordLength = 0;
            String tryedLetters = "";

            String secretWord = chooseWord();
            int nLettersSecretWord = secretWord.length();
            char[] xWord = new char[nLettersSecretWord];

            for (int i = 0; i < xWord.length; i++) {
                xWord[i] = 'X';
            }
            
            //loop that will still working unless the round has finished
            do {
                if (MAXIM_ERRORS - errors == 0) { 
                //if user errors are equal to the maximum allowed, a message will be printed
                    System.out.println("Has pedut, la paraula secreta era: " + secretWord);
                    finishRound = true;
                } else if (wordLength == secretWord.length()) {
                // if user hits all the letter, it will be shown a congratulations message
                    System.out.println("Enhorabona, has acertat la paraula correcta");
                    finishRound = true;
                } else {
                    System.out.println(xWord);
                    System.out.println("Errors permesos restants: " + (MAXIM_ERRORS - errors));
                    System.out.println("Lletres provades: " + tryedLetters);
                    System.out.print("Introduix una lletra: ");
                    char letter = sc.next().toUpperCase().charAt(0);
                    if (repeatedLetter(letter, tryedLetters)) {
                        // calls repeatedWord to check if that letter has already 
                        //been entered
                        System.out.println("Aquesta lletra ja l'has probat!\n");
                    } else if(!Character.isLetter(letter)){ 
                        // if char letter is not a character print a message error
                        System.out.println("ERROR! El valor introduit no es una llettra.\n "
                                + "Tens que introduir una lletra\n");
                    }else {
                        tryedLetters += letter;
                        boolean guessCorrectWord = false;

                        for (int i = 0; i < secretWord.length(); i++) {
                            if (secretWord.charAt(i) == letter) {
                                // If the correct letter is "X" on console it will apear 
                                // in lowercase, so that it differs from the letters 
                                // not yet guessed
                                if (letter == 'X') {
                                    xWord[i] = Character.toLowerCase(letter);
                                }else{
                                    xWord[i] = letter;
                                }
                                
                                guessCorrectWord = true;
                                wordLength++;
                            }
                        }
                        // if guessCorrectWord is false it means that the word doesn't
                        // contain that letter.
                        if (!guessCorrectWord) {
                            System.out.println("Aquesta paraula no conté la lletra: " + letter);
                            errors++;
                        }
                        System.out.println("\n");
                    }

                }
            } while (!finishRound);
            
            do {
                System.out.print("Vols jugar una altra partida (S/N): ");
                char YesNo = sc.next().toUpperCase().charAt(0);
                if (YesNo == 'S') { // if the answer is Yes, starts another round
                    System.out.println("\n \n \n");
                    System.out.println("Nova Partida");
                    yesNoAnswer = true;
                } else if (YesNo == 'N') { // if the answer is No, the program is over
                    newGame = false;
                    yesNoAnswer = true;
                }

            } while (!yesNoAnswer);
            
        } while (newGame);
    }
}
