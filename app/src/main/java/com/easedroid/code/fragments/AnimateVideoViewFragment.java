package com.easedroid.code.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.VideoView;

import com.easedroid.code.R;

/**
 * Created by bin.jing on 2017/12/29.
 */

public class AnimateVideoViewFragment extends BaseFragment implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, View.OnClickListener {

    private VideoView videoView;
    private Button btnPlay;
    private String videoUrl = "http://47.93.236.202:5001/nn_vod/nn_x64/aWQ9YzFkMGM1NzVlMDQ0NTU4OTI1OGU4Mzk1NTY3ZmJhZDYmdXJsX2MxPTM1NjYzOTYyMzczNTYzMzE2MjYzNjIzNzM0NjQ2NjM0MzkzNDM1MzU2NDYzNjEzODMzMzE2MjYxMzQzOTM4NjIyZjMyMzAzMTM3MzEzMjMyMzEyZjM0MzIzMzY0NjIzNzYzMzc2NTMwMzgzMDM4NjE2MTY0NjYzMTM4MzgzOTM3MzYzMDM5Mzg2MTM3Mzc2NjMzMzUyZjViZTk5OGIzZTU4NTg5ZTc5NGI1ZTViZGIxNzc3Nzc3MmU3OTY3NjQ3OTM4MmU2ZTY1NzQ1ZDJlZTVhZjg2ZTY4ODk4MmU0ODQ0MmUzNzMyMzA3MDJlZTU5YmJkZThhZmFkZTRiOGFkZTVhZDk3MmU2ZDcwMzQwMCZubl9haz0wMTdjMDNiZmYwOTFiMGY2MmU3MjQ4ODI4NDBkYmI2M2RjJm50dGw9MyZucGlwcz00Ny45My4yMzYuMjAyOjUxMDAmbmNtc2lkPTEwMDAwMSZuZ3M9NWE0NWZmYzIwMDAxMzY4YjAwZDU0MTcxYzBmMjEyNTcmbmZ0PW1wNA,,/c1d0c575e0445589258e8395567fbad6.ts";
    private View vCover;
    private RadioGroup rgAnimateType;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animate_video, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        btnPlay = view.findViewById(R.id.btn_play);
        btnPlay.setOnClickListener(this);
        btnPlay.requestFocus();

        vCover = view.findViewById(R.id.v_cover);
        rgAnimateType = view.findViewById(R.id.rg_animate_type);

        videoView = view.findViewById(R.id.vv_player);
        videoView.setOnCompletionListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnErrorListener(this);
        videoView.setFocusable(false);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        onClick(btnPlay);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        RadioButton radioButton = getView().findViewById(rgAnimateType.getCheckedRadioButtonId());
        final String tag = (String) radioButton.getTag();

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if ("positionX".equals(tag)) {
                    int width = vCover.getMeasuredWidth();
                    int scroll = (int) (value * 1.0f / 100 * width);
                    vCover.setX(scroll);
                } else if ("alpha".equals(tag)) {
                    float alpha = 1 - value * 1.0f / 100;
                    vCover.setAlpha(alpha);
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                vCover.setVisibility(View.GONE);
            }
        });
        animator.setStartDelay(1000);
        animator.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onClick(View v) {
        vCover.setX(0);
        vCover.setAlpha(1);
        vCover.setVisibility(View.VISIBLE);
        videoView.setVideoPath(videoUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (videoView != null) {
            videoView.stopPlayback();
            videoView.setOnCompletionListener(null);
            videoView.setOnPreparedListener(null);
            videoView.setOnErrorListener(null);
            videoView = null;
        }
    }
}
