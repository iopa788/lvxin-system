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
package com.farsunset.lvxin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.app.Global;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.bean.User;
import com.farsunset.lvxin.util.MLog;

public class CycleLocationReceiver extends BroadcastReceiver implements BDLocationListener {
    final static String TAG = CycleLocationReceiver.class.getSimpleName();
    private LocationClient mLocationClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        mLocationClient = new LocationClient(LvxinApplication.getInstance());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//设置定位模式
        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(this);
        mLocationClient.start();
    }

    @Override
    public void onReceiveLocation(BDLocation location) {

        mLocationClient.stop();
        mLocationClient.unRegisterLocationListener(this);
        mLocationClient = null;

        User self = Global.getCurrentUser();
        if (location != null && self != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            SentBody sent = new SentBody();
            sent.setKey("client_cycle_location");
            sent.put("account", self.account);
            sent.put("latitude", String.valueOf(latitude));
            sent.put("longitude", String.valueOf(longitude));
            sent.put("location", location.getAddrStr());

            if (CIMPushManager.isConnected(LvxinApplication.getInstance())) {
                CIMPushManager.sendRequest(LvxinApplication.getInstance(), sent);
            }

            self.latitude = latitude;
            self.longitude = longitude;
            self.location = location.getAddrStr();
            Global.modifyAccount(self);

            ClientConfig.setCurrentRegion(location.getCity());
            MLog.i(TAG, "*******************定位成功:" + location.getAddrStr());
        }
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }


}
