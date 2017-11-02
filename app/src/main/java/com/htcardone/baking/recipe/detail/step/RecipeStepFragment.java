package com.htcardone.baking.recipe.detail.step;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.GetChars;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.htcardone.baking.R;
import com.htcardone.baking.data.model.StepsItem;
import com.htcardone.baking.util.ActivityUtils;
import com.htcardone.baking.util.Log;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RecipeStepFragment extends Fragment implements RecipeStepContract.View {
    private final String LOG_TAG = RecipeStepFragment.class.getSimpleName();

    private RecipeStepContract.Presenter mPresenter;

    private SimpleExoPlayer mPlayer;

    @BindView(R.id.playerView_recipeStep)
    SimpleExoPlayerView mPlayerView;

    @BindView(R.id.textView_recipeStep_noVideo)
    TextView mTextViewNoVideo;

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
        Log.d(LOG_TAG, "onResume()");
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
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
            mPlayerView.setVisibility(View.INVISIBLE);
            mTextViewNoVideo.setVisibility(View.VISIBLE);
        } else {
            mTextViewNoVideo.setVisibility(View.INVISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);

            if (!TextUtils.isEmpty(stepsItem.getThumbnailURL())) {
                try {
                    Bitmap thumbnail = Picasso.with(getContext())
                            .load(stepsItem.getThumbnailURL())
                            .get();

                    Log.d(LOG_TAG, "showStep() getThumbnailURL()" + stepsItem.getThumbnailURL() + " bmp=" + thumbnail);

                    mPlayerView.setDefaultArtwork(thumbnail);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            initializePlayer(Uri.parse(stepsItem.getVideoURL()));
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

    @Override
    public void stopVideo() {
        releasePlayer();
    }

    /**
    * Initialize ExoPlayer.
    * @param mediaUri The URI of the sample to play.
    */
    private void initializePlayer(Uri mediaUri) {
        if (mPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mPlayer);

            // Prepare the MediaSource.
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name))),
                    new DefaultExtractorsFactory(), null, null);
            mPlayer.prepare(mediaSource);
            mPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
