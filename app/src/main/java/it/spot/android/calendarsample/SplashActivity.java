package it.spot.android.calendarsample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Created by a.rinaldi on 6/8/2015.
 */
public class SplashActivity extends Activity {

    private View mCursor;

    private AnimationSet mAnimationSet;
    private TranslateAnimation mTranslation1;
    private TranslateAnimation mTranslation2;
    private TranslateAnimation mTranslation3;
    private TranslateAnimation mTranslation4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_splash);

        this.mCursor = this.findViewById(R.id.cursor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mAnimationSet == null) {
            this.initAnimation();
        }
    }


    protected void initAnimation() {

        if (this.mAnimationSet == null) {
            this.mAnimationSet = new AnimationSet(true);
            this.mAnimationSet.setInterpolator(new LinearInterpolator());

            // first animation
            this.mTranslation1 = new TranslateAnimation(0f, 0f, 0f, -250f);
            this.mTranslation1.setFillEnabled(true);
            this.mTranslation1.setFillAfter(true);
            this.mTranslation1.setDuration(100);

            // second animation
            this.mTranslation2 = new TranslateAnimation(0f, 0f, -250f, 0f);
            this.mTranslation2.setFillEnabled(true);
            this.mTranslation2.setFillAfter(true);
            this.mTranslation2.setDuration(100);
            this.mTranslation2.setStartOffset(100);

            // third animation
            this.mTranslation3 = new TranslateAnimation(0f, 0f, 0f, 250f);
            this.mTranslation3.setFillEnabled(true);
            this.mTranslation3.setFillAfter(true);
            this.mTranslation3.setDuration(100);
            this.mTranslation3.setStartOffset(200);

            // fourth animation
            this.mTranslation4 = new TranslateAnimation(0f, 0f, 250f, 0f);
            this.mTranslation4.setFillEnabled(true);
            this.mTranslation4.setFillAfter(true);
            this.mTranslation4.setDuration(100);
            this.mTranslation4.setStartOffset(300);

            this.mAnimationSet.addAnimation(this.mTranslation1);
            this.mAnimationSet.addAnimation(this.mTranslation2);
            this.mAnimationSet.addAnimation(this.mTranslation3);
            this.mAnimationSet.addAnimation(this.mTranslation4);

            this.mAnimationSet.setRepeatCount(Animation.INFINITE);
            this.mAnimationSet.setRepeatMode(Animation.RESTART);

            this.mAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mCursor.startAnimation(mAnimationSet);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            this.mCursor.startAnimation(this.mAnimationSet);
        }
    }
}
