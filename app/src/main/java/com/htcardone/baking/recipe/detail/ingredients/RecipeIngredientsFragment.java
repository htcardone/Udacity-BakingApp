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
import com.htcardone.baking.util.Log;

public class RecipeIngredientsFragment extends Fragment implements RecipeIngredientsContract.View {

    private final String LOG_TAG = RecipeIngredientsFragment.class.getSimpleName();

    private final String CLICKED_INGREDIENTS_POS_KEY = "clickedIngPosKey";

    private RecipeIngredientsContract.Presenter mPresenter;

    private RecipeIngredientsAdapter mAdapter;
    
    private boolean[] mClickedIngredientsPos;

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

        // Load previously saved state, if available.
        if (savedInstanceState != null) {
            mClickedIngredientsPos = savedInstanceState.getBooleanArray(CLICKED_INGREDIENTS_POS_KEY);
        }

        mAdapter = new RecipeIngredientsAdapter(new RecipeIngredientsAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int position) {
                mClickedIngredientsPos[position] = !mClickedIngredientsPos[position];
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
        mPresenter.loadIngredients();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBooleanArray(CLICKED_INGREDIENTS_POS_KEY, mClickedIngredientsPos);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setPresenter(RecipeIngredientsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showIngredients(List<IngredientsItem> ingredients) {
        if (mClickedIngredientsPos == null) {
            mClickedIngredientsPos = new boolean[ingredients.size()];
        }

        mAdapter.replaceData(ingredients, mClickedIngredientsPos);
    }
}
