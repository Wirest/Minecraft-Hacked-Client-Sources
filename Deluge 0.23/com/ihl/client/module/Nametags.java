package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventRender;
import com.ihl.client.font.MinecraftFontRenderer;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import com.ihl.client.util.*;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@EventHandler(events = {EventRender.class})
public class Nametags extends Module {

    public Nametags(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("health", new Option("Health", "Show the players health", new ValueBoolean(true), Option.Type.BOOLEAN));
        options.put("ping", new Option("Ping", "Show the players connection latency", new ValueBoolean(true), Option.Type.BOOLEAN));
        options.put("inventory", new Option("Inventory", "Show the players current equipment", new ValueBoolean(true), Option.Type.BOOLEAN));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    protected void onEvent(Event event) {
        boolean health = Option.get(options, "health").BOOLEAN();
        boolean ping = Option.get(options, "ping").BOOLEAN();
        boolean inventory = Option.get(options, "inventory").BOOLEAN();

        if (event instanceof EventRender) {
            EventRender e = (EventRender) event;
            if (e.type == Event.Type.POST) {
                GlStateManager.pushMatrix();
                double scale = Helper.scaled().getScaleFactor() / Math.pow(Helper.scaled().getScaleFactor(), 2);
                GlStateManager.scale(scale, scale, scale);

                for (Object object : Helper.world().getLoadedEntityList()) {
                    if (object instanceof EntityPlayer && object != Helper.player()) {
                        EntityPlayer entity = (EntityPlayer) object;

                        if (EntityUtil.entityData.containsKey(entity)) {
                            List<Vec3> data = EntityUtil.entityData.get(entity);
                            Vec3 point = data.get(4);

                            if (point.zCoord >= 0 && point.zCoord < 1) {
                                double distance = Helper.player().getDistanceToEntity(entity);
                                MinecraftFontRenderer font = RenderUtil.fontSmall[0];
                                if (distance < 10) {
                                    font = RenderUtil.fontMedium[0];
                                }
                                if (distance < 5) {
                                    font = RenderUtil.fontLarge[0];
                                }

                                String displayTag = entity.getDisplayName().getFormattedText();
                                int color = -1;

                                double width = (font.getStringWidth(displayTag) / 2) + 4;
                                double height = (font.getStringHeight(displayTag) / 2) + 1;
                                RenderUtil2D.rectBordered((int) (point.xCoord - width), (int) (point.yCoord - (height * 2)), (int) (point.xCoord + width), (int) point.yCoord, 0x80000000, 0xFF000000, 2f);
                                RenderUtil2D.string(font, displayTag, (int) point.xCoord - 1, (int) point.yCoord - 2, color, 0, -1, true);

                                if (health) {
                                    float playerHealth = entity.getHealth();
                                    String hp = MathHelper.ceiling_float_int(playerHealth) + "";

                                    playerHealth = Math.min(Math.max(0, playerHealth), 20);
                                    if (playerHealth > 10) {
                                        color = ColorUtil.blend(new Color(0xFF00FF00), new Color(0xFFFFFF00), (1 / 10f) * (playerHealth - 10)).getRGB();
                                    } else {
                                        color = ColorUtil.blend(new Color(0xFFFFFF00), new Color(0xFFFF0000), (1 / 10f) * playerHealth).getRGB();
                                    }

                                    double w = font.getStringWidth(hp);

                                    RenderUtil2D.rectBordered((int) (point.xCoord - width - 10 - (w)), (int) (point.yCoord - (height * 2)), (int) (point.xCoord - width - 4), (int) point.yCoord, 0x80000000, 0xFF000000, 2f);
                                    RenderUtil2D.string(font, hp, (int) (point.xCoord - width - 8), (int) point.yCoord - 2, color, -1, -1, true);
                                }

                                if (ping) {
                                    NetworkPlayerInfo playerInfo = Helper.player().sendQueue.func_175104_a(entity.getName());
                                    float playerPing = playerInfo != null ? playerInfo.getResponseTime() : -1;
                                    String p = (int) playerPing + "";

                                    color = 0xFF00FFFF;

                                    double w = font.getStringWidth(p);

                                    RenderUtil2D.rectBordered((int) (point.xCoord + width + 4), (int) (point.yCoord - (height * 2)), (int) (point.xCoord + width + 12 + w), (int) point.yCoord, 0x80000000, 0xFF000000, 2f);
                                    RenderUtil2D.string(font, p, (int) (point.xCoord + width + 7), (int) point.yCoord - 2, color, 1, -1, true);
                                }

                                if (inventory) {
                                    List<ItemStack> items = new ArrayList();
                                    for (int i = 4; i >= 0; i--) {
                                        ItemStack stack = entity.getEquipmentInSlot(i);
                                        if (stack != null) {
                                            items.add(stack);
                                        }
                                    }

                                    int ax = (int) (point.xCoord - (items.size() * 16));
                                    int ay = (int) (point.yCoord - (height * 2) - 32) - 6;

                                    if (items.size() > 0) {
                                        RenderUtil2D.rectBordered(ax - 2, ay - 2, ax + (items.size() * 32) + 2, ay + 34, 0x80000000, 0xFF000000, 2f);
                                        GlStateManager.pushMatrix();
                                        GlStateManager.scale(2, 2, 2);
                                        RenderHelper.enableGUIStandardItemLighting();
                                        for (int i = 0; i < items.size(); i++) {
                                            double xx = MathUtil.roundInc(ax + (i * 32), 1);
                                            double yy = MathUtil.roundInc(ay, 1);

                                            GlStateManager.pushMatrix();
                                            GlStateManager.translate(xx / 2, yy / 2, 0);
                                            ItemStack stack = items.get(i);
                                            Helper.mc().getRenderItem().func_180450_b(stack, 0, 0);
                                            Helper.mc().getRenderItem().func_180453_a(Helper.mc().fontRendererObj, stack, 0, 0, stack.stackSize > 1 ? "" + stack.stackSize : "");

                                            if (stack.getMaxDamage() != 0) {
                                                GlStateManager.scale(0.5, 0.5, 0.5);
                                                GlStateManager.disableDepth();
                                                GlStateManager.disableLighting();
                                                Helper.mc().fontRendererObj.drawStringWithShadow("" + (stack.getMaxDamage() - stack.getItemDamage()), 0, 0, -1);
                                                GlStateManager.enableLighting();
                                                GlStateManager.enableDepth();
                                            }

                                            GlStateManager.popMatrix();
                                        }
                                        RenderHelper.disableStandardItemLighting();
                                        GlStateManager.popMatrix();
                                    }
                                }
                            }
                        }
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

}
