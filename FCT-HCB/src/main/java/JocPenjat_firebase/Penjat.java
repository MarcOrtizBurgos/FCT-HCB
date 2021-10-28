package JocPenjat_firebase;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author jcadafalch
 */
public class Penjat {

    private final static Scanner sc = new Scanner(System.in);
    private final static Random rand = new Random();
    private static String username = "", passwd = "", tryedLetters;
    private static int errors, wordLength;
    private static Firestore db;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        boolean newGame = true, validUsername = false;
        String secretWord = "";
        try {
            Connect();
        } catch (IOException e) {
            System.out.println("Error en la conexió");
        }

        do {
            username = logIn();
            if (username != null) {
                System.out.println("Benvingut "+username);
                validUsername = true;
            }

        } while (!validUsername);
        // loop that will still working unless the user wants to stop playing
        do {
            boolean finishRound = false, yesNoAnswer = false;
            errors = 0;
            wordLength = 0;
            tryedLetters = "";

            updateTryedLettersNumErrors(errors, tryedLetters);

            try {
                secretWord = getSecretWord();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("Error en l'obtenció de paraula");
            }
            if ("".equals(secretWord)) {
                System.out.println("Error en l'obtenció de paraula");
            }

            int nLettersSecretWord = secretWord.length();
            char[] xWord = new char[nLettersSecretWord];

            for (int i = 0; i < xWord.length; i++) {
                xWord[i] = 'X';
            }

            //loop that will still working unless the round has finished
            do {
                if (getMaxErrors() - getNumErrors() == 0) {
                    //if user errors are equal to the maximum allowed, a message will be printed
                    System.out.println("Has pedut, la paraula secreta era: " + secretWord);
                    finishRound = true;
                } else if (wordLength == secretWord.length()) {
                    // if user hits all the letter, it will be shown a congratulations message
                    System.out.println("Enhorabona, has acertat la paraula correcta");
                    finishRound = true;
                } else {
                    System.out.println(xWord);
                    System.out.println("Errors permesos restants: "
                            + (getMaxErrors() - getNumErrors()));
                    System.out.println("Lletres provades: " + getTryedLetters());
                    System.out.print("Introduix una lletra: ");
                    char letter = sc.next().toUpperCase().charAt(0);
                    if (repeatedLetter(letter, getTryedLetters())) {
                        // calls repeatedWord to check if that letter has already 
                        //been entered
                        System.out.println("Aquesta lletra ja l'has probat!\n");
                    } else if (!Character.isLetter(letter)) {
                        // if char letter is not a character print a message error
                        System.out.println("ERROR! El valor introduit no es una llettra.\n "
                                + "Tens que introduir una lletra\n");
                    } else {
                        tryedLetters += letter;
                        updateTryedLetter(tryedLetters);
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
                            updateNumErrors(errors);
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
                    updateTryedLettersNumErrors(errors, tryedLetters);
                    newGame = false;
                    yesNoAnswer = true;
                }

            } while (!yesNoAnswer);

        } while (newGame);
    }

    private static void Connect() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("penjat.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);

        db = FirestoreClient.getFirestore();
        //System.out.println("Se conecto con exito");

    }

    private static String logIn() {
        System.out.print("Introdueix el nom d'usuari: ");
        username = sc.nextLine();
        System.out.print("Introdueix la contrasenya: ");
        passwd = sc.nextLine();

        // https://firebase.google.com/docs/firestore/query-data/get-data
        /* DocumentReference docRef = db.collection("users").document(username);
        System.out.println("User: " + docRef.getId());*/
        ApiFuture<QuerySnapshot> query = db.collection("users").get();

        QuerySnapshot querySnapshot;
        try {
            querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                /*if (document.contains("username")) {
                System.out.println("Username: " + document.getString("username"));
                }
                if (document.contains("password")) {
                System.out.println("Passowrd: " + document.getString("password"));
                }*/
                if (username.equals(document.getString("username")) && passwd.equals(document.getString("password"))) {
                    System.out.println("Usuari: " + username + ", ha iniciat sessió");
                    return username;

                }
            }
        } catch (InterruptedException | ExecutionException ex) {
            System.out.println("Error en l'inici de sessió");
        }
        return null;
    }

    private static String getSecretWord() throws InterruptedException, ExecutionException {
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

    private static void updateNumErrors(int numErrors) {
        DocumentReference docRef = db.collection("joc").document(username);
        Map<String, Object> data = new HashMap<>();
        data.put("numErrors", numErrors);

        docRef.update(data);

        //return docRef;
    }

    private static void updateTryedLetter(String tryedLetters) {
        DocumentReference docRef = db.collection("joc").document(username);
        Map<String, Object> data = new HashMap<>();
        data.put("tryedLetters", tryedLetters);
        docRef.update(data);

        //return docRef;
    }

    private static void updateTryedLettersNumErrors(int numErrors, String tryedLetters) {
        DocumentReference docRef = db.collection("joc").document(username);
        Map<String, Object> data = new HashMap<>();
        data.put("tryedLetters", tryedLetters);
        data.put("numErrors", numErrors);

        docRef.update(data);
    }

    private static DocumentSnapshot readGame() throws InterruptedException, ExecutionException {
        ApiFuture<DocumentSnapshot> query = db.collection("joc").document(username).get();
        DocumentSnapshot querySnapshot = query.get();

        //tryedLetters = querySnapshot.get("tryedLetters").toString();
        //errors = querySnapshot.get("numErrors").hashCode();
        return querySnapshot;
    }

    private static int getMaxErrors() throws InterruptedException, ExecutionException {
        return readGame().get("maxErrors").hashCode();
    }

    private static int getNumErrors() throws InterruptedException, ExecutionException {
        return readGame().get("numErrors").hashCode();
    }

    private static String getTryedLetters() throws InterruptedException, ExecutionException {
        return readGame().get("tryedLetters").toString();
    }
}
