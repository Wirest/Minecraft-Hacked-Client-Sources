// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import me.aristhena.event.events.PacketSendEvent;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityMob;
import me.aristhena.client.friend.FriendManager;
import net.minecraft.entity.EntityLivingBase;
import me.aristhena.event.events.TickEvent;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.utils.StateManager;
import me.aristhena.event.events.JumpEvent;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.entity.item.EntityItem;
import me.aristhena.utils.ClientUtils;
import net.minecraft.network.play.server.S0DPacketCollectItem;
import me.aristhena.event.events.PacketReceiveEvent;
import me.aristhena.client.option.OptionManager;
import me.aristhena.utils.Timer;
import net.minecraft.item.ItemStack;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.modules.combat.aura.Juan;
import me.aristhena.client.module.modules.combat.aura.Dick22;
import me.aristhena.client.module.modules.combat.aura.Single;
import me.aristhena.client.module.modules.combat.aura.Vanilla;
import me.aristhena.client.module.modules.combat.aura.Switch;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;;


@Mod
public class Aura extends Module
{
    private Switch switchMode;
    private Vanilla vanilla;
    private Single single;
    private Dick22 tick;
    private Juan juan;
    @Option.Op(min = 0.0, max = 20.0, increment = 0.25)
    public double speed;
    @Option.Op(min = 0.1, max = 7.0, increment = 0.1)
    public double range;
    @Option.Op(name = "Block Range", min = 3.5, max = 12.0, increment = 0.5)
    public double blockRange;
    @Option.Op(min = 0.0, max = 180.0, increment = 5.0)
    public double degrees;
    @Option.Op(name = "Ticks Existed", min = 0.0, max = 25.0, increment = 1.0)
    public double ticksExisted;
    @Option.Op
    private boolean players;
    @Option.Op
    private boolean monsters;
    @Option.Op
    private boolean animals;
    @Option.Op
    private boolean bats;
    @Option.Op
    private boolean villagers;
    @Option.Op
    private boolean golems;
    @Option.Op
    private boolean booblebee;
    @Option.Op
    private boolean noswing;
    @Option.Op
    public boolean noslowdown;
    @Option.Op
    public boolean criticals;
    @Option.Op
    public boolean focus;
    @Option.Op
    public boolean experimental;
    @Option.Op(name = "Durability")
    public boolean dura;
    @Option.Op(name = "Clans")
    public boolean clans;
    @Option.Op(name = "Auto Block")
    public boolean autoblock;
    @Option.Op(name = "Armor Check")
    private boolean armorCheck;
    @Option.Op(name = "Attack Friends")
    private boolean attackFriends;
    private boolean jumpNextTick;
    private ItemStack predictedItem;
    private Timer pickupTimer;
    private Timer potTimer;
    
    public Aura() {
        this.switchMode = new Switch("Switch", true, this);
        this.vanilla = new Vanilla("Vanilla", false, this);
        this.single = new Single("Single", false, this);
        this.tick = new Dick22("Tick", false, this);
        this.juan = new Juan("Juan", false, this);
        this.speed = 8.0;
        this.range = 4.2;
        this.blockRange = 8.0;
        this.degrees = 90.0;
        this.ticksExisted = 10.0;
        this.players = true;
        this.booblebee = true;
        this.pickupTimer = new Timer();
        this.potTimer = new Timer();
    }
    
    @Override
    public void preInitialize() {
        OptionManager.getOptionList().add(this.switchMode);
        OptionManager.getOptionList().add(this.vanilla);
        OptionManager.getOptionList().add(this.single);
        OptionManager.getOptionList().add(this.tick);
        OptionManager.getOptionList().add(this.juan);
        this.updateSuffix();
        super.preInitialize();
    }
    
    @Override
    public void enable() {
        this.single.enable();
        this.tick.enable();
        this.juan.enable();
        super.enable();
    }
    
