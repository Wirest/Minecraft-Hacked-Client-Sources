package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class TileEntityCommandBlock extends TileEntity {
    private final CommandBlockLogic field_145994_a = new CommandBlockLogic() {
        private static final String __OBFID = "CL_00000348";

        @Override
        public BlockPos getPosition() {
            return TileEntityCommandBlock.this.pos;
        }

        @Override
        public Vec3 getPositionVector() {
            return new Vec3(TileEntityCommandBlock.this.pos.getX() + 0.5D, TileEntityCommandBlock.this.pos.getY() + 0.5D, TileEntityCommandBlock.this.pos.getZ() + 0.5D);
        }

        @Override
        public World getEntityWorld() {
            return TileEntityCommandBlock.this.getWorld();
        }

        @Override
        public void setCommand(String p_145752_1_) {
            super.setCommand(p_145752_1_);
            TileEntityCommandBlock.this.markDirty();
        }

        @Override
        public void func_145756_e() {
            TileEntityCommandBlock.this.getWorld().markBlockForUpdate(TileEntityCommandBlock.this.pos);
        }

        @Override
        public int func_145751_f() {
            return 0;
        }

        @Override
        public void func_145757_a(ByteBuf p_145757_1_) {
            p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getX());
            p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getY());
            p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getZ());
        }

        @Override
        public Entity getCommandSenderEntity() {
            return null;
        }
    };
    private static final String __OBFID = "CL_00000347";

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        field_145994_a.writeDataToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        field_145994_a.readDataFromNBT(compound);
    }

    /**
     * Overriden in a sign to provide the text.
     */
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound var1 = new NBTTagCompound();
        writeToNBT(var1);
        return new S35PacketUpdateTileEntity(pos, 2, var1);
    }

    public CommandBlockLogic getCommandBlockLogic() {
        return field_145994_a;
    }

    public CommandResultStats func_175124_c() {
        return field_145994_a.func_175572_n();
    }
}
