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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;


public class ScrollableWebView extends WebView {
    private MarginLayoutParams layoutParams;
    private int currentTop;
    private int startY;
    private int minTopMargin;
    private int maxTopMargin;

    public ScrollableWebView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        maxTopMargin = Resources.getSystem().getDisplayMetrics().widthPixels / 2;
        setEnabled(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (changed && minTopMargin == 0) {
            layoutParams = (MarginLayoutParams) this.getLayoutParams();
            minTopMargin = layoutParams.topMargin;
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (getScrollY() != 0) {
            return super.dispatchTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                break;

            case MotionEvent.ACTION_UP:
                currentTop = minTopMargin;
                layoutParams.setMargins(0, currentTop, 0, 0);
                setLayoutParams(layoutParams);
                break;

            case MotionEvent.ACTION_MOVE:
                if (event.getY() > startY) {
                    currentTop = Math.min((int) (minTopMargin + (event.getY() - startY) / 3), maxTopMargin);
                    layoutParams.setMargins(0, currentTop, 0, 0);
                    setLayoutParams(layoutParams);
                }
                if (event.getY() < startY) {
                    currentTop = Math.max((int) (minTopMargin - (startY - event.getY()) / 3), minTopMargin);
                    layoutParams.setMargins(0, currentTop, 0, 0);
                    setLayoutParams(layoutParams);
                }
                break;
        }

        if (currentTop != 0 && currentTop != minTopMargin) {
            return false;
        }

        return super.dispatchTouchEvent(event);
    }

}
