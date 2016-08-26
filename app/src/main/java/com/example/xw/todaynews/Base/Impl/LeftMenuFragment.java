package com.example.xw.todaynews.Base.Impl;

import android.view.View;

import com.example.xw.todaynews.Base.BaseFragment;
import com.example.xw.todaynews.R;

/**
 * Created by xw on 2016/8/26.
 */
public class LeftMenuFragment extends BaseFragment {
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        return view;
    }
}
