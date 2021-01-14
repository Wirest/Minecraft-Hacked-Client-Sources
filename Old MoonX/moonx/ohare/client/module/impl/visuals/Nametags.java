package moonx.ohare.client.module.impl.visuals;

import com.mojang.realmsclient.gui.ChatFormatting;
import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.NameplateEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.combat.AntiBot;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.font.MCFontRenderer;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.ColorValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.FontValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Nametags extends Module {
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    private BooleanValue health = new BooleanValue("Health", true);
    private BooleanValue armor = new BooleanValue("Armor", true);
    private BooleanValue distance = new BooleanValue("Distance", true);
    private BooleanValue potions = new BooleanValue("Potions", true);
    private BooleanValue stripColorCodes = new BooleanValue("StripColorCodes", false);
    private ColorValue color = new ColorValue("Color", new Color(240, 177, 175).getRGB());
    private EnumValue<backDropMode> backDropModeEnumValue = new EnumValue<>("BackdropMode", backDropMode.BOTH);
    private BooleanValue font = new BooleanValue("Font", true);
    public FontValue fontValue = new FontValue("NameFont", new MCFontRenderer(new Font("Arial", Font.PLAIN, 16), true, true));

    public Nametags() {
        super("Nametags", Category.VISUALS, new Color(240, 177, 175).getRGB());
    }

    private enum backDropMode {
        BOTH, ARMOR, NAME, NONE
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        getMc().theWorld.loadedEntityList.forEach(entity -> {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) entity;
                if (isValid(ent) && entity.getUniqueID() != getMc().thePlayer.getUniqueID() && RenderUtil.isInViewFrustrum(ent)) {
                    double posX = RenderUtil.interpolate(entity.posX, entity.lastTickPosX, event.getPartialTicks());
                    double posY = RenderUtil.interpolate(entity.posY, entity.lastTickPosY, event.getPartialTicks());
                    double posZ = RenderUtil.interpolate(entity.posZ, entity.lastTickPosZ, event.getPartialTicks());
                    double width = entity.width / 1.5;
                    double height = entity.height + (entity.isSneaking() ? -0.3 : 0.2);
                    AxisAlignedBB aabb = new AxisAlignedBB(posX - width, posY, posZ - width, posX + width, posY + height + 0.05, posZ + width);
                    List<Vector3d> vectors = Arrays.asList(new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ), new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ));
                    getMc().entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                    Vector4d position = null;
                    for (Vector3d vector : vectors) {
                        vector = RenderUtil.project(vector.x - getMc().getRenderManager().viewerPosX, vector.y - getMc().getRenderManager().viewerPosY, vector.z - getMc().getRenderManager().viewerPosZ);
                        if (vector != null && vector.z >= 0.0 && vector.z < 1.0) {
                            if (position == null) {
                                position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                            }
                            position.x = Math.min(vector.x, position.x);
                            position.y = Math.min(vector.y, position.y);
                            position.z = Math.max(vector.x, position.z);
                            position.w = Math.max(vector.y, position.w);
                        }
                    }
                    getMc().entityRenderer.setupOverlayRendering();
                    if (position != null) {
                        GL11.glPushMatrix();
                        if (ent instanceof EntityPlayer) {
                            if (armor.isEnabled())
                                drawArmor((EntityPlayer) ent, (position.x + ((position.z - position.x) / 2)), position.y - 21 - fontValue.getValue().getHeight() * 2);
                            if (potions.isEnabled())
                                drawPotions((EntityPlayer) ent, (position.x + ((position.z - position.x) / 2)), position.y - 13 - fontValue.getValue().getHeight() * 2);
                        }
                        final float x = (float) position.x;
                        final float x2 = (float) position.z;
                        final float y = (float) position.y - 1;
                        final String healthtext = (ent.getHealth() >= 16 ? ChatFormatting.GREEN : ent.getHealth() >= 12 ? ChatFormatting.YELLOW : ent.getHealth() >= 8 ? ChatFormatting.RED : ChatFormatting.DARK_RED) + " " + (int) ent.getHealth();
                        final String nametext = (distance.isEnabled() ? "(" + Math.round(getMc().thePlayer.getDistance(ent.posX, ent.posY, ent.posZ)) + "m) " : "") + (((Moonx.INSTANCE.getFriendManager().isFriend(ent.getName()) && Moonx.INSTANCE.getFriendManager().getFriend(ent.getName()).getAlias() != null) ? Moonx.INSTANCE.getFriendManager().getFriend(ent.getName()).getAlias() : (stripColorCodes.isEnabled() ?StringUtils.stripControlCodes(ent.getName()):ent.getDisplayName().getUnformattedText()))) + (health.isEnabled() ? healthtext : "");
                        if (backDropModeEnumValue.getValue() == backDropMode.BOTH || backDropModeEnumValue.getValue() == backDropMode.NAME)
                            RenderUtil.drawRect((x + (x2 - x) / 2) - (font.isEnabled() ? fontValue.getValue().getStringWidth(nametext) : getMc().fontRendererObj.getStringWidth(nametext) / 2) / 2 - 2.5, y - (font.isEnabled() ? fontValue.getValue().getHeight() + 6 : getMc().fontRendererObj.FONT_HEIGHT), (font.isEnabled() ? fontValue.getValue().getStringWidth(nametext) : getMc().fontRendererObj.getStringWidth(nametext) / 2) + 5, (font.isEnabled() ? fontValue.getValue().getHeight() + 6 : getMc().fontRendererObj.FONT_HEIGHT), new Color(0, 0, 0, 120).getRGB());
                        if (font.isEnabled())
                            fontValue.getValue().drawStringWithShadow(nametext, (x + ((x2 - x) / 2)) - (fontValue.getValue().getStringWidth(nametext) / 2), y - fontValue.getValue().getHeight() - 2, getNameColor(ent));
                        else {
                            GL11.glPushMatrix();
                            GL11.glScalef(0.5f, 0.5f, 0.5f);
                            getMc().fontRendererObj.drawStringWithShadow(nametext, ((x + ((x2 - x) / 2)) - (getMc().fontRendererObj.getStringWidth(nametext) / 4)) * 2, (y - getMc().fontRendererObj.FONT_HEIGHT / 2 - 2) * 2, getNameColor(ent));
                            GL11.glPopMatrix();
                            GL11.glScalef(1.0f, 1.0f, 1.0f);
                        }
                        GL11.glPopMatrix();
                    }
                }
            }
        });
    }

    @Handler
    public void onNamePlate(NameplateEvent event) {
        event.setCanceled(event.getEntity() instanceof EntityLivingBase && isValid((EntityLivingBase) event.getEntity()));
    }

    public void drawArmor(EntityPlayer player, double posX, double posY) {
        final ArrayList<ItemStack> stacks = new ArrayList<>();
        if (player.getHeldItem() != null) stacks.add(player.getHeldItem());
        stacks.addAll(Arrays.stream(player.inventory.armorInventory).filter(Objects::nonNull).collect(Collectors.toList()));
        double offset = (posX - (stacks.size() * 9.5));
        for (ItemStack stack : stacks) {
            if (backDropModeEnumValue.getValue() == backDropMode.BOTH || backDropModeEnumValue.getValue() == backDropMode.ARMOR)
                RenderUtil.drawRect(offset - 1, posY - 1, 18, 18, new Color(0, 0, 0, 120).getRGB());
            GlStateManager.enableLighting();
            getMc().getRenderItem().renderItemIntoGUI(stack, offset, posY);
            getMc().getRenderItem().renderItemOverlayIntoGUI(getMc().fontRendererObj, stack, offset, posY, "");
            GlStateManager.disableLighting();
            NBTTagList enchants = stack.getEnchantmentTagList();
            GlStateManager.pushMatrix();
            GlStateManager.disableDepth();
            if (stack.isStackable()) {
                if (font.isEnabled())
                    fontValue.getValue().drawStringWithShadow(String.valueOf(stack.stackSize), offset, posY + 11, 0xDDD1E6);
                else {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 0.5f);
                    getMc().fontRendererObj.drawStringWithShadow(String.valueOf(stack.stackSize), (float) (offset * 2), (float) (posY + 11) * 2, 0xDDD1E6);
                    GL11.glPopMatrix();
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                }
            }
            if (stack.getItem() == Items.golden_apple && stack.getMetadata() == 1) {
                if (font.isEnabled())
                    fontValue.getValue().drawStringWithShadow("op", offset, posY + 2, 0xFFFF0000);
                else {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 0.5f);
                    getMc().fontRendererObj.drawStringWithShadow("op", (float) (offset * 2), (float) ((posY + 2) * 2), 0xFFFF0000);
                    GL11.glPopMatrix();
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                }
            }
            Enchantment[] goodEnchants = new Enchantment[]{Enchantment.protection, Enchantment.unbreaking, Enchantment.sharpness, Enchantment.fireAspect, Enchantment.efficiency, Enchantment.featherFalling, Enchantment.power, Enchantment.flame, Enchantment.punch, Enchantment.fortune, Enchantment.infinity, Enchantment.thorns};
            if (enchants != null) {
                double enchantmentY = posY + 11;
                if (enchants.tagCount() > 3) {
                    if (font.isEnabled())
                        fontValue.getValue().drawStringWithShadow("god", offset, enchantmentY, new Color(0xF0311D).getRGB());
                    else {
                        GL11.glPushMatrix();
                        GL11.glScalef(0.5f, 0.5f, 0.5f);
                        getMc().fontRendererObj.drawStringWithShadow("god", (float) (offset * 2), (float) (enchantmentY * 2), new Color(0xF0311D).getRGB());
                        GL11.glPopMatrix();
                        GL11.glScalef(1.0f, 1.0f, 1.0f);
                    }
                } else {
                    for (int index = 0; index < enchants.tagCount(); ++index) {
                        short id = enchants.getCompoundTagAt(index).getShort("id");
                        short level = enchants.getCompoundTagAt(index).getShort("lvl");
                        Enchantment enc = Enchantment.getEnchantmentById(id);
                        for (Enchantment goodEnchant : goodEnchants) {
                            if (enc == goodEnchant) {
                                String encName = Objects.requireNonNull(enc).getTranslatedName(level).substring(0, 1).toLowerCase();
                                if (level > 99) encName = encName + "99+";
                                else encName = encName + level;
                                if (font.isEnabled())
                                    fontValue.getValue().drawStringWithShadow(encName, offset, enchantmentY, 0xDDD1E6);
                                else {
                                    GL11.glPushMatrix();
                                    GL11.glScalef(0.5f, 0.5f, 0.5f);
                                    getMc().fontRendererObj.drawStringWithShadow(encName, (float) (offset * 2), (float) (enchantmentY * 2), 0xDDD1E6);
                                    GL11.glPopMatrix();
                                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                                }
                                enchantmentY -= 5;
                                break;
                            }
                        }
                    }
                }
            }
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
            offset += 19;
        }
    }

    public void drawPotions(EntityPlayer player, double posX, double posY) {
        final ArrayList<ItemStack> stacks = new ArrayList<>();
        if (player.getHeldItem() != null) stacks.add(player.getHeldItem());
        stacks.addAll(Arrays.stream(player.inventory.armorInventory).filter(Objects::nonNull).collect(Collectors.toList()));
        double offset = posY - (stacks.isEmpty() ? font.isEnabled() ? 0 : -4 : font.isEnabled() ? 20 : 16);
        for (Object o : player.getActivePotionEffects()) {
            PotionEffect effect = (PotionEffect) o;
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            boolean potRanOut = effect.getDuration() != 0.0;
            String effectpower = " ";
            if (potion != null && player.isPotionActive(potion) && potRanOut) {
                if (effect.getAmplifier() == 1) {
                    effectpower = " II ";
                } else if (effect.getAmplifier() == 2) {
                    effectpower = " III ";
                } else if (effect.getAmplifier() == 3) {
                    effectpower = " IV ";
                }
                if (font.isEnabled())
                    fontValue.getValue().drawStringWithShadow(I18n.format(potion.getName()) + ChatFormatting.GRAY + effectpower + ": " + Potion.getDurationString(effect), posX - fontValue.getValue().getStringWidth(I18n.format(potion.getName()) + ChatFormatting.GRAY + effectpower + ": " + Potion.getDurationString(effect)) / 2, offset, potion.getLiquidColor());
                else {
                    GL11.glPushMatrix();
                    GL11.glScalef(0.5f, 0.5f, 0.5f);
                    getMc().fontRendererObj.drawStringWithShadow(I18n.format(potion.getName()) + ChatFormatting.GRAY + effectpower + ": " + Potion.getDurationString(effect), (float) ((posX - getMc().fontRendererObj.getStringWidth(I18n.format(potion.getName()) + ChatFormatting.GRAY + effectpower + ": " + Potion.getDurationString(effect)) / 4) * 2), (float) (offset * 2), potion.getLiquidColor());
                    GL11.glPopMatrix();
                    GL11.glScalef(1.0f, 1.0f, 1.0f);
                }
                offset -= font.isEnabled() ? 10 : 6;
            }
        }
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && getMc().thePlayer != entity && entity.getEntityId() != -1488 && isValidType(entity) && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || ((mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime)) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private int getNameColor(EntityLivingBase ent) {
        if (Moonx.INSTANCE.getFriendManager().isFriend(ent.getName())) return new Color(122, 190, 255).getRGB();
        else if (ent.getName().equals(getMc().thePlayer.getName())) return new Color(0xFF99ff99).getRGB();
        return color.getValue();
    }
}
