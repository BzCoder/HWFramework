package android.print;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPrintJobStateChangeListener extends IInterface {

    public static abstract class Stub extends Binder implements IPrintJobStateChangeListener {
        private static final String DESCRIPTOR = "android.print.IPrintJobStateChangeListener";
        static final int TRANSACTION_onPrintJobStateChanged = 1;

        private static class Proxy implements IPrintJobStateChangeListener {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void onPrintJobStateChanged(PrintJobId printJobId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (printJobId != null) {
                        _data.writeInt(Stub.TRANSACTION_onPrintJobStateChanged);
                        printJobId.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(Stub.TRANSACTION_onPrintJobStateChanged, _data, null, Stub.TRANSACTION_onPrintJobStateChanged);
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPrintJobStateChangeListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPrintJobStateChangeListener)) {
                return new Proxy(obj);
            }
            return (IPrintJobStateChangeListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case TRANSACTION_onPrintJobStateChanged /*1*/:
                    PrintJobId printJobId;
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        printJobId = (PrintJobId) PrintJobId.CREATOR.createFromParcel(data);
                    } else {
                        printJobId = null;
                    }
                    onPrintJobStateChanged(printJobId);
                    return true;
                case IBinder.INTERFACE_TRANSACTION /*1598968902*/:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }
    }

    void onPrintJobStateChanged(PrintJobId printJobId) throws RemoteException;
}
