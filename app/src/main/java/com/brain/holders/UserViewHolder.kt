package com.brain.holders

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.brain.R
import com.brain.activities.LoginActivity
import com.brain.util.AuthPreferences
import com.brain.util.Util.getCurrentDateTime

/**
 * @author brain30316@gmail.com
 *
 */
class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.nameUser)
    private val dateTime: TextView = itemView.findViewById(R.id.timestamp)
    private val icLogout: ImageView = itemView.findViewById(R.id.icLogout)

    fun bind(userName: String) {
        name.text = userName
        dateTime.text = getCurrentDateTime()
        icLogout.setOnClickListener { logoutClick(itemView) }
    }
}

private fun logoutClick(v: View) {
    logout(v)
}

fun logout(v: View) {
    AuthPreferences.clearToken(v.context)

    val intent = Intent(v.context, LoginActivity::class.java)
    // Clear stack activities
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    v.context.startActivity(intent)

    if (v.context is AppCompatActivity) {
        (v.context as AppCompatActivity).finish()
    }
}