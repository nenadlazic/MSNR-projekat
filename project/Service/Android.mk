LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_PACKAGE_NAME := Service
LOCAL_MODULE_TAGS := optional
LOCAL_DEX_PREOPT := false

LOCAL_SRC_FILES += src/matf/msnr/netbroadcast/BootReceiver.java
LOCAL_SRC_FILES += src/matf/msnr/netbroadcast/BroadcastService.java
LOCAL_SRC_FILES += src/matf/msnr/netbroadcast/IIntentBroadcast.aidl
LOCAL_SRC_FILES += src/matf/msnr/netbroadcast/MainActivity.java

include $(BUILD_PACKAGE)
