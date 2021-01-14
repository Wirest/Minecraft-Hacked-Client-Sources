
package me.memewaredevs.client.event;

public abstract class Event {
    private boolean cancelled;
    protected boolean pre = true;

    public void cancel() {
        this.cancelled = true;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }
}
