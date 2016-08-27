package com.example.xw.todaynews.Base.Impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.xw.todaynews.Base.BaseMenuDetailPager;

/**
 * 12个页签的页面对象
 * Created by xw on 2016/8/27.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    public TabDetailPager(Activity activity) {
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
