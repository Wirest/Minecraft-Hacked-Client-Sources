// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils.PathFinder;

import net.minecraft.entity.EntityLivingBase;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSign;
import me.CheerioFX.FusionX.utils.BlockUtils2;
import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;

public class PathFinder
{
    private final Minecraft mc;
    private final boolean invulnerable;
    private final boolean creativeFlying;
    protected final boolean flying;
    private final boolean immuneToFallDamage;
    private final boolean noSlowdownActive = true;
    private final boolean liquidwalk = true;
    private final boolean spider = false;
    protected boolean fallingAllowed;
    private final PathPos start;
    protected PathPos current;
    private final BlockPos goal;
    private final HashMap<PathPos, Float> costMap;
    protected final HashMap<PathPos, PathPos> prevPosMap;
    private final PathQueue queue;
    protected int thinkSpeed;
    protected int thinkTime;
    private int iterations;
    protected boolean done;
    protected boolean failed;
    private final ArrayList<PathPos> path;
    
    public PathFinder(final BlockPos goal) {
        this.mc = Minecraft.getMinecraft();
        this.invulnerable = this.mc.thePlayer.capabilities.isCreativeMode;
        this.creativeFlying = this.mc.thePlayer.capabilities.isFlying;
        this.flying = true;
        this.immuneToFallDamage = true;
        this.fallingAllowed = true;
        this.costMap = new HashMap<PathPos, Float>();
        this.prevPosMap = new HashMap<PathPos, PathPos>();
        this.queue = new PathQueue();
        this.thinkSpeed = 1024;
        this.thinkTime = 200;
        this.path = new ArrayList<PathPos>();
        this.start = new PathPos(new BlockPos(this.mc.thePlayer));
        this.goal = goal;
        this.costMap.put(this.start, 0.0f);
        this.queue.add(this.start, this.getHeuristic(this.start));
    }
    
    public PathFinder(final PathFinder pathFinder) {
        this(pathFinder.goal);
        this.thinkSpeed = pathFinder.thinkSpeed;
        this.thinkTime = pathFinder.thinkTime;
    }
    
    public void think() {
        if (this.done) {
            throw new IllegalStateException("Path was already found!");
        }
        int i;
        for (i = 0; i < this.thinkSpeed && !this.checkFailed(); ++i) {
            this.current = this.queue.poll();
            if (this.checkDone()) {
                return;
            }
            for (final PathPos next : this.getNeighbors(this.current)) {
                final float newCost = this.costMap.get(this.current) + this.getCost(this.current, next);
                if (this.costMap.containsKey(next) && this.costMap.get(next) <= newCost) {
                    continue;
                }
                this.costMap.put(next, newCost);
                this.prevPosMap.put(next, this.current);
                this.queue.add(next, newCost + this.getHeuristic(next));
            }
        }
        this.iterations += i;
    }
    
    protected boolean checkDone() {
        return this.done = this.goal.equals(this.current);
    }
    
    private boolean checkFailed() {
        return this.failed = (this.queue.isEmpty() || this.iterations >= this.thinkSpeed * this.thinkTime);
    }
    
    private ArrayList<PathPos> getNeighbors(final PathPos pos) {
        final ArrayList<PathPos> neighbors = new ArrayList<PathPos>();
        if (Math.abs(this.start.getX() - pos.getX()) > 256 || Math.abs(this.start.getZ() - pos.getZ()) > 256) {
            return neighbors;
        }
        final BlockPos north = pos.offsetNorth();
        final BlockPos east = pos.offsetEast();
        final BlockPos south = pos.offsetSouth();
        final BlockPos west = pos.offsetWest();
        final BlockPos northEast = north.offsetEast();
        final BlockPos southEast = south.offsetEast();
        final BlockPos southWest = south.offsetWest();
        final BlockPos northWest = north.offsetWest();
        final BlockPos up = pos.offsetUp();
        final BlockPos down = pos.offsetDown();
        final boolean flying = this.canFlyAt(pos);
        final boolean onGround = this.canBeSolid(down);
        if (flying || onGround || pos.isJumping() || this.canMoveSidewaysInMidairAt(pos) || this.canClimbUpAt(pos.offsetDown())) {
            if (this.checkHorizontalMovement(pos, north)) {
                neighbors.add(new PathPos(north));
            }
            if (this.checkHorizontalMovement(pos, east)) {
                neighbors.add(new PathPos(east));
            }
            if (this.checkHorizontalMovement(pos, south)) {
                neighbors.add(new PathPos(south));
            }
            if (this.checkHorizontalMovement(pos, west)) {
                neighbors.add(new PathPos(west));
            }
            if (this.checkDiagonalMovement(pos, EnumFacing.NORTH, EnumFacing.EAST)) {
                neighbors.add(new PathPos(northEast));
            }
            if (this.checkDiagonalMovement(pos, EnumFacing.SOUTH, EnumFacing.EAST)) {
                neighbors.add(new PathPos(southEast));
            }
            if (this.checkDiagonalMovement(pos, EnumFacing.SOUTH, EnumFacing.WEST)) {
                neighbors.add(new PathPos(southWest));
            }
            if (this.checkDiagonalMovement(pos, EnumFacing.NORTH, EnumFacing.WEST)) {
                neighbors.add(new PathPos(northWest));
            }
        }
        if (pos.getY() < 256 && this.canGoThrough(up.offsetUp()) && (flying || onGround || this.canClimbUpAt(pos)) && (flying || this.canClimbUpAt(pos) || this.goal.equals(up) || this.canSafelyStandOn(north) || this.canSafelyStandOn(east) || this.canSafelyStandOn(south) || this.canSafelyStandOn(west))) {
            neighbors.add(new PathPos(up, onGround));
        }
        if (pos.getY() > 0 && this.canGoThrough(down) && (flying || this.canFallBelow(pos))) {
            neighbors.add(new PathPos(down));
        }
        return neighbors;
    }
    
