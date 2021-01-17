/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package me.slowly.client.mod.mods.render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventRender;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.astar.AStarNode;
import me.slowly.client.util.astar.AStarPath;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class Direction
extends Mod {
    public EntityPlayer player;
    private ArrayList<AStarNode> path = new ArrayList();
    private AStarPath pathFinder;
    private boolean startThread = true;
    private Value<Boolean> walk = new Value<Boolean>("Direction_Walk", true);

    public Direction() {
        super("Direction", Mod.Category.RENDER, Colors.RED.c);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        ClientUtil.sendClientMessage("Direction Disable", ClientNotification.Type.ERROR);
    }
    public void onEnable() {
    	super.isEnabled();
        ClientUtil.sendClientMessage("Direction Enable", ClientNotification.Type.SUCCESS);
    }

    @EventTarget
    public void onUpdate(EventPreMotion event) {
        this.setEntity();
        this.updatePathFinder();
        if (this.startThread) {
            this.startThread = false;
            Runnable run = () -> {
                this.setEntity();
                this.updatePathFinder();
                this.pathFinder.doAstar();
                this.path = this.pathFinder.getPath();
                this.startThread = true;
            };
            new Thread(run).start();
        }
        if (this.path.size() > 1 && this.walk.getValueState().booleanValue()) {
            AStarNode lastNode = this.path.get(0);
            if (this.mc.thePlayer.getDistance(lastNode.getX(), lastNode.getY(), lastNode.getZ()) > 3.0) {
                AStarNode node1 = this.path.get(this.path.size() - 1);
                AStarNode node2 = this.path.get(this.path.size() - 2);
                AStarNode node3 = this.path.get(this.path.size() - 3);
                AStarNode pos = this.mc.thePlayer.getDistance(node1.getX(), node1.getY(), node1.getZ()) < 1.5 ? (this.mc.thePlayer.getDistance(node2.getX(), node2.getY(), node2.getZ()) < 1.5 ? node3 : node2) : node1;
                float[] rot = CombatUtil.getRotationsNeededBlock(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                this.mc.thePlayer.rotationYaw = rot[0];
                this.mc.gameSettings.keyBindForward.pressed = true;
                if (this.mc.thePlayer.onGround && (pos.getY() - this.mc.thePlayer.posY > 1.0 || this.mc.thePlayer.isCollidedHorizontally)) {
                    this.mc.thePlayer.jump();
                }
            } else {
                float[] rot = CombatUtil.getRotationsNeededBlock(lastNode.getX(), lastNode.getY() + 1.0, lastNode.getZ());
                this.mc.gameSettings.keyBindForward.pressed = false;
            }
        }
    }

    private void updatePathFinder() {
        this.pathFinder = new AStarPath(new BlockPos((int)this.mc.thePlayer.posX, (int)this.mc.thePlayer.posY, (int)this.mc.thePlayer.posZ), new BlockPos((int)this.player.posX, (int)this.player.posY, (int)this.player.posZ));
    }

    @EventTarget
    public void onRender(EventRender event) {
        if (this.path.size() > 0) {
            this.mc.getRenderManager();
            double x = this.player.lastTickPosX + (this.player.posX - this.player.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = this.player.lastTickPosY + (this.player.posY - this.player.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = this.player.lastTickPosZ + (this.player.posZ - this.player.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            this.mc.getRenderManager();
            double playerX = this.mc.thePlayer.lastTickPosX + (this.mc.thePlayer.posX - this.mc.thePlayer.lastTickPosX) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double playerY = this.mc.thePlayer.lastTickPosY + (this.mc.thePlayer.posY - this.mc.thePlayer.lastTickPosY) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double playerZ = this.mc.thePlayer.lastTickPosZ + (this.mc.thePlayer.posZ - this.mc.thePlayer.lastTickPosZ) * (double)this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glEnable((int)2848);
            GL11.glDisable((int)2929);
            GL11.glDisable((int)3553);
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glLineWidth((float)2.85f);
            RenderUtil.color(FlatColors.YELLOW.c);
            GL11.glLoadIdentity();
            boolean bobbing = this.mc.gameSettings.viewBobbing;
            this.mc.gameSettings.viewBobbing = false;
            this.mc.entityRenderer.orientCamera(this.mc.timer.renderPartialTicks);
            GL11.glBegin((int)3);
            GL11.glVertex3d((double)x, (double)(y + (double)this.player.getEyeHeight()), (double)z);
            GL11.glVertex3d((double)x, (double)y, (double)z);
            int i = 0;
            while (i < this.path.size()) {
                AStarNode node = this.path.get(i);
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                this.mc.getRenderManager();
                GL11.glVertex3d((double)(node.getX() - RenderManager.renderPosX + 0.5), (double)(node.getY() - RenderManager.renderPosY), (double)(node.getZ() - RenderManager.renderPosZ + 0.5));
                ++i;
            }
            GL11.glEnd();
            this.mc.gameSettings.viewBobbing = bobbing;
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GL11.glDisable((int)2848);
            GL11.glDisable((int)3042);
            GL11.glPopMatrix();
        }
    }

    public void setEntity() {
        EntityPlayer newPlayer = null;
        Iterator var3 = this.mc.theWorld.playerEntities.iterator();

        while(var3.hasNext()) {
           EntityPlayer player = (EntityPlayer)var3.next();
           if (this.mc.thePlayer != player && !player.isInvisible() && !player.isDead) {
              if (newPlayer == null) {
                 newPlayer = player;
              } else if (this.mc.thePlayer.getDistanceToEntity(player) < this.mc.thePlayer.getDistanceToEntity(newPlayer)) {
                 newPlayer = player;
              }
           }
        }

        this.player = newPlayer;
     }
  }

