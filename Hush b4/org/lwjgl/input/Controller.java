// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

public interface Controller
{
    String getName();
    
    int getIndex();
    
    int getButtonCount();
    
    String getButtonName(final int p0);
    
    boolean isButtonPressed(final int p0);
    
    void poll();
    
    float getPovX();
    
    float getPovY();
    
    float getDeadZone(final int p0);
    
    void setDeadZone(final int p0, final float p1);
    
    int getAxisCount();
    
    String getAxisName(final int p0);
    
    float getAxisValue(final int p0);
    
    float getXAxisValue();
    
    float getXAxisDeadZone();
    
    void setXAxisDeadZone(final float p0);
    
    float getYAxisValue();
    
    float getYAxisDeadZone();
    
    void setYAxisDeadZone(final float p0);
    
    float getZAxisValue();
    
    float getZAxisDeadZone();
    
    void setZAxisDeadZone(final float p0);
    
    float getRXAxisValue();
    
    float getRXAxisDeadZone();
    
    void setRXAxisDeadZone(final float p0);
    
    float getRYAxisValue();
    
    float getRYAxisDeadZone();
    
    void setRYAxisDeadZone(final float p0);
    
    float getRZAxisValue();
    
    float getRZAxisDeadZone();
    
    void setRZAxisDeadZone(final float p0);
    
    int getRumblerCount();
    
    String getRumblerName(final int p0);
    
    void setRumblerStrength(final int p0, final float p1);
}
