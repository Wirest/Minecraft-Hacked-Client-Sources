package moonx.ohare.client.module.impl.player;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.*;

import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import moonx.ohare.client.utils.value.impl.RangedValue;
import net.minecraft.block.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.*;
import net.minecraft.util.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AntiAim extends Module {
    private EnumValue<mode> Mode = new EnumValue<>("Mode", mode.EXPERIMENTAL);
    public static NumberValue<Integer> speed = new NumberValue<>("Speed", 25, 2, 55, 1);
    private BooleanValue protection = new BooleanValue("Killaura Protection", "Killaura Protection", true);
    private TimerUtil timerUtil = new TimerUtil();
    private float KeepYaw, KeepPitch;

    public AntiAim() {
        super("AntiAim", Category.PLAYER, new Color(0xAF0E00).getRGB());
        setRenderLabel("Anti Aim");
    }

    @Override
    public void onDisable() {
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
        switch (Mode.getValue()) {
            case NORMAL:
                if (event.isPre()) {
                    if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
                        float sens = CombatUtil.getSensitivityMultiplier();
                        float yaw = KeepYaw + speed.getValue() + MathUtils.getRandomInRange(0.1F, 5F);
                        yaw = Math.round(yaw / sens) * sens;
                        float pitch = 90F;
                        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
                        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
                        event.setPitch(pitch);
                        KeepYaw = yaw;
                        KeepPitch = pitch;
                    }
                }
                break;
            case EXPERIMENTAL:
                if (event.isPre()) {

                }
                break;
        }
    }
    @Handler
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook || event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook) {
            C03PacketPlayer.C06PacketPlayerPosLook packet = (C03PacketPlayer.C06PacketPlayerPosLook) event.getPacket();
            packet.setYaw(KeepYaw);
        }
    }

    private enum mode {
        NORMAL, EXPERIMENTAL
    }
}
