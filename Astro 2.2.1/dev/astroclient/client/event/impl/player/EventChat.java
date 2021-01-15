package dev.astroclient.client.event.impl.player;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventCancellable;

/**
 * @author Xen for Astro
 * @since 11/10/2019
 **/
public class EventChat extends EventCancellable {
    private String message;

    public EventChat(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
