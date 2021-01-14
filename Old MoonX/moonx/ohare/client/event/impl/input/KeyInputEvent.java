package moonx.ohare.client.event.impl.input;

import moonx.ohare.client.event.Event;

/**
 * made by oHare for eclipse
 *
 * @since 8/27/2019
 **/
public class KeyInputEvent extends Event {
    private int key;

    public KeyInputEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
