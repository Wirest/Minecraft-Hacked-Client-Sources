package io.netty.channel.epoll;

public final class Epoll {
    private static final Throwable UNAVAILABILITY_CAUSE;

    static {
        Object localObject1 = null;
        int i = -1;
        int j = -1;
        try {
            i = Native.epollCreate();
            j = Native.eventFd();
            if (i != -1) {
                try {
                    Native.close(i);
                } catch (Exception localException1) {
                }
            }
            if (j != -1) {
                try {
                    Native.close(j);
                } catch (Exception localException2) {
                }
            }
            if (localObject1 == null) {
                break label119;
            }
        } catch (Throwable localThrowable) {
            localObject1 = localThrowable;
        } finally {
            if (i != -1) {
                try {
                    Native.close(i);
                } catch (Exception localException5) {
                }
            }
            if (j != -1) {
                try {
                    Native.close(j);
                } catch (Exception localException6) {
                }
            }
        }
        UNAVAILABILITY_CAUSE = (Throwable) localObject1;
        return;
        label119:
        UNAVAILABILITY_CAUSE = null;
    }

    public static boolean isAvailable() {
        return UNAVAILABILITY_CAUSE == null;
    }

    public static void ensureAvailability() {
        if (UNAVAILABILITY_CAUSE != null) {
            throw ((Error) new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
        }
    }

    public static Throwable unavailabilityCause() {
        return UNAVAILABILITY_CAUSE;
    }
}




