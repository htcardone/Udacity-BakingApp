package com.htcardone.baking;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.data.source.remote.FakeRecipesRemoteDataSource;
import com.htcardone.baking.recipes.RecipesActivity;
import com.htcardone.baking.recipes.RecipesAdapter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipesActivityTest {

    /**
     * {@link ActivityTestRule} is a JUnit {@link Rule @Rule} to launch our activity under test.
     * <p>
     * Rules are interceptors which are executed for each test method and are important building
     * blocks of Junit tests.
     */
    @Rule
    public ActivityTestRule<RecipesActivity> mRecipesActivityTestRule =
            new ActivityTestRule<>(RecipesActivity.class);

    // Convenience helpers
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Test
    public void showRecipes() {
        List<Recipe> recipeList = FakeRecipesRemoteDataSource
                .getInstance(InstrumentationRegistry.getContext())
                .getFakeRecipes();

        for (int i = 0; i < recipeList.size(); i++) {
            Recipe recipe = recipeList.get(i);

            onView(withId(R.id.recyclerView_recipes)).perform(scrollToPosition(i));

            ViewInteraction itemView =
                    onView(withRecyclerView(R.id.recyclerView_recipes).atPosition(i));

            // Check name
            itemView.check(matches(hasDescendant(
                    withText(recipe.getName()))));
            // Check ingredients
            itemView.check(matches(hasDescendant(
                    withText(RecipesAdapter.getListSize(recipe.getIngredients())))));
            // Check steps
            itemView.check(matches(hasDescendant(
                    withText(RecipesAdapter.getListSize(recipe.getSteps())))));
            // Check servings
            itemView.check(matches(hasDescendant(
                    withText(String.valueOf(recipe.getServings())))));

            // Check icon
            ViewInteraction imgView = onView(withRecyclerView(R.id.recyclerView_recipes)
                    .atPositionOnView(i, R.id.imageView_recipeItem_icon));

            int iconRes = RecipesAdapter.getRecipeIcon(recipe.getName());

            // If we don't have the resource for the recipe, the ImageView should be hidden.
            if (iconRes > 0) {
                imgView.check(matches(isDisplayed()));
            } else {
                imgView.check(matches(not(isDisplayed())));
            }

            // Check if the resource used is correct.
            imgView.check(matches(withTagValue(is((Object) iconRes))));
        }
    }
}
