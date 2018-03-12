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
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.farsunset.lvxin.util.KeyboardUtil;

public class KeyboardSmartAdapterLayout extends RelativeLayout {

    private final static String TAG = KeyboardSmartAdapterLayout.class.getSimpleName();
    private InputAreaPanelView mPanelLayout;

    private boolean mIsKeyboardShowing = false;
    private int maxBottom = 0;
    private int mStatusBarHeight;
    private int mOldHeight = -1;
    private int mNavBarHeight;

    public KeyboardSmartAdapterLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mPanelLayout = (InputAreaPanelView) findViewById(android.R.id.inputArea);
        mStatusBarHeight = getStatusBarHeight();
        mNavBarHeight = getNavBarHeight();
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public int getNavBarHeight() {
        int resourceId = 0;
        int rid = getContext().getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = getContext().getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return getContext().getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        // 由当前布局被键盘挤压，获知，由于键盘的活动，导致布局将要发生变化。
        do {
            final int height = MeasureSpec.getSize(heightMeasureSpec);
            if (height < 0) {
                break;
            }

            if (mOldHeight < 0) {
                mOldHeight = height;
                break;
            }

            final int offset = mOldHeight - height;

            if (offset == 0) {
                Log.d(TAG, "" + offset + " == 0 break;");
                break;
            }

            if (Math.abs(offset) == mStatusBarHeight || (mNavBarHeight > 0 && Math.abs(offset) == mNavBarHeight)) {
                break;
            }

            mOldHeight = height;

            // 检测到真正的 由于键盘收起触发了本次的布局变化
            onGetKeyboardHeight(offset);

            if (offset > 0) {
                //键盘弹起 (offset > 0，高度变小)
                mPanelLayout.setIsHide(true);
            } else if (mIsKeyboardShowing) {
                // 1. 总得来首，在监听到键盘已经显示的前提下，键盘收回才是有效有意义的。
                // 2. 修复在Android L下使用V7.Theme.AppCompat主题，进入Activity，默认弹起面板bug，
                // 第2点的bug出现原因:在Android L下使用V7.Theme.AppCompat主题，并且不使用系统的ActionBar/ToolBar，V7.Theme.AppCompat主题,还是会先默认绘制一帧默认ActionBar，然后再将他去掉（略无语）
                //键盘收回 (offset < 0，高度变大)
                mPanelLayout.setIsShow(true);
            }

        } while (false);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    protected void onKeyboardShowing(final boolean isShowing) {
        this.mIsKeyboardShowing = isShowing;
        mPanelLayout.setIsKeyboardShowing(isShowing);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        int offset = Math.abs(maxBottom - b);
        //状态栏显示的时候
        if (offset == mStatusBarHeight || (mNavBarHeight > 0 && offset == mNavBarHeight)) {
            return;
        }
        if (b >= maxBottom && maxBottom != 0) {
            onKeyboardShowing(false);
        } else if (maxBottom != 0) {
            onKeyboardShowing(true);
        }

        if (maxBottom < b) {
            maxBottom = b;
        }
    }


    private void onGetKeyboardHeight(int keyboardHeight) {
        final boolean change = KeyboardUtil.saveKeyboardHeight(keyboardHeight);
        if (change) {
            final int panelHeight = mPanelLayout.getHeight();
            if (panelHeight != keyboardHeight) {
                mPanelLayout.refreshHeight();
            }
        }
    }

}

