// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import javax.sound.sampled.AudioFormat;
import java.net.URL;
import java.util.ListIterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;

public class SoundSystem
{
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    protected SoundSystemLogger logger;
    protected Library soundLibrary;
    protected List<CommandObject> commandQueue;
    private List<CommandObject> sourcePlayList;
    protected CommandThread commandThread;
    public Random randomNumberGenerator;
    protected String className;
    private static Class currentLibrary;
    private static boolean initialized;
    private static SoundSystemException lastException;
    
    public SoundSystem() {
        this.className = "SoundSystem";
        this.logger = SoundSystemConfig.getLogger();
        if (this.logger == null) {
            SoundSystemConfig.setLogger(this.logger = new SoundSystemLogger());
        }
        this.linkDefaultLibrariesAndCodecs();
        final LinkedList<Class> libraries = SoundSystemConfig.getLibraries();
        if (libraries != null) {
            final ListIterator<Class> i = (ListIterator<Class>)libraries.listIterator();
            while (i.hasNext()) {
                final Class c = i.next();
                try {
                    this.init(c);
                    return;
                }
                catch (SoundSystemException sse) {
                    this.logger.printExceptionMessage(sse, 1);
                    continue;
                }
                break;
            }
        }
        try {
            this.init(Library.class);
        }
        catch (SoundSystemException sse2) {
            this.logger.printExceptionMessage(sse2, 1);
        }
    }
    
    public SoundSystem(final Class libraryClass) throws SoundSystemException {
        this.className = "SoundSystem";
        this.logger = SoundSystemConfig.getLogger();
        if (this.logger == null) {
            SoundSystemConfig.setLogger(this.logger = new SoundSystemLogger());
        }
        this.linkDefaultLibrariesAndCodecs();
        this.init(libraryClass);
    }
    
    protected void linkDefaultLibrariesAndCodecs() {
    }
    
    protected void init(final Class libraryClass) throws SoundSystemException {
        this.message("", 0);
        this.message("Starting up " + this.className + "...", 0);
        this.randomNumberGenerator = new Random();
        this.commandQueue = new LinkedList<CommandObject>();
        this.sourcePlayList = new LinkedList<CommandObject>();
        (this.commandThread = new CommandThread(this)).start();
        snooze(200L);
        this.newLibrary(libraryClass);
        this.message("", 0);
    }
    
    public void cleanup() {
        boolean killException = false;
        this.message("", 0);
        this.message(this.className + " shutting down...", 0);
        try {
            this.commandThread.kill();
            this.commandThread.interrupt();
        }
        catch (Exception e) {
            killException = true;
        }
        if (!killException) {
            for (int i = 0; i < 50; ++i) {
                if (!this.commandThread.alive()) {
                    break;
                }
                snooze(100L);
            }
        }
        if (killException || this.commandThread.alive()) {
            this.errorMessage("Command thread did not die!", 0);
            this.message("Ignoring errors... continuing clean-up.", 0);
        }
        initialized(true, false);
        currentLibrary(true, null);
        try {
            if (this.soundLibrary != null) {
                this.soundLibrary.cleanup();
            }
        }
        catch (Exception e) {
            this.errorMessage("Problem during Library.cleanup()!", 0);
            this.message("Ignoring errors... continuing clean-up.", 0);
        }
        try {
            if (this.commandQueue != null) {
                this.commandQueue.clear();
            }
        }
        catch (Exception e) {
            this.errorMessage("Unable to clear the command queue!", 0);
            this.message("Ignoring errors... continuing clean-up.", 0);
        }
        try {
            if (this.sourcePlayList != null) {
                this.sourcePlayList.clear();
            }
        }
        catch (Exception e) {
            this.errorMessage("Unable to clear the source management list!", 0);
            this.message("Ignoring errors... continuing clean-up.", 0);
        }
        this.randomNumberGenerator = null;
        this.soundLibrary = null;
        this.commandQueue = null;
        this.sourcePlayList = null;
        this.commandThread = null;
        this.importantMessage("Author: Paul Lamb, www.paulscode.com", 1);
        this.message("", 0);
    }
    
    public void interruptCommandThread() {
        if (this.commandThread == null) {
            this.errorMessage("Command Thread null in method 'interruptCommandThread'", 0);
            return;
        }
        this.commandThread.interrupt();
    }
    
    public void loadSound(final String filename) {
        this.CommandQueue(new CommandObject(2, new FilenameURL(filename)));
        this.commandThread.interrupt();
    }
    
    public void loadSound(final URL url, final String identifier) {
        this.CommandQueue(new CommandObject(2, new FilenameURL(url, identifier)));
        this.commandThread.interrupt();
    }
    
    public void loadSound(final byte[] data, final AudioFormat format, final String identifier) {
        this.CommandQueue(new CommandObject(3, identifier, new SoundBuffer(data, format)));
        this.commandThread.interrupt();
    }
    
    public void unloadSound(final String filename) {
        this.CommandQueue(new CommandObject(4, filename));
        this.commandThread.interrupt();
    }
    
    public void queueSound(final String sourcename, final String filename) {
        this.CommandQueue(new CommandObject(5, sourcename, new FilenameURL(filename)));
        this.commandThread.interrupt();
    }
    
    public void queueSound(final String sourcename, final URL url, final String identifier) {
        this.CommandQueue(new CommandObject(5, sourcename, new FilenameURL(url, identifier)));
        this.commandThread.interrupt();
    }
    
    public void dequeueSound(final String sourcename, final String filename) {
        this.CommandQueue(new CommandObject(6, sourcename, filename));
        this.commandThread.interrupt();
    }
    
    public void fadeOut(final String sourcename, final String filename, final long milis) {
        FilenameURL fu = null;
        if (filename != null) {
            fu = new FilenameURL(filename);
        }
        this.CommandQueue(new CommandObject(7, sourcename, fu, milis));
        this.commandThread.interrupt();
    }
    
    public void fadeOut(final String sourcename, final URL url, final String identifier, final long milis) {
        FilenameURL fu = null;
        if (url != null && identifier != null) {
            fu = new FilenameURL(url, identifier);
        }
        this.CommandQueue(new CommandObject(7, sourcename, fu, milis));
        this.commandThread.interrupt();
    }
    
    public void fadeOutIn(final String sourcename, final String filename, final long milisOut, final long milisIn) {
        this.CommandQueue(new CommandObject(8, sourcename, new FilenameURL(filename), milisOut, milisIn));
        this.commandThread.interrupt();
    }
    
    public void fadeOutIn(final String sourcename, final URL url, final String identifier, final long milisOut, final long milisIn) {
        this.CommandQueue(new CommandObject(8, sourcename, new FilenameURL(url, identifier), milisOut, milisIn));
        this.commandThread.interrupt();
    }
    
    public void checkFadeVolumes() {
        this.CommandQueue(new CommandObject(9));
        this.commandThread.interrupt();
    }
    
    public void backgroundMusic(final String sourcename, final String filename, final boolean toLoop) {
        this.CommandQueue(new CommandObject(12, true, true, toLoop, sourcename, new FilenameURL(filename), 0.0f, 0.0f, 0.0f, 0, 0.0f, false));
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
    }
    
    public void backgroundMusic(final String sourcename, final URL url, final String identifier, final boolean toLoop) {
        this.CommandQueue(new CommandObject(12, true, true, toLoop, sourcename, new FilenameURL(url, identifier), 0.0f, 0.0f, 0.0f, 0, 0.0f, false));
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
    }
    
    public void newSource(final boolean priority, final String sourcename, final String filename, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        this.CommandQueue(new CommandObject(10, priority, false, toLoop, sourcename, new FilenameURL(filename), x, y, z, attmodel, distOrRoll));
        this.commandThread.interrupt();
    }
    
    public void newSource(final boolean priority, final String sourcename, final URL url, final String identifier, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        this.CommandQueue(new CommandObject(10, priority, false, toLoop, sourcename, new FilenameURL(url, identifier), x, y, z, attmodel, distOrRoll));
        this.commandThread.interrupt();
    }
    
    public void newStreamingSource(final boolean priority, final String sourcename, final String filename, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        this.CommandQueue(new CommandObject(10, priority, true, toLoop, sourcename, new FilenameURL(filename), x, y, z, attmodel, distOrRoll));
        this.commandThread.interrupt();
    }
    
    public void newStreamingSource(final boolean priority, final String sourcename, final URL url, final String identifier, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        this.CommandQueue(new CommandObject(10, priority, true, toLoop, sourcename, new FilenameURL(url, identifier), x, y, z, attmodel, distOrRoll));
        this.commandThread.interrupt();
    }
    
    public void rawDataStream(final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        this.CommandQueue(new CommandObject(11, audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll));
        this.commandThread.interrupt();
    }
    
    public String quickPlay(final boolean priority, final String filename, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        final String sourcename = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
        this.CommandQueue(new CommandObject(12, priority, false, toLoop, sourcename, new FilenameURL(filename), x, y, z, attmodel, distOrRoll, true));
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
        return sourcename;
    }
    
    public String quickPlay(final boolean priority, final URL url, final String identifier, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        final String sourcename = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
        this.CommandQueue(new CommandObject(12, priority, false, toLoop, sourcename, new FilenameURL(url, identifier), x, y, z, attmodel, distOrRoll, true));
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
        return sourcename;
    }
    
    public String quickStream(final boolean priority, final String filename, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        final String sourcename = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
        this.CommandQueue(new CommandObject(12, priority, true, toLoop, sourcename, new FilenameURL(filename), x, y, z, attmodel, distOrRoll, true));
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
        return sourcename;
    }
    
