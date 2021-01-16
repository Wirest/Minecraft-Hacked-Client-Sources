package org.m0jang.crystal.Mod.Collection.Combat;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventRespawn;
import org.m0jang.crystal.Events.EventState;
import org.m0jang.crystal.Events.EventTick;
import org.m0jang.crystal.Events.EventUpdate;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.ClientUtils;
import org.m0jang.crystal.Utils.ModeUtils;
import org.m0jang.crystal.Utils.RotationUtils;
import org.m0jang.crystal.Utils.TimeHelper;
import org.m0jang.crystal.Values.Value;

public class Aura extends Module {
   private TimeHelper switchTimer = new TimeHelper();
   public static EntityLivingBase currentTarget;
   private static int switchIndex;
   private boolean AIMed;
   private boolean cancelNextTick;
   private long attacked;
   public static Value autoBlock;
   public static Value hvh;
   public static Value mode;
   public static Value range;
   public static Value cps;
   public static Value AttackMonster;
   public static Value AttackAnimal;
   public static Value AttackPlayer;
   public static Value AttackInvisible;
   public static Value team;
   TimeHelper timer = new TimeHelper();
   EntityLivingBase before;
   public static boolean unBlocked;

   static {
      autoBlock = new Value("Aura", Boolean.TYPE, "Autoblock", true);
      hvh = new Value("Aura", Boolean.TYPE, "HvH", false);
      mode = new Value("Aura", String.class, "Mode", "Switch", new String[]{"Single", "Tick", "Switch", "Multi"});
      range = new Value("Aura", Float.TYPE, "Range", 4.3F, 1.0F, 6.0F, 0.1F);
      cps = new Value("Aura", Float.TYPE, "CPS", 12.0F, 1.0F, 40.0F, 1.0F);
      AttackMonster = new Value("Aura", Boolean.TYPE, "Monster", false);
      AttackAnimal = new Value("Aura", Boolean.TYPE, "Animal", false);
      AttackPlayer = new Value("Aura", Boolean.TYPE, "Player", true);
      AttackInvisible = new Value("Aura", Boolean.TYPE, "Invisible", true);
      team = new Value("Aura", Boolean.TYPE, "Team", false);
   }

   public Aura() {
      super("Aura", Category.Combat, mode);
   }

   public void onDisable() {
      super.onDisable();
      currentTarget = null;
   }

   @EventTarget
   public void onTick(EventTick event) {
      this.setRenderName(this.getName() + " \247f" + mode.getSelectedOption());
   }

   @EventTarget
   public void onRespawn(EventRespawn eventRespawn) {
      this.setEnabled(false);
      ChatUtils.sendMessageToPlayer("Auto Disabled \247a" + this.getName() + "\247r by Respawn.");
   }

   @EventTarget
   private void onUpdate(EventUpdate event) {
      try {
         if (event.state == EventState.PRE) {
            if (needBlock() && autoBlock.getBooleanValue() && Minecraft.thePlayer.inventory.getCurrentItem() != null && Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
               Minecraft.thePlayer.setItemInUse(ClientUtils.player().getCurrentEquippedItem(), ClientUtils.player().getCurrentEquippedItem().getMaxItemUseDuration());
            }

            if (this.getTargets().size() <= 0) {
               currentTarget = null;
               this.AIMed = false;
               return;
            }

            if (mode.getSelectedOption().equals("Switch")) {
               if (switchIndex >= this.getTargets().size()) {
                  switchIndex = 0;
               }

               currentTarget = (EntityLivingBase)this.getTargets().get(switchIndex);
               if (this.switchTimer.hasPassed(1500.0D)) {
                  ++switchIndex;
                  this.switchTimer.reset();
               }
            } else {
               currentTarget = (EntityLivingBase)this.getTargets().get(0);
            }

            float[] NeedRotation = RotationUtils.getRotationsForAura(currentTarget, (double)(range.getFloatValue() + 1.0F));
            if (NeedRotation == null) {
               this.AIMed = false;
               return;
            }

            event.yaw = NeedRotation[0];
            event.pitch = NeedRotation[1];
            this.AIMed = true;
         } else if (event.state == EventState.POST) {
            if (!this.AIMed) {
               return;
            }

            if (currentTarget == null) {
               return;
            }

            if (!this.timer.hasPassed((double)(1000.0F / cps.getFloatValue()))) {
               return;
            }

            if (mode.getSelectedOption().equals("Tick") && currentTarget.hurtResistantTime >= 15) {
               return;
            }

            if (isEntityValid(currentTarget, true)) {
               this.attack(currentTarget, Crystal.INSTANCE.getMods().get(Criticals.class).isEnabled());
               this.timer.reset();
            }
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public int getScoreForAura(EntityLivingBase entity) {
      int result = 0;
      if (entity.hurtResistantTime < 16) {
         result -= 1000;
      }

      float[] NeedRotation = RotationUtils.getRotationsForAura(entity, (double)range.getFloatValue());
      if (NeedRotation == null) {
         return 9999;
      } else {
         result += this.getDistanceFromOldYaw(NeedRotation[0]);
         return result;
      }
   }

   public int getScoreForAuraNormal(final EntityLivingBase entity) {
       int result = 0;
       final float[] NeedRotation = RotationUtils.getRotationsForAura((Entity)entity, (double)Aura.range.getFloatValue());
       if (NeedRotation == null) {
           return 9999;
       }
       result += this.getDistanceFromOldYaw(NeedRotation[0]);
       return result;
   }

   public int getDistanceFromOldYaw(float yaw) {
      float neededYaw = Minecraft.thePlayer.rotationYaw - yaw;
      float distanceFromMouse = MathHelper.sqrt_float(neededYaw * neededYaw);
      return (int)distanceFromMouse;
   }

   public static boolean isEntityValid(Entity entity, boolean attack) {
      if (entity != null && entity instanceof EntityLivingBase) {
         EntityLivingBase entityLiving = (EntityLivingBase)entity;
         double seenRange = (double)(attack ? (Minecraft.thePlayer.canEntityBeSeen(entity) ? range.getFloatValue() : 3.0F) : range.getFloatValue() + 1.0F);
         boolean okRange = RotationUtils.getRotationsForAura(entityLiving, seenRange) != null;
         if (ClientUtils.player().isEntityAlive() && entityLiving.isEntityAlive() && okRange) {
            if (!ModeUtils.isValidForAura(entity)) {
               return false;
            } else {
               return !Crystal.INSTANCE.getMods().get(AntiBots.class).isEnabled() || !((AntiBots)Crystal.INSTANCE.getMods().get(AntiBots.class)).isNPC((EntityLivingBase)entity);
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean needBlock() {
      Iterator var1 = ClientUtils.loadedEntityList().iterator();

      while(var1.hasNext()) {
         Entity entity = (Entity)var1.next();
         if (isEntityValid(entity, false)) {
            return true;
         }
      }

      return false;
   }

   private List getTargets() {
      try {
         ArrayList targets = new ArrayList();
         Iterator var3 = ClientUtils.loadedEntityList().iterator();

         while(var3.hasNext()) {
            Entity entity = (Entity)var3.next();
            if (isEntityValid(entity, false)) {
               targets.add((EntityLivingBase)entity);
            }
         }

         targets.sort(new Comparator<EntityLivingBase>() {
            public int compare(EntityLivingBase target1, EntityLivingBase target2) {
               return Aura.mode.getSelectedOption().equals("Tick") ? Math.round((float)(Aura.this.getScoreForAura(target1) - Aura.this.getScoreForAura(target2))) : Math.round((float)(Aura.this.getScoreForAuraNormal(target1) - Aura.this.getScoreForAuraNormal(target2)));
            }
         });
         return targets;
      } catch (Exception var4) {
         var4.printStackTrace();
         return null;
      }
   }

   private void attack(EntityLivingBase entity, boolean crit) {
      ItemStack currentItem = Minecraft.thePlayer.inventory.getCurrentItem();
      if (autoBlock.getBooleanValue()) {
         Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
         unBlocked = true;
      }

      if (crit) {
         Criticals.critical();
      }

      Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
      Minecraft.thePlayer.swingItem();
      if (this.mc.playerController.currentGameType != WorldSettings.GameType.SPECTATOR) {
         Minecraft.thePlayer.attackTargetEntityWithCurrentItem(entity);
      }

      if (hvh.getBooleanValue()) {
         Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C08PacketPlayerBlockPlacement(Minecraft.thePlayer.inventory.getCurrentItem()));
      }

   }
}
