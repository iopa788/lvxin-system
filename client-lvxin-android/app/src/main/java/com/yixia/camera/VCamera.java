package com.yixia.camera;

import android.content.Context;

import com.farsunset.lvxin.pro.BuildConfig;
import com.yixia.camera.util.DeviceUtils;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;

/**
 * 拍摄SDK
 *
 * @author yixia.com
 */
public class VCamera {

    /**
     * SDK版本号
     */
    public final static String VCAMERA_SDK_VERSION = "1.2.0";
    /**
     * 预览视频宽度
     */
    public static int VIDEO_WIDTH = 1280;
    /**
     * 预览视频高度
     */
    public static int VIDEO_HEIGHT = 720;
    /**
     * 输出视频宽度
     */
    public static int OUTVIDEO_WIDTH = 640;
    /**
     * 输出视频高度
     */
    public static int OUTVIDEO_HEIGHT = 360;
    /**
     * 是否是前置摄像头
     */
    public static Boolean isfont = false;
    /**
     * 应用包名
     */
    private static String mPackageName;
    /**
     * 应用版本名称
     */
    private static String mAppVersionName;
    /**
     * 应用版本号
     */
    private static int mAppVersionCode;
    /**
     * 视频缓存路径
     */
    private static String mVideoCachePath;

    /**
     * 初始化SDK
     */
    public static void initialize(Context context) {
        mPackageName = context.getPackageName();

        mAppVersionName = BuildConfig.VERSION_NAME;
        mAppVersionCode = BuildConfig.VERSION_CODE;

        //初始化底层库
        UtilityAdapter.FFmpegInit(context, String.format("versionName=%s&versionCode=%d&sdkVersion=%s&android=%s&device=%s",
                mAppVersionName, mAppVersionCode, VCAMERA_SDK_VERSION, DeviceUtils.getReleaseVersion(), DeviceUtils.getDeviceModel()));
    }

    /**
     * 是否开启log输出
     */
    public static boolean isLog() {
        return BuildConfig.DEBUG;
    }

    public static String getPackageName() {
        return mPackageName;
    }


    /**
     * 获取视频缓存文件夹
     */
    public static String getVideoCachePath() {
        return mVideoCachePath;
    }

    /**
     * 设置视频缓存路径
     */
    public static void setVideoCachePath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        mVideoCachePath = path;
    }
}
