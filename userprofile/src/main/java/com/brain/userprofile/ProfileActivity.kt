package com.brain.userprofile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.brain.userprofile.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ProfileActivity : AppCompatActivity() {
    private lateinit var backdropProfile: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var headerFullName: TextView
    private lateinit var txtOccupation: TextView
    private lateinit var txtFullName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtEmail: TextView
    private lateinit var toolbar: Toolbar
    private var user: User? = null
    private val requestCodePermissions = 1001

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        fullScreenAndHideNavigationBar()

        backdropProfile = findViewById(R.id.backdropProfile)
        imgProfile = findViewById(R.id.imgProfile)
        headerFullName = findViewById(R.id.headerFullName)
        txtOccupation = findViewById(R.id.txtOccupation)
        txtFullName = findViewById(R.id.txtFullName)
        txtPhone = findViewById(R.id.txtPhone)
        txtEmail = findViewById(R.id.txtEmail)
        toolbar = findViewById(R.id.toolbarProfile)

        user = intent.getSerializableExtra("user", User::class.java)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        setUserDetails()

        toolbar.setNavigationOnClickListener(onBackToHome)
        backdropProfile.setOnClickListener(onPickBackdropListener)
        imgProfile.setOnClickListener(onPickProfileListener)
    }

    @SuppressLint("NewApi")
    private fun fullScreenAndHideNavigationBar() {
        window.setFlags(android.R.attr.windowFullscreen, android.R.attr.windowFullscreen)
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
    }

    @SuppressLint("SetTextI18n")
    private fun setUserDetails() {
        headerFullName.text = "\n${user!!.fullName}"
        txtOccupation.text = "\n${user!!.email}"
        txtFullName.text = "\n${user!!.fullName}"
        txtPhone.text = "\n${user!!.phone}"
        txtEmail.text = "\n${user!!.email}"
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                requestCodePermissions
            )
        }
    }

    private val pickProfileMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data!!.data
            loadImgProfileWithRoundCorners(uri!!)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val pickBackdropMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data!!.data
            loadImgBackdropProfile(uri!!)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private fun loadImgProfileWithRoundCorners(uri: Uri) {
        Glide.with(this).load(uri)
            .skipMemoryCache(true)
            .placeholder(circularProgress(this))
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(imgProfile)
    }

    private fun loadImgBackdropProfile(uri: Uri) {
        Glide.with(this).load(uri)
            .skipMemoryCache(true)
            .placeholder(circularProgress(this))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(backdropProfile)
    }

    private fun circularProgress(ctx: Context): Drawable {
        val circularProgress = CircularProgressDrawable(ctx)
        circularProgress.strokeWidth = 5f
        circularProgress.centerRadius = 20f
        circularProgress.setColorSchemeColors(ContextCompat.getColor(ctx, R.color.circular_progress_gray))
        circularProgress.start()
        return circularProgress
    }

    private var onPickProfileListener: View.OnClickListener = View.OnClickListener {
        checkPermissions()
        val pickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickProfileMedia.launch(pickerIntent)
    }

    private var onPickBackdropListener: View.OnClickListener = View.OnClickListener {
        checkPermissions()
        val pickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickBackdropMedia.launch(pickerIntent)
    }

    private var onBackToHome: View.OnClickListener = View.OnClickListener {
        finish()
    }
}