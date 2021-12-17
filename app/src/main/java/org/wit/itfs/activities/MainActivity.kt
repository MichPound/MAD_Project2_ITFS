package org.wit.itfs.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import org.wit.itfs.R
import org.wit.itfs.databinding.ActivityMainBinding
import org.wit.itfs.helpers.showImagePicker
import org.wit.itfs.main.MainApp
import org.wit.itfs.models.TourSpotModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var imageIntentLauncher: ActivityResultLauncher<Intent>

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
            binding.tourSpotTicket.isChecked = tourSpot.ticket
            binding.btnAdd.setText(R.string.update_tourSpot)

            Picasso.get()
                .load(tourSpot.image.toUri())
                .resize(1200, 800)
                .centerCrop()
                .into(binding.tourSpotImage)
            if (tourSpot.image != "") {
                binding.chooseImage.setText(R.string.update_image)
            }
        }

        binding.btnAdd.setOnClickListener() {

            if (binding.tourSpotTitle.text.toString() == "") {
                Toast.makeText(
                    this,
                    "Please enter title",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotCounty.text.toString() == "") {
                Toast.makeText(
                    this,
                    "Please enter county",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotDescription.text.toString() == "") {
                Toast.makeText(
                    this,
                    "Please enter description",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotLatitude.text.toString().toDoubleOrNull() == null) {
                Toast.makeText(
                    this,
                    "please enter latitude (numeric)",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotLongitude.text.toString().toDoubleOrNull() == null) {
                Toast.makeText(
                    this,
                    "Please enter longitude (numeric)",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotContact.text.toString() == "") {
                Toast.makeText(
                    this,
                    "Please enter contact details",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotOpen.text.toString().toDoubleOrNull() == null) {
                Toast.makeText(
                    this,
                    "Please enter opening time (numeric)",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (binding.tourSpotClose.text.toString().toDoubleOrNull() == null) {
                Toast.makeText(
                    this,
                    "Please enter closing time (numeric)",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                tourSpot.title = binding.tourSpotTitle.text.toString()
                tourSpot.county = binding.tourSpotCounty.text.toString()
                tourSpot.desc = binding.tourSpotDescription.text.toString()
                tourSpot.lat = binding.tourSpotLatitude.text.toString().toDouble()
                tourSpot.long = binding.tourSpotLongitude.text.toString().toDouble()
                tourSpot.contactInfo = binding.tourSpotContact.text.toString()
                tourSpot.openTime = binding.tourSpotOpen.text.toString().toDouble()
                tourSpot.closingTime = binding.tourSpotClose.text.toString().toDouble()
                tourSpot.ticket = binding.tourSpotTicket.isChecked

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
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)
        }

        registerImagePickerCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        if (edit) menu.getItem(1).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_cancel -> {
                finish()
            }
            R.id.item_delete -> {
                app.tourSpots.delete(tourSpot)
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
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            tourSpot.image = result.data?.data!!.toString()

                            Picasso.get()
                                .load(tourSpot.image.toUri())
                                .resize(1200, 800)
                                .centerCrop()
                                .into(binding.tourSpotImage)
                            binding.chooseImage.setText(R.string.update_image)
                        }
                    }
                    RESULT_CANCELED -> {}
                    else -> {}
                }
            }
    }
}