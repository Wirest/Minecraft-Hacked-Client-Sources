package info.sigmaclient.module.impl.movement;

import java.io.IOException;
import java.util.List;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.management.MoveUtils;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventJump;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventStep;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.BlockUtils;
import info.sigmaclient.util.misc.ChatUtil;
import io.netty.buffer.Unpooled;
import net.java.games.input.Keyboard;
import net.minecraft.block.Block;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Util;

public class Bhop extends Module {

    public final static String MODE = "MODE";
    private final String BOOST = "BOOST";
    private final String LAGBACK = "LAGBACK";
    private float air, ground, aacSlow;
    public boolean shouldslow = false;
    double count = 0;
    int jumps;
    public static Timer timer = new Timer();
    boolean collided = false, lessSlow;
    int spoofSlot = 0;
    double less, stair;

    public Bhop(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Speed Mode", "Hypixel", new String[]{"Bhop", "Hypixel", "OnGround", "OldHop", "Mineplex", "AAC", "Janitor", "Cubecraft", "MineplexHop", "AACWall"}), "Speed bypass method."));
        settings.put(LAGBACK, new Setting<>("LagBack", false, "Disable speed if you get lagback."));
    }

    private double speed, speedvalue;
    private double lastDist;
    public static int stage, aacCount;
    Timer aac = new Timer();
    Timer lastFall = new Timer();
    Timer lastCheck = new Timer();


