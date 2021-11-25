package cat.copernic.jcadafalch.penjatfirebase.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cat.copernic.jcadafalch.penjatfirebase.R.layout.activity_auth
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    private val db = Firebase.firestore
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_auth)

        supportActionBar?.hide()

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        setup()
    }

    private fun setup() {
        title = ""
        logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && !passwordEditText.text.isNullOrEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        username = extractUsernameFromEmail(emailEditText.text.toString())
                        println("GOOL $username")
                        db.collection("joc").document(username).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    if (document.get("started").toString() == "true") {
                                        finish()
                                        showRecuperaPartida(it.result?.user?.email ?: "")
                                    }
                                    if (document.get("started").toString() == "false") {
                                        finish()
                                        showHome(it.result?.user?.email ?: "")
                                    }
                                } else {
                                    Log.d("MainActivity", "No such document")
                                }
                            }

                    } else {
                        showAlert(
                            "Error con el Inicio de Sessión", "S'ha produït un error " +
                                    "en intentar iniciar sessió\n\n" +
                                    "Consideri la possibilitat que aquest usuari no estigui registrat."
                        )
                    }
                }
            } else {
                showAlert("Error!", "Els camps estan buits")
            }
        }

        singUpButton.setOnClickListener {
            showRegistre()
        }

        hasOblidatContrasenya.setOnClickListener {
           showRecuperaContrasenya()
        }
    }

    private fun showAlert(title: String, msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("username", extractUsernameFromEmail(email))
        }
        newSecretWord()
        Handler().postDelayed(
            {
                startActivity(homeIntent)
            },
            500
        )
    }

    private fun showRegistre(){
        startActivity(Intent(this, RegistreActivity::class.java))
    }

    private fun showRecuperaPartida(email: String) {
        val intent = Intent(this, RecuperaPartida::class.java).apply {
            putExtra("username", extractUsernameFromEmail(email))
        }
        startActivity(intent)
    }

    private fun showRecuperaContrasenya(){
        val intent = Intent(this, RecuperarContrasenyaActivity::class.java).apply {}
        startActivity(intent)
    }

    private fun extractUsernameFromEmail(email: String): String {
        val parts: List<String> = email.split('@')
        return parts[0]
    }

    private fun newSecretWord() {
        changedataTxt.text = ""
        db.collection("words").document("arrWords").get().addOnSuccessListener { document ->
            if (document != null) {
                changedataTxt.text = randomWord(document.get("arrayW").toString())
                createNewRound()
            }
        }
    }

    private fun createNewRound() {
        val secret = changedataTxt.text.length
        var xWord = ""
        for (i in 0 until secret) {
            xWord += "x"
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
                "xWord" to xWord,
                "points" to 70
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