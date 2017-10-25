package com.htcardone.baking.recipe.detail.step;

import android.os.Bundle;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.recipe.RecipeActivity;

import static com.htcardone.baking.util.Constants.EXTRA_RECIPE_ID;
import static com.htcardone.baking.util.Constants.EXTRA_STEP_POS;

public class RecipeStepActivity extends RecipeActivity {

    private RecipeStepFragment mStepFragment;
    private RecipeStepPresenter mStepPresenter;

    private final String CURRENT_STEP_POS_KEY = "CURR_STEP_POS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mStepFragment = (RecipeStepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_recipeDetail_container);

        if (mStepFragment == null) {
            mStepFragment = RecipeStepFragment.newInstance();
        }

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);
        int stepPos = getIntent().getIntExtra(EXTRA_STEP_POS, 0);

        if (savedInstanceState != null) {
            stepPos = savedInstanceState.getInt(CURRENT_STEP_POS_KEY);
        }

        mStepPresenter = new RecipeStepPresenter(recipeId, stepPos,
                Injection.provideRecipesRepository(this), mStepFragment, this);

        mStepFragment.setPresenter(mStepPresenter);

        if (!mStepFragment.isAdded()) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_recipeDetail_container, mStepFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_STEP_POS_KEY, mStepPresenter.getStepPos());
        super.onSaveInstanceState(outState);
    }
}
