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
package com.farsunset.lvxin.activity.contact;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.ContactSelectorAdapter;
import com.farsunset.lvxin.component.CharSelectorBar;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.listener.OnContactHandleListener;
import com.farsunset.lvxin.listener.OnTouchMoveCharListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.pro.R;

import java.util.List;

public class ContactSelectorActivity extends BaseActivity implements OnContactHandleListener, TextWatcher, OnTouchMoveCharListener {

    public static final int RESULT_CODE = 8155;
    protected ContactSelectorAdapter adapter;
    protected Button button;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private LinearLayout memberIconPanel;
    private EditText keyword;
    private HorizontalScrollView horizontalScrollView;
    private View emptyView;

    @Override
    public void initComponents() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( layoutManager = new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new ContactSelectorAdapter());

        adapter.setOnContactHandleListener(this);
        memberIconPanel = (LinearLayout) findViewById(R.id.memberIconPanel);
        keyword = (EditText) findViewById(R.id.keyword);
        keyword.addTextChangedListener(this);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        emptyView = findViewById(R.id.emptyView);
        emptyView.setOnClickListener(this);


        CharSelectorBar sideBar = (CharSelectorBar) findViewById(R.id.sidrbar);
        sideBar.setTextView((TextView) findViewById(R.id.dialog));
        sideBar.setOnTouchMoveCharListener(this);

        adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            onConfirmMenuClicked();
        }
        if (v.getId() == R.id.icon) {
            Friend friend = (Friend) v.getTag(R.id.target);
            View itemView = recyclerView.findViewWithTag(friend);
            if (itemView != null) {
                ((CheckBox) itemView.findViewById(R.id.checkbox)).setChecked(false);
            }
            adapter.getSelectedList().remove(friend);
            onContactCanceled(friend);
        }

    }
    public void onLoadContactList(List<Friend> list) {
        adapter.notifyDataSetChanged(list);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_contact_selecotr;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.title_choice_contact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        button = (Button) menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setText(R.string.common_confirm);
        button.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onContactClicked(MessageSource source) {

    }

    @Override
    public void onContactSelected(MessageSource source) {

        int side = (int) (this.getResources().getDisplayMetrics().density * 38);
        int padding = (int) (this.getResources().getDisplayMetrics().density * 5);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(side, side);
        if (memberIconPanel.getChildCount() > 0) {
            lp.setMargins(padding, 0, 0, 0);
        }
        WebImageView image = new WebImageView(this);
        image.setTag(R.id.target, source);
        image.setId(R.id.icon);
        image.setLayoutParams(lp);
        image.load(source.getWebIcon(), R.drawable.icon_def_head);
        image.setOnClickListener(this);
        memberIconPanel.addView(image);
        RelativeLayout.LayoutParams rl = (RelativeLayout.LayoutParams) keyword.getLayoutParams();
        if (memberIconPanel.getChildCount() >= 5) {
            horizontalScrollView.getLayoutParams().width = 5 * (side + padding);
        }
        horizontalScrollView.scrollTo(horizontalScrollView.getBottom(), 0);
        keyword.setLayoutParams(rl);

        button.setEnabled(true);
    }

    @Override
    public void onContactCanceled(MessageSource source) {

        int side = (int) (this.getResources().getDisplayMetrics().density * 38);
        int padding = (int) (this.getResources().getDisplayMetrics().density * 5);

        for (int i = 0; i < memberIconPanel.getChildCount(); i++) {
            if (memberIconPanel.getChildAt(i).getTag(R.id.target).equals(source)) {
                memberIconPanel.removeViewAt(i);
            }
        }

        if (memberIconPanel.getChildCount() >= 5) {
            horizontalScrollView.getLayoutParams().width = 5 * (side + padding);
        } else {
            horizontalScrollView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        if (adapter.getSelectedList().isEmpty()) {
            button.setEnabled(false);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (!TextUtils.isEmpty(text.toString().trim())) {
            List<Friend> tempList = FriendRepository.queryLike(text.toString());
            if (tempList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged(tempList);
            }
        } else {
            emptyView.setVisibility(View.GONE);
            adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onCharChanged(char s) {
        // 该字母首次出现的位置
        int position = adapter.getPositionForSection(s);
        if (position != -1) {
            layoutManager.scrollToPositionWithOffset(position,0);
            layoutManager.setStackFromEnd(true);
        }
    }

    public void onConfirmMenuClicked() {
        Intent intent = new Intent();
        intent.putExtra("data", adapter.getSelectedList());
        setResult(RESULT_OK, intent);
        finish();
    }
}
