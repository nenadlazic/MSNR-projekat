/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/nenad/Desktop/nenad_msnr/pnrs2_VioletaCikos/Service/src/matf/msnr/netbroadcast/IIntentBroadcast.aidl
 */
package matf.msnr.netbroadcast;
public interface IIntentBroadcast extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements matf.msnr.netbroadcast.IIntentBroadcast
{
private static final java.lang.String DESCRIPTOR = "matf.msnr.netbroadcast.IIntentBroadcast";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an matf.msnr.netbroadcast.IIntentBroadcast interface,
 * generating a proxy if needed.
 */
public static matf.msnr.netbroadcast.IIntentBroadcast asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof matf.msnr.netbroadcast.IIntentBroadcast))) {
return ((matf.msnr.netbroadcast.IIntentBroadcast)iin);
}
return new matf.msnr.netbroadcast.IIntentBroadcast.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_sendBroadcast:
{
data.enforceInterface(DESCRIPTOR);
android.content.Intent _arg0;
if ((0!=data.readInt())) {
_arg0 = android.content.Intent.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
java.lang.String _arg1;
_arg1 = data.readString();
this.sendBroadcast(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements matf.msnr.netbroadcast.IIntentBroadcast
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void sendBroadcast(android.content.Intent intent, java.lang.String addr) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((intent!=null)) {
_data.writeInt(1);
intent.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(addr);
mRemote.transact(Stub.TRANSACTION_sendBroadcast, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_sendBroadcast = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void sendBroadcast(android.content.Intent intent, java.lang.String addr) throws android.os.RemoteException;
}
