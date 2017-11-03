package com.htcardone.baking.recipe.list;

import com.htcardone.baking.BaseView;
import com.htcardone.baking.data.model.Recipe;

public interface RecipeListContract {
    interface View extends BaseView<Presenter> {
        void showRecipe(Recipe recipe);
        void openIngredientsDetails(int recipeId);
        void openStepDetails(int recipeId, int stepPos);
        void highlightStep(int stepPos);
    }

    interface Presenter {
        void loadRecipe();
        void onIngredientsClicked();
        void onStepClicked(int stepPos);
        void setHighlight(int stepPos);
    }
}
