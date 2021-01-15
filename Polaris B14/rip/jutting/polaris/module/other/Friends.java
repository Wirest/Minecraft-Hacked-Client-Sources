/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.event.events.EventMouse;
/*    */ import rip.jutting.polaris.friend.FriendManager;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ 
/*    */ public class Friends extends Module
/*    */ {
/*    */   public Friends()
/*    */   {
/* 15 */     super("Friends", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onPress(EventMouse event) {
/* 20 */     if ((event.getKey() == 2) && (mc.objectMouseOver != null) && (mc.objectMouseOver.entityHit != null) && ((mc.objectMouseOver.entityHit instanceof net.minecraft.entity.player.EntityPlayer))) {
/* 21 */       if (FriendManager.isFriend(mc.objectMouseOver.entityHit.getName())) {
/* 22 */         FriendManager.removeFriend(mc.objectMouseOver.entityHit.getName());
/* 23 */         Polaris.sendMessage("§aSuccessfully removed §f" + mc.objectMouseOver.entityHit.getName());
/*    */       }
/*    */       else {
/* 26 */         FriendManager.addFriend(mc.objectMouseOver.entityHit.getName(), mc.objectMouseOver.entityHit.getName());
/* 27 */         Polaris.sendMessage("§aSuccessfully added §f" + mc.objectMouseOver.entityHit.getName());
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Friends.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */