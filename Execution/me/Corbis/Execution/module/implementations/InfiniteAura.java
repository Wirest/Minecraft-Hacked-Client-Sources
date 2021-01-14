package me.Corbis.Execution.module.implementations;

import de.Hero.settings.Setting;
import me.Corbis.Execution.Execution;
import me.Corbis.Execution.event.EventTarget;
import me.Corbis.Execution.event.events.Event3D;
import me.Corbis.Execution.event.events.EventMotion;
import me.Corbis.Execution.event.events.EventMotionUpdate;
import me.Corbis.Execution.module.Category;
import me.Corbis.Execution.module.Module;
import me.Corbis.Execution.utils.AStarCustomPathFinder;
import me.Corbis.Execution.utils.TimeHelper;
import me.Corbis.Execution.utils.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InfiniteAura extends Module {
    public Setting maxtTargets;
    public Setting delay;
    public InfiniteAura(){
        super("InfiniteAura", Keyboard.KEY_NONE, Category.COMBAT);
        Execution.instance.settingsManager.rSetting(delay = new Setting("InfAura Delay",this, 20, 1, 1000, true));

        Execution.instance.settingsManager.rSetting(maxtTargets = new Setting("Max Targets",this, 1, 1, 10, true));
    }
    private ArrayList<Vec3> path = new ArrayList<>();
    private List<Vec3>[] test = new ArrayList[50];
    private List<EntityLivingBase> targets = new CopyOnWriteArrayList<>();
    private TimeHelper cps = new TimeHelper();
    public static TimeHelper timer = new TimeHelper();
    public static boolean canReach;
    int ticks = 0;

    @EventTarget
    public void onPre(EventMotionUpdate event){

        if(event.isPre()){
            targets = getTargets();

                if (targets.size() > 0) {
                    ticks++;
                    Execution.instance.addChatMessage(ticks + "");
                    test = new ArrayList[50];
                    for (int i = 0; i < (targets.size() > 1 ? maxtTargets.getValDouble() : targets.size()); i++) {
                        EntityLivingBase T = targets.get(i);
                        Vec3 topFrom = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
                        Vec3 to = new Vec3(T.posX, T.posY, T.posZ);

                        path = computePath(topFrom, to);
                        test[i] = path;
                        if(ticks == 19) {
                            for (Vec3 pathElm : path) {

                                event.setX(pathElm.getX());
                                event.setY(pathElm.getY());
                                event.setZ(pathElm.getZ());

                            }
                            Execution.instance.addChatMessage(ticks + "true");
                        }

                        if(ticks == 20) {

                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer, T);
                            Collections.reverse(path);
                            Execution.instance.addChatMessage(ticks + "true");
                        }

                        if(ticks >= 21) {
                            for (Vec3 pathElm : path) {
                                event.setX(pathElm.getX());
                                event.setY(pathElm.getY());
                                event.setZ(pathElm.getZ());
                            }
                            Execution.instance.addChatMessage(ticks + "true");
                            ticks = 0;
                        }
                    }
                    cps.reset();
                }
        }
    }
    @EventTarget
    public void on3D(Event3D event){
        if(targets.size() > 0){
            for (int i = 0; i < path.size(); i++) {
                Vec3 pathElm = path.get(i);
                Vec3 pathOther = path.get(i < path.size() - 1 ? i + 1 : i);


                double x = pathElm.getX() + (pathElm.getX() - pathElm.getX()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
                double y = pathElm.getY() + (pathElm.getY() - pathElm.getY()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
                double z = pathElm.getZ() + (pathElm.getZ() - pathElm.getZ()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
                double x1 = pathOther.getX() + (pathOther.getX() - pathOther.getX()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX;
                double y1 = pathOther.getY() + (pathOther.getY() - pathOther.getY()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY;
                double z1 = pathOther.getZ() + (pathOther.getZ() - pathOther.getZ()) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;
                drawLine(new double[]{255, 255, 255}, x1, y1, z1, x, y, z);

            }
        }
    }
    private void drawLine(double[] color, double x, double y, double z, double playerX, double playerY, double playerZ) {


        GlStateManager.color(255, 255, 255, 255);

        GL11.glLineWidth(5);
        GL11.glBegin(1);
        GL11.glVertex3d(playerX, playerY, playerZ);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glColor4f(1,1,1,1);
    }
    double dashDistance = 5;
    public boolean canAttack(EntityLivingBase player) {
        if(player == mc.thePlayer)
            return false;
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !Execution.instance.moduleManager.getModuleByName("Players").isEnabled)
                return false;
            if (player instanceof EntityAnimal && !Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled)
                return false;
            if (player instanceof EntityMob && !Execution.instance.moduleManager.getModuleByName("Mobs").isEnabled)
                return false;
            if (player instanceof EntityVillager && !Execution.instance.moduleManager.getModuleByName("Villagers").isEnabled)
                return false;

        }
        if(player instanceof EntityPlayer) {
            if (AntiBot.isBot((EntityPlayer) player))
                return false;
        }
        if (mc.thePlayer.isOnSameTeam(player) && Execution.instance.moduleManager.getModuleByName("Teams").isEnabled)
            return false;
        if (player.isInvisible() && !Execution.instance.moduleManager.getModuleByName("Invisibles").isEnabled)
            return false;

        return true;
    }

    private List<EntityLivingBase> getTargets() {
        List<EntityLivingBase> targets = new ArrayList<>();

        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) o;
                if (canAttack(entity)) {
                    targets.add(entity);
                }
            }
        }
        targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) * 1000 - o2.getDistanceToEntity(mc.thePlayer) * 1000));
        return targets;
    }
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


}
