/**
 * Time: 6:58:51 PM
 * Date: Jan 4, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.hud;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.*;
import org.lwjgl.input.Keyboard;

import info.sigmaclient.Client;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventKeyPress;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author cool1
 */
public class BubbleGui extends Module {

    public int currentSetting, currentType, currentMod, typeRot = -180, setRot = -180, modRot = -180,
            targetTypeRot = -180, targetSetRot = -180, targetModRot = -180, hiddenX, targetX, targetModX, modX,
            targetModSetX, setModX, targetSetX, setX;
    public boolean inModules, inModSet, inSet;
    public Module selectedModule;
    Timer timer = new Timer();
    int opacity = 45;
    int targetOpacity = 45;
    boolean isActive;

    /**
     * @param data
     */
    public BubbleGui(ModuleData data) {
        super(data);
    }

    @Override
    public void onEnable() {
        inModules = false;
        inModSet = false;
        inSet = false;
        targetX = 0;
        typeRot = -180;
        targetTypeRot = -180;
    }

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    @RegisterEvent(events = {EventRenderGui.class, EventTick.class, EventKeyPress.class})
    public void onEvent(Event event) {
        if (mc.gameSettings.showDebugInfo) {
            return;
        }
        if (event instanceof EventRenderGui) {
            EventRenderGui er = (EventRenderGui) event;
            if (timer.delay(4500)) {
                targetX = -150;
                targetSetX = -150;
                targetModX = -150;
                inModules = false;
                inModSet = false;
                inSet = false;
                isActive = false;
            }
            animate();
            int rot = setRot;
            if (inModSet) {
                Color[] color = new Color[getSettings(selectedModule).size()];
                int currentModVal = 0;
                for (Setting mod : getSettings(selectedModule)) {
                    int posX = (int) (140 / 1.5);
                    int posZ = 0;
                    float cos = (float) Math.cos(Math.toRadians(rot));
                    float sin = (float) Math.sin(Math.toRadians(rot));
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    int posXT = (int) ((int) (84 + 16 + 40 + 25) / 1.5);
                    int posYT = -mc.fontRendererObj.FONT_HEIGHT / 2;
                    float rotY1 = -(posYT * cos - posXT * sin);
                    float rotX1 = -(posXT * cos + posYT * sin);
                    color[currentModVal] = new Color((int) (Math.sin(currentModVal) * 127 + 128),
                            (int) (Math.sin(currentModVal + Math.PI / 2) * 127 + 128),
                            (int) (Math.sin(currentModVal + Math.PI) * 127 + 128));

                    RenderingUtil.drawBorderedCircle(setX + (int) rotX,
                            (int) rotY + er.getResolution().getScaledHeight() / 2, 15 / 1.5, 1,
                            Colors.getColor(color[currentModVal].getRed(), color[currentModVal].getGreen(),
                                    color[currentModVal].getBlue(), getAlpha(rot, 2.35f)),
                            Colors.getColor(20, 20, 20, getAlpha(rot, 2.35f)));
                    String xd = mod.getName().charAt(0) + mod.getName().toLowerCase().substring(1);
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    RenderingUtil.drawOutlinedString(
                            xd + " " + (getSettings(selectedModule).get(currentSetting) == mod ? mod.getValue() : ""),
                            (setX) + (int) rotX1, (int) rotY1 + er.getResolution().getScaledHeight() / 2,
                            Colors.getColor(255, 255, 255, getAlpha(rot, 2.35f)));
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                    rot -= 19;
                    currentModVal++;
                }
            }
            rot = modRot;
            if (inModules) {
                Color[] color = new Color[getModules(ModuleData.Type.values()[currentType]).size()];
                int currentModVal = 0;
                for (Module mod : getModules(ModuleData.Type.values()[currentType])) {
                    int posX = (int) (100 / 1.5);
                    int posZ = 0;
                    float cos = (float) Math.cos(Math.toRadians(rot));
                    float sin = (float) Math.sin(Math.toRadians(rot));
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    int posXT = (int) (125 / 1.5);
                    int posYT = -mc.fontRendererObj.FONT_HEIGHT / 2;
                    float rotY1 = -(posYT * cos - posXT * sin);
                    float rotX1 = -(posXT * cos + posYT * sin);
                    color[currentModVal] = new Color((int) (Math.sin(currentModVal) * 127 + 128),
                            (int) (Math.sin(currentModVal + Math.PI / 2) * 127 + 128),
                            (int) (Math.sin(currentModVal + Math.PI) * 127 + 128));

                    RenderingUtil.drawBorderedCircle(modX + (int) rotX,
                            (int) rotY + er.getResolution().getScaledHeight() / 2, 15 / 1.5, 1,
                            Colors.getColor(color[currentModVal].getRed(), color[currentModVal].getGreen(),
                                    color[currentModVal].getBlue(), getAlpha(rot, 2.25f)),
                            Colors.getColor(20, 20, 20, getAlpha(rot, 2.25f)));
                    if (!inModSet) {
                        GlStateManager.pushMatrix();
                        GlStateManager.enableBlend();
                        RenderingUtil.drawOutlinedString(mod.getName(), (modX) + (int) rotX1,
                                (int) rotY1 + er.getResolution().getScaledHeight() / 2,
                                mod.isEnabled() ? Colors.getColor(255, 255, 255, getAlpha(rot, 2.25f))
                                        : Colors.getColor(175, 175, 175, getAlpha(rot, 2.25f)));
                        GlStateManager.disableBlend();
                        GlStateManager.popMatrix();
                    }
                    rot -= 27;
                    currentModVal++;
                }
            }
            // TODO: double a = (h == 0 ? 0 : Math.toDegrees(Math.sin(o / h))) * 2;
            rot = typeRot;
            for (ModuleData.Type type : ModuleData.Type.values()) {
                int posX = (int) (60 / 1.5);
                int posZ = 0;
                float cos = (float) Math.cos(Math.toRadians(rot));
                float sin = (float) Math.sin(Math.toRadians(rot));
                float rotY = -(posZ * cos - posX * sin);
                float rotX = -(posX * cos + posZ * sin);
                int posXT = (int) 34 + 24;
                int posYT = -mc.fontRendererObj.FONT_HEIGHT / 2;
                float rotY1 = -(posYT * cos - posXT * sin);
                float rotX1 = -(posXT * cos + posYT * sin);
                Color color = new Color(Module.getColor(type));
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                RenderingUtil.drawBorderedCircle((hiddenX) + (int) rotX,
                        (int) rotY + er.getResolution().getScaledHeight() / 2, 15 / 1.5, 1,
                        Colors.getColor(color.getRed(), color.getGreen(), color.getBlue(), getAlpha(rot, 2.2f)),
                        Colors.getColor(20, 20, 20, getAlpha(rot, 2.2f)));
                GlStateManager.disableBlend();
                GlStateManager.popMatrix();
                if (!inModules) {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    RenderingUtil.drawOutlinedString(type.name(), (hiddenX) + (int) rotX1 + 5,
                            (int) rotY1 + er.getResolution().getScaledHeight() / 2,
                            Colors.getColor(255, 255, 255, getAlpha(rot, 2.2f)));
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }
                rot -= 45;
            }
            RenderingUtil.drawBorderedCircle((hiddenX / 8), er.getResolution().getScaledHeight() / 2, 36 / 1.5, 1,
                    Colors.getColor(50, 255, 163), Colors.getColor(20, 20, 20));
        }
        if (event instanceof EventKeyPress) {
            EventKeyPress ek = (EventKeyPress) event;
            if (isActive && this.keyCheck(ek.getKey())) {
                timer.reset();
            }
            if (!isActive && keyCheck(ek.getKey())) {
                isActive = true;
                inModules = false;
                targetX = 0;
                targetModX = -150;
                timer.reset();
                return;
            }
            if (!inModules && isActive) {
                if (ek.getKey() == Keyboard.KEY_DOWN) {
                    targetTypeRot += 45;
                    currentType++;
                    if (currentType > ModuleData.Type.values().length - 1) {
                        targetTypeRot = -180;
                        currentType = 0;
                    }
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    targetTypeRot -= 45;
                    currentType--;
                    if (currentType < 0) {
                        currentType = ModuleData.Type.values().length - 1;
                        targetTypeRot = -180 + (ModuleData.Type.values().length - 1) * 45;
                    }
                } else if (ek.getKey() == Keyboard.KEY_RIGHT) {
                    inModules = true;
                    targetModRot = -180;
                    modRot = -180;
                    currentMod = 0;
                    targetModX = 0;
                } else if (ek.getKey() == Keyboard.KEY_LEFT) {
                    isActive = false;
                    targetX = -150;
                    modX = -175;
                }
            } else if (inModules && !inModSet && isActive && !inSet) {
                if (ek.getKey() == Keyboard.KEY_LEFT) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                targetModX = -170;
                                Thread.sleep(150);
                            } catch (InterruptedException e) {
                            }
                            inModules = false;
                        }

                    });
                    thread.start();
                }
                if (ek.getKey() == Keyboard.KEY_DOWN) {
                    targetModRot += 27;
                    currentMod++;
                    if (currentMod > getModules(ModuleData.Type.values()[currentType]).size() - 1) {
                        targetModRot = -180;
                        currentMod = 0;
                    }
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    targetModRot -= 27;
                    currentMod--;
                    if (currentMod < 0) {
                        targetModRot = (-180 + (getModules(ModuleData.Type.values()[currentType]).size() - 1) * 27);
                        currentMod = getModules(ModuleData.Type.values()[currentType]).size() - 1;
                    }
                } else if (ek.getKey() == Keyboard.KEY_RETURN) {
                    try {
                        Module mod = getModules(ModuleData.Type.values()[currentType]).get(currentMod);
                        if (mod != this || mod != Client.getModuleManager().get(TabGUI.class)) {
                            mod.toggle();
                        }
                    } catch (Exception e) {
                        ChatUtil.printChat(
                                getModules(ModuleData.Type.values()[currentType]).size() + ", " + currentMod + ", ");
                    }
                } else if (ek.getKey() == Keyboard.KEY_RIGHT) {
                    selectedModule = getModules(ModuleData.Type.values()[currentType]).get(currentMod);
                    if (!(getSettings(selectedModule) == null)) {
                        inModSet = true;
                        targetSetX = 0;
                        currentSetting = 0;
                        this.setRot = -180;
                        this.targetSetRot = -180;
                    } else if (selectedModule != Client.getModuleManager().get(BubbleGui.class) || selectedModule != Client.getModuleManager().get(TabGUI.class)) {
                        selectedModule.toggle();
                    }
                }
            } else if (inModSet && !inSet) {
                if (ek.getKey() == Keyboard.KEY_LEFT) {
                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                targetSetX = -200;
                                Thread.sleep(100);
                            } catch (InterruptedException e) {
                            }
                            inModSet = false;
                        }

                    });
                    thread.start();
                } else if (ek.getKey() == Keyboard.KEY_DOWN) {
                    targetSetRot += 19;
                    currentSetting++;
                    if (currentSetting > getSettings(selectedModule).size() - 1) {
                        targetSetRot = -180;
                        currentSetting = 0;
                    }
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    targetSetRot -= 19;
                    currentSetting--;
                    if (currentSetting < 0) {
                        targetSetRot = (-180 + (getSettings(selectedModule).size() - 1) * 19);
                        currentSetting = getSettings(selectedModule).size() - 1;
                    }
                } else if (ek.getKey() == Keyboard.KEY_RIGHT) {

                    Setting set = getSettings(selectedModule).get(currentSetting);
                    if (set.getValue() instanceof Number) {
                        inSet = true;
                    } else if (set.getValue().getClass().equals(Boolean.class)) {
                        boolean xd = ((Boolean) set.getValue()).booleanValue();
                        set.setValue(!xd);
                        Module.saveSettings();
                    }
                }
            } else if (inSet) {
                if (ek.getKey() == Keyboard.KEY_LEFT) {
                    inSet = false;
                } else if (ek.getKey() == Keyboard.KEY_UP) {
                    Setting set = getSettings(selectedModule).get(currentSetting);
                    if (set.getValue() instanceof Number) {
                        double increment = (set.getInc() != 0 ? set.getInc() : 0.5);
                        String str = MathUtils.roundToPlace((((Number) (set.getValue())).doubleValue() + increment), 1) + "";
                        if (Double.parseDouble(str) > set.getMax() && set.getInc() != 0) {
                            return;
                        }
                        Object newValue = (StringConversions.castNumber(str, set.getValue()));
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                            return;
                        }
                    }
                } else if (ek.getKey() == Keyboard.KEY_DOWN) {
                    Setting set = getSettings(selectedModule).get(currentSetting);
                    if (set.getValue() instanceof Number) {
                        double increment = (set.getInc() != 0 ? set.getInc() : 0.5);
                        String str = MathUtils.roundToPlace((((Number) (set.getValue())).doubleValue() - increment), 1) + "";
                        if (Double.parseDouble(str) < set.getMin() && set.getInc() != 0) {
                            return;
                        }
                        Object newValue = (StringConversions.castNumber(str, set.getValue()));
                        if (newValue != null) {
                            set.setValue(newValue);
                            Module.saveSettings();
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
     *
     */
    private void animate() {
        int diffType = (targetTypeRot) - (typeRot);
        typeRot += (int) (diffType * 0.1);
        if (diffType > 0) {
            typeRot++;
        } else if (diffType < 0) {
            typeRot--;
        }

        int diffMod = (targetModRot) - (modRot);
        modRot += (int) (diffMod * 0.15);
        if (diffMod > 0) {
            modRot++;
        } else if (diffMod < 0) {
            modRot--;
        }

        int diffSetMod = (targetSetRot) - setRot;
        setRot += (int) (diffSetMod * 0.2);
        if (diffSetMod > 0) {
            setRot++;
        } else if (diffSetMod < 0) {
            setRot--;
        }

        int diffHidden = (targetX) - (hiddenX);
        hiddenX += (int) (diffHidden / 9);
        if (diffHidden > 0) {
            hiddenX++;
        } else if (diffHidden < 0) {
            hiddenX--;
        }

        int diffModX = (targetModX) - (modX);
        modX += (int) (diffModX / 7);
        if (diffModX > 0) {
            modX++;
        } else if (diffModX < 0) {
            modX--;
        }

        int diffSetModX = (targetModSetX) - (setModX);
        setModX += (int) (diffSetModX / 7);
        if (diffSetModX > 0) {
            setModX++;
        } else if (diffSetModX < 0) {
            setModX--;
        }

        int diffSetX = (targetSetX) - (setX);
        setX += (int) (diffSetX / 7);
        if (diffSetX > 0) {
            setX++;
        } else if (diffSetX < 0) {
            setX--;
        }

        int opacityDiff = (targetOpacity) - (opacity);
        opacity += opacityDiff * 0.25;
    }

    /**
     * @param key
     * @return
     */
    private boolean keyCheck(int key) {
        boolean active = false;
        switch (key) {
            case Keyboard.KEY_DOWN:
                active = true;
                break;
            case Keyboard.KEY_UP:
                active = true;
                break;
            case Keyboard.KEY_RETURN:
                active = true;
                break;
            case Keyboard.KEY_LEFT:
                if (!inModules) {
                    active = true;
                }
                break;
            case Keyboard.KEY_RIGHT:
                active = true;
                break;
            default:
                break;
        }
        return active;
    }

    /**
     * @return
     */
    private List<Setting> getSettings(Module mod) {
        List<Setting> settings = new ArrayList();
        for (Setting set : mod.getSettings().values()) {
            settings.add(set);
        }
        if (settings.isEmpty()) {
            return null;
        }
        return settings;
    }

    private int getAlpha(int rotation, float multiplier) {
        float dist = RotationUtils.getDistanceBetweenAngles(-180, rotation);
        int alpha = (int) Math.abs((((dist * multiplier / 2.5) * 0.01f) * 0) + ((1 - (((dist * multiplier / 2.5) * 0.01f))) * 255));
        if (dist > 90) {
            alpha = 20;
        }
        return alpha;
    }

    /**
     * @return
     */
    private List<Module> getModules(ModuleData.Type type) {
        List<Module> modulesInType = new ArrayList();
        for (Module mod : Client.getModuleManager().getArray()) {
            if (mod.getType() == type) {
                modulesInType.add(mod);
                int width = 0;
                if (mc.fontRendererObj.getStringWidth(mod.getName()) > width) {
                    width = mc.fontRendererObj.getStringWidth(mod.getName());
                }
            }
        }
        if (modulesInType.isEmpty()) {
            return null;
        }
        modulesInType.sort(new Comparator<Module>() {

            @Override
            public int compare(Module m1, Module m2) {
                return m1.getName().compareTo(m2.getName());
            }

        });
        return modulesInType;
    }
}
