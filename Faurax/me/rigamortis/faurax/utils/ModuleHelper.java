package me.rigamortis.faurax.utils;

import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.block.*;
import net.minecraft.network.*;
import net.minecraft.inventory.*;
import net.minecraft.tileentity.*;
import java.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import net.minecraft.potion.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import me.rigamortis.faurax.friends.*;
import net.minecraft.enchantment.*;
import net.minecraft.network.play.client.*;

public class ModuleHelper extends Gui
{
    public Minecraft mc;
    public ArrayList<Entity> entities;
    public float delay;
    public int lookDelay;
    public float oldYaw;
    public float oldPitch;
    public float blockDamage;
    public float hitDelay;
    
    public ModuleHelper() {
        this.mc = Minecraft.getMinecraft();
        this.entities = new ArrayList<Entity>();
    }
    
    public int getBestToolForBlock(final BlockPos pos) {
        final Block block = this.mc.theWorld.getBlockState(pos).getBlock();
        int slot = 0;
        float damage = 0.1f;
        for (int index = 36; index < 45; ++index) {
            final ItemStack itemStack = this.mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (itemStack != null && block != null && itemStack.getItem().getStrVsBlock(itemStack, block) > damage) {
                slot = index - 36;
                damage = itemStack.getItem().getStrVsBlock(itemStack, block);
            }
        }
        if (damage > 0.1f) {
            return slot;
        }
        return this.mc.thePlayer.inventory.currentItem;
    }
    
    public boolean shouldHitBlock(final int x, final int y, final int z, final double dist) {
        final Block block = this.getBlock(x, y, z);
        final boolean isNotLiquid = !(block instanceof BlockLiquid);
        final boolean canSeeBlock = this.canBlockBeSeen(x, y, z);
        return isNotLiquid && canSeeBlock;
    }
    
    public void placeBlock(final int posX, final int posY, final int posZ) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(this.getBlockPos(posX, posY, posZ), this.getEnumFacing(posX, posY, posZ).getIndex(), this.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
    }
    
    public int getNextSlotInContainer(final Container container) {
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getInventory().get(i) != null) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean isContainerEmpty(final Container container) {
        for (int i = 0, slotAmount = (container.inventorySlots.size() == 90) ? 54 : 27; i < slotAmount; ++i) {
            if (container.getSlot(i).getHasStack()) {
                return false;
            }
        }
        return true;
    }
    
    public boolean canReach(final double posX, final double posY, final double posZ, final float distance) {
        final double blockDistance = this.getDistance(posX, posY, posZ);
        return blockDistance < distance && blockDistance > -distance;
    }
    
    public TileEntityChest getBestEntity(final double range) {
        TileEntityChest tempEntity = null;
        double dist = range;
        for (final Object i : this.mc.theWorld.loadedEntityList) {
            final TileEntityChest entity = (TileEntityChest)i;
            if (this.getDistanceToEntity(entity) <= 6.0f) {
                final double curDist = this.getDistanceToEntity(entity);
                if (curDist > dist) {
                    continue;
                }
                dist = curDist;
                tempEntity = entity;
            }
        }
        return tempEntity;
    }
    
    public float getDistanceToEntity(final TileEntity tileEntity) {
        float var2 = (float)(this.mc.thePlayer.posX - tileEntity.getPos().getX());
        var2 = (float)(this.mc.thePlayer.posY - tileEntity.getPos().getY());
        final float var3 = (float)(this.mc.thePlayer.posZ - tileEntity.getPos().getZ());
        return MathHelper.sqrt_float(var2 * var2 + var2 * var2 + var3 * var3);
    }
    
