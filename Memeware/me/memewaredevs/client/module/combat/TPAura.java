/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package me.memewaredevs.client.module.combat;

import com.google.common.collect.Lists;
import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.util.combat.CombatUtil;
import me.memewaredevs.client.util.misc.Timer;
import me.memewaredevs.client.util.pathfinding.Vec3;
import me.memewaredevs.client.util.pathfinding.PathFinder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TPAura extends Module {

    private Timer timer = new Timer();

    public TPAura() {
        super("TP Aura", 24, Module.Category.COMBAT);
        addInt("Targets", 1, 1, 250);
        addInt("CPS", 1, 1, 20);
        addBoolean("NoHitDelay Breaker", false);
    }

    private boolean canPassThrough(BlockPos pos) {
        Block block = Minecraft.getMinecraft().theWorld
                .getBlockState(new BlockPos(pos.getX(), pos.getY(), pos.getZ())).getBlock();
        return block.getMaterial() == Material.air || block.getMaterial() == Material.plants
                || block.getMaterial() == Material.vine || block == Blocks.ladder || block == Blocks.water
                || block == Blocks.flowing_water || block == Blocks.wall_sign || block == Blocks.standing_sign;
    }

    private ArrayList<Vec3> computePath(Vec3 topFrom, Vec3 to) {
        if (!canPassThrough(new BlockPos(topFrom.mc()))) {
            topFrom = topFrom.addVector(0.0, 1.0, 0.0);
        }
        PathFinder pathfinder = new PathFinder(topFrom, to);
        pathfinder.compute();
        int i = 0;
        Vec3 lastLoc = null;
        Vec3 lastDashLoc = null;
        ArrayList<Vec3> path = new ArrayList<>();
        ArrayList<Vec3> pathFinderPath = pathfinder.getPath();
        for (Vec3 pathElm : pathFinderPath) {
            if (i == 0 || i == pathFinderPath.size() - 1) {
                if (lastLoc != null) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                }
                path.add(pathElm.addVector(0.5, 0.0, 0.5));
                lastDashLoc = pathElm;
            } else {
                boolean canContinue = true;
                if (pathElm.squareDistanceTo(lastDashLoc) > 5 * 5) {
                    canContinue = false;
                } else {
                    double smallX = Math.min(lastDashLoc.getX(), pathElm.getX());
                    double smallY = Math.min(lastDashLoc.getY(), pathElm.getY());
                    double smallZ = Math.min(lastDashLoc.getZ(), pathElm.getZ());
                    double bigX = Math.max(lastDashLoc.getX(), pathElm.getX());
                    double bigY = Math.max(lastDashLoc.getY(), pathElm.getY());
                    double bigZ = Math.max(lastDashLoc.getZ(), pathElm.getZ());
                    int x = (int) smallX;
                    block1:
                    while (x <= bigX) {
                        int y2 = (int) smallY;
                        while (y2 <= bigY) {
                            int z = (int) smallZ;
                            while (z <= bigZ) {
                                if (!PathFinder.isValid(x, y2, z, false)) {
                                    canContinue = false;
                                    break block1;
                                }
                                ++z;
                            }
                            ++y2;
                        }
                        ++x;
                    }
                }
                if (!canContinue) {
                    path.add(lastLoc.addVector(0.5, 0.0, 0.5));
                    lastDashLoc = lastLoc;
                }
            }
            lastLoc = pathElm;
            ++i;
        }
        return path;
    }

    @Handler
    public Consumer<UpdateEvent> eventConsumer0 = event -> {
        if (timer.delay(1000L / getDouble("CPS").intValue())) {
            for (EntityLivingBase e : CombatUtil.getTargets(false, getDouble("Targets").intValue(), true, true, 218.0, true)) {
                ArrayList<Vec3> list = computePath(
                        new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ),
                        new Vec3(e.posX, e.posY, e.posZ));

                List<Vec3> backwards = Lists.reverse(list);

                for (Vec3 vec : list) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(),
                            vec.getY(), vec.getZ(), true));
                }

                mc.thePlayer.swingItem();
                if (getBool("NoHitDelay Breaker"))
                    for (int i : new int[10])
                        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                else
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
                for (Vec3 vec : backwards) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(vec.getX(),
                            vec.getY(), vec.getZ(), true));
                }
            }
            timer.reset();
        }
    };
}
