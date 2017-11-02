package com.htcardone.baking.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.htcardone.baking.data.model.Recipe;

/**
 * The Room Database that contains the Recipes table.
 */
@Database(entities = {Recipe.class}, version = 1)
@TypeConverters({Recipe.Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract RecipeDao recipesDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "app.db")
                        .build();
            }
            return INSTANCE;
        }
    }
}
