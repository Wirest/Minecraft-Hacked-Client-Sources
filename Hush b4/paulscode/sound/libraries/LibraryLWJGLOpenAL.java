// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.libraries;

import paulscode.sound.ListenerData;
import paulscode.sound.Source;
import javax.sound.sampled.AudioFormat;
import java.net.URL;
import paulscode.sound.ICodec;
import java.nio.ByteBuffer;
import paulscode.sound.SoundBuffer;
import paulscode.sound.FilenameURL;
import java.util.Iterator;
import java.util.Set;
import paulscode.sound.Channel;
import paulscode.sound.SoundSystemConfig;
import org.lwjgl.openal.AL10;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import paulscode.sound.SoundSystemException;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.nio.FloatBuffer;
import paulscode.sound.Library;

public class LibraryLWJGLOpenAL extends Library
{
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final boolean XXX = false;
    private FloatBuffer listenerPositionAL;
    private FloatBuffer listenerOrientation;
    private FloatBuffer listenerVelocity;
    private HashMap<String, IntBuffer> ALBufferMap;
    private static boolean alPitchSupported;
    
    public LibraryLWJGLOpenAL() throws SoundSystemException {
        this.listenerPositionAL = null;
        this.listenerOrientation = null;
        this.listenerVelocity = null;
        this.ALBufferMap = null;
        this.ALBufferMap = new HashMap<String, IntBuffer>();
        this.reverseByteOrder = true;
    }
    
