// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import java.util.Locale;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.ListIterator;
import java.util.LinkedList;

public class SoundSystemConfig
{
    public static final Object THREAD_SYNC;
    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_STREAMING = 1;
    public static final int ATTENUATION_NONE = 0;
    public static final int ATTENUATION_ROLLOFF = 1;
    public static final int ATTENUATION_LINEAR = 2;
    public static String EXTENSION_MIDI;
    public static String PREFIX_URL;
    private static SoundSystemLogger logger;
    private static LinkedList<Class> libraries;
    private static LinkedList<Codec> codecs;
    private static LinkedList<IStreamListener> streamListeners;
    private static final Object streamListenersLock;
    private static int numberNormalChannels;
    private static int numberStreamingChannels;
    private static float masterGain;
    private static int defaultAttenuationModel;
    private static float defaultRolloffFactor;
    private static float dopplerFactor;
    private static float dopplerVelocity;
    private static float defaultFadeDistance;
    private static String soundFilesPackage;
    private static int streamingBufferSize;
    private static int numberStreamingBuffers;
    private static boolean streamQueueFormatsMatch;
    private static int maxFileSize;
    private static int fileChunkSize;
    private static boolean midiCodec;
    private static String overrideMIDISynthesizer;
    
    public static void addLibrary(final Class libraryClass) throws SoundSystemException {
        if (libraryClass == null) {
            throw new SoundSystemException("Parameter null in method 'addLibrary'", 2);
        }
        if (!Library.class.isAssignableFrom(libraryClass)) {
            throw new SoundSystemException("The specified class does not extend class 'Library' in method 'addLibrary'");
        }
        if (SoundSystemConfig.libraries == null) {
            SoundSystemConfig.libraries = new LinkedList<Class>();
        }
        if (!SoundSystemConfig.libraries.contains(libraryClass)) {
            SoundSystemConfig.libraries.add(libraryClass);
        }
    }
    
    public static void removeLibrary(final Class libraryClass) throws SoundSystemException {
        if (SoundSystemConfig.libraries == null || libraryClass == null) {
            return;
        }
        SoundSystemConfig.libraries.remove(libraryClass);
    }
    
    public static LinkedList<Class> getLibraries() {
        return SoundSystemConfig.libraries;
    }
    
    public static boolean libraryCompatible(final Class libraryClass) {
        if (libraryClass == null) {
            errorMessage("Parameter 'libraryClass' null in method'librayCompatible'");
            return false;
        }
        if (!Library.class.isAssignableFrom(libraryClass)) {
            errorMessage("The specified class does not extend class 'Library' in method 'libraryCompatible'");
            return false;
        }
        final Object o = runMethod(libraryClass, "libraryCompatible", new Class[0], new Object[0]);
        if (o == null) {
            errorMessage("Method 'Library.libraryCompatible' returned 'null' in method 'libraryCompatible'");
            return false;
        }
        return (boolean)o;
    }
    
    public static String getLibraryTitle(final Class libraryClass) {
        if (libraryClass == null) {
            errorMessage("Parameter 'libraryClass' null in method'getLibrayTitle'");
            return null;
        }
        if (!Library.class.isAssignableFrom(libraryClass)) {
            errorMessage("The specified class does not extend class 'Library' in method 'getLibraryTitle'");
            return null;
        }
        final Object o = runMethod(libraryClass, "getTitle", new Class[0], new Object[0]);
        if (o == null) {
            errorMessage("Method 'Library.getTitle' returned 'null' in method 'getLibraryTitle'");
            return null;
        }
        return (String)o;
    }
    
    public static String getLibraryDescription(final Class libraryClass) {
        if (libraryClass == null) {
            errorMessage("Parameter 'libraryClass' null in method'getLibrayDescription'");
            return null;
        }
        if (!Library.class.isAssignableFrom(libraryClass)) {
            errorMessage("The specified class does not extend class 'Library' in method 'getLibraryDescription'");
            return null;
        }
        final Object o = runMethod(libraryClass, "getDescription", new Class[0], new Object[0]);
        if (o == null) {
            errorMessage("Method 'Library.getDescription' returned 'null' in method 'getLibraryDescription'");
            return null;
        }
        return (String)o;
    }
    
