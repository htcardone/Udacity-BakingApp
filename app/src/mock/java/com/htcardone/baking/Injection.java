package com.htcardone.baking;

import android.content.Context;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.source.local.AppDatabase;
import com.htcardone.baking.data.source.local.RecipesLocalDataSource;
import com.htcardone.baking.data.source.remote.FakeRecipesRemoteDataSource;
import com.htcardone.baking.util.AppExecutors;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Enables injection of mock implementations for
 * {@link RecipesDataSource} at compile time.
 */
public class Injection {

    public static RecipesRepository provideRecipesRepository(Context context) {
        checkNotNull(context);
        AppDatabase database = AppDatabase.getInstance(context);
        return RecipesRepository.getInstance(FakeRecipesRemoteDataSource.getInstance(context),
                RecipesLocalDataSource.getInstance(new AppExecutors(), database.recipesDao()));
    }
}
