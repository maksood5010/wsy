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
import com.wsyapp.data.database.entity.Cart
import com.wsyapp.data.model.response.CarModel
import com.wsyapp.utils.MyConstants
import kotlinx.android.synthetic.main.fragment_app_map.actv_search
import kotlinx.android.synthetic.main.fragment_app_map.iv_current_poition
import kotlinx.android.synthetic.main.fragment_app_map.iv_map_type
import kotlinx.android.synthetic.main.fragment_mark_address.*
import java.io.IOException
import java.util.*

private const val TAG = "AddLocationFragment"

class AddLocationFragment : BaseFragment(), OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var googleApiClient: GoogleApiClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var currentLocMarker: Marker
    private lateinit var lastLocation: Location
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var state: String? =null
    private var country:String?=null
    private var subLocality:String?=null
    private var street:String?=null

    private var currentMapTheme = GoogleMap.MAP_TYPE_NORMAL
    private val cartList: MutableList<Cart>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mark_address, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolBar()
        initMap()
        getCartProduct()

        iv_current_poition.setOnClickListener(this)
        iv_map_type.setOnClickListener(this)
        actv_search.setOnClickListener(this)
        tv_select.setOnClickListener(this)

    }

    private fun getCartProduct() {
        val arguments = arguments ?: return
        val size=arguments.getInt("size")?:return
        var i=0
        while (i<size){
            val cartLi=arguments.getParcelable<Cart>(i.toString()) ?: return
            i++
            cartList!!.add(cartLi)
        }
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
        googleMap.setOnMapClickListener {
            markLocation(it)
        }

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
        if (lastLocation == null) return
        onLocationChanged(lastLocation)
    }

    private fun markLocation(location: LatLng) {
        lat = location.latitude
        lon = location.longitude
        googleMap.clear()

        val markerOptions = MarkerOptions()
        markerOptions.position(
            LatLng(
                lat,
                lon
            )
        )
        currentLocMarker = googleMap.addMarker(markerOptions)
        currentLocMarker.showInfoWindow()
        Log.d(TAG, "onMapClicked lat : $lat lon $lon ")
        MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
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
            lon = locations.longitude
            lat = locations.latitude
            getAddress()

        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f))
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient,
                this
            )
        }
        markLocation(location = LatLng(location!!.latitude, location!!.longitude))

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.actv_search -> openSearchFragment()
            R.id.iv_current_poition -> setCurrentLocation()
            R.id.iv_map_type -> changeMapTheme()
            R.id.tv_select -> {
                getAddress()

                val bundle = Bundle()
                bundle.putInt("size",cartList!!.size)
                for (i in cartList!!.indices) {
                    bundle.putParcelable(i.toString(), cartList!![i])
                }
                bundle.putString(MyConstants.COUNTRY, country)
                bundle.putString(MyConstants.STATE, state)
                bundle.putString(MyConstants.STREET, street)
                bundle.putString(MyConstants.AREA, subLocality)
                findNavController().navigate(R.id.action_markAddress_to_addaddress,bundle)
            }
        }
    }
    private fun getAddress() {
        val geocoder = Geocoder(
            getMainActivity().applicationContext,
            Locale.getDefault()
        )
        try {
            val listAddresses: List<Address>? = geocoder.getFromLocation(
                lat,
                lon, 1
            )
            if (null != listAddresses && listAddresses.size > 0) {
                val state1: String = listAddresses[0].adminArea
                val country1: String = listAddresses[0].countryName

                if (listAddresses[0].subLocality != null&&listAddresses[0].thoroughfare != null) {
                    val subLocality1: String = listAddresses[0].subLocality
                    val area: String = listAddresses[0].thoroughfare

                    Log.d(TAG, "address: $state1 $country1 $subLocality1")
                    state=state1
                    country=country1
                    subLocality=subLocality1
                    street=area
                }

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(getMainActivity().applicationContext, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                    Log.d("Address $i", strReturnedAddress.toString())
                }
                strAdd = strReturnedAddress.toString()

            } else {
                Log.d("Address", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("Address", "Canont get Address!")
        }
        return strAdd
    }
}