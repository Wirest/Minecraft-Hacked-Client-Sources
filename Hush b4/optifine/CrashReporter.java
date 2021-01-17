// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import shadersmod.client.Shaders;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import java.util.HashMap;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;

public class CrashReporter
{
    public static void onCrashReport(final CrashReport p_onCrashReport_0_, final CrashReportCategory p_onCrashReport_1_) {
        try {
            final GameSettings gamesettings = Config.getGameSettings();
            if (gamesettings == null) {
                return;
            }
            if (!gamesettings.snooperEnabled) {
                return;
            }
            final Throwable throwable = p_onCrashReport_0_.getCrashCause();
            if (throwable == null) {
                return;
            }
            if (throwable.getClass() == Throwable.class) {
                return;
            }
            if (throwable.getClass().getName().contains(".fml.client.SplashProgress")) {
                return;
            }
            extendCrashReport(p_onCrashReport_1_);
            final String s = "http://optifine.net/crashReport";
            final String s2 = makeReport(p_onCrashReport_0_);
            final byte[] abyte = s2.getBytes("ASCII");
            final IFileUploadListener ifileuploadlistener = new IFileUploadListener() {
                @Override
                public void fileUploadFinished(final String p_fileUploadFinished_1_, final byte[] p_fileUploadFinished_2_, final Throwable p_fileUploadFinished_3_) {
                }
            };
            final Map map = new HashMap();
            map.put("OF-Version", Config.getVersion());
            map.put("OF-Summary", makeSummary(p_onCrashReport_0_));
            final FileUploadThread fileuploadthread = new FileUploadThread(s, map, abyte, ifileuploadlistener);
            fileuploadthread.setPriority(10);
            fileuploadthread.start();
            Thread.sleep(1000L);
        }
        catch (Exception exception) {
            Config.dbg(String.valueOf(exception.getClass().getName()) + ": " + exception.getMessage());
        }
    }
    
    private static String makeReport(final CrashReport p_makeReport_0_) {
        final StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append("OptiFineVersion: " + Config.getVersion() + "\n");
        stringbuffer.append("Summary: " + makeSummary(p_makeReport_0_) + "\n");
        stringbuffer.append("\n");
        stringbuffer.append(p_makeReport_0_.getCompleteReport());
        stringbuffer.append("\n");
        return stringbuffer.toString();
    }
    
    private static String makeSummary(final CrashReport p_makeSummary_0_) {
        final Throwable throwable = p_makeSummary_0_.getCrashCause();
        if (throwable == null) {
            return "Unknown";
        }
        final StackTraceElement[] astacktraceelement = throwable.getStackTrace();
        String s = "unknown";
        if (astacktraceelement.length > 0) {
            s = astacktraceelement[0].toString().trim();
        }
        final String s2 = String.valueOf(throwable.getClass().getName()) + ": " + throwable.getMessage() + " (" + p_makeSummary_0_.getDescription() + ")" + " [" + s + "]";
        return s2;
    }
    
    public static void extendCrashReport(final CrashReportCategory p_extendCrashReport_0_) {
        p_extendCrashReport_0_.addCrashSection("OptiFine Version", Config.getVersion());
        if (Config.getGameSettings() != null) {
            p_extendCrashReport_0_.addCrashSection("Render Distance Chunks", new StringBuilder().append(Config.getChunkViewDistance()).toString());
            p_extendCrashReport_0_.addCrashSection("Mipmaps", new StringBuilder().append(Config.getMipmapLevels()).toString());
            p_extendCrashReport_0_.addCrashSection("Anisotropic Filtering", new StringBuilder().append(Config.getAnisotropicFilterLevel()).toString());
            p_extendCrashReport_0_.addCrashSection("Antialiasing", new StringBuilder().append(Config.getAntialiasingLevel()).toString());
            p_extendCrashReport_0_.addCrashSection("Multitexture", new StringBuilder().append(Config.isMultiTexture()).toString());
        }
        p_extendCrashReport_0_.addCrashSection("Shaders", new StringBuilder().append(Shaders.getShaderPackName()).toString());
        p_extendCrashReport_0_.addCrashSection("OpenGlVersion", new StringBuilder().append(Config.openGlVersion).toString());
        p_extendCrashReport_0_.addCrashSection("OpenGlRenderer", new StringBuilder().append(Config.openGlRenderer).toString());
        p_extendCrashReport_0_.addCrashSection("OpenGlVendor", new StringBuilder().append(Config.openGlVendor).toString());
        p_extendCrashReport_0_.addCrashSection("CpuCount", new StringBuilder().append(Config.getAvailableProcessors()).toString());
    }
}
