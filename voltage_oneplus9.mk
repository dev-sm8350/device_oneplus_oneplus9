#
# Copyright (C) 2021-2023 The VoltageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

ifeq (voltage_oneplus9,$(TARGET_PRODUCT))

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit_only.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_base_telephony.mk)

# Inherit from the custom device configuration.
$(call inherit-product, device/oneplus/oneplus9/device.mk)

# Inherit some common Voltage stuff.
$(call inherit-product, vendor/voltage/config/common_full_phone.mk)

PRODUCT_BRAND := OnePlus
PRODUCT_DEVICE := oneplus9
PRODUCT_MANUFACTURER := OnePlus
PRODUCT_MODEL := LE2115
PRODUCT_NAME := voltage_oneplus9

PRODUCT_SYSTEM_NAME := OnePlus9
PRODUCT_SYSTEM_DEVICE := OnePlus9

PRODUCT_BUILD_PROP_OVERRIDES += \
    TARGET_DEVICE=$(PRODUCT_SYSTEM_DEVICE) \
    TARGET_PRODUCT=$(PRODUCT_SYSTEM_NAME)

PRODUCT_GMS_CLIENTID_BASE := android-oneplus

# Boot animation resolution.
TARGET_BOOT_ANIMATION_RES := 1080

# VoltageOS Stuffs
VOLTAGE_BUILD_TYPE := OFFICIAL

endif
