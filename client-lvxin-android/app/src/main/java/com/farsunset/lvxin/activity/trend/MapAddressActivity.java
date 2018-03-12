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
package com.farsunset.lvxin.activity.trend;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.adapter.MapAddressListAdapter;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.network.model.MapAddress;
import com.farsunset.lvxin.pro.R;

public class MapAddressActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnItemClickedListener, BDLocationListener {
    public static final int REQUEST_CODE = 7452;
    private PoiSearch mPoiSearch;
    private MapAddressListAdapter adapter;
    private LocationClient mLocClient;
    private LatLng mLocation;

    @Override
    public void initComponents() {

        RecyclerView listview = (RecyclerView) findViewById(R.id.recyclerView);
        listview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MapAddressListAdapter();
        listview.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);
        initLocation();

    }

    private void initLocation() {
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.requestLocation();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPoiSearch != null) {
            mPoiSearch.destroy();
        }
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.title_select_address;
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult.getAllPoi() != null) {
            adapter.addAll(poiResult.getAllPoi(), mLocation);
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj == null) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            PoiInfo poiInfo = (PoiInfo) obj;
            MapAddress mapAddress = new MapAddress();
            mapAddress.name = poiInfo.name;
            mapAddress.latitude = poiInfo.location.latitude;
            mapAddress.longitude = poiInfo.location.longitude;
            intent.putExtra(MapAddress.class.getName(), mapAddress);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        mLocClient.unRegisterLocationListener(this);
        mLocClient.stop();
        if (location != null) {
            mLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mPoiSearch = PoiSearch.newInstance();
            mPoiSearch.setOnGetPoiSearchResultListener(this);
            PoiNearbySearchOption searchOption = new PoiNearbySearchOption();
            searchOption.location(mLocation);
            searchOption.radius(300);// 检索半径，单位是米
            searchOption.pageCapacity(3);
            String[] keywords = getResources().getStringArray(R.array.poi_keyword);
            for (String keyword : keywords) {
                searchOption.keyword(keyword);
                mPoiSearch.searchNearby(searchOption);
            }
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
