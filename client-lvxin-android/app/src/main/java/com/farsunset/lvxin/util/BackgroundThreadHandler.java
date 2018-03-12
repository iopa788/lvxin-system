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
package com.farsunset.lvxin.util;
/**
 * probject:lvxin
 *
 * @version 5.0
 * @author 3979434@qq.com
 */

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

public class BackgroundThreadHandler {
    private static final String TAG = BackgroundThreadHandler.class.getName();

    private static final HandlerThread HANDLR_THREAD = new HandlerThread(TAG, Process.THREAD_PRIORITY_BACKGROUND);
    private static final Handler WORKER_HANDLER;
    private static final Handler UI_WORKER_HANDLER = new Handler(Looper.getMainLooper());

    static {
        HANDLR_THREAD.start();
        WORKER_HANDLER = new Handler(HANDLR_THREAD.getLooper());
    }

    private BackgroundThreadHandler() {
    }

    public static void post(Runnable runnable) {
        postRunnable(null, runnable, 0);
    }

    public static void postDelayed(Runnable runnable, long delay) {
        postRunnable(null, runnable, delay);
    }

    public static void removeCallback(Runnable runnable) {
        WORKER_HANDLER.removeCallbacks(runnable);
    }

    public static void removeMainCallback(Runnable runnable) {
        UI_WORKER_HANDLER.removeCallbacks(runnable);
    }

    public static void postUIThread(Runnable runnable) {
        postRunnable(UI_WORKER_HANDLER, runnable, 0);
    }


    public static void postUIThreadDelayed(Runnable runnable, long delay) {
        postRunnable(UI_WORKER_HANDLER, runnable, delay);
    }


    public static void postRunnable(Handler threadHandler, Runnable runnable, long delay) {
        Handler handler = (null == threadHandler) ? WORKER_HANDLER : threadHandler;
        handler.removeCallbacks(runnable);
        if (delay > 0) {
            handler.postDelayed(runnable, delay);
        } else {
            handler.post(runnable);
        }
    }

    public static void postUIThreadClear(Runnable runnable) {
        UI_WORKER_HANDLER.removeCallbacksAndMessages(runnable);
        UI_WORKER_HANDLER.post(runnable);
    }

}
