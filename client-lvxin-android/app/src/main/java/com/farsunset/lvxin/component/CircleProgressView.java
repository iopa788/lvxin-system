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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressView extends View {
    private Paint mBorderPaint;
    private Paint mProgressPaint;
    private int progress;
    private RectF mRectF = new RectF();

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressPaint = new Paint();
        mProgressPaint.setColor(0x7fffffff);
        mProgressPaint.setAntiAlias(true);
        mBorderPaint = new Paint();
        mBorderPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 2);
        mBorderPaint.setColor(0xffffffff);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);


    }

    public void setProgress(int progress) {
        this.progress = progress;
        mProgressPaint.setColor(0x7fffffff);
        mBorderPaint.setColor(0xffffffff);
        invalidate();
    }

    public void loadError() {
        invalidate();
        mProgressPaint.setColor(0x7fED445C);
        mBorderPaint.setColor(0xffED445C);
    }

    @Override
    public void onDraw(Canvas canvas) {
        int radius = (int) (getWidth() / 2 * 0.9);
        int ox = getWidth() / 2;
        int oy = getHeight() / 2;
        mRectF.left = ox - radius;
        mRectF.top = oy - radius;
        mRectF.right = ox + radius;
        mRectF.bottom = oy + radius;
        canvas.drawArc(mRectF, -90, 360 * progress / 100, true, mProgressPaint);
        canvas.drawArc(mRectF, 0, 360, false, mBorderPaint);
    }


}
