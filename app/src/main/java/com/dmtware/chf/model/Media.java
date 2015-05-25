package com.dmtware.chf.model;

import com.google.gson.annotations.SerializedName;

/**
 * Media model
 */
public class Media {

    @SerializedName("ID")
    int id;

    @SerializedName("Key")
    String key;

    @SerializedName("Resource")
    String resource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return getResource();
    }
}
