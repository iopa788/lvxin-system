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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.Random;

public class ColorBallView extends View implements Animation.AnimationListener {
    private Paint mPaint;
    private int oX;
    private int oY;
    private int mRadius;
    private int mX;
    private int mY;
    private int duration = 2000;
    private TranslateAnimation animation;
    private int moveRange = 0;
    private boolean canMove;

    private int mStartColor;
    private int mEndColor;

    public ColorBallView(Context context, int mX, int mY, int mRadius, int color, boolean canMove) {
        super(context, null);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        this.mRadius = mRadius;
        this.oX = mX;
        this.oY = mY;
        mPaint.setColor(color);
        this.canMove = canMove;
        moveRange = (int) (Resources.getSystem().getDisplayMetrics().density * 5);
    }

    public void setColor(@ColorInt int startColor, @ColorInt int endColor) {
        mStartColor = startColor;
        mEndColor = endColor;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed && canMove) {
            this.mX = left;
            this.mY = top;
            startAnim();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animation != null) {
            animation.cancel();
        }
    }

    public void setMargin(int left, int top) {
        ((FrameLayout.LayoutParams) getLayoutParams()).setMargins(left, top, 0, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStartColor != 0 && mEndColor != 0) {
            mPaint.setShader(new LinearGradient(oX, oY, mRadius, mRadius, mStartColor, mEndColor, Shader.TileMode.MIRROR));
        }

        canvas.drawCircle(oX, oY, mRadius, mPaint);
    }

    public void startAnim() {

        int random = new Random().nextInt(moveRange) * -1 + moveRange;
        int tX = (int) (getX() + random);
        random = new Random().nextInt(moveRange) * -1 + moveRange;
        int tY = (int) (getY() + random);
        animation = new TranslateAnimation(mX, tX, mY, tY);
        animation.setDuration(duration);
        animation.setRepeatCount(0);
        animation.setAnimationListener(this);
        animation.setFillAfter(true);
        this.startAnimation(animation);
        mY = tY;
        mX = tX;
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        startAnim();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void stopAnimation() {
        animation.cancel();
        this.clearAnimation();
    }
}
