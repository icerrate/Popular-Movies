package com.icerrate.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author Ivan Cerrate
 */

public class TrailersResponse<T> implements Parcelable {

    private Integer id;
    private ArrayList<T> results;

    public TrailersResponse() {
        id = 0;
        results = new ArrayList<>();
    }

    public Integer getPage() {
        return id;
    }

    public ArrayList<T> getResults() {
        return results;
    }

    protected TrailersResponse(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        results = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeList(results);
    }

    public static final Creator<TrailersResponse> CREATOR = new Creator<TrailersResponse>() {
        @Override
        public TrailersResponse createFromParcel(Parcel in) {
            return new TrailersResponse(in);
        }

        @Override
        public TrailersResponse[] newArray(int size) {
            return new TrailersResponse[size];
        }
    };
}
