package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.io.PrintStream;
import java.util.ArrayList;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventReceivePacket;
import me.slowly.client.events.EventSendPacket;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import org.lwjgl.input.Keyboard;

public class Fly
extends Mod {
    private boolean canbestart = false;
    public static Value<String> mode = new Value("Fly", "Mode", 0);
    private Value<Double> glide = new Value<Double>("Fly_Glide", 0.05, 0.0, 3.0, 0.01);
    private Value<Double> guardian = new Value<Double>("Fly_Guardian", 2.0, 1.0, 8.0, 1.0);
    private Value<Double> aacglide = new Value<Double>("Fly_AAC FallDistance", 0.21, 0.00, 15.00, 0.01);
    private Value<Double> aacglidetimer = new Value<Double>("Fly_AAC Glide Timer", 0.21, 0.01, 1.00, 0.01);
    private int delay = 3;
    private TimeHelper timer = new TimeHelper();
    private double alpha;
    private int p;
    private double y;
	private int counter;
    Timer kickTimer = new Timer();
	private double flyHeight;
    public Fly() {
        super("Fly", Mod.Category.MOVEMENT, 2719929);
        Fly.mode.mode.add("Vanilla");
        Fly.mode.mode.add("Motion");
        Fly.mode.mode.add("AntiKick");
        Fly.mode.mode.add("AAC3.1.6");
        Fly.mode.mode.add("AAC3.2.0");
        Fly.mode.mode.add("AAC1.9.10");
        Fly.mode.mode.add("AAC3.0.5");
        Fly.mode.mode.add("Hypixel");
        Fly.mode.mode.add("Glide");
        Fly.mode.mode.add("Test");
        Fly.mode.mode.add("Guardian");
        Fly.mode.mode.add("Omegacraft");
        Fly.mode.mode.add("CubeCraft");
        Fly.mode.mode.add("CubeCraft2");
        Fly.mode.mode.add("CubeCraft3");
        Fly.mode.mode.add("MCGamster");
        Fly.mode.mode.add("oldMineplex");
        Fly.mode.mode.add("Rewi");
        Fly.mode.mode.add("Gomme");
        Fly.mode.mode.add("AirJump");
        Fly.mode.mode.add("AAC3315");
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        this.showValue = mode;
        if (this.mc.thePlayer.hurtTime < 9) {
            this.mc.thePlayer.hurtTime = 0;
        }
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.showValue = mode;
        this.mc.thePlayer.capabilities.isFlying = false;
        if (mode.isCurrentMode("Vanilla")) {
            this.mc.thePlayer.capabilities.isFlying = true;
        } else if (mode.isCurrentMode("Motion")) {
            this.mc.thePlayer.onGround = true;
            this.mc.thePlayer.motionY = 0.0;
            if (this.mc.gameSettings.keyBindForward.isKeyDown() || this.mc.gameSettings.keyBindLeft.isKeyDown() || this.mc.gameSettings.keyBindRight.isKeyDown() || this.mc.gameSettings.keyBindBack.isKeyDown()) {
                PlayerUtil.setSpeed(1.0);
            }
            if (this.mc.gameSettings.keyBindSneak.pressed) {
                this.mc.thePlayer.motionY -= 0.5;
            } else if (this.mc.gameSettings.keyBindJump.pressed) {
                this.mc.thePlayer.motionY += 0.5;
            }
        } else if (mode.isCurrentMode("AntiKick")) {
            if (mc.thePlayer.movementInput.jump) {
                mc.thePlayer.motionY = (PlayerUtil.getSpeed() * 0.6D);
              } else if (mc.thePlayer.movementInput.sneak) {
                mc.thePlayer.motionY = (-PlayerUtil.getSpeed() * 0.6D);
              } else {
                mc.thePlayer.motionY = 0.0D;
              }
              updateFlyHeight();
              mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
              if (((this.flyHeight <= 290.0D) && (this.kickTimer.delay(500.0F))) || ((this.flyHeight > 290.0D) && 
                (this.kickTimer.delay(100.0F))))
              {
                goToGround();
                this.kickTimer.reset();
              }
        } else if (mode.isCurrentMode("AAC1.9.10")) {
            if ((double)this.mc.thePlayer.fallDistance > 3.5) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                this.mc.thePlayer.motionY += 1.59;
                this.mc.thePlayer.fallDistance = 0.0f;
                if (PlayerUtil.MovementInput()) {
                    PlayerUtil.toFwd(0.07);
                }
            }
            
        } else if (mode.isCurrentMode("AAC3.2.0")) {
        	mc.thePlayer.motionY = 0;
        	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 11,
	                mc.thePlayer.posY + (mc.gameSettings.keyBindJump.pressed ? 0.0625
	                        : mc.gameSettings.keyBindSneak.pressed ? -0.0625 : 0),
	                mc.thePlayer.posZ + mc.thePlayer.motionZ * 11, false));
        	mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + mc.thePlayer.motionX * 11,
	                mc.theWorld.getHeight(),
	                mc.thePlayer.posZ + mc.thePlayer.motionZ * 11, false));
        } else if (mode.isCurrentMode("AAC3.1.6")) {
        	mc.thePlayer.motionY = 0.0F;
       	if(mc.gameSettings.keyBindJump.pressed){
       		mc.thePlayer.motionY = +0.1f;
       	}else if (mc.gameSettings.keyBindSneak.pressed){
       		mc.thePlayer.motionY = -0.1f;
       		}
    	mc.thePlayer.motionY = -0.1f;

        } else if (mode.isCurrentMode("AAC3.0.5")) {
            if (this.delay == 2) {
                this.mc.thePlayer.motionY = 0.075;
            } else if (this.delay > 2) {
                this.delay = 0;
            }
            ++this.delay;
        } else if(this.mode.isCurrentMode("Hypixel")) {
            mc.thePlayer.onGround = false;
            mc.thePlayer.capabilities.isFlying = false;
            if(this.mc.gameSettings.keyBindSneak.isKeyDown()) {
               mc.thePlayer.motionY *= 0.0D;
            } else if(this.mc.gameSettings.keyBindJump.isKeyDown()) {
               mc.thePlayer.motionY *= 0.0D;
            }

            if(PlayerUtil.MovementInput()) {
               this.mc.thePlayer.setSpeed(PlayerUtil.getSpeed() - 0.05D);
            } else {
            	this.mc.thePlayer.setSpeed(PlayerUtil.getSpeed());
            }

            ++this.counter;
            if(PlayerUtil.MovementInput()) {
            	this.mc.thePlayer.setSpeed(PlayerUtil.getSpeed());
            } else {
               mc.thePlayer.motionX *= 0.0D;
               mc.thePlayer.motionZ *= 0.0D;
               this.mc.timer.timerSpeed = 1.0F;
            }

            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
            switch(this.counter) {
            case 1:
            default:
               break;
            case 2:
               mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-5D, mc.thePlayer.posZ);
               this.counter = 0;
               break;
            case 3:
               mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 1.0E-5D, mc.thePlayer.posZ);
               this.counter = 0;
            }

            mc.thePlayer.motionY = mc.thePlayer.motionY = 0.0D;
        } else if (mode.isCurrentMode("Glide")) {
            this.mc.thePlayer.onGround = true;
            this.mc.thePlayer.motionY = (- this.glide.getValueState().doubleValue()) / 6.0;
        } else if (mode.isCurrentMode("Test")) {
            ++this.p;
            if (this.mc.gameSettings.keyBindSneak.isPressed()) {
                this.p = 0;
            }
            if (this.p == 0) {
                this.p = 1;
                this.mc.thePlayer.motionY = -3.3;
            } else if (this.p == 1) {
                this.p = 2;
            } else if (this.p == 2) {
                System.out.println("HI");
                event.y -= 182.13708;
                this.mc.thePlayer.motionY = 8.0;
            }
        } else if (mode.isCurrentMode("Guardian")) {
            if ((double)this.mc.thePlayer.fallDistance > 1.5 && PlayerUtil.MovementInput()) {
                if (PlayerUtil.MovementInput()) {
                    PlayerUtil.toFwd(this.guardian.getValueState());
                }
                this.mc.thePlayer.motionY = 0.49;
                this.mc.thePlayer.fallDistance = 0.0f;
            } else if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionY = 0.0;
            }
            if (this.mc.gameSettings.keyBindJump.pressed && !PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionY = 3.0;
            }
            if (this.mc.gameSettings.keyBindSneak.pressed && !PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionY = -3.0;
            }
            if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        } else if (mode.isCurrentMode("Omegacraft")) {
            event.onGround = true;
            double val = 150.0;
            event.y = 2.147483647E9;
            this.mc.thePlayer.motionY = 0.2;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + this.mc.thePlayer.motionX * val, this.mc.thePlayer.prevPosY, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ * val, true));
            if (this.mc.thePlayer.ticksExisted % 3 == 0) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + this.mc.thePlayer.motionX * val, -2.147483648E9, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ * val, true));
            }
        } else if (mode.isCurrentMode("AAC3315")) {
            if (mc.thePlayer.fallDistance >= aacglide.getValueState()) {
                mc.thePlayer.motionY = 0;	
                mc.timer.timerSpeed = aacglidetimer.getValueState();
                mc.thePlayer.fallDistance = 0.0F;
                PlayerUtil.setSpeed(PlayerUtil.getSpeed()); 
            }  {
            }
        } else if (mode.isCurrentMode("Cubecraft")) {
            this.mc.timer.timerSpeed = 0.2f;
            event.onGround = true;
            this.mc.thePlayer.onGround = true;
            this.mc.thePlayer.motionY = -0.3;
            if (PlayerUtil.MovementInput()) {
                PlayerUtil.setSpeed(1.0);
            }
            if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        } else if (mode.isCurrentMode("MCGamster")) {
            this.mc.thePlayer.motionY = 0.0;
            if (PlayerUtil.MovementInput()) {
                PlayerUtil.setSpeed(8.0);
            } else {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        } else if (mode.isCurrentMode("oldMineplex")) {
            if (this.mc.thePlayer.onGround) {
                this.y = this.mc.thePlayer.posY;
            }
            if (this.mc.thePlayer.inventory.getCurrentItem() == null) {
                if (this.mc.gameSettings.keyBindJump.isKeyDown() && this.timer.isDelayComplete(100L)) {
                    this.timer.reset();
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.6, this.mc.thePlayer.posZ);
                    this.y += 0.6;
                }
                if (this.mc.gameSettings.keyBindSneak.isKeyDown() && this.timer.isDelayComplete(100L)) {
                    this.timer.reset();
                    this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.6, this.mc.thePlayer.posZ);
                    this.y -= 0.6;
                }
                this.alpha += 0.1;
                if (Keyboard.isKeyDown((int)78)) {
                    this.mc.timer.timerSpeed = 20.0f;
                } else {
                    this.mc.thePlayer.motionZ = 0.0;
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.timer.timerSpeed = 1.0f;
                }
                float f = PlayerUtil.getDirection();
                this.mc.thePlayer.motionX = - (double)(MathHelper.sin(f) * 0.4f);
                this.mc.thePlayer.motionZ = MathHelper.cos(f) * 0.4f;
                this.mc.thePlayer.posY = this.y + Math.sin(this.alpha * 15.0) / 15.0;
                this.mc.thePlayer.onGround = true;
                this.mc.thePlayer.motionY = 0.0;
                BlockPos underP = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.getEntityBoundingBox().minY - 1.0, this.mc.thePlayer.posZ);
                Vec3 vec = new Vec3(underP).addVector(0.4000000059604645, 0.4000000059604645, 0.4000000059604645).add(new Vec3(EnumFacing.UP.getDirectionVec()).multi(0.4000000059604645));
                this.mc.playerController.onPlayerRightClick(this.mc.thePlayer, this.mc.theWorld, this.mc.thePlayer.inventory.getCurrentItem(), underP, EnumFacing.UP, vec);
            } else {
                this.mc.timer.timerSpeed = 1.0f;
            }
        } else if (mode.isCurrentMode("CubeCraft2")) {
            this.mc.timer.timerSpeed = 0.21f;
            this.mc.thePlayer.motionY = 0.0;
            if (PlayerUtil.MovementInput()) {
                this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX - Math.sin(PlayerUtil.getDirection()) * 3.0, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + Math.cos(PlayerUtil.getDirection()) * 3.0);
            }
        } else if (mode.isCurrentMode("CubeCraft3")) {
            float speed = (float)PlayerUtil.getSpeed();
            double blocks = 1.0;
            this.mc.thePlayer.motionY = -0.4;
            this.mc.timer.timerSpeed = 0.3f;
            float x = (float)(- (double)MathHelper.sin(PlayerUtil.getDirection()) * blocks);
            float z = (float)((double)MathHelper.cos(PlayerUtil.getDirection()) * blocks);
            this.mc.thePlayer.setPositionAndUpdate(this.mc.thePlayer.posX + (double)x, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + (double)z);
        } else if (mode.isCurrentMode("Rewi")) {
            double val = 400.0;
            this.mc.thePlayer.motionY = 0.200000003;
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + this.mc.thePlayer.motionX * val, this.mc.thePlayer.prevPosY, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ * val, true));
            if (this.mc.thePlayer.ticksExisted % 3 == 0) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX + this.mc.thePlayer.motionX * val, -2.147483648E9, this.mc.thePlayer.posZ + this.mc.thePlayer.motionZ * val, true));
            }
        } else if (mode.isCurrentMode("Gomme")) {
            if (this.mc.thePlayer.fallDistance > 180.0f) {
                this.mc.thePlayer.motionY = 9.0;
                this.mc.thePlayer.fallDistance = 0.0f;
            }
            this.mc.timer.timerSpeed = this.mc.thePlayer.motionY > 0.42 ? 0.4f : 1.0f;
        } else if (mode.isCurrentMode("AirJump")) {
            this.mc.thePlayer.onGround = true;
        }
        }
    

        public void updateFlyHeight()
        {
          double h = 1.0D;
          AxisAlignedBB box = mc.thePlayer.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
          for (this.flyHeight = 0.0D; this.flyHeight < mc.thePlayer.posY; this.flyHeight += h)
          {
            AxisAlignedBB nextBox = box.offset(0.0D, -this.flyHeight, 0.0D);
            if (mc.theWorld.checkBlockCollision(nextBox))
            {
              if (h < 0.0625D) {
                break;
              }
              this.flyHeight -= h;
              h /= 2.0D;
            }
          }
        }
        public void goToGround()
        {
          if (this.flyHeight > 300.0D) {
            return;
          }
          double minY = mc.thePlayer.posY - this.flyHeight;
          if (minY <= 0.0D) {
            return;
          }
          for (double y = mc.thePlayer.posY; y > minY;)
          {
            y -= 8.0D;
            if (y < minY) {
              y = minY;
            }
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
            
            mc.thePlayer.sendQueue.addToSendQueue(packet);
          }
          for (double y = minY; y < mc.thePlayer.posY;)
          {
            y += 8.0D;
            if (y > mc.thePlayer.posY) {
              y = mc.thePlayer.posY;
            }
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, y, mc.thePlayer.posZ, true);
            
            mc.thePlayer.sendQueue.addToSendQueue(packet);
          }
        }
	@EventTarget
    public void onPacket(EventReceivePacket event) {
        if (mode.isCurrentMode("AAC1.9.10") && event.getPacket() instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity packet = (S12PacketEntityVelocity)event.getPacket();
            if (packet.entityID == this.mc.thePlayer.getEntityId()) {
                event.cancel = true;
            }
        }
    }

    @EventTarget
    public void onPacketSend(EventSendPacket event) {
    }

    @Override
    public void onEnable() {
        super.onEnable();
        this.alpha = 0.0;
        this.y = this.mc.thePlayer.posY;
        ClientUtil.sendClientMessage("Fly Enable", ClientNotification.Type.SUCCESS);
        this.delay = 1;
    }

    @Override
    public void onDisable() {
        this.y = this.mc.thePlayer.posY;
        this.alpha = 0.0;
        this.delay = 1;
        super.onDisable();
        if (PlayerUtil.MovementInput()) {
            PlayerUtil.setSpeed(0.0);
        }
        this.canbestart = false;
        this.mc.thePlayer.capabilities.isFlying = false;
        this.mc.timer.timerSpeed = 1.0f;
        ClientUtil.sendClientMessage("Fly Disable", ClientNotification.Type.ERROR);
    }
}

