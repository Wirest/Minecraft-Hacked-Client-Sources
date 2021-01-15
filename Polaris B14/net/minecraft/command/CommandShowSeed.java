/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandShowSeed
/*    */   extends CommandBase
/*    */ {
/*    */   public boolean canCommandSenderUseCommand(ICommandSender sender)
/*    */   {
/* 15 */     return (MinecraftServer.getServer().isSinglePlayer()) || (super.canCommandSenderUseCommand(sender));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandName()
/*    */   {
/* 23 */     return "seed";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 31 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 39 */     return "commands.seed.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 47 */     World world = (sender instanceof EntityPlayer) ? ((EntityPlayer)sender).worldObj : MinecraftServer.getServer().worldServerForDimension(0);
/* 48 */     sender.addChatMessage(new ChatComponentTranslation("commands.seed.success", new Object[] { Long.valueOf(world.getSeed()) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandShowSeed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */