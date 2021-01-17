// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.util;

import java.io.InputStream;
import java.util.Map;
import java.io.IOException;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

public class VersionInfo
{
    public static final String UNAVAILABLE = "UNAVAILABLE";
    public static final String VERSION_PROPERTY_FILE = "version.properties";
    public static final String PROPERTY_MODULE = "info.module";
    public static final String PROPERTY_RELEASE = "info.release";
    public static final String PROPERTY_TIMESTAMP = "info.timestamp";
    private final String infoPackage;
    private final String infoModule;
    private final String infoRelease;
    private final String infoTimestamp;
    private final String infoClassloader;
    
    protected VersionInfo(final String pckg, final String module, final String release, final String time, final String clsldr) {
        Args.notNull(pckg, "Package identifier");
        this.infoPackage = pckg;
        this.infoModule = ((module != null) ? module : "UNAVAILABLE");
        this.infoRelease = ((release != null) ? release : "UNAVAILABLE");
        this.infoTimestamp = ((time != null) ? time : "UNAVAILABLE");
        this.infoClassloader = ((clsldr != null) ? clsldr : "UNAVAILABLE");
    }
    
    public final String getPackage() {
        return this.infoPackage;
    }
    
    public final String getModule() {
        return this.infoModule;
    }
    
    public final String getRelease() {
        return this.infoRelease;
    }
    
    public final String getTimestamp() {
        return this.infoTimestamp;
    }
    
    public final String getClassloader() {
        return this.infoClassloader;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(20 + this.infoPackage.length() + this.infoModule.length() + this.infoRelease.length() + this.infoTimestamp.length() + this.infoClassloader.length());
        sb.append("VersionInfo(").append(this.infoPackage).append(':').append(this.infoModule);
        if (!"UNAVAILABLE".equals(this.infoRelease)) {
            sb.append(':').append(this.infoRelease);
        }
        if (!"UNAVAILABLE".equals(this.infoTimestamp)) {
            sb.append(':').append(this.infoTimestamp);
        }
        sb.append(')');
        if (!"UNAVAILABLE".equals(this.infoClassloader)) {
            sb.append('@').append(this.infoClassloader);
        }
        return sb.toString();
    }
    
    public static VersionInfo[] loadVersionInfo(final String[] pckgs, final ClassLoader clsldr) {
        Args.notNull(pckgs, "Package identifier array");
        final List<VersionInfo> vil = new ArrayList<VersionInfo>(pckgs.length);
        for (final String pckg : pckgs) {
            final VersionInfo vi = loadVersionInfo(pckg, clsldr);
            if (vi != null) {
                vil.add(vi);
            }
        }
        return vil.toArray(new VersionInfo[vil.size()]);
    }
    
    public static VersionInfo loadVersionInfo(final String pckg, final ClassLoader clsldr) {
        Args.notNull(pckg, "Package identifier");
        final ClassLoader cl = (clsldr != null) ? clsldr : Thread.currentThread().getContextClassLoader();
        Properties vip = null;
        try {
            final InputStream is = cl.getResourceAsStream(pckg.replace('.', '/') + "/" + "version.properties");
            if (is != null) {
                try {
                    final Properties props = new Properties();
                    props.load(is);
                    vip = props;
                }
                finally {
                    is.close();
                }
            }
        }
        catch (IOException ex) {}
        VersionInfo result = null;
        if (vip != null) {
            result = fromMap(pckg, vip, cl);
        }
        return result;
    }
    
    protected static VersionInfo fromMap(final String pckg, final Map<?, ?> info, final ClassLoader clsldr) {
        Args.notNull(pckg, "Package identifier");
        String module = null;
        String release = null;
        String timestamp = null;
        if (info != null) {
            module = (String)info.get("info.module");
            if (module != null && module.length() < 1) {
                module = null;
            }
            release = (String)info.get("info.release");
            if (release != null && (release.length() < 1 || release.equals("${pom.version}"))) {
                release = null;
            }
            timestamp = (String)info.get("info.timestamp");
            if (timestamp != null && (timestamp.length() < 1 || timestamp.equals("${mvn.timestamp}"))) {
                timestamp = null;
            }
        }
        String clsldrstr = null;
        if (clsldr != null) {
            clsldrstr = clsldr.toString();
        }
        return new VersionInfo(pckg, module, release, timestamp, clsldrstr);
    }
    
    public static String getUserAgent(final String name, final String pkg, final Class<?> cls) {
        final VersionInfo vi = loadVersionInfo(pkg, cls.getClassLoader());
        final String release = (vi != null) ? vi.getRelease() : "UNAVAILABLE";
        final String javaVersion = System.getProperty("java.version");
        return name + "/" + release + " (Java 1.5 minimum; Java/" + javaVersion + ")";
    }
}
