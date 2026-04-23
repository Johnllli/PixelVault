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

import java.util.Locale;

public class GameDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        if (getArguments() != null) {

            // Path 1 — came from HomeFragment (full GameResponse object)
            if (getArguments().containsKey("game_data")) {
                GameResponse game = (GameResponse) getArguments()
                        .getSerializable("game_data");
                if (game != null) {
                    populateFromGameResponse(view, game);
                }

                // Path 2 — came from FavoritesFragment (individual Bundle fields)
            } else if (getArguments().containsKey("detail_name")) {
                populateFromBundle(view, getArguments());
            }
        }

        return view;
    }

    // Called when opening from HomeFragment
    private void populateFromGameResponse(View view, GameResponse game) {
        TextView title   = view.findViewById(R.id.game_title);
        TextView dev     = view.findViewById(R.id.entry_developer);
        TextView rating  = view.findViewById(R.id.detail_rating_score);
        TextView summary = view.findViewById(R.id.detail_summary);
        ImageView cover  = view.findViewById(R.id.game_cover);

        title.setText(game.getName());
        dev.setText(game.getDeveloperName());
        summary.setText(game.getSummary() != null
                ? game.getSummary()
                : "No archive data available for this title.");

        // IGDB rates out of 100, design shows out of 5
        double ratingValue = game.getRating() / 20.0;
        rating.setText(String.format(Locale.US, "%.1f", ratingValue));

        Glide.with(this)
                .load(game.getImageUrl())
                .placeholder(R.color.surface_container_highest)
                .into(cover);
    }

    // Called when opening from FavoritesFragment
    private void populateFromBundle(View view, Bundle args) {
        TextView title   = view.findViewById(R.id.game_title);
        TextView dev     = view.findViewById(R.id.entry_developer);
        TextView rating  = view.findViewById(R.id.detail_rating_score);
        TextView summary = view.findViewById(R.id.detail_summary);
        ImageView cover  = view.findViewById(R.id.game_cover);

        title.setText(args.getString("detail_name", "Unknown Game"));

        // Developer and summary are not stored in FavoriteGame
        // so we show a graceful fallback message from strings.xml
        dev.setText(getString(R.string.detail_limited_developer));
        summary.setText(getString(R.string.detail_limited_summary));

        double ratingValue = args.getDouble("detail_rating", 0.0) / 20.0;
        rating.setText(String.format(Locale.US, "%.1f", ratingValue));

        String coverUrl = args.getString("detail_coverUrl", "");
        if (!coverUrl.isEmpty()) {
            Glide.with(this)
                    .load(coverUrl)
                    .placeholder(R.color.surface_container_highest)
                    .into(cover);
        }
    }
}