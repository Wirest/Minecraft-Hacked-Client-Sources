package skyline.specc.render.modules;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.RenderUtil;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.render.VitalFontRenderer;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.value.BooleanValue;
import skyline.specc.SkyLine;
import skyline.specc.managers.FriendManager;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.render.renderevnts.EventRenderNametag;
import skyline.specc.render.renderevnts.EventRenderWorld;
import skyline.specc.utils.MathUtil;
import skyline.specc.utils.TimerUtils;
import skyline.specc.utils.Wrapper;

public class NametagsMod extends Module {

	private VitalFontRenderer fontRenderer;
	public BooleanValue armor;
	private TimerUtils time;
	public BooleanValue hypixelbots;

	public NametagsMod() {
		super(new ModData("Nametags", 48, new Color(40, 255, 120)), ModType.RENDER);
		this.fontRenderer = VitalFontRenderer.createFontRenderer(VitalFontRenderer.FontObjectType.CFONT,
				new Font("Arial", 0, 20));
		this.armor = new BooleanValue("Armor", Boolean.valueOf(true));
		this.hypixelbots = new BooleanValue("invisibles", Boolean.valueOf(true));
		time = new TimerUtils();
		addValue(this.armor);
		addValue(this.hypixelbots);
	}



	@EventListener
	public void onWorldRender(EventRenderWorld event) {
		for (Object i : Mineman.getMinecraft().theWorld.loadedEntityList) {
			if ((i instanceof EntityPlayer)) {
				EntityPlayer entity = (EntityPlayer) i;
				if ((entity.getName() != Mineman.thePlayer.getName())
						&& ((((Boolean) this.hypixelbots.getValue()).booleanValue()) || (!entity.isInvisible()))) {
					EntityOtherPlayerMP ep = (EntityOtherPlayerMP) entity;
					double n = ep.lastTickPosX + (ep.posX - ep.lastTickPosX) * mc.timer.renderPartialTicks;
					mc.getRenderManager();
					double pX = n - RenderManager.renderPosX;
					double n2 = ep.lastTickPosY + (ep.posY - ep.lastTickPosY) * mc.timer.renderPartialTicks;
					mc.getRenderManager();
					double pY = n2 - RenderManager.renderPosY;
					double n3 = ep.lastTickPosZ + (ep.posZ - ep.lastTickPosZ) * mc.timer.renderPartialTicks;
					mc.getRenderManager();
					double pZ = n3 - RenderManager.renderPosZ;
					renderNameTag(ep, ep.getName(), pX, pY, pZ);
				}
			}
		}
	}

	public void renderArmor(EntityPlayer player, int x, int y) {
		ItemStack[] items = player.getInventory();
		ItemStack inHand = player.getCurrentEquippedItem();
		ItemStack boots = items[0];
		ItemStack leggings = items[1];
		ItemStack body = items[2];
		ItemStack helm = items[3];
		ItemStack[] stuff = null;
		if (inHand != null) {
			stuff = new ItemStack[] { inHand, helm, body, leggings, boots };
		} else {
			stuff = new ItemStack[] { helm, body, leggings, boots };
		}
		List<ItemStack> stacks = new ArrayList();
		ItemStack[] array;
		int length = (array = stuff).length;
		ItemStack i;
		for (int j = 0; j < length; j++) {
			i = array[j];
			if ((i != null) && (i.getItem() != null)) {
				stacks.add(i);
			}
		}
		int width = 16 * stacks.size() / 2;
		x -= width;
		GlStateManager.disableDepth();
		for (ItemStack stack : stacks) {
			renderItem(stack, x, y);
			x += 16;
		}
		GlStateManager.enableDepth();
	}

	public void renderItem(ItemStack stack, int x, int y) {
		EnchantEntry[] enchants = { new EnchantEntry(Enchantment.field_180310_c, "Prot"),
				new EnchantEntry(Enchantment.thorns, "Th"), new EnchantEntry(Enchantment.field_180314_l, "Sharp"),
				new EnchantEntry(Enchantment.fireAspect, "Fire"), new EnchantEntry(Enchantment.field_180313_o, "Kb"),
				new EnchantEntry(Enchantment.unbreaking, "Unb") };
		GlStateManager.pushMatrix();
		GlStateManager.pushMatrix();
		float scale1 = 0.3f;
		GlStateManager.translate(x - 3, y + 10, 0.0f);
		GlStateManager.scale(0.3f, 0.3f, 0.3f);
		GlStateManager.popMatrix();
		RenderHelper.enableGUIStandardItemLighting();
		NametagsMod.mc.getRenderItem().zLevel = -100.0f;
		GlStateManager.disableDepth();
		NametagsMod.mc.getRenderItem().renderItemAboveHead(stack, x, y);
		NametagsMod.mc.getRenderItem().renderItemOverlayIntoGUI(NametagsMod.mc.fontRendererObj, stack, x, y, null);
		GlStateManager.enableDepth();
		EnchantEntry[] array;
		for (int length = (array = enchants).length, i = 0; i < length; ++i) {
			EnchantEntry enchant = array[i];
			int level = EnchantmentHelper.getEnchantmentLevel(enchant.getEnchant().effectId, stack);
			String levelDisplay = new StringBuilder().append(level).toString();
			if (level > 10) {
				levelDisplay = "10+";
			}
			if (level > 0) {
				float scale2 = 0.32f;
				GlStateManager.translate(x + 2, y + 1, 0.0f);
				GlStateManager.scale(0.32f, 0.32f, 0.32f);
				GlStateManager.disableDepth();
				GlStateManager.disableLighting();
				RenderUtil.setColor(Color.white);
				NametagsMod.mc.fontRendererObj.drawString("§f" + enchant.getName() + " " + levelDisplay,
						20 - NametagsMod.mc.fontRendererObj
								.getStringWidth("§f" + enchant.getName() + " " + levelDisplay) / 2,
						0.0f, Color.WHITE.getRGB());
				RenderUtil.setColor(Color.white);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
				GlStateManager.scale(3.125f, 3.125f, 3.125f);
				GlStateManager.translate(-x - 1, -y - 1, 0.0f);
				y += (int) ((NametagsMod.mc.fontRendererObj.FONT_HEIGHT + 3) * 0.32f);
			}
		}
		NametagsMod.mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		GlStateManager.popMatrix();
	}

