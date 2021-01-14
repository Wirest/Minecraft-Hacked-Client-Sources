package info.sigmaclient.event;

public abstract class Event {
    protected boolean cancelled;

    public void fire() {
        cancelled = false;
        EventSystem.fire(this);
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
