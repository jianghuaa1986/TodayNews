package com.example.xw.todaynews.Base.Impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.xw.todaynews.Base.BaseMenuDetailPager;
import com.example.xw.todaynews.Domain.NewsMenuData;

/**
 * 12个页签的页面对象
 * Created by xw on 2016/8/27.
 */
public class TabDetailPager extends BaseMenuDetailPager {

    private NewsMenuData.NewsTabData mTabData;
    private TextView mTextView;

    public TabDetailPager(Activity activity, NewsMenuData.NewsTabData tabData) {
        super(activity);
        mTabData = tabData;
    }

    @Override
    public View initView() {
        mTextView = new TextView(mActivity);
        mTextView.setTextColor(Color.RED);
        mTextView.setTextSize(22);
        mTextView.setGravity(Gravity.CENTER);
        return mTextView;
    }

    @Override
    public void initData() {
        mTextView.setText(mTabData.title);
    }

}
