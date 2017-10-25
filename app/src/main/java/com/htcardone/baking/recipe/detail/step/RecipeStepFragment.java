package com.htcardone.baking.recipe.detail.step;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.htcardone.baking.R;
import com.htcardone.baking.data.model.StepsItem;
import com.htcardone.baking.util.ActivityUtils;
import com.htcardone.baking.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment implements RecipeStepContract.View {
    private final String LOG_TAG = RecipeStepFragment.class.getSimpleName();

    private RecipeStepContract.Presenter mPresenter;

    @BindView(R.id.playerView_recipeStep)
    SimpleExoPlayerView mPlayerView;

    @Nullable
    @BindView(R.id.textView_recipeStep_desc)
    TextView mTextViewDesc;

    @Nullable
    @BindView(R.id.button_recipeStep_next)
    Button mButtonNext;

    @Nullable
    @BindView(R.id.button_recipeStep_prev)
    Button mButtonPrev;

    public static RecipeStepFragment newInstance() {
        return new RecipeStepFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, rootView);

        // Setup the buttons listeners
        if (mButtonNext != null) {
            mButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onNextClicked();
                }
            });

            mButtonPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.onPrevClicked();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(RecipeStepContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStep(StepsItem stepsItem) {
        // On phone landscape mode, mTextViewDesc is not present in the layout
        if (mTextViewDesc != null){
            mTextViewDesc.setText(stepsItem.getDescription());
        }

        if (TextUtils.isEmpty(stepsItem.getVideoURL())) {
            mPlayerView.setVisibility(View.GONE);
        } else {
            mPlayerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void enableNavButtons(boolean enablePrev, boolean enableNext) {
        // On phone landscape mode, these views are not present in the layout
        if (mButtonPrev != null) {
            mButtonPrev.setEnabled(enablePrev);
            mButtonNext.setEnabled(enableNext);
        }
    }
}
