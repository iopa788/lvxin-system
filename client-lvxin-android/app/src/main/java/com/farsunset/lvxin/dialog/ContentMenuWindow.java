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
package com.farsunset.lvxin.dialog;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.farsunset.lvxin.listener.OnMenuClickedListener;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;

import java.lang.reflect.Field;

public class ContentMenuWindow extends PopupWindow implements View.OnClickListener {
    private ViewGroup rootView;
    private OnMenuClickedListener operationListener;

    public ContentMenuWindow(Context context) {

        super(context);
        rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_context_menu_dialog, null);
        setWidth(AppTools.dip2px(150));

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        setAnimationStyle(R.style.popubWindowScaleAnimation);
        setContentView(rootView);
        setAllMenuClickListener();
    }

    public void  show(View anchor){
        int[] location = new int[2];
        anchor.getLocationInWindow(location);
        float longClickX = 0;
        float longClickY = 0;
        try {
            Field xField = View.class.getDeclaredField("mLongClickX");
            xField.setAccessible(true);
            longClickX = xField.getFloat(anchor);
            Field yField = View.class.getDeclaredField("mLongClickY");
            yField.setAccessible(true);
            longClickY = yField.getFloat(anchor);
        } catch (Exception e) {
        }
        int x = (int) (location[0] + longClickX);
        int y = (int) (location[1] + longClickY);

        super.showAtLocation(anchor, Gravity.NO_GRAVITY,x,y);
    }

    public void setOnMenuClickedListener(OnMenuClickedListener operationListener) {
        this.operationListener = operationListener;
    }

    public Object getTag() {

        return rootView.getTag();
    }

    public void setTag(Object tag) {

        rootView.setTag(tag);
    }

    public  View findViewById(int id){
        return rootView.findViewById(id);
    }

    public void buildChatMenuGroup(boolean hasTop, boolean noRead) {
        hideAll();
        if (!hasTop) {
            findViewById(R.id.menu_chat_top).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.menu_cancel_top).setVisibility(View.VISIBLE);
        }
        if (!noRead) {
            findViewById(R.id.menu_mark_noread).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.menu_mark_read).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.menu_delete_chat).setVisibility(View.VISIBLE);
    }

    public void buildChatRecordMenuGroup(boolean canCopy, boolean canRevoke) {
        hideAll();
        findViewById(R.id.menu_delete).setVisibility(View.VISIBLE);
        findViewById(R.id.menu_forward).setVisibility(View.VISIBLE);

        if (canCopy) {
            findViewById(R.id.menu_copy).setVisibility(View.VISIBLE);
        }
        if (canRevoke) {
            findViewById(R.id.menu_revoke).setVisibility(View.VISIBLE);
        }
    }

    private void hideAll() {
        for (int i = 0; i < rootView.getChildCount(); i++) {
            rootView.getChildAt(i).setVisibility(View.GONE);
        }
    }

    private void setAllMenuClickListener() {
        for (int i = 0; i < rootView.getChildCount(); i++) {
            rootView.getChildAt(i).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        operationListener.onMenuItemClicked(v.getId());
    }
}
