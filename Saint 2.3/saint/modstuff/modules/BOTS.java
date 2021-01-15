package saint.modstuff.modules;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.EventChatSent;
import saint.eventstuff.events.OnUpdate;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.EntityHelper;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class BOTS extends Module {
   private final Value spam = new Value("bots_spam", true);
   private final Value follow = new Value("bots_follow", true);
   private final Value killaura = new Value("bots_killaura", true);
   public static final List targets = new CopyOnWriteArrayList();
   private final TimeHelper delay = new TimeHelper();

   public BOTS() {
      super("BOTS", -657931, ModManager.Category.PLAYER);
      Saint.getCommandManager().getContentList().add(new Command("bots", "spam/follow", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("spam")) {
               BOTS.this.spam.setValueState(!(Boolean)BOTS.this.spam.getValueState());
               Logger.writeChat("Your BOTS will " + ((Boolean)BOTS.this.spam.getValueState() ? "now" : "no longer") + " repeat your message.");
            } else if (message.split(" ")[1].equalsIgnoreCase("follow")) {
               BOTS.this.follow.setValueState(!(Boolean)BOTS.this.follow.getValueState());
               Logger.writeChat("The BOTS will " + ((Boolean)BOTS.this.follow.getValueState() ? "now" : "no longer") + " follow you if you're inside their bodies (LOL).");
            } else if (message.split(" ")[1].equalsIgnoreCase("killaura")) {
               BOTS.this.killaura.setValueState(!(Boolean)BOTS.this.killaura.getValueState());
               Logger.writeChat("The BOTS will " + ((Boolean)BOTS.this.killaura.getValueState() ? "now" : "no longer") + " use Kill Aura on other player or animals.");
            } else {
               Logger.writeChat("Option not valid! Available options: spam, follow.");
            }

         }
      });
   }

   public boolean isValidTarget(Entity entity) {
      boolean valid = false;
      if (entity == mc.thePlayer.ridingEntity) {
         return false;
      } else {
         if (entity instanceof EntityPlayer) {
            valid = entity != null && !PlayerControllerMP.entities.contains(entity) && entity != mc.thePlayer && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= 4.0F && !Saint.getFriendManager().isFriend(entity.getName());
         } else if (entity instanceof IMob) {
            valid = entity != null && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= 4.0F;
         } else if (entity instanceof IAnimals && !(entity instanceof IMob)) {
            valid = entity != null && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= 4.0F;
         }

         return valid;
      }
   }

   private void attack(Entity entity) {
      Iterator var3 = PlayerControllerMP.entities.iterator();

      while(var3.hasNext()) {
         EntityPlayerSP entity1 = (EntityPlayerSP)var3.next();
         if (entity1 != null) {
            EntityPlayerSP var10000 = mc.thePlayer;
         }
      }

   }

   public void onEvent(Event event) {
      if (event instanceof EventChatSent) {
         String message = ((EventChatSent)event).getMessage();
         Iterator var4 = PlayerControllerMP.entities.iterator();

         while(var4.hasNext()) {
            EntityPlayerSP entity = (EntityPlayerSP)var4.next();
            if (entity != mc.thePlayer && !message.startsWith(".") && (Boolean)this.spam.getValueState() && entity != null) {
               entity.sendQueue.addToSendQueue(new C01PacketChatMessage(message));
            }
         }
      } else {
         EntityPlayerSP entity;
         Iterator var8;
         if (event instanceof PreMotion) {
            if (targets.isEmpty()) {
               this.populateTargets();
            }

            var8 = PlayerControllerMP.entities.iterator();

            while(var8.hasNext()) {
               entity = (EntityPlayerSP)var8.next();
               if (entity != null && entity != mc.thePlayer && (Boolean)this.follow.getValueState() && mc.thePlayer.getDistanceToEntity(entity) <= 1.0F) {
                  entity.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                  entity.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(entity.posX, entity.posY, entity.posZ, entity.onGround));
                  if (mc.thePlayer.isSneaking()) {
                     entity.setSneaking(true);
                     entity.sendQueue.addToSendQueue(new C0BPacketEntityAction(entity, C0BPacketEntityAction.Action.START_SNEAKING));
                  } else {
                     entity.sendQueue.addToSendQueue(new C0BPacketEntityAction(entity, C0BPacketEntityAction.Action.STOP_SNEAKING));
                  }
               }
            }
         } else if (event instanceof OnUpdate) {
            var8 = PlayerControllerMP.entities.iterator();

            while(true) {
               do {
                  do {
                     do {
                        if (!var8.hasNext()) {
                           return;
                        }

                        entity = (EntityPlayerSP)var8.next();
                     } while(entity == null);
                  } while(entity == mc.thePlayer);
               } while(!(Boolean)this.killaura.getValueState());

               Iterator var5 = targets.iterator();

               while(var5.hasNext()) {
                  Entity target = (Entity)var5.next();
                  if (this.isValidTarget(target) && this.delay.hasReached(100L)) {
                     float[] rotations = EntityHelper.getEntityRotations(entity, target);
                     entity.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], entity.onGround));
                     entity.sendQueue.addToSendQueue(new C0APacketAnimation());
                     entity.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
                     entity.sendQueue.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                     this.delay.reset();
                  }
               }
            }
         }
      }

   }

   private void populateTargets() {
      Iterator var2 = mc.theWorld.loadedEntityList.iterator();

      while(var2.hasNext()) {
         Object o = var2.next();
         Entity entity = (Entity)o;
         if (this.isValidTarget(entity)) {
            targets.add(entity);
         }
      }

   }
}
