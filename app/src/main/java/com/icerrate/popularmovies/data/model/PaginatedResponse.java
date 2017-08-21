package com.icerrate.popularmovies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ivan Cerrate
 */
public class PaginatedResponse<T> implements Parcelable {

    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;
    private ArrayList<T> results;

    public PaginatedResponse() {
        page = 0;
        results = new ArrayList<>();
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public boolean isLastPage() {
        if (page != null && totalPages != null && page.intValue() == totalPages.intValue()) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<T> getResults() {
        return results;
    }

    public void setMeta(Integer page, Integer totalResults, Integer totalPages) {
        this.page = page;
        this.totalResults = totalResults;
        this.totalPages = totalPages;
    }

    protected PaginatedResponse(Parcel in) {
        page = in.readByte() == 0x00 ? null : in.readInt();
        totalResults = in.readByte() == 0x00 ? null : in.readInt();
        totalPages = in.readByte() == 0x00 ? null : in.readInt();
        results = in.readArrayList(null);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (page == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(page);
        }
        if (totalResults == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalResults);
        }
        if (totalPages == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalPages);
        }
        dest.writeList(results);
    }

    public static final Creator<PaginatedResponse> CREATOR = new Creator<PaginatedResponse>() {
        @Override
        public PaginatedResponse createFromParcel(Parcel in) {
            return new PaginatedResponse(in);
        }

        @Override
        public PaginatedResponse[] newArray(int size) {
            return new PaginatedResponse[size];
        }
    };

}
