/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.eventhandler.EventPriority
 *  cpw.mods.fml.common.eventhandler.SubscribeEvent
 *  cpw.mods.fml.common.gameevent.InputEvent$KeyInputEvent
 *  cpw.mods.fml.common.gameevent.TickEvent$PlayerTickEvent
 *  me.xtrm.atlaspluginloader.core.event.Event$State
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.event.events.player.EventClick
 *  me.xtrm.delta.api.event.events.player.EventClick$ClickType
 *  me.xtrm.delta.api.event.events.player.EventClick$MouseButton
 *  me.xtrm.delta.api.event.events.player.EventJump
 *  me.xtrm.delta.api.event.events.player.EventKeyboard
 *  me.xtrm.delta.api.event.events.render.EventRender2D
 *  me.xtrm.delta.api.event.events.render.EventRender3D
 *  me.xtrm.delta.api.event.events.update.EventTick
 *  me.xtrm.delta.api.event.events.update.EventUpdate
 *  me.xtrm.delta.api.module.IModule
 *  net.minecraft.client.entity.EntityClientPlayerMP
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.gui.GuiChat
 *  net.minecraft.client.gui.GuiCommandBlock
 *  net.minecraft.client.gui.GuiEnchantment
 *  net.minecraft.client.gui.GuiHopper
 *  net.minecraft.client.gui.GuiIngameMenu
 *  net.minecraft.client.gui.GuiRepair
 *  net.minecraft.client.gui.inventory.GuiBeacon
 *  net.minecraft.client.gui.inventory.GuiBrewingStand
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainerCreative
 *  net.minecraft.client.gui.inventory.GuiCrafting
 *  net.minecraft.client.gui.inventory.GuiDispenser
 *  net.minecraft.client.gui.inventory.GuiEditSign
 *  net.minecraft.client.gui.inventory.GuiFurnace
 *  net.minecraft.client.gui.inventory.GuiInventory
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C03PacketPlayer$C05PacketPlayerLook
 *  net.minecraft.network.play.client.C03PacketPlayer$C06PacketPlayerPosLook
 *  net.minecraftforge.client.event.GuiOpenEvent
 *  net.minecraftforge.client.event.MouseEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent
 *  net.minecraftforge.client.event.RenderGameOverlayEvent$ElementType
 *  net.minecraftforge.client.event.RenderLivingEvent$Specials$Pre
 *  net.minecraftforge.client.event.RenderWorldLastEvent
 *  net.minecraftforge.client.gui.ForgeGuiFactory$ForgeConfigGui
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingJumpEvent
 *  net.minecraftforge.event.entity.living.LivingEvent$LivingUpdateEvent
 *  org.lwjgl.input.Keyboard
 */
package delta;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import delta.Class45;
import delta.Class55;
import delta.client.DeltaClient;
import delta.guis.BlackListErrorGui;
import delta.guis.click.ClickGUI;
import delta.utils.Wrapper;
import me.xtrm.atlaspluginloader.core.event.Event;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.event.events.player.EventClick;
import me.xtrm.delta.api.event.events.player.EventJump;
import me.xtrm.delta.api.event.events.player.EventKeyboard;
import me.xtrm.delta.api.event.events.render.EventRender2D;
import me.xtrm.delta.api.event.events.render.EventRender3D;
import me.xtrm.delta.api.event.events.update.EventTick;
import me.xtrm.delta.api.event.events.update.EventUpdate;
import me.xtrm.delta.api.module.IModule;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiCommandBlock;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.gui.ForgeGuiFactory;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.lwjgl.input.Keyboard;

public class Class200 {
    @SubscribeEvent
    public void _helps(InputEvent.KeyInputEvent keyInputEvent) {
        if (!Keyboard.getEventKeyState()) {
            return;
        }
        EventKeyboard eventKeyboard = new EventKeyboard(Keyboard.getEventKey());
        eventKeyboard.call();
    }

    @SubscribeEvent
    public void _ranking(LivingEvent.LivingJumpEvent livingJumpEvent) {
        if (livingJumpEvent.entity instanceof EntityPlayerSP) {
            EventJump eventJump = new EventJump();
            eventJump.call();
        }
    }

