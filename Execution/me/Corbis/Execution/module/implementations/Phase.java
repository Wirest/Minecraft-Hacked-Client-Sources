package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.EventCollide;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.event.events.EventReceivePacket;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.MoveUtils;
import me.Corbis.Execution.utils.MoveUtils2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

public class Phase extends Module {
    public Setting fullBlock;
    public Setting mode;

    public Phase() {
        super("Phase", Keyboard.KEY_J, Category.MOVEMENT);
        ArrayList<String> options = new ArrayList<>();
        options.add("Hypixel");
        options.add("Hypixel2");
        Execution.instance.settingsManager.rSetting(mode = new Setting("Phase mode", this, "Hypixel", options));
        Execution.instance.settingsManager.rSetting(fullBlock = new Setting("FullBlock", this, false));
    }

    boolean flagged = false;
    boolean join = false;

    @EventTarget
    public void onMove(EventMotion event) {
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {
            if (flagged) {
                if (isInsideBlock() && !fullBlock.getValBoolean()) {
                    if (getMc().gameSettings.keyBindJump.isKeyDown()) event.setY(getMc().thePlayer.motionY += 0.09f);
                    else if (getMc().gameSettings.keyBindSneak.isKeyDown())
                        event.setY(getMc().thePlayer.motionY -= 0.00);
                    else event.setY(getMc().thePlayer.motionY = 0.0f);
                    MoveUtils.setMotion(event, 1.635);
                } else {
                    MoveUtils.setMotion(event, 0.005);
                }
                if (fullBlock.getValBoolean()) {
                    if (mc.thePlayer.isCollidedHorizontally && mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.42;
                        C03PacketPlayer.C04PacketPlayerPosition pac = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + MoveUtils2.getPosForSetPosX(1.29), mc.thePlayer.posY, mc.thePlayer.posZ + MoveUtils2.getPosForSetPosZ(1.29), true);
                        for (int i = 0; i < 12; i++) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition());
                        }
                        MoveUtils.setPosPlusUpdate(MoveUtils2.getPosForSetPosX(1.29), 0, MoveUtils2.getPosForSetPosZ(1.29));
                        if (isInsideBlock()) {
                            mc.thePlayer.motionY = 0;
                        }

                    }
                }
            } else {
                mc.thePlayer.noClip = true;
                mc.thePlayer.noClipHorizontal = true;
                MoveUtils.setMotion(event, 0.05);
            }
        }else {
            if(mc.thePlayer.isCollidedHorizontally){


                for(int i = 0; i < 10; i++){
                    mc.thePlayer.sendQueue.addToSendQueueSilent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + MoveUtils2.getPosForSetPosX(i), 8.988465674311579, mc.thePlayer.posZ+ MoveUtils2.getPosForSetPosZ(i),false));
                }
                MoveUtils.setPosPlusUpdate(MoveUtils2.getPosForSetPosX(1.69), 0.42,MoveUtils2.getPosForSetPosZ(1.69));
            }else {
                mc.thePlayer.noClip = false;
            }
        }

    }

    @EventTarget
    public void onCollide(EventCollide event) {
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {

            if (isInsideBlock() && !fullBlock.getValBoolean()) {
                event.setBoundingBox(null);
            }
        }else {
            if (isInsideBlock()) {
                event.setBoundingBox(null);
            }
        }
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
        if(mode.getValString().equalsIgnoreCase("Hypixel")) {

            if (!event.isPre()) {
                double multiplier = 0.3;
                double mx = -Math.sin(Math.toRadians(getMc().thePlayer.rotationYaw));
                double mz = Math.cos(Math.toRadians(getMc().thePlayer.rotationYaw));
                double x = getMc().thePlayer.movementInput.moveForward * multiplier * mx + getMc().thePlayer.movementInput.moveStrafe * multiplier * mz;
                double z = getMc().thePlayer.movementInput.moveForward * multiplier * mz - getMc().thePlayer.movementInput.moveStrafe * multiplier * mx;
                if (getMc().thePlayer.isCollidedHorizontally && !getMc().thePlayer.isOnLadder()) {
                    getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX + x, getMc().thePlayer.posY, getMc().thePlayer.posZ + z, false));
                    for (int i = 1; i < 10; ++i) {
                        // getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, 8.988465674311579E307, getMc().thePlayer.posZ, false));
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, 8.988465674311579, getMc().thePlayer.posZ, false));
                    }
                    getMc().thePlayer.setPosition(getMc().thePlayer.posX + x, getMc().thePlayer.posY, getMc().thePlayer.posZ + z);
                }
            }
        }
    }


    public static boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minX); x < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minY); y < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.minZ); z < MathHelper.floor_double(Minecraft.getMinecraft().thePlayer.boundingBox.maxZ) + 1; z++) {
                    Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null && !(block instanceof BlockAir)) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Minecraft.getMinecraft().theWorld, new BlockPos(x, y, z), Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if (block instanceof BlockHopper)
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        if (boundingBox != null && Minecraft.getMinecraft().thePlayer.boundingBox.intersectsWith(boundingBox))
                            return true;
                    }
                }
            }
        }
        return false;
    }


    @EventTarget
    public void
    onReceivePacket(EventReceivePacket event) {
        if (
                event.
                        getPacket()
                        instanceof
                        S08PacketPlayerPosLook
        ) {
            flagged
                    =
                    true
            ;
        }
    }


    @Override
    public void onEnable() {
        flagged = false;
        super.onEnable();
    }
}
