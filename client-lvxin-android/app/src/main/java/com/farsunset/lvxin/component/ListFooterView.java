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
import android.widget.RelativeLayout;

import com.farsunset.lvxin.pro.R;

public class ListFooterView extends RelativeLayout {
    private View footerProgressBar;
    private View footerHintView;

    public ListFooterView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        footerProgressBar = findViewById(R.id.footer_progressBar);
        footerHintView = findViewById(R.id.footer_hint);
    }

    public void hideProgressBar() {
        footerProgressBar.setVisibility(INVISIBLE);
    }

    public void showProgressBar() {
        setVisibility(VISIBLE);
        footerProgressBar.setVisibility(VISIBLE);
    }

    public void hideHintView() {
        footerHintView.setVisibility(INVISIBLE);
    }

    public void showHintView() {
        setVisibility(VISIBLE);
        footerHintView.setVisibility(VISIBLE);
    }
}
