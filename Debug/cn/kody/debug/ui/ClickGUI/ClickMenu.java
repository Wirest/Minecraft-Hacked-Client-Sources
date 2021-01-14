package cn.kody.debug.ui.ClickGUI;

import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;

import cn.kody.debug.Client;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.ui.ClickGUI.button.Button;
import cn.kody.debug.ui.ClickGUI.option.UISlider;
import cn.kody.debug.ui.ClickGUI.option.UIToggleButton;
import cn.kody.debug.utils.Type;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.handler.MouseInputHandler;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.value.Value;

import java.awt.Color;
import java.awt.List;

import org.lwjgl.input.*;
import java.io.*;
import java.util.*;

public class ClickMenu
{
    private ArrayList<ClickMenuCategory> categories;
    public static int WIDTH;
    public static int TAB_HEIGHT;
    private MouseInputHandler handler;
    private Minecraft mc;
    private String fileDir;
    public boolean settingMode;
    public Mod currentMod;
    public ArrayList<Value> modBooleanValue;
    public ArrayList<Value> modModeValue;
    public ArrayList<Value> modDoubleValue;
    
    public static Map<Value, Button> booleanValueMap;
    public static Map<Value, UISlider> doubleValueMap;
    
    public boolean isSelectingMode;
    public Value currentSelectingMode;
    public float currentStartY;
    public boolean isDraggingSlider;
    public float wheelSmoothValue;
    public float wheelStateValue;
    
