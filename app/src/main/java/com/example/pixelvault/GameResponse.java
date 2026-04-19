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

    // Inside GameResponse.java
    @SerializedName("summary")
    private String summary;

    public String getSummary() { return summary; }

    // 1. Cover must contain image_id
    public static class Cover implements Serializable {
        @SerializedName("image_id")
        private String imageId;

        public String getImageId() { return imageId; }
    }

    // 2. InvolvedCompany must contain the Company object
    public static class InvolvedCompany implements Serializable {
        @SerializedName("developer")
        private boolean developer;

        @SerializedName("company")
        private Company company;

        public boolean isDeveloper() { return developer; }
        public Company getCompany() { return company; }
    }

    // 3. Company contains the actual name
    public static class Company implements Serializable {
        @SerializedName("name")
        private String name;
        public String getName() { return name; }
    }

    // --- GETTERS ---

    public String getName() { return name; }
    public double getRating() { return rating; }

    public String getImageUrl() {
        if (cover != null && cover.getImageId() != null) {
            return "https://images.igdb.com/igdb/image/upload/t_cover_big/" + cover.getImageId() + ".jpg";
        }
        return null;
    }

    public String getDeveloperName() {
        if (involvedCompanies == null || involvedCompanies.isEmpty()) return "Unknown Developer";
        for (InvolvedCompany ic : involvedCompanies) {
            if (ic.isDeveloper() && ic.getCompany() != null) {
                return ic.getCompany().getName();
            }
        }
        return "Unknown Developer";
    }
}