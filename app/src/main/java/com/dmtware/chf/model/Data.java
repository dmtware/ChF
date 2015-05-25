package com.dmtware.chf.model;

import com.google.gson.annotations.SerializedName;

/**
 * Media Group Model
 */
public class Data {

    @SerializedName("Name")
    String name;
    @SerializedName("FriendlyName")
    String friendlyName;
    @SerializedName("GroupEntities")
    String groupEntities;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getGroupEntities() {
        return groupEntities;
    }

    public void setGroupEntities(String groupEntities) {
        this.groupEntities = groupEntities;
    }



    @Override
    public String toString() {
        return name + "\n" + friendlyName + "\n" + groupEntities + "\n";
    }
}
