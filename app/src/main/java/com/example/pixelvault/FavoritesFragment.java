package com.example.pixelvault;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesFragment extends Fragment {

    private FavoritesAdapter adapter;
    private LinearLayout layoutEmptyState;
    private RecyclerView recyclerView;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView     = view.findViewById(R.id.rv_favorites);
        layoutEmptyState = view.findViewById(R.id.layout_empty_state);

        // "Browse Games" button navigates back to Home tab
        view.findViewById(R.id.btn_browse).setOnClickListener(v -> {
            if (getActivity() != null) {
                ((MainActivity) getActivity())
                        .getBottomNav()
                        .setSelectedItemId(R.id.nav_home);
            }
        });

        adapter = new FavoritesAdapter(this::onUnfavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        // LiveData observer — list updates automatically when DB changes
        AppDatabase.getInstance(requireContext())
                .favoriteDao()
                .getAllFavorites()
                .observe(getViewLifecycleOwner(), favorites -> {
                    adapter.setGames(favorites);
                    if (favorites == null || favorites.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        layoutEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        layoutEmptyState.setVisibility(View.GONE);
                    }
                });
    }

    private void onUnfavorite(FavoriteGame game) {
        executor.execute(() ->
                AppDatabase.getInstance(requireContext())
                        .favoriteDao()
                        .delete(game)
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        executor.shutdown();
    }
}