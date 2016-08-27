package com.example.xw.todaynews.Base.Impl;

import android.app.Activity;
import android.graphics.Color;
import android.provider.SyncStateContract;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xw.todaynews.Activity.MainActivity;
import com.example.xw.todaynews.Base.BaseMenuDetailPager;
import com.example.xw.todaynews.Base.BasePager;
import com.example.xw.todaynews.Base.Impl.menudetail.InteractMenuDetailPager;
import com.example.xw.todaynews.Base.Impl.menudetail.NewsMenuDetailPager;
import com.example.xw.todaynews.Base.Impl.menudetail.PhotosMenuDetailPager;
import com.example.xw.todaynews.Base.Impl.menudetail.TopicMenuDetailPager;
import com.example.xw.todaynews.Domain.NewsMenuData;
import com.example.xw.todaynews.Global.Constants;
import com.example.xw.todaynews.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * Created by xw on 2016/8/26.
 */
public class NewsCenterPager extends BasePager {
   private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// 菜单详情页集合
    private NewsMenuData mNewsMenuData;// 新闻分类信息网络数据

    public NewsCenterPager(Activity activity) {
        super(activity);
    }


    @Override
    public void initData() {
        tvTitle.setText("新闻");
        // 1.首先先看本地有没有缓存
        // 2.有缓存,直接加载缓存
        String cache = CacheUtils.getCache(Constants.CATEGORIES_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            // 有缓存
            processResult(cache);
        }
        // 即使发现有缓存,仍继续调用网络, 获取最新数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, Constants.CATEGORIES_URL,
                new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        // 请求成功
                        String result = responseInfo.result;// 获取json字符串
                        CacheUtils.setCache(Constants.CATEGORIES_URL, result,
                                mActivity);
                       processResult(result);



                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        // 请求失败
                        error.printStackTrace();
                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }
    protected void processResult(String result) {
        // gson->json
        Gson gson = new Gson();
        mNewsMenuData = gson.fromJson(result, NewsMenuData.class);
        // 获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        // 将网络数据设置给侧边栏
        leftMenuFragment.setData(mNewsMenuData.data);
        // 初始化4个菜单详情页
        mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity));
        mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        // 菜单详情页-新闻作为初始页面
        setCurrentMenuDetailPager(0);
    }

    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = mMenuDetailPagers.get(position);
        // 移除之前所有的view对象, 清理屏幕
        flContent.removeAllViews();
        flContent.addView(pager.mRootView);
        pager.initData();// 初始化数据

        // 更改标题
        tvTitle.setText(mNewsMenuData.data.get(position).title);
    }
}
