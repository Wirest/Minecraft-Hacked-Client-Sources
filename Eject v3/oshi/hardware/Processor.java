package oshi.hardware;

public abstract interface Processor {
    public abstract String getVendor();

    public abstract void setVendor(String paramString);

    public abstract String getName();

    public abstract void setName(String paramString);

    public abstract String getIdentifier();

    public abstract void setIdentifier(String paramString);

    public abstract boolean isCpu64bit();

    public abstract void setCpu64(boolean paramBoolean);

    public abstract String getStepping();

    public abstract void setStepping(String paramString);

    public abstract String getModel();

    public abstract void setModel(String paramString);

    public abstract String getFamily();

    public abstract void setFamily(String paramString);

    public abstract float getLoad();
}




