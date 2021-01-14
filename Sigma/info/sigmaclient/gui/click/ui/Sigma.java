package info.sigmaclient.gui.click.ui;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.gui.click.components.CategoryButton;
import info.sigmaclient.gui.click.components.CategoryPanel;
import info.sigmaclient.gui.click.components.Checkbox;
import info.sigmaclient.gui.click.components.ColorPreview;
import info.sigmaclient.gui.click.components.DropdownBox;
import info.sigmaclient.gui.click.components.DropdownButton;
import info.sigmaclient.gui.click.components.MainPanel;
import info.sigmaclient.gui.click.components.RGBSlider;
import info.sigmaclient.gui.click.components.SLButton;
import info.sigmaclient.gui.click.components.Slider;
import info.sigmaclient.management.ColorManager;
import info.sigmaclient.management.ColorObject;
import info.sigmaclient.management.agora.AgoraModule;
import info.sigmaclient.management.agora.AgoraSwitchModule;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.module.impl.movement.Fly;
import info.sigmaclient.module.impl.player.InventoryManager;
import info.sigmaclient.module.impl.render.Animations;
import info.sigmaclient.util.MathUtils;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.ResourceLocation;

public class Sigma extends UI {

    public Expand expand = new Expand(0, 0, 0, 0);
    public Expand setExpand = new Expand(200, 175, 0, 0);
    private Timer bugFixTimer = new Timer();
    @Override
    public void mainConstructor(ClickGui p0, MainPanel panel) {
        float x = 0;
        float y = 0;
        panel.typeButton.add(new CategoryButton(panel, "Combat", x - 45, y - 80));
        panel.typeButton.add(new CategoryButton(panel, "Player", x + 45, y - 80));
        panel.typeButton.add(new CategoryButton(panel, "Movement", x - 45, y));
        panel.typeButton.add(new CategoryButton(panel, "Visuals", x + 45, y));
        panel.typeButton.add(new CategoryButton(panel, "Other", x - 45, y + 80));
        panel.typeButton.add(new CategoryButton(panel, "Colors", x + 45, y + 80));
    }

    @Override
    public void onClose() {
        if (!Client.isLowEndPC)
            g.entityRenderer.theShaderGroup = null;
        expand.setExpandX(0);
        expand.setExpandY(0);
        setExpand.setExpandX(0);
        setExpand.setExpandY(0);
    }

    @Override
    public void mainPanelDraw(MainPanel panel, int p0, int p1) {
        if (expand.getExpandX() == 0) {
            if (panel.currentPanel != null) {
                panel.currentPanel.settingModule = null;
                panel.currentPanel = null;
            }
            if (!Client.isLowEndPC){
            	 g.entityRenderer.func_175069_a(EntityRenderer.shaderResourceLocations[18]);
            }
               
        }
        int speedx = (int) (((panel.currentPanel != null ? 400 : 200) - expand.getExpandX()) * (panel.currentPanel != null ? 0.22 : 0.4));
        if (speedx < 2 && (panel.currentPanel != null ? 400 : 200) - expand.getExpandX() > 0) {
            speedx = 8;
        }
        int speedy = (int) (((panel.currentPanel != null ? 350 : 300) - expand.getExpandY()) * 0.4);
        if (speedy < 3 && (panel.currentPanel != null ? 350 : 300) - expand.getExpandY() > 0) {
            speedy = panel.currentPanel != null ? 6 : 12;
        }
        if (panel.currentPanel != null) {
            expand.interpolate(300, 400, speedx, speedy);
        } else {
            expand.interpolate(200, 300, speedx, speedy);
        }
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        int s = res.getScaleFactor();
        glPushMatrix();
        glScissor((int) (x - expand.getExpandX() / 2) * s, (int) (y - expand.getExpandY() / 2) * s, (int) (expand.getExpandX()) * s, (int) (expand.getExpandY()) * s);
        glEnable(GL_SCISSOR_TEST);

        if (panel.currentPanel == null) {
            RenderingUtil.drawRoundedRect(x - 99, y - 149, x + 99, y + 149, Colors.getColor(232, 220), Colors.getColor(232, 220));
            TTFFontRenderer font = Client.fm.getFont("SFR 14");
            font.drawCenteredString("Sigma", x, y - 140, Colors.getColor(45));
            for (CategoryButton button : panel.typeButton) {
                button.draw(p0, p1);
            }
        }
        if (panel.currentPanel != null) {
            panel.currentPanel.draw(p0, p1);
            boolean h = p0 >= x + 125 && p0 <= x + 125 + 16 && p1 >= y - 165 && p1 <= y - 165 + 16;
            g.fontRendererObj.drawStringWithShadow("", 300, 300, -1);
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            glScissor((int) (x - expand.getExpandX() / 2) * s, (int) (y - expand.getExpandY() / 2) * s, (int) (expand.getExpandX()) * s, (int) (expand.getExpandY()) * s);
            glEnable(GL_SCISSOR_TEST);
            g.getTextureManager().bindTexture(h ? xmark2 : xmark);
            drawModalRectWithCustomSizedTexture(x + 125, y - 165, 0, 0, 16, 16, 16, 16);
            glDisable(GL_SCISSOR_TEST);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        } else {
            boolean h = p0 >= x + 78 && p0 <= x + 78 + 16 && p1 >= y - 145 && p1 <= y - 145 + 16;
            GlStateManager.enableBlend();
            g.getTextureManager().bindTexture(h ? xmark2 : xmark);
            drawModalRectWithCustomSizedTexture(x + 78, y - 145, 0, 0, 16, 16, 16, 16);
            GlStateManager.disableBlend();
        }
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
    }

    @Override
    public void mainPanelKeyPress(MainPanel panel, int key) {

    }

    @Override
    public void panelConstructor(MainPanel mainPanel, float x, float y) {

    }

