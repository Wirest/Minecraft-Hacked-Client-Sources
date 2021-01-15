package nivia.modules.miscellanous;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import nivia.events.EventTarget;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Wrapper;
import nivia.utils.utils.Timer;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ChestAura extends Module {
	private final Timer timer = new Timer();

	public ChestAura() {
		super("ChestAura", 0, 0x005C00, Category.MISCELLANEOUS, "Gets all the items from a chest.",
				new String[] { "csteal", "chests", "cstealer" }, true);
	}
	public final ArrayList<Item> items = new ArrayList<Item>();
	public PropertyManager.DoubleProperty delay = new PropertyManager.DoubleProperty(this, "Delay", 130L, 0, 2000);
	public Property<Boolean> selective = new Property<Boolean>(this, "Selective", false);
	public Property<Boolean> dropper = new Property<Boolean>(this, "Dropper", false);
	public Property<Boolean> invert = new Property<Boolean>(this, "Invert Items", false);
	
    public TileEntity getBestChest(double range) {
    	AtomicReference<TileEntity> tempEn = new AtomicReference<>();
        AtomicReference<Double> dist = new AtomicReference<>();
        AtomicReference<Double> curDist = new AtomicReference<>();
        dist.set(range);
        Wrapper.getWorld().loadedTileEntityList.forEach(o -> {
        	TileEntity tileEntity = (TileEntity)o;
        	curDist.set(getDistanceToEntity(tileEntity));
        	if (curDist.get() <= dist.get()) {
        		dist.getAndSet(curDist.get());
        		tempEn.set(tileEntity);
        	}
        });
        return tempEn.get();
    }
    
    public double getDistanceToEntity(TileEntity tileEntity) {
        double x = (Wrapper.getPlayer().posX - tileEntity.getPos().getX());
        double y = (Wrapper.getPlayer().posY - tileEntity.getPos().getY());
        double z = (Wrapper.getPlayer().posZ - tileEntity.getPos().getZ());
        return MathHelper.sqrt_double(x * x + y * y + z * z);
    }
    
	@EventTarget
	public void onPre(EventPreMotionUpdates e) {
        TileEntity tileEntity = this.getBestChest(3.0);
        if (Wrapper.getMinecraft().currentScreen == null) {
            Wrapper.getFontRenderer().gameSettings.keyBindUseItem.pressed = false;
            double posX = tileEntity.getPos().getX();
            double posY = tileEntity.getPos().getY();
            double posZ = tileEntity.getPos().getZ();
            Wrapper.getPlayer().swingItem();
            Wrapper.getPlayer().rotationYaw += (((float) Math.toDegrees(Math.atan2(Wrapper.getPlayer().posZ - posZ, Wrapper.getPlayer().posX - posX)) + 95.0f - Wrapper.getPlayer().rotationYaw) % 360 + 540) % 360 - 180;
        }
	}

	protected void addCommand() {
		
	}

}