    private void onPacketReceive(final PacketReceiveEvent event) {
        if (event.getPacket() instanceof S0DPacketCollectItem) {
            final S0DPacketCollectItem packet = (S0DPacketCollectItem)event.getPacket();
            final Entity item = ClientUtils.world().getEntityByID(packet.func_149354_c());
            if (item instanceof EntityItem) {
                final EntityItem itemEntity = (EntityItem)item;
                this.predictedItem = itemEntity.getEntityItem();
                this.pickupTimer.reset();
            }
        }
        if (event.getPacket() instanceof S2FPacketSetSlot) {
            final S2FPacketSetSlot packet2 = (S2FPacketSetSlot)event.getPacket();
            if (!this.potTimer.delay(6.0f) && packet2.func_149173_d() == -1 && packet2.func_149175_c() == -1) {
                this.potTimer.setDifference(7L);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(6L);
                        }
                        catch (InterruptedException ex) {}
                        ClientUtils.sendMessage("Post - " + System.currentTimeMillis());
                        ClientUtils.packet(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                        ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, -999, 0, 5, ClientUtils.player());
                        ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, 36 + ClientUtils.player().inventory.currentItem, 1, 5, ClientUtils.player());
                        ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, -999, 2, 5, ClientUtils.player());
                        if (ClientUtils.player().inventory.getItemStack() != null) {
                            ClientUtils.sendMessage("Fake Placingxd");
                            ClientUtils.playerController().windowClick(ClientUtils.player().inventoryContainer.windowId, 36 + ClientUtils.player().inventory.currentItem, 0, 0, ClientUtils.player());
                        }
                    }
                }).start();
            }
            if (!this.pickupTimer.delay(10.0f) && this.predictedItem != null && ItemStack.areItemStackTagsEqual(this.predictedItem, packet2.func_149174_e()) && packet2.func_149173_d() == 36) {
                event.setCancelled(true);
                this.predictedItem = null;
            }
            else {
                this.predictedItem = null;
            }
        }
        if (event.getPacket() instanceof S30PacketWindowItems && this.potTimer.delay(400.0f)) {
            final S30PacketWindowItems packet3 = (S30PacketWindowItems)event.getPacket();
            ClientUtils.sendMessage("Pre - " + System.currentTimeMillis());
            this.potTimer.reset();
        }
        this.potTimer.delay(10.0f);
    }
    
    @EventTarget
    private void onJump(final JumpEvent event) {
        if (StateManager.offsetLastPacketAura()) {
            event.setCancelled(this.jumpNextTick = true);
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (this.jumpNextTick && !StateManager.offsetLastPacketAura()) {
            ClientUtils.player().jump();
            this.jumpNextTick = false;
        }
        this.vanilla.onUpdate(event);
        this.switchMode.onUpdate(event);
        this.single.onUpdate(event);
        this.tick.onUpdate(event);
        this.juan.onUpdate(event);
    }
    
    @EventTarget
    private void onTick(final TickEvent event) {
        this.updateSuffix();
    }
    
    private void updateSuffix() {
        if (this.switchMode.getValue()) {
            this.setSuffix("Switch");
        }
        else if (this.vanilla.getValue()) {
            this.setSuffix("Vanilla");
        }
        else if (this.tick.getValue()) {
            this.setSuffix("Tick");
        }
        else if (this.juan.getValue()) {
            this.setSuffix("Juan");
        }
        else {
            this.setSuffix("Single");
        }
    }
    
    public boolean isEntityValid(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLiving = (EntityLivingBase)entity;
            if (!ClientUtils.player().isEntityAlive() || !entityLiving.isEntityAlive() || entityLiving.getDistanceToEntity(ClientUtils.player()) > (ClientUtils.player().canEntityBeSeen(entityLiving) ? this.range : 3.0)) {
                return false;
            }
            if (entityLiving.ticksExisted < this.ticksExisted) {
                return false;
            }
            if (this.players && entityLiving instanceof EntityPlayer) {
                final EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
                if (FriendManager.isFriend(entityPlayer.getName()) && !this.attackFriends) {
                    return false;
                }
                if (this.armorCheck && !this.hasArmor(entityPlayer)) {
                    return false;
                }
                final ItemStack boots = entityPlayer.getEquipmentInSlot(1);
                final ItemStack legs = entityPlayer.getEquipmentInSlot(2);
                final ItemStack chest = entityPlayer.getEquipmentInSlot(3);
                final ItemStack helm = entityPlayer.getEquipmentInSlot(4);
                boolean fuckedUpArmorOrder = false;
                if (boots != null && boots.getUnlocalizedName().contains("helmet")) {
                    fuckedUpArmorOrder = true;
                }
                if (legs != null && legs.getUnlocalizedName().contains("chestplate")) {
                    fuckedUpArmorOrder = true;
                }
                if (chest != null && chest.getUnlocalizedName().contains("leggings")) {
                    fuckedUpArmorOrder = true;
                }
                if (helm != null && helm.getUnlocalizedName().contains("boots")) {
                    fuckedUpArmorOrder = true;
                }
                return !fuckedUpArmorOrder;
            }
            else {
                if (this.monsters && (entityLiving instanceof EntityMob || entityLiving instanceof EntityGhast || entityLiving instanceof EntityDragon || entityLiving instanceof EntityWither || entityLiving instanceof EntitySlime || (entityLiving instanceof EntityWolf && ((EntityWolf)entityLiving).getOwner() != ClientUtils.player()))) {
                    return true;
                }
                if (this.golems && entityLiving instanceof EntityGolem) {
                    return true;
                }
                if (this.animals && (entityLiving instanceof EntityAnimal || entityLiving instanceof EntitySquid)) {
                    return true;
                }
                if (this.bats && entityLiving instanceof EntityBat) {
                    return true;
                }
                if (this.villagers && entityLiving instanceof EntityVillager) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @EventTarget
    private void onPacketSend(final PacketSendEvent event) {
        if (event.getPacket() instanceof C09PacketHeldItemChange) {
            Switch.potTimer.reset();
        }
    }
    
    public boolean isEntityValidType(final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            final EntityLivingBase entityLiving = (EntityLivingBase)entity;
            if (!ClientUtils.player().isEntityAlive() || !entityLiving.isEntityAlive()) {
                return false;
            }
            if (this.players && entityLiving instanceof EntityPlayer) {
                final EntityPlayer entityPlayer = (EntityPlayer)entityLiving;
                if (entityPlayer.isInvisible() && !this.booblebee) {
                    return false;
                }
                if (FriendManager.isFriend(entityPlayer.getName()) && !this.attackFriends) {
                    return false;
                }
                if (this.armorCheck && !this.hasArmor(entityPlayer)) {
                    return false;
                }
                final ItemStack boots = entityPlayer.getEquipmentInSlot(1);
                final ItemStack legs = entityPlayer.getEquipmentInSlot(2);
                final ItemStack chest = entityPlayer.getEquipmentInSlot(3);
                final ItemStack helm = entityPlayer.getEquipmentInSlot(4);
                boolean fuckedUpArmorOrder = false;
                if (boots != null && boots.getUnlocalizedName().contains("helmet")) {
                    fuckedUpArmorOrder = true;
                }
                if (legs != null && legs.getUnlocalizedName().contains("chestplate")) {
                    fuckedUpArmorOrder = true;
                }
                if (chest != null && chest.getUnlocalizedName().contains("leggings")) {
                    fuckedUpArmorOrder = true;
                }
                if (helm != null && helm.getUnlocalizedName().contains("boots")) {
                    fuckedUpArmorOrder = true;
                }
                return !fuckedUpArmorOrder;
            }
            else {
                if (this.monsters && (entityLiving instanceof EntityMob || entityLiving instanceof EntityGhast || entityLiving instanceof EntityDragon || entityLiving instanceof EntityWither || entityLiving instanceof EntitySlime || (entityLiving instanceof EntityWolf && ((EntityWolf)entityLiving).getOwner() != ClientUtils.player()))) {
                    return true;
                }
                if (this.golems && entityLiving instanceof EntityGolem) {
                    return true;
                }
                if (this.animals && (entityLiving instanceof EntityAnimal || entityLiving instanceof EntitySquid)) {
                    return true;
                }
                if (this.bats && entityLiving instanceof EntityBat) {
                    return true;
                }
                if (this.villagers && entityLiving instanceof EntityVillager) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean hasArmor(final EntityPlayer player) {
        final ItemStack boots = player.inventory.armorInventory[0];
        final ItemStack pants = player.inventory.armorInventory[1];
        final ItemStack chest = player.inventory.armorInventory[2];
        final ItemStack head = player.inventory.armorInventory[3];
        return boots != null || pants != null || chest != null || head != null;
    }
    
    public void attack(final EntityLivingBase entity) {
        this.attack(entity, this.criticals);
    }
    
    public void attack(final EntityLivingBase entity, final boolean crit) {
        this.swingItem();
        final float sharpLevel = EnchantmentHelper.func_152377_a(ClientUtils.player().getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = ClientUtils.player().fallDistance > 0.0f && !ClientUtils.player().onGround && !ClientUtils.player().isOnLadder() && !ClientUtils.player().isInWater() && !ClientUtils.player().isPotionActive(Potion.blindness) && ClientUtils.player().ridingEntity == null;
        ClientUtils.packet(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            ClientUtils.player().onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            ClientUtils.player().onEnchantmentCritical(entity);
        }
    }
    
    public void pseudoAttack(final EntityLivingBase entity, final boolean crit) {
        this.swingItem();
        final float sharpLevel = EnchantmentHelper.func_152377_a(ClientUtils.player().getHeldItem(), entity.getCreatureAttribute());
        final boolean vanillaCrit = ClientUtils.player().fallDistance > 0.0f && !ClientUtils.player().onGround && !ClientUtils.player().isOnLadder() && !ClientUtils.player().isInWater() && !ClientUtils.player().isPotionActive(Potion.blindness) && ClientUtils.player().ridingEntity == null;
        if (crit || vanillaCrit) {
            ClientUtils.player().onCriticalHit(entity);
        }
        if (sharpLevel > 0.0f) {
            ClientUtils.player().onEnchantmentCritical(entity);
        }
    }
    
    public void swingItem() {
        if (!this.noswing) {
            ClientUtils.player().swingItem();
        }
    }
    
    @Override
    public void disable() {
        StateManager.setOffsetLastPacketAura(false);
        this.tick.disable();
        super.disable();
    }
}
