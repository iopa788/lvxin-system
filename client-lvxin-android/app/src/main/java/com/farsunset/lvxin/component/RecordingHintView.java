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
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.AppTools;


public class RecordingHintView extends RelativeLayout {

    private TextView recordingTime;
    private TextView recordingHint;
    private RecordingColorView colorView;
    private boolean recording = true;

    public RecordingHintView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setHintText(int resId) {
        recordingHint.setText(resId);
    }

    public void setTimeText(String txt) {
        recordingTime.setText(txt);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        recordingHint = (TextView) findViewById(R.id.recordingHint);
        recordingTime = (TextView) findViewById(R.id.recordingTime);
        AppTools.measureView(recordingHint);
        AppTools.measureView(recordingTime);
        colorView = (RecordingColorView) findViewById(R.id.recordingColorView);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recordingTime.getLayoutParams();
        params.setMargins(0, (int) ((colorView.getRealHeight() - recordingTime.getMeasuredHeight()) / 2.5), 0, 0);
        recordingTime.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) recordingHint.getLayoutParams();
        params.setMargins(0, colorView.getRealHeight() - recordingHint.getMeasuredHeight() * 2, 0, 0);
        recordingHint.setLayoutParams(params);

    }

    public boolean getRecording() {
        return recording;

    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == GONE) {
            colorView.setTouchOutSide(false);
        }
    }

    public void setTouchOutSide(boolean outSide) {
        if (outSide) {
            setHintText(R.string.label_chat_unlashcancle);
        } else {
            setHintText(R.string.label_chat_soundcancle);
        }
        colorView.setTouchOutSide(outSide);
    }
}
