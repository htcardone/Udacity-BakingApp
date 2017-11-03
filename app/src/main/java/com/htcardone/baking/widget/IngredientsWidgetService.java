package com.htcardone.baking.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.htcardone.baking.Injection;
import com.htcardone.baking.R;
import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.model.IngredientsItem;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.util.Constants;
import com.htcardone.baking.util.Log;

import java.util.ArrayList;
import java.util.List;

public class IngredientsWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String LOG_TAG = "IngredientsWidget";

    private List<IngredientsItem> mDataSet = new ArrayList<>();
    private Context mContext;
    private int mAppWidgetId;
    private int mRecipeId;
    private Recipe mRecipe;

    public IngredientsRemoteViewsFactory(Context context, Intent intent) {
        Log.d(LOG_TAG, "constructor()");
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mRecipeId = IngredientsWidgetConfigActivity.loadRecipePref(context, mAppWidgetId);

        RecipesRepository repository = Injection.provideRecipesRepository(mContext);
        repository.getRecipe(mRecipeId, new RecipesDataSource.GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                Log.d(LOG_TAG, "constructor() onRecipeLoaded()");
                mRecipe = recipe;
                mDataSet = mRecipe.getIngredients();
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(LOG_TAG, "constructor() onDataNotAvailable()");
                //TODO
            }
        });
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate() mAppWidgetId=" + mAppWidgetId + " mRecipeId=" + mRecipeId);
        if (mRecipe != null) {
            mDataSet = mRecipe.getIngredients();
        }
    }

    @Override
    public void onDataSetChanged() {
        Log.d(LOG_TAG, "onDataSetChanged()");
        if (mRecipe != null) {
            mDataSet = mRecipe.getIngredients();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG, "getCount() " + mDataSet.size());
        return mDataSet.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(LOG_TAG, "getViewAt() mAppWidgetId=" + mAppWidgetId + " mRecipeId=" + mRecipeId
                + " position=" + position);
        // Construct a remote views item based on the app widget item XML file,
        // and set the text based on the position.
        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_ingredients_item);
        views.setTextViewText(R.id.textView_ingredientsWidgetItem,
                mDataSet.get(position).getIngredient());

        // Return the remote views object.
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
