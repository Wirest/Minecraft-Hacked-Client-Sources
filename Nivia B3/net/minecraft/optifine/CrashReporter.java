package net.minecraft.optifine;

import java.util.HashMap;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;

public class CrashReporter
{
    public static void onCrashReport(CrashReport crashReport)
    {
        try
        {
            GameSettings e = Config.getGameSettings();

            if (e == null)
            {
                return;
            }

            if (!e.snooperEnabled)
            {
                return;
            }

            Throwable cause = crashReport.getCrashCause();

            if (cause == null)
            {
                return;
            }

            if (cause.getClass() == Throwable.class)
            {
                return;
            }

            if (cause.getClass().getName().contains(".fml.client.SplashProgress"))
            {
                return;
            }

            String url = "http://optifine.net/crashReport";
            String reportStr = makeReport(crashReport);
            byte[] content = reportStr.getBytes("ASCII");
            IFileUploadListener listener = new IFileUploadListener()
            {
                public void fileUploadFinished(String url, byte[] content, Throwable exception) {}
            };
            HashMap headers = new HashMap();
            headers.put("OF-Version", Config.getVersion());
            headers.put("OF-Summary", makeSummary(crashReport));
            FileUploadThread fut = new FileUploadThread(url, headers, content, listener);
            fut.setPriority(10);
            fut.start();
            Thread.sleep(1000L);
        }
        catch (Exception var9)
        {
            Config.dbg(var9.getClass().getName() + ": " + var9.getMessage());
        }
    }

    private static String makeReport(CrashReport crashReport)
    {
        StringBuffer sb = new StringBuffer();
        sb.append("OptiFineVersion: " + Config.getVersion() + "\n");
        sb.append("Summary: " + makeSummary(crashReport) + "\n");
        sb.append("\n");
        sb.append(crashReport.getCompleteReport());
        sb.append("\n");
        sb.append("OpenGlVersion: " + Config.openGlVersion + "\n");
        sb.append("OpenGlRenderer: " + Config.openGlRenderer + "\n");
        sb.append("OpenGlVendor: " + Config.openGlVendor + "\n");
        sb.append("CpuCount: " + Config.getAvailableProcessors() + "\n");
        return sb.toString();
    }

    private static String makeSummary(CrashReport crashReport)
    {
        Throwable t = crashReport.getCrashCause();

        if (t == null)
        {
            return "Unknown";
        }
        else
        {
            StackTraceElement[] traces = t.getStackTrace();
            String firstTrace = "unknown";

            if (traces.length > 0)
            {
                firstTrace = traces[0].toString().trim();
            }

            String sum = t.getClass().getName() + ": " + t.getMessage() + " (" + crashReport.getDescription() + ")" + " [" + firstTrace + "]";
            return sum;
        }
    }
}
