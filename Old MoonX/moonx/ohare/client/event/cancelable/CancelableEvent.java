package moonx.ohare.client.event.cancelable;

import moonx.ohare.client.event.Event;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class CancelableEvent extends Event {
    private boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
