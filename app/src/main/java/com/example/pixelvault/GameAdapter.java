package com.example.pixelvault;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<GameResponse> gameList;

    // Keeps track of which game IDs are currently favorited
    private Set<Integer> favoritedIds = new HashSet<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public GameAdapter(List<GameResponse> gameList) {
        this.gameList = gameList;
    }

    // Called by HomeFragment whenever the favorites DB changes
    public void setFavoritedIds(Set<Integer> ids) {
        this.favoritedIds = ids;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_card, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        GameResponse game = gameList.get(position);
        Context ctx = holder.itemView.getContext();

        holder.gameTitle.setText(game.getName());

        Glide.with(ctx)
                .load(game.getImageUrl())
                .placeholder(R.color.surface_container_highest)
                .centerCrop()
                .into(holder.gameCover);

        // Set heart color: pink if favorited, grey if not
        boolean isFavorited = favoritedIds.contains(game.getId());
        holder.btnFavorite.setImageResource(R.drawable.favorite);
        holder.btnFavorite.setColorFilter(
                ctx.getResources().getColor(
                        isFavorited ? R.color.tertiary : R.color.on_surface_variant,
                        null
                )
        );

        // Heart button click — toggle favorite in Room DB
        holder.btnFavorite.setOnClickListener(v -> {
            if (favoritedIds.contains(game.getId())) {
                // Already favorited → remove it
                favoritedIds.remove(game.getId());
                holder.btnFavorite.setColorFilter(
                        ctx.getResources().getColor(R.color.on_surface_variant, null)
                );
                executor.execute(() -> {
                    FavoriteGame existing = AppDatabase.getInstance(ctx)
                            .favoriteDao()
                            .getById(game.getId());
                    if (existing != null) {
                        AppDatabase.getInstance(ctx).favoriteDao().delete(existing);
                    }
                });
            } else {
                // Not favorited → add it
                favoritedIds.add(game.getId());
                holder.btnFavorite.setColorFilter(
                        ctx.getResources().getColor(R.color.tertiary, null)
                );
                executor.execute(() -> {
                    FavoriteGame fav = new FavoriteGame(
                            game.getId(),
                            game.getName(),
                            game.getImageUrl() != null ? game.getImageUrl() : "",
                            game.getGenreName(),
                            game.getRating()
                    );
                    AppDatabase.getInstance(ctx).favoriteDao().insert(fav);
                });
            }
        });

        // Card click → open detail page
        holder.itemView.setOnClickListener(v -> {
            if (ctx instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) ctx;

                GameDetailFragment detailFragment = new GameDetailFragment();
                android.os.Bundle bundle = new android.os.Bundle();
                bundle.putSerializable("game_data", game);
                detailFragment.setArguments(bundle);

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
        ImageView gameCover, btnFavorite;
        TextView gameTitle;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameCover   = itemView.findViewById(R.id.game_cover);
            gameTitle   = itemView.findViewById(R.id.game_title);
            btnFavorite = itemView.findViewById(R.id.btn_favorite);
        }
    }
}