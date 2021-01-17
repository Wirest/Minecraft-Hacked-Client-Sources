// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

public final class Version
{
    private static final String apiVersion = "2.0.5";
    private static final String buildNumber = "1088";
    private static final String antBuildNumberToken = "@BUILD_NUMBER@";
    private static final String antAPIVersionToken = "@API_VERSION@";
    
    private Version() {
    }
    
    public static String getVersion() {
        String version = "Unversioned";
        if (!"@API_VERSION@".equals("2.0.5")) {
            version = "2.0.5";
        }
        if (!"@BUILD_NUMBER@".equals("1088")) {
            version += "-b1088";
        }
        return version;
    }
}
