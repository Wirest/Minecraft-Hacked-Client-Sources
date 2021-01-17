// 
// Decompiled by Procyon v0.5.36
// 

package tv.twitch;

public abstract class CoreAPI
{
    public abstract ErrorCode init(final String p0, final String p1);
    
    public abstract ErrorCode shutdown();
    
    public abstract ErrorCode setTraceLevel(final MessageLevel p0);
    
    public abstract ErrorCode setTraceOutput(final String p0);
    
    public abstract String errorToString(final ErrorCode p0);
}
