package com.example.pixelvault;

import com.google.gson.annotations.SerializedName;
import java.util.List;

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

    @SerializedName("genres")
    private List<Genre> genres;

    @SerializedName("involved_companies")
    private List<InvolvedCompany> involvedCompanies;

    public int getId() { return id; }
    public String getName() { return name; }
    public double getRating() { return rating; }
    public long getFirstReleaseDate() { return firstReleaseDate; }
    public int getCoverId() { return coverId; }
    public List<Genre> getGenres() { return genres; }
    public List<InvolvedCompany> getInvolvedCompanies() { return involvedCompanies; }

    public String getDeveloperName() {
        if (involvedCompanies == null) return null;
        return involvedCompanies.stream()
                .filter(InvolvedCompany::isDeveloper)
                .map(ic -> ic.getCompany() != null ? ic.getCompany().getName() : null)
                .filter(name -> name != null)
                .findFirst()
                .orElse(null);
    }
}