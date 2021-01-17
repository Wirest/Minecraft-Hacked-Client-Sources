// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.gui.screens;

import net.minecraft.realms.RealmsScrolledSelectionList;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.RealmsDefaultVertexFormat;
import org.lwjgl.opengl.GL11;
import net.minecraft.realms.Tezzelator;
import net.minecraft.realms.Realms;
import net.minecraft.realms.RealmsButton;
import java.util.Calendar;
import java.util.TimeZone;
import com.mojang.realmsclient.dto.ServerActivity;
import com.google.common.collect.Lists;
import java.util.Iterator;
import com.mojang.realmsclient.dto.ServerActivityList;
import com.mojang.realmsclient.exception.RealmsServiceException;
import java.util.Collections;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.mojang.realmsclient.client.RealmsClient;
import org.lwjgl.input.Keyboard;
import java.util.Arrays;
import java.util.ArrayList;
import com.google.common.cache.LoadingCache;
import java.util.List;
import com.mojang.realmsclient.dto.RealmsServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsActivityScreen extends RealmsScreen
{
    private static final Logger LOGGER;
    private final RealmsScreen lastScreen;
    private final RealmsServer serverData;
    private volatile List<ActivityRow> activityMap;
    private DetailsList list;
    private int matrixWidth;
    private String toolTip;
    private volatile List<Day> dayList;
    private List<Color> colors;
    private int colorIndex;
    private long periodInMillis;
    private int maxKeyWidth;
    private Boolean noActivity;
    private int activityPoint;
    private int dayWidth;
    private double hourWidth;
    private double minuteWidth;
    private int BUTTON_BACK_ID;
    private static LoadingCache<String, String> activitiesNameCache;
    
    public RealmsActivityScreen(final RealmsScreen lastScreen, final RealmsServer serverData) {
        this.activityMap = new ArrayList<ActivityRow>();
        this.dayList = new ArrayList<Day>();
        this.colors = Arrays.asList(new Color(79, 243, 29), new Color(243, 175, 29), new Color(243, 29, 190), new Color(29, 165, 243), new Color(29, 243, 130), new Color(243, 29, 64), new Color(29, 74, 243));
        this.colorIndex = 0;
        this.maxKeyWidth = 0;
        this.noActivity = false;
        this.activityPoint = 0;
        this.dayWidth = 0;
        this.hourWidth = 0.0;
        this.minuteWidth = 0.0;
        this.BUTTON_BACK_ID = 0;
        this.lastScreen = lastScreen;
        this.serverData = serverData;
        this.getActivities();
    }
    
    @Override
    public void mouseEvent() {
        super.mouseEvent();
        this.list.mouseEvent();
    }
    
    @Override
    public void init() {
        Keyboard.enableRepeatEvents(true);
        this.buttonsClear();
        this.matrixWidth = this.width();
        this.list = new DetailsList();
        this.buttonsAdd(RealmsScreen.newButton(this.BUTTON_BACK_ID, this.width() / 2 - 100, this.height() - 30, 200, 20, RealmsScreen.getLocalizedString("gui.back")));
    }
    
    private Color getColor() {
        if (this.colorIndex > this.colors.size() - 1) {
            this.colorIndex = 0;
        }
        return this.colors.get(this.colorIndex++);
    }
    
    private void getActivities() {
        new Thread() {
            @Override
            public void run() {
                final RealmsClient client = RealmsClient.createRealmsClient();
                try {
                    final ServerActivityList activities = client.getActivity(RealmsActivityScreen.this.serverData.id);
                    RealmsActivityScreen.this.activityMap = RealmsActivityScreen.this.convertToActivityMatrix(activities);
                    final List<Day> tempDayList = new ArrayList<Day>();
                    for (final ActivityRow row : RealmsActivityScreen.this.activityMap) {
                        for (final Activity activity : row.activities) {
                            final String day = new SimpleDateFormat("dd/MM").format(new Date(activity.start));
                            final Day the_day = new Day(day, activity.start);
                            if (!tempDayList.contains(the_day)) {
                                tempDayList.add(the_day);
                            }
                        }
                    }
                    Collections.sort(tempDayList);
                    for (final ActivityRow row : RealmsActivityScreen.this.activityMap) {
                        for (final Activity activity : row.activities) {
                            final String day = new SimpleDateFormat("dd/MM").format(new Date(activity.start));
                            final Day the_day = new Day(day, activity.start);
                            activity.dayIndex = tempDayList.indexOf(the_day) + 1;
                        }
                    }
                    RealmsActivityScreen.this.dayList = tempDayList;
                }
                catch (RealmsServiceException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    private List<ActivityRow> convertToActivityMatrix(final ServerActivityList serverActivityList) {
        final List<ActivityRow> activityRows = (List<ActivityRow>)Lists.newArrayList();
        this.periodInMillis = serverActivityList.periodInMillis;
        final long base = System.currentTimeMillis() - serverActivityList.periodInMillis;
        for (final ServerActivity sa : serverActivityList.serverActivities) {
            ActivityRow activityRow = this.find(sa.profileUuid, activityRows);
            final Calendar joinTime = Calendar.getInstance(TimeZone.getDefault());
            joinTime.setTimeInMillis(sa.joinTime);
            final Calendar leaveTime = Calendar.getInstance(TimeZone.getDefault());
            leaveTime.setTimeInMillis(sa.leaveTime);
            final Activity e = new Activity(base, joinTime.getTimeInMillis(), leaveTime.getTimeInMillis());
            if (activityRow == null) {
                String name = "";
                try {
                    name = RealmsActivityScreen.activitiesNameCache.get(sa.profileUuid);
                }
                catch (Exception exception) {
                    RealmsActivityScreen.LOGGER.error("Could not get name for " + sa.profileUuid, exception);
                    continue;
                }
                activityRow = new ActivityRow(sa.profileUuid, new ArrayList<Activity>(), name, sa.profileUuid);
                activityRow.activities.add(e);
                activityRows.add(activityRow);
            }
            else {
                activityRow.activities.add(e);
            }
        }
        Collections.sort(activityRows);
        for (final ActivityRow row : activityRows) {
            row.color = this.getColor();
            Collections.sort(row.activities);
        }
        this.noActivity = (activityRows.size() == 0);
        return activityRows;
    }
    
    private ActivityRow find(final String key, final List<ActivityRow> rows) {
        for (final ActivityRow row : rows) {
            if (row.key.equals(key)) {
                return row;
            }
        }
        return null;
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void buttonClicked(final RealmsButton button) {
        if (button.id() == this.BUTTON_BACK_ID) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void keyPressed(final char ch, final int eventKey) {
        if (eventKey == 1) {
            Realms.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public void render(final int xm, final int ym, final float a) {
        this.toolTip = null;
        this.renderBackground();
        for (final ActivityRow row : this.activityMap) {
            final int keyWidth = this.fontWidth(row.name);
            if (keyWidth > this.maxKeyWidth) {
                this.maxKeyWidth = keyWidth + 10;
            }
        }
        final int keyRightPadding = 25;
        this.activityPoint = this.maxKeyWidth + keyRightPadding;
        final int spaceLeft = this.matrixWidth - this.activityPoint - 10;
        final int days = (this.dayList.size() < 1) ? 1 : this.dayList.size();
        this.dayWidth = spaceLeft / days;
        this.hourWidth = this.dayWidth / 24.0;
        this.minuteWidth = this.hourWidth / 60.0;
        this.list.render(xm, ym, a);
        if (this.activityMap != null && this.activityMap.size() > 0) {
            final Tezzelator t = Tezzelator.instance;
            GL11.glDisable(3553);
            t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
            t.vertex(this.activityPoint, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(this.activityPoint + 1, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(this.activityPoint + 1, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.vertex(this.activityPoint, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
            t.end();
            GL11.glEnable(3553);
            for (final Day day : this.dayList) {
                final int daysIndex = this.dayList.indexOf(day) + 1;
                this.drawString(day.day, this.activityPoint + (daysIndex - 1) * this.dayWidth + (this.dayWidth - this.fontWidth(day.day)) / 2 + 2, this.height() - 52, 16777215);
                GL11.glDisable(3553);
                t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
                t.vertex(this.activityPoint + daysIndex * this.dayWidth, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
                t.vertex(this.activityPoint + daysIndex * this.dayWidth + 1, this.height() - 40, 0.0).color(128, 128, 128, 255).endVertex();
                t.vertex(this.activityPoint + daysIndex * this.dayWidth + 1, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
                t.vertex(this.activityPoint + daysIndex * this.dayWidth, 30.0, 0.0).color(128, 128, 128, 255).endVertex();
                t.end();
                GL11.glEnable(3553);
            }
        }
        super.render(xm, ym, a);
        this.drawCenteredString(RealmsScreen.getLocalizedString("mco.activity.title"), this.width() / 2, 10, 16777215);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(this.toolTip, xm, ym);
        }
        if (this.noActivity) {
            this.drawCenteredString(RealmsScreen.getLocalizedString("mco.activity.noactivity", TimeUnit.DAYS.convert(this.periodInMillis, TimeUnit.MILLISECONDS)), this.width() / 2, this.height() / 2 - 20, 16777215);
        }
    }
    
    protected void renderMousehoverTooltip(final String msg, final int x, final int y) {
        if (msg == null) {
            return;
        }
        int index = 0;
        int width = 0;
        for (final String s : msg.split("\n")) {
            final int the_width = this.fontWidth(s);
            if (the_width > width) {
                width = the_width;
            }
        }
        int rx = x - width - 5;
        final int ry = y;
        if (rx < 0) {
            rx = x + 12;
        }
        for (final String s2 : msg.split("\n")) {
            this.fillGradient(rx - 3, ry - ((index == 0) ? 3 : 0) + index, rx + width + 3, ry + 8 + 3 + index, -1073741824, -1073741824);
            this.fontDrawShadow(s2, rx, ry + index, -1);
            index += 10;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        RealmsActivityScreen.activitiesNameCache = CacheBuilder.newBuilder().build((CacheLoader<? super String, String>)new CacheLoader<String, String>() {
            @Override
            public String load(final String uuid) throws Exception {
                final String name = Realms.uuidToName(uuid);
                if (name == null) {
                    throw new Exception("Couldn't get username");
                }
                return name;
            }
        });
    }
    
    static class Color
    {
        int r;
        int g;
        int b;
        
        Color(final int r, final int g, final int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
    
    static class Day implements Comparable<Day>
    {
        String day;
        Long timestamp;
        
        @Override
        public int compareTo(final Day o) {
            return this.timestamp.compareTo(o.timestamp);
        }
        
        Day(final String day, final Long timestamp) {
            this.day = day;
            this.timestamp = timestamp;
        }
        
        @Override
        public boolean equals(final Object d) {
            if (!(d instanceof Day)) {
                return false;
            }
            final Day that = (Day)d;
            return this.day.equals(that.day);
        }
    }
    
    static class ActivityRow implements Comparable<ActivityRow>
    {
        String key;
        List<Activity> activities;
        Color color;
        String name;
        String uuid;
        
        @Override
        public int compareTo(final ActivityRow o) {
            return this.name.compareTo(o.name);
        }
        
        ActivityRow(final String key, final List<Activity> activities, final String name, final String uuid) {
            this.key = key;
            this.activities = activities;
            this.name = name;
            this.uuid = uuid;
        }
    }
    
    static class Activity implements Comparable<Activity>
    {
        long base;
        long start;
        long end;
        int dayIndex;
        
        private Activity(final long base, final long start, final long end) {
            this.base = base;
            this.start = start;
            this.end = end;
        }
        
        @Override
        public int compareTo(final Activity o) {
            return (int)(this.start - o.start);
        }
        
        public int hourIndice() {
            final String hour = new SimpleDateFormat("HH").format(new Date(this.start));
            return Integer.parseInt(hour);
        }
        
        public int minuteIndice() {
            final String minute = new SimpleDateFormat("mm").format(new Date(this.start));
            return Integer.parseInt(minute);
        }
    }
    
    static class ActivityRender
    {
        double start;
        double width;
        String tooltip;
        
        private ActivityRender(final double start, final double width, final String tooltip) {
            this.start = start;
            this.width = width;
            this.tooltip = tooltip;
        }
    }
    
    class DetailsList extends RealmsScrolledSelectionList
    {
        public DetailsList() {
            super(RealmsActivityScreen.this.width(), RealmsActivityScreen.this.height(), 30, RealmsActivityScreen.this.height() - 40, RealmsActivityScreen.this.fontLineHeight() + 1);
        }
        
        @Override
        public int getItemCount() {
            return RealmsActivityScreen.this.activityMap.size();
        }
        
        @Override
        public void selectItem(final int item, final boolean doubleClick, final int xMouse, final int yMouse) {
        }
        
        @Override
        public boolean isSelectedItem(final int item) {
            return false;
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * (RealmsActivityScreen.this.fontLineHeight() + 1) + 15;
        }
        
        @Override
        protected void renderItem(final int i, final int x, final int y, final int h, final Tezzelator t, final int mouseX, final int mouseY) {
            if (RealmsActivityScreen.this.activityMap != null && RealmsActivityScreen.this.activityMap.size() > i) {
                final ActivityRow row = RealmsActivityScreen.this.activityMap.get(i);
                RealmsActivityScreen.this.drawString(row.name, 20, y, RealmsActivityScreen.this.activityMap.get(i).uuid.equals(Realms.getUUID()) ? 8388479 : 16777215);
                final int r = row.color.r;
                final int g = row.color.g;
                final int b = row.color.b;
                GL11.glDisable(3553);
                t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
                t.vertex(RealmsActivityScreen.this.activityPoint - 8, y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                t.vertex(RealmsActivityScreen.this.activityPoint - 3, y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                t.vertex(RealmsActivityScreen.this.activityPoint - 3, y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                t.vertex(RealmsActivityScreen.this.activityPoint - 8, y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                t.end();
                GL11.glEnable(3553);
                RealmsScreen.bindFace(RealmsActivityScreen.this.activityMap.get(i).uuid, RealmsActivityScreen.this.activityMap.get(i).name);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                RealmsScreen.blit(10, y, 8.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                RealmsScreen.blit(10, y, 40.0f, 8.0f, 8, 8, 8, 8, 64.0f, 64.0f);
                final List<ActivityRender> toRender = new ArrayList<ActivityRender>();
                for (final Activity activity : row.activities) {
                    final int minute = activity.minuteIndice();
                    final int hour = activity.hourIndice();
                    double itemWidth = RealmsActivityScreen.this.minuteWidth * TimeUnit.MINUTES.convert(activity.end - activity.start, TimeUnit.MILLISECONDS);
                    if (itemWidth < 3.0) {
                        itemWidth = 3.0;
                    }
                    final double pos = RealmsActivityScreen.this.activityPoint + (RealmsActivityScreen.this.dayWidth * activity.dayIndex - RealmsActivityScreen.this.dayWidth) + hour * RealmsActivityScreen.this.hourWidth + minute * RealmsActivityScreen.this.minuteWidth;
                    final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                    final Date startDate = new Date(activity.start);
                    final Date endDate = new Date(activity.end);
                    int length = (int)Math.ceil(TimeUnit.SECONDS.convert(activity.end - activity.start, TimeUnit.MILLISECONDS) / 60.0);
                    if (length < 1) {
                        length = 1;
                    }
                    final String tooltip = "[" + format.format(startDate) + " - " + format.format(endDate) + "] " + length + ((length > 1) ? " minutes" : " minute");
                    boolean exists = false;
                    for (final ActivityRender render : toRender) {
                        if (render.start + render.width >= pos - 0.5) {
                            final double overlap = render.start + render.width - pos;
                            final double padding = Math.max(0.0, pos - (render.start + render.width));
                            render.width = render.width - Math.max(0.0, overlap) + itemWidth + padding;
                            final StringBuilder sb = new StringBuilder();
                            final ActivityRender activityRender = render;
                            activityRender.tooltip = sb.append(activityRender.tooltip).append("\n").append(tooltip).toString();
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        toRender.add(new ActivityRender(pos, itemWidth, tooltip));
                    }
                }
                for (final ActivityRender render2 : toRender) {
                    GL11.glDisable(3553);
                    t.begin(7, RealmsDefaultVertexFormat.POSITION_COLOR);
                    t.vertex(render2.start, y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                    t.vertex(render2.start + render2.width, y + 6.5, 0.0).color(r, g, b, 255).endVertex();
                    t.vertex(render2.start + render2.width, y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                    t.vertex(render2.start, y + 1.5, 0.0).color(r, g, b, 255).endVertex();
                    t.end();
                    GL11.glEnable(3553);
                    if (this.xm() >= render2.start && this.xm() <= render2.start + render2.width && this.ym() >= y + 1.5 && this.ym() <= y + 6.5) {
                        RealmsActivityScreen.this.toolTip = render2.tooltip.trim();
                    }
                }
            }
        }
        
        @Override
        public int getScrollbarPosition() {
            return this.width() - 7;
        }
    }
}
