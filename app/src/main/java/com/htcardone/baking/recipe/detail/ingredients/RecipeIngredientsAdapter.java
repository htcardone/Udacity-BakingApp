package com.htcardone.baking.recipe.detail.ingredients;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htcardone.baking.R;
import com.htcardone.baking.data.model.IngredientsItem;
import com.htcardone.baking.recipe.list.RecipeListAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientsAdapter extends
        RecyclerView.Adapter<RecipeIngredientsAdapter.IngredientItemViewHolder> {

    private List<IngredientsItem> mDataSet;

    @Override
    public IngredientItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ingredient, parent, false);
        return new RecipeIngredientsAdapter.IngredientItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientItemViewHolder holder, int position) {
        IngredientsItem ingredient = mDataSet.get(position);
        holder.textViewMeasure.setText(ingredient.getMeasure());
        holder.textViewQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.textViewName.setText(ingredient.getIngredient());
    }

    @Override
    public int getItemCount() {
        return (mDataSet == null) ? 0 : mDataSet.size();
    }

    public void replaceData(List<IngredientsItem> ingredients) {
        mDataSet = ingredients;
    }

    class IngredientItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_ingredientItem_quantity)
        TextView textViewQuantity;

        @BindView(R.id.textView_ingredientItem_measure)
        TextView textViewMeasure;

        @BindView(R.id.textView_ingredientItem_name)
        TextView textViewName;
        IngredientItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
