package com.example.xw.todaynews.Base.Impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.xw.todaynews.Base.BasePager;

/**
 * Created by xw on 2016/8/26.
 */
public class SmartServicePager extends BasePager {
    public SmartServicePager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        tvTitle.setText("智慧服务");

        TextView view = new TextView(mActivity);
        view.setText("智慧服务");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);

        flContent.addView(view);
    }
}