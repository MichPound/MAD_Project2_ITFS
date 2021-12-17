package org.wit.itfs.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import org.wit.itfs.R
import org.wit.itfs.databinding.ActivityMapBinding
import org.wit.itfs.models.TourSpotModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    private lateinit var spots: Array<TourSpotModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        spots = intent.extras?.getParcelable("spots")!!

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

//        for (spot in spots){
//            mMap.addMarker(MarkerOptions().position(LatLng(spot.lat, spot.long)).title(spot.title))
//        }

        val wit = LatLng(52.24, -7.13)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(wit))
    }
}