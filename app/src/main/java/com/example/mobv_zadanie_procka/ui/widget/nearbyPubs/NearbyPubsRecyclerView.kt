package com.example.mobv_zadanie_procka.ui.widget.nearbyPubs

import android.content.Context
import android.util.AttributeSet
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobv_zadanie_procka.ui.viewmodels.data.NearbyPub

class NearbyPubsRecyclerView : RecyclerView {
    private lateinit var pubsAdapter: NearbyPubsAdapter
    var events: NearbyPubsEvents? = null
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
        pubsAdapter = NearbyPubsAdapter(object : NearbyPubsEvents {
            override fun onPubClick(nearbyPub: NearbyPub) {
                events?.onPubClick(nearbyPub)
            }

        })
        adapter = pubsAdapter
    }
}

@BindingAdapter(value = ["nearbyPubs"])
fun NearbyPubsRecyclerView.applyNearbyPubs(
    pubs: List<NearbyPub>
) {
    (adapter as NearbyPubsAdapter).items = pubs
}