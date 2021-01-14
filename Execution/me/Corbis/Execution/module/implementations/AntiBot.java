package me.Corbis.Execution.module.implementations;
import com.google.common.collect.Ordering;
import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.event.events.EventUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;
import java.util.*;

public class AntiBot extends Module {

    private int botsFound = 0;
    public boolean invalid;
    private Object EntityArmorStand;
    public static List<EntityLivingBase> bots = new ArrayList<>();
    private static ArrayList<Suspect> invisSus = new ArrayList<Suspect>();
    private static ArrayList<Suspect> visSus = new ArrayList<Suspect>();
    private static ArrayList<Suspect> neverRemoveBots = new ArrayList<Suspect>();
    private Map<Integer, Double> distanceMap = new HashMap<>();
    private Set<Integer> swingSet = new HashSet<>();
    public AntiBot() {
        super("AntiBot", Keyboard.KEY_NONE, Category.MISC);
        ArrayList<String> options = new ArrayList<>();
        options.add("Advanced");
        options.add("Watchdog");
        //	options.add("Cubecraft");
        options.add("Mineplex");
        options.add("Mc-central");
        Execution.instance.settingsManager.rSetting(new Setting("AntiBot Mode", this, "Advanced", options));
    }


    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        String mode = Execution.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();

        if (mode.equalsIgnoreCase("Advanced") && event.getPacket() instanceof S0CPacketSpawnPlayer) {
            S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) event.getPacket();
            double posX = packet.getX() / 32D;
            double posY = packet.getY() / 32D;
            double posZ = packet.getZ() / 32D;

            double diffX = mc.thePlayer.posX - posX;
            double diffY = mc.thePlayer.posY - posY;
            double diffZ = mc.thePlayer.posZ - posZ;

            double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);

            if (dist <= 8 && posX != mc.thePlayer.posX && posY != mc.thePlayer.posY && posZ != mc.thePlayer.posZ)
                this.bots.add((EntityLivingBase) mc.theWorld.getEntityByID(packet.getEntityID()));


        }
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
    public boolean neverRemoveContains(EntityPlayer ep) {
        for (int i = 0; i < this.neverRemoveBots.size(); i++) {
            if (ep == this.neverRemoveBots.get(i).player) {
                return true;
            }
        }
        return false;
    }

    private void add(EntityPlayer ep) {
        if (!this.bots.contains(ep)) {
            botsFound++;
            this.bots.add(ep);
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Execution.instance.settingsManager.getSettingByName("AntiBot Mode").getValString();


        if(mode.equalsIgnoreCase("Cubecraft")){
            for (Object entity : mc.theWorld.loadedEntityList)
                if (((Entity) entity).isInvisible() || entity == EntityArmorStand) {
                    //DivineClientB1.addChatMessage("Bot Detected. Removing Entity");
                    //mc.theWorld.removeEntity((Entity) entity);
                    this.bots.add((EntityLivingBase)entity);
                }else {
                    this.bots.remove(entity);
                }
        }
        if(mode.equalsIgnoreCase("Mineplex")){
            for(Object o : mc.theWorld.loadedEntityList){
                Entity en = (Entity)o;
                if(en instanceof EntityPlayer && !(en instanceof EntityPlayerSP)){
                    EntityPlayer bot = (EntityPlayer) en;
                    if(bot.groundTicks <= 20){
                        this.bots.add(bot);
                    }else {
                        this.bots.remove(bot);
                    }
                }


            }
        }



        if(mode.equalsIgnoreCase("Mc-central")){
            for(int k = 0; k < mc.theWorld.playerEntities.size(); k++){
                EntityPlayer ent = mc.theWorld.playerEntities.get(k);
                List<EntityPlayer> tablist = this.getPlayerTabList();
                if(tablist.contains(ent) && ent.getName().equals("alex_markey") || ent.getName().equals("vislo") || ent.getName().equals("Teddzy") || ent.getName().equals("TeddyBearrr") || ent.getName().equals("Timppali") || ent.getName().equals("Unadvised") || ent.getName().equals("fouffy") || ent.getName().equals("teddy3684") || ent.getName().equals("KierenBoal") || ent.getName().equals("AcceptedAppeal") || ent.getName().equals("UglyKidSteve") || ent.getName().equals("Swaggle") || ent.getName().equals("Bouncehouses") || ent.getName().equals("AyeItsBeck") || ent.getName().equals("socialisinq") || ent.getName().equals("Kane") || ent.getName().equals("Chilo_") || ent.getName().equals("Eunbin") || ent.getName().equals("ray01") || ent.getName().equals("kvng_steph") || ent.getName().equals("BoofPacks") || ent.getName().equals("xBenz") || ent.getName().equals("Muel_") || ent.getName().equals("Recrement") || ent.getName().equals("LongDays") || ent.getName().equals("JapanCrafter") || ent.getName().equals("seekingattention") || ent.getName().equals("DeathStrokeDevil") || ent.getName().equals("AZXG") || ent.getName().equals("Pizzicato_") || ent.getName().equals("TrippedUp") || ent.getName().equals("MicroSquid") || ent.getName().equals("PlayerUnbound") || ent.getName().equals("Master_Aqua") || ent.getName().equals("Cxrtr") || ent.getName().equals("Personalisation")){


                    mc.thePlayer.sendChatMessage("/leave");


                }

                if(mc.theWorld.playerEntities.contains(ent) && ent.getName().equals("alex_markey") || ent.getName().equals("vislo") || ent.getName().equals("Teddzy") || ent.getName().equals("TeddyBearrr") || ent.getName().equals("Timppali") || ent.getName().equals("Unadvised") || ent.getName().equals("fouffy") || ent.getName().equals("teddy3684") || ent.getName().equals("KierenBoal") || ent.getName().equals("AcceptedAppeal") || ent.getName().equals("UglyKidSteve") || ent.getName().equals("Swaggle") || ent.getName().equals("Bouncehouses") || ent.getName().equals("AyeItsBeck") || ent.getName().equals("socialisinq") || ent.getName().equals("Kane") || ent.getName().equals("Chilo_") || ent.getName().equals("Eunbin") || ent.getName().equals("ray01") || ent.getName().equals("kvng_steph") || ent.getName().equals("BoofPacks") || ent.getName().equals("xBenz") || ent.getName().equals("Muel_") || ent.getName().equals("Recrement") || ent.getName().equals("LongDays") || ent.getName().equals("JapanCrafter") || ent.getName().equals("seekingattention") || ent.getName().equals("DeathStrokeDevil") || ent.getName().equals("AZXG") || ent.getName().equals("Pizzicato_") || ent.getName().equals("TrippedUp") || ent.getName().equals("MicroSquid") || ent.getName().equals("PlayerUnbound") || ent.getName().equals("Master_Aqua") || ent.getName().equals("Cxrtr") || ent.getName().equals("Personalisation")){

                    mc.thePlayer.sendChatMessage("/leave");

                }


            }
        }
        this.setDisplayName("Antibot §f[" + mode + "]");
        if(mode.equalsIgnoreCase("Watchdog")){
                if (getMc().thePlayer.ticksExisted % 600 == 0) {
                    bots.clear();
                }

            for (Entity entity : getMc().theWorld.getLoadedEntityList()) {
                if (entity instanceof EntityPlayer) {
                    if (entity != getMc().thePlayer) {
                        if (isEntityBot(entity)) {
                            bots.add((EntityLivingBase) entity);
                        } else bots.remove(entity);
                    }
                }
            }
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
        Iterator var2 = mc.getNetHandler().getPlayerInfoMap().iterator();

        NetworkPlayerInfo info;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            info = (NetworkPlayerInfo)var2.next();
        } while(!info.getGameProfile().getName().equals(entity.getName()));

        return true;
    }
    public Ordering<NetworkPlayerInfo> field_175252_a(){
        try
        {
            final Class<GuiPlayerTabOverlay> c = GuiPlayerTabOverlay.class;
            final Field f = c.getField("field_175252_a");
            f.setAccessible(true);
            return (Ordering<NetworkPlayerInfo>)f.get(GuiPlayerTabOverlay.class);
        }catch(Exception e)
        {
            return null;

        }
    }

    private List<EntityPlayer> getPlayerTabList() {
        final List<EntityPlayer> list;
        (list = new ArrayList<EntityPlayer>()).clear();
        Ordering<NetworkPlayerInfo> field_175252_a = field_175252_a();
        if(field_175252_a == null){
            return list;
        }

        final List players = field_175252_a.sortedCopy(mc.thePlayer.sendQueue.getPlayerInfoMap());
        for(final Object o : players){
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if(info == null){
                continue;
            }
            list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
/*	public static List<EntityPlayer> getTabPlayerList() {
		final NetHandlerPlayClient var4 = Wrapper.mc.thePlayer.sendQueue;
		final List<EntityPlayer> list = new ArrayList<>();
		final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
		for (final Object o : players) {
			final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
			if (info == null) {
				continue;
			}
			list.add(Wrapper.mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}*/

    @Override
    public void onDisable(){
        super.onDisable();
        bots.clear();
    }


    public static boolean isBot(EntityPlayer ep) {
        return bots.contains(ep);
    }

    public Suspect getInvisSuspect(EntityPlayer ep) {
        for (int i = 0; i < invisSus.size(); i++) {
            if (invisSus.get(i).player == ep) {
                return invisSus.get(i);
            }
        }
        return null;
    }

    public Suspect getVisSuspect(EntityPlayer ep) {
        for (int i = 0; i < visSus.size(); i++) {
            if (visSus.get(i).player == ep) {
                return visSus.get(i);
            }
        }
        return null;
    }





}

class Suspect {
    public EntityPlayer player;
    public long TPTime;
}
