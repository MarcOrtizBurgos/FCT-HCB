package JocPenjat_POO_firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcadafalch
 */
public class Penjat {

    private static int maxErrors;
    private final static Random rand = new Random();
    private final static Scanner sc = new Scanner(System.in);
    private static Firestore db;

    public static void Connect() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("penjat.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
        //System.out.println("Se conecto con exito");

    }

    public Penjat(int errors) {
        this.maxErrors = errors;
    }

    private void welcome() {
        System.out.println("===========================");
        System.out.println("BENVINGUT AL JOC DEL PENJAT");
        System.out.println("===========================");
        System.out.println("");
    }

    public void start() throws InterruptedException, ExecutionException {
        try {
            Connect();

        } catch (IOException e) {
        } catch (Exception ex) {
            Logger.getLogger(Penjat.class.getName()).log(Level.SEVERE, null, ex);
        }

        /*Usuari user = new Usuari(db);
        String username = user.logIn();
        ystem.out.println(username);*/
        String username = "agrau";
        welcome();
        Partida partida = new Partida(maxErrors, db);
        partida.play(username);
    }
}
