package dev.astroclient.client.feature.impl.movement;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.player.EventMove;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.ChatUtil;
import dev.astroclient.client.util.EntityUtil;
import dev.astroclient.client.util.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

import java.util.List;

/**
 * @author Zane2711 for PublicBase
 * @since 10/28/19
 */

@Toggleable(label = "Flight", description = "Flight", category = Category.MOVEMENT)
public class Flight extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", true, "Watchdog", new String[]{"Watchdog", "Motion"});
    public BooleanProperty teleport = new BooleanProperty("Teleport", true, true);
    public NumberProperty<Integer> teleportDuration = new NumberProperty<>("Teleport Duration", true, 15, 1, 1, 100, Type.MILLISECONDS);
    public NumberProperty<Float> teleportSpeed = new NumberProperty<>("Teleport Speed", true, 9.6F, .05F, 1F, 10F);

    public BooleanProperty damage = new BooleanProperty("Damage", true, true);

    public NumberProperty<Float> multiply = new NumberProperty<>("Multiplier", true, 1.47F, .01F, .1F, 3F);
    public NumberProperty<Integer> multiplyTime = new NumberProperty<>("Multiply Time", true, 800, 1, 1, 2500, Type.MILLISECONDS);

    public NumberProperty<Float> boostSpeed = new NumberProperty<>("Boost Speed", mode.getValue().equals("HypixelBoost"), 1.85F, .01F, .1F, 2F);
    public NumberProperty<Float> motionSpeed = new NumberProperty<>("Motion Speed", mode.getValue().equals("Motion"), 2F, .25F, .1F, 10F);

    private double moveSpeed, lastDist;

    private int counter, stage, ticks;

    private boolean boost, damaged;

    private Timer initTimer = new Timer();
    private Timer timer = new Timer();

    public void onEnable() {
        counter = 0;
        stage = 1;
        ticks = 0;
        lastDist = 0;
        mc.thePlayer.stepHeight = 0;
        initTimer.reset();
        damaged = false;
        if (mc.thePlayer.onGround && mc.thePlayer.fallDistance == 0)
            boost = true;
        if (teleport.getValue() && boost)
            mc.timer.timerSpeed = teleportSpeed.getValue();

        if (damage.getValue() && boost) {
            mc.thePlayer.motionZ = 0;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = .0625;
            for (int i = 0; i < 67; i++) {
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 0.05, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + 0.00001, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
            }
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }

    }

    public void onDisable() {
        mc.thePlayer.motionZ = 0;
        boost = false;
        mc.thePlayer.motionX = 0;
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.stepHeight = .6F;
    }

    @Subscribe
    public void onEvent(EventMove eventMove) {
        switch (mode.getValue()) {
            case "Watchdog":
                if (boost) {
                    List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0, 0, 0));
                    double boost = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? boostSpeed.getValue() : boostSpeed.getValue() * 1.15;
                    switch (stage) {
                        case 1:
                            moveSpeed = boost * mc.thePlayer.getBaseMoveSpeed();
                            break;
                        case 2:
                            moveSpeed *= boost;
                            break;
                        case 3:
                            moveSpeed = lastDist - (lastDist / 156) * (lastDist - mc.thePlayer.getBaseMoveSpeed());
                            break;
                        default:
                            if (Client.INSTANCE.featureManager.killAura.target == null) {
                                if (!timer.hasReached(multiplyTime.getValue())) {
                                    if (!teleport.getValue() && damaged)
                                        mc.timer.timerSpeed = multiply.getValue();
                                } else
                                    mc.timer.timerSpeed = 1.0F;
                            } else
                                mc.timer.timerSpeed = 1.0F;
                            if (collidingList.size() > 0 || mc.thePlayer.isCollidedVertically)
                                stage = 1;

                            moveSpeed = lastDist - (lastDist / 600);
                            break;
                    }
                    stage++;
                    EntityUtil.setMoveSpeed(eventMove, moveSpeed = Math.max(mc.thePlayer.getBaseMoveSpeed(), moveSpeed));
                }
                break;
            case "Motion":
                EntityUtil.setMoveSpeed(eventMove, motionSpeed.getValue());
                break;
        }
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        setSuffix(mode.getValue());
        if (eventMotion.getEventType() == EventType.PRE) {
            if (boost) {
                double minY = mc.thePlayer.getEntityBoundingBox().minY;
                switch (ticks) {
                    case 0:
                        eventMotion.setY(minY + .4085);
                        break;
                    case 1:
                        eventMotion.setY(minY);
                        break;
                }

                if (teleport.getValue())
                    if (initTimer.hasReached(teleportDuration.getValue()) && !timer.hasReached(multiplyTime.getValue()) && Client.INSTANCE.featureManager.killAura.target == null)
                        if (damaged)
                            mc.timer.timerSpeed = multiply.getValue();
                        else
                            mc.timer.timerSpeed = 1.0F;

                if (mc.thePlayer.hurtTime > 3)
                    damaged = true;

                ticks++;

                if (!damaged)
                    timer.reset();

            }
            lastDist = Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * .99;
            counter++;
            if (mc.gameSettings.keyBindJump.pressed)
                mc.thePlayer.motionY += .2;
            else if (mc.gameSettings.keyBindSneak.pressed)
                mc.thePlayer.motionY -= .2;
            else
                mc.thePlayer.motionY = 0;
            mc.thePlayer.onGround = true;
            switch (counter) {
                case 1:
                    EntityUtil.tpRel(0, 1.0E-13D, 0);
                    break;
                case 2:
                    EntityUtil.tpRel(0, 1.0E-123D, 0);
                    counter = 0;
                    break;
            }
        }
    }
}
