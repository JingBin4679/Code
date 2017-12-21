package com.easedroid.code;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easedroid.code.fragments.BaseFragment;

/**
 * Created by bin.jing on 2017/12/20.
 */

public class CommonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        Intent intent = getIntent();
        String clazz = intent.getStringExtra("clazz");
        bindTitle();

        BaseFragment fragment = createFragment(clazz);
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return;
        }
        // TODO: 2017/12/20 处理加载Fragment失败的情况

    }

    private void bindTitle() {
        String name = getIntent().getStringExtra("name");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null && !TextUtils.isEmpty(name)) {
            actionBar.setTitle(name);
        }
    }

    private BaseFragment createFragment(String clazz) {
        if (TextUtils.isEmpty(clazz)) {
            return null;
        }
        try {
            Class<?> aClass = Class.forName(clazz);
            Object instance = aClass.newInstance();
            if (instance instanceof BaseFragment) {
                BaseFragment fragment = (BaseFragment) instance;
                return fragment;
            }
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
