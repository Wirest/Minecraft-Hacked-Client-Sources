package info.spicyclient.ClickGUI;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.Module.Category;
import info.spicyclient.settings.BooleanSetting;
import info.spicyclient.settings.KeybindSetting;
import info.spicyclient.settings.ModeSetting;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.settings.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.util.ResourceLocation;

public class ClickGUI extends GuiScreen {
	
	public ClickGUI(GuiScreen last) {
		last = this.last;
	}
	
	public static int accentColor = 0xffff0000;
	
	public static FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
	
	public static ArrayList<Tab> tabs = new ArrayList<Tab>();
	public GuiScreen last;
	
	public Tab selectedTab = null;
	public float changedX = 0, changedY = 0, offsetX = 0, offsetY = 0;
	
	public boolean currentlySettingKeybind = false;
	
	public void initGui() {
		currentlySettingKeybind = false;
	}
	
	public void onGuiClosed() {
		info.spicyclient.modules.render.ClickGui c = SpicyClient.config.clickgui;
		c.toggled = false;
	}
	
	public boolean doesGuiPauseGame() {
        return false;
    }
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		Gui.drawRect(0, 0, this.width, this.height, 0x9f000000);
		
		fr = Minecraft.getMinecraft().fontRendererObj;
		
		float maxWidth;
		for (Tab t : this.tabs) {
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(4, 4, 0);
			GlStateManager.scale(1, 1, 1);
			GlStateManager.translate(-4, -4, 0);
			
			maxWidth = fr.getStringWidth(t.getName());
			for (Module m : SpicyClient.modules) {
				if (fr.getStringWidth(m.name) > maxWidth && m.category.equals(t.category)) {
					maxWidth = fr.getStringWidth(m.name);
				}
			}
			
			if (selectedTab == null) {
				
			}
			else if (t.equals(selectedTab)) {
				
				//t.setX(maxWidth - ((mouseX*0.67f) + offsetX - maxWidth));
				//t.setY((mouseY*0.67f));
				//changedX = maxWidth - ((mouseX*0.67f) + offsetX - maxWidth);
				//changedY = ((mouseY*0.67f));
				
				t.setX(mouseX - offsetX);
				t.setY(mouseY - offsetY);
				changedX = (mouseX - offsetX);
				changedY = (mouseY - offsetY);
				
			}
			
			Gui.drawRect(t.x - 2, t.y - 2, t.x + maxWidth + 2, fr.FONT_HEIGHT + t.y + 2, (t.extended) ? 0xff1c1c1c : 0xff2e2e2e);
			Gui.drawRect(t.x - 2, t.y - 2, t.x - 4, fr.FONT_HEIGHT + t.y + 2, accentColor);
			fr.drawString(t.name, t.x, t.y, -1, false);
			
			if (t.extended) {
				
				float modNum = 13;
				
				for (Module m : SpicyClient.modules) {
					
					if (m.category == t.category) {
						
						Gui.drawRect(t.x - 2, (t.y - 2) + modNum, t.x + maxWidth + 2, (fr.FONT_HEIGHT + t.y + 2) + modNum, m.toggled ? 0xb9171717 : 0xbf2e2e2e);
						Gui.drawRect(t.x - 2, (t.y - 2) + modNum, t.x - 4, (fr.FONT_HEIGHT + t.y + 2) + modNum, accentColor);
						fr.drawString(m.name, t.x, t.y + modNum, -1, false);
						
						if (m.ClickGuiExpanded) {
							
							Gui.drawRect((t.x + maxWidth + 2) - 2, (t.y - 1) + modNum, t.x + maxWidth + 1, (fr.FONT_HEIGHT + t.y + 1) + modNum, accentColor);
							
							float maxSettingsWidth = 0;
							for (Setting s : m.settings) {
								
								String temp;
								if (s instanceof BooleanSetting) {
									BooleanSetting b = (BooleanSetting) s;
									temp = b.name + ": false";
								}
								else if (s instanceof NumberSetting) {
									NumberSetting b = (NumberSetting) s;
									temp = b.name + ": " + b.getMaximum();
								}
								else if (s instanceof ModeSetting) {
									
									ModeSetting b = (ModeSetting) s;
									temp = b.name + ": " + b.getMode();
									
									for (String str : b.modes) {
										
										if (fr.getStringWidth(b.name + ": " + str) >= fr.getStringWidth(b.name + ": " + temp)) {
											
											temp = b.name + ": " + str;
											
										}
										
									}
									
								}
								else if (s instanceof KeybindSetting) {
									KeybindSetting b = (KeybindSetting) s;
									temp = b.name + ": " + Keyboard.getKeyName(b.getKeycode());
								}else {
									temp = " ";
								}
								
								if (fr.getStringWidth(temp) > maxSettingsWidth) {
									maxSettingsWidth = fr.getStringWidth(temp);

								}
								
							}
							float settingNum = 0;
							
							for (Setting s : m.settings) {
								
								//Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, m.toggled ? 0xffbfbfbf : 0xffffffff);
								
								if (s instanceof BooleanSetting) {
									BooleanSetting b = (BooleanSetting) s;
									Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, 0xb9171717);
									fr.drawString(b.name + ": " + b.enabled, t.x + maxWidth + 4, ((t.y) + modNum) + settingNum, -1, false);
								}
								else if (s instanceof NumberSetting) {
									NumberSetting b = (NumberSetting) s;
									
									if (b.ClickGuiSelected) {
										
										if (mouseX < (t.x + maxWidth + 2 + maxSettingsWidth) * 1f && mouseX > (t.x + maxWidth + 2) * 1f) {
											float percentage = 0;
											percentage = (mouseX - ((t.x + maxWidth) * 1f) / ((t.x + maxWidth + 2 + maxSettingsWidth + 2) * 1f) - ((t.x + maxWidth) * 1f));
											percentage = percentage / (maxSettingsWidth);
											b.setValue(b.getMaximum() * percentage);
										}
										else if (mouseX > (t.x + maxWidth + 2 + maxSettingsWidth) * 1f) {
											b.setValue(b.getMaximum());
											//System.out.println("Defaulted to the maximum value");
										}
										else if (mouseX < (t.x + maxWidth) * 1f) {
											b.setValue(b.getMinimum());
											//System.out.println("Defaulted to the minimum value");
										}
									}
									
									Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, 0xb9171717);
									Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 - (((t.x + maxWidth + 2) - (t.x + maxWidth + 2 + maxSettingsWidth + 2)) * (b.getValue() / b.getMaximum())), ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, 0xbf525252);
									fr.drawString(b.name + ": " + b.getValue(), t.x + maxWidth + 4, ((t.y) + modNum) + settingNum, -1, false);
								}
								else if (s instanceof ModeSetting) {
									ModeSetting b = (ModeSetting) s;
									Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, 0xb9171717);
									fr.drawString(b.name + ": " + b.getMode(), t.x + maxWidth + 4, ((t.y) + modNum) + settingNum, -1, false);
								}
								else if (s instanceof KeybindSetting) {
									KeybindSetting b = (KeybindSetting) s;
									Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, ((KeybindSetting) s).ClickGuiSelected ? 0xbf525252 : 0xb9171717);
									fr.drawString(b.name + ": " + Keyboard.getKeyName(b.getKeycode()), t.x + maxWidth + 4, ((t.y) + modNum) + settingNum, -1, false);
								}
								
								if (m.settings.indexOf(s) != m.settings.size() - 1) {
									Gui.drawRect(t.x + maxWidth + 2, ((fr.FONT_HEIGHT + t.y + 1) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, accentColor);
								}
								
								settingNum += 13;
								
							}
							
						}
						modNum += 13;
						
					}
					
				}
				
			}
			
			GlStateManager.popMatrix();
			
		}
		
    }
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (currentlySettingKeybind) {
			for (Module m : SpicyClient.modules) {
				for (Setting s : m.settings) {
					if (s instanceof KeybindSetting) {
						KeybindSetting k = (KeybindSetting) s;
						if (k.ClickGuiSelected) {
							if (keyCode == Keyboard.KEY_ESCAPE) {
								k.code = Keyboard.KEY_NONE;
							}else {
								k.code = keyCode;
							}
							k.ClickGuiSelected = false;
							this.currentlySettingKeybind = false;
						}
					}
				}
			}
			currentlySettingKeybind = false;
		}else {
			info.spicyclient.modules.render.ClickGui tempClickGui = SpicyClient.config.clickgui;
			if (keyCode == Keyboard.KEY_ESCAPE || keyCode == tempClickGui.getKey()) {
				mc.displayGuiScreen(this.last);
			}
		}
		
    }
	
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		for (Tab t : this.tabs) {
			
			GlStateManager.pushMatrix();
			GlStateManager.translate(4, 4, 0);
			GlStateManager.scale(1.5, 1.5, 1);
			GlStateManager.translate(-4, -4, 0);
			
			float maxWidth = fr.getStringWidth(t.getName());
			
			for (Module m : SpicyClient.modules) {
				if (fr.getStringWidth(m.name) > maxWidth && m.category.equals(t.category)) {
					maxWidth = fr.getStringWidth(m.name);
				}
			}
			
			float modNum = 13;
			for (Module m : SpicyClient.modules) {
				
				if (m.category == t.category) {
					
					if (mouseX > (t.x - 2) * 1f && mouseX < (t.x + maxWidth + 2) * 1f) {
						if (mouseY > ((t.y - 2) + modNum) * 1f && mouseY < ((fr.FONT_HEIGHT + t.y + 2) + modNum) * 1f){
							
							if (t.extended) {
								if (mouseButton == 1) {
									m.ClickGuiExpanded = !m.ClickGuiExpanded;
								}
								else if (mouseButton == 0) {
									m.toggle();
								}
								
							}
							
						}
						
					}
					
					if (m.ClickGuiExpanded) {
						
						float maxSettingsWidth = 0;
						for (Setting s : m.settings) {
							
							String temp;
							if (s instanceof BooleanSetting) {
								BooleanSetting b = (BooleanSetting) s;
								temp = b.name + ": " + b.enabled;
							}
							else if (s instanceof NumberSetting) {
								NumberSetting b = (NumberSetting) s;
								temp = b.name + ": " + b.getValue();
							}
							else if (s instanceof ModeSetting) {
								ModeSetting b = (ModeSetting) s;
								temp = b.name + ": " + b.getMode();
							}
							else if (s instanceof KeybindSetting) {
								KeybindSetting b = (KeybindSetting) s;
								temp = b.name + ": " + Keyboard.getKeyName(b.getKeycode());
							}else {
								temp = " ";
							}
							
							if (fr.getStringWidth(temp) > maxSettingsWidth) {
								maxSettingsWidth = fr.getStringWidth(temp);

							}
							
						}
						float settingNum = 0;
						for (Setting s : m.settings) {
							
							//Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, m.toggled ? 0xffbfbfbf : 0xffffffff);
							Gui.drawRect(t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum, -1);
							
							if (s instanceof BooleanSetting) {
								BooleanSetting b = (BooleanSetting) s;
								if (mouseX > (t.x + maxWidth + 2) * 1f && mouseX < (t.x + maxWidth + 2 + maxSettingsWidth + 2) * 1f) {
									if (mouseY > (((t.y - 2) + modNum) + settingNum) * 1f && mouseY < (((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum) * 1f) {
										b.toggle();
										return;
									}
								}
							}
							else if (s instanceof NumberSetting) {
								NumberSetting b = (NumberSetting) s;
								if (mouseX > (t.x + maxWidth + 2) * 1f && mouseX < (t.x + maxWidth + 2 + maxSettingsWidth + 2) * 1f) {
									if (mouseY > (((t.y - 2) + modNum) + settingNum) * 1f && mouseY < (((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum) * 1f) {
										b.ClickGuiSelected = true;
										return;
									}
								}
							}
							else if (s instanceof ModeSetting) {
								ModeSetting b = (ModeSetting) s;
								if (mouseX > (t.x + maxWidth + 2) * 1f && mouseX < (t.x + maxWidth + 2 + maxSettingsWidth + 2) * 1f) {
									if (mouseY > (((t.y - 2) + modNum) + settingNum) * 1f && mouseY < (((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum) * 1f) {
										if (mouseButton == 0) {
											b.cycle(false);
										}else {
											b.cycle(true);
										}
										return;
									}
								}
							}
							else if (s instanceof KeybindSetting) {
								KeybindSetting b = (KeybindSetting) s;
								// t.x + maxWidth + 2, ((t.y - 2) + modNum) + settingNum, t.x + maxWidth + 2 + maxSettingsWidth + 2, ((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum
								if (mouseX > (t.x + maxWidth + 2) * 1f && mouseX < (t.x + maxWidth + 2 + maxSettingsWidth + 2) * 1f) {
									if (mouseY > (((t.y - 2) + modNum) + settingNum) * 1f && mouseY < (((fr.FONT_HEIGHT + t.y + 2) + modNum) + settingNum) * 1f) {
										b.ClickGuiSelected = true;
										this.currentlySettingKeybind = true;
										return;
									}
								}
								
							}
							
							settingNum += 13;
							
						}
						
					}
					
					modNum += 13;
				}
				
			}
			
			if (mouseX > (t.x - 4) * 1 && mouseX < (t.x + maxWidth + 2) * 1) {
				if (mouseY > (t.y - 4) * 1 && mouseY < (fr.FONT_HEIGHT + t.y + 2) * 1) {
					
					// Left click is 0, right click is 1 and scrollwheel click is 2
					if (mouseButton == 1) {
						t.extended = !t.extended;
						for (Module m : SpicyClient.modules) {
							if (m.category == t.category) {
								m.ClickGuiExpanded = false;
							}
						}
					}
					else if (mouseButton == 0) {
						
						offsetX = mouseX - t.getX();
						offsetY = mouseY - t.getY();
						selectedTab = t;
						
					}
					
				}
			}
			
			GlStateManager.popMatrix();
				
		}
		
    }
	
	protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		
		for (Module m : SpicyClient.modules) {
			for (Setting s : m.settings) {
				if (s instanceof NumberSetting) {
					NumberSetting n = (NumberSetting) s;
					n.ClickGuiSelected = false;
				}
			}
		}
		
		if (mouseButton == 0) {
			if (selectedTab == null) {
				
			}else {
				this.tabs.get(this.tabs.indexOf(selectedTab)).setX(changedX);
				this.tabs.get(this.tabs.indexOf(selectedTab)).setY(changedY);
				selectedTab = null;
			}
		}
		
    }
	
}
