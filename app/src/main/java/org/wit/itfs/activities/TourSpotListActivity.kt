package org.wit.itfs.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import org.wit.itfs.R
import org.wit.itfs.adapters.TourSpotAdapter
import org.wit.itfs.adapters.TourSpotListener
import org.wit.itfs.databinding.ActivityTourSpotListBinding
import org.wit.itfs.main.MainApp
import org.wit.itfs.models.TourSpotModel

class TourSpotListActivity : AppCompatActivity(), TourSpotListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityTourSpotListBinding
    private lateinit var refreshIntentLauncher: ActivityResultLauncher<Intent>
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourSpotListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
//        binding.recyclerView.adapter = TourSpotAdapter(app.tourSpots.findAll(),this)
        loadTourSpots()


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(s: String): Boolean {
                val search = binding.searchView.query.toString()
                searchTourSpots(search)
                return true
            }

            override fun onQueryTextSubmit(s: String): Boolean {
                return true
            }
        })

        registerRefreshCallback()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MainActivity::class.java)
                startActivityForResult(launcherIntent, 0)
            }
            R.id.item_logout -> {
//                auth = FirebaseAuth.getInstance()
                auth.signOut()
                finish()
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
        startActivityForResult(launcherIntent, 0)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadTourSpots() {
        showTourSpots(app.tourSpots.findAll())
    }

    private fun searchTourSpots(search: String) {
        showTourSpots(app.tourSpots.search(search))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun showTourSpots(tourSpots: List<TourSpotModel>) {
        binding.recyclerView.adapter = TourSpotAdapter(tourSpots, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadTourSpots() }
    }
}
