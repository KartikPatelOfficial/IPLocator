package co.deucate.iplocator

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


class Adapter(val Values: ArrayList<String>) : RecyclerView.Adapter<Viewholder>() {

    val keys: ArrayList<String> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        keys.add("dat")
        keys.add("city")
        keys.add("country")
        keys.add("isp")
        keys.add("query")
        keys.add("region")
        keys.add("timezone")
        keys.add("zip")

        return Viewholder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return 8
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val currentKey = keys[position]
        val currentValue = Values[position]

        holder.keyTV.text = currentKey
        holder.valueTV.text = currentValue
    }

}

class Viewholder(view: View) : RecyclerView.ViewHolder(view) {
    val keyTV = view.findViewById<TextView>(R.id.keyTV)
    val valueTV = view.findViewById<TextView>(R.id.valueTV)
}
