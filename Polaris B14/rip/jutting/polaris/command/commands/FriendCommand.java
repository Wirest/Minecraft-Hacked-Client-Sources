/*    */ package rip.jutting.polaris.command.commands;
/*    */ 
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.command.Command;
/*    */ import rip.jutting.polaris.friend.FriendManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FriendCommand
/*    */   implements Command
/*    */ {
/*    */   public boolean run(String[] args)
/*    */   {
/* 16 */     if ((args.length == 3) || (args.length == 4)) {
/* 17 */       if ((args[1].equalsIgnoreCase("add")) && (args.length == 4))
/*    */       {
/* 19 */         if (FriendManager.isFriend(args[2])) {
/* 20 */           Polaris.sendMessage(args[2] + " is already your friend.");
/*    */         }
/*    */         
/* 23 */         String alias = args[3];
/*    */         
/* 25 */         FriendManager.addFriend(args[2], alias);
/* 26 */         Polaris.sendMessage("Added " + alias + " as a friend.");
/*    */       }
/* 28 */       else if ((args[1].equalsIgnoreCase("add")) && (args.length == 3))
/*    */       {
/* 30 */         if (FriendManager.isFriend(args[2])) {
/* 31 */           Polaris.sendMessage(args[2] + " is already your friend.");
/*    */         }
/*    */         
/* 34 */         String alias = args[2];
/*    */         
/* 36 */         FriendManager.addFriend(args[2], alias);
/* 37 */         Polaris.sendMessage("Added " + alias + " as a friend.");
/*    */       }
/* 39 */       else if (args[1].equalsIgnoreCase("del"))
/*    */       {
/* 41 */         if (FriendManager.isFriend(args[2])) {
/* 42 */           FriendManager.removeFriend(args[2]);
/* 43 */           Polaris.sendMessage("Removed " + args[2] + " as a friend.");
/*    */         }
/*    */         else
/*    */         {
/* 47 */           Polaris.sendMessage(args[2] + " is not your friend.");
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 52 */     return true;
/*    */   }
/*    */   
/*    */   public String usage()
/*    */   {
/* 57 */     return "-friend [add/del] [name] [alias]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\command\commands\FriendCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */