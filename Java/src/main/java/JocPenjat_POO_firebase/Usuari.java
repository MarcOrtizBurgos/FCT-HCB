/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JocPenjat_POO_firebase;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author jcadafalch
 */
public class Usuari {

    private final Scanner sc = new Scanner(System.in);
    private String username = "", passwd = "";
    private final Firestore db;

    public Usuari(Firestore db) {
        this.db = db;
    }

    public String logIn() {
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

}
