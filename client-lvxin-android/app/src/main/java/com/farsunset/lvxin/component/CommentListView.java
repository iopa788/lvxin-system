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
import android.content.res.Resources;
import android.os.Vibrator;
import android.text.Html;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.farsunset.lvxin.activity.contact.UserDetailActivity;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.database.CommentRepository;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.Comment;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Moment;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.model.CommentObject;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AnimationTools;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

import java.util.List;

public class CommentListView extends LinearLayout implements OnDialogButtonClickListener, OnLongClickListener, OnClickListener {
    private Moment moment;
    private OnItemClickedListener onCommentClickListener;
    private CustomDialog customDialog;
    private User self;
    private Comment removeComment;
    private int mLastTouchY;
    private GridLayout gridLayout;
    private LinearLayout commentPanel;
    private RelativeLayout praisePanel;
    private View divider;
    private int spacing;
    private int iconWidth;

    public CommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.LEFT);
        customDialog = new CustomDialog(getContext());
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setTitle(R.string.common_delete);
        customDialog.setMessage(getContext().getString(R.string.tip_delete_comment));
        customDialog.setButtonsText(getContext().getString(R.string.common_cancel), getContext().getString(R.string.common_confirm));
        self = Global.getCurrentUser();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        commentPanel = (LinearLayout) findViewById(R.id.commentPanel);
        praisePanel = (RelativeLayout) findViewById(R.id.praisePanel);
        gridLayout = (GridLayout) praisePanel.findViewById(R.id.gridLayout);
        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        divider = praisePanel.findViewById(R.id.divider);

        float density = Resources.getSystem().getDisplayMetrics().density;
        int column = gridLayout.getColumnCount();
        iconWidth = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels - (113 * density + (column - 1) * spacing)) / column);
    }

    public void setOnItemClickListener(OnItemClickedListener onCommentClickListener) {
        this.onCommentClickListener = onCommentClickListener;
    }


    public int getLastTouchY() {
        return mLastTouchY;
    }


    public void showCommentsAndPraises(Moment moment) {
        this.moment = moment;
        showPraiseView();
        showCommentView();
    }

    private void showPraiseView() {
        List<Comment> praiseList = moment.getPraiseList();
        praisePanel.setVisibility(praiseList.isEmpty() ? GONE : VISIBLE);
        gridLayout.removeAllViews();
        tooglePanelDivider();

        for (int i = 0; i < praiseList.size(); i++) {
            WebImageView itemView = new WebImageView(this.getContext());
            itemView.setId(R.id.icon);
            itemView.setTag(praiseList.get(i));
            itemView.setOnClickListener(this);
            itemView.load(FileURLBuilder.getUserIconUrl(praiseList.get(i).account), R.drawable.icon_def_head);
            gridLayout.addView(itemView, iconWidth, iconWidth);

            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
        }

    }


    private void showCommentView() {
        List<Comment> commentList = moment.getTextList();
        commentPanel.removeAllViews();
        commentPanel.setVisibility(commentList.isEmpty() ? GONE : VISIBLE);

        for (int i = 0; i < commentList.size(); i++) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_circle_comment, null);
            commentPanel.addView(itemView);
            itemView.setOnClickListener(this);
            display(itemView, commentList.get(i));
        }
    }

    private void display(View itemView, Comment comment) {
        EmoticonTextView commentText = (EmoticonTextView) itemView;
        String name = FriendRepository.queryFriendName(comment.account);
        CommentObject body = new Gson().fromJson(comment.content, CommentObject.class);
        if (Comment.TYPE_1.equals(comment.type)) {
            String replyName = FriendRepository.queryFriendName(body.replyAccount);
            String string = getResources().getString(R.string.label_moment_replay_user, name, replyName, body.content);
            commentText.setText(Html.fromHtml(string));
        } else {
            String string = getResources().getString(R.string.label_moment_replay, name, body.content);
            commentText.setText(Html.fromHtml(string));
        }
        itemView.setTag(comment);
        itemView.setOnLongClickListener(this);
    }

    private void tooglePanelDivider() {
        if (moment.getTextCount() > 0 && moment.getPraiseCount() > 0) {
            divider.setVisibility(VISIBLE);
        }
        if (moment.getTextCount() == 0) {
            divider.setVisibility(GONE);
        }
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }


    @Override
    public void onRightButtonClicked() {

        customDialog.dismiss();

        performRemoveCommentRequest();

        removeComment(removeComment);
    }

    private void performRemoveCommentRequest() {
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.COMMENT_DELETE_URL, BaseResult.class);
        requestBody.addParameter("authorAccount", removeComment.articleId);
        requestBody.addParameter("gid", removeComment.gid);
        HttpRequestLauncher.executeQuietly(requestBody);
    }


    @Override
    public boolean onLongClick(View view) {
        Comment comment = (Comment) view.getTag();
        if (comment.account.equals(self.account) && comment.gid != null) {
            ((Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(20);
            removeComment = comment;
            customDialog.show();
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        Comment comment = (Comment) view.getTag();
        if (view.getId() == R.id.icon) {
            Friend friend = FriendRepository.queryFriend(comment.account);
            Intent intent = new Intent(getContext(), UserDetailActivity.class);
            intent.putExtra(Friend.class.getName(), friend);
            getContext().startActivity(intent);
            return;
        }

        int[] loc = new int[2];
        view.getLocationOnScreen(loc);
        mLastTouchY = loc[1] + view.getMeasuredHeight();
        if (!self.account.equals(comment.account)) {
            onCommentClickListener.onItemClicked(comment, this);
        }

    }

    public int getEndYOnScrenn() {
        int[] loc = new int[2];
        if (getChildCount() == 0 || getVisibility() == GONE) {
            View parent = (View) getParent();
            parent.getLocationOnScreen(loc);
            return loc[1] + parent.getMeasuredHeight();
        }

        getLocationOnScreen(loc);
        return loc[1] + getMeasuredHeight();
    }

    public void addPraise(Comment data) {
        WebImageView icon = new WebImageView(getContext());
        icon.load(FileURLBuilder.getUserIconUrl(data.account), R.drawable.icon_def_head);
        icon.setOnClickListener(this);
        icon.setTag(data);
        icon.setId(R.id.icon);
        gridLayout.addView(icon, iconWidth, iconWidth);

        boolean isRowFirst = (gridLayout.getChildCount() - 1) % gridLayout.getColumnCount() != 0;
        ((GridLayout.LayoutParams) icon.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

        boolean isFirstRow = (gridLayout.getChildCount() - 1) < gridLayout.getColumnCount();
        ((GridLayout.LayoutParams) icon.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;

        praisePanel.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
        tooglePanelDivider();
    }


    public void addComment(final Comment comment) {
        moment.add(comment);
        setVisibility(View.VISIBLE);
        commentPanel.setVisibility(View.VISIBLE);
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_circle_comment, null);
        commentPanel.addView(itemView);
        AnimationTools.start(itemView, R.anim.appear);
        display(itemView, comment);
        itemView.setOnClickListener(this);
        tooglePanelDivider();
    }

    public void removeComment(Comment comment) {
        moment.remove(comment);
        for (int i = 0; i < commentPanel.getChildCount(); i++) {
            View itemView = commentPanel.getChildAt(i);
            if (comment.equals(itemView.getTag())) {
                AnimationTools.start(itemView, R.anim.disappear);
                commentPanel.removeViewAt(i);
            }
        }

        CommentRepository.deleteById(comment.gid);

        if (moment.getAllCount() == 0) {
            setVisibility(GONE);
        }

        tooglePanelDivider();
    }

    public void removePraise(Comment data) {

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View itemView = gridLayout.getChildAt(i);
            if (data.equals(itemView.getTag())) {
                AnimationTools.start(itemView, R.anim.disappear);
                gridLayout.removeViewAt(i);
            }
        }
        praisePanel.setVisibility(moment.getPraiseCount() == 0 ? GONE : VISIBLE);

        if (moment.getAllCount() == 0) {
            setVisibility(GONE);
        }

    }
}
