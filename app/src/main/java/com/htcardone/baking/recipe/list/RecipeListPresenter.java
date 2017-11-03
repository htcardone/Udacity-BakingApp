package com.htcardone.baking.recipe.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.recipe.RecipeContract;

import static android.support.v4.util.Preconditions.checkNotNull;

public class RecipeListPresenter implements RecipeListContract.Presenter {
    private final String LOG_TAG = RecipeListPresenter.class.getSimpleName();

    private final int mRecipeId;
    private final RecipesRepository mRepository;
    private final RecipeListContract.View mView;
    private final RecipeContract.ContainerView mContainerView;

    public RecipeListPresenter(@Nullable int recipeId,
                               @NonNull RecipesRepository repository,
                               @NonNull RecipeListContract.View view,
                               @NonNull RecipeContract.ContainerView containerView) {

        mRecipeId = recipeId;
        mRepository = checkNotNull(repository, "recipesRepository cannot be null!");
        mView = checkNotNull(view, "view cannot be null!");
        mContainerView = checkNotNull(containerView, "containerView cannot be null!");
    }

    @Override
    public void loadRecipe() {
        mRepository.getRecipe(mRecipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                mView.showRecipe(recipe);
                mContainerView.setTitle(recipe.getName());
            }

            @Override
            public void onDataNotAvailable() {
                //TODO
            }
        });
    }

    @Override
    public void onIngredientsClicked() {
        mView.openIngredientsDetails(mRecipeId);
    }

    @Override
    public void onStepClicked(int stepPos) {
        mView.openStepDetails(mRecipeId, stepPos);
        setHighlight(stepPos);
    }

    @Override
    public void setHighlight(int stepPos) {
        mView.highlightStep(stepPos);
    }
}
