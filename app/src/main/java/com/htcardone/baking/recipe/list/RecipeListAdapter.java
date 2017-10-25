package com.htcardone.baking.recipe.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.htcardone.baking.R;
import com.htcardone.baking.data.model.StepsItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ListItemViewHolder> {
    private final ListItemClickListener mItemClickListener;
    private final Context mContext;
    private List<StepsItem> mDataSet;
    private int mHighlightPos = -1;

    public RecipeListAdapter(Context mContext, ListItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
        this.mContext = mContext;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recipe_list, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        if (position == 0) {
            holder.mTextViewNumber.setVisibility(View.INVISIBLE);
        } else {
            holder.mTextViewNumber.setVisibility(View.VISIBLE);
        }

        if (position == mHighlightPos) {
            holder.mViewHighlight.setVisibility(View.VISIBLE);
        } else {
            holder.mViewHighlight.setVisibility(View.INVISIBLE);
        }

        holder.mTextViewNumber.setText(String.format(
                mContext.getString(R.string.recipe_list_number), String.valueOf(position)));
        holder.mTextViewDesc.setText(mDataSet.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return (mDataSet == null) ? 0 : mDataSet.size();
    }

    public void setHighlightPos(int position) {
        mHighlightPos = position;
    }

    public void replaceData(List<StepsItem> steps) {
        mDataSet = steps;
    }

    public interface ListItemClickListener {
        void onListItemClick(int stepPos);
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_recipeListItem_number)
        TextView mTextViewNumber;

        @BindView(R.id.textView_recipeListItem_desc)
        TextView mTextViewDesc;

        @BindView(R.id.view_recipeListItem_highlight)
        View mViewHighlight;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onListItemClick(getAdapterPosition());
                }
            });
        }
    }
}
