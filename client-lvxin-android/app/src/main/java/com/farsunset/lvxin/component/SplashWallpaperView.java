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
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

import java.util.Random;

public class SplashWallpaperView extends RelativeLayout {
    private Paint mColorPaint;

    Ball ball1;
    Ball ball2;
    Ball ball3;
    Ball ball4;
    Ball ball5;
    Ball ball6;
    Ball ball7;
    DisplayMetrics displayMetrics;
    private int moveRange;
    private WallpaperDrawable wallpaper;

    public SplashWallpaperView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setStyle(Paint.Style.FILL);
        mColorPaint.setColor(0x7FFFFFFF);
        displayMetrics = Resources.getSystem().getDisplayMetrics();
        int w = displayMetrics.widthPixels;
        moveRange = (int) (displayMetrics.density * 20 + 0.5f);
        ball1 = new Ball((int) (-w/4), -w/2, (int) (w/1.2));
        ball2 = new Ball(w - w/ 8 , -w/8, (int) (w/4));
        ball3 = new Ball(0, 0, (int) (w/2));
        ball4 = new Ball((int) w/5, -w/20, (int) (w/1.8));
        ball5 = new Ball(w/2, w/6, (int) (w/2.5));

        ball6 = new Ball((int) (w*0.8), -w/6, (int) (w/1.6));
        ball7 = new Ball((int) (w*0.88), -w/5, (int) (w/1.5));

        setBackground(wallpaper = new WallpaperDrawable());

    }
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            mHandler.removeMessages(0);
            ball1.relayout();
            ball2.relayout();
            ball3.relayout();
            ball4.relayout();
            ball5.relayout();
            ball6.relayout();
            ball7.relayout();
            wallpaper.invalidateSelf();
        }
    };
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }



    public class WallpaperDrawable extends Drawable{

        @Override
        public void draw(@NonNull Canvas canvas) {
            mColorPaint.setColor(0xff4a97d2);
            canvas.drawRect(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels,mColorPaint);
            mColorPaint.setColor(0x7fFFFFFF);
            canvas.drawCircle(ball7.x, ball7.y, ball7.radius, mColorPaint);
            canvas.drawCircle(ball6.x, ball6.y, ball6.radius, mColorPaint);
            canvas.drawCircle(ball5.x, ball5.y, ball5.radius, mColorPaint);
            mColorPaint.setColor(0x8cFFFFFF);
            canvas.drawCircle(ball3.x, ball3.y, ball3.radius, mColorPaint);
            mColorPaint.setColor(0xA4FFFFFF);
            canvas.drawCircle(ball4.x, ball4.y, ball4.radius, mColorPaint);
            canvas.drawCircle(ball1.x, ball1.y, ball1.radius, mColorPaint);
            canvas.drawCircle(ball2.x, ball2.y, ball2.radius, mColorPaint);
            mHandler.sendEmptyMessageDelayed(0,100);
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }
    }



    private class Ball {
        public int x;
        public int y;
        public int radius;
        int oY;
        private Random random;
        boolean isFristLayout = true;
        boolean upward = true;
        public Ball(int x, int y, int radius){
            random = new Random();
            this.radius = radius;
            this.x = x;
            this.y = y;
            this.oY = y;
        }

        public void  relayout(){
            if (isFristLayout)
            {
                upward = random.nextInt() % 2 == 0 ;
                isFristLayout = false;
            }

            if (upward && y == oY - moveRange)
            {
                upward = false;
                y++;
                return;
            }

            if (upward && y > oY - moveRange)
            {
                y--;
                return;
            }

            if (!upward && y == oY + moveRange)
            {
                upward = true;
                y--;
                return;
            }

            if (!upward && y < oY+ moveRange)
            {
                y++;
                return;
            }

        }
    }
}
