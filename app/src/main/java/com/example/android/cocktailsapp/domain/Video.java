package com.example.android.cocktailsapp.domain;

import com.example.android.cocktailsapp.JsonConstants;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 5/24/2018.
 */

public class Video {

    @Getter
    @Setter
    String id;

    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_ISO_639_1)
    String iso6391;

    @Getter
    @Setter
    @SerializedName(JsonConstants.MOVIE_SERVICE_JSON_ISO_3166_1)
    String iso31661;

    @Getter
    @Setter
    String key;

    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    String site;

    @Getter
    @Setter
    int size;

    @Getter
    @Setter
    String type;
}
