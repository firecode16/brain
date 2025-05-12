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
import androidx.core.net.toUri
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.brain.userprofile.model.User
import com.brain.userprofile.service.ProfileService
import com.brain.userprofile.util.Util.Companion.BASE_URL
import com.brain.userprofile.util.Util.Companion.URL_AVATAR_PART
import com.brain.userprofile.util.Util.Companion.URL_BACKDROP_PART
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.button.MaterialButton

class ProfileActivity : AppCompatActivity() {
    private lateinit var backdropProfile: ImageView
    private lateinit var imgProfile: ImageView
    private lateinit var headerFullName: TextView
    private lateinit var txtOccupation: TextView
    private lateinit var txtFullName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtEmail: TextView
    private lateinit var toolbar: Toolbar
    private lateinit var btnSave: MaterialButton
    private var user: User? = null
    private var uriBackdropProfile: Uri? = null
    private var uriImgProfile: Uri? = null
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
        btnSave = findViewById(R.id.btnSave)

        user = intent.getSerializableExtra("user", User::class.java)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)
        setUserDetails()

        toolbar.setNavigationOnClickListener(onBackToHome)
        backdropProfile.setOnClickListener(onPickBackdropListener)
        imgProfile.setOnClickListener(onPickProfileListener)
        btnSave.setOnClickListener(onUpdateProfile)

        initLoadProfileImages()
    }

    @SuppressLint("NewApi")
    private fun fullScreenAndHideNavigationBar() {
        window.setFlags(android.R.attr.windowFullscreen, android.R.attr.windowFullscreen)
        window.decorView.windowInsetsController?.hide(WindowInsets.Type.systemBars())
    }

    private fun initLoadProfileImages() {
        val backdropUri = BASE_URL + URL_BACKDROP_PART + user!!.userId
        val avatarUri = BASE_URL + URL_AVATAR_PART + user!!.userId
        loadImgBackdropProfile(backdropUri.toUri())
        loadImgProfileWithRoundCorners(avatarUri.toUri())
        urlBackdropAndAvatarIsNull()
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
            uriImgProfile = data!!.data
            loadImgProfileWithRoundCorners(uriImgProfile!!)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    private val pickBackdropMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            uriBackdropProfile = data!!.data
            loadImgBackdropProfile(uriBackdropProfile!!)
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

        btnSaveShow()
    }

    private fun loadImgBackdropProfile(uri: Uri) {
        Glide.with(this).load(uri)
            .skipMemoryCache(true)
            .placeholder(circularProgress(this))
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(backdropProfile)

        btnSaveShow()
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

    private var onUpdateProfile: View.OnClickListener = View.OnClickListener {
        user?.backdropProfile = uriBackdropProfile
        user?.avatarProfile = uriImgProfile
        ProfileService.updateProfile(user!!, applicationContext)
        ProfileService.clean()
    }

    private fun urlBackdropAndAvatarIsNull() {
        if (uriBackdropProfile == null || uriImgProfile == null) {
            btnSaveHidden()
        }
    }

    private fun btnSaveShow() {
        btnSave.visibility = View.VISIBLE
    }

    private fun btnSaveHidden() {
        btnSave.visibility = View.INVISIBLE
    }
}