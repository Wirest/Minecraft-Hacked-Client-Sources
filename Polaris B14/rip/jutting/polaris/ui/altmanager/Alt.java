/*    */ package rip.jutting.polaris.ui.altmanager;
/*    */ 
/*    */ 
/*    */ public class Alt
/*    */ {
/*    */   private String mask;
/*    */   
/*    */   private final String username;
/*    */   private String password;
/*    */   
/*    */   public Alt(String username, String password)
/*    */   {
/* 13 */     this(username, password, "");
/*    */   }
/*    */   
/*    */   public Alt(String username, String password, String mask)
/*    */   {
/* 18 */     this.mask = "";
/* 19 */     this.username = username;
/* 20 */     this.password = password;
/* 21 */     this.mask = mask;
/*    */   }
/*    */   
/*    */   public String getMask() {
/* 25 */     return this.mask;
/*    */   }
/*    */   
/*    */   public String getPassword() {
/* 29 */     return this.password;
/*    */   }
/*    */   
/*    */   public String getUsername() {
/* 33 */     return this.username;
/*    */   }
/*    */   
/*    */   public void setMask(String mask) {
/* 37 */     this.mask = mask;
/*    */   }
/*    */   
/*    */   public void setPassword(String password) {
/* 41 */     this.password = password;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\altmanager\Alt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */