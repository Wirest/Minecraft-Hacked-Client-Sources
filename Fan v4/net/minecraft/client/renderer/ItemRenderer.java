package net.minecraft.client.renderer;

import cedo.Fan;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;
import net.optifine.DynamicLights;
import net.optifine.reflect.Reflector;
import net.optifine.shaders.Shaders;
import org.lwjgl.opengl.GL11;

public class ItemRenderer
{
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");

    /** A reference to the Minecraft object. */
    private final Minecraft mc;
    private ItemStack itemToRender;

    /**
     * How far the current item has been equipped (0 disequipped and 1 fully up)
     */
    private float equippedProgress;
    private float prevEquippedProgress;
    private final RenderManager renderManager;
    private final RenderItem itemRenderer;

    /** The index of the currently held item (0-8, or -1 if not yet updated) */
    private int equippedItemSlot = -1;

    public ItemRenderer(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.renderManager = mcIn.getRenderManager();
        this.itemRenderer = mcIn.getRenderItem();
    }

    public void renderItem(EntityLivingBase entityIn, ItemStack heldStack, ItemCameraTransforms.TransformType transform)
    {
        if (heldStack != null)
        {
            Item item = heldStack.getItem();
            Block block = Block.getBlockFromItem(item);
            GlStateManager.pushMatrix();

            if (this.itemRenderer.shouldRenderItemIn3D(heldStack))
            {
                GlStateManager.scale(2.0F, 2.0F, 2.0F);

                if (this.isBlockTranslucent(block) && (!Config.isShaders() || !Shaders.renderItemKeepDepthMask))
                {
                    GlStateManager.depthMask(false);
                }
            }

            this.itemRenderer.renderItemModelForEntity(heldStack, entityIn, transform);

            if (this.isBlockTranslucent(block))
            {
                GlStateManager.depthMask(true);
            }

            GlStateManager.popMatrix();
        }
    }

    /**
     * Returns true if given block is translucent
     */
    private boolean isBlockTranslucent(Block blockIn)
    {
        return blockIn != null && blockIn.getBlockLayer() == EnumWorldBlockLayer.TRANSLUCENT;
    }

