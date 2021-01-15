package VenusClient.online.Module.impl.World;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import VenusClient.online.Utils.BlockUtil;
import VenusClient.online.Utils.ClickType;
import VenusClient.online.Utils.RayCastUtil;
import VenusClient.online.Utils.TimeHelper;
import de.Hero.settings.Setting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;

public class ChestStealer extends Module {

    public ChestStealer() {
        super("ChestStealer", "ChestStealer", Keyboard.KEY_K, Category.WORLD);
    }

    @Override
    public void setup() {
        Client.instance.setmgr.rSetting(new Setting("ChestStealer Delay", this, 100, 1, 2000, false));
    }

    private TimeHelper timer = new TimeHelper();
    private TimeHelper stealTimer = new TimeHelper();
    private boolean isStealing;
    private float[] rotations = new float[2];
    private boolean rotated;
 
    @Override
    protected void onEnable() {
        super.onEnable();
        shouldChest = false;
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        rotations[0] = 0;
        rotations[1] = 0;
        stealTimer.setLastMS();
        shouldChest = false;
    }

    @EventTarget
    public void onUpdate(EventMotionUpdate event) {
    	if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
      		return;
    	}
        for (Object o : mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                TileEntityChest chest = (TileEntityChest) o;
                float x = chest.getPos().getX();
                float y = chest.getPos().getY();
                float z = chest.getPos().getZ();
                boolean chestaura = Math.abs(mc.thePlayer.rotationYawHead - mc.thePlayer.rotationYaw) < 90;

                if (mc.thePlayer.getDistance(x, y, z) <= 4.5) {
                    shouldChest = true;
                } else {
                    shouldChest = false;
                }
                if (mc.thePlayer.getDistance(x, y, z) <= 4.5) {

                    rotations = BlockUtil.getRotations(chest.getPos(), getFacingDirection(chest.getPos()));


                    rotated = rayTrace(rotations[0], rotations[1], chest.getPos());

                }
                if (stealTimer.hasReached(150) && isStealing) {
                    stealTimer.setLastMS();
                    isStealing = false;
                }
            } else {
                if (mc.currentScreen instanceof GuiChest) {
                    GuiChest guiChest = (GuiChest) mc.currentScreen;
                    String name = guiChest.lowerChestInventory.getDisplayName().getUnformattedText().toLowerCase();
                    String[] list = new String[]{"menu", "selector", "game", "gui", "server", "inventory", "play", "teleporter", "shop", "melee", "armor",
                            "block", "castle", "mini", "warp", "teleport", "user", "team", "tool", "sure", "trade", "cancel", "accept", "soul", "book", "recipe",
                            "profile", "tele", "port", "map", "kit", "select", "lobby", "vault", "lock", "anticheat", "travel", "settings", "user", "preference",
                            "compass", "cake", "wars", "buy", "upgrade", "ranged", "potions", "utility"};
                    String[] chestname = new String[]{"chest"};


                    for (String str : list) {
                        if (name.contains(str))
                            return;

                    }
                    isStealing = true;
                    boolean full = true;
                    int j = mc.thePlayer.inventory.mainInventory.length;
                    for (int i = 0; i < j; i++) {
                        ItemStack item = mc.thePlayer.inventory.mainInventory[i];
                        if (!mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                            full = false;
                            break;
                        }
                    }
                    boolean containsItems = false;
                    if (!full) {
                        for (int index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); index++) {
                            ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(index);

                            if (guiChest.lowerChestInventory.getStackInSlot(index) != null && !isBad(stack)) {
                                containsItems = true;
                                break;
                            }
                        }
                        if (containsItems) {
                            for (int index = 0; index < guiChest.lowerChestInventory.getSizeInventory(); index++) {
                                ItemStack stack = guiChest.lowerChestInventory.getStackInSlot(index);
                                int delay = (int) Client.instance.setmgr.getSettingByName("ChestStealer Delay").getValDouble();

                                if (guiChest.lowerChestInventory.getStackInSlot(index) != null && timer.hasReached(delay) && !isBad(stack)) {
                                    mc.playerController.windowClick(guiChest.inventorySlots.windowId, index, 0, ClickType.QUICK_MOVE, mc.thePlayer);
                                    if (mc.getCurrentServerData() != null && mc.getIntegratedServer() == null) {
                                        if (mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
                                            mc.playerController.windowClick(guiChest.inventorySlots.windowId, index, 1, ClickType.QUICK_MOVE, mc.thePlayer);
                                        }
                                    }
                                    timer.setLastMS();
                                }
                            }
                        }else {
                            mc.thePlayer.closeScreen();
                            isStealing = false;
                        }
                    } else {
                        isStealing = false;
                    }
                }
            }
        }
    }
    private EnumFacing getFacingDirection(final BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube() && mc.theWorld.getBlockState(pos.add(0, 1, 0)).getBlock() != Blocks.air) {
            direction = EnumFacing.UP;
        }
        final MovingObjectPosition rayResult = mc.theWorld.rayTraceBlocks(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ), new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        if (rayResult != null) {
            return rayResult.sideHit;
        }
        return direction;
    }


    private boolean isBad(ItemStack item) {
        ItemStack is = null;
        float lastDamage = -1;
//        InvCleaner cleaner = (InvCleaner)Client.instance.moduleManager.getModulesbycls(InvCleaner.class);
//        if (cleaner.shouldDrop(item)) {
//        	return true;
//        }
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is1 = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is1.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
                    if (lastDamage < getDamage(is1)) {
                        lastDamage = getDamage(is1);
                        is = is1;
                    }
                }
                if (is1.getItem() instanceof ItemAxe && item.getItem() instanceof ItemAxe) {
                    is = is1;
                }
                if (is1.getItem() instanceof ItemPickaxe && item.getItem() instanceof ItemPickaxe) {
                    is = is1;
                }
