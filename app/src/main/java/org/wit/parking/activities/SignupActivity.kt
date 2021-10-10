package org.wit.parking.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.parking.R
import org.wit.parking.databinding.ActivitySignupBinding
import org.wit.parking.models.UserModel
import org.wit.parking.main.MainApp
import android.view.MenuItem


//https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var user = UserModel()
    lateinit var app: MainApp


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var edit = false

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)


        app = application as MainApp


        if (intent.hasExtra("user_edit")) {
            edit = true
            user = intent.extras?.getParcelable("user_edit")!!
            binding.username.setText(user!!.username)
            binding.password.setText(user!!.password)
        }


        binding.btnSignup.setOnClickListener {
            user!!.username = binding.username.text.toString()
            user!!.password = binding.password.text.toString()
            if (user!!.username.isNotEmpty() && user!!.password.isNotEmpty()) {
                if(edit){
                    app.users.update(user!!.copy())
                }else{
                    app.users.create(user!!.copy())
                }
                setResult(RESULT_OK)
                finish()
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
            R.id.item_login -> {
                val launcherIntent = Intent(this, LoginActivity::class.java)
                startActivityForResult(launcherIntent,0)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}