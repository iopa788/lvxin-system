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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.farsunset.lvxin.listener.OnListViewRefreshListener;
import com.farsunset.lvxin.network.model.Page;
import com.farsunset.lvxin.pro.R;

public class NearbyPullRefreshListView extends ListView implements OnScrollListener {
    private final static int RATIO = 3;

    private final static int RELEASE_TO_REFRESH = 0;
    private final static int PULL_TO_REFRESH = 1;
    private final static int REFRESHING = 2;
    private final static int DONE = 3;
    private final static int LOADING = 4;
    private final static int LOADING_MORE = 5;
    private final static int LOADING_MORE_DONE = 6;
    private ListHeaderView header;
    private ListFooterView footer;
    private int startY;
    private int headerHeight;
    private int state;
    private Page mPage;

    private OnListViewRefreshListener refreshListener;
    private boolean isRecored;


    public NearbyPullRefreshListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setOnScrollListener(this);
        init(paramContext);
    }


    public void init(final Context context) {
        header = (ListHeaderView) LayoutInflater.from(context).inflate(R.layout.layout_nearby_listheader, null);
        footer = (ListFooterView) LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);

        headerHeight = header.getTopHeaderHeight();
        addHeaderView(header, null, false);

        addFooterView(footer, null, false);
        footer.setVisibility(View.GONE);
        header.setPadding(0, -1 * headerHeight, 0, 0);
    }


    @Override
    public void onScroll(AbsListView arg0, int firstVisiableItem, int visibleItemCount, int count) {

    }

    public void doRefresh() {
        state = REFRESHING;
        changeHeaderViewByState();
        onRefresh();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!isRecored) {
                    isRecored = true;
                    startY = (int) event.getY();
                    //Log.v(TAG, "在down时候记录当前位置‘");
                }
                break;

            case MotionEvent.ACTION_UP:

                if (state != REFRESHING && state != LOADING) {

                    if (state == PULL_TO_REFRESH) {
                        state = DONE;
                        changeHeaderViewByState();

                        //Log.v(TAG, "由下拉刷新状态，到done状态");
                    }
                    if (state == RELEASE_TO_REFRESH) {
                        state = REFRESHING;
                        changeHeaderViewByState();
                        onRefresh();

                        //	Log.v(TAG, "由松开刷新状态，到done状态");
                    }
                }

                isRecored = false;
                break;

            case MotionEvent.ACTION_MOVE:
                int tempY = (int) event.getY();

                if (!isRecored) {
                    //Log.v(TAG, "在move时候记录下位置");
                    isRecored = true;
                    startY = tempY;
                }

                if (state != REFRESHING && isRecored && state != LOADING) {

                    // 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动

                    // 可以松手去刷新了
                    if (state == RELEASE_TO_REFRESH) {

                        setSelection(0);

                        // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                        if (((tempY - startY) / RATIO < headerHeight)
                                && (tempY - startY) > 0) {
                            state = PULL_TO_REFRESH;
                            changeHeaderViewByState();

                            //Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
                        }
                        // 一下子推到顶了
                        else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();

                            //Log.v(TAG, "由松开刷新状态转变到done状态");
                        }
                        // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                    }
                    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                    if (state == PULL_TO_REFRESH) {

                        //setSelection(0);

                        // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        if ((tempY - startY) / RATIO >= headerHeight) {
                            state = RELEASE_TO_REFRESH;
                            changeHeaderViewByState();

                            //Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
                        }
                        // 上推到顶了
                        else if (tempY - startY <= 0) {
                            state = DONE;
                            changeHeaderViewByState();

                            ///Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
                        }
                    }

                    // done状态下
                    if (state == DONE && tempY - startY > 0) {
                        state = PULL_TO_REFRESH;
                        changeHeaderViewByState();
                    }

                    // 更新headView的size
                    if (state == PULL_TO_REFRESH) {
                        header.setPadding(0, -1 * headerHeight + (tempY - startY) / RATIO, 0, 0);

                    }

                    // 更新headView的paddingTop
                    if (state == RELEASE_TO_REFRESH && ((tempY - startY) / RATIO - headerHeight) <= 0) {
                        header.setPadding(0, (tempY - startY) / RATIO - headerHeight, 0, 0);
                    }
                    float alpha = (float) (headerHeight - Math.abs(header.getPaddingTop())) / headerHeight;
                    header.setBridAlpha(alpha);
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    public boolean isSlideToBottom() {
        int scrollExtent = computeVerticalScrollExtent();
        int scrollOffset = computeVerticalScrollOffset();
        int scrollRange = computeVerticalScrollRange();
        return scrollExtent + scrollOffset >= scrollRange;

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && isSlideToBottom() && computeVerticalScrollOffset() > 0) {
            if (mPage == null || mPage.hasMore()) {
                footer.showProgressBar();
                footer.hideHintView();
                refreshListener.onGetNextPage();
            } else {
                footer.hideProgressBar();
                footer.showHintView();
            }
        }
    }


    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case REFRESHING:
                header.setPadding(0, 0, 0, 0);
                break;
            case DONE:
                header.setPadding(0, -1 * headerHeight, 0, 0);
                break;
        }
    }


    public void setOnRefreshListener(OnListViewRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }


    public void refreshComplete(Page page) {
        state = DONE;
        this.mPage = page;
        header.cancelBridAnimation();
        changeHeaderViewByState();
    }

    private void onRefresh() {
        header.startBridAnimation();
        if (refreshListener != null) {
            refreshListener.onGetFirstPage();
        }
    }

}
