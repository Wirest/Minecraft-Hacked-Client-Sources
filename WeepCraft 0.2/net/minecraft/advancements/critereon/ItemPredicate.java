package net.minecraft.advancements.critereon;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

public class ItemPredicate
{
    public static final ItemPredicate field_192495_a = new ItemPredicate();
    private final Item field_192496_b;
    private final Integer field_192497_c;
    private final MinMaxBounds field_192498_d;
    private final MinMaxBounds field_193444_e;
    private final EnchantmentPredicate[] field_192499_e;
    private final PotionType field_192500_f;
    private final NBTPredicate field_193445_h;

    public ItemPredicate()
    {
        this.field_192496_b = null;
        this.field_192497_c = null;
        this.field_192500_f = null;
        this.field_192498_d = MinMaxBounds.field_192516_a;
        this.field_193444_e = MinMaxBounds.field_192516_a;
        this.field_192499_e = new EnchantmentPredicate[0];
        this.field_193445_h = NBTPredicate.field_193479_a;
    }

    public ItemPredicate(@Nullable Item p_i47540_1_, @Nullable Integer p_i47540_2_, MinMaxBounds p_i47540_3_, MinMaxBounds p_i47540_4_, EnchantmentPredicate[] p_i47540_5_, @Nullable PotionType p_i47540_6_, NBTPredicate p_i47540_7_)
    {
        this.field_192496_b = p_i47540_1_;
        this.field_192497_c = p_i47540_2_;
        this.field_192498_d = p_i47540_3_;
        this.field_193444_e = p_i47540_4_;
        this.field_192499_e = p_i47540_5_;
        this.field_192500_f = p_i47540_6_;
        this.field_193445_h = p_i47540_7_;
    }

    public boolean func_192493_a(ItemStack p_192493_1_)
    {
        if (this.field_192496_b != null && p_192493_1_.getItem() != this.field_192496_b)
        {
            return false;
        }
        else if (this.field_192497_c != null && p_192493_1_.getMetadata() != this.field_192497_c.intValue())
        {
            return false;
        }
        else if (!this.field_192498_d.func_192514_a((float)p_192493_1_.func_190916_E()))
        {
            return false;
        }
        else if (this.field_193444_e != MinMaxBounds.field_192516_a && !p_192493_1_.isItemStackDamageable())
        {
            return false;
        }
        else if (!this.field_193444_e.func_192514_a((float)(p_192493_1_.getMaxDamage() - p_192493_1_.getItemDamage())))
        {
            return false;
        }
        else if (!this.field_193445_h.func_193478_a(p_192493_1_))
        {
            return false;
        }
        else
        {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(p_192493_1_);

            for (int i = 0; i < this.field_192499_e.length; ++i)
            {
                if (!this.field_192499_e[i].func_192463_a(map))
                {
                    return false;
                }
            }

            PotionType potiontype = PotionUtils.getPotionFromItem(p_192493_1_);

            if (this.field_192500_f != null && this.field_192500_f != potiontype)
            {
                return false;
            }
            else
            {
                return true;
            }
        }
    }

    public static ItemPredicate func_192492_a(@Nullable JsonElement p_192492_0_)
    {
        if (p_192492_0_ != null && !p_192492_0_.isJsonNull())
        {
            JsonObject jsonobject = JsonUtils.getJsonObject(p_192492_0_, "item");
            MinMaxBounds minmaxbounds = MinMaxBounds.func_192515_a(jsonobject.get("count"));
            MinMaxBounds minmaxbounds1 = MinMaxBounds.func_192515_a(jsonobject.get("durability"));
            Integer integer = jsonobject.has("data") ? JsonUtils.getInt(jsonobject, "data") : null;
            NBTPredicate nbtpredicate = NBTPredicate.func_193476_a(jsonobject.get("nbt"));
            Item item = null;

            if (jsonobject.has("item"))
            {
                ResourceLocation resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "item"));
                item = Item.REGISTRY.getObject(resourcelocation);

                if (item == null)
                {
                    throw new JsonSyntaxException("Unknown item id '" + resourcelocation + "'");
                }
            }

            EnchantmentPredicate[] aenchantmentpredicate = EnchantmentPredicate.func_192465_b(jsonobject.get("enchantments"));
            PotionType potiontype = null;

            if (jsonobject.has("potion"))
            {
                ResourceLocation resourcelocation1 = new ResourceLocation(JsonUtils.getString(jsonobject, "potion"));

                if (!PotionType.REGISTRY.containsKey(resourcelocation1))
                {
                    throw new JsonSyntaxException("Unknown potion '" + resourcelocation1 + "'");
                }

                potiontype = PotionType.REGISTRY.getObject(resourcelocation1);
            }

            return new ItemPredicate(item, integer, minmaxbounds, minmaxbounds1, aenchantmentpredicate, potiontype, nbtpredicate);
        }
        else
        {
            return field_192495_a;
        }
    }

    public static ItemPredicate[] func_192494_b(@Nullable JsonElement p_192494_0_)
    {
        if (p_192494_0_ != null && !p_192494_0_.isJsonNull())
        {
            JsonArray jsonarray = JsonUtils.getJsonArray(p_192494_0_, "items");
            ItemPredicate[] aitempredicate = new ItemPredicate[jsonarray.size()];

            for (int i = 0; i < aitempredicate.length; ++i)
            {
                aitempredicate[i] = func_192492_a(jsonarray.get(i));
            }

            return aitempredicate;
        }
        else
        {
            return new ItemPredicate[0];
        }
    }
}
