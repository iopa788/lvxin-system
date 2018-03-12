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
package com.farsunset.lvxin.app;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.File;

//声音播放
public class GlobalVoicePlayer implements OnCompletionListener {

    static GlobalVoicePlayer player;
    MediaPlayer mMediaPlayer;
    OnPlayListener onPlayListener;
    String token;

    private GlobalVoicePlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
    }

    public static GlobalVoicePlayer getPlayer() {
        if (player == null) {
            player = new GlobalVoicePlayer();
        }
        return player;
    }

    public void start(File file, OnPlayListener l) {


        if (token != null) {
            if (isPlaying() && file.getAbsolutePath().equals(token)) {
                stop();
                return;
            }
            if (isPlaying() && !file.getAbsolutePath().equals(token) && onPlayListener != null) {
                onPlayListener.onVoiceStop();
            }

        }
        onPlayListener = l;
        token = file.getAbsolutePath();
        start();
    }

    private void start() {
        try {
            if (isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(token);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        if (onPlayListener != null) {
            onPlayListener.onVoiceStop();
            onPlayListener = null;
        }
    }

    private boolean isPlaying() {
        try {
            return mMediaPlayer.isPlaying();
        } catch (Exception e) {
        }

        return false;

    }

    public void onSwitchMode() {
        if (token != null) {
            if (isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            start();
        }

    }

    public void pause() {
        if (token != null) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaplayer) {
        if (onPlayListener != null) {
            onPlayListener.onCompletion(mediaplayer);
        }
        token = null;
    }

    public interface OnPlayListener {

        void onCompletion(MediaPlayer mediaplayer);

        void onVoiceStop();
    }

}
