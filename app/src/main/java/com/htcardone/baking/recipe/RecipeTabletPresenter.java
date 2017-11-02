package com.htcardone.baking.recipe;

import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsContract;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsPresenter;
import com.htcardone.baking.recipe.detail.step.RecipeStepContract;
import com.htcardone.baking.recipe.detail.step.RecipeStepPresenter;
import com.htcardone.baking.recipe.list.RecipeListContract;
import com.htcardone.baking.recipe.list.RecipeListPresenter;
import com.htcardone.baking.util.Log;

import static com.htcardone.baking.util.Constants.FRAG_TYPE_STEP;

/**
 * Presenter for the tablet screen that can act as a List Presenter, a Ingredients Detail Presenter
 * and a Step Detail Presenter.
 */
public class RecipeTabletPresenter implements RecipeListContract.Presenter,
        RecipeStepContract.Presenter, RecipeIngredientsContract.Presenter {

    private final String LOG_TAG = RecipeTabletPresenter.class.getSimpleName();

    private final RecipesRepository mRepository;
    private final RecipeListPresenter mListPresenter;
    private final RecipeIngredientsPresenter mIngredientsPresenter;
    private final RecipeStepPresenter mStepPresenter;
    private final RecipeContract.TabletView mTabletView;
    private final int mFragmentType;

    public RecipeTabletPresenter(@NonNull RecipesRepository recipesRepository,
                                 @NonNull RecipeListPresenter listPresenter,
                                 @NonNull RecipeIngredientsPresenter ingredientsPresenter,
                                 @NonNull RecipeStepPresenter stepPresenter,
                                 @NonNull RecipeContract.TabletView tabletView,
                                 @NonNull int fragmentType) {

        mRepository = recipesRepository;
        mListPresenter = listPresenter;
        mIngredientsPresenter = ingredientsPresenter;
        mStepPresenter = stepPresenter;
        mTabletView = tabletView;
        mFragmentType = fragmentType;
    }

    @Override
    public void start() {
        Log.d(LOG_TAG, "start()");

        loadRecipe();
        loadIngredients();

        if (mFragmentType == FRAG_TYPE_STEP) {
            setHighlight(getStepPos());
            mTabletView.showStepView();
        } else {
            setHighlight(-1);
            mTabletView.showIngredientsView();
        }
    }

    @Override
    public int getRecipeId() {
        return mStepPresenter.getRecipeId();
    }

    @Override
    public int getStepPos() {
        return mStepPresenter.getStepPos();
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
        Log.d(LOG_TAG, "loadRecipe()");
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
        mStepPresenter.setStepPos(stepPos);
        mStepPresenter.start();
        mTabletView.showStepView();
    }

    @Override
    public void setHighlight(int stepPos) {
        mListPresenter.setHighlight(stepPos);
    }
}
