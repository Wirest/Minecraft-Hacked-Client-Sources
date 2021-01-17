// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound.libraries;

import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.util.ListIterator;
import paulscode.sound.SoundSystem;
import javax.sound.sampled.AudioFormat;
import java.util.Iterator;
import java.util.Set;
import paulscode.sound.Source;
import java.net.URL;
import paulscode.sound.ICodec;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundBuffer;
import java.util.HashMap;
import paulscode.sound.FilenameURL;
import paulscode.sound.Channel;
import javax.sound.sampled.AudioSystem;
import paulscode.sound.SoundSystemException;
import javax.sound.sampled.Mixer;
import paulscode.sound.Library;

public class LibraryJavaSound extends Library
{
    private static final boolean GET = false;
    private static final boolean SET = true;
    private static final int XXX = 0;
    private final int maxClipSize = 1048576;
    private static Mixer myMixer;
    private static MixerRanking myMixerRanking;
    private static LibraryJavaSound instance;
    private static int minSampleRate;
    private static int maxSampleRate;
    private static int lineCount;
    private static boolean useGainControl;
    private static boolean usePanControl;
    private static boolean useSampleRateControl;
    
    public LibraryJavaSound() throws SoundSystemException {
        LibraryJavaSound.instance = this;
    }
    
    @Override
    public void init() throws SoundSystemException {
        MixerRanking mixerRanker = null;
        if (LibraryJavaSound.myMixer == null) {
            final Mixer.Info[] arr$ = AudioSystem.getMixerInfo();
            final int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                final Mixer.Info mixerInfo = arr$[i$];
                if (mixerInfo.getName().equals("Java Sound Audio Engine")) {
                    mixerRanker = new MixerRanking();
                    try {
                        mixerRanker.rank(mixerInfo);
                    }
                    catch (Exception ljse) {
                        break;
                    }
                    if (mixerRanker.rank < 14) {
                        break;
                    }
                    LibraryJavaSound.myMixer = AudioSystem.getMixer(mixerInfo);
                    mixerRanking(true, mixerRanker);
                    break;
                }
                else {
                    ++i$;
                }
            }
            if (LibraryJavaSound.myMixer == null) {
                MixerRanking bestRankedMixer = mixerRanker;
                for (final Mixer.Info mixerInfo2 : AudioSystem.getMixerInfo()) {
                    mixerRanker = new MixerRanking();
                    try {
                        mixerRanker.rank(mixerInfo2);
                    }
                    catch (Exception ex) {}
                    if (bestRankedMixer == null || mixerRanker.rank > bestRankedMixer.rank) {
                        bestRankedMixer = mixerRanker;
                    }
                }
                if (bestRankedMixer == null) {
                    throw new Exception("No useable mixers found!", new MixerRanking());
                }
                try {
                    LibraryJavaSound.myMixer = AudioSystem.getMixer(bestRankedMixer.mixerInfo);
                    mixerRanking(true, bestRankedMixer);
                }
                catch (java.lang.Exception e) {
                    throw new Exception("No useable mixers available!", new MixerRanking());
                }
            }
        }
        this.setMasterVolume(1.0f);
        this.message("JavaSound initialized.");
        super.init();
    }
    
    public static boolean libraryCompatible() {
        for (final Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            if (mixerInfo.getName().equals("Java Sound Audio Engine")) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected Channel createChannel(final int type) {
        return new ChannelJavaSound(type, LibraryJavaSound.myMixer);
    }
    
    @Override
    public void cleanup() {
        super.cleanup();
        LibraryJavaSound.instance = null;
        LibraryJavaSound.myMixer = null;
        LibraryJavaSound.myMixerRanking = null;
    }
    
    @Override
    public boolean loadSound(final FilenameURL filenameURL) {
        if (this.bufferMap == null) {
            this.bufferMap = new HashMap<String, SoundBuffer>();
            this.importantMessage("Buffer Map was null in method 'loadSound'");
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
        final URL url = filenameURL.getURL();
        if (this.errorCheck(url == null, "Unable to open file '" + filenameURL.getFilename() + "' in method 'loadSound'")) {
            return false;
        }
        codec.initialize(url);
        final SoundBuffer buffer = codec.readAll();
        codec.cleanup();
        codec = null;
        if (buffer != null) {
            this.bufferMap.put(filenameURL.getFilename(), buffer);
        }
        else {
            this.errorMessage("Sound buffer null in method 'loadSound'");
        }
        return true;
    }
    
    @Override
    public boolean loadSound(final SoundBuffer buffer, final String identifier) {
        if (this.bufferMap == null) {
            this.bufferMap = new HashMap<String, SoundBuffer>();
            this.importantMessage("Buffer Map was null in method 'loadSound'");
        }
        if (this.errorCheck(identifier == null, "Identifier not specified in method 'loadSound'")) {
            return false;
        }
        if (this.bufferMap.get(identifier) != null) {
            return true;
        }
        if (buffer != null) {
            this.bufferMap.put(identifier, buffer);
        }
        else {
            this.errorMessage("Sound buffer null in method 'loadSound'");
        }
        return true;
    }
    
    @Override
    public void setMasterVolume(final float value) {
        super.setMasterVolume(value);
        final Set<String> keys = this.sourceMap.keySet();
        for (final String sourcename : keys) {
            final Source source = this.sourceMap.get(sourcename);
            if (source != null) {
                source.positionChanged();
            }
        }
    }
    
    @Override
    public void newSource(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
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
        if (!toStream && buffer != null) {
            buffer.trimData(1048576);
        }
        this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, priority, toStream, toLoop, sourcename, filenameURL, buffer, x, y, z, attModel, distOrRoll, false));
    }
    
    @Override
    public void rawDataStream(final AudioFormat audioFormat, final boolean priority, final String sourcename, final float x, final float y, final float z, final int attModel, final float distOrRoll) {
        this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, audioFormat, priority, sourcename, x, y, z, attModel, distOrRoll));
    }
    
    @Override
    public void quickPlay(final boolean priority, final boolean toStream, final boolean toLoop, final String sourcename, final FilenameURL filenameURL, final float x, final float y, final float z, final int attModel, final float distOrRoll, final boolean temporary) {
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
        if (!toStream && buffer != null) {
            buffer.trimData(1048576);
        }
        this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, priority, toStream, toLoop, sourcename, filenameURL, buffer, x, y, z, attModel, distOrRoll, temporary));
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
                if (!source.toStream && buffer != null) {
                    buffer.trimData(1048576);
                }
                if (!source.toStream && buffer == null) {
                    continue;
                }
                this.sourceMap.put(sourcename, new SourceJavaSound(this.listener, source, buffer));
            }
        }
    }
    
    @Override
    public void setListenerVelocity(final float x, final float y, final float z) {
        super.setListenerVelocity(x, y, z);
        this.listenerMoved();
    }
    
    @Override
    public void dopplerChanged() {
        super.dopplerChanged();
        this.listenerMoved();
    }
    
    public static Mixer getMixer() {
        return mixer(false, null);
    }
    
    public static void setMixer(final Mixer m) throws SoundSystemException {
        mixer(true, m);
        final SoundSystemException e = SoundSystem.getLastException();
        SoundSystem.setException(null);
        if (e != null) {
            throw e;
        }
    }
    
    private static synchronized Mixer mixer(final boolean action, final Mixer m) {
        if (action) {
            if (m == null) {
                return LibraryJavaSound.myMixer;
            }
            final MixerRanking mixerRanker = new MixerRanking();
            try {
                mixerRanker.rank(m.getMixerInfo());
            }
            catch (Exception ljse) {
                SoundSystemConfig.getLogger().printStackTrace(ljse, 1);
                SoundSystem.setException(ljse);
            }
            LibraryJavaSound.myMixer = m;
            mixerRanking(true, mixerRanker);
            if (LibraryJavaSound.instance != null) {
                ListIterator<Channel> itr = LibraryJavaSound.instance.normalChannels.listIterator();
                SoundSystem.setException(null);
                while (itr.hasNext()) {
                    final ChannelJavaSound c = itr.next();
                    c.newMixer(m);
                }
                itr = LibraryJavaSound.instance.streamingChannels.listIterator();
                while (itr.hasNext()) {
                    final ChannelJavaSound c = itr.next();
                    c.newMixer(m);
                }
            }
        }
        return LibraryJavaSound.myMixer;
    }
    
    public static MixerRanking getMixerRanking() {
        return mixerRanking(false, null);
    }
    
    private static synchronized MixerRanking mixerRanking(final boolean action, final MixerRanking value) {
        if (action) {
            LibraryJavaSound.myMixerRanking = value;
        }
        return LibraryJavaSound.myMixerRanking;
    }
    
    public static void setMinSampleRate(final int value) {
        minSampleRate(true, value);
    }
    
    private static synchronized int minSampleRate(final boolean action, final int value) {
        if (action) {
            LibraryJavaSound.minSampleRate = value;
        }
        return LibraryJavaSound.minSampleRate;
    }
    
    public static void setMaxSampleRate(final int value) {
        maxSampleRate(true, value);
    }
    
    private static synchronized int maxSampleRate(final boolean action, final int value) {
        if (action) {
            LibraryJavaSound.maxSampleRate = value;
        }
        return LibraryJavaSound.maxSampleRate;
    }
    
    public static void setLineCount(final int value) {
        lineCount(true, value);
    }
    
    private static synchronized int lineCount(final boolean action, final int value) {
        if (action) {
            LibraryJavaSound.lineCount = value;
        }
        return LibraryJavaSound.lineCount;
    }
    
    public static void useGainControl(final boolean value) {
        useGainControl(true, value);
    }
    
    private static synchronized boolean useGainControl(final boolean action, final boolean value) {
        if (action) {
            LibraryJavaSound.useGainControl = value;
        }
        return LibraryJavaSound.useGainControl;
    }
    
    public static void usePanControl(final boolean value) {
        usePanControl(true, value);
    }
    
    private static synchronized boolean usePanControl(final boolean action, final boolean value) {
        if (action) {
            LibraryJavaSound.usePanControl = value;
        }
        return LibraryJavaSound.usePanControl;
    }
    
    public static void useSampleRateControl(final boolean value) {
        useSampleRateControl(true, value);
    }
    
    private static synchronized boolean useSampleRateControl(final boolean action, final boolean value) {
        if (action) {
            LibraryJavaSound.useSampleRateControl = value;
        }
        return LibraryJavaSound.useSampleRateControl;
    }
    
    public static String getTitle() {
        return "Java Sound";
    }
    
    public static String getDescription() {
        return "The Java Sound API.  For more information, see http://java.sun.com/products/java-media/sound/";
    }
    
    @Override
    public String getClassName() {
        return "LibraryJavaSound";
    }
    
    static {
        LibraryJavaSound.myMixer = null;
        LibraryJavaSound.myMixerRanking = null;
        LibraryJavaSound.instance = null;
        LibraryJavaSound.minSampleRate = 4000;
        LibraryJavaSound.maxSampleRate = 48000;
        LibraryJavaSound.lineCount = 32;
        LibraryJavaSound.useGainControl = true;
        LibraryJavaSound.usePanControl = true;
        LibraryJavaSound.useSampleRateControl = true;
    }
    
    public static class MixerRanking
    {
        public static final int HIGH = 1;
        public static final int MEDIUM = 2;
        public static final int LOW = 3;
        public static final int NONE = 4;
        public static int MIXER_EXISTS_PRIORITY;
        public static int MIN_SAMPLE_RATE_PRIORITY;
        public static int MAX_SAMPLE_RATE_PRIORITY;
        public static int LINE_COUNT_PRIORITY;
        public static int GAIN_CONTROL_PRIORITY;
        public static int PAN_CONTROL_PRIORITY;
        public static int SAMPLE_RATE_CONTROL_PRIORITY;
        public Mixer.Info mixerInfo;
        public int rank;
        public boolean mixerExists;
        public boolean minSampleRateOK;
        public boolean maxSampleRateOK;
        public boolean lineCountOK;
        public boolean gainControlOK;
        public boolean panControlOK;
        public boolean sampleRateControlOK;
        public int minSampleRatePossible;
        public int maxSampleRatePossible;
        public int maxLinesPossible;
        
        public MixerRanking() {
            this.mixerInfo = null;
            this.rank = 0;
            this.mixerExists = false;
            this.minSampleRateOK = false;
            this.maxSampleRateOK = false;
            this.lineCountOK = false;
            this.gainControlOK = false;
            this.panControlOK = false;
            this.sampleRateControlOK = false;
            this.minSampleRatePossible = -1;
            this.maxSampleRatePossible = -1;
            this.maxLinesPossible = 0;
        }
        
        public MixerRanking(final Mixer.Info i, final int r, final boolean e, final boolean mnsr, final boolean mxsr, final boolean lc, final boolean gc, final boolean pc, final boolean src) {
            this.mixerInfo = null;
            this.rank = 0;
            this.mixerExists = false;
            this.minSampleRateOK = false;
            this.maxSampleRateOK = false;
            this.lineCountOK = false;
            this.gainControlOK = false;
            this.panControlOK = false;
            this.sampleRateControlOK = false;
            this.minSampleRatePossible = -1;
            this.maxSampleRatePossible = -1;
            this.maxLinesPossible = 0;
            this.mixerInfo = i;
            this.rank = r;
            this.mixerExists = e;
            this.minSampleRateOK = mnsr;
            this.maxSampleRateOK = mxsr;
            this.lineCountOK = lc;
            this.gainControlOK = gc;
            this.panControlOK = pc;
            this.sampleRateControlOK = src;
        }
        
        public void rank(final Mixer.Info i) throws Exception {
            if (i == null) {
                throw new Exception("No Mixer info specified in method 'MixerRanking.rank'", this);
            }
            this.mixerInfo = i;
            Mixer m;
            try {
                m = AudioSystem.getMixer(this.mixerInfo);
            }
            catch (java.lang.Exception e) {
                throw new Exception("Unable to acquire the specified Mixer in method 'MixerRanking.rank'", this);
            }
            if (m == null) {
                throw new Exception("Unable to acquire the specified Mixer in method 'MixerRanking.rank'", this);
            }
            this.mixerExists = true;
            DataLine.Info lineInfo;
            try {
                final AudioFormat format = new AudioFormat((float)minSampleRate(false, 0), 16, 2, true, false);
                lineInfo = new DataLine.Info(SourceDataLine.class, format);
            }
            catch (java.lang.Exception e2) {
                throw new Exception("Invalid minimum sample-rate specified in method 'MixerRanking.rank'", this);
            }
            if (!AudioSystem.isLineSupported(lineInfo)) {
                if (MixerRanking.MIN_SAMPLE_RATE_PRIORITY == 1) {
                    throw new Exception("Specified minimum sample-rate not possible for Mixer '" + this.mixerInfo.getName() + "'", this);
                }
            }
            else {
                this.minSampleRateOK = true;
            }
            try {
                final AudioFormat format = new AudioFormat((float)maxSampleRate(false, 0), 16, 2, true, false);
                lineInfo = new DataLine.Info(SourceDataLine.class, format);
            }
            catch (java.lang.Exception e2) {
                throw new Exception("Invalid maximum sample-rate specified in method 'MixerRanking.rank'", this);
            }
            if (!AudioSystem.isLineSupported(lineInfo)) {
                if (MixerRanking.MAX_SAMPLE_RATE_PRIORITY == 1) {
                    throw new Exception("Specified maximum sample-rate not possible for Mixer '" + this.mixerInfo.getName() + "'", this);
                }
            }
            else {
                this.maxSampleRateOK = true;
            }
            if (this.minSampleRateOK) {
                this.minSampleRatePossible = minSampleRate(false, 0);
            }
            else {
                int lL = minSampleRate(false, 0);
                int uL = maxSampleRate(false, 0);
                while (uL - lL > 1) {
                    final int testSampleRate = lL + (uL - lL) / 2;
                    final AudioFormat format = new AudioFormat((float)testSampleRate, 16, 2, true, false);
                    lineInfo = new DataLine.Info(SourceDataLine.class, format);
                    if (AudioSystem.isLineSupported(lineInfo)) {
                        this.minSampleRatePossible = testSampleRate;
                        uL = testSampleRate;
                    }
                    else {
                        lL = testSampleRate;
                    }
                }
            }
            if (this.maxSampleRateOK) {
                this.maxSampleRatePossible = maxSampleRate(false, 0);
            }
            else if (this.minSampleRatePossible != -1) {
                int uL = maxSampleRate(false, 0);
                int lL = this.minSampleRatePossible;
                while (uL - lL > 1) {
                    final int testSampleRate = lL + (uL - lL) / 2;
                    final AudioFormat format = new AudioFormat((float)testSampleRate, 16, 2, true, false);
                    lineInfo = new DataLine.Info(SourceDataLine.class, format);
                    if (AudioSystem.isLineSupported(lineInfo)) {
                        this.maxSampleRatePossible = testSampleRate;
                        lL = testSampleRate;
                    }
                    else {
                        uL = testSampleRate;
                    }
                }
            }
            if (this.minSampleRatePossible == -1 || this.maxSampleRatePossible == -1) {
                throw new Exception("No possible sample-rate found for Mixer '" + this.mixerInfo.getName() + "'", this);
            }
            AudioFormat format = new AudioFormat((float)this.minSampleRatePossible, 16, 2, true, false);
            Clip clip = null;
            try {
                final DataLine.Info clipLineInfo = new DataLine.Info(Clip.class, format);
                clip = (Clip)m.getLine(clipLineInfo);
                final byte[] buffer = new byte[10];
                clip.open(format, buffer, 0, buffer.length);
            }
            catch (java.lang.Exception e3) {
                throw new Exception("Unable to attach an actual audio buffer to an actual Clip... Mixer '" + this.mixerInfo.getName() + "' is unuseable.", this);
            }
            this.maxLinesPossible = 1;
            lineInfo = new DataLine.Info(SourceDataLine.class, format);
            SourceDataLine[] lines = new SourceDataLine[lineCount(false, 0) - 1];
            final int c = 0;
            for (int x = 1; x < lines.length + 1; ++x) {
                try {
                    lines[x - 1] = (SourceDataLine)m.getLine(lineInfo);
                }
                catch (java.lang.Exception e4) {
                    if (x == 0) {
                        throw new Exception("No output lines possible for Mixer '" + this.mixerInfo.getName() + "'", this);
                    }
                    if (MixerRanking.LINE_COUNT_PRIORITY == 1) {
                        throw new Exception("Specified maximum number of lines not possible for Mixer '" + this.mixerInfo.getName() + "'", this);
                    }
                    break;
                }
                this.maxLinesPossible = x + 1;
            }
            try {
                clip.close();
            }
            catch (java.lang.Exception ex) {}
            clip = null;
            if (this.maxLinesPossible == lineCount(false, 0)) {
                this.lineCountOK = true;
            }
            if (!useGainControl(false, false)) {
                MixerRanking.GAIN_CONTROL_PRIORITY = 4;
            }
            else if (!lines[0].isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                if (MixerRanking.GAIN_CONTROL_PRIORITY == 1) {
                    throw new Exception("Gain control not available for Mixer '" + this.mixerInfo.getName() + "'", this);
                }
            }
            else {
                this.gainControlOK = true;
            }
            if (!usePanControl(false, false)) {
                MixerRanking.PAN_CONTROL_PRIORITY = 4;
            }
            else if (!lines[0].isControlSupported(FloatControl.Type.PAN)) {
                if (MixerRanking.PAN_CONTROL_PRIORITY == 1) {
                    throw new Exception("Pan control not available for Mixer '" + this.mixerInfo.getName() + "'", this);
                }
            }
            else {
                this.panControlOK = true;
            }
            if (!useSampleRateControl(false, false)) {
                MixerRanking.SAMPLE_RATE_CONTROL_PRIORITY = 4;
            }
            else if (!lines[0].isControlSupported(FloatControl.Type.SAMPLE_RATE)) {
                if (MixerRanking.SAMPLE_RATE_CONTROL_PRIORITY == 1) {
                    throw new Exception("Sample-rate control not available for Mixer '" + this.mixerInfo.getName() + "'", this);
                }
            }
            else {
                this.sampleRateControlOK = true;
            }
            this.rank += this.getRankValue(this.mixerExists, MixerRanking.MIXER_EXISTS_PRIORITY);
            this.rank += this.getRankValue(this.minSampleRateOK, MixerRanking.MIN_SAMPLE_RATE_PRIORITY);
            this.rank += this.getRankValue(this.maxSampleRateOK, MixerRanking.MAX_SAMPLE_RATE_PRIORITY);
            this.rank += this.getRankValue(this.lineCountOK, MixerRanking.LINE_COUNT_PRIORITY);
            this.rank += this.getRankValue(this.gainControlOK, MixerRanking.GAIN_CONTROL_PRIORITY);
            this.rank += this.getRankValue(this.panControlOK, MixerRanking.PAN_CONTROL_PRIORITY);
            this.rank += this.getRankValue(this.sampleRateControlOK, MixerRanking.SAMPLE_RATE_CONTROL_PRIORITY);
            m = null;
            format = null;
            lineInfo = null;
            lines = null;
        }
        
        private int getRankValue(final boolean property, final int priority) {
            if (property) {
                return 2;
            }
            if (priority == 4) {
                return 2;
            }
            if (priority == 3) {
                return 1;
            }
            return 0;
        }
        
        static {
            MixerRanking.MIXER_EXISTS_PRIORITY = 1;
            MixerRanking.MIN_SAMPLE_RATE_PRIORITY = 1;
            MixerRanking.MAX_SAMPLE_RATE_PRIORITY = 1;
            MixerRanking.LINE_COUNT_PRIORITY = 1;
            MixerRanking.GAIN_CONTROL_PRIORITY = 2;
            MixerRanking.PAN_CONTROL_PRIORITY = 2;
            MixerRanking.SAMPLE_RATE_CONTROL_PRIORITY = 3;
        }
    }
    
    public static class Exception extends SoundSystemException
    {
        public static final int MIXER_PROBLEM = 101;
        public static MixerRanking mixerRanking;
        
        public Exception(final String message) {
            super(message);
        }
        
        public Exception(final String message, final int type) {
            super(message, type);
        }
        
        public Exception(final String message, final MixerRanking rank) {
            super(message, 101);
            Exception.mixerRanking = rank;
        }
        
        static {
            Exception.mixerRanking = null;
        }
    }
}
