package com.example.pixelvault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<GameResponse> gameList;

    public GameAdapter(List<GameResponse> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This MUST be the small card layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_card, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameResponse game = gameList.get(position);

        holder.gameTitle.setText(game.getName());

        Glide.with(holder.itemView.getContext())
                .load(game.getImageUrl())
                .placeholder(R.color.surface_container_highest)
                .into(holder.gameCover);

        // This is the "Jump" logic
        holder.itemView.setOnClickListener(v -> {
            if (v.getContext() instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                GameDetailFragment detailFragment = new GameDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("game_data", game);
                detailFragment.setArguments(bundle);

                // This replaces the HomeFragment with the DetailFragment
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, detailFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return gameList != null ? gameList.size() : 0;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView gameCover;
        TextView gameTitle;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ensure these IDs exist in your item_game_card.xml
            gameCover = itemView.findViewById(R.id.game_cover);
            gameTitle = itemView.findViewById(R.id.game_title);
        }
    }
}