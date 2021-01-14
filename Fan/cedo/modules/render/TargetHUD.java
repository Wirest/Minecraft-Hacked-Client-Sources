package cedo.modules.render;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.impl.BooleanSetting;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.ui.animations.Direction;
import cedo.ui.animations.impl.SmoothStepAnimation;
import cedo.ui.elements.Draw;
import cedo.util.ColorManager;
import cedo.util.Logger;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import cedo.util.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

@SuppressWarnings("rawtypes")
public class TargetHUD extends Module {
    public ModeSetting size = new ModeSetting("Size", "Compact", "Big", "Compact", "Text", "Script"),
            healthBarPos = new ModeSetting("Health Bar Pos", "Top", "Bottom", "Top"),
    		mode = new ModeSetting("Colors", "Regular", "Regular", "Rainbow", "Chill Rainbow");
    BooleanSetting nameOfPlayer = new BooleanSetting("Name", true),
            healthNumber = new BooleanSetting("Health Number", true),
            armorSetting = new BooleanSetting("Armor", true),
            distanceSetting = new BooleanSetting("Distance", true),
            healthBar = new BooleanSetting("Health Bar", true);
    NumberSetting xValue = new NumberSetting("X", 1, -500, 500, 1);
    NumberSetting yValue = new NumberSetting("Y", 1, -500, 500, 1);
    int width = 0, height = 0, movement = 0, yValueOfArmor = 0, scale = 0, xValueOfArmor = 0, movement5 = 0, indexOfArmor = 0,
            xValueOfHealthNumber = 0, xValueOfDistanceSet = 0, yValueOfHealthNumber = 0,
            yValueOfDistanceSet = 0, xValueOfPlayerName = 0, yValueOfPlayerName = 0, xValueOfHealthBar = 0;

    SmoothStepAnimation barAnimation;

    float movement4 = 0;
    float targetPrevHealth = 0;

    public TargetHUD() {
        super("TargetHUD", Keyboard.KEY_O, Category.RENDER);
        addSettings(size, mode, yValue, xValue, healthBar, healthBarPos, healthNumber, nameOfPlayer, distanceSetting, armorSetting);
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {

        	int rainbowColors =  ColorManager.rainbow(5,20, mode.is("Chill Rainbow") ? 0.5f : 1f, 1, 0.8f).getRGB();
        	
        	Color xd1 = new Color(ColorManager.rainbow(1, 20, mode.is("Chill Rainbow") ? 0.5f : 1f, 1, 0.9f).getRGB());
    		Color xd2 = new Color(ColorManager.rainbow(90, 20, mode.is("Chill Rainbow") ? 0.5f : 1f, 1, 0.9f).getRGB());

            EntityLivingBase target = Fan.killaura.target;
            if(size.is("Script")){
                this.setSuffix("\247cMichealXF");
            }else
                this.setSuffix("");

            if (Fan.killaura.isEnabled() && Fan.killaura.target != null) {

                EventRenderGUI event = (EventRenderGUI) e;
                ScaledResolution sr = event.sr;


                int yValueOfTargetHUD = 219;
                int yValueOfHealthBar = 0;

                MinecraftFontRenderer fr = FontUtil.cleanmedium;
                MinecraftFontRenderer fr2 = FontUtil.cleanmedium;
                xValueOfHealthBar = 430;

                int movementIndex = 0;
                if (nameOfPlayer.isEnabled())
                    movementIndex++;

                if (healthNumber.isEnabled())
                    movementIndex++;

                if (distanceSetting.isEnabled())
                    movementIndex++;


                boolean notText = !size.is("Text");

                switch (size.getSelected()) {
                    case "Compact":
                        width = 100;
                        height = 59;
                        scale = 19;
                        movement = 178;
                        yValueOfArmor = 188;
                        xValueOfArmor = 416;
                        movement4 = 5;
                        movement5 = 415;
                        indexOfArmor = 17;
                        xValueOfHealthNumber = 360;
                        xValueOfDistanceSet = 362;
                        yValueOfHealthNumber = 203;
                        yValueOfDistanceSet = 192;
                        xValueOfPlayerName = 375;
                        yValueOfPlayerName = 213;

                        switch (movementIndex) {
                            case 1:
                                fr2 = FontUtil.cleanlarge;
                                yValueOfHealthNumber = 203;
                                xValueOfDistanceSet = 369;
                                yValueOfPlayerName = 207;
                                xValueOfPlayerName = 380;
                                break;
                            case 2:
                                fr2 = FontUtil.clean;
                                if (!nameOfPlayer.isEnabled())
                                    yValueOfHealthNumber = 210;
                                else
                                    yValueOfHealthNumber = 195;

                                xValueOfPlayerName = 380;
                                xValueOfDistanceSet = 369;
                                break;
                                
                        }

                        if (healthBarPos.is("Top")) {
                            yValueOfHealthBar = 219;
                        } else if (healthBarPos.is("Bottom")) {
                        	if(armorSetting.isEnabled())
                            yValueOfHealthBar = 223 - height;
                        	else
                        		yValueOfHealthBar = 230 - height;
                        }

                        break;

                    case "Big":
                        fr = FontUtil.clean;
                        fr2 = FontUtil.clean;
                        width = 150;
                        height = 80;
                        scale = 33;
                        movement = 147;
                        yValueOfArmor = 169;
                        xValueOfArmor = 385;
                        movement4 = (float) 7.5;
                        movement5 = 405;
                        indexOfArmor = 21;
                        xValueOfHealthNumber = 315;
                        yValueOfHealthNumber = 196;
                        xValueOfDistanceSet = 320;
                        yValueOfDistanceSet = 180;
                        xValueOfPlayerName = 323;
                        yValueOfPlayerName = 211;

                        if (healthBarPos.is("Top")) {
                            yValueOfHealthBar = 219;
                        } else if (healthBarPos.is("Bottom"))
                            yValueOfHealthBar = 143;

                        break;

                    case "Text":
                        yValueOfHealthBar = sr.getScaledHeight() / 2 + 40;
                        xValueOfHealthBar = sr.getScaledWidth() / 2 + 55;
                        movement4 = 5;
                        xValueOfHealthNumber = sr.getScaledWidth() / 2 - 30;
                        yValueOfHealthNumber = sr.getScaledHeight() / 2 + 30;
                        xValueOfDistanceSet = sr.getScaledWidth() / 2 + 35;
                        yValueOfDistanceSet = sr.getScaledHeight() / 2 + 30;
                        break;
                }

                if (!armorSetting.isEnabled())
                    height = 50;

                if (size.is("Text")) {
                    FontUtil.clean.drawCenteredStringWithShadow("" + target.getName(), (float) ((sr.getScaledWidth() / 2) + xValue.getValue()), (float) ((sr.getScaledHeight() / 2 - 17) + yValue.getValue()), -1);

                }

                String colorCode = target.getHealth() > 15 ? "\247a" : target.getHealth() > 10 ? "\247e" : target.getHealth() > 7 ? "\2476" : "\247c";
                
                int colorrectCode = target.getHealth() > 15 ? 0xff4DF75B : target.getHealth() > 10 ? 0xffF1F74D : target.getHealth() > 7 ? 0xffF7854D : 0xffF7524D;
                Color bruhhh = new Color(target.getHealth() > 15 ? 0xff4DF75B : target.getHealth() > 10 ? 0xffF1F74D : target.getHealth() > 7 ? 0xffF7854D : 0xffF7524D);
                int targetHealth = Math.round(target.getHealth());


                float distanceValue = Math.round(mc.thePlayer.getDistanceToEntity(target) * 10) / 10f;

                boolean hasArmor = false;
                if (armorSetting.isEnabled()) {
                    for (int index = 0; index < 5; index++) {

                        if (target.getEquipmentInSlot(index) == null)
                            continue;

                        hasArmor = true;
                    }
                }

                if (!hasArmor)
                    if (size.is("Compact"))
                        height = 50;

                if (notText && !size.is("Script")) {
                    Draw.color(0x9f000000);
                    Draw.drawRoundedRect((sr.getScaledWidth() - 430)  + xValue.getValue(), (sr.getScaledHeight() - yValueOfTargetHUD) + yValue.getValue(), width, height, 2);
                }

                if (armorSetting.isEnabled() & !size.is("Text") & !size.is("Script")) {
                    for (int index = 0; index < 5; index++) {

                        if (target.getEquipmentInSlot(index) == null)
                            continue;

                        hasArmor = true;


                        Nametags.renderItem(target.getEquipmentInSlot(index), (int) (((index * indexOfArmor) + sr.getScaledWidth() - xValueOfArmor) + xValue.getValue()), (int) ((sr.getScaledHeight() - yValueOfArmor) + yValue.getValue()));
                    }
                }


                String targetName = target.getName();


                if (nameOfPlayer.isEnabled() && notText && !size.is("Script")) {
                    fr2.drawStringWithShadow("\247c" + targetName, (sr.getScaledWidth() - xValueOfPlayerName - fr2.getStringWidth(targetName) + 40) + xValue.getValue(), (float) ((sr.getScaledHeight() - yValueOfPlayerName) + yValue.getValue()), -1);
                }
                
                if (healthNumber.isEnabled()&& !size.is("Script")) {
                    fr.drawCenteredStringWithShadow("Health: " + colorCode + targetHealth, (float) ((sr.getScaledWidth() - xValueOfHealthNumber) + xValue.getValue()), (float) ((sr.getScaledHeight() - yValueOfHealthNumber) + yValue.getValue()), -1);
                }
                    
                if (distanceSetting.isEnabled() && !size.is("Script"))
                    fr.drawCenteredStringWithShadow("Distance: " + distanceValue, (float) ((sr.getScaledWidth() - xValueOfDistanceSet) + xValue.getValue()), (float) ((sr.getScaledHeight() - yValueOfDistanceSet) + yValue.getValue()), -1);

                
                int jinneedstodev = 0;
                if(size.is("Text")) {
                	jinneedstodev = 2;
                }
              


                if (healthBar.isEnabled() & !size.is("Script")) {
                    if (size.is("Text")) {
                        Draw.color(0x9f000000);
                        Draw.drawRoundedRect((sr.getScaledWidth() - xValueOfHealthBar) + xValue.getValue(), (sr.getScaledHeight() - yValueOfHealthBar - 1.5) + yValue.getValue(), 102, 7.5, 3);
                    }
                    if(mode.is("Regular")) {
                    Draw.color(colorrectCode);
                    Draw.drawRoundedRect((sr.getScaledWidth() - xValueOfHealthBar + (size.is("Text") ? 1 : 0)) + xValue.getValue(),(sr.getScaledHeight() - yValueOfHealthBar) + yValue.getValue(), target.getHealth() * movement4, 4.5, 2);
                    }else
                    RenderUtil.gradientSideways((sr.getScaledWidth() - xValueOfHealthBar + jinneedstodev) + xValue.getValue(), (sr.getScaledHeight() - yValueOfHealthBar) + yValue.getValue(), (target.getHealth() * movement4) - jinneedstodev, 4, xd1, xd2);

                }
                
                

                

                //The entity on screen
                if (notText && !size.is("Script")) {
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GuiInventory.drawEntityOnScreen((int) ((sr.getScaledWidth() - movement5) + xValue.getValue()), (int) ((sr.getScaledHeight() - movement) + yValue.getValue()), scale, target.rotationYaw, target.rotationPitch, target);
                }
                
                
                
                
                if(size.is("Script")) {
                	String itemHolding = target.getEquipmentInSlot(0) == null ? "Nothing" : target.getEquipmentInSlot(0).getDisplayName();
                	
                	//background
            		Draw.color(new Color(48, 46, 47).getRGB());
            		Draw.drawRoundedRect((sr.getScaledWidth() - 420) + xValue.getValue(), (sr.getScaledHeight() - 200) + yValue.getValue(), 151.5 + FontUtil.cleankindalarge.getStringWidth(itemHolding), 75, 2);
            		
            		 if (healthNumber.isEnabled()) {
                         FontUtil.expandedfont.drawCenteredStringWithShadow(targetHealth + " \247fHP", (float) ((sr.getScaledWidth() - 380) + xValue.getValue()),
                                 (float) ((sr.getScaledHeight() - 165) + yValue.getValue()), mode.is("Regular") ? colorrectCode : rainbowColors);
            		 }
            		
            		if (nameOfPlayer.isEnabled()) {
            			FontUtil.cleankindalarge.drawStringWithShadow(targetName, (float) (sr.getScaledWidth() - 407) + xValue.getValue(), (float) ((sr.getScaledHeight() - 188) + yValue.getValue()), -1);
                    }
            		
            		FontUtil.cleankindalarge.drawStringWithShadow(itemHolding, (sr.getScaledWidth() - 300) + xValue.getValue(), (float) ((sr.getScaledHeight() - 188) + yValue.getValue()), 0xff909090);

            		if (healthBar.isEnabled()) {
                        float healthDiff = targetPrevHealth - target.getHealth();
                        if (barAnimation != null && barAnimation.isDone()) barAnimation = null;
            		    if (healthDiff != 0) { //The moment health changes
                            if (barAnimation == null) {
                                barAnimation = new SmoothStepAnimation(250, healthDiff, Direction.BACKWARDS);
                            }
                        }

            			int healthbarY = healthBarPos.is("Top") ? 200 : 130;

            			

            			if(mode.is("Regular")) {
            				
            				Draw.colorRGBA(bruhhh.getRGB());
                           //Gui.drawRect(0, 0, 0, 0, mode.is("Regular") ? bruhhh.getRGB() : ColorManager.rainbow(-5, 20, mode.is("Chill Rainbow") ? 0.5f : 1f, 1, 1f).getRGB());
                            Draw.drawRoundedRect((sr.getScaledWidth() - 420) + xValue.getValue(), (sr.getScaledHeight() - healthbarY) + yValue.getValue(), (151.5 + FontUtil.cleankindalarge.getStringWidth(itemHolding)) *
                                    ((target.getHealth() + (barAnimation != null ? barAnimation.getOutput() : 0)) / target.getMaxHealth()), 5, 1);
            			} else {
                            RenderUtil.gradientSideways((sr.getScaledWidth() - 420) + xValue.getValue(), (sr.getScaledHeight() - healthbarY) + yValue.getValue(),
                                    (151.5 + FontUtil.cleankindalarge.getStringWidth(itemHolding)) *
                                            ((target.getHealth() + (barAnimation != null ? barAnimation.getOutput() : 0)) / target.getMaxHealth()), 5, xd1, xd2);
                        }
            				
            			//Draw.color(mode.is("Regular") ? colorrectCode : ColorManager.rainbow(-5, 20, mode.is("Chill Rainbow") ? 0.5f : 1f, 1, 0.8f).getRGB());
            			//Gui.drawGradientRect(sr.getScaledWidth() - 410 + target.getHealth() * 10 , sr.getScaledHeight() - 125, 500, 390, xd1, xd2);
            			//Draw.drawRoundedRect(sr.getScaledWidth() - 420, sr.getScaledHeight() - 130, target.getHealth() * 10, 5, 1);
            		}
            		
            		
            	}
                
                

            }
            if (target != null) targetPrevHealth = target.getHealth();
        }
    }
}
