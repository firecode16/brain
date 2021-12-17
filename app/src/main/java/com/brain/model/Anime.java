package com.brain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Anime implements Parcelable {
    private final int image;
    private final String name;
    private final int visits;

    public Anime(int image, String name, int visits) {
        this.image = image;
        this.name = name;
        this.visits = visits;
    }

    protected Anime(Parcel in) {
        image = in.readInt();
        name = in.readString();
        visits = in.readInt();
    }

    public static final Creator<Anime> CREATOR = new Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(name);
        dest.writeInt(visits);
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getVisits() {
        return visits;
    }
}
