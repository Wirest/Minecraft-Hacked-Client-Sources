/*    */ package net.minecraft.block.material;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapColor
/*    */ {
/*  8 */   public static final MapColor[] mapColorArray = new MapColor[64];
/*  9 */   public static final MapColor airColor = new MapColor(0, 0);
/* 10 */   public static final MapColor grassColor = new MapColor(1, 8368696);
/* 11 */   public static final MapColor sandColor = new MapColor(2, 16247203);
/* 12 */   public static final MapColor clothColor = new MapColor(3, 13092807);
/* 13 */   public static final MapColor tntColor = new MapColor(4, 16711680);
/* 14 */   public static final MapColor iceColor = new MapColor(5, 10526975);
/* 15 */   public static final MapColor ironColor = new MapColor(6, 10987431);
/* 16 */   public static final MapColor foliageColor = new MapColor(7, 31744);
/* 17 */   public static final MapColor snowColor = new MapColor(8, 16777215);
/* 18 */   public static final MapColor clayColor = new MapColor(9, 10791096);
/* 19 */   public static final MapColor dirtColor = new MapColor(10, 9923917);
/* 20 */   public static final MapColor stoneColor = new MapColor(11, 7368816);
/* 21 */   public static final MapColor waterColor = new MapColor(12, 4210943);
/* 22 */   public static final MapColor woodColor = new MapColor(13, 9402184);
/* 23 */   public static final MapColor quartzColor = new MapColor(14, 16776437);
/* 24 */   public static final MapColor adobeColor = new MapColor(15, 14188339);
/* 25 */   public static final MapColor magentaColor = new MapColor(16, 11685080);
/* 26 */   public static final MapColor lightBlueColor = new MapColor(17, 6724056);
/* 27 */   public static final MapColor yellowColor = new MapColor(18, 15066419);
/* 28 */   public static final MapColor limeColor = new MapColor(19, 8375321);
/* 29 */   public static final MapColor pinkColor = new MapColor(20, 15892389);
/* 30 */   public static final MapColor grayColor = new MapColor(21, 5000268);
/* 31 */   public static final MapColor silverColor = new MapColor(22, 10066329);
/* 32 */   public static final MapColor cyanColor = new MapColor(23, 5013401);
/* 33 */   public static final MapColor purpleColor = new MapColor(24, 8339378);
/* 34 */   public static final MapColor blueColor = new MapColor(25, 3361970);
/* 35 */   public static final MapColor brownColor = new MapColor(26, 6704179);
/* 36 */   public static final MapColor greenColor = new MapColor(27, 6717235);
/* 37 */   public static final MapColor redColor = new MapColor(28, 10040115);
/* 38 */   public static final MapColor blackColor = new MapColor(29, 1644825);
/* 39 */   public static final MapColor goldColor = new MapColor(30, 16445005);
/* 40 */   public static final MapColor diamondColor = new MapColor(31, 6085589);
/* 41 */   public static final MapColor lapisColor = new MapColor(32, 4882687);
/* 42 */   public static final MapColor emeraldColor = new MapColor(33, 55610);
/* 43 */   public static final MapColor obsidianColor = new MapColor(34, 8476209);
/* 44 */   public static final MapColor netherrackColor = new MapColor(35, 7340544);
/*    */   
/*    */ 
/*    */   public final int colorValue;
/*    */   
/*    */   public final int colorIndex;
/*    */   
/*    */ 
/*    */   private MapColor(int index, int color)
/*    */   {
/* 54 */     if ((index >= 0) && (index <= 63))
/*    */     {
/* 56 */       this.colorIndex = index;
/* 57 */       this.colorValue = color;
/* 58 */       mapColorArray[index] = this;
/*    */     }
/*    */     else
/*    */     {
/* 62 */       throw new IndexOutOfBoundsException("Map colour ID must be between 0 and 63 (inclusive)");
/*    */     }
/*    */   }
/*    */   
/*    */   public int func_151643_b(int p_151643_1_)
/*    */   {
/* 68 */     int i = 220;
/*    */     
/* 70 */     if (p_151643_1_ == 3)
/*    */     {
/* 72 */       i = 135;
/*    */     }
/*    */     
/* 75 */     if (p_151643_1_ == 2)
/*    */     {
/* 77 */       i = 255;
/*    */     }
/*    */     
/* 80 */     if (p_151643_1_ == 1)
/*    */     {
/* 82 */       i = 220;
/*    */     }
/*    */     
/* 85 */     if (p_151643_1_ == 0)
/*    */     {
/* 87 */       i = 180;
/*    */     }
/*    */     
/* 90 */     int j = (this.colorValue >> 16 & 0xFF) * i / 255;
/* 91 */     int k = (this.colorValue >> 8 & 0xFF) * i / 255;
/* 92 */     int l = (this.colorValue & 0xFF) * i / 255;
/* 93 */     return 0xFF000000 | j << 16 | k << 8 | l;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\material\MapColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */