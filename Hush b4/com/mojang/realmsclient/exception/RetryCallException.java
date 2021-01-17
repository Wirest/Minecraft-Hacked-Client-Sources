// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.exception;

public class RetryCallException extends RealmsServiceException
{
    public static final int DEFAULT_DELAY = 5;
    public final int delaySeconds;
    
    public RetryCallException(final int delaySeconds) {
        super(503, "Retry operation", -1, "");
        if (delaySeconds < 0 || delaySeconds > 120) {
            this.delaySeconds = 5;
        }
        else {
            this.delaySeconds = delaySeconds;
        }
    }
}
