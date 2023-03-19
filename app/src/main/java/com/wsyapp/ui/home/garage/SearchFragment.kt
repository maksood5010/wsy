package com.wsyapp.ui.home.garage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.wsyapp.R
import java.util.*

private const val TAG = "SearchFragment"

class SearchFragment : Fragment() {

    var API_KEY = ""

    lateinit var placeClient: PlacesClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlaces()

        var autocomplete_fragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
        autocomplete_fragment as AutocompleteSupportFragment
        autocomplete_fragment?.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME))
        autocomplete_fragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(p0: Place) {
                Log.e(TAG, "onPlaceSelected: " + p0!!.name + ", " + p0!!.id)
            }

            override fun onError(p0: Status) {
                Log.i(TAG, "An error occurred: " + p0);
            }

        })
    }

    private fun initPlaces() {
        API_KEY = resources.getString(R.string.GOOGLE_API_KEY)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), API_KEY)
        }
        placeClient = Places.createClient(requireContext())
    }

}