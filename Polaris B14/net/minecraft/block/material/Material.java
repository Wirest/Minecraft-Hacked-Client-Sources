/*     */ package net.minecraft.block.material;
/*     */ 
/*     */ public class Material
/*     */ {
/*   5 */   public static final Material air = new MaterialTransparent(MapColor.airColor);
/*   6 */   public static final Material grass = new Material(MapColor.grassColor);
/*   7 */   public static final Material ground = new Material(MapColor.dirtColor);
/*   8 */   public static final Material wood = new Material(MapColor.woodColor).setBurning();
/*   9 */   public static final Material rock = new Material(MapColor.stoneColor).setRequiresTool();
/*  10 */   public static final Material iron = new Material(MapColor.ironColor).setRequiresTool();
/*  11 */   public static final Material anvil = new Material(MapColor.ironColor).setRequiresTool().setImmovableMobility();
/*  12 */   public static final Material water = new MaterialLiquid(MapColor.waterColor).setNoPushMobility();
/*  13 */   public static final Material lava = new MaterialLiquid(MapColor.tntColor).setNoPushMobility();
/*  14 */   public static final Material leaves = new Material(MapColor.foliageColor).setBurning().setTranslucent().setNoPushMobility();
/*  15 */   public static final Material plants = new MaterialLogic(MapColor.foliageColor).setNoPushMobility();
/*  16 */   public static final Material vine = new MaterialLogic(MapColor.foliageColor).setBurning().setNoPushMobility().setReplaceable();
/*  17 */   public static final Material sponge = new Material(MapColor.yellowColor);
/*  18 */   public static final Material cloth = new Material(MapColor.clothColor).setBurning();
/*  19 */   public static final Material fire = new MaterialTransparent(MapColor.airColor).setNoPushMobility();
/*  20 */   public static final Material sand = new Material(MapColor.sandColor);
/*  21 */   public static final Material circuits = new MaterialLogic(MapColor.airColor).setNoPushMobility();
/*  22 */   public static final Material carpet = new MaterialLogic(MapColor.clothColor).setBurning();
/*  23 */   public static final Material glass = new Material(MapColor.airColor).setTranslucent().setAdventureModeExempt();
/*  24 */   public static final Material redstoneLight = new Material(MapColor.airColor).setAdventureModeExempt();
/*  25 */   public static final Material tnt = new Material(MapColor.tntColor).setBurning().setTranslucent();
/*  26 */   public static final Material coral = new Material(MapColor.foliageColor).setNoPushMobility();
/*  27 */   public static final Material ice = new Material(MapColor.iceColor).setTranslucent().setAdventureModeExempt();
/*  28 */   public static final Material packedIce = new Material(MapColor.iceColor).setAdventureModeExempt();
/*  29 */   public static final Material snow = new MaterialLogic(MapColor.snowColor).setReplaceable().setTranslucent().setRequiresTool().setNoPushMobility();
/*     */   
/*     */ 
/*  32 */   public static final Material craftedSnow = new Material(MapColor.snowColor).setRequiresTool();
/*  33 */   public static final Material cactus = new Material(MapColor.foliageColor).setTranslucent().setNoPushMobility();
/*  34 */   public static final Material clay = new Material(MapColor.clayColor);
/*  35 */   public static final Material gourd = new Material(MapColor.foliageColor).setNoPushMobility();
/*  36 */   public static final Material dragonEgg = new Material(MapColor.foliageColor).setNoPushMobility();
/*  37 */   public static final Material portal = new MaterialPortal(MapColor.airColor).setImmovableMobility();
/*  38 */   public static final Material cake = new Material(MapColor.airColor).setNoPushMobility();
/*  39 */   public static final Material web = new Material(MapColor.clothColor)
/*     */   {
/*     */     public boolean blocksMovement()
/*     */     {
/*  43 */       return false;
/*     */     }
/*  45 */   }.setRequiresTool().setNoPushMobility();
/*     */   
/*     */ 
/*  48 */   public static final Material piston = new Material(MapColor.stoneColor).setImmovableMobility();
/*  49 */   public static final Material barrier = new Material(MapColor.airColor).setRequiresTool().setImmovableMobility();
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean canBurn;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean replaceable;
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isTranslucent;
/*     */   
/*     */ 
/*     */ 
/*     */   private final MapColor materialMapColor;
/*     */   
/*     */ 
/*     */ 
/*  69 */   private boolean requiresNoTool = true;
/*     */   
/*     */ 
/*     */   private int mobilityFlag;
/*     */   
/*     */ 
/*     */   private boolean isAdventureModeExempt;
/*     */   
/*     */ 
/*     */   public Material(MapColor color)
/*     */   {
/*  80 */     this.materialMapColor = color;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLiquid()
/*     */   {
/*  88 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSolid()
/*     */   {
/*  96 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean blocksLight()
/*     */   {
/* 104 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean blocksMovement()
/*     */   {
/* 112 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Material setTranslucent()
/*     */   {
/* 120 */     this.isTranslucent = true;
/* 121 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Material setRequiresTool()
/*     */   {
/* 129 */     this.requiresNoTool = false;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Material setBurning()
/*     */   {
/* 138 */     this.canBurn = true;
/* 139 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getCanBurn()
/*     */   {
/* 147 */     return this.canBurn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Material setReplaceable()
/*     */   {
/* 155 */     this.replaceable = true;
/* 156 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReplaceable()
/*     */   {
/* 164 */     return this.replaceable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaque()
/*     */   {
/* 172 */     return this.isTranslucent ? false : blocksMovement();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isToolNotRequired()
/*     */   {
/* 180 */     return this.requiresNoTool;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaterialMobility()
/*     */   {
/* 189 */     return this.mobilityFlag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Material setNoPushMobility()
/*     */   {
/* 197 */     this.mobilityFlag = 1;
/* 198 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Material setImmovableMobility()
/*     */   {
/* 206 */     this.mobilityFlag = 2;
/* 207 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Material setAdventureModeExempt()
/*     */   {
/* 215 */     this.isAdventureModeExempt = true;
/* 216 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMaterialMapColor()
/*     */   {
/* 224 */     return this.materialMapColor;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\material\Material.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */