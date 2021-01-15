/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.network.NetHandlerPlayClient
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.entity.monster.EntityMob
 *  net.minecraft.entity.passive.EntityAnimal
 *  net.minecraft.entity.passive.EntityBat
 *  net.minecraft.entity.passive.EntitySquid
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.InventoryPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C03PacketPlayer
 *  net.minecraft.network.play.client.C03PacketPlayer$C04PacketPlayerPosition
 *  net.minecraft.network.play.client.C07PacketPlayerDigging
 *  net.minecraft.network.play.client.C07PacketPlayerDigging$Action
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.network.play.client.C0BPacketEntityAction$Action
 *  net.minecraft.potion.Potion
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 */
package me.aristhena.lucid.modules.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.TickEvent;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.friend.FriendManager;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.modules.movement.Speed;
import me.aristhena.lucid.modules.movement.Sprint;
import me.aristhena.lucid.modules.render.HUD;
import me.aristhena.lucid.util.RotationUtils;
import me.aristhena.lucid.util.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

@Mod
public class Aura extends Module
{
    @Op
    private boolean players;
    @Op
    private boolean monsters;
    @Op
    private boolean animals;
    @Op
    private boolean bats;
    @Op
    private boolean friend;
    @Op
    private boolean knockback;
    @Op
    private boolean noArmor;
    @Op
    private boolean criticals;
    @Op
    private boolean autoBlock;
    @Op
    private boolean noSwing;
    @Op
    private boolean dura;
    @Op
    private boolean angle;
    @Op
    private boolean lockview;
    @Val(min = 0.0, max = 10.0, increment = 0.5)
    private double speed;
    @Val(min = 0.0, max = 6.0, increment = 0.25)
    private double range;
    @Val(min = 0.0, max = 6.0, increment = 1.0)
    private double blockRange;
    private Timer pseudoTimer;
    private Timer angleTimer;
    private static EntityLivingBase target;
    public static EntityLivingBase pseudoTarget;
    
    public Aura() {
        this.players = true;
        this.criticals = true;
        this.autoBlock = true;
        this.angle = true;
        this.speed = 8.0;
        this.range = 4.25;
        this.blockRange = 8.0;
        this.pseudoTimer = new Timer();
        this.angleTimer = new Timer();
    }
    
    @EventTarget
    private void onTick(final TickEvent event) {
        final Character colorFormatCharacter = new Character('§');
        if (OptionManager.getOption("Hyphen", ModuleManager.getModule(HUD.class)).value) {
            this.suffix = colorFormatCharacter + "7 - " + "Tick";
        }
        else {
            this.suffix = colorFormatCharacter + "7 " + "Tick";
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        switch (event.state) {
            case PRE: {
                event.ground = true;
                Aura.target = null;
                final List<EntityLivingBase> attackableEntities = new ArrayList<EntityLivingBase>();
                for (final Object o : this.mc.theWorld.loadedEntityList) {
                    if (o instanceof EntityLivingBase) {
                        final EntityLivingBase entityLivingBase;
                        final EntityLivingBase entity = entityLivingBase = (EntityLivingBase)o;
                        --entityLivingBase.auraTicks;
                        if (!this.checkValidity(entity) || ((entity.auraTicks != 10 || this.dura) && entity.auraTicks != 9 && entity.auraTicks > 0)) {
                            continue;
                        }
                        attackableEntities.add(entity);
                    }
                }
                Collections.sort(attackableEntities, new Comparator<EntityLivingBase>() {
                    @Override
                    public int compare(final EntityLivingBase o1, final EntityLivingBase o2) {
                        return o1.auraTicks - o2.auraTicks;
                    }
                });
                for (final EntityLivingBase entity2 : attackableEntities) {
                    if ((Aura.pseudoTarget != null && Aura.pseudoTarget == entity2) || this.angleTimer.delay(150.0f)) {
                        if (Aura.pseudoTarget == null || Aura.pseudoTarget != entity2) {
                            this.angleTimer.reset();
                        }
                        Aura.pseudoTarget = (Aura.target = entity2);
                        break;
                    }
                }
                if (Aura.pseudoTarget != null && !this.checkValidity(Aura.pseudoTarget)) {
                    Aura.pseudoTarget = null;
                }
                if (Aura.pseudoTarget == null) {
                    break;
                }
                final float[] rotations = RotationUtils.getRotations(Aura.pseudoTarget);
                event.yaw = rotations[0];
                event.pitch = rotations[1];
                if (this.lockview) {
                    this.mc.thePlayer.rotationYaw = rotations[0];
                    this.mc.thePlayer.rotationPitch = rotations[1];
                    break;
                }
                break;
            }
            case POST: {
                if (Aura.target != null) {
                    final boolean fakeSprint = ModuleManager.getModule(Sprint.class).enabled && OptionManager.getOption("fake", ModuleManager.getModule(Sprint.class)).value;
                    if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                        this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    if (!fakeSprint) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
                    }
                    if (this.angle) {
                        if (this.dura) {
                            if (Aura.target.auraTicks != 10) {
                                this.swap(9, this.mc.thePlayer.inventory.currentItem);
                                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                                this.attack(Aura.target, false);
                                this.attack(Aura.target, false);
                                this.attack(Aura.target, true);
                                this.swap(9, this.mc.thePlayer.inventory.currentItem);
                                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                                this.attack(Aura.target, false);
                                this.attack(Aura.target, true);
                            }
                        }
                        else {
                            this.attack(Aura.target, this.criticals);
                            this.attack(Aura.target, this.criticals);
                        }
                    }
                    else if (this.dura) {
                        this.attack(Aura.target, false);
                        if (Aura.target.auraTicks != 10) {
                            this.attack(Aura.target, this.criticals);
                        }
                    }
                    else {
                        this.attack(Aura.target, this.criticals);
                    }
                    if (Aura.target.auraTicks != 10) {
                        Aura.target.auraTicks = 20;
                    }
                    final boolean sprint = this.mc.thePlayer.isSprinting();
                    if (sprint && !fakeSprint) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(this.mc.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
                    }
                }
                else if (Aura.pseudoTarget != null && this.pseudoTimer.delay((float)(1000.0 / this.speed))) {
                    this.fakeAttack(Aura.pseudoTarget);
                    this.pseudoTimer.reset();
                }
                final double oldRange = this.range;
                this.range = this.blockRange;
                int enemiesArmound = 0;
                for (final Object o2 : this.mc.theWorld.loadedEntityList) {
                    if (o2 instanceof EntityLivingBase) {
                        final EntityLivingBase entity3 = (EntityLivingBase)o2;
                        if (!this.checkValidity(entity3)) {
                            continue;
                        }
                        ++enemiesArmound;
                    }
                }
                this.range = oldRange;
                if (enemiesArmound > 0 && this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && (this.mc.gameSettings.keyBindUseItem.pressed || this.autoBlock)) {
                    this.mc.thePlayer.setItemInUse(this.mc.thePlayer.getHeldItem(), this.mc.thePlayer.getHeldItem().getMaxItemUseDuration());
                    break;
                }
                break;
            }
        }
    }
    
