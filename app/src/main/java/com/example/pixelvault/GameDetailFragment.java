package com.example.pixelvault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import java.io.Serializable;

public class GameDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if (getArguments() != null) {
            GameResponse game = (GameResponse) getArguments().getSerializable("game_data");
            if (game != null) {
                populateData(view, game);
            }
        }
        return view;
    }

    private void populateData(View view, GameResponse game) {
        // Find views based on your fragment_detail.xml IDs
        TextView title = view.findViewById(R.id.game_title);
        TextView dev = view.findViewById(R.id.entry_developer);
        TextView rating = view.findViewById(R.id.detail_rating_score);
        TextView summary = view.findViewById(R.id.detail_summary);
        ImageView heroImage = view.findViewById(R.id.game_cover);

        // Set Data
        title.setText(game.getName());
        dev.setText(game.getDeveloperName());

        // Populate Summary (The "Executive Summary" section)
        if (game.getSummary() != null) {
            summary.setText(game.getSummary());
        } else {
            summary.setText("No archive data available for this title.");
        }

        // Format Rating (IGDB 100-point scale to 5.0 scale)
        double ratingValue = game.getRating() / 20.0;
        rating.setText(String.format("%.1f", ratingValue));

        // Hero Image with Glide
        Glide.with(this)
                .load(game.getImageUrl())
                .placeholder(R.color.surface_container_highest)
                .into(heroImage);
    }
}