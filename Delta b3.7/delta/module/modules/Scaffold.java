/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.relauncher.ReflectionHelper
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  me.xtrm.delta.api.setting.Setting
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.ItemBlock
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C09PacketHeldItemChange
 *  net.minecraft.network.play.client.C0APacketAnimation
 *  net.minecraft.network.play.client.C0BPacketEntityAction
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 */
package delta.module.modules;

import cpw.mods.fml.relauncher.ReflectionHelper;
import delta.Class38;
import delta.Class46;
import delta.Class8;
import delta.utils.BoundingBox;
import delta.utils.MovementUtils;
import delta.utils.TimeHelper;
import delta.utils.Wrapper;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.event.events.move.EventMotion;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import me.xtrm.delta.api.setting.Setting;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Scaffold
extends Module {
    private static List<String> soldiers$;
    private EnumFacing comments$;
    private boolean postings$ = 176 - 200 + 153 + -129;
    private BoundingBox fruits$;
    private TimeHelper glass$ = new TimeHelper();

    public Scaffold() {
        super("Scaffold", Category.World);
        this.setDescription("Bridge en posant des blocks en dessous du joueur");
        this.addSetting(new Setting("Expand", (IModule)this, 0.0, 0.0, 5.0, 138 - 217 + 92 + -13));
        this.addSetting(new Setting("Tower", (IModule)this, 199 - 261 + 140 + -77));
        this.addSetting(new Setting("Sneak", (IModule)this, 233 - 338 + 234 - 140 + 11));
        this.addSetting(new Setting("NoSwing", (IModule)this, 103 - 114 + 76 - 30 + -35));
        String[] arrstring = new String[24 - 32 + 18 + -3];
        arrstring[173 - 267 + 47 + 47] = Blocks.sand.func_149739_a();
        arrstring[188 - 321 + 288 - 148 + -6] = Blocks.enchanting_table.getUnlocalizedName();
        arrstring[70 - 126 + 88 + -30] = Blocks.rail.getUnlocalizedName();
        arrstring[54 - 106 + 17 + 38] = Blocks.gravel.getUnlocalizedName();
        arrstring[98 - 167 + 123 + -50] = Blocks.anvil.getUnlocalizedName();
        arrstring[121 - 130 + 103 + -89] = Blocks.carpet.getUnlocalizedName();
        arrstring[53 - 92 + 14 + 31] = Blocks.brewing_stand.getUnlocalizedName();
        soldiers$ = Arrays.asList(arrstring);
    }

    public int pv20(EnumFacing enumFacing) {
        switch (Class46.banner$[enumFacing.ordinal()]) {
            case 1: {
                return 260 - 468 + 442 + -234;
            }
            case 2: {
                return 214 - 300 + 171 - 39 + -45;
            }
            case 3: {
                return 125 - 132 + 83 - 18 + -56;
            }
            case 4: {
                return 215 - 361 + 81 - 71 + 139;
            }
            case 5: {
                return 151 - 281 + 5 - 4 + 133;
            }
            case 6: {
                return 157 - 287 + 119 + 16;
            }
        }
        return 181 - 210 + 119 - 33 + -58;
    }

    public void onEnable() {
        Wrapper.timer.timerSpeed = 1.0f;
        super.onEnable();
    }

    @EventTarget
    public void y1PZ(EventMotion eventMotion) {
        Random random = new Random();
        if (this.getSetting("Sneak").getCheckValue()) {
            String[] arrstring = new String[220 - 314 + 308 + -212];
            arrstring[125 - 176 + 23 + 28] = "pressed";
            arrstring[105 - 202 + 105 + -7] = "field_74513_e";
            ReflectionHelper.setPrivateValue(KeyBinding.class, (Object)this.mc.gameSettings.keyBindSneak, (Object)this.postings$, (String[])arrstring);
        }
        switch (Class46.bermuda$[eventMotion.getType().ordinal()]) {
            case 1: {
                this.fruits$ = null;
                this.comments$ = null;
                this.postings$ = 135 - 160 + 143 + -118;
                BoundingBox boundingBox = new BoundingBox(this.mc.thePlayer.field_70121_D.minX + (this.mc.thePlayer.field_70121_D.maxX - this.mc.thePlayer.field_70121_D.minX), this.mc.thePlayer.field_70121_D.minY - 0.5, this.mc.thePlayer.field_70121_D.minZ + (this.mc.thePlayer.field_70121_D.maxZ - this.mc.thePlayer.field_70121_D.minZ));
                if (this.mc.thePlayer.field_70122_E && !this.mc.gameSettings.keyBindJump.isPressed() && this.getSetting("Expand").getSliderValue() != 0.0) {
                    boundingBox = new BoundingBox(this.mc.thePlayer.field_70165_t + (double)(-MathHelper.sin((float)MovementUtils.getDirection())) * this.getSetting("Expand").getSliderValue(), this.mc.thePlayer.field_70163_u - 1.0, this.mc.thePlayer.field_70161_v + (double)MathHelper.cos((float)MovementUtils.getDirection()) * this.getSetting("Expand").getSliderValue());
                }
                System.out.println(boundingBox);
                if (!boundingBox._maria().getMaterial().isReplaceable()) break;
                this.Twl2(boundingBox);
                if (this.fruits$ == null) break;
                float[] arrf = Class8._demand((int)this.fruits$._talented(), (int)this.fruits$._adelaide(), (int)this.fruits$._produce(), this.comments$);
                float f = arrf[78 - 86 + 10 + -2];
                float f2 = Math.min(90.0f, arrf[43 - 59 + 25 + -8] + 9.0f);
                eventMotion.setYaw(f);
                eventMotion.setPitch(f2);
                this.postings$ = 223 - 385 + 210 - 20 + -27;
                break;
            }
            case 2: {
                if (this.fruits$ == null) break;
                int n = 154 - 272 + 20 + 97;
                int n2 = 75 - 110 + 103 - 17 + -52;
                for (int i = 110 - 204 + 114 - 35 + 15; i < 99 - 165 + 84 + -9; ++i) {
                    ItemStack itemStack;
                    ItemStack itemStack2 = itemStack = this.mc.thePlayer.field_71071_by.getStackInSlot(i) != null ? this.mc.thePlayer.field_71071_by.getStackInSlot(i) : null;
                    if (itemStack == null || Scaffold.dkz0(itemStack) || itemStack.getItem() == null || !(itemStack.getItem() instanceof ItemBlock)) continue;
                    n2 = i;
                }
                if (n2 == 231 - 382 + 141 + 9) {
                    return;
                }
                n = this.mc.thePlayer.field_71071_by.currentItem;
                this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(n2));
                this.mc.thePlayer.field_71071_by.currentItem = n2;
                Class38 class38 = new Class38(this.fruits$).KzcX(0.5f, 0.5f, 0.5f).kxe8(new Class38(this.comments$.getFrontOffsetX(), this.comments$.getFrontOffsetY(), this.comments$.getFrontOffsetZ()).4uJ7(0.5f));
                if (this.mc.playerController.onPlayerRightClick((EntityPlayer)this.mc.thePlayer, (World)this.mc.theWorld, this.mc.thePlayer.func_71045_bC(), (int)this.fruits$._talented(), (int)this.fruits$._adelaide(), (int)this.fruits$._produce(), this.pv20(this.comments$), Vec3.createVectorHelper((double)class38.x, (double)class38.y, (double)class38.z))) {
                    if (this.getSetting("NoSwing").getCheckValue()) {
                        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
                    } else {
                        this.mc.thePlayer.swingItem();
                    }
                }
                if (n != 160 - 251 + 41 + 49) {
                    this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C09PacketHeldItemChange(n));
                    this.mc.thePlayer.field_71071_by.currentItem = n;
                }
                if (!this.getSetting("Tower").getCheckValue() || !this.mc.gameSettings.keyBindJump.isPressed()) break;
                MovementUtils.setSpeed(0.0);
                if (new BoundingBox(this.mc.thePlayer.field_70165_t, this.mc.thePlayer.field_70163_u - 1.0, this.mc.thePlayer.field_70161_v)._maria().getMaterial().isReplaceable()) break;
                this.mc.thePlayer.field_70181_x = 0.42;
                if (!this.glass$.hasReached(193L - 303L + 234L - 130L + 1206L)) break;
                this.mc.thePlayer.field_70181_x = -0.28;
                this.glass$.setLastMS();
                break;
            }
        }
    }

    private void Twl2(BoundingBox boundingBox) {
        if (!boundingBox._exclude().offset(0.0, -1.0, 0.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(0.0, -1.0, 0.0);
            this.comments$ = EnumFacing.UP;
        } else if (!boundingBox._exclude().offset(-1.0, 0.0, 0.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(-1.0, 0.0, 0.0);
            this.comments$ = EnumFacing.EAST;
        } else if (!boundingBox._exclude().offset(1.0, 0.0, 0.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(1.0, 0.0, 0.0);
            this.comments$ = EnumFacing.WEST;
        } else if (!boundingBox._exclude().offset(0.0, 0.0, -1.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(0.0, 0.0, -1.0);
            this.comments$ = EnumFacing.SOUTH;
        } else if (!boundingBox._exclude().offset(0.0, 0.0, 1.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(0.0, 0.0, 1.0);
            this.comments$ = EnumFacing.NORTH;
        } else if (!boundingBox._exclude().offset(-1.0, 0.0, 1.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(-1.0, 0.0, 1.0);
            this.comments$ = EnumFacing.EAST;
        } else if (!boundingBox._exclude().offset(-1.0, 0.0, -1.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(-1.0, 0.0, -1.0);
            this.comments$ = EnumFacing.EAST;
        } else if (!boundingBox._exclude().offset(1.0, 0.0, -1.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(1.0, 0.0, -1.0);
            this.comments$ = EnumFacing.WEST;
        } else if (!boundingBox._exclude().offset(1.0, 0.0, 1.0)._maria().getMaterial().isReplaceable()) {
            this.fruits$ = boundingBox._exclude().offset(1.0, 0.0, 1.0);
            this.comments$ = EnumFacing.WEST;
        } else {
            this.fruits$ = null;
            this.comments$ = null;
        }
    }

    public void onDisable() {
        super.onDisable();
        this.mc.thePlayer.sendQueue.addToSendQueue((Packet)new C0BPacketEntityAction((Entity)this.mc.thePlayer, 117 - 128 + 109 + -93));
    }

    public static boolean dkz0(ItemStack itemStack) {
        return soldiers$.contains(itemStack.getUnlocalizedName());
    }
}

