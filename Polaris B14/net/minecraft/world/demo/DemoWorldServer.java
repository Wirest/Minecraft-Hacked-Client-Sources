/*    */ package net.minecraft.world.demo;
/*    */ 
/*    */ import net.minecraft.profiler.Profiler;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.WorldServer;
/*    */ import net.minecraft.world.WorldSettings;
/*    */ import net.minecraft.world.WorldType;
/*    */ import net.minecraft.world.storage.ISaveHandler;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ public class DemoWorldServer extends WorldServer
/*    */ {
/* 13 */   private static final long demoWorldSeed = "North Carolina".hashCode();
/* 14 */   public static final WorldSettings demoWorldSettings = new WorldSettings(demoWorldSeed, net.minecraft.world.WorldSettings.GameType.SURVIVAL, true, false, WorldType.DEFAULT).enableBonusChest();
/*    */   
/*    */   public DemoWorldServer(MinecraftServer server, ISaveHandler saveHandlerIn, WorldInfo worldInfoIn, int dimensionId, Profiler profilerIn)
/*    */   {
/* 18 */     super(server, saveHandlerIn, worldInfoIn, dimensionId, profilerIn);
/* 19 */     this.worldInfo.populateFromWorldSettings(demoWorldSettings);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\demo\DemoWorldServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */