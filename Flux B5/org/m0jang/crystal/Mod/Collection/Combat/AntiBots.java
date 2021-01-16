package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventPacketReceive;
import org.m0jang.crystal.Events.EventPlayerList;
import org.m0jang.crystal.Events.EventRespawn;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ClientUtils;
import org.m0jang.crystal.Utils.Location;
import org.m0jang.crystal.Values.Value;

public class AntiBots extends Module {
   private List tablist = new ArrayList();
   private HashMap UUIDAndEntityID = new HashMap();
   public List GroundedChecked = new ArrayList();
   public List ReflexBot = new ArrayList();
   public static Value Mode = new Value("AntiBots", String.class, "Mode", "Advanced", new String[]{"Advanced", "Basic"});

   public AntiBots() {
      super("AntiBots", Category.Combat, Mode);
      this.tablist.clear();
   }

   public void onEnable() {
      super.onEnable();
      if (Minecraft.theWorld != null) {
         Iterator var2 = ClientUtils.loadedEntityList().iterator();

         while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity.isEntityAlive() && entity instanceof EntityPlayer && entity.onGround) {
               this.onGroundEvent(entity.getEntityId());
            }
         }

      }
   }

   public void onDisable() {
      super.onDisable();
      this.tablist.clear();
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(this.getName() + " \247f" + Mode.getSelectedOption());
   }

   public boolean isNPC(EntityLivingBase entity) {
      if (entity == null) {
         return true;
      } else if (Mode.getSelectedOption().equals("GCheat")) {
         return !entity.isInvisible();
      } else if (entity.equals(Minecraft.thePlayer)) {
         return true;
      } else if (entity.deathTime > 0) {
         return true;
      } else if (Crystal.INSTANCE.friendManager.isFriend(entity.getName())) {
         return true;
      } else if (!this.tablist.contains(entity.getUniqueID()) && !Mode.getSelectedOption().equals("AAC")) {
         return true;
      } else if (!entity.isEntityAlive()) {
         return true;
      } else if (entity.getHealth() < 0.0F) {
         return true;
      } else if (entity.isDead) {
         return true;
      } else if (entity.isInvisible() && !Mode.getSelectedOption().equals("AAC")) {
         return true;
      } else if (entity.isPlayerSleeping()) {
         return true;
      } else {
         if (Mode.getSelectedOption().equalsIgnoreCase("Mineplex")) {
            if (!this.UUIDAndEntityID.containsKey(entity.getUniqueID())) {
               return true;
            }

            if (((Integer)this.UUIDAndEntityID.get(entity.getUniqueID())).intValue() != entity.getEntityId()) {
               return true;
            }
         }

         if (Mode.getSelectedOption().equals("Advanced") && !this.isChecked(entity)) {
            return true;
         } else if (Mode.getSelectedOption().equals("Reflex") && this.ReflexBot.contains(entity.getEntityId())) {
            return true;
         } else {
            return Mode.getSelectedOption().equals("AAC") && entity.getEntityId() > 20000000 && !isBindedAACBot(entity);
         }
      }
   }

   public static boolean isBindedAACBot(EntityLivingBase entity) {
      if (!entity.isInvisible()) {
         return false;
      } else {
         Iterator var2 = ClientUtils.loadedEntityList().iterator();

         Entity ent;
         do {
            if (!var2.hasNext()) {
               return false;
            }

            ent = (Entity)var2.next();
         } while(ent == entity || ent == Minecraft.thePlayer || !(ent instanceof EntityPlayer) || ent.isInvisible() || (new Location(ent.getPosition())).distanceTo(new Location(entity.getPosition())) >= 1.5D || (new Location(ent.getPosition())).distanceToY(new Location(entity.getPosition())) >= 0.5D);

         return true;
      }
   }

   public static boolean isBindedGCheatBot(EntityLivingBase entity) {
      Iterator var2 = ClientUtils.loadedEntityList().iterator();

      Entity ent;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         ent = (Entity)var2.next();
      } while(ent == entity || ent == Minecraft.thePlayer || !(ent instanceof EntityPlayer) || ent.isInvisible() || (new Location(ent.getPosition())).distanceTo(new Location(entity.getPosition())) >= 1.0D || (new Location(ent.getPosition())).distanceToY(new Location(entity.getPosition())) >= 0.5D);

      return true;
   }

   @EventTarget
   public void onUpdatePlayerList(EventPlayerList event) {
      if (event.action == S38PacketPlayerListItem.Action.ADD_PLAYER) {
         if (!this.tablist.contains(event.uuid)) {
            this.tablist.add(event.uuid);
         }
      } else if (event.action == S38PacketPlayerListItem.Action.REMOVE_PLAYER && this.tablist.contains(event.uuid)) {
         this.tablist.remove(event.uuid);
      }

   }

   @EventTarget
   public void onPacketRecieve(EventPacketReceive event) {
      if (event.packet instanceof S0CPacketSpawnPlayer) {
         S0CPacketSpawnPlayer packetSpawnPlayer = (S0CPacketSpawnPlayer)event.packet;
         if (this.UUIDAndEntityID.containsKey(packetSpawnPlayer.getPlayerId())) {
            if (((Integer)this.UUIDAndEntityID.get(packetSpawnPlayer.getPlayerId())).intValue() > packetSpawnPlayer.getEntityId()) {
               this.UUIDAndEntityID.remove(packetSpawnPlayer.getPlayerId());
               this.UUIDAndEntityID.put(packetSpawnPlayer.getPlayerId(), packetSpawnPlayer.getEntityId());
            }
         } else {
            this.UUIDAndEntityID.put(packetSpawnPlayer.getPlayerId(), packetSpawnPlayer.getEntityId());
         }
      }

   }

   public boolean isChecked(Entity entity) {
      return this.GroundedChecked.contains(entity.getEntityId());
   }

   @EventTarget
   public void onRespawn(EventRespawn event) {
      this.UUIDAndEntityID.clear();
      this.GroundedChecked.clear();
      this.ReflexBot.clear();
   }

   public void onGroundEvent(Integer entityId) {
      if (!this.GroundedChecked.contains(entityId) && this.getEntityByWorld(entityId) != null && this.getEntityByWorld(entityId) instanceof EntityLivingBase) {
         this.GroundedChecked.add(entityId);
      }

   }

   @EventTarget
   public void onUpdateOtherPlayer(EventPacketReceive event) {
      if (event.getPacket() instanceof S14PacketEntity) {
         S14PacketEntity packetEntity = (S14PacketEntity)event.getPacket();
         if (!(packetEntity.getEntity(Minecraft.theWorld) instanceof EntityPlayer)) {
            return;
         }

         EntityPlayer entity = (EntityPlayer)packetEntity.getEntity(Minecraft.theWorld);
         if (entity.onGround) {
            this.onGroundEvent(entity.getEntityId());
         }

         if (!this.ReflexBot.contains(entity.getEntityId()) && entity.hasCustomName()) {
            this.ReflexBot.add(entity.getEntityId());
         } else if (!this.ReflexBot.contains(entity.getEntityId()) && !entity.hasCustomName()) {
            this.ReflexBot.remove(entity.getEntityId());
         }
      }

   }

   public Entity getEntityByWorld(Integer entityId) {
      return Minecraft.theWorld.getEntityByID(entityId.intValue());
   }
}
