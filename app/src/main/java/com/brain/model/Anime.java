package com.brain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Anime implements Parcelable {
    private Long id;
    private String userName;
    private String email;
    private int image;
    private String descriptionFooter;
    private int visits;

    public Anime() {
    }

    public Anime(Long id, String userName, String email, int image, String descriptionFooter, int visits) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.image = image;
        this.descriptionFooter = descriptionFooter;
        this.visits = visits;
    }

    protected Anime(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userName = in.readString();
        email = in.readString();
        image = in.readInt();
        descriptionFooter = in.readString();
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
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeInt(image);
        dest.writeString(descriptionFooter);
        dest.writeInt(visits);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDescriptionFooter() {
        return descriptionFooter;
    }

    public void setDescriptionFooter(String descriptionFooter) {
        this.descriptionFooter = descriptionFooter;
    }

    public int getVisits() {
        return visits;
    }

    public void setVisits(int visits) {
        this.visits = visits;
    }
}
