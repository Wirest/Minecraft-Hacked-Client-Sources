// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows.nt;

import com.sun.jna.platform.win32.Win32Exception;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import oshi.hardware.Memory;

public class GlobalMemory implements Memory
{
    WinBase.MEMORYSTATUSEX _memory;
    
    public GlobalMemory() {
        this._memory = new WinBase.MEMORYSTATUSEX();
        if (!Kernel32.INSTANCE.GlobalMemoryStatusEx(this._memory)) {
            throw new Win32Exception(Kernel32.INSTANCE.GetLastError());
        }
    }
    
    public long getAvailable() {
        return this._memory.ullAvailPhys.longValue();
    }
    
    public long getTotal() {
        return this._memory.ullTotalPhys.longValue();
    }
}
