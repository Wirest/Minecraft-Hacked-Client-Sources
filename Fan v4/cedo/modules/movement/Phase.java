package cedo.modules.movement;

import cedo.Wrapper;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.movement.MovementUtil;
import cedo.util.server.PacketUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;

public class Phase extends Module {

    ModeSetting phaseMode = new ModeSetting("Phase Mode", "New", "Old", "New");
    BooleanSetting holdDown = new BooleanSetting("Hold down", true);
    NumberSetting strength = new NumberSetting("Strength", 0.4, 0.2, 0.4, 0.01);

    public Phase() {
        super("Phase", Keyboard.KEY_NONE, Category.MOVEMENT);
        addSettings(phaseMode, holdDown, strength);
    }


    public void onDisable() {
        mc.thePlayer.stepHeight = 0.625f;
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            switch (phaseMode.getSelected()) {
                case "Old":
                    if (holdDown.isEnabled() && !Keyboard.isKeyDown(getKey())) {
                        if (Wrapper.getFuckedPrinceKin != 423499) {
                            PacketUtil.sendPacketNoEvent(new C13PacketPlayerAbilities(232, 321, false, true, false, true));
                        }
                        toggle();
                    }
                    mc.thePlayer.stepHeight = 0;
                    double mx = Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
                    double mz = Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90.0F));
                    double x = (double) mc.thePlayer.movementInput.moveForward * strength.getValue() * mx + (double) mc.thePlayer.movementInput.moveStrafe * strength.getValue() * mz;
                    double z = (double) mc.thePlayer.movementInput.moveForward * strength.getValue() * mz - (double) mc.thePlayer.movementInput.moveStrafe * strength.getValue() * mx;

                    if (mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isOnLadder()) {
                        mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z, false));
                        mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3, mc.thePlayer.posZ, false));
                        mc.thePlayer.setPosition(mc.thePlayer.posX + x, mc.thePlayer.posY, mc.thePlayer.posZ + z);
                    }
                    break;

                case "New":
                    if (holdDown.isEnabled() && !Keyboard.isKeyDown(getKey())) toggle();

                    mc.thePlayer.setSneaking(true);
                    if (mc.thePlayer.isCollidedHorizontally) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX + -Math.sin(Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationYaw)) * 0.01, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationYaw)) * 0.01);
                    } else if (isInsideBlock()) {
                        mc.thePlayer.setPosition(mc.thePlayer.posX + -Math.sin(Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationYaw)) * 0.3, mc.thePlayer.posY, mc.thePlayer.posZ + Math.cos(Math.toRadians((Minecraft.getMinecraft()).thePlayer.rotationYaw)) * 0.3);
                        MovementUtil.setSpeed(0);
                    }
                    break;
            }
        }
    }

    /**
     * Credits to minty codes
     **/
    public boolean isInsideBlock() {
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
}