    public ClickMenu() {
        this.handler = new MouseInputHandler(0);
        this.mc = Minecraft.getMinecraft();
        this.settingMode = false;
        this.currentMod = null;
        this.modBooleanValue = new ArrayList<Value>();
        this.modModeValue = new ArrayList<Value>();
        this.modDoubleValue = new ArrayList<Value>();
        this.isSelectingMode = false;
        this.currentSelectingMode = null;
        this.currentStartY = 0.0f;
        this.isDraggingSlider = false;
        this.fileDir = String.valueOf(this.mc.mcDataDir.getAbsolutePath()) + "/" + Client.CLIENT_FILEMANAGER_FOLDERNAME;
        this.categories = new ArrayList<ClickMenuCategory>();
        this.addCategorys();
        try {
            this.loadClickGui();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public void draw(int n, int n2) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        float n3 = (float)scaledResolution.getScaledWidth();
        float n4 = (float)scaledResolution.getScaledHeight();
        Iterator<ClickMenuCategory> iterator = this.categories.iterator();
        while (iterator.hasNext()) {
            iterator.next().draw(n, n2);
        }
        if (this.settingMode && this.currentMod != null) {
            RenderUtil.drawRect(0.0f, 0.0f, n3, n4, Notification.reAlpha(Colors.BLACK.c, 0.45f));
            this.modBooleanValue.clear();
            this.modModeValue.clear();
            this.modDoubleValue.clear();
            for (Value values : Value.list) {
                if (values.getValueName().split("_")[0].equalsIgnoreCase(this.currentMod.getName())) {
                    Mod currentMod = this.currentMod;
                    ++currentMod.valueSize;
                    if (values.isValueDouble) {
                        this.modDoubleValue.add(values);
                    }
                    if (values.isValueMode) {
                        this.modModeValue.add(values);
                    }
                    if (values.isValueBoolean) {
                        this.modBooleanValue.add(values);
                    }
                }
            }
            Client.instance.fontMgr.tahoma30.drawString(this.currentMod.getRenderName(), n3 / 2.0f - 100.0f, n4 / 2.0f - 135.0f - Client.instance.fontMgr.tahoma30.FONT_HEIGHT, Colors.WHITE.c);
            RenderUtil.drawRoundedRect(n3 / 2.0f - 100.0f, n4 / 2.0f - 130.0f, n3 / 2.0f + 100.0f, n4 / 2.0f + 130.0f, (int) 5.0f, Colors.WHITE.c);
            if (!RenderUtil.isHovering(n, n2, n3 / 2.0f - 105.0f, n4 / 2.0f - 135.0f, n3 / 2.0f + 105.0f, n4 / 2.0f + 135.0f) && this.handler.canExcecute() && !this.isSelectingMode && !this.isDraggingSlider) {
                this.settingMode = false;
                this.currentMod = null;
                this.wheelStateValue = 0.0f;
            }
            if (!this.isSelectingMode) {
                this.processWheel(n, n2, n3, n4);
            }
            this.wheelSmoothValue = (float) RenderUtil.getAnimationState(this.wheelSmoothValue, this.wheelStateValue * 30.0f, (float)(Math.max(10.0f, Math.abs(this.wheelSmoothValue - this.wheelStateValue * 30.0f) * 50.0f) * 0.3));
            float currentStartY = n4 / 2.0f - 122.0f + this.wheelSmoothValue;
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor((int)n3 / 2 - 98, (int)n4 / 2 - 128, (int)(n3 / 2.0f + 98.0f - (n3 / 2.0f - 98.0f)), (int)(n4 / 2.0f + 128.0f) - (int)(n4 / 2.0f - 128.0f));
            //boolean
            for (Value values2 : this.modBooleanValue) {
                String s = values2.getValueName().split("_")[0];//ModName
                Client.instance.fontMgr.tahoma20.drawString(values2.getValueName().split("_")[1], n3 / 2.0f - 94.0f, currentStartY - 2.0f, Colors.BLACK.c);
                Object o;
                if (booleanValueMap.containsKey(values2)) {
                    o = booleanValueMap.get(values2);
                }
                else {
                    o = new UIToggleButton(this, s, (boolean)values2.getValueState(), values2);
                    booleanValueMap.put(values2, (Button)o);
                }
                ((Button)o).draw(n3 / 2.0f + 92.0f, currentStartY + 4.0f);
                currentStartY += 18.0f;
            }
            
            //double
            for (Value values3 : this.modDoubleValue) {
                String s2 = values3.getValueName().split("_")[1];
                UISlider uislider;
                if (doubleValueMap.containsKey(values3)) {
                    uislider = doubleValueMap.get(values3);
                }
                else {
                    uislider = new UISlider(values3);
                    doubleValueMap.put(values3, uislider);
                }
                uislider.draw(n3 / 2.0f + 104.0f, currentStartY);
                uislider.onPress(n, n2);
                currentStartY += 18.0f;
            }
            
            
            
            for (Value currentSelectingMode : this.modModeValue) {
                Client.instance.fontMgr.tahoma20.drawString(currentSelectingMode.getModeTitle(), n3 / 2.0f - 94.0f, currentStartY - 2.0f, Colors.BLACK.c);
                try {
                    Client.instance.fontMgr.tahoma16.drawString(currentSelectingMode.getModeAt(currentSelectingMode.getCurrentMode()), n3 / 2.0f + 28.0f, currentStartY, Colors.DARKGREY.c);
                }
                catch (Exception ex) {
                    Notification.tellPlayer("\u93c4\u5267\u305a\u93c3\u8dfa\u5f42\u9422\u71bc\u654a\u7487\ufffd,\u5bb8\u8336\ue195\u7f03\ue1bb\u8d1f\u699b\u6a3f\ue17b\u59af\u2033\u7d21", Type.ERROR);
                    currentSelectingMode.setCurrentMode(0);
                }
                if (!this.isSelectingMode) {
                    Client.instance.fontMgr.tahoma25.drawString(">", n3 / 2.0f + 84.0f, currentStartY - 4.0f, new Color(Colors.GREY.c).brighter().getRGB());
                    if (RenderUtil.isHovering(n, n2, n3 / 2.0f + 85.0f, currentStartY, n3 / 2.0f + 95.0f, currentStartY + 10.0f) && this.handler.canExcecute()) {
                        this.isSelectingMode = true;
                        this.currentSelectingMode = currentSelectingMode;
                    }
                }
                else if (RenderUtil.isHovering(n, n2, n3 / 2.0f + 84.0f, currentStartY, n3 / 2.0f + 95.0f, currentStartY + 10.0f) && this.handler.canExcecute()) {
                    this.isSelectingMode = false;
                    this.currentSelectingMode = null;
                }
                if (this.isSelectingMode) {
                    if (this.currentSelectingMode == currentSelectingMode) {
                        Client.instance.fontMgr.tahoma25.drawString("-", n3 / 2.0f + 87.0f, currentStartY - 4.0f, new Color(Colors.GREY.c).brighter().getRGB());
                        this.currentStartY = currentStartY;
                    }
                    else {
                        Client.instance.fontMgr.tahoma25.drawString(">", n3 / 2.0f + 84.0f, currentStartY - 3.0f, new Color(Colors.GREY.c).brighter().getRGB());
                    }
                }
                currentStartY += 18.0f;
            }
            GL11.glDisable(3089);
            GL11.glPopMatrix();
            if (this.isSelectingMode && this.currentSelectingMode != null) {
                Value currentSelectingMode2 = this.currentSelectingMode;
                float currentStartY2 = this.currentStartY;
                RenderUtil.drawRoundedRect(n3 / 2.0f + 102.0f, currentStartY2, n3 / 2.0f + 182.0f, currentStartY2 + 15 * currentSelectingMode2.mode.size(), (int) 2.0f, Colors.WHITE.c);
                int i = 0;
                while (i < currentSelectingMode2.mode.size()) {
                    Client.instance.fontMgr.tahoma15.drawString((String)currentSelectingMode2.mode.get(i), n3 / 2.0f + 106.0f, currentStartY2 + 3.0f, Colors.BLACK.c);
                    if (RenderUtil.isHovering(n, n2, n3 / 2.0f + 102.0f, currentStartY2, n3 / 2.0f + 182.0f, currentStartY2 + 15.0f) && Mouse.isButtonDown(0)) {
                        this.currentSelectingMode.setCurrentMode(i);
                        this.isSelectingMode = false;
                        this.currentSelectingMode = null;
                        Client.instance.fileMgr.saveValues();
                    }
                    currentStartY2 += 15.0f;
                    ++i;
                }
            }
        }
    }
    
    private void processWheel(int n, int n2, float n3, float n4) {
        int dWheel = Mouse.getDWheel();
        if (RenderUtil.isHovering(n, n2, n3 / 2.0f - 100.0f, n4 / 2.0f - 130.0f, n3 / 2.0f + 100.0f, n4 / 2.0f + 130.0f)) {
            if (dWheel > 0) {
                if (this.wheelStateValue < 0.0f) {
                    ++this.wheelStateValue;
                }
            }
            else if (dWheel < 0 && this.wheelStateValue * 30.0f > this.currentMod.valueSize * -40) {
                --this.wheelStateValue;
            }
        }
    }
    
    private void addCategorys() {
        TAB_HEIGHT = 25;
        WIDTH = 85;
        int n = 10;
        Category[] array = Category.values();
        int length = array.length;
        int i = 0;
        while (i < length) {
            this.categories.add(new ClickMenuCategory(array[i], n, 100, WIDTH, TAB_HEIGHT, this.handler));
            n += 115;
            ++i;
        }
    }
    
    public void mouseClick(int n, int n2) {
        Iterator<cn.kody.debug.ui.ClickGUI.button.Button> iterator = booleanValueMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().isPressed(n, n2);
        }
        Iterator<ClickMenuCategory> iterator2 = this.categories.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().mouseClick(n, n2);
        }
    }
    
    public void mouseRelease(int n, int n2) {
        Iterator<ClickMenuCategory> iterator = this.categories.iterator();
        while (iterator.hasNext()) {
            iterator.next().mouseRelease(n, n2);
        }
        this.saveClickGui();
    }
    
    public ArrayList<ClickMenuCategory> getCategories() {
        return this.categories;
    }
    
    public void saveClickGui() {
        File file = new File(String.valueOf(this.fileDir) + "/gui.txt");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            PrintWriter printWriter = new PrintWriter(file);
            for (ClickMenuCategory menu : this.getCategories()) {
                printWriter.print(String.valueOf(menu.c.name()) + ":" + String.valueOf(menu.x) + ":" + String.valueOf(menu.y) + ":" + String.valueOf(menu.uiMenuMods.open) + "\n");
            }
            printWriter.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadClickGui() throws IOException {
        File file = new File(String.valueOf(this.fileDir) + "/gui.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        else {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                try {
                    String[] array = line.split(":");
                    if (array.length < 4) {
                        continue;
                    }
                    else {
                        String s = array[0];
                        int intValue = Integer.valueOf(array[1]);
                        int intValue2 = Integer.valueOf(array[2]);
                        boolean booleanValue = Boolean.valueOf(array[3]);
                        for (ClickMenuCategory menu : this.getCategories()) {
                            if (!menu.c.name().equals(s)) {
                                continue;
                            }
                            else {
                                menu.x = intValue;
                                menu.y = intValue2;
                                menu.uiMenuMods.open = booleanValue;
                                continue;
                            }
                        }
                        continue;
                    }
                }
                catch (Exception ex) {
                    continue;
                }
            }
            bufferedReader.close();
        }
    }
    
    static {
        WIDTH = 100;
        TAB_HEIGHT = 30;
        booleanValueMap = new HashMap<Value, Button>();
        doubleValueMap = new HashMap<Value, UISlider>();
        }
}
