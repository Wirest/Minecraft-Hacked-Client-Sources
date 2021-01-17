package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import com.darkmagician6.eventapi.*;

public class LEETSpeak extends Module
{
    public LEETSpeak() {
        this.setName("1337Speak");
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
            final String newMsg = e.getMsg().replaceAll("A", "4").replaceAll("a", "4").replaceAll("E", "3").replaceAll("e", "3").replaceAll("I", "!").replaceAll("i", "!").replaceAll("O", "0").replaceAll("o", "0").replaceAll("S", "5").replaceAll("s", "5");
            e.setMsg(newMsg);
        }
    }
}
