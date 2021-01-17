// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.List;
import java.util.ArrayList;

public class WinTabContext
{
    private DummyWindow window;
    private long hCTX;
    private Controller[] controllers;
    
    public WinTabContext(final DummyWindow window) {
        this.window = window;
    }
    
    public Controller[] getControllers() {
        if (this.hCTX == 0L) {
            throw new IllegalStateException("Context must be open before getting the controllers");
        }
        return this.controllers;
    }
    
    public synchronized void open() {
        this.hCTX = nOpen(this.window.getHwnd());
        final List devices = new ArrayList();
        for (int numSupportedDevices = nGetNumberOfSupportedDevices(), i = 0; i < numSupportedDevices; ++i) {
            final WinTabDevice newDevice = WinTabDevice.createDevice(this, i);
            if (newDevice != null) {
                devices.add(newDevice);
            }
        }
        this.controllers = devices.toArray(new Controller[0]);
    }
    
    public synchronized void close() {
        nClose(this.hCTX);
    }
    
    public synchronized void processEvents() {
        final WinTabPacket[] packets = nGetPackets(this.hCTX);
        for (int i = 0; i < packets.length; ++i) {
            ((WinTabDevice)this.getControllers()[0]).processPacket(packets[i]);
        }
    }
    
    private static final native int nGetNumberOfSupportedDevices();
    
    private static final native long nOpen(final long p0);
    
    private static final native void nClose(final long p0);
    
    private static final native WinTabPacket[] nGetPackets(final long p0);
}
