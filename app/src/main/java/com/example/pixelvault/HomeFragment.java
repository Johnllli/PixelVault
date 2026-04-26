package com.example.pixelvault;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GameAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.rv_games);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        fetchGames();

        // Observe favorites table — keeps heart icons in sync across the list
        AppDatabase.getInstance(requireContext())
                .favoriteDao()
                .getAllFavorites()
                .observe(getViewLifecycleOwner(), favorites -> {
                    if (adapter != null && favorites != null) {
                        Set<Integer> ids = new HashSet<>();
                        for (FavoriteGame fav : favorites) {
                            ids.add(fav.getId());
                        }
                        adapter.setFavoritedIds(ids);
                    }
                });

        return view;
    }

    private void fetchGames() {
        IGDBService service = RetrofitClient.getIGDBService();

        String query = "fields name, rating, summary, genres.name, cover.image_id, " +
                "involved_companies.developer, involved_companies.company.name; " +
                "where first_release_date > 1704067200 & hypes != null; " +
                "sort hypes desc; " +
                "limit 50;";

        RequestBody body = RequestBody.create(query, MediaType.parse("text/plain"));

        service.getGames(body).enqueue(new Callback<List<GameResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GameResponse>> call,
                                   @NonNull Response<List<GameResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<GameResponse> games = response.body();

                    if (!games.isEmpty()) {
                        Log.d("CHECK_DATA", "First Game: " + games.get(0).getName()
                                + " URL: " + games.get(0).getImageUrl());
                    }

                    adapter = new GameAdapter(games);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<GameResponse>> call,
                                  @NonNull Throwable t) {
                Log.e("API_ERROR", "Network failed: " + t.getMessage());
            }
        });
    }
}