package org.m0jang.crystal.Mod.Collection.Render;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.m0jang.crystal.Events.EventPreRenderBlock;
import org.m0jang.crystal.Events.EventRender3D;
import org.m0jang.crystal.Mod.Category;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.RenderUtils;

public class Search extends Module {
   public static ArrayList blocks = new ArrayList();
   public static final boolean[] ids = new boolean[4096];
   public static boolean enabled;

   public Search() {
      super("Search", Category.Render, false);
   }

   public void onEnable() {
      super.onEnable();
      this.mc.renderGlobal.loadRenderers();
      add(56);
      add(14);
      add(15);
      add(21);
      add(73);
      add(74);
      add(129);
   }

   public void onDisable() {
      super.onDisable();
   }

   @EventTarget
   public void preRenderBlock(EventPreRenderBlock e) {
      if (blocks.size() >= 1000) {
         blocks.clear();
      }

      Vector3f vec3 = new Vector3f((float)e.getPos().getX(), (float)e.getPos().getY(), (float)e.getPos().getZ());
      if (!blocks.contains(vec3) && shouldRender(Block.getIdFromBlock(e.getBlock()))) {
         BlockPos pos = new BlockPos((double)vec3.getX(), (double)vec3.getY(), (double)vec3.getZ());
         int id = Block.getIdFromBlock(Minecraft.theWorld.getBlockState(pos).getBlock());
         if (Minecraft.thePlayer.getDistance((double)vec3.getX(), (double)vec3.getY(), (double)vec3.getZ()) <= 128.0D && id != 0) {
            blocks.add(new Vector3f((float)e.getPos().getX(), (float)e.getPos().getY(), (float)e.getPos().getZ()));
         }
      }

   }

   public int getColorForBlock(double posX, double posY, double posZ) {
      int color = -188;
      BlockPos pos = new BlockPos(posX, posY, posZ);
      int id = Block.getIdFromBlock(Minecraft.theWorld.getBlockState(pos).getBlock());
      if (id == 56) {
         color = 9480789;
      }

      if (id == 14) {
         color = -1869610923;
      }

      if (id == 15) {
         color = -2140123051;
      }

      if (id == 16) {
         color = 538976341;
      }

      if (id == 21) {
         color = 3170389;
      }

      if (id == 73) {
         color = 1610612821;
      }

      if (id == 74) {
         color = 1610612821;
      }

      if (id == 129) {
         color = 8396885;
      }

      return color;
   }

   @EventTarget
   public void renderWorld(EventRender3D e) {
      Iterator var3 = blocks.iterator();

      while(var3.hasNext()) {
         Vector3f vec = (Vector3f)var3.next();
         double n = (double)vec.getX();
         double posX = n - RenderManager.renderPosX;
         double n2 = (double)vec.getY();
         double posY = n2 - RenderManager.renderPosY;
         double n3 = (double)vec.getZ();
         double posZ = n3 - RenderManager.renderPosZ;
         if (Minecraft.thePlayer.getDistance((double)vec.getX(), (double)vec.getY(), (double)vec.getZ()) <= 128.0D && shouldRender(Block.getIdFromBlock(Minecraft.theWorld.getBlockState(new BlockPos((double)vec.getX(), (double)vec.getY(), (double)vec.getZ())).getBlock()))) {
            RenderUtils.drawBoundingBoxESP(new AxisAlignedBB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D), 1.5F, this.getColorForBlock((double)vec.getX(), (double)vec.getY(), (double)vec.getZ()));
            RenderUtils.drawFilledBBESP(new AxisAlignedBB(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D), this.getColorForBlock((double)vec.getX(), (double)vec.getY(), (double)vec.getZ()));
         }
      }

      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static boolean contains(int id) {
      return ids[id];
   }

   public static void add(int id) {
      ids[id] = true;
   }

   public static void remove(int id) {
      ids[id] = false;
   }

   public static void clear() {
      for(int i = 0; i < ids.length; ++i) {
         ids[i] = false;
      }

   }

   public static boolean shouldRender(int id) {
      return ids[id];
   }
}
