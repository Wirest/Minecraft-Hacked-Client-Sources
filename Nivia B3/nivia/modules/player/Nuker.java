package nivia.modules.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.util.BlockPos;
import nivia.events.EventTarget;
import nivia.events.Priority;
import nivia.events.events.Event3D;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.events.events.EventTick;
import nivia.managers.PropertyManager;
import nivia.managers.PropertyManager.DoubleProperty;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.utils.Timer;

public class Nuker extends Module {
    public Nuker() {
        super("Nuker", 0, 0x666666, Category.PLAYER, "Mines blocks around you.",
                new String[] { "swalk", "sw" }, true);
    }

    private int posX, posY, posZ;

    
    public DoubleProperty radius = new DoubleProperty(this, "Horizontal Radius", 3, 0, 50);
    public DoubleProperty height = new DoubleProperty(this, "Height Radius", 1, 0, 50);
    public PropertyManager.Property<Boolean> silent = new PropertyManager.Property<Boolean>(this, "Silent", true);
    private boolean isRunning;
    private Timer timer = new Timer();



    @EventTarget
    public void onTick(EventTick e){

    }
    @EventTarget(Priority.HIGHEST)
    public void onPre(EventPreMotionUpdates e){
        this.isRunning = false;
        int radius = (int) this.radius.getValue();
        int height = (int) this.height.getValue();
        for(int y = height; y >= -height; --y) {
            for(int x = -radius; x < radius; ++x) {
                for(int z = -radius; z < radius; ++z) {
                    this.posX = (int)Math.floor(this.mc.thePlayer.posX) + x;
                    this.posY = (int)Math.floor(this.mc.thePlayer.posY) + y;
                    this.posZ = (int)Math.floor(this.mc.thePlayer.posZ) + z;
                    if(this.mc.thePlayer.getDistanceSq(this.mc.thePlayer.posX + (double)x, this.mc.thePlayer.posY + (double)y, this.mc.thePlayer.posZ + (double)z) <= 16.0D) {
                        Block block = Helper.blockUtils().getBlock(this.posX , this.posY, this.posZ);
                        boolean blockChecks = timer.hasTimeElapsed(50L);
                        Block selected = Helper.blockUtils().getBlock(mc.objectMouseOver.func_178782_a());

                        blockChecks = blockChecks && Helper.blockUtils().canSeeBlock(this.posX + 0.5F, this.posY + 0.9f, this.posZ + 0.5F) && !(block instanceof BlockAir);
                        blockChecks = blockChecks && (block.getBlockHardness(this.mc.theWorld, BlockPos.ORIGIN) != -1.0F || this.mc.playerController.isInCreativeMode());
                        if(blockChecks) {
                            this.isRunning = true;
                            
                            float[] angles = Helper.worldUtils().faceBlock(this.posX + 0.5F, this.posY + 0.9, this.posZ + 0.5F);
                            if(silent.value){

                            	e.setYaw(angles[0]);
                                e.setPitch(angles[1]);
                            } else {
                            	
                                mc.thePlayer.rotationYaw = angles[0];
                                mc.thePlayer.rotationPitch = angles[1];
                            }
                            return;
                        }
                    }
                }
            }
        }
    }
    @EventTarget
    public void onRender(Event3D e) {

    }
    @Override
    public void onDisable(){
        super.onDisable();
        isRunning = false;
        posX = posY = posZ = 0;
    }
    @EventTarget
    public void onPost(EventPostMotionUpdates e){
        Block block = Helper.blockUtils().getBlock(this.posX, this.posY, this.posZ);
        if(this.isRunning) {
            this.mc.thePlayer.swingItem();
            this.mc.playerController.func_180512_c(new BlockPos(this.posX , this.posY, this.posZ), Helper.blockUtils().getFacing(new BlockPos(this.posX, this.posY, this.posZ)));
            if((double)this.mc.playerController.curBlockDamageMP >= 1.0D)
               timer.reset();

        }
    }
}
