// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;

public class RingBufferLogEventHandler implements SequenceReportingEventHandler<RingBufferLogEvent>
{
    private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
    private Sequence sequenceCallback;
    private int counter;
    
    public void setSequenceCallback(final Sequence sequenceCallback) {
        this.sequenceCallback = sequenceCallback;
    }
    
    public void onEvent(final RingBufferLogEvent event, final long sequence, final boolean endOfBatch) throws Exception {
        event.execute(endOfBatch);
        event.clear();
        if (++this.counter > 50) {
            this.sequenceCallback.set(sequence);
            this.counter = 0;
        }
    }
}
