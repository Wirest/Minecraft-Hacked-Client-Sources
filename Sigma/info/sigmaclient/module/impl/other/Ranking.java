package info.sigmaclient.module.impl.other;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventAttack;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.users.TTSystem;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class Ranking extends Module {
    private HashMap<Integer, HitEntity> hitEntities = new HashMap<>();
    private HashMap<Integer, HitEntity> prosecutorEntities = new HashMap<>();
    private HashMap<Integer, Long> lastAnimationTimestamp = new HashMap<>();
    private info.sigmaclient.util.Timer delay = new Timer();

    public Ranking(ModuleData data) {
        super(data);
        setHidden(true);
        if (!isEnabled()) {
            toggle();
        }
    }

    @Override
    @RegisterEvent(events = {EventAttack.class, EventPacket.class, EventUpdate.class})
    public void onEvent(Event e) {
        if (e instanceof EventAttack) {
            EventAttack ea = (EventAttack) e;
            if (ea.getEntity() instanceof EntityPlayer) {
                EntityPlayer ep = (EntityPlayer) ea.getEntity();
                if (!ep.getName().isEmpty()) {
                    if (!hitEntities.containsKey(ep.getEntityId())) {
                        hitEntities.put(ep.getEntityId(), new HitEntity(ep));
                    } else {
                        hitEntities.get(ep.getEntityId()).refresh();
                    }
                }
            }
        } else if (e instanceof EventPacket) {
            EventPacket ep = (EventPacket) e;
            Packet packet = ep.getPacket();
            if (packet instanceof S0BPacketAnimation) {
                S0BPacketAnimation packetAnimation = (S0BPacketAnimation) packet;
                lastAnimationTimestamp.put(packetAnimation.func_148978_c(), System.currentTimeMillis());
            }
            if (packet instanceof S06PacketUpdateHealth) {
                S06PacketUpdateHealth packetUpdateHealth = (S06PacketUpdateHealth) packet;
                if (packetUpdateHealth.getHealth() <= Minecraft.getMinecraft().thePlayer.getHealth()) {
                	try{
                		for (Object o : mc.theWorld.getLoadedEntityList()) {
                            if (o instanceof EntityPlayer) {
                                EntityPlayer entityPlayer = (EntityPlayer) o;
                                if (!(entityPlayer instanceof EntityPlayerSP) && lastAnimationTimestamp.containsKey(entityPlayer.getEntityId())) {
                                    if (lastAnimationTimestamp.get(entityPlayer.getEntityId()) > System.currentTimeMillis() - 70 && entityPlayer.getPositionVector().squareDistanceTo(Minecraft.getMinecraft().thePlayer.getPositionVector()) < 9 * 9) {
                                        if (!prosecutorEntities.containsKey(entityPlayer.getEntityId())) {
                                            prosecutorEntities.put(entityPlayer.getEntityId(), new HitEntity(entityPlayer, true));
                                        } else {
                                            prosecutorEntities.get(entityPlayer.getEntityId()).refresh();
                                        }
                                    }
                                }
                            }
                        }
                	}catch(ConcurrentModificationException ee){};
                    
                }
                if (packetUpdateHealth.getHealth() <= 0) {
                    onDeath();
                }
            }
            if (packet instanceof S02PacketChat) {
                S02PacketChat packetChat = (S02PacketChat) packet;
                String message = packetChat.func_148915_c().getUnformattedText();

                Iterator it = hitEntities.entrySet().iterator();
                while (it.hasNext()) {
                    HitEntity hitEntity = ((Map.Entry<Integer, HitEntity>) it.next()).getValue();
                    if (hitEntity.getTime() > System.currentTimeMillis() - 3000 && message.contains(hitEntity.getName())) {
                        if (hitEntity.onChatMessage()) {
                            it.remove();
                        }
                    }
                }

                Iterator it2 = prosecutorEntities.entrySet().iterator();
                while (it2.hasNext()) {
                    HitEntity hitEntity = ((Map.Entry<Integer, HitEntity>) it2.next()).getValue();
                    if (hitEntity.getTime() > System.currentTimeMillis() - 3000 && message.contains(hitEntity.getName()) && message.contains(Minecraft.getMinecraft().thePlayer.getName())) {
                        if (hitEntity.onChatMessage()) {
                            it2.remove();
                        }
                    }
                }
            } else if (packet instanceof S18PacketEntityTeleport) {
                S18PacketEntityTeleport packetTeleport = (S18PacketEntityTeleport) packet;
                int entityId = packetTeleport.func_149451_c();

                onEntityRemoved(entityId);
            } else if (packet instanceof S08PacketPlayerPosLook) {
                onDeath();
            } else if (packet instanceof S13PacketDestroyEntities) {
                S13PacketDestroyEntities packetDestroyEntities = (S13PacketDestroyEntities) packet;
                for (int entityId : packetDestroyEntities.func_149098_c()) {
                    onEntityRemoved(entityId);
                }
            } else if (packet instanceof S40PacketDisconnect) {
                S40PacketDisconnect packetDisconnect = (S40PacketDisconnect) packet;
                if (packetDisconnect.func_149165_c().getUnformattedText().toLowerCase().contains("ban") && Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.ticksExisted > 15 * 20) {
                    sendStat("ban", "");
                }
            }
        } else if (e instanceof EventUpdate) {
            if (delay.delay(1000 * 60 * 10)) {
                delay.reset();
                sendStat("tick", "");
            }
        }
    }

    private void onDeath() {
        Iterator it2 = prosecutorEntities.entrySet().iterator();
        while (it2.hasNext()) {
            HitEntity hitEntity = ((Map.Entry<Integer, HitEntity>) it2.next()).getValue();
            if (hitEntity.getTime() > System.currentTimeMillis() - 3000) {
                if (hitEntity.onRemoved()) {
                    it2.remove();
                }
            }
        }
    }

    private void onEntityRemoved(int entityId) {
        Iterator it = hitEntities.entrySet().iterator();
        while (it.hasNext()) {
            HitEntity hitEntity = ((Map.Entry<Integer, HitEntity>) it.next()).getValue();
            if (hitEntity.getTime() > System.currentTimeMillis() - 3000 && entityId == hitEntity.getEntityId()) {
                if (hitEntity.onRemoved()) {
                    it.remove();
                }
            }
        }
    }

    public void sendStat(String type, String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://" + "sabrinaprg" + '.' + "sigmaclient.info" + "/" + Client.um.getVersionString() + Client.um.bytesToHex(TTSystem.getInstance().getTT()) + "/registerStat";
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty("USN", Client.um.getUserSerialNumber());
                    connection.setRequestProperty("Session", Client.um.getSession());
                    connection.setDoOutput(true);
                    connection.getOutputStream().write(("type=" + type + data).getBytes("UTF-8"));
                    connection.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class HitEntity {
        private long time = System.currentTimeMillis();
        private UUID uuid;
        private String name;
        private int entityId;
        private boolean removed = false;
        private boolean chatMessage = false;
        private boolean prosecutor = false;

        public HitEntity(EntityPlayer ep) {
            uuid = ep.getUniqueID();
            name = ep.getName();
            entityId = ep.getEntityId();
        }

        public HitEntity(EntityPlayer ep, boolean prosecutor) {
            uuid = ep.getUniqueID();
            name = ep.getName();
            entityId = ep.getEntityId();
            this.prosecutor = prosecutor;
        }

        public boolean onRemoved() {
            removed = true;
            if (chatMessage) {
                if (prosecutor) {
                    prosecuted();
                } else {
                    killed();
                }
                return true;
            }
            return false;
        }

        public boolean onChatMessage() {
            chatMessage = true;
            if (removed) {
                if (prosecutor) {
                    prosecuted();
                } else {
                    killed();
                }
                return true;
            }
            return false;
        }

        private void killed() {
            //ChatUtil.printChat("Kill (" + name + ") " + uuid.toString());
            sendStat("kill", "&uuid=" + uuid.toString().replaceAll("-", ""));
        }

        public void refresh() {
            time = System.currentTimeMillis();
        }

        public String getName() {
            return name;
        }

        public long getTime() {
            return time;
        }

        public int getEntityId() {
            return entityId;
        }

        public void prosecuted() {
            //ChatUtil.printChat("Prosecuted (" + name + ") " + uuid.toString());
            sendStat("death", "&uuid=" + uuid.toString().replaceAll("-", ""));
        }
    }
}
