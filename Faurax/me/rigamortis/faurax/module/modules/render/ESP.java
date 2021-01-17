package me.rigamortis.faurax.module.modules.render;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.helpers.RenderHelper;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.entity.*;
import me.rigamortis.faurax.friends.*;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.utils.*;
import java.text.*;
import net.minecraft.entity.player.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import java.util.regex.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.renderer.*;

public class ESP extends Module implements CombatHelper, RenderHelper, Runnable
{
    public static boolean state;
    public static float alpha;
    public static boolean enabled;
    public static int items;
    public static Value mode;
    public static Value mobs;
    public static Value animals;
    public static Value players;
    public static Value color;
    public static Value colorRed;
    public static Value colorBlue;
    public static Value colorGreen;
    public static Value colorAlpha;
    public static Value width;
    public static Value innerWidth;
    public static Value cross;
    
    static {
        ESP.items = 0;
        ESP.mode = new Value("ESP", String.class, "Mode", "Outline", new String[] { "Outline", "2D", "2D Corner", "Box" });
        ESP.mobs = new Value("ESP", Boolean.TYPE, "Mobs", false);
        ESP.animals = new Value("ESP", Boolean.TYPE, "Animals", false);
        ESP.players = new Value("ESP", Boolean.TYPE, "Players", true);
        ESP.color = new Value("ESP", String.class, "Color", "Distance", new String[] { "Distance", "Team", "Health", "Custom" });
        ESP.colorRed = new Value("ESP", Float.TYPE, "Red", 0.0f, 0.0f, 1.0f);
        ESP.colorBlue = new Value("ESP", Float.TYPE, "Blue", 0.0f, 0.0f, 1.0f);
        ESP.colorGreen = new Value("ESP", Float.TYPE, "Green", 0.0f, 0.0f, 1.0f);
        ESP.colorAlpha = new Value("ESP", Float.TYPE, "Alpha", 0.0f, 0.0f, 1.0f);
        ESP.width = new Value("ESP", Float.TYPE, "Width", 4.0f, 1.0f, 5.0f);
        ESP.innerWidth = new Value("ESP", Float.TYPE, "Cross Width", 4.0f, 1.0f, 5.0f);
        ESP.cross = new Value("ESP", Boolean.TYPE, "Cross", true);
    }
    
    public ESP() {
        this.setName("ESP");
        this.setKey("Y");
        this.setType(ModuleType.RENDER);
        this.setColor(-15096001);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(ESP.mode);
        Client.getValues();
        ValueManager.values.add(ESP.mobs);
        Client.getValues();
        ValueManager.values.add(ESP.animals);
        Client.getValues();
        ValueManager.values.add(ESP.players);
        Client.getValues();
        ValueManager.values.add(ESP.color);
        Client.getValues();
        ValueManager.values.add(ESP.colorRed);
        Client.getValues();
        ValueManager.values.add(ESP.colorBlue);
        Client.getValues();
        ValueManager.values.add(ESP.colorGreen);
        Client.getValues();
        ValueManager.values.add(ESP.colorAlpha);
        Client.getValues();
        ValueManager.values.add(ESP.width);
        Client.getValues();
        ValueManager.values.add(ESP.cross);
        Client.getValues();
        ValueManager.values.add(ESP.innerWidth);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        ESP.state = true;
        new Thread(new ESP()).start();
        ESP.enabled = true;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        ESP.state = false;
        new Thread(new ESP()).start();
        ESP.enabled = false;
    }
    
    @EventTarget(4)
    public void renderWorld(final EventRenderWorld e) {
        if (this.isToggled()) {
            if (ESP.mode.getSelectedOption().equalsIgnoreCase("Box")) {
                for (final Object i : ESP.mc.theWorld.loadedEntityList) {
                    final Entity player = (Entity)i;
                    if (player != ESP.mc.thePlayer && validEntity(player) && player != null && player.getName() != ESP.mc.thePlayer.getName()) {
                        final float posX = (float)((float)player.lastTickPosX + (player.posX - player.lastTickPosX) * ESP.mc.timer.renderPartialTicks);
                        final float posY = (float)((float)player.lastTickPosY + (player.posY - player.lastTickPosY) * ESP.mc.timer.renderPartialTicks);
                        final float posZ = (float)((float)player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ESP.mc.timer.renderPartialTicks);
                        final float distance = ESP.mc.thePlayer.getDistanceToEntity(player);
                        final float health = ((EntityLivingBase)player).getHealth();
                        if (!FriendManager.isFriend(player.getName())) {
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Custom")) {
                                ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), ESP.colorRed.getFloatValue(), ESP.colorGreen.getFloatValue(), ESP.colorBlue.getFloatValue(), ESP.colorAlpha.getFloatValue());
                                if (ESP.cross.getBooleanValue()) {
                                    final GuiUtils guiUtils = ESP.guiUtils;
                                    GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), ESP.colorRed.getFloatValue(), ESP.colorGreen.getFloatValue(), ESP.colorBlue.getFloatValue(), ESP.colorAlpha.getFloatValue());
                                }
                            }
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Health")) {
                                final DecimalFormat decimal = new DecimalFormat("#.#");
                                final float percent = Float.valueOf(decimal.format(health / 2.0f));
                                if (percent >= 6.0f) {
                                    ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 0.1f, 1.0f, 0.1f, ESP.alpha);
                                    if (ESP.cross.getBooleanValue()) {
                                        final GuiUtils guiUtils2 = ESP.guiUtils;
                                        GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 0.1f, 1.0f, 0.1f, ESP.alpha);
                                    }
                                }
                                if (percent < 6.0f) {
                                    ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 1.0f, 0.5f, 0.0f, ESP.alpha);
                                    if (ESP.cross.getBooleanValue()) {
                                        final GuiUtils guiUtils3 = ESP.guiUtils;
                                        GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 1.0f, 0.5f, 0.0f, ESP.alpha);
                                    }
                                }
                                if (percent < 3.0f) {
                                    ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 1.0f, 0.0f, 0.0f, ESP.alpha);
                                    if (ESP.cross.getBooleanValue()) {
                                        final GuiUtils guiUtils4 = ESP.guiUtils;
                                        GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 1.0f, 0.0f, 0.0f, ESP.alpha);
                                    }
                                }
                            }
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Distance")) {
                                if (distance <= 5.0f) {
                                    ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 1.0f, 0.0f, 0.0f, ESP.alpha);
                                    if (ESP.cross.getBooleanValue()) {
                                        final GuiUtils guiUtils5 = ESP.guiUtils;
                                        GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 1.0f, 0.0f, 0.0f, ESP.alpha);
                                    }
                                }
                                else if (distance <= 40.0f) {
                                    ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 1.0f, distance / 100.0f, 0.0f, ESP.alpha);
                                    if (ESP.cross.getBooleanValue()) {
                                        final GuiUtils guiUtils6 = ESP.guiUtils;
                                        GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 1.0f, distance / 100.0f, 0.0f, ESP.alpha);
                                    }
                                }
                                else if (distance > 40.0f) {
                                    ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 0.0f, 255.0f, 0.0f, ESP.alpha);
                                    if (ESP.cross.getBooleanValue()) {
                                        final GuiUtils guiUtils7 = ESP.guiUtils;
                                        GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 0.0f, 255.0f, 0.0f, ESP.alpha);
                                    }
                                }
                            }
                            if (!ESP.color.getSelectedOption().equalsIgnoreCase("Team")) {
                                continue;
                            }
                            if (isOnSameTeam((EntityPlayer)player, ESP.mc.thePlayer)) {
                                ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 0.0f, 255.0f, 0.0f, ESP.alpha);
                                if (!ESP.cross.getBooleanValue()) {
                                    continue;
                                }
                                final GuiUtils guiUtils8 = ESP.guiUtils;
                                GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 0.0f, 255.0f, 0.0f, ESP.alpha);
                            }
                            else {
                                ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 255.0f, 0.0f, 0.0f, ESP.alpha);
                                if (!ESP.cross.getBooleanValue()) {
                                    continue;
                                }
                                final GuiUtils guiUtils9 = ESP.guiUtils;
                                GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 255.0f, 0.0f, 0.0f, ESP.alpha);
                            }
                        }
                        else {
                            ESP.guiUtils.drawOutlineForEntity(player, new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.width.getFloatValue(), 0.1f, 0.6f, 0.8f, ESP.alpha);
                            if (!ESP.cross.getBooleanValue()) {
                                continue;
                            }
                            final GuiUtils guiUtils10 = ESP.guiUtils;
                            GuiUtils.drawLines(new AxisAlignedBB(posX - RenderManager.renderPosX - 0.4000000059604645, posY - RenderManager.renderPosY - 0.10000000149011612, posZ - RenderManager.renderPosZ - 0.4000000059604645, posX + 0.4f - RenderManager.renderPosX, posY + player.height - RenderManager.renderPosY + 0.10000000149011612, posZ + 0.4f - RenderManager.renderPosZ), ESP.innerWidth.getFloatValue(), 0.1f, 0.6f, 0.8f, ESP.alpha);
                        }
                    }
                }
            }
            if (ESP.mode.getSelectedOption().equalsIgnoreCase("2D")) {
                for (final Object i : ESP.mc.theWorld.loadedEntityList) {
                    final Entity player = (Entity)i;
                    if (player != ESP.mc.thePlayer && validEntity(player) && player != null && player.getName() != ESP.mc.thePlayer.getName()) {
                        final float posX = (float)((float)player.lastTickPosX + (player.posX - player.lastTickPosX) * ESP.mc.timer.renderPartialTicks);
                        final float posY = (float)((float)player.lastTickPosY + (player.posY - player.lastTickPosY) * ESP.mc.timer.renderPartialTicks);
                        final float posZ = (float)((float)player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ESP.mc.timer.renderPartialTicks);
                        final float distance = ESP.mc.thePlayer.getDistanceToEntity(player);
                        final float health = ((EntityLivingBase)player).getHealth();
                        if (!FriendManager.isFriend(player.getName())) {
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Team")) {
                                if (isOnSameTeam((EntityPlayer)player, ESP.mc.thePlayer)) {
                                    this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.0f, 1.0f, 0.0f, ESP.alpha);
                                }
                                else {
                                    this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, ESP.alpha);
                                }
                            }
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Custom")) {
                                this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, ESP.colorRed.getFloatValue(), ESP.colorGreen.getFloatValue(), ESP.colorBlue.getFloatValue(), ESP.colorAlpha.getFloatValue());
                            }
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Distance")) {
                                if (distance <= 5.0f) {
                                    this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, ESP.alpha);
                                }
                                else if (distance <= 40.0f) {
                                    this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, distance / 100.0f, 0.0f, ESP.alpha);
                                }
                                else if (distance > 40.0f) {
                                    this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.0f, 255.0f, 0.0f, ESP.alpha);
                                }
                            }
                            if (!ESP.color.getSelectedOption().equalsIgnoreCase("Health")) {
                                continue;
                            }
                            final DecimalFormat decimal = new DecimalFormat("#.#");
                            final float percent = Float.valueOf(decimal.format(health / 2.0f));
                            if (percent >= 6.0f) {
                                this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.1f, 1.0f, 0.1f, ESP.alpha);
                            }
                            if (percent < 6.0f) {
                                this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.5f, 0.0f, ESP.alpha);
                            }
                            if (percent >= 3.0f) {
                                continue;
                            }
                            this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, ESP.alpha);
                        }
                        else {
                            this.draw2D(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.0f, 0.7f, 0.8f, ESP.alpha);
                        }
                    }
                }
            }
            if (ESP.mode.getSelectedOption().equalsIgnoreCase("2D Corner")) {
                for (final Object i : ESP.mc.theWorld.loadedEntityList) {
                    final Entity player = (Entity)i;
                    if (player != ESP.mc.thePlayer && validEntity(player) && player != null && player.getName() != ESP.mc.thePlayer.getName()) {
                        final float posX = (float)((float)player.lastTickPosX + (player.posX - player.lastTickPosX) * ESP.mc.timer.renderPartialTicks);
                        final float posY = (float)((float)player.lastTickPosY + (player.posY - player.lastTickPosY) * ESP.mc.timer.renderPartialTicks);
                        final float posZ = (float)((float)player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ESP.mc.timer.renderPartialTicks);
                        final float distance = ESP.mc.thePlayer.getDistanceToEntity(player);
                        final float health = ((EntityLivingBase)player).getHealth();
                        if (!FriendManager.isFriend(player.getName())) {
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Team")) {
                                if (isOnSameTeam((EntityPlayer)player, ESP.mc.thePlayer)) {
                                    this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.0f, 1.0f, 0.0f, ESP.alpha);
                                }
                                else {
                                    this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, ESP.alpha);
                                }
                            }
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Custom")) {
                                this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, ESP.colorRed.getFloatValue(), ESP.colorGreen.getFloatValue(), ESP.colorBlue.getFloatValue(), ESP.colorAlpha.getFloatValue());
                            }
                            if (ESP.color.getSelectedOption().equalsIgnoreCase("Distance")) {
                                if (distance <= 5.0f) {
                                    this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, ESP.alpha);
                                }
                                else if (distance <= 40.0f) {
                                    this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, distance / 100.0f, 0.0f, ESP.alpha);
                                }
                                else if (distance > 40.0f) {
                                    this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.0f, 255.0f, 0.0f, ESP.alpha);
                                }
                            }
                            if (!ESP.color.getSelectedOption().equalsIgnoreCase("Health")) {
                                continue;
                            }
                            final DecimalFormat decimal = new DecimalFormat("#.#");
                            final float percent = Float.valueOf(decimal.format(health / 2.0f));
                            if (percent >= 6.0f) {
                                this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.1f, 1.0f, 0.1f, ESP.alpha);
                            }
                            if (percent < 6.0f) {
                                this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.5f, 0.0f, ESP.alpha);
                            }
                            if (percent >= 3.0f) {
                                continue;
                            }
                            this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 1.0f, 0.0f, 0.0f, ESP.alpha);
                        }
                        else {
                            this.draw2DCorner(player, posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ, 0.0f, 0.7f, 0.8f, ESP.alpha);
                        }
                    }
                }
            }
        }
    }
    
    public static boolean isOnSameTeam(final EntityPlayer e, final EntityPlayer e2) {
        return e.getDisplayName().getFormattedText().contains("§" + getTeamFromName(e)) && e2.getDisplayName().getFormattedText().contains("§" + getTeamFromName(e));
    }
    
    public static String getTeamFromName(final Entity e) {
        final Matcher m = Pattern.compile("§(.).*§r").matcher(e.getDisplayName().getFormattedText());
        if (m.find()) {
            return m.group(1);
        }
        return "f";
    }
    
    public void draw2D(final Entity e, final double posX, final double posY, final double posZ, final float alpha, final float red, final float green, final float blue) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GlStateManager.func_179098_w();
        GlStateManager.depthMask(true);
        ESP.guiUtils.drawOutlineRect(-6.2f, -19.2f, 6.2f, 2.2f, Integer.MIN_VALUE);
        ESP.guiUtils.drawOutlineRect(-5.8f, -18.8f, 5.8f, 1.8f, Integer.MIN_VALUE);
        ESP.guiUtils.drawOutlinedRect(-6.0f, -19.0f, 6.0f, 2.0f, alpha, red, green, blue);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
    }
    
    public void draw2DCorner(final Entity e, final double posX, final double posY, final double posZ, final float alpha, final float red, final float green, final float blue) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(posX, posY, posZ);
        GL11.glNormal3f(0.0f, 0.0f, 0.0f);
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.scale(-0.1, -0.1, 0.1);
        GL11.glDisable(2896);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GlStateManager.func_179098_w();
        GlStateManager.depthMask(true);
        final GuiUtils guiUtils = ESP.guiUtils;
        GuiUtils.drawRectColor(4.0, -20.0, 7.0, -19.5, alpha, red, green, blue);
        final GuiUtils guiUtils2 = ESP.guiUtils;
        GuiUtils.drawRect(-7.0, -20.0, -4.0, -19.5, alpha, red, green, blue);
        final GuiUtils guiUtils3 = ESP.guiUtils;
        GuiUtils.drawRect(6.5, -20.0, 7.0, -17.5, alpha, red, green, blue);
        final GuiUtils guiUtils4 = ESP.guiUtils;
        GuiUtils.drawRect(-7.0, -20.0, -6.5, -17.5, alpha, red, green, blue);
        final GuiUtils guiUtils5 = ESP.guiUtils;
        GuiUtils.drawRect(-7.0, 2.5, -4.0, 3.0, alpha, red, green, blue);
        final GuiUtils guiUtils6 = ESP.guiUtils;
        GuiUtils.drawRect(4.0, 2.5, 7.0, 3.0, alpha, red, green, blue);
        final GuiUtils guiUtils7 = ESP.guiUtils;
        GuiUtils.drawRect(-7.0, 0.5, -6.5, 3.0, alpha, red, green, blue);
        final GuiUtils guiUtils8 = ESP.guiUtils;
        GuiUtils.drawRect(6.5, 0.5, 7.0, 3.0, alpha, red, green, blue);
        final GuiUtils guiUtils9 = ESP.guiUtils;
        GuiUtils.drawRect(7.0, -20.0, 7.300000190734863, -17.5, Integer.MIN_VALUE);
        final GuiUtils guiUtils10 = ESP.guiUtils;
        GuiUtils.drawRect(-7.300000190734863, -20.0, -7.0, -17.5, Integer.MIN_VALUE);
        final GuiUtils guiUtils11 = ESP.guiUtils;
        GuiUtils.drawRect(4.0, -20.299999237060547, 7.300000190734863, -20.0, Integer.MIN_VALUE);
        final GuiUtils guiUtils12 = ESP.guiUtils;
        GuiUtils.drawRect(-7.300000190734863, -20.299999237060547, -4.0, -20.0, Integer.MIN_VALUE);
        final GuiUtils guiUtils13 = ESP.guiUtils;
        GuiUtils.drawRect(-7.0, 3.0, -4.0, 3.299999952316284, Integer.MIN_VALUE);
        final GuiUtils guiUtils14 = ESP.guiUtils;
        GuiUtils.drawRect(4.0, 3.0, 7.0, 3.299999952316284, Integer.MIN_VALUE);
        final GuiUtils guiUtils15 = ESP.guiUtils;
        GuiUtils.drawRect(-7.300000190734863, 0.5, -7.0, 3.299999952316284, Integer.MIN_VALUE);
        final GuiUtils guiUtils16 = ESP.guiUtils;
        GuiUtils.drawRect(7.0, 0.5, 7.300000190734863, 3.299999952316284, Integer.MIN_VALUE);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glEnable(2896);
        GlStateManager.popMatrix();
    }
    
    public static boolean validEntity(final Entity e) {
        final boolean isValidEntity = (ESP.mobs.getBooleanValue() && e instanceof EntityMob && !(e instanceof EntityAnimal) && !(e instanceof EntityPlayer)) || (ESP.animals.getBooleanValue() && e instanceof EntityAnimal && !(e instanceof EntityMob) && !(e instanceof EntityPlayer)) || (ESP.players.getBooleanValue() && e instanceof EntityPlayer && !(e instanceof EntityMob) && !(e instanceof EntityAnimal)) || (ESP.items == 1 && e instanceof EntityItem && !(e instanceof EntityPlayer) && !(e instanceof EntityMob) && !(e instanceof EntityAnimal));
        return isValidEntity;
    }
    
    public static void renderOne() {
        GL11.glPushAttrib(1048575);
        GL11.glDisable(3008);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glEnable(2960);
        GL11.glClear(1024);
        GL11.glClearStencil(15);
        GL11.glStencilFunc(512, 1, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glLineWidth(0.5f);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderTwo() {
        GL11.glStencilFunc(512, 0, 15);
        GL11.glStencilOp(7681, 7681, 7681);
        GL11.glPolygonMode(1032, 6914);
    }
    
    public static void renderThree() {
        GL11.glStencilFunc(514, 1, 15);
        GL11.glStencilOp(7680, 7680, 7680);
        GL11.glPolygonMode(1032, 6913);
    }
    
    public static void renderFour() {
        GL11.glEnable(10754);
        GL11.glPolygonOffset(1.0f, -2000000.0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
    }
    
    public static void renderFive() {
        GL11.glPolygonOffset(1.0f, 2000000.0f);
        GL11.glDisable(10754);
        GL11.glDisable(2960);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        GL11.glPopAttrib();
    }
    
    @Override
    public void run() {
        if (ESP.state) {
            while (ESP.alpha < 0.7f) {
                if (!Client.getModules().isModToggled("ESP")) {
                    break;
                }
                ESP.alpha += 0.01f;
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            ESP.alpha = 0.0f;
        }
    }
}
