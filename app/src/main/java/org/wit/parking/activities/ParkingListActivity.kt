package org.wit.parking.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.wit.parking.databinding.ActivityParkingListBinding
import org.wit.parking.main.MainApp
import org.wit.parking.R
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.wit.parking.adapters.ParkingAdapter
import org.wit.parking.adapters.ParkingListener
import org.wit.parking.models.ParkingModel
import timber.log.Timber.i

class ParkingListActivity : AppCompatActivity(), ParkingListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityParkingListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { binding.recyclerView.adapter?.notifyDataSetChanged() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerRefreshCallback()

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ParkingAdapter(app.parkings.findAll(), this)


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_parking_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ParkingActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_profile -> {
                i("item_profile")
                val launcherIntent = Intent(this, SignupActivity::class.java)
                launcherIntent.putExtra("user_edit", app.users.findById(app.loggedInUserId!!))
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_logout -> {
                i("item_logout")
                app.loggedInUserId = null
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

}
