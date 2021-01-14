package cheatware.module.combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import cheatware.Cheatware;
import cheatware.event.EventTarget;
import cheatware.event.events.EventReceivePacket;
import cheatware.event.events.EventUpdate;
import cheatware.module.Category;
import cheatware.module.Module;
import cheatware.utils.TimerUtil;
import de.Hero.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;

public class AntiBot extends Module {
	
    private int groundTicks;

	public AntiBot() {
        super("AntiBot", Keyboard.KEY_M, Category.COMBAT);
    }
    
    static ArrayList<EntityPlayer> bots;
    
    static {
        AntiBot.bots = new ArrayList<EntityPlayer>();
    }
    
    TimerUtil timer = new TimerUtil();

    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        options.add("Advanced");
        options.add("Mineplex");
        Cheatware.instance.settingsManager.rSetting(new Setting("AntiBot Mode", this, "Advanced", options));
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String mode = Cheatware.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();

        if(mode.equalsIgnoreCase("Advanced") && event.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)event.getPacket();
            double posX = packet.getX() / 32D;
            double posY = packet.getY() / 32D;
            double posZ = packet.getZ() / 32D;

            double diffX = mc.thePlayer.posX - posX;
            double diffY = mc.thePlayer.posY - posY;
            double diffZ = mc.thePlayer.posZ - posZ;

            double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

            if(dist <= 17D && posX != mc.thePlayer.posX && posY != mc.thePlayer.posY && posZ != mc.thePlayer.posZ)
                event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Cheatware.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();
        this.setDisplayName("AntiBot \u00A77" + mode);

        if(mode.equalsIgnoreCase("Watchdog")) {
            for (Object entity : mc.theWorld.loadedEntityList)
                if (((Entity) entity).isInvisible() && entity != mc.thePlayer)
                    mc.theWorld.removeEntity((Entity) entity);
        } else if(mode.equalsIgnoreCase("Mineplex")) {
        	 for (final Entity e : Minecraft.theWorld.loadedEntityList) {
                 if (e instanceof EntityPlayer) {
                     final EntityPlayer bot = (EntityPlayer)e;
                 	if(bot.onGround && bot.ticksExisted > 2) {
                 		bots.remove(bot);
                 	} else {
                 		bots.add(bot);
                 	}
                 }
             }
        }
    }
}
