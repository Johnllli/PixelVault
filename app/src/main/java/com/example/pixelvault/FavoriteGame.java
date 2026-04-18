package com.example.pixelvault;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorites")
public class FavoriteGame {

    @PrimaryKey
    private int id;
    private String name;
    private String coverUrl;
    private String genre;
    private double rating;

    public FavoriteGame(int id, String name, String coverUrl, String genre, double rating) {
        this.id = id;
        this.name = name;
        this.coverUrl = coverUrl;
        this.genre = genre;
        this.rating = rating;
    }

    public int getId()          { return id; }
    public String getName()     { return name; }
    public String getCoverUrl() { return coverUrl; }
    public String getGenre()    { return genre; }
    public double getRating()   { return rating; }
}