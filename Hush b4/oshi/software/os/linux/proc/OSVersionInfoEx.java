// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux.proc;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import oshi.software.os.OperatingSystemVersion;

public class OSVersionInfoEx implements OperatingSystemVersion
{
    private String _version;
    private String _codeName;
    private String version;
    
    public OSVersionInfoEx() {
        this._version = null;
        this._codeName = null;
        this.version = null;
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("/etc/os-release"));
        }
        catch (FileNotFoundException e) {
            return;
        }
        in.useDelimiter("\n");
        while (in.hasNext()) {
            final String[] splittedLine = in.next().split("=");
            if (splittedLine[0].equals("VERSION_ID")) {
                this.setVersion(splittedLine[1].replaceAll("^\"|\"$", ""));
            }
            if (splittedLine[0].equals("VERSION")) {
                splittedLine[1] = splittedLine[1].replaceAll("^\"|\"$", "");
                String[] split = splittedLine[1].split("[()]");
                if (split.length <= 1) {
                    split = splittedLine[1].split(", ");
                }
                if (split.length > 1) {
                    this.setCodeName(split[1]);
                }
                else {
                    this.setCodeName(splittedLine[1]);
                }
            }
        }
        in.close();
    }
    
    public String getCodeName() {
        return this._codeName;
    }
    
    public String getVersion() {
        return this._version;
    }
    
    public void setCodeName(final String _codeName) {
        this._codeName = _codeName;
    }
    
    public void setVersion(final String _version) {
        this._version = _version;
    }
    
    @Override
    public String toString() {
        if (this.version == null) {
            this.version = this.getVersion() + " (" + this.getCodeName() + ")";
        }
        return this.version;
    }
}
