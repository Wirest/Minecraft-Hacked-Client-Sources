package me.rigamortis.faurax.events;

import com.darkmagician6.eventapi.events.callables.*;
import com.darkmagician6.eventapi.events.*;

public class EventSendChat extends EventCancellable implements Event
{
    private String msg;
    
    public EventSendChat(final String msg) {
        this.setMsg(msg);
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
}
