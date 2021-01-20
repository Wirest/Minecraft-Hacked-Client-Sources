package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventPacket;
import cedo.events.listeners.EventRenderGUI;
import cedo.modules.Module;
import cedo.settings.impl.NumberSetting;
import cedo.ui.animations.Direction;
import cedo.ui.animations.impl.SmoothStepAnimation;
import cedo.ui.elements.Draw;
import cedo.util.Timer;
import cedo.util.font.FontUtil;
import cedo.util.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.S45PacketTitle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("rawtypes")
public class Statistics extends Module {
    public long gameStartTime;
    private boolean gameInSession;
    private StatWindow statWindow;
    private HashMap<String, Integer> statsMap = new HashMap<String, Integer>();
    private NumberSetting displayDuration = new NumberSetting("Duration", 12, 3, 30, 1);
    private NumberSetting scale = new NumberSetting("Scale", 1, 0.8, 2.5, 0.1);
    private NumberSetting xPos = new NumberSetting("X", 0.1, 0.01, 1, 0.01);
    private NumberSetting yPos = new NumberSetting("Y", 0.1, 0.01, 1, 0.01);

    public Statistics() {
        super("Statistics", Keyboard.KEY_NONE, Category.PLAYER);
        addSettings(displayDuration, xPos, yPos);
    }

    public void increment(String key) {
        statsMap.merge(key, 1, Integer::sum);
    }

    public void add(String key, int value) {
        if (statsMap.containsKey(key)) {
            statsMap.put(key, statsMap.get(key) + value);
        } else {
            statsMap.put(key, value);
        }
    }

    public void onEvent(Event e) {
        if (e instanceof EventRenderGUI) {
            if (statWindow != null) {
                if(statWindow.shouldNullify()) {
                    statWindow = null;
                    return;
                }
                statWindow.draw();
            }
        }
        if (e instanceof EventPacket && e.isIncoming()) {
            EventPacket event = (EventPacket) e;
            /*if ((event.getPacket() instanceof S3EPacketTeams)) {
                String message = StringUtils.stripControlCodes(((S3EPacketTeams) event.getPacket()).func_149311_e());
                if (message.equals("Mode: Normal") || message.equals("Mode: Insane") || message.equals("Mode: Mega")) {
                    if(!gameInSession) {
                        startGame();
                    }
                }
            }*/
            if (event.getPacket() instanceof S45PacketTitle) {
                if (((S45PacketTitle) event.getPacket()).getMessage() == null)
                    return;

                String message = ((S45PacketTitle) event.getPacket()).getMessage().getUnformattedText();
                if (message.equalsIgnoreCase("INSANE MODE") || message.equalsIgnoreCase("NORMAL MODE")) {
                    startGame();
                    showStartScreen(message);
                } else if (message.equals("YOU DIED!") || message.equals("GAME END") || message.equals("VICTORY!")
                        || message.equals("You are now a spectator!")) {
                    showEndScreen(System.currentTimeMillis());
                    endGame();
                }
            }
        }
    }

    private void startGame() {
        gameStartTime = System.currentTimeMillis();
        gameInSession = true;
        resetStats();
    }

    private void endGame() {
        gameInSession = false;
        resetStats();
    }

    private void resetStats() {
        statsMap.clear();
    }

    private void showStartScreen(String mode) {
        statWindow = new StatWindow("Game starting", 5000);
        statWindow.add("Mode", mode);
    }

