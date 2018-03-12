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
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.SearchResultContactsAdapter;
import com.farsunset.lvxin.app.URLConstant;
import com.farsunset.lvxin.database.FriendRepository;
import com.farsunset.lvxin.database.GroupRepository;
import com.farsunset.lvxin.database.PublicAccountRepository;
import com.farsunset.lvxin.listener.HttpRequestListener;
import com.farsunset.lvxin.listener.OnContactHandleListener;
import com.farsunset.lvxin.model.Friend;
import com.farsunset.lvxin.model.Group;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.model.PublicAccount;
import com.farsunset.lvxin.network.HttpRequestBody;
import com.farsunset.lvxin.network.HttpRequestLauncher;
import com.farsunset.lvxin.network.result.BaseResult;
import com.farsunset.lvxin.network.result.GroupResult;
import com.farsunset.lvxin.network.result.PublicAccountResult;
import com.farsunset.lvxin.pro.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements HttpRequestListener, OnClickListener, TextWatcher, OnContactHandleListener {

    private SearchResultContactsAdapter resuleAdapter;
    private EditText searchView;
    private TextView label_group;
    private TextView label_pubaccount;
    private ListView memberListView;
    private List<MessageSource> contactsList = new ArrayList<>();

    @Override
    public void initComponents() {


        findViewById(R.id.clearButton).setOnClickListener(this);

        searchView = (EditText) findViewById(R.id.searchView);
        searchView.addTextChangedListener(this);
        memberListView = (ListView) findViewById(R.id.listView);
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_search_result_header, null);
        memberListView.addHeaderView(headerView);

        resuleAdapter = new SearchResultContactsAdapter(this);
        resuleAdapter.setOnContactHandleListener(this);
        memberListView.setAdapter(resuleAdapter);

        headerView.findViewById(R.id.item_group_id).setOnClickListener(this);
        headerView.findViewById(R.id.item_public_account).setOnClickListener(this);

        label_group = (TextView) findViewById(R.id.label_group_id);
        label_pubaccount = (TextView) findViewById(R.id.label_public_account);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.item_group_id:
                performSearchGroupRequest();
                break;
            case R.id.clearButton:
                searchView.setText(null);
                break;
            case R.id.item_public_account:
                performSearchPubAccountRequest();
                break;
        }
    }

    private void performSearchGroupRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_query)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.GROUP_DETAILED_URL, GroupResult.class);
        requestBody.addParameter("groupId", searchView.getText().toString());
        HttpRequestLauncher.execute(requestBody, this);
    }

    private void performSearchPubAccountRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_query)));
        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.PUBACCOUNT_DETAILED_URL, PublicAccountResult.class);
        requestBody.addParameter("account", searchView.getText().toString());
        HttpRequestLauncher.execute(requestBody, this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, String url) {

        hideProgressDialog();

        if (result instanceof GroupResult && ((GroupResult) result).data != null) {
            setartContactsActivity(((GroupResult) result).data);
            return;
        }

        if (result instanceof PublicAccountResult && ((PublicAccountResult) result).data != null) {
            setartContactsActivity(((PublicAccountResult) result).data);
            return;
        }

        showToastView(R.string.tip_search_noresult);
    }


    public void setartContactsActivity(MessageSource source) {
        Intent intent = null;
        if (source instanceof Friend) {
            intent = new Intent(SearchActivity.this, UserDetailActivity.class);
            intent.putExtra(Friend.class.getName(), source);
            startActivity(intent);
            return;
        }
        if (source instanceof Group) {
            intent = new Intent(SearchActivity.this, GroupDetailActivity.class);
            intent.putExtra("group", source);
            startActivity(intent);
            return;
        }

        if (source instanceof PublicAccount) {
            intent = new Intent(SearchActivity.this, PubAccountDetailActivity.class);
            intent.putExtra(PublicAccount.NAME, source);
            startActivity(intent);
        }

    }

    @Override
    public void onHttpRequestFailure(Exception e, String url) {
        hideProgressDialog();
        showToastView(R.string.tip_search_noresult);
    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_search;
    }


    @Override
    public int getToolbarTitle() {

        return R.string.common_add;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String keyword = searchView.getText().toString();
        if (TextUtils.isEmpty(keyword)) {
            memberListView.setVisibility(View.GONE);
        } else {

            memberListView.setVisibility(View.VISIBLE);
            SpannableStringBuilder text = new SpannableStringBuilder(getString(R.string.label_search_group, keyword));
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#45C01A")), 5, 5 + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            label_group.setText(text);

            text.clear();
            text.append(getString(R.string.label_search_pubaccount, keyword));
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#45C01A")), 5, 5 + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            label_pubaccount.setText(text);

            contactsList.clear();
            contactsList.addAll(FriendRepository.queryLike(keyword));
            contactsList.addAll(PublicAccountRepository.queryLike(keyword));
            contactsList.addAll(GroupRepository.queryLike(keyword));
            resuleAdapter.addAll(contactsList);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }


    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onContactClicked(MessageSource source) {
        setartContactsActivity(source);
    }

    @Override
    public void onContactSelected(MessageSource source) {

    }

    @Override
    public void onContactCanceled(MessageSource source) {

    }

}