    private boolean checkHorizontalMovement(final BlockPos current, final BlockPos next) {
        return this.isPassable(next) && (this.canFlyAt(current) || this.canGoThrough(next.offsetDown()) || this.canSafelyStandOn(next.offsetDown()));
    }
    
    private boolean checkDiagonalMovement(final BlockPos current, final EnumFacing direction1, final EnumFacing direction2) {
        final BlockPos horizontal1 = current.offset(direction1);
        final BlockPos horizontal2 = current.offset(direction2);
        final BlockPos next = horizontal1.offset(direction2);
        return this.isPassable(horizontal1) && this.isPassable(horizontal2) && this.checkHorizontalMovement(current, next);
    }
    
    protected boolean isPassable(final BlockPos pos) {
        return this.canGoThrough(pos) && this.canGoThrough(pos.offsetUp()) && this.canGoAbove(pos.offsetDown());
    }
    
    protected boolean canBeSolid(final BlockPos pos) {
        final Material material = BlockUtils2.getMaterial(pos);
        final Block block = BlockUtils2.getBlock(pos);
        return (material.blocksMovement() && !(block instanceof BlockSign)) || block instanceof BlockLadder || material == Material.water || material == Material.lava;
    }
    
    private boolean canGoThrough(final BlockPos pos) {
        if (!this.mc.theWorld.isBlockLoaded(pos, false)) {
            return false;
        }
        final Material material = BlockUtils2.getMaterial(pos);
        final Block block = BlockUtils2.getBlock(pos);
        return (!material.blocksMovement() || block instanceof BlockSign) && !(block instanceof BlockTripWire) && !(block instanceof BlockPressurePlate) && (this.invulnerable || (material != Material.lava && material != Material.fire));
    }
    
    private boolean canGoAbove(final BlockPos pos) {
        final Block block = BlockUtils2.getBlock(pos);
        return !(block instanceof BlockFence) && !(block instanceof BlockWall) && !(block instanceof BlockFenceGate);
    }
    
    private boolean canSafelyStandOn(final BlockPos pos) {
        final Material material = BlockUtils2.getMaterial(pos);
        return this.canBeSolid(pos) && (this.invulnerable || (material != Material.cactus && material != Material.lava));
    }
    
    private boolean canFallBelow(final PathPos pos) {
        final BlockPos down2 = pos.offsetDown(2);
        if (this.canGoThrough(down2)) {
            return true;
        }
        if (!this.canSafelyStandOn(down2)) {
            return false;
        }
        if (this.immuneToFallDamage && this.fallingAllowed) {
            return true;
        }
        if (BlockUtils2.getBlock(down2) instanceof BlockSlime && this.fallingAllowed) {
            return true;
        }
        BlockPos prevPos = pos;
        for (int i = 0; i <= (this.fallingAllowed ? 3 : 1); ++i) {
            if (prevPos == null) {
                return true;
            }
            if (!pos.offsetUp(i).equals(prevPos)) {
                return true;
            }
            final Block prevBlock = BlockUtils2.getBlock(prevPos);
            if (prevBlock instanceof BlockLiquid || prevBlock instanceof BlockLadder || prevBlock instanceof BlockVine || prevBlock instanceof BlockWeb) {
                return true;
            }
            prevPos = this.prevPosMap.get(prevPos);
        }
        return false;
    }
    
    private boolean canFlyAt(final BlockPos pos) {
        return this.flying;
    }
    
