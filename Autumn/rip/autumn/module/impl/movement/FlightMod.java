package rip.autumn.module.impl.movement;

import java.util.ArrayDeque;
import java.util.ArrayList;
import me.zane.basicbus.api.annotations.Listener;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0CPacketInput;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.Timer;
import rip.autumn.annotations.Label;
import rip.autumn.core.Autumn;
import rip.autumn.events.packet.ReceivePacketEvent;
import rip.autumn.events.packet.SendPacketEvent;
import rip.autumn.events.player.MotionUpdateEvent;
import rip.autumn.events.player.MoveEvent;
import rip.autumn.module.Module;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.annotations.Aliases;
import rip.autumn.module.annotations.Bind;
import rip.autumn.module.annotations.Category;
import rip.autumn.module.option.Option;
import rip.autumn.module.option.impl.BoolOption;
import rip.autumn.module.option.impl.DoubleOption;
import rip.autumn.module.option.impl.EnumOption;
import rip.autumn.notification.NotificationPublisher;
import rip.autumn.notification.NotificationType;
import rip.autumn.utils.MathUtils;
import rip.autumn.utils.MovementUtils;
import rip.autumn.utils.PlayerUtils;
import rip.autumn.utils.Stopwatch;
import rip.autumn.utils.pathfinding.CustomVec3;
import rip.autumn.utils.pathfinding.PathfindingUtils;

@Bind("F")
@Label("Flight")
@Category(ModuleCategory.MOVEMENT)
@Aliases({"flight", "fly"})
public final class FlightMod extends Module {
   public final EnumOption mode;
   public final BoolOption viewBobbing;
   public final BoolOption multiplier;
   public final DoubleOption multiplyspeed;
   public final DoubleOption multiplytime;
   public final DoubleOption speed;
   public final DoubleOption vanillaSpeed;
   private final ArrayDeque arrayDeque;
   private final Stopwatch stopwatch;
   private int ticks;
   private int stage;
   private CustomVec3 lastPos;
   private boolean tp;
   private double lastDist;
   private double moveSpeed;
   private TargetStrafeMod targetStrafe;
   private double y;

   public FlightMod() {
      this.mode = new EnumOption("Mode", FlightMod.Mode.WATCHDOG);
      this.viewBobbing = new BoolOption("View Bobbing", true);
      this.multiplier = new BoolOption("Multiplier", false, () -> {
         return this.mode.getValue() == FlightMod.Mode.WATCHDOG;
      });
      this.multiplyspeed = new DoubleOption("Multiply Speed", 1.8D, () -> {
         return this.mode.getValue() == FlightMod.Mode.WATCHDOG;
      }, 0.1D, 3.0D, 0.05D);
      this.multiplytime = new DoubleOption("Multiply Time", 1190.0D, () -> {
         return this.mode.getValue() == FlightMod.Mode.WATCHDOG;
      }, 100.0D, 3000.0D, 50.0D);
      this.speed = new DoubleOption("Watchdog Speed", 1.0D, () -> {
         return this.mode.getValue() == FlightMod.Mode.WATCHDOG;
      }, 0.5D, 1.0D, 0.05D);
      this.vanillaSpeed = new DoubleOption("Vanilla Speed", 5.0D, () -> {
         return this.mode.getValue() == FlightMod.Mode.VANILLA;
      }, 0.1D, 7.0D, 0.1D);
      this.arrayDeque = new ArrayDeque();
      this.stopwatch = new Stopwatch();
      this.setMode(this.mode);
      this.addOptions(new Option[]{this.mode, this.speed, this.vanillaSpeed, this.multiplyspeed, this.multiplytime, this.viewBobbing, this.multiplier});
   }

   public static FlightMod getInstance() {
      return (FlightMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(FlightMod.class);
   }

   @Listener(SendPacketEvent.class)
   public final void onSendPacket(SendPacketEvent event) {
      if (this.mode.getValue() == FlightMod.Mode.WATCHDOG && this.stage == 0) {
         event.setCancelled();
      }

      if (this.mode.getValue() == FlightMod.Mode.MINEPLEX && !this.stopwatch.elapsed(6100L)) {
         event.setCancelled();
      }

      if (this.mode.getValue() == FlightMod.Mode.DISABLER && event.getPacket() instanceof C03PacketPlayer && !this.tp) {
         event.setCancelled();
      }

   }

   @Listener(ReceivePacketEvent.class)
   public final void onReceivePacket(ReceivePacketEvent event) {
      if (this.mode.getValue() == FlightMod.Mode.MINEPLEX) {
         if ((event.getPacket() instanceof S02PacketChat || event.getPacket() instanceof S45PacketTitle) && !this.stopwatch.elapsed(6000L)) {
            event.setCancelled();
         }
      } else if (this.mode.getValue() == FlightMod.Mode.WATCHDOG) {
         if (event.getPacket() instanceof S08PacketPlayerPosLook) {
            NotificationPublisher.queue("Flag", "Disabled Flight due to flag.", NotificationType.INFO);
            this.toggle();
         }
      } else if (this.mode.getValue() == FlightMod.Mode.DISABLER && event.getPacket() instanceof S08PacketPlayerPosLook) {
         NotificationPublisher.queue("Teleported", "Teleported after lagback.", NotificationType.INFO);
         if (!this.tp) {
            this.tp = true;
         }
      }

   }

   private void mineplexDamage(EntityPlayerSP playerRef) {
      NetHandlerPlayClient netHandler = mc.getNetHandler();
      double offset = 0.060100000351667404D;

      for(int i = 0; i < 20; ++i) {
         for(int j = 0; (double)j < (double)PlayerUtils.getMaxFallDist() / 0.060100000351667404D + 1.0D; ++j) {
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.060100000351667404D, mc.thePlayer.posZ, false));
            netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 5.000000237487257E-4D, mc.thePlayer.posZ, false));
         }
      }

      netHandler.addToSendQueueSilent(new C03PacketPlayer(true));
   }

   public void onEnabled() {
      this.arrayDeque.clear();
      this.tp = false;
      this.lastPos = new CustomVec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
      EntityPlayerSP player = mc.thePlayer;
      if (this.mode.getValue() == FlightMod.Mode.MINEPLEX) {
         this.mineplexDamage(player);
      }

      if (this.mode.getValue() == FlightMod.Mode.DISABLER) {
         mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
         mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.18D, mc.thePlayer.posZ, true));
         mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.08D, mc.thePlayer.posZ, true));
      }

      this.stopwatch.reset();
      this.y = 0.0D;
      this.lastDist = 0.0D;
      this.moveSpeed = 0.0D;
      this.stage = 0;
      this.ticks = 0;
      player.stepHeight = 0.0F;
      player.motionX = 0.0D;
      player.motionZ = 0.0D;
      if (this.targetStrafe == null) {
         this.targetStrafe = (TargetStrafeMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(TargetStrafeMod.class);
      }

   }

   public void onDisabled() {
      EntityPlayerSP player = mc.thePlayer;
      mc.timer.timerSpeed = 1.0F;
      player.stepHeight = 0.625F;
      player.motionX = 0.0D;
      player.motionZ = 0.0D;
      if (this.mode.getValue() == FlightMod.Mode.WATCHDOG) {
         player.setPosition(player.posX, player.posY + this.y, player.posZ);
      }

   }

   @Listener(MoveEvent.class)
   public final void onMove(MoveEvent event) {
      EntityPlayerSP player = mc.thePlayer;
      GameSettings gameSettings = mc.gameSettings;
      switch((FlightMod.Mode)this.mode.getValue()) {
      case WATCHDOG:
         if (player.isMoving()) {
            switch(this.stage) {
            case 0:
               if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                  PlayerUtils.damage();
                  this.moveSpeed = 0.5D * (Double)this.speed.getValue();
               }
               break;
            case 1:
               if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                  event.y = player.motionY = MovementUtils.getJumpBoostModifier(0.39999994D);
               }

               this.moveSpeed *= 2.149D;
               break;
            case 2:
               this.moveSpeed = 1.3D * (Double)this.speed.getValue();
               break;
            default:
               this.moveSpeed = this.lastDist - this.lastDist / 159.0D;
            }

            MovementUtils.setSpeed(event, Math.max(this.moveSpeed, MovementUtils.getBaseMoveSpeed()));
            ++this.stage;
         }
         break;
      case MINEPLEX:
         event.y = player.motionY = 0.0D;
         if (this.stopwatch.elapsed(6100L)) {
            MovementUtils.setSpeed(event, 6.0D);
            if (gameSettings.keyBindJump.isKeyDown()) {
               event.y = player.motionY = 1.0D;
            }

            if (gameSettings.keyBindSneak.isKeyDown()) {
               event.y = player.motionY = -1.0D;
            }
         } else {
            MovementUtils.setSpeed(event, 0.0D);
         }

         if (this.stopwatch.elapsed(7000L)) {
            MovementUtils.setSpeed(event, 0.0D);
            this.mineplexDamage(player);
            this.stopwatch.reset();
         }
         break;
      case VANILLA:
         MovementUtils.setSpeed(event, (double)((Double)this.vanillaSpeed.getValue()).intValue());
         break;
      case DISABLER:
         event.y = mc.thePlayer.motionY = 0.0D;
         if (this.tp) {
            if (gameSettings.keyBindJump.isKeyDown()) {
               event.y = mc.thePlayer.motionY = 2.0D;
            } else if (gameSettings.keyBindSneak.isKeyDown()) {
               event.y = mc.thePlayer.motionY = -2.0D;
            }

            MovementUtils.setSpeed(event, 2.0D);
         } else {
            MovementUtils.setSpeed(event, 0.0D);
         }
         break;
      case CUBECRAFT:
         mc.timer.timerSpeed = 1.0F;
         MovementUtils.setSpeed(event, 0.0D);
         if (this.ticks > 12) {
            if (this.stage == 4) {
               MovementUtils.setSpeed(event, 0.953532D);
               this.stage = 0;
            } else {
               MovementUtils.setSpeed(event, 0.121984218421847D);
            }
         } else {
            MovementUtils.setSpeed(event, 0.0D);
         }
      }

   }

   @Listener(MotionUpdateEvent.class)
   public final void onMotionUpdate(MotionUpdateEvent event) {
      EntityPlayerSP player = mc.thePlayer;
      Timer timer = mc.timer;
      GameSettings gameSettings = mc.gameSettings;
      Stopwatch stopwatch = this.stopwatch;
      double offset;
      switch((FlightMod.Mode)this.mode.getValue()) {
      case WATCHDOG:
         if (this.multiplier.getValue()) {
            if (!stopwatch.elapsed(((Double)this.multiplytime.getValue()).longValue())) {
               mc.timer.timerSpeed = ((Double)this.multiplyspeed.getValue()).floatValue();
            } else {
               mc.timer.timerSpeed = 1.0F;
            }
         }

         if (event.isPre()) {
            if (this.stage > 2) {
               player.motionY = 0.0D;
            }

            if (this.viewBobbing.getValue()) {
               player.cameraYaw = 0.105F;
            }

            if (this.stage > 2) {
               player.setPosition(player.posX, player.posY - 0.003D, player.posZ);
               ++this.ticks;
               offset = 3.25E-4D;
               switch(this.ticks) {
               case 1:
                  this.y *= -0.949999988079071D;
                  break;
               case 2:
               case 3:
               case 4:
                  this.y += 3.25E-4D;
                  break;
               case 5:
                  this.y += 5.0E-4D;
                  this.ticks = 0;
               }

               event.setPosY(player.posY + this.y);
            }
         } else if (this.stage > 2) {
            player.setPosition(player.posX, player.posY + 0.003D, player.posZ);
         }
         break;
      case MINEPLEX:
         if (player.hurtTime == 9) {
            stopwatch.reset();
         }

         if (stopwatch.elapsed(6000L) && !this.tp) {
            NetHandlerPlayClient netHandler = mc.getNetHandler();
            netHandler.addToSendQueueSilent(new C0CPacketInput(0.0F, 0.0F, true, true));
            CustomVec3 lastPos = this.lastPos;
            ArrayList computePath = PathfindingUtils.computePath(new CustomVec3(player.posX, player.posY, player.posZ), lastPos);
            int i = 0;

            for(int computePathSize = computePath.size(); i < computePathSize; ++i) {
               CustomVec3 vec3 = (CustomVec3)computePath.get(i);
               netHandler.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(vec3.getX(), vec3.getY(), vec3.getZ(), true));
            }

            NotificationPublisher.queue("Flight", "Exploit completed.", NotificationType.SUCCESS);
            mc.thePlayer.setPosition(lastPos.getX(), lastPos.getY(), lastPos.getZ());
            this.tp = true;
         }
         break;
      case VANILLA:
         if (event.isPre()) {
            player.motionY = 0.0D;
            if (gameSettings.keyBindJump.isKeyDown()) {
               player.motionY = 2.0D;
            } else if (gameSettings.keyBindSneak.isKeyDown()) {
               player.motionY = -2.0D;
            }
         }
      case DISABLER:
      default:
         break;
      case CUBECRAFT:
         if (event.isPre()) {
            timer.timerSpeed = 0.82F;
            ++this.ticks;
            float y = (float)Math.floor(player.posY);
            if (this.ticks == 1 && !player.onGround) {
               this.toggle();
            }

            player.motionY = 0.0D;
            if (this.ticks < 10) {
               event.setPosY((double)y - 1.01D);
               player.motionY = -0.101D;
            } else if (this.ticks > 12) {
               ++this.stage;
               switch(this.stage) {
               case 1:
                  event.setPosY((double)y + 0.381D + MathUtils.randomNumber(0.03D, 0.05D));
                  break;
               case 2:
                  event.setPosY((double)y + 0.355D + MathUtils.randomNumber(0.03D, 0.05D));
                  break;
               case 3:
                  event.setPosY((double)y + 0.325D + MathUtils.randomNumber(0.03D, 0.05D));
                  break;
               case 4:
                  event.setPosY((double)y + MathUtils.randomNumber(0.03D, 0.05D));
               }
            }
         }
      }

      if (event.isPre()) {
         offset = player.posX - player.prevPosX;
         double zDif = player.posZ - player.prevPosZ;
         this.lastDist = Math.sqrt(offset * offset + zDif * zDif);
      }

   }

   public static enum Mode {
      WATCHDOG,
      DISABLER,
      CUBECRAFT,
      MINEPLEX,
      VANILLA;
   }
}
