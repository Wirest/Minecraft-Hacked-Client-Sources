// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Misc;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.Packet;
import cf.euphoria.euphorical.Utils.NetUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import cf.euphoria.euphorical.Events.EventUpdate;
import com.darkmagician6.eventapi.EventManager;
import cf.euphoria.euphorical.Utils.ChatUtils;
import cf.euphoria.euphorical.Mod.Category;
import cf.euphoria.euphorical.Mod.Mod;

public class Lagger extends Mod
{
    public Lagger() {
        super("Lagger", Category.MISC);
        this.setRenderName(String.format("%s", this.getModName()));
    }
    
    @Override
    public void onEnable() {
        if (this.mc.isSingleplayer()) {
            ChatUtils.sendMessageToPlayer("Multiplayer Only!");
            this.setEnabled(false);
            return;
        }
        EventManager.register(this);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        for (int i = 0; i < 2000; ++i) {
            NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
            NetUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
        }
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
}