    @Override
    public void panelMouseClicked(MainPanel mainPanel, int p1, int p2, int p3) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        boolean h;
        if (mainPanel.currentPanel != null) {
            h = p1 >= x + 125 && p1 <= x + 125 + 16 && p2 >= y - 165 && p2 <= y - 165 + 16;
        } else {
            h = p1 >= x + 78 && p1 <= x + 78 + 16 && p2 >= y - 145 && p2 <= y - 145 + 16;
        }
        if (h && p3 == 0) {
            if (mainPanel.currentPanel == null) {
                g.displayGuiScreen(null);
                expand.setExpandX(0);
                expand.setExpandY(0);
            } else if (mainPanel.currentPanel.settingModule == null) {
                expand.setExpandX(0);
                expand.setExpandY(0);
                mainPanel.currentPanel.visible = false;
                mainPanel.currentPanel = null;
            } else {
                mainPanel.currentPanel.settingModule = null;
            }
        }
        if (mainPanel.currentPanel == null) {
            for (CategoryButton button : mainPanel.typeButton) {
                button.mouseClicked(p1, p2, p3);
            }
        } else {
            mainPanel.currentPanel.mouseClicked(p1, p2, p3);
        }
    }

    @Override
    public void panelMouseMovedOrUp(MainPanel mainPanel, int p1, int p2, int p3) {
        for (CategoryButton button : mainPanel.typeButton) {
            button.mouseReleased(p1, p2, p3);
        }
    }

    @Override
    public void categoryButtonConstructor(CategoryButton p0, MainPanel p1) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        p0.categoryPanel = new CategoryPanel(p0.name, p0, x, y);
    }

    @Override
    public void categoryButtonMouseClicked(CategoryButton p0, MainPanel p1, int p2, int p3, int p4) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        boolean h = p2 >= p0.x + x - 28 && p3 >= p0.y + y - 36 && p2 <= p0.x + x + 28 && p3 <= p0.y + y + 10;
        if (h && p4 == 0) {
            expand.setExpandX(1);
            expand.setExpandY(1);
            p1.currentPanel = p0.categoryPanel;
            p0.categoryPanel.visible = true;
        }
        p0.categoryPanel.mouseClicked(p2, p3, p4);
    }


    @Override
    public void categoryButtonDraw(CategoryButton p0, float p2, float p3) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        boolean h = p2 >= p0.x + x - 28 && p3 >= p0.y + y - 36 && p2 <= p0.x + x + 28 && p3 <= p0.y + y + 10;
        int color = !h ? Colors.getColor(32) : Colors.getColor(232, 60, 40);

        Client.fm.getFont("SFM 12").drawCenteredString(p0.name, p0.x + x, p0.y + y + 2, color);
        GlStateManager.enableBlend();
        switch (p0.name) {
            case "Combat":
                g.getTextureManager().bindTexture(h ? combat2 : combat);
                drawModalRectWithCustomSizedTexture(p0.x + x - 14, p0.y + y - 36, 0, 0, 32, 32, 32, 32);
                break;
            case "Movement":
                g.getTextureManager().bindTexture(h ? movement2 : movement);
                drawModalRectWithCustomSizedTexture(p0.x + x - 14, p0.y + y - 36, 0, 0, 32, 32, 32, 32);
                break;
            case "Player":
                g.getTextureManager().bindTexture(h ? player2 : player);
                drawModalRectWithCustomSizedTexture(p0.x + x - 16, p0.y + y - 36, 0, -5, 32, 32, 32, 32);
                break;
            case "Visuals":
                g.getTextureManager().bindTexture(h ? visuals2 : visuals);
                drawModalRectWithCustomSizedTexture(p0.x + x - 16, p0.y + y - 36, 0, -4, 32, 32, 32, 32);
                break;
            case "Colors":
                g.getTextureManager().bindTexture(h ? colors2 : colors);
                drawModalRectWithCustomSizedTexture(p0.x + x - 16, p0.y + y - 36, 0, 0, 32, 32, 32, 32);
                break;
            case "Other":
                g.getTextureManager().bindTexture(h ? others2 : others);
                drawModalRectWithCustomSizedTexture(p0.x + x - 16, p0.y + y - 36, 0, 0, 32, 32, 32, 32);
                break;

        }
        GlStateManager.disableBlend();
    }

    private List<Setting> getSettings(Module mod) {
        List<Setting> settings = new CopyOnWriteArrayList<>();
        settings.addAll(mod.getSettings().values());
        for (Setting setting : settings) {
            if (setting.getValue().getClass().equals(String.class)) {
                settings.remove(setting);
            }
        }
        if (settings.isEmpty()) {
            return null;
        }
        settings.sort(Comparator.comparing(Setting::getName));
        return settings;
    }

    private List<Module> getModules(ModuleData.Type type) {
        List<Module> modulesInType = new ArrayList<>();
        for (Module mod : Client.getModuleManager().getArray()) {
            if (mod.getType() == type && !(mod instanceof info.sigmaclient.module.impl.other.ClickGui) &&
            		!(mod instanceof info.sigmaclient.module.impl.other.Ranking) && !(mod instanceof AgoraModule)&& !(mod instanceof AgoraSwitchModule)) {
                modulesInType.add(mod);
            }
        }
        if (modulesInType.isEmpty()) {
            return null;
        }
       
        modulesInType.sort(Comparator.comparing(Module::getName));
        return modulesInType;
    }

    @Override
    public void categoryPanelConstructor(CategoryPanel categoryPanel, CategoryButton categoryButton, float x, float y) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        int yOff = 0;
        if (categoryButton.name.equalsIgnoreCase("Combat")) {
            int rowCount = 0;
            for (Module module : getModules(ModuleData.Type.Combat)) {
                categoryPanel.buttons.add(new info.sigmaclient.gui.click.components.Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                rowCount++;
                if (rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                int meme = 0;
                if (getSettings(module) != null) {
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                            categoryPanel.checkboxes.add(new info.sigmaclient.gui.click.components.Checkbox(categoryPanel, setting.getName(), 0, meme, setting, module));
                            meme += 14;
                        }
                    }
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Number) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, 0, meme, setting, module));
                            meme += 22;
                        }
                    }
                    List<Options> optionsList = new ArrayList<>();
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Options) {
                            optionsList.add((Options) setting.getValue());
                        }
                    }
                    if (!optionsList.isEmpty()) {
                        //meme += (optionsList.size() - 1) * 25;
                        int xmeme = 0;
                        for (Options option : optionsList) {
                        	if(xmeme >= 5){
                        		meme += 25;
                        		xmeme = 0;
                        	}
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme*50, meme, categoryPanel, module)); 
                        	xmeme += 1;
                            
                        }
                    }
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Movement")) {
            int rowCount = 0;
            for (Module module : getModules(ModuleData.Type.Movement)) {
                categoryPanel.buttons.add(new info.sigmaclient.gui.click.components.Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                rowCount++;
                if (rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                int meme = 0;
                if (getSettings(module) != null) {
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                        	categoryPanel.checkboxes.add(new info.sigmaclient.gui.click.components.Checkbox(categoryPanel, setting.getName(), 0, meme, setting, module));
                        	
                           
                            meme += 14;
                        }
                    }
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Number) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, 0, meme, setting, module));
                            meme += 22;
                        }
                    }
                    List<Options> optionsList = new ArrayList<>();
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Options) {
                            optionsList.add((Options) setting.getValue());
                        }
                    }
                    if (!optionsList.isEmpty()) {
                        //meme += (optionsList.size() - 1) * 25;
                        int xmeme = 0;
                        for (Options option : optionsList) {
                        	if(xmeme >= 5){
                        		meme += 25;
                        		xmeme = 0;
                        	}
                        	if(module instanceof Fly){
                        		categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme*70, meme, categoryPanel, module)); 	
                        	}else{
                        		categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme*50, meme, categoryPanel, module)); 
                        	}
                           
                        	xmeme += 1;
                            
                        }
                    }
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Player")) {
            int rowCount = 0;
            for (Module module : getModules(ModuleData.Type.Player)) {
                categoryPanel.buttons.add(new info.sigmaclient.gui.click.components.Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                rowCount++;
                if (rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                int meme = 0;
                if (getSettings(module) != null) {
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                            categoryPanel.checkboxes.add(new info.sigmaclient.gui.click.components.Checkbox(categoryPanel, setting.getName(), 0, meme, setting, module));
                            meme += 14;
                        }
                    }
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Number) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, 0, meme, setting, module));
                            meme += 22;
                        }
                    }
                    List<Options> optionsList = new ArrayList<>();
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Options) {
                            optionsList.add((Options) setting.getValue());
                        }
                    }
                    if (!optionsList.isEmpty()) {
                        //meme += (optionsList.size() - 1) * 25;
                        int xmeme = 0;
                        for (Options option : optionsList) {
                        	if(xmeme >= 5){
                        		meme += 25;
                        		xmeme = 0;
                        	}
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme*50, meme, categoryPanel, module)); 
                        	xmeme += 1;
                            
                        }
                    }
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Visuals")) {
            int rowCount = 0;
            for (Module module : getModules(ModuleData.Type.Visuals)) {
                categoryPanel.buttons.add(new info.sigmaclient.gui.click.components.Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                rowCount++;
                if (rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                int meme = 0;
                if (getSettings(module) != null) {
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                            categoryPanel.checkboxes.add(new info.sigmaclient.gui.click.components.Checkbox(categoryPanel, setting.getName(), 0, meme, setting, module));
                            meme += 14;
                        }
                    }
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Number) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, 0, meme, setting, module));
                            meme += 22;
                        }
                    }
                    List<Options> optionsList = new ArrayList<>();
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Options) {
                            optionsList.add((Options) setting.getValue());
                        }
                    }
                    if (!optionsList.isEmpty()) {
                       // meme += (optionsList.size() - 1) * 25;
                        int xmeme = 0;
                        for (Options option : optionsList) {
                        	if(xmeme >= 250){
                        		meme += 25;
                        		xmeme = 0;
                        	}
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme, meme, categoryPanel, module));
                            //TODO:BUG FIXED (BAD)
                            if(module instanceof Animations){
                            	xmeme += 90;
                            }else{
                            	xmeme += 50;
                            }
                            
                        }
                    }
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Other")) {
            int rowCount = 0;
            for (Module module : getModules(ModuleData.Type.Other)) {
                categoryPanel.buttons.add(new info.sigmaclient.gui.click.components.Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                rowCount++;
                if (rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                int meme = 0;
                if (getSettings(module) != null) {
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                            categoryPanel.checkboxes.add(new info.sigmaclient.gui.click.components.Checkbox(categoryPanel, setting.getName(), 0, meme, setting, module));
                            meme += 14;
                        }
                    }
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Number) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, 0, meme, setting, module));
                            meme += 22;
                        }
                    }
                    List<Options> optionsList = new ArrayList<>();
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Options) {
                            optionsList.add((Options) setting.getValue());
                        }
                    }
                    if (!optionsList.isEmpty()) {
                        //meme += (optionsList.size() - 1) * 25;
                        int xmeme = 0;
                        for (Options option : optionsList) {
                        	if(xmeme >= 5){
                        		meme += 25;
                        		xmeme = 0;
                        	}
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme*50, meme, categoryPanel, module));
                        	xmeme += 1;
                            
                        }
                    }
                }
            }
            for (Module module : getModules(ModuleData.Type.MiniGames)) {
                categoryPanel.buttons.add(new info.sigmaclient.gui.click.components.Button(categoryPanel, module.getName(), 85 * rowCount, yOff, module));
                rowCount++;
                if (rowCount == 3) {
                    yOff += 42;
                    rowCount = 0;
                }
                int meme = 0;
                if (getSettings(module) != null) {
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue().getClass().equals(Boolean.class)) {
                            categoryPanel.checkboxes.add(new info.sigmaclient.gui.click.components.Checkbox(categoryPanel, setting.getName(), 0, meme, setting, module));
                            meme += 14;
                        }
                    }
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Number) {
                            categoryPanel.sliders.add(new Slider(categoryPanel, 0, meme, setting, module));
                            meme += 22;
                        }
                    }
                    List<Options> optionsList = new ArrayList<>();
                    for (Setting setting : getSettings(module)) {
                        if (setting.getValue() instanceof Options) {
                            optionsList.add((Options) setting.getValue());
                        }
                    }
                    if (!optionsList.isEmpty()) {
                        //meme += (optionsList.size() - 1) * 25;
                        int xmeme = 0;
                        for (Options option : optionsList) {
                        	if(xmeme >= 5){
                        		meme += 25;
                        		xmeme = 0;
                        	}
                            categoryPanel.dropdownBoxes.add(new DropdownBox(option, xmeme*50, meme, categoryPanel, module));
                        	xmeme += 1;
                            
                        }
                    }
                }
            }
        }
        if (categoryButton.name.equalsIgnoreCase("Colors")) {
            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getHudColor(), "Hud Color", 10, 0, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getXhairColor(), "X-Hair Color", 135, 0, categoryButton));
            categoryPanel.colorPreviews.add(new ColorPreview(Client.cm.getESPColor(), "ESP Color", 10, 70, categoryButton));
        }

    }

    @Override
    public void categoryPanelMouseClicked(CategoryPanel categoryPanel, int p1, int p2, int p3) {
        if (expand.getExpandX() > 150 && categoryPanel.categoryButton.panel.currentPanel == categoryPanel) {
            for (info.sigmaclient.gui.click.components.Button button : categoryPanel.buttons) {
                button.mouseClicked(p1, p2, p3);
            }
            //TODO bugfix
            if (categoryPanel.settingModule != null && bugFixTimer.delay(10)) {
                for (info.sigmaclient.gui.click.components.Checkbox checkbox : categoryPanel.checkboxes) {
                    if (checkbox.module == categoryPanel.settingModule)
                        checkbox.mouseClicked(p1, p2, p3);
                }
                for (Slider slider : categoryPanel.sliders) {
                    if (slider.module == categoryPanel.settingModule)
                        slider.mouseClicked(p1, p2, p3);
                }
                boolean active = false;
                for (DropdownBox db : categoryPanel.dropdownBoxes) {
                    if (db.active) {
                        db.mouseClicked(p1, p2, p3);
                        active = true;
                        break;
                    }
                }
                if (!active) {
                    for (DropdownBox db : categoryPanel.dropdownBoxes) {
                        db.mouseClicked(p1, p2, p3);
                    }
                }
            }
            for (ColorPreview colorPreview : categoryPanel.colorPreviews) {
                for (RGBSlider slidr : colorPreview.sliders) {
                    slidr.mouseClicked(p1, p2, p3);
                }
            }
        }
    }

    @Override
    public void categoryPanelDraw(CategoryPanel categoryPanel, float x, float y) {

        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2;
        float y1 = res.getScaledHeight() / 2;
        RenderingUtil.drawRoundedRect(x1 - 148, y1 - 173, x1 + 148, y1 + 173, Colors.getColor(232, 220), Colors.getColor(232, 220));
        Client.fm.getFont("SFR 12").drawCenteredString(categoryPanel.headerString, x1, y1 - 160, Colors.getColor(0));
        if (categoryPanel.settingModule != null) {
            setExpand.interpolate(setExpand.getX(), 310, 0, 25);
        } else {
            setExpand.interpolate(setExpand.getX(), 0, 0, 32);
        }
        for (ColorPreview colorPreview : categoryPanel.colorPreviews) {
            colorPreview.draw(x, y);
        }
        for (info.sigmaclient.gui.click.components.Button button : categoryPanel.buttons) {
            button.draw(x, y);
        }

        int s = res.getScaleFactor();
        glPushMatrix();
        glScissor((int) (x1 - 146) * s, (int) (y1 - 170) * s, 292 * s, (int) (setExpand.getExpandY()) * s);
        glEnable(GL_SCISSOR_TEST);
        RenderingUtil.drawRoundedRect((x1 - 145), y1 - 138, (x1 + 145), y1 + 170, Colors.getColor(215), Colors.getColor(215));
        for (Slider slider : categoryPanel.sliders) {
            if (slider.module == categoryPanel.settingModule)
                slider.draw(x, y);
        }
        for (DropdownBox dropdownBox : categoryPanel.dropdownBoxes) {
            if (dropdownBox.module == categoryPanel.settingModule)
                dropdownBox.draw(x, y);
        }
        for (Checkbox checkbox : categoryPanel.checkboxes) {
            if (checkbox.module == categoryPanel.settingModule){
            	checkbox.draw(x, y);
            }
             
        }
        if (categoryPanel.settingModule != null) {
            Client.fm.getFont("SFR 10").drawString(categoryPanel.settingModule.getName() + " Settings", x1 - 140, y1 - 133, Colors.getColor(32));
        }
        if(categoryPanel.settingModule instanceof InventoryManager){
        	Client.fm.getFont("SFR 10").drawString("Change settings in your inventory", x1 - 40, y1 + 5, Colors.getColor(32));
        }
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();

    }

    @Override
    public void categoryPanelMouseMovedOrUp(CategoryPanel categoryPanel, int x, int y, int button) {
        if (categoryPanel.settingModule != null) {
            for (Slider slider : categoryPanel.sliders) {
                if (slider.module == categoryPanel.settingModule)
                    slider.mouseReleased(x, y, button);
            }
        }
        for (ColorPreview colorPreview : categoryPanel.colorPreviews) {
            for (RGBSlider slidr : colorPreview.sliders) {
                slidr.mouseReleased(x, y, button);
            }
        }
    }

    @Override
    public void buttonContructor(info.sigmaclient.gui.click.components.Button p0, CategoryPanel panel) {

    }

    @Override
    public void buttonMouseClicked(info.sigmaclient.gui.click.components.Button p0, int p2, int p3, int p4, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 120 + p0.x;
        float y1 = res.getScaledHeight() / 2 - 130;
        boolean hovered = p2 >= x1 + 51 && p3 >= y1 + p0.y - 4 && p2 <= x1 + 71 && p3 <= y1 + p0.y + 5;
        boolean setHovered = p2 >= x1 + 60 && p3 >= y1 + p0.y + 10.5f && p2 <= x1 + 70 && p3 <= y1 + p0.y + 20.5f;
        if (panel.settingModule == null) {
            if (hovered && p4 == 0) {
                p0.module.toggle();
            } else if (setHovered && p4 == 0 && getSettings(p0.module) != null) {
            	bugFixTimer.reset();
                panel.settingModule = p0.module;
            } else if (p4 == 1) {
                panel.settingModule = null;
            }
        }
    }

    private ResourceLocation gear = new ResourceLocation("textures/gear.png");
    //Hovered icons
    private ResourceLocation gear2 = new ResourceLocation("textures/gear2.png");

    @Override
    public void buttonDraw(info.sigmaclient.gui.click.components.Button p0, float p2, float p3, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 120 + p0.x;
        float y1 = res.getScaledHeight() / 2 - 130;
        RenderingUtil.rectangleBordered(x1 + 51, y1 + p0.y - 4, x1 + 71, y1 + p0.y + 5, 1, Colors.getColor(140), Colors.getColor(165));
        if (p0.module.isEnabled()) {
            RenderingUtil.rectangle(x1 + 55, y1 + p0.y - 2, x1 + 56, y1 + p0.y + 3, Colors.getColor(50, 200, 65));
        } else {
            RenderingUtil.drawCircle(x1 + 66, y1 + p0.y + 0.6f, 2.3f, 64, Colors.getColor(200, 50, 65));
            RenderingUtil.drawCircle(0, 0, 0, 3, Colors.getColor(165));
        }
        p0.translate.interpolate(p0.module.isEnabled() ? 10 : 0, 0, 2);
        float offset = p0.translate.getX();
        RenderingUtil.rectangle(x1 + 53 + offset, y1 + p0.y - 2, x1 + 59 + offset, y1 + p0.y + 3, Colors.getColor(165));
        Client.fm.getFont("SFM 8").drawString(p0.name, x1, y1 + p0.y - 1, p0.module.isEnabled() ? Colors.getColor(32) : Colors.getColor(110));
        String meme = !p0.module.getKeybind().getKeyStr().equalsIgnoreCase("None") ? p0.module.getKeybind().getKeyStr() : "None";
        Client.fm.getFont("SFM 7").drawString("Bind", x1 + 2, y1 + p0.y + 11, Colors.getColor(32));

        Client.fm.getFont("SFR 7").drawString(meme, x1 + 2, y1 + p0.y + 20, Colors.getColor(32));

        if (getSettings(p0.module) != null) {
            Client.fm.getFont("SFM 6").drawString("Settings", x1 + 36, y1 + p0.y + 12, Colors.getColor(32));
            GlStateManager.pushMatrix();
            GlStateManager.bindTexture(0);
            GlStateManager.enableBlend();
            boolean h = p2 >= x1 + 60 && p3 >= y1 + p0.y + 10.5f && p2 <= x1 + 70 && p3 <= y1 + p0.y + 20.5f;
            p0.rotate.interpolate(h ? 360 : 0, (h ? 4 : 5));
            if (p0.rotate.getAngle() >= 360)
                p0.rotate.setAngle(0);
            GlStateManager.translate(x1 + 65, y1 + p0.y + 12.5f, 0);
            GlStateManager.rotate(p0.rotate.getAngle(), 0, 0, 1);
            g.getTextureManager().bindTexture(h ? gear2 : gear);
            drawModalRectWithCustomSizedTexture(-5, -5, 0, 0, 10, 10, 10, 10);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        boolean hovered = p2 >= x1 && p3 >= y1 + p0.y - 1 && p2 <= x1 + 66 && p3 <= y1 + p0.y + 20;
        if (hovered) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            boolean h = p2 >= x1 + 60 && p3 >= y1 + p0.y + 10.5f && p2 <= x1 + 70 && p3 <= y1 + p0.y + 20.5f;
            GlStateManager.translate(x1 - p0.x - 20, y1 + 296.5, 0);
            GlStateManager.rotate(90f, 0, 0, 1);
            g.getTextureManager().bindTexture(uparrow);
            drawModalRectWithCustomSizedTexture(-5, -5, 0, 0, 10, 10, 10, 10);
            GlStateManager.popMatrix();
            Client.fm.getFont("SFM 8").drawString(p0.module.getDescription(), x1 - p0.x - 15, y1 + 295, Colors.getColor(32));
        }

    }

    @Override
    public void buttonKeyPressed(info.sigmaclient.gui.click.components.Button button, int key) {

    }

    @Override
    public void checkBoxMouseClicked(info.sigmaclient.gui.click.components.Checkbox p0, int p2, int p3, int p4, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 135;
        float y1 = res.getScaledHeight() / 2 - 120 + p0.y;
        if (p0.panel.settingModule == p0.module) {
            boolean hovered = p2 >= x1 + 61 && p3 >= y1 - 1 && p2 <= x1 + 81 && p3 <= y1 + 8;
            if (hovered && p4 == 0) {
                boolean xd = ((Boolean) p0.setting.getValue());
                p0.setting.setValue(!xd);
                Module.saveSettings();
            }
        }
    }

    @Override
    public void checkBoxDraw(info.sigmaclient.gui.click.components.Checkbox p0, float p2, float p3, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 135;
        float y1 = res.getScaledHeight() / 2 - 120 + p0.y;
        boolean enabled = p0.enabled = (Boolean) p0.setting.getValue();
        RenderingUtil.rectangle(0, 0, 0, 0, 0);

        RenderingUtil.rectangleBordered(x1 + 61, y1 - 1, x1 + 81, y1 + 8, 1, Colors.getColor(140), Colors.getColor(165));
        if (enabled) {
            RenderingUtil.rectangle(x1 + 65, y1 + 1, x1 + 66, y1 + 6, Colors.getColor(50, 200, 65));
        } else {
            RenderingUtil.drawCircle(x1 + 76, y1 + 3.5f, 2f, 64, Colors.getColor(200, 50, 65));
        }
        p0.translate.interpolate(enabled ? 10 : 0, 0, 2);
        float offset = p0.translate.getX();
        RenderingUtil.rectangle(x1 + 63 + offset, y1 + 1, x1 + 69 + offset, y1 + 6, Colors.getColor(165));
        String xd = p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1);
        if(p0.name.equalsIgnoreCase("SameY"))
        	xd = "SameY";
        
        Client.fm.getFont("SFR 10").drawString(xd, x1 + 8, y1, Colors.getColor(32));
        RenderingUtil.rectangle(0, 0, 0, 0, 0);
        RenderingUtil.drawRoundedRect(x1 + 90, y1 - 1, x1 + 265, y1 + 8, 0, Colors.getColor(200));
        Client.fm.getFont("SFR 8").drawString(p0.setting.getDesc(), x1 + 92, y1 + 2, Colors.getColor(32));
    }

    @Override
    public void dropDownContructor(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
        int y = 10;
        for (String value : p0.option.getOptions()) {
            p0.buttons.add(new DropdownButton(value, p2, p3 + y, p0));
            y += 8;
        }
    }

    @Override
    public void dropDownMouseClicked(DropdownBox dropDown, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = res.getScaledHeight() / 2 - 120 + dropDown.y + 10;
        for (DropdownButton db : dropDown.buttons) {
            if (dropDown.module == panel.settingModule && dropDown.active) {
                db.mouseClicked(mouseX, mouseY, mouse);
            }
        }
        boolean hovering = (mouseX >= dropDown.x + xOff - 0.3) && (mouseY >= yOff - 0.3) && (mouseX <= dropDown.x + xOff + 40 + 0.3) && (mouseY <= yOff + 9 + 0.3);
        if (hovering && (mouse == 0)) {
            dropDown.active = (!dropDown.active);
        } else {
            dropDown.active = false;
        }
    }

    @Override
    public void dropDownDraw(DropdownBox p0, float p2, float p3, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = res.getScaledHeight() / 2 - 120 + p0.y + 10;
        boolean hovering = (p2 >= p0.x + xOff - 0.3) && (p3 >= yOff - 0.3) && (p2 <= p0.x + xOff + 40 + 0.3) && (p3 <= yOff + 9 + 0.3);

        RenderingUtil.rectangle(p0.x + xOff - 0.3, yOff - 0.3, p0.x + xOff + 40 + 0.3, yOff + 9 + 0.3, Colors.getColor(10));

        RenderingUtil.drawGradient(p0.x + xOff, yOff, p0.x + xOff + 40, yOff + 9, Colors.getColor(31), Colors.getColor(36));
        if (hovering) {
           RenderingUtil.rectangleBordered(p0.x + xOff , yOff, p0.x + 40.3 + xOff, yOff + 9, 0.3, Colors.getColor(0, 0), Colors.getColor(90));
        }
        Client.fm.getFont("SFR 10").drawString(p0.option.getName(), (xOff + p0.x - 1), (-10 + yOff), Colors.getColor(32));
        GlStateManager.pushMatrix();
        GlStateManager.translate((p0.x + xOff + 38 - (p0.active ? 2.5 : 0)), (4 + yOff), 0);
        GlStateManager.rotate(p0.active ? 270 : 90, 0, 0, 90);

        RenderingUtil.rectangle(0 - 1, 0, 0.5 - 1, 2.5, Colors.getColor(0));
        RenderingUtil.rectangle(0.5 - 1, 0, 1 - 1, 2.5, Colors.getColor(151));
        RenderingUtil.rectangle(1 - 1, 0.5, 1.5 - 1, 2, Colors.getColor(151));
        RenderingUtil.rectangle(1.5 - 1, 1, 2 - 1, 1.5, Colors.getColor(151));

        GlStateManager.popMatrix();
        Client.fm.getFont("SFR 8").drawString(p0.option.getSelected(), (p0.x + 4 + xOff) - 1, (3f + yOff), Colors.getColor(151));
        if (p0.active) {
            int i = p0.buttons.size();
            RenderingUtil.rectangle(p0.x + xOff - 0.3, 10 + yOff - 0.3, p0.x + xOff + 40 + 0.3, yOff + 9 + (9 * i) + 0.3, Colors.getColor(10));
            RenderingUtil.drawGradient(p0.x + xOff, yOff + 10, p0.x + xOff + 40, yOff + 9 + (9 * i), Colors.getColor(31), Colors.getColor(36));
            for (DropdownButton button : p0.buttons) {
                button.draw(p2, p3);
            }
        }
    }


    @Override
    public void dropDownButtonMouseClicked(DropdownButton p0, DropdownBox p1, int x, int y, int mouse) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = res.getScaledHeight() / 2 - 120 + p0.y + 10;
        boolean hovering = (x >= p0.x + xOff - 0.3) && (y >= yOff - 0.3) && (x <= p0.x + xOff + 40 + 0.3) && (y <= yOff + 9 + 0.3);
        if ((hovering) && (mouse == 0)) {
            p1.option.setSelected(p0.name);
            p1.active = false;
        }
    }

    @Override
    public void dropDownButtonDraw(DropdownButton p0, DropdownBox p1, float x, float y) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = res.getScaledHeight() / 2 - 120 + p0.y + 10;
        boolean hovering = (x >= xOff + p0.x) && (y >= yOff) && (x <= xOff + p0.x + 40) && (y <= yOff + 8.5);
        GlStateManager.pushMatrix();
        Client.fm.getFont("SFR 6").drawStringWithShadow(p0.name, (p0.x + 3 + xOff), (3 + yOff), hovering ? Colors.getColor(ColorManager.hudColor.red, ColorManager.hudColor.green, ColorManager.hudColor.blue, 255) : -1);
        GlStateManager.scale(1, 1, 1);
        GlStateManager.popMatrix();
    }

    @Override
    public void SliderContructor(Slider p0, CategoryPanel panel) {
        double percent = (((Number) p0.setting.getValue()).doubleValue() - p0.setting.getMin()) / (p0.setting.getMax() - p0.setting.getMin());
        p0.dragX = 120 * percent;
        p0.dragging = false;
    }

    @Override
    public void SliderMouseClicked(Slider slider, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = res.getScaledHeight() / 2 - 120 + slider.y + 12;
        if (panel.visible && mouseX >= xOff && mouseY >= yOff && mouseX <= xOff + 120.0f && mouseY <= yOff + 2.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = mouseX - slider.dragX;
        }
    }

    @Override
    public void SliderMouseMovedOrUp(Slider p0, int mouseX, int mouseY, int mouse, CategoryPanel panel) {
        if (mouse == 0) {
            Module.saveSettings();
            p0.dragging = false;
        }

    }

    private String getValue(double value) {
        return MathUtils.isInteger(value) ? (int) value + "" : value + "";
    }

    @Override
    public void SliderDraw(Slider p0, float p2, float p3, CategoryPanel panel) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 135;
        float y1 = res.getScaledHeight() / 2 - 120 + p0.y;

        String xd = p0.setting.getName().charAt(0) + p0.setting.getName().toLowerCase().substring(1) + ": " + p0.setting.getValue();

        Client.fm.getFont("SFR 10").drawString(xd, x1 + 8, y1, Colors.getColor(32));

        RenderingUtil.rectangle(0, 0, 0, 0, 0);

        RenderingUtil.drawRoundedRect(x1 + 90, y1 - 1, x1 + 265, y1 + 8, 0, Colors.getColor(200));

        Client.fm.getFont("SFR 8").drawString(p0.setting.getDesc(), x1 + 92, y1 + 2, Colors.getColor(32));

        RenderingUtil.rectangle(0, 0, 0, 0, 0);

        final double percent = p0.dragX / 120;

        final double value = MathUtils.getIncremental((percent * 100) * (p0.setting.getMax() - p0.setting.getMin()) / 100 + p0.setting.getMin(), p0.setting.getInc());

        float sliderX = (float) ((percent) * 120);

        float xOff = res.getScaledWidth() / 2 - 125;
        float yOff = res.getScaledHeight() / 2 - 120 + p0.y + 12;
        RenderingUtil.drawRoundedRect(p0.x + xOff, yOff, p0.x + xOff + 120, yOff + 2, Colors.getColor(0), Colors.getColor(120));
        RenderingUtil.drawRoundedRect(p0.x + xOff, yOff, p0.x + xOff + sliderX, yOff + 2, Colors.getColor(0), Colors.getColor(160));
        RenderingUtil.drawRoundedRect(p0.x + xOff + sliderX - 1.5f, yOff - 1.5f, p0.x + xOff + sliderX + 1.5f, yOff + 3.5f, 0, Colors.getColor(180));
        RenderingUtil.drawRoundedRect(p0.x + xOff + sliderX - 1, yOff - 1, p0.x + xOff + sliderX + 1, yOff + 3, 0, Colors.getColor(200));
        if (p0.dragging) {
            p0.dragX = p2 - (p0.x + xOff);
            Object newValue = (StringConversions.castNumber(getValue(value), p0.setting.getInc()));
            p0.setting.setValue(newValue);
        }
        if (((Number) p0.setting.getValue()).doubleValue() <= p0.setting.getMin()) {
            Object newValue = (StringConversions.castNumber(getValue(p0.setting.getMin()), p0.setting.getInc()));
            p0.setting.setValue(newValue);
        }
        if (((Number) p0.setting.getValue()).doubleValue() >= p0.setting.getMax()) {
            Object newValue = (StringConversions.castNumber(getValue(p0.setting.getMax()), p0.setting.getInc()));
            p0.setting.setValue(newValue);
        }
        if (p0.dragX <= 0.0f) {
            p0.dragX = 0.0f;
        }
        if (p0.dragX >= 120) {
            p0.dragX = 120;
        }
    }

    @Override
    public void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button) {
        categoryButton.categoryPanel.mouseReleased(x, y, button);
    }

    @Override
    public void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel) {

    }

    @Override
    public void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel) {

    }

    @Override
    public void colorConstructor(ColorPreview colorPreview, float x, float y) {
        int i = 0;
        for (RGBSlider.Colors xd : RGBSlider.Colors.values()) {
        	if(xd == RGBSlider.Colors.ALPHA && colorPreview.colorObject.getAlpha() == -1){
        		continue;
        	}
        	
            colorPreview.sliders.add(new RGBSlider(x + 3, y + i, colorPreview, xd));
            i += 10;
        }
    }

    @Override
    public void colorPrewviewDraw(ColorPreview colorPreview, float x, float y) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float x1 = res.getScaledWidth() / 2 - 120 + colorPreview.x;
        float y1 = res.getScaledHeight() / 2 - 130 + colorPreview.y;
        RenderingUtil.rectangleBordered(x1 - 5, y1 - 5, x1 + 100,colorPreview.colorObject.getAlpha() == -1 ?  y1 + 30 : y1 + 40, 1.5, Colors.getColor(150), Colors.getColor(165));

        Client.fm.getFont("SFR 8").drawString(colorPreview.colorName, x1, y1 - 11, Colors.getColor(32));

        for (RGBSlider slider : colorPreview.sliders) {
            slider.draw(x, y);
        }

    }

    @Override
    public void rgbSliderDraw(RGBSlider slider, float x, float y) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = slider.x + res.getScaledWidth() / 2 - 120;
        float yOff = slider.y + res.getScaledHeight() / 2 - 130;
        final double fraction = slider.dragX / 90;
        final double value = MathUtils.getIncremental(fraction * 255, 1);
        ColorObject cO = slider.colorPreview.colorObject;
        int faggotNiggerColor = Colors.getColor(cO.red, cO.green, cO.blue, 255);
        int faggotNiggerColor2 = Colors.getColor(cO.red, cO.green, cO.blue, 120);
        RenderingUtil.rectangle(xOff, yOff, xOff + 90, yOff + 6, Colors.getColor(32));
        if(slider.colorPreview.colorName.equalsIgnoreCase("ESP Color")){
    		faggotNiggerColor2 = Colors.getColor(cO.red, cO.green, cO.blue, cO.alpha/2);
    	}
        g.fontRendererObj.drawString("", 100, 100, -1);
        RenderingUtil.rectangle(xOff, yOff, xOff + (90 * fraction), yOff + 6, Colors.getColor(0));
        RenderingUtil.drawGradient(xOff, yOff, xOff + (90 * fraction), yOff + 6, faggotNiggerColor, faggotNiggerColor2);
        String current = "R";
        switch (slider.rgba) {
            case BLUE:
                current = "B";
                break;
            case GREEN:
                current = "G";
                break;
            case ALPHA:
                current = "A";
                break;
        }
        Client.fm.getFont("SFR 6").drawStringWithShadow(current, xOff - 6, yOff + 2.5f, -1);

        float textX = (xOff + 30 - Client.fm.getFont("SFR 6").getWidth(Integer.toString((int) value)) / 2);
        Client.fm.getFont("SFR 6").drawStringWithShadow(Integer.toString((int) value), textX, yOff + 5, -1);
        double newValue = 0;
        if (slider.dragging) {
            slider.dragX = x - slider.lastDragX;
            if (value <= 255 && value >= 0) {
                newValue = value;
            }
            switch (slider.rgba) {
                case RED:
                    slider.colorPreview.colorObject.setRed((int) newValue);
                    break;
                case GREEN:
                    slider.colorPreview.colorObject.setGreen((int) newValue);
                    break;
                case BLUE:
                    slider.colorPreview.colorObject.setBlue((int) newValue);
                    break;
                case ALPHA:
                    slider.colorPreview.colorObject.setAlpha((int) newValue);
                    break;
            }

        }
        if (slider.dragX <= 0.0f) {
            slider.dragX = 0.0f;
        }
        if (slider.dragX >= 90) {
            slider.dragX = 90;
        }
    }

    @Override
    public void rgbSliderClick(RGBSlider slider, float x, float y, int mouse) {
        ScaledResolution res = new ScaledResolution(g, g.displayWidth, g.displayHeight);
        float xOff = slider.x + res.getScaledWidth() / 2 - 120;
        float yOff = slider.y + res.getScaledHeight() / 2 - 130;
        if (x >= xOff && y >= yOff && x <= xOff + 90.0f && y <= yOff + 6.0f && mouse == 0) {
            slider.dragging = true;
            slider.lastDragX = x - slider.dragX;
        }
    }

    @Override
    public void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse) {
        if (mouse == 0) {
            info.sigmaclient.management.command.impl.Color.saveStatus();
            slider.dragging = false;
        }
    }

    private void drawModalRectWithCustomSizedTexture(float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        GlStateManager.pushMatrix();
        float var8 = 1.0F / textureWidth;
        float var9 = 1.0F / textureHeight;
        Tessellator var10 = Tessellator.getInstance();
        WorldRenderer var11 = var10.getWorldRenderer();
        boolean var1 = false;
        var11.startDrawingQuads();
        var11.addVertexWithUV((double) x, (double) (y + height), 0.0D, (double) (u * var8), (double) ((v + height) * var9));
        var11.addVertexWithUV((double) (x + width), (double) (y + height), 0.0D, (double) ((u + width) * var8), (double) ((v + height) * var9));
        var11.addVertexWithUV((double) (x + width), (double) y, 0.0D, (double) ((u + width) * var8), (double) (v * var9));
        var11.addVertexWithUV((double) x, (double) y, 0.0D, (double) (u * var8), (double) (v * var9));
        var10.draw();
        GlStateManager.popMatrix();
    }

    private ResourceLocation darrow = new ResourceLocation("textures/downarrow.png");
    private ResourceLocation uparrow = new ResourceLocation("textures/uparrow.png");

    private ResourceLocation msgo = new ResourceLocation("textures/minigames.png");
    private ResourceLocation others = new ResourceLocation("textures/others.png");
    private ResourceLocation xmark = new ResourceLocation("textures/xmark.png");
    private ResourceLocation combat = new ResourceLocation("textures/combat.png");
    private ResourceLocation player = new ResourceLocation("textures/player.png");
    private ResourceLocation visuals = new ResourceLocation("textures/visuals.png");
    private ResourceLocation movement = new ResourceLocation("textures/movement.png");
    private ResourceLocation colors = new ResourceLocation("textures/colors.png");
    //Hovered icons
    private ResourceLocation msgo2 = new ResourceLocation("textures/msgo2.png");
    private ResourceLocation others2 = new ResourceLocation("textures/others2.png");
    private ResourceLocation xmark2 = new ResourceLocation("textures/xmark2.png");
    private ResourceLocation combat2 = new ResourceLocation("textures/combat2.png");
    private ResourceLocation player2 = new ResourceLocation("textures/player2.png");
    private ResourceLocation visuals2 = new ResourceLocation("textures/visuals2.png");
    private ResourceLocation movement2 = new ResourceLocation("textures/movement2.png");
    private ResourceLocation colors2 = new ResourceLocation("textures/colors2.png");

}
