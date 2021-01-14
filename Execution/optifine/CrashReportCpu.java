package optifine;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.OpenGlHelper;

public class CrashReportCpu implements Callable
{
    public Object call() throws Exception
    {
        return OpenGlHelper.func_183029_j();
    }
}
