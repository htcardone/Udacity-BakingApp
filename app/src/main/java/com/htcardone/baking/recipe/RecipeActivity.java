package com.htcardone.baking.recipe;


import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsFragment;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsPresenter;
import com.htcardone.baking.recipe.detail.step.RecipeStepFragment;
import com.htcardone.baking.recipe.detail.step.RecipeStepPresenter;
import com.htcardone.baking.recipe.list.RecipeListFragment;
import com.htcardone.baking.recipe.list.RecipeListPresenter;
import com.htcardone.baking.util.ActivityUtils;
import com.htcardone.baking.util.Log;

import butterknife.BindView;

public class RecipeActivity extends AppCompatActivity implements RecipeContract.ContainerView {
    private static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    protected boolean mIsTwoPaneMode;

    @BindView(R.id.fragment_recipeList_container)
    View mViewRecipeListContainer;

    protected RecipeListFragment findListFragment() {
        return (RecipeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipeList_container);
    }

    protected <T extends Fragment> T findOrCreateFragment(Class<T> type) {
        Fragment fragment = getSupportFragmentManager()
                .findFragmentByTag(type.getSimpleName());

        T genericFragment;

        if (fragment == null) {
            try {
                genericFragment = type.newInstance();

                ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), genericFragment,
                        R.id.fragment_recipeDetail_container);

                return genericFragment;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return type.cast(fragment);
    }

    protected RecipeIngredientsFragment findOrCreateIngredientsFragment() {
        RecipeIngredientsFragment fragment = (RecipeIngredientsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipeDetail_container);

        if (fragment == null) {
            fragment = RecipeIngredientsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.fragment_recipeDetail_container);
        }

        return fragment;
    }

    protected RecipeStepFragment findOrCreateStepFragment() {
        RecipeStepFragment fragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipeDetail_container);

        if (fragment == null) {
            fragment = RecipeStepFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment,
                    R.id.fragment_recipeDetail_container);
        }

        return fragment;
    }

    protected RecipeListPresenter createListPresenter(int recipeId, RecipeListFragment listFragment) {
        return new RecipeListPresenter(
                recipeId, Injection.provideRecipesRepository(this), listFragment, this);
    }

    protected RecipeIngredientsPresenter createIngredientsPresenter(
            int recipeId, RecipeIngredientsFragment ingredientsFragment) {

        return new RecipeIngredientsPresenter(recipeId, Injection.provideRecipesRepository(this),
                ingredientsFragment);
    }

    protected RecipeStepPresenter createStepPresenter(
            int recipeId, int stepPos, RecipeStepFragment stepFragment) {

        return new RecipeStepPresenter(recipeId, stepPos, Injection.provideRecipesRepository(this),
                stepFragment, this);
    }

    @Override
    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void enablePortraitLock(boolean enable) {
        ActivityUtils.enablePortraitLock(this, enable);
    }

    @Override
    public void toggleImmersiveMode() {
        if (ActivityUtils.isLandscape(this) && !ActivityUtils.isTablet(this)) {
            mViewRecipeListContainer.setVisibility(View.GONE);
            ActivityUtils.enterImmersiveMode(this);
        }
    }
}
