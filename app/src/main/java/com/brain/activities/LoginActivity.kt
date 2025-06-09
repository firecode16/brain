package com.brain.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.auth0.android.jwt.JWT
import com.brain.R
import com.brain.api.RetrofitClient
import com.brain.model.LoginRequest
import com.brain.model.LoginResponse
import com.brain.model.RefreshTokenRequest
import com.brain.model.RefreshTokenResponse
import com.brain.userprofile.model.User
import com.brain.util.AuthPreferences
import com.brain.util.Util.isTokenExpired
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author brain30316@gmail.com
 *
 */
class LoginActivity : AppCompatActivity() {
    private var navigation: Intent? = null
    private lateinit var btnSignIn: Button
    private lateinit var btnGmail: Button
    private lateinit var txtUserName: TextInputEditText
    private lateinit var txtPassword: TextInputEditText
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        txtUserName = findViewById(R.id.editTextUsername)
        txtPassword = findViewById(R.id.editTextPassword)
        btnSignIn = findViewById(R.id.btnSignIn)
        btnGmail = findViewById(R.id.btnGmail)

        btnSignIn.setOnClickListener {
            val username = txtUserName.text.toString()
            val password = txtPassword.text.toString()

            loginUser(username, password)
        }

        btnGmail.setOnClickListener {
            Toast.makeText(this, "Logueo con Gmail. Proximamente.",Toast.LENGTH_SHORT).show()
        }
    }

    fun viewRegisterClicked(view: View) {
        navigation = Intent(this@LoginActivity, RegisterActivity::class.java)
        this@LoginActivity.startActivity(navigation)
    }

    private fun loginUser(username: String, password: String) {
        var token: String? = validateToken()
        val loginRequest = LoginRequest(username, password)
        val callClient = RetrofitClient.authApiService(token!!).login(loginRequest)

        callClient.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    token = response.body()?.token
                    if (token != null) {
                        AuthPreferences.saveToken(applicationContext, token!!)
                        authenticateToken(token!!)
                    } else {
                        Toast.makeText(this@LoginActivity, "The Token is empty", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "The username or password is wrong", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Failed Network: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun authenticateToken(token: String) {
        if (isTokenExpired(token)) {
            // The token is valid
            // You can get user information from the token
            val claim = JWT(token)
            startHome(claim)
        } else {
            // Token has expired !
            refreshToken(token)
        }
    }

    private fun refreshToken(token: String) {
        val refreshRequest = RefreshTokenRequest(token)
        val callClient = RetrofitClient.authApiService(token).refreshToken(refreshRequest)

        callClient.enqueue(object : Callback<RefreshTokenResponse> {
            override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                if (response.isSuccessful) {
                    val refreshToken = response.body()!!.refreshToken
                    AuthPreferences.clearToken(applicationContext)
                    AuthPreferences.saveToken(applicationContext, refreshToken)
                }
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Failed Network: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startHome(claim: JWT) {
        val userId = claim.getClaim("userid").asLong()
        val username = claim.getClaim("username").asString()
        val email = claim.getClaim("email").asString()
        val fullName = claim.getClaim("fullName").asString()
        val phone = claim.getClaim("phone").asString()
        val dateTime = claim.getClaim("date").asString()

        user = User(
            userId!!,
            username!!,
            fullName!!,
            email!!,
            phone!!,
            null,
            null,
            "",
            dateTime!!,
            0,
            true
        )

        navigation = Intent(this@LoginActivity, HomeActivity::class.java)
        navigation!!.putExtra("user", user)
        this@LoginActivity.startActivity(navigation)
    }

    private fun validateToken(): String? {
        return if (AuthPreferences.getToken(applicationContext) == null) "" else AuthPreferences.getToken(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        val token: String? = validateToken()

        if (token != "") {
            authenticateToken(token!!)
        }
    }
}