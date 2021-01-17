// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.FutureTask;

public class Util
{
    public static EnumOS getOSType() {
        final String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? EnumOS.WINDOWS : (s.contains("mac") ? EnumOS.OSX : (s.contains("solaris") ? EnumOS.SOLARIS : (s.contains("sunos") ? EnumOS.SOLARIS : (s.contains("linux") ? EnumOS.LINUX : (s.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }
    
    public static <V> V func_181617_a(final FutureTask<V> p_181617_0_, final Logger p_181617_1_) {
        try {
            p_181617_0_.run();
            return p_181617_0_.get();
        }
        catch (ExecutionException executionexception) {
            p_181617_1_.fatal("Error executing task", executionexception);
        }
        catch (InterruptedException interruptedexception) {
            p_181617_1_.fatal("Error executing task", interruptedexception);
        }
        return null;
    }
    
    public enum EnumOS
    {
        LINUX("LINUX", 0), 
        SOLARIS("SOLARIS", 1), 
        WINDOWS("WINDOWS", 2), 
        OSX("OSX", 3), 
        UNKNOWN("UNKNOWN", 4);
        
        private EnumOS(final String name, final int ordinal) {
        }
    }
}