    public static boolean reverseByteOrder(final Class libraryClass) {
        if (libraryClass == null) {
            errorMessage("Parameter 'libraryClass' null in method'reverseByteOrder'");
            return false;
        }
        if (!Library.class.isAssignableFrom(libraryClass)) {
            errorMessage("The specified class does not extend class 'Library' in method 'reverseByteOrder'");
            return false;
        }
        final Object o = runMethod(libraryClass, "reversByteOrder", new Class[0], new Object[0]);
        if (o == null) {
            errorMessage("Method 'Library.reverseByteOrder' returned 'null' in method 'getLibraryDescription'");
            return false;
        }
        return (boolean)o;
    }
    
    public static void setLogger(final SoundSystemLogger l) {
        SoundSystemConfig.logger = l;
    }
    
    public static SoundSystemLogger getLogger() {
        return SoundSystemConfig.logger;
    }
    
    public static synchronized void setNumberNormalChannels(final int number) {
        SoundSystemConfig.numberNormalChannels = number;
    }
    
    public static synchronized int getNumberNormalChannels() {
        return SoundSystemConfig.numberNormalChannels;
    }
    
    public static synchronized void setNumberStreamingChannels(final int number) {
        SoundSystemConfig.numberStreamingChannels = number;
    }
    
    public static synchronized int getNumberStreamingChannels() {
        return SoundSystemConfig.numberStreamingChannels;
    }
    
    public static synchronized void setMasterGain(final float value) {
        SoundSystemConfig.masterGain = value;
    }
    
    public static synchronized float getMasterGain() {
        return SoundSystemConfig.masterGain;
    }
    
    public static synchronized void setDefaultAttenuation(final int model) {
        SoundSystemConfig.defaultAttenuationModel = model;
    }
    
    public static synchronized int getDefaultAttenuation() {
        return SoundSystemConfig.defaultAttenuationModel;
    }
    
    public static synchronized void setDefaultRolloff(final float rolloff) {
        SoundSystemConfig.defaultRolloffFactor = rolloff;
    }
    
    public static synchronized float getDopplerFactor() {
        return SoundSystemConfig.dopplerFactor;
    }
    
    public static synchronized void setDopplerFactor(final float factor) {
        SoundSystemConfig.dopplerFactor = factor;
    }
    
    public static synchronized float getDopplerVelocity() {
        return SoundSystemConfig.dopplerVelocity;
    }
    
    public static synchronized void setDopplerVelocity(final float velocity) {
        SoundSystemConfig.dopplerVelocity = velocity;
    }
    
    public static synchronized float getDefaultRolloff() {
        return SoundSystemConfig.defaultRolloffFactor;
    }
    
    public static synchronized void setDefaultFadeDistance(final float distance) {
        SoundSystemConfig.defaultFadeDistance = distance;
    }
    
    public static synchronized float getDefaultFadeDistance() {
        return SoundSystemConfig.defaultFadeDistance;
    }
    
    public static synchronized void setSoundFilesPackage(final String location) {
        SoundSystemConfig.soundFilesPackage = location;
    }
    
    public static synchronized String getSoundFilesPackage() {
        return SoundSystemConfig.soundFilesPackage;
    }
    
    public static synchronized void setStreamingBufferSize(final int size) {
        SoundSystemConfig.streamingBufferSize = size;
    }
    
    public static synchronized int getStreamingBufferSize() {
        return SoundSystemConfig.streamingBufferSize;
    }
    
    public static synchronized void setNumberStreamingBuffers(final int num) {
        SoundSystemConfig.numberStreamingBuffers = num;
    }
    
    public static synchronized int getNumberStreamingBuffers() {
        return SoundSystemConfig.numberStreamingBuffers;
    }
    
    public static synchronized void setStreamQueueFormatsMatch(final boolean val) {
        SoundSystemConfig.streamQueueFormatsMatch = val;
    }
    
    public static synchronized boolean getStreamQueueFormatsMatch() {
        return SoundSystemConfig.streamQueueFormatsMatch;
    }
    
    public static synchronized void setMaxFileSize(final int size) {
        SoundSystemConfig.maxFileSize = size;
    }
    
    public static synchronized int getMaxFileSize() {
        return SoundSystemConfig.maxFileSize;
    }
    
    public static synchronized void setFileChunkSize(final int size) {
        SoundSystemConfig.fileChunkSize = size;
    }
    
    public static synchronized int getFileChunkSize() {
        return SoundSystemConfig.fileChunkSize;
    }
    
    public static synchronized String getOverrideMIDISynthesizer() {
        return SoundSystemConfig.overrideMIDISynthesizer;
    }
    
