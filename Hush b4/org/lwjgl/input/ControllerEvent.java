// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.input;

class ControllerEvent
{
    public static final int BUTTON = 1;
    public static final int AXIS = 2;
    public static final int POVX = 3;
    public static final int POVY = 4;
    private Controller source;
    private int index;
    private int type;
    private boolean buttonState;
    private boolean xaxis;
    private boolean yaxis;
    private long timeStamp;
    private float xaxisValue;
    private float yaxisValue;
    
    ControllerEvent(final Controller source, final long timeStamp, final int type, final int index, final boolean xaxis, final boolean yaxis) {
        this(source, timeStamp, type, index, false, xaxis, yaxis, 0.0f, 0.0f);
    }
    
    ControllerEvent(final Controller source, final long timeStamp, final int type, final int index, final boolean buttonState, final boolean xaxis, final boolean yaxis, final float xaxisValue, final float yaxisValue) {
        this.source = source;
        this.timeStamp = timeStamp;
        this.type = type;
        this.index = index;
        this.buttonState = buttonState;
        this.xaxis = xaxis;
        this.yaxis = yaxis;
        this.xaxisValue = xaxisValue;
        this.yaxisValue = yaxisValue;
    }
    
    public long getTimeStamp() {
        return this.timeStamp;
    }
    
    public Controller getSource() {
        return this.source;
    }
    
    public int getControlIndex() {
        return this.index;
    }
    
    public boolean isButton() {
        return this.type == 1;
    }
    
    public boolean getButtonState() {
        return this.buttonState;
    }
    
    public boolean isAxis() {
        return this.type == 2;
    }
    
    public boolean isPovY() {
        return this.type == 4;
    }
    
    public boolean isPovX() {
        return this.type == 3;
    }
    
    public boolean isXAxis() {
        return this.xaxis;
    }
    
    public boolean isYAxis() {
        return this.yaxis;
    }
    
    public float getXAxisValue() {
        return this.xaxisValue;
    }
    
    public float getYAxisValue() {
        return this.yaxisValue;
    }
    
    @Override
    public String toString() {
        return "[" + this.source + " type=" + this.type + " xaxis=" + this.xaxis + " yaxis=" + this.yaxis + "]";
    }
}
