/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 * Website : http://www.farsunset.com                           *
 * *
 * **************************************************************************************
 */
package com.farsunset.lvxin.component;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

public class ListHeaderView extends RelativeLayout {
    private ImageView leaf1;
    private ImageView leaf2;
    private ImageView leaf3;
    private ImageView leaf4;
    private ImageView leaf5;
    private ImageView leaf6;
    private ImageView leaf7;
    private ImageView leaf8;
    private ImageView brid;
    private ObjectAnimator bridAnimator;
    private int width;
    private int height;
    private int leafSize;

    public ListHeaderView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        width = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = AppTools.dip2px(150);
        leafSize = AppTools.dip2px(18);

        int y = height / 15;
        brid = (ImageView) findViewById(R.id.brid);
        bridAnimator = ObjectAnimator.ofFloat(brid, "translationY", -y, y, -y);
        bridAnimator.setDuration(1200);
        bridAnimator.setRepeatCount(ValueAnimator.INFINITE);
        bridAnimator.setRepeatMode(ValueAnimator.RESTART);
        bridAnimator.start();

        leaf1 = new ImageView(getContext());
        leaf1.setImageResource(R.drawable.icon_leaf_1);

        leaf2 = new ImageView(getContext());
        leaf2.setImageResource(R.drawable.icon_leaf_2);

        leaf3 = new ImageView(getContext());
        leaf3.setImageResource(R.drawable.icon_leaf_3);
        leaf4 = new ImageView(getContext());
        leaf4.setImageResource(R.drawable.icon_leaf_4);

        leaf5 = new ImageView(getContext());
        leaf5.setImageResource(R.drawable.icon_leaf_5);

        leaf6 = new ImageView(getContext());
        leaf6.setImageResource(R.drawable.icon_leaf_6);

        leaf7 = new ImageView(getContext());
        leaf7.setImageResource(R.drawable.icon_leaf_7);

        leaf8 = new ImageView(getContext());
        leaf8.setImageResource(R.drawable.icon_leaf_8);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.setMargins(0, -leafSize, 0, 0);
        addView(leaf1, 0, lp1);

        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp2.setMargins(width / 10, -leafSize, 0, 0);
        addView(leaf2, 0, lp2);

        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp3.setMargins(width / 6, -leafSize, 0, 0);
        addView(leaf3, 0, lp3);

        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp4.setMargins(width / 5, -leafSize, 0, 0);
        addView(leaf4, 0, lp4);

        RelativeLayout.LayoutParams lp5 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp5.setMargins(width / 4, -leafSize, 0, 0);
        addView(leaf5, 0, lp5);

        RelativeLayout.LayoutParams lp6 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp6.setMargins((int) (width * 0.9f), -leafSize, 0, 0);
        addView(leaf6, 0, lp6);

        RelativeLayout.LayoutParams lp7 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp7.setMargins(width, 0, -leafSize, 0);
        addView(leaf7, 0, lp7);

        RelativeLayout.LayoutParams lp8 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp8.setMargins(width / 2, -leafSize, 0, 0);
        addView(leaf8, 0, lp8);


        leaf1.startAnimation(buildLeaf1Animation());

        leaf2.startAnimation(buildLeaf2Animation());


        leaf3.startAnimation(buildLeaf3Animation());


        leaf4.startAnimation(buildLeaf4Animation());


        leaf5.startAnimation(buildLeaf5Animation());


        leaf6.startAnimation(buildLeaf6Animation());


        leaf7.startAnimation(buildLeaf7Animation());

        leaf8.startAnimation(buildLeaf8Animation());
    }

    private AnimationSet buildLeaf1Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setFillAfter(false);
        alphaAnimation.setDuration(16000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setFillAfter(false);
        rotateAnimation.setDuration(25000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, width / 3.5F, 0, height);
        translateAnimation.setFillAfter(false);
        translateAnimation.setDuration(16000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    private AnimationSet buildLeaf2Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(33000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(33000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, width / 2.3F, 0, height);
        translateAnimation.setDuration(33000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    private AnimationSet buildLeaf3Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(30000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(30000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, width / 1.8F, 0, height);
        translateAnimation.setDuration(30000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }

    private AnimationSet buildLeaf4Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(27000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(27000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, -width / 6, 0, height);
        translateAnimation.setDuration(27000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    private AnimationSet buildLeaf5Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(18000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(18000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, width * 0.75f, 0, height);
        translateAnimation.setDuration(18000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }

    private AnimationSet buildLeaf6Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(25000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(25000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, -width * 0.64f, 0, height);
        translateAnimation.setDuration(25000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    private AnimationSet buildLeaf7Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(20000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(20000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, -width * 0.5f, 0, height);
        translateAnimation.setDuration(20000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }

    private AnimationSet buildLeaf8Animation() {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(24000);
        alphaAnimation.setRepeatCount(Animation.INFINITE);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(24000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        TranslateAnimation translateAnimation = new TranslateAnimation(0, width * 0.5f, 0, height);
        translateAnimation.setDuration(24000);
        translateAnimation.setRepeatCount(Animation.INFINITE);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }


    public void setBridAlpha(float alpha) {
        brid.setAlpha(alpha);
    }

    public void startBridAnimation() {
        brid.setAlpha(1f);
    }

    public void cancelBridAnimation() {
        brid.setAlpha(0f);
    }

    public int getTopHeaderHeight() {
        return AppTools.dip2px(50);
    }
}
