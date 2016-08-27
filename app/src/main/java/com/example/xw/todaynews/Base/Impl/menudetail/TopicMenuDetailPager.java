package com.example.xw.todaynews.Base.Impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.widget.ViewDragHelper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.xw.todaynews.Base.BaseMenuDetailPager;

/**
 * 菜单详情页——专题
 * Created by xw on 2016/8/27.
 */
public class TopicMenuDetailPager extends BaseMenuDetailPager {
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("菜单详情页-专题");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
}
