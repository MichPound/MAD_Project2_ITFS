package org.wit.itfs.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.squareup.picasso.Picasso
import org.wit.itfs.R
import org.wit.itfs.databinding.ActivityMainBinding
import org.wit.itfs.helpers.showImagePicker
import org.wit.itfs.main.MainApp
import org.wit.itfs.models.TourSpotModel

class MainActivity : AppCompatActivity() {

//    var longitude : View ?= null
//    var latitude : View ?= null
//    private lateinit var fusedLocationClient: FusedLocationProviderClient
//    lateinit var locationManager: LocationManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>

    var tourSpot = TourSpotModel()
    lateinit var app: MainApp
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp

        if (intent.hasExtra("tourSpot_edit")) {
            edit = true
            tourSpot = intent.extras?.getParcelable("tourSpot_edit")!!
            binding.tourSpotTitle.setText(tourSpot.title)
            binding.tourSpotCounty.setText(tourSpot.county)
            binding.tourSpotDescription.setText(tourSpot.desc)
            binding.tourSpotLatitude.setText(tourSpot.lat.toString())
            binding.tourSpotLongitude.setText(tourSpot.long.toString())
            binding.tourSpotContact.setText(tourSpot.contactInfo)
            binding.tourSpotOpen.setText(tourSpot.openTime.toString())
            binding.tourSpotClose.setText(tourSpot.closingTime.toString())
            binding.tourSpotTicket.setText("Ticket")
            binding.btnAdd.setText(R.string.update_tourSpot)

            Picasso.get()
                .load(tourSpot.image)
                .into(binding.tourSpotImage)
            if (tourSpot.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.update_image)
            }
        }

        binding.btnAdd.setOnClickListener() {
            tourSpot.title = binding.tourSpotTitle.text.toString()
            tourSpot.county = binding.tourSpotCounty.text.toString()
            tourSpot.desc = binding.tourSpotDescription.text.toString()
            tourSpot.lat = binding.tourSpotLatitude.text.toString().toDouble()
            tourSpot.long = binding.tourSpotLongitude.text.toString().toDouble()
            tourSpot.contactInfo = binding.tourSpotContact.text.toString()
            tourSpot.openTime = binding.tourSpotOpen.text.toString().toDouble()
            tourSpot.closingTime = binding.tourSpotClose.text.toString().toDouble()
//            tourSpot.ticket = binding.tourSpotTicket.text.toString().toBoolean()
            tourSpot.ticket = false

            if (!edit) {
                app.tourSpots.create(tourSpot.copy())
                setResult(RESULT_OK)
                finish()
            } else {
                app.tourSpots.update(tourSpot.copy())
                setResult(RESULT_OK)
                finish()
            }

        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()


//        setContentView(R.layout.activity_main)

//        longitude = findViewById<>(R.id.longitudeVal)
//        latitude = findViewById<>(R.id.lataitudeVal)
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        https://medium.com/swlh/how-to-get-the-current-location-through-the-android-application-in-kotlin-and-then-save-the-e3a977059f15

//        R.id. = "this is a test"


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        if (edit) menu.getItem(1).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> { finish() }
            R.id.item_delete -> {
                app.tourSpots.delete(tourSpot)
//                finish()
                val intent = Intent(this, TourSpotListActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
//                            i("Got Result ${result.data!!.data}")
                            tourSpot.image = result.data!!.data!!
                            Picasso.get()
                                .load(tourSpot.image)
                                .into(binding.tourSpotImage)
                            binding.chooseImage.setText(R.string.update_image)
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

//    private fun getLocation() {
//        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        if ((ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 2)
//        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
//    }

}