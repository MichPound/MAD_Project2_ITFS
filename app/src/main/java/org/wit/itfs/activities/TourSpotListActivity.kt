package org.wit.itfs.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.itfs.R
import org.wit.itfs.adapters.TourSpotAdapter
import org.wit.itfs.adapters.TourSpotListener
import org.wit.itfs.databinding.ActivityTourSpotListBinding
import org.wit.itfs.main.MainApp
import org.wit.itfs.models.TourSpotModel

class TourSpotListActivity : AppCompatActivity(), TourSpotListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityTourSpotListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourSpotListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = TourSpotAdapter(app.tourSpots.findAll(),this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MainActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onTourSpotClick(tourSpot: TourSpotModel) {
        val launcherIntent = Intent(this, Weather::class.java)
        launcherIntent.putExtra("tourSpot_edit", tourSpot)
        startActivityForResult(launcherIntent,0)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}
