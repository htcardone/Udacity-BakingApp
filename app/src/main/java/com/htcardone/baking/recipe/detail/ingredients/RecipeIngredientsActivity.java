package com.htcardone.baking.recipe.detail.ingredients;

import android.os.Bundle;

import com.htcardone.baking.R;
import com.htcardone.baking.recipe.RecipeActivity;

import static com.htcardone.baking.util.Constants.EXTRA_RECIPE_ID;

public class RecipeIngredientsActivity extends RecipeActivity {
    private RecipeIngredientsContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_ingredients);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        int recipeId = getIntent().getIntExtra(EXTRA_RECIPE_ID, 0);

        RecipeIngredientsFragment fragment = findOrCreateIngredientsFragment();
        mPresenter = createIngredientsPresenter(recipeId, fragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }
}
