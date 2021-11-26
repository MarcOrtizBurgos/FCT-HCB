package cat.copernic.jcadafalch.penjatfirebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.jcadafalch.penjatfirebase.R
import cat.copernic.jcadafalch.penjatfirebase.dataclass.User

class CustomAdapter(private val userList: ArrayList<User>): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    /*private val images = intArrayOf(
        R.mipmap.penjat0_foreground,
        R.mipmap.penjat1_foreground,
        R.mipmap.penjat2_foreground,
        R.mipmap.penjat3_foreground,
        R.mipmap.penjat4_foreground,
        R.mipmap.penjat5_foreground,
        R.mipmap.penjat6_foreground)*/

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_layout, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val user: User = userList[i]
        viewHolder.itemUsername.text = user.username
        viewHolder.itemTimestamp.text = user.date.toString()
        viewHolder.itemImage.setImageResource(R.mipmap.penjat6_foreground)
        viewHolder.itemPoint.text = user.points.toString()
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemUsername: TextView = itemView.findViewById(R.id.item_username)
        val itemTimestamp: TextView = itemView.findViewById(R.id.item_timestamp)
        val itemPoint: TextView = itemView.findViewById(R.id.item_points)
    }
}