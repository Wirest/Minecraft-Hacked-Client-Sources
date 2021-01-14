package cn.kody.debug.mod.mods.RENDER;

import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Random;
import java.util.function.Consumer;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventLivingUpdate;
import cn.kody.debug.events.EventRender;
import cn.kody.debug.events.EventRespawn;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.Particles;
import cn.kody.debug.utils.angle.Location;
import cn.kody.debug.utils.render.RenderUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class DamageParticle extends Mod
{
    private HashMap<EntityLivingBase, Float> healthMap;
    private List<Particles> particles;
    
    public DamageParticle() {
        super("DamageParticle", "Damage Particle", Category.RENDER);
        this.healthMap = new HashMap<EntityLivingBase, Float>();
        this.particles = new ArrayList<Particles>();
    }
    
    @EventTarget
    public void onRespawn(EventRespawn class1785) {
        this.particles.clear();
        this.healthMap.clear();
    }
    
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        this.particles.forEach(this::lambda$onUpdate$0);
    }
    
    @EventTarget
    public void onLivingUpdate(EventLivingUpdate class2165) {
        Entity entity = class2165.getEntity();
        if (entity == this.mc.thePlayer) {
            return;
        }
        if (!this.healthMap.containsKey(entity)) {
            this.healthMap.put((EntityLivingBase) entity, ((EntityLivingBase) entity).getHealth());
        }
        float floatValue = this.healthMap.get(entity);
        float health = ((EntityLivingBase) entity).getHealth();
        if (floatValue != health) {
            String p_i1238_2_;
            if (floatValue - health < 0.0f) {
                p_i1238_2_ = "\247a" + roundToPlace((floatValue - health) * -1.0f, 1);
            }
            else {
                p_i1238_2_ = "\247e" + roundToPlace(floatValue - health, 1);
            }
            Location p_i1238_1_ = new Location(entity);
            p_i1238_1_.setY(entity.getEntityBoundingBox().minY + (entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2.0);
            p_i1238_1_.setX(p_i1238_1_.getX() - 0.5 + new Random(System.currentTimeMillis()).nextInt(5) * 0.1);
            p_i1238_1_.setZ(p_i1238_1_.getZ() - 0.5 + new Random(System.currentTimeMillis() + (0x203FF36645D9EA2EL ^ 0x203FF36645D9EA2FL)).nextInt(5) * 0.1);
            this.particles.add(new Particles(p_i1238_1_, p_i1238_2_));
            this.healthMap.remove(entity);
            this.healthMap.put((EntityLivingBase) entity, ((EntityLivingBase) entity).getHealth());
        }
    }
    public static double roundToPlace(double p_roundToPlace_0_,int p_roundToPlace_2_) {
        if (p_roundToPlace_2_ < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(p_roundToPlace_0_).setScale(p_roundToPlace_2_, RoundingMode.HALF_UP).doubleValue();
    }
    
    @EventTarget
    public void onRender(EventRender class1170) {
        for (Particles class1171 : this.particles) {
            double x = class1171.location.getX();
            this.mc.getRenderManager();
            double n = x - RenderManager.renderPosX;
            double y = class1171.location.getY();
            this.mc.getRenderManager();
            double n2 = y - RenderManager.renderPosY;
            double z = class1171.location.getZ();
            this.mc.getRenderManager();
            double n3 = z - RenderManager.renderPosZ;
            GlStateManager.pushMatrix();
            GlStateManager.enablePolygonOffset();
            GlStateManager.doPolygonOffset(1.0f, -1500000.0f);
            GlStateManager.translate((float)n, (float)n2, (float)n3);
            GlStateManager.rotate(-this.mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
            float p_rotate_1_;
            if (this.mc.gameSettings.thirdPersonView == 2) {
                p_rotate_1_ = -1.0f;
            }
            else {
                p_rotate_1_ = 1.0f;
            }
            GlStateManager.rotate(this.mc.getRenderManager().playerViewX, p_rotate_1_, 0.0f, 0.0f);
            double p_scale_4_ = 0.03;
            GlStateManager.scale(-p_scale_4_, -p_scale_4_, p_scale_4_);
            RenderUtil.enableGL2D();
            RenderUtil.disableGL2D();
            GL11.glDepthMask(false);
            this.mc.fontRendererObj.drawStringWithShadow(class1171.text, (float)(-(this.mc.fontRendererObj.getStringWidth(class1171.text) / 2)), (float)(-(this.mc.fontRendererObj.FONT_HEIGHT - 1)), 0);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            GL11.glDepthMask(true);
            GlStateManager.doPolygonOffset(1.0f, 1500000.0f);
            GlStateManager.disablePolygonOffset();
            GlStateManager.popMatrix();
        }
    }
    
    private void lambda$onUpdate$0(Particles Particles) {
        ++Particles.ticks;
        if (Particles.ticks <= 10) {
            Particles.location.setY(Particles.location.getY() + Particles.ticks * 0.005);
        }
        if (Particles.ticks > 20) {
            this.particles.remove(Particles);
        }
    }
}
