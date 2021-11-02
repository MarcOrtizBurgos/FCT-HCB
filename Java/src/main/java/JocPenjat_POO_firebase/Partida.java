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
public class Partida {

    private final Scanner sc = new Scanner(System.in);
    private final int maxErrors;
    private final Firestore db;

    public Partida(int maxErrors, Firestore db) {
        this.maxErrors = maxErrors;
        this.db = db;
    }

    public void play(String username) throws InterruptedException, ExecutionException {
        boolean finishGame = false;
        do {
            boolean yesNoAnswer = false;

            Ronda round = new Ronda(maxErrors, db);
            round.startRound(username);

            do {
                System.out.print("Vols jugar una altra partida (S/N): ");
                System.out.println("");
                char YesNo = sc.next().toUpperCase().charAt(0);
                if (YesNo == 'S') {
                    // if the answer is Yes, starts another round
                    System.out.println("\n \n \n");
                    System.out.println("Nova Partida");
                    yesNoAnswer = true;
                } else if (YesNo == 'N') { // if the answer is No, the program is over
                    finishGame = true;
                    yesNoAnswer = true;
                }

            } while (!yesNoAnswer);

        } while (!finishGame);
    }
}
