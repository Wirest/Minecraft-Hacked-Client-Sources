// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.util.concurrent.ListenableFuture;

public interface IThreadListener
{
    ListenableFuture<Object> addScheduledTask(final Runnable p0);
    
    boolean isCallingFromMinecraftThread();
}
