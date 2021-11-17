package cat.copernic.jcadafalch.penjatfirebase

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_over.*


@SuppressLint("StaticFieldLeak")
private val db = Firebase.firestore

class OverActivity : AppCompatActivity() {
    private lateinit var username: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_over)

        val bundle = intent.extras
        username = bundle?.getString("username").toString()

        setup()
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

    private fun setup() {
        title = ""
        usernameOverText.text = username

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
            showAuth()
        }

        novaPartidaButton.setOnClickListener {
            showHome(username)
        }
    }

    private fun showHome(username: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("username", username)
        }
        newSecretWord()
        Handler().postDelayed(
                {
                    startActivity(homeIntent)
                },
            500
        )

    }

    private fun newSecretWord() {

        db.collection("words").document("arrWords").get().addOnSuccessListener { document ->
            if (document != null) {
                changedataTxt4.text = randomWord(document.get("arrayW").toString())
                createNewRound()
            }
        }
    }


    private fun createNewRound() {
        val secret = changedataTxt4.text.length
        var xWord = ""
        for (i in 0 until secret) {
            xWord += "X"
        }
        db.collection("joc").document(username).set(
                hashMapOf(
                        "maxErrors" to 6,
                        "numErrors" to 0,
                        "secretWord" to changedataTxt4.text.toString(),
                        "secretWordL" to changedataTxt4.text.length,
                        "started" to true,
                        "tryedLetters" to "",
                        "wordLenght" to 0,
                        "xWord" to xWord
                )
        )
    }

    private fun randomWord(word: String): String {
        val replace = word.replace("[", "")
        val replace1 = replace.replace("]", "")
        val arrWords = listOf(replace1.split(", ").toTypedArray())
        val rt = (0 until 10).random()
        return arrWords[0][rt]
    }
}