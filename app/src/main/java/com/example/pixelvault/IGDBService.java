package com.example.pixelvault;


import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import okhttp3.RequestBody;
import okhttp3.MediaType;

public interface IGDBService {
    @POST("games")
    @Headers({
            "Client-ID: ypbihjn1e1lici4moragoo1src2g15",
            "Authorization: Bearer lc92laaglds1cm43nnms3xr5e3xbdn"
    })
        // Change String to RequestBody
    Call<List<GameResponse>> getGames(@Body RequestBody body);
}