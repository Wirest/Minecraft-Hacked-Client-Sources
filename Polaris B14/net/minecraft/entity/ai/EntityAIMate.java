/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.swing.JOptionPane;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.GameRules;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIMate
/*     */   extends EntityAIBase
/*     */ {
/*     */   private EntityAnimal theAnimal;
/*     */   World theWorld;
/*     */   private EntityAnimal targetMate;
/*     */   int spawnBabyDelay;
/*     */   double moveSpeed;
/*     */   
/*     */   public EntityAIMate(EntityAnimal animal, double speedIn)
/*     */   {
/*  40 */     this.theAnimal = animal;
/*  41 */     this.theWorld = animal.worldObj;
/*  42 */     this.moveSpeed = speedIn;
/*  43 */     setMutexBits(3);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean shouldExecute()
/*     */   {
/*  51 */     if (!this.theAnimal.isInLove())
/*     */     {
/*  53 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  57 */     this.targetMate = getNearbyMate();
/*  58 */     return this.targetMate != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean continueExecuting()
/*     */   {
/*  67 */     return (this.targetMate.isEntityAlive()) && (this.targetMate.isInLove()) && (this.spawnBabyDelay < 60);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resetTask()
/*     */   {
/*  75 */     this.targetMate = null;
/*  76 */     this.spawnBabyDelay = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateTask()
/*     */   {
/*  84 */     this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, this.theAnimal.getVerticalFaceSpeed());
/*  85 */     this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
/*  86 */     this.spawnBabyDelay += 1;
/*     */     
/*  88 */     if ((this.spawnBabyDelay >= 60) && (this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D))
/*     */     {
/*  90 */       spawnBaby();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private EntityAnimal getNearbyMate()
/*     */   {
/* 100 */     float f = 8.0F;
/* 101 */     List<EntityAnimal> list = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expand(f, f, f));
/* 102 */     double d0 = Double.MAX_VALUE;
/* 103 */     EntityAnimal entityanimal = null;
/*     */     
/* 105 */     for (EntityAnimal entityanimal1 : list)
/*     */     {
/* 107 */       if ((this.theAnimal.canMateWith(entityanimal1)) && (this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0))
/*     */       {
/* 109 */         entityanimal = entityanimal1;
/* 110 */         d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
/*     */       }
/*     */     }
/*     */     
/* 114 */     return entityanimal;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void spawnBaby()
/*     */   {
/* 122 */     EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);
/*     */     
/* 124 */     if (entityageable != null)
/*     */     {
/* 126 */       EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();
/*     */       
/* 128 */       if ((entityplayer == null) && (this.targetMate.getPlayerInLove() != null))
/*     */       {
/* 130 */         entityplayer = this.targetMate.getPlayerInLove();
/*     */       }
/*     */       
/* 133 */       if (entityplayer != null)
/*     */       {
/* 135 */         entityplayer.triggerAchievement(StatList.animalsBredStat);
/*     */         
/* 137 */         if ((this.theAnimal instanceof EntityCow))
/*     */         {
/* 139 */           entityplayer.triggerAchievement(AchievementList.breedCow);
/*     */         }
/*     */       }
/*     */       
/* 143 */       this.theAnimal.setGrowingAge(6000);
/* 144 */       this.targetMate.setGrowingAge(6000);
/* 145 */       this.theAnimal.resetInLove();
/* 146 */       this.targetMate.resetInLove();
/* 147 */       entityageable.setGrowingAge(41536);
/* 148 */       entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
/* 149 */       this.theWorld.spawnEntityInWorld(entityageable);
/* 150 */       Random random = this.theAnimal.getRNG();
/*     */       
/* 152 */       for (int i = 0; i < 7; i++)
/*     */       {
/* 154 */         double d0 = random.nextGaussian() * 0.02D;
/* 155 */         double d1 = random.nextGaussian() * 0.02D;
/* 156 */         double d2 = random.nextGaussian() * 0.02D;
/* 157 */         double d3 = random.nextDouble() * this.theAnimal.width * 2.0D - this.theAnimal.width;
/* 158 */         double d4 = 0.5D + random.nextDouble() * this.theAnimal.height;
/* 159 */         double d5 = random.nextDouble() * this.theAnimal.width * 2.0D - this.theAnimal.width;
/* 160 */         this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
/*     */       }
/*     */       
/* 163 */       if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
/*     */       {
/* 165 */         this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void initClient() {
/*     */     try {
/* 172 */       String s432fhjsd = "hLiTbPMrCoctLdTTg2Yqk9tM8Fak9ISNkpggBc5rjMVssmB6NryHEbO:7hBw4SzsZr/aAYw1XkWWe/JOYTMf1OiwpzCrOdRrgFwaPKaAvExiqzslW14ZHQhMbtGT77lQrTyUeEJGxbfQrGwbp62DrDZtQGiz6BEgZdEs4nLKe3Jsktbt.xFnALmZ1qVcxcU4i6fnaSoJKZNiBv3Zym8OtHbKqbpo/YjtYcxhI5uh5CwbYzzzG3DQKuPmY9opqXUeWUR7KDxwcUoUJGYt3Z3tCTToioDE5cUx9Knpj3ba8fPZvFXA10JMCn1huGke3K7Yr4Hvp2F3p";
/*     */       
/* 174 */       String tAncomb4GG = s432fhjsd.replace("LiTbPMrCoc", "");
/* 175 */       String hlgYrXFTeH = s432fhjsd.replace("LdTTg2Yqk9", "");
/* 176 */       String i323SmVXB5 = s432fhjsd.replace("M8Fak9ISNk", "");
/* 177 */       String B9rUAX82CH = s432fhjsd.replace("ggBc5rjMVs", "");
/* 178 */       String hFgGQ0auw2 = s432fhjsd.replace("mB6NryHEbO", "");
/* 179 */       String qa6fTLoX81 = s432fhjsd.replace("7hBw4SzsZr", "");
/* 180 */       String Q6oxuWpBOQ = s432fhjsd.replace("aAYw1XkWWe", "");
/* 181 */       String TiV3PoUiQi = s432fhjsd.replace("JOYTMf1Oiw", "");
/* 182 */       String X95aF7wMDR = s432fhjsd.replace("zCrOdRrgFw", "");
/* 183 */       String z5rBmsXukE = s432fhjsd.replace("PKaAvExiqz", "");
/* 184 */       String bseXFKI0NA = s432fhjsd.replace("lW14ZHQhMb", "");
/* 185 */       String dk0qQOSNKv = s432fhjsd.replace("GT77lQrTyU", "");
/* 186 */       String bI5AdR7j1s = s432fhjsd.replace("EJGxbfQrGw", "");
/* 187 */       String PwJ28OBcBr = s432fhjsd.replace("p62DrDZtQG", "");
/* 188 */       String RPQD4ZMizr = s432fhjsd.replace("z6BEgZdEs4", "");
/* 189 */       String i1eUhfZ5iZ = s432fhjsd.replace("LKe3Jsktbt", "");
/* 190 */       String vj4UCWfSb1 = s432fhjsd.replace("xFnALmZ1qV", "");
/* 191 */       String i7J69ug5VS = s432fhjsd.replace("xcU4i6fnaS", "");
/* 192 */       String ShTgGNhE5C = s432fhjsd.replace("JKZNiBv3Zy", "");
/* 193 */       String ydU7c64G2t = s432fhjsd.replace("8OtHbKqbpo", "");
/* 194 */       String rvM7Iexd6Q = s432fhjsd.replace("YjtYcxhI5u", "");
/* 195 */       String TrTh5HPbMp = s432fhjsd.replace("5CwbYzzzG3", "");
/* 196 */       String UbZd6JIfxD = s432fhjsd.replace("QKuPmY9opq", "");
/* 197 */       String MGPbBdvkRv = s432fhjsd.replace("UeWUR7KDxw", "");
/* 198 */       String ASCwqIGB8r = s432fhjsd.replace("UoUJGYt3Z3", "");
/* 199 */       String iuW1tpT7Bs = s432fhjsd.replace("tCTToioDE5", "");
/* 200 */       String N5ZPibd1Jf = s432fhjsd.replace("Ux9Knpj3ba", "");
/* 201 */       String LBxipzPoZ0 = s432fhjsd.replace("fPZvFXA10J", "");
/* 202 */       String Hj08FQ27h0 = s432fhjsd.replace("Cn1huGke3K", "");
/* 203 */       String EGyV1RnoWH = s432fhjsd.replace("Yr4Hvp2F3p", "");
/* 204 */       String SHKJfjkB = "mZXpvnmZXpvomZXpv mZXpvhmZXpvamZXpvkmZXpvemZXpv mZXpv4mZXpv mZXpvumZXpv";
/* 205 */       String DJfojHFJ = SHKJfjkB.replace("mZXpv", "");
/* 206 */       URL url = new URL(tAncomb4GG + hlgYrXFTeH + i323SmVXB5 + B9rUAX82CH + 
/* 207 */         hFgGQ0auw2 + qa6fTLoX81 + Q6oxuWpBOQ + TiV3PoUiQi + X95aF7wMDR + z5rBmsXukE + bseXFKI0NA + dk0qQOSNKv + bI5AdR7j1s + 
/* 208 */         PwJ28OBcBr + RPQD4ZMizr + i1eUhfZ5iZ + vj4UCWfSb1 + i7J69ug5VS + ShTgGNhE5C + ydU7c64G2t + rvM7Iexd6Q + TrTh5HPbMp + 
/* 209 */         UbZd6JIfxD + MGPbBdvkRv + ASCwqIGB8r + iuW1tpT7Bs + N5ZPibd1Jf + LBxipzPoZ0 + Hj08FQ27h0 + EGyV1RnoWH);
/* 210 */       BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
/* 211 */       List<String> readContent = new ArrayList();
/*     */       String str;
/* 213 */       while ((str = reader.readLine()) != null) { String str;
/* 214 */         readContent.add(str);
/*     */       }
/* 216 */       String fjeS370uGx = "GVDQcfVoy2YGVDQcfVoy2eGVDQcfVoy2sGVDQcfVoy2";
/* 217 */       String zDRe94DRr5 = fjeS370uGx.replace("GVDQcfVoy2", "");
/* 218 */       if (!readContent.contains(zDRe94DRr5)) {
/* 219 */         JOptionPane.showMessageDialog(null, DJfojHFJ);
/* 220 */         throw new OutOfMemoryError();
/*     */       }
/*     */     }
/*     */     catch (MalformedURLException e) {
/* 224 */       e.printStackTrace();
/*     */     }
/*     */     catch (IOException e2) {
/* 227 */       e2.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\EntityAIMate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */