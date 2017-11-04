package com.htcardone.baking.recipe.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.htcardone.baking.R;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.recipe.detail.ingredients.RecipeIngredientsActivity;
import com.htcardone.baking.recipe.detail.step.RecipeStepActivity;
import com.htcardone.baking.util.Log;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v4.util.Preconditions.checkNotNull;
import static com.htcardone.baking.util.Constants.EXTRA_RECIPE_ID;
import static com.htcardone.baking.util.Constants.EXTRA_STEP_POS;

public class RecipeListFragment extends Fragment implements RecipeListContract.View {
    private final static String LOG_TAG = RecipeListFragment.class.getSimpleName();

    private RecipeListContract.Presenter mPresenter;
    private RecipeListAdapter mAdapter;

    @BindView(R.id.nestedScrollView_recipeList)
    NestedScrollView mScrollView;
    @BindView(R.id.recyclerView_recipeList)
    RecyclerView mRecyclerView;
    @BindView(R.id.textView_recipeListItem_image)
    ImageView mImageView;
    @BindView(R.id.layout_recipeListItem_image)
    View mLayoutImageContainer;
    @BindView(R.id.layout_recipeListItem_ingredients)
    View mLayoutIngredients;
    @BindView(R.id.view_recipeListItem_highlight)
    View mViewHighlight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        mLayoutIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onIngredientsClicked();
            }
        });

        mAdapter = new RecipeListAdapter(getContext(),
                new RecipeListAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int stepPos) {
                        mPresenter.onStepClicked(stepPos);
                    }
                });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume()");
        mPresenter.loadRecipe();
    }

    @Override
    public void showRecipe(Recipe recipe) {
        Log.d(LOG_TAG, "showRecipe()");
        if(TextUtils.isEmpty(recipe.getImage())) {
            mLayoutImageContainer.setVisibility(View.GONE);
        } else {
            mLayoutImageContainer.setVisibility(View.VISIBLE);
            Picasso.with(getContext())
                    .load(recipe.getImage())
                    .into(mImageView);
        }

        mAdapter.replaceData(recipe.getSteps());
    }

    @Override
    public void openIngredientsDetails(int recipeId) {
        Intent intent = new Intent(getContext(), RecipeIngredientsActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @Override
    public void openStepDetails(int recipeId, int stepPos) {
        Intent intent = new Intent(getContext(), RecipeStepActivity.class);
        intent.putExtra(EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(EXTRA_STEP_POS, stepPos);
        startActivity(intent);
    }

    @Override
    public void highlightStep(int stepPos) {
        if (stepPos == -1) {
            mViewHighlight.setVisibility(View.VISIBLE);
        } else {
            mViewHighlight.setVisibility(View.INVISIBLE);
            Log.d(LOG_TAG, "highlightStep stepPos=" + stepPos + " " + mRecyclerView.getChildAt(stepPos) + " " + mRecyclerView.getChildCount());
            //mScrollView.smoothScrollTo(0, (int) mRecyclerView.getChildAt(stepPos).getY());
        }

        mAdapter.setHighlightPos(stepPos);
        mAdapter.notifyDataSetChanged();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setPresenter(RecipeListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }
}
