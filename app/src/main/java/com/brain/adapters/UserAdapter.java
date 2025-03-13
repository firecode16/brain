package com.brain.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;
import com.brain.holders.UserViewHolder;
import com.brain.model.Profile;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    final Context context;
    private final List<Profile> items;

    public UserAdapter(Context context, List<Profile> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.container_chat, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        String userName = items.get(position).getFullName();
        holder.bind(userName);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