    @Override
    public void init() throws SoundSystemException {
        boolean errors = false;
        try {
            AL.create();
            errors = this.checkALError();
        }
        catch (LWJGLException e) {
            this.errorMessage("Unable to initialize OpenAL.  Probable cause: OpenAL not supported.");
            this.printStackTrace(e);
            throw new Exception(e.getMessage(), 101);
        }
        if (errors) {
            this.importantMessage("OpenAL did not initialize properly!");
        }
        else {
            this.message("OpenAL initialized.");
        }
        this.listenerPositionAL = BufferUtils.createFloatBuffer(3).put(new float[] { this.listener.position.x, this.listener.position.y, this.listener.position.z });
        this.listenerOrientation = BufferUtils.createFloatBuffer(6).put(new float[] { this.listener.lookAt.x, this.listener.lookAt.y, this.listener.lookAt.z, this.listener.up.x, this.listener.up.y, this.listener.up.z });
        this.listenerVelocity = BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f });
        this.listenerPositionAL.flip();
        this.listenerOrientation.flip();
        this.listenerVelocity.flip();
        AL10.alListener(4100, this.listenerPositionAL);
        errors = (this.checkALError() || errors);
        AL10.alListener(4111, this.listenerOrientation);
        errors = (this.checkALError() || errors);
        AL10.alListener(4102, this.listenerVelocity);
        errors = (this.checkALError() || errors);
        AL10.alDopplerFactor(SoundSystemConfig.getDopplerFactor());
        errors = (this.checkALError() || errors);
        AL10.alDopplerVelocity(SoundSystemConfig.getDopplerVelocity());
        errors = (this.checkALError() || errors);
        if (errors) {
            this.importantMessage("OpenAL did not initialize properly!");
            throw new Exception("Problem encountered while loading OpenAL or creating the listener.  Probable cause:  OpenAL not supported", 101);
        }
        super.init();
        final ChannelLWJGLOpenAL channel = this.normalChannels.get(1);
        try {
            AL10.alSourcef(channel.ALSource.get(0), 4099, 1.0f);
            if (this.checkALError()) {
                alPitchSupported(true, false);
                throw new Exception("OpenAL: AL_PITCH not supported.", 108);
            }
            alPitchSupported(true, true);
        }
        catch (java.lang.Exception e2) {
            alPitchSupported(true, false);
            throw new Exception("OpenAL: AL_PITCH not supported.", 108);
        }
    }
    
    public static boolean libraryCompatible() {
        if (AL.isCreated()) {
            return true;
        }
        try {
            AL.create();
        }
        catch (java.lang.Exception e) {
            return false;
        }
        try {
            AL.destroy();
        }
        catch (java.lang.Exception ex) {}
        return true;
    }
    
    @Override
    protected Channel createChannel(final int type) {
        final IntBuffer ALSource = BufferUtils.createIntBuffer(1);
        try {
            AL10.alGenSources(ALSource);
        }
        catch (java.lang.Exception e) {
            AL10.alGetError();
            return null;
        }
        if (AL10.alGetError() != 0) {
            return null;
        }
        final ChannelLWJGLOpenAL channel = new ChannelLWJGLOpenAL(type, ALSource);
        return channel;
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        final Set<String> keys = this.bufferMap.keySet();
        for (final String filename : keys) {
            final IntBuffer buffer = this.ALBufferMap.get(filename);
            if (buffer != null) {
                AL10.alDeleteBuffers(buffer);
                this.checkALError();
                buffer.clear();
            }
        }
        this.bufferMap.clear();
        AL.destroy();
        this.bufferMap = null;
        this.listenerPositionAL = null;
        this.listenerOrientation = null;
        this.listenerVelocity = null;
    }
    
    @Override
    public boolean loadSound(final FilenameURL filenameURL) {
        if (this.bufferMap == null) {
            this.bufferMap = new HashMap<String, SoundBuffer>();
            this.importantMessage("Buffer Map was null in method 'loadSound'");
        }
        if (this.ALBufferMap == null) {
            this.ALBufferMap = new HashMap<String, IntBuffer>();
            this.importantMessage("Open AL Buffer Map was null in method'loadSound'");
        }
        if (this.errorCheck(filenameURL == null, "Filename/URL not specified in method 'loadSound'")) {
            return false;
        }
        if (this.bufferMap.get(filenameURL.getFilename()) != null) {
            return true;
        }
        ICodec codec = SoundSystemConfig.getCodec(filenameURL.getFilename());
        if (this.errorCheck(codec == null, "No codec found for file '" + filenameURL.getFilename() + "' in method 'loadSound'")) {
            return false;
        }
        codec.reverseByteOrder(true);
        final URL url = filenameURL.getURL();
        if (this.errorCheck(url == null, "Unable to open file '" + filenameURL.getFilename() + "' in method 'loadSound'")) {
            return false;
        }
        codec.initialize(url);
        final SoundBuffer buffer = codec.readAll();
        codec.cleanup();
        codec = null;
        if (this.errorCheck(buffer == null, "Sound buffer null in method 'loadSound'")) {
            return false;
        }
        this.bufferMap.put(filenameURL.getFilename(), buffer);
        final AudioFormat audioFormat = buffer.audioFormat;
        int soundFormat = 0;
        if (audioFormat.getChannels() == 1) {
            if (audioFormat.getSampleSizeInBits() == 8) {
                soundFormat = 4352;
            }
            else {
                if (audioFormat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'loadSound'");
                    return false;
                }
                soundFormat = 4353;
            }
        }
        else {
            if (audioFormat.getChannels() != 2) {
                this.errorMessage("File neither mono nor stereo in method 'loadSound'");
                return false;
            }
            if (audioFormat.getSampleSizeInBits() == 8) {
                soundFormat = 4354;
            }
            else {
                if (audioFormat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'loadSound'");
                    return false;
                }
                soundFormat = 4355;
            }
        }
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(intBuffer);
        if (this.errorCheck(AL10.alGetError() != 0, "alGenBuffers error when loading " + filenameURL.getFilename())) {
            return false;
        }
        AL10.alBufferData(intBuffer.get(0), soundFormat, (ByteBuffer)BufferUtils.createByteBuffer(buffer.audioData.length).put(buffer.audioData).flip(), (int)audioFormat.getSampleRate());
        if (this.errorCheck(AL10.alGetError() != 0, "alBufferData error when loading " + filenameURL.getFilename()) && this.errorCheck(intBuffer == null, "Sound buffer was not created for " + filenameURL.getFilename())) {
            return false;
        }
        this.ALBufferMap.put(filenameURL.getFilename(), intBuffer);
        return true;
    }
    
    @Override
    public boolean loadSound(final SoundBuffer buffer, final String identifier) {
        if (this.bufferMap == null) {
            this.bufferMap = new HashMap<String, SoundBuffer>();
            this.importantMessage("Buffer Map was null in method 'loadSound'");
        }
        if (this.ALBufferMap == null) {
            this.ALBufferMap = new HashMap<String, IntBuffer>();
            this.importantMessage("Open AL Buffer Map was null in method'loadSound'");
        }
        if (this.errorCheck(identifier == null, "Identifier not specified in method 'loadSound'")) {
            return false;
        }
        if (this.bufferMap.get(identifier) != null) {
            return true;
        }
        if (this.errorCheck(buffer == null, "Sound buffer null in method 'loadSound'")) {
            return false;
        }
        this.bufferMap.put(identifier, buffer);
        final AudioFormat audioFormat = buffer.audioFormat;
        int soundFormat = 0;
        if (audioFormat.getChannels() == 1) {
            if (audioFormat.getSampleSizeInBits() == 8) {
                soundFormat = 4352;
            }
            else {
                if (audioFormat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'loadSound'");
                    return false;
                }
                soundFormat = 4353;
            }
        }
        else {
            if (audioFormat.getChannels() != 2) {
                this.errorMessage("File neither mono nor stereo in method 'loadSound'");
                return false;
            }
            if (audioFormat.getSampleSizeInBits() == 8) {
                soundFormat = 4354;
            }
            else {
                if (audioFormat.getSampleSizeInBits() != 16) {
                    this.errorMessage("Illegal sample size in method 'loadSound'");
                    return false;
                }
                soundFormat = 4355;
            }
        }
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(1);
        AL10.alGenBuffers(intBuffer);
        if (this.errorCheck(AL10.alGetError() != 0, "alGenBuffers error when saving " + identifier)) {
            return false;
        }
        AL10.alBufferData(intBuffer.get(0), soundFormat, (ByteBuffer)BufferUtils.createByteBuffer(buffer.audioData.length).put(buffer.audioData).flip(), (int)audioFormat.getSampleRate());
        if (this.errorCheck(AL10.alGetError() != 0, "alBufferData error when saving " + identifier) && this.errorCheck(intBuffer == null, "Sound buffer was not created for " + identifier)) {
            return false;
        }
        this.ALBufferMap.put(identifier, intBuffer);
        return true;
    }
    
    @Override
    public void unloadSound(final String filename) {
        this.ALBufferMap.remove(filename);
        super.unloadSound(filename);
    }
    
    @Override
    public void setMasterVolume(final float value) {
        super.setMasterVolume(value);
        AL10.alListenerf(4106, value);
        this.checkALError();
    }
    
    @Override
    public void newSource(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        IntBuffer myBuffer = null;
        if (!toStream) {
            myBuffer = this.ALBufferMap.get(filenameURL.getFilename());
            if (myBuffer == null && !this.loadSound(filenameURL)) {
                this.errorMessage("Source '" + sourcename + "' was not created " + "because an error occurred while loading " + filenameURL.getFilename());
                return;
            }
            myBuffer = this.ALBufferMap.get(filenameURL.getFilename());
            if (myBuffer == null) {
                this.errorMessage("Source '" + sourcename + "' was not created " + "because a sound buffer was not found for " + filenameURL.getFilename());
                return;
            }
        }
        SoundBuffer buffer = null;
        if (!toStream) {
            buffer = this.bufferMap.get(filenameURL.getFilename());
            if (buffer == null && !this.loadSound(filenameURL)) {
                this.errorMessage("Source '" + sourcename + "' was not created " + "because an error occurred while loading " + filenameURL.getFilename());
                return;
            }
            buffer = this.bufferMap.get(filenameURL.getFilename());
            if (buffer == null) {
                this.errorMessage("Source '" + sourcename + "' was not created " + "because audio data was not found for " + filenameURL.getFilename());
                return;
            }
        }
        this.sourceMap.put(sourcename, new SourceLWJGLOpenAL(this.listenerPositionAL, myBuffer, priority, toStream, toLoop, sourcename, filenameURL, buffer, x, y, z, attModel, distOrRoll, false));
    }
    
    @Override
    public void rawDataStream(final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        this.sourceMap.put(sourcename, new SourceLWJGLOpenAL(this.listenerPositionAL, audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll));
    }
    
    @Override
    public void quickPlay(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
        IntBuffer myBuffer = null;
        if (!toStream) {
            myBuffer = this.ALBufferMap.get(filenameURL.getFilename());
            if (myBuffer == null) {
                this.loadSound(filenameURL);
            }
            myBuffer = this.ALBufferMap.get(filenameURL.getFilename());
            if (myBuffer == null) {
                this.errorMessage("Sound buffer was not created for " + filenameURL.getFilename());
                return;
            }
        }
        SoundBuffer buffer = null;
        if (!toStream) {
            buffer = this.bufferMap.get(filenameURL.getFilename());
            if (buffer == null && !this.loadSound(filenameURL)) {
                this.errorMessage("Source '" + sourcename + "' was not created " + "because an error occurred while loading " + filenameURL.getFilename());
                return;
            }
            buffer = this.bufferMap.get(filenameURL.getFilename());
            if (buffer == null) {
                this.errorMessage("Source '" + sourcename + "' was not created " + "because audio data was not found for " + filenameURL.getFilename());
                return;
            }
        }
        final SourceLWJGLOpenAL s = new SourceLWJGLOpenAL(this.listenerPositionAL, myBuffer, priority, toStream, toLoop, sourcename, filenameURL, buffer, x, y, z, attModel, distOrRoll, false);
        this.sourceMap.put(sourcename, s);
        this.play(s);
        if (temporary) {
            s.setTemporary(true);
        }
    }
    
    @Override
    public void copySources(final HashMap<String, Source> srcMap) {
        if (srcMap == null) {
            return;
        }
        final Set<String> keys = srcMap.keySet();
        final Iterator<String> iter = keys.iterator();
        if (this.bufferMap == null) {
            this.bufferMap = new HashMap<String, SoundBuffer>();
            this.importantMessage("Buffer Map was null in method 'copySources'");
        }
        if (this.ALBufferMap == null) {
            this.ALBufferMap = new HashMap<String, IntBuffer>();
            this.importantMessage("Open AL Buffer Map was null in method'copySources'");
        }
        this.sourceMap.clear();
        while (iter.hasNext()) {
            final String sourcename = iter.next();
            final Source source = srcMap.get(sourcename);
            if (source != null) {
                SoundBuffer buffer = null;
                if (!source.toStream) {
                    this.loadSound(source.filenameURL);
                    buffer = this.bufferMap.get(source.filenameURL.getFilename());
                }
                if (!source.toStream && buffer == null) {
                    continue;
                }
                this.sourceMap.put(sourcename, new SourceLWJGLOpenAL(this.listenerPositionAL, this.ALBufferMap.get(source.filenameURL.getFilename()), source, buffer));
            }
        }
    }
    
    @Override
    public void setListenerPosition(final float x, final float y, final float z) {
        super.setListenerPosition(x, y, z);
        this.listenerPositionAL.put(0, x);
        this.listenerPositionAL.put(1, y);
        this.listenerPositionAL.put(2, z);
        AL10.alListener(4100, this.listenerPositionAL);
        this.checkALError();
    }
    
    @Override
    public void setListenerAngle(final float angle) {
        super.setListenerAngle(angle);
        this.listenerOrientation.put(0, this.listener.lookAt.x);
        this.listenerOrientation.put(2, this.listener.lookAt.z);
        AL10.alListener(4111, this.listenerOrientation);
        this.checkALError();
    }
    
    @Override
    public void setListenerOrientation(final float lookX, final float lookY, final float lookZ, final float upX, final float upY, final float upZ) {
        super.setListenerOrientation(lookX, lookY, lookZ, upX, upY, upZ);
        this.listenerOrientation.put(0, lookX);
        this.listenerOrientation.put(1, lookY);
        this.listenerOrientation.put(2, lookZ);
        this.listenerOrientation.put(3, upX);
        this.listenerOrientation.put(4, upY);
        this.listenerOrientation.put(5, upZ);
        AL10.alListener(4111, this.listenerOrientation);
        this.checkALError();
    }
    
    @Override
    public void setListenerData(final ListenerData l) {
        super.setListenerData(l);
        this.listenerPositionAL.put(0, l.position.x);
        this.listenerPositionAL.put(1, l.position.y);
        this.listenerPositionAL.put(2, l.position.z);
        AL10.alListener(4100, this.listenerPositionAL);
        this.checkALError();
        this.listenerOrientation.put(0, l.lookAt.x);
        this.listenerOrientation.put(1, l.lookAt.y);
        this.listenerOrientation.put(2, l.lookAt.z);
        this.listenerOrientation.put(3, l.up.x);
        this.listenerOrientation.put(4, l.up.y);
        this.listenerOrientation.put(5, l.up.z);
        AL10.alListener(4111, this.listenerOrientation);
        this.checkALError();
        this.listenerVelocity.put(0, l.velocity.x);
        this.listenerVelocity.put(1, l.velocity.y);
        this.listenerVelocity.put(2, l.velocity.z);
        AL10.alListener(4102, this.listenerVelocity);
        this.checkALError();
    }
    
    @Override
    public void setListenerVelocity(final float x, final float y, final float z) {
        super.setListenerVelocity(x, y, z);
        this.listenerVelocity.put(0, this.listener.velocity.x);
        this.listenerVelocity.put(1, this.listener.velocity.y);
        this.listenerVelocity.put(2, this.listener.velocity.z);
        AL10.alListener(4102, this.listenerVelocity);
    }
    
    @Override
    public void dopplerChanged() {
        super.dopplerChanged();
        AL10.alDopplerFactor(SoundSystemConfig.getDopplerFactor());
        this.checkALError();
        AL10.alDopplerVelocity(SoundSystemConfig.getDopplerVelocity());
        this.checkALError();
    }
    
    private boolean checkALError() {
        switch (AL10.alGetError()) {
            case 0: {
                return false;
            }
            case 40961: {
                this.errorMessage("Invalid name parameter.");
                return true;
            }
            case 40962: {
                this.errorMessage("Invalid parameter.");
                return true;
            }
            case 40963: {
                this.errorMessage("Invalid enumerated parameter value.");
                return true;
            }
            case 40964: {
                this.errorMessage("Illegal call.");
                return true;
            }
            case 40965: {
                this.errorMessage("Unable to allocate memory.");
                return true;
            }
            default: {
                this.errorMessage("An unrecognized error occurred.");
                return true;
            }
        }
    }
    
    public static boolean alPitchSupported() {
        return alPitchSupported(false, false);
    }
    
    private static synchronized boolean alPitchSupported(final boolean action, final boolean value) {
        if (action) {
            LibraryLWJGLOpenAL.alPitchSupported = value;
        }
        return LibraryLWJGLOpenAL.alPitchSupported;
    }
    
    public static String getTitle() {
        return "LWJGL OpenAL";
    }
    
    public static String getDescription() {
        return "The LWJGL binding of OpenAL.  For more information, see http://www.lwjgl.org";
    }
    
    @Override
    public String getClassName() {
        return "LibraryLWJGLOpenAL";
    }
    
    static {
        LibraryLWJGLOpenAL.alPitchSupported = true;
    }
    
    public static class Exception extends SoundSystemException
    {
        public static final int CREATE = 101;
        public static final int INVALID_NAME = 102;
        public static final int INVALID_ENUM = 103;
        public static final int INVALID_VALUE = 104;
        public static final int INVALID_OPERATION = 105;
        public static final int OUT_OF_MEMORY = 106;
        public static final int LISTENER = 107;
        public static final int NO_AL_PITCH = 108;
        
        public Exception(final String message) {
            super(message);
        }
        
        public Exception(final String message, final int type) {
            super(message, type);
        }
    }
}
