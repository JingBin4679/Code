package com.easedroid.code.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easedroid.code.R;

/**
 * Created by bin.jing on 2017/12/20.
 */

public class BitmapReuseFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bitmap_reuse, container, false);
        return view;
    }
}