    protected void swap(final int slot, final int hotbarNum) {
        this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, this.mc.thePlayer);
    }
    
    private void fakeAttack(final EntityLivingBase ent) {
        this.fakeSwingItem();
        final float sharpLevel = EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), ent.getCreatureAttribute());
        final boolean vanillaCrit = this.mc.thePlayer.fallDistance > 0.0f && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null;
        if (this.criticals || vanillaCrit) {
            this.mc.thePlayer.onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            this.mc.thePlayer.onEnchantmentCritical(ent);
        }
        this.pseudoTimer.reset();
    }
    
    private void attack(final EntityLivingBase ent, final boolean crit) {
        this.swingItem();
        if (crit) {
            this.crit();
        }
        else {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
        }
        final float sharpLevel = EnchantmentHelper.func_152377_a(this.mc.thePlayer.getHeldItem(), ent.getCreatureAttribute());
        final boolean vanillaCrit = this.mc.thePlayer.fallDistance > 0.0f && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null;
        this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(ent, C02PacketUseEntity.Action.ATTACK));
        if (crit || vanillaCrit) {
            this.mc.thePlayer.onCriticalHit(ent);
        }
        if (sharpLevel > 0.0f) {
            this.mc.thePlayer.onEnchantmentCritical(ent);
        }
    }
    
    private void fakeSwingItem() {
        if (!this.noSwing) {
            this.mc.thePlayer.fakeSwingItem();
        }
    }
    
    private void swingItem() {
        if (this.noSwing) {
            this.mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
        }
        else {
            this.mc.thePlayer.swingItem();
        }
    }
    
    private void crit() {
        final double posY = this.mc.thePlayer.posY + Speed.yOffset;
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, posY + 0.0625, this.mc.thePlayer.posZ, true));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, posY, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, posY + 1.1E-5, this.mc.thePlayer.posZ, false));
        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, posY, this.mc.thePlayer.posZ, false));
    }
    
    private boolean checkValidity(final EntityLivingBase entity) {
        if (entity == this.mc.thePlayer) {
            return false;
        }
        if (!entity.isEntityAlive()) {
            return false;
        }
        if (this.mc.thePlayer.getDistanceToEntity(entity) > this.range) {
            return false;
        }
        if (!(entity instanceof EntityPlayer)) {
            return (this.monsters && entity instanceof EntityMob) || (this.animals && (entity instanceof EntityAnimal || entity instanceof EntitySquid)) || (this.bats && entity instanceof EntityBat);
        }
        if (this.players) {
            final EntityPlayer player = (EntityPlayer)entity;
            return (this.friend && FriendManager.isFriend(player.getCommandSenderName())) || (!FriendManager.isFriend(player.getCommandSenderName()) && (!this.noArmor || this.hasArmor(player)));
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
}


