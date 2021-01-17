package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayer extends Container {

   public InventoryCrafting field_75181_e = new InventoryCrafting(this, 2, 2);
   public IInventory field_75179_f = new InventoryCraftResult();
   public boolean field_75180_g;
   private final EntityPlayer field_82862_h;
   private static final String __OBFID = "CL_00001754";


   public ContainerPlayer(final InventoryPlayer p_i1819_1_, boolean p_i1819_2_, EntityPlayer p_i1819_3_) {
      this.field_75180_g = p_i1819_2_;
      this.field_82862_h = p_i1819_3_;
      this.func_75146_a(new SlotCrafting(p_i1819_1_.field_70458_d, this.field_75181_e, this.field_75179_f, 0, 144, 36));

      final int var4;
      int var5;
      for(var4 = 0; var4 < 2; ++var4) {
         for(var5 = 0; var5 < 2; ++var5) {
            this.func_75146_a(new Slot(this.field_75181_e, var5 + var4 * 2, 88 + var5 * 18, 26 + var4 * 18));
         }
      }

      for(var4 = 0; var4 < 4; ++var4) {
         this.func_75146_a(new Slot(p_i1819_1_, p_i1819_1_.func_70302_i_() - 1 - var4, 8, 8 + var4 * 18) {

            private static final String __OBFID = "CL_00001755";

            public int func_75219_a() {
               return 1;
            }
            public boolean func_75214_a(ItemStack p_75214_1_) {
               return p_75214_1_ == null?false:(p_75214_1_.func_77973_b() instanceof ItemArmor?((ItemArmor)p_75214_1_.func_77973_b()).field_77881_a == var4:(p_75214_1_.func_77973_b() != Item.func_150898_a(Blocks.field_150423_aK) && p_75214_1_.func_77973_b() != Items.field_151144_bL?false:var4 == 0));
            }
            public String func_178171_c() {
               return ItemArmor.field_94603_a[var4];
            }
         });
      }

      for(var4 = 0; var4 < 3; ++var4) {
         for(var5 = 0; var5 < 9; ++var5) {
            this.func_75146_a(new Slot(p_i1819_1_, var5 + (var4 + 1) * 9, 8 + var5 * 18, 84 + var4 * 18));
         }
      }

      for(var4 = 0; var4 < 9; ++var4) {
         this.func_75146_a(new Slot(p_i1819_1_, var4, 8 + var4 * 18, 142));
      }

      this.func_75130_a(this.field_75181_e);
   }

   public void func_75130_a(IInventory p_75130_1_) {
      this.field_75179_f.func_70299_a(0, CraftingManager.func_77594_a().func_82787_a(this.field_75181_e, this.field_82862_h.field_70170_p));
   }

   public void func_75134_a(EntityPlayer p_75134_1_) {
      super.func_75134_a(p_75134_1_);

      for(int var2 = 0; var2 < 4; ++var2) {
         ItemStack var3 = this.field_75181_e.func_70304_b(var2);
         if(var3 != null) {
            p_75134_1_.func_71019_a(var3, false);
         }
      }

      this.field_75179_f.func_70299_a(0, (ItemStack)null);
   }

   public boolean func_75145_c(EntityPlayer p_75145_1_) {
      return true;
   }

   public ItemStack func_82846_b(EntityPlayer p_82846_1_, int p_82846_2_) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.field_75151_b.get(p_82846_2_);
      if(var4 != null && var4.func_75216_d()) {
         ItemStack var5 = var4.func_75211_c();
         var3 = var5.func_77946_l();
         if(p_82846_2_ == 0) {
            if(!this.func_75135_a(var5, 9, 45, true)) {
               return null;
            }

            var4.func_75220_a(var5, var3);
         } else if(p_82846_2_ >= 1 && p_82846_2_ < 5) {
            if(!this.func_75135_a(var5, 9, 45, false)) {
               return null;
            }
         } else if(p_82846_2_ >= 5 && p_82846_2_ < 9) {
            if(!this.func_75135_a(var5, 9, 45, false)) {
               return null;
            }
         } else if(var3.func_77973_b() instanceof ItemArmor && !((Slot)this.field_75151_b.get(5 + ((ItemArmor)var3.func_77973_b()).field_77881_a)).func_75216_d()) {
            int var6 = 5 + ((ItemArmor)var3.func_77973_b()).field_77881_a;
            if(!this.func_75135_a(var5, var6, var6 + 1, false)) {
               return null;
            }
         } else if(p_82846_2_ >= 9 && p_82846_2_ < 36) {
            if(!this.func_75135_a(var5, 36, 45, false)) {
               return null;
            }
         } else if(p_82846_2_ >= 36 && p_82846_2_ < 45) {
            if(!this.func_75135_a(var5, 9, 36, false)) {
               return null;
            }
         } else if(!this.func_75135_a(var5, 9, 45, false)) {
            return null;
         }

         if(var5.field_77994_a == 0) {
            var4.func_75215_d((ItemStack)null);
         } else {
            var4.func_75218_e();
         }

         if(var5.field_77994_a == var3.field_77994_a) {
            return null;
         }

         var4.func_82870_a(p_82846_1_, var5);
      }

      return var3;
   }

   public boolean func_94530_a(ItemStack p_94530_1_, Slot p_94530_2_) {
      return p_94530_2_.field_75224_c != this.field_75179_f && super.func_94530_a(p_94530_1_, p_94530_2_);
   }
}
