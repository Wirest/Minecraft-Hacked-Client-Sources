package net.minecraft.client.gui.inventory;

import java.io.IOException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class GuiChest extends GuiContainer {
	/** The ResourceLocation containing the chest GUI texture. */
	private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation(
			"textures/gui/container/generic_54.png");
	private IInventory upperChestInventory;
	private IInventory lowerChestInventory;

	/**
	 * window height is calculated with these values; the more rows, the heigher
	 */
	private int inventoryRows;

	public GuiChest(IInventory upperInv, IInventory lowerInv) {
		super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer));
		this.upperChestInventory = upperInv;
		this.lowerChestInventory = lowerInv;
		this.allowUserInput = false;
		int i = 222;
		int j = i - 108;
		this.inventoryRows = lowerInv.getSizeInventory() / 9;
		this.ySize = j + this.inventoryRows * 18;
	}

	@Override
	public void initGui() {
		if (!Jigsaw.ghostMode) {
			this.buttonList.add(new GuiButton(0, width / 2 - 150, height / 2 + 40, 63, 20, "Steal"));
			this.buttonList.add(new GuiButton(1, width / 2 - 150, height / 2 - 50, 63, 20, "Store"));
		}
		super.initGui();
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 0) {
			steal();
		}
		if (button.id == 1) {
			store();
		}
	}

	public void steal() {
		if(ClientSettings.chestStealDelay) {
			stealNCP();
		}
		else {
			for (int ii = 0; ii < this.inventoryRows * 9; ii++) {
				Slot s = inventorySlots.inventorySlots.get(ii);
				if (s.getStack() == null) {
					continue;
				}
				handleMouseClick(s, s.slotNumber, 0, 1);
				handleMouseClick(s, s.slotNumber, 0, 6);
			}
		}
	}
	
	public boolean isEmpty() {
		for (int ii = 0; ii < this.inventoryRows * 9; ii++) {
			Slot s = inventorySlots.inventorySlots.get(ii);
			if (s.getStack() == null) {
				continue;
			}
			return false;
		}
		return true;
	}
	
	public void stealNCP() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int ii = 0; ii < inventoryRows * 9; ii++) {
					Slot s = inventorySlots.inventorySlots.get(ii);
					if (s.getStack() == null) {
						continue;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handleMouseClick(s, s.slotNumber, 0, 1);
					handleMouseClick(s, s.slotNumber, 0, 6);
				}
			}
		}).start();
		
	}

	public void store() {
		for (int ii = 0; ii < 4 * 9; ii++) {
			Slot s = inventorySlots.inventorySlots.get(ii + inventoryRows * 9);
			if (s.getStack() == null) {
				continue;
			}
			handleMouseClick(s, s.slotNumber, 0, 1);
			handleMouseClick(s, s.slotNumber, 0, 6);
		}
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of
	 * the items). Args : mouseX, mouseY
	 */
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 4210752);
		this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8,
				this.ySize - 96 + 2, 4210752);
	}

	/**
	 * Args : renderPartialTicks, mouseX, mouseY
	 */
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
		this.drawTexturedModalRect(i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
	}
}
