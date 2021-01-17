// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os.linux.proc;

import oshi.util.FormatUtil;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import oshi.hardware.Processor;

public class CentralProcessor implements Processor
{
    private String _vendor;
    private String _name;
    private String _identifier;
    private String _stepping;
    private String _model;
    private String _family;
    private boolean _cpu64;
    
    public CentralProcessor() {
        this._identifier = null;
    }
    
    public String getVendor() {
        return this._vendor;
    }
    
    public void setVendor(final String vendor) {
        this._vendor = vendor;
    }
    
    public String getName() {
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
        return this._cpu64;
    }
    
    public void setCpu64(final boolean cpu64) {
        this._cpu64 = cpu64;
    }
    
    public String getStepping() {
        return this._stepping;
    }
    
    public void setStepping(final String _stepping) {
        this._stepping = _stepping;
    }
    
    public String getModel() {
        return this._model;
    }
    
    public void setModel(final String _model) {
        this._model = _model;
    }
    
    public String getFamily() {
        return this._family;
    }
    
    public void setFamily(final String _family) {
        this._family = _family;
    }
    
    public float getLoad() {
        Scanner in = null;
        try {
            in = new Scanner(new FileReader("/proc/stat"));
        }
        catch (FileNotFoundException e) {
            System.err.println("Problem with: /proc/stat");
            System.err.println(e.getMessage());
            return -1.0f;
        }
        in.useDelimiter("\n");
        final String[] result = in.next().split(" ");
        final ArrayList<Float> loads = new ArrayList<Float>();
        for (final String load : result) {
            if (load.matches("-?\\d+(\\.\\d+)?")) {
                loads.add(Float.valueOf(load));
            }
        }
        final float totalCpuLoad = (loads.get(0) + loads.get(2)) * 100.0f / (loads.get(0) + loads.get(2) + loads.get(3));
        return FormatUtil.round(totalCpuLoad, 2);
    }
    
    @Override
    public String toString() {
        return this.getName();
    }
}
