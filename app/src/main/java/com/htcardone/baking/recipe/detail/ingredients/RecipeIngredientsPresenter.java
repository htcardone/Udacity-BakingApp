package com.htcardone.baking.recipe.detail.ingredients;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.IngredientsItem;
import com.htcardone.baking.data.model.Recipe;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class RecipeIngredientsPresenter implements RecipeIngredientsContract.Presenter {

    private Recipe mRecipe;
    private List<IngredientsItem> mIngredients;

    private final RecipeIngredientsContract.View mView;

    @SuppressLint("RestrictedApi")
    public RecipeIngredientsPresenter(int recipeId,
                                      @NonNull RecipesRepository recipesRepository,
                                      @NonNull RecipeIngredientsContract.View view) {

        checkNotNull(recipesRepository, "recipesRepository cannot be null!");

        recipesRepository.getRecipe(recipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                mRecipe = recipe;
                mIngredients = mRecipe.getIngredients();
            }

            @Override
            public void onDataNotAvailable() {

            }
        });

        mView = checkNotNull(view, "view cannot be null!");
    }

    @Override
    public void loadIngredients() {
        mView.showIngredients(mIngredients);
    }
}
