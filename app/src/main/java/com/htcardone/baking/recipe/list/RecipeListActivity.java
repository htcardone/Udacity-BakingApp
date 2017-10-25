package com.htcardone.baking.recipe.list;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.recipe.RecipeActivity;
import com.htcardone.baking.recipe.RecipeContract;
import com.htcardone.baking.recipe.RecipeTabletPresenter;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsFragment;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsPresenter;
import com.htcardone.baking.recipe.detail.step.RecipeStepFragment;
import com.htcardone.baking.recipe.detail.step.RecipeStepPresenter;
import com.htcardone.baking.util.ActivityUtils;
import com.htcardone.baking.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.htcardone.baking.util.Constants.EXTRA_RECIPE_ID;

public class RecipeListActivity extends RecipeActivity implements RecipeContract.TabletView {
    private static final String LOG_TAG = RecipeListActivity.class.getSimpleName();
    private final String CURRENT_STEP_POS_KEY = "CURR_STEP_POS";

    private RecipeTabletPresenter mTabletPresenter;

    private RecipeIngredientsFragment mIngredientsFragment;
    private RecipeStepFragment mStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        mIsTwoPaneMode = ActivityUtils.isTablet(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Get the requested recipe id
        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        int stepPos = 0;
        if (savedInstanceState != null) {
            stepPos = savedInstanceState.getInt(CURRENT_STEP_POS_KEY);
        }

        if (mIsTwoPaneMode) {
            createTabletElements(recipeId, stepPos);
        } else {
            createPhoneElements(recipeId);
        }
    }

    private void createPhoneElements(int recipeId) {
        RecipeListFragment listFragment = findListFragment();
        RecipeListPresenter listPresenter = createListPresenter(recipeId, listFragment);
        listFragment.setPresenter(listPresenter);
    }

    private void createTabletElements(int recipeId, int stepPos) {
        // Fragment 1: List (Ingredients + Steps)
        RecipeListFragment listFragment = findListFragment();
        RecipeListPresenter listPresenter = createListPresenter(recipeId, listFragment);

        // Fragment 2: Detail - Ingredients
        mIngredientsFragment = findOrCreateFragment(RecipeIngredientsFragment.class);
        RecipeIngredientsPresenter ingredientsPresenter =
                createIngredientsPresenter(recipeId, mIngredientsFragment);

        // Fragment 3: Detail - Step
        mStepFragment = findOrCreateFragment(RecipeStepFragment.class);
        RecipeStepPresenter stepPresenter = createStepPresenter(recipeId, stepPos, mStepFragment);

        // Fragments connect to their presenters through a tablet presenter:
        mTabletPresenter = new RecipeTabletPresenter(
                Injection.provideRecipesRepository(this),
                listPresenter, ingredientsPresenter, stepPresenter, this);

        listFragment.setPresenter(mTabletPresenter);
        mIngredientsFragment.setPresenter(mTabletPresenter);
        mStepFragment.setPresenter(mTabletPresenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mIsTwoPaneMode) {
            //outState.putInt(CURRENT_STEP_POS_KEY, mStepPresenter.getStepPos());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showIngredientsView() {
        getSupportFragmentManager().beginTransaction()
                .hide(mStepFragment)
                .show(mIngredientsFragment)
                .commit();
    }

    @Override
    public void showStepView() {
        getSupportFragmentManager().beginTransaction()
                .hide(mIngredientsFragment)
                .show(mStepFragment)
                .commit();
    }
}
