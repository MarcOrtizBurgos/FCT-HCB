package cat.copernic.jcadafalch.penjatfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?:"")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.logOutIcon -> {
                FirebaseAuth.getInstance().signOut()
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun setup(email: String){
        //title = "Inicio"
        title=""
        val errors = 0


        emailTextView.text = "Benvingut $email"
        penjat.setImageResource(R.mipmap.penjat1_foreground)

        when (errors){
            0 -> penjat.setImageResource(R.mipmap.penjat0_foreground)
            1 -> penjat.setImageResource(R.mipmap.penjat1_foreground)
            2 -> penjat.setImageResource(R.mipmap.penjat2_foreground)
            3 -> penjat.setImageResource(R.mipmap.penjat3_foreground)
            4 -> penjat.setImageResource(R.mipmap.penjat4_foreground)
            5 -> penjat.setImageResource(R.mipmap.penjat5_foreground)
            6 -> penjat.setImageResource(R.mipmap.penjat6_foreground)
            else -> Toast.makeText(this, "Error amb el numero de errors", Toast.LENGTH_SHORT).show()
        }


        /*logOutButton.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }*/
    }

    private fun checkThatIsAString(s : String){

    }
}