package skyline.specc.helper;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.MoveEvents.EventMoveRaw;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import skyline.SkyLiner;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.command.Command;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerChat;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerInteractEntity;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPushOut;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventSetAir;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.Event;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventSystem;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;
import skyline.specc.helper.loc.Loc;
import skyline.specc.render.renderevnts.EventOpaqueBlock;
import skyline.specc.utils.Wrapper;

public class Player extends EntityPlayerSP {

	private boolean canJump = true;

	public Player(Mineman mcIn, World worldIn, NetHandlerPlayClient p_i46278_3_, StatFileWriter p_i46278_4_) {
		super(mcIn, worldIn, p_i46278_3_, p_i46278_4_);
		for (Module module : SkyLine.getManagers().getModuleManager().getContents()) {
			module.mc = Mineman.getMinecraft();
			module.p = this;
		}
	}

	@Override
	public void setAir(int air) {
		EventSetAir event = new EventSetAir(air);

		EventSystem.call(event);

		if (event.isCancelled())
			return;

		air = event.getAir();

		super.setAir(air);
	}

	public Loc getLastPosition() {
		return new Loc(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ);
	}

	public void attackTargetEntityWithCurrentItem() {
		this.attackTargetEntityWithCurrentItem(
				new EntityCritFX(this.worldObj, this.posX, this.posY, this.posZ, 0, 0, 0));
	}

