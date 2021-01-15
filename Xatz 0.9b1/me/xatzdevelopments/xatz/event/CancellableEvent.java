package me.xatzdevelopments.xatz.event;

/**
 * @author antja03
 */
public class CancellableEvent {

    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
