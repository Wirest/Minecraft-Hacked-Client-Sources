package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class IdiotSpeek extends Module
{
    public IdiotSpeek() {
        this.setName("IdiotSpeak");
        this.setKey("HOME");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget(0)
    public void sendChat(final EventSendChat e) {
        if (this.isToggled()) {
            if (e.getMsg().startsWith("/") || e.getMsg().startsWith(".")) {
                return;
            }
            final StringBuilder sb = new StringBuilder();
            char[] charArray;
            for (int length = (charArray = e.getMsg().toCharArray()).length, i = 0; i < length; ++i) {
                final char c = charArray[i];
                sb.append(c).append(" ");
            }
            final String newMsg = sb.toString().toUpperCase();
            e.setMsg(newMsg);
        }
    }
}
