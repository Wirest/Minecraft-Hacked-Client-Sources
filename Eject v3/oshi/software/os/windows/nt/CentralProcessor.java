package oshi.software.os.windows.nt;

import oshi.hardware.Processor;

public class CentralProcessor
        implements Processor {
    private String _vendor;
    private String _name;
    private String _identifier;

    public String getVendor() {
        return this._vendor;
    }

    public void setVendor(String paramString) {
        this._vendor = paramString;
    }

    public String getName() {
        return this._name;
    }

    public void setName(String paramString) {
        this._name = paramString;
    }

    public String getIdentifier() {
        return this._identifier;
    }

    public void setIdentifier(String paramString) {
        this._identifier = paramString;
    }

    public boolean isCpu64bit() {
        throw new UnsupportedOperationException();
    }

    public void setCpu64(boolean paramBoolean) {
        throw new UnsupportedOperationException();
    }

    public String getStepping() {
        throw new UnsupportedOperationException();
    }

    public void setStepping(String paramString) {
        throw new UnsupportedOperationException();
    }

    public String getModel() {
        throw new UnsupportedOperationException();
    }

    public void setModel(String paramString) {
        throw new UnsupportedOperationException();
    }

    public String getFamily() {
        throw new UnsupportedOperationException();
    }

    public void setFamily(String paramString) {
        throw new UnsupportedOperationException();
    }

    public float getLoad() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        return this._name;
    }
}




