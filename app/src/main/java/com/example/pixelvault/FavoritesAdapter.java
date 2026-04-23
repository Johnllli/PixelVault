package com.example.pixelvault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.ViewHolder> {

    public interface OnUnfavoriteListener {
        void onUnfavorite(FavoriteGame game);
    }

    public interface OnGameClickListener {
        void onGameClick(FavoriteGame game);
    }

    private List<FavoriteGame> games = new ArrayList<>();
    private final OnUnfavoriteListener unfavoriteListener;
    private final OnGameClickListener clickListener;

    public FavoritesAdapter(OnUnfavoriteListener unfavoriteListener,
                            OnGameClickListener clickListener) {
        this.unfavoriteListener = unfavoriteListener;
        this.clickListener = clickListener;
    }

    public void setGames(List<FavoriteGame> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_game, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteGame game = games.get(position);
        Context ctx = holder.itemView.getContext();

        holder.tvTitle.setText(game.getName());
        holder.tvGenre.setText(game.getGenre());
        holder.tvRating.setText(String.format(Locale.US, "★ %.1f", game.getRating()));

        if (game.getCoverUrl() != null && !game.getCoverUrl().isEmpty()) {
            Glide.with(ctx)
                    .load(game.getCoverUrl())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(holder.imgCover);
        } else {
            holder.imgCover.setImageResource(android.R.color.darker_gray);
        }

        holder.btnUnfavorite.setOnClickListener(v -> unfavoriteListener.onUnfavorite(game));
        holder.itemView.setOnClickListener(v -> clickListener.onGameClick(game));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover, btnUnfavorite;
        TextView tvTitle, tvGenre, tvRating;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover      = itemView.findViewById(R.id.img_cover);
            btnUnfavorite = itemView.findViewById(R.id.btn_unfavorite);
            tvTitle       = itemView.findViewById(R.id.tv_game_title);
            tvGenre       = itemView.findViewById(R.id.tv_genre);
            tvRating      = itemView.findViewById(R.id.tv_rating);
        }
    }
}