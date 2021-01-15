/*    */ package net.minecraft.network;
/*    */ 
/*    */ import net.minecraft.util.IThreadListener;
/*    */ 
/*    */ public class PacketThreadUtil
/*    */ {
/*    */   public static <T extends INetHandler> void checkThreadAndEnqueue(Packet<T> p_180031_0_, final T p_180031_1_, IThreadListener p_180031_2_) throws ThreadQuickExitException
/*    */   {
/*  9 */     if (!p_180031_2_.isCallingFromMinecraftThread())
/*    */     {
/* 11 */       p_180031_2_.addScheduledTask(new Runnable()
/*    */       {
/*    */         public void run()
/*    */         {
/* 15 */           PacketThreadUtil.this.processPacket(p_180031_1_);
/*    */         }
/* 17 */       });
/* 18 */       throw ThreadQuickExitException.field_179886_a;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\PacketThreadUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */