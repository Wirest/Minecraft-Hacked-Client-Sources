package rip.autumn.module.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.game.TickEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.ModuleManager;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.impl.movement.SpeedMod;
import rip.autumn.module.impl.player.FriendProtectMod;
import rip.autumn.module.impl.world.ScaffoldMod;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.utils.PlayerUtils;
import rip.autumn.utils.RotationUtils;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.entity.EntityValidator;
import rip.autumn.utils.entity.impl.AliveCheck;
import rip.autumn.utils.entity.impl.ConstantDistanceCheck;
import rip.autumn.utils.entity.impl.DistanceCheck;
import rip.autumn.utils.entity.impl.EntityCheck;
import rip.autumn.utils.entity.impl.TeamsCheck;

@Label("Aura")
@Bind("R")
@Category(ModuleCategory.COMBAT)
@Aliases({"aura", "killaura", "ka"})
public final class AuraMod extends Module {
   public static boolean blocking;
   private static final Random RANDOM = new Random();
   public final BoolOption autoBlock = new BoolOption("Auto Block", true);
   public final DoubleOption aps = new DoubleOption("APS", 13.0D, 1.0D, 20.0D, 1.0D);
   public final DoubleOption randomization = new DoubleOption("Randomization", 0.0D, 0.0D, 5.0D, 1.0D);
   public final EnumOption mode;
   public final EnumOption autoBlockMode;
   public final DoubleOption switchDelay;
   public final EnumOption sortingMode;
   public final DoubleOption range;
   public final BoolOption teams;
   public final BoolOption players;
   public final BoolOption animals;
   public final BoolOption monsters;
   public final BoolOption prioritizePlayers;
   public final BoolOption invisibles;
   public final BoolOption forceUpdate;
   public final BoolOption disableOnDeath;
   private final List targets;
   private final Stopwatch attackStopwatch;
   private final Stopwatch updateStopwatch;
   private final Stopwatch critStopwatch;
   private final EntityValidator entityValidator;
   private final EntityValidator blockValidator;
   private final double[] hypixelOffsets;
   private final double[] offsets;
   private int targetIndex;
   private boolean changeTarget;

   public AuraMod() {
      this.mode = new EnumOption("Mode", AuraMod.Mode.SWITCH);
      this.autoBlockMode = new EnumOption("Auto Block Mode", AuraMod.AutoBlockMode.OFFSET);
      this.switchDelay = new DoubleOption("Switch Delay", 3.0D, () -> {
         return this.mode.getValue() == AuraMod.Mode.SWITCH;
      }, 1.0D, 10.0D, 1.0D);
      this.sortingMode = new EnumOption("Sorting Mode", AuraMod.SortingMode.DISTANCE, () -> {
         return this.mode.getValue() == AuraMod.Mode.SINGLE;
      });
      this.range = new DoubleOption("Range", 4.2D, 3.0D, 7.0D, 0.1D);
      this.teams = new BoolOption("Teams", true);
      this.players = new BoolOption("Players", true);
      this.animals = new BoolOption("Animals", false);
      this.monsters = new BoolOption("Monsters", false);
      this.prioritizePlayers = new BoolOption("Prioritize Players", true, () -> {
         return (this.animals.getValue() || this.monsters.getValue()) && this.players.getValue();
      });
      this.invisibles = new BoolOption("Invisibles", false);
      this.forceUpdate = new BoolOption("Force Update", true);
      this.disableOnDeath = new BoolOption("Disable on death", true);
      this.targets = new ArrayList();
      this.attackStopwatch = new Stopwatch();
      this.updateStopwatch = new Stopwatch();
      this.critStopwatch = new Stopwatch();
      this.entityValidator = new EntityValidator();
      this.blockValidator = new EntityValidator();
      this.hypixelOffsets = new double[]{0.05000000074505806D, 0.0015999999595806003D, 0.029999999329447746D, 0.0015999999595806003D};
      this.offsets = new double[]{0.05D, 0.0D, 0.012511D, 0.0D};
      this.setMode(this.mode);
      this.addOptions(new Option[]{this.mode, this.sortingMode, this.autoBlockMode, this.aps, this.randomization, this.range, this.switchDelay, this.teams, this.players, this.prioritizePlayers, this.animals, this.monsters, this.invisibles, this.autoBlock, this.forceUpdate, this.disableOnDeath});
      AliveCheck aliveCheck = new AliveCheck();
      EntityCheck entityCheck = new EntityCheck(this.players, this.animals, this.monsters, this.invisibles, () -> {
         return ((FriendProtectMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(FriendProtectMod.class)).isEnabled();
      });
      TeamsCheck teamsCheck = new TeamsCheck(this.teams);
      this.entityValidator.add(aliveCheck);
      this.entityValidator.add(new DistanceCheck(this.range));
      this.entityValidator.add(entityCheck);
      this.entityValidator.add(teamsCheck);
      this.blockValidator.add(aliveCheck);
      this.blockValidator.add(new ConstantDistanceCheck(8.0F));
      this.blockValidator.add(entityCheck);
      this.blockValidator.add(teamsCheck);
   }

   public void onDisabled() {
      this.unblock();
   }

   public void onEnabled() {
      this.updateStopwatch.reset();
      this.critStopwatch.reset();
      this.targetIndex = 0;
      this.targets.clear();
      this.changeTarget = false;
   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      this.updateTargets();
      this.sortTargets();
      if (!PlayerUtils.isHoldingSword()) {
         blocking = false;
      }

      EntityLivingBase target = this.getTarget();
      if (target == null) {
         this.unblock();
      }

      if (event.isPre() && this.canAttack() && target != null) {
         if (this.updateStopwatch.elapsed(56L) && this.forceUpdate.getValue() && !mc.thePlayer.isMoving()) {
            mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
            this.updateStopwatch.reset();
         }

         float[] angles = RotationUtils.getRotationsEntity(target);
         event.setYaw(angles[0]);
         event.setPitch(angles[1]);
      }

   }

   @Listener(TickEvent.class)
   public final void onTick() {
      EntityLivingBase target = this.getTarget();
      if (target != null && this.canAttack()) {
         if (this.attackStopwatch.elapsed((long)(1000 / ((Double)this.aps.getValue()).intValue())) && this.canAttack()) {
            this.attack(target);
            this.attackStopwatch.reset();
         }

         if ((double)target.hurtTime >= (Double)this.switchDelay.getValue()) {
            this.changeTarget = true;
         }
      }

      this.block();
   }

   public final EntityLivingBase getTarget() {
      if (this.targets.isEmpty()) {
         return null;
      } else if (this.mode.getValue() == AuraMod.Mode.SINGLE) {
         return (EntityLivingBase)this.targets.get(0);
      } else {
         int size = this.targets.size();
         if (size >= this.targetIndex && this.changeTarget) {
            ++this.targetIndex;
            this.changeTarget = false;
         }

         if (size <= this.targetIndex) {
            this.targetIndex = 0;
         }

         return (EntityLivingBase)this.targets.get(this.targetIndex);
      }
   }

   private boolean isEntityNearby() {
      List loadedEntityList = mc.theWorld.loadedEntityList;
      int i = 0;

      for(int loadedEntityListSize = loadedEntityList.size(); i < loadedEntityListSize; ++i) {
         Entity entity = (Entity)loadedEntityList.get(i);
         if (this.blockValidator.validate(entity)) {
            return true;
         }
      }

      return false;
   }

   private void attack(EntityLivingBase entity) {
      EntityPlayerSP player = mc.thePlayer;
      NetHandlerPlayClient netHandler = mc.getNetHandler();
      ModuleManager mm = Autumn.MANAGER_REGISTRY.moduleManager;
      boolean hypixel = PlayerUtils.isOnHypixel();
      boolean shouldCritical = player.isCollidedVertically && player.onGround && ((CriticalsMod)mm.getModuleOrNull(CriticalsMod.class)).isEnabled() && !((SpeedMod)mm.getModuleOrNull(SpeedMod.class)).isEnabled() && this.critStopwatch.elapsed(200L) && entity.hurtTime <= 0;
      if (shouldCritical) {
         double[] var7 = hypixel ? this.hypixelOffsets : this.offsets;
         int var8 = var7.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            double offset = var7[var9];
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(player.posX, player.posY + offset, player.posZ, false));
         }

         this.critStopwatch.reset();
      }

      this.unblock();
      player.swingItem();
      netHandler.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
      if (this.autoBlockMode.getValue() == AuraMod.AutoBlockMode.OFFSET) {
         mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
      }

   }

   private void unblock() {
      if ((this.autoBlock.getValue() || mc.thePlayer.isBlocking()) && PlayerUtils.isHoldingSword()) {
         switch((AuraMod.AutoBlockMode)this.autoBlockMode.getValue()) {
         case OFFSET:
         case SMART:
            if (blocking) {
               mc.playerController.syncCurrentPlayItem();
               mc.getNetHandler().addToSendQueueSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
               blocking = false;
            }
         }
      }

   }

   private void block() {
      if (this.autoBlock.getValue() && PlayerUtils.isHoldingSword() && this.isEntityNearby()) {
         switch((AuraMod.AutoBlockMode)this.autoBlockMode.getValue()) {
         case OFFSET:
            mc.playerController.sendBlockSword(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
            blocking = true;
            break;
         case SMART:
            if (!blocking) {
               mc.playerController.syncCurrentPlayItem();
               mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
               blocking = true;
            }
         }
      }

   }

   private boolean canAttack() {
      return !ScaffoldMod.getInstance().isEnabled();
   }

   private void updateTargets() {
      this.targets.clear();
      List entities = mc.theWorld.loadedEntityList;
      int i = 0;

      for(int entitiesSize = entities.size(); i < entitiesSize; ++i) {
         Entity entity = (Entity)entities.get(i);
         if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            if (this.entityValidator.validate(entityLivingBase)) {
               this.targets.add(entityLivingBase);
            }
         }
      }

   }

   private void sortTargets() {
      switch((AuraMod.SortingMode)this.sortingMode.getValue()) {
      case HEALTH:
         this.targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
         break;
      case DISTANCE:
         this.targets.sort(Comparator.comparingDouble((entity) -> {
            return (double)mc.thePlayer.getDistanceToEntity(entity);
         }));
      }

      if (this.prioritizePlayers.getValue()) {
         this.targets.sort(Comparator.comparing((entity) -> {
            return entity instanceof EntityPlayer;
         }));
      }

   }

   private static enum SortingMode {
      DISTANCE,
      HEALTH;
   }

   private static enum AutoBlockMode {
      SMART,
      OFFSET;
   }

   private static enum Mode {
      SINGLE,
      SWITCH;
   }
}
