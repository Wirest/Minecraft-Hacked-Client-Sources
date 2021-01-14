package moonx.ohare.client.module.impl.combat;

import java.awt.Color;
import java.util.*;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import org.apache.commons.lang3.StringUtils;

import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Module {
    private static ArrayList<Entity> bots = new ArrayList<>();
    private EnumValue<Mode> mode = new EnumValue<>("Mode", Mode.HYPIXEL);
    private BooleanValue remove = new BooleanValue("Remove Bots", "Remove Bots From World", false);
    private Map<Integer, Double> distanceMap = new HashMap<>();
    private Set<Integer> swingSet = new HashSet<>();
    private TimerUtil timerUtil = new TimerUtil();

    public AntiBot() {
        super("AntiBot", Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        setDescription("Add bots to a list of poop heads");
        setRenderLabel("Anti Bot");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().theWorld == null) return;
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        switch (mode.getValue()) {
            case HYPIXEL:
                if (event.isPre()) {
                    if (getMc().thePlayer.ticksExisted % 600 == 0) {
                        bots.clear();
                    }
                }
                for (Entity entity : getMc().theWorld.getLoadedEntityList()) {
                    if (entity instanceof EntityPlayer) {
                        if (entity != getMc().thePlayer) {
                            if (isEntityBot(entity)) {
                                if (remove.isEnabled()) getMc().theWorld.removeEntity(entity);
                                bots.add(entity);
                            } else bots.remove(entity);
                        }
                    }
                }
                break;
            case MINEPLEX:
                for (Entity e : getMc().theWorld.getLoadedEntityList()) {
                    if (e instanceof EntityPlayer) {
                        if (e.ticksExisted < 2 && ((EntityPlayer) e).getHealth() < 20 && ((EntityPlayer) e).getHealth() > 0 && e != getMc().thePlayer) {
                            if (remove.isEnabled()) getMc().theWorld.removeEntity(e);
                            bots.add(e);
                        }
                    }
                }
                ;
                break;
            default:
                break;
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (getMc().theWorld == null) return;
        if (event.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) event.getPacket();
            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();
            double d = getMc().thePlayer.getDistance(x, y, z);

            distanceMap.put(packet.getEntityID(), d);
        }

        if (event.getPacket() instanceof S0BPacketAnimation) {
            S0BPacketAnimation packet = (S0BPacketAnimation) event.getPacket();

            if (packet.getAnimationType() != 0) return;

            swingSet.add(packet.getEntityID());
        }

    }

    private boolean isEntityBot(Entity entity) {
        if (!distanceMap.containsKey(entity.getEntityId())) return false;
        double distance = distanceMap.get(entity.getEntityId());
        if (getMc().getCurrentServerData() == null || !swingSet.contains(entity.getEntityId()) || !(entity instanceof EntityPlayer))
            return false;
        return getMc().getCurrentServerData().serverIP.toLowerCase().contains("hypixel") && ((distance > 14.5 && distance < 17) || entity.getName().startsWith("\247") || entity.getDisplayName().getFormattedText().startsWith("ยง") || entity.getDisplayName().getFormattedText().toLowerCase().startsWith("npc") || !isOnTab(entity)) && getMc().thePlayer.ticksExisted > 100;
    }

    private boolean isOnTab(Entity entity) {
        return getMc().getNetHandler().getPlayerInfoMap().stream().anyMatch(info -> info.getGameProfile().getName().equalsIgnoreCase(entity.getName()));
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

    public static List<Entity> getBots() {
        return bots;
    }
}
