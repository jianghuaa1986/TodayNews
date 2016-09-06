package com.example.xw.todaynews.Base.Impl.menudetail;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xw.todaynews.Activity.NewsDetailActivity;
import com.example.xw.todaynews.Base.BaseMenuDetailPager;
import com.example.xw.todaynews.Domain.NewsData;
import com.example.xw.todaynews.Domain.NewsMenuData;
import com.example.xw.todaynews.Global.Constants;
import com.example.xw.todaynews.R;
import com.example.xw.todaynews.View.HorizontalScrollViewPager;
import com.example.xw.todaynews.View.RefreshListView;
import com.example.xw.todaynews.utils.CacheUtils;
import com.example.xw.todaynews.utils.PreUtils;
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

    private String mMoreUrl;// 下一页的链接

    private Handler mHandler = null;

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

            @Override
            public void loadMore() {
                // 加载更多数据
                if (mMoreUrl != null) {
                    System.out.println("加载下一页数据...");
                    getMoreDataFromServer();
                } else {
                    lvList.onRefreshComplete(true);// 收起加载更多布局
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                NewsData.News news = mNewsList.get(position);

                // 当前点击的item的标题颜色置灰
                TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
                tvTitle.setTextColor(Color.GRAY);

                // 将已读状态持久化到本地
                // key: read_ids; value: 1324,1325,1326
                String readIds = PreUtils.getString("read_ids", "", mActivity);
                if (!readIds.contains(news.id)) {// 以前没有添加过,才添加进来
                    readIds = readIds + news.id + ",";// 1324,1325,
                    PreUtils.putString("read_ids", readIds, mActivity);
                }

               // 跳到详情页
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", news.url);
                mActivity.startActivity(intent);
            }
        });

        return view;
    }
    /**
     * 加载更多数据
     */
    protected void getMoreDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result,true);
                // 收起加载更多布局
                lvList.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                // 收起加载更多布局
                lvList.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)) {
            processResult(cache,false);
        }

        getDataFromServer();
    }
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                processResult(result,false);
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
    protected void processResult(String result, boolean isMore) {
        Gson gson = new Gson();
        mNewsTabData = gson.fromJson(result, NewsData.class);

        if (!TextUtils.isEmpty(mNewsTabData.data.more)) {
            // 初始化下一页链接地址
            mMoreUrl = Constants.SERVER_URL + mNewsTabData.data.more;
        } else {
            // 没有下一页了
            mMoreUrl = null;
        }

        if (!isMore) {
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
                        //System.out.println("position:" + position);
                        NewsData.TopNews topNews = mTopNewsList.get(position);
                        tvTopNewsTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrolled(int position,
                                               float positionOffset, int positionOffsetPixels) {
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

            if (mHandler == null) {
                mHandler = new Handler() {
                    public void handleMessage(android.os.Message msg) {
                        int currentItem = mViewPager.getCurrentItem();

                        if (currentItem < mTopNewsList.size() - 1) {
                            currentItem++;
                        } else {
                            currentItem = 0;
                        }

                        mViewPager.setCurrentItem(currentItem);

                        mHandler.sendEmptyMessageDelayed(0, 2000);
                    };
                };

                // 延时2秒切换广告条
                mHandler.sendEmptyMessageDelayed(0, 2000);

                mViewPager.setOnTouchListener(new View.OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                System.out.println("ACTION_DOWN");
                                // 删除所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_CANCEL:// 事件取消(当按下后,然后移动下拉刷新,导致抬起后无法响应ACTION_UP,
                                // 但此时会响应ACTION_CANCEL,也需要继续播放轮播条)
                            case MotionEvent.ACTION_UP:
                                // 延时2秒切换广告条
                                mHandler.sendEmptyMessageDelayed(0, 2000);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        } else {
            // 加载更多
            ArrayList<NewsData.News> moreData = mNewsTabData.data.news;
            mNewsList.addAll(moreData);// 追加数据
            mNewsAdapter.notifyDataSetChanged();// 刷新listview
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

            // 标记已读和未读
            String readIds = PreUtils.getString("read_ids", "", mActivity);
            if (readIds.contains(news.id)) {
                // 已读
                holder.tvTitle.setTextColor(Color.GRAY);
            } else {
                // 未读
                holder.tvTitle.setTextColor(Color.BLACK);
            }

            return convertView;
        }

    }
    static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDate;
        public ImageView ivIcon;
    }

}
