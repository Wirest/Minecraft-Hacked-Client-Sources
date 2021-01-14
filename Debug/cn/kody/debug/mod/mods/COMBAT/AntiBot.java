package cn.kody.debug.mod.mods.COMBAT;

import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.utils.PlayerUtil;
import cn.kody.debug.value.Value;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IChatComponent;

public class AntiBot
extends Mod {
    public static Value<String> mode = new Value("AntiBot", "Mode", 0);

    public AntiBot() {
        super("AntiBot", "Anti Bot", Category.COMBAT);
        mode.addValue("Hypixel");
        mode.addValue("MinePlex");
    }

    public List<EntityPlayer> getPlayerList() {
        Minecraft.getMinecraft();
        Collection<NetworkPlayerInfo> playerInfoMap = Minecraft.thePlayer.sendQueue.getPlayerInfoMap();
        ArrayList<EntityPlayer> list = new ArrayList<EntityPlayer>();
        for (NetworkPlayerInfo networkPlayerInfo : playerInfoMap) {
            list.add(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(networkPlayerInfo.getGameProfile().getName()));
        }
        return list;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mode.isCurrentMode("MinePlex")) {
            this.setDisplayName("Mineplex");
            for (Entity var3 : this.mc.theWorld.loadedEntityList) {
                if (!(var3 instanceof EntityPlayer) || Minecraft.thePlayer.getDistanceToEntity(var3) > 5.0f || Float.isNaN(((EntityPlayer)var3).getHealth()) || var3 == Minecraft.thePlayer || (double)((EntityPlayer)var3).getHealth() >= 20.0 || (double)((EntityPlayer)var3).getHealth() == 1.0) continue;
                this.mc.theWorld.removeEntity(var3);
                PlayerUtil.tellPlayer("\u00a7d[Debug Client]\u00a7fRemoved a bot: " + var3.getName() + " Health: " + ((EntityPlayer)var3).getHealth());
            }
        }
        if (mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            for (Object list : this.mc.theWorld.playerEntities) {
                EntityPlayer entity = (EntityPlayer)list;
                if (list == Minecraft.thePlayer || entity.isDead || !entity.isInvisible() || !this.getPlayerList().contains(entity) || entity.getCustomNameTag().length() < 2) continue;
                for (PotionEffect potionEffect : entity.getActivePotionEffects()) {
                }
                this.mc.theWorld.removeEntity(entity);
            }
        }
    }

    public static boolean isInTablist(EntityPlayer player) {
        if (Minecraft.getMinecraft().isSingleplayer()) {
            return true;
        }
        for (NetworkPlayerInfo o : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = o;
            if (!playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) continue;
            return true;
        }
        return false;
    }

    public static boolean isBot(Entity entity) {
        if (ModManager.getModByName("AntiBot").isEnabled() && mode.isCurrentMode("Hypixel")) {
            if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.getDisplayName().getFormattedText().toLowerCase().contains("[npc")) {
                return false;
            }
            return true;
        }
        return false;
    }
}