    private void func_178101_a(float angle, float p_178101_2_)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(angle, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(p_178101_2_, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    private void func_178109_a(AbstractClientPlayer clientPlayer)
    {
        int i = this.mc.theWorld.getCombinedLight(new BlockPos(clientPlayer.posX, clientPlayer.posY + (double)clientPlayer.getEyeHeight(), clientPlayer.posZ), 0);

        if (Config.isDynamicLights())
        {
            i = DynamicLights.getCombinedLight(this.mc.getRenderViewEntity(), i);
        }

        float f = (float)(i & 65535);
        float f1 = (float)(i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, f1);
    }

    private void func_178110_a(EntityPlayerSP entityplayerspIn, float partialTicks)
    {
        float f = entityplayerspIn.prevRenderArmPitch + (entityplayerspIn.renderArmPitch - entityplayerspIn.prevRenderArmPitch) * partialTicks;
        float f1 = entityplayerspIn.prevRenderArmYaw + (entityplayerspIn.renderArmYaw - entityplayerspIn.prevRenderArmYaw) * partialTicks;
        GlStateManager.rotate((entityplayerspIn.rotationPitch - f) * 0.1F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate((entityplayerspIn.rotationYaw - f1) * 0.1F, 0.0F, 1.0F, 0.0F);
    }

    private float func_178100_c(float p_178100_1_)
    {
        float f = 1.0F - p_178100_1_ / 45.0F + 0.1F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        f = -MathHelper.cos(f * (float)Math.PI) * 0.5F + 0.5F;
        return f;
    }

    private void renderRightArm(RenderPlayer renderPlayerIn)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(54.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(64.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-62.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(0.25F, -0.85F, 0.75F);
        renderPlayerIn.renderRightArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderLeftArm(RenderPlayer renderPlayerIn)
    {
        GlStateManager.pushMatrix();
        GlStateManager.rotate(92.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(41.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.translate(-0.3F, -1.1F, 0.45F);
        renderPlayerIn.renderLeftArm(this.mc.thePlayer);
        GlStateManager.popMatrix();
    }

    private void renderPlayerArms(AbstractClientPlayer clientPlayer)
    {
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.thePlayer);
        RenderPlayer renderplayer = (RenderPlayer)render;

        if (!clientPlayer.isInvisible())
        {
            GlStateManager.disableCull();
            this.renderRightArm(renderplayer);
            this.renderLeftArm(renderplayer);
            GlStateManager.enableCull();
        }
    }

    private void renderItemMap(AbstractClientPlayer clientPlayer, float p_178097_2_, float p_178097_3_, float p_178097_4_)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(p_178097_4_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        float f3 = this.func_178100_c(p_178097_2_);
        GlStateManager.translate(0.0F, 0.04F, -0.72F);
        GlStateManager.translate(0.0F, p_178097_3_ * -1.2F, 0.0F);
        GlStateManager.translate(0.0F, f3 * -0.5F, 0.0F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -85.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        this.renderPlayerArms(clientPlayer);
        float f4 = MathHelper.sin(p_178097_4_ * p_178097_4_ * (float)Math.PI);
        float f5 = MathHelper.sin(MathHelper.sqrt_float(p_178097_4_) * (float)Math.PI);
        GlStateManager.rotate(f4 * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f5 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f5 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.38F, 0.38F, 0.38F);
        GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(0.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-1.0F, -1.0F, 0.0F);
        GlStateManager.scale(0.015625F, 0.015625F, 0.015625F);
        this.mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(-7.0D, 135.0D, 0.0D).func_181673_a(0.0D, 1.0D).func_181675_d();
        worldrenderer.func_181662_b(135.0D, 135.0D, 0.0D).func_181673_a(1.0D, 1.0D).func_181675_d();
        worldrenderer.func_181662_b(135.0D, -7.0D, 0.0D).func_181673_a(1.0D, 0.0D).func_181675_d();
        worldrenderer.func_181662_b(-7.0D, -7.0D, 0.0D).func_181673_a(0.0D, 0.0D).func_181675_d();
        tessellator.draw();
        MapData mapdata = Items.filled_map.getMapData(this.itemToRender, this.mc.theWorld);

        if (mapdata != null)
        {
            this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, false);
        }
    }

    private void func_178095_a(AbstractClientPlayer clientPlayer, float p_178095_2_, float p_178095_3_)
    {
        float f = -0.3F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        float f1 = 0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI * 2.0F);
        float f2 = -0.4F * MathHelper.sin(p_178095_3_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
        GlStateManager.translate(0.64000005F, -0.6F, -0.71999997F);
        GlStateManager.translate(0.0F, p_178095_2_ * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f3 = MathHelper.sin(p_178095_3_ * p_178095_3_ * (float)Math.PI);
        float f4 = MathHelper.sin(MathHelper.sqrt_float(p_178095_3_) * (float)Math.PI);
        GlStateManager.rotate(f4 * 70.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * -20.0F, 0.0F, 0.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(clientPlayer.getLocationSkin());
        GlStateManager.translate(-1.0F, 3.6F, 3.5F);
        GlStateManager.rotate(120.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(200.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        GlStateManager.translate(5.6F, 0.0F, 0.0F);
        Render<AbstractClientPlayer> render = this.renderManager.<AbstractClientPlayer>getEntityRenderObject(this.mc.thePlayer);
        GlStateManager.disableCull();
        RenderPlayer renderplayer = (RenderPlayer)render;
        renderplayer.renderRightArm(this.mc.thePlayer);
        GlStateManager.enableCull();
    }

    private void func_178105_d(float p_178105_1_)
    {
        float f = -0.4F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI);
        float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt_float(p_178105_1_) * (float)Math.PI * 2.0F);
        float f2 = -0.2F * MathHelper.sin(p_178105_1_ * (float)Math.PI);
        GlStateManager.translate(f, f1, f2);
    }

    private void func_178104_a(AbstractClientPlayer clientPlayer, float p_178104_2_)
    {
        float f = (float)clientPlayer.getItemInUseCount() - p_178104_2_ + 1.0F;
        float f1 = f / (float)this.itemToRender.getMaxItemUseDuration();
        float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);

        if (f1 >= 0.8F)
        {
            f2 = 0.0F;
        }

        GlStateManager.translate(0.0F, f2, 0.0F);
        float f3 = 1.0F - (float)Math.pow(f1, 27.0D);
        GlStateManager.translate(f3 * 0.6F, f3 * -0.5F, f3 * 0.0F);
        GlStateManager.rotate(f3 * 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f3 * 10.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(f3 * 30.0F, 0.0F, 0.0F, 1.0F);
    }

    /**
     * Performs transformations prior to the rendering of a held item in first person.
     *  
     * @param equipProgress The progress of the animation to equip (raise from out of frame) while switching held items.
     * @param swingProgress The progress of the arm swing animation.
     */
    private void transformFirstPersonItem(float equipProgress, float swingProgress)
    {
        float itemSize = (float) ((float) Fan.itemCustomization.itemSize.getValue() * .01);
        float xValue = (float) (((float) Fan.itemCustomization.swordXValue.getValue()) * .01);
        float yValue = (float) (((float) Fan.itemCustomization.swordYValue.getValue()) * .01);
        GlStateManager.translate(xValue, -yValue, -0.71999997F);
        GlStateManager.translate(0.0F, equipProgress * -0.6F, 0.0F);
        GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
        float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        float f1 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        GlStateManager.rotate(f * -20.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(f1 * -20.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(itemSize, itemSize, itemSize);
    }


    private void func_178098_a(float p_178098_1_, AbstractClientPlayer clientPlayer)
    {

        GlStateManager.rotate(-18.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(-12.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-8.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.translate(-0.9F, 0.2F, 0.0F);
        float f = (float)this.itemToRender.getMaxItemUseDuration() - ((float)clientPlayer.getItemInUseCount() - p_178098_1_ + 1.0F);
        float f1 = f / 20.0F;
        f1 = (f1 * f1 + f1 * 2.0F) / 3.0F;

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        if (f1 > 0.1F)
        {
            float f2 = MathHelper.sin((f - 0.1F) * 1.3F);
            float f3 = f1 - 0.1F;
            float f4 = f2 * f3;
            GlStateManager.translate(f4 * 0.0F, f4 * 0.01F, f4 * 0.0F);
        }

        GlStateManager.translate(f1 * 0.0F, f1 * 0.0F, f1 * 0.1F);
        GlStateManager.scale(1.0F, 1.0F, 1.0F + f1 * 0.2F);
    }

    private void func_178103_d()
    {
        float angle = (int) ((System.currentTimeMillis() / 1.5) % 360);
        angle =  (angle > 180 ? 360 - angle : angle) * 2;
        angle /= 180f;


        float angle3 = (int) ((System.currentTimeMillis() / 3.5) % 100);
        angle3 =  (angle3 > 40 ? 110 - angle3 : angle3) * 2;
        angle3 /= 1f;

        int random1 = 0;
        int random2 = 0;
        int random3 = 0;
        int random4 = 0;
        float random5 = 0;
        int random6 = 0;
        int random7 = 0;
        float random8 = 0;
        int random10 = 0;

        String mode = Fan.effects.mode.getSelected();


        if(mode.equalsIgnoreCase("Swirl") || mode.equalsIgnoreCase("Item360")){
            random1 = (int) (System.currentTimeMillis() / (mode.equals("Swirl") ? 2 : 3) % 360);
        }else{
            random1 = 80;
        }


        switch(mode) {
            case "UpDown":
                random1 = (int) (System.currentTimeMillis() / 3 % 360);
                random2 = 0;
                random10 = 3;
                break;

            case "Whack":
                random2 = 1;
                random3 = 0;
                random4 = (int) angle3;
                random5 = 0;
                random6 = 1;
                random7 = 1;
                random8 = -0.5f;
                random10 = 0;
                break;

            case "Swirl":
                random1 = (int) (System.currentTimeMillis() / 2 % 360);
                random2 = 1;
                random5 = 1;
                random4 = -59;
                random6 = -1;
                random7 = 0;
                random10 = 3;
                break;

            case "Penis":
                random2 = 1;
                random5 = angle;
                random4 = -70;
                random6 = -1;
                random7 = 0;
                random8 = random5 - 2.5f;
                random10 = 3;
                break;

            case "Item360":
                random1 = (int) (System.currentTimeMillis() / 3 % 360);
                random2 = 0;
                random3 = 1;
                random10 = 3;
                break;

            case "1.8":
                case "1.7":
                case "Chill":
                case "Tiny Whack":
                case "Long Hit":
                case "Butter":
                case "Astolfo":
                random2 = 1;
                random3 = 0;
                random4 = 30;
                random5 = 0;
                random6 = 0;
                random7 = 1;
                random8 = -0.5f;
                random10 = 0;
                break;
        }

        /*if(mode.equalsIgnoreCase("Shotbowxd") {
            this.transformFirstPersonItem(f, f2 / 40.0f);
            this.func_178103_d();
            break Label_1045;
        }*/

        //BLOCK ANIMATION STUFF
        //
        GlStateManager.translate(random8, 0.2f, -random5);
        GlStateManager.rotate(random4, random6, random7, random10);
        GlStateManager.rotate(-random1, random2, random3, 0.0F);
        GlStateManager.rotate(60.0F, 0.0F, 1.0F, 0.0F);
    }

    /**
     * Renders the active item in the player's hand when in first person mode. Args: partialTickTime
     *  
     * @param partialTicks The amount of time passed during the current tick, ranging from 0 to 1.
     */
    public void renderItemInFirstPerson(float partialTicks)
    {
        float f = 1.0F - (this.prevEquippedProgress + (this.equippedProgress - this.prevEquippedProgress) * partialTicks);
        EntityPlayerSP entityplayersp = this.mc.thePlayer;
        float f1 = entityplayersp.getSwingProgress(partialTicks);
        float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * partialTicks;
        float f3 = entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * partialTicks;
        this.func_178101_a(f2, f3);
        this.func_178109_a(entityplayersp);
        this.func_178110_a(entityplayersp, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemToRender != null)
        {
            if (this.itemToRender.getItem() instanceof ItemMap)
            {
                this.renderItemMap(entityplayersp, f2, f, f1);
            }
            else if (entityplayersp.getItemInUseCount() > 0 || Fan.killaura.blocking)
            {
                EnumAction enumaction = this.itemToRender.getItemUseAction();

                switch (ItemRenderer.ItemRenderer$1.field_178094_a[enumaction.ordinal()]) {
                    case 1:
                        this.transformFirstPersonItem(f, 0.0F);
                        break;

                    case 2:
                    case 3:
                        this.func_178104_a(entityplayersp, partialTicks);
                        this.transformFirstPersonItem(f, 0.0F);
                        break;

                    case 4:
                        final float f16 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
                        switch (Fan.effects.mode.getSelected()){
                            case "1.7":
                                this.transformFirstPersonItem(f - 0.3F, f1);
                                this.func_178103_d();
                                break;

                            case "Chill":
                                this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                                GL11.glRotatef(f16 * 60.0f / 2.0f, -f16 / 2.0f, -0.0f, -16.0f);
                                GL11.glRotatef(-f16 * 30.0f, 1.0f, f16 / 2.0f, -1.0f);
                                this.func_178103_d();
                                break;

                            case "Tiny Whack":
                                this.transformFirstPersonItem(f / 2.0f - 0.18f, 0.0f);
                                GL11.glRotatef(-f16 * 40.0f / 2.0f, f16 / 2.0f, -0.0f, 9.0f);
                                GL11.glRotatef(-f16 * 30.0f, 1.0f, f16 / 2.0f, -0.0f);
                                this.func_178103_d();
                                break;

                            case "Long Hit":
                                this.transformFirstPersonItem(f, 0.0f);
                                this.func_178103_d();
                                final float var19 = MathHelper.sin(MathHelper.sqrt_float(f1) * 3.1415927f);
                                GlStateManager.translate(-0.05f, 0.6f, 0.3f);
                                GlStateManager.rotate(-var19 * 70.0f / 2.0f, -8.0f, -0.0f, 9.0f);
                                GlStateManager.rotate(-var19 * 70.0f, 1.5f, -0.4f, -0.0f);
                                break;

                            case "Butter":
                                this.transformFirstPersonItem(f * 0.5f, 0.0f);
                                GlStateManager.rotate(-f16 * -74.0f / 4.0f, -8.0f, -0.0f, 9.0f);
                                GlStateManager.rotate(-f16 * 15.0f, 1.0f, f16 / 2.0f, -0.0f);
                                this.func_178103_d();
                                GL11.glTranslated(1.2, 0.3, 0.5);
                                GL11.glTranslatef(-1.0f, this.mc.thePlayer.isSneaking() ? -0.1f : -0.2f, 0.2f);
                                break;

                            case "Astolfo":
                                GlStateManager.rotate((float)(System.currentTimeMillis() / Fan.itemCustomization.handspeed.getValue() % 360), 0.0f, 0.0f, -0.1f);
                                this.transformFirstPersonItem(f / 1.6f, 0.0f);
                                this.func_178103_d();
                                break;
                        }
                        if(!Fan.effects.mode.getSelected().equalsIgnoreCase("1.7") && !Fan.effects.mode.getSelected().equalsIgnoreCase("Chill")
                                && !Fan.effects.mode.getSelected().equalsIgnoreCase("Tiny Whack") && !Fan.effects.mode.getSelected().equalsIgnoreCase("Long Hit")
                                && !Fan.effects.mode.getSelected().equalsIgnoreCase("Butter") && !Fan.effects.mode.getSelected().equalsIgnoreCase("Astolfo")) {
                            this.transformFirstPersonItem(f, 0.0F);
                            this.func_178103_d();
                        }
                        break;

                    case 5:
                        this.transformFirstPersonItem(f, 0.0F);
                        this.func_178098_a(partialTicks, entityplayersp);
                }
            }
            else
            {
                this.func_178105_d(f1);
                this.transformFirstPersonItem(f, f1);
            }

            this.renderItem(entityplayersp, this.itemToRender, ItemCameraTransforms.TransformType.FIRST_PERSON);
        }
        else if (!entityplayersp.isInvisible())
        {
            this.func_178095_a(entityplayersp, f, f1);
        }

        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * Renders all the overlays that are in first person mode. Args: partialTickTime
     */
    public void renderOverlays(float partialTicks)
    {
        GlStateManager.disableAlpha();

        if (this.mc.thePlayer.isEntityInsideOpaqueBlock())
        {
            IBlockState iblockstate = this.mc.theWorld.getBlockState(new BlockPos(this.mc.thePlayer));
            BlockPos blockpos = new BlockPos(this.mc.thePlayer);
            EntityPlayer entityplayer = this.mc.thePlayer;

            for (int i = 0; i < 8; ++i)
            {
                double d0 = entityplayer.posX + (double)(((float)((i) % 2) - 0.5F) * entityplayer.width * 0.8F);
                double d1 = entityplayer.posY + (double)(((float)((i >> 1) % 2) - 0.5F) * 0.1F);
                double d2 = entityplayer.posZ + (double)(((float)((i >> 2) % 2) - 0.5F) * entityplayer.width * 0.8F);
                BlockPos blockpos1 = new BlockPos(d0, d1 + (double)entityplayer.getEyeHeight(), d2);
                IBlockState iblockstate1 = this.mc.theWorld.getBlockState(blockpos1);

                if (iblockstate1.getBlock().isVisuallyOpaque())
                {
                    iblockstate = iblockstate1;
                    blockpos = blockpos1;
                }
            }

            if (iblockstate.getBlock().getRenderType() != -1)
            {
                this.func_178108_a(partialTicks, this.mc.getBlockRendererDispatcher().getBlockModelShapes().getTexture(iblockstate));
            }
        }

        if (!this.mc.thePlayer.isSpectator())
        {
            if (this.mc.thePlayer.isInsideOfMaterial(Material.water))
            {
                this.renderWaterOverlayTexture(partialTicks);
            }

            if (this.mc.thePlayer.isBurning())
            {
                this.renderFireInFirstPerson(partialTicks);
            }
        }

        GlStateManager.enableAlpha();
    }

    private void func_178108_a(float p_178108_1_, TextureAtlasSprite p_178108_2_)
    {
        this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        float f = 0.1F;
        GlStateManager.color(0.1F, 0.1F, 0.1F, 0.5F);
        GlStateManager.pushMatrix();
        float f1 = -1.0F;
        float f2 = 1.0F;
        float f3 = -1.0F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = p_178108_2_.getMinU();
        float f7 = p_178108_2_.getMaxU();
        float f8 = p_178108_2_.getMinV();
        float f9 = p_178108_2_.getMaxV();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b(-1.0D, -1.0D, -0.5D).func_181673_a(f7, f9).func_181675_d();
        worldrenderer.func_181662_b(1.0D, -1.0D, -0.5D).func_181673_a(f6, f9).func_181675_d();
        worldrenderer.func_181662_b(1.0D, 1.0D, -0.5D).func_181673_a(f6, f8).func_181675_d();
        worldrenderer.func_181662_b(-1.0D, 1.0D, -0.5D).func_181673_a(f7, f8).func_181675_d();
        tessellator.draw();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a texture that warps around based on the direction the player is looking. Texture needs to be bound
     * before being called. Used for the water overlay. Args: parialTickTime
     */
    private void renderWaterOverlayTexture(float p_78448_1_)
    {
        if (!Config.isShaders() || Shaders.isUnderwaterOverlay())
        {
            this.mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            float f = this.mc.thePlayer.getBrightness(p_78448_1_);
            GlStateManager.color(f, f, f, 0.5F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.pushMatrix();
            float f1 = 4.0F;
            float f2 = -1.0F;
            float f3 = 1.0F;
            float f4 = -1.0F;
            float f5 = 1.0F;
            float f6 = -0.5F;
            float f7 = -this.mc.thePlayer.rotationYaw / 64.0F;
            float f8 = this.mc.thePlayer.rotationPitch / 64.0F;
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
            worldrenderer.func_181662_b(-1.0D, -1.0D, -0.5D).func_181673_a(4.0F + f7, 4.0F + f8).func_181675_d();
            worldrenderer.func_181662_b(1.0D, -1.0D, -0.5D).func_181673_a(0.0F + f7, 4.0F + f8).func_181675_d();
            worldrenderer.func_181662_b(1.0D, 1.0D, -0.5D).func_181673_a(0.0F + f7, 0.0F + f8).func_181675_d();
            worldrenderer.func_181662_b(-1.0D, 1.0D, -0.5D).func_181673_a(4.0F + f7, 0.0F + f8).func_181675_d();
            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableBlend();
        }
    }

    /**
     * Renders the fire on the screen for first person mode. Arg: partialTickTime
     */
    private void renderFireInFirstPerson(float p_78442_1_)
    {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.9F);
        GlStateManager.depthFunc(519);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        float f = 1.0F;

        for (int i = 0; i < 2; ++i)
        {
            GlStateManager.pushMatrix();
            TextureAtlasSprite textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_1");
            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f1 = textureatlassprite.getMinU();
            float f2 = textureatlassprite.getMaxU();
            float f3 = textureatlassprite.getMinV();
            float f4 = textureatlassprite.getMaxV();
            float f5 = (0.0F - f) / 2.0F;
            float f6 = f5 + f;
            float f7 = 0.0F - f / 2.0F;
            float f8 = f7 + f;
            float f9 = -0.5F;
            GlStateManager.translate((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GlStateManager.rotate((float)(i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
            worldrenderer.setSprite(textureatlassprite);
            worldrenderer.func_181662_b(f5, f7, f9).func_181673_a(f2, f4).func_181675_d();
            worldrenderer.func_181662_b(f6, f7, f9).func_181673_a(f1, f4).func_181675_d();
            worldrenderer.func_181662_b(f6, f8, f9).func_181673_a(f1, f3).func_181675_d();
            worldrenderer.func_181662_b(f5, f8, f9).func_181673_a(f2, f3).func_181675_d();
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.depthFunc(515);
    }

    public void updateEquippedItem()
    {
        this.prevEquippedProgress = this.equippedProgress;
        EntityPlayer entityplayer = this.mc.thePlayer;
        ItemStack itemstack = entityplayer.inventory.getCurrentItem();
        boolean flag = false;

        if (this.itemToRender != null && itemstack != null)
        {
            if (!this.itemToRender.getIsItemStackEqual(itemstack))
            {
                flag = true;
            }
        }
        else if (this.itemToRender == null && itemstack == null)
        {
            flag = false;
        }
        else
        {
            flag = true;
        }

        float f2 = 0.4F;
        float f = flag ? 0.0F : 1.0F;
        float f1 = MathHelper.clamp_float(f - this.equippedProgress, -f2, f2);
        this.equippedProgress += f1;

        if (this.equippedProgress < 0.1F)
        {
            this.itemToRender = itemstack;
            this.equippedItemSlot = entityplayer.inventory.currentItem;

            if (Config.isShaders())
            {
                Shaders.setItemToRenderMain(itemstack);
            }
        }
    }

    /**
     * Resets equippedProgress
     */
    public void resetEquippedProgress()
    {
        this.equippedProgress = 0.0F;
    }

    /**
     * Resets equippedProgress
     */
    public void resetEquippedProgress2()
    {
        this.equippedProgress = 0.0F;
    }

    static final class ItemRenderer$1
    {
        static final int[] field_178094_a = new int[EnumAction.values().length];
        private static final String __OBFID = "CL_00002537";

        static
        {
            try
            {
                field_178094_a[EnumAction.NONE.ordinal()] = 1;
            }
            catch (NoSuchFieldError ignored)
            {
            }

            try
            {
                field_178094_a[EnumAction.EAT.ordinal()] = 2;
            }
            catch (NoSuchFieldError ignored)
            {
            }

            try
            {
                field_178094_a[EnumAction.DRINK.ordinal()] = 3;
            }
            catch (NoSuchFieldError ignored)
            {
            }

            try
            {
                field_178094_a[EnumAction.BLOCK.ordinal()] = 4;
            }
            catch (NoSuchFieldError ignored)
            {
            }

            try
            {
                field_178094_a[EnumAction.BOW.ordinal()] = 5;
            }
            catch (NoSuchFieldError ignored)
            {
            }
        }
    }
}
