package com.htcardone.baking.recipe.detail.ingredients;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.htcardone.baking.R;
import com.htcardone.baking.data.model.IngredientsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsFragment extends Fragment implements RecipeIngredientsContract.View {

    private RecipeIngredientsContract.Presenter mPresenter;

    private final RecipeIngredientsAdapter mAdapter = new RecipeIngredientsAdapter();

    @BindView(R.id.recyclerView_ingredients)
    RecyclerView mRecyclerView;

    public static RecipeIngredientsFragment newInstance() {
        return new RecipeIngredientsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView =
                inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void setPresenter(RecipeIngredientsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showIngredients(List<IngredientsItem> ingredients) {
        mAdapter.replaceData(ingredients);
    }
}
