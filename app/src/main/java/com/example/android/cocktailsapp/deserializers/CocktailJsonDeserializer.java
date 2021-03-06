package com.example.android.cocktailsapp.deserializers;

import com.example.android.cocktailsapp.JsonConstants;
import com.example.android.cocktailsapp.domain.Cocktail;
import com.example.android.cocktailsapp.domain.Ingredient;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by angelov on 10/1/2018.
 */

public class CocktailJsonDeserializer implements JsonDeserializer<Cocktail> {
    @Override
    public Cocktail deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();

        int cocktailId = jsonObject.get(JsonConstants.COCKTAILS_SERVICE_JSON_ID_FIELD).getAsInt();
        String cocktailName = jsonObject.get(JsonConstants.COCKTAILS_SERVICE_JSON_NAME_FIELD).getAsString();
        String imageUrl = jsonObject.get(JsonConstants.COCKTAILS_SERVICE_JSON_IMAGE_URL_FIELD)
                .getAsString().replaceAll("\\/","/");
        String category = "";
        String alcoholic = "";
        String instructions = "";
        if (jsonObject.has(JsonConstants.COCKTAILS_SERVICE_JSON_CATEGORY_FIELD)) {
            category = jsonObject.get(JsonConstants.COCKTAILS_SERVICE_JSON_CATEGORY_FIELD).getAsString();
        }

        if (jsonObject.has(JsonConstants.COCKTAILS_SERVICE_JSON_ALCOHOLIC_FIELD)) {
            alcoholic = jsonObject.get(JsonConstants.COCKTAILS_SERVICE_JSON_ALCOHOLIC_FIELD).getAsString();
        }

        if (jsonObject.has(JsonConstants.COCKTAILS_SERVICE_JSON_INSTRUCTIONS_FIELD)) {
            instructions = jsonObject.get(JsonConstants.COCKTAILS_SERVICE_JSON_INSTRUCTIONS_FIELD)
                    .getAsString().replaceAll("\\/","/");
        }

        ArrayList<Ingredient> ingredients = new ArrayList<>();

        if (jsonObject.has(JsonConstants.COCKTAILS_SERVICE_JSON_INGREDIENT_FIELD + 1)) {
            for (int i = 0; i < 15; i++) {
                String ingredient = JsonConstants.COCKTAILS_SERVICE_JSON_INGREDIENT_FIELD + (i + 1);
                String measurement = JsonConstants.COCKTAILS_SERVICE_JSON_MEASUREMENT_FIELD + (i + 1);

                String ingredientValue = jsonObject.get(ingredient).getAsString()
                        .replaceAll("\\/", "/");

                if(StringUtils.isEmpty(ingredientValue)) {
                    break;
                }

                ingredients.add(new Ingredient(ingredientValue,
                        jsonObject.get(measurement).getAsString()
                                .replaceAll("\\/", "/")));
            }
        }

        return new Cocktail(cocktailId, 0,
                cocktailName, category, alcoholic, imageUrl, instructions, ingredients);
    }
}
