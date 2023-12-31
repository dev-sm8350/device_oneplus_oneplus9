/*
 * Copyright (C) 2023 The Nameless-AOSP Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.nameless.device.OnePlusSettings.nrmode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast

import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceFragmentCompat

import org.nameless.device.OnePlusSettings.R
import org.nameless.device.OnePlusSettings.nrmode.radio.OplusRadioWrapper.setNrMode
import org.nameless.device.OnePlusSettings.nrmode.util.Constants.INTENT_SIM_STATE_CHANGED_CUSTOM
import org.nameless.device.OnePlusSettings.nrmode.util.Constants.KEY_NR_MODE_SIM_1
import org.nameless.device.OnePlusSettings.nrmode.util.Constants.KEY_NR_MODE_SIM_2
import org.nameless.device.OnePlusSettings.nrmode.util.Constants.SIM_CARD_1
import org.nameless.device.OnePlusSettings.nrmode.util.Constants.SIM_CARD_2
import org.nameless.device.OnePlusSettings.nrmode.util.SettingsHelper.getUserPreferredNrMode
import org.nameless.device.OnePlusSettings.nrmode.util.SettingsHelper.setUserPreferredNrMode
import org.nameless.device.OnePlusSettings.nrmode.util.SimStateHelper.isSimCardAvailable

class NrModeSettingsFragment : PreferenceFragmentCompat(),
        OnPreferenceChangeListener {

    private lateinit var preferredModeSim1: ListPreference
    private lateinit var preferredModeSim2: ListPreference

    private var simStateChangeListening = false

    private val simStateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updatePreferenceState()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.nrmode_settings, rootKey)

        preferredModeSim1 = findPreference(KEY_NR_MODE_SIM_1)!!
        preferredModeSim1.setValue(getUserPreferredNrMode(requireContext(), SIM_CARD_1).toString())
        preferredModeSim1.setOnPreferenceChangeListener(this)

        preferredModeSim2 = findPreference(KEY_NR_MODE_SIM_2)!!
        preferredModeSim2.setValue(getUserPreferredNrMode(requireContext(), SIM_CARD_2).toString())
        preferredModeSim2.setOnPreferenceChangeListener(this)
        
        registerSimStateChangeListener(true)
        updatePreferenceState()
    }
    
    override fun onDestroy() {
        registerSimStateChangeListener(false)
        super.onDestroy()
    }

    override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
        if (preference == preferredModeSim1 && newValue is String) {
            newValue.toInt().let {
                if (setNrMode(SIM_CARD_1, it)) {
                    setUserPreferredNrMode(requireContext(), SIM_CARD_1, it)
                    preferredModeSim1.setSummary(
                        preferredModeSim1.getEntries()[
                            preferredModeSim1.findIndexOfValue(it.toString())])
                    return true
                } else {
                    Toast.makeText(requireContext(), R.string.error_fail_to_set, Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        } else if (preference == preferredModeSim2 && newValue is String) {
            newValue.toInt().let {
                if (setNrMode(SIM_CARD_2, it)) {
                    setUserPreferredNrMode(requireContext(), SIM_CARD_2, it)
                    preferredModeSim2.setSummary(
                        preferredModeSim2.getEntries()[
                            preferredModeSim2.findIndexOfValue(it.toString())])
                    return true
                } else {
                    Toast.makeText(requireContext(), R.string.error_fail_to_set, Toast.LENGTH_SHORT).show()
                    return false
                }
            }
        }
        return false
    }

    private fun updatePreferenceState() {
        isSimCardAvailable(SIM_CARD_1).let {
            preferredModeSim1.setEnabled(it)
            preferredModeSim1.setSummary(if (it) {
                preferredModeSim1.getEntry()
            } else {
                getString(R.string.error_sim_card_unavailable)
            })
        }
        isSimCardAvailable(SIM_CARD_2).let {
            preferredModeSim2.setEnabled(it)
            preferredModeSim2.setSummary(if (it) {
                preferredModeSim2.getEntry()
            } else {
                getString(R.string.error_sim_card_unavailable)
            })
        }
    }

    private fun registerSimStateChangeListener(listening: Boolean) {
        if (simStateChangeListening == listening) {
            return
        }
        if (listening) {
            requireContext().registerReceiver(simStateChangeReceiver, IntentFilter(INTENT_SIM_STATE_CHANGED_CUSTOM))
        } else {
            requireContext().unregisterReceiver(simStateChangeReceiver)
        }
        simStateChangeListening = listening
    }
}
