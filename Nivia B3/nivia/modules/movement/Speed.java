package nivia.modules.movement;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockIce;
import net.minecraft.block.BlockPackedIce;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import nivia.Pandora;
import nivia.commands.Command;
import nivia.events.EventTarget;
import nivia.events.events.EventMove;
import nivia.events.events.EventPostMotionUpdates;
import nivia.events.events.EventPreMotionUpdates;
import nivia.managers.PropertyManager.Property;
import nivia.modules.Module;
import nivia.utils.Helper;
import nivia.utils.Logger;
import nivia.utils.utils.MathUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.List;
 
public class Speed extends Module {
    private double delay1 = 0, delay2 = 0, delay3;
    private double hAllowedDistance = 0.2873000087011036D;
    private int JUMPTicks;
    private boolean speedTick;
    public Property<Boolean> ice = new Property<Boolean>(this, "Ice", true);
    public Property<Boolean> sprint = new Property<Boolean>(this, "Sprint", true);
    public Property<Boolean> ladders = new Property<Boolean>(this, "Ladders", true);
    public Property<Boolean> sanik = new Property<Boolean>(this, "Sanik", true);
 
    public Property<speedMode> mode = new Property<speedMode>(this, "Mode", speedMode.HOP);
    private nivia.utils.utils.Timer timerino = new nivia.utils.utils.Timer();
    private int level;
    private double moveSpeed;
    private double lastDist;
    private boolean b2, b3;
    private boolean glide = false, slowFall = false;
 
    public Speed() {
        super("Speed", Keyboard.KEY_O, 0x75FF47, Category.MOVEMENT, "Makes you move faster.", new String[]{"sp", "sanik", "sped"} , true);
    }
 
    public enum speedMode{
        OLD, HOP, SLOWHOP,  YPORT, TRENDE, LONGHOP, LATEST, MINEPLEX;
    }
 
    @Override
    public void onDisable(){
        super.onDisable();
        delay1 = delay2 = delay3 = 0;
        Timer.timerSpeed = 1;
        speedTick = false;
        slowFall = false;
        moveSpeed = getBaseMoveSpeed();
        b2 = true;
        glide = false;
        level = 0;
        Blocks.packed_ice.slipperiness = 0.98f;
        Blocks.ice.slipperiness = 0.98f;
        timerino.reset();
        mc.gameSettings.viewBobbing = b2;
    }
    @Override
    public void onEnable(){
        super.onEnable();
        b2 = mc.gameSettings.viewBobbing;
        timerino.reset();
        level = 1;
        moveSpeed = (mc.thePlayer == null ? 0.2873D : getBaseMoveSpeed());
        b3 = true;
 
    }
    public void clear(){
        delay1 = delay2 = delay3 = 0;
        Timer.timerSpeed = 1;
        speedTick = false;
        moveSpeed = getBaseMoveSpeed();
        b2 = true;
        glide = false;
        level = 0;
        Blocks.packed_ice.slipperiness = 0.98f;
        Blocks.ice.slipperiness = 0.98f;
    }
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @EventTarget
    public void onPost(EventPostMotionUpdates e){
        this.setSuffix(mode.value.toString());

        if(ice.value){
            if(Helper.blockUtils().getBlockUnderPlayer(mc.thePlayer, 0.001) instanceof BlockIce || Helper.blockUtils().getBlockUnderPlayer(mc.thePlayer, 0.001) instanceof BlockPackedIce  ){
                Blocks.ice.slipperiness = 0.39f;
                Blocks.packed_ice.slipperiness = 0.39f;
                return;
            }
        } else {
            Blocks.ice.slipperiness = 0.98f;
            Blocks.packed_ice.slipperiness = 0.98f;
        }
        if(sanik.value && canSpeed()){
            if(mode.value.equals(speedMode.OLD))
                doOLDSpeed();          
        }
        if(this.glide && Helper.playerUtils().getDistanceToFall() <= 0.475 && Helper.playerUtils().MovementInput()) {
            this.mc.thePlayer.motionY = -0.005D;
        }
    }
 
