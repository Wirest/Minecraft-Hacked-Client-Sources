// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.init.Blocks;
import net.minecraft.util.ITickable;

public class TileEntityMobSpawner extends TileEntity implements ITickable
{
    private final MobSpawnerBaseLogic spawnerLogic;
    
    public TileEntityMobSpawner() {
        this.spawnerLogic = new MobSpawnerBaseLogic() {
            @Override
            public void func_98267_a(final int id) {
                TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.mob_spawner, id, 0);
            }
            
            @Override
            public World getSpawnerWorld() {
                return TileEntityMobSpawner.this.worldObj;
            }
            
            @Override
            public BlockPos getSpawnerPosition() {
                return TileEntityMobSpawner.this.pos;
            }
            
            @Override
            public void setRandomEntity(final WeightedRandomMinecart p_98277_1_) {
                super.setRandomEntity(p_98277_1_);
                if (this.getSpawnerWorld() != null) {
                    this.getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.pos);
                }
            }
        };
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.spawnerLogic.readFromNBT(compound);
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.spawnerLogic.writeToNBT(compound);
    }
    
    @Override
    public void update() {
        this.spawnerLogic.updateSpawner();
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        nbttagcompound.removeTag("SpawnPotentials");
        return new S35PacketUpdateTileEntity(this.pos, 1, nbttagcompound);
    }
    
    @Override
    public boolean receiveClientEvent(final int id, final int type) {
        return this.spawnerLogic.setDelayToMin(id) || super.receiveClientEvent(id, type);
    }
    
    @Override
    public boolean func_183000_F() {
        return true;
    }
    
    public MobSpawnerBaseLogic getSpawnerBaseLogic() {
        return this.spawnerLogic;
    }
}
