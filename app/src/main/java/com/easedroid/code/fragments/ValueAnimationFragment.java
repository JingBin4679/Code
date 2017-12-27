package com.easedroid.code.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.easedroid.code.R;

/**
 * Created by bin.jing on 2017/12/27.
 */

public class ValueAnimationFragment extends BaseFragment implements View.OnClickListener {

    private View ivIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_value_animation, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        View rootView = getView();
        View view = rootView.findViewById(R.id.btn_value_anim_alpha);
        view.setOnClickListener(this);

        view = rootView.findViewById(R.id.btn_value_anim_rotate);
        view.setOnClickListener(this);

        view = rootView.findViewById(R.id.btn_value_anim_set);
        view.setOnClickListener(this);

        ivIcon = rootView.findViewById(R.id.iv_icon);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_value_anim_alpha) {
            buildAlphaAnim();
        }

        if (v.getId() == R.id.btn_value_anim_rotate) {
            buildRotateAnim();
        }

        if (v.getId() == R.id.btn_value_anim_set) {
            buildAnimSet();
        }
    }

    private void buildAnimSet() {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(ivIcon, "Rotation", 0f, 360f);
        ObjectAnimator scale = ObjectAnimator.ofFloat(ivIcon, "scaleX", 1f, 2f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(ivIcon, "Alpha", 0f, 1f);
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(2000);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        //三个动画同时执行
        animSet.playTogether(rotation, alpha, scale);
        animSet.start();
    }

    private void buildRotateAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ivIcon.setRotation(value * 360F / 100);
            }
        });
        animator.start();
    }

    private void buildAlphaAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                ivIcon.setAlpha(value * 1F / 100);
            }
        });
        animator.start();
    }
}
