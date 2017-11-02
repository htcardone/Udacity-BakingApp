package com.htcardone.baking.recipe;

public interface RecipeContract {

    interface ContainerView {
        void setTitle(String title);
        void toggleImmersiveMode();
    }

    interface TabletView {
        void showIngredientsView();
        void showStepView();
    }
}
