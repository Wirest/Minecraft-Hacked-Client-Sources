/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.combat.aura;

import darkmagician6.EventManager;
import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketRecieve;
import darkmagician6.events.EventPacketSent;
import darkmagician6.events.EventPostMotionUpdates;
import darkmagician6.events.EventPreMotionUpdates;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.module.modules.combat.aura.AuraType;
import me.razerboy420.weepcraft.module.modules.combat.aura.types.MultiAura;
import me.razerboy420.weepcraft.module.modules.combat.aura.types.SingleAura;
import me.razerboy420.weepcraft.module.modules.combat.aura.types.SwitchAura;
import me.razerboy420.weepcraft.util.MathUtils;
import me.razerboy420.weepcraft.util.Timer;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.world.World;

@Module.Mod(category=Module.Category.COMBAT, description="Hits shit around you", key=0, name="Aura")
public class Aura
extends Module {
    public static Value mode = new Value("aura_Mode", "Switch", new String[]{"Switch", "Single", "Multi"});
    public static Value range = new Value("aura_Range", Float.valueOf(3.9f), Float.valueOf(1.0f), Float.valueOf(6.0f), Float.valueOf(0.1f));
    public static Value blockrange = new Value("aura_Block Range", Float.valueOf(8.0f), Float.valueOf(1.0f), Float.valueOf(10.0f), Float.valueOf(0.1f));
    public static Value delay = new Value("aura_APS", Float.valueOf(10.5f), Float.valueOf(1.0f), Float.valueOf(20.0f), Float.valueOf(0.1f));
    public static Value random = new Value("aura_APS Rand", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f));
    public static Value yrandom = new Value("aura_Yaw Rand", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f));
    public static Value prandom = new Value("aura_Pitch Rand", Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(5.0f), Float.valueOf(0.1f));
    public static Value existed = new Value("aura_Existed", Float.valueOf(30.0f), Float.valueOf(0.0f), Float.valueOf(200.0f), Float.valueOf(1.0f));
    public static Value player = new Value("aura_Player", true);
    public static Value mob = new Value("aura_Mob", false);
    public static Value botcheck = new Value("aura_BotCheck", false);
    public static Value autoaps = new Value("aura_Auto APS", true);
    public static Value teams = new Value("aura_Team Check", false);
    public static Value block = new Value("aura_Block", true);
    public static Value lockview = new Value("aura_Lockview", false);
    public static Value crits = new Value("aura_Criticals", true);
    public static Value invisible = new Value("aura_Invisible", true);
    public static Value wallcheck = new Value("aura_Walls", true);
    public static Value dura = new Value("aura_Dura", false);
    public static Value noswing = new Value("aura_NoSwing", false);
    public Timer timer = new Timer();

    @Override
    public void onEnable() {
        Weepcraft.getAura().getCurrent();
        AuraType.jew = null;
        EventManager.register(this);
    }

    @Override
    public void onDisable() {
        EventManager.unregister(this);
        Weepcraft.getAura().getCurrent().gasChamber.clear();
        Weepcraft.getAura().getCurrent();
        AuraType.jew = null;
        Weepcraft.getAura().getCurrent().tTicks = 0;
        Weepcraft.getAura().getCurrent().critTicks = 0;
        Weepcraft.getAura().getCurrent();
        AuraType.counter = 0;
    }

    @EventTarget
    public void onUpdate(EventPreMotionUpdates event) {
        if (Aura.mode.stringvalue.equalsIgnoreCase("Switch")) {
            this.setDisplayName("Aura [Switch] [" + Weepcraft.getAura().getCurrent().gasChamber.size() + "]");
            if (!(Weepcraft.getAura().getCurrent() instanceof SwitchAura)) {
                Weepcraft.getAura().setCurrent(new SwitchAura());
            }
        }
        if (Aura.mode.stringvalue.equalsIgnoreCase("Single")) {
            this.setDisplayName("Aura [Single]");
            if (!(Weepcraft.getAura().getCurrent() instanceof SingleAura)) {
                Weepcraft.getAura().setCurrent(new SingleAura());
            }
        }
        if (Aura.mode.stringvalue.equalsIgnoreCase("Multi")) {
            this.setDisplayName("Aura [Multi]");
            if (!(Weepcraft.getAura().getCurrent() instanceof MultiAura)) {
                Weepcraft.getAura().setCurrent(new MultiAura());
            }
        }
        Weepcraft.getAura().getCurrent().onUpdate(event);
    }

    @EventTarget
    public void onPostUpdate(EventPostMotionUpdates event) {
        Weepcraft.getAura().getCurrent().afterUpdate(event);
    }

    @EventTarget
    public void onPacketSent(EventPacketSent event) {
        CPacketUseEntity packetUseEntity1;
        if (!(event.getPacket() instanceof CPacketPlayer) && event.getPacket() instanceof CPacketPlayerDigging) {
            CPacketPlayerDigging cPacketPlayerDigging = (CPacketPlayerDigging)event.getPacket();
        }
        Weepcraft.getAura().getCurrent().onPacketOut(event);
        if (Aura.dura.boolvalue && event.getPacket() instanceof CPacketUseEntity && (packetUseEntity1 = (CPacketUseEntity)event.getPacket()).getAction() == CPacketUseEntity.Action.ATTACK) {
            EntityLivingBase ent = (EntityLivingBase)packetUseEntity1.getEntityFromWorld(Wrapper.mc().world);
            if (ent.hurtTime > 6) {
                return;
            }
            ItemStack current = Wrapper.getPlayer().getHeldItemMainhand();
            ItemStack toSwitch = Wrapper.getPlayer().inventoryContainer.getSlot(27).getStack();
            if (current != null && (current.getItem() instanceof ItemSword || current.getItem() instanceof ItemAxe || current.getItem() instanceof ItemPickaxe || current.getItem() instanceof ItemSpade)) {
                boolean var10000 = true;
            } else {
                boolean var10000 = false;
            }
            if (toSwitch != null) {
                Wrapper.mc().playerController.windowClick(0, 27, Wrapper.getPlayer().inventory.currentItem, ClickType.QUICK_MOVE, Wrapper.getPlayer());
            }
        }
    }

    @EventTarget
    public void onPacketGet(EventPacketRecieve event) {
        if (event.getPacket() instanceof SPacketEntityEquipment) {
            SPacketEntityEquipment p = (SPacketEntityEquipment)event.getPacket();
            if (MathUtils.isAuraBlocking() && p.getEquipmentSlot() == EntityEquipmentSlot.OFFHAND && Wrapper.getPlayer().getHeldItemOffhand() != null && Wrapper.getPlayer().getHeldItemOffhand().getItem() instanceof ItemShield) {
                event.setCancelled(true);
            }
        }
    }
}

