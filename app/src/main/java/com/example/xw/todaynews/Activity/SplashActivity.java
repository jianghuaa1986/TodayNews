package com.example.xw.todaynews.Activity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.xw.todaynews.R;
import com.example.xw.todaynews.utils.PreUtils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        RelativeLayout sp_root= (RelativeLayout) findViewById(R.id.sp_root);
        //添加动画
        RotateAnimation rotateAnimation=new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation=new ScaleAnimation(0,1,0,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(rotateAnimation);
        animSet.addAnimation(scaleAnimation);
        animSet.addAnimation(alphaAnimation);

        sp_root.startAnimation(animSet);

        animSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isGuideShow = PreUtils.getBoolean("is_guide_show",
                        false, getApplicationContext());

                if (isGuideShow) {
                    // 动画结束后跳主页面
                    startActivity(new Intent(getApplicationContext(),
                            MainActivity.class));
                } else {
                    // 跳到新手引导
                    startActivity(new Intent(getApplicationContext(),
                            GuideActivity.class));
                }

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
