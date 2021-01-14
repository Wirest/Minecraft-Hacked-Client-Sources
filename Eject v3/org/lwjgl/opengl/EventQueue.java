package org.lwjgl.opengl;

import java.nio.ByteBuffer;

class EventQueue {
    private static final int QUEUE_SIZE = 200;
    private final int event_size;
    private final ByteBuffer queue;

    protected EventQueue(int paramInt) {
        this.event_size = paramInt;
        this.queue = ByteBuffer.allocate(200 * paramInt);
    }

    protected synchronized void clearEvents() {
        this.queue.clear();
    }

    public synchronized void copyEvents(ByteBuffer paramByteBuffer) {
        this.queue.flip();
        int i = this.queue.limit();
        if (paramByteBuffer.remaining() < this.queue.remaining()) {
            this.queue.limit(paramByteBuffer.remaining() | this.queue.position());
        }
        paramByteBuffer.put(this.queue);
        this.queue.limit(i);
        this.queue.compact();
    }

    public synchronized boolean putEvent(ByteBuffer paramByteBuffer) {
        if (paramByteBuffer.remaining() != this.event_size) {
            throw new IllegalArgumentException("Internal error: event size " + this.event_size + " does not equal the given event size " + paramByteBuffer.remaining());
        }
        if (this.queue.remaining() >= paramByteBuffer.remaining()) {
            this.queue.put(paramByteBuffer);
            return true;
        }
        return false;
    }
}




