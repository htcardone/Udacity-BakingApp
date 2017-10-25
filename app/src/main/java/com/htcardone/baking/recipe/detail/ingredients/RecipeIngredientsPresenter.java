package com.htcardone.baking.recipe.detail.ingredients;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.IngredientsItem;
import com.htcardone.baking.data.model.Recipe;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

public class RecipeIngredientsPresenter implements RecipeIngredientsContract.Presenter {

    private final Recipe mRecipe;
    private final List<IngredientsItem> mIngredients;

    private final RecipeIngredientsContract.View mView;

    public RecipeIngredientsPresenter(int recipeId,
                                      @NonNull RecipesRepository recipesRepository,
                                      @NonNull RecipeIngredientsContract.View view) {

        RecipesRepository repository =
                checkNotNull(recipesRepository, "recipesRepository cannot be null!");
        mRecipe = repository.getRecipe(recipeId);
        mIngredients = mRecipe.getIngredients();
        mView = checkNotNull(view, "view cannot be null!");
    }

    @Override
    public void start() {
        loadIngredients();
    }

    @Override
    public void loadIngredients() {
        mView.showIngredients(mIngredients);
    }
}
