/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JocPenjat_POO;

import java.util.Scanner;

/**
 *
 * @author jcadafalch
 */
public class Partida {

    private final Scanner sc = new Scanner(System.in);
    private final int maxErrors;

    public Partida(int maxErrors) {
        this.maxErrors = maxErrors;
    }

    public void play() {
        boolean finishGame = false;
        do {
            boolean yesNoAnswer = false;

            Ronda round = new Ronda(maxErrors);
            round.startRound();

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
