// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;
import com.sun.jna.WString;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.Structure;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Advapi32 extends StdCallLibrary
{
    public static final Advapi32 INSTANCE = (Advapi32)Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);
    
    boolean GetUserNameW(final char[] p0, final IntByReference p1);
    
    boolean LookupAccountName(final String p0, final String p1, final WinNT.PSID p2, final IntByReference p3, final char[] p4, final IntByReference p5, final PointerByReference p6);
    
    boolean LookupAccountSid(final String p0, final WinNT.PSID p1, final char[] p2, final IntByReference p3, final char[] p4, final IntByReference p5, final PointerByReference p6);
    
    boolean ConvertSidToStringSid(final WinNT.PSID p0, final PointerByReference p1);
    
    boolean ConvertStringSidToSid(final String p0, final WinNT.PSIDByReference p1);
    
    int GetLengthSid(final WinNT.PSID p0);
    
    boolean IsValidSid(final WinNT.PSID p0);
    
    boolean IsWellKnownSid(final WinNT.PSID p0, final int p1);
    
    boolean CreateWellKnownSid(final int p0, final WinNT.PSID p1, final WinNT.PSID p2, final IntByReference p3);
    
    boolean LogonUser(final String p0, final String p1, final String p2, final int p3, final int p4, final WinNT.HANDLEByReference p5);
    
    boolean OpenThreadToken(final WinNT.HANDLE p0, final int p1, final boolean p2, final WinNT.HANDLEByReference p3);
    
    boolean OpenProcessToken(final WinNT.HANDLE p0, final int p1, final WinNT.HANDLEByReference p2);
    
    boolean DuplicateToken(final WinNT.HANDLE p0, final int p1, final WinNT.HANDLEByReference p2);
    
    boolean DuplicateTokenEx(final WinNT.HANDLE p0, final int p1, final WinBase.SECURITY_ATTRIBUTES p2, final int p3, final int p4, final WinNT.HANDLEByReference p5);
    
    boolean GetTokenInformation(final WinNT.HANDLE p0, final int p1, final Structure p2, final int p3, final IntByReference p4);
    
    boolean ImpersonateLoggedOnUser(final WinNT.HANDLE p0);
    
    boolean ImpersonateSelf(final int p0);
    
    boolean RevertToSelf();
    
    int RegOpenKeyEx(final WinReg.HKEY p0, final String p1, final int p2, final int p3, final WinReg.HKEYByReference p4);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final char[] p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final byte[] p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final IntByReference p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final LongByReference p4, final IntByReference p5);
    
    int RegQueryValueEx(final WinReg.HKEY p0, final String p1, final int p2, final IntByReference p3, final Pointer p4, final IntByReference p5);
    
    int RegCloseKey(final WinReg.HKEY p0);
    
    int RegDeleteValue(final WinReg.HKEY p0, final String p1);
    
    int RegSetValueEx(final WinReg.HKEY p0, final String p1, final int p2, final int p3, final char[] p4, final int p5);
    
    int RegSetValueEx(final WinReg.HKEY p0, final String p1, final int p2, final int p3, final byte[] p4, final int p5);
    
    int RegCreateKeyEx(final WinReg.HKEY p0, final String p1, final int p2, final String p3, final int p4, final int p5, final WinBase.SECURITY_ATTRIBUTES p6, final WinReg.HKEYByReference p7, final IntByReference p8);
    
    int RegDeleteKey(final WinReg.HKEY p0, final String p1);
    
    int RegEnumKeyEx(final WinReg.HKEY p0, final int p1, final char[] p2, final IntByReference p3, final IntByReference p4, final char[] p5, final IntByReference p6, final WinBase.FILETIME p7);
    
    int RegEnumValue(final WinReg.HKEY p0, final int p1, final char[] p2, final IntByReference p3, final IntByReference p4, final IntByReference p5, final byte[] p6, final IntByReference p7);
    
    int RegQueryInfoKey(final WinReg.HKEY p0, final char[] p1, final IntByReference p2, final IntByReference p3, final IntByReference p4, final IntByReference p5, final IntByReference p6, final IntByReference p7, final IntByReference p8, final IntByReference p9, final IntByReference p10, final WinBase.FILETIME p11);
    
    WinNT.HANDLE RegisterEventSource(final String p0, final String p1);
    
    boolean DeregisterEventSource(final WinNT.HANDLE p0);
    
    WinNT.HANDLE OpenEventLog(final String p0, final String p1);
    
    boolean CloseEventLog(final WinNT.HANDLE p0);
    
    boolean GetNumberOfEventLogRecords(final WinNT.HANDLE p0, final IntByReference p1);
    
    boolean ReportEvent(final WinNT.HANDLE p0, final int p1, final int p2, final int p3, final WinNT.PSID p4, final int p5, final int p6, final String[] p7, final Pointer p8);
    
    boolean ClearEventLog(final WinNT.HANDLE p0, final String p1);
    
    boolean BackupEventLog(final WinNT.HANDLE p0, final String p1);
    
    WinNT.HANDLE OpenBackupEventLog(final String p0, final String p1);
    
    boolean ReadEventLog(final WinNT.HANDLE p0, final int p1, final int p2, final Pointer p3, final int p4, final IntByReference p5, final IntByReference p6);
    
    boolean GetOldestEventLogRecord(final WinNT.HANDLE p0, final IntByReference p1);
    
    boolean QueryServiceStatusEx(final Winsvc.SC_HANDLE p0, final int p1, final Winsvc.SERVICE_STATUS_PROCESS p2, final int p3, final IntByReference p4);
    
    boolean ControlService(final Winsvc.SC_HANDLE p0, final int p1, final Winsvc.SERVICE_STATUS p2);
    
    boolean StartService(final Winsvc.SC_HANDLE p0, final int p1, final String[] p2);
    
    boolean CloseServiceHandle(final Winsvc.SC_HANDLE p0);
    
    Winsvc.SC_HANDLE OpenService(final Winsvc.SC_HANDLE p0, final String p1, final int p2);
    
    Winsvc.SC_HANDLE OpenSCManager(final String p0, final String p1, final int p2);
    
    boolean CreateProcessAsUser(final WinNT.HANDLE p0, final String p1, final String p2, final WinBase.SECURITY_ATTRIBUTES p3, final WinBase.SECURITY_ATTRIBUTES p4, final boolean p5, final int p6, final String p7, final String p8, final WinBase.STARTUPINFO p9, final WinBase.PROCESS_INFORMATION p10);
    
    boolean AdjustTokenPrivileges(final WinNT.HANDLE p0, final boolean p1, final WinNT.TOKEN_PRIVILEGES p2, final int p3, final WinNT.TOKEN_PRIVILEGES p4, final IntByReference p5);
    
    boolean LookupPrivilegeName(final String p0, final WinNT.LUID p1, final char[] p2, final IntByReference p3);
    
    boolean LookupPrivilegeValue(final String p0, final String p1, final WinNT.LUID p2);
    
    boolean GetFileSecurity(final WString p0, final int p1, final Pointer p2, final int p3, final IntByReference p4);
}
