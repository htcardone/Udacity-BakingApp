package com.htcardone.baking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.htcardone.baking.R;
import com.htcardone.baking.util.Constants;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in
 * {@link IngredientsWidgetConfigActivity IngredientsWidgetConfigActivity}
 */
public class IngredientsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        int recipeId = IngredientsWidgetConfigActivity.loadRecipePref(context, appWidgetId);

        // Set up the intent that starts the StackViewService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, IngredientsWidgetService.class);

        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra(Constants.EXTRA_RECIPE_ID, recipeId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        // Instantiate the RemoteViews object for the app widget layout.
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);

        // Set up the RemoteViews object to use a RemoteViews adapter.
        // This adapter connects to a RemoteViewsService  through the specified intent.
        views.setRemoteAdapter(R.id.listView_ingredientsWidget, intent);

        // The empty view is displayed when the collection has no items.
        // It should be in the same layout used to instantiate the RemoteViews object above.
        views.setEmptyView(R.id.listView_ingredientsWidget, R.id.textView_ingredientsWidget_empty);

        // Set the recipe name
        views.setTextViewText(R.id.textView_ingredientsWidget_name, "" + recipeId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
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

