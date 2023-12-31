/*
 * Copyright (C) 2022 The Nameless-AOSP Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.nameless.device.OnePlusSettings.Utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.UserHandle;

import androidx.preference.Preference;

import java.util.ArrayList;

import org.nameless.device.OnePlusSettings.ModeSwitch;
import org.nameless.device.OnePlusSettings.ModeSwitch.Node;
import org.nameless.device.OnePlusSettings.Services.HBMModeService;

public class SwitchUtils {

    public static final String FILE_HBM = "/sys/kernel/oplus_display/hbm";
    public static final String FILE_GAME = "/proc/touchpanel/game_switch_enable";
    public static final String FILE_EDGE = "/proc/touchpanel/oplus_tp_direction";

    public static ModeSwitch getHBMModeSwitch(Context context) {
        return getHBMModeSwitch(context, null);
    }

    public static ModeSwitch getHBMModeSwitch(Context context, Preference preference) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(FILE_HBM, "1", "0", false));
        return new ModeSwitch(context, preference, nodes) {
            @Override
            public void extCommand(Context context, boolean enabled) {
                Intent hbmIntent = new Intent(context, HBMModeService.class);
                if (enabled) {
                    context.startServiceAsUser(hbmIntent, UserHandle.CURRENT);
                } else {
                    context.stopServiceAsUser(hbmIntent, UserHandle.CURRENT);
                }
            }
        };
    }

    public static ModeSwitch getGameModeSwitch(Context context) {
        return getGameModeSwitch(context, null);
    }

    public static ModeSwitch getGameModeSwitch(Context context, Preference preference) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(FILE_GAME, "1", "0", false));
        return new ModeSwitch(context, preference, nodes);
    }

    public static ModeSwitch getEdgeModeSwitch(Context context) {
        return getEdgeModeSwitch(context, null);
    }

    public static ModeSwitch getEdgeModeSwitch(Context context, Preference preference) {
        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(FILE_EDGE, "1", "0", false));
        return new ModeSwitch(context, preference, nodes);
    }

    public static boolean isEdgeTouchSupported() {
        return !Build.DEVICE.equals("lemonadep");
    }
}
