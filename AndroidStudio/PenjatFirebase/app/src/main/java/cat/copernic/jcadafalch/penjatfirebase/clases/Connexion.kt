package cat.copernic.jcadafalch.penjatfirebase.clases

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.jcadafalch.penjatfirebase.ui.RecuperaPartida
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Connexion(activity: AppCompatActivity) {

    val authActivity1 = activity
    fun getConnectionState() {
        // [START rtdb_listen_connected]
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java) ?: false
                if (connected) {
                    showAlert("Connexion","Conectado exitosamente")
                    //Log.d(TAG, "connected")
                } else {
                    showAlert("Connexion","No conectado exitosamente")
                    //Log.d(TAG, "not connected")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showAlert("Connexion","Errorsito")
                Log.w(ContentValues.TAG, "Listener was cancelled")
            }
        })
        // [END rtdb_listen_connected]
    }

    fun isInternetAvailable(context: Context) {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities)
            if (actNw != null) {
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }

                }
            }
        }
        if(result == true){
            showAlert("Wifi","Funciona")
        }
        else{
            showAlert("Wifi","Error Wifi")
        }
    }

    private fun showAlert(title: String, msg: String) {
        val builder = AlertDialog.Builder(authActivity1)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}