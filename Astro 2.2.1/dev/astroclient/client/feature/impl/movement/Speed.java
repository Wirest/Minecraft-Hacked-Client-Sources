package dev.astroclient.client.feature.impl.movement;

import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.player.EventMove;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.util.EntityUtil;
import net.minecraft.potion.Potion;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import org.lwjgl.Sys;

@Toggleable(label = "Speed", category = Category.MOVEMENT)
public class Speed extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", true, "Watchdog", new String[]{"Watchdog"});

    public int stage;
    private double moveSpeed, lastDist;

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
    }

    public void onEnable() {
        stage = 0;
        moveSpeed = 0;
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        setSuffix(mode.getValue());
        if (eventMotion.getEventType() == EventType.PRE) {
            lastDist = Math.sqrt(((mc.thePlayer.posX - mc.thePlayer.prevPosX) * (mc.thePlayer.posX - mc.thePlayer.prevPosX)) + ((mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)));
            stage++;
        }
    }

    @Subscribe
    public void onEvent(EventMove eventMove) {
        double difference = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? (mc.thePlayer.isPotionActive(Potion.jump) ? .82 : .84) : .78);
        switch (stage) {
            case 0:
                lastDist = 0;
                break;
            case 3:
                lastDist = 0;
                break;
            case 4:
                if (mc.thePlayer.isMoving() && mc.thePlayer.onGround) {
                    double motionY = .4085;
                    if (mc.thePlayer.isPotionActive(Potion.jump))
                        motionY += ((mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
                    eventMove.setY(mc.thePlayer.motionY = motionY);
                    moveSpeed *= 2.145;
                }
                break;
            case 5:
                moveSpeed = lastDist - difference * (lastDist - mc.thePlayer.getBaseMoveSpeed());
                break;
            default:
                if ((mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, mc.thePlayer.motionY, 0.0D)).size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0)
                    stage = !mc.thePlayer.isMoving() ? 0 : 2;
                moveSpeed = lastDist - (lastDist / 159);
                break;
        }
        if (stage > 0)
            EntityUtil.setMoveSpeed(eventMove, Math.max(moveSpeed, mc.thePlayer.getBaseMoveSpeed()));
    }
}
