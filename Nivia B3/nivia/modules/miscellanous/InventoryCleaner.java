package nivia.modules.miscellanous;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventTick;
import nivia.modules.Module;
import nivia.utils.Logger;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class InventoryCleaner extends Module {
	
    private static final Minecraft MC = Minecraft.getMinecraft();
	private static final Random RANDOM = new Random();

	private final Set<Integer> blacklistedItemIDs = new HashSet<>();

	private ItemStack[] bestArmorSet;
	private ItemStack bestSword;
    private ItemStack bestPickAxe;
    private ItemStack bestBow;

    public InventoryCleaner() {
        super("InventoryCleaner", 0, 0x005C00, Category.MISCELLANEOUS, "Automatically throws out unneeded.",
                new String[]{"invclean", "invcleaner", "clean", "cleaner", "cleaninv", "invc"}, true);

        Arrays.stream(new int[]{
                //Egg
                344,
                //Stick
                280,
                //String
                287,
                //Flint
                318,
                //Compass
                345,
                //Feather
                288,
                //Experience Bottle
                384,
                //Enchanting Table
                116,
                //Chest
                54,
                //Snowball
                332,
                //Anvil
                145
        }).forEach(this.blacklistedItemIDs::add);
    }

    @EventTarget
    private void onTick(EventTick event) {
    	 if (MC.thePlayer.ticksExisted % 2 == 0 && RANDOM.nextInt(2) == 0) {
    	     this.bestArmorSet = getBestArmorSet();
    	     this.bestSword = getBestItem(ItemSword.class, Comparator.comparingDouble(this::getSwordDamage));
    	     this.bestPickAxe = getBestItem(ItemPickaxe.class, Comparator.comparingDouble(this::getMiningSpeed));
    	     this.bestBow = getBestItem(ItemBow.class, Comparator.comparingDouble(this::getBowPower));

    	     Optional<Slot> blacklistedItem = ((List<Slot>)MC.thePlayer.inventoryContainer.inventorySlots)
                     .stream()
                     .filter(Slot::getHasStack)
                     .filter(slot -> Arrays.stream(MC.thePlayer.inventory.armorInventory).noneMatch(slot.getStack()::equals))
                     .filter(slot -> !slot.getStack().equals(MC.thePlayer.getHeldItem()))
                     .filter(slot -> isItemBlackListed(slot.getStack()))
                     .findFirst();
             if (blacklistedItem.isPresent()) {
                 this.dropItem(blacklistedItem.get().slotNumber);
             }else{
                 this.setState(false);
             }
         }
    }

    private void dropItem(int slotID) {
        MC.playerController.windowClick(0, slotID, 1, 4, MC.thePlayer);
    }

    //Objects.requireNonNull is just for debugging. It can't be null
    //Things to throw out
    private boolean isItemBlackListed(ItemStack itemStack) {
        Item item = itemStack.getItem();

        return blacklistedItemIDs.contains(Item.getIdFromItem(item)) ||
                item instanceof ItemBow && !this.bestBow.equals(itemStack) ||
                item instanceof ItemTool && !this.bestPickAxe.equals(itemStack) ||
                item instanceof ItemFishingRod || item instanceof ItemGlassBottle || item instanceof ItemBucket ||
                item instanceof ItemFood && !(item instanceof ItemAppleGold) ||
                item instanceof ItemSword && !this.bestSword.equals(itemStack) ||
                item instanceof ItemArmor && !this.bestArmorSet[((ItemArmor) item).armorType].equals(itemStack) ||
                item instanceof ItemPotion && isPotionNegative(itemStack);
    }

    //Improved check to reduce copy pasty code
    private ItemStack getBestItem(Class<? extends Item> itemType, Comparator comparator) {
        Optional<ItemStack> bestItem = ((List<Slot>)MC.thePlayer.inventoryContainer.inventorySlots)
                .stream()
                .map(Slot::getStack)
                .filter(Objects::nonNull)
                .filter(itemStack -> itemStack.getItem().getClass().equals(itemType))
                .max(comparator);

        return bestItem.orElse(null);
    }
    //Armor check
    private ItemStack[] getBestArmorSet() {
        ItemStack[] bestArmorSet = new ItemStack[4];

        List<ItemStack> armor = ((List<Slot>)MC.thePlayer.inventoryContainer.inventorySlots)
                .stream()
                .filter(Slot::getHasStack)
                .map(Slot::getStack)
                .filter(itemStack -> itemStack.getItem() instanceof ItemArmor)
                .collect(Collectors.toList());

        for (ItemStack itemStack : armor) {
            ItemArmor itemArmor = (ItemArmor) itemStack.getItem();

            ItemStack bestArmor = bestArmorSet[itemArmor.armorType];

            if (bestArmor == null || getArmorDamageReduction(itemStack) > getArmorDamageReduction(bestArmor)) {
                bestArmorSet[itemArmor.armorType] = itemStack;
            }
        }

        return bestArmorSet;
    }

    private double getSwordDamage(ItemStack itemStack) {
        double damage = 0;

        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

        if (attributeModifier.isPresent()) {
            damage = attributeModifier.get().getAmount();
        }

        damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

        return damage;
    }

    private double getBowPower(ItemStack itemStack) {
        double power = 0;

        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

        if (attributeModifier.isPresent()) {
            power = attributeModifier.get().getAmount();
        }

        power += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

        return power;
    }

    private double getMiningSpeed(ItemStack itemStack) {
        double speed = 0;

        Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

        if (attributeModifier.isPresent()) {
            speed = attributeModifier.get().getAmount();
        }

        speed += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

        return speed;
    }


    private double getArmorDamageReduction(ItemStack itemStack) {
        int damageReductionAmount = ((ItemArmor) itemStack.getItem()).damageReduceAmount;

        damageReductionAmount += EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack},
                DamageSource.causePlayerDamage(MC.thePlayer));

        return damageReductionAmount;
    }



    private boolean isPotionNegative(ItemStack itemStack) {
        ItemPotion potion = (ItemPotion) itemStack.getItem();

        List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

        return potionEffectList.stream()
                .map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
                .anyMatch(Potion::isBadEffect);
    }

    @Override
    protected void addCommand() {
        Pandora.getCommandManager().cmds.add(new Command("InventoryCleaner", "Manages Inventory Cleaner",
                Logger.LogExecutionFail("Option, Options:", new String[]{"Add", "Remove", "Clear"}), "invc", "inventcleaner", "inventoryclean", "invclean") {
            @Override
            public void execute(String commandName, String[] arguments) {
                String itemName;
                Item item;

                try {
                    itemName = arguments[2];
                    item = Item.getByNameOrId(itemName);
                }catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
                    e.printStackTrace();
                    Logger.logChat(this.getError());
                    return;
                }
                switch (arguments[1].toLowerCase()){
                    case "add":

                        blacklistedItemIDs.add(Item.getIdFromItem(item));

                        Logger.logChat(String.format("%s has been added.", getItemDisplayName(item)));

                        break;
                    case "remove":

                        blacklistedItemIDs.remove(Item.getIdFromItem(item));

                        Logger.logChat(String.format("%s has been removed.", getItemDisplayName(item)));
                        break;
                    case "clear":

                        blacklistedItemIDs.removeAll(blacklistedItemIDs);

                        Logger.logChat(String.format("List has been cleared. *Except defaults"));
                        break;

                    default:
                        Logger.logChat(this.getError());
                        break;
                }
            }
        });
    }

    public String getItemDisplayName(Item item)
    {
        return ("" + StatCollector.translateToLocal(StatCollector.translateToLocal(item.getUnlocalizedName()) + ".name")).trim();
    }
}