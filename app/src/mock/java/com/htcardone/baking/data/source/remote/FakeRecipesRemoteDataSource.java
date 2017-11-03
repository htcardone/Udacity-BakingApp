package com.htcardone.baking.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FakeRecipesRemoteDataSource implements RecipesDataSource {
    private final String LOG_TAG = FakeRecipesRemoteDataSource.class.getSimpleName();
    private static FakeRecipesRemoteDataSource INSTANCE;
    private final List<Recipe> mFakeRecipes;

    private FakeRecipesRemoteDataSource(Context context) {
        InputStream inputStream = context.getResources().openRawResource(R.raw.baking);
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));

        Type recipeListType = new TypeToken<ArrayList<Recipe>>(){}.getType();
        mFakeRecipes = new Gson().fromJson(reader, recipeListType);
    }

    public static FakeRecipesRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new FakeRecipesRemoteDataSource(context);
        }

        return INSTANCE;
    }

    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        callback.onRecipesLoaded(mFakeRecipes);
    }

    @Override
    public void getRecipe(@NonNull int recipeId, @NonNull GetRecipeCallback callback) {
        throw new UnsupportedOperationException("This method should be only used to get local " +
                "Recipe");
    }

    @Override
    public void saveRecipe(@NonNull Recipe recipe) {
        throw new UnsupportedOperationException("This method should be only used to save local " +
                "Recipe");
    }

    @Override
    public void refreshRecipes() {
        throw new UnsupportedOperationException("This method should be only used to refresh" +
                " local Recipes");
    }

    @Override
    public void deleteAllRecipes() {
        throw new UnsupportedOperationException("This method should be only used to get local " +
                "Recipe");
    }

    public List<Recipe> getFakeRecipes() {
        return mFakeRecipes;
    }
}
