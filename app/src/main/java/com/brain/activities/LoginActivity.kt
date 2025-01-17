package com.brain.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.brain.R
import com.brain.multimediaposts.model.User

class LoginActivity : AppCompatActivity() {
    private var navigation: Intent? = null
    private lateinit var btnSignIn: Button
    private var response: Boolean = true
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnSignIn = findViewById(R.id.btnSignIn)

        btnSignIn.setOnClickListener {
            if (response) {
                user = User(
                    555111L,
                    "firecode16",
                    "Brain StartUp",
                    "startup30@brain.com",
                    8118965749L,
                    "Descripcion breve de la publicacion",
                    "\\backdropProfile.png",
                    20,
                    true
                )

                navigation = Intent(this@LoginActivity, HomeActivity::class.java)
                navigation!!.putExtra("user", user)
                this@LoginActivity.startActivity(navigation)
            }
        }
    }

    fun viewRegisterClicked(view: View) {
        navigation = Intent(this@LoginActivity, RegisterActivity::class.java)
        this@LoginActivity.startActivity(navigation)
    }
}