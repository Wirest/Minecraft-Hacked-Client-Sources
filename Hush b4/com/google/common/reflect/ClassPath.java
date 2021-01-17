// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.jar.Attributes;
import javax.annotation.Nullable;
import java.util.jar.Manifest;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.io.File;
import com.google.common.collect.Sets;
import java.util.Comparator;
import com.google.common.collect.Ordering;
import java.util.Set;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.base.CharMatcher;
import com.google.common.annotations.VisibleForTesting;
import java.net.URL;
import java.util.LinkedHashMap;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;
import java.io.IOException;
import java.util.Iterator;
import java.net.URI;
import java.util.Map;
import com.google.common.collect.ImmutableSet;
import com.google.common.base.Splitter;
import com.google.common.base.Predicate;
import java.util.logging.Logger;
import com.google.common.annotations.Beta;

@Beta
public final class ClassPath
{
    private static final Logger logger;
    private static final Predicate<ClassInfo> IS_TOP_LEVEL;
    private static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR;
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    private final ImmutableSet<ResourceInfo> resources;
    
    private ClassPath(final ImmutableSet<ResourceInfo> resources) {
        this.resources = resources;
    }
    
    public static ClassPath from(final ClassLoader classloader) throws IOException {
        final Scanner scanner = new Scanner();
        for (final Map.Entry<URI, ClassLoader> entry : getClassPathEntries(classloader).entrySet()) {
            scanner.scan(entry.getKey(), entry.getValue());
        }
        return new ClassPath(scanner.getResources());
    }
    
    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }
    
    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).toSet();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from(this.resources).filter(ClassInfo.class).filter(ClassPath.IS_TOP_LEVEL).toSet();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClasses(final String packageName) {
        Preconditions.checkNotNull(packageName);
        final ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        for (final ClassInfo classInfo : this.getTopLevelClasses()) {
            if (classInfo.getPackageName().equals(packageName)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }
    
    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(final String packageName) {
        Preconditions.checkNotNull(packageName);
        final String packagePrefix = packageName + '.';
        final ImmutableSet.Builder<ClassInfo> builder = ImmutableSet.builder();
        for (final ClassInfo classInfo : this.getTopLevelClasses()) {
            if (classInfo.getName().startsWith(packagePrefix)) {
                builder.add(classInfo);
            }
        }
        return builder.build();
    }
    
    @VisibleForTesting
    static ImmutableMap<URI, ClassLoader> getClassPathEntries(final ClassLoader classloader) {
        final LinkedHashMap<URI, ClassLoader> entries = Maps.newLinkedHashMap();
        final ClassLoader parent = classloader.getParent();
        if (parent != null) {
            entries.putAll((Map<?, ?>)getClassPathEntries(parent));
        }
        if (classloader instanceof URLClassLoader) {
            final URLClassLoader urlClassLoader = (URLClassLoader)classloader;
            for (final URL entry : urlClassLoader.getURLs()) {
                URI uri;
                try {
                    uri = entry.toURI();
                }
                catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
                if (!entries.containsKey(uri)) {
                    entries.put(uri, classloader);
                }
            }
        }
        return ImmutableMap.copyOf((Map<? extends URI, ? extends ClassLoader>)entries);
    }
    
    @VisibleForTesting
    static String getClassName(final String filename) {
        final int classNameEnd = filename.length() - ".class".length();
        return filename.substring(0, classNameEnd).replace('/', '.');
    }
    
    static {
        logger = Logger.getLogger(ClassPath.class.getName());
        IS_TOP_LEVEL = new Predicate<ClassInfo>() {
            @Override
            public boolean apply(final ClassInfo info) {
                return info.className.indexOf(36) == -1;
            }
        };
        CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.on(" ").omitEmptyStrings();
    }
    
    @Beta
    public static class ResourceInfo
    {
        private final String resourceName;
        final ClassLoader loader;
        
        static ResourceInfo of(final String resourceName, final ClassLoader loader) {
            if (resourceName.endsWith(".class")) {
                return new ClassInfo(resourceName, loader);
            }
            return new ResourceInfo(resourceName, loader);
        }
        
        ResourceInfo(final String resourceName, final ClassLoader loader) {
            this.resourceName = Preconditions.checkNotNull(resourceName);
            this.loader = Preconditions.checkNotNull(loader);
        }
        
        public final URL url() {
            return Preconditions.checkNotNull(this.loader.getResource(this.resourceName), "Failed to load resource: %s", this.resourceName);
        }
        
        public final String getResourceName() {
            return this.resourceName;
        }
        
        @Override
        public int hashCode() {
            return this.resourceName.hashCode();
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof ResourceInfo) {
                final ResourceInfo that = (ResourceInfo)obj;
                return this.resourceName.equals(that.resourceName) && this.loader == that.loader;
            }
            return false;
        }
        
        @Override
        public String toString() {
            return this.resourceName;
        }
    }
    
    @Beta
    public static final class ClassInfo extends ResourceInfo
    {
        private final String className;
        
        ClassInfo(final String resourceName, final ClassLoader loader) {
            super(resourceName, loader);
            this.className = ClassPath.getClassName(resourceName);
        }
        
        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }
        
        public String getSimpleName() {
            final int lastDollarSign = this.className.lastIndexOf(36);
            if (lastDollarSign != -1) {
                final String innerClassName = this.className.substring(lastDollarSign + 1);
                return CharMatcher.DIGIT.trimLeadingFrom(innerClassName);
            }
            final String packageName = this.getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }
        
        public String getName() {
            return this.className;
        }
        
        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            }
            catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        
        @Override
        public String toString() {
            return this.className;
        }
    }
    
    @VisibleForTesting
    static final class Scanner
    {
        private final ImmutableSortedSet.Builder<ResourceInfo> resources;
        private final Set<URI> scannedUris;
        
        Scanner() {
            this.resources = new ImmutableSortedSet.Builder<ResourceInfo>(Ordering.usingToString());
            this.scannedUris = (Set<URI>)Sets.newHashSet();
        }
        
        ImmutableSortedSet<ResourceInfo> getResources() {
            return this.resources.build();
        }
        
        void scan(final URI uri, final ClassLoader classloader) throws IOException {
            if (uri.getScheme().equals("file") && this.scannedUris.add(uri)) {
                this.scanFrom(new File(uri), classloader);
            }
        }
        
        @VisibleForTesting
        void scanFrom(final File file, final ClassLoader classloader) throws IOException {
            if (!file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                this.scanDirectory(file, classloader);
            }
            else {
                this.scanJar(file, classloader);
            }
        }
        
        private void scanDirectory(final File directory, final ClassLoader classloader) throws IOException {
            this.scanDirectory(directory, classloader, "", ImmutableSet.of());
        }
        
        private void scanDirectory(final File directory, final ClassLoader classloader, final String packagePrefix, final ImmutableSet<File> ancestors) throws IOException {
            final File canonical = directory.getCanonicalFile();
            if (ancestors.contains(canonical)) {
                return;
            }
            final File[] files = directory.listFiles();
            if (files == null) {
                ClassPath.logger.warning("Cannot read directory " + directory);
                return;
            }
            final ImmutableSet<File> newAncestors = ImmutableSet.builder().addAll(ancestors).add(canonical).build();
            for (final File f : files) {
                final String name = f.getName();
                if (f.isDirectory()) {
                    this.scanDirectory(f, classloader, packagePrefix + name + "/", newAncestors);
                }
                else {
                    final String resourceName = packagePrefix + name;
                    if (!resourceName.equals("META-INF/MANIFEST.MF")) {
                        this.resources.add(ResourceInfo.of(resourceName, classloader));
                    }
                }
            }
        }
        
        private void scanJar(final File file, final ClassLoader classloader) throws IOException {
            JarFile jarFile;
            try {
                jarFile = new JarFile(file);
            }
            catch (IOException e) {
                return;
            }
            try {
                for (final URI uri : getClassPathFromManifest(file, jarFile.getManifest())) {
                    this.scan(uri, classloader);
                }
                final Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    final JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        if (entry.getName().equals("META-INF/MANIFEST.MF")) {
                            continue;
                        }
                        this.resources.add(ResourceInfo.of(entry.getName(), classloader));
                    }
                }
            }
            finally {
                try {
                    jarFile.close();
                }
                catch (IOException ex) {}
            }
        }
        
        @VisibleForTesting
        static ImmutableSet<URI> getClassPathFromManifest(final File jarFile, @Nullable final Manifest manifest) {
            if (manifest == null) {
                return ImmutableSet.of();
            }
            final ImmutableSet.Builder<URI> builder = ImmutableSet.builder();
            final String classpathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
            if (classpathAttribute != null) {
                for (final String path : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(classpathAttribute)) {
                    URI uri;
                    try {
                        uri = getClassPathEntry(jarFile, path);
                    }
                    catch (URISyntaxException e) {
                        ClassPath.logger.warning("Invalid Class-Path entry: " + path);
                        continue;
                    }
                    builder.add(uri);
                }
            }
            return builder.build();
        }
        
        @VisibleForTesting
        static URI getClassPathEntry(final File jarFile, final String path) throws URISyntaxException {
            final URI uri = new URI(path);
            if (uri.isAbsolute()) {
                return uri;
            }
            return new File(jarFile.getParentFile(), path.replace('/', File.separatorChar)).toURI();
        }
    }
}
