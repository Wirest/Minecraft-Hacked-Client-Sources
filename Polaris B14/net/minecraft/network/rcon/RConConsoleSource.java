/*    */ package net.minecraft.network.rcon;
/*    */ 
/*    */ import net.minecraft.command.CommandResultStats.Type;
/*    */ import net.minecraft.command.ICommandSender;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class RConConsoleSource
/*    */   implements ICommandSender
/*    */ {
/* 16 */   private static final RConConsoleSource instance = new RConConsoleSource();
/*    */   
/*    */ 
/* 19 */   private StringBuffer buffer = new StringBuffer();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 26 */     return "Rcon";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IChatComponent getDisplayName()
/*    */   {
/* 34 */     return new ChatComponentText(getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addChatMessage(IChatComponent component)
/*    */   {
/* 42 */     this.buffer.append(component.getUnformattedText());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*    */   {
/* 50 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BlockPos getPosition()
/*    */   {
/* 59 */     return new BlockPos(0, 0, 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Vec3 getPositionVector()
/*    */   {
/* 68 */     return new Vec3(0.0D, 0.0D, 0.0D);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public World getEntityWorld()
/*    */   {
/* 77 */     return MinecraftServer.getServer().getEntityWorld();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Entity getCommandSenderEntity()
/*    */   {
/* 85 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean sendCommandFeedback()
/*    */   {
/* 93 */     return true;
/*    */   }
/*    */   
/*    */   public void setCommandStat(CommandResultStats.Type type, int amount) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\rcon\RConConsoleSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */