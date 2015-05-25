package com.dmtware.chf.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Offer Model
 */
public class Offer {

    @SerializedName("ID")
    int id;

    @SerializedName("Name")
    String name;

    @SerializedName("Media")
    ArrayList<Media> media;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Media> media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "" + id + " " + name + " | " + media.toString();
    }
}
