package info.sigmaclient.gui.screen;

import info.sigmaclient.gui.screen.component.InvManagerItem;
import info.sigmaclient.gui.screen.component.InvManagerSlot;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.management.keybinding.Keybind;
import info.sigmaclient.module.impl.player.InventoryManager;
import info.sigmaclient.util.FileUtils;
import info.sigmaclient.util.misc.Timer;
import info.sigmaclient.util.render.Colors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiInvManager  extends GuiScreen {
	protected final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/widgets.png");
	protected final ResourceLocation trash = new ResourceLocation("textures/trash.png");
	private Translate translateBox = new Translate(0 , 100);
	public static ArrayList<InvManagerSlot> slots = new ArrayList();
	boolean isClicking;
	InvManagerSlot mouseSlot;
	InvManagerItem selected;
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 20, 200, 20, "Close"));
    }
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                this.mc.displayGuiScreen(null);
                saveConfigs();
                break;
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        Minecraft m = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(m, m.displayWidth, m.displayHeight);
        int midX= res.getScaledWidth() / 2;
        int midY = res.getScaledHeight() / 2;
        int left = midX - 130;
        int top = midY - 20;
        int s = res.getScaleFactor();
    
        if(!slots.isEmpty()){
        	mouseSlot = null;
        	for(InvManagerSlot slot : slots){
        		int x = slot.getX();
        		int y = slot.getY();
        		InvManagerItem item = slot.getItem();
        		int renderX = left + x*21;
        		int renderY = top - y * 21;
        		ItemStack stack = item == null ? null : new ItemStack(Item.getItemById(item.getId()));
        		if(isMouseOverSlot(renderX, renderY, mouseX, mouseY)){
        			mouseSlot = slot;		
        		}
        		drawSlot(renderX, renderY);
        		
        		if(item != null)
        		if(!item.isSelected()){
        			drawItemStack(renderX+3, renderY+3, stack);
        		}else{
        			drawItemStack(mouseX-7, mouseY-7, stack);
        		}
        	}
        }
        double xT = left/1.7 + 36;
        double yT = top/1.7;     
        drawTrash(xT, yT);
    }

    
    @Override
    protected void mouseClicked(int clicX, int clicY, int button) {
        try {
            if(mouseSlot != null){
            	if(mouseSlot.getItem() != null){
            		mouseSlot.getItem().setSelected(true);
            		selected = mouseSlot.getItem();
            	}
            }
            super.mouseClicked(clicX, clicY, button);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
    	if(selected != null){
			if(mouseSlot == null){
				selected.getSlot().setItem(new InvManagerItem(selected.getSlot(), selected.getId(), false));
			}else{
				if(mouseSlot.getItem() == null){
					selected.getSlot().setItem(null);
		   			mouseSlot.setItem(new InvManagerItem(mouseSlot, selected.getId(), false));
				}else{
					selected.getSlot().setItem(new InvManagerItem(selected.getSlot(), selected.getId(), false));
				}  		
			}		
			selected = null;	
		}
    	
		super.mouseReleased(mouseX, mouseY, state);
    
    }
    private boolean isMouseOverSlot(int slotX, int slotY, int mouseX, int mouseY){
    	int top = slotY+1;
    	int bottom = slotY + 21;
    	int left = slotX;
    	int right = slotX + 21;
        return mouseX >= left && mouseX < right && mouseY >= top && mouseY <  bottom;
    }
  
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	if(keyCode == Keyboard.KEY_ESCAPE){
    		this.mc.displayGuiScreen(null);
    		saveConfigs();
    	}else if(keyCode == mc.gameSettings.keyBindInventory.getKeyCode()){
    		this.mc.displayGuiScreen(new GuiInventory(mc.thePlayer));
    		saveConfigs();
    	}
    }
    void drawSlot(int x, int y) {
    	this.mc.getTextureManager().bindTexture(inventoryBackground);
    	this.drawVerticalLine(x+21,y-1, y+22, Colors.getColor(0, 0, 0, 255));
    	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);
        this.drawTexturedModalRect(x, y, 0.0D, 0, 21, 22);	
    }
    void drawTrash(double x, double y){
       	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.blendFunc(770, 771);      
    	this.mc.getTextureManager().bindTexture(trash);
    	GL11.glScalef(0.17f, 0.17f, 0.17f);
        this.drawTexturedModalRect(x*10 -420, y*10-300, 0, 0, 220, 255);	  
    }
    
    public void drawTexturedModalRect(double x, double y, double textureX, double textureY, double width, double height) {
        float var7 = 0.00390625F;
        float var8 = 0.00390625F;
        Tessellator var9 = Tessellator.getInstance();
        WorldRenderer var10 = var9.getWorldRenderer();

        var10.startDrawingQuads();
        var10.addVertexWithUV((x + 0), (y + height), (double) this.zLevel, (double) ((float) (textureX + 0) * var7), (double) ((float) (textureY + height) * var8));
        var10.addVertexWithUV((x + width), (y + height), (double) this.zLevel, (double) ((float) (textureX + width) * var7), (double) ((float) (textureY + height) * var8));
        var10.addVertexWithUV((x + width), (y + 0), (double) this.zLevel, (double) ((float) (textureX + width) * var7), (double) ((float) (textureY + 0) * var8));
        var10.addVertexWithUV((x + 0), (y + 0), (double) this.zLevel, (double) ((float) (textureX + 0) * var7), (double) ((float) (textureY + 0) * var8));
        var9.draw();
        
    }
    void drawItemStack(int x, int y, ItemStack stack){
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        this.itemRender.func_180450_b(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(this.fontRendererObj, stack, x, y, "");
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;
        GL11.glColor4f(1, 1, 1, 1);
        RenderHelper.disableStandardItemLighting();
    }
    public static void saveConfigs(){
    	  List<String> fileContent = new ArrayList<>();
          for (InvManagerSlot slot : slots) {
        	  InvManagerItem it = slot.getItem();
        	  if(it != null){
        		  String add = it.getId() + ":" + it.getSlot().getID();
        		  fileContent.add(add);
        	  }
          }
          File SETTINGS_DIR = FileUtils.getConfigFile("InvManager");
          FileUtils.write(SETTINGS_DIR, fileContent, true);
          for (InvManagerSlot slot : slots) {
        	  InvManagerItem it = slot.getItem();
        	  if(it == null)
        	  	continue;
        	  int id = it.getId();
        	  int slotID = slot.getID();
        	  if(id > 0)
        	  if(id==276){
        		  InventoryManager.weaponSlot = 36 + slotID;
        	  }else if(id == 277){
        		  InventoryManager.shovelSlot = 36 + slotID;
        	  }else if(id == 278){
        		  InventoryManager.pickaxeSlot = 36 + slotID;
        	  }else if(id == 279){
        		  InventoryManager.axeSlot = 36 +  slotID;
        	  }
          }	 
    }
    public static void loadConfig(){ 	
        try {
        	File SETTINGS_DIR = FileUtils.getConfigFile("InvManager");
            List<String> fileContent = FileUtils.read(SETTINGS_DIR);
       
            int lenght = fileContent.size();
            int id = 276;
            for(int i = -4; i < 9; i++){
            	int x = i<0?0: i+2;
            	int y = i < 0? i+1:0;	
            	slots.add(new InvManagerSlot(i, x, y, null));
            }
            if(fileContent.isEmpty()){
            	for(int i = 4 ; i < 8; i++){
                	slots.get(i).setItem(new InvManagerItem(slots.get(i), id, false));
                	id++;
                }
            }else{
            	for(int i = 0; i < lenght; i++){
                	String[] split = fileContent.get(i).split(":");
                	id = Integer.parseInt(split[0]);
                	int slotID = Integer.parseInt(split[1]);
                	for(InvManagerSlot slot : slots){
                		if(slot.getID() == slotID){
                			slot.setItem(new InvManagerItem(slot, id, false));
                		}
                	}
                } 
            }   
            saveConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