    public static synchronized void setOverrideMIDISynthesizer(final String name) {
        SoundSystemConfig.overrideMIDISynthesizer = name;
    }
    
    public static synchronized void setCodec(final String extension, final Class iCodecClass) throws SoundSystemException {
        if (extension == null) {
            throw new SoundSystemException("Parameter 'extension' null in method 'setCodec'.", 2);
        }
        if (iCodecClass == null) {
            throw new SoundSystemException("Parameter 'iCodecClass' null in method 'setCodec'.", 2);
        }
        if (!ICodec.class.isAssignableFrom(iCodecClass)) {
            throw new SoundSystemException("The specified class does not implement interface 'ICodec' in method 'setCodec'", 3);
        }
        if (SoundSystemConfig.codecs == null) {
            SoundSystemConfig.codecs = new LinkedList<Codec>();
        }
        final ListIterator<Codec> i = SoundSystemConfig.codecs.listIterator();
        while (i.hasNext()) {
            final Codec codec = i.next();
            if (extension.matches(codec.extensionRegX)) {
                i.remove();
            }
        }
        SoundSystemConfig.codecs.add(new Codec(extension, iCodecClass));
        if (extension.matches(SoundSystemConfig.EXTENSION_MIDI)) {
            SoundSystemConfig.midiCodec = true;
        }
    }
    
    public static synchronized ICodec getCodec(final String filename) {
        if (SoundSystemConfig.codecs == null) {
            return null;
        }
        final ListIterator<Codec> i = SoundSystemConfig.codecs.listIterator();
        while (i.hasNext()) {
            final Codec codec = i.next();
            if (filename.matches(codec.extensionRegX)) {
                return codec.getInstance();
            }
        }
        return null;
    }
    
    public static boolean midiCodec() {
        return SoundSystemConfig.midiCodec;
    }
    
    public static void addStreamListener(final IStreamListener streamListener) {
        synchronized (SoundSystemConfig.streamListenersLock) {
            if (SoundSystemConfig.streamListeners == null) {
                SoundSystemConfig.streamListeners = new LinkedList<IStreamListener>();
            }
            if (!SoundSystemConfig.streamListeners.contains(streamListener)) {
                SoundSystemConfig.streamListeners.add(streamListener);
            }
        }
    }
    
    public static void removeStreamListener(final IStreamListener streamListener) {
        synchronized (SoundSystemConfig.streamListenersLock) {
            if (SoundSystemConfig.streamListeners == null) {
                SoundSystemConfig.streamListeners = new LinkedList<IStreamListener>();
            }
            if (SoundSystemConfig.streamListeners.contains(streamListener)) {
                SoundSystemConfig.streamListeners.remove(streamListener);
            }
        }
    }
    
    public static void notifyEOS(final String sourcename, final int queueSize) {
        synchronized (SoundSystemConfig.streamListenersLock) {
            if (SoundSystemConfig.streamListeners == null) {
                return;
            }
        }
        final String srcName = sourcename;
        final int qSize = queueSize;
        new Thread() {
            @Override
            public void run() {
                synchronized (SoundSystemConfig.streamListenersLock) {
                    if (SoundSystemConfig.streamListeners == null) {
                        return;
                    }
                    final ListIterator<IStreamListener> i = (ListIterator<IStreamListener>)SoundSystemConfig.streamListeners.listIterator();
                    while (i.hasNext()) {
                        final IStreamListener streamListener = i.next();
                        if (streamListener == null) {
                            i.remove();
                        }
                        else {
                            streamListener.endOfStream(srcName, qSize);
                        }
                    }
                }
            }
        }.start();
    }
    
    private static void errorMessage(final String message) {
        if (SoundSystemConfig.logger != null) {
            SoundSystemConfig.logger.errorMessage("SoundSystemConfig", message, 0);
        }
    }
    
