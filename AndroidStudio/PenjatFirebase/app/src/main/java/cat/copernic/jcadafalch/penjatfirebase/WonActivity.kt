package cat.copernic.jcadafalch.penjatfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_won.*

class WonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_won)
        val bundle = intent.extras
        val username = bundle?.getString("username")

        setup(username ?: "")
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
                showAuth()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseAuth.getInstance().signOut()
        showAuth()
        finish()
    }

    private fun showAuth() {
        val authIntent = Intent(this, AuthActivity::class.java).apply {  }
        startActivity(authIntent)

    }

    private fun setup(username: String) {
        title = "Game Wom"
        usernameWonText.text = username

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
        startActivity(homeIntent)

    }
}