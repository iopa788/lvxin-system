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
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;

public class MomentRespondWindow extends PopupWindow implements OnClickListener {
    private TextView praise;
    private OnItemClickedListener onItemClickedListener;

    public MomentRespondWindow(Context context) {
        super(context, null);
        View contentView = LayoutInflater.from(context).inflate(R.layout.window_moment_respond, null);
        setContentView(contentView);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        contentView.measure(w, h);
        setWidth(contentView.getMeasuredWidth());
        setHeight(contentView.getMeasuredHeight());

        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(0x00000000));
        praise = ((TextView) getContentView().findViewById(R.id.praise_text));
        getContentView().findViewById(R.id.bar_praise).setOnClickListener(this);
        getContentView().findViewById(R.id.bar_comment).setOnClickListener(this);
        setAnimationStyle(R.style.popubWindowAlphaAnimation);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        onItemClickedListener.onItemClicked(v.getTag(), v);
    }


    public void showAtLocation(View parent, boolean hasPraise) {
        praise.setText(hasPraise ? R.string.common_cancel : R.string.common_praise);
        int[] loc = new int[2];
        parent.getLocationOnScreen(loc);
        int x = loc[0];
        int y = (int) (loc[1] - Resources.getSystem().getDisplayMetrics().density * 8);
        super.showAtLocation(parent, Gravity.NO_GRAVITY, x, y);
    }

    public void enablePariseMenu() {
        getContentView().findViewById(R.id.bar_praise).setEnabled(true);
    }

    public void disenablePariseMenu() {
        getContentView().findViewById(R.id.bar_praise).setEnabled(false);
    }
}
