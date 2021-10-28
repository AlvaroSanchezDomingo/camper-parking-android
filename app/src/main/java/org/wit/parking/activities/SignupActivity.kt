package org.wit.parking.activities

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.wit.parking.R
import org.wit.parking.databinding.ActivitySignupBinding
import org.wit.parking.main.MainApp
import android.view.MenuItem
import org.wit.parking.models.UserModel


class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var user = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var edit = false


        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)


        app = application as MainApp


        if (intent.hasExtra("user_edit")) {
            edit = true
            user = intent.extras?.getParcelable("user_edit")!!
            binding.username.setText(user.username)
            binding.btnSignup.setText(R.string.button_update)

        }


        binding.btnSignup.setOnClickListener {
            user.username = binding.username.text.toString()
            user.password = binding.password.text.toString()
            val passwordCheck = binding.passwordCheck.text.toString()

            if (user.username.isNotEmpty() && user.password.isNotEmpty()) {
                if (user.password == passwordCheck) {
                    if(edit){
                        app.parkings.update(user.copy())
                        setResult(RESULT_OK)
                        finish()
                    }else{
                        val isUserCreated = app.parkings.create(user.copy())
                        if(isUserCreated){
                            setResult(RESULT_OK)
                            finish()
                        }else{
                            Snackbar.make(it,R.string.toast_userExist, Snackbar.LENGTH_LONG).show()
                        }
                    }

                }else {
                    Snackbar.make(it,R.string.toast_passwordNoMatch, Snackbar.LENGTH_LONG).show()
                }
            }
            else {
                Snackbar.make(it,R.string.toast_enterUsername, Snackbar.LENGTH_LONG).show()
            }
        }

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_signup, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_back -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}