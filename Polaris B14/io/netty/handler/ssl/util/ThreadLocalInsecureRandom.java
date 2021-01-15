/*    */ package io.netty.handler.ssl.util;
/*    */ 
/*    */ import io.netty.util.internal.ThreadLocalRandom;
/*    */ import java.security.SecureRandom;
/*    */ import java.util.Random;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class ThreadLocalInsecureRandom
/*    */   extends SecureRandom
/*    */ {
/*    */   private static final long serialVersionUID = -8209473337192526191L;
/* 31 */   private static final SecureRandom INSTANCE = new ThreadLocalInsecureRandom();
/*    */   
/*    */   static SecureRandom current() {
/* 34 */     return INSTANCE;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getAlgorithm()
/*    */   {
/* 41 */     return "insecure";
/*    */   }
/*    */   
/*    */ 
/*    */   public void setSeed(byte[] seed) {}
/*    */   
/*    */ 
/*    */   public void setSeed(long seed) {}
/*    */   
/*    */   public void nextBytes(byte[] bytes)
/*    */   {
/* 52 */     random().nextBytes(bytes);
/*    */   }
/*    */   
/*    */   public byte[] generateSeed(int numBytes)
/*    */   {
/* 57 */     byte[] seed = new byte[numBytes];
/* 58 */     random().nextBytes(seed);
/* 59 */     return seed;
/*    */   }
/*    */   
/*    */   public int nextInt()
/*    */   {
/* 64 */     return random().nextInt();
/*    */   }
/*    */   
/*    */   public int nextInt(int n)
/*    */   {
/* 69 */     return random().nextInt(n);
/*    */   }
/*    */   
/*    */   public boolean nextBoolean()
/*    */   {
/* 74 */     return random().nextBoolean();
/*    */   }
/*    */   
/*    */   public long nextLong()
/*    */   {
/* 79 */     return random().nextLong();
/*    */   }
/*    */   
/*    */   public float nextFloat()
/*    */   {
/* 84 */     return random().nextFloat();
/*    */   }
/*    */   
/*    */   public double nextDouble()
/*    */   {
/* 89 */     return random().nextDouble();
/*    */   }
/*    */   
/*    */   public double nextGaussian()
/*    */   {
/* 94 */     return random().nextGaussian();
/*    */   }
/*    */   
/*    */   private static Random random() {
/* 98 */     return ThreadLocalRandom.current();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\util\ThreadLocalInsecureRandom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */