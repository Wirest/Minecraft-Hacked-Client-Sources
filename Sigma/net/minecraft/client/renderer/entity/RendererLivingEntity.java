package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;

import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.EventSystem;
import info.sigmaclient.event.impl.EventNametagRender;
import info.sigmaclient.event.impl.EventRenderEntity;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.combat.BowAimbot;
import info.sigmaclient.module.impl.combat.Killaura;
import info.sigmaclient.module.impl.minigames.BedFucker;
import info.sigmaclient.module.impl.minigames.CakeEater;
import info.sigmaclient.module.impl.player.Scaffold;
import info.sigmaclient.module.impl.render.ESP;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public abstract class RendererLivingEntity extends Render {
    private static final Logger logger = LogManager.getLogger();
    private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
    protected ModelBase mainModel;
    protected FloatBuffer field_177095_g = GLAllocation.createDirectFloatBuffer(4);
    protected List field_177097_h = Lists.newArrayList();
    public static boolean field_177098_i = false;
    private static final String __OBFID = "CL_00001012";
    public static boolean renderLayers = true;
    public static boolean rendername = true;
    public RendererLivingEntity(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_) {
        super(p_i46156_1_);
        this.mainModel = p_i46156_2_;
        this.shadowSize = p_i46156_3_;
    }

    protected boolean addLayer(LayerRenderer p_177094_1_) {
        return this.field_177097_h.add(p_177094_1_);
    }

    protected boolean func_177089_b(LayerRenderer p_177089_1_) {
        return this.field_177097_h.remove(p_177089_1_);
    }

    public ModelBase getMainModel() {
        return this.mainModel;
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    protected float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
        float var4;

        for (var4 = p_77034_2_ - p_77034_1_; var4 < -180.0F; var4 += 360.0F) {
            ;
        }

        while (var4 >= 180.0F) {
            var4 -= 360.0F;
        }

        return p_77034_1_ + p_77034_3_ * var4;
    }

    public void func_82422_c() {
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(EntityLivingBase entity, double x, double y, double z, float yaw, float partialTicks) {
        EventRenderEntity em = (EventRenderEntity) EventSystem.getInstance(EventRenderEntity.class);
        em.fire(entity, true);
        if (em.isCancelled()) return;
        
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
        this.mainModel.isRiding = entity.isRiding();
        this.mainModel.isChild = entity.isChild();
        //TODO
        try {
            float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
            float f1 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
            float var20 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
            float f2 = f1 - f;
            float f3;

            if (entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase) {
                EntityLivingBase var13 = (EntityLivingBase) entity.ridingEntity;
                f = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, partialTicks);
                f2 = f1 - f;
                f3 = MathHelper.wrapAngleTo180_float(f2);

                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }

                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }
                f = f1 - f3;

                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }
            }

            if(entity instanceof EntityPlayerSP && yaw != 0){
            	float YAW = EventUpdate.YAW;
            	float PITCH = EventUpdate.PITCH;
                float PREVYAW = EventUpdate.PREVYAW;
                float PREVPITCH = EventUpdate.PREVPITCH;
                boolean sneaking = EventUpdate.SNEAKING;
                if(Killaura.target != null || Scaffold.yaw != 999 || (BowAimbot.target != null && BowAimbot.shouldAim()) ||
                		(CakeEater.blockBreaking != null && ((Options)Client.getModuleManager().get(CakeEater.class).getSetting(CakeEater.MODE).getValue()).getSelected().equalsIgnoreCase("Basic"))
                		|| BedFucker.blockBreaking != null){
                	 f = this.interpolateRotation(PREVYAW, YAW, partialTicks);
                }              
                
                float renderYaw = this.interpolateRotation(PREVYAW, YAW, partialTicks) - f;
                float renderPitch = this.interpolateRotation(PREVPITCH, PITCH, partialTicks);	
                f2 = renderYaw;
                var20 = renderPitch;
            }
            this.renderLivingAt(entity, x, y, z);
            float f7 = this.handleRotationFloat(entity, partialTicks);
            this.rotateCorpse(entity, f7, f, partialTicks);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            this.preRenderCallback(entity, partialTicks);
            float f4 = 0.0625F;
            GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
            float f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
            float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

            if (entity.isChild()) {
                f6 *= 3.0F;
            }

            if (f5 > 1.0F) {
                f5 = 1.0F;
            }

            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
            this.mainModel.setRotationAngles(f6, f5, f7, f2, var20, 0.0625F, entity);
            boolean flag1;

            if (this.field_177098_i) {
            	flag1 = this.func_177088_c(entity);
                this.renderModel(entity, f6, f5, f7, f2, var20, 0.0625F);

                if (flag1) {
                    this.func_180565_e();
                }
            } else {
            	flag1= this.func_177090_c(entity, partialTicks);

                this.renderModel(entity, f6, f5, f7, f2, var20, 0.0625F);
            	boolean valid = entity instanceof EntityMob || entity instanceof EntityIronGolem ||
						entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntityPlayer;
            	
            	if(valid){
                    Module espMod =  Client.getModuleManager().get(ESP.class);
                	boolean esp = espMod.isEnabled();
            		String mode = ((Options) espMod.getSetting(ESP.MODE).getValue()).getSelected();
            	   	if(esp && (mode.equalsIgnoreCase("CSGO") || mode.equalsIgnoreCase("Fill"))){
                    	if(ESP.isValid(entity)){
                       		Minecraft mc = Minecraft.getMinecraft();
                    		String colorMode =  ((Options) espMod.getSetting(ESP.COLORMODE).getValue()).getSelected();
                    		int renderColor = Client.cm.getESPColor().getColorInt();
                    		switch(colorMode){
     	                    case"Rainbow":{
     	                    	final Color color = Color.getHSBColor(ESP.h / 255.0f, 0.8f, 1.0f);
     	            			final int c = color.getRGB();
     	                    	renderColor = c;
     	                    }
     	                    break;
     	                    case"Team":{
     	                    String text = entity.getDisplayName().getFormattedText();
     	                	if(Character.toLowerCase(text.charAt(0)) == '§'){
     	                		
     	                    	char oneMore = Character.toLowerCase(text.charAt(1));
     	                    	int colorCode = "0123456789abcdefklmnorg".indexOf(oneMore);
     	                    	
     	                    	if (colorCode < 16) {
     	                            try {
     	                                int newColor = mc.fontRendererObj.colorCode[colorCode];   
     	                                 renderColor = Colors.getColor((newColor >> 16), (newColor >> 8 & 0xFF), (newColor & 0xFF), 255);
     	                            } catch (ArrayIndexOutOfBoundsException ignored) {
     	                            }
     	                        }
     	                	}else{
     	                		renderColor = Colors.getColor(255, 255, 255, 255);
     	                	}
     	                    }
     	                    break;
     	                    case"Health":{
     	                    	float health = entity.getHealth();
     	                        if (health > 20) {
     	                            health = 20;
     	                        }
     	                        float[] fractions = new float[]{0f, 0.5f, 1f};
     	                        Color[] colors = new Color[]{Color.RED, Color.YELLOW, Color.GREEN};
     	                        float progress = (health * 5) * 0.01f;
     	                        Color customColor = ESP.blendColors(fractions, colors, progress).brighter();
     	                        renderColor = customColor.getRGB();
     	                    }
     	                    break;
     	                    case"Custom":
     	                    	renderColor = Client.cm.getESPColor().getColorInt();
     	                    	break;
     	                    }        
     	                    if(entity.hurtResistantTime > 15 && colorMode.equalsIgnoreCase("Fill")){
     	                    	renderColor = Colors.getColor(1, 0, 0, 1);
     	                    }
     	                    if(AntiBot.getInvalid().contains(entity)){
     	                    	renderColor = Colors.getColor(100,100,100,255);
     	                    }
     	                    if(FriendManager.isFriend(entity.getName()) && !(entity instanceof EntityPlayerSP)){
     	                    	renderColor = Colors.getColor(0,195,255,255);
     	                    }                     
                        	mc.entityRenderer.func_175072_h();
                        	RenderingUtil.glColor(renderColor);
                    		GL11.glPushMatrix();
                            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                            GL11.glDisable(GL11.GL_TEXTURE_2D);
                            if(!mode.equalsIgnoreCase("CSGO")){
                           	 	RenderHelper.disableStandardItemLighting();
                            }
                            
                        	GL11.glEnable(32823);
                            GL11.glPolygonOffset(1.0f, -3900000.0f);
                            this.renderModel(entity, f6, f5, f7, f2, var20, 0.0625F);
                            GL11.glEnable(GL11.GL_TEXTURE_2D);
                            GL11.glEnable(GL11.GL_LIGHTING);
                            GL11.glEnable(GL11.GL_DEPTH_TEST);
                            if(!mode.equalsIgnoreCase("CSGO")){
                                GlStateManager.enableLighting();
                                GlStateManager.enableBooleanStateAt(0);
                                GlStateManager.enableBooleanStateAt(1);
                                GlStateManager.enableColorMaterial();
                            }
                            GL11.glPopMatrix();
                            mc.entityRenderer.func_175072_h();
                            RenderingUtil.glColor(-1);
                          
                    	}
                	}
            	}
         

                if (flag1) {
                    this.func_177091_f();
                }
            
                GlStateManager.depthMask(true);
       
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).func_175149_v()) {

                	this.func_177093_a(entity, f6, f5, partialTicks, f7, f2, var20, 0.0625F);
                	
                }
            	GL11.glDisable(32823);
            }

            GlStateManager.disableRescaleNormal();
        } catch (Exception var19) {
            logger.error("Couldn\'t render entity", var19);
        }

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();

        if (!this.field_177098_i) {
            super.doRender(entity, x, y, z, yaw, partialTicks);
        }
        em.fire(entity, false);
    }

    protected boolean func_177088_c(EntityLivingBase p_177088_1_) {
        int var2 = 16777215;

        if (p_177088_1_ instanceof EntityPlayer) {
            ScorePlayerTeam var3 = (ScorePlayerTeam) p_177088_1_.getTeam();

            if (var3 != null) {
                String var4 = FontRenderer.getFormatFromString(var3.getColorPrefix());

                if (var4.length() >= 2) {
                    var2 = this.getFontRendererFromRenderManager().func_175064_b(var4.charAt(1));
                }
            }
        }

        float var6 = (float) (var2 >> 16 & 255) / 255.0F;
        float var7 = (float) (var2 >> 8 & 255) / 255.0F;
        float var5 = (float) (var2 & 255) / 255.0F;
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(var6, var7, var5, 1.0F);
        GlStateManager.disableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected void func_180565_e() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        boolean var8 = !p_77036_1_.isInvisible();
        boolean var9 = !var8 && !p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);

        if (var8 || var9) {
            if (!this.bindEntityTexture(p_77036_1_)) {
                return;
            }

            if (var9) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);

            if (var9) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }

    protected boolean func_177090_c(EntityLivingBase p_177090_1_, float p_177090_2_) {
        return this.func_177092_a(p_177090_1_, p_177090_2_, true);
    }

    protected boolean func_177092_a(EntityLivingBase p_177092_1_, float p_177092_2_, boolean p_177092_3_) {
        float var4 = p_177092_1_.getBrightness(p_177092_2_);
        int var5 = this.getColorMultiplier(p_177092_1_, var4, p_177092_2_);
        boolean var6 = (var5 >> 24 & 255) > 0;
        boolean var7 = p_177092_1_.hurtTime > 0 || p_177092_1_.deathTime > 0;

        if (!var6 && !var7) {
            return false;
        } else if (!var6 && !p_177092_3_) {
            return false;
        } else {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTextures();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.field_176095_s);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176099_x, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176098_y, OpenGlHelper.defaultTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176097_z, OpenGlHelper.field_176093_u);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176081_B, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176082_C, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176077_E, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176078_F, OpenGlHelper.defaultTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176085_I, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTextures();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.field_176095_s);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176099_x, OpenGlHelper.field_176094_t);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176098_y, OpenGlHelper.field_176092_v);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176080_A, OpenGlHelper.field_176092_v);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176081_B, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176082_C, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176076_D, GL11.GL_SRC_ALPHA);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176077_E, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176078_F, OpenGlHelper.field_176091_w);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176085_I, GL11.GL_SRC_ALPHA);
            this.field_177095_g.position(0);

            if (var7) {
                this.field_177095_g.put(1.0F);
                this.field_177095_g.put(0.0F);
                this.field_177095_g.put(0.0F);
                this.field_177095_g.put(0.3F);
            } else {
                float var8 = (float) (var5 >> 24 & 255) / 255.0F;
                float var9 = (float) (var5 >> 16 & 255) / 255.0F;
                float f = (float) (var5 >> 8 & 255) / 255.0F;
                float f1 = (float) (var5 & 255) / 255.0F;
                this.field_177095_g.put(var9);
                this.field_177095_g.put(f);
                this.field_177095_g.put(f1);
                this.field_177095_g.put(1.0F - var8);
            }

            this.field_177095_g.flip();
            GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, this.field_177095_g);
            GlStateManager.setActiveTexture(OpenGlHelper.field_176096_r);
            GlStateManager.enableTextures();
            GlStateManager.bindTexture(field_177096_e.getGlTextureId());
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.field_176095_s);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176099_x, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176098_y, OpenGlHelper.field_176091_w);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176097_z, OpenGlHelper.lightmapTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176081_B, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176082_C, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176077_E, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176078_F, OpenGlHelper.field_176091_w);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176085_I, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
    }

    protected void func_177091_f() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTextures();
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176099_x, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176098_y, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176097_z, OpenGlHelper.field_176093_u);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176081_B, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176082_C, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176077_E, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176078_F, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176079_G, OpenGlHelper.field_176093_u);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176085_I, GL11.GL_SRC_ALPHA);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176086_J, GL11.GL_SRC_ALPHA);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176099_x, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176081_B, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176082_C, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176098_y, GL11.GL_TEXTURE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176077_E, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176085_I, GL11.GL_SRC_ALPHA);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176078_F, GL11.GL_TEXTURE);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.setActiveTexture(OpenGlHelper.field_176096_r);
        GlStateManager.disableTextures();
        GlStateManager.bindTexture(0);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176099_x, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176081_B, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176082_C, GL11.GL_SRC_COLOR);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176098_y, GL11.GL_TEXTURE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176077_E, GL11.GL_MODULATE);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176085_I, GL11.GL_SRC_ALPHA);
        GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.field_176078_F, GL11.GL_TEXTURE);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLivingBase p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_) {
        GlStateManager.translate((float) p_77039_2_, (float) p_77039_4_, (float) p_77039_6_);
    }

    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);

        if (p_77043_1_.deathTime > 0) {
            float var5 = ((float) p_77043_1_.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
            var5 = MathHelper.sqrt_float(var5);

            if (var5 > 1.0F) {
                var5 = 1.0F;
            }

            GlStateManager.rotate(var5 * this.getDeathMaxRotation(p_77043_1_), 0.0F, 0.0F, 1.0F);
        } else {
            String var6 = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getName());

            if (var6 != null && (var6.equals("Dinnerbone") || var6.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || ((EntityPlayer) p_77043_1_).func_175148_a(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0F, p_77043_1_.height + 0.1F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args : entity, partialTickTime
     */
    protected float getSwingProgress(EntityLivingBase p_77040_1_, float p_77040_2_) {
        return p_77040_1_.getSwingProgress(p_77040_2_);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
        return (float) p_77044_1_.ticksExisted + p_77044_2_;
    }

    protected void func_177093_a(EntityLivingBase p_177093_1_, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
        Iterator var9 = this.field_177097_h.iterator();

        while (var9.hasNext()) {
            LayerRenderer f = (LayerRenderer) var9.next();
            boolean f1 = this.func_177092_a(p_177093_1_, p_177093_4_, f.shouldCombineTextures());
            if (renderLayers)
                f.doRenderLayer(p_177093_1_, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);

            if (f1) {
                this.func_177091_f();
            }
        }
    }

    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
        return 90.0F;
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLivingBase p_77030_1_, float p_77030_2_, float p_77030_3_) {
        return 0;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
    }

    /**
     * Passes the specialRender and renders it
     */
    public void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
    	if(!rendername)
    		return;
        if (this.canRenderName(p_77033_1_)) {
            double var8 = p_77033_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f = p_77033_1_.isSneaking() ? 32.0F : 64.0F;

            if (var8 < (double) (f * f)) {
                String f1 = p_77033_1_.getDisplayName().getFormattedText();
                float f2 = 0.02666667F;
                GlStateManager.alphaFunc(516, 0.1F);

                if (p_77033_1_.isSneaking()) {
                    Event event = EventSystem.getInstance(EventNametagRender.class);
                    event.fire();
                    if (event.isCancelled()) {
                        return;
                    }
                    FontRenderer var13 = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float) p_77033_2_, (float) p_77033_4_ + p_77033_1_.height + 0.5F - (p_77033_1_.isChild() ? p_77033_1_.height / 2.0F : 0.0F), (float) p_77033_6_);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
                    GlStateManager.translate(0.0F, 9.374999F, 0.0F);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableTextures();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    Tessellator f3 = Tessellator.getInstance();
                    WorldRenderer f4 = f3.getWorldRenderer();
                    f4.startDrawingQuads();
                    int f5 = var13.getStringWidth(f1) / 2;
                    f4.setColorRGBA(0.0F, 0.0F, 0.0F, 0.25F);
                    f4.addVertex((double) (-f5 - 1), -1.0D, 0.0D);
                    f4.addVertex((double) (-f5 - 1), 8.0D, 0.0D);
                    f4.addVertex((double) (f5 + 1), 8.0D, 0.0D);
                    f4.addVertex((double) (f5 + 1), -1.0D, 0.0D);
                    f3.draw();
                    GlStateManager.enableTextures();
                    GlStateManager.depthMask(true);
                    var13.drawString(f1, -var13.getStringWidth(f1) / 2, 0, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                } else {
                    this.func_177069_a(p_77033_1_, p_77033_2_, p_77033_4_ - (p_77033_1_.isChild() ? (double) (p_77033_1_.height / 2.0F) : 0.0D), p_77033_6_, f1, 0.02666667F, var8);
                }
            }
        }
    }

    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLivingBase targetEntity) {
        EntityPlayerSP var2 = Minecraft.getMinecraft().thePlayer;

        if (targetEntity instanceof EntityPlayer && targetEntity != var2) {
            Team var3 = targetEntity.getTeam();
            Team var4 = var2.getTeam();

            if (var3 != null) {
                Team.EnumVisible var5 = var3.func_178770_i();

                switch (RendererLivingEntity.SwitchEnumVisible.field_178679_a[var5.ordinal()]) {
                    case 1:
                        return true;

                    case 2:
                        return false;

                    case 3:
                        return var4 == null || var3.isSameTeam(var4);

                    case 4:
                        return var4 == null || !var3.isSameTeam(var4);

                    default:
                        return true;
                }
            }
        }

        return Minecraft.isGuiEnabled() && targetEntity != this.renderManager.livingPlayer && !targetEntity.isInvisibleToPlayer(var2) && targetEntity.riddenByEntity == null;
    }

    public void func_177086_a(boolean p_177086_1_) {
        this.field_177098_i = p_177086_1_;
    }

    protected boolean func_177070_b(Entity p_177070_1_) {
        return this.canRenderName((EntityLivingBase) p_177070_1_);
    }

    public void func_177067_a(Entity p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_) {
        this.passSpecialRender((EntityLivingBase) p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.doRender((EntityLivingBase) entity, x, y, z, yaw, partialTicks);
    }

    static {
        int[] var0 = field_177096_e.getTextureData();

        for (int var1 = 0; var1 < 256; ++var1) {
            var0[var1] = -1;
        }

        field_177096_e.updateDynamicTexture();
    }

    static final class SwitchEnumVisible {
        static final int[] field_178679_a = new int[Team.EnumVisible.values().length];
        private static final String __OBFID = "CL_00002435";

        static {
            try {
                field_178679_a[Team.EnumVisible.ALWAYS.ordinal()] = 1;
            } catch (NoSuchFieldError var4) {
                ;
            }

            try {
                field_178679_a[Team.EnumVisible.NEVER.ordinal()] = 2;
            } catch (NoSuchFieldError var3) {
                ;
            }

            try {
                field_178679_a[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
            } catch (NoSuchFieldError var2) {
                ;
            }

            try {
                field_178679_a[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
            } catch (NoSuchFieldError var1) {
                ;
            }
        }
    }
}
