package com.htcardone.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.recipes.RecipesAdapter;
import com.htcardone.baking.recipes.RecipesContract;
import com.htcardone.baking.recipes.RecipesPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the
 * {@link IngredientsWidget IngredientsWidget} AppWidget.
 */
public class IngredientsWidgetConfigActivity extends AppCompatActivity
        implements RecipesContract.View {

    private static final String PREFS_NAME = "com.htcardone.baking.widget.IngredientsWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @BindView(R.id.recyclerView_ingredientsWidget)
    public RecyclerView mRecyclerView;

    private RecipesAdapter mAdapter;
    private RecipesContract.Presenter mPresenter;

    public IngredientsWidgetConfigActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipePref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the first recipe
    static int loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 1);
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.widget_ingredients_configure);
        ButterKnife.bind(this);

        // Setup the Recycler View
        mAdapter = new RecipesAdapter(new RecipesAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int recipeId) {
                mPresenter.onRecipeClicked(recipeId);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID,
        // finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        // Setup the presenter
        mPresenter = new RecipesPresenter(Injection.provideRecipesRepository(this),
                this);
        mPresenter.loadRecipes(false);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
    }

    @Override
    public void showRecipes(List<Recipe> recipes) {
        mAdapter.replaceData(recipes);
    }

    @Override
    public void showNoRecipes() {
        // TODO
    }

    @Override
    public void showRecipe(int recipeId) {
        // When the button is clicked, store the string locally
        //String widgetText = mAppWidgetText.getText().toString();
        saveRecipePref(this, mAppWidgetId, recipeId);

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        IngredientsWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

