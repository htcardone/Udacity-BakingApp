package com.htcardone.baking.data.source.remote;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipesRemoteDataSource implements RecipesDataSource {
    private final String LOG_TAG = RecipesRemoteDataSource.class.getSimpleName();
    private static RecipesRemoteDataSource INSTANCE;
    private static BakingApi bakingApi;

    private RecipesRemoteDataSource() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bakingApi = retrofit.create(BakingApi.class);
    }

    public static RecipesRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRemoteDataSource();
        }

        return INSTANCE;
    }

    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        bakingApi.getRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(LOG_TAG, "getRecipes() onResponse isSuccessful ? " + response.isSuccessful());
                if (response.isSuccessful()) callback.onRecipesLoaded(response.body());
                else callback.onDataNotAvailable();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(LOG_TAG, "getRecipes() onFailure", t);
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getRecipe(@NonNull int recipeId, GetRecipeCallback callback) {
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

    private interface BakingApi {
        @GET("baking.json")
        Call<List<Recipe>> getRecipes();
    }
}