	@EventListener
	public void onRenderNametag(EventRenderNametag event) {
		if ((event.getEntity() instanceof EntityOtherPlayerMP)) {
			event.setCancelled(true);
		}
	}

	public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
		FontRenderer var12 = mc.fontRendererObj;
		boolean healthBool = true;
		if (entity.getName().equals(this.p.getName())) {
			return;
		}
		pY += (entity.isSneaking() ? 0.5D : 0.7D);
		float var13 = this.p.getDistanceToEntity(entity) / 4.0F;
		if (var13 < 1.6F) {
			var13 = 1.6F;
		}
		int color = 0xFFFFFF;

		if (entity.isSneaking()) {
			color = 0xFFFC0000;
		} else if (entity.isInvisible()) {
			color = 0x808080;
		} 
		Color friendColor = new Color(60, 60, 60);
		if (SkyLine.getManagers().getFriendManager().hasFriend(entity.getName())) {
			FriendManager.Friend friend = SkyLine.getManagers().getFriendManager().getFriend(entity.getName());
			if (friend != null) {
				tag = "§3" + friend.getNickname();
				friendColor = new Color(66, 147, 179);
			}
		}

		int health = (int) entity.getHealth();
		if (health <= entity.getMaxHealth() * 0.25D) {
			tag = tag + "§4";
		} else if (health <= entity.getMaxHealth() * 0.5D) {
			tag = tag + "§6";
		} else if (health <= entity.getMaxHealth() * 0.75D) {
			tag = tag + "§e";
		} else if (health <= entity.getMaxHealth()) {
			tag = tag + "§2";
		}
		tag = String.valueOf(tag) + " " + Math.round(health / 2.0D);

		RenderManager renderManager = mc.getRenderManager();
		float scale = var13;
		scale /= 30.0F;
		scale = (float) (scale * 0.3D);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) pX, (float) pY + 1.4F, (float) pZ);
		GL11.glNormal3f(1.0F, 1.0F, 1.0F);
		GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
		GL11.glScalef(-scale, -scale, scale);
		GL11.glDisable(2896);
		GL11.glDisable(2929);
		Tessellator var14 = Tessellator.getInstance();
		WorldRenderer var15 = var14.getWorldRenderer();
		int width = mc.fontRendererObj.getStringWidth(tag) / 2;
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		RenderUtil.drawRect(-width - 2, -(mc.fontRendererObj.FONT_HEIGHT + 1), width + 2, 2.0F,
				new Color(20, 20, 20, 160));
		RenderUtil.drawRect(-width - 3.0F, -(mc.fontRendererObj.FONT_HEIGHT + 1) - 1.0F, width + 3.0F,
				-(mc.fontRendererObj.FONT_HEIGHT + 1), friendColor);
		RenderUtil.drawRect(-width - 3.0F, 3.0F, width + 3.0F, 2.0F, friendColor);
		RenderUtil.drawRect(-width - 3.0F, -(mc.fontRendererObj.FONT_HEIGHT + 1) - 1.0F, -width - 2, 3.0F, friendColor);
		RenderUtil.drawRect(width + 3.0F, -(mc.fontRendererObj.FONT_HEIGHT + 1) - 1.0F, width + 2, 3.0F, friendColor);
		mc.fontRendererObj.func_175065_a(tag, MathUtil.getMiddle(-width - 2, width + 2) - width,
				-(mc.fontRendererObj.FONT_HEIGHT - 1), color, true);
		if (((entity instanceof EntityPlayer)) && (((Boolean) this.armor.getValue()).booleanValue())) {
			GlStateManager.translate(0.0F, 1.0F, 0.0F);
			renderArmor(entity, 0, -(mc.fontRendererObj.FONT_HEIGHT + 1) - 20);
			GlStateManager.translate(0.0F, -1.0F, 0.0F);
		}
		GL11.glPushMatrix();
		GL11.glPopMatrix();
		GL11.glEnable(2896);
		GL11.glEnable(2929);
		GL11.glDisable(3042);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glPopMatrix();
	}

	public static class EnchantEntry {
		private Enchantment enchant;
		private String name;

		public EnchantEntry(Enchantment enchant, String name) {
			this.enchant = enchant;
			this.name = name;
		}

		public Enchantment getEnchant() {
			return this.enchant;
		}

		public String getName() {
			return this.name;
		}
	}
}
