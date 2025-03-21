package com.brain.multimediaposts.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.brain.multimediaposts.adapters.GridItemAdapter
import com.brain.multimediaposts.model.PostResponse
import com.brain.multimediaposts.repository.PostsRepository
import com.brain.multimediaposts.util.Util.Companion.BASE_URL
import com.brain.userprofile.model.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author brain30316@gmail.com
 *
 */
class PostsService {
    companion object {
        private var postMap: MutableMap<String, RequestBody> = mutableMapOf()
        private var bodyPart: MutableList<MultipartBody.Part> = mutableListOf()

        object RetrofitInstance {
            val postService: PostsRepository by lazy {
                Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(PostsRepository::class.java)
            }
        }

        fun savePost(context: Context, gridItemAdapter: GridItemAdapter, user: User) {
            val listPost = gridItemAdapter.getPost()

            listPost.forEach { model ->
                bodyPart.add(gridItemAdapter.getMultipartFile(model.filePath, model.mimeType))
            }

            val collectionId = createPartFromString(listPost[0].collectionId.toString())
            val userId = createPartFromString(user.userId.toString())
            val userName = createPartFromString(user.userName)
            val fullName = createPartFromString(user.fullName)
            val email = createPartFromString(user.email)
            val phone = createPartFromString(user.phone)
            val overview = createPartFromString("Descripción breve de la publicación")
            val post = createPartFromString(listPost.toString())

            postMap["collectionId"] = collectionId
            postMap["userId"] = userId
            postMap["userName"] = userName
            postMap["fullName"] = fullName
            postMap["email"] = email
            postMap["phone"] = phone
            postMap["overview"] = overview
            postMap["post"] = post

            val call = RetrofitInstance.postService.savePost(postMap, bodyPart)
            call.enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    val postResponse = response.body()
                    if (postResponse?.result == "OK") {
                        showCustomDurationToast(context, "¡Se ha publicado! Podrás verlo en unos minutos.", 9000)
                    } else {
                        Toast.makeText(context, "¡Ocurrio algo inesperado!", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                    call.cancel()
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        private fun createPartFromString(value: String): RequestBody {
            return value.toRequestBody(MultipartBody.FORM)
        }

        private fun showCustomDurationToast(context: Context, message: String, durationInMillis: Long) {
            val toast: Toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
            toast.show()
            Handler(Looper.getMainLooper()).postDelayed(toast::cancel, durationInMillis)
        }

        fun clean() {
            postMap.clear()
            bodyPart.clear()
        }
    }
}