/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelPlayer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerCape;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
/*     */ import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
/*     */ import net.minecraft.entity.player.EnumPlayerModelParts;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.EnumAction;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class RenderPlayer extends RendererLivingEntity<AbstractClientPlayer>
/*     */ {
/*     */   private boolean smallArms;
/*     */   
/*     */   public RenderPlayer(RenderManager renderManager)
/*     */   {
/*  28 */     this(renderManager, false);
/*     */   }
/*     */   
/*     */   public RenderPlayer(RenderManager renderManager, boolean useSmallArms)
/*     */   {
/*  33 */     super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
/*  34 */     this.smallArms = useSmallArms;
/*  35 */     addLayer(new LayerBipedArmor(this));
/*  36 */     addLayer(new LayerHeldItem(this));
/*  37 */     addLayer(new net.minecraft.client.renderer.entity.layers.LayerArrow(this));
/*  38 */     addLayer(new LayerDeadmau5Head(this));
/*  39 */     addLayer(new LayerCape(this));
/*  40 */     addLayer(new LayerCustomHead(getMainModel().bipedHead));
/*     */   }
/*     */   
/*     */   public ModelPlayer getMainModel()
/*     */   {
/*  45 */     return (ModelPlayer)super.getMainModel();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/*  56 */     if ((!entity.isUser()) || (this.renderManager.livingPlayer == entity))
/*     */     {
/*  58 */       double d0 = y;
/*     */       
/*  60 */       if ((entity.isSneaking()) && (!(entity instanceof EntityPlayerSP)))
/*     */       {
/*  62 */         d0 = y - 0.125D;
/*     */       }
/*     */       
/*  65 */       setModelVisibilities(entity);
/*  66 */       super.doRender(entity, x, d0, z, entityYaw, partialTicks);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setModelVisibilities(AbstractClientPlayer clientPlayer)
/*     */   {
/*  72 */     ModelPlayer modelplayer = getMainModel();
/*     */     
/*  74 */     if (clientPlayer.isSpectator())
/*     */     {
/*  76 */       modelplayer.setInvisible(false);
/*  77 */       modelplayer.bipedHead.showModel = true;
/*  78 */       modelplayer.bipedHeadwear.showModel = true;
/*     */     }
/*     */     else
/*     */     {
/*  82 */       ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
/*  83 */       modelplayer.setInvisible(true);
/*  84 */       modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
/*  85 */       modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
/*  86 */       modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
/*  87 */       modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
/*  88 */       modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
/*  89 */       modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
/*  90 */       modelplayer.heldItemLeft = 0;
/*  91 */       modelplayer.aimedBow = false;
/*  92 */       modelplayer.isSneak = clientPlayer.isSneaking();
/*     */       
/*  94 */       if (itemstack == null)
/*     */       {
/*  96 */         modelplayer.heldItemRight = 0;
/*     */       }
/*     */       else
/*     */       {
/* 100 */         modelplayer.heldItemRight = 1;
/*     */         
/* 102 */         if (clientPlayer.getItemInUseCount() > 0)
/*     */         {
/* 104 */           EnumAction enumaction = itemstack.getItemUseAction();
/*     */           
/* 106 */           if (enumaction == EnumAction.BLOCK)
/*     */           {
/* 108 */             modelplayer.heldItemRight = 3;
/*     */           }
/* 110 */           else if (enumaction == EnumAction.BOW)
/*     */           {
/* 112 */             modelplayer.aimedBow = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResourceLocation getEntityTexture(AbstractClientPlayer entity)
/*     */   {
/* 124 */     return entity.getLocationSkin();
/*     */   }
/*     */   
/*     */   public void transformHeldFull3DItemLayer()
/*     */   {
/* 129 */     GlStateManager.translate(0.0F, 0.1875F, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime)
/*     */   {
/* 138 */     float f = 0.9375F;
/* 139 */     GlStateManager.scale(f, f, f);
/*     */   }
/*     */   
/*     */   protected void renderOffsetLivingLabel(AbstractClientPlayer entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_)
/*     */   {
/* 144 */     if (p_177069_10_ < 100.0D)
/*     */     {
/* 146 */       Scoreboard scoreboard = entityIn.getWorldScoreboard();
/* 147 */       ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
/*     */       
/* 149 */       if (scoreobjective != null)
/*     */       {
/* 151 */         Score score = scoreboard.getValueFromObjective(entityIn.getName(), scoreobjective);
/* 152 */         renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
/* 153 */         y += getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * p_177069_9_;
/*     */       }
/*     */     }
/*     */     
/* 157 */     super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
/*     */   }
/*     */   
/*     */   public void renderRightArm(AbstractClientPlayer clientPlayer)
/*     */   {
/* 162 */     float f = 1.0F;
/* 163 */     GlStateManager.color(f, f, f);
/* 164 */     ModelPlayer modelplayer = getMainModel();
/* 165 */     setModelVisibilities(clientPlayer);
/* 166 */     modelplayer.swingProgress = 0.0F;
/* 167 */     modelplayer.isSneak = false;
/* 168 */     modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
/* 169 */     modelplayer.renderRightArm();
/*     */   }
/*     */   
/*     */   public void renderLeftArm(AbstractClientPlayer clientPlayer)
/*     */   {
/* 174 */     float f = 1.0F;
/* 175 */     GlStateManager.color(f, f, f);
/* 176 */     ModelPlayer modelplayer = getMainModel();
/* 177 */     setModelVisibilities(clientPlayer);
/* 178 */     modelplayer.isSneak = false;
/* 179 */     modelplayer.swingProgress = 0.0F;
/* 180 */     modelplayer.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
/* 181 */     modelplayer.renderLeftArm();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z)
/*     */   {
/* 189 */     if ((entityLivingBaseIn.isEntityAlive()) && (entityLivingBaseIn.isPlayerSleeping()))
/*     */     {
/* 191 */       super.renderLivingAt(entityLivingBaseIn, x + entityLivingBaseIn.renderOffsetX, y + entityLivingBaseIn.renderOffsetY, z + entityLivingBaseIn.renderOffsetZ);
/*     */     }
/*     */     else
/*     */     {
/* 195 */       super.renderLivingAt(entityLivingBaseIn, x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void rotateCorpse(AbstractClientPlayer bat, float p_77043_2_, float p_77043_3_, float partialTicks)
/*     */   {
/* 201 */     if ((bat.isEntityAlive()) && (bat.isPlayerSleeping()))
/*     */     {
/* 203 */       GlStateManager.rotate(bat.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
/* 204 */       GlStateManager.rotate(getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
/* 205 */       GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
/*     */     }
/*     */     else
/*     */     {
/* 209 */       super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */