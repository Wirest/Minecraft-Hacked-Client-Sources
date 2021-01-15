package saint.modstuff.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderIn3D;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.RenderHelper;
import saint.valuestuff.Value;

public class ESP extends Module {
   private final Value animals = new Value("esp_animals", false);
   private final Value enderpearls = new Value("esp_enderpearls", true);
   private final Value invisibles = new Value("esp_invisibles", true);
   private final Value items = new Value("esp_items", false);
   private final Value mobs = new Value("esp_mobs", false);
   private final Value players = new Value("esp_players", true);
   private final List entities = new ArrayList();
   private int ticks = 0;

   public ESP() {
      super("ESP", -12799119, ModManager.Category.RENDER);
      Saint.getCommandManager().getContentList().add(new Command("esp", "<animals/enderpearls/invisibles/items/mobs/players>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("animals")) {
               ESP.this.animals.setValueState(!(Boolean)ESP.this.animals.getValueState());
               Logger.writeChat("ESP will " + ((Boolean)ESP.this.animals.getValueState() ? "now" : "no longer") + " render a boxes for animals.");
            } else if (message.split(" ")[1].equalsIgnoreCase("enderpearls")) {
               ESP.this.enderpearls.setValueState(!(Boolean)ESP.this.enderpearls.getValueState());
               Logger.writeChat("ESP will " + ((Boolean)ESP.this.enderpearls.getValueState() ? "now" : "no longer") + " render boxes for enderpearls.");
            } else if (message.split(" ")[1].equalsIgnoreCase("invisibles")) {
               ESP.this.invisibles.setValueState(!(Boolean)ESP.this.invisibles.getValueState());
               Logger.writeChat("ESP will " + ((Boolean)ESP.this.invisibles.getValueState() ? "now" : "no longer") + " render boxes for invisible entities.");
            } else if (message.split(" ")[1].equalsIgnoreCase("items")) {
               ESP.this.items.setValueState(!(Boolean)ESP.this.items.getValueState());
               Logger.writeChat("ESP will " + ((Boolean)ESP.this.items.getValueState() ? "now" : "no longer") + " render boxes for items.");
            } else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
               ESP.this.mobs.setValueState(!(Boolean)ESP.this.mobs.getValueState());
               Logger.writeChat("ESP will " + ((Boolean)ESP.this.mobs.getValueState() ? "now" : "no longer") + " render boxes for mobs.");
            } else if (message.split(" ")[1].equalsIgnoreCase("players")) {
               ESP.this.players.setValueState(!(Boolean)ESP.this.players.getValueState());
               Logger.writeChat("ESP will " + ((Boolean)ESP.this.players.getValueState() ? "now" : "no longer") + " render boxes for players.");
            } else {
               Logger.writeChat("Option not valid! Available options: animals, enderpearls, invisibles, items, mobs, players.");
            }

         }
      });
   }

   private boolean isValidTarget(Entity entity) {
      boolean valid = false;
      String ignore = (String)((KillAura)Saint.getModuleManager().getModuleUsingName("killaura")).getIgnore().getValueState();
      if (!ignore.equals("") && entity.getDisplayName().getFormattedText().startsWith("§" + ignore) && entity instanceof EntityPlayer && !Saint.getFriendManager().isFriend(entity.getName())) {
         return false;
      } else if (entity.isInvisible() && !(Boolean)this.invisibles.getValueState()) {
         return false;
      } else {
         if (entity instanceof EntityPlayer && (Boolean)this.players.getValueState()) {
            valid = entity != null && entity != mc.thePlayer && entity.isEntityAlive() && entity.ticksExisted > 20;
         } else if (entity instanceof IMob && (Boolean)this.mobs.getValueState()) {
            valid = entity != null && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= 30.0F && entity.ticksExisted > 20;
         } else if (entity instanceof IAnimals && !(entity instanceof IMob) && (Boolean)this.animals.getValueState()) {
            valid = entity != null && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= 30.0F && entity.ticksExisted > 20;
         } else if (entity instanceof EntityEnderPearl && (Boolean)this.enderpearls.getValueState()) {
            valid = entity != null && entity.isEntityAlive();
         } else if (entity instanceof EntityItem && (Boolean)this.items.getValueState()) {
            valid = entity != null && entity.isEntityAlive() && mc.thePlayer.getDistanceToEntity(entity) <= 30.0F;
         }

         return valid;
      }
   }

   public void onEvent(Event event) {
      if (event instanceof RenderIn3D) {
         Trajectories trajectories = (Trajectories)Saint.getModuleManager().getModuleUsingName("trajectories");
         if (Minecraft.isGuiEnabled()) {
            Iterator var4 = mc.theWorld.loadedEntityList.iterator();

            while(var4.hasNext()) {
               Entity entity = (Entity)var4.next();
               if (entity != null && entity.isEntityAlive()) {
                  this.entities.add(entity);
               }
            }

            RenderIn3D render = (RenderIn3D)event;
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);
            Iterator var5 = mc.theWorld.loadedEntityList.iterator();

            while(true) {
               Entity entity;
               do {
                  if (!var5.hasNext()) {
                     GL11.glDepthMask(true);
                     GL11.glDisable(2848);
                     GL11.glEnable(2929);
                     GL11.glEnable(2896);
                     GL11.glDisable(3042);
                     GL11.glEnable(3553);
                     GL11.glPopMatrix();
                     return;
                  }

                  entity = (Entity)var5.next();
               } while(!this.isValidTarget(entity));

               if (entity.getDistanceToEntity(mc.thePlayer) <= 64.0F) {
                  GL11.glLineWidth(1.5F);
               }

               double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)render.getPartialTicks() - RenderManager.renderPosX;
               double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)render.getPartialTicks() - RenderManager.renderPosY;
               double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)render.getPartialTicks() - RenderManager.renderPosZ;
               GL11.glPushMatrix();
               GL11.glTranslated(posX, posY, posZ);
               GL11.glRotatef(-entity.rotationYaw, 0.0F, entity.height, 0.0F);
               GL11.glTranslated(-posX, -posY, -posZ);
               AxisAlignedBB box = AxisAlignedBB.fromBounds(posX - (double)entity.width, posY, posZ - (double)entity.width, posX + (double)entity.width, posY + (double)entity.height + 0.2D, posZ + (double)entity.width);
               if (entity instanceof EntityLivingBase) {
                  box = AxisAlignedBB.fromBounds(posX - (double)entity.width + 0.2D, posY, posZ - (double)entity.width + 0.2D, posX + (double)entity.width - 0.2D, posY + (double)entity.height + (entity.isSneaking() ? 0.02D : 0.2D), posZ + (double)entity.width - 0.2D);
               }

               float fade = 0.0F;
               ++this.ticks;

               for(int i = 0; i < 100; ++i) {
                  if (this.ticks >= i) {
                     fade += 0.01F;
                  }
               }

               if (this.ticks > 100) {
                  fade = 1.0F;
               }

               if (mc.theWorld == null) {
                  this.ticks = 0;
               }

               if (this.entities.isEmpty()) {
                  this.ticks = 0;
               }

               float distance = mc.thePlayer.getDistanceToEntity(entity);
               Paralyze para = (Paralyze)Saint.getModuleManager().getModuleUsingName("paralyze");
               float[] color;
               if (entity instanceof EntityPlayer && Saint.getFriendManager().isFriend(entity.getName())) {
                  color = new float[]{0.92F, 0.72F, 0.0F};
               } else if (entity.isInvisibleToPlayer(mc.thePlayer)) {
                  color = new float[]{1.0F, 0.9F, 0.0F};
               } else if (para.isEnabled() && (double)distance <= 0.5D) {
                  color = new float[]{2.55F, 2.55F, 0.0F};
               } else if (entity != KillAura.lastAttackedTarget && entity != KillAura.lastAttackedTarget2 && !KillAura.targetsToRender.contains(entity)) {
                  color = new float[]{0.2F, 1.2F, 0.4F};
               } else {
                  color = new float[]{0.0F, 0.4F, 2.55F};
               }

               if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).hurtTime > 0) {
                  color = new float[]{2.55F, 0.0F, 0.0F};
               }

               GL11.glColor4f(color[0], color[1], color[2], fade / 5.0F);
               RenderHelper.drawFilledBox(box);
               GL11.glColor4f(color[0], color[1], color[2], fade);
               RenderHelper.drawOutlinedBoundingBox(box);
               fade = 0.0F;
               GL11.glPopMatrix();
            }
         }
      }
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.theWorld != null) {
         this.ticks = 0;
      }

   }
}
