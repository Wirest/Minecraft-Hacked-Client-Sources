package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.Event;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.*;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.module.ModuleManager;
import me.Corbis.Execution.ui.Notifications.Notification;
import me.Corbis.Execution.ui.Notifications.NotificationType;
import me.Corbis.Execution.utils.*;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import net.minecraft.util.Vec3;
import optifine.MathUtils;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Flight extends Module {
    public Setting mode;
    public Setting bob;
    Position lastPos;

    private float air, groundTicks;
    boolean wasOnGround, hasJumped;
    double motionY;
    int count;
    boolean collided, half;
    public Setting antikick;
    double y = 0;
    public int ticks = 0;
    int xd, level;
    boolean canFly = false;
    public Setting hypixelMode;
    public Setting speed;
    public Setting tpBoost;
    private double mineplexSpeed;
    public static boolean back, done;
    int stage = 0;
    double lastDist = 0, moveSpeed = 0;
    public static double startX, startY, startZ;
    Position FastPos;
    TimeHelper cubecraftTeleportTickTimer = new TimeHelper();

    ArrayList<Packet> packets = new ArrayList<>();

    public Setting boostMcCentral;
    private boolean damageFly, onGroundCheck, allowed;
    public int counter, maxSize;
    public Setting blink;
    private TimeHelper timer = new TimeHelper();
    public Setting timerBoost;
    public Setting timerSpeed;
    public Setting timerDuration;
    public Setting inBetween;
    public Setting tpSpeed;

    public Flight() {
        super("Flight", Keyboard.KEY_F, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        ArrayList<String> options2 = new ArrayList<>();
        options.add("Vanilla");
        options.add("Hypixel");
        options.add("Unlag");
        options.add("NCP");
        options.add("Cubecraft");
        options.add("Mineplex");
        options.add("Mineplex2");
        options.add("Spartan");
        options.add("Matrix");
        options.add("MCC");
        options.add("Hive");
        options.add("Redesky");
        options.add("Motion");
        options.add("ZeltikAC");
        options.add("Bow");
        options2.add("Normal");
        options2.add("Fast");
        options2.add("NonBlinkFast");
        options2.add("Drop");
        options2.add("Boost");
        options2.add("Fire");
        options2.add("Disabler");
        options2.add("No Damage");
        options2.add("EPearl");
        Execution.instance.settingsManager.rSetting(mode = new Setting("Flight Mode", this, "Vanilla", options));
        Execution.instance.settingsManager.rSetting(antikick = new Setting("AntiKick", this, false));

        Execution.instance.settingsManager.rSetting(boostMcCentral = new Setting("Mc Central Boost", this, false));
        Execution.instance.settingsManager.rSetting(timerBoost = new Setting("Timer Boost", this, false));
        Execution.instance.settingsManager.rSetting(timerSpeed = new Setting("Timer Speed", this, 2.0F, 1.1, 5f, false));

        Execution.instance.settingsManager.rSetting(timerDuration = new Setting("Timer Duration", this, 200, 20, 2000, true));

        Execution.instance.settingsManager.rSetting(bob = new Setting("View Bobbing", this, false));
        Execution.instance.settingsManager.rSetting(tpBoost = new Setting("TPBoost", this, false));
        Execution.instance.settingsManager.rSetting(tpSpeed = new Setting("TP Distance", this, 5, 1, 10, false));
        Execution.instance.settingsManager.rSetting(blink = new Setting("Blink", this, true));

        Execution.instance.settingsManager.rSetting(inBetween = new Setting("Machine Learning Boost", this, false));


        Execution.instance.settingsManager.rSetting(speed = new Setting("Speed", this, 1, 0, 10, false));

        Execution.instance.settingsManager.rSetting(hypixelMode = new Setting("Hypixel Mode", this, "Normal", options2));
    }

    public static double randomNumber(double max, double min) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    boolean hypixelDamaged = false;

    @EventTarget
    public void onMotion(EventMotionUpdate event) {
        if (this.bob.getValBoolean()) {
            mc.thePlayer.cameraYaw = 0.105F;
        }
        this.setDisplayName("Flight §f[" + mode.getValString() + "]");
        if (event.getState() == Event.State.PRE) {

            switch (mode.getValString()) {
                case "Vanilla":
                    mc.thePlayer.capabilities.isFlying = true;
                    mc.thePlayer.capabilities.setFlySpeed((float) speed.getValDouble() / 10);
                    break;
                case "Hypixel":
                    if (hypixelMode.getValString().equalsIgnoreCase("Normal")) {
                        mc.thePlayer.motionY = 0;
                        MoveUtils.setMotion(MoveUtils.getBaseMoveSpeed());
                        ticks++;
                        switch (this.ticks) {
                            case 1: {
                                this.y *= -0.949999988079071D;
                                break;
                            }
                            case 2:
                            case 3:
                            case 4: {
                                this.y += 1.0e-5;
                                break;
                            }
                            case 5: {
                                this.y += 8e-7;
                                this.ticks = 0;
                                break;
                            }
                        }
                        if(mc.thePlayer.ticksExisted % 2 == 0) {
                            event.setY(event.getY() + 1e-8);

                        }


                        MoveUtils.setPosPlus(0, y, 0);

                    } else if (hypixelMode.getValString().equalsIgnoreCase("Boost")) {
                        final double amplifier = 1 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.2 *
                                (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) : 0);
                        final double baseSpeed = MoveUtils.getBaseMoveSpeed();


                        ++this.ticks;
                        switch (this.ticks) {
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
                        event.setY(event.getY() + y);
                        mc.thePlayer.motionY = 0;//aldojinfante@gmail.com:Tobydog312

                        if (!MoveUtils.isMoving()) {
                            MoveUtils.setMotion(0);
                        }


                    } else if (hypixelMode.getValString().equalsIgnoreCase("No Damage")) {

                        if (this.stage == 1) {
                            this.moveSpeed = 1.34D * MoveUtils.getBaseMoveSpeed();
                        } else if (this.stage == 2) {
                            this.moveSpeed *= mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.12D : 2.13D;
                        } else if (this.stage == 3) {
                            this.moveSpeed = this.moveSpeed - 0.66D * (this.lastDist - MoveUtils.getBaseMoveSpeed());
                        } else {
                            this.moveSpeed = this.moveSpeed - this.lastDist / 159.8D;
                        }
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            event.setY(event.getY() + 0.005423123132);
                        }
                        mc.thePlayer.motionY = 0;

                        ++this.stage;
                        if (stage < 5) {
                            mc.timer.timerSpeed = 3.0f;
                        } else if (stage < 22 && stage > 5) {
                            mc.timer.timerSpeed = 2.0f;
                        }
                        if (stage > 22) {
                            mc.timer.timerSpeed = 1.0f;
                        }

                        MoveUtils.setMotion(MoveUtils.getBaseMoveSpeed());


                    } else if (hypixelMode.getValString().equalsIgnoreCase("Fast")) {

                        if (!canFly)
                            return;


                        if (stage > 1 && !mc.gameSettings.keyBindSneak.pressed) {
                            mc.thePlayer.motionY = 0;

                            if (!MoveUtils.isMoving()) {
                                MoveUtils.setMotion(0);
                            }
                        }
                        ticks++;

                        switch (this.ticks) {
                            case 1: {
                                this.y *= -0.949999988079071D;
                                break;
                            }
                            case 2:
                            case 3:
                            case 4: {
                                this.y += 1.0e-5;
                                break;
                            }
                            case 5: {
                                this.y += 8e-7;
                                this.ticks = 0;
                                break;
                            }
                        }
                        event.setY(event.getY() + y);




                    } else if (hypixelMode.getValString().equalsIgnoreCase("EPearl")) {
                        if (mc.thePlayer.hurtTime > 0) {
                            hypixelDamaged = true;
                        }


                        if (mc.thePlayer.onGround) {
                            moveSpeed = this.speed.getValDouble();
                            mc.thePlayer.jump();
                            ;
                            for (int i = 0; i < 45; i++) {
                                if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                                    continue;
                                if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemEnderPearl) {
                                    if (i > 9) {
                                        moveBlocksToHotbar();
                                    }
                                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(i));
                                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(mc.thePlayer.rotationYaw, 90, mc.thePlayer.onGround));
                                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                                    if (i > 9) {
                                        moveBlocksBackToInventory();
                                    }
                                }
                            }
                        }


                        if (hypixelDamaged) {

                            moveSpeed = moveSpeed - lastDist / 159;

                            mc.thePlayer.motionY = 0;
                            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                                mc.thePlayer.motionY = this.speed.getValDouble();
                            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                                mc.thePlayer.motionY = -this.speed.getValDouble();
                            }
                            MoveUtils.setMotion(moveSpeed);

                        } else {
                            mc.thePlayer.motionY = 0;
                        }

                    } else if (hypixelMode.getValString().equalsIgnoreCase("Drop")) {
                        switch (stage) {
                            case 0:
                                if (mc.thePlayer.fallDistance > 5.3) {
                                    moveSpeed = this.speed.getValDouble();
                                    C13PacketPlayerAbilities packetPlayerAbilities = new C13PacketPlayerAbilities();
                                    packetPlayerAbilities.setCreativeMode(true);
                                    mc.getNetHandler().addToSendQueue(packetPlayerAbilities);
                                    stage++;
                                }
                                break;
                            case 1:
                                mc.thePlayer.motionY = 0;
                                if (mc.thePlayer.hurtTime > 0) {
                                    hypixelDamaged = true;
                                }
                                if (hypixelDamaged) {
                                    MoveUtils.setMotion(moveSpeed);
                                    if (mc.gameSettings.keyBindJump.pressed) {
                                        mc.thePlayer.motionY = moveSpeed;
                                    }
                                    if (mc.thePlayer.movementInput.sneak) {
                                        mc.thePlayer.motionY = -moveSpeed;
                                    }
                                    if (moveSpeed > MoveUtils.getBaseMoveSpeed()) {

                                    }
                                    ticks++;
                                    switch (this.ticks) {
                                        case 1: {
                                            this.y *= -0.859749988079071;
                                            break;
                                        }
                                        case 2:
                                        case 3:
                                        case 4: {
                                            this.y += 2E-4;
                                            break;
                                        }
                                        case 5: {
                                            this.y += 2.86E-4;
                                            this.ticks = 0;
                                            break;
                                        }
                                    }
                                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
                                } else {
                                    MoveUtils.setMotion(0);
                                }
                                break;
                        }
                    } else if (hypixelMode.getValString().equalsIgnoreCase("NonBlinkFast")) {
                        if (stage > 1) {
                            mc.thePlayer.motionY = 0;
                            if (mc.thePlayer.ticksExisted % 2 == 0) {
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0006185623663, mc.thePlayer.posZ);
                            } else {
                                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0006185623663, mc.thePlayer.posZ);
                            }
                        }
                    }
                    break;
                case "Spartan":
                    if (mc.thePlayer.posY <= startY) {
                        mc.thePlayer.motionY = 0.41999998688697815D;
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, startY, mc.thePlayer.posZ, false));
                    }
                    break;
                case "NCP":

                    MoveUtils.setMotion(MoveUtils.getBaseMoveSpeed() * this.speed.getValDouble());
                    if (mc.thePlayer.ticksExisted % 2 == 0) {
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.28, mc.thePlayer.posZ, true));
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.28, mc.thePlayer.posZ, false));

                    }
                    //   mc.thePlayer.sendQueue.addToSendQueueSilent(new C18PacketSpectate());
                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C0CPacketInput(0, 0, true, true));
                    mc.thePlayer.motionY = 0;
                    break;
                case "MCC":
                    mc.thePlayer.motionY = 0;
                    if (mc.thePlayer.motionY > 0) {
                        mc.thePlayer.motionY -= speed.getValDouble() / 10;
                    }
                    if (mc.thePlayer.movementInput.sneak) {
                        mc.thePlayer.motionY -= speed.getValDouble() / 10;
                    } else if (mc.thePlayer.movementInput.jump) {
                        mc.thePlayer.motionY += speed.getValDouble() / 10;
                    }
                    break;
                case "ZeltikAC":

                    MoveUtils.strafe();
                    MoveUtils.setMotion(speed.getValDouble());
                    mc.thePlayer.motionY = 0;
                    mc.timer.timerSpeed = 0.3f;
                    if (mc.thePlayer.ticksExisted % 3 == 0) {
                        mc.thePlayer.motionY = -16;
                    } else {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, startY, mc.thePlayer.posZ);
                    }


                    break;
                case "Mineplex2":
                    for(int i = 0; i < 159; i++){

                    }


                    break;
                case "Motion":
                    if (antikick.getValBoolean()) {
                        if (mc.thePlayer.ticksExisted % 2 == 0) {
                            mc.thePlayer.motionY = 0.1;
                        } else {
                            mc.thePlayer.motionY = -0.12;
                        }
                        if (!MoveUtils.isMoving()) {
                            MoveUtils.setMotion(0);
                        } else {
                            MoveUtils.setMotion(speed.getValDouble());
                        }
                        if (mc.gameSettings.keyBindJump.pressed) {
                            mc.thePlayer.motionY = speed.getValDouble();
                        }
                        if (mc.gameSettings.keyBindSneak.pressed) {
                            mc.thePlayer.motionY = -speed.getValDouble();
                        }
                        return;
                    }
                    mc.thePlayer.motionY = 0;
                    if (!MoveUtils.isMoving()) {
                        MoveUtils.setMotion(0);
                    } else {
                        MoveUtils.setMotion(speed.getValDouble());
                    }
                    if (mc.gameSettings.keyBindJump.pressed) {
                        mc.thePlayer.motionY = speed.getValDouble();
                    }
                    if (mc.gameSettings.keyBindSneak.pressed) {
                        mc.thePlayer.motionY = -speed.getValDouble();
                    }


                    break;
                case "Hive":
                    if (mc.thePlayer.posY < -70) {
                        mc.thePlayer.motionY = speed.getValDouble();
                    }
                    if (mc.gameSettings.keyBindSprint.isKeyDown()) {
                        mc.timer.timerSpeed = 0.3f;

                    } else {
                        mc.timer.timerSpeed = 1f;
                    }
                    break;
                case "Cubecraft":
                    mc.timer.timerSpeed = 0.3f;
                    mc.thePlayer.motionY = 0.0D;
                    mc.thePlayer.motionX = 0.0D;
                    mc.thePlayer.motionZ = 0.0D;
                    double value = MathHelper.getRandomDoubleInRange(new Random(), 2.1D, 3.1D);
                    double x = -Math.sin((double) MoveUtils.getDirection()) * value;
                    double z = Math.cos((double) MoveUtils.getDirection()) * value;
                    if (mc.thePlayer.ticksExisted % 4 == 0) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.15D, mc.thePlayer.posZ);
                        if (MoveUtils.isMoving()) {
                            mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                        }
                    } else {
                        mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.05D, mc.thePlayer.posZ);
                    }
                    break;
                case "Redesky":
                    mc.thePlayer.motionY = 0.0D;
                    mc.thePlayer.onGround = true;
                    this.count++;
                    if (this.count == 1) {
                        event.setY(event.getY() + 2e-7);
                    } else if (this.count == 3) {
                        mc.thePlayer.posY += 1e-6;
                        event.setY(event.getY() - 2e-7);
                        this.count = 0;
                    }
                    break;
                case "Unlag":
                    ++this.ticks;
                    final double offset = 3E-4;
                    switch (this.ticks) {
                        case 1: {
                            this.y *= -0.859749988079071;
                            break;
                        }
                        case 2:
                        case 3:
                        case 4: {
                            this.y += 2E-4;
                            break;
                        }
                        case 5: {
                            this.y += 2.86E-4;
                            this.ticks = 0;
                            break;
                        }
                    }
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + y, mc.thePlayer.posZ);
                    mc.thePlayer.motionY = 0;
                    MoveUtils.setMotion(this.speed.getValDouble());
                    if (mc.gameSettings.keyBindJump.pressed) {
                        mc.thePlayer.motionY = this.speed.getValDouble();
                    } else if (mc.gameSettings.keyBindSneak.pressed) {
                        mc.thePlayer.motionY = -this.speed.getValDouble();
                    }
                    break;
                case "Bow":

                    for (int i = 0; i < 9; i++) {
                        if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                            continue;
                        if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemBow) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                        }
                    }
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }

                    if (MoveUtils.isMoving() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        if (mc.thePlayer.motionY < 0) {
                            mc.timer.timerSpeed = 0.1f;
                        } else if (mc.timer.timerSpeed == 0.1f) {
                            mc.timer.timerSpeed = 1f;
                        }
                        event.setPitch(-60);
                        MoveUtils.strafe();
                        if (mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime && mc.thePlayer.maxHurtTime > 0) {
                            if (mc.thePlayer.hurtTime == 1) {
                                mc.thePlayer.motionY = 0.42;
                            }
                        }
                        ++this.ticks;
                        Packet C07 = new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN);
                        Packet C08 = new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem());
                        if (ticks >= 4) {
                            mc.thePlayer.sendQueue.addToSendQueue(C07);
                            ticks = 0;
                        } else if (ticks == 1) {
                            mc.thePlayer.sendQueue.addToSendQueue(C08);
                        }


                    }
                    break;
                case "Mineplex":
                    //if(done) {
                    //    mc.thePlayer.posY = startY;
                    //  if (mc.gameSettings.keyBindJump.pressed) {
                    //     startY += 0.5;
                    // } else if (mc.gameSettings.keyBindSneak.pressed) {
                    //     startY -= 0.5;
                    // }
                    // }
                    break;


            }
        } else {
            if (mc.thePlayer.ticksExisted % 10 == 0) {
                onGroundCheck = mc.thePlayer.onGround;
            }
            if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Fast")) {
                if(stage < 5){

                }
            }
        }
    }

    public void forceMove() {
        double speed = .15;
        mc.thePlayer.motionX = (-Math.sin(MoveUtils.getDirection())) * speed;
        mc.thePlayer.motionZ = Math.cos(MoveUtils.getDirection()) * speed;
    }

    private void moveBlocksToHotbar() {
        boolean added = false;
        if (!isHotbarFull()) {
            for (int k = 0; k < mc.thePlayer.inventory.mainInventory.length; ++k) {
                if (k > 8 && !added) {
                    final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
                    if (itemStack != null && itemStack.getItem() instanceof ItemEnderPearl) {
                        shiftClick(k);
                        added = true;
                    }
                }
            }
        }
    }

    public void moveBlocksBackToInventory() {
        for (int i = 0; i < 9; i++) {
            if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                continue;
            if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemEnderPearl) {
                shiftClick(i);
            }
        }
    }

    public static void shiftClick(int slot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().thePlayer.inventoryContainer.windowId, slot, 0, 1, Minecraft.getMinecraft().thePlayer);
    }

    public boolean isHotbarFull() {
        int count = 0;
        for (int k = 0; k < 9; ++k) {
            final ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
            if (itemStack != null) {
                count++;
            }
        }
        return count == 8;
    }


    public static double getRandomInRange(double min, double max) {
        SecureRandom rand = new SecureRandom();

        return rand.nextDouble() * (max - min) + min;
    }

    public double fallPacket() {
        double i;
        for (i = mc.thePlayer.posY; i > getGroundLevel(); i -= 8.0) {
            if (i < getGroundLevel()) {
                i = getGroundLevel();
            }
            mc.thePlayer.sendQueue.addToSendQueue(
                    new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
        }
        return i;
    }

    public void ascendPacket() {
        for (double i = getGroundLevel(); i < mc.thePlayer.posY; i += 8.0) {
            if (i > mc.thePlayer.posY) {
                i = mc.thePlayer.posY;
            }
            mc.thePlayer.sendQueue.addToSendQueue(
                    new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, i, mc.thePlayer.posZ, true));
        }
    }

    public double getGroundLevel() {
        for (int i = (int) Math.round(mc.thePlayer.posY); i > 0; --i) {
            final AxisAlignedBB box = mc.thePlayer.boundingBox.addCoord(0.0, 0.0, 0.0);
            box.minY = i - 1;
            box.maxY = i;
            if (!isColliding(box) || !(box.minY <= mc.thePlayer.posY)) {
                continue;
            }
            return i;
        }
        return 0.0;
    }

    public boolean isColliding(final AxisAlignedBB box) {
        return mc.theWorld.checkBlockCollision(box);
    }


    public static int airSlot() {
        for (int j = 0; j < 8; ++j) {
            if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[j] == null) {
                return j;
            }
        }
        Execution.instance.addChatMessage("Clear a hotbar slot.");
        return -10;
    }

    @EventTarget
    public void onRenderEntity(EventRenderEntity event) {
        if (event.getEntity() instanceof EntityEnderPearl) {
            event.setCancelled(true);
        }
    }

    boolean boosted = false;

    @EventTarget
    public void onMotion(EventMotion event) {

        if (mode.getValString().equalsIgnoreCase("Mineplex")) {
            if (airSlot() == -10) {
                Execution.instance.addChatMessage("Empty a hotbar slot.");
                this.toggle();
                return;
            }

            if (!done) {
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(airSlot()));
                setBlockAndFacing.BlockUtil.placeHeldItemUnderPlayer();

                MoveUtils.setMotion(event, back ? -mineplexSpeed : mineplexSpeed);
                back = !back;
                mineplexSpeed += 0.1;
                if (mineplexSpeed >= speed.getValDouble() * 1.3 && mc.thePlayer.isCollidedVertically && mc.thePlayer.isMovingOnGround()) {
                    event.setY(mc.thePlayer.motionY = 0.42F);
                    MoveUtils.setMotion(event, 0.0);
                    Execution.instance.notificationManager.show(new Notification("Gwen Disabled.", 2, NotificationType.INFO));

                    done = true;
                    return;
                }
            } else {
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                if (mc.thePlayer.fallDistance == 0) {
                    event.setY(mc.thePlayer.motionY += 0.039);
                } else if (mc.thePlayer.fallDistance <= 1.4) {
                    event.setY(mc.thePlayer.motionY += 0.032);
                }
                MoveUtils.setMotion(event, mineplexSpeed *= 0.979);
                if (MoveUtils.isMoving() && mc.thePlayer.isCollidedVertically) {
                    done = false;
                }

            }
        }
        if (mode.getValString().equalsIgnoreCase("Hypixel") && (hypixelMode.getValString().equalsIgnoreCase("EPearl"))) {
            if (!hypixelDamaged) {
                MoveUtils.setMotion(event, 0);
            }
        }
        if(mode.getValString().equalsIgnoreCase("Mineplex2")){
            if (airSlot() == -10) {
                Execution.instance.addChatMessage("Empty a hotbar slot.");
                this.toggle();
                return;
            }

            mc.thePlayer.sendQueue.addToSendQueueSilent(new C09PacketHeldItemChange(airSlot()));
            for(int i = 0; i < 25; i++) {
                setBlockAndFacing.BlockUtil.placeHeldItemUnderPlayer();
            }
            if(MoveUtils.isMoving())
                MoveUtils.setMotion(event, MoveUtils.getSpeed() * 0.999);
            if(!mc.thePlayer.movementInput.sneak) {
                event.setY(mc.thePlayer.ticksExisted % 2 == 0 ? -1.0e-99 : 1.0e-99);
                if (mc.thePlayer.movementInput.jump) {
                    event.setY(mc.thePlayer.ticksExisted % 2 == 0 ? 1.0e-2 : 1.0);
                }
            }
            for(int i = 0; i < 25; i++){
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C0CPacketInput(-1, -1, true, false));
                mc.thePlayer.capabilities.isCreativeMode = true;
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C13PacketPlayerAbilities(mc.thePlayer.capabilities  ));
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C0FPacketConfirmTransaction(Integer.MIN_VALUE, (short) 0, false));
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C0DPacketCloseWindow(mc.thePlayer.inventoryContainer.windowId));
            }


        }

        if (mode.getValString().equalsIgnoreCase("Hypixel") && (hypixelMode.getValString().equalsIgnoreCase("Boost"))) {
            if (MoveUtils.isMoving()) {
                if (mc.thePlayer.movementInput.sneak) {
                    event.setY(event.getY() * MoveUtils.WATCHDOG_BUNNY_SLOPE);
                    event.setY(event.getY() - 0.032);
                }
                if (mc.thePlayer.hurtTime > 0) {
                    hypixelDamaged = true;
                }
                if (timerBoost.getValBoolean()) {
                    if (!hypixelDamaged) {
                        mc.timer.timerSpeed = 3.5f;
                    } else {
                        mc.timer.timerSpeed = 1.0f;
                    }
                }
                final double amplifier = 1 + (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 0.2 *
                        (mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1) : 0);
                final double baseSpeed = 0.29D * amplifier;

                if (MoveUtils.isMoving()) {
                    switch (stage) {
                        case 0:
                            moveSpeed = (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034) * this.speed.getValDouble();
                            if (tpBoost.getValBoolean()) {
                                MoveUtils.setPosPlus(MoveUtils2.getPosForSetPosX(tpSpeed.getValDouble()), 0, MoveUtils2.getPosForSetPosZ(tpSpeed.getValDouble()));
                            }
                            break;
                        case 1:
                            event.setY(0.4199999);
                            moveSpeed *= 2.16;

                            break;
                        default:
                            moveSpeed = moveSpeed - lastDist / 159.8D;
                            //event.setY(mc.thePlayer.motionY = 0)    ;

                            break;

                    }
                    MoveUtils.setMotion(Math.max(moveSpeed, MoveUtils.getBaseMoveSpeed()));

                    stage++;


                }
            }
        }

        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Fast")) {
            /**
             * moveSpeed = event.getMovementSpeed() * (mc.thePlayer.isPotionActive(Potion.moveSpeed) ? 2.219 : 2.1);
             * moveSpeed *= 2.149999;
             * moveSpeed = Math.min(speed.getValDouble(), 1.4445f);
             */
            if (timerBoost.getValBoolean() && canFly) {
                if (!timer.hasReached(timerDuration.getValInt())) {
                    mc.timer.timerSpeed = (float) timerSpeed.getValDouble();
                } else {
                    mc.timer.timerSpeed = 1.0f;
                }
            }



            if (canFly) {
                if (mc.thePlayer.hurtTime > 0) {
                    hypixelDamaged = true;
                }


                if (MoveUtils.isMoving()) {
                    switch (stage) {
                        case -1:
                            mc.timer.timerSpeed = 0.5f;
                            damagePlayer();


                            break;
                        case 0:
                            mc.thePlayer.capabilities.isFlying = false;
                            mc.thePlayer.capabilities.allowFlying = false;
                            mc.timer.timerSpeed = 0.6f;
                            break;
                        case 1:
                            if (mc.thePlayer.onGround && mc.thePlayer.isCollidedVertically) {
                                event.setY(MoveUtils.getJumpBoostModifier((float) event.getLegitMotion()));
                                mc.thePlayer.posY += 0.42;
                            }
                            mc.timer.timerSpeed = 0.7f;


                            moveSpeed = 0.635;

                            break;
                        case 2:
                            mc.timer.timerSpeed = 2.5f;
                            moveSpeed = Math.min(speed.getValDouble() / 1.635, 1.635);
                            break;

                        default:

                            if(stage > 10){
                                mc.timer.timerSpeed = 1.0f;
                            }
                            moveSpeed = lastDist - lastDist / 159.9999;

                            break;


                    }
                    moveSpeed = Math.max(moveSpeed, MoveUtils.getBaseMoveSpeed());
                    ++stage;
                    if (stage > 0)
                        MoveUtils.setMotion(event, moveSpeed);


                } else {
                    MoveUtils.setMotion(event, 0);
                    this.moveSpeed = 0;
                }

                //torak2@live.nl:Jappie1994-

                if(mc.thePlayer.ticksExisted % 2 == 0) {
                    event.setY(event.getY() + 1e-8);

                }

                if (mc.thePlayer.isCollidedHorizontally) {
                    moveSpeed = 0;
                }
                MoveUtils.setMotion(event, Math.max(moveSpeed, MoveUtils.getBaseMoveSpeed()));
                if (TargetStrafe.canStrafe()) {
                    TargetStrafe.strafe(event, Math.max(moveSpeed, MoveUtils.getBaseMoveSpeed()), Execution.instance.getModuleManager().getModule(Aura.class).target, true);
                }
            } else {
                startY = mc.thePlayer.posY + 50;
                mc.thePlayer.capabilities.isCreativeMode = true;
                mc.thePlayer.capabilities.isFlying = true;
                mc.thePlayer.capabilities.allowFlying = true;
               // C13PacketPlayerAbilities packet = new C13PacketPlayerAbilities();
                //packet.setCreativeMode(true);
                //mc.getNetHandler().addToSendQueueSilent(packet);
                canFly = true;
            }


        }
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("NonBlinkFast")) {
            Execution.instance.addChatMessage("" + stage);
            switch (stage) {
                case 0:
                    stage++;
                    break;
                case 1:
                    MoveUtils.setMotion(event, 0);
                    //    if(!hypixelDamaged) {
                    damagePlayer();
                    //   hypixelDamaged = true;
                    //   }
                    // if(mc.thePlayer.hurtTime > 0){
                    event.setY(0.42F);
                    moveSpeed = MoveUtils.getBaseMoveSpeed() * 1.6275;
                    stage++;
                    //   }
                    break;
                case 2:
                    stage++;
                    moveSpeed = 0.5275;
                    break;
                default:
                    moveSpeed = moveSpeed - lastDist / 159;
                    break;
            }
            moveSpeed = Math.max(moveSpeed, MoveUtils.getBaseMoveSpeed());
            MoveUtils.setMotion(event, moveSpeed);

        }
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Disabler")) {
            if (!this.canFly && !mc.thePlayer.onGround) {
                event.setY(mc.thePlayer.motionY = 1e-6);
                MoveUtils.setMotion(event, 0);


            } else if (this.canFly) {
                if (mc.gameSettings.keyBindJump.isKeyDown() && !mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.y = mc.thePlayer.motionY = this.speed.getValDouble();
                } else if (mc.gameSettings.keyBindSneak.isKeyDown() && !mc.gameSettings.keyBindJump.isKeyDown()) {
                    event.y = mc.thePlayer.motionY = -this.speed.getValDouble();
                } else {
                    event.y = mc.thePlayer.motionY = 0;
                }

                MoveUtils.setMotion(event, this.speed.getValDouble());

            }
        }
    }


    public void damagePlayer() {
        if(mc.thePlayer.onGround){
            for(int i = 0; i < 9; i++){
                mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
            }
            double fallDistance = 3.125;
            while(fallDistance > 0){
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0624986421, mc.thePlayer.posZ, false));
                mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0000013579, mc.thePlayer.posZ, false));
                fallDistance -= 0.0624986421;
            }
            mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));

        }

    }




    @EventTarget
    public void onUpdate(EventUpdate event) {
        lastPos = new Position(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        //     Execution.instance.addChatMessage(moveSpeed + "");
        if (mode.getValString().equalsIgnoreCase("Motion")) {
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                MoveUtils.setPosPlus(0, 8e-7, 0);
            }
        }
        if (mode.getValString().equalsIgnoreCase("Hypixel")) {
            if (hypixelMode.getValString().equalsIgnoreCase("Boost")) {
                C13PacketPlayerAbilities packetPlayerAbilities = new C13PacketPlayerAbilities();
                packetPlayerAbilities.setCreativeMode(true);
                ;
                mc.getNetHandler().addToSendQueueSilent(packetPlayerAbilities);


            }
            if (hypixelMode.getValString().equalsIgnoreCase("Disabler")) {
                if (canFly) {
                    mc.thePlayer.capabilities.isCreativeMode = true;
                }
            }
            if (hypixelMode.getValString().equalsIgnoreCase("EPearl")) {
                if (hypixelDamaged) {
                    if (mc.thePlayer.ticksExisted % 30 == 0) {
                        C13PacketPlayerAbilities packetPlayerAbilities = new C13PacketPlayerAbilities();
                        packetPlayerAbilities.setCreativeMode(true);
                        mc.getNetHandler().addToSendQueueSilent(packetPlayerAbilities);
                        Execution.instance.addChatMessage("Final: " + moveSpeed);

                    }
                }
            }

            if (hypixelMode.getValString().equalsIgnoreCase("Fast") || hypixelMode.getValString().equalsIgnoreCase("Normal") || mode.getValString().equalsIgnoreCase("Motion")) {


                //mc.thePlayer.capabilities.isCreativeMode = true;

                // mc.thePlayer.sendQueue.addToSendQueue(new S13PacketDestroyEntities(mc.thePlayer.getEntityId()));


                if(packets.size() > 45){
                    for(Packet p : packets){
                        mc.getNetHandler().addToSendQueueSilent(p);
                    }
                    packets.clear();
                }


            }
        }
    }

    boolean blinked = false;

    @EventTarget
    public void onSendPAcket(EventSendPacket event) {
        if (mode.getValString().equalsIgnoreCase("Hypixel")) {
            if (hypixelMode.getValString().equalsIgnoreCase("Boost") || hypixelMode.getValString().equalsIgnoreCase("Fast") || (hypixelMode.getValString().equalsIgnoreCase("Disabler") && canFly) || (hypixelMode.getValString().equalsIgnoreCase("EPearl") && hypixelDamaged) || mode.getValString().equalsIgnoreCase("Motion")) {
                if(!blink.getValBoolean()) {
                    if (!canFly || !hypixelDamaged) {
                        if (event.getPacket() instanceof C03PacketPlayer) {
                            packets.add(event.getPacket());
                            event.setCancelled(true);
                        }
                    }
                    if (hypixelDamaged && !blinked) {
                        for (Packet packet : packets) {
                            mc.thePlayer.sendQueue.addToSendQueueSilent(packet);
                        }
                        blinked = true;
                        packets.clear();
                    }
                }
                if (event.getPacket() instanceof C03PacketPlayer && blink.getValBoolean()) {
                    packets.add(event.getPacket());

                    event.setCancelled(true);
                }



            }

        } else if (mode.getValString().equalsIgnoreCase("ZeltikAC")) {
            if (event.getPacket() instanceof C0CPacketInput) {
                event.setCancelled(true);
            }
        } else if (mode.getValString().equalsIgnoreCase("NCP")) {
            if (mc.thePlayer.ticksExisted % 2 == 0 && event.getPacket() instanceof C03PacketPlayer) {
                //   event.setCancelled(true);
            }
        } else if (mode.getValString().equalsIgnoreCase("Matrix")) {
            EntityPlayerSP player = mc.thePlayer;
            if (event.getPacket() instanceof C03PacketPlayer) ticks++;
            if ((event.getPacket() instanceof C03PacketPlayer) && (ticks % 6 == 0)) {
                mc.timer.timerSpeed = 0.5f;
                player.motionY = 0.0;
                player.motionX *= this.speed.getValDouble();
                player.motionZ *= this.speed.getValDouble();
            }
        }
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket packetEvent) {
        if (mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP.contains("hypixel")) {
            if (mode.getValString().equalsIgnoreCase("Motion")) {
                if (packetEvent.getPacket() instanceof S00PacketDisconnect) {
                    //     packetEvent.setCancelled(true);
                }
            }
        }
        if (packetEvent.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) packetEvent.getPacket();
            canFly = true;
            if (this.mode.getValString().equalsIgnoreCase("NCP")) {
                if (mc.thePlayer.ticksExisted % 2 == 0) {
                    packetEvent.setCancelled(true);
                }
            }
            if (this.mode.getValString().equalsIgnoreCase("Hypixel") && this.hypixelMode.getValString().equalsIgnoreCase("Fast")) {
                if (packetEvent.getPacket() instanceof S01PacketPong) {
                }
            }
            if (this.mode.getValString().equalsIgnoreCase("Unlag")) {

                if (lastPos != null) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(lastPos.getX(), lastPos.getY(), lastPos.getZ(), true));
                }
            }
        }
    }

    boolean viewBobbing = false;

    @Override
    public void onEnable() {
        super.onEnable();
        air = 0;
        lastDist = 0;
        mineplexSpeed = 0;
        hypixelDamaged = false;
        viewBobbing = mc.gameSettings.viewBobbing;
        done = false;
        blinked = false;
        canFly = false;
        startX = mc.thePlayer.posX;
        startY = mc.thePlayer.posY;
        startZ = mc.thePlayer.posZ;
        timer.reset();
        back = false;
        ticks = 0;
        counter = 0;
        packets.clear();
        wasOnGround = false;
        hasJumped = false;
        blinked = false;

        moveSpeed = 0;
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Boost")) {
            if (!Execution.instance.moduleManager.getModuleByName("Disabler").isEnabled) {
                Execution.instance.addChatMessage("You need the module Disabler on to use Boost fly!");
                this.toggle();
                return;

            }
            damagePlayer();
            C13PacketPlayerAbilities packetPlayerAbilities = new C13PacketPlayerAbilities();
            packetPlayerAbilities.setCreativeMode(true);
            ;
            //   mc.getNetHandler().addToSendQueueSilent(packetPlayerAbilities);
            C10PacketCreativeInventoryAction packet = new C10PacketCreativeInventoryAction();
            packet.slotId = Integer.MIN_VALUE;
            packet.stack = new ItemStack(Items.ender_pearl);
            for (int i = 0; i < 10; i++) {
                mc.getNetHandler().addToSendQueue(packet);
            }
        }
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Fast")) {
            if (!Execution.instance.moduleManager.getModuleByName("Disabler").isEnabled) {
                Execution.instance.addChatMessage("You need the module Disabler on to use Fast fly!");
                this.toggle();
                return;

            }
            mc.thePlayer.fall(4, 1.0f);

            C13PacketPlayerAbilities packet = new C13PacketPlayerAbilities();
            packet.setCreativeMode(true);
            //  mc.getNetHandler().addToSendQueue(packet);

            stage = -1;


        } else {
            stage = 0;
        }
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Disabler")) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.280, mc.thePlayer.posZ, true));
            mc.thePlayer.jump();
            MoveUtils.setMotion(0);
        }
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("No Damage")) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump();
            }
            mc.timer.timerSpeed = 1.9f;
        }
        if (mc.thePlayer.onGround && mode.getValString().equalsIgnoreCase("MCC") || mc.thePlayer.onGround && mode.getValString().equalsIgnoreCase("Vanilla")) {
            mc.thePlayer.jump();
        }
        if (mode.getValString().equalsIgnoreCase("Mineplex")) {
        }


        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Fire")) {
            for (int i = 0; i < 9; i++) {
                if (mc.thePlayer.inventory.getStackInSlot(i) == null)
                    continue;
                if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemFlintAndSteel || mc.thePlayer.inventory.getStackInSlot(i).getItem() == Items.lava_bucket) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                    mc.thePlayer.rotationPitch = 89;
                    mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), EnumFacing.UP, new Vec3(mc.thePlayer.posX * 0.5, mc.thePlayer.posY * 0.5, mc.thePlayer.posZ * 0.5));
                    mc.thePlayer.rotationPitch = mc.thePlayer.rotationPitch - 89;

                } else {
                    Execution.instance.addChatMessage("You Need a flint and steel or a lava bucket in your hotbar!");
                    //    this.toggle();
                    return;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();

        y = 0;
        MoveUtils.setMotion(0);
        //jaycadena1@yahoo.com:ngcjsc10
        mc.thePlayer.capabilities.isFlying = false;
        if (mode.getValString().equalsIgnoreCase("Hypixel") && hypixelMode.getValString().equalsIgnoreCase("Boost") || (hypixelMode.getValString().equalsIgnoreCase("Disabler") && canFly) || hypixelMode.getValString().equalsIgnoreCase("Fast") || (hypixelMode.getValString().equalsIgnoreCase("EPearl") && hypixelDamaged) || mode.getValString().equalsIgnoreCase("Motion")) {
            if (!inBetween.getValBoolean()) {
                for (Packet packet : packets) {
                    // if (this.blink.getValBoolean())
                    mc.thePlayer.capabilities.isCreativeMode = false;
                    mc.getNetHandler().addToSendQueue(packet);
                }



            } else {
                for (int i = 0; i < packets.size(); i++) {
                    if (this.blink.getValBoolean()) {
                        C03PacketPlayer a = (C03PacketPlayer) packets.get(i);
                        C03PacketPlayer b = (C03PacketPlayer) packets.get(i >= packets.size() - 1 ? i : i + 1);
                        Position delta = MoveUtils.findInBetween(new Position(a.getPositionX(), a.getPositionY(), a.getPositionZ()), new Position(b.getPositionX(), b.getPositionY(), b.getPositionZ()));
                        mc.getNetHandler().addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(delta.getX(), delta.getY(), delta.getZ(), false));
                        mc.getNetHandler().addToSendQueueSilent(a);
                    }
                }
            }
            mc.thePlayer.capabilities.isCreativeMode = false;
            packets.clear();
        }
        mc.gameSettings.viewBobbing = viewBobbing;
        mc.timer.timerSpeed = 1f;
        count = 0;
        canFly = false;
    }


}
