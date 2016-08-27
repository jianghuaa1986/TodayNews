package com.example.xw.todaynews.Base;

import android.app.Activity;
import android.view.View;

/**
 * Created by xw on 2016/8/27.
 */
public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    // 菜单详情页根布局
    public View mRootView;

    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    public abstract View initView();

    public void initData() {

    }
}
