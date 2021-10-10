package org.wit.parking.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.parking.R
import org.wit.parking.databinding.ActivityLoginBinding
import org.wit.parking.models.UserModel
import org.wit.parking.main.MainApp
import android.view.MenuItem



//https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var user = UserModel()
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var edit = false

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)


        app = application as MainApp

        binding.btnLogin.setOnClickListener() {
            user.username = binding.username.text.toString()
            user.password = binding.password.text.toString()
            if (user.username.isNotEmpty()) {
                val authenticated:Boolean = app.users.authenticate(user.copy())
                if(authenticated){
                    app.loggedInUserId = user.id
                    val launcherIntent = Intent(this, ParkingListActivity::class.java)
                    startActivityForResult(launcherIntent,0)
                }else{
                    Snackbar.make(it,R.string.toast_enterValidUserPassword, Snackbar.LENGTH_LONG).show()
                    binding.username.setText("")
                    binding.password.setText("")
                }
            }
            else {
                Snackbar.make(it,R.string.toast_enterUsername, Snackbar.LENGTH_LONG).show()
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_authentication, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_signup -> {
                val launcherIntent = Intent(this, SignupActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}