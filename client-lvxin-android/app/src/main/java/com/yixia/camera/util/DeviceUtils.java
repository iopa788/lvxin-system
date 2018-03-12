package com.yixia.camera.util;

import android.os.Build;

/**
 * 系统版本信息类
 *
 * @author tangjun
 */
public class DeviceUtils {


    /**
     * 获得设备的固件版本号
     */
    public static String getReleaseVersion() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 检测当前设备是否是特定的设备
     *
     * @param devices
     * @return
     */
    public static boolean isDevice(String... devices) {
        String model = DeviceUtils.getDeviceModel();
        if (devices != null && model != null) {
            for (String device : devices) {
                if (model.indexOf(device) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        return (Build.MODEL.trim());
    }


}