    @Override
    public void onEnable() {
        collided = mc.thePlayer.isCollidedHorizontally;
        spoofSlot = mc.thePlayer.inventory.currentItem;
        lessSlow = false;
        if (mc.thePlayer != null) {
            speed = MoveUtils.defaultSpeed();
        }
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Bhop")) {
            if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                Notifications.getManager().post("Speed", "Bhop mode does not bypass on hypixel.", Notifications.Type.WARNING);
            }
        }
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Guardian")) {
            if (!timer.delay(2000l)) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, -10, mc.thePlayer.posZ, true));

            }
        }
        less = 0;
        jumps = 0;
        count = 0;
        lastDist = 0.0;
        stage = 2;
        mc.timer.timerSpeed = 1;
        air = 0;
        if (premiumAddon != null) {
            premiumAddon.onEnable();
        }

    }


    public void onDisable() {
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("Mineplex")) {
            if (mc.thePlayer.inventory.currentItem != spoofSlot) {
                mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                mc.playerController.updateController();
            }
        }
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("AACWall")) {
            mc.thePlayer.motionX *= 0.2;
            mc.thePlayer.motionZ *= 0.2;
        }
        aacCount = 0;
        mc.timer.timerSpeed = 1;
        if (((Options) settings.get(MODE).getValue()).getSelected().equalsIgnoreCase("cubecraft")) {
            mc.thePlayer.motionX *= 0.5;
            mc.thePlayer.motionZ *= 0.5;
        }
    }


    @Override
    @RegisterEvent(events = {EventMove.class, EventUpdate.class, EventPacket.class, EventStep.class})
    public void onEvent(Event event) {
        String currentMode = ((Options) settings.get(MODE).getValue()).getSelected();
        setSuffix(currentMode);

        //  if(!Client.getModuleManager().isEnabled(Scaffold.class))
        if (currentMode.equalsIgnoreCase("MineplexHop")) {
            if (premiumAddon != null) {
                premiumAddon.onEvent(event);
            } else {
                Notifications.getManager().post("Premium Bypass", "MineplexHop mode is a premium only bypass", Notifications.Type.WARNING);
                toggle();
            }
        }
        if (event instanceof EventStep) {
            EventStep es = (EventStep) event;
            if (!es.isPre()) {
                double height = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
                if (height > 0.7) {
                    less = 0;
                }
                if (height == 0.5)
                    stair = 0.75;
            }
        }
        if (event instanceof EventMove) {
            EventMove em = (EventMove) event;
            switch (currentMode) {

                case "Hypixel": {
                    if (mc.thePlayer.isCollidedHorizontally) {
                        collided = true;
                    }
                    if (collided) {
                        mc.timer.timerSpeed = 1;
                        stage = -1;
                    }
                    if (stair > 0)
                        stair -= 0.25;
                    less -= less > 1 ? 0.12 : 0.11;
                    if (less < 0)
                        less = 0;
                    if (!BlockUtils.isInLiquid() && MoveUtils.isOnGround(0.01) && (PlayerUtil.isMoving2())) {
                        collided = mc.thePlayer.isCollidedHorizontally;
                        if (stage >= 0 || collided) {
                            stage = 0;

                            double motY = 0.407 + MoveUtils.getJumpEffect() * 0.1;
                            if (stair == 0) {
                                mc.thePlayer.jump();
                                em.setY(mc.thePlayer.motionY = motY);
                            } else {

                            }

                            less++;
                            if (less > 1 && !lessSlow)
                                lessSlow = true;
                            else
                                lessSlow = false;
                            if (less > 1.12)
                                less = 1.12;
                        }
                    }
                    speed = getHypixelSpeed(stage) + 0.0331;
                    speed *= 0.91;
                    if (stair > 0) {
                        speed *= 0.7 - MoveUtils.getSpeedEffect() * 0.1;
                    }

                    if (stage < 0)
                        speed = MoveUtils.defaultSpeed();
                    if (lessSlow) {
                        speed *= 0.95;
                    }


                    if (BlockUtils.isInLiquid()) {
                        speed = 0.12;
                    }

                    if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        setMotion(em, speed);
                        ++stage;
                    }
                    break;
                }

                case "AAC": {

                    /*
                     * Made by LeakedPvP
                     */

                    if (mc.thePlayer.fallDistance > 1.2) {
                        lastFall.reset();
                    }
                    if (!BlockUtils.isInLiquid() && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        stage = 0;
                        mc.thePlayer.jump();
                        em.setY(mc.thePlayer.motionY = 0.41999998688698 + MoveUtils.getJumpEffect());
                        if (aacCount < 4)
                            aacCount++;
                    }
                    speed = getAACSpeed(stage, aacCount);
                    if ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        if (BlockUtils.isInLiquid()) {
                            speed = 0.075;
                        }
                        setMotion(em, speed);
                    }

                    if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                        ++stage;
                    }

                    break;
                }
                case "Bhop": {

                    if (mc.thePlayer.moveForward == 0.0f && mc.thePlayer.moveStrafing == 0.0f) {
                        speed = MoveUtils.defaultSpeed();
                    }
                    if (stage == 1 && mc.thePlayer.isCollidedVertically && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        speed = 1.35 + MoveUtils.defaultSpeed() - 0.01;
                    }
                    if (!BlockUtils.isInLiquid() && stage == 2 && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f)) {
                        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump))
                            em.setY(mc.thePlayer.motionY = 0.41999998688698 + (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1);
                        else
                            em.setY(mc.thePlayer.motionY = 0.41999998688698);
                        mc.thePlayer.jump();
                        speed *= 1.533D;
                    } else if (stage == 3) {
                        final double difference = 0.66 * (lastDist - MoveUtils.defaultSpeed());
                        speed = lastDist - difference;
                    } else {
                        final List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
                        if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && stage > 0) {
                            stage = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
                        }
                        speed = lastDist - lastDist / 159.0;
                    }
                    speed = Math.max(speed, MoveUtils.defaultSpeed());

                    //Stage checks if you're greater than 0 as step sets you -6 stage to make sure the player wont flag.
                    if (stage > 0) {
                        //Set strafe motion.
                        if (BlockUtils.isInLiquid())
                            speed = 0.1;
                        setMotion(em, speed);
                    }
                    //If the player is moving, step the stage up.
                    if (mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) {
                        ++stage;
                    }
                    break;
                }
                case "OldHop": {

                    if ((mc.thePlayer.moveForward == 0.0F) && (mc.thePlayer.moveStrafing == 0.0F)) {
                        speed = MoveUtils.defaultSpeed();
                    }
                    if ((stage == 1) && (mc.thePlayer.isCollidedVertically) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
                        speed = (0.25D + MoveUtils.defaultSpeed() - 0.01D);
                    } else if (!BlockUtils.isInLiquid() && (stage == 2) && (mc.thePlayer.isCollidedVertically) && MoveUtils.isOnGround(0.001) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
                        mc.thePlayer.motionY = 0.4D;
                        em.setY(0.4D);
                        mc.thePlayer.jump();
                        speed *= 2.149D;
                    } else if (stage == 3) {
                        double difference = 0.66D * (this.lastDist - MoveUtils.defaultSpeed());
                        speed = (this.lastDist - difference);
                    } else {
                        List collidingList = mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0D, mc.thePlayer.motionY, 0.0D));
                        if ((collidingList.size() > 0) || (mc.thePlayer.isCollidedVertically)) {
                            if (stage > 0) {
                                if (1.35D * MoveUtils.defaultSpeed() - 0.01D > speed) {
                                    stage = 0;
                                } else {
                                    stage = (mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F) ? 1 : 0;
                                }
                            }
                        }
                        speed = (this.lastDist - this.lastDist / 159.0D);
                    }
                    speed = Math.max(speed, MoveUtils.defaultSpeed());
                    if (stage > 0) {
                        if (BlockUtils.isInLiquid())
                            speed = 0.1;
                        setMotion(em, speed);
                    }
                    if ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F)) {
                        stage += 1;
                    }
                    break;
                }
            }

        } else if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
                double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
                lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
                if (BlockUtils.isInLiquid())
                    return;
            }


            switch (currentMode) {
                case "Cubecraft":
                    if (Client.getModuleManager().isEnabled(Scaffold.class) || !em.isPre() || mc.thePlayer.isInsideOfMaterial(Material.lava) || mc.thePlayer.isInWater())
                        return;
                    if (mc.thePlayer.isCollidedHorizontally || !PlayerUtil.isMoving2())
                        jumps = 0;
                    if (mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && PlayerUtil.isMoving2() && Step.lastStep.delay(60)) {

                        mc.thePlayer.motionY = 0.4;
                        double more = jumps <= 0 ? 0 : (double) jumps / 10;
                        speed = 0.45 + MoveUtils.getSpeedEffect() * 0.1;
                        stage = 0;
                        if (jumps < 2)
                            jumps++;
                    } else {
                        stage++;
                        double more = jumps <= 0 ? 0 : (double) jumps / 10;
                        speed = 0.35 - (double) stage / 200 + MoveUtils.getSpeedEffect() * 0.1;

                        if (mc.gameSettings.keyBindBack.getIsKeyPressed()) {
                            speed -= 0.07;
                        }
                    }
                    speed = Math.max(shouldslow ? speed - 0.1 : speed, 0.2);
                    MoveUtils.setMotion(speed);
                    break;
                case "AACWall": {
                    if (em.isPre()) {
                        if (mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0) {
                            if (count > 0) {
                                mc.thePlayer.motionX *= 0.2;
                                mc.thePlayer.motionZ *= 0.2;
                                count = 0;
                            }
                            air = 0;
                            return;
                        }
                        if (MoveUtils.isCollidedH(0.2)) {
                            if (MoveUtils.isOnGround(0.01)) {
                                em.setOnGround(true);
                                mc.thePlayer.motionY = 0.42;
                                mc.thePlayer.motionX = 0;
                                mc.thePlayer.motionZ = 0;
                                double speed = 0;
                                if (count == 0) {
                                    speed = 0.37;
                                } else if (count >= 1) {
                                    speed = 0.575;
                                }
                                if (MoveUtils.isBlockAboveHead()) {
                                    if (count >= 1) {
                                        speed = 0.472;
                                    }
                                }
                                MoveUtils.setMotion(speed - 0.005);
                                if (count < 2) {
                                    count++;
                                }
                                air = 0;

                            } else {

                                mc.thePlayer.motionY = -0.21;
                                mc.thePlayer.motionX = 0;
                                mc.thePlayer.motionZ = 0;
                                double speed = 0;
                                if (air == 0) {
                                    if (count == 1) {
                                        speed = 0.277;
                                    } else if (count == 2) {
                                        speed = 0.339;
                                    }


                                } else if (air == 1) {
                                    if (count == 1) {
                                        speed = 0.275;
                                    } else if (count == 2) {
                                        speed = 0.336;
                                    }
                                }
                                if (MoveUtils.isBlockAboveHead()) {
                                    if (count == 2) {
                                        speed = 0.3;
                                    }
                                }
                                MoveUtils.setMotion(speed - 0.005);
                                air++;

                            }
                        } else {
                            if (count > 0) {
                                mc.thePlayer.motionX *= 0.2;
                                mc.thePlayer.motionZ *= 0.2;
                                air = 0;
                                count = 0;
                            }
                        }
                    }
                }
                break;
                case "Mineplex": {
                    if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically && MoveUtils.isOnGround(0.01) && !mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                        if (em.isPre()) {
                            if (!Client.um.isPremium()) {
                                Notifications.getManager().post("Premium Bypass", "This mode is a premium only bypass", Notifications.Type.WARNING);
                                toggle();
                                return;
                            }
                            if (invCheck()) {
                                ItemStack is = new ItemStack(Item.getItemById(261));
                                for (int i = 9; i < 36; i++) {
                                    int count = 0;
                                    if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {

                                        for (int a = 36; a < 45; a++) {
                                            if (mc.thePlayer.inventoryContainer.canAddItemToSlot(mc.thePlayer.inventoryContainer.getSlot(a), is, true)) {
                                                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, a - 36, 2, mc.thePlayer);
                                                count++;
                                                break;
                                            }
                                        }

                                        if (count == 0) {
                                            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 7, 2, mc.thePlayer);
                                        }
                                        break;
                                    }
                                }
                            }
                            if (mc.thePlayer.inventory.getCurrentItem() != null)
                                if (spoofSlot != mc.thePlayer.inventory.currentItem) {
                                    spoofSlot = mc.thePlayer.inventory.currentItem;
                                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                                    mc.playerController.updateController();
                                }
                            if (mc.thePlayer.inventory.getCurrentItem() != null && mc.thePlayer.onGround)
                                if (timer.delay(500)) {
                                    timer.reset();
                                    MineplexSpoof();
                                }

                            C08PacketPlayerBlockPlacement place = new C08PacketPlayerBlockPlacement(new BlockPos(mc.thePlayer).add(0, -1, 0),
                                    EnumFacing.UP.getIndex(), null, 0.5f, 1f, 0.5f);
                            if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0)
                                mc.thePlayer.sendQueue.addToSendQueue(place);
                            mc.thePlayer.motionX *= 0;
                            mc.thePlayer.motionZ *= 0;
                            if (mc.thePlayer.inventory.getCurrentItem() == null) {
                                MoveUtils.setMotion(0.44);
                            } else {
                                MoveUtils.setMotion(0.35);
                            }

                        }
                    } else {
                        if (em.isPre()) {
                            mc.thePlayer.motionX *= 0;
                            mc.thePlayer.motionZ *= 0;
                            double speed = 0.35;
                            MoveUtils.setMotion(speed);
                        }
                    }

                }
                break;
                case "Janitor":

                    if (em.isPre()) {
                        double forward = mc.thePlayer.movementInput.moveForward;
                        double strafe = mc.thePlayer.movementInput.moveStrafe;
                        if ((forward != 0 || strafe != 0) && !mc.thePlayer.isJumping && !mc.thePlayer.isInWater()
                                && !mc.thePlayer.isOnLadder() && (!mc.thePlayer.isCollidedHorizontally)) {
                            em.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.4 : 0));
                        }
                        speed = Math.max(mc.thePlayer.ticksExisted % 2 == 0 ? 4.4 : 2.2, MoveUtils.defaultSpeed());
                        float yaw = mc.thePlayer.rotationYaw;
                        if ((forward == 0.0D) && (strafe == 0.0D)) {
                            mc.thePlayer.motionX = (0.0D);
                            mc.thePlayer.motionZ = (0.0D);
                        } else {
                            if (forward != 0.0D) {
                                if (strafe > 0.0D) {
                                    yaw += (forward > 0.0D ? -45 : 45);
                                } else if (strafe < 0.0D) {
                                    yaw += (forward > 0.0D ? 45 : -45);
                                }
                                strafe = 0.0D;
                                if (forward > 0.0D) {
                                    forward = 0.15;
                                } else if (forward < 0.0D) {
                                    forward = -0.15;
                                }
                            }
                            if (strafe > 0) {
                                strafe = 0.15;
                            } else if (strafe < 0) {
                                strafe = -0.15;
                            }
                            mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
                                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                            mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
                                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
                            if (mc.thePlayer.onGround) {
                                mc.thePlayer.motionY = 0.39;
                            }
                        }
                    }
                    break;

                case "OnGround": {
                    if (em.isPre()) {
                        mc.timer.timerSpeed = 1.085f;
                        double forward = mc.thePlayer.movementInput.moveForward;
                        double strafe = mc.thePlayer.movementInput.moveStrafe;
                        if ((forward != 0 || strafe != 0) && !mc.thePlayer.isJumping && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && (!mc.thePlayer.isCollidedHorizontally)) {

                            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, 0.4d, 0.0D)).isEmpty()) {
                                em.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.2 : 0));
                            } else {
                                em.setY(mc.thePlayer.posY + (mc.thePlayer.ticksExisted % 2 != 0 ? 0.4198 : 0));
                            }

                        }
                        speed = Math.max(mc.thePlayer.ticksExisted % 2 == 0 ? 2.1 : 1.3, MoveUtils.defaultSpeed());
                        float yaw = mc.thePlayer.rotationYaw;
                        if ((forward == 0.0D) && (strafe == 0.0D)) {
                            mc.thePlayer.motionX = (0.0D);
                            mc.thePlayer.motionZ = (0.0D);
                        } else {
                            if (forward != 0.0D) {
                                if (strafe > 0.0D) {
                                    yaw += (forward > 0.0D ? -45 : 45);
                                } else if (strafe < 0.0D) {
                                    yaw += (forward > 0.0D ? 45 : -45);
                                }
                                strafe = 0.0D;
                                if (forward > 0.0D) {
                                    forward = 0.15;
                                } else if (forward < 0.0D) {
                                    forward = -0.15;
                                }
                            }
                            if (strafe > 0) {
                                strafe = 0.15;
                            } else if (strafe < 0) {
                                strafe = -0.15;
                            }
                            mc.thePlayer.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                            mc.thePlayer.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
                        }
                    }

                    break;
                }
            }
        } else if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            Packet p = ep.getPacket();
            if (ep.isIncoming()) {
                if (p instanceof S08PacketPlayerPosLook) {

                    aacCount = 0;
                    count = 0;
                    jumps = -3;
                    S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
                    if ((Boolean) settings.get(LAGBACK).getValue()) {

                        Notifications.getManager().post("Lagback checks", "Disabled Speed", Notifications.Type.WARNING);
                        mc.thePlayer.onGround = false;
                        mc.thePlayer.motionX *= 0;
                        mc.thePlayer.motionZ *= 0;
                        mc.thePlayer.jumpMovementFactor = 0;
                        this.toggle();
                    } else if (lastCheck.delay(300)) {
                        pac.yaw = mc.thePlayer.rotationYaw;
                        pac.pitch = mc.thePlayer.rotationPitch;
                    }
                    if (currentMode.equalsIgnoreCase("Guardian")) {
                        pac.yaw = mc.thePlayer.rotationYaw;
                        pac.pitch = mc.thePlayer.rotationPitch;
                    }
                    stage = -4;
                    lastCheck.reset();
                }
            }
        }
    }
    private void setMotion(EventMove em, double speed) {
        double forward = mc.thePlayer.movementInput.moveForward;
        double strafe = mc.thePlayer.movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0D) && (strafe == 0.0D)) {
            em.setX(0.0D);
            em.setZ(0.0D);
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
    private double getHypixelSpeed(int stage) {
        double value = MoveUtils.defaultSpeed() + (0.028 * MoveUtils.getSpeedEffect()) + (double) MoveUtils.getSpeedEffect() / 15;
        double firstvalue = 0.4145 + (double) MoveUtils.getSpeedEffect() / 12.5;
        double decr = (((double) stage / 500) * 2);


        if (stage == 0) {
            //JUMP
            if (timer.delay(300)) {
                timer.reset();
                //mc.timer.timerSpeed = 1.354f;
            }
            if (!lastCheck.delay(500)) {
                if (!shouldslow)
                    shouldslow = true;
            } else {
                if (shouldslow)
                    shouldslow = false;
            }
            value = 0.64 + (MoveUtils.getSpeedEffect() + (0.028 * MoveUtils.getSpeedEffect())) * 0.134;

        } else if (stage == 1) {
            if (mc.timer.timerSpeed == 1.354f) {
                //mc.timer.timerSpeed = 1.254f;
            }
            value = firstvalue;
        } else if (stage >= 2) {
            if (mc.timer.timerSpeed == 1.254f) {
                //mc.timer.timerSpeed = 1f;
            }
            value = firstvalue - decr;
        }
        if (shouldslow || !lastCheck.delay(500) || collided) {
            value = 0.2;
            if (stage == 0)
                value = 0;
        }


        return Math.max(value, shouldslow ? value : MoveUtils.defaultSpeed() + (0.028 * MoveUtils.getSpeedEffect()));
    }

    private double getAACSpeed(int stage, int jumps) {
        double value = 0.29;
        double firstvalue = 0.3019;
        double thirdvalue = 0.0286 - (double) stage / 1000;
        if (stage == 0) {
            //JUMP
            value = 0.497;
            if (jumps >= 2) {
                value += 0.1069;
            }
            if (jumps >= 3) {
                value += 0.046;
            }
            Block block = MoveUtils.getBlockUnderPlayer(mc.thePlayer, 0.01);
            if (block instanceof BlockIce || block instanceof BlockPackedIce) {
                value = 0.59;
            }
        } else if (stage == 1) {
            value = 0.3031;
            if (jumps >= 2) {
                value += 0.0642;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 2) {
            value = 0.302;
            if (jumps >= 2) {
                value += 0.0629;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 3) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0607;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 4) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0584;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 5) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0561;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 6) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0539;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 7) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0517;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 8) {
            value = firstvalue;
            if (MoveUtils.isOnGround(0.05))
                value -= 0.002;

            if (jumps >= 2) {
                value += 0.0496;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 9) {
            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0475;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 10) {

            value = firstvalue;
            if (jumps >= 2) {
                value += 0.0455;
            }
            if (jumps >= 3) {
                value += thirdvalue;
            }
        } else if (stage == 11) {

            value = 0.3;
            if (jumps >= 2) {
                value += 0.045;
            }
            if (jumps >= 3) {
                value += 0.018;
            }

        } else if (stage == 12) {
            value = 0.301;
            if (jumps <= 2)
                aacCount = 0;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        } else if (stage == 13) {
            value = 0.298;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        } else if (stage == 14) {

            value = 0.297;
            if (jumps >= 2) {
                value += 0.042;
            }
            if (jumps >= 3) {
                value += thirdvalue + 0.001;
            }
        }
        if (mc.thePlayer.moveForward <= 0) {
            value -= 0.06;
        }

        if (mc.thePlayer.isCollidedHorizontally) {
            value -= 0.1;
            aacCount = 0;
        }
        return value;
    }

    private void MineplexSpoof() {
        try {

            for (int i = 36; i < 45; i++) {

                int theSlot = i - 36;
                if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {

                    if (mc.thePlayer.inventory.currentItem != theSlot) {
                        mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(theSlot));
                        mc.playerController.updateController();
                        spoofSlot = theSlot;
                    }
                    break;
                }

            }
        } catch (Exception e) {

        }
    }

    private boolean invCheck() {
        for (int i = 36; i < 45; i++) {
            if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }

}

