package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;
import java.util.Locale;

public class ProxyInfo implements Parcelable {
    public static final Creator<ProxyInfo> CREATOR = null;
    public static final String LOCAL_EXCL_LIST = "";
    public static final String LOCAL_HOST = "localhost";
    public static final int LOCAL_PORT = -1;
    private String mExclusionList;
    private String mHost;
    private Uri mPacFileUrl;
    private String[] mParsedExclusionList;
    private int mPort;

    static {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.DecodeException: Load method exception in method: android.net.ProxyInfo.<clinit>():void
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:113)
	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:256)
	at jadx.core.ProcessClass.process(ProcessClass.java:34)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
Caused by: jadx.core.utils.exceptions.DecodeException:  in method: android.net.ProxyInfo.<clinit>():void
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:46)
	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:98)
	... 7 more
Caused by: java.lang.IllegalArgumentException: bogus opcode: 0073
	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1197)
	at com.android.dx.io.OpcodeInfo.getFormat(OpcodeInfo.java:1212)
	at com.android.dx.io.instructions.DecodedInstruction.decode(DecodedInstruction.java:72)
	at jadx.core.dex.instructions.InsnDecoder.decodeInsns(InsnDecoder.java:43)
	... 8 more
*/
        /*
        // Can't load method instructions.
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ProxyInfo.<clinit>():void");
    }

    public static ProxyInfo buildDirectProxy(String host, int port) {
        return new ProxyInfo(host, port, null);
    }

    public static ProxyInfo buildDirectProxy(String host, int port, List<String> exclList) {
        String[] array = (String[]) exclList.toArray(new String[exclList.size()]);
        return new ProxyInfo(host, port, TextUtils.join(",", array), array);
    }

    public static ProxyInfo buildPacProxy(Uri pacUri) {
        return new ProxyInfo(pacUri);
    }

    public ProxyInfo(String host, int port, String exclList) {
        this.mHost = host;
        this.mPort = port;
        setExclusionList(exclList);
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(Uri pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = LOCAL_PORT;
        setExclusionList(LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    public ProxyInfo(String pacFileUrl) {
        this.mHost = LOCAL_HOST;
        this.mPort = LOCAL_PORT;
        setExclusionList(LOCAL_EXCL_LIST);
        this.mPacFileUrl = Uri.parse(pacFileUrl);
    }

    public ProxyInfo(Uri pacFileUrl, int localProxyPort) {
        this.mHost = LOCAL_HOST;
        this.mPort = localProxyPort;
        setExclusionList(LOCAL_EXCL_LIST);
        if (pacFileUrl == null) {
            throw new NullPointerException();
        }
        this.mPacFileUrl = pacFileUrl;
    }

    private ProxyInfo(String host, int port, String exclList, String[] parsedExclList) {
        this.mHost = host;
        this.mPort = port;
        this.mExclusionList = exclList;
        this.mParsedExclusionList = parsedExclList;
        this.mPacFileUrl = Uri.EMPTY;
    }

    public ProxyInfo(ProxyInfo source) {
        if (source != null) {
            this.mHost = source.getHost();
            this.mPort = source.getPort();
            this.mPacFileUrl = source.mPacFileUrl;
            this.mExclusionList = source.getExclusionListAsString();
            this.mParsedExclusionList = source.mParsedExclusionList;
            return;
        }
        this.mPacFileUrl = Uri.EMPTY;
    }

    public InetSocketAddress getSocketAddress() {
        try {
            return new InetSocketAddress(this.mHost, this.mPort);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Uri getPacFileUrl() {
        return this.mPacFileUrl;
    }

    public String getHost() {
        return this.mHost;
    }

    public int getPort() {
        return this.mPort;
    }

    public String[] getExclusionList() {
        return this.mParsedExclusionList;
    }

    public String getExclusionListAsString() {
        return this.mExclusionList;
    }

    private void setExclusionList(String exclusionList) {
        this.mExclusionList = exclusionList;
        if (this.mExclusionList == null) {
            this.mParsedExclusionList = new String[0];
        } else {
            this.mParsedExclusionList = exclusionList.toLowerCase(Locale.ROOT).split(",");
        }
    }

    public boolean isValid() {
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            return true;
        }
        boolean z;
        if (Proxy.validate(this.mHost == null ? LOCAL_EXCL_LIST : this.mHost, this.mPort == 0 ? LOCAL_EXCL_LIST : Integer.toString(this.mPort), this.mExclusionList == null ? LOCAL_EXCL_LIST : this.mExclusionList) == 0) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public Proxy makeProxy() {
        Proxy proxy = Proxy.NO_PROXY;
        if (this.mHost == null) {
            return proxy;
        }
        try {
            return new Proxy(Type.HTTP, new InetSocketAddress(this.mHost, this.mPort));
        } catch (IllegalArgumentException e) {
            return proxy;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            sb.append("PAC Script: ");
            sb.append(this.mPacFileUrl);
        }
        if (this.mHost != null) {
            sb.append("[");
            sb.append(this.mHost);
            sb.append("] ");
            sb.append(Integer.toString(this.mPort));
            if (this.mExclusionList != null) {
                sb.append(" xl=").append(this.mExclusionList);
            }
        } else {
            sb.append("[ProxyProperties.mHost == null]");
        }
        return sb.toString();
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (!(o instanceof ProxyInfo)) {
            return false;
        }
        ProxyInfo p = (ProxyInfo) o;
        if (!Uri.EMPTY.equals(this.mPacFileUrl)) {
            if (!(this.mPacFileUrl.equals(p.getPacFileUrl()) && this.mPort == p.mPort)) {
                z = false;
            }
            return z;
        } else if (!Uri.EMPTY.equals(p.mPacFileUrl)) {
            return false;
        } else {
            if (this.mExclusionList != null && !this.mExclusionList.equals(p.getExclusionListAsString())) {
                return false;
            }
            if (this.mHost != null && p.getHost() != null && !this.mHost.equals(p.getHost())) {
                return false;
            }
            if (this.mHost == null || p.mHost != null) {
                return (this.mHost != null || p.mHost == null) && this.mPort == p.mPort;
            } else {
                return false;
            }
        }
    }

    public int describeContents() {
        return 0;
    }

    public int hashCode() {
        int i = 0;
        int hashCode = this.mHost == null ? 0 : this.mHost.hashCode();
        if (this.mExclusionList != null) {
            i = this.mExclusionList.hashCode();
        }
        return (hashCode + i) + this.mPort;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (Uri.EMPTY.equals(this.mPacFileUrl)) {
            dest.writeByte((byte) 0);
            if (this.mHost != null) {
                dest.writeByte((byte) 1);
                dest.writeString(this.mHost);
                dest.writeInt(this.mPort);
            } else {
                dest.writeByte((byte) 0);
            }
            dest.writeString(this.mExclusionList);
            dest.writeStringArray(this.mParsedExclusionList);
            return;
        }
        dest.writeByte((byte) 1);
        this.mPacFileUrl.writeToParcel(dest, 0);
        dest.writeInt(this.mPort);
    }
}
