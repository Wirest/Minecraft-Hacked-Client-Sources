/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandDifficulty
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 16 */     return "difficulty";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 24 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 32 */     return "commands.difficulty.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 40 */     if (args.length <= 0)
/*    */     {
/* 42 */       throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
/*    */     }
/*    */     
/*    */ 
/* 46 */     EnumDifficulty enumdifficulty = getDifficultyFromCommand(args[0]);
/* 47 */     MinecraftServer.getServer().setDifficultyForAllWorlds(enumdifficulty);
/* 48 */     notifyOperators(sender, this, "commands.difficulty.success", new Object[] { new ChatComponentTranslation(enumdifficulty.getDifficultyResourceKey(), new Object[0]) });
/*    */   }
/*    */   
/*    */   protected EnumDifficulty getDifficultyFromCommand(String p_180531_1_)
/*    */     throws CommandException, NumberInvalidException
/*    */   {
/* 54 */     return (!p_180531_1_.equalsIgnoreCase("peaceful")) && (!p_180531_1_.equalsIgnoreCase("p")) ? EnumDifficulty.EASY : (!p_180531_1_.equalsIgnoreCase("easy")) && (!p_180531_1_.equalsIgnoreCase("e")) ? EnumDifficulty.NORMAL : (!p_180531_1_.equalsIgnoreCase("normal")) && (!p_180531_1_.equalsIgnoreCase("n")) ? EnumDifficulty.HARD : (!p_180531_1_.equalsIgnoreCase("hard")) && (!p_180531_1_.equalsIgnoreCase("h")) ? EnumDifficulty.getDifficultyEnum(parseInt(p_180531_1_, 0, 3)) : EnumDifficulty.PEACEFUL;
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 59 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "peaceful", "easy", "normal", "hard" }) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandDifficulty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */