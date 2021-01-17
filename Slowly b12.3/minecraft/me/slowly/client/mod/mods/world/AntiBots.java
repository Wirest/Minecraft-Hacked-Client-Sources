package me.slowly.client.mod.mods.world;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.mod.Mod;
import me.slowly.client.util.Colors;
import me.slowly.client.value.Value;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBots extends Mod {
   private static ArrayList playerList = new ArrayList();
   public static Value movedOnce = new Value("AntiBots_Moved", false);
   public static Value touchedGround = new Value("AntiBots_TouchedGround", false);
   public static Value wasInvisible = new Value("AntiBots_Invisible", false);
   public static Value swing = new Value("AntiBots_Swing", false);
   public static Value sprint = new Value("AntiBots_Sprint", false);
   public static Value derp = new Value("AntiBots_Derp", false);
   public static Value hypixel = new Value("AntiBots_Hypixel", false);
   public static Value enableTicksExsitedCheck = new Value("AntiBots_EnableTicksExisted", false);
   public static Value ticksExisted = new Value("AntiBots_TicksExisted", 0.0D, 0.0D, 10000.0D, 50.0D);
   public static Value enableIDCheck = new Value("AntiBots_IDCheck", false);
   public static Value maximumID = new Value("AntiBots_MaximumID", 1500000.0D, 1000000.0D, 1.0E7D, 100000.0D);

   public AntiBots() {
      super("AntiBots", Mod.Category.WORLD, Colors.GREY.c);
   }

   public void onDisable() {
      playerList.clear();
      super.onDisable();
   }

   @EventTarget
   public void onUpdate(EventPreMotion event) {
       if (((Boolean)hypixel.getValueState()).booleanValue()) {
           for (Object entity : this.mc.theWorld.loadedEntityList) {
               if (!((Entity)entity).isInvisible() || entity == this.mc.thePlayer) continue;
               this.mc.theWorld.removeEntity((Entity)entity);
           }
       }
      this.removeOld();
      Iterator var3 = this.mc.theWorld.playerEntities.iterator();

      while(var3.hasNext()) {
         EntityPlayer p = (EntityPlayer)var3.next();
         boolean exist = false;
         Iterator var6 = playerList.iterator();

         while(var6.hasNext()) {
            AntiBots.Player player = (AntiBots.Player)var6.next();
            if (player.player.getName().equalsIgnoreCase(p.getName())) {
               player.update();
               exist = true;
            }
         }

         if (!exist) {
            playerList.add(new AntiBots.Player(p));
         }
      }

   }

   private void removeOld() {
      for(int i = 0; i < playerList.size(); ++i) {
         AntiBots.Player player = (AntiBots.Player)playerList.get(i);
         boolean exist = false;
         Iterator var5 = this.mc.theWorld.playerEntities.iterator();

         while(var5.hasNext()) {
            EntityPlayer p = (EntityPlayer)var5.next();
            if (p.getName().equalsIgnoreCase(player.player.getName())) {
               exist = true;
            }
         }

         if (!exist) {
            playerList.remove(i);
         }
      }

   }

   public static boolean isBot(EntityPlayer player) {
      Iterator var2 = playerList.iterator();

      while(var2.hasNext()) {
         AntiBots.Player p = (AntiBots.Player)var2.next();
         if (p.player.getName().equalsIgnoreCase(player.getName())) {
            if (((Boolean)movedOnce.getValueState()).booleanValue() && !p.moved()) {
               return true;
            }

            if (((Boolean)touchedGround.getValueState()).booleanValue() && !p.touchedGround) {
               return true;
            }
            if (((Boolean)swing.getValueState()).booleanValue() && !p.swing) {
                return true;
             }
            if (((Boolean)sprint.getValueState()).booleanValue() && !p.sprint) {
                return true;
             }
            if (((Boolean)derp.getValueState()).booleanValue() && !p.derp) {
                return true;
             }

            if (((Boolean)wasInvisible.getValueState()).booleanValue() && p.wasInvisible) {
               return true;
            }

            if (((Boolean)enableTicksExsitedCheck.getValueState()).booleanValue() && (double)player.ticksExisted <= ((Double)ticksExisted.getValueState()).doubleValue()) {
               return true;
            }

            if (((Boolean)enableIDCheck.getValueState()).booleanValue() && (double)player.getEntityId() >= ((Double)maximumID.getValueState()).doubleValue()) {
               return true;
            }
         }
      }

      return false;
   }

   class Player {
      EntityPlayer player;
      int firstX;
      int firstY;
      int firstZ;
      boolean touchedGround;
      boolean wasInvisible;
      boolean swing;
      boolean sprint;
      boolean derp;
      public Player(EntityPlayer player) {
         this.player = player;
         this.firstX = (int)player.posX;
         this.firstY = (int)player.posY;
         this.firstZ = (int)player.posZ;
         this.touchedGround = false;
         this.wasInvisible = false;
      }

      void update() {
         if (this.player.onGround) {
            this.touchedGround = true;
         }

         if (this.player.isInvisible()) {
            this.wasInvisible = true;
         }

      }

      boolean moved() {
         return this.firstX != (int)this.player.posX || this.firstY != (int)this.player.posY || this.firstZ != (int)this.player.posZ;
      }
   }
}