//                if (cleaner.shouldDrop(item, i)) {
//                	return true;
//                }
            }

        }

        if (is != null && is.getItem() instanceof ItemAxe && item.getItem() instanceof ItemAxe) {
            return false;
        }

        if (is != null && is.getItem() instanceof ItemPickaxe && item.getItem() instanceof ItemPickaxe) {
            return false;
        }

        if (is != null && is.getItem() instanceof ItemSword && item.getItem() instanceof ItemSword) {
            float currentDamage = getDamage(is);
            float itemDamage = getDamage(item);
            if (itemDamage > currentDamage) {
                return false;
            }
        }

        return item != null &&
                ((item.getItem().getUnlocalizedName().contains("tnt")) ||
                        (item.getItem().getUnlocalizedName().contains("stick")) ||
                        (item.getItem().getUnlocalizedName().contains("egg") && !item.getItem().getUnlocalizedName().contains("leg")) ||
                        (item.getItem().getUnlocalizedName().contains("string")) ||
                        (item.getItem().getUnlocalizedName().contains("flint")) ||
                        (item.getItem().getUnlocalizedName().contains("compass")) ||
                        (item.getItem().getUnlocalizedName().contains("feather")) ||
                        (item.getItem().getUnlocalizedName().contains("bucket")) ||
                        (item.getItem().getUnlocalizedName().contains("snow")) ||
                        (item.getItem().getUnlocalizedName().contains("fish")) ||
                        (item.getItem().getUnlocalizedName().contains("enchant")) ||
                        (item.getItem().getUnlocalizedName().contains("exp")) ||
                        (item.getItem().getUnlocalizedName().contains("shears")) ||
                        (item.getItem().getUnlocalizedName().contains("anvil")) ||
                        (item.getItem().getUnlocalizedName().contains("torch")) ||
                        (item.getItem().getUnlocalizedName().contains("seeds")) ||
                        (item.getItem().getUnlocalizedName().contains("leather")) ||
                        ((item.getItem() instanceof ItemGlassBottle)) ||
                        (item.getItem().getUnlocalizedName().contains("piston")));
    }

    private float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f + ((ItemSword) stack.getItem()).getDamageVsEntity();
    }

    public static boolean shouldChest = false;

    private boolean rayTrace(float yaw, float pitch, BlockPos pos) {
        Vec3 vec3 = mc.thePlayer.getPositionEyes(1.0f);
        Vec3 vec31 = RayCastUtil.getVectorForRotation(yaw, pitch);
        Vec3 vec32 = vec3.addVector(vec31.xCoord + 0.5, vec31.yCoord + 0.5, vec31.zCoord + 0.5);

        MovingObjectPosition result = mc.theWorld.rayTraceBlocks(vec3, vec32, false);

        return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && pos.equals(result.getBlockPos());
    }

}
