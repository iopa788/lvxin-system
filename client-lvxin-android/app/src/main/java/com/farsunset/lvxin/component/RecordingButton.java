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
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ViewStubCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.network.model.ChatVoice;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.StringUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;


public class RecordingButton extends CardView {
    private long startTime;
    private RecordingHintView recordingHintView;
    private long endTime;
    private MediaRecorder mediaRecorder;
    private File voiceFile;
    private OnRecordCompletedListener onRecordCompletedListener;
    private TextView textView;
    Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            long t = System.currentTimeMillis();
            int seconds = (int) ((t - startTime) / 1000);
            if (seconds >= 60) {
                endTime = t;
                recordCompleted(startTime > 0);
            } else {
                if (seconds < 10) {
                    recordingHintView.setTimeText("00:0" + seconds);
                } else {
                    recordingHintView.setTimeText("00:" + seconds);
                }
            }
            if (recordingHintView.getRecording()) {
                recordHandler.sendMessageDelayed(recordHandler.obtainMessage(), 1000);
            }
        }
    };

    public RecordingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textView = (TextView) getChildAt(0);
    }

    public void setOnRecordCompletedListener(OnRecordCompletedListener onRecordCompletedListener) {
        this.onRecordCompletedListener = onRecordCompletedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private void inintHintView() {
        if (recordingHintView == null) {
            View view = ViewStubCompat.inflate(getContext(), R.layout.layout_chat_recording_panel, (ViewGroup) getParent().getParent().getParent());
            recordingHintView = (RecordingHintView) view.findViewById(R.id.recordingHintView);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                inintHintView();

                if (isTouchInside(event)) {

                    recordingHintView.setRecording(true);
                    recordHandler.sendMessage(recordHandler.obtainMessage());
                    setSelected(true);
                    textView.setText(R.string.label_chat_soundrecord_send);
                    recordingHintView.setHintText(R.string.label_chat_soundcancle);
                    try {

                        recordingHintView.setVisibility(View.VISIBLE);
                        mediaRecorder = new MediaRecorder();
                        voiceFile = new File(LvxinApplication.CACHE_DIR_VOICE, StringUtils.getUUID());
                        voiceFile.createNewFile();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mediaRecorder.setOutputFile(voiceFile.getAbsolutePath());
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                        startTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (recordingHintView.getRecording()) {
                    if (!isTouchInside(event)) {
                        recordingHintView.setTouchOutSide(true);
                        textView.setText(R.string.label_chat_soundrecord_abort);
                        setSelected(false);
                    } else {
                        recordingHintView.setTouchOutSide(false);
                        setSelected(true);
                        textView.setText(R.string.label_chat_soundrecord_send);
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                endRecordHandle(event);
                break;
            case MotionEvent.ACTION_UP:
                endRecordHandle(event);
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    private void endRecordHandle(MotionEvent event) {
        endTime = System.currentTimeMillis();
        recordCompleted(isTouchInside(event) && (endTime - startTime) / 1000 > 0 && startTime > 0);
    }

    private void recordCompleted(boolean isValid) {
        if (recordingHintView.getRecording()) {

            try {
                mediaRecorder.stop();
                mediaRecorder.release();
            } catch (Exception e) {
            }
            setSelected(false);
            recordingHintView.setVisibility(View.GONE);
            textView.setText(R.string.label_chat_soundrecord_normal);
            recordingHintView.setTimeText("00:00");
            if (isValid) {
                // 消息内容为 语音时长
                ChatVoice chatVoice = new ChatVoice();
                chatVoice.length = (endTime - startTime) / 1000;
                chatVoice.key = voiceFile.getName();
                onRecordCompletedListener.onRecordCompleted(chatVoice);
            } else {
                FileUtils.deleteQuietly(voiceFile);
            }
        }

        recordingHintView.setRecording(false);


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recordHandler.removeCallbacksAndMessages(null);
    }

    public boolean isTouchInside(MotionEvent event) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        return event.getRawY() >= loc[1] && event.getRawY() <= loc[1] + getHeight()
                && event.getRawX() >= loc[0] && event.getRawX() <= loc[0] + getWidth();
    }


    public interface OnRecordCompletedListener {
        void onRecordCompleted(ChatVoice voice);
    }


}
