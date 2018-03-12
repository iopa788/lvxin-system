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
package com.farsunset.lvxin.activity.util;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;

import com.farsunset.lvxin.activity.base.BaseActivity;
import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.app.PermissionCompat;
import com.farsunset.lvxin.component.LinearProgressView;
import com.farsunset.lvxin.dialog.CustomDialog;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.listener.OnVideoRecordListener;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class VideoRecorderActivity extends BaseActivity implements OnClickListener, OnVideoRecordListener, MediaRecorderBase.OnErrorListener, MediaRecorderBase.OnEncodeListener, OnDialogButtonClickListener {

    public static final int REQUEST_CODE = 3685;
    /**
     * 录制最长时间
     */
    private static int RECORD_TIME_MAX = 10 * 1000;
    /**
     * 录制最小时间
     */
    private static int RECORD_TIME_MIN = 3 * 1000;
    /**
     * 拍摄按钮
     */
    private Button mRecordController;


    /**
     * 摄像头数据显示画布
     */
    private SurfaceView mSurfaceView;
    /**
     * 录制进度
     */
    private LinearProgressView mProgressView;

    /**
     * SDK视频录制对象
     */
    private MediaRecorderNative mMediaRecorder;
    /**
     * 视频信息
     */
    private MediaObject mMediaObject;

    /**
     * 是否是点击状态
     */
    private boolean isRecordingMode;

    private CustomDialog permissionDialog;
    private boolean isEncodeing = false;
    private boolean lightMode = false;
    private MenuItem menuToggle;
    private SNSVideo video;
    private OrientationEventListener mScreenOrientationEventListener;

    //1横 0 竖
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_media_recorder;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_video_recorder;
    }


    /**
     * 加载视图
     */
    @Override
    public void initComponents() {
        setBackIcon(R.drawable.abc_ic_clear_material);
        mSurfaceView = (SurfaceView) findViewById(R.id.record_preview);
        mProgressView = (LinearProgressView) findViewById(R.id.record_progress);
        mRecordController = (Button) findViewById(R.id.recorder);
        mRecordController.setSelected(true);
        mProgressView.setMaxDuration(RECORD_TIME_MAX);
        mProgressView.setMinDuration(RECORD_TIME_MIN);
        mProgressView.setOnVideoRecordListener(this);

        findViewById(R.id.confirm).setOnClickListener(this);

        mScreenOrientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int i) {
                if (isRecordingMode) {
                    byte mode = (45 <= i && i < 135) || (225 <= i && i < 315) ? SNSVideo.HORIZONTAL : SNSVideo.VERTICAL;
                    mMediaRecorder.setMode(mode);
                }
            }
        };
        mScreenOrientationEventListener.enable();

        if (checkCameraPermission())
        {
            initMediaRecorder();
        }
    }


    private  boolean checkCameraPermission() {
        boolean hasPermission = PermissionCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (hasPermission) {
            return true;
        }
        boolean isPermissionDenied = PermissionCompat.checkPermissionForeverDenied(this, Manifest.permission.CAMERA);
        if (isPermissionDenied) {
            permissionDialog = new CustomDialog(this);
            permissionDialog.setTitle(R.string.tip_permission_denied);
            permissionDialog.setMessage(this.getString(R.string.tip_permission_camera_disable));
            permissionDialog.hideCancelButton();
            permissionDialog.setOnDialogButtonClickListener(this);
            permissionDialog.show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 8);
        }
        return false;
    }

    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {

        //设置视频缓存路径
        VCamera.setVideoCachePath(LvxinApplication.CACHE_DIR_VIDEO);
        // 开启log输出,ffmpeg输出到logcat
        // 初始化拍摄SDK，必须
        VCamera.initialize(this);

        mMediaRecorder = new MediaRecorderNative();
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setOnEncodeListener(this);

        String key = com.farsunset.lvxin.util.StringUtils.getUUID();
        //设置缓存文件夹
        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath());
        //设置视频预览源
        mMediaRecorder.setSurfaceHolder(mSurfaceView.getHolder());
        //准备
        mMediaRecorder.prepare();
        //滤波器相关
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN && isTouchInside(event) && !isEncodeing) {
            mRecordController.setSelected(true);
            startRecord();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE && !isEncodeing) {
            if (!isTouchInside(event) && isRecordingMode) {
                mRecordController.setSelected(false);
                stopRecord();
            }
            if (isTouchInside(event) && isRecordingMode == false) {
                mRecordController.setSelected(true);
                startRecord();
            }
        }

        if ((event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP) && !isEncodeing) {
            mRecordController.setSelected(true);
            stopRecord();
        }
        return super.dispatchTouchEvent(event);
    }

    public boolean isTouchInside(MotionEvent event) {
        int[] loc = new int[2];
        mRecordController.getLocationInWindow(loc);
        return event.getRawY() >= loc[1] && event.getRawY() <= loc[1] + mRecordController.getHeight()
                && event.getRawX() >= loc[0] && event.getRawX() <= loc[0] + mRecordController.getWidth();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMediaRecorder!=null)
        mMediaRecorder.startPreview();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaRecorder!=null)
        mMediaRecorder.stopPreview();
    }


    /**
     * 开始录制
     */
    private void startRecord() {
        mMediaRecorder.startRecord();
        isRecordingMode = true;
        mProgressView.start();
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        isRecordingMode = false;
        mMediaRecorder.stopRecord();
        mProgressView.stop();
        if (mProgressView.isDurationQualified()) {
            findViewById(R.id.confirm).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.confirm).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confirm) {
            onRecordCompleted();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaRecorder != null) {
            mMediaRecorder.stopRecord();
            mMediaRecorder.release();
        }
        mScreenOrientationEventListener.disable();

        if (mMediaObject!=null)
        {
            mMediaObject.delete();
        }

    }

    @Override
    public void onEncodeStart() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
    }

    @Override
    public void onEncodeProgress(int progress) {
    }

    /**
     * 转码完成
     */
    @Override
    public void onEncodeComplete() {
        isEncodeing = false;
        hideProgressDialog();
        video = new SNSVideo();

        video.size = mMediaObject.getOutputVideoSize();
        video.duration = mProgressView.getDurationSecond();
        video.video = mMediaObject.getOutputVideoName();
        video.thumbnail = mMediaObject.getOutputVideoThumbName();
        video.mode = mMediaRecorder.getMode();
        mMediaObject.moveOutTempDir();


        LvxinApplication.getInstance().startVideoActivity(this, true, video, null);
    }

    /**
     * 转码失败 检查sdcard是否可用，检查分块是否存在
     */
    @Override
    public void onEncodeError() {
        hideProgressDialog();
        isEncodeing = false;
        finish();
    }

    @Override
    public void onVideoError(int what, int extra) {

    }

    @Override
    public void onAudioError(int what, String message) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_recorder, menu);
        menuToggle = menu.findItem(R.id.menu_toggle);
        // 是否支持前置摄像头
        if (!MediaRecorderBase.isSupportFrontCamera()) {
            menuToggle.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_toggle) {
            if (lightMode) {
                if (mMediaRecorder != null) {
                    mMediaRecorder.toggleFlashMode();
                }
                lightMode = false;
            }
            if (mMediaRecorder != null) {
                mMediaRecorder.switchCamera();
            }
            lightMode = !mMediaRecorder.isFrontCamera();

        }
        if (item.getItemId() == R.id.menu_light && mMediaRecorder != null && !mMediaRecorder.isFrontCamera()) {
            mMediaRecorder.toggleFlashMode();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRecordCompleted() {
        stopRecord();
        mScreenOrientationEventListener.disable();
        findViewById(R.id.confirm).setVisibility(View.GONE);
        mMediaRecorder.startEncoding();
        isEncodeing = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Intent intent = new Intent();
            intent.putExtra(SNSVideo.class.getName(), video);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            FileUtils.deleteQuietly(new File(LvxinApplication.CACHE_DIR_VIDEO, video.thumbnail));
            FileUtils.deleteQuietly(new File(LvxinApplication.CACHE_DIR_VIDEO, video.video));
            mProgressView.reset();
            findViewById(R.id.confirm).setVisibility(View.GONE);
            mScreenOrientationEventListener.enable();
        }
    }

    @Override
    public void onLeftButtonClicked() {

    }

    @Override
    public void onRightButtonClicked() {
        permissionDialog.dismiss();
        gotoPermissionSettingActivity();
    }

    private void gotoPermissionSettingActivity() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", getPackageName(), null));
        LvxinApplication.getInstance().startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initMediaRecorder();
        } else {
            showToastView(R.string.tip_permission_camera_rejected);
        }

    }
}
