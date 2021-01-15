/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandWeather
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName()
/*    */   {
/* 17 */     return "weather";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRequiredPermissionLevel()
/*    */   {
/* 25 */     return 2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getCommandUsage(ICommandSender sender)
/*    */   {
/* 33 */     return "commands.weather.usage";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void processCommand(ICommandSender sender, String[] args)
/*    */     throws CommandException
/*    */   {
/* 41 */     if ((args.length >= 1) && (args.length <= 2))
/*    */     {
/* 43 */       int i = (300 + new Random().nextInt(600)) * 20;
/*    */       
/* 45 */       if (args.length >= 2)
/*    */       {
/* 47 */         i = parseInt(args[1], 1, 1000000) * 20;
/*    */       }
/*    */       
/* 50 */       World world = net.minecraft.server.MinecraftServer.getServer().worldServers[0];
/* 51 */       WorldInfo worldinfo = world.getWorldInfo();
/*    */       
/* 53 */       if ("clear".equalsIgnoreCase(args[0]))
/*    */       {
/* 55 */         worldinfo.setCleanWeatherTime(i);
/* 56 */         worldinfo.setRainTime(0);
/* 57 */         worldinfo.setThunderTime(0);
/* 58 */         worldinfo.setRaining(false);
/* 59 */         worldinfo.setThundering(false);
/* 60 */         notifyOperators(sender, this, "commands.weather.clear", new Object[0]);
/*    */       }
/* 62 */       else if ("rain".equalsIgnoreCase(args[0]))
/*    */       {
/* 64 */         worldinfo.setCleanWeatherTime(0);
/* 65 */         worldinfo.setRainTime(i);
/* 66 */         worldinfo.setThunderTime(i);
/* 67 */         worldinfo.setRaining(true);
/* 68 */         worldinfo.setThundering(false);
/* 69 */         notifyOperators(sender, this, "commands.weather.rain", new Object[0]);
/*    */       }
/*    */       else
/*    */       {
/* 73 */         if (!"thunder".equalsIgnoreCase(args[0]))
/*    */         {
/* 75 */           throw new WrongUsageException("commands.weather.usage", new Object[0]);
/*    */         }
/*    */         
/* 78 */         worldinfo.setCleanWeatherTime(0);
/* 79 */         worldinfo.setRainTime(i);
/* 80 */         worldinfo.setThunderTime(i);
/* 81 */         worldinfo.setRaining(true);
/* 82 */         worldinfo.setThundering(true);
/* 83 */         notifyOperators(sender, this, "commands.weather.thunder", new Object[0]);
/*    */       }
/*    */     }
/*    */     else
/*    */     {
/* 88 */       throw new WrongUsageException("commands.weather.usage", new Object[0]);
/*    */     }
/*    */   }
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
/*    */   {
/* 94 */     return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] { "clear", "rain", "thunder" }) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\command\CommandWeather.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */