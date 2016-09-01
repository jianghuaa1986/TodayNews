package com.example.xw.todaynews.Base.Impl.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xw.todaynews.Activity.MainActivity;
import com.example.xw.todaynews.Base.BaseMenuDetailPager;
import com.example.xw.todaynews.Domain.NewsMenuData;
import com.example.xw.todaynews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * 菜单详情页——新闻
 * Created by xw on 2016/8/27.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager implements
        ViewPager.OnPageChangeListener {

    @ViewInject(R.id.vp_news_detail)
    private ViewPager mViewPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    private ArrayList<NewsMenuData.NewsTabData> mTabList;// 页签网络数据集合
    private ArrayList<TabDetailPager> mTabPagers;// 页签页面集合

    public NewsMenuDetailPager(Activity activity,
                               ArrayList<NewsMenuData.NewsTabData> children) {
        super(activity);
        mTabList = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_menu_detail_news,
                null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        // 初始化12个页签
        mTabPagers = new ArrayList<TabDetailPager>();
        for (NewsMenuData.NewsTabData tabData : mTabList) {
            // 创建一个页签对象
            TabDetailPager pager = new TabDetailPager(mActivity, tabData);
            mTabPagers.add(pager);
        }

        mViewPager.setAdapter(new NewsMenuAdapter());
        // mViewPager.setOnPageChangeListener(this);

        // 此方法在viewpager设置完数据之后再调用
        mIndicator.setViewPager(mViewPager);// 将页面指示器和ViewPager关联起来
        mIndicator.setOnPageChangeListener(this);// 当viewpager和指针绑定时,需要将页面切换监听设置给指针
    }

    class NewsMenuAdapter extends PagerAdapter {
        // 返回页面指示器的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return mTabList.get(position).title;
        }

        @Override
        public int getCount() {
            return mTabPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabPagers.get(position);
            container.addView(pager.mRootView);
            pager.initData();// 初始化数据
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
       // System.out.println("position:" + position);
        if (position == 0) {// 在第一个页签,允许侧边栏出现
            // 开启侧边栏
            setSlidingMenuEnable(true);
        } else {// 其他页签,禁用侧边栏, 保证viewpager可以正常向右滑动
            // 关闭侧边栏
            setSlidingMenuEnable(false);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * 设置侧边栏可用不可用
     *
     * @param enable
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

    @OnClick(R.id.iv_next_page)
    public void nextPage(View view) {
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }

}