    private void showEndScreen(long gameEndTime) {
        statWindow = new StatWindow("Game Stats", (long) (displayDuration.getValue() * 1000));
        if(gameStartTime != 0) statWindow.add("Duration", formatTime(gameEndTime - gameStartTime));
        if(statsMap.containsKey("Fly Toggles")) statWindow.add("Times Flown", Integer.toString(statsMap.get("Fly Toggles")));
        if(statsMap.containsKey("Speed Duration")) statWindow.add("Speed Duration", formatTime(statsMap.get("Speed Duration")));
        if(statsMap.containsKey("Scaffold Blocks Placed")) statWindow.add("Scaffold Blocks Placed", Integer.toString(statsMap.get("Scaffold Blocks Placed")));
        if(statsMap.containsKey("Pots Thrown")) statWindow.add("Pots Thrown", Integer.toString(statsMap.get("Pots Thrown")));
        //damage dealt - estimate, apparently you can't tell if damage is from you
        //kills
        //targetstrafe circles around player
        //speed and fly blocks travelled
        //for the future: shows all players killed
        //items cleaned
        //times crit
        //hits
        //damage taken
    }

    private String formatTime(long time) {
        time /= 1000;
        return String.format("%d:%02d", time / 60, time % 60);
    }

    private class StatWindow {
        int width = 100;
        int height = 40;
        List<Stat> stats = new ArrayList<Stat>();
        String title;
        SmoothStepAnimation animation = new SmoothStepAnimation(500, 1, Direction.FORWARDS);
        Timer timer = new Timer();
        long duration;

        public StatWindow(String title, long duration) {
            this.title = title;
            this.duration = duration;
        }

        public boolean shouldNullify() {
            return timer.hasTimeElapsed(duration, false) && animation.isDone() && animation.getDirection().equals(Direction.BACKWARDS);
        }

        public void add(String name, String stat) {
            stats.add(new Stat(name, stat));
        }

        public void draw() {
            if (timer.hasTimeElapsed(duration, false)){
                animation.setDirection(Direction.BACKWARDS);
            }
            int statsHeight = 0;
            int longestStat = (int) (FontUtil.cleanmedium.getStringWidth(title) + 14);
            for (Stat stat : stats) {
                statsHeight += FontUtil.cleanSmall.getHeight() + 1;
                longestStat = (int) Math.max(longestStat, FontUtil.cleanSmall.getStringWidth(stat.getStat()) + 8 + FontUtil.cleanSmall.getStringWidth(stat.getName() + ":"));
            }
            height = 20 + FontUtil.cleanmedium.getHeight() + statsHeight;
            width = 16 + longestStat;
            int x = (int) (xPos.getValue() * (mc.displayWidth / 2) - width / 2);
            int y = (int) (yPos.getValue() * (mc.displayHeight / 2));
            GL11.glPushMatrix();
            GlStateManager.translate(x, y, 0);
            GlStateManager.scale(scale.getValue(), scale.getValue(), 1.0f);
            GlStateManager.translate(-x, -y, 0);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtil.scissor(x, y, (int) (width * scale.getValue() * (animation != null ? animation.getOutput() : 0)), height * scale.getValue());
            GlStateManager.pushMatrix();
            Draw.color(new Color(54, 57, 63).getRGB());
            Draw.drawRoundedRect(x, y, width, height, 4);
            GlStateManager.popMatrix();
            Gui.drawRect(x, y, x + 4, y + height, new Color(114, 137, 218).getRGB());

            FontUtil.cleanmedium.drawString(title, x + 10, y + 8, 0xffeeeeee);
            int currentY = y + 6 + FontUtil.cleanmedium.getHeight();
            for (Stat stat : stats) {
                currentY += FontUtil.cleanSmall.getHeight() + 1;
                FontUtil.cleanSmall.drawString(stat.getName() + ":", x + 12, currentY, 0xffcccccc);
                FontUtil.cleanSmall.drawString(stat.getStat(), x + 16 + FontUtil.cleanSmall.getStringWidth(stat.getName() + ":"), currentY, 0xffaaaaaa);
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();
        }

        private class Stat {
            String name, stat;
            public Stat(String name, String stat) {
                this.name = name;
                this.stat = stat;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getStat() {
                return stat;
            }

            public void setStat(String stat) {
                this.stat = stat;
            }
        }
    }
}