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
package com.farsunset.lvxin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.farsunset.lvxin.activity.HomeActivity;
import com.farsunset.lvxin.app.ClientConfig;
import com.farsunset.lvxin.listener.CloudImageLoadListener;
import com.farsunset.lvxin.message.parser.MessageParser;
import com.farsunset.lvxin.message.parser.MessageParserFactory;
import com.farsunset.lvxin.model.Message;
import com.farsunset.lvxin.model.MessageSource;
import com.farsunset.lvxin.network.CloudImageLoaderFactory;
import com.farsunset.lvxin.pro.R;

public class MessageNotifyService extends Service {

    private NotificationManager notificationMgr;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ClientConfig.getMessageNotifyEnable()) {
            Message message = (Message) intent.getSerializableExtra(Message.NAME);
            buildNotification(message);
        }
        return START_NOT_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void buildNotification(Message message) {

        MessageParser messageParser = MessageParserFactory.getFactory().getMessageParser(message.action);
        if (messageParser == null) {
            return;
        }
        final MessageSource source = messageParser.getMessageSource(message);
        if (source == null) {
            return;
        }

        String channelId = source.getIdentityId();
        final int notificationId = channelId.hashCode();
        String content = messageParser.getMessagePreviewText(message);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 1, new Intent(this, HomeActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
        builder.setAutoCancel(true);
        builder.setWhen(message.timestamp);
        builder.setSmallIcon(R.drawable.icon_notify);
        builder.setTicker(content);
        builder.setContentTitle(source.getTitle());
        builder.setContentText(content);
        builder.setContentIntent(contentIntent);
        builder.setPriority(source.getNotificationPriority());
        if (ClientConfig.getMessageSoundEnable())
        {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_SOUND);
        }else {
            builder.setDefaults(NotificationCompat.DEFAULT_LIGHTS );
        }

        if (source.getWebIcon() == null)
        {
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), source.getNotifyIcon()));
            showNotification(notificationId,builder.build());
            return;
        }


        CloudImageLoaderFactory.get().downloadOnly(source.getWebIcon(), new CloudImageLoadListener() {
            @Override
            public void onImageLoadFailure(Object key) {
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), source.getNotifyIcon()));
                showNotification(notificationId,builder.build());
            }

            @Override
            public void onImageLoadSucceed(Object key, Bitmap resource) {
                builder.setLargeIcon(resource);
                showNotification(notificationId,builder.build());
            }
        });
    }

    private void showNotification(int id,Notification notification) {
        notificationMgr.notify(id, notification);
    }

    @Override
    public void onDestroy() {
        if (notificationMgr != null) {
            notificationMgr.cancelAll();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
