package com.htcardone.baking.recipes;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.htcardone.baking.R;
import com.htcardone.baking.data.model.Recipe;
import com.htcardone.baking.util.Log;

import java.util.ArrayList;
import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeItemViewHolder> {
    private final String LOG_TAG = RecipesAdapter.class.getSimpleName();
    private List<Recipe> mDataSet = new ArrayList<>();
    private final ListItemClickListener mOnClickListener;

    public RecipesAdapter(ListItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int recipeId);
    }

    public void replaceData(List<Recipe> recipes) {
        mDataSet = recipes;
        notifyDataSetChanged();
    }

    public class RecipeItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cardView_recipeItem)
        CardView cardView;
        @BindView(R.id.textView_recipeItem_name)
        TextView textViewName;
        @BindView(R.id.textView_recipeItem_ingredients)
        TextView textViewIngredients;
        @BindView(R.id.textView_recipeItem_steps)
        TextView textViewSteps;
        @BindView(R.id.textView_recipeItem_servings)
        TextView textViewServings;
        @BindView(R.id.imageView_recipeItem_icon)
        ImageView imageViewIcon;

        public RecipeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onListItemClick(mDataSet.get(getAdapterPosition()).getId());
                }
            });
        }
    }

    @Override
    public RecipeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe, parent, false);
        return new RecipeItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeItemViewHolder holder, int position) {
        Recipe recipe = mDataSet.get(position);

        holder.textViewName.setText(recipe.getName());
        holder.textViewServings.setText(String.valueOf(recipe.getServings()));
        holder.textViewIngredients.setText(getListSize(recipe.getIngredients()));
        holder.textViewSteps.setText(getListSize(recipe.getSteps()));

        holder.imageViewIcon.setVisibility(View.VISIBLE);
        int iconRes = getRecipeIcon(recipe.getName());

        if (iconRes > 0) {
            holder.imageViewIcon.setImageResource(iconRes);
        } else {
            holder.imageViewIcon.setVisibility(View.INVISIBLE);
        }

        // tagging the view in order to test it, checking the resource id
        holder.imageViewIcon.setTag(iconRes);
    }

    public static int getRecipeIcon(String recipeName) {
        String name = recipeName.toLowerCase();
        if (name.contains("pie")) return R.drawable.ic_pie;
        if (name.contains("brownie")) return R.drawable.ic_brownie;
        if (name.contains("cheesecake")) return R.drawable.ic_cake;
        if (name.contains("cupcake")) return R.drawable.ic_cupcake;
        if (name.contains("cake")) return R.drawable.ic_cake;
        if (name.contains("doughnut")) return R.drawable.ic_doughnut;
        if (name.contains("cookie")) return R.drawable.ic_cookie;
        return 0;
    }

    public static String getListSize(List list) {
        if (list != null) return String.valueOf(list.size());
        else return "0";
    }

    @Override
    public long getItemId(int position) {
        return mDataSet.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
