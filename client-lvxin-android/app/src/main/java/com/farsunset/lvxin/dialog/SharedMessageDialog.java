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
package com.farsunset.lvxin.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.farsunset.lvxin.app.Constant;
import com.farsunset.lvxin.component.EmoticonTextView;
import com.farsunset.lvxin.component.HorizontalPaddingDecoration;
import com.farsunset.lvxin.component.WebImageView;
import com.farsunset.lvxin.listener.OnDialogButtonClickListener;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.model.ChatFile;
import com.farsunset.lvxin.network.model.ChatMap;
import com.farsunset.lvxin.network.model.ChatVoice;
import com.farsunset.lvxin.network.model.SNSImage;
import com.farsunset.lvxin.network.model.SNSVideo;
import com.farsunset.lvxin.pro.R;
import com.farsunset.lvxin.util.FileURLBuilder;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.util.List;

public class SharedMessageDialog extends Dialog implements View.OnClickListener {
    private List<MessageSource> receiverList = new ArrayList<>();
    private EmoticonTextView text;
    private TextView multipleText;
    private TextView name;
    private WebImageView icon;
    private WebImageView image;
    private RecyclerView recyclerView;
    private OnDialogButtonClickListener onDialogButtonClickListener;
    private CardView videoView;
    private WebImageView videoThumbnail;

    public SharedMessageDialog(Context context) {

        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_shared_message);
        text = (EmoticonTextView) findViewById(R.id.text);
        text.setFaceSize(20);
        name = (TextView) findViewById(R.id.name);
        videoView = (CardView) findViewById(R.id.videoView);

        multipleText = (TextView) findViewById(R.id.multipleText);
        icon = (WebImageView) findViewById(R.id.icon);
        image = (WebImageView) findViewById(R.id.image);
        videoThumbnail = (WebImageView) findViewById(R.id.thumbnail);

        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.multi);
        int padding = (int) Resources.getSystem().getDisplayMetrics().density * 5;

        recyclerView.addItemDecoration(new HorizontalPaddingDecoration(padding));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new ReceiverAdapter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams p = getWindow().getAttributes();
        p.width = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels) * 0.9f);
        getWindow().setAttributes(p);
    }
    public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }


    public void setMessage(Message message) {

        if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
            text.setVisibility(View.VISIBLE);
            text.setText(message.content);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VOICE)) {
            multipleText.setVisibility(View.VISIBLE);
            ChatVoice chatVoice = new Gson().fromJson(message.content, ChatVoice.class);
            multipleText.setText(getContext().getString(R.string.title_shared_voice_message, chatVoice.length + "\""));
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_FILE)) {
            multipleText.setVisibility(View.VISIBLE);
            ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);
            multipleText.setText(getContext().getString(R.string.title_shared_file_message, FileUtils.byteCountToDisplaySize(chatFile.size), chatFile.name));
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_IMAGE)) {
            image.setVisibility(View.VISIBLE);
            SNSImage chatImage = new Gson().fromJson(message.content, SNSImage.class);
            Uri uri = Uri.parse(chatImage.image);
            image.load(uri.isRelative() ? FileURLBuilder.getFileUrl(chatImage.image) : uri.toString(), 0);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            videoView.setVisibility(View.VISIBLE);
            SNSVideo video = new Gson().fromJson(message.content, SNSVideo.class);
            initVidwoView(video);
        }
        if (message.format.equals(Constant.MessageFormat.FORMAT_MAP)) {
            image.setVisibility(View.VISIBLE);
            ChatMap chatMap = new Gson().fromJson(message.content, ChatMap.class);
            image.load(FileURLBuilder.getFileUrl(chatMap.key));
        }
    }

    public void initVidwoView(SNSVideo video) {

        if (video.mode == SNSVideo.HORIZONTAL) {
            videoThumbnail.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_height);
            videoThumbnail.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_width);
        } else {
            videoThumbnail.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_width);
            videoThumbnail.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.preview_video_height);
        }
        videoThumbnail.requestLayout();

        videoThumbnail.load(FileURLBuilder.getFileUrl(video.thumbnail), R.drawable.def_chat_video_background);
    }


    public void show(MessageSource source) {
        recyclerView.setVisibility(View.GONE);
        findViewById(R.id.single).setVisibility(View.VISIBLE);
        name.setText(source.getName());
        icon.load(source.getWebIcon(), R.drawable.icon_def_head);
        super.show();
    }

    public void show(List<MessageSource> selectedList) {
        if (selectedList.size() == 1) {
            show(selectedList.get(0));
            return;
        }
        recyclerView.setVisibility(View.VISIBLE);
        findViewById(R.id.single).setVisibility(View.GONE);
        receiverList.clear();
        receiverList.addAll(selectedList);
        recyclerView.getAdapter().notifyDataSetChanged();
        super.show();
    }

    @Override
    public void onClick(View view) {
        if (R.id.leftButton == view.getId()) {
            dismiss();
        }
        if (R.id.rightButton == view.getId()) {
            onDialogButtonClickListener.onRightButtonClicked();
        }
    }


    private class ReceiverAdapter extends RecyclerView.Adapter<IconHolder> {

        @Override
        public IconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new IconHolder(View.inflate(getContext(), R.layout.item_icon_name, null));
        }

        @Override
        public void onBindViewHolder(IconHolder holder, int position) {
            MessageSource source = receiverList.get(position);
            holder.icon.load(source.getWebIcon(), source.getDefaultIconRID());
            holder.name.setText(source.getName());
        }

        @Override
        public int getItemCount() {
            return receiverList.size();
        }
    }


    private class IconHolder extends RecyclerView.ViewHolder {
        public WebImageView icon;
        public TextView name;

        public IconHolder(View itemView) {
            super(itemView);
            icon = (WebImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
