package cat.copernic.jcadafalch.penjatfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import java.util.*
import kotlin.collections.hashMapOf as hashMapOf


@SuppressLint("StaticFieldLeak")
private val db = Firebase.firestore




class HomeActivity : AppCompatActivity() {
    private lateinit var username:  String
    private lateinit var tryedLetters: String
    private lateinit var secretWord:  String
    private var maxErrors: Int = 0
    private var numErrors: Int = 0
    private var errors: Int = 0
    private var wordLength:  Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        username = bundle?.getString("username").toString()
        val started: Boolean = bundle?.getBoolean("started") == true
        setup(started)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logOutIcon -> {
                FirebaseAuth.getInstance().signOut()
                finish()
                showAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun showAuth() {
        val authIntent = Intent(this, AuthActivity::class.java).apply {
        }
        startActivity(authIntent)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseAuth.getInstance().signOut()
        showAuth()
        finish()
    }

    //TODO SETUP TOP-------------------------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    private fun setup(started: Boolean) {
        title = ""
        emailTextView.text = "Benvingut $username"

        if (!started) {
            newSecretWord()
        }

        enviaButton.setOnClickListener {
            when {
                editTextTextPersonName.toString().isEmpty() -> {
                    Toast.makeText(this, "No has introduit ninguna lletra", Toast.LENGTH_SHORT).show()
                }
                editTextTextPersonName.toString().isNotEmpty() -> {

                }
                else -> {
                    Toast.makeText(this, "Error amb la entrada de lletra", Toast.LENGTH_SHORT).show()
                }
            }
        }

        println("SECRET WORD: TOP")
        getXWord()
        println("SECRET WORD: BOTTOM")

        //setXWord()

        gameOverButton.setOnClickListener {
            showOver(username)
        }

        gameWonButton.setOnClickListener {
            showWon(username)
        }
    }
//TODO SET UP BOTTOM-------------------------------------------------------------------------------------------------------

    private fun oneCharacter(){
        if (editTextTextPersonName.length() == 1){
           if(editTextTextPersonName.equals("a-zA..Z")){

           }
        }
    }

    private fun updateNumErrorsTryedLetters(numErrors: Int, tryedLetters: String) {
        db.collection("joc").document(username).update(
            hashMapOf(
                "tryedLetters" to tryedLetters,
                "numErrors" to numErrors
            ) as Map<String, Any>
        )
    }

    private fun createNewRound() {
        //newSecretWord()
        val lengthWord: Int = changedataTxt.text.toString().length
        val secret = changedataTxt.text.length
        var xWord: String = ""
        for (i in 0 until secret) {
            xWord+= "X"
        }
        db.collection("joc").document(username).set(
            hashMapOf(
                "maxErrors" to 6,
                "numErrors" to 0,
                "secretWord" to changedataTxt.text.toString(),
                "secretWordL" to changedataTxt.text.length,
                "started" to true,
                "tryedLetters" to "",
                "wordLenght" to 0,
                "xWord" to xWord
            )
        )

        textView.text = xWord
       //cleanChangedataTxt()
    }

    private fun newSecretWord() {

        db.collection("words").document("arrWords").get().addOnSuccessListener { document ->
            if (document != null) {
                //changedataTxt.setText(document.get("arrayW").toString())
                changedataTxt.text = randomWord(document.get("arrayW").toString())
                createNewRound()
            }
        }
    }

    private fun randomWord(word: String): String {
        /*val replace: String = word.replace("[", "")
        val replace1 = replace.replace("]", "")
        val arrWords: String = ArrayList<String>(replace1.split(", ")).toString()
        val rand = Random
        val rt = rand.nextInt(arrWords.length);
        return arrWords[1].toString()*/

        val replace = word.replace("[", "")
        val replace1 = replace.replace("]", "")
        val arrWords = Arrays.asList(replace1.split(", ").toTypedArray())
        val rand = Random
        //val rt = rand.nextInt(arrWords.size)
        val rt = (0 until 10).random()
        //print(arrWords[0][0])
        return arrWords[0][rt]
    }


    private fun getNumErrors() {
        db.collection("joc").document(username).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("numErrors")}")
                txtNumErrors.setText(document.get("numErrors").toString()).toString()
            } else {
                Log.d("MainActivity", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("MainActivity", "get failed with ", exception)
            }
    }

    private fun getTryedLetters() {
        var tmp: String
        db.collection("joc").document(username).get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("tryedLetters")}")
                txtTryedLetters.text = document.get("tryedLetters").toString()
                //tmp = document.data?.get("tryedLetters").toString()

            } else {
                Log.d("MainActivity", "No such document")
            }
        }
    }

    private fun setImageErrors(errors: Int){
        when (errors) {
            0 -> penjat.setImageResource(R.mipmap.penjat0_foreground)
            1 -> penjat.setImageResource(R.mipmap.penjat1_foreground)
            2 -> penjat.setImageResource(R.mipmap.penjat2_foreground)
            3 -> penjat.setImageResource(R.mipmap.penjat3_foreground)
            4 -> penjat.setImageResource(R.mipmap.penjat4_foreground)
            5 -> penjat.setImageResource(R.mipmap.penjat5_foreground)
            6 -> penjat.setImageResource(R.mipmap.penjat6_foreground)
            else -> Toast.makeText(this, "Error amb el numero de errors", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getXWord(){
        db.collection("joc").document(username).get().addOnSuccessListener { document ->
            val secret = document.get("xWord").toString()

            while (secret == ""){
                println("SECRET WORD: $secret")
            }

            println("SECRET WORD: $secret")

            textView.text = secret

        }
    }

    //TODO DELETE BUTTON NAV TEST---------------------------------------
    private fun showWon(username: String) {
        val wonIntent = Intent(this, WonActivity::class.java).apply {
            putExtra("username", username)
        }

        startActivity(wonIntent)
    }

    private fun showOver(username: String) {
        val overIntent = Intent(this, OverActivity::class.java).apply {
            putExtra("username", username)
        }

        startActivity(overIntent)
    }

    private fun cleanChangedataTxt(){
        changedataTxt.text = ""
    }
}


