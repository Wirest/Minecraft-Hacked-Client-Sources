package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.utils.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.network.play.server.*;
import me.rigamortis.faurax.friends.*;
import net.minecraft.entity.player.*;
import me.rigamortis.faurax.module.modules.world.*;
import me.rigamortis.faurax.*;
import java.util.*;

public class CommandBot extends Module
{
    public int delay;
    private boolean following;
    private String followName;
    
    public CommandBot() {
        this.setName("CMDBot");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (CommandBot.mc.thePlayer.isDead) {
                CommandBot.mc.thePlayer.respawnPlayer();
            }
            if (this.following) {
                try {
                    final PathFind pathFind = new PathFind(this.followName);
                }
                catch (Exception ex) {}
            }
        }
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled()) {
            final S02PacketChat message = (S02PacketChat)e.getPacket();
            if (message.func_148915_c().getFormattedText().contains("-control pos1")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        for (final Object i : CommandBot.mc.theWorld.loadedEntityList) {
                            if (i instanceof EntityPlayer && i != null && ((EntityPlayer)i).getName().equalsIgnoreCase(friend.getName())) {
                                final EntityPlayer player = (EntityPlayer)i;
                                Control.pos1 = player.getPositionVector();
                                final String pos = String.valueOf((int)player.posX) + ", " + (int)player.posY + ", " + (int)player.posZ;
                                CommandBot.mc.thePlayer.sendChatMessage("Control pos1 set. " + pos);
                                System.out.println("/msg " + friend.getName() + " Control pos1 set. " + pos);
                            }
                        }
                        break;
                    }
                }
            }
            if (message.func_148915_c().getFormattedText().contains("-control pos2")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        for (final Object i : CommandBot.mc.theWorld.loadedEntityList) {
                            if (i instanceof EntityPlayer && i != null && ((EntityPlayer)i).getName().equalsIgnoreCase(friend.getName())) {
                                final EntityPlayer player = (EntityPlayer)i;
                                Control.pos2 = player.getPositionVector();
                                final String pos = String.valueOf((int)player.posX) + ", " + (int)player.posY + ", " + (int)player.posZ;
                                CommandBot.mc.thePlayer.sendChatMessage("Control pos2 set. " + pos);
                            }
                        }
                        break;
                    }
                }
            }
            if (message.func_148915_c().getFormattedText().contains("-follow")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        String s = message.func_148915_c().getFormattedText();
                        s = s.substring(s.indexOf("-follow ") + 8);
                        s = s.substring(0, s.indexOf("§"));
                        this.following = true;
                        this.followName = s;
                        CommandBot.mc.thePlayer.sendChatMessage("Now following " + s);
                        break;
                    }
                }
            }
            if (message.func_148915_c().getFormattedText().contains("-stopfollow")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        String s = message.func_148915_c().getFormattedText();
                        s = s.substring(s.indexOf("-stopfollow ") + 12);
                        s = s.substring(0, s.indexOf("§"));
                        this.following = false;
                        this.followName = "";
                        CommandBot.mc.thePlayer.sendChatMessage("No longer following " + s);
                        break;
                    }
                }
            }
            if (message.func_148915_c().getFormattedText().contains("-an hero")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        CommandBot.mc.thePlayer.sendChatMessage("/suicide");
                        break;
                    }
                }
            }
            if (message.func_148915_c().getFormattedText().contains("-tpahere")) {
                for (final Friend friend : FriendManager.friends) {
                    if (message.func_148915_c().getFormattedText().contains(friend.getName())) {
                        CommandBot.mc.thePlayer.sendChatMessage("/tpa " + friend.getName());
                        break;
                    }
                }
            }
            for (final Module mod : Client.getModules().mods) {
                if (message.func_148915_c().getFormattedText().contains("-toggle " + mod.getName())) {
                    for (final Friend friend2 : FriendManager.friends) {
                        if (message.func_148915_c().getFormattedText().contains(friend2.getName())) {
                            mod.toggle();
                            final boolean state = mod.isToggled();
                            final String s2 = state ? "On" : "Off";
                            CommandBot.mc.thePlayer.sendChatMessage("Toggled " + mod.getName() + " " + s2);
                            break;
                        }
                    }
                }
            }
        }
    }
}
