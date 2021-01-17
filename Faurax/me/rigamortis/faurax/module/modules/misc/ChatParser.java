package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import com.darkmagician6.eventapi.*;

public class ChatParser extends Module implements PlayerHelper
{
    public ChatParser() {
        this.setName("parse");
        this.setKey("HOME");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled() && e.getPacket() instanceof S02PacketChat) {
            final S02PacketChat chat = (S02PacketChat)e.getPacket();
            if (chat.func_148915_c().getFormattedText().contains("Home: ")) {
                final String split = chat.func_148915_c().getFormattedText();
                final String[] splitted = split.split("Home: ");
                final int length = splitted[1].length();
                String result = "";
                for (int i = 0; i < length; ++i) {
                    final Character character = splitted[1].charAt(i);
                    if (Character.isDigit(character)) {
                        result = String.valueOf(result) + character;
                    }
                }
                System.out.println("LOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOLOL" + chat.func_148915_c().getFormattedText().replaceAll("Home|", "".replaceAll(" ", "")));
            }
        }
    }
}
