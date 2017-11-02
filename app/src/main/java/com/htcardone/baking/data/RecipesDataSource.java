package com.htcardone.baking.data;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.model.Recipe;

import java.util.List;

/**
 * Main entry point for accessing recipes data.
 */

public interface RecipesDataSource {
    interface LoadRecipesCallback {
        void onRecipesLoaded(List<Recipe> recipes);
        void onDataNotAvailable();
    }

    interface GetRecipeCallback {
        void onRecipeLoaded(Recipe recipe);
        void onDataNotAvailable();
    }

    void getRecipes(@NonNull LoadRecipesCallback callback);

    void getRecipe(@NonNull int recipeId, @NonNull GetRecipeCallback callback);

    void saveRecipe(@NonNull Recipe recipe);

    void refreshRecipes();

    void deleteAllRecipes();
}
