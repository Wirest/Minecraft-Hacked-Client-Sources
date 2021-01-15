/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.FrameTimer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.MovingObjectPosition.MovingObjectType;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import optfine.Reflector;
/*     */ import org.lwjgl.opengl.Display;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ public class GuiOverlayDebug extends Gui
/*     */ {
/*     */   private final Minecraft mc;
/*     */   private final FontRenderer fontRenderer;
/*     */   private static final String __OBFID = "CL_00001956";
/*     */   
/*     */   public GuiOverlayDebug(Minecraft mc)
/*     */   {
/*  42 */     this.mc = mc;
/*  43 */     this.fontRenderer = mc.fontRendererObj;
/*     */   }
/*     */   
/*     */   public void renderDebugInfo(ScaledResolution scaledResolutionIn)
/*     */   {
/*  48 */     this.mc.mcProfiler.startSection("debug");
/*  49 */     GlStateManager.pushMatrix();
/*  50 */     renderDebugInfoLeft();
/*  51 */     renderDebugInfoRight(scaledResolutionIn);
/*  52 */     GlStateManager.popMatrix();
/*  53 */     this.mc.mcProfiler.endSection();
/*     */   }
/*     */   
/*     */   private boolean isReducedDebug()
/*     */   {
/*  58 */     return (this.mc.thePlayer.hasReducedDebug()) || (this.mc.gameSettings.reducedDebugInfo);
/*     */   }
/*     */   
/*     */   protected void renderDebugInfoLeft()
/*     */   {
/*  63 */     List list = call();
/*     */     
/*  65 */     for (int i = 0; i < list.size(); i++)
/*     */     {
/*  67 */       String s = (String)list.get(i);
/*     */       
/*  69 */       if (!Strings.isNullOrEmpty(s))
/*     */       {
/*  71 */         int j = this.fontRenderer.FONT_HEIGHT;
/*  72 */         int k = this.fontRenderer.getStringWidth(s);
/*  73 */         boolean flag = true;
/*  74 */         int l = 2 + j * i;
/*  75 */         drawRect(1.0D, l - 1, 2 + k + 1, l + j - 1, -1873784752);
/*  76 */         this.fontRenderer.drawString(s, 2.0D, l, 14737632);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void renderDebugInfoRight(ScaledResolution p_175239_1_)
/*     */   {
/*  83 */     List list = getDebugInfoRight();
/*     */     
/*  85 */     for (int i = 0; i < list.size(); i++)
/*     */     {
/*  87 */       String s = (String)list.get(i);
/*     */       
/*  89 */       if (!Strings.isNullOrEmpty(s))
/*     */       {
/*  91 */         int j = this.fontRenderer.FONT_HEIGHT;
/*  92 */         int k = this.fontRenderer.getStringWidth(s);
/*  93 */         int l = ScaledResolution.getScaledWidth() - 2 - k;
/*  94 */         int i1 = 2 + j * i;
/*  95 */         drawRect(l - 1, i1 - 1, l + k + 1, i1 + j - 1, -1873784752);
/*  96 */         this.fontRenderer.drawString(s, l, i1, 14737632);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected List call()
/*     */   {
/* 103 */     BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
/*     */     
/* 105 */     if (isReducedDebug())
/*     */     {
/* 107 */       return Lists.newArrayList(new String[] { "Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + net.minecraft.client.ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("Chunk-relative: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 0xF), Integer.valueOf(blockpos.getY() & 0xF), Integer.valueOf(blockpos.getZ() & 0xF) }) });
/*     */     }
/*     */     
/*     */ 
/* 111 */     Entity entity = this.mc.getRenderViewEntity();
/* 112 */     EnumFacing enumfacing = entity.getHorizontalFacing();
/* 113 */     String s = "Invalid";
/*     */     
/* 115 */     switch (GuiOverlayDebug.1.field_178907_a[enumfacing.ordinal()])
/*     */     {
/*     */     case 1: 
/* 118 */       s = "Towards negative Z";
/* 119 */       break;
/*     */     
/*     */     case 2: 
/* 122 */       s = "Towards positive Z";
/* 123 */       break;
/*     */     
/*     */     case 3: 
/* 126 */       s = "Towards negative X";
/* 127 */       break;
/*     */     
/*     */     case 4: 
/* 130 */       s = "Towards positive X";
/*     */     }
/*     */     
/* 133 */     ArrayList arraylist = Lists.newArrayList(new String[] { "Minecraft 1.8.8 (" + this.mc.getVersion() + "/" + net.minecraft.client.ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.theWorld.getDebugLoadedEntities(), this.mc.theWorld.getProviderName(), "", String.format("XYZ: %.3f / %.5f / %.3f", new Object[] { Double.valueOf(this.mc.getRenderViewEntity().posX), Double.valueOf(this.mc.getRenderViewEntity().getEntityBoundingBox().minY), Double.valueOf(this.mc.getRenderViewEntity().posZ) }), String.format("Block: %d %d %d", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) }), String.format("Chunk: %d %d %d in %d %d %d", new Object[] { Integer.valueOf(blockpos.getX() & 0xF), Integer.valueOf(blockpos.getY() & 0xF), Integer.valueOf(blockpos.getZ() & 0xF), Integer.valueOf(blockpos.getX() >> 4), Integer.valueOf(blockpos.getY() >> 4), Integer.valueOf(blockpos.getZ() >> 4) }), String.format("Facing: %s (%s) (%.1f / %.1f)", new Object[] { enumfacing, s, Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationYaw)), Float.valueOf(MathHelper.wrapAngleTo180_float(entity.rotationPitch)) }) });
/*     */     
/* 135 */     if ((this.mc.theWorld != null) && (this.mc.theWorld.isBlockLoaded(blockpos)))
/*     */     {
/* 137 */       Chunk chunk = this.mc.theWorld.getChunkFromBlockCoords(blockpos);
/* 138 */       arraylist.add("Biome: " + chunk.getBiome(blockpos, this.mc.theWorld.getWorldChunkManager()).biomeName);
/* 139 */       arraylist.add("Light: " + chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(net.minecraft.world.EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(net.minecraft.world.EnumSkyBlock.BLOCK, blockpos) + " block)");
/* 140 */       DifficultyInstance difficultyinstance = this.mc.theWorld.getDifficultyForLocation(blockpos);
/*     */       
/* 142 */       if ((this.mc.isIntegratedServerRunning()) && (this.mc.getIntegratedServer() != null))
/*     */       {
/* 144 */         net.minecraft.entity.player.EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getConfigurationManager().getPlayerByUUID(this.mc.thePlayer.getUniqueID());
/*     */         
/* 146 */         if (entityplayermp != null)
/*     */         {
/* 148 */           difficultyinstance = entityplayermp.worldObj.getDifficultyForLocation(new BlockPos(entityplayermp));
/*     */         }
/*     */       }
/*     */       
/* 152 */       arraylist.add(String.format("Local Difficulty: %.2f (Day %d)", new Object[] { Float.valueOf(difficultyinstance.getAdditionalDifficulty()), Long.valueOf(this.mc.theWorld.getWorldTime() / 24000L) }));
/*     */     }
/*     */     
/* 155 */     if ((this.mc.entityRenderer != null) && (this.mc.entityRenderer.isShaderActive()))
/*     */     {
/* 157 */       arraylist.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
/*     */     }
/*     */     
/* 160 */     if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (this.mc.objectMouseOver.getBlockPos() != null))
/*     */     {
/* 162 */       BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
/* 163 */       arraylist.add(String.format("Looking at: %d %d %d", new Object[] { Integer.valueOf(blockpos1.getX()), Integer.valueOf(blockpos1.getY()), Integer.valueOf(blockpos1.getZ()) }));
/*     */     }
/*     */     
/* 166 */     return arraylist;
/*     */   }
/*     */   
/*     */ 
/*     */   protected List getDebugInfoRight()
/*     */   {
/* 172 */     long i = Runtime.getRuntime().maxMemory();
/* 173 */     long j = Runtime.getRuntime().totalMemory();
/* 174 */     long k = Runtime.getRuntime().freeMemory();
/* 175 */     long l = j - k;
/* 176 */     ArrayList arraylist = Lists.newArrayList(new String[] { String.format("Java: %s %dbit", new Object[] { System.getProperty("java.version"), Integer.valueOf(this.mc.isJava64bit() ? 64 : 32) }), String.format("Mem: % 2d%% %03d/%03dMB", new Object[] { Long.valueOf(l * 100L / i), Long.valueOf(bytesToMb(l)), Long.valueOf(bytesToMb(i)) }), String.format("Allocated: % 2d%% %03dMB", new Object[] { Long.valueOf(j * 100L / i), Long.valueOf(bytesToMb(j)) }), "", String.format("CPU: %s", new Object[] { net.minecraft.client.renderer.OpenGlHelper.func_183029_j() }), "", String.format("Display: %dx%d (%s)", new Object[] { Integer.valueOf(Display.getWidth()), Integer.valueOf(Display.getHeight()), GL11.glGetString(7936) }), GL11.glGetString(7937), GL11.glGetString(7938) });
/*     */     
/* 178 */     if (Reflector.FMLCommonHandler_getBrandings.exists())
/*     */     {
/* 180 */       Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
/* 181 */       arraylist.add("");
/* 182 */       arraylist.addAll((java.util.Collection)Reflector.call(object, Reflector.FMLCommonHandler_getBrandings, new Object[] { Boolean.valueOf(false) }));
/*     */     }
/*     */     
/* 185 */     if (isReducedDebug())
/*     */     {
/* 187 */       return arraylist;
/*     */     }
/*     */     
/*     */ 
/* 191 */     if ((this.mc.objectMouseOver != null) && (this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) && (this.mc.objectMouseOver.getBlockPos() != null))
/*     */     {
/* 193 */       BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/* 194 */       IBlockState iblockstate = this.mc.theWorld.getBlockState(blockpos);
/*     */       
/* 196 */       if (this.mc.theWorld.getWorldType() != net.minecraft.world.WorldType.DEBUG_WORLD)
/*     */       {
/* 198 */         iblockstate = iblockstate.getBlock().getActualState(iblockstate, this.mc.theWorld, blockpos);
/*     */       }
/*     */       
/* 201 */       arraylist.add("");
/* 202 */       arraylist.add(String.valueOf(Block.blockRegistry.getNameForObject(iblockstate.getBlock())));
/*     */       
/*     */       Map.Entry entry;
/*     */       String s;
/* 206 */       for (Iterator iterator = iblockstate.getProperties().entrySet().iterator(); iterator.hasNext(); arraylist.add(((IProperty)entry.getKey()).getName() + ": " + s))
/*     */       {
/* 208 */         entry = (Map.Entry)iterator.next();
/* 209 */         s = ((Comparable)entry.getValue()).toString();
/*     */         
/* 211 */         if (entry.getValue() == Boolean.TRUE)
/*     */         {
/* 213 */           s = EnumChatFormatting.GREEN + s;
/*     */         }
/* 215 */         else if (entry.getValue() == Boolean.FALSE)
/*     */         {
/* 217 */           s = EnumChatFormatting.RED + s;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 222 */     return arraylist;
/*     */   }
/*     */   
/*     */ 
/*     */   private void func_181554_e()
/*     */   {
/* 228 */     GlStateManager.disableDepth();
/* 229 */     FrameTimer frametimer = this.mc.func_181539_aj();
/* 230 */     int i = frametimer.func_181749_a();
/* 231 */     int j = frametimer.func_181750_b();
/* 232 */     long[] along = frametimer.func_181746_c();
/* 233 */     ScaledResolution scaledresolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
/* 234 */     int k = i;
/* 235 */     int l = 0;
/* 236 */     drawRect(0.0D, ScaledResolution.getScaledHeight() - 60, 240.0D, ScaledResolution.getScaledHeight(), -1873784752);
/*     */     
/* 238 */     while (k != j)
/*     */     {
/* 240 */       int i1 = frametimer.func_181748_a(along[k], 30);
/* 241 */       int j1 = func_181552_c(MathHelper.clamp_int(i1, 0, 60), 0, 30, 60);
/* 242 */       drawVerticalLine(l, ScaledResolution.getScaledHeight(), ScaledResolution.getScaledHeight() - i1, j1);
/* 243 */       l++;
/* 244 */       k = frametimer.func_181751_b(k + 1);
/*     */     }
/*     */     
/* 247 */     drawRect(1.0D, ScaledResolution.getScaledHeight() - 30 + 1, 14.0D, ScaledResolution.getScaledHeight() - 30 + 10, -1873784752);
/* 248 */     this.fontRenderer.drawString("60", 2.0D, ScaledResolution.getScaledHeight() - 30 + 2, 14737632);
/* 249 */     drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 30, -1);
/* 250 */     drawRect(1.0D, ScaledResolution.getScaledHeight() - 60 + 1, 14.0D, ScaledResolution.getScaledHeight() - 60 + 10, -1873784752);
/* 251 */     this.fontRenderer.drawString("30", 2.0D, ScaledResolution.getScaledHeight() - 60 + 2, 14737632);
/* 252 */     drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 60, -1);
/* 253 */     drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 1, -1);
/* 254 */     drawVerticalLine(0, ScaledResolution.getScaledHeight() - 60, ScaledResolution.getScaledHeight(), -1);
/* 255 */     drawVerticalLine(239, ScaledResolution.getScaledHeight() - 60, ScaledResolution.getScaledHeight(), -1);
/*     */     
/* 257 */     if (this.mc.gameSettings.limitFramerate <= 120)
/*     */     {
/* 259 */       drawHorizontalLine(0, 239, ScaledResolution.getScaledHeight() - 60 + this.mc.gameSettings.limitFramerate / 2, -16711681);
/*     */     }
/*     */     
/* 262 */     GlStateManager.enableDepth();
/*     */   }
/*     */   
/*     */   private int func_181552_c(int p_181552_1_, int p_181552_2_, int p_181552_3_, int p_181552_4_)
/*     */   {
/* 267 */     return p_181552_1_ < p_181552_3_ ? func_181553_a(-16711936, 65280, p_181552_1_ / p_181552_3_) : func_181553_a(65280, -65536, (p_181552_1_ - p_181552_3_) / (p_181552_4_ - p_181552_3_));
/*     */   }
/*     */   
/*     */   private int func_181553_a(int p_181553_1_, int p_181553_2_, float p_181553_3_)
/*     */   {
/* 272 */     int i = p_181553_1_ >> 24 & 0xFF;
/* 273 */     int j = p_181553_1_ >> 16 & 0xFF;
/* 274 */     int k = p_181553_1_ >> 8 & 0xFF;
/* 275 */     int l = p_181553_1_ & 0xFF;
/* 276 */     int i1 = p_181553_2_ >> 24 & 0xFF;
/* 277 */     int j1 = p_181553_2_ >> 16 & 0xFF;
/* 278 */     int k1 = p_181553_2_ >> 8 & 0xFF;
/* 279 */     int l1 = p_181553_2_ & 0xFF;
/* 280 */     int i2 = MathHelper.clamp_int((int)(i + (i1 - i) * p_181553_3_), 0, 255);
/* 281 */     int j2 = MathHelper.clamp_int((int)(j + (j1 - j) * p_181553_3_), 0, 255);
/* 282 */     int k2 = MathHelper.clamp_int((int)(k + (k1 - k) * p_181553_3_), 0, 255);
/* 283 */     int l2 = MathHelper.clamp_int((int)(l + (l1 - l) * p_181553_3_), 0, 255);
/* 284 */     return i2 << 24 | j2 << 16 | k2 << 8 | l2;
/*     */   }
/*     */   
/*     */   private static long bytesToMb(long bytes)
/*     */   {
/* 289 */     return bytes / 1024L / 1024L;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiOverlayDebug.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */