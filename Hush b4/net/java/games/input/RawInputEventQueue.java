// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.Set;
import java.util.HashSet;
import java.io.IOException;
import java.util.List;

final class RawInputEventQueue
{
    private final Object monitor;
    private List devices;
    
    RawInputEventQueue() {
        this.monitor = new Object();
    }
    
    public final void start(final List devices) throws IOException {
        this.devices = devices;
        final QueueThread queue = new QueueThread();
        synchronized (this.monitor) {
            queue.start();
            while (!queue.isInitialized()) {
                try {
                    this.monitor.wait();
                }
                catch (InterruptedException e) {}
            }
        }
        if (queue.getException() != null) {
            throw queue.getException();
        }
    }
    
    private final RawDevice lookupDevice(final long handle) {
        for (int i = 0; i < this.devices.size(); ++i) {
            final RawDevice device = this.devices.get(i);
            if (device.getHandle() == handle) {
                return device;
            }
        }
        return null;
    }
    
    private final void addMouseEvent(final long handle, final long millis, final int flags, final int button_flags, final int button_data, final long raw_buttons, final long last_x, final long last_y, final long extra_information) {
        final RawDevice device = this.lookupDevice(handle);
        if (device == null) {
            return;
        }
        device.addMouseEvent(millis, flags, button_flags, button_data, raw_buttons, last_x, last_y, extra_information);
    }
    
    private final void addKeyboardEvent(final long handle, final long millis, final int make_code, final int flags, final int vkey, final int message, final long extra_information) {
        final RawDevice device = this.lookupDevice(handle);
        if (device == null) {
            return;
        }
        device.addKeyboardEvent(millis, make_code, flags, vkey, message, extra_information);
    }
    
    private final void poll(final DummyWindow window) throws IOException {
        this.nPoll(window.getHwnd());
    }
    
    private final native void nPoll(final long p0) throws IOException;
    
    private static final void registerDevices(final DummyWindow window, final RawDeviceInfo[] devices) throws IOException {
        nRegisterDevices(0, window.getHwnd(), devices);
    }
    
    private static final native void nRegisterDevices(final int p0, final long p1, final RawDeviceInfo[] p2) throws IOException;
    
    private final class QueueThread extends Thread
    {
        private boolean initialized;
        private DummyWindow window;
        private IOException exception;
        
        public QueueThread() {
            this.setDaemon(true);
        }
        
        public final boolean isInitialized() {
            return this.initialized;
        }
        
        public final IOException getException() {
            return this.exception;
        }
        
        public final void run() {
            try {
                this.window = new DummyWindow();
            }
            catch (IOException e) {
                this.exception = e;
            }
            this.initialized = true;
            synchronized (RawInputEventQueue.this.monitor) {
                RawInputEventQueue.this.monitor.notify();
            }
            if (this.exception != null) {
                return;
            }
            final Set active_infos = new HashSet();
            try {
                for (int i = 0; i < RawInputEventQueue.this.devices.size(); ++i) {
                    final RawDevice device = RawInputEventQueue.this.devices.get(i);
                    active_infos.add(device.getInfo());
                }
                final RawDeviceInfo[] active_infos_array = new RawDeviceInfo[active_infos.size()];
                active_infos.toArray(active_infos_array);
                try {
                    registerDevices(this.window, active_infos_array);
                    while (!this.isInterrupted()) {
                        RawInputEventQueue.this.poll(this.window);
                    }
                }
                finally {
                    this.window.destroy();
                }
            }
            catch (IOException e2) {
                this.exception = e2;
            }
        }
    }
}
