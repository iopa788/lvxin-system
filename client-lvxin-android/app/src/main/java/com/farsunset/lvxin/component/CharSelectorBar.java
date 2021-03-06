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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.farsunset.lvxin.listener.OnTouchMoveCharListener;
import com.farsunset.lvxin.pro.R;

public class CharSelectorBar extends View {
    public final static char STAR = '☆';
    private final static char[] ARRAYS = {STAR, 'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','#'};
    private OnTouchMoveCharListener onTouchMoveCharListener;
    private int current = -1;
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public CharSelectorBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / ARRAYS.length;

        for (int i = 0; i < ARRAYS.length; i++) {
            paint.setColor(Color.parseColor("#989898"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            int size = getResources().getDimensionPixelOffset(R.dimen.sort_char_size);
            paint.setTextSize(size);

            if (i == current) {

                paint.setColor(Color.parseColor("#45C01A"));
                paint.setFakeBoldText(true);
            }

            String charStr = String.valueOf(ARRAYS[i]);
            float xPos = width / 2 - paint.measureText(charStr) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(charStr, xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final float y = event.getY();
        final int c = (int) (y / getHeight() * ARRAYS.length);

        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            setBackgroundResource(android.R.color.white);
            current = -1;
            mTextDialog.setVisibility(View.INVISIBLE);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setBackgroundResource(R.color.gray_pressed);
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE && current != c && c >= 0 && c < ARRAYS.length) {

            String charStr = String.valueOf(ARRAYS[c]);
            onTouchMoveCharListener.onCharChanged(ARRAYS[c]);
            mTextDialog.setText(charStr);
            mTextDialog.setVisibility(View.VISIBLE);
            current = c;
        }

        return true;
    }

    public void setOnTouchMoveCharListener(OnTouchMoveCharListener onTouchMoveCharListener) {
        this.onTouchMoveCharListener = onTouchMoveCharListener;
    }
}