    public void breakBlockLegit(final int posX, final int posY, final int posZ, final int delay) {
        ++this.hitDelay;
        this.mc.thePlayer.swingItem();
        if (this.blockDamage == 0.0f) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.getBlockPos(posX, posY, posZ), this.getEnumFacing(posX, posY, posZ)));
        }
        if (this.hitDelay >= delay) {
            this.blockDamage += this.getBlock(posX, posY, posZ).getPlayerRelativeBlockHardness(this.mc.thePlayer, this.mc.theWorld, new BlockPos(posX, posY, posZ));
            this.mc.theWorld.sendBlockBreakProgress(this.mc.thePlayer.getEntityId(), new BlockPos(posX, posY, posZ), (int)(this.blockDamage * 10.0f) - 1);
            if (this.blockDamage >= (this.mc.playerController.isInCreativeMode() ? 0.0f : 1.0f)) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.getBlockPos(posX, posY, posZ), this.getEnumFacing(posX, posY, posZ)));
                this.mc.playerController.func_178888_a(this.getBlockPos(posX, posY, posZ), this.getEnumFacing(posX, posY, posZ));
                this.blockDamage = 0.0f;
                this.hitDelay = 0.0f;
            }
        }
    }
    
    public boolean canBlockBeSeen(final int posX, final int posY, final int posZ) {
        final MovingObjectPosition result = this.rayTrace(posX, posY, posZ);
        System.out.println(result.field_178784_b);
        return result == null || result.func_178782_a() != this.getBlockPos(posX, posY, posZ) || result.field_178784_b != null;
    }
    
    public MovingObjectPosition rayTrace(final int posX, final int posY, final int posZ) {
        final Vec3 player = new Vec3(this.mc.thePlayer.posX, this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ);
        final Vec3 block = new Vec3(posX + 0.5f, posY + 0.5f, posZ + 0.5f);
        return this.mc.theWorld.rayTraceBlocks(player, block);
    }
    
    public void breakBlock(final double posX, final double posY, final double posZ) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, this.getBlockPos(posX, posY, posZ), this.getEnumFacing((int)posX, (int)posY, (int)posZ)));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, this.getBlockPos(posX, posY, posZ), this.getEnumFacing((int)posX, (int)posY, (int)posZ)));
    }
    
    public EnumFacing getEnumFacing(final float posX, final float posY, final float posZ) {
        return EnumFacing.func_176737_a(posX, posY, posZ);
    }
    
    public BlockPos getBlockPos(final double x, final double y, final double z) {
        final BlockPos pos = new BlockPos(x, y, z);
        return pos;
    }
    
    public void moveForward() {
        this.mc.gameSettings.keyBindForward.pressed = true;
    }
    
    public void moveLeft() {
        this.mc.gameSettings.keyBindLeft.pressed = true;
    }
    
    public void moveRight() {
        this.mc.gameSettings.keyBindRight.pressed = true;
    }
    
    public void moveBack() {
        this.mc.gameSettings.keyBindBack.pressed = true;
    }
    
    public void stopMoving() {
        this.mc.gameSettings.keyBindForward.pressed = false;
        this.mc.gameSettings.keyBindLeft.pressed = false;
        this.mc.gameSettings.keyBindRight.pressed = false;
        this.mc.gameSettings.keyBindBack.pressed = false;
    }
    
    public Block getBlock(double posX, double posY, double posZ) {
        posX = MathHelper.floor_double(posX);
        posY = MathHelper.floor_double(posY);
        posZ = MathHelper.floor_double(posZ);
        return this.mc.theWorld.getChunkFromBlockCoords(new BlockPos(posX, posY, posZ)).getBlock(new BlockPos(posX, posY, posZ));
    }
    
    public float getDistance(final double x, final double y, final double z) {
        final float xDiff = (float)(this.mc.thePlayer.posX - x);
        final float yDiff = (float)(this.mc.thePlayer.posY - y);
        final float zDiff = (float)(this.mc.thePlayer.posZ - z);
        return MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public float getDistanceToVec(final double x, final double y, final double z, final double x2, final double y2, final double z2) {
        final float xDiff = (float)(x - x2);
        final float yDiff = (float)(y - y2);
        final float zDiff = (float)(z - z2);
        return MathHelper.sqrt_float(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
    }
    
    public void faceBlock(final double posX, final double posY, final double posZ) {
        final double diffX = posX - this.mc.thePlayer.posX;
        final double diffZ = posZ - this.mc.thePlayer.posZ;
        final double diffY = posY - (this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight());
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - this.mc.thePlayer.rotationPitch);
        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
        thePlayer2.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - this.mc.thePlayer.rotationYaw);
    }
    
    public boolean isVisibleFOV(final TileEntity tileEntity, final EntityPlayerSP thePlayer, final int fov) {
        return ((Math.abs(this.getRotationsTileEntity(tileEntity)[0] - thePlayer.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(this.getRotationsTileEntity(tileEntity)[0] - thePlayer.rotationYaw) % 360.0f) : (Math.abs(this.getRotationsTileEntity(tileEntity)[0] - thePlayer.rotationYaw) % 360.0f)) <= fov;
    }
    
    public void damage() {
        final double[] d = { 0.2, 0.26 };
        for (int a = 0; a < 60; ++a) {
            for (int i = 0; i < d.length; ++i) {
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + d[i], this.mc.thePlayer.posZ, false));
            }
        }
    }
    
    public String direction(final EntityLivingBase player) {
        final EnumFacing face = EnumFacing.getHorizontal(MathHelper.floor_double(player.rotationYaw * 4.0f / 360.0f + 0.5) & 0x3);
        int direction = face.getHorizontalIndex();
        if (this.mc.thePlayer.moveForward < 0.0f) {
            direction = face.getOpposite().getHorizontalIndex();
        }
        if (direction == 0) {
            return "SOUTH";
        }
        if (direction == 1) {
            return "WEST";
        }
        if (direction == 2) {
            return "NORTH";
        }
        if (direction == 3) {
            return "EAST";
        }
        return null;
    }
    
    public void saveAngles() {
        this.oldPitch = this.mc.thePlayer.rotationPitch;
        this.oldYaw = this.mc.thePlayer.rotationYaw;
    }
    
    public void resetAngles() {
        this.mc.thePlayer.rotationPitch = this.oldPitch;
        this.mc.thePlayer.rotationYaw = this.oldYaw;
    }
    
    public boolean isOnFire() {
        return (this.mc.thePlayer.isBurning() && !this.mc.thePlayer.isUsingItem() && !this.mc.thePlayer.isPotionActive(Potion.fireResistance) && !this.mc.thePlayer.isImmuneToFire() && this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isInsideOfMaterial(Material.lava) && !this.mc.thePlayer.isInsideOfMaterial(Material.fire)) || this.mc.thePlayer.isPotionActive(Potion.digSlowdown) || this.mc.thePlayer.isPotionActive(Potion.poison) || this.mc.thePlayer.isPotionActive(Potion.weakness) || this.mc.thePlayer.isPotionActive(Potion.moveSlowdown);
    }
    
    public Entity getBestEntity(final double range, final float fov, final boolean rayTrace, final int ticksExisted, final int invisibles, final int players, final int mobs, final int animals) {
        Entity tempEntity = null;
        double dist = range;
        for (final Object i : this.mc.theWorld.loadedEntityList) {
            final boolean isValidEntity = (mobs == 1 && i instanceof EntityMob && !((Entity)i).isInvisible() && !(i instanceof EntityAnimal) && !(i instanceof EntityPlayer)) || (animals == 1 && i instanceof EntityAnimal && !((Entity)i).isInvisible() && !(i instanceof EntityMob) && !(i instanceof EntityPlayer)) || (players == 1 && i instanceof EntityPlayer && !((Entity)i).isInvisible() && !(i instanceof EntityAnimal) && !(i instanceof EntityMob) && !FriendManager.isFriend(((Entity)i).getName())) || (invisibles == 1 && ((Entity)i).isInvisible() && !FriendManager.isFriend(((Entity)i).getName()));
            if (isValidEntity) {
                final Entity entity = (Entity)i;
                if (!this.shouldHitEntity(entity, range, fov, rayTrace, ticksExisted, invisibles, players, mobs, animals)) {
                    continue;
                }
                final double curDist = this.mc.thePlayer.getDistanceToEntity(entity);
                if (curDist > dist) {
                    continue;
                }
                dist = curDist;
                tempEntity = entity;
            }
        }
        return tempEntity;
    }
    
    public void clearEntities() {
        this.entities.clear();
    }
    
    public EntityLivingBase getClosestEntityToCursor(final float angle, final double range, final float fov, final boolean rayTrace, final int ticksExisted, final int invisibles, final int players, final int mobs, final int animals) {
        float distance = angle;
        EntityLivingBase tempEntity = null;
        for (final Object o : this.mc.theWorld.loadedEntityList) {
        	Entity entity = (Entity)o;
            if (!(entity instanceof EntityLivingBase)) {
                continue;
            }
            final EntityLivingBase living = (EntityLivingBase)entity;
            if (!this.shouldHitEntity(entity, range, fov, rayTrace, ticksExisted, invisibles, players, mobs, animals)) {
                continue;
            }
            final Float[] angles = this.getRotations(living);
            final float yaw = this.getDistanceBetweenAngles(this.mc.thePlayer.rotationYawHead, angles[0]);
            final float pitch = this.getDistanceBetweenAngles(this.mc.thePlayer.rotationPitch, angles[1]);
            if (yaw > angle) {
                continue;
            }
            if (pitch > angle) {
                continue;
            }
            final float curDistance = (yaw + pitch) / 2.0f;
            if (curDistance > distance) {
                continue;
            }
            distance = curDistance;
            tempEntity = living;
        }
        return tempEntity;
    }
    
    public boolean shouldHitEntity(final Entity e, final double range, final float fov, final boolean rayTrace, final int ticksExisted, final int invisibles, final int players, final int mobs, final int animals) {
        final boolean isAlive = e instanceof EntityLivingBase;
        final boolean isNotMe = e != this.mc.thePlayer;
        final boolean isNotNull = e != null;
        final boolean isInRange = this.mc.thePlayer.getDistanceToEntity(e) <= range;
        final boolean isInFov = this.isVisibleFOV(e, this.mc.thePlayer, fov);
        final boolean isNotDead = !e.isDead;
        final boolean canSeeEntity = this.mc.thePlayer.canEntityBeSeen(e);
        final boolean ticks = e.ticksExisted >= ticksExisted;
        final boolean isNotFakeDummie = e.getName() != this.mc.thePlayer.getName();
        if (rayTrace) {
            return isAlive && isNotFakeDummie && isNotDead && canSeeEntity && ticks && isInFov && isNotMe && isNotNull && isInRange;
        }
        return isAlive && isNotFakeDummie && isNotDead && ticks && isInFov && isNotMe && isNotNull && isInRange;
    }
    
    public float getDistanceBetweenAngles(final float ang1, final float ang2) {
        return Math.abs(((ang1 - ang2 + 180.0f) % 360.0f + 360.0f) % 360.0f - 180.0f);
    }
    
    public void attackTarget(final EntityLivingBase entity) {
        final float sharpLevel = EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), entity.getCreatureAttribute());
        this.mc.thePlayer.swingItem();
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (sharpLevel > 0.0f) {
            this.mc.thePlayer.onEnchantmentCritical(entity);
        }
    }
    
    public float getPitchChange(final Entity entity) {
        final double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        final double deltaY = entity.posY - 2.2 + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        final double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        final double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(Minecraft.getMinecraft().thePlayer.rotationPitch - (float)pitchToEntity) - 2.5f;
    }
    
    public float getYawChange(final Entity entity) {
        final double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        final double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0.0;
        if (deltaZ < 0.0 && deltaX < 0.0) {
            yawToEntity = 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else if (deltaZ < 0.0 && deltaX > 0.0) {
            yawToEntity = -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX));
        }
        else {
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }
        return MathHelper.wrapAngleTo180_float(-(Minecraft.getMinecraft().thePlayer.rotationYaw - (float)yawToEntity));
    }
    
    public boolean canCrit() {
        return !this.mc.thePlayer.isInWater() && this.mc.thePlayer.onGround;
    }
    
    public boolean needsHealth() {
        final boolean isLow = this.mc.thePlayer.getHealth() <= 20.0f;
        return isLow;
    }
    
    public void faceEntity(final Entity entity) {
        final double diffX = entity.posX - this.mc.thePlayer.posX;
        final double diffZ = entity.posZ - this.mc.thePlayer.posZ;
        final double diffY = entity.posY + entity.getEyeHeight() - this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight() * -2.0f + 1.2999999523162842;
        final double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        thePlayer.rotationPitch += MathHelper.wrapAngleTo180_float(pitch - this.mc.thePlayer.rotationPitch);
        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
        thePlayer2.rotationYaw += MathHelper.wrapAngleTo180_float(yaw - this.mc.thePlayer.rotationYaw);
    }
    
    public boolean isMouseOverEntity(final Entity entity) {
        return this.mc.objectMouseOver.entityHit == entity;
    }
    
    private Float[] getRotations(final Entity entity) {
        final double posX = entity.posX - this.mc.thePlayer.posX;
        final double posZ = entity.posZ - this.mc.thePlayer.posZ;
        final double posY = entity.posY + entity.getEyeHeight() - this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight();
        final double helper = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float newYaw = (float)Math.toDegrees(-Math.atan(posX / posZ));
        final float newPitch = (float)(-Math.toDegrees(Math.atan(posY / helper)));
        if (posZ < 0.0 && posX < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(posZ / posX)));
        }
        else if (posZ < 0.0 && posX > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(posZ / posX)));
        }
        return new Float[] { newYaw, newPitch };
    }
    
    private Float[] getRotationsTileEntity(final TileEntity entity) {
        final double posX = entity.getPos().getX() - this.mc.thePlayer.posX;
        final double posZ = entity.getPos().getY() - this.mc.thePlayer.posZ;
        final double posY = entity.getPos().getZ() + 1 - this.mc.thePlayer.posY + this.mc.thePlayer.getEyeHeight();
        final double helper = MathHelper.sqrt_double(posX * posX + posZ * posZ);
        float newYaw = (float)Math.toDegrees(-Math.atan(posX / posZ));
        final float newPitch = (float)(-Math.toDegrees(Math.atan(posY / helper)));
        if (posZ < 0.0 && posX < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(posZ / posX)));
        }
        else if (posZ < 0.0 && posX > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(posZ / posX)));
        }
        return new Float[] { newYaw, newPitch };
    }
    
    public boolean faceEntitySmooth(final Entity e, final float speed) {
        final double x = e.posX - this.mc.thePlayer.posX;
        final double y = e.posY - this.mc.thePlayer.posY;
        final double z = e.posZ - this.mc.thePlayer.posZ;
        final double d1 = this.mc.thePlayer.posY + Minecraft.getMinecraft().thePlayer.getEyeHeight() - (e.posY + e.getEyeHeight());
        final double d2 = MathHelper.sqrt_double(x * x + z * z);
        float f = (float)(Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f;
        float f2 = (float)(Math.atan2(d1, d2) * 180.0 / 3.141592653589793);
        f = MathHelper.wrapAngleTo180_float(f);
        f2 = MathHelper.wrapAngleTo180_float(f2);
        f -= MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationYaw);
        f2 -= MathHelper.wrapAngleTo180_float(this.mc.thePlayer.rotationPitch);
        final boolean facing = f < speed && f2 < speed && f > -speed && f2 > -speed;
        if (f > 0.0f) {
            f = MathHelper.clamp_float(f, 0.0f, speed);
        }
        else if (f < 0.0f) {
            f = MathHelper.clamp_float(f, -speed, 0.0f);
        }
        if (f2 > 0.0f) {
            f2 = MathHelper.clamp_float(f2, 0.0f, speed);
        }
        else if (f2 < 0.0f) {
            f2 = MathHelper.clamp_float(f2, -speed, 0.0f);
        }
        this.mc.thePlayer.rotationYaw += f;
        this.mc.thePlayer.rotationPitch += f2;
        return facing;
    }
    
    public boolean isVisibleFOV(final Entity e, final Entity e2, final float fov) {
        return ((Math.abs(this.getRotations(e)[0] - e2.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(this.getRotations(e)[0] - e2.rotationYaw) % 360.0f) : (Math.abs(this.getRotations(e)[0] - e2.rotationYaw) % 360.0f)) <= fov;
    }
    
    public boolean isVisibleFOVTileEntity(final TileEntity e, final Entity e2, final float fov) {
        return ((Math.abs(this.getRotationsTileEntity(e)[0] - e2.rotationYaw) % 360.0f > 180.0f) ? (360.0f - Math.abs(this.getRotationsTileEntity(e)[0] - e2.rotationYaw) % 360.0f) : (Math.abs(this.getRotationsTileEntity(e)[0] - e2.rotationYaw) % 360.0f)) <= fov;
    }
    
    public boolean canAttackEntityNotLegit(final Entity e) {
        if (e == null) {
            return false;
        }
        final boolean isNotMe = e != this.mc.thePlayer;
        final boolean isInRange = this.mc.thePlayer.getDistanceToEntity(e) <= 6.0f;
        final boolean isAlive = e instanceof EntityLivingBase && !e.isDead;
        return isNotMe && isInRange && isAlive;
    }
    
    public void attackEntityNotLegit(final Entity e) {
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.ATTACK));
    }
}
