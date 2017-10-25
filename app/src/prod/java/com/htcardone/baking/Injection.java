package com.htcardone.baking;

import android.content.Context;

import com.htcardone.baking.data.RecipesDataSource;
import com.htcardone.baking.data.RecipesRepository;
import com.htcardone.baking.data.source.local.RecipesLocalDataSource;
import com.htcardone.baking.data.source.remote.RecipesRemoteDataSource;

/**
 * Enables injection of production implementations for
 * {@link RecipesDataSource} at compile time.
 */
public class Injection {

    public static RecipesRepository provideRecipesRepository(Context context) {
        return RecipesRepository.getInstance(RecipesRemoteDataSource.getInstance(),
                RecipesLocalDataSource.getInstance());
    }
}