    @SubscribeEvent
    public void _publish(LivingEvent.LivingUpdateEvent livingUpdateEvent) {
        if (livingUpdateEvent.entityLiving instanceof EntityPlayerSP) {
            EventUpdate eventUpdate = new EventUpdate();
            eventUpdate.call();
            if (!DeltaClient.instance._resort()._commonly()) {
                double d = livingUpdateEvent.entityLiving.field_70165_t;
                double d2 = livingUpdateEvent.entityLiving.field_70121_D.minY;
                double d3 = livingUpdateEvent.entityLiving.field_70161_v;
                float f = livingUpdateEvent.entityLiving.field_70177_z;
                float f2 = livingUpdateEvent.entityLiving.field_70125_A;
                boolean bl = livingUpdateEvent.entityLiving.field_70122_E;
                EventMotion eventMotion = new EventMotion(Event.State.PRE, d, d2, d3, f, f2, bl);
                eventMotion.call();
                if (eventMotion.isCancelled()) {
                    return;
                }
                int n = 101 - 151 + 85 + -35;
                int n2 = 103 - 205 + 21 + 81;
                if (d != eventMotion.getX() || d2 != eventMotion.getY() || d3 != eventMotion.getZ()) {
                    n = 196 - 307 + 27 - 23 + 108;
                }
                if (f != eventMotion.getYaw() || f2 != eventMotion.getYaw()) {
                    n2 = 210 - 266 + 89 - 54 + 22;
                }
                if (n != 0 && n2 != 0) {
                    ((EntityClientPlayerMP)livingUpdateEvent.entityLiving).sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C06PacketPlayerPosLook(eventMotion.getX(), eventMotion.getY(), eventMotion.getY() + (livingUpdateEvent.entity.posY - livingUpdateEvent.entity.boundingBox.minY), eventMotion.getZ(), eventMotion.getYaw(), eventMotion.getPitch(), eventMotion.isOnGround()));
                } else if (n != 0) {
                    ((EntityClientPlayerMP)livingUpdateEvent.entityLiving).sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(eventMotion.getX(), eventMotion.getY(), eventMotion.getY() + (livingUpdateEvent.entity.posY - livingUpdateEvent.entity.boundingBox.minY), eventMotion.getZ(), eventMotion.isOnGround()));
                } else if (n2 != 0) {
                    ((EntityClientPlayerMP)livingUpdateEvent.entityLiving).sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C05PacketPlayerLook(eventMotion.getYaw(), eventMotion.getPitch(), eventMotion.isOnGround()));
                } else if (bl != eventMotion.isOnGround()) {
                    ((EntityClientPlayerMP)livingUpdateEvent.entityLiving).sendQueue.addToSendQueue((Packet)new C03PacketPlayer(eventMotion.isOnGround()));
                }
                if (!eventMotion.isSilent()) {
                    livingUpdateEvent.entity.posX = eventMotion.getX();
                    livingUpdateEvent.entity.posY = eventMotion.getY();
                    livingUpdateEvent.entity.posZ = eventMotion.getZ();
                    livingUpdateEvent.entity.rotationYaw = eventMotion.getYaw();
                    livingUpdateEvent.entity.rotationPitch = eventMotion.getPitch();
                    livingUpdateEvent.entity.onGround = eventMotion.isOnGround();
                }
            }
        }
    }

    @SubscribeEvent
    public void _itself(RenderWorldLastEvent renderWorldLastEvent) {
        EventRender3D eventRender3D = new EventRender3D(renderWorldLastEvent.partialTicks);
        eventRender3D.call();
    }

    @SubscribeEvent(priority=EventPriority.HIGHEST)
    public void _known(GuiOpenEvent guiOpenEvent) {
        if (!(guiOpenEvent.gui == null || guiOpenEvent.gui instanceof GuiChat || guiOpenEvent.gui instanceof GuiIngameMenu || guiOpenEvent.gui instanceof GuiChest || guiOpenEvent.gui instanceof GuiInventory || guiOpenEvent.gui instanceof GuiContainerCreative || guiOpenEvent.gui instanceof GuiCommandBlock || guiOpenEvent.gui instanceof ClickGUI || guiOpenEvent.gui instanceof GuiEnchantment || guiOpenEvent.gui instanceof GuiRepair || guiOpenEvent.gui instanceof GuiInventory || guiOpenEvent.gui instanceof GuiCrafting || guiOpenEvent.gui instanceof GuiFurnace || guiOpenEvent.gui instanceof GuiBeacon || guiOpenEvent.gui instanceof GuiDispenser || guiOpenEvent.gui instanceof GuiHopper || guiOpenEvent.gui instanceof GuiBrewingStand || guiOpenEvent.gui instanceof GuiEditSign || guiOpenEvent.gui instanceof ForgeGuiFactory.ForgeConfigGui)) {
            DeltaClient.instance.managers.rpc._cooling("In menus", "Clicking buttons");
        }
        if (Class55._remove()) {
            return;
        }
        if (guiOpenEvent != null && guiOpenEvent.gui != null && guiOpenEvent.gui.getClass() != null && guiOpenEvent.gui.getClass().getName() != null && !guiOpenEvent.gui.getClass().getName().toLowerCase().contains("delta") && guiOpenEvent.gui.getClass().getName().toLowerCase().contains("main") && guiOpenEvent.gui.getClass().getName().toLowerCase().contains("menu")) {
            guiOpenEvent.gui = new BlackListErrorGui(new Class45());
        }
    }

    @SubscribeEvent
    public void _onion(RenderLivingEvent.Specials.Pre pre) {
        IModule iModule = DeltaClient.instance.managers.modulesManager.getModule("Nametags");
        if (iModule != null && iModule.isEnabled()) {
            pre.setCanceled(33 - 56 + 21 - 20 + 23);
        }
    }

    @SubscribeEvent
    public void _valves(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.player != Wrapper.mc.thePlayer) {
            return;
        }
        EventTick eventTick = new EventTick();
        eventTick.call();
    }

    @SubscribeEvent
    public void _bloggers(MouseEvent mouseEvent) {
        if (mouseEvent.button != 207 - 329 + 283 + -162) {
            EventClick eventClick = new EventClick(mouseEvent.button == 0 ? EventClick.MouseButton.LEFT : (mouseEvent.button == 135 - 231 + 154 + -56 ? EventClick.MouseButton.MIDDLE : EventClick.MouseButton.RIGHT), mouseEvent.buttonstate ? EventClick.ClickType.PRESS : EventClick.ClickType.RELEASE);
            eventClick.call();
        }
    }

    @SubscribeEvent
    public void _enclosed(RenderGameOverlayEvent renderGameOverlayEvent) {
        if (renderGameOverlayEvent.type == RenderGameOverlayEvent.ElementType.ALL) {
            EventRender2D eventRender2D = new EventRender2D(renderGameOverlayEvent.partialTicks);
            eventRender2D.call();
        }
    }
}

