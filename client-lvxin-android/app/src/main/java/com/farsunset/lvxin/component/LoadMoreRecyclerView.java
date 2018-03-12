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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.farsunset.lvxin.listener.OnLoadRecyclerViewListener;
import com.farsunset.lvxin.network.model.Page;

public class LoadMoreRecyclerView extends RecyclerView {
    private OnLoadRecyclerViewListener onLoadEventListener;
    private Page mPage;
    private ListFooterView footerView;

    public LoadMoreRecyclerView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setLayoutManager(new LinearLayoutManager(paramContext));
        setItemAnimator(new DefaultItemAnimator());
        addOnScrollListener(new RecyclerOnScrollListener());
    }

    public void setOnLoadEventListener(OnLoadRecyclerViewListener onLoadEventListener) {
        this.onLoadEventListener = onLoadEventListener;
    }

    public void hideHintView() {
        footerView.hideHintView();
    }

    public void showProgressBar() {
        footerView.showProgressBar();
    }

    public boolean isSlideToBottom() {
        int scrollExtent = computeVerticalScrollExtent();
        int scrollOffset = computeVerticalScrollOffset();
        int scrollRange = computeVerticalScrollRange();

        return scrollExtent + scrollOffset >= scrollRange;

    }

    public void setFooterView(ListFooterView footerView) {
        this.footerView = footerView;
    }

    public void showMoreComplete(Page page) {
        mPage = page;
        footerView.hideProgressBar();
        if (mPage == null) {
            footerView.hideHintView();
            return;
        }
        if (!mPage.hasMore()) {
            footerView.showHintView();
        }
    }


    public class RecyclerOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                onLoadEventListener.onListViewStartScroll();
            }
            if (newState == RecyclerView.SCROLL_STATE_IDLE && isSlideToBottom() && computeVerticalScrollOffset() > 0) {
                if (mPage == null || mPage.hasMore()) {
                    footerView.showProgressBar();
                    footerView.hideHintView();
                    onLoadEventListener.onGetNextPage();
                } else {
                    footerView.hideProgressBar();
                    footerView.showHintView();
                }
            }
        }
    }
}
