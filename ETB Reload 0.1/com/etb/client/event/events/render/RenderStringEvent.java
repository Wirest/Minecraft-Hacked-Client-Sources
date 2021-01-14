package com.etb.client.event.events.render;

import com.etb.client.event.Event;

public class RenderStringEvent extends Event {
    private String text;

    public RenderStringEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
