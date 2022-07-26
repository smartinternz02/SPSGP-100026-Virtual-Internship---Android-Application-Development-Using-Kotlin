package com.sampathpatro.vipalsiddh

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

 private lateinit var map: GoogleMap
 private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
 private lateinit var lastLocation: Location

private fun placeMarkerOnMap(location: LatLng){
     val markerOptions = MarkerOptions().position(location)
     val titleString = getAddress(location)
     markerOptions.title(titleString)
     map.addMarker(markerOptions)
 }

 private fun getAddress(latLng: LatLng): String {
     val geocoder = Geocoder (this)
     val addresses: List<Address>
     val address: Address?
     var addressText = ""

     try {
         addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

         if (null != addresses && addresses.isNotEmpty()){
             address = addresses[0]
             for (i in 0 until address.maxAddressLineIndex){
                 addressText += if (i==0) address.getAddressLine(i) else
                     "\n" + address.getAddressLine(i)
             }
         }
     } catch (e: IOException){
         Log.e("MapsActivity", e.localizedMessage)
     }
     return  addressText
 }

 companion object {
     private const val LOCATION_PERMISSION_REQUEST_CODE = 1
 }

 private fun setUpMap() {
     if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
             PackageManager.PERMISSION_GRANTED) {
         ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
         LOCATION_PERMISSION_REQUEST_CODE)
         return
     }

     map.isMyLocationEnabled = true
     fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
         if (location != null){
             lastLocation = location
             val currentLatLng = LatLng(location.latitude, location.longitude)
             placeMarkerOnMap(currentLatLng)
             map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
         }
     }
 }

 override fun onCreate(savedInstanceState: Bundle?) {
     super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_maps)
     // Obtain the SupportMapFragment and get notified when the map is ready to be used.
     val mapFragment = supportFragmentManager
         .findFragmentById(R.id.map) as SupportMapFragment
     mapFragment.getMapAsync(this)

     fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

 }

 /**
  * Manipulates the map once available.
  * This callback is triggered when the map is ready to be used.
  * This is where we can add markers or lines, add listeners or move the camera. In this case,
  * we just add a marker near Sydney, Australia.
  * If Google Play services is not installed on the device, the user will be prompted to install
  * it inside the SupportMapFragment. This method will only be triggered once the user has
  * installed Google Play services and returned to the app.
  */
 override fun onMapReady(googleMap: GoogleMap) {
     map = googleMap

     map.uiSettings.isZoomControlsEnabled = true
     map.setOnMarkerClickListener(this)
     setUpMap()
 }

 override fun onMarkerClick(p0: Marker?) = false
} ```