    @EventTarget
    public void onPre(EventPreMotionUpdates e){
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX, zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        if(sanik.value)
            delay1++;
        if (canSpeed() && sanik.value)
            delay2++;
        if(!canSpeed()){
            delay2 = -2;
            Timer.timerSpeed = 1.0F;
        }
        if (ladders.value){
            if(mc.thePlayer.isOnLadder() && mc.thePlayer.isSneaking())
                mc.thePlayer.motionY *= 0;
            else if (mc.thePlayer.isOnLadder() && mc.thePlayer.isCollidedHorizontally)
                mc.thePlayer.motionY *= 2.40;
        }
        if (sprint.value) {
            if(mc.thePlayer.isSneaking() || mc.thePlayer.isCollidedHorizontally || mc.thePlayer.motionX == 0 || mc.thePlayer.isOnLadder() || (!Pandora.getModManager().getModState("NoSlow") && mc.thePlayer.isBlocking()))
                mc.thePlayer.setSprinting(false);
            else {
                if(!sanik.value && mc.thePlayer.moveForward > 0)
                    mc.thePlayer.setSprinting(true);
                else if(sanik.value)
                    mc.thePlayer.setSprinting(true);
            }
        }
        if(mode.value.equals(speedMode.LATEST) && Helper.playerUtils().MovementInput()){
            if(!Helper.playerUtils().MovementInput() || Helper.playerUtils().getDistanceToFall() > 1.5 || !this.canSpeed())
                return;
            if(mc.gameSettings.keyBindJump.pressed || mc.thePlayer.motionY > 0) return;
            if(isColliding(mc.thePlayer.boundingBox.expand(0.4, 0, 0.4)) && Helper.playerUtils().MovementInput() || mc.thePlayer.isCollidedHorizontally) {
                e.setGround(true);
                return;
            }
            if(!mc.thePlayer.isCollidedVertically || !mc.thePlayer.onGround){
                if(!e.isGround())
                    e.setGround(true);
                return;
            }
            if(Helper.playerUtils().MovementInput() && Pandora.getModManager().getModState("Criticals"))
                e.setGround(false);
            if (delay2 == 2)
                e.setY(e.getY() + 0.4);
       
        }
        if (mode.value.equals(speedMode.MINEPLEX)) {
            if (Helper.playerUtils().MovementInput()) {
                Helper.player().motionX *= 1.39;
                Helper.player().setPosition(Helper.player().posX, Helper.player().posY + 0.4, Helper.player().posZ);
                Helper.player().motionZ *= 1.39;
                Helper.player().onGround = true;
            } else {
                Helper.player().motionX *= 0;
                Helper.player().motionX *= 0;
            }
        }
    }
 
 
    @EventTarget
    public void onMove(EventMove event){
        if(!Helper.playerUtils().MovementInput()){
            level = 1;
            timerino.reset();
            return;
        }
 
        MovementInput movementInput = mc.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = mc.thePlayer.rotationYaw;
        if ((forward == 0.0F) && (strafe == 0.0F)) {
            event.x = 0.0D;
            event.z = 0.0D;
        } else if (forward != 0.0F) {
            if (strafe >= 1.0F) {
                yaw += (forward > 0.0F ? -45 : 45);
                strafe = 0.0F;
            } else if (strafe <= -1.0F) {
                yaw += (forward > 0.0F ? 45 : -45);
                strafe = 0.0F;
            }
            if (forward > 0.0F)
                forward = 1.0F;
            else if (forward < 0.0F)
                forward = -1.0F;
        }
        double mx = Math.cos(Math.toRadians(yaw + 90.0F));
        double mz = Math.sin(Math.toRadians(yaw + 90.0F));
        if(mode.value.equals(speedMode.YPORT))
            mc.gameSettings.viewBobbing = false;
        this.doHops(event, forward, strafe, mx, mz);
        if(mode.value.equals(speedMode.TRENDE))
            this.doGlideSpeed(event, forward, strafe, mx, mz);
        if(mode.value.equals(speedMode.LONGHOP))
            this.doLongJumps(event, forward, strafe, mx, mz);
        if(mode.value.equals(speedMode.LATEST))
            this.doLatestSpeed(event, forward, strafe, mx, mz);
        if(Helper.player().isInWater() )
            this.doJesusSpeed(event, forward, strafe, mx, mz);
           
    }
    public void doLatestSpeed(EventMove e, float forward, float strafe, double mx, double mz){
        if(mc.thePlayer.isCollidedHorizontally) return;
        double m =  0.455;
        double m2 = 0.31;
        final int amplifier = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
        double boost = 1;
        switch(amplifier){
            case 1:            
                boost = 1.2;
                break;
            case 2:
                boost = 1.4;
                break;
            case 3:
                boost = 2.8;
                break;
            case 4:
                boost = 3.4;
                break;
        }
       
        if(mc.thePlayer.ticksExisted % 3 == 0)
            Timer.timerSpeed = 1.3F;
        else Timer.timerSpeed = 1.0F;
       
        if(Helper.playerUtils().getDistanceToFall() > 1.4 && Helper.playerUtils().getDistanceToFall() < 2)
            e.y = mc.thePlayer.motionY = -5;
        if (delay2 == 1) {     
            e.y = mc.thePlayer.motionY = -5;
            e.x = (forward * m * mx + strafe * m * mz);
            e.z = (forward * m * mz - strafe * m * mx);
        } else if (delay2 == 2) {
            e.x = (forward * m2 * mx + strafe * m2 * mz);
            e.z = (forward * m2 * mz - strafe * m2 * mx);  
            delay2 = 0;
        }
        e.x *= boost;
        e.z *= boost;
    }
    public void doGlideSpeed(EventMove e, float forward, float strafe, double mx, double mz){
 
        if (!Helper.playerUtils().MovementInput() || !sanik.value || Helper.blockUtils().isOnLiquid() || mc.thePlayer.isInWater() || !Helper.playerUtils().isMoving()) {
            level = -8;
            delay3 = -10;
            return;
        }
        if(b2){
            if (!Helper.playerUtils().MovementInput())
                this.level = 1;
            if (this.level == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
                this.level = 2;
                final int amplifier = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
                double boost = 4.4;
                switch(amplifier){
                    case 1:
                        boost = 3.9;
                        break;
                    case 2:
                        boost = 3.5;
                        break;
                    case 3:
                        boost = 3.1;
                        break;
                    case 4:
                        boost = 2.7;
                        break;
                }
           
                this.moveSpeed = (boost) * this.getBaseMoveSpeed() - 0.01;
            } else if (this.level == 2) {
                this.level = 3;
               
                this.mc.thePlayer.motionY = 0.4242D;
                e.y = 0.4242D;
                this.moveSpeed *= (2.149802);
            } else if (this.level == 3) {
 
                this.level = 4;
                final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            } else {
                if (mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
                        this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0|| this.mc.thePlayer.isCollidedVertically)
                    this.level = 1;
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
 
            e.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
            e.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
            if (forward == 0.0f && strafe == 0.0f) {
                e.x = 0;
                e.z = 0;
            }
        }
            if(this.timerino.hasTimeElapsed(460)) {
                glide = true;
            }
            if(this.timerino.hasTimeElapsed(1890)) {
                Timer.timerSpeed = 1.05F;
                glide = false;
                mc.thePlayer.motionX *= 0.45;
                mc.thePlayer.motionZ *= 0.45;
                b2 = false;
               
            }
            if(this.timerino.hasTimeElapsed(2125) && mc.thePlayer.onGround)
                this.mc.thePlayer.motionY = 0.4D;
           
            if(this.timerino.hasTimeElapsed(2350L)) {
                Timer.timerSpeed = 1.0F;
                this.b2 = true;
                level = 0;
                this.timerino.reset();     
        }
    }
    public void doLongJumps(EventMove e, float forward, float strafe, double mx, double mz) {
        if (!Helper.playerUtils().MovementInput() || !sanik.value || Helper.blockUtils().isOnLiquid() || mc.thePlayer.isInWater() || !Helper.playerUtils().isMoving()) {
            level = -8;
            delay3 = -10;
            return;
        }
        if (b2) {
            if (!Helper.playerUtils().MovementInput()) {
                this.level = 1;
            }
            if (this.level == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
                this.level = 2;
                final int amplifier = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1 : 0;
                double boost = 3.5D;
                switch (amplifier) {
                    case 1:
                        boost = 3.3;
                        break;
                    case 2:
                        boost = 3.1;
                        break;
                    case 3:
                        boost = 2.9;
                        break;
                    case 4:
                        boost = 2.7;
                        break;
                }                          
               
                this.moveSpeed = (boost) * this.getBaseMoveSpeed() - 0.01;
            } else if (this.level == 2) {
                this.level = 3;
                this.mc.thePlayer.motionY = 0.424;
                e.y = 0.424;
                this.moveSpeed *= (2.149802);
            } else if (this.level == 3) {
   
                this.level = 4;
                final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            } else {
                Timer.timerSpeed = 1.0F;
                if (mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer,
                        this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically)
                    this.level = 1;
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            e.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
            e.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
            if (forward == 0.0f && strafe == 0.0f) {
                e.x = 0;
                e.z = 0;
            }
        }
        if(this.timerino.hasTimeElapsed(315) && Helper.playerUtils().getDistanceToFall() <= 1.4){
            mc.thePlayer.motionY *= 0.78;
            e.y *= 0.78;
 
        }
        if(timerino.hasTimeElapsed(500,false) &&mc.thePlayer.isCollidedVertically){
 
           
 
        }
        if (this.timerino.hasTimeElapsed(620)   ) {
            Timer.timerSpeed = 1.05F;
            if(Helper.playerUtils().getDistanceToFall() < 2){
                mc.thePlayer.motionX *= 0.35;
                mc.thePlayer.motionZ *= 0.35;
            }
            level = 1;
            b2 = false;
            Timer.timerSpeed = 1.0F;
        }
 
        if (this.timerino.hasTimeElapsed(1000) && mc.thePlayer.isCollidedVertically) {
            Timer.timerSpeed = 1.0F;
            this.timerino.reset();
            level = 1;
            this.b2 = true;
        }
    }
    public void doHops(EventMove event, float forward, float strafe, double mx, double mz) {
 
        if (mode.value != speedMode.HOP && mode.value != speedMode.YPORT && mode.value != speedMode.SLOWHOP)
            return;
        boolean s = mode.value == speedMode.SLOWHOP;
        if (!Helper.playerUtils().MovementInput() || !sanik.value || Helper.blockUtils().isOnLiquid() || mc.thePlayer.isInWater() || !Helper.playerUtils().isMoving()) {
            level = -8;
            delay3 = -10;
            return;
        }
 
        delay3++;
        float tinc = Pandora.getModManager().getModState("Glide") ? 1.0F : (mode.value.equals(speedMode.HOP) || mode.value.equals(speedMode.YPORT)) ? 1.175f : 2.5F;
        if (mc.thePlayer.onGround) {
            if (Helper.playerUtils().isMoving() && delay3 > 8)
                level = 2;
        }
        if(mc.thePlayer.ticksExisted % 3 == 0)
            Timer.timerSpeed = tinc;
        else  Timer.timerSpeed = 1.0F;
 
        if (MathUtils.round(mc.thePlayer.posY - (int) mc.thePlayer.posY, 3) == MathUtils.round(0.138D, 3) && mode.value.equals(speedMode.YPORT)) {
            mc.thePlayer.motionY -= 5D;
            event.y -= 5D;
        }
        if (level == -1) {
            event.x *= 0.3;
            event.z *= 0.3;
        }
        if ((level == 1) && ((mc.thePlayer.moveForward != 0.0F) || (mc.thePlayer.moveStrafing != 0.0F))) {
            moveSpeed = (0.8D * getBaseMoveSpeed());
            if (mode.value.equals(speedMode.YPORT)) {
                mc.thePlayer.motionY -= 5D;
                event.y -= 5D;
            }
        } else if (level == 2) {
            if (mode.value.equals(speedMode.YPORT))
                moveSpeed *= 0.9;
            if (mc.thePlayer.isCollidedVertically) {
                if (!mode.value.equals(speedMode.YPORT))
                    moveSpeed *= s ? 1.5 : 2.149D;
                    mc.thePlayer.motionY = 0.405;
                event.y = 0.405;
                if (mode.value.equals(speedMode.YPORT)) {
                    moveSpeed *= 2.3875D;
                    Timer.timerSpeed = 1.05F;
                    mc.thePlayer.motionY = -5.0D;
                }
            }
 
        } else if (level == 3) {
            double difference = 0.66D * (lastDist - getBaseMoveSpeed());
            moveSpeed = (lastDist - difference);
        } else {
            final List collidingList = mc.theWorld.getCollidingBlockBoundingBoxes(mc.thePlayer, mc.thePlayer.boundingBox.offset(0.0, mc.thePlayer.motionY, 0.0));
            if ((collidingList.size() > 0 || mc.thePlayer.isCollidedVertically) && level > 0) {
                level = ((mc.thePlayer.moveForward != 0.0f || mc.thePlayer.moveStrafing != 0.0f) ? 1 : 0);
            }
            moveSpeed = (lastDist - lastDist / 159.0D);
        }
 
        moveSpeed = Math.max(moveSpeed, getBaseMoveSpeed());
        event.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
        event.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
        if ((forward == 0.0F) && (strafe == 0.0F)) {
            event.x = 0.0D;
            event.z = 0.0D;
        }
        level++;
    }
    public void doJesusSpeed(EventMove e, float forward, float strafe, double mx, double mz) {

    }
    public void doOLDSpeed(){
 
        boolean strafing = mc.thePlayer.moveStrafing != 0;
        double speed = strafing ? 3 : 3.1;
        if(mc.thePlayer.ticksExisted % 3 == 0){
            Timer.timerSpeed = 1.3F;
        } else Timer.timerSpeed = 1.0F;
        if (delay2 == 2) {
            mc.thePlayer.motionX *= speed;
            mc.thePlayer.motionZ *= speed;
        } else if (delay2 == 3) {
            mc.thePlayer.motionX /= 1.466D;
            mc.thePlayer.motionZ /= 1.466D;
        }  else if (delay2 >= 4)
            delay2 = 0;
    }
 
    private boolean canSpeed(){
        boolean ignore = (mode.value == speedMode.HOP || mode.value == speedMode.YPORT);
        if(
                !Helper.player().isOnLadder() &&
                        !Helper.player().isInWater() &&
                        !Helper.player().isSneaking() &&
                        (ignore ? true : mc.thePlayer.onGround) &&
                        (ignore ? true : !mc.gameSettings.keyBindJump.getIsKeyPressed()) &&
                        Helper.player().motionX != 0 &&
                        Helper.player().motionZ != 0 &&
                        !Helper.blockUtils().isOnLiquid()) {
            return true;
        }
        return false;
    }
 
    private Block getBlock(AxisAlignedBB bb){
        int y = (int)bb.minY;
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; x++){
            for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; z++){
                Block block = Helper.mc().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null)
                    return block;
            }
        }
        return null;
    }
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    public boolean isColliding(AxisAlignedBB bb){
        boolean colliding = false;
        Iterator iterator = Helper.mc().theWorld.getCollidingBoundingBoxes(Helper.player(),bb).iterator();
        while (iterator.hasNext()){
            AxisAlignedBB boundingBox = (AxisAlignedBB)iterator.next();
            colliding = true;
        }
        if ((getBlock(bb.offset(0.0D, -0.1D, 0.0D)) instanceof BlockAir))
            colliding = true;
        return colliding;
    }
    protected void addCommand(){
        Pandora.getCommandManager().cmds.add(new Command
                ("Speed", "Manages speed values", Logger.LogExecutionFail("Option, Options:", new String[]{"Ice", "Sprint", "Ladders", "Sanik", "Old", "Hop", "Latest", "YPort","SlowHop", "TrendE", "LongHOP"}) , "sp") {
            @Override
            public void execute(String commandName,String[] arguments){
                String message = arguments[1];
                clear();
                switch(message.toLowerCase()){
                    case "ice":
                        ice.value = !ice.value;
                        Logger.logToggleMessage("Ice", ice.value, "Speed");
                        break;
 
                    case "sprint": case "sp":
                        sprint.value = !sprint.value;
                        Logger.logToggleMessage("Sprint", sprint.value, "Speed");
                        break;
                    case "ladders": case "lad":
                        ladders.value = !ladders.value;
                        Logger.logToggleMessage("Ladders", ladders.value, "Speed");
                        break;
                    case "speed": case "sanik":
                        sanik.value = !sanik.value;
                        Logger.logToggleMessage("Sanik", sanik.value, "Speed");
                        break;
                    case "old": case "o":
                        mode.value = speedMode.OLD;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "hop":case "bhop":
                        mode.value = speedMode.HOP;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "trende":case "t":case "tren": case "tr":
                        mode.value = speedMode.TRENDE;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "longhop":case "lh":case "longjump": case "longh": case "lj":
                        mode.value = speedMode.LONGHOP;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "yport": case "yp":
                        mode.value = speedMode.YPORT;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "latest": case "l": case "ncp": case "lncp": case "ln": case "lastncp":
                        mode.value = speedMode.LATEST;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "slowhop": case "sh": case "shop": case "slowh":
                        mode.value = speedMode.SLOWHOP;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "mineplex": case "mp":
                        mode.value = speedMode.MINEPLEX;
                        Logger.logSetMessage("Speed", "Mode", mode);
                        break;
                    case "values": case "actual":
                        logValues();
                        break;
                    default:
                        Logger.logChat(this.getError());
                        break;
                }
                mc.gameSettings.viewBobbing = b2;
            }
        });
    }
}