package com.example.pixelvault;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<GameResponse> gameList;

    public GameAdapter(List<GameResponse> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // This connects the Java code to your item_game_card.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game_card, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameResponse game = gameList.get(position);

        holder.gameTitle.setText(game.getName());

        // Show Developer instead of just "Rating" to look more professional
        holder.gameAddedTime.setText(game.getDeveloperName() + " • " + Math.round(game.getRating()) + "%");

        // Use the fixed Image URL
        Glide.with(holder.itemView.getContext())
                .load(game.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.color.surface_container_highest)
                .into(holder.gameCover);
    }

    @Override
    public int getItemCount() {
        return gameList != null ? gameList.size() : 0;
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView gameCover;
        TextView gameTitle, gameAddedTime;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            // These IDs MUST match your XML IDs exactly
            gameCover = itemView.findViewById(R.id.game_cover);
            gameTitle = itemView.findViewById(R.id.game_title);
            gameAddedTime = itemView.findViewById(R.id.game_added_time);
        }
    }
}