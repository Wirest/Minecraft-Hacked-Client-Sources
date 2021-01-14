package paulscode.sound;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class StreamThread
        extends SimpleThread {
    private final Object listLock = new Object();
    private SoundSystemLogger logger = SoundSystemConfig.getLogger();
    private List<Source> streamingSources = new LinkedList();

    protected void cleanup() {
        kill();
        super.cleanup();
    }

    public void run() {
        snooze(3600000L);
        while (!dying()) {
            while ((!dying()) && (!this.streamingSources.isEmpty())) {
                synchronized (this.listLock) {
                    ListIterator localListIterator = this.streamingSources.listIterator();
                    while ((!dying()) && (localListIterator.hasNext())) {
                        Source localSource = (Source) localListIterator.next();
                        if (localSource == null) {
                            localListIterator.remove();
                        } else if (localSource.stopped()) {
                            if (!localSource.rawDataStream) {
                                localListIterator.remove();
                            }
                        } else if (!localSource.active()) {
                            if ((localSource.toLoop) || (localSource.rawDataStream)) {
                                localSource.toPlay = true;
                            }
                            localListIterator.remove();
                        } else if (!localSource.paused()) {
                            localSource.checkFadeOut();
                            if ((!localSource.stream()) && (!localSource.rawDataStream) && ((localSource.channel == null) || (!localSource.channel.processBuffer()))) {
                                if (localSource.nextCodec == null) {
                                    localSource.readBuffersFromNextSoundInSequence();
                                }
                                if (localSource.toLoop) {
                                    if (!localSource.playing()) {
                                        SoundSystemConfig.notifyEOS(localSource.sourcename, localSource.getSoundSequenceQueueSize());
                                        if (localSource.checkFadeOut()) {
                                            localSource.preLoad = true;
                                        } else {
                                            localSource.incrementSoundSequence();
                                            localSource.preLoad = true;
                                        }
                                    }
                                } else if (!localSource.playing()) {
                                    SoundSystemConfig.notifyEOS(localSource.sourcename, localSource.getSoundSequenceQueueSize());
                                    if (!localSource.checkFadeOut()) {
                                        if (localSource.incrementSoundSequence()) {
                                            localSource.preLoad = true;
                                        } else {
                                            localListIterator.remove();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if ((!dying()) && (!this.streamingSources.isEmpty())) {
                    snooze(20L);
                }
            }
            if ((!dying()) && (this.streamingSources.isEmpty())) {
                snooze(3600000L);
            }
        }
        cleanup();
    }

    public void watch(Source paramSource) {
        if (paramSource == null) {
            return;
        }
        if (this.streamingSources.contains(paramSource)) {
            return;
        }
        synchronized (this.listLock) {
            ListIterator localListIterator = this.streamingSources.listIterator();
            while (localListIterator.hasNext()) {
                Source localSource = (Source) localListIterator.next();
                if (localSource == null) {
                    localListIterator.remove();
                } else if (paramSource.channel == localSource.channel) {
                    localSource.stop();
                    localListIterator.remove();
                }
            }
            this.streamingSources.add(paramSource);
        }
    }

    private void message(String paramString) {
        this.logger.message(paramString, 0);
    }

    private void importantMessage(String paramString) {
        this.logger.importantMessage(paramString, 0);
    }

    private boolean errorCheck(boolean paramBoolean, String paramString) {
        return this.logger.errorCheck(paramBoolean, "StreamThread", paramString, 0);
    }

    private void errorMessage(String paramString) {
        this.logger.errorMessage("StreamThread", paramString, 0);
    }
}




