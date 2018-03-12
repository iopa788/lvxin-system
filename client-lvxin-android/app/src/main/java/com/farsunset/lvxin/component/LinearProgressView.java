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
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.farsunset.lvxin.listener.OnVideoRecordListener;
import com.farsunset.lvxin.pro.R;

public class LinearProgressView extends View {
    /**
     * 进度条
     */
    private Paint mProgressPaint;
    /**
     * 三秒
     */
    private Paint mThreePaint;
    private boolean mRecording = false;
    /**
     * 最长时长
     */
    private int mMaxDuration;
    private int mMinDuration;
    private int duration;
    private int width;
    private OnVideoRecordListener onVideoRecordListener;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (mRecording) {
                if (duration == mMaxDuration) {
                    onVideoRecordListener.onRecordCompleted();
                    stop();
                    return;
                }
                duration += 20;
                invalidate();
                mHandler.sendEmptyMessageDelayed(0, 20);

            }
        }
    };

    public LinearProgressView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void setOnVideoRecordListener(OnVideoRecordListener onVideoRecordListener) {
        this.onVideoRecordListener = onVideoRecordListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int left = width * mMinDuration / mMaxDuration;
        // 画三秒
        canvas.drawRect(left, 0.0F, left + getMeasuredHeight(), getMeasuredHeight(), mThreePaint);

        // 画进度

        canvas.drawRect(0, 0.0F, width * duration / mMaxDuration, getMeasuredHeight(), mProgressPaint);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        width = Resources.getSystem().getDisplayMetrics().widthPixels;

        mProgressPaint = new Paint();
        mThreePaint = new Paint();
        float[] positions = new float[]{0.1f, 0.3f, 0.6f, 1.0f};
        int[] progressColors = new int[]{Color.parseColor("#ED445C"), Color.parseColor("#F6D45C"), Color.parseColor("#54ABEF"), Color.parseColor("#45C01A")};
        LinearGradient gradient = new LinearGradient(0, 0, width, 0, progressColors, positions, Shader.TileMode.MIRROR);
        mProgressPaint.setShader(gradient);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mThreePaint.setColor(ContextCompat.getColor(getContext(), R.color.theme_red));
        mThreePaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mRecording = false;
        mHandler.removeCallbacksAndMessages(null);
    }


    public void setMinDuration(int duration) {
        this.mMinDuration = duration;
    }

    public void setMaxDuration(int duration) {
        this.mMaxDuration = duration;
    }

    public void start() {
        mRecording = true;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessage(0);
    }

    public void stop() {
        mRecording = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    public boolean isDurationQualified() {
        return mMinDuration <= duration;
    }

    public int getDurationSecond() {
        return duration / 1000;
    }

    public void reset() {
        mRecording = false;
        mHandler.removeCallbacksAndMessages(null);
        duration = 0;
        invalidate();
    }
}
