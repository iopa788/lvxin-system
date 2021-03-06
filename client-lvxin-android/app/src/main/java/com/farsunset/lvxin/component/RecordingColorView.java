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
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class RecordingColorView extends View {
    private Paint mColorPaint;
    private Path mPath;
    private int[] greenColors = new int[]{0xFF1aad19, 0xe1199018};
    private int[] redColors = new int[]{0xffFF4C23, 0xe1E21674};
    private boolean touchOutSide = false;
    private int mWidth;
    private int mHeight;
    private int mArcHeight;
    private int ox;
    private int oy;

    public RecordingColorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        mHeight = mWidth * 9 / 20;
        mArcHeight = mHeight - (int) (Resources.getSystem().getDisplayMetrics().density * 10);
        ox = mWidth / 2;
        oy = (int) (-mWidth / 2.2);
        mPath = new Path();
        mColorPaint = new Paint();
        mColorPaint.setAntiAlias(true);
        mColorPaint.setStyle(Paint.Style.FILL);
    }

    public int getRealHeight() {
        return mHeight;
    }

    public void setTouchOutSide(boolean outSide) {
        this.touchOutSide = outSide;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!touchOutSide) {
            mColorPaint.setShader(new LinearGradient(ox, oy, mWidth, mHeight, greenColors[0], greenColors[1], Shader.TileMode.MIRROR));
        } else {
            mColorPaint.setShader(new LinearGradient(ox, oy, mWidth, mHeight, redColors[0], redColors[1], Shader.TileMode.MIRROR));
        }

        canvas.drawRect(0, 0, mWidth, mArcHeight, mColorPaint);
        mPath.reset();
        mPath.moveTo(0, mArcHeight);
        mPath.quadTo(0.5f * mWidth, mArcHeight + (mHeight - mArcHeight) * 2, mWidth, mArcHeight);

        mColorPaint.setShadowLayer(15, 0, 0, mColorPaint.getColor());
        canvas.drawPath(mPath, mColorPaint);
    }


}
