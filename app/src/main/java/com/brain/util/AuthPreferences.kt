package com.brain.util

import android.content.Context

class AuthPreferences {
    companion object {
        fun saveToken(context: Context, token: String) {
            val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            sharedPreferences.edit().putString("jwt_token", token).apply()
        }

        fun getToken(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            return sharedPreferences.getString("jwt_token", null)
        }

        fun clearToken(context: Context) {
            val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            sharedPreferences.edit().remove("jwt_token").apply()
        }
    }
}