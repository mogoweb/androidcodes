/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whtr.example.mtimer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


final class JWebCoreJavaBridge extends Handler {
    // Identifier for the timer message.
    private static final int TIMER_MESSAGE = 1;
    // Log system identifier.
    private static final String LOGTAG = "webkit-timers";
    
    // Native object pointer for interacting in native code.
    private int mNativeBridge;
    // Instant timer is used to implement a timer that needs to fire almost
    // immediately.
    private boolean mHasInstantTimer;

    // Reference count the pause/resume of timers
    private int mPauseTimerRefCount;
    
    private boolean mTimerPaused;
    private boolean mHasDeferredTimers;

    private Context mContext;
    
    /**
     * Construct a new JWebCoreJavaBridge to interface with
     * WebCore timers and cookies.
     */
    public JWebCoreJavaBridge(Context context) {
        mContext = context;
        nativeConstructor();
    }

    @Override
    protected void finalize() {
        nativeFinalize();
    }
    
    /**
     * Call native timer callbacks.
     */
    private void fireSharedTimer() { 
        
        // clear the flag so that sharedTimerFired() can set a new timer
        mHasInstantTimer = false;
        sharedTimerFired();
    }
    
    /**
     * handleMessage
     * @param msg The dispatched message.
     *
     * The only accepted message currently is TIMER_MESSAGE
     */
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case TIMER_MESSAGE: {
                if (mTimerPaused) {
                    mHasDeferredTimers = true;
                } else {
                    fireSharedTimer();
                }
                break;
            }
        }
    }
    
    /**
     * Pause all timers.
     */
    public void pause() {
        if (--mPauseTimerRefCount == 0) {
            mTimerPaused = true;
            mHasDeferredTimers = false;
        }
    }

    /**
     * Resume all timers.
     */
    public void resume() {
        if (++mPauseTimerRefCount == 1) {
           mTimerPaused = false;
           if (mHasDeferredTimers) {
               mHasDeferredTimers = false;
               fireSharedTimer();
           }
        }
    }
    
    /**
     * setSharedTimer
     * @param timemillis The relative time when the timer should fire
     */
    private void setSharedTimer(long timemillis) {
        
        if (timemillis <= 0) {
            // we don't accumulate the sharedTimer unless it is a delayed
            // request. This way we won't flood the message queue with
            // WebKit messages. This should improve the browser's
            // responsiveness to key events.
            if (mHasInstantTimer) {
                return;
            } else {
                mHasInstantTimer = true;
                Message msg = obtainMessage(TIMER_MESSAGE);
                sendMessageDelayed(msg, timemillis);
            }
        } else {
            Message msg = obtainMessage(TIMER_MESSAGE);
            sendMessageDelayed(msg, timemillis);
        }
    }
    
    /**
     * Stop the shared timer.
     */
    private void stopSharedTimer() {
        removeMessages(TIMER_MESSAGE);
        mHasInstantTimer = false;
        mHasDeferredTimers = false;
    }
    
    private native void nativeConstructor();
    private native void nativeFinalize();
    private native void sharedTimerFired();
}