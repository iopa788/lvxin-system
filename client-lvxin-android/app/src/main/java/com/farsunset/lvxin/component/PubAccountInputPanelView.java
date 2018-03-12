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
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.farsunset.lvxin.app.LvxinApplication;
import com.farsunset.lvxin.comparator.PubMenuAscComparator;
import com.farsunset.lvxin.dialog.PubAccountMenuWindow;
import com.farsunset.lvxin.dialog.PubAccountMenuWindow.OnMenuClickListener;
import com.farsunset.lvxin.listener.OnInputPanelEventListener;
import com.farsunset.lvxin.listener.OnItemClickedListener;
import com.farsunset.lvxin.model.PublicMenu;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.KeyboardUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PubAccountInputPanelView extends FrameLayout implements OnClickListener, OnItemClickedListener, TextWatcher, OnMenuClickListener, InputAreaPanelView.KeyboardStateObserver {
    private List<PublicMenu> menuList;
    private View chatEmotionButton;
    private EditText messageEditText;
    private EmoticonPanelView emoticoPanelView;
    private OnInputPanelEventListener onInputPanelEventListener;
    private InputMethodManager inputMethodManager;
    private LinearLayout menuBarView;
    private PubAccountMenuWindow menuWindow;
    private OnMenuClickListener OnMenuClickListener;
    private View menuView;
    private View inputView;
    private InputAreaPanelView inputAreaPanelView;

    public PubAccountInputPanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        menuWindow = new PubAccountMenuWindow(context, this);
    }


    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        OnMenuClickListener = onMenuClickListener;
    }


    public void buildMenus(List<PublicMenu> list) {
        this.menuList = list;
        int i = 0;
        List<PublicMenu> rootList = getRootMenuList();
        for (PublicMenu menu : rootList) {
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.layout_pub_root_menu, null);
            ((TextView) itemView.findViewById(R.id.menu_name)).setText(menu.name);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1));
            if (!menu.hasSubMenu()) {
                itemView.findViewById(R.id.root_menu_mark).setVisibility(View.INVISIBLE);
            }
            itemView.setTag(menu);
            itemView.setTag(R.drawable.icon, i);
            itemView.setOnClickListener(this);
            menuBarView.addView(itemView);

            i++;

            if (i < rootList.size()) {
                View divider = new View(getContext());
                divider.setLayoutParams(new LinearLayout.LayoutParams(1, LayoutParams.MATCH_PARENT));
                divider.setBackgroundResource(R.color.list_border);
                menuBarView.addView(divider);
            }
        }
    }

    public List<PublicMenu> getSubMenuList(String pid) {

        List<PublicMenu> list = new ArrayList<PublicMenu>();
        for (PublicMenu menu : menuList) {
            if (pid.equals(menu.fid)) {
                list.add(menu);
            }
        }
        Collections.sort(list, new PubMenuAscComparator());
        return list;
    }


    public List<PublicMenu> getRootMenuList() {

        List<PublicMenu> list = new ArrayList<PublicMenu>();
        for (PublicMenu menu : menuList) {
            if (menu.isRootMenu()) {
                list.add(menu);
            }
        }

        Collections.sort(list, new PubMenuAscComparator());
        return list;
    }


    public void show() {
        messageEditText.requestFocus();
        this.setVisibility(View.VISIBLE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        menuBarView = (LinearLayout) findViewById(R.id.menuBarView);
        messageEditText = (EditText) this.findViewById(R.id.messageEditText);
        emoticoPanelView = (EmoticonPanelView) this.findViewById(R.id.emoticoPanelView);
        emoticoPanelView.setOnEmotionSelectedListener(this);
        messageEditText.addTextChangedListener(this);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        chatEmotionButton = findViewById(R.id.chat_emotion);
        chatEmotionButton.setOnClickListener(this);
        findViewById(R.id.keyboardSwitchButton).setOnClickListener(this);
        findViewById(R.id.sendMessageButton).setOnClickListener(this);
        findViewById(R.id.menuSwitchButton).setOnClickListener(this);
        menuView = findViewById(R.id.menuView);
        inputView = findViewById(R.id.keyboardView);
        inputAreaPanelView = (InputAreaPanelView) findViewById(android.R.id.inputArea);
        inputAreaPanelView.setKeyboardStateObserver(this);
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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.chat_emotion:
                onEmoticonClicked();
                break;
            case R.id.sendMessageButton:
                if (!TextUtils.isEmpty(messageEditText.getText())) {
                    onInputPanelEventListener.onSendButtonClicked(messageEditText.getText().toString());
                    ((EditText) findViewById(R.id.messageEditText)).getText().clear();
                }
                break;

            case R.id.root_menu:
                if (((PublicMenu) view.getTag()).hasSubMenu()) {
                    menuWindow.showAtLocation(view, getSubMenuList(((PublicMenu) view.getTag()).gid));
                } else {
                    OnMenuClickListener.onMenuClicked(((PublicMenu) view.getTag()));
                }
                break;
            case R.id.keyboardSwitchButton:
                toggleKeyboradMode();
                break;

            case R.id.menuSwitchButton:
                toggleMenuMode();
                break;
        }

    }

    private void toggleKeyboradMode() {
        Animation apperAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.menu_slide_in_from_bottom);
        apperAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PubAccountInputPanelView.this.clearAnimation();
            }
        });
        inputView.setVisibility(View.VISIBLE);
        this.startAnimation(apperAnimation);
        menuView.setVisibility(View.GONE);
        messageEditText.requestFocus();
    }

    private void toggleMenuMode() {
        Animation menuAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.menu_slide_in_from_bottom);
        menuAnimation.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PubAccountInputPanelView.this.clearAnimation();
            }
        });
        chatEmotionButton.setSelected(false);
        inputAreaPanelView.setVisibility(View.GONE);
        menuView.setVisibility(View.VISIBLE);
        this.startAnimation(menuAnimation);
        inputView.setVisibility(View.GONE);
        KeyboardUtil.hideKeyboard(messageEditText);
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
        SpannableString ss = new SpannableString(key);

        int id = LvxinApplication.EMOTION_MAP.get(key);

        Drawable drawable = ContextCompat.getDrawable(getContext(), id);
        if (drawable != null) {
            int size = (int) (0.5F + this.getResources().getDisplayMetrics().density * 20);
            drawable.setBounds(0, 0, size, size);
            ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
            ss.setSpan(span, 0, key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //追加到editText
            messageEditText.getEditableText().insert(messageEditText.getSelectionStart(), ss);
        }
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
        KeyboardUtil.hideKeyboard(messageEditText);
        inputAreaPanelView.setVisibility(GONE);
        chatEmotionButton.setSelected(false);
    }


    @Override
    public void onMenuClicked(PublicMenu menu) {
        OnMenuClickListener.onMenuClicked(menu);
    }

    public void setOnInputPanelEventListener(OnInputPanelEventListener onInputPanelEventListener) {
        this.onInputPanelEventListener = onInputPanelEventListener;
    }
}
