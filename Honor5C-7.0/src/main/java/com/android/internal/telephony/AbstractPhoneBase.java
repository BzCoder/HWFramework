package com.android.internal.telephony;

import android.common.HwFrameworkFactory;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public abstract class AbstractPhoneBase extends Handler implements PhoneInternalInterface {
    public static final int BUFFER_SIZE = 120;
    public static final int DEVICE_ID_MASK_ALL = 2;
    public static final int DEVICE_ID_MASK_IMEI = 1;
    private static final int DEVICE_ID_RETRY_COUNT = 5;
    private static final int DEVICE_ID_RETRY_INTERVAL = 6000;
    public static final int EVENT_ECC_NUM = 104;
    protected static final int EVENT_GET_CALL_FORWARD_TIMER_DONE = 110;
    public static final int EVENT_GET_IMSI_DONE = 105;
    public static final int EVENT_GET_LTE_RELEASE_VERSION_DONE = 108;
    public static final int EVENT_HW_CUST_BASE = 100;
    public static final int EVENT_RETRY_GET_DEVICE_ID = 1000;
    protected static final int EVENT_SET_CALL_FORWARD_TIMER_DONE = 109;
    public static final int EVENT_SET_MODE_TO_AUTO = 111;
    private static final String HOEMHOOK = "HOEMHOOK";
    public static final int HW_ENCRYPT_CALL = 0;
    public static final int HW_KMC_REMOTE_COMMUNICATION = 1;
    private static final int INT_LENGTH = 4;
    private static final boolean IS_CHINA_TELECOM = false;
    private static final boolean IS_RESTORE_AUTO = false;
    private static final String LOG_TAG = "HwPhoneBase";
    public static final int LTE_RELEASE_VERSION_R10 = 1;
    public static final int LTE_RELEASE_VERSION_R9 = 0;
    public static final int LTE_RELEASE_VERSION_UNKNOWN = -1;
    private static final String MUTIL_SIM_CONFIGURATION_DSDA = "dsda";
    private static final String MUTIL_SIM_CONFIGURATION_DSDS = "dsds";
    private static final String MUTIL_SIM_CONFIGURATION_UNKNOW = "unknow";
    public static final int SET_TO_AOTO_TIME = 5000;
    public static final int SPEECH_INFO_CODEC_NB = 1;
    public static final int SPEECH_INFO_CODEC_WB = 2;
    private CommandsInterface mAbstractPhoneBaseCi;
    private boolean mIsAdaptMultiSimConfiguration;
    private boolean mNeedShowOOS;
    private HwPhoneBaseReference mReference;
    private int speechInfoCodec;

    public interface HwPhoneBaseReference {
        int getDataRoamingScope();

        boolean setDataRoamingScope(int i);
    }

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: com.android.internal.telephony.AbstractPhoneBase.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: com.android.internal.telephony.AbstractPhoneBase.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 5 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 6 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.AbstractPhoneBase.<clinit>():void");
    }

    public abstract int getPhoneId();

    protected AbstractPhoneBase() {
        this.speechInfoCodec = SPEECH_INFO_CODEC_NB;
        this.mReference = HwTelephonyFactory.getHwPhoneManager().createHwPhoneBaseReference(this);
        this.mNeedShowOOS = IS_RESTORE_AUTO;
        this.mIsAdaptMultiSimConfiguration = SystemProperties.getBoolean("ro.config.multi_sim_cfg_adapt", IS_RESTORE_AUTO);
    }

    protected AbstractPhoneBase(CommandsInterface ci) {
        this.speechInfoCodec = SPEECH_INFO_CODEC_NB;
        this.mReference = HwTelephonyFactory.getHwPhoneManager().createHwPhoneBaseReference(this);
        this.mNeedShowOOS = IS_RESTORE_AUTO;
        this.mIsAdaptMultiSimConfiguration = SystemProperties.getBoolean("ro.config.multi_sim_cfg_adapt", IS_RESTORE_AUTO);
        this.mAbstractPhoneBaseCi = ci;
    }

    public boolean isSupportCFT() {
        Rlog.d(LOG_TAG, "isSupportCFT should be override by subclass");
        return IS_RESTORE_AUTO;
    }

    public void setCallForwardingUncondTimerOption(int startHour, int startMinute, int endHour, int endMinute, int commandInterfaceCFAction, int commandInterfaceCFReason, String dialingNumber, Message onComplete) {
        Rlog.d(LOG_TAG, "setCallForwardingUncondTimerOption should be override by subclass");
    }

    public void setImsSwitch(boolean on) {
        Rlog.d(LOG_TAG, "setImsSwitch should be override by subclass");
    }

    public boolean getImsSwitch() {
        Rlog.d(LOG_TAG, "getImsSwitch should be override by subclass");
        return IS_RESTORE_AUTO;
    }

    public void retryGetDeviceId(int curRetryCount, int deviceIdMask) {
        int count = curRetryCount + SPEECH_INFO_CODEC_NB;
        if (count <= DEVICE_ID_RETRY_COUNT) {
            Rlog.i(LOG_TAG, "retryGetDeviceId:" + count + " try after " + DEVICE_ID_RETRY_INTERVAL + "ms");
            sendMessageDelayed(obtainMessage(EVENT_RETRY_GET_DEVICE_ID, count, deviceIdMask, null), 6000);
        }
    }

    public String getMeid() {
        Rlog.d(LOG_TAG, "[HwPhoneBase] getMeid() is a CDMA method");
        return "Meid";
    }

    public String getNVESN() {
        return this.mAbstractPhoneBaseCi.getNVESN();
    }

    public String getPesn() {
        Rlog.e(LOG_TAG, "[HwPhoneBase] getPesn() is a CDMA method");
        return ProxyController.MODEM_0;
    }

    public void getCallbarringOption(String facility, String serviceClass, Message response) {
        Rlog.e(LOG_TAG, "[HwPhoneBase] getPesn() is a GSM method");
    }

    public void setCallbarringOption(String facility, String password, boolean isActivate, String serviceClass, Message response) {
        Rlog.e(LOG_TAG, "[HwPhoneBase] getPesn() is a GSM method");
    }

    public void changeBarringPassword(String oldPassword, String newPassword, Message response) {
        Rlog.e(LOG_TAG, "[HwPhoneBase] getPesn() is a GSM method");
    }

    protected String getVoiceMailNumberHwCust() {
        Rlog.e(LOG_TAG, "[HwPhoneBase] getVoiceMailNumberHwCust() is a CDMA method");
        return "";
    }

    public void closeRrc() {
        Rlog.e(LOG_TAG, "[HwPhoneBase] closeRrc()");
    }

    public void cleanDeviceId() {
        Rlog.d(LOG_TAG, "cleanDeviceId should be override by subclass");
    }

    public void registerForUnsolSpeechInfo(Handler h, int what, Object obj) {
        this.mAbstractPhoneBaseCi.registerForUnsolSpeechInfo(h, what, obj);
    }

    public void unregisterForUnsolSpeechInfo(Handler h) {
        this.mAbstractPhoneBaseCi.unregisterForUnsolSpeechInfo(h);
    }

    public void setSpeechInfoCodec(int speechinfocodec) {
        this.speechInfoCodec = speechinfocodec;
    }

    public String getSpeechInfoCodec() {
        String ret = "";
        if (SPEECH_INFO_CODEC_WB == this.speechInfoCodec) {
            return "incall_wb=on";
        }
        if (SPEECH_INFO_CODEC_NB == this.speechInfoCodec) {
            return "incall_wb=off";
        }
        return ret;
    }

    public void switchVoiceCallBackgroundState(int state) {
    }

    public boolean isMmiCode(String dialString) {
        return IS_RESTORE_AUTO;
    }

    public void getPOLCapabilty(Message response) {
        Rlog.d(LOG_TAG, "getPOLCapabilty should be override by subclass");
    }

    public void getPreferedOperatorList(Message response) {
        Rlog.d(LOG_TAG, "getPreferedOperatorList should be override by subclass");
    }

    public void setPOLEntry(int index, String numeric, int nAct, Message response) {
        Rlog.d(LOG_TAG, "setPOLEntry should be override by subclass");
    }

    public void riseCdmaCutoffFreq(boolean on) {
    }

    public void setLTEReleaseVersion(boolean state, Message response) {
        Rlog.d(LOG_TAG, "setLTEReleaseVersion should be override by subclass");
    }

    public int getLteReleaseVersion() {
        Rlog.d(LOG_TAG, "getLteReleaseVersion should be override by subclass");
        return LTE_RELEASE_VERSION_UNKNOWN;
    }

    public void setOOSFlagOnSelectNetworkManually(boolean flag) {
        this.mNeedShowOOS = flag;
    }

    public boolean getOOSFlag() {
        return this.mNeedShowOOS;
    }

    public void restoreNetworkSelectionAuto() {
        if (IS_RESTORE_AUTO && IS_CHINA_TELECOM && HwFrameworkFactory.getHwInnerTelephonyManager().getDefault4GSlotId() == getPhoneId()) {
            Rlog.d(LOG_TAG, "set mode to automatic for ct when received manual complete");
            Message message = Message.obtain(this);
            message.what = EVENT_SET_MODE_TO_AUTO;
            sendMessageDelayed(message, 5000);
        }
    }

    public void hasNetworkSelectionAuto() {
        if (IS_RESTORE_AUTO && IS_CHINA_TELECOM && HwFrameworkFactory.getHwInnerTelephonyManager().getDefault4GSlotId() == getPhoneId() && hasMessages(EVENT_SET_MODE_TO_AUTO)) {
            Rlog.d(LOG_TAG, "remove EVENT_SET_MODE_TO_AUTO");
            removeMessages(EVENT_SET_MODE_TO_AUTO);
        }
    }

    public int getDataRoamingScope() {
        return this.mReference.getDataRoamingScope();
    }

    public boolean setDataRoamingScope(int scope) {
        return this.mReference.setDataRoamingScope(scope);
    }

    public boolean setISMCOEX(String setISMCoex) {
        Rlog.d(LOG_TAG, "setISMCOEX should be override by subclass");
        return IS_RESTORE_AUTO;
    }

    public void setImsDomainConfig(int domainType) {
        Rlog.d(LOG_TAG, "setImsDomainConfig should be override by subclass");
    }

    public void getImsDomain(Message response) {
        Rlog.d(LOG_TAG, "getImsDomain should be override by subclass");
    }

    public void handleUiccAuth(int auth_type, byte[] rand, byte[] auth, Message response) {
        Rlog.d(LOG_TAG, "handleUiccAuth should be override by subclass");
    }

    public void handleMapconImsaReq(byte[] Msg) {
        Rlog.d(LOG_TAG, "handleMapconImsaReq should be override by subclass");
    }

    public void setCSGNetworkSelectionModeManual(byte[] data, Message response) {
        Rlog.e(LOG_TAG, "[HwPhoneBase] setCSGNetworkSelectionModeManual() is a GSM method");
    }

    public void selectCsgNetworkManually(Message response) {
        Rlog.e(LOG_TAG, "[HwPhoneBase] selectCsgNetworkManually() is a GSM method");
    }

    public void registerForHWBuffer(Handler h, int what, Object obj) {
        Rlog.v(LOG_TAG, "PhoneBase.registerForHWBuffer() >>h: " + h + ", what: " + what);
        this.mAbstractPhoneBaseCi.registerForHWBuffer(h, what, obj);
    }

    public void unregisterForHWBuffer(Handler h) {
        Rlog.v(LOG_TAG, "PhoneBase.unregisterForHWBuffer() >>h: " + h);
        this.mAbstractPhoneBaseCi.unregisterForHWBuffer(h);
    }

    public void sendHWSolicited(Message reqMsg, int event, byte[] reqData) {
        if (event < 0) {
            Rlog.w(LOG_TAG, "sendHWSolicited() event not less than 0 ");
        } else if (reqData == null || BUFFER_SIZE < reqData.length) {
            Rlog.w(LOG_TAG, "sendHWSolicited() reqData is null or length overstep");
        } else {
            this.mAbstractPhoneBaseCi.sendHWBufferSolicited(reqMsg, event, reqData);
        }
    }

    public boolean cmdForECInfo(int event, int action, byte[] buf) {
        return this.mAbstractPhoneBaseCi.cmdForECInfo(event, action, buf);
    }

    public void notifyMultiSimConfigurationChanged() {
        if (this.mIsAdaptMultiSimConfiguration) {
            Phone[] phones = PhoneFactory.getPhones();
            if (phones == null) {
                Rlog.e(LOG_TAG, "get phones failed");
                return;
            }
            try {
                int phoneCnt = phones.length;
                String oldMultiSimConfiguration = SystemProperties.get("persist.radio.multisim.config", MUTIL_SIM_CONFIGURATION_UNKNOW);
                String newMultiSimConfiguration = oldMultiSimConfiguration;
                if (phoneCnt == SPEECH_INFO_CODEC_WB) {
                    int[] phoneTypes = new int[SPEECH_INFO_CODEC_WB];
                    phoneTypes[LTE_RELEASE_VERSION_R9] = phones[LTE_RELEASE_VERSION_R9].getPhoneType();
                    phoneTypes[SPEECH_INFO_CODEC_NB] = phones[SPEECH_INFO_CODEC_NB].getPhoneType();
                    Rlog.d(LOG_TAG, "phoneTypes[0] = " + phoneTypes[LTE_RELEASE_VERSION_R9] + ", phoneTypes[1] = " + phoneTypes[SPEECH_INFO_CODEC_NB]);
                    Arrays.sort(phoneTypes);
                    if (phoneTypes[LTE_RELEASE_VERSION_R9] == SPEECH_INFO_CODEC_NB && phoneTypes[SPEECH_INFO_CODEC_NB] == SPEECH_INFO_CODEC_NB) {
                        newMultiSimConfiguration = MUTIL_SIM_CONFIGURATION_DSDS;
                    } else if (phoneTypes[LTE_RELEASE_VERSION_R9] == SPEECH_INFO_CODEC_NB && phoneTypes[SPEECH_INFO_CODEC_NB] == SPEECH_INFO_CODEC_WB) {
                        newMultiSimConfiguration = MUTIL_SIM_CONFIGURATION_DSDA;
                    }
                }
                if (!newMultiSimConfiguration.equals(oldMultiSimConfiguration)) {
                    Rlog.d(LOG_TAG, "MultiSimConfiguration changed:newMultiSimConfiguration = " + newMultiSimConfiguration + ", oldMultiSimConfiguration = " + oldMultiSimConfiguration);
                    SystemProperties.set("persist.radio.multisim.config", newMultiSimConfiguration);
                    notifyMultiSimConfigurationChanged(newMultiSimConfiguration);
                }
            } catch (Exception e) {
                Rlog.e(LOG_TAG, "e = " + e);
            }
        }
    }

    private void notifyMultiSimConfigurationChanged(String multiSimConfiguration) {
        try {
            ByteBuffer payload = ByteBuffer.allocate(((HOEMHOOK.length() + INT_LENGTH) + INT_LENGTH) + multiSimConfiguration.length());
            payload.order(ByteOrder.nativeOrder());
            payload.put(HOEMHOOK.getBytes("UTF-8"));
            payload.putInt(524589);
            payload.putInt(multiSimConfiguration.length());
            payload.put(multiSimConfiguration.getBytes("UTF-8"));
            Phone phone = (Phone) this;
            phone.mNotifier.notifyOemHookRawEventForSubscriber(phone.getSubId(), payload.array());
        } catch (Exception e) {
            Rlog.e(LOG_TAG, "e = " + e);
        }
    }
}
