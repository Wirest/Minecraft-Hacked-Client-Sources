package optifine;

import java.util.concurrent.Callable;
import net.minecraft.client.renderer.OpenGlHelper;

public class CrashReportCpu implements Callable<String>
{
    public String call() throws Exception
    {
        return OpenGlHelper.getCpu();
    }
}
