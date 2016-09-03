package com.example.xw.todaynews.Base.Impl.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xw.todaynews.Base.BaseMenuDetailPager;
import com.example.xw.todaynews.Domain.NewsData;
import com.example.xw.todaynews.Domain.NewsMenuData;
import com.example.xw.todaynews.Global.Constants;
import com.example.xw.todaynews.R;
import com.example.xw.todaynews.View.HorizontalScrollViewPager;
import com.example.xw.todaynews.View.RefreshListView;
import com.example.xw.todaynews.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

/**
 * 12个页签的页面对象
 * Created by xw on 2016/8/27.
 */
public class TabDetailPager extends BaseMenuDetailPager {
    //分类叶签
    private NewsMenuData.NewsTabData mTabData;
    // 网络返回的新闻列表数据
    private NewsData mNewsTabData;
    // 头条新闻的网络数据
    private ArrayList<NewsData.TopNews> mTopNewsList;
    // 头条新闻的数据适配器
    private TopNewsAdapter mTopNewsAdapter;
    // 新闻列表的集合
    private ArrayList<NewsData.News> mNewsList;
    // 加载新闻列表的url
    private String mUrl;

    private NewsAdapter mNewsAdapter;

    @ViewInject(R.id.vp_tab_detail)
    private HorizontalScrollViewPager mViewPager;

    @ViewInject(R.id.lv_tab_detail)
    private RefreshListView lvList;

    @ViewInject(R.id.indicator)
    private CirclePageIndicator mIndicator;

    @ViewInject(R.id.tv_title)
    private TextView tvTopNewsTitle;

    public TabDetailPager(Activity activity, NewsMenuData.NewsTabData tabData) {
        super(activity);
        mTabData = tabData;
        mUrl = Constants.SERVER_URL + mTabData.url;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        View header = View.inflate(mActivity, R.layout.list_header_topnews,
                null);
        ViewUtils.inject(this, header);// 必须也将头布局注入到ViewUtils

        // 给listview添加头布局
        lvList.addHeaderView(header);

        // 设置下拉刷新监听
        lvList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                // 从网络加载数据
                getDataFromServer();
            }
        });

        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processResult(cache);
        }

        getDataFromServer();
    }
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result);
                CacheUtils.setCache(mUrl, result, mActivity);
                // 收起下拉刷新控件
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 收起下拉刷新控件
                lvList.onRefreshComplete(false);

                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void processResult(String result) {
        Gson gson = new Gson();
        mNewsTabData = gson.fromJson(result, NewsData.class);

        // 初始化头条新闻
        mTopNewsList = mNewsTabData.data.topnews;
        if (mTopNewsList != null) {
            mTopNewsAdapter = new TopNewsAdapter();
            mViewPager.setAdapter(mTopNewsAdapter);
            mIndicator.setViewPager(mViewPager);// 将指示器和viewpager绑定
            mIndicator.setSnap(true);// 快照模式
            mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    System.out.println("position:" + position);
                    NewsData.TopNews topNews = mTopNewsList.get(position);
                    tvTopNewsTitle.setText(topNews.title);
                }

                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

            mIndicator.onPageSelected(0);// 将小圆点位置归零, 解决它会在页面销毁时仍记录上次位置的bug
            tvTopNewsTitle.setText(mTopNewsList.get(0).title);// 初始化第一页标题
        }

        // 初始化新闻列表
        mNewsList = mNewsTabData.data.news;
        if (mNewsList != null) {
            mNewsAdapter = new NewsAdapter();
            lvList.setAdapter(mNewsAdapter);
        }
    }
    class TopNewsAdapter extends PagerAdapter {

        BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            // 初始化xutils中的加载图片的工具
            mBitmapUtils = new BitmapUtils(mActivity);
            // 设置默认加载图片
            mBitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopNewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
            view.setScaleType(ImageView.ScaleType.FIT_XY);// 设置图片填充效果, 表示填充父窗体
            // 获取图片链接, 使用链接下载图片, 将图片设置给ImageView, 考虑内存溢出问题, 图片本地缓存
            mBitmapUtils.display(view, mTopNewsList.get(position).topimage);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
    class NewsAdapter extends BaseAdapter {

        public BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils
                    .configDefaultLoadingImage(R.drawable.pic_item_list_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsData.News getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news,
                        null);
                holder = new ViewHolder();
                holder.tvTitle = (TextView) convertView
                        .findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView
                        .findViewById(R.id.tv_date);
                holder.ivIcon = (ImageView) convertView
                        .findViewById(R.id.iv_icon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            NewsData.News news = getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);

            mBitmapUtils.display(holder.ivIcon, news.listimage);

            return convertView;
        }

    }
    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivIcon;
    }

}
