package splash.client.modules.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import me.hippo.systems.lwjeb.event.Stage;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import splash.Splash;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.BooleanValue;
import splash.api.value.impl.ModeValue;
import splash.client.events.render.EntityRenderEvent;
import splash.client.modules.combat.AntiBot;
import splash.utilities.visual.RenderUtilities;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Author: Ice
 * Created: 00:31, 14-Jun-20
 * Project: Client
 */
public class ESP extends Module {

    public BooleanValue<Boolean> playerESPValue = new BooleanValue<>("Players", true, this);
    public ModeValue<ESPMode> playersESPValue = new ModeValue<>("Players Mode", ESPMode.SHADER, this);
    public BooleanValue<Boolean> chestESPValue = new BooleanValue<>("Chests", true, this);
    public BooleanValue<Boolean> itemsESPValue = new BooleanValue<>("Items", true, this);

    public DecimalFormat decimalFormat = new DecimalFormat("#.#");

    public ESP() {
        super("ESP", "Lets you see kids through walls.", ModuleCategory.VISUALS);
    }

    public enum ESPMode {
        SHADER, TWODIMENSIONAL, FILL;
    }


    public static void draw2D(final Entity e, final double posX, final double posY, final double posZ, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        RenderUtilities.INSTANCE.drawBorderedRect((int)-6.2, (int)-20.0, (int)6.2, (int)3.5, (int) 1, Splash.getInstance().getClientColor(), Color.TRANSLUCENT);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
    }

    @Collect
    public void onRender(EntityRenderEvent entityRenderEvent) {
        if(playerESPValue.getValue()) {
        if(playersESPValue.getValue() == ESPMode.FILL) {
            if (entityRenderEvent.getStage() == Stage.PRE && entityRenderEvent.getEntity() instanceof EntityPlayer) {
                GL11.glEnable(32823);
                GL11.glPolygonOffset(1.0f, 1100000.0f);
            } else if (entityRenderEvent.getStage() == Stage.POST && entityRenderEvent.getEntity() instanceof EntityPlayer) {
                GL11.glDisable(32823);
                GL11.glPolygonOffset(1.0f, -1100000.0f);
            }
        }
        if(playersESPValue.getValue() == ESPMode.TWODIMENSIONAL) {
           for(Entity e : mc.theWorld.loadedEntityList) {
               if(e instanceof EntityPlayer && !(e.getEntityId() == mc.thePlayer.getEntityId())) {
                   EntityPlayer player = (EntityPlayer) e;
                   if (AntiBot.bots.contains(player)) return;
                   double posX = (player.lastTickPosX + (player.posX - player.lastTickPosX) * entityRenderEvent.getPartialTicks());
                   double posY = (player.lastTickPosY + (player.posY - player.lastTickPosY) * entityRenderEvent.getPartialTicks());
                   double posZ = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * entityRenderEvent.getPartialTicks());
                   draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, new Color(255, 0, 0, 255).getRGB());
                   GL11.glColor4f(1f, 1f, 1f, 1f);
               }
           }
        }

        }
        if(chestESPValue.getValue()) {
            for (TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
                if (tileEntity instanceof TileEntityChest) {
                    Color color = new Color(Splash.getInstance().getClientColor());
                    double renderX = tileEntity.getPos().getX() - mc.getRenderManager().renderPosX;
                    double renderY = tileEntity.getPos().getY() - mc.getRenderManager().renderPosY;
                    double renderZ = tileEntity.getPos().getZ() - mc.getRenderManager().renderPosZ;
                    GL11.glTranslated(renderX, renderY, renderZ);
                    RenderUtilities.drawChestEsp(tileEntity, 0.0D, 0.0D, 0.0D, color.getRed(), color.getGreen(), color.getBlue(),
                            90);
                    GL11.glTranslated(-renderX, -renderY, -renderZ);
                }
            }

        }
    }
}
