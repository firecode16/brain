package com.brain.userprofile.service

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.brain.userprofile.model.ProfileResponse
import com.brain.userprofile.model.User
import com.brain.userprofile.repository.ProfileRepository
import com.brain.userprofile.util.Util.Companion.BASE_URL
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileService {
    companion object {
        private var bodyPart: MutableList<MultipartBody.Part> = mutableListOf()
        private const val AVATAR: String = "_AVATAR"
        private const val BACKDROP: String = "_BACKDROP"

        object RetrofitInstance {
            val profileService: ProfileRepository by lazy {
                Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(ProfileRepository::class.java)
            }
        }

        fun updateProfile(user: User, ctx: Context) {
            if (user.backdropProfile != null && user.avatarProfile != null) {
                bodyPart.add(uploadImage(user.backdropProfile!!, ctx, ""))
                bodyPart.add(uploadImage(user.avatarProfile!!, ctx, ""))
            } else {
                if (user.backdropProfile != null) {
                    bodyPart.add(uploadImage(user.backdropProfile!!, ctx, BACKDROP))
                }

                if (user.avatarProfile != null) {
                    bodyPart.add(uploadImage(user.avatarProfile!!, ctx, AVATAR))
                }
            }

            val call = RetrofitInstance.profileService.updateProfile(user.userId, bodyPart)
            call.enqueue(object : Callback<ProfileResponse> {
                override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                    val result = response.body()
                    if (result?.result == "OK") {
                        Toast.makeText(ctx, "El perfil ha sido actualizado", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(ctx, "Al parcer ocurrio un error.!", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    call.cancel()
                    Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun uploadImage(uri: Uri, ctx: Context, imageAux: String?): MultipartBody.Part {
            val inputStream = ctx.contentResolver.openInputStream(uri)
            val requestBody = inputStream!!.readBytes().toRequestBody("image/*".toMediaTypeOrNull())
            val fileName = getFileName(uri, ctx, imageAux)

            val filePart: MultipartBody.Part = MultipartBody.Part.createFormData("file", fileName, requestBody)
            return filePart
        }

        @SuppressLint("Recycle")
        private fun getFileName(uri: Uri, ctx: Context, imageAux: String?): String {
            var fileName: String? = null

            if (uri.scheme == "content") {
                val cursor = ctx.contentResolver.query(uri, null, null, null, null)
                if (cursor != null) {
                    cursor.moveToFirst()
                    fileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))

                    if (imageAux != "") {
                        fileName = addTextBeforePoint(fileName, imageAux)
                    }
                }
            }
            if (fileName == null) {
                fileName = uri.path
                val cut = fileName?.lastIndexOf('/')
                if (cut != -1) {
                    fileName = fileName?.substring(cut!! + 1)
                }
            }
            return fileName ?: "temp_image.jpg"
        }

        private fun addTextBeforePoint(fileName: String?, imageAux: String?): String {
            val index = fileName?.lastIndexOf('.')
            return if (index == -1) {
                fileName + imageAux
            } else {
                fileName?.substring(0, index!!) + imageAux + fileName?.substring(index!!)
            }
        }

        fun clean() {
            bodyPart.clear()
        }
    }
}