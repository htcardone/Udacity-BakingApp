package com.htcardone.baking.recipe.detail.step;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.data.model.StepsItem;
import com.htcardone.baking.recipe.RecipeContract;

import static android.support.v4.util.Preconditions.checkNotNull;

public class RecipeStepPresenter implements RecipeStepContract.Presenter {

    private Recipe mRecipe;
    private int mStepPos;
    private StepsItem mStepsItem;

    private final RecipeStepContract.View mView;
    private final RecipeContract.ContainerView mContainerView;

    @SuppressLint("RestrictedApi")
    public RecipeStepPresenter(int recipeId, int stepPos,
                               @NonNull RecipesRepository recipesRepository,
                               @NonNull RecipeStepContract.View view,
                               @NonNull RecipeContract.ContainerView containerView) {

        checkNotNull(recipesRepository, "recipesRepository cannot be null!");

        recipesRepository.getRecipe(recipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                mRecipe = recipe;
            }

            @Override
            public void onDataNotAvailable() {
                //TODO
            }
        });

        mStepPos = stepPos;
        mView = checkNotNull(view, "view cannot be null!");
        mContainerView = checkNotNull(containerView, "containerView cannot be null!");
    }

    @Override
    public int getRecipeId() {
        return mRecipe.getId();
    }

    @Override
    public int getStepPos() {
        return mStepPos;
    }

    @Override
    public void loadStep() {
        mContainerView.setTitle(mRecipe.getName());
        mContainerView.toggleImmersiveMode();

        mStepsItem = mRecipe.getSteps().get(mStepPos);
        mView.showStep(mStepsItem);
        mView.enableNavButtons(hasPrev(), hasNext());
    }

    @Override
    public int onPrevClicked() {
        mView.stopVideo();
        if (hasPrev()) {
            mStepPos--;
            mView.stopVideo();
            loadStep();
        }
        return mStepPos;
    }

    @Override
    public int onNextClicked() {
        mView.stopVideo();
        if (hasNext()) {
            mStepPos++;
            mView.stopVideo();
            loadStep();
        }
        return mStepPos;
    }

    private boolean hasPrev() {
        return mStepPos > 0;
    }

    private boolean hasNext() {
        return mStepPos < mRecipe.getSteps().size() - 1;
    }

    public void setStepPos(int stepPos) {
        mStepPos = stepPos;
    }
}
