package com.example.xw.todaynews.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.example.xw.todaynews.Base.Impl.ContentFragment;
import com.example.xw.todaynews.Base.Impl.LeftMenuFragment;
import com.example.xw.todaynews.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * Created by xw on 2016/8/26.
 */
public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
    private static final String TAG_CONTENT = "TAG_CONTENT";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 添加侧边栏
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        // 全屏触摸
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();

        // 屏幕预留200像素(预留一定比例,进行屏幕适配)
        slidingMenu.setBehindOffset((int) (width * 0.625));


        initFragment();
    }

    private void initFragment() {
        // Fragment管理器
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();// 开始事务
        // 将帧布局替换为对应的Fragment
        transaction
                .replace(R.id.fl_content, new ContentFragment(), TAG_CONTENT);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
                TAG_LEFT_MENU);
        transaction.commit();// 提交事务
        // fm.findFragmentByTag(TAG_CONTENT);
    }
    /**
     * 获取侧边栏对象
     *
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm
                .findFragmentByTag(TAG_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取主页对象
     *
     */
    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm
                .findFragmentByTag(TAG_CONTENT);
        return fragment;
    }
}
