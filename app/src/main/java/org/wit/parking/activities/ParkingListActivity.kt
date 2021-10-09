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
import org.wit.parking.adapters.ParkingAdapter
import org.wit.parking.adapters.ParkingListener
import org.wit.parking.models.ParkingModel


class ParkingListActivity : AppCompatActivity(), ParkingListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityParkingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParkingListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = ParkingAdapter(app.parkings.findAll(), this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, ParkingActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onParkingClick(parking: ParkingModel) {
        val launcherIntent = Intent(this, ParkingActivity::class.java)
        launcherIntent.putExtra("parking_edit", parking)
        startActivityForResult(launcherIntent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        binding.recyclerView.adapter?.notifyDataSetChanged()
        super.onActivityResult(requestCode, resultCode, data)
    }
}

