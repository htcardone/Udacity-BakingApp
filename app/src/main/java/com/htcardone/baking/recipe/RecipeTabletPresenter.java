package com.htcardone.baking.recipe;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsContract;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsPresenter;
import com.htcardone.baking.recipe.detail.step.RecipeStepContract;
import com.htcardone.baking.recipe.detail.step.RecipeStepPresenter;
import com.htcardone.baking.recipe.list.RecipeListContract;
import com.htcardone.baking.recipe.list.RecipeListPresenter;

/**
 * Presenter for the tablet screen that can act as a List Presenter, a Ingredients Detail Presenter
 * and a Step Detail Presenter.
 */
public class RecipeTabletPresenter implements RecipeListContract.Presenter,
        RecipeStepContract.Presenter, RecipeIngredientsContract.Presenter {

    private final RecipesRepository mRepository;
    private final RecipeListPresenter mListPresenter;
    private final RecipeIngredientsPresenter mIngredientsPresenter;
    private final RecipeStepPresenter mStepPresenter;
    private final RecipeContract.TabletView mTabletView;

    public RecipeTabletPresenter(@NonNull RecipesRepository recipesRepository,
                                 @NonNull RecipeListPresenter listPresenter,
                                 @NonNull RecipeIngredientsPresenter ingredientsPresenter,
                                 @NonNull RecipeStepPresenter stepPresenter,
                                 @NonNull RecipeContract.TabletView tabletView) {

        mRepository = recipesRepository;
        mListPresenter = listPresenter;
        mIngredientsPresenter = ingredientsPresenter;
        mStepPresenter = stepPresenter;
        mTabletView = tabletView;
    }

    @Override
    public void start() {
        loadRecipe();
        loadIngredients();
        //loadStep();
        mTabletView.showIngredientsView();
    }

    @Override
    public int getRecipeId() {
        return 0;
    }

    @Override
    public int getStepPos() {
        return 0;
    }

    @Override
    public void loadStep() {
        mStepPresenter.loadStep();
    }

    @Override
    public int onNextClicked() {
        int position = mStepPresenter.onNextClicked();
        setHighlight(position);
        return position;
    }

    @Override
    public int onPrevClicked() {
        int position = mStepPresenter.onPrevClicked();
        setHighlight(position);
        return position;
    }

    @Override
    public void loadIngredients() {
        mIngredientsPresenter.loadIngredients();
    }

    @Override
    public void loadRecipe() {
        mListPresenter.loadRecipe();
    }

    @Override
    public void onIngredientsClicked() {
        setHighlight(-1);
        mTabletView.showIngredientsView();
    }

    @Override
    public void onStepClicked(int stepPos) {
        setHighlight(stepPos);
        mTabletView.showStepView();
        mStepPresenter.setStepPos(stepPos);
        mStepPresenter.start();
    }

    @Override
    public void setHighlight(int stepPos) {
        mListPresenter.setHighlight(stepPos);
    }
}
