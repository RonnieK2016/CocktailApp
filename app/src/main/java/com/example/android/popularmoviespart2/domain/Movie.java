package com.example.android.popularmoviespart2.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.popularmoviespart2.JsonConstants;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 5/3/2018.
 */

public class Movie implements Parcelable {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int databaseId;
    @Getter
    @Setter
    private boolean video;
    @Getter
    @Setter
    private boolean adult;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_VOTE_COUNT)
    private int voteCount;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_VOTE_AVERAGE)
    private double voteAverage;
    @Getter
    @Setter
    private double popularity;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_POSTER_PATH)
    private String posterPath;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_ORIGINAL_LANGUAGE)
    private String originalLanguage;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_ORIGINAL_TITLE)
    private String originalTitle;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_GENRE_IDS)
    private int[] genreIds;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_BACKDROP_PATH)
    private String backDropPath;
    @Getter
    @Setter
    private String overview;
    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_RELEASE_DATE)
    private String releaseDate;

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public Movie(int id, int databaseId, String title, String posterPath, double voteAverage,
                 String overview, String releaseDate){
        this.id = id;
        this.databaseId = databaseId;
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    private Movie(Parcel source) {
        id = source.readInt();
        video = source.readByte() == 1;
        adult = source.readByte() == 1;
        voteCount = source.readInt();
        voteAverage = source.readDouble();
        popularity = source.readDouble();
        title = source.readString();
        posterPath = source.readString();
        originalLanguage = source.readString();
        originalTitle = source.readString();
        genreIds = source.createIntArray();
        backDropPath = source.readString();
        overview = source.readString();
        releaseDate = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeIntArray(genreIds);
        dest.writeString(backDropPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    public boolean isFavourite() {
        return databaseId != 0;
    }
}
