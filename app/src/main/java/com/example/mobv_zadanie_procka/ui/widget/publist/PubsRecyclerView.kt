package com.example.mobv_zadanie_procka.ui.widget.publist

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobv_zadanie_procka.data.database.model.Pub
import com.example.mobv_zadanie_procka.ui.fragments.PubsFragmentDirections

class PubsRecyclerView : RecyclerView {
    private lateinit var pubsAdapter: PubsAdapter
    /**
     * Default constructor
     *
     * @param context context for the activity
     */
    constructor(context: Context) : super(context) {
        init(context)
    }

    /**
     * Constructor for XML layout
     *
     * @param context activity context
     * @param attrs   xml attributes
     */
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context, VERTICAL, false)
        pubsAdapter = PubsAdapter(object : PubsEvents {
            override fun onPubClick(pub: Pub) {
                this@PubsRecyclerView.findNavController().navigate(
                    PubsFragmentDirections.actionToDetail(pub.id)
                )
            }
        })
        adapter = pubsAdapter
    }
}

@BindingAdapter(value = ["pubItems"])
fun PubsRecyclerView.applyItems(
    pubs: List<Pub>?
) {
    (adapter as PubsAdapter).items = pubs ?: emptyList()
}