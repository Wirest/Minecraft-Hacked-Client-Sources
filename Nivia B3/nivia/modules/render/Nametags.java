package nivia.modules.render;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event3D;
import nivia.managers.FriendManager;
import nivia.managers.FriendManager.Friend;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.RenderUtils;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Nametags extends Module {
	public Nametags() {
		super("Nametags", 0, 0, Category.RENDER, "Draws bigger and more detailed nametags.", new String[]{"names", "nt", "namet", "ntag"}, false);
	}

	public Property<Boolean> health = new Property<Boolean>(this, "Health", true);
	public Property<Boolean> ping = new Property<Boolean>(this, "Ping", false);
	public Property<Boolean> armor = new Property<Boolean>(this, "Armor", true);
	public Property<Boolean> font = new Property<Boolean>(this, "Font", true);
	public Property<Boolean> damage = new Property<Boolean>(this, "Damage", true);
	public Property<Boolean> invisibles = new Property<Boolean>(this, "Invisibles", true);
	public DoubleProperty scale = new DoubleProperty(this, "Scale", 6, 1, 10);
	public DoubleProperty entities = new DoubleProperty(this, "Entities", 50, 10, 150);


	private int i = 0;
	@EventTarget(Priority.LOWEST)
	public void onRender3D(Event3D render) {
		ArrayList<EntityPlayer> validEnt = new ArrayList<>();
		if(validEnt.size() > entities.getValue()) validEnt.clear();

		for (final EntityPlayer player : (List<EntityPlayer>) Helper.mc().theWorld.playerEntities) {
			if ((player.isEntityAlive()) && (!(player instanceof EntityPlayerSP))) {
				if(player.isInvisible() && !invisibles.value) {
					if(validEnt.contains(player)) validEnt.remove(player);
						continue;
				}
				if(validEnt.size() > entities.getValue()) break;
				if(!validEnt.contains(player) )
					validEnt.add(player);
			} else {
				if(validEnt.contains(player))
					validEnt.remove(player);
			}
		}
		validEnt.forEach(player -> {
			final float x = (float) (player.lastTickPosX + (player.posX - player.lastTickPosX) * render.getPartialTicks() - RenderManager.renderPosX);
			final float y = (float) (player.lastTickPosY + (player.posY - player.lastTickPosY) * render.getPartialTicks() - RenderManager.renderPosY);
			final float z = (float) (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * render.getPartialTicks() - RenderManager.renderPosZ);
			renderNametag(player, x, y, z);
		});
	}
	public String getHealth(EntityPlayer player) {
        DecimalFormat numberFormat = new DecimalFormat("0.#");
        return numberFormat.format(player.getHealth() / 2 + player.getAbsorptionAmount() / 2);
    }
	public String getPing(EntityPlayer player) {
		NetworkPlayerInfo playerInfo = Helper.player().sendQueue.func_175104_a(player.getName());
		int playerPing = playerInfo != null ? playerInfo.getResponseTime() : -1;
		return String.valueOf(playerPing);
	}
	private void drawNames(EntityPlayer player) {
		float xP = 2.2f;
		float width = ((getWidth(getPlayerName(player))) / 2F) + xP;
		if(health.value)
			width += getWidth(" " + getHealth(player)) / 2 - 0.25F;
		if(ping.value)
			width += getWidth("i " + getPing(player)) / 2;
		float w = width ;
		float nw = -width - xP;
		float offset = (getWidth(getPlayerName(player))) + (font.value ? 3 : 4) ;
		Helper.get2DUtils().drawBorderedRect(nw, -3, width, 10, 0xA0001111, 0);
		GlStateManager.disableDepth();		
		
		if(health.value)
			offset += getWidth(getHealth(player)) + getWidth(" ") - 1;
		
		drawString(getPlayerName(player) , w - offset  , 0, 0xFFFFFF);
		if(health.value) {
			int color;
			if(player.getHealth() == 10) color = 0xFFFF00;
			if (player.getHealth() > 10)
				color = Helper.colorUtils().blend(new Color(0xFF00FF00), new Color(0xFFFFFF00), (1 / player.getHealth() / 2) * (player.getHealth() - 10)).getRGB();
			else color = Helper.colorUtils().blend(new Color(0xFFFFFF00), new Color(0xFFFF0000), (1 / 10f) * player.getHealth()).getRGB();
			drawString(getHealth(player), w - getWidth(getHealth(player) + " ") , 0, color);
		}
		if(ping.value)
			drawString(getPing(player), -w + 1, 0, 0xFFCC00);
		GlStateManager.enableDepth();
	}
	private void drawString(String text, float x, float y, int color) {
		if(font.value)
			Helper.get2DUtils().drawCustomStringWithShadow(text, x, y, color);
		else mc.fontRendererObj.drawStringWithShadow(text, x, y, color);

	}
	private int getWidth(String text) {
		if(font.value)
			return (Pandora.testFont.getStringWidth(text) / 2) + 1;
		 else return mc.fontRendererObj.getStringWidth(text);
	}

	public void startDrawing(float x, float y, float z, EntityPlayer player) {
		float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
		double size = (getSize(player) / 10) * scale.getValue();
		GL11.glPushMatrix();
		Helper.get3DUtils().startDrawing();
		GL11.glTranslatef(x,y,z);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
		GL11.glScaled(-0.016666668F * size , -0.016666668F * size, 0.016666668F * size);
	
	}

	public void stopDrawing() {
		Helper.get3DUtils().stopDrawing();
		GlStateManager.color(1,1,1);
	
		GlStateManager.popMatrix();
	}
	private void renderNametag(EntityPlayer player, float x, float y, float z) {
		y += 1.55 + (player.isSneaking() ? 0.5D : 0.7D);
		startDrawing(x,y,z, player);
		drawNames(player);
		GL11.glColor4d(1,1,1,1);
		if (armor.value)
			renderArmor(player);
		stopDrawing();
	}
	private void renderArmor(EntityPlayer player){
		ItemStack[] renderStack = player.inventory.armorInventory;
		ItemStack armourStack;
		int length = renderStack.length;
		int xOffset = 0;
		for (ItemStack aRenderStack : renderStack) {
			armourStack = aRenderStack;
			if (armourStack != null)
				xOffset -= 8;
		}
		if (player.getHeldItem() != null) {
			xOffset -= 8;
			ItemStack stock = player.getHeldItem().copy();
			if (stock.hasEffect() && (stock.getItem() instanceof ItemTool || stock.getItem() instanceof ItemArmor))
				stock.stackSize = 1;
			renderItemStack(stock, xOffset, -20);
			xOffset += 16;
		}
		renderStack = player.inventory.armorInventory;
		for (int index = 3; index >= 0; index--) {
			armourStack = renderStack[index];
			if (armourStack != null) {
				renderItemStack(armourStack, xOffset, -20);
				xOffset += 16;
			}
		}
		GlStateManager.color(1,1,1,1);
	}

	private String getPlayerName(EntityPlayer player){
		String name = player.getDisplayName().getFormattedText();
		for(Friend f : FriendManager.friends) {
			if(f.getName().equalsIgnoreCase(player.getName())&& FriendManager.isFriend(player.getName()))
				name = "\247b" + f.getAlias();
		}
		return name;
	}
	private float getSize(EntityPlayer player) {
		return mc.thePlayer.getDistanceToEntity(player) / 4.0F <= 2.0F ? 2.0F : mc.thePlayer.getDistanceToEntity(player) / 4.0F;
	}
	private void renderItemStack(final ItemStack stack, int x, int y) {
		GlStateManager.pushMatrix();
		GlStateManager.depthMask(true);
		GlStateManager.clear(256);
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().zLevel = -150.0f;
		GlStateManager.disableDepth();
		GlStateManager.func_179090_x();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.func_179098_w();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		mc.getRenderItem().func_180450_b(stack, x, y);
		mc.getRenderItem().func_175030_a(mc.fontRendererObj, stack, x, y);
		mc.getRenderItem().zLevel = 0.0f;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableCull();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		double s = font.value ? 0.75 : 0.5;
		GlStateManager.scale(s, s, s);
		if(font.value) {
			x *= 0.66;
			y *= 0.66;
		}
		GlStateManager.disableDepth();
		renderEnchantText(stack, x, y);
		GlStateManager.enableDepth();
		GlStateManager.scale(2.0f, 2.0f, 2.0f);
		GlStateManager.popMatrix();
	}

	/**
	 *@Author anodise
	 */
	private void renderEnchantText(final ItemStack stack, final int x, final int y) {
		int enchantmentY = y - 24;
		if (stack.getEnchantmentTagList() != null && stack.getEnchantmentTagList().tagCount() >= 6) {
			Helper.get2DUtils().drawStringWithShadow("god", x * 2, enchantmentY, 16711680);
			return;
		}
		if (stack.getItem() instanceof ItemArmor) {
			final int protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180310_c.effectId, stack);
			final int projectileProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180308_g.effectId, stack);
			final int blastProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, stack);
			final int fireProtectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, stack);
			final int thornsLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack);
			final int unbreakingLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if(damage.value) {
				int damage = stack.getMaxDamage() - stack.getItemDamage();
				Helper.get2DUtils().drawStringWithShadow("" + damage, x * 2, y, 0xFFFFFF, RenderUtils.cgothic);
			}
			if (protectionLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "pr" + protectionLevel, x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (projectileProtectionLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "pp" + projectileProtectionLevel, x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (blastProtectionLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "bp" + blastProtectionLevel, x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (fireProtectionLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "fp" + fireProtectionLevel, x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (thornsLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "t" + thornsLevel, x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (unbreakingLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "u" + unbreakingLevel, x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemBow) {
			final int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack);
			final int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack);
			final int flameLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack);
			final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			if (powerLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "po" + powerLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (punchLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "pu" + punchLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (flameLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "f" + flameLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				Helper.get2DUtils().drawStringWithShadow( "u" + unbreakingLevel2, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
		}
		if (stack.getItem() instanceof ItemSword) {
			final int sharpnessLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180314_l.effectId, stack);
			final int knockbackLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_180313_o.effectId, stack);
			final int fireAspectLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack);
			final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);

			if (sharpnessLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "sh" + sharpnessLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (knockbackLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "kn" + knockbackLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (fireAspectLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "f" + fireAspectLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				Helper.get2DUtils().drawStringWithShadow( "ub" + unbreakingLevel2, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
			}
		}
		if(stack.getItem() instanceof ItemTool){
			final int unbreakingLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack);
			final int efficiencyLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, stack);
			final int fortuneLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack);
			final int silkTouch = EnchantmentHelper.getEnchantmentLevel(Enchantment.silkTouch.effectId, stack);
			if (efficiencyLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "eff" + efficiencyLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (fortuneLevel > 0) {
				Helper.get2DUtils().drawStringWithShadow( "fo" + fortuneLevel, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (silkTouch > 0) {
				Helper.get2DUtils().drawStringWithShadow( "st" + silkTouch, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
				enchantmentY += 8;
			}
			if (unbreakingLevel2 > 0) {
				Helper.get2DUtils().drawStringWithShadow( "ub" + unbreakingLevel2, x * 2, enchantmentY, 0x00FFFF, RenderUtils.cgothic);
			}
		}
		if (stack.getItem() == Items.golden_apple && stack.hasEffect()) {
			Helper.get2DUtils().drawStringWithShadow("god", x * 2, enchantmentY, 0x00CCFF, RenderUtils.cgothic);
		}
	}
	protected void addCommand(){
		Pandora.getCommandManager().cmds.add(new Command
				("Nametags", "Manages nametags", Logger.LogExecutionFail("Option, Options:", new String[]{"Scale", "Armor", "Health", "Damage", "Entities"}) , "nt") {
			@Override
			public void execute(String commandName, String[] arguments) {
				String message = arguments[1];
				String message2 = "";
				try { message2 = arguments[2];} catch (Exception e) {}
				switch (message.toLowerCase()) {
					case "scale":case"s":
					case "size":
						switch(message2){
							case "actual":
								logValue(scale);
								break;
							case "reset":
								scale.reset();
								break;
							default:
								int newScale = Integer.parseInt(message2);
								scale.setValue(newScale);
								Logger.logSetMessage("Nametags", "Size", scale);
								break;
						}
						break;
					case "ent":case"e":
					case "entities":
						switch(message2){
							case "actual":
								logValue(entities);
								break;
							case "reset":
								entities.reset();
								break;
							default:
								int newScale = Integer.parseInt(message2);
								entities.setValue(newScale);
								Logger.logSetMessage("Nametags", "Entities", entities);
								break;
						}
						break;
					case "armor":case"stuff":
					case "a":
						armor.value = !armor.value;
						Logger.logSetMessage("Nametags", "Armour", armor);
						break;
					case "damage":case"dmg":
					case "d":
						damage.value = !damage.value;
						Logger.logSetMessage("Nametags", "Damage", damage);
						break;
					case "invis":case"invisibles":
					case "i":
						invisibles.value = !invisibles.value;
						Logger.logSetMessage("Nametags", "Invisibles", invisibles);
						break;
					case "health":case"h":
					case "hp":
						health.value = !health.value;
						Logger.logSetMessage("Nametags", "Health", health);
						break;

					case "values":
						logValues();
						break;
					default:
						Logger.logChat(getError());
						break;
				}
			}});
	}
}