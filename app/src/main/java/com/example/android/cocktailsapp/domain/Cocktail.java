package com.example.android.cocktailsapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.android.cocktailsapp.JsonConstants;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by angelov on 5/3/2018.
 */

public class Cocktail implements Parcelable {
    @Getter
    @Setter
    private int id;
    @Getter
    @Setter
    private int databaseId;
    @Getter
    @Setter
    private String cocktailName;
    @Getter
    @Setter
    private String category;
    @Getter
    @Setter
    private String alcoholic;
    @Getter
    @Setter
    private String imageUrl;
    @Getter
    @Setter
    private String instructions;
    @Getter
    @Setter
    private String[] ingredients;
    @Getter
    @Setter
    private String[] measurements;


    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new Cocktail(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new Object[0];
        }
    };

    public Cocktail(int id, int databaseId, String cocktailName, String category, String alcoholic,
                    String imageUrl, String instructions, String[] ingredients, String[] measurements){
        this.id = id;
        this.databaseId = databaseId;
        this.cocktailName = cocktailName;
        this.category = category;
        this.alcoholic = alcoholic;
        this.imageUrl = imageUrl;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.measurements = measurements;
    }

    private Cocktail(Parcel source) {
        id = source.readInt();
        cocktailName = source.readString();
        category = source.readString();
        alcoholic = source.readString();
        imageUrl = source.readString();
        instructions = source.readString();
        ingredients = (String[])source.readArray(java.lang.String.class.getClassLoader());
        measurements = (String[])source.readArray(java.lang.String.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cocktailName);
        dest.writeString(category);
        dest.writeString(alcoholic);
        dest.writeString(imageUrl);
        dest.writeString(instructions);
        dest.writeStringArray(ingredients);
        dest.writeStringArray(measurements);
    }

    public boolean isFavourite() {
        return databaseId != 0;
    }
}
