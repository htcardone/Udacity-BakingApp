package com.htcardone.baking.recipe.list;

import android.os.Bundle;

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

import butterknife.ButterKnife;

import static com.htcardone.baking.util.Constants.EXTRA_RECIPE_ID;
import static com.htcardone.baking.util.Constants.FRAG_TYPE_INGREDIENT;
import static com.htcardone.baking.util.Constants.FRAG_TYPE_STEP;

public class RecipeListActivity extends RecipeActivity implements RecipeContract.TabletView {
    private final String LOG_TAG = RecipeListActivity.class.getSimpleName();

    private int mCurrentStepPos = -1; // -1: Ingredients, 0 -> n: Steps

    private final String CURRENT_FRAG_TYPE = "CURR_FRAG_TYPE";
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

        if (mIsTwoPaneMode) {
            int stepPos = 0;
            int fragmentType = FRAG_TYPE_INGREDIENT;

            if (savedInstanceState != null) {
                stepPos = savedInstanceState.getInt(CURRENT_STEP_POS_KEY);
                fragmentType = savedInstanceState.getInt(CURRENT_FRAG_TYPE);
            }

            createTabletElements(recipeId, stepPos, fragmentType);
        } else {
            createPhoneElements(recipeId);
        }
    }

    private void createPhoneElements(int recipeId) {
        RecipeListFragment listFragment = findListFragment();
        RecipeListPresenter listPresenter = createListPresenter(recipeId, listFragment);
        listFragment.setPresenter(listPresenter);
    }

    private void createTabletElements(int recipeId, int stepPos, int fragmentType) {
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
                listPresenter, ingredientsPresenter, stepPresenter, this, fragmentType);

        listFragment.setPresenter(mTabletPresenter);
        mIngredientsFragment.setPresenter(mTabletPresenter);
        mStepFragment.setPresenter(mTabletPresenter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mIsTwoPaneMode) {
            if (mCurrentStepPos == -1) {
                outState.putInt(CURRENT_FRAG_TYPE, FRAG_TYPE_INGREDIENT);
            } else {
                outState.putInt(CURRENT_FRAG_TYPE, FRAG_TYPE_STEP);
            }

            outState.putInt(CURRENT_STEP_POS_KEY, mCurrentStepPos);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showIngredientsView() {
        if (mIsTwoPaneMode) {
            mCurrentStepPos = -1;
        }

        getSupportFragmentManager().beginTransaction()
                .hide(mStepFragment)
                .show(mIngredientsFragment)
                .commit();
    }

    @Override
    public void showStepView() {
        if (mIsTwoPaneMode) {
            mCurrentStepPos = mTabletPresenter.getStepPos();
        }

        getSupportFragmentManager().beginTransaction()
                .hide(mIngredientsFragment)
                .show(mStepFragment)
                .commit();
    }
}
