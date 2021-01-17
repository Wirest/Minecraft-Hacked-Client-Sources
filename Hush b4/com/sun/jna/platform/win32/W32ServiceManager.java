// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

public class W32ServiceManager
{
    Winsvc.SC_HANDLE _handle;
    String _machineName;
    String _databaseName;
    
    public W32ServiceManager() {
        this._handle = null;
        this._machineName = null;
        this._databaseName = null;
    }
    
    public W32ServiceManager(final String machineName, final String databaseName) {
        this._handle = null;
        this._machineName = null;
        this._databaseName = null;
        this._machineName = machineName;
        this._databaseName = databaseName;
    }
    
    public void open(final int permissions) {
        this.close();
        this._handle = Advapi32.INSTANCE.OpenSCManager(this._machineName, this._databaseName, permissions);
        if (this._handle == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public void close() {
        if (this._handle != null) {
            if (!Advapi32.INSTANCE.CloseServiceHandle(this._handle)) {
                throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
            }
            this._handle = null;
        }
    }
    
    public W32Service openService(final String serviceName, final int permissions) {
        final Winsvc.SC_HANDLE serviceHandle = Advapi32.INSTANCE.OpenService(this._handle, serviceName, permissions);
        if (serviceHandle == null) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
        return new W32Service(serviceHandle);
    }
    
    public Winsvc.SC_HANDLE getHandle() {
        return this._handle;
    }
}
