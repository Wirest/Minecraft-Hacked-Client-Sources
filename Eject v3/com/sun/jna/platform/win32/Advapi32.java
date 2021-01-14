package com.sun.jna.platform.win32;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.WString;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.LongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

public abstract interface Advapi32
        extends StdCallLibrary {
    public static final Advapi32 INSTANCE = (Advapi32) Native.loadLibrary("Advapi32", Advapi32.class, W32APIOptions.UNICODE_OPTIONS);

    public abstract boolean GetUserNameW(char[] paramArrayOfChar, IntByReference paramIntByReference);

    public abstract boolean LookupAccountName(String paramString1, String paramString2, WinNT.PSID paramPSID, IntByReference paramIntByReference1, char[] paramArrayOfChar, IntByReference paramIntByReference2, PointerByReference paramPointerByReference);

    public abstract boolean LookupAccountSid(String paramString, WinNT.PSID paramPSID, char[] paramArrayOfChar1, IntByReference paramIntByReference1, char[] paramArrayOfChar2, IntByReference paramIntByReference2, PointerByReference paramPointerByReference);

    public abstract boolean ConvertSidToStringSid(WinNT.PSID paramPSID, PointerByReference paramPointerByReference);

    public abstract boolean ConvertStringSidToSid(String paramString, WinNT.PSIDByReference paramPSIDByReference);

    public abstract int GetLengthSid(WinNT.PSID paramPSID);

    public abstract boolean IsValidSid(WinNT.PSID paramPSID);

    public abstract boolean IsWellKnownSid(WinNT.PSID paramPSID, int paramInt);

    public abstract boolean CreateWellKnownSid(int paramInt, WinNT.PSID paramPSID1, WinNT.PSID paramPSID2, IntByReference paramIntByReference);

    public abstract boolean LogonUser(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, WinNT.HANDLEByReference paramHANDLEByReference);

    public abstract boolean OpenThreadToken(WinNT.HANDLE paramHANDLE, int paramInt, boolean paramBoolean, WinNT.HANDLEByReference paramHANDLEByReference);

    public abstract boolean OpenProcessToken(WinNT.HANDLE paramHANDLE, int paramInt, WinNT.HANDLEByReference paramHANDLEByReference);

    public abstract boolean DuplicateToken(WinNT.HANDLE paramHANDLE, int paramInt, WinNT.HANDLEByReference paramHANDLEByReference);

    public abstract boolean DuplicateTokenEx(WinNT.HANDLE paramHANDLE, int paramInt1, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, int paramInt2, int paramInt3, WinNT.HANDLEByReference paramHANDLEByReference);

    public abstract boolean GetTokenInformation(WinNT.HANDLE paramHANDLE, int paramInt1, Structure paramStructure, int paramInt2, IntByReference paramIntByReference);

    public abstract boolean ImpersonateLoggedOnUser(WinNT.HANDLE paramHANDLE);

    public abstract boolean ImpersonateSelf(int paramInt);

    public abstract boolean RevertToSelf();

    public abstract int RegOpenKeyEx(WinReg.HKEY paramHKEY, String paramString, int paramInt1, int paramInt2, WinReg.HKEYByReference paramHKEYByReference);

    public abstract int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, char[] paramArrayOfChar, IntByReference paramIntByReference2);

    public abstract int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, byte[] paramArrayOfByte, IntByReference paramIntByReference2);

    public abstract int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3);

    public abstract int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, LongByReference paramLongByReference, IntByReference paramIntByReference2);

    public abstract int RegQueryValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt, IntByReference paramIntByReference1, Pointer paramPointer, IntByReference paramIntByReference2);

    public abstract int RegCloseKey(WinReg.HKEY paramHKEY);

    public abstract int RegDeleteValue(WinReg.HKEY paramHKEY, String paramString);

    public abstract int RegSetValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3);

    public abstract int RegSetValueEx(WinReg.HKEY paramHKEY, String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3);

    public abstract int RegCreateKeyEx(WinReg.HKEY paramHKEY, String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES, WinReg.HKEYByReference paramHKEYByReference, IntByReference paramIntByReference);

    public abstract int RegDeleteKey(WinReg.HKEY paramHKEY, String paramString);

    public abstract int RegEnumKeyEx(WinReg.HKEY paramHKEY, int paramInt, char[] paramArrayOfChar1, IntByReference paramIntByReference1, IntByReference paramIntByReference2, char[] paramArrayOfChar2, IntByReference paramIntByReference3, WinBase.FILETIME paramFILETIME);

    public abstract int RegEnumValue(WinReg.HKEY paramHKEY, int paramInt, char[] paramArrayOfChar, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3, byte[] paramArrayOfByte, IntByReference paramIntByReference4);

    public abstract int RegQueryInfoKey(WinReg.HKEY paramHKEY, char[] paramArrayOfChar, IntByReference paramIntByReference1, IntByReference paramIntByReference2, IntByReference paramIntByReference3, IntByReference paramIntByReference4, IntByReference paramIntByReference5, IntByReference paramIntByReference6, IntByReference paramIntByReference7, IntByReference paramIntByReference8, IntByReference paramIntByReference9, WinBase.FILETIME paramFILETIME);

    public abstract WinNT.HANDLE RegisterEventSource(String paramString1, String paramString2);

    public abstract boolean DeregisterEventSource(WinNT.HANDLE paramHANDLE);

    public abstract WinNT.HANDLE OpenEventLog(String paramString1, String paramString2);

    public abstract boolean CloseEventLog(WinNT.HANDLE paramHANDLE);

    public abstract boolean GetNumberOfEventLogRecords(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference);

    public abstract boolean ReportEvent(WinNT.HANDLE paramHANDLE, int paramInt1, int paramInt2, int paramInt3, WinNT.PSID paramPSID, int paramInt4, int paramInt5, String[] paramArrayOfString, Pointer paramPointer);

    public abstract boolean ClearEventLog(WinNT.HANDLE paramHANDLE, String paramString);

    public abstract boolean BackupEventLog(WinNT.HANDLE paramHANDLE, String paramString);

    public abstract WinNT.HANDLE OpenBackupEventLog(String paramString1, String paramString2);

    public abstract boolean ReadEventLog(WinNT.HANDLE paramHANDLE, int paramInt1, int paramInt2, Pointer paramPointer, int paramInt3, IntByReference paramIntByReference1, IntByReference paramIntByReference2);

    public abstract boolean GetOldestEventLogRecord(WinNT.HANDLE paramHANDLE, IntByReference paramIntByReference);

    public abstract boolean QueryServiceStatusEx(Winsvc.SC_HANDLE paramSC_HANDLE, int paramInt1, Winsvc.SERVICE_STATUS_PROCESS paramSERVICE_STATUS_PROCESS, int paramInt2, IntByReference paramIntByReference);

    public abstract boolean ControlService(Winsvc.SC_HANDLE paramSC_HANDLE, int paramInt, Winsvc.SERVICE_STATUS paramSERVICE_STATUS);

    public abstract boolean StartService(Winsvc.SC_HANDLE paramSC_HANDLE, int paramInt, String[] paramArrayOfString);

    public abstract boolean CloseServiceHandle(Winsvc.SC_HANDLE paramSC_HANDLE);

    public abstract Winsvc.SC_HANDLE OpenService(Winsvc.SC_HANDLE paramSC_HANDLE, String paramString, int paramInt);

    public abstract Winsvc.SC_HANDLE OpenSCManager(String paramString1, String paramString2, int paramInt);

    public abstract boolean CreateProcessAsUser(WinNT.HANDLE paramHANDLE, String paramString1, String paramString2, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES1, WinBase.SECURITY_ATTRIBUTES paramSECURITY_ATTRIBUTES2, boolean paramBoolean, int paramInt, String paramString3, String paramString4, WinBase.STARTUPINFO paramSTARTUPINFO, WinBase.PROCESS_INFORMATION paramPROCESS_INFORMATION);

    public abstract boolean AdjustTokenPrivileges(WinNT.HANDLE paramHANDLE, boolean paramBoolean, WinNT.TOKEN_PRIVILEGES paramTOKEN_PRIVILEGES1, int paramInt, WinNT.TOKEN_PRIVILEGES paramTOKEN_PRIVILEGES2, IntByReference paramIntByReference);

    public abstract boolean LookupPrivilegeName(String paramString, WinNT.LUID paramLUID, char[] paramArrayOfChar, IntByReference paramIntByReference);

    public abstract boolean LookupPrivilegeValue(String paramString1, String paramString2, WinNT.LUID paramLUID);

    public abstract boolean GetFileSecurity(WString paramWString, int paramInt1, Pointer paramPointer, int paramInt2, IntByReference paramIntByReference);
}




