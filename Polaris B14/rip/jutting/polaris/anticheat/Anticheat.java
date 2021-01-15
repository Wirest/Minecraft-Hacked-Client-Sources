/*    */ package rip.jutting.polaris.anticheat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Anticheat
/*    */ {
/*  9 */   WATCHDOG(new String[] { "hypixel.net" }), 
/* 10 */   GWEN(new String[] { "mineplex.com" }), 
/* 11 */   NCP(new String[] { "hvh.gg", "poke.sexy", "envyclient.com" }), 
/* 12 */   AAC(new String[] { "gommehd" }), 
/* 13 */   AGC(new String[] { "mineman", "minemen", "wtap", "wtap.us", "minemen.club" }), 
/* 14 */   AREA51(new String[] { "faithfulmc", "faithful" }), 
/* 15 */   GUARDIAN(new String[] { "veltpvp", "arcane.cc", "pots.gg", "minehq" }), 
/* 16 */   SECURITY(new String[] { "security.combo.rip" }), 
/* 17 */   KOHI(new String[] { "kohi.life" }), 
/* 18 */   ZELIX(new String[] { "zelix", "zelix.us" }), 
/* 19 */   UNKNOWN(new String[] { "lmao.idk" }), 
/* 20 */   PVPLAND(new String[] { "pvp.land" }), 
/* 21 */   PVPTEMPLE(new String[] { "pvptemple.it" }), 
/* 22 */   SINGLEPLAYER(new String[] { "youhavenofriends.com" });
/*    */   
/*    */   String[] ips;
/*    */   
/*    */   private Anticheat(String[] ips) {
/* 27 */     this.ips = ips;
/*    */   }
/*    */   
/* 30 */   public String[] getIps() { return this.ips; }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\anticheat\Anticheat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */