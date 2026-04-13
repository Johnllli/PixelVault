package com.example.pixelvault;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IGDBService {

    @POST("games")
    @Headers({
            "Client-ID: XXX",
            "Authorization: XXX"
    })
    Call<List<GameResponse>> getGames(@Body String body);
}
