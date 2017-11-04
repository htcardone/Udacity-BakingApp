package com.htcardone.baking.data;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.util.Log;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Concrete implementation to load recipes from the data sources into a cache.
 */
public class RecipesRepository implements RecipesDataSource {
    private final String LOG_TAG = RecipesRepository.class.getSimpleName();
    private static RecipesRepository INSTANCE;
    private final RecipesDataSource mRecipesRemoteDataSource;
    private final RecipesDataSource mRecipesLocalDataSource;

    /**
     * This variable has package local visibility so it can be accessed from tests.
     */
    protected Map<Integer, Recipe> mCachedRecipes;

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    @SuppressLint("RestrictedApi")
    private RecipesRepository(@NonNull RecipesDataSource recipesRemoteDataSource,
                              @NonNull RecipesDataSource recipesLocalDataSource) {
        mRecipesRemoteDataSource = checkNotNull(recipesRemoteDataSource);
        mRecipesLocalDataSource = checkNotNull(recipesLocalDataSource);
    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param recipesRemoteDataSource the backend data source
     * @param recipesLocalDataSource the device storage data source
     * @return the {@link RecipesRepository} instance
     */
    public static RecipesRepository getInstance (RecipesDataSource recipesRemoteDataSource,
                                                 RecipesDataSource recipesLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRepository(recipesRemoteDataSource, recipesLocalDataSource);
        }

        return INSTANCE;
    }

    /**
     * Gets recipes from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     * <p>
     * Note: {@link LoadRecipesCallback#onDataNotAvailable()} is fired if all data sources fail to
     * get the data.
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        checkNotNull(callback);
        Log.d(LOG_TAG, "getRecipes()");

        // Respond immediately with cache if available and not dirty
        if (mCachedRecipes != null && !mCacheIsDirty) {
            Log.d(LOG_TAG, "getRecipes() using cache");
            callback.onRecipesLoaded(new ArrayList<>(mCachedRecipes.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getRecipesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mRecipesLocalDataSource.getRecipes(new LoadRecipesCallback() {
                @Override
                public void onRecipesLoaded(List<Recipe> recipes) {
                    Log.d(LOG_TAG, "getRecipes() using db");
                    refreshCache(recipes);
                    callback.onRecipesLoaded(new ArrayList<>(mCachedRecipes.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getRecipesFromRemoteDataSource(callback);
                }
            });
        }
    }

    /**
     * Gets recipe from local data source (sqlite) unless the table is new or empty.
     * <p>
     * Note: {@link GetRecipeCallback#onDataNotAvailable()} is fired if both data sources fail to
     * get the data.
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void getRecipe(final int recipeId, @NonNull final GetRecipeCallback callback) {
        checkNotNull(recipeId);
        checkNotNull(callback);

        Recipe cachedRecipe = getRecipeWithId(recipeId);

        // Respond immediately with cache if available
        if (cachedRecipe != null) {
            callback.onRecipeLoaded(cachedRecipe);
            return;
        }

        // Load from server/persisted if needed.

        // Is the recipe in the local data source? If not, query the network.
        mRecipesLocalDataSource.getRecipe(recipeId, new GetRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                // Do in memory cache update to keep the app UI up to date
                if (mCachedRecipes == null) {
                    mCachedRecipes = new LinkedHashMap<>();
                }
                mCachedRecipes.put(recipe.getId(), recipe);
                callback.onRecipeLoaded(recipe);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void saveRecipe(@NonNull Recipe recipe) {
        checkNotNull(recipe);
        mRecipesLocalDataSource.saveRecipe(recipe);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }
        mCachedRecipes.put(recipe.getId(), recipe);
    }

    @Override
    public void refreshRecipes() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllRecipes() {
        mRecipesLocalDataSource.deleteAllRecipes();

        if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }
        mCachedRecipes.clear();
    }


    private void getRecipesFromRemoteDataSource(final LoadRecipesCallback callback) {
        Log.d(LOG_TAG, "getRecipesFromRemoteDataSource()");
        mRecipesRemoteDataSource.getRecipes(new LoadRecipesCallback() {
            @Override
            public void onRecipesLoaded(List<Recipe> recipes) {
                Log.d(LOG_TAG, "getRecipesFromRemoteDataSource() onRecipesLoaded " + recipes);
                refreshCache(recipes);
                refreshLocalDataSource(recipes);
                callback.onRecipesLoaded(recipes);
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(LOG_TAG, "getRecipesFromRemoteDataSource() onDataNotAvailable");
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Recipe> recipes) {
        if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }

        mCachedRecipes.clear();

        for (Recipe recipe : recipes) {
            mCachedRecipes.put(recipe.getId(), recipe);
        }

        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Recipe> recipes) {
        mRecipesLocalDataSource.deleteAllRecipes();
        for (Recipe recipe : recipes) {
            mRecipesLocalDataSource.saveRecipe(recipe);
        }
    }

    @SuppressLint("RestrictedApi")
    @Nullable
    private Recipe getRecipeWithId(int recipeId) {
        checkNotNull(recipeId);
        if (mCachedRecipes == null || mCachedRecipes.isEmpty()) {
            return null;
        } else {
            return mCachedRecipes.get(recipeId);
        }
    }
}
