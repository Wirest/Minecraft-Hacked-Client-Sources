/*    */ package rip.jutting.polaris.ui.newconfig;
/*    */ 
/*    */ public final class Alt
/*    */ {
/*    */   private String mask;
/*    */   private final String username;
/*    */   private String password;
/*    */   
/*    */   public Alt(String username, String password) {
/* 10 */     this(username, password, "");
/*    */   }
/*    */   
/*    */   public Alt(String username, String password, String mask)
/*    */   {
/* 15 */     this.mask = "";
/* 16 */     this.username = username;
/* 17 */     this.password = password;
/* 18 */     this.mask = mask;
/*    */   }
/*    */   
/*    */   public String getMask() {
/* 22 */     return this.mask;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 26 */     return this.password;
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 30 */     return this.username;
/*    */   }
/*    */   
/*    */   public void setMask(String mask) {
/* 34 */     this.mask = mask;
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 38 */     this.password = password;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\newconfig\Alt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */