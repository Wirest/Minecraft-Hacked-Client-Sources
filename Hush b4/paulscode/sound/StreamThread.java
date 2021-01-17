// 
// Decompiled by Procyon v0.5.36
// 

package paulscode.sound;

import java.util.ListIterator;
import java.util.LinkedList;
import java.util.List;

public class StreamThread extends SimpleThread
{
    private SoundSystemLogger logger;
    private List<Source> streamingSources;
    private final Object listLock;
    
    public StreamThread() {
        this.listLock = new Object();
        this.logger = SoundSystemConfig.getLogger();
        this.streamingSources = new LinkedList<Source>();
    }
    
    @Override
    protected void cleanup() {
        this.kill();
        super.cleanup();
    }
    
    @Override
    public void run() {
        this.snooze(3600000L);
        while (!this.dying()) {
            while (!this.dying() && !this.streamingSources.isEmpty()) {
                synchronized (this.listLock) {
                    final ListIterator<Source> iter = this.streamingSources.listIterator();
                    while (!this.dying() && iter.hasNext()) {
                        final Source src = iter.next();
                        if (src == null) {
                            iter.remove();
                        }
                        else if (src.stopped()) {
                            if (src.rawDataStream) {
                                continue;
                            }
                            iter.remove();
                        }
                        else if (!src.active()) {
                            if (src.toLoop || src.rawDataStream) {
                                src.toPlay = true;
                            }
                            iter.remove();
                        }
                        else {
                            if (src.paused()) {
                                continue;
                            }
                            src.checkFadeOut();
                            if (src.stream() || src.rawDataStream || (src.channel != null && src.channel.processBuffer())) {
                                continue;
                            }
                            if (src.nextCodec == null) {
                                src.readBuffersFromNextSoundInSequence();
                            }
                            if (src.toLoop) {
                                if (src.playing()) {
                                    continue;
                                }
                                SoundSystemConfig.notifyEOS(src.sourcename, src.getSoundSequenceQueueSize());
                                if (src.checkFadeOut()) {
                                    src.preLoad = true;
                                }
                                else {
                                    src.incrementSoundSequence();
                                    src.preLoad = true;
                                }
                            }
                            else {
                                if (src.playing()) {
                                    continue;
                                }
                                SoundSystemConfig.notifyEOS(src.sourcename, src.getSoundSequenceQueueSize());
                                if (src.checkFadeOut()) {
                                    continue;
                                }
                                if (src.incrementSoundSequence()) {
                                    src.preLoad = true;
                                }
                                else {
                                    iter.remove();
                                }
                            }
                        }
                    }
                }
                if (!this.dying() && !this.streamingSources.isEmpty()) {
                    this.snooze(20L);
                }
            }
            if (!this.dying() && this.streamingSources.isEmpty()) {
                this.snooze(3600000L);
            }
        }
        this.cleanup();
    }
    
    public void watch(final Source source) {
        if (source == null) {
            return;
        }
        if (this.streamingSources.contains(source)) {
            return;
        }
        synchronized (this.listLock) {
            final ListIterator<Source> iter = this.streamingSources.listIterator();
            while (iter.hasNext()) {
                final Source src = iter.next();
                if (src == null) {
                    iter.remove();
                }
                else {
                    if (source.channel != src.channel) {
                        continue;
                    }
                    src.stop();
                    iter.remove();
                }
            }
            this.streamingSources.add(source);
        }
    }
    
    private void message(final String message) {
        this.logger.message(message, 0);
    }
    
    private void importantMessage(final String message) {
        this.logger.importantMessage(message, 0);
    }
    
    private boolean errorCheck(final boolean error, final String message) {
        return this.logger.errorCheck(error, "StreamThread", message, 0);
    }
    
    private void errorMessage(final String message) {
        this.logger.errorMessage("StreamThread", message, 0);
    }
}
