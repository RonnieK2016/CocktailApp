package com.example.android.cocktailsapp.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 5/24/2018.
 */

public class Review {

    @Setter
    @Getter
    String id;

    @Getter
    @Setter
    String author;

    @Getter
    @Setter
    String content;

    @Getter
    @Setter
    String url;
}
