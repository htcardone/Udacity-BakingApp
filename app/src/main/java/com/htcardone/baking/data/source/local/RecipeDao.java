package com.htcardone.baking.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.htcardone.baking.data.model.Recipe;

import java.util.List;

/**
 * Data Access Object for the Recipes table.
 */
@Dao
public interface RecipeDao {
    /**
     * Select all recipes from the recipes table.
     *
     * @return all recipes.
     */
    @Query("SELECT * FROM Recipe")
    List<Recipe> getRecipes();

    /**
     * Select a recipe by id.
     *
     * @param recipeId the recipe id.
     * @return the recipe with recipeId.
     */
    @Query("SELECT * FROM Recipe WHERE id = :recipeId")
    Recipe getRecipeById(int recipeId);

    /**
     * Insert a recipe in the database. If the recipe already exists, replace it.
     *
     * @param recipe the recipe to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipe(Recipe recipe);

    /**
     * Delete all recipes.
     */
    @Query("DELETE FROM Recipe")
    void deleteRecipes();
}