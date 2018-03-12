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
package com.farsunset.lvxin.activity.chat;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.network.model.ChatMap;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.BackgroundThreadHandler;
import com.farsunset.lvxin.util.BitmapUtils;
import com.farsunset.lvxin.util.StringUtils;

import java.io.File;

public class MapLocationActivity extends BaseActivity implements BDLocationListener, SnapshotReadyCallback, OnMapLoadedCallback {

    private ChatMap chatMap = new ChatMap();
    private MenuItem button;
    private TextView addressTextView;
    private MapView mapView;
    private BaiduMap mBaiduMap;
    private LocationClient mLocClient;

    /**
     * 设置一些amap的属性
     */
    @Override
    public void initComponents() {


        addressTextView = (TextView) findViewById(R.id.text);
        // 地图初始化
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setIsNeedAddress(true);
        mLocClient.setLocOption(option);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onReceiveLocation(final BDLocation location) {
        mLocClient.stop();
        if (location != null) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);

            OverlayOptions ooA = new MarkerOptions().position(ll).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_map_location))
                    .zIndex(9).draggable(true);
            mBaiduMap.addOverlay(ooA);

            chatMap.latitude = latitude;
            chatMap.longitude = longitude;
            chatMap.address = location.getAddrStr();


            BackgroundThreadHandler.postUIThread(new Runnable() {
                @Override
                public void run() {
                    addressTextView.setVisibility(View.VISIBLE);
                    addressTextView.setText(getString(R.string.common_current_location, location.getAddrStr()));
                    button.setVisible(true);
                }
            });
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        if (mLocClient != null) {

            mLocClient.unRegisterLocationListener(this);
            mLocClient.stop();
        }
        mLocClient = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_send) {
            int w = Resources.getSystem().getDisplayMetrics().widthPixels; // 得到图片的宽，高
            int h = Resources.getSystem().getDisplayMetrics().heightPixels;
            int cropHeight = (int) (w * 0.6);
            int cropY0 = (h - cropHeight) / 2 - toolbar.getMeasuredHeight();
            int cropY1 = cropY0 + cropHeight;
            final Rect rect = new Rect(0, cropY0, w, cropY1);
            mBaiduMap.snapshotScope(rect, this);
            showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSnapshotReady(Bitmap bitmap) {
        hideProgressDialog();
        File file = new File(LvxinApplication.CACHE_DIR_IMAGE, StringUtils.getUUID());

        if (BitmapUtils.saveMapBitmap2File(bitmap, file)) {
            chatMap.key = file.getName();
            Intent intent = new Intent();
            intent.putExtra(ChatMap.class.getName(), chatMap);
            setResult(Activity.RESULT_OK, intent);
            this.finish();
        }

    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_map_view;
    }


    @Override
    public int getToolbarTitle() {

        return R.string.common_location;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        button = menu.findItem(R.id.menu_send);
        button.setVisible(false);
        mLocClient.start();
        mLocClient.requestLocation();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onMapLoaded() {

        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(14));
    }


}
