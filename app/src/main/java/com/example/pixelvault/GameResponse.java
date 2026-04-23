package com.example.pixelvault;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class GameResponse implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("rating")
    private double rating;

    @SerializedName("cover")
    private Cover cover;

    @SerializedName("involved_companies")
    private List<InvolvedCompany> involvedCompanies;

    @SerializedName("summary")
    private String summary;

    @SerializedName("genres")
    private List<Genre> genres;

    // --- Getters ---

    public int getId()       { return id; }
    public String getName()  { return name; }
    public double getRating(){ return rating; }
    public String getSummary(){ return summary; }

    public String getGenreName() {
        if (genres != null && !genres.isEmpty()) {
            return genres.get(0).getName();
        }
        return "Unknown";
    }

    public String getImageUrl() {
        if (cover != null && cover.getImageId() != null) {
            return "https://images.igdb.com/igdb/image/upload/t_cover_big/"
                    + cover.getImageId() + ".jpg";
        }
        return null;
    }

    public String getDeveloperName() {
        if (involvedCompanies == null || involvedCompanies.isEmpty())
            return "Unknown Developer";
        for (InvolvedCompany ic : involvedCompanies) {
            if (ic.isDeveloper() && ic.getCompany() != null) {
                return ic.getCompany().getName();
            }
        }
        return "Unknown Developer";
    }

    // --- Nested classes ---

    public static class Cover implements Serializable {
        @SerializedName("image_id")
        private String imageId;
        public String getImageId() { return imageId; }
    }

    public static class Genre implements Serializable {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }

    public static class InvolvedCompany implements Serializable {
        @SerializedName("developer")
        private boolean developer;
        @SerializedName("company")
        private Company company;
        public boolean isDeveloper() { return developer; }
        public Company getCompany()  { return company; }
    }

    public static class Company implements Serializable {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }
}