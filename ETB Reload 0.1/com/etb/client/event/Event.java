package com.etb.client.event;

import org.greenrobot.eventbus.EventBus;


public class Event {

    public void dispatch() {
        if (EventBus.getDefault().hasSubscriberForEvent(getClass())) {
            EventBus.getDefault().post(this);
        }
    }
}
