package com.brain.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;

import com.brain.R;

public class DialogThinkFragment extends DialogFragment {

    public static DialogThinkFragment newInstance() {
        return new DialogThinkFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_think, container, false);
    }

}
