
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jcadafalch
 */
public class FirebaseConnection {
    
    static Firestore bd;

    public static void Connect() throws IOException {

        FileInputStream serviceAccount
                = new FileInputStream("penjat.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        
        bd = FirestoreClient.getFirestore();
        System.out.println("Se conecto con exito");

    }
    
    public static void main(String[] args) {
        try {
            Connect();
        } catch (Exception e) {
            System.out.println("Hubo un error en la conexión");
        }
    }
}
