// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux;

import oshi.software.os.linux.proc.OSVersionInfoEx;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import oshi.software.os.OperatingSystemVersion;
import oshi.software.os.OperatingSystem;

public class LinuxOperatingSystem implements OperatingSystem
{
    private OperatingSystemVersion _version;
    private String _family;
    
    public LinuxOperatingSystem() {
        this._version = null;
        this._family = null;
    }
    
    public String getFamily() {
        if (this._family == null) {
            Scanner in;
            try {
                in = new Scanner(new FileReader("/etc/os-release"));
            }
            catch (FileNotFoundException e) {
                return "";
            }
            in.useDelimiter("\n");
            while (in.hasNext()) {
                final String[] splittedLine = in.next().split("=");
                if (splittedLine[0].equals("NAME")) {
                    this._family = splittedLine[1].replaceAll("^\"|\"$", "");
                    break;
                }
            }
            in.close();
        }
        return this._family;
    }
    
    public String getManufacturer() {
        return "GNU/Linux";
    }
    
    public OperatingSystemVersion getVersion() {
        if (this._version == null) {
            this._version = new OSVersionInfoEx();
        }
        return this._version;
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
