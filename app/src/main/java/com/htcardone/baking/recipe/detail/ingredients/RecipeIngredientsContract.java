package com.htcardone.baking.recipe.detail.ingredients;

import com.htcardone.baking.BaseView;
import com.htcardone.baking.data.model.IngredientsItem;

import java.util.List;

public interface RecipeIngredientsContract {
    interface View extends BaseView<RecipeIngredientsContract.Presenter> {
        void showIngredients(List<IngredientsItem> ingredients);
    }

    interface Presenter {
        void loadIngredients();
    }
}
