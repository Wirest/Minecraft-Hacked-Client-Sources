// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows.nt;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import oshi.software.os.OperatingSystemVersion;

public class OSVersionInfoEx implements OperatingSystemVersion
{
    private WinNT.OSVERSIONINFOEX _versionInfo;
    
    public OSVersionInfoEx() {
        this._versionInfo = new WinNT.OSVERSIONINFOEX();
        if (!Kernel32.INSTANCE.GetVersionEx(this._versionInfo)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public int getMajor() {
        return this._versionInfo.dwMajorVersion.intValue();
    }
    
    public int getMinor() {
        return this._versionInfo.dwMinorVersion.intValue();
    }
    
    public int getBuildNumber() {
        return this._versionInfo.dwBuildNumber.intValue();
    }
    
    public int getPlatformId() {
        return this._versionInfo.dwPlatformId.intValue();
    }
    
    public String getServicePack() {
        return Native.toString(this._versionInfo.szCSDVersion);
    }
    
    public int getSuiteMask() {
        return this._versionInfo.wSuiteMask.intValue();
    }
    
    public byte getProductType() {
        return this._versionInfo.wProductType;
    }
    
    @Override
    public String toString() {
        String version = null;
        if (this.getPlatformId() == 2) {
            if (this.getMajor() == 6 && this.getMinor() == 3 && this.getProductType() == 1) {
                version = "8.1";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 3 && this.getProductType() != 1) {
                version = "Server 2012 R2";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 2 && this.getProductType() == 1) {
                version = "8";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 2 && this.getProductType() != 1) {
                version = "Server 2012";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 1 && this.getProductType() == 1) {
                version = "7";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 1 && this.getProductType() != 1) {
                version = "Server 2008 R2";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 0 && this.getProductType() != 1) {
                version = "Server 2008";
            }
            else if (this.getMajor() == 6 && this.getMinor() == 0 && this.getProductType() == 1) {
                version = "Vista";
            }
            else if (this.getMajor() == 5 && this.getMinor() == 2 && this.getProductType() != 1 && User32.INSTANCE.GetSystemMetrics(89) != 0) {
                version = "Server 2003";
            }
            else if (this.getMajor() == 5 && this.getMinor() == 2 && this.getProductType() != 1 && User32.INSTANCE.GetSystemMetrics(89) == 0) {
                version = "Server 2003 R2";
            }
            else if (this.getMajor() == 5 && this.getMinor() == 2 && this.getProductType() == 1) {
                version = "XP";
            }
            else if (this.getMajor() == 5 && this.getMinor() == 1) {
                version = "XP";
            }
            else if (this.getMajor() == 5 && this.getMinor() == 0) {
                version = "2000";
            }
            else {
                if (this.getMajor() != 4) {
                    throw new RuntimeException("Unsupported Windows NT version: " + this._versionInfo.toString());
                }
                version = "NT 4";
                if ("Service Pack 6".equals(this.getServicePack()) && Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\Hotfix\\Q246009")) {
                    return "NT4 SP6a";
                }
            }
            if (this._versionInfo.wServicePackMajor.intValue() > 0) {
                version = version + " SP" + this._versionInfo.wServicePackMajor.intValue();
            }
        }
        else {
            if (this.getPlatformId() != 1) {
                throw new RuntimeException("Unsupported Windows platform: " + this._versionInfo.toString());
            }
            if (this.getMajor() == 4 && this.getMinor() == 90) {
                version = "ME";
            }
            else if (this.getMajor() == 4 && this.getMinor() == 10) {
                if (this._versionInfo.szCSDVersion[1] == 'A') {
                    version = "98 SE";
                }
                else {
                    version = "98";
                }
            }
            else {
                if (this.getMajor() != 4 || this.getMinor() != 0) {
                    throw new RuntimeException("Unsupported Windows 9x version: " + this._versionInfo.toString());
                }
                if (this._versionInfo.szCSDVersion[1] == 'C' || this._versionInfo.szCSDVersion[1] == 'B') {
                    version = "95 OSR2";
                }
                else {
                    version = "95";
                }
            }
        }
        return version;
    }
    
    public OSVersionInfoEx(final WinNT.OSVERSIONINFOEX versionInfo) {
        this._versionInfo = versionInfo;
    }
}
