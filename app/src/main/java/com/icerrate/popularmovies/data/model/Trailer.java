package com.icerrate.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Ivan Cerrate
 */

public class Trailer implements Parcelable {

    private String id;

    @SerializedName("iso_639_1")
    private String iso639;

    @SerializedName("iso_3166_1")
    private String iso3166;

    private String key;

    private String name;

    private String site;

    private int size;

    private String type;

    public Trailer(String id, String iso639, String iso3166, String key, String name, String site, int size, String type){
        this.id = id;
        this.iso639 = iso639;
        this.iso3166 = iso3166;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getIso639() {
        return iso639;
    }

    public String getIso3166() {
        return iso3166;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getVideoUrl() {
        return "https://www.youtube.com/watch?v=" + key;
    }

    public String getVideoThumbnail() {
        return "http://img.youtube.com/vi/" + key + "/mqdefault.jpg";
    }

    protected Trailer(Parcel in) {
        id = in.readString();
        iso639 = in.readString();
        iso3166 =  in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flag) {
        dest.writeString(id);
        dest.writeString(iso639);
        dest.writeString(iso3166);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
