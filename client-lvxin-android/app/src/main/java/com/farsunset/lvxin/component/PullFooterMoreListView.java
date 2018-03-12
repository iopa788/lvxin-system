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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.farsunset.lvxin.listener.OnListViewRefreshListener;
import com.farsunset.lvxin.pro.R;

public class PullFooterMoreListView extends ListView implements OnScrollListener {

    private final static int LOADING_MORE = 5;
    private final static int LOADING_MORE_DONE = 6;
    private boolean hasMore = true;
    private View footer;
    private int footerState;
    private OnListViewRefreshListener refreshListener;


    public PullFooterMoreListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setOnScrollListener(this);
        init(paramContext);
    }


    public void init(final Context context) {
        footer = LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);
        addFooterView(footer, null, false);
        footer.setVisibility(View.GONE);
        footer.setPadding(0, 0, 0, -1 * footer.getMeasuredHeight());
    }


    @Override
    public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int count) {

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            //判断是否滚动到底部
            int lastIndex = view.getLastVisiblePosition();
            int count = view.getCount();
            boolean isBottom = lastIndex == count - 1;
            if (isBottom) {
                footer.setVisibility(View.VISIBLE);
                if (hasMore) {
                    footer.findViewById(R.id.footer_progressBar).setVisibility(View.VISIBLE);
                    footer.findViewById(R.id.footer_hint).setVisibility(View.GONE);
                    if (footerState != LOADING_MORE) {
                        refreshListener.onGetNextPage();
                        footerState = LOADING_MORE;
                    }
                }
            } else {
                footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
                footer.findViewById(R.id.footer_hint).setVisibility(View.VISIBLE);
            }
        }
    }


    public void setOnRefreshListener(OnListViewRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }


    public void showMoreComplete(boolean hasMore) {

        this.hasMore = hasMore;
        footerState = LOADING_MORE_DONE;
        footer.findViewById(R.id.footer_progressBar).setVisibility(View.GONE);
    }


}
