// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Pointer;
import com.sun.jna.Memory;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface Winsvc extends StdCallLibrary
{
    public static final int SERVICE_RUNS_IN_SYSTEM_PROCESS = 1;
    public static final int SC_MANAGER_CONNECT = 1;
    public static final int SC_MANAGER_CREATE_SERVICE = 2;
    public static final int SC_MANAGER_ENUMERATE_SERVICE = 4;
    public static final int SC_MANAGER_LOCK = 8;
    public static final int SC_MANAGER_QUERY_LOCK_STATUS = 16;
    public static final int SC_MANAGER_MODIFY_BOOT_CONFIG = 32;
    public static final int SC_MANAGER_ALL_ACCESS = 983103;
    public static final int SERVICE_QUERY_CONFIG = 1;
    public static final int SERVICE_CHANGE_CONFIG = 2;
    public static final int SERVICE_QUERY_STATUS = 4;
    public static final int SERVICE_ENUMERATE_DEPENDENTS = 8;
    public static final int SERVICE_START = 16;
    public static final int SERVICE_STOP = 32;
    public static final int SERVICE_PAUSE_CONTINUE = 64;
    public static final int SERVICE_INTERROGATE = 128;
    public static final int SERVICE_USER_DEFINED_CONTROL = 256;
    public static final int SERVICE_ALL_ACCESS = 983551;
    public static final int SERVICE_CONTROL_STOP = 1;
    public static final int SERVICE_CONTROL_PAUSE = 2;
    public static final int SERVICE_CONTROL_CONTINUE = 3;
    public static final int SERVICE_CONTROL_INTERROGATE = 4;
    public static final int SERVICE_CONTROL_PARAMCHANGE = 6;
    public static final int SERVICE_CONTROL_NETBINDADD = 7;
    public static final int SERVICE_CONTROL_NETBINDREMOVE = 8;
    public static final int SERVICE_CONTROL_NETBINDENABLE = 9;
    public static final int SERVICE_CONTROL_NETBINDDISABLE = 10;
    public static final int SERVICE_STOPPED = 1;
    public static final int SERVICE_START_PENDING = 2;
    public static final int SERVICE_STOP_PENDING = 3;
    public static final int SERVICE_RUNNING = 4;
    public static final int SERVICE_CONTINUE_PENDING = 5;
    public static final int SERVICE_PAUSE_PENDING = 6;
    public static final int SERVICE_PAUSED = 7;
    public static final int SERVICE_ACCEPT_STOP = 1;
    public static final int SERVICE_ACCEPT_PAUSE_CONTINUE = 2;
    public static final int SERVICE_ACCEPT_SHUTDOWN = 4;
    public static final int SERVICE_ACCEPT_PARAMCHANGE = 8;
    public static final int SERVICE_ACCEPT_NETBINDCHANGE = 16;
    public static final int SERVICE_ACCEPT_HARDWAREPROFILECHANGE = 32;
    public static final int SERVICE_ACCEPT_POWEREVENT = 64;
    public static final int SERVICE_ACCEPT_SESSIONCHANGE = 128;
    public static final int SERVICE_ACCEPT_PRESHUTDOWN = 256;
    public static final int SERVICE_ACCEPT_TIMECHANGE = 512;
    public static final int SERVICE_ACCEPT_TRIGGEREVENT = 1024;
    
    public static class SERVICE_STATUS extends Structure
    {
        public int dwServiceType;
        public int dwCurrentState;
        public int dwControlsAccepted;
        public int dwWin32ExitCode;
        public int dwServiceSpecificExitCode;
        public int dwCheckPoint;
        public int dwWaitHint;
    }
    
    public static class SERVICE_STATUS_PROCESS extends Structure
    {
        public int dwServiceType;
        public int dwCurrentState;
        public int dwControlsAccepted;
        public int dwWin32ExitCode;
        public int dwServiceSpecificExitCode;
        public int dwCheckPoint;
        public int dwWaitHint;
        public int dwProcessId;
        public int dwServiceFlags;
        
        public SERVICE_STATUS_PROCESS() {
        }
        
        public SERVICE_STATUS_PROCESS(final int size) {
            super(new Memory(size));
        }
    }
    
    public static class SC_HANDLE extends WinNT.HANDLE
    {
    }
    
    public abstract static class SC_STATUS_TYPE
    {
        public static final int SC_STATUS_PROCESS_INFO = 0;
    }
}
