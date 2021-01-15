package info.spicyclient.cosmetics.impl;

import org.lwjgl.opengl.GL11;

import info.spicyclient.cosmetics.CosmeticBase;
import info.spicyclient.cosmetics.CosmeticController;
import info.spicyclient.cosmetics.CosmeticModelBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class Tophat extends CosmeticBase {
	
	private final ModelTophat modelTophat;
	private static final ResourceLocation texture = new ResourceLocation("spicy/hat.png");
	
	public Tophat(RenderPlayer renderPlayer) {
		
		super(renderPlayer);
		modelTophat = new ModelTophat(renderPlayer);
		
	}
	
	@Override
	public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
		
		
		if (CosmeticController.shouldRenderTophat(player)) {
			
			GlStateManager.pushMatrix();
			playerRenderer.bindTexture(texture);
			
			if (player.isSneaking()) {
				GL11.glTranslated(0D, 0.225D, 0D);
			}
			
			float[] color = CosmeticController.getTophatColor(player);
			GL11.glColor3f(color[0], color[1], color[2]);
			modelTophat.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
			GL11.glColor3f(1, 1, 1);
			GlStateManager.popMatrix();
			
		}
		
	}
	
	private class ModelTophat extends CosmeticModelBase{
		
		private ModelRenderer rim;
		private ModelRenderer hatBody;
		
		public ModelTophat(RenderPlayer player) {
			super(player);
			
			rim = new ModelRenderer(playerModel, 0, 0);
			rim.addBox(-5.5f, -9f, -5.5f, 11, 2, 11);
			
			hatBody = new ModelRenderer(playerModel, 0, 13);
			hatBody.addBox(-3.5f, -17f, -3.5f, 7, 8, 7);
			
		}
		
		@Override
		public void render(Entity player, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw,
				float headPitch, float scale) {
			
			rim.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			rim.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			rim.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			rim.rotationPointX = 0.0f;
			rim.rotationPointY = 0.0f;
			rim.render(scale);
			
			hatBody.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			hatBody.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			hatBody.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			hatBody.rotationPointX = 0.0f;
			hatBody.rotationPointY = 0.0f;
			hatBody.render(scale);
			
		}
		
	}

}
