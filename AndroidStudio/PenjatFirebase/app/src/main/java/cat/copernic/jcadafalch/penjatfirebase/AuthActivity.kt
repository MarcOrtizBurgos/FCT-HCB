package cat.copernic.jcadafalch.penjatfirebase

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.activity_won.*

@SuppressLint("StaticFieldLeak")
private val db = Firebase.firestore

class AuthActivity : AppCompatActivity() {

    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_auth)

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        setup()
    }

    private fun setup() {
        val db = Firebase.firestore
        title = ""
        singUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()

                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        username = extractUsernameFromEmail(emailEditText.text.toString())
                        db.collection("joc").document(username).get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d(
                                        "MainActivity",
                                        "DocumentSnapshot data: ${document.get("started")}"
                                    )
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
                        //showAlert()
                        showAlertRegistro()
                    }
                }
            }
        }
        logInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
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
                                    Log.d(
                                        "MainActivity",
                                        "DocumentSnapshot data: ${document.get("started")}"
                                    )
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
                        //showAlert()
                        showAlertSession()
                    }
                }
            }
        }
    }

    private fun showAlertRegistro() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error con el registro")
        builder.setMessage(
            "Se ha producido un error al intentar registrar a este usuario\n\n" +
                    "Si la contrasenya es menor de 6 caracteres puede ser motivo de este error \n\n" +
                    "Considere la possibilidad que este usuario ya esté registrado"
        )
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertSession() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error con el Inicio de Sessión")
        builder.setMessage(
            "Se ha producido un error al intentar iniciar sessión\n\n" +
                    "Considere la posibilidad que este usuario no esté registrado."
        )
        builder.setPositiveButton("Aceptar", null)
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

    private fun showRecuperaPartida(email: String) {
        val intent = Intent(this, RecuperaPartida::class.java).apply {
            putExtra("username", extractUsernameFromEmail(email))
        }
        startActivity(intent)
    }

    private fun extractUsernameFromEmail(email: String): String {
        val parts: List<String> = email.split('@')
        return parts[0]
    }

    private fun newSecretWord() {

        db.collection("words").document("arrWords").get().addOnSuccessListener { document ->
            if (document != null) {
                changedataTxt3.text = randomWord(document.get("arrayW").toString())
                createNewRound()
            }
        }
    }

    private fun createNewRound() {
        val secret = changedataTxt3.text.length
        var xWord = ""
        for (i in 0 until secret) {
            xWord += "X"
        }
        db.collection("joc").document(username).set(
            hashMapOf(
                "maxErrors" to 6,
                "numErrors" to 0,
                "secretWord" to changedataTxt3.text.toString(),
                "secretWordL" to changedataTxt3.text.length,
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
