/*
 * Copyright (C) 2023 The Nameless-AOSP Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.nameless.device.OnePlusSettings.nrmode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.UserHandle

import org.nameless.device.OnePlusSettings.nrmode.service.SimCardListenerService
import org.nameless.device.OnePlusSettings.nrmode.util.Constants.logD

class BootCompletedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        logD(TAG, "Start SimCardListenerService")
        context.startServiceAsUser(Intent(context, SimCardListenerService::class.java), UserHandle.SYSTEM)
    }

    companion object {
        private const val TAG = "NrModeSwitcher"
    }
}
