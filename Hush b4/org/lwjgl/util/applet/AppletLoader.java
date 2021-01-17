// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.applet;

import java.awt.MediaTracker;
import java.util.zip.Checksum;
import java.util.zip.CheckedInputStream;
import java.io.BufferedInputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.net.JarURLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.GZIPInputStream;
import java.lang.reflect.Constructor;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.net.URLConnection;
import java.net.HttpURLConnection;
import java.util.concurrent.Future;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.util.Vector;
import org.lwjgl.LWJGLUtil;
import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.Permission;
import java.security.AllPermission;
import java.security.Permissions;
import java.security.PermissionCollection;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.net.URLClassLoader;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.security.AccessControlException;
import java.awt.EventQueue;
import java.io.File;
import java.util.StringTokenizer;
import java.awt.FontMetrics;
import java.awt.image.ImageObserver;
import java.awt.Graphics;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.jar.Pack200;
import java.util.HashMap;
import java.net.URL;
import java.awt.Color;
import java.awt.Image;
import java.applet.AppletStub;
import java.applet.Applet;

public class AppletLoader extends Applet implements Runnable, AppletStub
{
    public static final int STATE_INIT = 1;
    public static final int STATE_CHECK_JRE_VERSION = 2;
    public static final int STATE_DETERMINING_PACKAGES = 3;
    public static final int STATE_CHECKING_CACHE = 4;
    public static final int STATE_CHECKING_FOR_UPDATES = 5;
    public static final int STATE_DOWNLOADING = 6;
    public static final int STATE_EXTRACTING_PACKAGES = 7;
    public static final int STATE_VALIDATING_PACKAGES = 8;
    public static final int STATE_UPDATING_CLASSPATH = 9;
    public static final int STATE_SWITCHING_APPLET = 10;
    public static final int STATE_INITIALIZE_REAL_APPLET = 11;
    public static final int STATE_START_REAL_APPLET = 12;
    public static final int STATE_DONE = 13;
    protected volatile int percentage;
    protected int totalDownloadSize;
    protected int currentSizeExtract;
    protected int totalSizeExtract;
    protected Image logo;
    protected Image logoBuffer;
    protected Image progressbar;
    protected Image progressbarBuffer;
    protected Image offscreen;
    protected boolean painting;
    protected Color bgColor;
    protected Color fgColor;
    protected URL[] urlList;
    protected ClassLoader classLoader;
    protected Thread loaderThread;
    protected Thread animationThread;
    protected Applet lwjglApplet;
    protected boolean debugMode;
    protected boolean prependHost;
    protected HashMap<String, Long> filesLastModified;
    protected int[] fileSizes;
    protected int nativeJarCount;
    protected boolean cacheEnabled;
    protected String subtaskMessage;
    protected volatile int state;
    protected boolean lzmaSupported;
    protected boolean pack200Supported;
    protected boolean headless;
    protected boolean headlessWaiting;
    protected String[] headlessMessage;
    protected int concurrentLookupThreads;
    protected boolean fatalError;
    protected boolean certificateRefused;
    protected boolean minimumJreNotFound;
    protected String[] genericErrorMessage;
    protected String[] certificateRefusedMessage;
    protected String[] minimumJREMessage;
    protected String[] errorMessage;
    protected static boolean natives_loaded;
    
    public AppletLoader() {
        this.bgColor = Color.white;
        this.fgColor = Color.black;
        this.subtaskMessage = "";
        this.state = 1;
        this.headless = false;
        this.headlessWaiting = true;
        this.genericErrorMessage = new String[] { "An error occured while loading the applet.", "Please contact support to resolve this issue.", "<placeholder for error message>" };
        this.certificateRefusedMessage = new String[] { "Permissions for Applet Refused.", "Please accept the permissions dialog to allow", "the applet to continue the loading process." };
        this.minimumJREMessage = new String[] { "Your version of Java is out of date.", "Visit java.com to get the latest version.", "Java <al_min_jre> or greater is required." };
    }
    
    @Override
    public void init() {
        this.setState(1);
        final String[] arr$;
        final String[] requiredArgs = arr$ = new String[] { "al_main", "al_jars" };
        for (final String requiredArg : arr$) {
            if (this.getParameter(requiredArg) == null) {
                this.fatalErrorOccured("missing required applet parameter: " + requiredArg, null);
                return;
            }
        }
        this.cacheEnabled = this.getBooleanParameter("al_cache", true);
        this.debugMode = this.getBooleanParameter("al_debug", false);
        this.prependHost = this.getBooleanParameter("al_prepend_host", true);
        this.headless = this.getBooleanParameter("al_headless", false);
        this.concurrentLookupThreads = this.getIntParameter("al_lookup_threads", 1);
        this.setBackground(this.bgColor = this.getColor("boxbgcolor", Color.white));
        this.fgColor = this.getColor("boxfgcolor", Color.black);
        if (!this.headless) {
            this.logo = this.getImage(this.getStringParameter("al_logo", "appletlogo.gif"));
            this.progressbar = this.getImage(this.getStringParameter("al_progressbar", "appletprogress.gif"));
        }
        try {
            Class.forName("LZMA.LzmaInputStream");
            this.lzmaSupported = true;
        }
        catch (Throwable t) {}
        try {
            Pack200.class.getSimpleName();
            this.pack200Supported = true;
        }
        catch (Throwable t2) {}
    }
    
    private static String generateStacktrace(final Exception exception) {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        return result.toString();
    }
    
