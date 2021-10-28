package org.wit.parking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.parking.databinding.ActivityParkingListBinding
import org.wit.parking.main.MainApp
import org.wit.parking.R
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.wit.parking.adapters.ParkingAdapter
import org.wit.parking.adapters.ParkingListener
import org.wit.parking.models.ParkingModel
import timber.log.Timber.i

class MyParkingListActivity : AppCompatActivity(), ParkingListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityParkingListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    private lateinit var parkingsIntentLauncher : ActivityResultLauncher<Intent>

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadParkings() }
    }

    private fun registerParkingsCallback() {
        parkingsIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = "My Parkings"
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadParkings()


        registerRefreshCallback()
        registerParkingsCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_my_parking_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_all_parkings -> {
                val launcherIntent = Intent(this, ParkingListActivity::class.java)
                parkingsIntentLauncher.launch(launcherIntent)
            }

            R.id.item_add -> {
                val launcherIntent = Intent(this, ParkingActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_profile -> {
                i("item_profile")
                val launcherIntent = Intent(this, SignupActivity::class.java)
                launcherIntent.putExtra("user_edit", app.parkings.findByUsername(app.loggedInUser!!))
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_logout -> {
                i("item_logout")
                app.loggedInUser = null
                val launcherIntent = Intent(this, LoginActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onParkingClick(parking: ParkingModel) {
        val launcherIntent = Intent(this, ParkingActivity::class.java)
        launcherIntent.putExtra("parking_edit", parking)
        refreshIntentLauncher.launch(launcherIntent)
    }
    private fun loadParkings() {
        val myParkings = app.parkings.findParkingByUsername(app.loggedInUser!!)
        showParkings(myParkings!!)
    }

    fun showParkings (parkings: List<ParkingModel>?) {
        i("My parkings $parkings")
        binding.recyclerView.adapter = ParkingAdapter(parkings!!, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}

