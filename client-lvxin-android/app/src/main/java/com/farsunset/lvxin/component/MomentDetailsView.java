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
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.CommentRepository;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.dialog.MomentRespondWindow;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.PublishObject;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.CommentResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.farsunset.lvxin.util.StringUtils;
import com.google.gson.Gson;

import java.util.List;

public class MomentDetailsView extends RelativeLayout implements OnClickListener, OnItemClickedListener, HttpRequestListener {
    protected Moment moment;
    protected TextView text;
    private WebImageView icon;
    private TextView name;
    private TextView time;
    private User self;
    private View praiseView;
    private GridLayout gridLayout;
    private int cellWidth;
    private MomentRespondWindow respondWindow;
    private View commentButton;
    private OnItemClickedListener onItemClickedListener;
    private int spacing;

    public MomentDetailsView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (cellWidth == 0 && gridLayout.getMeasuredWidth() > 0) {
            cellWidth = (gridLayout.getMeasuredWidth() - (gridLayout.getColumnCount() - 1) * spacing) / gridLayout.getColumnCount();
            listPraiseView();
        }
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        self = Global.getCurrentUser();
        name = (TextView) findViewById(R.id.name);
        text = (TextView) findViewById(R.id.text);
        icon = (WebImageView) findViewById(R.id.icon);
        time = (TextView) findViewById(R.id.time);
        praiseView = findViewById(R.id.praise_header);
        gridLayout = (GridLayout) findViewById(R.id.praiserGridLayout);
        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(this);
        respondWindow = new MomentRespondWindow(getContext());
        respondWindow.setOnItemClickedListener(this);

        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);

    }

    public void displayMoment(Moment moment) {
        this.moment = moment;
        icon.load(FileURLBuilder.getUserIconUrl(moment.account), R.drawable.icon_def_head);
        name.getPaint().setFakeBoldText(true);
        if (moment.account.equals(self.account)) {
            name.setText(self.name);
        } else {
            name.setText(FriendRepository.queryFriendName(moment.account));
        }
        time.setText(AppTools.getRecentTimeString(moment.timestamp));

        PublishObject publishObject = new Gson().fromJson(moment.content, PublishObject.class);
        if (StringUtils.isNotEmpty(publishObject.content)) {
            text.setVisibility(View.VISIBLE);
            text.setText(publishObject.content);
        } else {
            text.setVisibility(View.GONE);
        }


    }


    private void listPraiseView() {
        List<Comment> praiseList = moment.getPraiseList();

        if (praiseList.isEmpty()) {
            praiseView.setVisibility(GONE);
            return;
        }

        for (int i = 0; i < praiseList.size(); i++) {
            WebImageView itemView = new WebImageView(getContext());
            gridLayout.addView(itemView, cellWidth, cellWidth);
            itemView.setTag(praiseList.get(i).account);
            itemView.setId(R.id.icon);
            itemView.setOnClickListener(this);
            itemView.load(FileURLBuilder.getUserIconUrl(praiseList.get(i).account), R.drawable.icon_def_head);

            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.icon) {
            Friend friend = FriendRepository.queryFriend(view.getTag().toString());
            Intent intent = new Intent(getContext(), UserDetailActivity.class);
            intent.putExtra(Friend.class.getName(), friend);
            getContext().startActivity(intent);
        }

        if (view == commentButton) {
            respondWindow.showAtLocation(commentButton, getHasPraise() != null);
        }

    }

    private Comment getHasPraise() {
        for (Comment comment : moment.getPraiseList()) {
            if (comment.account.equals(self.account)) {
                return comment;
            }
        }
        return null;
    }


    @Override
    public void onItemClicked(Object obj, final View view) {
        if (view.getId() == R.id.bar_comment) {
            BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    onItemClickedListener.onItemClicked(view, view);
                }
            }, 50);
        }
        if (view.getId() == R.id.bar_praise) {
            Comment praise = getHasPraise();
            if (praise != null) {


                performRemoveCommentRequest(praise.gid, moment.gid, self.account, moment.account);

            } else {
                performAddPraiseRequest(moment.gid, self.account, moment.account);
            }
        }
    }


    private void performRemoveCommentRequest(String cid, String articleId, String account, String authorAccount) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_DELETE_URL, BaseResult.class);
        requestBody.addParameter("authorAccount", authorAccount);
        requestBody.addParameter("gid", cid);
        requestBody.addParameter("articleId", articleId);
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performAddPraiseRequest(String articleId, String account, String authorAccount) {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_PRAISE_URL, CommentResult.class);
        requestBody.addParameter("authorAccount", authorAccount);
        requestBody.addParameter("articleId", articleId);
        HttpRequestLauncher.execute(requestBody, this);
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {
        if (url.equals(URLConstant.COMMENT_PRAISE_URL) && result.isSuccess()) {
            Comment comment = ((CommentResult) result).data;
            addPraise(comment);
            CommentRepository.add(comment);
        }
        if (url.equals(URLConstant.COMMENT_DELETE_URL) && result.isSuccess()) {
            Comment praise = getHasPraise();
            removePraise(praise);
            CommentRepository.delete(praise);
        }
    }


    public void addPraise(Comment data) {
        WebImageView icon = new WebImageView(getContext());
        icon.load(FileURLBuilder.getUserIconUrl(data.account), R.drawable.icon_def_head);
        icon.setOnClickListener(this);
        icon.setTag(data.account);
        gridLayout.addView(icon, cellWidth, cellWidth);
        moment.add(data);
        boolean isRowFirst = (gridLayout.getChildCount() - 1) % gridLayout.getColumnCount() != 0;
        ((GridLayout.LayoutParams) icon.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

        boolean isFirstRow = (gridLayout.getChildCount() - 1) < gridLayout.getColumnCount();
        ((GridLayout.LayoutParams) icon.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;

        praiseView.setVisibility(VISIBLE);
    }


    public void removePraise(Comment data) {
        if (data == null) {
            return;
        }
        int index = moment.getPraiseList().indexOf(data);

        if (index >= 0) {
            moment.remove(data);
            gridLayout.removeViewAt(index);
        }
        if (moment.getPraiseList().isEmpty()) {
            praiseView.setVisibility(GONE);
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, String url) {

    }


    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }
}