    private static Object runMethod(final Class c, final String method, final Class[] paramTypes, final Object[] params) {
        Method m = null;
        try {
            m = c.getMethod(method, (Class[])paramTypes);
        }
        catch (NoSuchMethodException nsme) {
            errorMessage("NoSuchMethodException thrown when attempting to call method '" + method + "' in " + "method 'runMethod'");
            return null;
        }
        catch (SecurityException se) {
            errorMessage("Access denied when attempting to call method '" + method + "' in method 'runMethod'");
            return null;
        }
        catch (NullPointerException npe) {
            errorMessage("NullPointerException thrown when attempting to call method '" + method + "' in " + "method 'runMethod'");
            return null;
        }
        if (m == null) {
            errorMessage("Method '" + method + "' not found for the class " + "specified in method 'runMethod'");
            return null;
        }
        Object o = null;
        try {
            o = m.invoke(null, params);
        }
        catch (IllegalAccessException iae) {
            errorMessage("IllegalAccessException thrown when attempting to invoke method '" + method + "' in " + "method 'runMethod'");
            return null;
        }
        catch (IllegalArgumentException iae2) {
            errorMessage("IllegalArgumentException thrown when attempting to invoke method '" + method + "' in " + "method 'runMethod'");
            return null;
        }
        catch (InvocationTargetException ite) {
            errorMessage("InvocationTargetException thrown while attempting to invoke method 'Library.getTitle' in method 'getLibraryTitle'");
            return null;
        }
        catch (NullPointerException npe2) {
            errorMessage("NullPointerException thrown when attempting to invoke method '" + method + "' in " + "method 'runMethod'");
            return null;
        }
        catch (ExceptionInInitializerError eiie) {
            errorMessage("ExceptionInInitializerError thrown when attempting to invoke method '" + method + "' in " + "method 'runMethod'");
            return null;
        }
        return o;
    }
    
    static {
        THREAD_SYNC = new Object();
        SoundSystemConfig.EXTENSION_MIDI = ".*[mM][iI][dD][iI]?$";
        SoundSystemConfig.PREFIX_URL = "^[hH][tT][tT][pP]://.*";
        SoundSystemConfig.logger = null;
        SoundSystemConfig.codecs = null;
        SoundSystemConfig.streamListeners = null;
        streamListenersLock = new Object();
        SoundSystemConfig.numberNormalChannels = 28;
        SoundSystemConfig.numberStreamingChannels = 4;
        SoundSystemConfig.masterGain = 1.0f;
        SoundSystemConfig.defaultAttenuationModel = 1;
        SoundSystemConfig.defaultRolloffFactor = 0.03f;
        SoundSystemConfig.dopplerFactor = 0.0f;
        SoundSystemConfig.dopplerVelocity = 1.0f;
        SoundSystemConfig.defaultFadeDistance = 1000.0f;
        SoundSystemConfig.soundFilesPackage = "Sounds/";
        SoundSystemConfig.streamingBufferSize = 131072;
        SoundSystemConfig.numberStreamingBuffers = 3;
        SoundSystemConfig.streamQueueFormatsMatch = false;
        SoundSystemConfig.maxFileSize = 268435456;
        SoundSystemConfig.fileChunkSize = 1048576;
        SoundSystemConfig.midiCodec = false;
        SoundSystemConfig.overrideMIDISynthesizer = "";
    }
    
    private static class Codec
    {
        public String extensionRegX;
        public Class iCodecClass;
        
        public Codec(final String extension, final Class iCodecClass) {
            this.extensionRegX = "";
            if (extension != null && extension.length() > 0) {
                this.extensionRegX = ".*";
                for (int x = 0; x < extension.length(); ++x) {
                    final String c = extension.substring(x, x + 1);
                    this.extensionRegX = this.extensionRegX + "[" + c.toLowerCase(Locale.ENGLISH) + c.toUpperCase(Locale.ENGLISH) + "]";
                }
                this.extensionRegX += "$";
            }
            this.iCodecClass = iCodecClass;
        }
        
        public ICodec getInstance() {
            if (this.iCodecClass == null) {
                return null;
            }
            Object o = null;
            try {
                o = this.iCodecClass.newInstance();
            }
            catch (InstantiationException ie) {
                this.instantiationErrorMessage();
                return null;
            }
            catch (IllegalAccessException iae) {
                this.instantiationErrorMessage();
                return null;
            }
            catch (ExceptionInInitializerError eiie) {
                this.instantiationErrorMessage();
                return null;
            }
            catch (SecurityException se) {
                this.instantiationErrorMessage();
                return null;
            }
            if (o == null) {
                this.instantiationErrorMessage();
                return null;
            }
            return (ICodec)o;
        }
        
        private void instantiationErrorMessage() {
            errorMessage("Unrecognized ICodec implementation in method 'getInstance'.  Ensure that the implementing class has one public, parameterless constructor.");
        }
    }
}
