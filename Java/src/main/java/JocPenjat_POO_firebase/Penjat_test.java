
package JocPenjat_POO_firebase;

import java.util.concurrent.ExecutionException;

/**
 *
 * @author jcadafalch
 */
public class Penjat_test {
    public static void main(String[] args) throws ExecutionException, InterruptedException{
        Penjat penjat = new Penjat(6);
        penjat.start();
    }
}
