// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

public final class Epoll
{
    private static final Throwable UNAVAILABILITY_CAUSE;
    
    public static boolean isAvailable() {
        return Epoll.UNAVAILABILITY_CAUSE == null;
    }
    
    public static void ensureAvailability() {
        if (Epoll.UNAVAILABILITY_CAUSE != null) {
            throw (Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(Epoll.UNAVAILABILITY_CAUSE);
        }
    }
    
    public static Throwable unavailabilityCause() {
        return Epoll.UNAVAILABILITY_CAUSE;
    }
    
    private Epoll() {
    }
    
    static {
        Throwable cause = null;
        int epollFd = -1;
        int eventFd = -1;
        try {
            epollFd = Native.epollCreate();
            eventFd = Native.eventFd();
        }
        catch (Throwable t) {
            cause = t;
        }
        finally {
            if (epollFd != -1) {
                try {
                    Native.close(epollFd);
                }
                catch (Exception ex) {}
            }
            if (eventFd != -1) {
                try {
                    Native.close(eventFd);
                }
                catch (Exception ex2) {}
            }
        }
        if (cause != null) {
            UNAVAILABILITY_CAUSE = cause;
        }
        else {
            UNAVAILABILITY_CAUSE = null;
        }
    }
}
