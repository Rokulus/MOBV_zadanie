package com.example.mobv_zadanie_procka.ui.widget.publist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobv_zadanie_procka.R
import com.example.mobv_zadanie_procka.data.database.model.Pub
import com.example.mobv_zadanie_procka.helpers.autoNotify
import com.google.android.material.chip.Chip
import kotlin.properties.Delegates

class PubsAdapter(val events: PubsEvents? = null) :
    RecyclerView.Adapter<PubsAdapter.PubItemViewHolder>() {
    var items: List<Pub> by Delegates.observable(emptyList()) { _, old, new ->
        autoNotify(old, new) { o, n -> o.id.compareTo(n.id) == 0 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PubItemViewHolder {
        return PubItemViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PubItemViewHolder, position: Int) {
        holder.bind(items[position], events)
    }

    class PubItemViewHolder(
        private val parent: ViewGroup,
        itemView: View = LayoutInflater.from(parent.context).inflate(
            R.layout.pub_item,
            parent,
            false)
    ) : RecyclerView.ViewHolder(itemView){

        fun bind(item: Pub, events: PubsEvents?) {
            itemView.findViewById<TextView>(R.id.name).text = item.name
            itemView.findViewById<TextView>(R.id.count).text = item.users.toString()
            itemView.findViewById<Chip>(R.id.type).text = item.type

            itemView.setOnClickListener { events?.onPubClick(item) }
        }
    }
}