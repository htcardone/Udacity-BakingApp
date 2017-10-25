package com.htcardone.baking.data.source.local;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.model.Recipe;

/**
 * Created by Henrique Cardone on 15/10/2017.
 */

public class RecipesLocalDataSource implements RecipesDataSource {
    private static RecipesLocalDataSource INSTANCE;

    private RecipesLocalDataSource() {

    }

    public static RecipesLocalDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipesLocalDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getRecipes(@NonNull LoadRecipesCallback callback) {

    }

    @Override
    public Recipe getRecipe(@NonNull int recipeId) {
        return null;
    }

    @Override
    public void refreshRecipes() {

    }

    @Override
    public void deleteAllRecipes() {

    }
}
