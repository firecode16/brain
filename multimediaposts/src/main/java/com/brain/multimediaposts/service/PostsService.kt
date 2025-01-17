package com.brain.multimediaposts.service

import android.content.Context
import android.widget.Toast
import com.brain.multimediaposts.adapters.GridItemAdapter
import com.brain.multimediaposts.model.PostResponse
import com.brain.multimediaposts.model.User
import com.brain.multimediaposts.repository.PostsRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostsService {
    companion object {
        private var postMap: MutableMap<String, RequestBody> = mutableMapOf()
        private var bodyPart: MutableList<MultipartBody.Part> = mutableListOf()
        private const val BASE_URL: String = "http://192.168.1.121:8081/api/"

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
            val phone = createPartFromString(user.phone.toString())
            val overview = createPartFromString(user.overview)
            val backdropImage = createPartFromString(user.backdropImage)
            val countContacts = createPartFromString(user.countContacts.toString())
            val auth = createPartFromString(user.auth.toString())
            val post = createPartFromString(listPost.toString())

            postMap["collectionId"] = collectionId
            postMap["userId"] = userId
            postMap["userName"] = userName
            postMap["fullName"] = fullName
            postMap["email"] = email
            postMap["phone"] = phone
            postMap["overview"] = overview
            postMap["backdropImage"] = backdropImage
            postMap["countContacts"] = countContacts
            postMap["auth"] = auth
            postMap["post"] = post

            val call = RetrofitInstance.postService.savePost(postMap, bodyPart)
            call.enqueue(object : Callback<PostResponse> {
                override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                    val postResponse = response.body()
                    if (postResponse?.result == "OK") {
                        Toast.makeText(context, "Se ha publicado !", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Ocurrio algo inesperado !", Toast.LENGTH_SHORT).show()
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

        fun clean() {
            postMap.clear()
            bodyPart.clear()
        }
    }
}