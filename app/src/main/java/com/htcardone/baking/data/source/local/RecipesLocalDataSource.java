package com.htcardone.baking.data.source.local;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.util.AppExecutors;

import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Concrete implementation of a data source as a db.
 */
public class RecipesLocalDataSource implements RecipesDataSource {

    private static RecipesLocalDataSource INSTANCE;

    private RecipeDao mRecipeDao;

    private AppExecutors mAppExecutors;

    private RecipesLocalDataSource(@NonNull AppExecutors appExecutors,
                                   @NonNull RecipeDao recipeDao) {
        mAppExecutors = appExecutors;
        mRecipeDao = recipeDao;

    }

    public static RecipesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                     @NonNull RecipeDao recipeDao) {
        if (INSTANCE == null) {
            synchronized (RecipesLocalDataSource.class) {
                INSTANCE = new RecipesLocalDataSource(appExecutors, recipeDao);
            }
        }

        return INSTANCE;
    }

    /**
     * Note: {@link LoadRecipesCallback#onDataNotAvailable()} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public void getRecipes(@NonNull final LoadRecipesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Recipe> recipes = mRecipeDao.getRecipes();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipes.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable();
                        } else {
                            callback.onRecipesLoaded(recipes);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    /**
     * Note: {@link GetRecipeCallback#onDataNotAvailable()} is fired if the {@link Recipe} isn't
     * found.
     */
    @Override
    public void getRecipe(final int recipeId, @NonNull final GetRecipeCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Recipe recipe = mRecipeDao.getRecipeById(recipeId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipe != null) {
                            callback.onRecipeLoaded(recipe);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void saveRecipe(@NonNull final Recipe recipe) {
        checkNotNull(recipe);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mRecipeDao.insertRecipe(recipe);
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshRecipes() {
        // Not required because the {@link RecipesRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteAllRecipes() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mRecipeDao.deleteRecipes();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }
}