    @Override
    public void start() {
        if (this.lwjglApplet != null) {
            this.lwjglApplet.start();
        }
        else if (this.loaderThread == null && !this.fatalError) {
            (this.loaderThread = new Thread(this)).setName("AppletLoader.loaderThread");
            this.loaderThread.start();
            if (!this.headless) {
                (this.animationThread = new Thread() {
                    @Override
                    public void run() {
                        while (AppletLoader.this.loaderThread != null) {
                            AppletLoader.this.repaint();
                            AppletLoader.this.sleep(100L);
                        }
                        AppletLoader.this.animationThread = null;
                    }
                }).setName("AppletLoader.animationthread");
                this.animationThread.start();
            }
        }
    }
    
    @Override
    public void stop() {
        if (this.lwjglApplet != null) {
            this.lwjglApplet.stop();
        }
    }
    
    @Override
    public void destroy() {
        if (this.lwjglApplet != null) {
            this.lwjglApplet.destroy();
        }
    }
    
    protected void cleanUp() {
        this.progressbar = null;
        this.logo = null;
        this.logoBuffer = null;
        this.progressbarBuffer = null;
        this.offscreen = null;
    }
    
    public Applet getApplet() {
        return this.lwjglApplet;
    }
    
    public int getStatus() {
        if (!this.fatalError) {
            if (this.percentage == 100 && this.headlessWaiting) {
                this.headlessWaiting = false;
            }
            if (this.percentage == 95) {
                this.percentage = 100;
            }
            final String[] message = { this.getDescriptionForState(), this.subtaskMessage };
            this.headlessMessage = message;
            return this.percentage;
        }
        this.headlessMessage = this.errorMessage;
        if (this.certificateRefused) {
            return -2;
        }
        if (this.minimumJreNotFound) {
            return -3;
        }
        return -1;
    }
    
    public String[] getMessages() {
        return this.headlessMessage;
    }
    
    public void appletResize(final int width, final int height) {
        this.resize(width, height);
    }
    
    @Override
    public final void update(final Graphics g) {
        this.paint(g);
    }
    
    @Override
    public void paint(final Graphics g) {
        if (this.state == 13) {
            this.cleanUp();
            return;
        }
        if (this.headless) {
            return;
        }
        if (this.offscreen == null) {
            this.offscreen = this.createImage(this.getWidth(), this.getHeight());
            if (this.logo != null) {
                this.logoBuffer = this.createImage(this.logo.getWidth(null), this.logo.getHeight(null));
                this.offscreen.getGraphics().drawImage(this.logo, 0, 0, this);
                this.imageUpdate(this.logo, 16, 0, 0, 0, 0);
            }
            if (this.progressbar != null) {
                this.progressbarBuffer = this.createImage(this.progressbar.getWidth(null), this.progressbar.getHeight(null));
                this.offscreen.getGraphics().drawImage(this.progressbar, 0, 0, this);
                this.imageUpdate(this.progressbar, 16, 0, 0, 0, 0);
            }
        }
        final Graphics og = this.offscreen.getGraphics();
        final FontMetrics fm = og.getFontMetrics();
        og.setColor(this.bgColor);
        og.fillRect(0, 0, this.offscreen.getWidth(null), this.offscreen.getHeight(null));
        og.setColor(this.fgColor);
        if (this.fatalError) {
            for (int i = 0; i < this.errorMessage.length; ++i) {
                if (this.errorMessage[i] != null) {
                    final int messageX = (this.offscreen.getWidth(null) - fm.stringWidth(this.errorMessage[i])) / 2;
                    final int messageY = (this.offscreen.getHeight(null) - fm.getHeight() * this.errorMessage.length) / 2;
                    og.drawString(this.errorMessage[i], messageX, messageY + i * fm.getHeight());
                }
            }
        }
        else {
            og.setColor(this.fgColor);
            this.painting = true;
            final int x = this.offscreen.getWidth(null) / 2;
            final int y = this.offscreen.getHeight(null) / 2;
            if (this.logo != null) {
                og.drawImage(this.logoBuffer, x - this.logo.getWidth(null) / 2, y - this.logo.getHeight(null) / 2, this);
            }
            final String message = this.getDescriptionForState();
            int messageX2 = (this.offscreen.getWidth(null) - fm.stringWidth(message)) / 2;
            int messageY2 = y + 20;
            if (this.logo != null) {
                messageY2 += this.logo.getHeight(null) / 2;
            }
            else if (this.progressbar != null) {
                messageY2 += this.progressbar.getHeight(null) / 2;
            }
            og.drawString(message, messageX2, messageY2);
            if (this.subtaskMessage.length() > 0) {
                messageX2 = (this.offscreen.getWidth(null) - fm.stringWidth(this.subtaskMessage)) / 2;
                og.drawString(this.subtaskMessage, messageX2, messageY2 + 20);
            }
            if (this.progressbar != null) {
                final int barSize = this.progressbar.getWidth(null) * this.percentage / 100;
                og.clipRect(x - this.progressbar.getWidth(null) / 2, 0, barSize, this.offscreen.getHeight(null));
                og.drawImage(this.progressbarBuffer, x - this.progressbar.getWidth(null) / 2, y - this.progressbar.getHeight(null) / 2, this);
            }
            this.painting = false;
        }
        og.dispose();
        g.drawImage(this.offscreen, (this.getWidth() - this.offscreen.getWidth(null)) / 2, (this.getHeight() - this.offscreen.getHeight(null)) / 2, null);
    }
    
    @Override
    public boolean imageUpdate(final Image img, final int flag, final int x, final int y, final int width, final int height) {
        if (this.state == 13) {
            return false;
        }
        if (flag == 16 && !this.painting) {
            Image buffer;
            if (img == this.logo) {
                buffer = this.logoBuffer;
            }
            else {
                buffer = this.progressbarBuffer;
            }
            final Graphics g = buffer.getGraphics();
            g.setColor(this.bgColor);
            g.fillRect(0, 0, buffer.getWidth(null), buffer.getHeight(null));
            if (img == this.progressbar && this.logo != null) {
                g.drawImage(this.logoBuffer, this.progressbar.getWidth(null) / 2 - this.logo.getWidth(null) / 2, this.progressbar.getHeight(null) / 2 - this.logo.getHeight(null) / 2, null);
            }
            g.drawImage(img, 0, 0, this);
            g.dispose();
            this.repaint();
        }
        return true;
    }
    
