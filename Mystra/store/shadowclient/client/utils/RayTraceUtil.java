package store.shadowclient.client.utils;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.util.vector.Vector3f;

public class RayTraceUtil {
   protected Minecraft mc = Minecraft.getMinecraft();
   private float startX;
   private float startY;
   private float startZ;
   private float endX;
   private float endY;
   private float endZ;
   private static final float MAX_STEP = 0.1F;
   private ArrayList positions = new ArrayList();
   private EntityLivingBase entity;

   public RayTraceUtil(EntityLivingBase entity) {
      this.startX = (float)this.mc.thePlayer.posX;
      this.startY = (float)this.mc.thePlayer.posY + 1.0F;
      this.startZ = (float)this.mc.thePlayer.posZ;
      this.endX = (float)entity.posX;
      this.endY = (float)entity.posY + entity.height / 2.0F;
      this.endZ = (float)entity.posZ;
      this.entity = entity;
      this.positions.clear();
      this.addPositions();
   }

   private void addPositions() {
      float diffX = this.endX - this.startX;
      float diffY = this.endY - this.startY;
      float diffZ = this.endZ - this.startZ;
      float currentX = 0.0F;
      float currentY = 1.0F;
      float currentZ = 0.0F;
      int steps = (int)Math.max(Math.abs(diffX) / 0.1F, Math.max(Math.abs(diffY) / 0.1F, Math.abs(diffZ) / 0.1F));

      for(int i = 0; i <= steps; ++i) {
         this.positions.add(new Vector3f(currentX, currentY, currentZ));
         currentX += diffX / (float)steps;
         currentY += diffY / (float)steps;
         currentZ += diffZ / (float)steps;
      }

   }

   private boolean isInBox(Vector3f point, EntityLivingBase target) {
      AxisAlignedBB box = target.getEntityBoundingBox();
      double posX = this.mc.thePlayer.posX + (double)point.x;
      double posY = this.mc.thePlayer.posY + (double)point.y;
      double posZ = this.mc.thePlayer.posZ + (double)point.z;
      boolean x = posX >= box.minX - 0.25D && posX <= box.maxX + 0.25D;
      boolean y = posY >= box.minY && posY <= box.maxY;
      boolean z = posZ >= box.minZ - 0.25D && posZ <= box.maxZ + 0.25D;
      return x && z && y;
   }

   public ArrayList getPositions() {
      return this.positions;
   }

   public EntityLivingBase getEntity() {
      new ArrayList();
      double dist = (double)this.mc.thePlayer.getDistanceToEntity(this.entity);
      EntityLivingBase entity = this.entity;
      Iterator var6 = this.mc.theWorld.loadedEntityList.iterator();

      while(true) {
         EntityLivingBase e;
         do {
            do {
               Object o;
               do {
                  if(!var6.hasNext()) {
                     return entity;
                  }

                  o = var6.next();
               } while(!(o instanceof EntityLivingBase));

               e = (EntityLivingBase)o;
            } while((double)this.mc.thePlayer.getDistanceToEntity(e) >= dist);
         } while(this.mc.thePlayer == e);

         Iterator var9 = this.getPositions().iterator();

         while(var9.hasNext()) {
            Vector3f vec = (Vector3f)var9.next();
            if(this.isInBox(vec, e) && this.mc.thePlayer.getDistanceToEntity(e) < this.mc.thePlayer.getDistanceToEntity(entity)) {
               entity = e;
            }
         }
      }
   }
}
