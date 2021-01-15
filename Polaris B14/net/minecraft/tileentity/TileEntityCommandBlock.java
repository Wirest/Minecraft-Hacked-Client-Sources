/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.command.CommandResultStats;
/*    */ import net.minecraft.command.server.CommandBlockLogic;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class TileEntityCommandBlock extends TileEntity
/*    */ {
/* 16 */   private final CommandBlockLogic commandBlockLogic = new CommandBlockLogic()
/*    */   {
/*    */     public BlockPos getPosition()
/*    */     {
/* 20 */       return TileEntityCommandBlock.this.pos;
/*    */     }
/*    */     
/*    */     public Vec3 getPositionVector() {
/* 24 */       return new Vec3(TileEntityCommandBlock.this.pos.getX() + 0.5D, TileEntityCommandBlock.this.pos.getY() + 0.5D, TileEntityCommandBlock.this.pos.getZ() + 0.5D);
/*    */     }
/*    */     
/*    */     public World getEntityWorld() {
/* 28 */       return TileEntityCommandBlock.this.getWorld();
/*    */     }
/*    */     
/*    */     public void setCommand(String command) {
/* 32 */       super.setCommand(command);
/* 33 */       TileEntityCommandBlock.this.markDirty();
/*    */     }
/*    */     
/*    */     public void updateCommand() {
/* 37 */       TileEntityCommandBlock.this.getWorld().markBlockForUpdate(TileEntityCommandBlock.this.pos);
/*    */     }
/*    */     
/*    */     public int func_145751_f() {
/* 41 */       return 0;
/*    */     }
/*    */     
/*    */     public void func_145757_a(ByteBuf p_145757_1_) {
/* 45 */       p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getX());
/* 46 */       p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getY());
/* 47 */       p_145757_1_.writeInt(TileEntityCommandBlock.this.pos.getZ());
/*    */     }
/*    */     
/*    */     public Entity getCommandSenderEntity() {
/* 51 */       return null;
/*    */     }
/*    */   };
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound)
/*    */   {
/* 57 */     super.writeToNBT(compound);
/* 58 */     this.commandBlockLogic.writeDataToNBT(compound);
/*    */   }
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound)
/*    */   {
/* 63 */     super.readFromNBT(compound);
/* 64 */     this.commandBlockLogic.readDataFromNBT(compound);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Packet getDescriptionPacket()
/*    */   {
/* 73 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 74 */     writeToNBT(nbttagcompound);
/* 75 */     return new S35PacketUpdateTileEntity(this.pos, 2, nbttagcompound);
/*    */   }
/*    */   
/*    */   public boolean func_183000_F()
/*    */   {
/* 80 */     return true;
/*    */   }
/*    */   
/*    */   public CommandBlockLogic getCommandBlockLogic()
/*    */   {
/* 85 */     return this.commandBlockLogic;
/*    */   }
/*    */   
/*    */   public CommandResultStats getCommandResultStats()
/*    */   {
/* 90 */     return this.commandBlockLogic.getCommandResultStats();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\TileEntityCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */