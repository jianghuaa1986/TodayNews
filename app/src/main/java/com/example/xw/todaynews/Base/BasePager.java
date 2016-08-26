package com.example.xw.todaynews.Base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xw.todaynews.R;

/**
 * Created by xw on 2016/8/26.
 */
public abstract class BasePager  {
    public Activity mActivity;
    public View mRootView;
    public TextView tvTitle;
    public ImageButton btnMenu;
    public FrameLayout flContent;
    public BasePager(Activity activity) {
        mActivity = activity;
        initView();
    }

    /**
     * 初始化布局
     */
    public void initView() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        btnMenu = (ImageButton) mRootView.findViewById(R.id.btn_menu);
        flContent = (FrameLayout) mRootView.findViewById(R.id.fl_content);
    }

    /**
     * 初始化数据
     */
    public abstract void initData();
}
