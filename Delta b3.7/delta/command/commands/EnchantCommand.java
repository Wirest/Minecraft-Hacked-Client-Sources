/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  cpw.mods.fml.common.network.ByteBufUtils
 *  cpw.mods.fml.common.network.internal.FMLProxyPacket
 *  io.netty.buffer.ByteBuf
 *  io.netty.buffer.Unpooled
 *  me.xtrm.delta.api.command.ICommand
 *  me.xtrm.delta.api.command.ICommandListener
 *  net.minecraft.enchantment.Enchantment
 *  net.minecraft.item.ItemStack
 */
package delta.command.commands;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import delta.Class159;
import delta.utils.PlayerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.xtrm.delta.api.command.ICommand;
import me.xtrm.delta.api.command.ICommandListener;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

public class EnchantCommand
implements ICommand {
    public String getName() {
        return "enchant";
    }

    public String[] getAliases() {
        return new String[266 - 528 + 253 - 215 + 224];
    }

    public String getDescription() {
        return "NIKTAMER";
    }

    public String getHelp() {
        return "enchant [<ID enchant> <lvl enchant>]";
    }

    public boolean execute(ICommandListener iCommandListener, String[] arrstring) {
        if (arrstring.length == 0) {
            int n = 41 - 62 + 48 + -27;
            Enchantment[] arrenchantment = Enchantment.enchantmentsList;
            int n2 = arrenchantment.length;
            for (int i = 214 - 326 + 282 + -170; i < n2; ++i) {
                Enchantment enchantment = arrenchantment[i];
                if (enchantment != null) {
                    PlayerUtils.addChatMessage("ench[" + n + "]: " + enchantment.getName());
                }
                ++n;
            }
            return 184 - 362 + 182 - 158 + 155;
        }
        if (arrstring.length == 60 - 72 + 51 + -37) {
            int n = Integer.parseInt(arrstring[36 - 68 + 9 - 6 + 29]);
            int n3 = Integer.parseInt(arrstring[227 - 341 + 114 + 1]);
            ByteBuf byteBuf = Unpooled.buffer();
            ItemStack itemStack = EnchantCommand.mc.thePlayer.func_70694_bm();
            itemStack.addEnchantment(Enchantment.enchantmentsList[n], n3);
            ByteBufUtils.writeItemStack((ByteBuf)byteBuf, (ItemStack)itemStack);
            try {
                Class159 class159 = Class159._imperial("fr.welsy.decodium.DecodiumMod");
                Object[] arrobject = new Object[167 - 329 + 141 - 20 + 42];
                arrobject[242 - 363 + 94 - 82 + 109] = new FMLProxyPacket(byteBuf, "DecodiumUpdateInv");
                class159._shipped("ch_Clipboard")._crawford("sendToServer", arrobject);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            return 120 - 208 + 195 + -106;
        }
        return 136 - 244 + 161 - 7 + -46;
    }
}

