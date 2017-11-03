package com.htcardone.baking.recipes;

import com.htcardone.baking.data.model.Recipe;

import java.util.List;

public interface RecipesContract {
    interface View {
        void setLoadingIndicator(boolean active);
        void showRecipes(List<Recipe> recipes);
        void showNoRecipes();
        void showRecipe(int recipeId);
    }

    interface Presenter {
        void loadRecipes(boolean forceUpdate);
        void onRecipeClicked(int recipeId);
    }
}
