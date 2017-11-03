package com.htcardone.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.recipe.list.RecipeListActivity;
import com.htcardone.baking.recipes.RecipesAdapter;
import com.htcardone.baking.util.Constants;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in
 * {@link IngredientsWidgetConfigActivity IngredientsWidgetConfigActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        int recipeId = IngredientsWidgetConfigActivity.loadRecipePref(context, appWidgetId);

        // Set up the intent that starts the StackViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, IngredientsWidgetService.class);

        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Instantiate the RemoteViews object for the app widget layout.
        final RemoteViews views =
                new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);

        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects to a RemoteViewsService  through the specified intent.
        views.setRemoteAdapter(R.id.listView_ingredientsWidget, intent);

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews object above.
        views.setEmptyView(R.id.listView_ingredientsWidget, R.id.textView_ingredientsWidget_empty);

        // Set the recipe name and icon
        RecipesRepository repository = Injection.provideRecipesRepository(context);
        repository.getRecipe(recipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                views.setTextViewText(R.id.textView_ingredientsWidget_name, recipe.getName());
                views.setImageViewResource(R.id.imageView_ingredientsWidget_icon,
                        RecipesAdapter.getRecipeIcon(recipe.getName()));

                // Instruct the widget manager to update the widget
                appWidgetManager.updateAppWidget(appWidgetId, views);
            }

            @Override
            public void onDataNotAvailable() {
                //TODO
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientsWidgetConfigActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

