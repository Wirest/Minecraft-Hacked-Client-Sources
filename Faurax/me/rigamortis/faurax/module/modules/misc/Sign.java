package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import com.darkmagician6.eventapi.*;

public class Sign extends Module
{
    public Sign() {
        this.setKey("");
        this.setName("Sign");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isToggled() && e.getPacket() instanceof C12PacketUpdateSign) {
            final C12PacketUpdateSign payload = (C12PacketUpdateSign)e.getPacket();
            final StringBuilder sb = new StringBuilder();
            final StringBuilder sb2 = new StringBuilder();
            final StringBuilder sb3 = new StringBuilder();
            final StringBuilder sb4 = new StringBuilder();
            sb.append("§1[Gamemode]");
            sb2.append("1");
            sb3.append("");
            sb4.append("");
            final IChatComponent[] text = { new ChatComponentText(sb.toString()), new ChatComponentText(sb2.toString()), new ChatComponentText(sb3.toString()), new ChatComponentText(sb4.toString()) };
            e.setPacket(new C12PacketUpdateSign(payload.func_179722_a(), text));
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
