// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.mac.local;

import java.util.ArrayList;
import oshi.util.ExecutingCommand;
import oshi.hardware.Processor;

public class CentralProcessor implements Processor
{
    private String _vendor;
    private String _name;
    private String _identifier;
    private String _stepping;
    private String _model;
    private String _family;
    private Boolean _cpu64;
    
    public CentralProcessor() {
        this._identifier = null;
    }
    
    public String getVendor() {
        if (this._vendor == null) {
            this._vendor = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.vendor");
        }
        return this._vendor;
    }
    
    public void setVendor(final String vendor) {
        this._vendor = vendor;
    }
    
    public String getName() {
        if (this._name == null) {
            this._name = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.brand_string");
        }
        return this._name;
    }
    
    public void setName(final String name) {
        this._name = name;
    }
    
    public String getIdentifier() {
        if (this._identifier == null) {
            final StringBuilder sb = new StringBuilder();
            if (this.getVendor().contentEquals("GenuineIntel")) {
                sb.append(this.isCpu64bit() ? "Intel64" : "x86");
            }
            else {
                sb.append(this.getVendor());
            }
            sb.append(" Family ");
            sb.append(this.getFamily());
            sb.append(" Model ");
            sb.append(this.getModel());
            sb.append(" Stepping ");
            sb.append(this.getStepping());
            this._identifier = sb.toString();
        }
        return this._identifier;
    }
    
    public void setIdentifier(final String identifier) {
        this._identifier = identifier;
    }
    
    public boolean isCpu64bit() {
        if (this._cpu64 == null) {
            this._cpu64 = ExecutingCommand.getFirstAnswer("sysctl -n hw.cpu64bit_capable").equals("1");
        }
        return this._cpu64;
    }
    
    public void setCpu64(final boolean cpu64) {
        this._cpu64 = cpu64;
    }
    
    public String getStepping() {
        if (this._stepping == null) {
            this._stepping = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.stepping");
        }
        return this._stepping;
    }
    
    public void setStepping(final String _stepping) {
        this._stepping = _stepping;
    }
    
    public String getModel() {
        if (this._model == null) {
            this._model = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.model");
        }
        return this._model;
    }
    
    public void setModel(final String _model) {
        this._model = _model;
    }
    
    public String getFamily() {
        if (this._family == null) {
            this._family = ExecutingCommand.getFirstAnswer("sysctl -n machdep.cpu.family");
        }
        return this._family;
    }
    
    public void setFamily(final String _family) {
        this._family = _family;
    }
    
    public float getLoad() {
        final ArrayList<String> topResult = ExecutingCommand.runNative("top -l 1 -R -F -n1");
        final String[] idle = topResult.get(3).split(" ");
        return 100.0f - Float.valueOf(idle[6].replace("%", ""));
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
