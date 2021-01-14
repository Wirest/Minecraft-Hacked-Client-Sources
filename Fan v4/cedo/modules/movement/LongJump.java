package cedo.modules.movement;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventMove;
import cedo.events.listeners.EventUpdate;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.ui.notifications.Notification;
import cedo.ui.notifications.NotificationType;
import cedo.util.BypassUtil;
import cedo.util.movement.MovementUtil;
import org.lwjgl.input.Keyboard;


public class LongJump extends Module {

    public ModeSetting glideMode = new ModeSetting("Glide Mode", "Normal", "Normal", "High");
    public BooleanSetting damage = new BooleanSetting("Damage", false);
    public BooleanSetting autoDisable = new BooleanSetting("AutoDisable", true);

    int counter;

    double movementSpeed;
    double lastDist;

    boolean didHop;
    boolean ifair;

    public LongJump() {
        super("LongJump", Keyboard.KEY_NONE, Category.MOVEMENT);
        addSettings(damage, glideMode);
    }

    public void onEnable() {
        ifair = false;
        counter = 1;
        lastDist = 0;
        movementSpeed = 0.4;
        super.onEnable();
    }

    public void onDisable() {
        ifair = false;
        super.onDisable();
    }
    //public void onEvent(Event e) {

    public void onEvent(Event e) {
        switch (glideMode.getSelected()) {
            case "Normal":
                if (e instanceof EventMotion) {
                    if (mc.thePlayer.onGround) {
                        movementSpeed = 0.5;
                        if (damage.isEnabled()) {
                            BypassUtil.damage();
                        }

                        mc.thePlayer.jump();
                    } else {
                        movementSpeed -= lastDist / 159.9999;
                        MovementUtil.setSpeed(movementSpeed);

                        if (mc.thePlayer.fallDistance > 0.8) {
                            movementSpeed = 0;
                            toggleSilent();
                        }
                    }
                } else if (e instanceof EventMove) {
                    if (mc.thePlayer.fallDistance < 0.2) {
                        if (mc.thePlayer.motionY < 0) {
                            ((EventMove) e).setY(((EventMove) e).getY() * 0.65);
                        }
                    }
                }

                if (e instanceof EventUpdate) {
                    double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                    double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                    lastDist = Math.sqrt(xDist * xDist + zDist * zDist);

                    if (autoDisable.isEnabled()) {
                        if (!ifair && !mc.thePlayer.onGround) {
                            ifair = true;
                        } else if (ifair && mc.thePlayer.onGround) {
                            toggle();
                        }
                    }
                }
                break;

            case "High":
                if (e instanceof EventMotion) {
                    switch (counter) {
                        case 1:
                            if (damage.isEnabled()) {
                                BypassUtil.damage();
                            } else {
                                toggleSilent();
                                Notification.post(NotificationType.WARNING, "Damage", "High Glide Longjump Requires Damage");
                            }
                            break;

                        case 3:
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.motionY = 0.55F;
                            }
                            movementSpeed = 0.65;
                            break;
                    }

                    if (counter > 3) {
                        if (mc.thePlayer.onGround) {
                            setToggled(false);
                        }
                    }

                    counter++;

                    movementSpeed = movementSpeed - lastDist / 159;

                    MovementUtil.setSpeed(movementSpeed);
                } else if (e instanceof EventMove) {
                    if (mc.thePlayer.fallDistance < 0.2) {
                        if (mc.thePlayer.motionY < 0) {
                            mc.thePlayer.motionY += 0.03F;
                            ((EventMove) e).setY(((EventMove) e).getY() * 0.66 * 0.96);
                        }
                    }
                }
                break;
        }
    }
}
