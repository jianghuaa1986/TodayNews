package com.example.xw.todaynews.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.xw.todaynews.R;
import com.example.xw.todaynews.utils.PreUtils;

import java.util.ArrayList;

/**
 * Created by xw on 2016/8/26.
 */
public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager mViewPager;
    private ImageView ivRedPoint;// 小红点
    private int mPointWidth;// 两个圆点的宽度
    private LinearLayout llContainer;
    private Button startButton;

    private int[] mImageIds = new int[] { R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3 };
    private ArrayList<ImageView> mImageViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mImageViewList=new ArrayList<ImageView>();
        ivRedPoint= (ImageView) findViewById(R.id.red_point);
        startButton= (Button) findViewById(R.id.btn_start);
        startButton.setOnClickListener(this);

        llContainer= (LinearLayout) findViewById(R.id.ll_container);
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            mImageViewList.add(view);
            //初始化圆点
            ImageView point=new ImageView(this);
            point.setImageResource(R.drawable.shape_circle_default);
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if(i>0){
                lp.leftMargin=8;
            }
            point.setLayoutParams(lp);
            llContainer.addView(point);
        }
        mViewPager= (ViewPager) findViewById(R.id.vp);
        mViewPager.setAdapter(new MyAdapter());

        // 页面绘制结束之后, 计算两个圆点的间距
        // 视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    // layout方法执行结束(位置确定)
                    @Override
                    public void onGlobalLayout() {
                        // 移除监听
                        ivRedPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);

                        // 获取两个圆点的间距
                        mPointWidth = llContainer.getChildAt(1).getLeft()
                                - llContainer.getChildAt(0).getLeft();
                        System.out.println("width:" + mPointWidth);
                    }
                });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // 计算当前小红点的左边距
                int leftMargin = (int) (mPointWidth * positionOffset + position
                        * mPointWidth);

                // 修改小红点的左边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;
                ivRedPoint.setLayoutParams(params);

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length - 1) {// 最后页面显示开始体验
                    startButton.setVisibility(View.VISIBLE);
                } else {
                    startButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                // 开始体验
                // 记录新手引导已经被展示的状态,下次启动不再展示
                PreUtils.putBoolean("is_guide_show", true, this);
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

            default:
                break;
        }

    }

    public class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);

            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }
    }
}
