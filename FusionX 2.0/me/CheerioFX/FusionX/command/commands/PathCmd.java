// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.command.commands;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.Minecraft;
import me.CheerioFX.FusionX.events.Event3D;
import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import com.darkmagician6.eventapi.EventManager;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.utils.EntityUtils2;
import net.minecraft.util.BlockPos;
import me.CheerioFX.FusionX.utils.PathFinder.PathPos;
import java.util.ArrayList;
import me.CheerioFX.FusionX.utils.PathFinder.PathFinder;
import me.CheerioFX.FusionX.command.Command;

public class PathCmd extends Command
{
    private PathFinder pathFinder;
    private ArrayList<PathPos> path;
    private boolean enabled;
    private BlockPos lastGoal;
    private EntityUtils2.TargetSettings targetSettings;
    public boolean debugMode;
    public boolean depthTest;
    private long startTime;
    
    public PathCmd() {
        this.targetSettings = new EntityUtils2.TargetSettings() {
            @Override
            public boolean targetFriends() {
                return true;
            }
            
            @Override
            public boolean targetBehindWalls() {
                return true;
            }
        };
        this.debugMode = false;
        this.depthTest = false;
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        boolean refresh = false;
        if (args.length > 0 && args[0].startsWith("-")) {
            final String s;
            switch (s = args[0]) {
                case "-refresh": {
                    if (this.lastGoal == null) {
                        FusionX.addChatMessage("Cannot refresh: no previous path.");
                    }
                    refresh = true;
                    break;
                }
                case "-debug": {
                    this.debugMode = !this.debugMode;
                    FusionX.addChatMessage("Debug mode " + (this.debugMode ? "on" : "off") + ".");
                    return;
                }
                case "-depth": {
                    this.debugMode = !this.debugMode;
                    FusionX.addChatMessage("Depth test " + (this.depthTest ? "on" : "off") + ".");
                    return;
                }
                default:
                    break;
            }
        }
        if (this.enabled) {
            EventManager.unregister(this);
            this.enabled = false;
            if (args.length == 0) {
                return;
            }
        }
        BlockPos goal;
        if (refresh) {
            goal = this.lastGoal;
        }
        else {
            final int[] posArray = this.argsToPos(this.targetSettings, args);
            goal = new BlockPos(posArray[0], posArray[1], posArray[2]);
            this.lastGoal = goal;
        }
        this.pathFinder = new PathFinder(goal);
        this.path = new ArrayList<PathPos>();
        this.enabled = true;
        EventManager.unregister(this);
        System.out.println("Finding path...");
        this.startTime = System.nanoTime();
        if (args.length > 0) {
            EventManager.register(this);
        }
        else {
            EventManager.unregister(this);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotionUpdates event) {
        final double passedTime = (System.nanoTime() - this.startTime) / 1000000.0;
        this.pathFinder.think();
        final boolean foundPath = this.pathFinder.isDone();
        if (foundPath || this.pathFinder.isFailed()) {
            if (foundPath) {
                this.path = this.pathFinder.formatPath();
            }
            else {
                FusionX.addChatMessage("Could not find a path.");
            }
            EventManager.unregister(this, EventPreMotionUpdates.class);
            System.out.println("Done after " + passedTime + "ms");
            if (this.debugMode) {
                System.out.println("Length: " + this.path.size() + ", processed: " + this.pathFinder.getProcessedBlocks().size() + ", queue: " + this.pathFinder.getQueueSize() + ", cost: " + this.pathFinder.getCost(this.pathFinder.getCurrentPos()));
            }
        }
    }
    
    @EventTarget
    public void onRender(final Event3D event) {
        class Renderer
        {
            void renderArrow(final BlockPos start, final BlockPos end) {
                Minecraft.getMinecraft().getRenderManager();
                final double x = start.getX() + 0.5 - RenderManager.renderPosX;
                Minecraft.getMinecraft().getRenderManager();
                final double y = start.getY() + 0.5 - RenderManager.renderPosY;
                Minecraft.getMinecraft().getRenderManager();
                final double z = start.getZ() + 0.5 - RenderManager.renderPosZ;
                Minecraft.getMinecraft().getRenderManager();
                final double nextX = end.getX() + 0.5 - RenderManager.renderPosX;
                Minecraft.getMinecraft().getRenderManager();
                final double nextY = end.getY() + 0.5 - RenderManager.renderPosY;
                Minecraft.getMinecraft().getRenderManager();
                final double nextZ = end.getZ() + 0.5 - RenderManager.renderPosZ;
                GL11.glBegin(1);
                GL11.glVertex3d(x, y, z);
                GL11.glVertex3d(nextX, nextY, nextZ);
                GL11.glEnd();
                GL11.glPushMatrix();
                GL11.glTranslated(nextX, nextY, nextZ);
                GL11.glScaled(0.0625, 0.0625, 0.0625);
                GL11.glRotated(Math.toDegrees(Math.atan2(nextY - y, z - nextZ)) + 90.0, 1.0, 0.0, 0.0);
                GL11.glRotated(Math.toDegrees(Math.atan2(nextX - x, Math.sqrt(Math.pow(y - nextY, 2.0) + Math.pow(z - nextZ, 2.0)))), 0.0, 0.0, 1.0);
                GL11.glBegin(1);
                GL11.glVertex3d(0.0, 2.0, 1.0);
                GL11.glVertex3d(-1.0, 2.0, 0.0);
                GL11.glVertex3d(-1.0, 2.0, 0.0);
                GL11.glVertex3d(0.0, 2.0, -1.0);
                GL11.glVertex3d(0.0, 2.0, -1.0);
                GL11.glVertex3d(1.0, 2.0, 0.0);
                GL11.glVertex3d(1.0, 2.0, 0.0);
                GL11.glVertex3d(0.0, 2.0, 1.0);
                GL11.glVertex3d(1.0, 2.0, 0.0);
                GL11.glVertex3d(-1.0, 2.0, 0.0);
                GL11.glVertex3d(0.0, 2.0, 1.0);
                GL11.glVertex3d(0.0, 2.0, -1.0);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(1.0, 2.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(-1.0, 2.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 2.0, -1.0);
                GL11.glVertex3d(0.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 2.0, 1.0);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
            
            void renderNode(final BlockPos pos) {
                Minecraft.getMinecraft().getRenderManager();
                final double x = pos.getX() + 0.5 - RenderManager.renderPosX;
                Minecraft.getMinecraft().getRenderManager();
                final double y = pos.getY() + 0.5 - RenderManager.renderPosY;
                Minecraft.getMinecraft().getRenderManager();
                final double z = pos.getZ() + 0.5 - RenderManager.renderPosZ;
                GL11.glPushMatrix();
                GL11.glTranslated(x, y, z);
                GL11.glScaled(0.1, 0.1, 0.1);
                GL11.glBegin(1);
                GL11.glVertex3d(0.0, 0.0, 1.0);
                GL11.glVertex3d(-1.0, 0.0, 0.0);
                GL11.glVertex3d(-1.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, -1.0);
                GL11.glVertex3d(0.0, 0.0, -1.0);
                GL11.glVertex3d(1.0, 0.0, 0.0);
                GL11.glVertex3d(1.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, 1.0);
                GL11.glVertex3d(0.0, 1.0, 0.0);
                GL11.glVertex3d(1.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 1.0, 0.0);
                GL11.glVertex3d(-1.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, 1.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, -1.0);
                GL11.glVertex3d(0.0, 1.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, 1.0);
                GL11.glVertex3d(0.0, -1.0, 0.0);
                GL11.glVertex3d(1.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -1.0, 0.0);
                GL11.glVertex3d(-1.0, 0.0, 0.0);
                GL11.glVertex3d(0.0, -1.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, -1.0);
                GL11.glVertex3d(0.0, -1.0, 0.0);
                GL11.glVertex3d(0.0, 0.0, 1.0);
                GL11.glEnd();
                GL11.glPopMatrix();
            }
        }
        final Renderer renderer = new Renderer();
        int renderedThings = 0;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        if (!this.depthTest) {
            GL11.glDisable(2929);
        }
        GL11.glDisable(2884);
        GL11.glDepthMask(false);
        if (this.debugMode) {
            GL11.glLineWidth(2.0f);
            GL11.glColor4f(1.0f, 1.0f, 0.0f, 0.75f);
            final PathPos[] queue = this.pathFinder.getQueuedBlocks();
            for (int i = 0; i < queue.length && renderedThings < 5000; ++renderedThings, ++i) {
                renderer.renderNode(queue[i]);
            }
            GL11.glLineWidth(2.0f);
            for (final PathPos pos : this.pathFinder.getProcessedBlocks()) {
                if (renderedThings >= 5000) {
                    break;
                }
                if (pos.isJumping()) {
                    GL11.glColor4f(1.0f, 0.0f, 1.0f, 0.75f);
                }
                else {
                    GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.75f);
                }
                renderer.renderArrow(this.pathFinder.getPrevPos(pos), pos);
                ++renderedThings;
            }
        }
        if (this.debugMode) {
            GL11.glLineWidth(4.0f);
            GL11.glColor4f(0.0f, 0.0f, 1.0f, 0.75f);
        }
        else {
            GL11.glLineWidth(2.0f);
            GL11.glColor4f(0.0f, 1.0f, 0.0f, 0.75f);
        }
        for (int j = 0; j < this.path.size() - 1; ++j) {
            final BlockPos pos2 = this.path.get(j);
            final BlockPos nextPos = this.path.get(j + 1);
            renderer.renderArrow(pos2, nextPos);
        }
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public BlockPos getLastGoal() {
        return this.lastGoal;
    }
    
    @Override
    public String getAlias() {
        return "path";
    }
    
    @Override
    public String getDescription() {
        return "Finds and draws the path using the Pathfinder AI.";
    }
    
    @Override
    public String getSyntax() {
        return String.valueOf(FusionX.prefix) + "path <args>";
    }
}
