/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JocPenjat_POO_firebase;

import com.google.cloud.firestore.Firestore;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author jcadafalch
 */
public class Ronda {

    private final Scanner sc = new Scanner(System.in);
    private boolean finishRound;
    private final int maxErrors;
    private int errors, wordLength, nLettersSecretWord;
    private char xWord[];
    private String secretWord, tryedLetters;
    private final Firestore db;

    public Ronda(int maxErrors, Firestore db) {
        this.maxErrors = maxErrors;
        this.db = db;
    }

    private boolean repeatedLetter(char c, String s) {
        boolean repeatWord = false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                repeatWord = true;
            }
        }

        return repeatWord;
    }

    //private method that when it's called, starts a new round
    public void startRound(String username) throws InterruptedException, ExecutionException {
        finishRound = false;
        tryedLetters = "";
        secretWord = Words.getWord();
        nLettersSecretWord = secretWord.length();
        xWord = new char[nLettersSecretWord];

        for (int i = 0; i < xWord.length; i++) {
            xWord[i] = 'X';
        }

        do {
            if (maxErrors - errors == 0) {
                //if user errors are equal to the maximum allowed, a message will be printed
                System.out.println("Has pedut, la paraula secreta era: " + secretWord);
                finishRound = true;
            } else if (wordLength == secretWord.length()) {
                // if user hits all the letter, it will be shown a congratulations message
                System.out.println("Enhorabona, has acertat la paraula correcta");
                finishRound = true;
            } else {
                System.out.println(xWord);
                System.out.println("Errors permesos restants: " + (maxErrors - errors));
                System.out.println("Lletres provades: " + tryedLetters);
                System.out.println("");
                System.out.print("Introduix una lletra: ");
                char letter = sc.next().toUpperCase().charAt(0);
                if (repeatedLetter(letter, tryedLetters)) {
                    // calls repeatedWord to check if that letter has already 
                    //been entered
                    System.out.println("Aquesta lletra ja l'has probat!\n");
                } else if (!Character.isLetter(letter)) {
                    // if char letter is not a character print a message error
                    System.out.println("ERROR! El valor introduit no es una llettra.\n "
                            + "Tens que introduir una lletra\n");
                } else {
                    tryedLetters += letter;
                    boolean guessCorrectWord = false;

                    for (int i = 0; i < secretWord.length(); i++) {
                        if (secretWord.charAt(i) == letter) {
                            // If the correct letter is "X" on console it will apear 
                            // in lowercase, so that it differs from the letters 
                            // not yet guessed
                            if (letter == 'X') {
                                xWord[i] = Character.toLowerCase(letter);
                            } else {
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
    }

}
