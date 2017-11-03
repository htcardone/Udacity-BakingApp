package com.htcardone.baking.recipes;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.Recipe;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link RecipesActivity}), retrieves the data and updates the
 * UI as required.
 */
public class RecipesPresenter implements RecipesContract.Presenter {
    private final String LOG_TAG = RecipesPresenter.class.getSimpleName();

    private final RecipesRepository mRecipesRepository;
    private final RecipesContract.View mRecipesView;

    public RecipesPresenter(@NonNull RecipesRepository recipesRepository,
                            @NonNull RecipesContract.View recipesView) {
        mRecipesRepository = checkNotNull(recipesRepository, "recipesRepository cannot be null!");
        mRecipesView = checkNotNull(recipesView, "recipesView cannot be null!");
    }

    @Override
    public void loadRecipes(boolean forceUpdate) {

        if (forceUpdate) {
            mRecipesView.setLoadingIndicator(true);
            mRecipesRepository.refreshRecipes();
        }

        mRecipesRepository.getRecipes(new RecipesDataSource.LoadRecipesCallback() {
            @Override
            public void onRecipesLoaded(List<Recipe> recipes) {
                mRecipesView.showRecipes(recipes);
                mRecipesView.setLoadingIndicator(false);
            }

            @Override
            public void onDataNotAvailable() {
                mRecipesView.showNoRecipes();
                mRecipesView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void onRecipeClicked(int recipeId) {
        mRecipesView.showRecipe(recipeId);
    }
}
