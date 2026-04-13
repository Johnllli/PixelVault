package com.example.pixelvault;

import com.google.gson.annotations.SerializedName;

public class GameResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private double rating;

    @SerializedName("first_release_date")
    private long firstReleaseDate;

    @SerializedName("cover")
    private int coverId;

    public int getId() { return id; }
    public String getName() { return name; }
    public double getRating() { return rating; }
    public long getFirstReleaseDate() { return firstReleaseDate; }
    public int getCoverId() { return coverId; }
}
