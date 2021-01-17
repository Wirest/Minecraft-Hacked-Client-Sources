// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins;

import org.apache.logging.log4j.status.StatusLogger;
import java.util.jar.JarEntry;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Collection;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import java.util.Enumeration;
import java.io.File;
import java.util.jar.JarInputStream;
import java.net.URLDecoder;
import org.apache.logging.log4j.core.helpers.Charsets;
import java.net.URL;
import java.io.IOException;
import java.lang.annotation.Annotation;
import org.apache.logging.log4j.core.helpers.Loader;
import java.util.HashSet;
import java.net.URI;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class ResolverUtil
{
    private static final Logger LOGGER;
    private static final String VFSZIP = "vfszip";
    private static final String BUNDLE_RESOURCE = "bundleresource";
    private final Set<Class<?>> classMatches;
    private final Set<URI> resourceMatches;
    private ClassLoader classloader;
    
    public ResolverUtil() {
        this.classMatches = new HashSet<Class<?>>();
        this.resourceMatches = new HashSet<URI>();
    }
    
    public Set<Class<?>> getClasses() {
        return this.classMatches;
    }
    
    public Set<URI> getResources() {
        return this.resourceMatches;
    }
    
    public ClassLoader getClassLoader() {
        return (this.classloader != null) ? this.classloader : (this.classloader = Loader.getClassLoader(ResolverUtil.class, null));
    }
    
    public void setClassLoader(final ClassLoader classloader) {
        this.classloader = classloader;
    }
    
    public void findImplementations(final Class<?> parent, final String... packageNames) {
        if (packageNames == null) {
            return;
        }
        final Test test = new IsA(parent);
        for (final String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }
    
    public void findSuffix(final String suffix, final String... packageNames) {
        if (packageNames == null) {
            return;
        }
        final Test test = new NameEndsWith(suffix);
        for (final String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }
    
    public void findAnnotated(final Class<? extends Annotation> annotation, final String... packageNames) {
        if (packageNames == null) {
            return;
        }
        final Test test = new AnnotatedWith(annotation);
        for (final String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }
    
    public void findNamedResource(final String name, final String... pathNames) {
        if (pathNames == null) {
            return;
        }
        final Test test = new NameIs(name);
        for (final String pkg : pathNames) {
            this.findInPackage(test, pkg);
        }
    }
    
    public void find(final Test test, final String... packageNames) {
        if (packageNames == null) {
            return;
        }
        for (final String pkg : packageNames) {
            this.findInPackage(test, pkg);
        }
    }
    
    public void findInPackage(final Test test, String packageName) {
        packageName = packageName.replace('.', '/');
        final ClassLoader loader = this.getClassLoader();
        Enumeration<URL> urls;
        try {
            urls = loader.getResources(packageName);
        }
        catch (IOException ioe) {
            ResolverUtil.LOGGER.warn("Could not read package: " + packageName, ioe);
            return;
        }
        while (urls.hasMoreElements()) {
            try {
                final URL url = urls.nextElement();
                String urlPath = url.getFile();
                urlPath = URLDecoder.decode(urlPath, Charsets.UTF_8.name());
                if (urlPath.startsWith("file:")) {
                    urlPath = urlPath.substring(5);
                }
                if (urlPath.indexOf(33) > 0) {
                    urlPath = urlPath.substring(0, urlPath.indexOf(33));
                }
                ResolverUtil.LOGGER.info("Scanning for classes in [" + urlPath + "] matching criteria: " + test);
                if ("vfszip".equals(url.getProtocol())) {
                    final String path = urlPath.substring(0, urlPath.length() - packageName.length() - 2);
                    final URL newURL = new URL(url.getProtocol(), url.getHost(), path);
                    final JarInputStream stream = new JarInputStream(newURL.openStream());
                    try {
                        this.loadImplementationsInJar(test, packageName, path, stream);
                    }
                    finally {
                        this.close(stream, newURL);
                    }
                }
                else if ("bundleresource".equals(url.getProtocol())) {
                    this.loadImplementationsInBundle(test, packageName);
                }
                else {
                    final File file = new File(urlPath);
                    if (file.isDirectory()) {
                        this.loadImplementationsInDirectory(test, packageName, file);
                    }
                    else {
                        this.loadImplementationsInJar(test, packageName, file);
                    }
                }
            }
            catch (IOException ioe) {
                ResolverUtil.LOGGER.warn("could not read entries", ioe);
            }
        }
    }
    
    private void loadImplementationsInBundle(final Test test, final String packageName) {
        final BundleWiring wiring = (BundleWiring)FrameworkUtil.getBundle((Class)ResolverUtil.class).adapt((Class)BundleWiring.class);
        final Collection<String> list = (Collection<String>)wiring.listResources(packageName, "*.class", 1);
        for (final String name : list) {
            this.addIfMatching(test, name);
        }
    }
    
    private void loadImplementationsInDirectory(final Test test, final String parent, final File location) {
        final File[] files = location.listFiles();
        if (files == null) {
            return;
        }
        for (final File file : files) {
            final StringBuilder builder = new StringBuilder();
            builder.append(parent).append("/").append(file.getName());
            final String packageOrClass = (parent == null) ? file.getName() : builder.toString();
            if (file.isDirectory()) {
                this.loadImplementationsInDirectory(test, packageOrClass, file);
            }
            else if (this.isTestApplicable(test, file.getName())) {
                this.addIfMatching(test, packageOrClass);
            }
        }
    }
    
    private boolean isTestApplicable(final Test test, final String path) {
        return test.doesMatchResource() || (path.endsWith(".class") && test.doesMatchClass());
    }
    
    private void loadImplementationsInJar(final Test test, final String parent, final File jarFile) {
        JarInputStream jarStream = null;
        try {
            jarStream = new JarInputStream(new FileInputStream(jarFile));
            this.loadImplementationsInJar(test, parent, jarFile.getPath(), jarStream);
        }
        catch (FileNotFoundException ex) {
            ResolverUtil.LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " file not found");
        }
        catch (IOException ioe) {
            ResolverUtil.LOGGER.error("Could not search jar file '" + jarFile + "' for classes matching criteria: " + test + " due to an IOException", ioe);
        }
        finally {
            this.close(jarStream, jarFile);
        }
    }
    
    private void close(final JarInputStream jarStream, final Object source) {
        if (jarStream != null) {
            try {
                jarStream.close();
            }
            catch (IOException e) {
                ResolverUtil.LOGGER.error("Error closing JAR file stream for {}", source, e);
            }
        }
    }
    
    private void loadImplementationsInJar(final Test test, final String parent, final String path, final JarInputStream stream) {
        try {
            JarEntry entry;
            while ((entry = stream.getNextJarEntry()) != null) {
                final String name = entry.getName();
                if (!entry.isDirectory() && name.startsWith(parent) && this.isTestApplicable(test, name)) {
                    this.addIfMatching(test, name);
                }
            }
        }
        catch (IOException ioe) {
            ResolverUtil.LOGGER.error("Could not search jar file '" + path + "' for classes matching criteria: " + test + " due to an IOException", ioe);
        }
    }
    
    protected void addIfMatching(final Test test, final String fqn) {
        try {
            final ClassLoader loader = this.getClassLoader();
            if (test.doesMatchClass()) {
                final String externalName = fqn.substring(0, fqn.indexOf(46)).replace('/', '.');
                if (ResolverUtil.LOGGER.isDebugEnabled()) {
                    ResolverUtil.LOGGER.debug("Checking to see if class " + externalName + " matches criteria [" + test + "]");
                }
                final Class<?> type = loader.loadClass(externalName);
                if (test.matches(type)) {
                    this.classMatches.add(type);
                }
            }
            if (test.doesMatchResource()) {
                URL url = loader.getResource(fqn);
                if (url == null) {
                    url = loader.getResource(fqn.substring(1));
                }
                if (url != null && test.matches(url.toURI())) {
                    this.resourceMatches.add(url.toURI());
                }
            }
        }
        catch (Throwable t) {
            ResolverUtil.LOGGER.warn("Could not examine class '" + fqn + "' due to a " + t.getClass().getName() + " with message: " + t.getMessage());
        }
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
    
    public abstract static class ClassTest implements Test
    {
        @Override
        public boolean matches(final URI resource) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean doesMatchClass() {
            return true;
        }
        
        @Override
        public boolean doesMatchResource() {
            return false;
        }
    }
    
    public abstract static class ResourceTest implements Test
    {
        @Override
        public boolean matches(final Class<?> cls) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public boolean doesMatchClass() {
            return false;
        }
        
        @Override
        public boolean doesMatchResource() {
            return true;
        }
    }
    
    public static class IsA extends ClassTest
    {
        private final Class<?> parent;
        
        public IsA(final Class<?> parentType) {
            this.parent = parentType;
        }
        
        @Override
        public boolean matches(final Class<?> type) {
            return type != null && this.parent.isAssignableFrom(type);
        }
        
        @Override
        public String toString() {
            return "is assignable to " + this.parent.getSimpleName();
        }
    }
    
    public static class NameEndsWith extends ClassTest
    {
        private final String suffix;
        
        public NameEndsWith(final String suffix) {
            this.suffix = suffix;
        }
        
        @Override
        public boolean matches(final Class<?> type) {
            return type != null && type.getName().endsWith(this.suffix);
        }
        
        @Override
        public String toString() {
            return "ends with the suffix " + this.suffix;
        }
    }
    
    public static class AnnotatedWith extends ClassTest
    {
        private final Class<? extends Annotation> annotation;
        
        public AnnotatedWith(final Class<? extends Annotation> annotation) {
            this.annotation = annotation;
        }
        
        @Override
        public boolean matches(final Class<?> type) {
            return type != null && type.isAnnotationPresent(this.annotation);
        }
        
        @Override
        public String toString() {
            return "annotated with @" + this.annotation.getSimpleName();
        }
    }
    
    public static class NameIs extends ResourceTest
    {
        private final String name;
        
        public NameIs(final String name) {
            this.name = "/" + name;
        }
        
        @Override
        public boolean matches(final URI resource) {
            return resource.getPath().endsWith(this.name);
        }
        
        @Override
        public String toString() {
            return "named " + this.name;
        }
    }
    
    public interface Test
    {
        boolean matches(final Class<?> p0);
        
        boolean matches(final URI p0);
        
        boolean doesMatchClass();
        
        boolean doesMatchResource();
    }
}
