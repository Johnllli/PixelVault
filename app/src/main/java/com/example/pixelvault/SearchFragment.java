package com.example.pixelvault;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class SearchFragment extends Fragment {

    // --- View references ---
    private EditText etSearch;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayout layoutPrompt;
    private LinearLayout layoutNoResults;
    private TextView tvNoResultsSubtitle;

    // --- Data ---
    private GameAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Step 1 — Find all views
        etSearch          = view.findViewById(R.id.et_search);
        recyclerView      = view.findViewById(R.id.rv_search_results);
        progressBar       = view.findViewById(R.id.search_progress);
        layoutPrompt      = view.findViewById(R.id.layout_search_prompt);
        layoutNoResults   = view.findViewById(R.id.layout_no_results);
        tvNoResultsSubtitle = view.findViewById(R.id.tv_no_results_subtitle);

        // Step 2 — Set up RecyclerView with 2-column grid
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Step 3 — Observe favorites so hearts stay in sync
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

        // Step 4 — Search button click
        view.findViewById(R.id.btn_search).setOnClickListener(v -> {
            performSearch();
        });

        // Step 5 — Keyboard "Search" action key does the same thing
        // This fires when the user taps the Search key on the keyboard
        // EditorInfo.IME_ACTION_SEARCH matches the imeOptions we set in XML
        etSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true; // true means "I handled this event, don't do anything else"
            }
            return false; // false means "I didn't handle it, pass it on"
        });
    }

    private void performSearch() {
        String query = etSearch.getText().toString().trim();

        // Don't search if the box is empty
        if (query.isEmpty()) {
            return;
        }

        // Hide keyboard after tapping search
        hideKeyboard();

        // Show loading spinner, hide everything else
        showState(State.LOADING);

        // Build the IGDB search query
        // The "search" keyword tells IGDB to rank results by name relevance
        // We escape the query in case it contains special characters
        String igdbQuery = "search \"" + query + "\"; " +
                "fields name, rating, summary, genres.name, cover.image_id, " +
                "involved_companies.developer, involved_companies.company.name; " +
                "limit 20;";

        RequestBody body = RequestBody.create(
                igdbQuery,
                MediaType.parse("text/plain")
        );

        RetrofitClient.getIGDBService().getGames(body)
                .enqueue(new Callback<List<GameResponse>>() {

                    @Override
                    public void onResponse(@NonNull Call<List<GameResponse>> call,
                                           @NonNull Response<List<GameResponse>> response) {

                        if (!isAdded()) return; // Fragment might have been removed

                        if (response.isSuccessful() && response.body() != null) {
                            List<GameResponse> results = response.body();

                            if (results.isEmpty()) {
                                // API returned successfully but found nothing
                                tvNoResultsSubtitle.setText(
                                        getString(R.string.search_no_results_title)
                                                + " for \"" + query + "\""
                                );
                                showState(State.NO_RESULTS);
                            } else {
                                // We have results — show the grid
                                adapter = new GameAdapter(results);
                                recyclerView.setAdapter(adapter);
                                showState(State.RESULTS);
                            }
                        } else {
                            // API responded but with an error code
                            tvNoResultsSubtitle.setText("Something went wrong. Try again.");
                            showState(State.NO_RESULTS);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<GameResponse>> call,
                                          @NonNull Throwable t) {
                        if (!isAdded()) return;
                        tvNoResultsSubtitle.setText("No internet connection.");
                        showState(State.NO_RESULTS);
                    }
                });
    }

    // --- State Management ---

    // An enum defines a fixed set of named values
    // This is much safer than using magic integers like showState(1) or showState(2)
    // because the compiler catches typos — showState(5) would be an error
    private enum State {
        PROMPT,    // Before user searches
        LOADING,   // API call in progress
        RESULTS,   // Got results, showing grid
        NO_RESULTS // API returned empty or failed
    }

    private void showState(State state) {
        // Hide everything first, then show only what this state needs
        layoutPrompt.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        layoutNoResults.setVisibility(View.GONE);

        switch (state) {
            case PROMPT:
                layoutPrompt.setVisibility(View.VISIBLE);
                break;
            case LOADING:
                progressBar.setVisibility(View.VISIBLE);
                break;
            case RESULTS:
                recyclerView.setVisibility(View.VISIBLE);
                break;
            case NO_RESULTS:
                layoutNoResults.setVisibility(View.VISIBLE);
                break;
        }
    }

    // --- Keyboard helper ---
    private void hideKeyboard() {
        // InputMethodManager is Android's system service that controls the keyboard
        InputMethodManager imm = (InputMethodManager)
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && etSearch.getWindowToken() != null) {
            // hideSoftInputFromWindow tells the system to dismiss the keyboard
            // getWindowToken() identifies which window is currently showing the keyboard
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
        }
    }
}