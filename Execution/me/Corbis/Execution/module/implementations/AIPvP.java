package me.Corbis.Execution.module.implementations;

import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event3D;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.AStarCustomPathFinder;
import me.Corbis.Execution.utils.RotationUtils;
import me.Corbis.Execution.utils.Vec3;
import me.Corbis.Execution.utils.setBlockAndFacing;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class AIPvP extends Module {

    private ArrayList<Vec3> path = new ArrayList<>();
    EntityLivingBase target;
    public AIPvP(){
        super("AIPvP", Keyboard.KEY_NONE, Category.COMBAT);
    }
    int i = 0;
    @EventTarget
    public void onUpdate(EventMotionUpdate event){
        if(event.isPre()){
            target = getClosest(150);
            if(target != null){
                if(mc.thePlayer.isBurning()){
                    for(int i = 0; i < 9; i++){
                        if(mc.thePlayer.inventory.getStackInSlot(i) == null)
                            continue;
                        if(mc.thePlayer.inventory.getStackInSlot(i).getItem() == Items.water_bucket){
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                            mc.thePlayer.rotationPitch = 89;
                            mc.thePlayer.swingItem();
                            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                        }
                    }
                }
             /*   if(!target.isBurning() && mc.thePlayer.ticksExisted % 50 == 0){
                    for(int i = 0; i < 9; i++){
                        if(mc.thePlayer.inventory.getStackInSlot(i) == null)
                            continue;
                        if(mc.thePlayer.inventory.getStackInSlot(i).getItem() == Items.lava_bucket){
                            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                            float[] rots = setBlockAndFacing.BlockUtil.getDirectionToBlock((int) target.posX, (int)target.posY - 1, (int)target.posZ, EnumFacing.UP);
                            mc.thePlayer.rotationYaw = rots[0];
                            mc.thePlayer.rotationPitch = rots[1];
                            BlockPos pos = new BlockPos((int) target.posX, (int)target.posY - 1, (int)target.posZ);
                            mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), pos, EnumFacing.UP, new net.minecraft.util.Vec3(pos.getX() * 0.5, pos.getY() * 0.5, pos.getZ() * 0.5));
                        }
                    }
                }*/
                if(mc.thePlayer.getDistanceToEntity(target) < 8){
                    //TODO: Make Attack AI
                    if(mc.thePlayer.getDistanceToEntity(target) < 4) {

                        float[] rots = RotationUtils.getRotations(target);
                        mc.thePlayer.rotationYaw = rots[0] + ThreadLocalRandom.current().nextInt(-2, 2);
                        mc.thePlayer.rotationPitch = rots[1] + ThreadLocalRandom.current().nextInt(-2, 2);
                        if (mc.thePlayer.ticksExisted % ThreadLocalRandom.current().nextInt(2, 4) == 0) {
                            Execution.instance.addChatMessage("[AIPvP] Target found withing 3.5 blocks. Attacking Target HurtTime:" + target.hurtTime);
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, target);

                        }

                    }else {
                        float[] rots = RotationUtils.getRotations(target);
                        mc.thePlayer.rotationYaw = rots[0] + ThreadLocalRandom.current().nextInt(-2, 2);
                        mc.thePlayer.rotationPitch = rots[1] + ThreadLocalRandom.current().nextInt(-2, 2);
                        if (mc.thePlayer.ticksExisted % ThreadLocalRandom.current().nextInt(5, 7) == 0) {
                            for(int i = 0; i < 9; i++){
                                if(mc.thePlayer.inventory.getStackInSlot(i) == null)
                                    continue;
                                if(mc.thePlayer.inventory.getStackInSlot(i).getItem() == Items.fishing_rod){
                                    mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i));
                                    mc.thePlayer.swingItem();
                                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
                                }
                            }
                        }
                    }
                }else {
                    Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                    Vec3 to = new Vec3(target.posX, target.posY, target.posZ);
                    path = computePath(topFrom, to);
                    Vec3 vec = path.get(i);
                    int x = (int)vec.getX();
                    int y = (int)vec.getY();
                    int z = (int)vec.getZ();
                    float[] rots = setBlockAndFacing.BlockUtil.getDirectionToBlock(x, y, z, EnumFacing.UP);
                    mc.thePlayer.rotationYaw = rots[0];
                    mc.thePlayer.rotationPitch = rots[1];
                    mc.gameSettings.keyBindForward.pressed = true;
                    mc.gameSettings.keyBindSprint.pressed = true;
                    if(mc.thePlayer.isCollidedHorizontally && mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround){
                        mc.thePlayer.jump();
                        Execution.instance.addChatMessage("[AIPvP] Obstacle found. Jumping, Final Motion:" + ThreadLocalRandom.current().nextDouble(0.42, 0.44) + "e" + mc.thePlayer.motionY);

                    }
                    if((int) mc.thePlayer.posX == x && (int) mc.thePlayer.posY == y && (int) mc.thePlayer.posZ == z){
                        Execution.instance.addChatMessage("[AIPvP] Player's position Connected to Hub " + i);
                        i++;

                    }

                }
            }else {
                i = 0;
            }
        }
    }

    private EntityLivingBase getClosest(double range) {
        double dist = range;
        EntityLivingBase target = null;
        for (Object object : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) object;
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase player = (EntityLivingBase) entity;
                if (player instanceof EntityPlayer && player != mc.thePlayer && player.isEntityAlive() && AntiBot.isBot((EntityPlayer) player)) {
                    double currentDist = mc.thePlayer.getDistanceToEntity(player);
                    if (currentDist <= dist) {
                        dist = currentDist;
                        target = player;
                    }
                }
            }
        }
        return target;
    }
    @EventTarget
    public void on3D(Event3D event){
        if(target != null){
            for (int i = 0; i < path.size(); i++) {
                Vec3 pathElm = path.get(i);
                Vec3 pathOther = path.get(i < path.size() - 1 ? i + 1 : i);


                double x = pathElm.getX() + (pathElm.getX() - pathElm.getX()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
                double y = pathElm.getY() + (pathElm.getY() - pathElm.getY()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
                double z = pathElm.getZ() + (pathElm.getZ() - pathElm.getZ()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
                double x1 = pathOther.getX() + (pathOther.getX() - pathOther.getX()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
                double y1 = pathOther.getY() + (pathOther.getY() - pathOther.getY()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
                double z1 = pathOther.getZ() + (pathOther.getZ() - pathOther.getZ()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
                GlStateManager.color(0, 255, 0, 255);
                drawLine(new double[]{255, 255, 255}, x1, y1, z1, x, y, z);
                GlStateManager.color(1,1,1,1);

            }
        }
    }
    private void drawLine(double[] color, double x, double y, double z, double playerX, double playerY, double playerZ) {


        GlStateManager.color(0, 255, 0, 255);

        GL11.glLineWidth(5);
        GL11.glBegin(1);
        GL11.glVertex3d(playerX, playerY, playerZ);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(1,1,1,1);
    }
    double dashDistance = 5;


    private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!canPassThrow(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0, 1, 0);
        }
        AStarCustomPathFinder pathfinder = new AStarCustomPathFinder(topFrom, to);
        pathfinder.compute();

        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<Vec3>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > dashDistance * dashDistance) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    cordsLoop:
                    for (int x = (int) smallX; x <= bigX; x++) {
                        for (int y = (int) smallY; y <= bigY; y++) {
                            for (int z = (int) smallZ; z <= bigZ; z++) {
                                if (!AStarCustomPathFinder.checkPositionValidity(x, y, z, false)) {
                                    canContinue = false;
                                    break cordsLoop;
                                }
                            }
                        }
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            i++;
        }
        return path;
    }

    private boolean canPassThrow(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld.getBlockState(new net.minecraft.util.BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    @Override
    public void onEnable(){
        super.onEnable();
        this.i = 0;
    }

}
