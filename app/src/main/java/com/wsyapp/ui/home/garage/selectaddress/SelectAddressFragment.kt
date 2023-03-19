package com.wsyapp.ui.home.garage.selectaddress

import android.content.Context
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.beautybirds.base.BaseFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.wsyapp.R
import kotlinx.android.synthetic.main.fragment_app_map.*
import java.io.IOException
import java.util.*

private const val TAG = "SelectAddressFragment"
class SelectAddressFragment : BaseFragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var currentLocMarker: Marker
    private lateinit var lastLocation: Location
    private var currentMapTheme = GoogleMap.MAP_TYPE_NORMAL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_address, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        initMap()

        iv_current_poition.setOnClickListener(this)
        iv_map_type.setOnClickListener(this)
        actv_search.setOnClickListener(this)

    }
    private fun initMap() {
        val fragment2 = childFragmentManager.findFragmentById(R.id.frag_map2)
        fragment2 as SupportMapFragment
        fragment2.getMapAsync(this)
    }

    private fun initToolBar() {
        getMainActivity().showToolBar()
        getMainActivity().hideLeftMenuOnToolBar()
        getMainActivity().showBackOnToolBar()
        getMainActivity().showTitleOnToolBar(getString(R.string.Select_Address))
        getMainActivity().lockDrawer()
        getMainActivity().hideRightAction()
        getMainActivity().updateToolBar(getString(R.string.Select_Address), View.VISIBLE)


    }

    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0!!
        setMapFirstTime()
        initializeGooglePlayService()
        Log.e(TAG, "onMapReady: $p0")
    }

    private fun setMapFirstTime() {
        val uiSettings = googleMap.uiSettings
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        uiSettings.isZoomControlsEnabled = false
        uiSettings.isZoomGesturesEnabled = true
        uiSettings.isCompassEnabled = false
        uiSettings.isMapToolbarEnabled = false
        uiSettings.isMyLocationButtonEnabled = false
    }

    fun initializeGooglePlayService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val checkPermission = getMainActivity().checkPermission()
            if (checkPermission) {
                buildGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
    }
    private fun changeMapTheme() {
        when (currentMapTheme) {
            GoogleMap.MAP_TYPE_NORMAL -> currentMapTheme = GoogleMap.MAP_TYPE_SATELLITE
            GoogleMap.MAP_TYPE_SATELLITE -> currentMapTheme = GoogleMap.MAP_TYPE_NORMAL
        }
        googleMap.setMapType(currentMapTheme)
    }

    private fun setCurrentLocation() {
        if (lastLocation==null)return
        onLocationChanged(lastLocation)
    }

    private fun openSearchFragment() {
        findNavController().navigate(R.id.action_selectAddressFragment_to_searchAddressFragment)
    }

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(requireContext())
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
        googleApiClient.connect()
    }

    override fun onConnected(p0: Bundle?) {

        locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locationRequest.fastestInterval = 1000
        locationRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        val checkPermission = getMainActivity().checkPermission()
        if (checkPermission) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient,
                locationRequest, this
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(location: Location?) {
        lastLocation = location!!
        currentLocMarker = googleMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            )
        )
        if (currentLocMarker != null) {
            currentLocMarker.remove()
        }
//Showing Current Location Marker on Map
        //Showing Current Location Marker on Map
        val latLng = LatLng(location.getLatitude(), location.getLongitude())
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        val locationManager =
            getMainActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val provider = locationManager!!.getBestProvider(Criteria(), true)
        if (!getMainActivity().checkPermission()) {
            return
        }
        val locations = locationManager!!.getLastKnownLocation(provider!!)
        val providerList =
            locationManager!!.allProviders
        if (null != locations && null != providerList && providerList.size > 0) {
            val longitude = locations.longitude
            val latitude = locations.latitude
            val geocoder = Geocoder(
                getMainActivity().applicationContext,
                Locale.getDefault()
            )
            try {
                val listAddresses: List<Address>? = geocoder.getFromLocation(
                    latitude,
                    longitude, 1
                )
                if (null != listAddresses && listAddresses.size > 0) {
                    val state: String = listAddresses[0].getAdminArea()
                    val country: String = listAddresses[0].getCountryName()
                    val subLocality: String = listAddresses[0].getSubLocality()
                    markerOptions.title(
                        "" + latLng + "," + subLocality + "," + state
                                + "," + country
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        currentLocMarker = googleMap.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient,
                this
            )
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.actv_search -> openSearchFragment()
            R.id.iv_current_poition -> setCurrentLocation()
            R.id.iv_map_type -> changeMapTheme()
        }
    }
}