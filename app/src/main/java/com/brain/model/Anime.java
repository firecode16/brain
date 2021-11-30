package com.brain.model;

public class Anime {
    private int image;
    private String name;
    private int visits;

    public Anime() {
    }

    public Anime(int image, String name, int visits) {
        this.image = image;
        this.name = name;
        this.visits = visits;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
