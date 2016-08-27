package com.example.xw.todaynews.Base.Impl;


import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.xw.todaynews.Activity.MainActivity;
import com.example.xw.todaynews.Base.BaseFragment;
import com.example.xw.todaynews.Base.BasePager;
import com.example.xw.todaynews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by xw on 2016/8/26.
 */
public class ContentFragment extends BaseFragment  {

   @ViewInject(R.id.vp_content)
    private ViewPager mViewPager;
    @ViewInject(R.id.rg_group)
    private RadioGroup rgGroup;

    private ArrayList<BasePager> mPagers;
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ViewUtils.inject(this,view);
        return view;
    }
    @Override
    public void initData() {
        // 初始化5个标签页面
        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));

        mViewPager.setAdapter(new ContentAdapter());

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.rb_home:
                        // 首页
                        // mViewPager.setCurrentItem(0);
                        mViewPager.setCurrentItem(0, false);// 禁用页面切换的动画效果
                        mPagers.get(0).initData();// 切到当前页面,再初始化数据
                        setSlidingMenuEnable(false);// 禁用侧边栏
                        break;
                    case R.id.rb_news:
                        // 新闻中心
                        mViewPager.setCurrentItem(1, false);
                        mPagers.get(1).initData();// 切到当前页面,再初始化数据
                        setSlidingMenuEnable(true);// 开启侧边栏
                        break;
                    case R.id.rb_smart:
                        // 智慧服务
                        mViewPager.setCurrentItem(2, false);
                        mPagers.get(2).initData();// 切到当前页面,再初始化数据
                        setSlidingMenuEnable(true);// 开启侧边栏
                        break;
                    case R.id.rb_gov:
                        // 政务
                        mViewPager.setCurrentItem(3, false);
                        mPagers.get(3).initData();// 切到当前页面,再初始化数据
                        setSlidingMenuEnable(true);// 开启侧边栏
                        break;
                    case R.id.rb_setting:
                        // 设置
                        mViewPager.setCurrentItem(4, false);
                        mPagers.get(4).initData();// 切到当前页面,再初始化数据
                        setSlidingMenuEnable(false);// 禁用侧边栏
                        break;

                    default:
                        break;
                }
            }
        });
        mPagers.get(0).initData();// 初始化首页数据
        setSlidingMenuEnable(false);// 首页禁用侧边栏
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            container.addView(pager.mRootView);// 将页面布局添加到容器中
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    /**
     * 设置侧边栏可用不可用
     *
     */
    private void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();

        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // 禁用掉侧边栏滑动效果
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) mPagers.get(1);
    }

}
