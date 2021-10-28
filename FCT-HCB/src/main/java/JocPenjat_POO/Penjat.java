package JocPenjat_POO;

import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author jcadafalch
 */
public class Penjat {

    private static int maxErrors;
    private final static Random rand = new Random();
    private final static Scanner sc = new Scanner(System.in);

    public Penjat(int errors) {
        this.maxErrors = errors;
    }

    private void welcome() {
        System.out.println("===========================");
        System.out.println("BENVINGUT AL JOC DEL PENJAT");
        System.out.println("===========================");
        System.out.println("");
    }

    public void start() {
        welcome();
        Partida partida = new Partida(maxErrors);
        partida.play();
    }
}
