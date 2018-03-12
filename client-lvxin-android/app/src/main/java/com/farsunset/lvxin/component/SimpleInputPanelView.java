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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.listener.OnInputPanelEventListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.KeyboardUtil;

import java.util.regex.Matcher;

public class SimpleInputPanelView extends LinearLayout implements OnClickListener, OnItemClickedListener, TextWatcher, InputAreaPanelView.KeyboardStateObserver {
    private static final int EMOTION_SIZE = 22;
    protected InputAreaPanelView inputAreaPanelView;
    protected View chatEmotionButton;
    protected View sendButton;
    protected EditText messageEditText;
    protected EmoticonPanelView emoticoPanelView;
    protected OnInputPanelEventListener onInputPanelEventListener;
    private int height;

    public SimpleInputPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        height = (int) (Resources.getSystem().getDisplayMetrics().density * 50 + 0.5f);
    }

    public int getFullHeight() {

        return height + KeyboardUtil.getKeyboardHeight(getContext());
    }

    public void show() {
        messageEditText.requestFocus();
        this.setVisibility(View.VISIBLE);
        KeyboardUtil.showKeyboard(messageEditText);
    }

    public void hide() {
        messageEditText.setHint(null);
        messageEditText.setText(null);
        chatEmotionButton.setSelected(false);
        KeyboardUtil.hideKeyboard(messageEditText);
        setVisibility(View.GONE);
    }

    public void onEmoticonClicked() {
        if (chatEmotionButton.isSelected()) {
            chatEmotionButton.setSelected(false);
            KeyboardUtil.showKeyboard(messageEditText);
        } else {
            chatEmotionButton.setSelected(true);
            inputAreaPanelView.setVisibility(View.VISIBLE);
            KeyboardUtil.hideKeyboard(messageEditText);
        }
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        sendButton = findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);
        messageEditText = (EditText) this.findViewById(R.id.messageEditText);
        emoticoPanelView = (EmoticonPanelView) this.findViewById(R.id.emoticoPanelView);
        emoticoPanelView.setOnEmotionSelectedListener(this);
        messageEditText.addTextChangedListener(this);
        chatEmotionButton = findViewById(R.id.chat_emotion);
        chatEmotionButton.setOnClickListener(this);
        inputAreaPanelView = (InputAreaPanelView) findViewById(android.R.id.inputArea);
        inputAreaPanelView.setKeyboardStateObserver(this);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.chat_emotion:
                onEmoticonClicked();
                break;
            case R.id.sendMessageButton:
                if (!TextUtils.isEmpty(messageEditText.getText())) {
                    onInputPanelEventListener.onSendButtonClicked(messageEditText.getText().toString());
                }
                break;

        }

    }

    private void appendImageSpan(String key) {

        SpannableString ss = new SpannableString(key);
        int id = LvxinApplication.EMOTION_MAP.get(key);

        Drawable drawable = ContextCompat.getDrawable(getContext(), id);
        if (drawable != null) {
            int size = (int) (0.5F + this.getResources().getDisplayMetrics().density * EMOTION_SIZE);
            drawable.setBounds(0, 0, size, size);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            ss.setSpan(span, 0, key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //追加到editText
            messageEditText.getEditableText().insert(messageEditText.getSelectionStart(), ss);
        }

    }

    public void clearText() {
        messageEditText.getText().clear();
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        String key = obj.toString();
        messageEditText.setCursorVisible(true);
        if ("DELETE".equals(key)) {
            KeyEvent keyEventDown = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
            messageEditText.onKeyDown(KeyEvent.KEYCODE_DEL, keyEventDown);
            return;
        }

        appendImageSpan(key);
    }


    public void setHint(String hint) {
        messageEditText.setHint(hint);
    }

    public void setContent(String text) {
        messageEditText.setText(text);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
    }

    @Override
    public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
    }


    @Override
    public void onKeyboardHeightChanged(int height) {

        emoticoPanelView.setHeight(height);
    }

    @Override
    public void onKeyboardVisableChanged(boolean visable) {
        if (visable) {
            inputAreaPanelView.setVisibility(View.GONE);
            chatEmotionButton.setSelected(false);
        }
    }


    public void resetInputPanel() {
        inputAreaPanelView.setVisibility(View.GONE);
        chatEmotionButton.setSelected(false);
        KeyboardUtil.hideKeyboard(messageEditText);
        messageEditText.setText(null);
    }

    public String getInputText() {
        return messageEditText.getText().toString().trim();
    }

    public void setInputText(String text) {
        if (!TextUtils.isEmpty(text)) {
            messageEditText.setText(matchEmoticon(text));
        }
    }

    public SpannableStringBuilder matchEmoticon(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = LvxinApplication.EMOTION_PATTERN.matcher(text);
        while (matcher.find()) {
            Integer id = LvxinApplication.EMOTION_MAP.get(matcher.group());
            if (id != null) {
                Drawable drawable = ContextCompat.getDrawable(getContext(), id);
                int size = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
                drawable.setBounds(0, 0, size, size);
                ImageSpan span = new ImageSpan(drawable);
                builder.setSpan(span, matcher.start(), matcher.end(),  ImageSpan.ALIGN_BASELINE);
            }
        }

        return builder;
    }

    public void setOnInputPanelEventListener(OnInputPanelEventListener onInputPanelEventListener) {
        this.onInputPanelEventListener = onInputPanelEventListener;
    }
}
