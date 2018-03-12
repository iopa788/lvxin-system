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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.farsunset.lvxin.util.KeyboardUtil;


public class InputAreaPanelView extends LinearLayout {

    private boolean mIsHide = false;
    private KeyboardStateObserver keyboardStateObserver;
    private boolean mIsKeyboardShowing = false;

    public InputAreaPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        refreshHeight();
    }

    public void refreshHeight() {
        if (isInEditMode()) {
            return;
        }
        final int keyboardHeight = KeyboardUtil.getKeyboardHeight(getContext());
        if (getHeight() == keyboardHeight) {

            keyboardStateObserver.onKeyboardHeightChanged(getHeight());
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams layoutParams = getLayoutParams();

                if (layoutParams == null) {
                    layoutParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                }
                layoutParams.height = keyboardHeight;

                keyboardStateObserver.onKeyboardHeightChanged(layoutParams.height);
                setLayoutParams(layoutParams);
            }
        });
    }

    public void setKeyboardStateObserver(KeyboardStateObserver keyboardStateObserver) {
        this.keyboardStateObserver = keyboardStateObserver;
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == VISIBLE) {
            this.mIsHide = false;
        }

        if (visibility == getVisibility()) {
            return;
        }


        if (mIsKeyboardShowing && visibility == VISIBLE) {
            return;
        }

        super.setVisibility(visibility);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mIsHide) {
            setVisibility(View.GONE);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setIsKeyboardShowing(final boolean isKeyboardShowing) {

        if (mIsKeyboardShowing == !isKeyboardShowing) {
            keyboardStateObserver.onKeyboardVisableChanged(isKeyboardShowing);
        }

        this.mIsKeyboardShowing = isKeyboardShowing;
    }


    public void setIsShow(final boolean isShow) {
        if (isShow) {
            super.setVisibility(View.VISIBLE);
        }
    }

    public void setIsHide(final boolean isHide) {
        this.mIsHide = isHide;
    }


    public interface KeyboardStateObserver {

        void onKeyboardHeightChanged(int height);

        void onKeyboardVisableChanged(boolean visable);
    }
}
