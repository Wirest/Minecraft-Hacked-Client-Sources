// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac.local;

import oshi.util.ExecutingCommand;
import oshi.software.os.OperatingSystemVersion;

public class OSVersionInfoEx implements OperatingSystemVersion
{
    private String _version;
    private String _codeName;
    private String version;
    private String _buildNumber;
    
    public OSVersionInfoEx() {
        this._version = null;
        this._codeName = null;
        this.version = null;
        this._buildNumber = null;
    }
    
    public String getVersion() {
        if (this._version == null) {
            this._version = ExecutingCommand.getFirstAnswer("sw_vers -productVersion");
        }
        return this._version;
    }
    
    public void setVersion(final String _version) {
        this._version = _version;
    }
    
    public String getCodeName() {
        if (this._codeName == null && this.getVersion() != null) {
            if ("10.0".equals(this.getVersion()) || this.getVersion().startsWith("10.0.")) {
                this._codeName = "Cheetah";
            }
            else if ("10.1".equals(this.getVersion()) || this.getVersion().startsWith("10.1.")) {
                this._codeName = "Puma";
            }
            else if ("10.2".equals(this.getVersion()) || this.getVersion().startsWith("10.2.")) {
                this._codeName = "Jaguar";
            }
            else if ("10.3".equals(this.getVersion()) || this.getVersion().startsWith("10.3.")) {
                this._codeName = "Panther";
            }
            else if ("10.4".equals(this.getVersion()) || this.getVersion().startsWith("10.4.")) {
                this._codeName = "Tiger";
            }
            else if ("10.5".equals(this.getVersion()) || this.getVersion().startsWith("10.5.")) {
                this._codeName = "Leopard";
            }
            else if ("10.6".equals(this.getVersion()) || this.getVersion().startsWith("10.6.")) {
                this._codeName = "Snow Leopard";
            }
            else if ("10.7".equals(this.getVersion()) || this.getVersion().startsWith("10.7.")) {
                this._codeName = "Lion";
            }
            else if ("10.8".equals(this.getVersion()) || this.getVersion().startsWith("10.8.")) {
                this._codeName = "Mountain Lion";
            }
            else if ("10.9".equals(this.getVersion()) || this.getVersion().startsWith("10.9.")) {
                this._codeName = "Mavericks";
            }
            else if ("10.10".equals(this.getVersion()) || this.getVersion().startsWith("10.10.")) {
                this._codeName = "Yosemite";
            }
        }
        return this._codeName;
    }
    
    public void setCodeName(final String _codeName) {
        this._codeName = _codeName;
    }
    
    public String getBuildNumber() {
        if (this._buildNumber == null) {
            this._buildNumber = ExecutingCommand.getFirstAnswer("sw_vers -buildVersion");
        }
        return this._buildNumber;
    }
    
    public void setBuildNumber(final String buildNumber) {
        this._buildNumber = buildNumber;
    }
    
    @Override
    public String toString() {
        if (this.version == null) {
            this.version = this.getVersion() + " (" + this.getCodeName() + ") build " + this.getBuildNumber();
        }
        return this.version;
    }
}