	public void damagePlayer() {
		for (int i = 0; i < 80.0 + 40.0 * (1.0 - 0.1); ++i) {
			this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
					this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.049, this.mc.thePlayer.posZ, false));
			this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
					this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
		}
		this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX,
				this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
	}

	public static boolean isInLiquid() {
		if (Wrapper.getPlayer() == null)
			return false;
		boolean inLiquid = false;
		int y = (int) Wrapper.getPlayer().boundingBox.minY;
		for (int x = MathHelper.floor_double(Wrapper.getPlayer().boundingBox.minX); x < MathHelper
				.floor_double(Wrapper.getPlayer().boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Wrapper.getPlayer().boundingBox.minZ); z < MathHelper
					.floor_double(Wrapper.getPlayer().boundingBox.maxZ) + 1; z++) {
				Block block = Wrapper.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid))
						return false;
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}

	public static boolean isOnLiquid() {
		if (Wrapper.getPlayer() == null)
			return false;
		boolean onLiquid = false;
		int y = (int) Wrapper.getPlayer().boundingBox.offset(0.0D, -0.01D, 0.0D).minY;
		for (int x = MathHelper.floor_double(Wrapper.getPlayer().boundingBox.minX); x < MathHelper
				.floor_double(Wrapper.getPlayer().boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Wrapper.getPlayer().boundingBox.minZ); z < MathHelper
					.floor_double(Wrapper.getPlayer().boundingBox.maxZ) + 1; z++) {
				Block block = Wrapper.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (!(block instanceof BlockLiquid))
						return false;
					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}

	@Override
	protected float func_175134_bD() {
		if (!canJump)
			return 0;
		return super.func_175134_bD();
	}

	public boolean canJump() {
		return this.canJump;
	}

	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}

	public static boolean isUnderBlock() {
		if (Wrapper.getPlayer() == null)
			return false;
		;
		int y = (int) Wrapper.getPlayer().getLoc().add(0, 2, 0).getY();
		for (int x = MathHelper.floor_double(Wrapper.getPlayer().boundingBox.minX); x < MathHelper
				.floor_double(Wrapper.getPlayer().boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor_double(Wrapper.getPlayer().boundingBox.minZ); z < MathHelper
					.floor_double(Wrapper.getPlayer().boundingBox.maxZ) + 1; z++) {
				Block block = Wrapper.getWorld().getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (block.isCollidable())
						return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean pushOutOfBlocks(double x, double y, double z) {
		Event event = new EventPushOut();

		EventSystem.call(event);

		if (event.isCancelled())
			return false;

		return super.pushOutOfBlocks(x, y, z);
	}

	public boolean isInsideBlock() {
		for (int x = MathHelper.floor_double(this.boundingBox.minX); x < MathHelper.floor_double(this.boundingBox.maxX)
				+ 1; x++) {
			for (int y = MathHelper.floor_double(this.boundingBox.minY); y < MathHelper
					.floor_double(this.boundingBox.maxY) + 1; y++) {
				for (int z = MathHelper.floor_double(this.boundingBox.minZ); z < MathHelper
						.floor_double(this.boundingBox.maxZ) + 1; z++) {
					Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
					if ((block != null) && (!(block instanceof BlockAir))) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.theWorld, new BlockPos(x, y, z),
								mc.theWorld.getBlockState(new BlockPos(x, y, z)));
						if ((boundingBox != null) && (this.boundingBox.intersectsWith(boundingBox)))
							return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void moveEntity(double x, double y, double z) {
		EventMoveRaw event = (EventMoveRaw) EventSystem.call(new EventMoveRaw(x, y, z));

		if (event.isCancelled()) {
			return;
		}

		x = event.x;
		y = event.y;
		z = event.z;
		super.moveEntity(x, y, z);
	}

	@Override
	public boolean isEntityInsideOpaqueBlock() {
		EventOpaqueBlock e = new EventOpaqueBlock();
		EventSystem.call(e);
		if (!e.isCancelled()) {
			return super.isEntityInsideOpaqueBlock();
		}
		return false;
	}

	public boolean isMoving() {
		return this.moveForward != 0 || this.moveStrafing != 0;
	}

	public void addChatMessage(String text) {
		this.addChatMessage(new ChatComponentText(text.replaceAll("&", "§")));
	}

	public float getSpeed() {
		float vel = (float) Math.sqrt((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
		return vel;
	}

	public void setSpeed(float speed) {
		this.motionX = -(Math.sin(this.getDirection()) * speed);
		this.motionZ = (Math.cos(this.getDirection()) * speed);
	}

	@Override
	public boolean interactWith(Entity p_70998_1_) {
		boolean bool;
		if (EventSystem.call(new EventPlayerInteractEntity(p_70998_1_, EventType.PRE)).isCancelled())
			return false;
		bool = super.interactWith(p_70998_1_);
		EventSystem.call(new EventPlayerInteractEntity(p_70998_1_, EventType.POST));
		return bool;
	}

	public float getDirection() {
		float var1 = this.rotationYaw;

		if (this.moveForward < 0.0F) {
			var1 += 180;
		}

		float forward = 1;

		if (this.moveForward < 0) {
			forward = -0.5f;
		} else if (this.moveForward > 0) {
			forward = 0.5f;
		} else {
			forward = 1;
		}

		if (this.moveStrafing > 0.0F) {
			var1 -= 90.0F * forward;
		}

		if (this.moveStrafing < 0.0F) {
			var1 += 90.0F * forward;
		}

		var1 *= 0.017453292F;

		return var1;
	}

	@Override
	public void onUpdate() {
		EventSystem.call(new EventPlayerUpdate(EventType.PRE));
		super.onUpdate();
		EventSystem.call(new EventPlayerUpdate(EventType.POST));
	}

	public static void setBindingAsInput(final KeyBinding binding) {
		if (binding.getKeyCode() < 0) {
			KeyBinding.setKeyBindState(binding.getKeyCode(), Mouse.isButtonDown(binding.getKeyCode() + 100));
		} else {
			KeyBinding.setKeyBindState(binding.getKeyCode(), Keyboard.isKeyDown(binding.getKeyCode()));
		}
	}

	@Override
	public void func_175161_p() {
		EventMotion event = (EventMotion) EventSystem.call(new EventMotion(this.getLoc(), this.onGround,
				this.rotationYaw, this.rotationPitch, EventType.PRE));

		if (event.isCancelled())
			return;

		Loc loc = this.getLoc();

		boolean tGround = this.onGround;

		float tYaw = rotationYaw;
		float tPitch = rotationPitch;

		this.setLocation(event.getLocation());
		this.onGround = event.isOnGround();

		super.func_175161_p();

		this.setLocation(loc);

		this.onGround = tGround;

		this.rotationYaw = tYaw;
		this.rotationPitch = tPitch;

		EventSystem.call(new EventMotion(this.getLoc(), this.onGround, this.rotationYaw, this.rotationPitch,
				EventType.POST));
	}

	@Override
	public void sendChatMessage(String p_71165_1_) {
		EventPlayerChat event = null;
		String message = p_71165_1_;

		if(message.startsWith(".")){
			
			String[] commandUndone = message.split("\\s+");

			String name = commandUndone[0].replaceFirst(".", "");
			List<String> args = new ArrayList<String>();

			for(int i = 1 ; i <= commandUndone.length - 1; i++){
				args.add(commandUndone[i]);
			}

			for(Command command : SkyLine.getManagers().getCommandManager().getContents()){
				if(command.getName().equalsIgnoreCase(name)){

					if(args.size() < 1){
						command.onCommand(new ArrayList<String>());
					}else{
						command.onCommand(args);
					}
				}
			}
			
			return;
		}else{
			event = (EventPlayerChat) EventSystem.call(new EventPlayerChat(
					p_71165_1_));
		}

		if(event != null){
			message = event.getMessage();
			if(event.isCancelled())
				return;
		}

		this.sendQueue.addToSendQueue(new C01PacketChatMessage(p_71165_1_));
	}


	public void sendPosPacket() {
		this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, this.posY, this.posZ,
				this.rotationYaw, this.rotationPitch, this.onGround));
	}

	public void sendPosPacketMax() {
		this.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(this.posX, Double.MAX_VALUE, this.posZ,
				this.rotationYaw, this.rotationPitch, this.onGround));
	}

	public int[] getSwordData() {
		int slot = -1, count = 0;
		for (int i = 0; i < 45; i++) {
			if (!this.inventoryContainer.getSlot(i).getHasStack()) {
				continue;
			}
			ItemStack is = this.inventoryContainer.getSlot(i).getStack();
			if (!(is.getItem() instanceof ItemSword)) {
				continue;
			}
			if (is == this.getHeldItem()) {
				continue;
			}
			slot = i;
			count++;
		}
		return new int[] { slot, count };
	}

	public void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.inventoryContainer.windowId, slot, hotbarNum, 2, this);
	}

}
