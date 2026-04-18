package com.example.pixelvault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Temporary placeholder view — your friend's real HomeFragment
        // will replace this file entirely when their branch is merged
        TextView placeholder = new TextView(getContext());
        placeholder.setText("Home — coming soon");
        placeholder.setTextColor(0xFFcc97ff);
        placeholder.setTextSize(20f);
        placeholder.setPadding(64, 120, 64, 0);
        return placeholder;
    }
}