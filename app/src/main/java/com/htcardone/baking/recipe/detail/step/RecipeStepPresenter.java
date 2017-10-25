package com.htcardone.baking.recipe.detail.step;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.data.model.StepsItem;
import com.htcardone.baking.recipe.RecipeContract;

import static android.support.v4.util.Preconditions.checkNotNull;

public class RecipeStepPresenter implements RecipeStepContract.Presenter {

    private final Recipe mRecipe;
    private int mStepPos;
    private StepsItem mStepsItem;

    private final RecipeStepContract.View mView;
    private final RecipeContract.ContainerView mContainerView;

    public RecipeStepPresenter(int recipeId, int stepPos,
                               @NonNull RecipesRepository recipesRepository,
                               @NonNull RecipeStepContract.View view,
                               @NonNull RecipeContract.ContainerView containerView) {

        mRecipe = checkNotNull(recipesRepository, "recipesRepository cannot be null!")
                .getRecipe(recipeId);
        mStepPos = stepPos;
        mView = checkNotNull(view, "view cannot be null!");
        mContainerView = checkNotNull(containerView, "containerView cannot be null!");
    }

    @Override
    public void start() {
        loadStep();
        mContainerView.setTitle(mRecipe.getName());
        mContainerView.toggleImmersiveMode();
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
        mStepsItem = mRecipe.getSteps().get(mStepPos);

        // To keep a consistent UX, lock the landscape mode when the step doesn't have a video
        //mContainerView.enablePortraitLock(TextUtils.isEmpty(mStepsItem.getVideoURL()));

        mView.showStep(mStepsItem);
        mView.enableNavButtons(hasPrev(), hasNext());
    }

    @Override
    public int onPrevClicked() {
        if (hasPrev()) {
            mStepPos--;
            loadStep();
        }
        return mStepPos;
    }

    @Override
    public int onNextClicked() {
        if (hasNext()) {
            mStepPos++;
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