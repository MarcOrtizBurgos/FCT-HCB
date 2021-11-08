package cat.copernic.jcadafalch.penjatfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cat.copernic.jcadafalch.penjatfirebase.R.layout.activity_auth
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_home.*


class AuthActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_auth)

        //Analytics Event
        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        //Setup
        setup()
    }

    private fun setup() {
        val db = Firebase.firestore
        title = "Autenticacion"
        singUpButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) { //TODO change "agrau" to email paremeter
                        db.collection("joc").document("agrau").get().addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("started")}")
                                if (document.get("started").toString() == "true"){
                                    showRecuperaPartida(it.result?.user?.email ?: "")
                                }
                                if (document.get("started").toString() == "false"){
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
                        //TODO change "agrau" to email paremeter
                        db.collection("joc").document("agrau").get().addOnSuccessListener { document ->
                            if (document != null) {
                                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("started")}")
                                if (document.get("started").toString() == "true"){
                                    showRecuperaPartida(it.result?.user?.email ?: "")
                                }
                                if (document.get("started").toString() == "false"){
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

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


    private fun showAlertRegistro(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error con el registro")
        builder.setMessage("Se ha producido un error al intentar registrar a este usuario\n\n" +
                "Si la contrasenya es menor de 6 caracteres puede ser motivo de este error \n\n" +
                "Considere la possibilidad que este usuario ya esté registrado")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertSession(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error con el Inicio de Sessión")
        builder.setMessage("Se ha producido un error al intentar iniciar sessión\n\n" +
                "Considere la posibilidad que este usuario no esté registrado.")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)

    }

    private fun showRecuperaPartida(email: String){
        val intent = Intent(this, RecuperaPartida::class.java).apply {
            putExtra("email", email)
        }

        startActivity(intent)
    }
}
