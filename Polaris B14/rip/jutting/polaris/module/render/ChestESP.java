/*     */ package rip.jutting.polaris.module.render;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.multiplayer.WorldClient;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityEnderChest;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.event.events.Event3D;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.utils.RenderUtils.R3DUtils;
/*     */ import rip.jutting.polaris.utils.RenderUtils.Stencil;
/*     */ 
/*     */ public class ChestESP extends Module
/*     */ {
/*     */   private net.minecraft.client.shader.Framebuffer blockFBO;
/*     */   
/*     */   public ChestESP()
/*     */   {
/*  33 */     super("ChestESP", 0, rip.jutting.polaris.module.Category.RENDER);
/*     */   }
/*     */   
/*     */   @rip.jutting.polaris.event.EventTarget
/*     */   private void onRender(Event3D event) {
/*  38 */     if ((event instanceof Event3D)) {
/*  39 */       Iterator var3 = mc.theWorld.loadedTileEntityList.iterator();
/*  40 */       while (var3.hasNext()) {
/*  41 */         Object o = var3.next();
/*  42 */         if ((o instanceof TileEntityChest)) {
/*  43 */           TileEntityChest entity7 = (TileEntityChest)o;
/*  44 */           Block x2 = mc.theWorld.getBlockState(entity7.getPos()).getBlock();
/*  45 */           Block border = mc.theWorld.getBlockState(new BlockPos(entity7.getPos().getX(), entity7.getPos().getY(), entity7.getPos().getZ() - 1)).getBlock();
/*  46 */           Block y2 = mc.theWorld.getBlockState(new BlockPos(entity7.getPos().getX(), entity7.getPos().getY(), entity7.getPos().getZ() + 1)).getBlock();
/*  47 */           Block border3 = mc.theWorld.getBlockState(new BlockPos(entity7.getPos().getX() - 1, entity7.getPos().getY(), entity7.getPos().getZ())).getBlock();
/*  48 */           Block z2 = mc.theWorld.getBlockState(new BlockPos(entity7.getPos().getX() + 1, entity7.getPos().getY(), entity7.getPos().getZ())).getBlock();
/*  49 */           double x = entity7.getPos().getX() - mc.getRenderManager().viewerPosX;
/*  50 */           double y = entity7.getPos().getY() - mc.getRenderManager().viewerPosY;
/*  51 */           double z = entity7.getPos().getZ() - mc.getRenderManager().viewerPosZ;
/*  52 */           GL11.glPushMatrix();
/*  53 */           RenderHelper.disableStandardItemLighting();
/*  54 */           if (x2 == Blocks.chest) {
/*  55 */             if (border != Blocks.chest) {
/*  56 */               if (y2 == Blocks.chest) {
/*  57 */                 draw(Blocks.chest, x, y, z, 1.0D, 1.0D, 2.0D);
/*  58 */               } else if (z2 == Blocks.chest) {
/*  59 */                 draw(Blocks.chest, x, y, z, 2.0D, 1.0D, 1.0D);
/*  60 */               } else if (z2 == Blocks.chest) {
/*  61 */                 draw(Blocks.chest, x, y, z, 1.0D, 1.0D, 1.0D);
/*  62 */               } else if ((border != Blocks.chest) && (y2 != Blocks.chest) && (border3 != Blocks.chest) && (z2 != Blocks.chest)) {
/*  63 */                 draw(Blocks.chest, x, y, z, 1.0D, 1.0D, 1.0D);
/*     */               }
/*     */             }
/*  66 */           } else if ((x2 == Blocks.trapped_chest) && (border != Blocks.trapped_chest)) {
/*  67 */             if (y2 == Blocks.trapped_chest) {
/*  68 */               draw(Blocks.trapped_chest, x, y, z, 1.0D, 1.0D, 2.0D);
/*  69 */             } else if (z2 == Blocks.trapped_chest) {
/*  70 */               draw(Blocks.trapped_chest, x, y, z, 2.0D, 1.0D, 1.0D);
/*  71 */             } else if (z2 == Blocks.trapped_chest) {
/*  72 */               draw(Blocks.trapped_chest, x, y, z, 1.0D, 1.0D, 1.0D);
/*  73 */             } else if ((border != Blocks.trapped_chest) && (y2 != Blocks.trapped_chest) && (border3 != Blocks.trapped_chest) && (z2 != Blocks.trapped_chest)) {
/*  74 */               draw(Blocks.trapped_chest, x, y, z, 1.0D, 1.0D, 1.0D);
/*     */             }
/*     */           }
/*     */           
/*  78 */           RenderHelper.enableStandardItemLighting();
/*  79 */           GL11.glPopMatrix();
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*  84 */         else if ((o instanceof TileEntityEnderChest)) {
/*  85 */           TileEntityEnderChest entity71 = (TileEntityEnderChest)o;
/*  86 */           double x21 = entity71.getPos().getX() - mc.getRenderManager().viewerPosX;
/*  87 */           double y21 = entity71.getPos().getY() - mc.getRenderManager().viewerPosY;
/*  88 */           double z21 = entity71.getPos().getZ() - mc.getRenderManager().viewerPosZ;
/*  89 */           GL11.glPushMatrix();
/*  90 */           RenderHelper.disableStandardItemLighting();
/*  91 */           draw(Blocks.ender_chest, x21, y21, z21, 1.0D, 1.0D, 1.0D);
/*  92 */           RenderHelper.enableStandardItemLighting();
/*  93 */           GL11.glPopMatrix();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void drawChestOutlines(TileEntity ent, float partialTicks)
/*     */   {
/* 102 */     int entityDispList = GL11.glGenLists(1);
/* 103 */     RenderUtils.Stencil.getInstance().startLayer();
/* 104 */     GL11.glPushMatrix();
/* 105 */     mc.entityRenderer.setupCameraTransform(partialTicks, 0);
/* 106 */     GL11.glMatrixMode(5888);
/* 107 */     RenderHelper.enableStandardItemLighting();
/* 108 */     GL11.glEnable(2884);
/* 109 */     Minecraft.getMinecraft();
/* 110 */     Frustum frustrum = new Frustum();
/* 111 */     frustrum.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
/* 112 */     GL11.glDisable(2929);
/* 113 */     GL11.glDepthMask(true);
/* 114 */     RenderUtils.Stencil.getInstance().setBuffer(true);
/* 115 */     GL11.glNewList(entityDispList, 4864);
/* 116 */     Iterator xd = mc.theWorld.loadedTileEntityList.iterator();
/*     */     
/* 118 */     while (xd.hasNext()) {
/* 119 */       Object obj = xd.next();
/* 120 */       TileEntity entity = (TileEntity)obj;
/* 121 */       if ((entity instanceof net.minecraft.tileentity.TileEntityLockable)) {
/* 122 */         GL11.glLineWidth(3.0F);
/* 123 */         GL11.glEnable(3042);
/* 124 */         GL11.glEnable(2848);
/* 125 */         GL11.glDisable(3553);
/* 126 */         GL11.glPushMatrix();
/* 127 */         GL11.glTranslated(entity.getPos().getX() - RenderManager.renderPosX, entity.getPos().getY() - RenderManager.renderPosY, entity.getPos().getZ() - RenderManager.renderPosZ);
/* 128 */         GL11.glColor4f(0.31F, 1.31F, 2.18F, 1.0F);
/* 129 */         TileEntityRendererDispatcher.instance.renderTileEntityAt(entity, 0.0D, 0.0D, 0.0D, partialTicks);
/* 130 */         GL11.glEnable(3553);
/* 131 */         GL11.glPopMatrix();
/*     */       }
/*     */     }
/*     */     
/* 135 */     GL11.glEndList();
/* 136 */     GL11.glPolygonMode(1032, 6913);
/* 137 */     GL11.glCallList(entityDispList);
/* 138 */     GL11.glPolygonMode(1032, 6912);
/* 139 */     GL11.glCallList(entityDispList);
/* 140 */     RenderUtils.Stencil.getInstance().setBuffer(false);
/* 141 */     GL11.glPolygonMode(1032, 6914);
/* 142 */     GL11.glCallList(entityDispList);
/* 143 */     RenderUtils.Stencil.getInstance().cropInside();
/* 144 */     GL11.glPolygonMode(1032, 6913);
/* 145 */     GL11.glCallList(entityDispList);
/* 146 */     GL11.glPolygonMode(1032, 6912);
/* 147 */     GL11.glCallList(entityDispList);
/* 148 */     GL11.glPolygonMode(1032, 6914);
/* 149 */     Minecraft.getMinecraft().entityRenderer.disableLightmap();
/* 150 */     RenderHelper.disableStandardItemLighting();
/* 151 */     Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
/* 152 */     RenderUtils.Stencil.getInstance().stopLayer();
/* 153 */     GL11.glDisable(2960);
/* 154 */     GL11.glPopMatrix();
/* 155 */     Minecraft.getMinecraft().entityRenderer.disableLightmap();
/* 156 */     RenderHelper.disableStandardItemLighting();
/* 157 */     Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
/* 158 */     GL11.glDeleteLists(entityDispList, 1);
/*     */   }
/*     */   
/*     */   public void draw(Block block, double x, double y, double z, double xo, double yo, double zo) {
/* 162 */     GL11.glDisable(2896);
/* 163 */     GL11.glDisable(3553);
/* 164 */     GL11.glEnable(3042);
/* 165 */     GL11.glBlendFunc(770, 771);
/* 166 */     GL11.glDisable(2929);
/* 167 */     GL11.glEnable(2848);
/* 168 */     GL11.glDepthMask(false);
/* 169 */     GL11.glLineWidth(0.75F);
/* 170 */     if (block == Blocks.ender_chest) {
/* 171 */       GL11.glColor4f(0.4F, 0.2F, 1.0F, 1.0F);
/* 172 */       x += 0.0650000000745058D;
/* 173 */       y += 0.0D;
/* 174 */       z += 0.06000000074505806D;
/* 175 */       xo -= 0.13000000149011612D;
/* 176 */       yo -= 0.1200000149011612D;
/* 177 */       zo -= 0.12000000149011612D;
/* 178 */     } else if (block == Blocks.chest) {
/* 179 */       GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
/* 180 */       x += 0.0650000000745058D;
/* 181 */       y += 0.0D;
/* 182 */       z += 0.06000000074505806D;
/* 183 */       xo -= 0.13000000149011612D;
/* 184 */       yo -= 0.1200000149011612D;
/* 185 */       zo -= 0.12000000149011612D;
/* 186 */     } else if (block == Blocks.trapped_chest) {
/* 187 */       GL11.glColor4f(1.0F, 0.6F, 0.0F, 1.0F);
/* 188 */       x += 0.0650000000745058D;
/* 189 */       y += 0.0D;
/* 190 */       z += 0.06000000074505806D;
/* 191 */       xo -= 0.13000000149011612D;
/* 192 */       yo -= 0.1200000149011612D;
/* 193 */       zo -= 0.12000000149011612D;
/*     */     }
/*     */     
/* 196 */     RenderUtils.R3DUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
/* 197 */     if (block == Blocks.ender_chest) {
/* 198 */       GL11.glColor4f(0.4F, 0.2F, 1.0F, 0.21F);
/* 199 */     } else if (block == Blocks.chest) {
/* 200 */       GL11.glColor4f(1.0F, 1.0F, 0.0F, 0.11F);
/* 201 */     } else if (block == Blocks.trapped_chest) {
/* 202 */       GL11.glColor4f(1.0F, 0.6F, 0.0F, 0.11F);
/*     */     }
/*     */     
/* 205 */     RenderUtils.R3DUtils.drawFilledBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
/* 206 */     if (block == Blocks.ender_chest) {
/* 207 */       GL11.glColor4f(0.4F, 0.2F, 1.0F, 1.0F);
/* 208 */     } else if (block == Blocks.chest) {
/* 209 */       GL11.glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
/* 210 */     } else if (block == Blocks.trapped_chest) {
/* 211 */       GL11.glColor4f(1.0F, 0.6F, 0.0F, 1.0F);
/*     */     }
/* 213 */     RenderUtils.R3DUtils.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + xo, y + yo, z + zo));
/* 214 */     GL11.glDepthMask(true);
/* 215 */     GL11.glDisable(2848);
/* 216 */     GL11.glEnable(2929);
/* 217 */     GL11.glDisable(3042);
/* 218 */     GL11.glEnable(2896);
/* 219 */     GL11.glEnable(3553);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\ChestESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */