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
import org.lwjgl.opengl.GL11;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.RenderTracers;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.valuestuff.Value;

public class Tracers extends Module {
   private final Value animals = new Value("tracers_animals", false);
   private final Value enderpearls = new Value("tracers_enderpearls", true);
   private final Value invisibles = new Value("tracers_invisibles", true);
   private final Value items = new Value("tracers_items", false);
   private final Value mobs = new Value("tracers_mobs", false);
   private final Value players = new Value("tracers_players", true);
   private boolean shouldBob;
   private final List entities = new ArrayList();
   private int ticks = 0;

   public Tracers() {
      super("Tracers", -29696, ModManager.Category.RENDER);
      Saint.getCommandManager().getContentList().add(new Command("tracers", "<animals/enderpearls/invisibles/items/mobs/players>", new String[0]) {
         public void run(String message) {
            if (message.split(" ")[1].equalsIgnoreCase("animals")) {
               Tracers.this.animals.setValueState(!(Boolean)Tracers.this.animals.getValueState());
               Logger.writeChat("Tracers will " + ((Boolean)Tracers.this.animals.getValueState() ? "now" : "no longer") + " render tracers for animals.");
            } else if (message.split(" ")[1].equalsIgnoreCase("enderpearls")) {
               Tracers.this.enderpearls.setValueState(!(Boolean)Tracers.this.enderpearls.getValueState());
               Logger.writeChat("Tracers will " + ((Boolean)Tracers.this.enderpearls.getValueState() ? "now" : "no longer") + " render tracers for enderpearls.");
            } else if (message.split(" ")[1].equalsIgnoreCase("invisibles")) {
               Tracers.this.invisibles.setValueState(!(Boolean)Tracers.this.invisibles.getValueState());
               Logger.writeChat("Tracers will " + ((Boolean)Tracers.this.invisibles.getValueState() ? "now" : "no longer") + " render tracers for invisible entities.");
            } else if (message.split(" ")[1].equalsIgnoreCase("items")) {
               Tracers.this.items.setValueState(!(Boolean)Tracers.this.items.getValueState());
               Logger.writeChat("Tracers will " + ((Boolean)Tracers.this.items.getValueState() ? "now" : "no longer") + " render tracers for items.");
            } else if (message.split(" ")[1].equalsIgnoreCase("mobs")) {
               Tracers.this.mobs.setValueState(!(Boolean)Tracers.this.mobs.getValueState());
               Logger.writeChat("Tracers will " + ((Boolean)Tracers.this.mobs.getValueState() ? "now" : "no longer") + " render tracers for mobs.");
            } else if (message.split(" ")[1].equalsIgnoreCase("players")) {
               Tracers.this.players.setValueState(!(Boolean)Tracers.this.players.getValueState());
               Logger.writeChat("Tracers will " + ((Boolean)Tracers.this.players.getValueState() ? "now" : "no longer") + " render tracers for players.");
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
      if (event instanceof RenderTracers) {
         if (Minecraft.isGuiEnabled()) {
            RenderTracers render = (RenderTracers)event;
            Iterator var4 = mc.theWorld.loadedEntityList.iterator();

            Entity entity;
            while(var4.hasNext()) {
               entity = (Entity)var4.next();
               if (entity != null && entity.isEntityAlive()) {
                  this.entities.add(entity);
               }
            }

            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glDisable(2896);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.2F);
            var4 = mc.theWorld.loadedEntityList.iterator();

            while(true) {
               do {
                  if (!var4.hasNext()) {
                     GL11.glDepthMask(true);
                     GL11.glDisable(2848);
                     GL11.glEnable(2929);
                     GL11.glEnable(2896);
                     GL11.glDisable(3042);
                     GL11.glEnable(3553);
                     GL11.glPopMatrix();
                     return;
                  }

                  entity = (Entity)var4.next();
               } while(!this.isValidTarget(entity));

               double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)render.getPartialTicks() - RenderManager.renderPosX;
               double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)render.getPartialTicks() - RenderManager.renderPosY;
               double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)render.getPartialTicks() - RenderManager.renderPosZ;
               float distance = mc.thePlayer.getDistanceToEntity(entity);
               GL11.glPushMatrix();
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

               float[] color;
               if (entity instanceof EntityPlayer && Saint.getFriendManager().isFriend(entity.getName())) {
                  color = new float[]{1.0F, 0.66F, 0.0F};
               } else if (entity.isInvisibleToPlayer(mc.thePlayer)) {
                  color = new float[]{1.0F, 0.9F, 0.0F};
               } else if (entity != KillAura.lastAttackedTarget && entity != KillAura.lastAttackedTarget2 && !KillAura.targetsToRender.contains(entity)) {
                  color = new float[]{0.2F, 1.2F, 0.4F};
               } else {
                  color = new float[]{0.0F, 0.4F, 2.55F};
               }

               if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).hurtTime > 0) {
                  color = new float[]{2.55F, 0.0F, 0.0F};
               }

               GL11.glColor4f(color[0], color[1], color[2], fade);
               GL11.glBegin(1);
               GL11.glVertex3d(0.0D, (double)mc.thePlayer.getEyeHeight(), 0.0D);
               GL11.glVertex3d(x, y, z);
               GL11.glEnd();
               if (entity instanceof EntityPlayer && !Saint.getModuleManager().getModuleUsingName("esp").isEnabled()) {
                  GL11.glBegin(1);
                  GL11.glVertex3d(x, y, z);
                  GL11.glVertex3d(x, y + 1.0D, z);
                  GL11.glEnd();
               }

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
