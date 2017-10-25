package com.htcardone.baking.data;

import android.support.annotation.NonNull;

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

        getRecipesFromRemoteDataSource(callback);

        /*if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getRecipesFromRemoteDataSource(callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mTasksLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    callback.onTasksLoaded(new ArrayList<>(mCachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }*/
    }

    @Override
    public Recipe getRecipe(@NonNull int recipeId) {
        return mCachedRecipes.get(recipeId);
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

    private void refreshLocalDataSource(List<Recipe> recipes) {
        if (mCachedRecipes == null) {
            mCachedRecipes = new LinkedHashMap<>();
        }

        mCachedRecipes.clear();

        for (Recipe recipe : recipes) {
            mCachedRecipes.put(recipe.getId(), recipe);
        }

        mCacheIsDirty = false;
    }

    private void refreshCache(List<Recipe> recipes) {
    }
}
