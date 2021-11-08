package cat.copernic.jcadafalch.penjatfirebase

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*


/*@SuppressLint("StaticFieldLeak")
private val db = FirebaseFirestore.getInstance()*/

@SuppressLint("StaticFieldLeak")
private val db = Firebase.firestore


private var username = String()
private var tryedLetters = String
private var maxErrors = Int.hashCode()
private var numErrors = Int.hashCode()
private var errors = Int
private var wordLength = Int


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")
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
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setup(email: String) {
        //title = "Inicio"
        title = ""
        val errors = 4
        username = "agrau"
        emailTextView.text = "Benvingut $email"
        //updateNumErrors(errors)
        //txtNumErrors.text = errors.toString()

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

        getNumErrors()
        getTryedLetters()


    }

    private fun updateNumErrors(numErrors: Int) {
        db.collection("joc").document(username.toString()).set(
            hashMapOf(
                "tryedLetters" to tryedLetters,
                "numErrors" to numErrors
            )
        )
    }

    private fun getMaxErrors() {
        db.collection("joc").document("agrau").get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("maxErrors")}")
                txtNumErrors.setText(document.get("maxErrors").toString())
            } else {
                Log.d("MainActivity", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("MainActivity", "get failed with ", exception)
            }

    }

    private fun getNumErrors() {
        db.collection("joc").document("agrau").get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("numErrors")}")
                txtNumErrors.setText(document.get("numErrors").toString())
            } else {
                Log.d("MainActivity", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("MainActivity", "get failed with ", exception)
            }
    }

    private fun getTryedLetters() {
        db.collection("joc").document("agrau").get().addOnSuccessListener { document ->
            if (document != null) {
                Log.d("MainActivity", "DocumentSnapshot data: ${document.get("tryedLetters")}")
                txtTryedLetters.setText(document.get("tryedLetters").toString())
            } else {
                Log.d("MainActivity", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("MainActivity", "get failed with ", exception)
            }
    }
}


