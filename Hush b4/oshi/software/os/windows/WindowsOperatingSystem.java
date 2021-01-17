// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.windows;

import oshi.software.os.windows.nt.OSVersionInfoEx;
import oshi.software.os.OperatingSystemVersion;
import oshi.software.os.OperatingSystem;

public class WindowsOperatingSystem implements OperatingSystem
{
    private OperatingSystemVersion _version;
    
    public WindowsOperatingSystem() {
        this._version = null;
    }
    
    public OperatingSystemVersion getVersion() {
        if (this._version == null) {
            this._version = new OSVersionInfoEx();
        }
        return this._version;
    }
    
    public String getFamily() {
        return "Windows";
    }
    
    public String getManufacturer() {
        return "Microsoft";
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getManufacturer());
        sb.append(" ");
        sb.append(this.getFamily());
        sb.append(" ");
        sb.append(this.getVersion().toString());
        return sb.toString();
    }
}
