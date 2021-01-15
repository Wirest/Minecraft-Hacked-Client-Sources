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

public class ProtogenMask extends CosmeticBase {
	
	private final ModelProtogenMask modelProtogenMask;
	private static final ResourceLocation texture = new ResourceLocation("spicy/splash/SpicyClient.png");
	
	public ProtogenMask(RenderPlayer renderPlayer) {
		
		super(renderPlayer);
		modelProtogenMask = new ModelProtogenMask(renderPlayer);
		
	}
	
	@Override
	public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {
		
		
		// if (CosmeticController.shouldRenderTophat(player)) {
		if (false) {
			
			GlStateManager.pushMatrix();
			playerRenderer.bindTexture(texture);
			
			if (player.isSneaking()) {
				GL11.glTranslated(0D, 0.225D, 0D);
			}
			
			float[] color = CosmeticController.getTophatColor(player);
			GL11.glColor3f(color[0], color[1], color[2]);
			modelProtogenMask.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
			GL11.glColor3f(1, 1, 1);
			GlStateManager.popMatrix();
			
		}
		
	}
	
	private class ModelProtogenMask extends CosmeticModelBase{
		
		private ModelRenderer rim, hatBody, obj3, obj4, obj5, obj6;
		
		public ModelProtogenMask(RenderPlayer player) {
			super(player);
			
			rim = new ModelRenderer(playerModel, 0, 0);
			rim.addBox(-4f, -8, -10f, 8, 8, 6);
			
			hatBody = new ModelRenderer(playerModel, 0, 0);
			hatBody.addBox(-2f, -6, -12f, 4, 4, 2);
			
			obj3 = new ModelRenderer(playerModel, 0, 0);
			obj3.addBox(-3f, -7, -11f, 6, 6, 2);
			
			obj4 = new ModelRenderer(playerModel, 0, 0);
			obj4.addBox(-5f, -9, -4.45f, 10, 10, 4);
			
			obj5 = new ModelRenderer(playerModel, 0, 0);
			obj5.addBox(-4.5f, -8.5f, -7f, 9, 9, 5);
			
			obj6 = new ModelRenderer(playerModel, 0, 0);
			obj6.addBox(-3f, -7, -11f, 6, 6, 2);
			
			// Keep these
			obj3 = new ModelRenderer(playerModel, 0, 0);
			obj3.addBox(-5f, 1, -10f, 9, 11, 6);
			
		}
		
		@Override
		public void render(Entity player, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw,
				float headPitch, float scale) {
			
			/*
			/*
			rim = new ModelRenderer(playerModel, 0, 0);
			rim.addBox(-4f, -8, -10f, 8, 8, 6);
			
			hatBody = new ModelRenderer(playerModel, 0, 0);
			hatBody.addBox(-2f, -6, -12f, 4, 4, 2);
			
			obj3 = new ModelRenderer(playerModel, 0, 0);
			obj3.addBox(-3f, -7, -11f, 6, 6, 2);
			
			obj4 = new ModelRenderer(playerModel, 0, 0);
			obj4.addBox(-5f, -9, -4.45f, 10, 10, 4);
			
			obj5 = new ModelRenderer(playerModel, 0, 0);
			obj5.addBox(-4.5f, -8.5f, -5f, 9, 9, 4);
			
			obj6 = new ModelRenderer(playerModel, 0, 0);
			obj6.addBox(offX, offY, offZ, width, height, depth)
			
			// Visor
			GL11.glColor3f(0.03f, 0.03f, 0.03f);
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
			
			obj3.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			obj3.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj3.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj3.rotationPointX = 0.0f;
			obj3.rotationPointY = 0.0f;
			obj3.render(scale);
			
			obj5.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			obj5.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj5.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj5.rotationPointX = 0.0f;
			obj5.rotationPointY = 0.0f;
			obj5.render(scale);
			// Visor
			
			// Visor rim
			GL11.glColor3f(0.3f, 0.3f, 0.3f);
			obj4.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			obj4.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj4.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj4.rotationPointX = 0.0f;
			obj4.rotationPointY = 0.0f;
			obj4.render(scale);
			// Visor rim
			
			obj6.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			obj6.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj6.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj6.rotationPointX = 0.0f;
			obj6.rotationPointY = 0.0f;
			obj6.render(scale);
			*/
			
			obj3 = new ModelRenderer(playerModel, 0, 0);
			obj3.addBox(-4f, 11, 0f, 1, 1, 9);
			
			GL11.glColor3f(1, 1, 1);
			
			GL11.glColor3f(0.01f, 0.01f, 0.01f);
			
			obj3.rotateAngleX = playerModel.bipedHead.rotateAngleX;
			obj3.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj3.rotateAngleY = playerModel.bipedHead.rotateAngleY;
			obj3.rotationPointX = 0.0f;
			obj3.rotationPointY = 0.0f;
			obj3.offsetX = 0.045f;
			obj3.render(scale);
		}
		
	}

}
