package com.example.pixelvault;

import com.google.gson.annotations.SerializedName;

public class InvolvedCompany {
    @SerializedName("id")
    private int id;

    @SerializedName("company")
    private Company company;

    @SerializedName("developer")
    private boolean developer;

    public int getId() { return id; }
    public Company getCompany() { return company; }
    public boolean isDeveloper() { return developer; }
}