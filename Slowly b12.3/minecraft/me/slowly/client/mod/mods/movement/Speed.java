/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.slowly.client.mod.mods.movement;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.UpdateEvent;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.PlayerUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.value.Value;
import net.minecraft.block.BlockSlab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

public class Speed
extends Mod {
    TimeHelper ticks = new TimeHelper();
    TimeHelper other = new TimeHelper();
    public static Value<String> mode = new Value("Speed", "Mode", 0);
    private Value<Double> AAC = new Value<Double>("Speed_FASTAAC", 3.0, 0.0, 6.0, 0.1);
    private Value<Double> dac = new Value<Double>("Speed_DACSpeed", 2.0, 0.0, 3.5, 0.1);
    private Value<Double> racespeed = new Value<Double>("Speed_RaceSpeed", 0.5, 0.0, 2.0, 0.05);
    private Value<Double> CustomY = new Value<Double>("Speed_CustomY", 0.41, 0.000, 2.0, 0.001);
    private Value<Double> customtimer = new Value<Double>("Speed_CustomTimer", 1.0, 0.00, 15.0, 0.01);
    private Value<Double> CustomSpeedFinal = new Value<Double>("Speed_CustomSpeed", 0.0006, 0.0000, 1.0000, 0.0001);
    private double posY;
    private int tick;
    private double moveSpeed;
    private int speedTick;
    private boolean firstjump;
    private boolean legitHop = false;

    public Speed() {
        super("Speed", Mod.Category.MOVEMENT, 2600544);
        Speed.mode.mode.add("AAC");
        Speed.mode.mode.add("Custom");
        Speed.mode.mode.add("FASTAAC3.3.11");
        Speed.mode.mode.add("AACPort");
        Speed.mode.mode.add("AAC3.3.10");
        Speed.mode.mode.add("PacketAAC");
        Speed.mode.mode.add("AACLowhop3.3.9");
        Speed.mode.mode.add("AACYport3.3.1");
        Speed.mode.mode.add("AAC3.1.5");
        Speed.mode.mode.add("BAC");
        Speed.mode.mode.add("Motion");
        Speed.mode.mode.add("NCP");
        Speed.mode.mode.add("NCP-YPort");
        Speed.mode.mode.add("Race");
        Speed.mode.mode.add("MineSecure");
        Speed.mode.mode.add("DAC");
        Speed.mode.mode.add("Rewinside");
        Speed.mode.mode.add("SpectreLowHop");
        Speed.mode.mode.add("SpectreBhop");
        Speed.mode.mode.add("Lowhop");
        this.showValue = mode;
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
        this.mc.thePlayer.cameraPitch = 0.0f;
    }

    @EventTarget
    public void onPreMotion(EventPreMotion event) {
        double[] posY = new double[]{0.0, 0.3875000000000002, 0.6743500058650973, 0.8625630155582433, 0.9541117671215256, 0.9541117671215256, 0.8612117655956464, 0.6772697608024796, 0.40410659107088165, 0.04350667799786301};
        double[] speed = new double[]{0.20149000298023223, 0.18106395212314025, 0.18944500042887916, 0.1970717546069033, 0.20401210110892484, 0.2103278166077823, 0.21607511787737882, 0.22130516218344062, 0.22606450263912023, 0.23039550257860736, 0.43433671561735787, 0.30819827198999616, 0.30513723484194827, 0.3023516909569458, 0.29981684594853975, 0.2975101369244113, 0.29541103165195853, 0.2935008457989753, 0.2917625766226639, 0.29018075162663265, 0.48874129381899145, 0.3379031751383907, 0.3321686974860294, 0.3269503226719879, 0.3222016014543529, 0.3178802650217648, 0.3139478487547781, 0.31036934984868847, 0.30711291575029703, 0.3041495606353575, 0.5014529103832777, 0.3448437185886533, 0.33848459220779137, 0.33269778703443226, 0.3274317941749105, 0.3226397405346395, 0.3182789715963163, 0.3143106717480765, 0.3106995187821054, 0.30741336948836545, 0.5044229765251116, 0.34646537489045426, 0.33996029948495987, 0.3340406806953576, 0.3286538274415713, 0.3237517908393499, 0.3192909374027674, 0.31523156065848684, 0.31153752771473014, 0.30817595763903166, 0.5051169317622176, 0.34684427449392435, 0.3403050981340547, 0.33435444747507653, 0.3289393552193444, 0.32401162112461174, 0.31952738296917005, 0.31544672613011426, 0.311733328299554, 0.3083541361763565, 0.5052790742358562, 0.34693280429481393, 0.34038566025518596, 0.33442775900741883, 0.3290060687156986, 0.3240723304080437, 0.31958262841868523, 0.31549699949062193, 0.3117790770589345, 0.3083957675485925, 0.5053169587856827, 0.3469534892614219, 0.3404044835753417, 0.3344448882292542, 0.329021656308018, 0.3240865151174631, 0.31959553650462896, 0.31550874584916927, 0.3117897662455206, 0.30840549470866624, 0.505325810501605, 0.3469583222988768, 0.3404088816395524, 0.3344488904678013, 0.3290252983452008, 0.32408982937139497, 0.31959855247579383, 0.3155114903830084, 0.3117922637713862, 0.3084077674572694, 0.5053278787028934, 0.34695945153691143, 0.3404099092461936, 0.33444982558987174, 0.3290261493063094, 0.32409060374602616, 0.31959925715672854, 0.3155121316426775, 0.3117928473177019, 0.30840829848443196, 0.5053283619376252, 0.34695971538310566, 0.3404101493462372, 0.3344500440809177, 0.329026348133167, 0.32409078467847174, 0.3195994218052588, 0.3155122814728443, 0.3117929836631576, 0.30840842255880024, 0.5053284748453036, 0.34695977703070524, 0.34041020544555445, 0.33445009513129786, 0.3290263945890143, 0.324090826953294, 0.31959946027534814, 0.3155123164806266, 0.3117930155202404, 0.3084084515487464, 0.5053285012261555, 0.346959791434652, 0.3404102185531464, 0.3344501070592069, 0.32902640544341183, 0.32409083683079604, 0.3195994692638752, 0.3155123246601865, 0.31179302296364014, 0.3084084583222404, 0.5053285073900351, 0.34695979480013067, 0.34041022161573203, 0.3344501098461599, 0.32902640797953914, 0.324090839138672, 0.31959947136404243, 0.3155123265713387, 0.3117930247027887, 0.3084084599048656, 0.5053285088302241, 0.34695979558647394, 0.34041022233130447, 0.33445011049733087, 0.3290264085721047, 0.32409083967790664, 0.31959947185474596, 0.31551232701787896, 0.3117930251091403, 0.3084084602746456, 0.5053285091667239, 0.34695979577020286, 0.3404102224984978, 0.3344501106494768, 0.3290264087105575, 0.3240908398038987, 0.31959947196939875, 0.315512327122213, 0.3117930252040843, 0.30840846036104463, 0.505328509245347, 0.3469597958131311, 0.3404102225375625, 0.3344501106850256, 0.32902640874290695, 0.3240908398333367, 0.3195994719961873, 0.3155123271465906, 0.31179302522626795, 0.30840846038123176, 0.5053285092637173, 0.3469597958231613, 0.34041022254668996, 0.33445011069333164, 0.3290264087504654, 0.3240908398402149, 0.31959947200244654, 0.3155123271522865, 0.3117930252314512, 0.3084084603859485, 0.5053285092680095, 0.34695979582550485, 0.3404102225488226, 0.3344501106952723};
        if (mode.isCurrentMode("PacketAAC") && !this.mc.thePlayer.isBlocking() && this.mc.thePlayer.onGround) {
            if (this.mc.gameSettings.keyBindForward.pressed) {
                event.y += posY[this.tick];
                boolean bl = this.mc.thePlayer.onGround = this.tick == 0;
                if (this.mc.thePlayer.hurtTime == 0) {
                    PlayerUtil.setSpeed(speed[this.speedTick]);
                }
                ++this.tick;
                if (this.tick > posY.length - 1) {
                    this.tick = 0;
                }
                ++this.speedTick;
                if (this.speedTick > speed.length - 1) {
                    this.speedTick = speed.length - 10;
                }
                if (PlayerUtil.getSpeed() == 0.0 || this.mc.thePlayer.hurtTime != 0) {
                    this.speedTick = 0;
                }
            } else {
                this.speedTick = 0;
                this.tick = 0;
            }
        }
    }

    @EventTarget
    public void onUpdate(UpdateEvent event) {
        if (mode.isCurrentMode("AAC3.3.10") && !this.mc.thePlayer.isBlocking()) {
            if (this.mc.thePlayer.hurtTime == 0) {
                if (PlayerUtil.MovementInput()) {
                    if (this.legitHop) {
                        if (this.mc.thePlayer.onGround) {
                            this.mc.thePlayer.jump();
                            this.mc.thePlayer.onGround = false;
                            this.legitHop = false;
                        }
                        return;
                    }
                    if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.onGround = false;
                        this.strafe(0.375f);
                        this.mc.thePlayer.jump();
                        this.mc.thePlayer.motionY = 0.41;
                    } else {
                        this.mc.thePlayer.speedInAir = 0.0211f;
                    }
                } else {
                    this.mc.thePlayer.motionZ = 0.0;
                    this.mc.thePlayer.motionX = 0.0;
                    this.legitHop = true;
                }
                if (this.mc.thePlayer.isAirBorne) {
                    PlayerUtil.setSpeed(PlayerUtil.getSpeed());
                }
            }
        } else if (mode.isCurrentMode("NCP")) {
    		if(this.mc.thePlayer.onGround && PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
                this.mc.timer.timerSpeed = 1.0F;
                this.mc.thePlayer.jump();
             } else if(PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
                this.mc.timer.timerSpeed = 1.095F;
                PlayerUtil.setSpeed(0.26D);
            }
            if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        } else if (mode.isCurrentMode("NCP-YPort")) {
            if (PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionY = 0.399399995003033;
                this.moveSpeed = 1.3 * this.getBaseMoveSpeed() - 1.0E-4;
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.jump();
                }
                this.mc.timer.timerSpeed = 1.2f;
            }
            if (!this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = -0.5;
            }
        } else if (mode.isCurrentMode("Race")) {
            if (this.mc.thePlayer.onGround && PlayerUtil.MovementInput()) {
                PlayerUtil.setSpeed(this.racespeed.getValueState());
            }
        } else if (mode.isCurrentMode("AAC3.1.5")) {
            if (this.mc.thePlayer.isCollidedHorizontally || this.mc.gameSettings.keyBindJump.getIsKeyPressed() || !this.mc.gameSettings.keyBindForward.getIsKeyPressed()) {
                return;
            }
            if (this.mc.thePlayer.hurtTime > 0) {
                return;
            }
            if (this.mc.thePlayer.onGround) {
                this.mc.thePlayer.jump();
                final EntityPlayerSP thePlayer = this.mc.thePlayer;
                thePlayer.motionX *= 1.0099999904632568;
                final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                thePlayer2.motionZ *= 1.0099999904632568;
                this.mc.thePlayer.motionY = 0.38510000705718994;
            }
            else if (this.mc.thePlayer.motionY > 0.0) {
                final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
                thePlayer3.motionY -= 0.014999999664723873;
            }
            else {
                final EntityPlayerSP thePlayer4 = this.mc.thePlayer;
                thePlayer4.motionY -= 0.01489999983459711;
                final BlockPos willBe = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.6, this.mc.thePlayer.posZ);
                if (this.mc.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMaxY() == this.mc.theWorld.getBlockState(willBe).getBlock().getBlockBoundsMinY() + 1.0 && !this.mc.theWorld.getBlockState(willBe).getBlock().isTranslucent() && this.mc.theWorld.getBlockState(willBe).getBlock() != Blocks.water && !(this.mc.theWorld.getBlockState(willBe).getBlock() instanceof BlockSlab)) {
                    this.mc.thePlayer.motionY = -1.0;
                }
            }
        } else if (mode.isCurrentMode("Motion")) {
			if (mc.thePlayer.onGround) {
				mc.thePlayer.motionY = 0.3;
				mc.thePlayer.speedInAir = 0.042F;
			}
        } else if (mode.isCurrentMode("BAC")) {
			if(!this.mc.gameSettings.keyBindForward.pressed)
				return;
            final BlockPos pos = new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 0.1, this.mc.thePlayer.posZ);
            if (this.mc.theWorld.getBlockState(pos).getBlock() == Blocks.air) {
                this.mc.timer.timerSpeed = 1.0f;
                return;
            }
            if (this.mc.thePlayer.onGround) {
                this.mc.timer.timerSpeed = 1.05f;
                this.mc.thePlayer.motionY = 0.09000000357627869;
                this.move(this.mc.thePlayer.rotationYaw, 0.432f);
            }
            else if (this.mc.thePlayer.motionY < -0.07) {
                this.move(this.mc.thePlayer.rotationYaw, 0.432f);
            }
            else {
                this.move(this.mc.thePlayer.rotationYaw, 0.333f);
            }
        } else if (mode.isCurrentMode("Custom")) {
        	mc.timer.timerSpeed = customtimer.getValueState();
            if (PlayerUtil.MovementInput()) {

                    if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.jump();
                        this.mc.thePlayer.motionY = CustomY.getValueState();
                    }
                    PlayerUtil.toFwd(this.firstjump ? 0.0000 : CustomSpeedFinal.getValueState() );
                
            } else {
                this.tick = 0;
                this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = (double)0;
            }
            PlayerUtil.setSpeed(PlayerUtil.getSpeed());
        } else if (mode.isCurrentMode("AACYport3.3.1")) {
            if (this.mc.thePlayer.onGround && PlayerUtil.MovementInput()) {
                this.mc.thePlayer.jump();
            } else {
                this.mc.thePlayer.motionY = -0.21;
            }
        } else if (mode.isCurrentMode("MineSecure")) {
            if (this.mc.thePlayer.onGround && PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
                this.mc.thePlayer.jump();
            } else if (PlayerUtil.MovementInput() && !this.mc.thePlayer.isInWater()) {
                PlayerUtil.setSpeed(1.0);
            }
            if (!PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionZ = 0.0;
                this.mc.thePlayer.motionX = 0.0;
            }
        } else if (mode.isCurrentMode("AACLowhop3.3.9")) {
            if (PlayerUtil.MovementInput()) {
                if (this.mc.thePlayer.hurtTime == 0) {
                    if (this.mc.thePlayer.onGround) {
                        this.mc.thePlayer.jump();
                        this.mc.thePlayer.motionY = 0.4075;
                    } else {
                        this.mc.thePlayer.motionY -= 0.0145;
                    }
                    PlayerUtil.toFwd(0.00149);
                }
            } else {
                this.tick = 0;
                this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = (double)0;
            }
        } else if (mode.isCurrentMode("DAC")) {
            if (this.mc.theWorld != null) {
                this.mc.timer.timerSpeed = 15.0f;
            }
        } else if (mode.isCurrentMode("Rewinside")) {
            if (PlayerUtil.MovementInput()) {
                this.mc.thePlayer.motionY = 0.399399995003033;
                this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 1.0E-4;
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.jump();
                }
            }
            if (!this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = -1.0;
            }
            PlayerUtil.setSpeed(PlayerUtil.getSpeed());
        } else if (mode.isCurrentMode("SpectreLowHop")) {
            if (!PlayerUtil.MovementInput() || this.mc.thePlayer.movementInput.jump) {
                return;
            }
            if (this.mc.thePlayer.onGround) {
                this.strafe(1.1f);
                this.mc.thePlayer.motionY = 0.15;
            } else {
                this.strafe((float)Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
            }
        } else if (mode.isCurrentMode("SpectreBhop")) {
            if (!PlayerUtil.MovementInput() || this.mc.thePlayer.movementInput.jump) {
                return;
            }
            if (this.mc.thePlayer.onGround) {
                this.strafe(1.1f);
                this.mc.thePlayer.motionY = 0.44;
            } else {
                this.strafe((float)Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
            }
        } else if (mode.isCurrentMode("AACPort")) {
            float f = this.mc.thePlayer.rotationYaw * 0.017453292f;
            double d = 0.2;
            while (d <= 1.0) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX - (double)MathHelper.sin(f) * d, this.mc.thePlayer.posY, this.mc.thePlayer.posZ + (double)MathHelper.cos(f) * d, true));
                d += 0.2;
            }
        } else if (mode.isCurrentMode("Lowhop")) {
            if (PlayerUtil.MovementInput()) {
                if (this.mc.thePlayer.onGround) {
                    this.strafe(0.7f);
                    this.mc.thePlayer.motionY = 0.3;
                } else {
                    this.strafe((float)Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
                }
            }
        } else if (mode.isCurrentMode("AAC")) {
            if (PlayerUtil.MovementInput()) {
                if (this.mc.thePlayer.hurtTime < 4) {
                    if (this.mc.thePlayer.onGround) {
                        if (!this.firstjump) {
                            this.firstjump = true;
                        }
                        this.mc.thePlayer.jump();
                        this.mc.thePlayer.motionY = 0.407;
                    } else {
                        this.firstjump = false;
                        this.mc.thePlayer.motionY -= 0.0149;
                    }
                    PlayerUtil.toFwd(this.firstjump ? 0.0000 : 0.0005);
                }
            } else {
                this.tick = 0;
                this.mc.thePlayer.motionX = this.mc.thePlayer.motionZ = (double)0;
            }
            PlayerUtil.setSpeed(PlayerUtil.getSpeed());
        } else if (mode.isCurrentMode("FASTAAC3.3.11")) {
            this.mc.timer.timerSpeed = this.AAC.getValueState().floatValue();
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
        }
    }
    public void move(final float yaw, final float multiplyer, final float up) {
        final double moveX = -Math.sin(Math.toRadians(yaw)) * multiplyer;
        final double moveZ = Math.cos(Math.toRadians(yaw)) * multiplyer;
        this.mc.thePlayer.motionX = moveX;
        this.mc.thePlayer.motionY = up;
        this.mc.thePlayer.motionZ = moveZ;
    }
    
    public void move(final float yaw, final float multiplyer) {
        final double moveX = -Math.sin(Math.toRadians(yaw)) * multiplyer;
        final double moveZ = Math.cos(Math.toRadians(yaw)) * multiplyer;
        this.mc.thePlayer.motionX = moveX;
        this.mc.thePlayer.motionZ = moveZ;
    }


	@Override
    public void onEnable() {
        super.onEnable();
        ClientUtil.sendClientMessage("Speed Enable", ClientNotification.Type.SUCCESS);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        this.posY = 0.0;
        this.mc.timer.timerSpeed = 1.0f;
        this.tick = 0;
        this.legitHop = true;
        this.firstjump = false;
        this.mc.thePlayer.speedInAir = 0.02f;
        this.speedTick = 0;
        ClientUtil.sendClientMessage("Speed Disable", ClientNotification.Type.ERROR);
    }

    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    private void strafe(float speed) {
        boolean isMovingStraight;
        boolean isMoving = this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f;
        boolean isMovingForward = this.mc.thePlayer.moveForward > 0.0f;
        boolean isMovingBackward = this.mc.thePlayer.moveForward < 0.0f;
        boolean isMovingRight = this.mc.thePlayer.moveStrafing > 0.0f;
        boolean isMovingLeft = this.mc.thePlayer.moveStrafing < 0.0f;
        boolean isMovingSideways = isMovingLeft || isMovingRight;
        boolean bl = isMovingStraight = isMovingForward || isMovingBackward;
        if (!isMoving) {
            return;
        }
        double yaw = this.mc.thePlayer.rotationYaw;
        if (isMovingForward && !isMovingSideways) {
            yaw += 0.0;
        } else if (isMovingBackward && !isMovingSideways) {
            yaw += 180.0;
        } else if (isMovingForward && isMovingLeft) {
            yaw += 45.0;
        } else if (isMovingForward) {
            yaw -= 45.0;
        } else if (!isMovingStraight && isMovingLeft) {
            yaw += 90.0;
        } else if (!isMovingStraight && isMovingRight) {
            yaw -= 90.0;
        } else if (isMovingBackward && isMovingLeft) {
            yaw += 135.0;
        } else if (isMovingBackward) {
            yaw -= 135.0;
        }
        yaw = Math.toRadians(yaw);
        this.mc.thePlayer.motionX = (- Math.sin(yaw)) * (double)speed;
        this.mc.thePlayer.motionZ = Math.cos(yaw) * (double)speed;
    }
}

