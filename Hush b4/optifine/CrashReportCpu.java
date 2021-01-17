// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.OpenGlHelper;
import java.util.concurrent.Callable;

public class CrashReportCpu implements Callable
{
    @Override
    public Object call() throws Exception {
        return OpenGlHelper.func_183029_j();
    }
}
