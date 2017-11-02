package com.htcardone.baking.recipe.detail.step;

import com.htcardone.baking.BasePresenter;
import com.htcardone.baking.BaseView;
import com.htcardone.baking.data.model.StepsItem;

public interface RecipeStepContract {

    interface View extends BaseView<RecipeStepContract.Presenter> {
        void showStep(StepsItem stepsItem);
        void enableNavButtons(boolean enablePrev, boolean enableNext);
        void stopVideo();
    }

    interface Presenter extends BasePresenter {
        int getRecipeId();
        int getStepPos();
        void loadStep();
        int onNextClicked();
        int onPrevClicked();
    }
}