    protected String getDescriptionForState() {
        switch (this.state) {
            case 1: {
                return "Initializing loader";
            }
            case 2: {
                return "Checking version";
            }
            case 3: {
                return "Determining packages to load";
            }
            case 4: {
                return "Calculating download size";
            }
            case 5: {
                return "Checking for updates";
            }
            case 6: {
                return "Downloading packages";
            }
            case 7: {
                return "Extracting downloaded packages";
            }
            case 8: {
                return "Validating packages";
            }
            case 9: {
                return "Updating classpath";
            }
            case 10: {
                return "Switching applet";
            }
            case 11: {
                return "Initializing real applet";
            }
            case 12: {
                return "Starting real applet";
            }
            case 13: {
                return "Done loading";
            }
            default: {
                return "unknown state";
            }
        }
    }
    
    protected String trimExtensionByCapabilities(String file) {
        if (!this.pack200Supported) {
            file = file.replace(".pack", "");
        }
        if (!this.lzmaSupported && file.endsWith(".lzma")) {
            file = file.replace(".lzma", "");
            System.out.println("LZMA decoder (lzma.jar) not found, trying " + file + " without lzma extension.");
        }
        return file;
    }
    
    protected void loadJarURLs() throws Exception {
        this.setState(3);
        String jarList = this.getParameter("al_jars");
        String nativeJarList = null;
        final String osName = System.getProperty("os.name");
        if (osName.startsWith("Win")) {
            if (System.getProperty("os.arch").endsWith("64")) {
                nativeJarList = this.getParameter("al_windows64");
            }
            else {
                nativeJarList = this.getParameter("al_windows32");
            }
            if (nativeJarList == null) {
                nativeJarList = this.getParameter("al_windows");
            }
        }
        else if (osName.startsWith("Linux") || osName.startsWith("Unix")) {
            if (System.getProperty("os.arch").endsWith("64")) {
                nativeJarList = this.getParameter("al_linux64");
            }
            else {
                nativeJarList = this.getParameter("al_linux32");
            }
            if (nativeJarList == null) {
                nativeJarList = this.getParameter("al_linux");
            }
        }
        else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
            if (System.getProperty("os.arch").endsWith("64")) {
                nativeJarList = this.getParameter("al_mac64");
            }
            else if (System.getProperty("os.arch").contains("ppc")) {
                nativeJarList = this.getParameter("al_macppc");
            }
            else {
                nativeJarList = this.getParameter("al_mac32");
            }
            if (nativeJarList == null) {
                nativeJarList = this.getParameter("al_mac");
            }
        }
        else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
            nativeJarList = this.getParameter("al_solaris");
        }
        else if (osName.startsWith("FreeBSD")) {
            nativeJarList = this.getParameter("al_freebsd");
        }
        else {
            if (!osName.startsWith("OpenBSD")) {
                this.fatalErrorOccured("OS (" + osName + ") not supported", null);
                return;
            }
            nativeJarList = this.getParameter("al_openbsd");
        }
        if (nativeJarList == null) {
            this.fatalErrorOccured("no lwjgl natives files found", null);
            return;
        }
        jarList = this.trimExtensionByCapabilities(jarList);
        final StringTokenizer jars = new StringTokenizer(jarList, ", ");
        nativeJarList = this.trimExtensionByCapabilities(nativeJarList);
        final StringTokenizer nativeJars = new StringTokenizer(nativeJarList, ", ");
        final int jarCount = jars.countTokens();
        this.nativeJarCount = nativeJars.countTokens();
        this.urlList = new URL[jarCount + this.nativeJarCount];
        final URL path = this.getCodeBase();
        for (int i = 0; i < jarCount; ++i) {
            this.urlList[i] = new URL(path, jars.nextToken());
        }
        for (int i = jarCount; i < jarCount + this.nativeJarCount; ++i) {
            this.urlList[i] = new URL(path, nativeJars.nextToken());
        }
    }
    
    public void run() {
        this.percentage = 5;
        try {
            this.debug_sleep(2000L);
            if (!this.isMinJREVersionAvailable()) {
                this.minimumJreNotFound = true;
                this.fatalErrorOccured("Java " + this.getStringParameter("al_min_jre", "1.5") + " or greater is required.", null);
                return;
            }
            this.loadJarURLs();
            final String path = this.getCacheDirectory();
            final File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File versionFile = new File(dir, "version");
            boolean versionAvailable = false;
            final String version = this.getParameter("al_version");
            if (version != null) {
                versionAvailable = this.compareVersion(versionFile, version.toLowerCase());
            }
            if (!versionAvailable) {
                this.getJarInfo(dir);
                this.downloadJars(path);
                this.extractJars(path);
                this.extractNatives(path);
                this.validateJars(path);
                if (version != null) {
                    this.percentage = 90;
                    this.writeObjectFile(versionFile, version.toLowerCase());
                }
                this.writeObjectFile(new File(dir, "timestamps"), this.filesLastModified);
            }
            this.updateClassPath(path);
            this.setLWJGLProperties();
            if (this.headless) {
                while (this.headlessWaiting) {
                    Thread.sleep(100L);
                }
            }
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    try {
                        AppletLoader.this.switchApplet();
                    }
                    catch (Exception e) {
                        AppletLoader.this.fatalErrorOccured("This occurred while '" + AppletLoader.this.getDescriptionForState() + "'", e);
                    }
                    AppletLoader.this.setState(13);
                    AppletLoader.this.repaint();
                }
            });
        }
        catch (Exception e) {
            this.certificateRefused = (e instanceof AccessControlException);
            this.fatalErrorOccured("This occurred while '" + this.getDescriptionForState() + "'", e);
        }
        finally {
            this.loaderThread = null;
        }
    }
    
    public boolean isMinJREVersionAvailable() throws Exception {
        this.setState(2);
        String minimumVersion = this.getStringParameter("al_min_jre", "1.5");
        String javaVersion = System.getProperty("java.version");
        minimumVersion = javaVersion.split("-")[0];
        javaVersion = minimumVersion.split("-")[0];
        final String[] jvmVersionData = javaVersion.split("[_\\.]");
        final String[] minVersionData = minimumVersion.split("[_\\.]");
        final int maxLength = Math.max(jvmVersionData.length, minVersionData.length);
        final int[] jvmVersion = new int[maxLength];
        final int[] minVersion = new int[maxLength];
        for (int i = 0; i < jvmVersionData.length; ++i) {
            jvmVersion[i] = Integer.parseInt(jvmVersionData[i]);
        }
        for (int i = 0; i < minVersionData.length; ++i) {
            minVersion[i] = Integer.parseInt(minVersionData[i]);
        }
        for (int i = 0; i < maxLength; ++i) {
            if (jvmVersion[i] < minVersion[i]) {
                return false;
            }
        }
        return true;
    }
    
    protected boolean compareVersion(final File versionFile, final String version) {
        if (versionFile.exists()) {
            final String s = this.readStringFile(versionFile);
            if (s != null && s.equals(version)) {
                this.percentage = 90;
                if (this.debugMode) {
                    System.out.println("Loading Cached Applet Version: " + version);
                }
                this.debug_sleep(2000L);
                return true;
            }
        }
        return false;
    }
    
    protected void setLWJGLProperties() {
        final String lwjglArguments = this.getParameter("lwjgl_arguments");
        if (lwjglArguments != null && lwjglArguments.length() > 0) {
            int end;
            for (int start = lwjglArguments.indexOf("-Dorg.lwjgl"); start != -1; start = lwjglArguments.indexOf("-Dorg.lwjgl", end)) {
                end = lwjglArguments.indexOf(" ", start);
                if (end == -1) {
                    end = lwjglArguments.length();
                }
                final String[] keyValue = lwjglArguments.substring(start + 2, end).split("=");
                System.setProperty(keyValue[0], keyValue[1]);
                if (this.debugMode) {
                    System.out.println("Setting property " + keyValue[0] + " to " + keyValue[1]);
                }
            }
        }
    }
    
    protected String getCacheDirectory() throws Exception {
        final String path = AccessController.doPrivileged((PrivilegedExceptionAction<String>)new PrivilegedExceptionAction<String>() {
            public String run() throws Exception {
                String codebase = "";
                if (AppletLoader.this.prependHost) {
                    codebase = AppletLoader.this.getCodeBase().getHost();
                    if (codebase == null || codebase.length() == 0) {
                        codebase = "localhost";
                    }
                    codebase += File.separator;
                }
                return AppletLoader.this.getLWJGLCacheDir() + File.separator + codebase + AppletLoader.this.getParameter("al_title") + File.separator;
            }
        });
        return path;
    }
    
    protected String getLWJGLCacheDir() {
        String cacheDir = System.getProperty("deployment.user.cachedir");
        if (cacheDir == null || System.getProperty("os.name").startsWith("Win")) {
            cacheDir = System.getProperty("java.io.tmpdir");
        }
        return cacheDir + File.separator + "lwjglcache";
    }
    
    protected String readStringFile(final File file) {
        try {
            return (String)this.readObjectFile(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    protected HashMap<String, Long> readHashMapFile(final File file) {
        try {
            return (HashMap<String, Long>)this.readObjectFile(file);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Long>();
        }
    }
    
    protected Object readObjectFile(final File file) throws Exception {
        final FileInputStream fis = new FileInputStream(file);
        try {
            final ObjectInputStream dis = new ObjectInputStream(fis);
            final Object object = dis.readObject();
            dis.close();
            return object;
        }
        catch (Exception e) {
            throw e;
        }
        finally {
            fis.close();
        }
    }
    
    protected void writeObjectFile(final File file, final Object object) throws Exception {
        final FileOutputStream fos = new FileOutputStream(file);
        try {
            final ObjectOutputStream dos = new ObjectOutputStream(fos);
            dos.writeObject(object);
            dos.close();
        }
        finally {
            fos.close();
        }
    }
    
    protected void updateClassPath(final String path) throws Exception {
        this.setState(9);
        this.percentage = 95;
        final URL[] urls = new URL[this.urlList.length];
        for (int i = 0; i < this.urlList.length; ++i) {
            String file = new File(path, this.getJarName(this.urlList[i])).toURI().toString();
            file = file.replace("!", "%21");
            urls[i] = new URL(file);
        }
        final Certificate[] certs = getCurrentCertificates();
        final String osName = System.getProperty("os.name");
        final boolean isMacOS = osName.startsWith("Mac") || osName.startsWith("Darwin");
        this.classLoader = new URLClassLoader(urls) {
            @Override
            protected PermissionCollection getPermissions(final CodeSource codesource) {
                PermissionCollection perms = null;
                try {
                    perms = new Permissions();
                    if (AppletLoader.certificatesMatch(certs, codesource.getCertificates())) {
                        perms.add(new AllPermission());
                        return perms;
                    }
                    final String host = AppletLoader.this.getCodeBase().getHost();
                    if (host != null && host.length() > 0) {
                        perms.add(new SocketPermission(host, "connect,accept"));
                    }
                    else if ("file".equals(codesource.getLocation().getProtocol())) {
                        final String path = codesource.getLocation().getFile().replace('/', File.separatorChar);
                        perms.add(new FilePermission(path, "read"));
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                return perms;
            }
            
            @Override
            protected String findLibrary(final String libname) {
                final String libPath = path + "natives" + File.separator + LWJGLUtil.mapLibraryName(libname);
                if (new File(libPath).exists()) {
                    return libPath;
                }
                return super.findLibrary(libname);
            }
        };
        this.debug_sleep(2000L);
        this.unloadNatives(path);
        System.setProperty("org.lwjgl.librarypath", path + "natives");
        System.setProperty("net.java.games.input.librarypath", path + "natives");
        System.setProperty("java.library.path", path + "natives");
        AppletLoader.natives_loaded = true;
    }
    
    private void unloadNatives(final String nativePath) {
        if (!AppletLoader.natives_loaded) {
            return;
        }
        try {
            final Field field = ClassLoader.class.getDeclaredField("loadedLibraryNames");
            field.setAccessible(true);
            final Vector libs = (Vector)field.get(this.getClass().getClassLoader());
            final String path = new File(nativePath).getCanonicalPath();
            for (int i = 0; i < libs.size(); ++i) {
                final String s = libs.get(i);
                if (s.startsWith(path)) {
                    libs.remove(i);
                    --i;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void switchApplet() throws Exception {
        this.setState(10);
        this.percentage = 100;
        this.debug_sleep(2000L);
        Thread.currentThread().setContextClassLoader(this.classLoader);
        final Class appletClass = this.classLoader.loadClass(this.getParameter("al_main"));
        (this.lwjglApplet = appletClass.newInstance()).setStub(this);
        this.lwjglApplet.setSize(this.getWidth(), this.getHeight());
        this.setLayout(new BorderLayout());
        this.add(this.lwjglApplet);
        this.validate();
        this.setState(11);
        this.lwjglApplet.init();
        this.setState(12);
        this.lwjglApplet.start();
    }
    
    protected void getJarInfo(final File dir) throws Exception {
        this.setState(4);
        this.filesLastModified = new HashMap<String, Long>();
        this.fileSizes = new int[this.urlList.length];
        final File timestampsFile = new File(dir, "timestamps");
        if (timestampsFile.exists()) {
            this.setState(5);
            this.filesLastModified = this.readHashMapFile(timestampsFile);
        }
        final ExecutorService executorService = Executors.newFixedThreadPool(this.concurrentLookupThreads);
        final Queue<Future> requests = new LinkedList<Future>();
        final Object sync = new Integer(1);
        for (int j = 0; j < this.urlList.length; ++j) {
            final int i = j;
            final Future request = executorService.submit(new Runnable() {
                public void run() {
                    try {
                        final URLConnection urlconnection = AppletLoader.this.urlList[i].openConnection();
                        urlconnection.setDefaultUseCaches(false);
                        if (urlconnection instanceof HttpURLConnection) {
                            ((HttpURLConnection)urlconnection).setRequestMethod("HEAD");
                        }
                        AppletLoader.this.fileSizes[i] = urlconnection.getContentLength();
                        final long lastModified = urlconnection.getLastModified();
                        final String fileName = AppletLoader.this.getFileName(AppletLoader.this.urlList[i]);
                        if (AppletLoader.this.cacheEnabled && lastModified != 0L && AppletLoader.this.filesLastModified.containsKey(fileName)) {
                            final long savedLastModified = AppletLoader.this.filesLastModified.get(fileName);
                            if (savedLastModified == lastModified) {
                                AppletLoader.this.fileSizes[i] = -2;
                            }
                        }
                        if (AppletLoader.this.fileSizes[i] >= 0) {
                            synchronized (sync) {
                                final AppletLoader this$0 = AppletLoader.this;
                                this$0.totalDownloadSize += AppletLoader.this.fileSizes[i];
                            }
                        }
                        AppletLoader.this.filesLastModified.put(fileName, lastModified);
                    }
                    catch (Exception e) {
                        throw new RuntimeException("Failed to fetch information for " + AppletLoader.this.urlList[i], e);
                    }
                }
            });
            requests.add(request);
        }
        while (!requests.isEmpty()) {
            final Iterator<Future> iterator = (Iterator<Future>)requests.iterator();
            while (iterator.hasNext()) {
                final Future request2 = iterator.next();
                if (request2.isDone()) {
                    request2.get();
                    iterator.remove();
                    this.percentage = 5 + (int)(10 * (this.urlList.length - requests.size()) / (float)this.urlList.length);
                }
            }
            Thread.sleep(10L);
        }
        executorService.shutdown();
    }
    
    protected void downloadJars(final String path) throws Exception {
        this.setState(6);
        final int percentage = 15;
        this.percentage = percentage;
        final int initialPercentage = percentage;
        int amountDownloaded = 0;
        final byte[] buffer = new byte[65536];
        for (int i = 0; i < this.urlList.length; ++i) {
            if (this.fileSizes[i] != -2) {
                int unsuccessfulAttempts = 0;
                final int maxUnsuccessfulAttempts = 3;
                boolean downloadFile = true;
                final String currentFile = this.getFileName(this.urlList[i]);
                while (downloadFile) {
                    downloadFile = false;
                    this.debug_sleep(2000L);
                    try {
                        final URLConnection urlconnection = this.urlList[i].openConnection();
                        urlconnection.setUseCaches(false);
                        if (urlconnection instanceof HttpURLConnection) {
                            urlconnection.setRequestProperty("Cache-Control", "no-store,max-age=0,no-cache");
                            urlconnection.connect();
                        }
                        final InputStream inputstream = this.getJarInputStream(currentFile, urlconnection);
                        final FileOutputStream fos = new FileOutputStream(path + currentFile);
                        int currentDownload = 0;
                        long downloadStartTime = System.currentTimeMillis();
                        int downloadedAmount = 0;
                        String downloadSpeedMessage = "";
                        try {
                            int bufferSize;
                            while ((bufferSize = inputstream.read(buffer, 0, buffer.length)) != -1) {
                                this.debug_sleep(10L);
                                fos.write(buffer, 0, bufferSize);
                                currentDownload += bufferSize;
                                final int totalDownloaded = amountDownloaded + currentDownload;
                                this.percentage = initialPercentage + totalDownloaded * 45 / this.totalDownloadSize;
                                this.subtaskMessage = "Retrieving: " + currentFile + " " + totalDownloaded * 100 / this.totalDownloadSize + "%";
                                downloadedAmount += bufferSize;
                                final long timeLapse = System.currentTimeMillis() - downloadStartTime;
                                if (timeLapse >= 1000L) {
                                    float downloadSpeed = downloadedAmount / (float)timeLapse;
                                    downloadSpeed = (int)(downloadSpeed * 100.0f) / 100.0f;
                                    downloadSpeedMessage = " - " + downloadSpeed + " KB/sec";
                                    downloadedAmount = 0;
                                    downloadStartTime = System.currentTimeMillis();
                                }
                                this.subtaskMessage += downloadSpeedMessage;
                            }
                        }
                        finally {
                            inputstream.close();
                            fos.close();
                        }
                        if (urlconnection instanceof HttpURLConnection) {
                            if (currentDownload != this.fileSizes[i]) {
                                if (this.fileSizes[i] > 0 || currentDownload == 0) {
                                    throw new Exception("size mismatch on download of " + currentFile + " expected " + this.fileSizes[i] + " got " + currentDownload);
                                }
                            }
                        }
                        amountDownloaded += this.fileSizes[i];
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        if (++unsuccessfulAttempts >= maxUnsuccessfulAttempts) {
                            throw new Exception("failed to download " + currentFile + " after " + maxUnsuccessfulAttempts + " attempts");
                        }
                        downloadFile = true;
                        Thread.sleep(100L);
                    }
                }
            }
        }
        this.subtaskMessage = "";
    }
    
    protected InputStream getJarInputStream(final String currentFile, final URLConnection urlconnection) throws Exception {
        final InputStream[] is = { null };
        for (int j = 0; j < 3 && is[0] == null; ++j) {
            final Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        is[0] = urlconnection.getInputStream();
                    }
                    catch (IOException ex) {}
                }
            };
            t.setName("JarInputStreamThread");
            t.start();
            int iterationCount = 0;
            while (is[0] == null && iterationCount++ < 5) {
                try {
                    t.join(1000L);
                }
                catch (InterruptedException inte) {}
            }
            if (is[0] == null) {
                try {
                    t.interrupt();
                    t.join();
                }
                catch (InterruptedException ex) {}
            }
        }
        if (is[0] == null) {
            throw new Exception("Unable to get input stream for " + currentFile);
        }
        return is[0];
    }
    
    protected void extractLZMA(final String in, final String out) throws Exception {
        final File f = new File(in);
        final FileInputStream fileInputHandle = new FileInputStream(f);
        final Class<?> clazz = Class.forName("LZMA.LzmaInputStream");
        final Constructor constructor = clazz.getDeclaredConstructor(InputStream.class);
        final InputStream inputHandle = constructor.newInstance(fileInputHandle);
        final OutputStream outputHandle = new FileOutputStream(out);
        final byte[] buffer = new byte[16384];
        try {
            for (int ret = inputHandle.read(buffer); ret >= 1; ret = inputHandle.read(buffer)) {
                outputHandle.write(buffer, 0, ret);
            }
        }
        finally {
            inputHandle.close();
            outputHandle.close();
        }
        f.delete();
    }
    
    protected void extractGZip(final String in, final String out) throws Exception {
        final File f = new File(in);
        final FileInputStream fileInputHandle = new FileInputStream(f);
        final InputStream inputHandle = new GZIPInputStream(fileInputHandle);
        final OutputStream outputHandle = new FileOutputStream(out);
        try {
            final byte[] buffer = new byte[16384];
            for (int ret = inputHandle.read(buffer); ret >= 1; ret = inputHandle.read(buffer)) {
                outputHandle.write(buffer, 0, ret);
            }
        }
        finally {
            inputHandle.close();
            outputHandle.close();
        }
        f.delete();
    }
    
    protected void extractPack(final String in, final String out) throws Exception {
        final File f = new File(in);
        final FileOutputStream fostream = new FileOutputStream(out);
        final JarOutputStream jostream = new JarOutputStream(fostream);
        try {
            final Pack200.Unpacker unpacker = Pack200.newUnpacker();
            unpacker.unpack(f, jostream);
        }
        finally {
            jostream.close();
            fostream.close();
        }
        f.delete();
    }
    
    protected void extractJars(final String path) throws Exception {
        this.setState(7);
        final float increment = 10.0f / this.urlList.length;
        for (int i = 0; i < this.urlList.length; ++i) {
            if (this.fileSizes[i] != -2) {
                this.percentage = 55 + (int)(increment * (i + 1));
                final String filename = this.getFileName(this.urlList[i]);
                if (filename.endsWith(".pack.lzma")) {
                    this.subtaskMessage = "Extracting: " + filename + " to " + this.replaceLast(filename, ".lzma", "");
                    this.debug_sleep(1000L);
                    this.extractLZMA(path + filename, path + this.replaceLast(filename, ".lzma", ""));
                    this.subtaskMessage = "Extracting: " + this.replaceLast(filename, ".lzma", "") + " to " + this.replaceLast(filename, ".pack.lzma", "");
                    this.debug_sleep(1000L);
                    this.extractPack(path + this.replaceLast(filename, ".lzma", ""), path + this.replaceLast(filename, ".pack.lzma", ""));
                }
                else if (filename.endsWith(".pack.gz")) {
                    this.subtaskMessage = "Extracting: " + filename + " to " + this.replaceLast(filename, ".gz", "");
                    this.debug_sleep(1000L);
                    this.extractGZip(path + filename, path + this.replaceLast(filename, ".gz", ""));
                    this.subtaskMessage = "Extracting: " + this.replaceLast(filename, ".gz", "") + " to " + this.replaceLast(filename, ".pack.gz", "");
                    this.debug_sleep(1000L);
                    this.extractPack(path + this.replaceLast(filename, ".gz", ""), path + this.replaceLast(filename, ".pack.gz", ""));
                }
                else if (filename.endsWith(".pack")) {
                    this.subtaskMessage = "Extracting: " + filename + " to " + this.replaceLast(filename, ".pack", "");
                    this.debug_sleep(1000L);
                    this.extractPack(path + filename, path + this.replaceLast(filename, ".pack", ""));
                }
                else if (filename.endsWith(".lzma")) {
                    this.subtaskMessage = "Extracting: " + filename + " to " + this.replaceLast(filename, ".lzma", "");
                    this.debug_sleep(1000L);
                    this.extractLZMA(path + filename, path + this.replaceLast(filename, ".lzma", ""));
                }
                else if (filename.endsWith(".gz")) {
                    this.subtaskMessage = "Extracting: " + filename + " to " + this.replaceLast(filename, ".gz", "");
                    this.debug_sleep(1000L);
                    this.extractGZip(path + filename, path + this.replaceLast(filename, ".gz", ""));
                }
            }
        }
    }
    
    protected void extractNatives(final String path) throws Exception {
        this.setState(7);
        final float percentageParts = 15.0f / this.nativeJarCount;
        final File nativeFolder = new File(path + "natives");
        if (!nativeFolder.exists()) {
            nativeFolder.mkdir();
        }
        final Certificate[] certificate = getCurrentCertificates();
        for (int i = this.urlList.length - this.nativeJarCount; i < this.urlList.length; ++i) {
            if (this.fileSizes[i] != -2) {
                final String nativeJar = this.getJarName(this.urlList[i]);
                final JarFile jarFile = new JarFile(path + nativeJar, true);
                Enumeration entities = jarFile.entries();
                this.totalSizeExtract = 0;
                final int jarNum = i - (this.urlList.length - this.nativeJarCount);
                while (entities.hasMoreElements()) {
                    final JarEntry entry = entities.nextElement();
                    if (!entry.isDirectory()) {
                        if (entry.getName().indexOf(47) != -1) {
                            continue;
                        }
                        this.totalSizeExtract += (int)entry.getSize();
                    }
                }
                this.currentSizeExtract = 0;
                entities = jarFile.entries();
                while (entities.hasMoreElements()) {
                    final JarEntry entry = entities.nextElement();
                    if (!entry.isDirectory()) {
                        if (entry.getName().indexOf(47) != -1) {
                            continue;
                        }
                        final File f = new File(path + "natives" + File.separator + entry.getName());
                        if (f.exists() && !f.delete()) {
                            continue;
                        }
                        this.debug_sleep(1000L);
                        final InputStream in = jarFile.getInputStream(jarFile.getEntry(entry.getName()));
                        final OutputStream out = new FileOutputStream(path + "natives" + File.separator + entry.getName());
                        try {
                            final byte[] buffer = new byte[65536];
                            int bufferSize;
                            while ((bufferSize = in.read(buffer, 0, buffer.length)) != -1) {
                                this.debug_sleep(10L);
                                out.write(buffer, 0, bufferSize);
                                this.currentSizeExtract += bufferSize;
                                this.percentage = 65 + (int)(percentageParts * (jarNum + this.currentSizeExtract / (float)this.totalSizeExtract));
                                this.subtaskMessage = "Extracting: " + entry.getName() + " " + this.currentSizeExtract * 100 / this.totalSizeExtract + "%";
                            }
                        }
                        finally {
                            in.close();
                            out.close();
                        }
                        if (!certificatesMatch(certificate, entry.getCertificates())) {
                            f.delete();
                            throw new Exception("The certificate(s) in " + nativeJar + " do not match the AppletLoader!");
                        }
                        continue;
                    }
                }
                this.subtaskMessage = "";
                jarFile.close();
                final File f2 = new File(path + nativeJar);
                f2.delete();
            }
        }
    }
    
    protected static boolean certificatesMatch(final Certificate[] certs1, final Certificate[] certs2) throws Exception {
        if (certs1 == null || certs2 == null) {
            return false;
        }
        if (certs1.length != certs2.length) {
            System.out.println("Certificate chain differs in length [" + certs1.length + " vs " + certs2.length + "]!");
            return false;
        }
        for (int i = 0; i < certs1.length; ++i) {
            if (!certs1[i].equals(certs2[i])) {
                System.out.println("Certificate mismatch found!");
                return false;
            }
        }
        return true;
    }
    
    protected static Certificate[] getCurrentCertificates() throws Exception {
        Certificate[] certificate = AppletLoader.class.getProtectionDomain().getCodeSource().getCertificates();
        if (certificate == null) {
            final URL location = AppletLoader.class.getProtectionDomain().getCodeSource().getLocation();
            final JarURLConnection jurl = (JarURLConnection)new URL("jar:" + location.toString() + "!/org/lwjgl/util/applet/AppletLoader.class").openConnection();
            jurl.setDefaultUseCaches(true);
            certificate = jurl.getCertificates();
            jurl.setDefaultUseCaches(false);
        }
        return certificate;
    }
    
    protected void validateJars(final String path) throws Exception {
        this.setState(8);
        this.percentage = 80;
        final float percentageParts = 10.0f / this.urlList.length;
        for (int i = 0; i < this.urlList.length - this.nativeJarCount; ++i) {
            this.debug_sleep(1000L);
            if (this.fileSizes[i] != -2) {
                this.subtaskMessage = "Validating: " + this.getJarName(this.urlList[i]);
                final File file = new File(path, this.getJarName(this.urlList[i]));
                if (!this.isZipValid(file)) {
                    throw new Exception("The file " + this.getJarName(this.urlList[i]) + " is corrupt!");
                }
                this.percentage = 80 + (int)(percentageParts * i);
            }
        }
        this.subtaskMessage = "";
    }
    
    protected boolean isZipValid(final File file) {
        try {
            final ZipFile zipFile = new ZipFile(file);
            try {
                final Enumeration e = zipFile.entries();
                final byte[] buffer = new byte[4096];
                while (e.hasMoreElements()) {
                    final ZipEntry zipEntry = e.nextElement();
                    final CRC32 crc = new CRC32();
                    final BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                    final CheckedInputStream cis = new CheckedInputStream(bis, crc);
                    while (cis.read(buffer, 0, buffer.length) != -1) {}
                    if (crc.getValue() != zipEntry.getCrc()) {
                        return false;
                    }
                }
                return true;
            }
            finally {
                zipFile.close();
            }
        }
        catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }
    
    protected Image getImage(final String s) {
        if (s.length() == 0) {
            return null;
        }
        Image image = null;
        try {
            image = this.getImage(new URL(this.getCodeBase(), s));
        }
        catch (Exception ex) {}
        if (image == null) {
            image = this.getImage(Thread.currentThread().getContextClassLoader().getResource(s));
        }
        if (image != null) {
            return image;
        }
        this.fatalErrorOccured("Unable to load the logo/progressbar image: " + s, null);
        return null;
    }
    
    @Override
    public Image getImage(final URL url) {
        try {
            final MediaTracker tracker = new MediaTracker(this);
            final Image image = super.getImage(url);
            tracker.addImage(image, 0);
            tracker.waitForAll();
            if (!tracker.isErrorAny()) {
                return image;
            }
        }
        catch (Exception ex) {}
        return null;
    }
    
    protected String getJarName(final URL url) {
        String fileName = url.getFile();
        if (fileName.endsWith(".pack.lzma")) {
            fileName = this.replaceLast(fileName, ".pack.lzma", "");
        }
        else if (fileName.endsWith(".pack.gz")) {
            fileName = this.replaceLast(fileName, ".pack.gz", "");
        }
        else if (fileName.endsWith(".pack")) {
            fileName = this.replaceLast(fileName, ".pack", "");
        }
        else if (fileName.endsWith(".lzma")) {
            fileName = this.replaceLast(fileName, ".lzma", "");
        }
        else if (fileName.endsWith(".gz")) {
            fileName = this.replaceLast(fileName, ".gz", "");
        }
        return fileName.substring(fileName.lastIndexOf(47) + 1);
    }
    
    protected String getFileName(final URL url) {
        final String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf(47) + 1);
    }
    
    protected Color getColor(final String param, final Color defaultColor) {
        final String color = this.getParameter(param);
        if (color == null) {
            return defaultColor;
        }
        if (color.indexOf(",") != -1) {
            final StringTokenizer st = new StringTokenizer(color, ",");
            try {
                return new Color(Integer.parseInt(st.nextToken().trim()), Integer.parseInt(st.nextToken().trim()), Integer.parseInt(st.nextToken().trim()));
            }
            catch (Exception e) {
                return defaultColor;
            }
        }
        try {
            return Color.decode(color);
        }
        catch (NumberFormatException e2) {
            try {
                return (Color)Color.class.getField(color).get(null);
            }
            catch (Exception e3) {
                return defaultColor;
            }
        }
    }
    
    public String replaceLast(final String original, final String target, final String replacement) {
        final int index = original.lastIndexOf(target);
        if (index == -1) {
            return original;
        }
        return original.substring(0, index) + replacement + original.substring(index + target.length());
    }
    
    protected String getStringParameter(final String name, final String defaultValue) {
        final String parameter = this.getParameter(name);
        if (parameter != null) {
            return parameter;
        }
        return defaultValue;
    }
    
    protected boolean getBooleanParameter(final String name, final boolean defaultValue) {
        final String parameter = this.getParameter(name);
        if (parameter != null) {
            return Boolean.parseBoolean(parameter);
        }
        return defaultValue;
    }
    
    protected int getIntParameter(final String name, final int defaultValue) {
        final String parameter = this.getParameter(name);
        if (parameter != null) {
            return Integer.parseInt(parameter);
        }
        return defaultValue;
    }
    
    protected void fatalErrorOccured(final String error, final Exception e) {
        this.fatalError = true;
        if (this.minimumJreNotFound) {
            (this.errorMessage = this.minimumJREMessage)[this.errorMessage.length - 1] = error;
        }
        else if (this.certificateRefused) {
            this.errorMessage = this.certificateRefusedMessage;
        }
        else {
            (this.errorMessage = this.genericErrorMessage)[this.errorMessage.length - 1] = error;
        }
        System.out.println(error);
        if (e != null) {
            System.out.println(e.getMessage());
            System.out.println(generateStacktrace(e));
        }
        this.repaint();
    }
    
    protected void setState(final int state) {
        this.state = state;
        if (this.debugMode) {
            System.out.println(this.getDescriptionForState());
        }
    }
    
    protected void debug_sleep(final long ms) {
        if (this.debugMode) {
            this.sleep(ms);
        }
    }
    
    protected void sleep(final long ms) {
        try {
            Thread.sleep(ms);
        }
        catch (Exception ex) {}
    }
}
