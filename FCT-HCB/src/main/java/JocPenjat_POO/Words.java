/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JocPenjat_POO;

import java.util.Random;

/**
 *
 * @author jcadafalch
 */
public class Words {

    /**
     * Class used to choose a random word
     */
    private final static Random rand = new Random();
    private final static String[] arrWords = {"ATACAR", "CALFREDS", "APROPAR", "FUNCIONARI",
        "MESURAR", "OFICIAL", "SIGNATURA", "LLEGENDA", "PARAFANGS", "SIGNATURA"};

    public Words() {
    }

    public static String getWord() {
        int chooseWord = rand.nextInt(arrWords.length);
        return arrWords[chooseWord];
    }

}
