package com.etb.client.module.modules.combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.module.Module;
import com.etb.client.utils.TimerUtil;
import com.etb.client.utils.value.impl.EnumValue;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import static com.etb.client.module.modules.movement.Speed.Mode.HYPIXEL;

public class AntiBot extends Module {
    public static ArrayList<EntityPlayer> bots = new ArrayList<>();
    private TimerUtil timer = new TimerUtil();
    private EnumValue<Mode> mode = new EnumValue("Mode", Mode.HYPIXEL);

    public AntiBot() {
        super("AntiBot", Module.Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        setDescription("Add bots to a list of poop heads");
        addValues(mode);
    }
    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        switch (mode.getValue()) {
            case HYPIXEL:
                if (mc.getCurrentServerData() != null && mc.theWorld != null && mc.getCurrentServerData().serverIP.contains("hypixel")) {
                    for (Object entities : mc.theWorld.loadedEntityList) {
                        if (entities instanceof EntityPlayer) {
                            EntityPlayer entity = (EntityPlayer) entities;
                            if (entity != mc.thePlayer) {
                                if (mc.thePlayer.getDistanceToEntity(entity) < 10) {
                                    if (!entity.getDisplayName().getFormattedText().startsWith("ยง") || entity.isInvisible() || entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")) {
                                        bots.add(entity);
                                    }
                                }
                            }
                            if (bots.contains(entity) && !entity.isInvisible()) {
                                bots.remove(entity);
                            }
                        }
                    }
                }
                break;
            case MINEPLEX:
                for (Object object : mc.theWorld.playerEntities) {
                    if (object instanceof EntityPlayer) {
                        EntityPlayer e = (EntityPlayer) object;
                        if (e.ticksExisted < 2 && e.getHealth() < 20 && e.getHealth() > 0 && e != mc.thePlayer) {
                            mc.theWorld.removeEntity(e);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private enum Mode {
        HYPIXEL, MINEPLEX
    }

    @Override
    public void onEnable() {
        bots.clear();
    }

    @Override
    public void onDisable() {
        bots.clear();
    }

    public static List<EntityPlayer> getBots() {
        return bots;
    }
}