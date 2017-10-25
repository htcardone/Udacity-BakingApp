package com.htcardone.baking.data;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.model.Recipe;

import java.util.List;

public interface RecipesDataSource {
    interface LoadRecipesCallback {
        void onRecipesLoaded(List<Recipe> recipes);
        void onDataNotAvailable();
    }

    void getRecipes(@NonNull LoadRecipesCallback callback);
    Recipe getRecipe(@NonNull int recipeId);

    void refreshRecipes();
    void deleteAllRecipes();
}
