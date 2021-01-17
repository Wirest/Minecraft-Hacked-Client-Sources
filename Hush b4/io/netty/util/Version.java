// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Set;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TreeMap;
import java.util.HashSet;
import java.net.URL;
import java.util.Properties;
import io.netty.util.internal.PlatformDependent;
import java.util.Map;

public final class Version
{
    private static final String PROP_VERSION = ".version";
    private static final String PROP_BUILD_DATE = ".buildDate";
    private static final String PROP_COMMIT_DATE = ".commitDate";
    private static final String PROP_SHORT_COMMIT_HASH = ".shortCommitHash";
    private static final String PROP_LONG_COMMIT_HASH = ".longCommitHash";
    private static final String PROP_REPO_STATUS = ".repoStatus";
    private final String artifactId;
    private final String artifactVersion;
    private final long buildTimeMillis;
    private final long commitTimeMillis;
    private final String shortCommitHash;
    private final String longCommitHash;
    private final String repositoryStatus;
    
    public static Map<String, Version> identify() {
        return identify(null);
    }
    
    public static Map<String, Version> identify(ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = PlatformDependent.getContextClassLoader();
        }
        final Properties props = new Properties();
        try {
            final Enumeration<URL> resources = classLoader.getResources("META-INF/io.netty.versions.properties");
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                final InputStream in = url.openStream();
                try {
                    props.load(in);
                }
                finally {
                    try {
                        in.close();
                    }
                    catch (Exception ex) {}
                }
            }
        }
        catch (Exception ex2) {}
        final Set<String> artifactIds = new HashSet<String>();
        for (final Object o : props.keySet()) {
            final String k = (String)o;
            final int dotIndex = k.indexOf(46);
            if (dotIndex <= 0) {
                continue;
            }
            final String artifactId = k.substring(0, dotIndex);
            if (!props.containsKey(artifactId + ".version") || !props.containsKey(artifactId + ".buildDate") || !props.containsKey(artifactId + ".commitDate") || !props.containsKey(artifactId + ".shortCommitHash") || !props.containsKey(artifactId + ".longCommitHash")) {
                continue;
            }
            if (!props.containsKey(artifactId + ".repoStatus")) {
                continue;
            }
            artifactIds.add(artifactId);
        }
        final Map<String, Version> versions = new TreeMap<String, Version>();
        for (final String artifactId2 : artifactIds) {
            versions.put(artifactId2, new Version(artifactId2, props.getProperty(artifactId2 + ".version"), parseIso8601(props.getProperty(artifactId2 + ".buildDate")), parseIso8601(props.getProperty(artifactId2 + ".commitDate")), props.getProperty(artifactId2 + ".shortCommitHash"), props.getProperty(artifactId2 + ".longCommitHash"), props.getProperty(artifactId2 + ".repoStatus")));
        }
        return versions;
    }
    
    private static long parseIso8601(final String value) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(value).getTime();
        }
        catch (ParseException ignored) {
            return 0L;
        }
    }
    
    public static void main(final String[] args) {
        for (final Version v : identify().values()) {
            System.err.println(v);
        }
    }
    
    private Version(final String artifactId, final String artifactVersion, final long buildTimeMillis, final long commitTimeMillis, final String shortCommitHash, final String longCommitHash, final String repositoryStatus) {
        this.artifactId = artifactId;
        this.artifactVersion = artifactVersion;
        this.buildTimeMillis = buildTimeMillis;
        this.commitTimeMillis = commitTimeMillis;
        this.shortCommitHash = shortCommitHash;
        this.longCommitHash = longCommitHash;
        this.repositoryStatus = repositoryStatus;
    }
    
    public String artifactId() {
        return this.artifactId;
    }
    
    public String artifactVersion() {
        return this.artifactVersion;
    }
    
    public long buildTimeMillis() {
        return this.buildTimeMillis;
    }
    
    public long commitTimeMillis() {
        return this.commitTimeMillis;
    }
    
    public String shortCommitHash() {
        return this.shortCommitHash;
    }
    
    public String longCommitHash() {
        return this.longCommitHash;
    }
    
    public String repositoryStatus() {
        return this.repositoryStatus;
    }
    
    @Override
    public String toString() {
        return this.artifactId + '-' + this.artifactVersion + '.' + this.shortCommitHash + ("clean".equals(this.repositoryStatus) ? "" : (" (repository: " + this.repositoryStatus + ')'));
    }
}
