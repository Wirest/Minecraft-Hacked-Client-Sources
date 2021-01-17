// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.tileentity;

import net.minecraft.command.CommandResultStats;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.Entity;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import net.minecraft.util.BlockPos;
import net.minecraft.command.server.CommandBlockLogic;

public class TileEntityCommandBlock extends TileEntity
{
    private final CommandBlockLogic commandBlockLogic;
    
    public TileEntityCommandBlock() {
        this.commandBlockLogic = new CommandBlockLogic() {
            @Override
            public BlockPos getPosition() {
                return TileEntityCommandBlock.this.pos;
            }
            
            @Override
            public Vec3 getPositionVector() {
                return new Vec3(TileEntityCommandBlock.this.pos.getX() + 0.5, TileEntityCommandBlock.this.pos.getY() + 0.5, TileEntityCommandBlock.this.pos.getZ() + 0.5);
            }
            
            @Override
            public World getEntityWorld() {
                return TileEntityCommandBlock.this.getWorld();
            }
            
            @Override
            public void setCommand(final String command) {
                super.setCommand(command);
                TileEntityCommandBlock.this.markDirty();
            }
            
            @Override
            public void updateCommand() {
                TileEntityCommandBlock.this.getWorld().markBlockForUpdate(TileEntityCommandBlock.this.pos);
            }
            
            @Override
            public int func_145751_f() {
                return 0;
            }
            
            @Override
            public void func_145757_a(final ByteBuf p_145757_1_) {
                p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getX());
                p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getY());
                p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getZ());
            }
            
            @Override
            public Entity getCommandSenderEntity() {
                return null;
            }
        };
    }
    
    @Override
    public void writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        this.commandBlockLogic.writeDataToNBT(compound);
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.commandBlockLogic.readDataFromNBT(compound);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        final NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeToNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(this.pos, 2, nbttagcompound);
    }
    
    @Override
    public boolean func_183000_F() {
        return true;
    }
    
    public CommandBlockLogic getCommandBlockLogic() {
        return this.commandBlockLogic;
    }
    
    public CommandResultStats getCommandResultStats() {
        return this.commandBlockLogic.getCommandResultStats();
    }
}
