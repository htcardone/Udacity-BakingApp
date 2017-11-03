package com.htcardone.baking.recipes;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.recipe.list.RecipeListActivity;
import com.htcardone.baking.util.ActivityUtils;
import com.htcardone.baking.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.htcardone.baking.util.Constants.EXTRA_RECIPE_ID;

public class RecipesActivity extends AppCompatActivity implements RecipesContract.View {
    private final String LOG_TAG = RecipesActivity.class.getSimpleName();

    @BindView(R.id.swipeRefreshLayout_recipes)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView_recipes)
    RecyclerView mRecyclerView;

    private RecipesContract.Presenter mRecipesPresenter;
    private RecipesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);

        // LayoutManager setup
        LayoutManager layoutManager =
                ActivityUtils.isTablet(this) || ActivityUtils.isLandscape(this) ?
                    new GridLayoutManager(this, 2) :
                    new LinearLayoutManager(this);

        // RecyclerView setup
        mAdapter = new RecipesAdapter(new RecipesAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int recipeId) {
                mRecipesPresenter.onRecipeClicked(recipeId);
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Create the presenter
        mRecipesPresenter = new RecipesPresenter(Injection.provideRecipesRepository(this), this);

        // Swipe to refresh setup
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRecipesPresenter.loadRecipes(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecipesPresenter.loadRecipes(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.activity_recipes_refresh:
                mRecipesPresenter.loadRecipes(true);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        mSwipeRefreshLayout.setRefreshing(active);
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mAdapter.replaceData(recipes);
    }

    @Override
    public void showNoRecipes() {
        //TODO
    }

    @Override
    public void showRecipe(int recipeId) {
        Log.d(LOG_TAG, "showRecipe() " + recipeId);
        Intent intent = new Intent(this, RecipeListActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        startActivity(intent);
    }
}