    private boolean canClimbUpAt(final BlockPos pos) {
        final Block block = BlockUtils2.getBlock(pos);
        if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
            return false;
        }
        final BlockPos up = pos.offsetUp();
        return this.canBeSolid(pos.offsetNorth()) || this.canBeSolid(pos.offsetEast()) || this.canBeSolid(pos.offsetSouth()) || this.canBeSolid(pos.offsetWest()) || this.canBeSolid(up.offsetNorth()) || this.canBeSolid(up.offsetEast()) || this.canBeSolid(up.offsetSouth()) || this.canBeSolid(up.offsetWest());
    }
    
    private boolean canMoveSidewaysInMidairAt(final BlockPos pos) {
        final Block blockFeet = BlockUtils2.getBlock(pos);
        if (blockFeet instanceof BlockLiquid || blockFeet instanceof BlockLadder || blockFeet instanceof BlockVine || blockFeet instanceof BlockWeb) {
            return true;
        }
        final Block blockHead = BlockUtils2.getBlock(pos.offsetUp());
        return blockHead instanceof BlockLiquid || blockHead instanceof BlockWeb;
    }
    
    private float getCost(final BlockPos current, final BlockPos next) {
        float cost = 1.0f;
        if (current.getX() != next.getX() && current.getZ() != next.getZ()) {
            cost *= 1.4142135f;
        }
        final Material nextMaterial = BlockUtils2.getMaterial(next);
        if (nextMaterial == Material.lava) {
            cost *= 4.5395155f;
        }
        if (!this.canFlyAt(next) && BlockUtils2.getBlock(next.offsetDown()) instanceof BlockSoulSand) {
            cost *= 2.5f;
        }
        return cost;
    }
    
    private float getHeuristic(final BlockPos pos) {
        final float dx = Math.abs(pos.getX() - this.goal.getX());
        final float dy = Math.abs(pos.getY() - this.goal.getY());
        final float dz = Math.abs(pos.getZ() - this.goal.getZ());
        return 1.001f * (dx + dy + dz - 0.58578646f * Math.min(dx, dz));
    }
    
    public PathPos getCurrentPos() {
        return this.current;
    }
    
    public BlockPos getGoal() {
        return this.goal;
    }
    
    public Set<PathPos> getProcessedBlocks() {
        return this.prevPosMap.keySet();
    }
    
    public PathPos[] getQueuedBlocks() {
        return this.queue.toArray();
    }
    
    public int getQueueSize() {
        return this.queue.size();
    }
    
    public float getCost(final BlockPos pos) {
        return this.costMap.get(pos);
    }
    
    public BlockPos getPrevPos(final BlockPos pos) {
        return this.prevPosMap.get(pos);
    }
    
    public boolean isDone() {
        return this.done;
    }
    
    public boolean isFailed() {
        return this.failed;
    }
    
    public ArrayList<PathPos> formatPath() {
        if (!this.done && !this.failed) {
            throw new IllegalStateException("No path found!");
        }
        if (!this.path.isEmpty()) {
            throw new IllegalStateException("Path was already formatted!");
        }
        PathPos pos;
        if (!this.failed) {
            pos = this.current;
        }
        else {
            pos = this.start;
            for (final PathPos next : this.prevPosMap.keySet()) {
                if (this.getHeuristic(next) < this.getHeuristic(pos)) {
                    pos = next;
                }
            }
        }
        while (pos != null) {
            this.path.add(pos);
            pos = this.prevPosMap.get(pos);
        }
        Collections.reverse(this.path);
        return this.path;
    }
    
    public boolean isPathStillValid(final int index) {
        if (this.path.isEmpty()) {
            throw new IllegalStateException("Path is not formatted!");
        }
        for (int i = Math.max(1, index); i < this.path.size(); ++i) {
            if (!this.getNeighbors(this.path.get(i - 1)).contains(this.path.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public PathProcessor getProcessor() {
        if (this.flying) {
            return new TPPathProcessor(this.path, this.creativeFlying);
        }
        return new WalkPathProcessor(this.path);
    }
    
    public PathProcessor getInfiniteAuraProcessor(final EntityLivingBase e) {
        return new InfiniteauraPathProcessor(this.path, this.creativeFlying, e);
    }
    
    public PathProcessor getProcessor(final String type) {
        if (type.equalsIgnoreCase("walk")) {
            return new WalkPathProcessor(this.path);
        }
        if (type.equalsIgnoreCase("fly")) {
            return new FlyPathProcessor(this.path, this.creativeFlying);
        }
        if (type.equalsIgnoreCase("eggaura")) {
            return new EggauraPathProcessor(this.path, this.creativeFlying);
        }
        if (type.equalsIgnoreCase("tpnback") || type.equalsIgnoreCase("tpback")) {
            return new TPnBACKPathProcessor(this.path, this.creativeFlying);
        }
        return new TPPathProcessor(this.path, true);
    }
    
    public void setThinkSpeed(final int thinkSpeed) {
        this.thinkSpeed = thinkSpeed;
    }
    
    public void setThinkTime(final int thinkTime) {
        this.thinkTime = thinkTime;
    }
    
    public void setFallingAllowed(final boolean fallingAllowed) {
        this.fallingAllowed = fallingAllowed;
    }
}
