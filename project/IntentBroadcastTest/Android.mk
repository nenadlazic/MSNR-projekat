LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := IntentBroadcastTest
LOCAL_MODULE_TAGS := optional
LOCAL_DEX_PREOPT := false

LOCAL_SRC_FILES += src/matf/msnr/intenttest/IntentBroadcastTestActivity.java
LOCAL_SRC_FILES += src/matf/msnr/netbroadcast/IIntentBroadcast.aidl

include $(BUILD_PACKAGE)