    public String quickStream(final boolean priority, final URL url, final String identifier, final boolean toLoop, final float x, final float y, final float z, final int attmodel, final float distOrRoll) {
        final String sourcename = "Source_" + this.randomNumberGenerator.nextInt() + "_" + this.randomNumberGenerator.nextInt();
        this.CommandQueue(new CommandObject(12, priority, true, toLoop, sourcename, new FilenameURL(url, identifier), x, y, z, attmodel, distOrRoll, true));
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
        return sourcename;
    }
    
    public void setPosition(final String sourcename, final float x, final float y, final float z) {
        this.CommandQueue(new CommandObject(13, sourcename, x, y, z));
        this.commandThread.interrupt();
    }
    
    public void setVolume(final String sourcename, final float value) {
        this.CommandQueue(new CommandObject(14, sourcename, value));
        this.commandThread.interrupt();
    }
    
    public float getVolume(final String sourcename) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary != null) {
                return this.soundLibrary.getVolume(sourcename);
            }
            return 0.0f;
        }
    }
    
    public void setPitch(final String sourcename, final float value) {
        this.CommandQueue(new CommandObject(15, sourcename, value));
        this.commandThread.interrupt();
    }
    
    public float getPitch(final String sourcename) {
        if (this.soundLibrary != null) {
            return this.soundLibrary.getPitch(sourcename);
        }
        return 1.0f;
    }
    
    public void setPriority(final String sourcename, final boolean pri) {
        this.CommandQueue(new CommandObject(16, sourcename, pri));
        this.commandThread.interrupt();
    }
    
    public void setLooping(final String sourcename, final boolean lp) {
        this.CommandQueue(new CommandObject(17, sourcename, lp));
        this.commandThread.interrupt();
    }
    
    public void setAttenuation(final String sourcename, final int model) {
        this.CommandQueue(new CommandObject(18, sourcename, model));
        this.commandThread.interrupt();
    }
    
    public void setDistOrRoll(final String sourcename, final float dr) {
        this.CommandQueue(new CommandObject(19, sourcename, dr));
        this.commandThread.interrupt();
    }
    
    public void changeDopplerFactor(final float dopplerFactor) {
        this.CommandQueue(new CommandObject(20, dopplerFactor));
        this.commandThread.interrupt();
    }
    
    public void changeDopplerVelocity(final float dopplerVelocity) {
        this.CommandQueue(new CommandObject(21, dopplerVelocity));
        this.commandThread.interrupt();
    }
    
    public void setVelocity(final String sourcename, final float x, final float y, final float z) {
        this.CommandQueue(new CommandObject(22, sourcename, x, y, z));
        this.commandThread.interrupt();
    }
    
    public void setListenerVelocity(final float x, final float y, final float z) {
        this.CommandQueue(new CommandObject(23, x, y, z));
        this.commandThread.interrupt();
    }
    
    public float millisecondsPlayed(final String sourcename) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            return this.soundLibrary.millisecondsPlayed(sourcename);
        }
    }
    
    public void feedRawAudioData(final String sourcename, final byte[] buffer) {
        this.CommandQueue(new CommandObject(25, sourcename, buffer));
        this.commandThread.interrupt();
    }
    
    public void play(final String sourcename) {
        this.CommandQueue(new CommandObject(24, sourcename));
        this.commandThread.interrupt();
    }
    
    public void pause(final String sourcename) {
        this.CommandQueue(new CommandObject(26, sourcename));
        this.commandThread.interrupt();
    }
    
    public void stop(final String sourcename) {
        this.CommandQueue(new CommandObject(27, sourcename));
        this.commandThread.interrupt();
    }
    
    public void rewind(final String sourcename) {
        this.CommandQueue(new CommandObject(28, sourcename));
        this.commandThread.interrupt();
    }
    
    public void flush(final String sourcename) {
        this.CommandQueue(new CommandObject(29, sourcename));
        this.commandThread.interrupt();
    }
    
    public void cull(final String sourcename) {
        this.CommandQueue(new CommandObject(30, sourcename));
        this.commandThread.interrupt();
    }
    
    public void activate(final String sourcename) {
        this.CommandQueue(new CommandObject(31, sourcename));
        this.commandThread.interrupt();
    }
    
    public void setTemporary(final String sourcename, final boolean temporary) {
        this.CommandQueue(new CommandObject(32, sourcename, temporary));
        this.commandThread.interrupt();
    }
    
    public void removeSource(final String sourcename) {
        this.CommandQueue(new CommandObject(33, sourcename));
        this.commandThread.interrupt();
    }
    
    public void moveListener(final float x, final float y, final float z) {
        this.CommandQueue(new CommandObject(34, x, y, z));
        this.commandThread.interrupt();
    }
    
    public void setListenerPosition(final float x, final float y, final float z) {
        this.CommandQueue(new CommandObject(35, x, y, z));
        this.commandThread.interrupt();
    }
    
    public void turnListener(final float angle) {
        this.CommandQueue(new CommandObject(36, angle));
        this.commandThread.interrupt();
    }
    
    public void setListenerAngle(final float angle) {
        this.CommandQueue(new CommandObject(37, angle));
        this.commandThread.interrupt();
    }
    
    public void setListenerOrientation(final float lookX, final float lookY, final float lookZ, final float upX, final float upY, final float upZ) {
        this.CommandQueue(new CommandObject(38, lookX, lookY, lookZ, upX, upY, upZ));
        this.commandThread.interrupt();
    }
    
    public void setMasterVolume(final float value) {
        this.CommandQueue(new CommandObject(39, value));
        this.commandThread.interrupt();
    }
    
    public float getMasterVolume() {
        return SoundSystemConfig.getMasterGain();
    }
    
    public ListenerData getListenerData() {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            return this.soundLibrary.getListenerData();
        }
    }
    
    public boolean switchLibrary(final Class libraryClass) throws SoundSystemException {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            initialized(true, false);
            HashMap<String, Source> sourceMap = null;
            ListenerData listenerData = null;
            boolean wasMidiChannel = false;
            MidiChannel midiChannel = null;
            FilenameURL midiFilenameURL = null;
            String midiSourcename = "";
            boolean midiToLoop = true;
            if (this.soundLibrary != null) {
                currentLibrary(true, null);
                sourceMap = this.copySources(this.soundLibrary.getSources());
                listenerData = this.soundLibrary.getListenerData();
                midiChannel = this.soundLibrary.getMidiChannel();
                if (midiChannel != null) {
                    wasMidiChannel = true;
                    midiToLoop = midiChannel.getLooping();
                    midiSourcename = midiChannel.getSourcename();
                    midiFilenameURL = midiChannel.getFilenameURL();
                }
                this.soundLibrary.cleanup();
                this.soundLibrary = null;
            }
            this.message("", 0);
            this.message("Switching to " + SoundSystemConfig.getLibraryTitle(libraryClass), 0);
            this.message("(" + SoundSystemConfig.getLibraryDescription(libraryClass) + ")", 1);
            try {
                this.soundLibrary = libraryClass.newInstance();
            }
            catch (InstantiationException ie) {
                this.errorMessage("The specified library did not load properly", 1);
            }
            catch (IllegalAccessException iae) {
                this.errorMessage("The specified library did not load properly", 1);
            }
            catch (ExceptionInInitializerError eiie) {
                this.errorMessage("The specified library did not load properly", 1);
            }
            catch (SecurityException se) {
                this.errorMessage("The specified library did not load properly", 1);
            }
            SoundSystemException sse = null;
            if (this.errorCheck(this.soundLibrary == null, "Library null after initialization in method 'switchLibrary'", 1)) {
                sse = new SoundSystemException(this.className + " did not load properly.  " + "Library was null after initialization.", 4);
                lastException(true, sse);
                initialized(true, true);
                throw sse;
            }
            try {
                this.soundLibrary.init();
            }
            catch (SoundSystemException sse) {
                lastException(true, sse);
                initialized(true, true);
                throw sse;
            }
            this.soundLibrary.setListenerData(listenerData);
            if (wasMidiChannel) {
                if (midiChannel != null) {
                    midiChannel.cleanup();
                }
                midiChannel = new MidiChannel(midiToLoop, midiSourcename, midiFilenameURL);
                this.soundLibrary.setMidiChannel(midiChannel);
            }
            this.soundLibrary.copySources(sourceMap);
            this.message("", 0);
            lastException(true, null);
            initialized(true, true);
            return true;
        }
    }
    
    public boolean newLibrary(final Class libraryClass) throws SoundSystemException {
        initialized(true, false);
        this.CommandQueue(new CommandObject(40, libraryClass));
        this.commandThread.interrupt();
        for (int x = 0; !initialized(false, false) && x < 100; ++x) {
            snooze(400L);
            this.commandThread.interrupt();
        }
        if (!initialized(false, false)) {
            final SoundSystemException sse = new SoundSystemException(this.className + " did not load after 30 seconds.", 4);
            lastException(true, sse);
            throw sse;
        }
        final SoundSystemException sse = lastException(false, null);
        if (sse != null) {
            throw sse;
        }
        return true;
    }
    
    private void CommandNewLibrary(final Class libraryClass) {
        initialized(true, false);
        String headerMessage = "Initializing ";
        if (this.soundLibrary != null) {
            currentLibrary(true, null);
            headerMessage = "Switching to ";
            this.soundLibrary.cleanup();
            this.soundLibrary = null;
        }
        this.message(headerMessage + SoundSystemConfig.getLibraryTitle(libraryClass), 0);
        this.message("(" + SoundSystemConfig.getLibraryDescription(libraryClass) + ")", 1);
        try {
            this.soundLibrary = libraryClass.newInstance();
        }
        catch (InstantiationException ie) {
            this.errorMessage("The specified library did not load properly", 1);
        }
        catch (IllegalAccessException iae) {
            this.errorMessage("The specified library did not load properly", 1);
        }
        catch (ExceptionInInitializerError eiie) {
            this.errorMessage("The specified library did not load properly", 1);
        }
        catch (SecurityException se) {
            this.errorMessage("The specified library did not load properly", 1);
        }
        if (this.errorCheck(this.soundLibrary == null, "Library null after initialization in method 'newLibrary'", 1)) {
            lastException(true, new SoundSystemException(this.className + " did not load properly.  " + "Library was null after initialization.", 4));
            this.importantMessage("Switching to silent mode", 1);
            try {
                this.soundLibrary = new Library();
            }
            catch (SoundSystemException sse) {
                lastException(true, new SoundSystemException("Silent mode did not load properly.  Library was null after initialization.", 4));
                initialized(true, true);
                return;
            }
        }
        try {
            this.soundLibrary.init();
        }
        catch (SoundSystemException sse) {
            lastException(true, sse);
            initialized(true, true);
            return;
        }
        lastException(true, null);
        initialized(true, true);
    }
    
    private void CommandInitialize() {
        try {
            if (this.errorCheck(this.soundLibrary == null, "Library null after initialization in method 'CommandInitialize'", 1)) {
                final SoundSystemException sse = new SoundSystemException(this.className + " did not load properly.  " + "Library was null after initialization.", 4);
                lastException(true, sse);
                throw sse;
            }
            this.soundLibrary.init();
        }
        catch (SoundSystemException sse) {
            lastException(true, sse);
            initialized(true, true);
        }
    }
    
    private void CommandLoadSound(final FilenameURL filenameURL) {
        if (this.soundLibrary != null) {
            this.soundLibrary.loadSound(filenameURL);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandLoadSound'", 0);
        }
    }
    
    private void CommandLoadSound(final SoundBuffer buffer, final String identifier) {
        if (this.soundLibrary != null) {
            this.soundLibrary.loadSound(buffer, identifier);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandLoadSound'", 0);
        }
    }
    
    private void CommandUnloadSound(final String filename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.unloadSound(filename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandLoadSound'", 0);
        }
    }
    
    private void CommandQueueSound(final String sourcename, final FilenameURL filenameURL) {
        if (this.soundLibrary != null) {
            this.soundLibrary.queueSound(sourcename, filenameURL);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandQueueSound'", 0);
        }
    }
    
    private void CommandDequeueSound(final String sourcename, final String filename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.dequeueSound(sourcename, filename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandDequeueSound'", 0);
        }
    }
    
    private void CommandFadeOut(final String sourcename, final FilenameURL filenameURL, final long milis) {
        if (this.soundLibrary != null) {
            this.soundLibrary.fadeOut(sourcename, filenameURL, milis);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandFadeOut'", 0);
        }
    }
    
    private void CommandFadeOutIn(final String sourcename, final FilenameURL filenameURL, final long milisOut, final long milisIn) {
        if (this.soundLibrary != null) {
            this.soundLibrary.fadeOutIn(sourcename, filenameURL, milisOut, milisIn);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandFadeOutIn'", 0);
        }
    }
    
    private void CommandCheckFadeVolumes() {
        if (this.soundLibrary != null) {
            this.soundLibrary.checkFadeVolumes();
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandCheckFadeVolumes'", 0);
        }
    }
    
    private void CommandNewSource(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float x, final float y, final float z, final int attModel, final float distORroll) {
        if (this.soundLibrary != null) {
            if (filenameURL.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI) && !SoundSystemConfig.midiCodec()) {
                this.soundLibrary.loadMidi(toLoop, sourcename, filenameURL);
            }
            else {
                this.soundLibrary.newSource(priority, toStream, toLoop, sourcename, filenameURL, x, y, z, attModel, distORroll);
            }
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandNewSource'", 0);
        }
    }
    
    private void CommandRawDataStream(final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        if (this.soundLibrary != null) {
            this.soundLibrary.rawDataStream(audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandRawDataStream'", 0);
        }
    }
    
    private void CommandQuickPlay(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float x, final float y, final float z, final int attModel, final float distORroll, final boolean temporary) {
        if (this.soundLibrary != null) {
            if (filenameURL.getFilename().matches(SoundSystemConfig.EXTENSION_MIDI) && !SoundSystemConfig.midiCodec()) {
                this.soundLibrary.loadMidi(toLoop, sourcename, filenameURL);
            }
            else {
                this.soundLibrary.quickPlay(priority, toStream, toLoop, sourcename, filenameURL, x, y, z, attModel, distORroll, temporary);
            }
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandQuickPlay'", 0);
        }
    }
    
    private void CommandSetPosition(final String sourcename, final float x, final float y, final float z) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setPosition(sourcename, x, y, z);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandMoveSource'", 0);
        }
    }
    
    private void CommandSetVolume(final String sourcename, final float value) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setVolume(sourcename, value);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetVolume'", 0);
        }
    }
    
    private void CommandSetPitch(final String sourcename, final float value) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setPitch(sourcename, value);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetPitch'", 0);
        }
    }
    
    private void CommandSetPriority(final String sourcename, final boolean pri) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setPriority(sourcename, pri);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetPriority'", 0);
        }
    }
    
    private void CommandSetLooping(final String sourcename, final boolean lp) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setLooping(sourcename, lp);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetLooping'", 0);
        }
    }
    
    private void CommandSetAttenuation(final String sourcename, final int model) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setAttenuation(sourcename, model);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetAttenuation'", 0);
        }
    }
    
    private void CommandSetDistOrRoll(final String sourcename, final float dr) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setDistOrRoll(sourcename, dr);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetDistOrRoll'", 0);
        }
    }
    
    private void CommandChangeDopplerFactor(final float dopplerFactor) {
        if (this.soundLibrary != null) {
            SoundSystemConfig.setDopplerFactor(dopplerFactor);
            this.soundLibrary.dopplerChanged();
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetDopplerFactor'", 0);
        }
    }
    
    private void CommandChangeDopplerVelocity(final float dopplerVelocity) {
        if (this.soundLibrary != null) {
            SoundSystemConfig.setDopplerVelocity(dopplerVelocity);
            this.soundLibrary.dopplerChanged();
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetDopplerFactor'", 0);
        }
    }
    
    private void CommandSetVelocity(final String sourcename, final float x, final float y, final float z) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setVelocity(sourcename, x, y, z);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandVelocity'", 0);
        }
    }
    
    private void CommandSetListenerVelocity(final float x, final float y, final float z) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setListenerVelocity(x, y, z);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerVelocity'", 0);
        }
    }
    
    private void CommandPlay(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.play(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandPlay'", 0);
        }
    }
    
    private void CommandFeedRawAudioData(final String sourcename, final byte[] buffer) {
        if (this.soundLibrary != null) {
            this.soundLibrary.feedRawAudioData(sourcename, buffer);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandFeedRawAudioData'", 0);
        }
    }
    
    private void CommandPause(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.pause(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandPause'", 0);
        }
    }
    
    private void CommandStop(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.stop(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandStop'", 0);
        }
    }
    
    private void CommandRewind(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.rewind(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandRewind'", 0);
        }
    }
    
    private void CommandFlush(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.flush(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandFlush'", 0);
        }
    }
    
    private void CommandSetTemporary(final String sourcename, final boolean temporary) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setTemporary(sourcename, temporary);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetActive'", 0);
        }
    }
    
    private void CommandRemoveSource(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.removeSource(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandRemoveSource'", 0);
        }
    }
    
    private void CommandMoveListener(final float x, final float y, final float z) {
        if (this.soundLibrary != null) {
            this.soundLibrary.moveListener(x, y, z);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandMoveListener'", 0);
        }
    }
    
    private void CommandSetListenerPosition(final float x, final float y, final float z) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setListenerPosition(x, y, z);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerPosition'", 0);
        }
    }
    
    private void CommandTurnListener(final float angle) {
        if (this.soundLibrary != null) {
            this.soundLibrary.turnListener(angle);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandTurnListener'", 0);
        }
    }
    
    private void CommandSetListenerAngle(final float angle) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setListenerAngle(angle);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerAngle'", 0);
        }
    }
    
    private void CommandSetListenerOrientation(final float lookX, final float lookY, final float lookZ, final float upX, final float upY, final float upZ) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setListenerOrientation(lookX, lookY, lookZ, upX, upY, upZ);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetListenerOrientation'", 0);
        }
    }
    
    private void CommandCull(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.cull(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandCull'", 0);
        }
    }
    
    private void CommandActivate(final String sourcename) {
        if (this.soundLibrary != null) {
            this.soundLibrary.activate(sourcename);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandActivate'", 0);
        }
    }
    
    private void CommandSetMasterVolume(final float value) {
        if (this.soundLibrary != null) {
            this.soundLibrary.setMasterVolume(value);
        }
        else {
            this.errorMessage("Variable 'soundLibrary' null in method 'CommandSetMasterVolume'", 0);
        }
    }
    
    protected void ManageSources() {
    }
    
    public boolean CommandQueue(final CommandObject newCommand) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (newCommand == null) {
                boolean activations = false;
                while (this.commandQueue != null && this.commandQueue.size() > 0) {
                    final CommandObject commandObject = this.commandQueue.remove(0);
                    if (commandObject != null) {
                        switch (commandObject.Command) {
                            case 1: {
                                this.CommandInitialize();
                                continue;
                            }
                            case 2: {
                                this.CommandLoadSound((FilenameURL)commandObject.objectArgs[0]);
                                continue;
                            }
                            case 3: {
                                this.CommandLoadSound((SoundBuffer)commandObject.objectArgs[0], commandObject.stringArgs[0]);
                                continue;
                            }
                            case 4: {
                                this.CommandUnloadSound(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 5: {
                                this.CommandQueueSound(commandObject.stringArgs[0], (FilenameURL)commandObject.objectArgs[0]);
                                continue;
                            }
                            case 6: {
                                this.CommandDequeueSound(commandObject.stringArgs[0], commandObject.stringArgs[1]);
                                continue;
                            }
                            case 7: {
                                this.CommandFadeOut(commandObject.stringArgs[0], (FilenameURL)commandObject.objectArgs[0], commandObject.longArgs[0]);
                                continue;
                            }
                            case 8: {
                                this.CommandFadeOutIn(commandObject.stringArgs[0], (FilenameURL)commandObject.objectArgs[0], commandObject.longArgs[0], commandObject.longArgs[1]);
                                continue;
                            }
                            case 9: {
                                this.CommandCheckFadeVolumes();
                                continue;
                            }
                            case 10: {
                                this.CommandNewSource(commandObject.boolArgs[0], commandObject.boolArgs[1], commandObject.boolArgs[2], commandObject.stringArgs[0], (FilenameURL)commandObject.objectArgs[0], commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2], commandObject.intArgs[0], commandObject.floatArgs[3]);
                                continue;
                            }
                            case 11: {
                                this.CommandRawDataStream((AudioFormat)commandObject.objectArgs[0], commandObject.boolArgs[0], commandObject.stringArgs[0], commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2], commandObject.intArgs[0], commandObject.floatArgs[3]);
                                continue;
                            }
                            case 12: {
                                this.CommandQuickPlay(commandObject.boolArgs[0], commandObject.boolArgs[1], commandObject.boolArgs[2], commandObject.stringArgs[0], (FilenameURL)commandObject.objectArgs[0], commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2], commandObject.intArgs[0], commandObject.floatArgs[3], commandObject.boolArgs[3]);
                                continue;
                            }
                            case 13: {
                                this.CommandSetPosition(commandObject.stringArgs[0], commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2]);
                                continue;
                            }
                            case 14: {
                                this.CommandSetVolume(commandObject.stringArgs[0], commandObject.floatArgs[0]);
                                continue;
                            }
                            case 15: {
                                this.CommandSetPitch(commandObject.stringArgs[0], commandObject.floatArgs[0]);
                                continue;
                            }
                            case 16: {
                                this.CommandSetPriority(commandObject.stringArgs[0], commandObject.boolArgs[0]);
                                continue;
                            }
                            case 17: {
                                this.CommandSetLooping(commandObject.stringArgs[0], commandObject.boolArgs[0]);
                                continue;
                            }
                            case 18: {
                                this.CommandSetAttenuation(commandObject.stringArgs[0], commandObject.intArgs[0]);
                                continue;
                            }
                            case 19: {
                                this.CommandSetDistOrRoll(commandObject.stringArgs[0], commandObject.floatArgs[0]);
                                continue;
                            }
                            case 20: {
                                this.CommandChangeDopplerFactor(commandObject.floatArgs[0]);
                                continue;
                            }
                            case 21: {
                                this.CommandChangeDopplerVelocity(commandObject.floatArgs[0]);
                                continue;
                            }
                            case 22: {
                                this.CommandSetVelocity(commandObject.stringArgs[0], commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2]);
                                continue;
                            }
                            case 23: {
                                this.CommandSetListenerVelocity(commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2]);
                                continue;
                            }
                            case 24: {
                                this.sourcePlayList.add(commandObject);
                                continue;
                            }
                            case 25: {
                                this.sourcePlayList.add(commandObject);
                                continue;
                            }
                            case 26: {
                                this.CommandPause(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 27: {
                                this.CommandStop(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 28: {
                                this.CommandRewind(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 29: {
                                this.CommandFlush(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 30: {
                                this.CommandCull(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 31: {
                                activations = true;
                                this.CommandActivate(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 32: {
                                this.CommandSetTemporary(commandObject.stringArgs[0], commandObject.boolArgs[0]);
                                continue;
                            }
                            case 33: {
                                this.CommandRemoveSource(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 34: {
                                this.CommandMoveListener(commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2]);
                                continue;
                            }
                            case 35: {
                                this.CommandSetListenerPosition(commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2]);
                                continue;
                            }
                            case 36: {
                                this.CommandTurnListener(commandObject.floatArgs[0]);
                                continue;
                            }
                            case 37: {
                                this.CommandSetListenerAngle(commandObject.floatArgs[0]);
                                continue;
                            }
                            case 38: {
                                this.CommandSetListenerOrientation(commandObject.floatArgs[0], commandObject.floatArgs[1], commandObject.floatArgs[2], commandObject.floatArgs[3], commandObject.floatArgs[4], commandObject.floatArgs[5]);
                                continue;
                            }
                            case 39: {
                                this.CommandSetMasterVolume(commandObject.floatArgs[0]);
                                continue;
                            }
                            case 40: {
                                this.CommandNewLibrary(commandObject.classArgs[0]);
                                continue;
                            }
                            default: {
                                continue;
                            }
                        }
                    }
                }
                if (activations) {
                    this.soundLibrary.replaySources();
                }
                while (this.sourcePlayList != null && this.sourcePlayList.size() > 0) {
                    final CommandObject commandObject = this.sourcePlayList.remove(0);
                    if (commandObject != null) {
                        switch (commandObject.Command) {
                            case 24: {
                                this.CommandPlay(commandObject.stringArgs[0]);
                                continue;
                            }
                            case 25: {
                                this.CommandFeedRawAudioData(commandObject.stringArgs[0], commandObject.buffer);
                                continue;
                            }
                        }
                    }
                }
                return this.commandQueue != null && this.commandQueue.size() > 0;
            }
            if (this.commandQueue == null) {
                return false;
            }
            this.commandQueue.add(newCommand);
            return true;
        }
    }
    
    public void removeTemporarySources() {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary != null) {
                this.soundLibrary.removeTemporarySources();
            }
        }
    }
    
    public boolean playing(final String sourcename) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary == null) {
                return false;
            }
            final Source src = this.soundLibrary.getSources().get(sourcename);
            return src != null && src.playing();
        }
    }
    
    public boolean playing() {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (this.soundLibrary == null) {
                return false;
            }
            final HashMap<String, Source> sourceMap = this.soundLibrary.getSources();
            if (sourceMap == null) {
                return false;
            }
            final Set<String> keys = sourceMap.keySet();
            for (final String sourcename : keys) {
                final Source source = sourceMap.get(sourcename);
                if (source != null && source.playing()) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private HashMap<String, Source> copySources(final HashMap<String, Source> sourceMap) {
        final Set<String> keys = sourceMap.keySet();
        final Iterator<String> iter = keys.iterator();
        final HashMap<String, Source> returnMap = new HashMap<String, Source>();
        while (iter.hasNext()) {
            final String sourcename = iter.next();
            final Source source = sourceMap.get(sourcename);
            if (source != null) {
                returnMap.put(sourcename, new Source(source, null));
            }
        }
        return returnMap;
    }
    
    public static boolean libraryCompatible(final Class libraryClass) {
        SoundSystemLogger logger = SoundSystemConfig.getLogger();
        if (logger == null) {
            logger = new SoundSystemLogger();
            SoundSystemConfig.setLogger(logger);
        }
        logger.message("", 0);
        logger.message("Checking if " + SoundSystemConfig.getLibraryTitle(libraryClass) + " is compatible...", 0);
        final boolean comp = SoundSystemConfig.libraryCompatible(libraryClass);
        if (comp) {
            logger.message("...yes", 1);
        }
        else {
            logger.message("...no", 1);
        }
        return comp;
    }
    
    public static Class currentLibrary() {
        return currentLibrary(false, null);
    }
    
    public static boolean initialized() {
        return initialized(false, false);
    }
    
    public static SoundSystemException getLastException() {
        return lastException(false, null);
    }
    
    public static void setException(final SoundSystemException e) {
        lastException(true, e);
    }
    
    private static boolean initialized(final boolean action, final boolean value) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (action) {
                SoundSystem.initialized = value;
            }
            return SoundSystem.initialized;
        }
    }
    
    private static Class currentLibrary(final boolean action, final Class value) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (action) {
                SoundSystem.currentLibrary = value;
            }
            return SoundSystem.currentLibrary;
        }
    }
    
    private static SoundSystemException lastException(final boolean action, final SoundSystemException e) {
        synchronized (SoundSystemConfig.THREAD_SYNC) {
            if (action) {
                SoundSystem.lastException = e;
            }
            return SoundSystem.lastException;
        }
    }
    
    protected static void snooze(final long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException ex) {}
    }
    
    protected void message(final String message, final int indent) {
        this.logger.message(message, indent);
    }
    
    protected void importantMessage(final String message, final int indent) {
        this.logger.importantMessage(message, indent);
    }
    
    protected boolean errorCheck(final boolean error, final String message, final int indent) {
        return this.logger.errorCheck(error, this.className, message, indent);
    }
    
    protected void errorMessage(final String message, final int indent) {
        this.logger.errorMessage(this.className, message, indent);
    }
    
    static {
        SoundSystem.currentLibrary = null;
        SoundSystem.initialized = false;
        SoundSystem.lastException = null;
    }
}
