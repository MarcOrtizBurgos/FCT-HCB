/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JocPenjat_POO_firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author jcadafalch
 */
public class Words {

    /**
     * Class used to choose a random word
     */
    private final static Random rand = new Random();
    private static String word;
    private static Firestore db;

    public Words(Firestore db) {
        Words.db = db;
    }

    public static String getWord() throws InterruptedException, ExecutionException {
        String paraula = "";
        try {
            paraula = paraulaRandom(getWords());
        } catch (NullPointerException e) {
            System.out.println("Error en la elecció de paraula.");
        }

        return paraula;
    }

    private static String getWords() throws InterruptedException, ExecutionException {
        /*ApiFuture<DocumentSnapshot> query = db.collection("words").document("arrWords").get();
        DocumentSnapshot querySnapshot = (DocumentSnapshot) query.get().get("arrayW");*/
        //System.out.println(querySnapshot.get("arrayW"));

        ApiFuture<DocumentSnapshot> query = db.collection("joc").document("words").get();

        DocumentSnapshot querySnapshot = query.get();

        return querySnapshot.get("arrWords").toString();
    }

    private static String paraulaRandom(String paraules) {
        String replace = paraules.replace("[", "");
        String replace1 = paraules.replace("]", "");
        ArrayList<String> arrWords = new ArrayList<>(Arrays.asList(replace1.split(", ")));
        int val = rand.nextInt(arrWords.size());
        return arrWords.get(val);
    }

// words --> arrWords --> arrayW
// plantilla --> penjat --> paraules
}
