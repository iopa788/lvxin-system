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
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.farsunset.lvxin.adapter.ViewPaperAdapter;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EmoticonPanelView extends LinearLayout implements OnPageChangeListener, View.OnClickListener {

    private static final int PAGE_SIZE = 30;
    private ViewPager viewPager;
    private OnItemClickedListener emotionSelectedListener;
    private Context context;
    private List<View> pagerListView = new ArrayList<View>();
    private int pageIndex = 0;
    private LinearLayout emoticoViewPagerTagPanel;

    public EmoticonPanelView(Context context) {
        super(context);
        this.context = context;
    }

    public EmoticonPanelView(Context context, AttributeSet attrs) {


        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (viewPager.getMeasuredHeight() > 0 && viewPager.getChildCount() == 0) {
            buildEmotionPagerView();
        }
    }

    private void buildEmotionPagerView() {
        int cellWidth = Resources.getSystem().getDisplayMetrics().widthPixels / 6;
        int cellHeight = viewPager.getMeasuredHeight() / 5;
        Iterator<Map.Entry<String, Integer>> entries = LvxinApplication.EMOTION_MAP.entrySet().iterator();
        int index = 0;
        while (entries.hasNext()) {
            Map.Entry<String, Integer> entry = entries.next();
            if (index % PAGE_SIZE == 0) {
                GridLayout gridLayout = new GridLayout(getContext());
                gridLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                gridLayout.setColumnCount(6);
                gridLayout.setRowCount(5);
                pagerListView.add(gridLayout);
            }
            int line = index / PAGE_SIZE;
            GridLayout gridLayout = (GridLayout) pagerListView.get(line);
            ImageView cellView = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_emoticon_itemview, gridLayout, false);
            cellView.setImageResource(entry.getValue());
            cellView.setOnClickListener(this);
            cellView.setTag(entry.getKey());
            gridLayout.addView(cellView, new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellWidth, cellHeight)));

            if (index % PAGE_SIZE == PAGE_SIZE - 2) {
                ImageView deleteButton = (ImageView) LayoutInflater.from(context).inflate(R.layout.layout_emoticon_itemview, gridLayout, false);
                int pading = (int) (Resources.getSystem().getDisplayMetrics().density * 12 + 0.5f);
                deleteButton.setPadding(pading, pading, pading, pading);
                deleteButton.setImageResource(R.drawable.icon_emotion_delete);
                deleteButton.setOnClickListener(this);
                deleteButton.setTag("DELETE");
                gridLayout.addView(deleteButton, new GridLayout.LayoutParams(new ViewGroup.LayoutParams(cellWidth, cellHeight)));
                index++;
            }
            index++;
        }

        viewPager.setAdapter(new ViewPaperAdapter(pagerListView));
        viewPager.addOnPageChangeListener(this);

        emoticoViewPagerTagPanel.getChildAt(0).setSelected(true);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        emoticoViewPagerTagPanel = (LinearLayout) findViewById(R.id.emoticoViewPagerTagPanel);
        viewPager = (ViewPager) findViewById(R.id.emoticoViewPager);
    }

    @Override
    public void onClick(View v) {
        emotionSelectedListener.onItemClicked(v.getTag(), v);
    }


    public void setOnEmotionSelectedListener(OnItemClickedListener emoticoSelectedListener) {
        this.emotionSelectedListener = emoticoSelectedListener;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {


    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {


    }

    @Override
    public void onPageSelected(int index) {
        if (pageIndex != index) {
            emoticoViewPagerTagPanel.getChildAt(index).setSelected(true);
            emoticoViewPagerTagPanel.getChildAt(pageIndex).setSelected(false);
            pageIndex = index;
        }

    }

    public void setHeight(int height) {
        getLayoutParams().height = height;
        viewPager.getLayoutParams().height = (int) (height - Resources.getSystem().getDisplayMetrics().density * 35);
    }

}
