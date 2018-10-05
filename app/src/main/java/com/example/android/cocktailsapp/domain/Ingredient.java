package com.example.android.cocktailsapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 10/4/2018.
 */

public class Ingredient implements Parcelable {
    @Getter
    @Setter
    private String ingredientName;

    @Getter
    @Setter
    private String measurement;

    public Ingredient(String ingredientName, String measurement) {
        this.ingredientName = ingredientName;
        this.measurement = measurement;
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public Ingredient(Parcel source) {
        ingredientName = source.readString();
        measurement = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ingredientName);
        dest.writeString(measurement);
    }
}
