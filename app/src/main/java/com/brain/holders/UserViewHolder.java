package com.brain.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView email;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.nameUser);
        email = (TextView) itemView.findViewById(R.id.message);
    }
}
