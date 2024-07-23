package com.brain.holders;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.brain.R;

/**
 * @Author FLE
 * @Company Brain Inc.
 * @Email hfredi35@gmail.com
 */
public class ProgressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ProgressBar progressBar;
    protected ImageButton retryBtn;
    public TextView errorTxt;
    public LinearLayout errorLayout;

    public ProgressViewHolder(@NonNull View itemView) {
        super(itemView);
        progressBar = itemView.findViewById(R.id.loadMoreProgress);
        retryBtn = itemView.findViewById(R.id.load_more_retry);
        errorTxt = itemView.findViewById(R.id.loadmore_errortxt);
        errorLayout = itemView.findViewById(R.id.loadmore_errorlayout);

        retryBtn.setOnClickListener(this);
        errorLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.load_more_retry) {
            // logic
        } else if (id == R.id.loadmore_errorlayout) {
            // logic
        }
    }
}
