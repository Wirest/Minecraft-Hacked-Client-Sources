package moonx.ohare.client.event.impl.player;

import moonx.ohare.client.event.cancelable.CancelableEvent;

public class PushEvent extends CancelableEvent {
   private boolean pre;
    public PushEvent(boolean pre) {
        this.pre = pre;
    }
    public boolean isPre() {
        return pre;
    }
}
