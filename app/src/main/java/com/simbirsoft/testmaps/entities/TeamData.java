package com.simbirsoft.testmaps.entities;

import com.google.gson.annotations.SerializedName;

public class TeamData {
    @SerializedName("count_lives")
    private int lives;
    @SerializedName("count_kills")
    private int kills;
    @SerializedName("count_flamethrower")
    private int flamethrowers;
    @SerializedName("count_jacket")
    private int jackets;

    public int getLives() {
        return lives;
    }

    public int getKills() {
        return kills;
    }

    public int getFlamethrowers() {
        return flamethrowers;
    }

    public int getJackets() {
        return jackets;
    }
}
