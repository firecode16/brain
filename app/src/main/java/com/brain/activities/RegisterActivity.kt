package com.brain.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.brain.R
import com.brain.model.RegisterRequest
import com.brain.model.RegisterResponse
import com.brain.multimediaposts.model.User
import com.brain.repository.AuthRepository
import com.brain.util.AuthPreferences
import com.brain.util.Util.BASE_AUTH_URL
import com.brain.util.Util.generateUID
import com.brain.util.Util.getCurrentDateTime
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author brain30316@gmail.com
 *
 */
class RegisterActivity : AppCompatActivity() {
    private var registerRequest: RegisterRequest? = null
    private lateinit var txtEmail: TextInputEditText
    private lateinit var txtUsername: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var txtFullName: TextInputEditText
    private lateinit var txtPhoneNumber: TextInputEditText
    private var currentDateTime: String? = null
    private var userId: Long = 0L
    private lateinit var user: User
    private var navigation: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtEmail = findViewById(R.id.txtEmail)
        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPassword)
        txtFullName = findViewById(R.id.txtFullName)
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber)
    }

    fun onClickLogin(view: View) {
        val navigation = Intent(this@RegisterActivity, LoginActivity::class.java)
        this@RegisterActivity.startActivity(navigation)
    }

    @SuppressLint("NewApi")
    fun onClickRegister(view: View) {
        userId = generateUID()
        currentDateTime = getCurrentDateTime()

        registerRequest = RegisterRequest(
            userId,
            txtEmail.text.toString(),
            txtUsername.text.toString(),
            txtPassword.text.toString(),
            txtFullName.text.toString(),
            txtPhoneNumber.text.toString(),
            currentDateTime.toString()
        )

        val call = RetrofitInstance.authService.register(registerRequest!!)
        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) {
                        AuthPreferences.saveToken(applicationContext, token)
                        startHome()
                        clean()
                    } else {
                        Toast.makeText(this@RegisterActivity, "The Token is empty", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(view.context, "Register failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                call.cancel()
                Toast.makeText(view.context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startHome() {
        user = User(
            userId,
            txtUsername.text.toString(),
            txtFullName.text.toString(),
            txtEmail.text.toString(),
            txtPhoneNumber.text.toString(),
            currentDateTime.toString(),
            20,
            true
        )

        navigation = Intent(this@RegisterActivity, HomeActivity::class.java)
        navigation!!.putExtra("user", user)
        this@RegisterActivity.startActivity(navigation)
    }

    private fun clean() {
        userId = 0L
        txtEmail.text?.clear()
        txtUsername.text?.clear()
        txtPassword.text?.clear()
        txtFullName.text?.clear()
        txtPhoneNumber.text?.clear()
    }

    object RetrofitInstance {
        val authService: AuthRepository by lazy {
            Retrofit.Builder().baseUrl(BASE_AUTH_URL).addConverterFactory(GsonConverterFactory.create()).build().create(AuthRepository::class.java)
        }
    }
}