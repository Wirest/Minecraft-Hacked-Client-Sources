// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.Minecraft;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import java.util.List;

public class SpectatorMenu
{
    private static final ISpectatorMenuObject field_178655_b;
    private static final ISpectatorMenuObject field_178656_c;
    private static final ISpectatorMenuObject field_178653_d;
    private static final ISpectatorMenuObject field_178654_e;
    public static final ISpectatorMenuObject field_178657_a;
    private final ISpectatorMenuRecipient field_178651_f;
    private final List<SpectatorDetails> field_178652_g;
    private ISpectatorMenuView field_178659_h;
    private int field_178660_i;
    private int field_178658_j;
    
    static {
        field_178655_b = new EndSpectatorObject(null);
        field_178656_c = new MoveMenuObject(-1, true);
        field_178653_d = new MoveMenuObject(1, true);
        field_178654_e = new MoveMenuObject(1, false);
        field_178657_a = new ISpectatorMenuObject() {
            @Override
            public void func_178661_a(final SpectatorMenu menu) {
            }
            
            @Override
            public IChatComponent getSpectatorName() {
                return new ChatComponentText("");
            }
            
            @Override
            public void func_178663_a(final float p_178663_1_, final int alpha) {
            }
            
            @Override
            public boolean func_178662_A_() {
                return false;
            }
        };
    }
    
    public SpectatorMenu(final ISpectatorMenuRecipient p_i45497_1_) {
        this.field_178652_g = (List<SpectatorDetails>)Lists.newArrayList();
        this.field_178659_h = new BaseSpectatorGroup();
        this.field_178660_i = -1;
        this.field_178651_f = p_i45497_1_;
    }
    
    public ISpectatorMenuObject func_178643_a(final int p_178643_1_) {
        final int i = p_178643_1_ + this.field_178658_j * 6;
        return (this.field_178658_j > 0 && p_178643_1_ == 0) ? SpectatorMenu.field_178656_c : ((p_178643_1_ == 7) ? ((i < this.field_178659_h.func_178669_a().size()) ? SpectatorMenu.field_178653_d : SpectatorMenu.field_178654_e) : ((p_178643_1_ == 8) ? SpectatorMenu.field_178655_b : ((i >= 0 && i < this.field_178659_h.func_178669_a().size()) ? Objects.firstNonNull(this.field_178659_h.func_178669_a().get(i), SpectatorMenu.field_178657_a) : SpectatorMenu.field_178657_a)));
    }
    
    public List<ISpectatorMenuObject> func_178642_a() {
        final List<ISpectatorMenuObject> list = (List<ISpectatorMenuObject>)Lists.newArrayList();
        for (int i = 0; i <= 8; ++i) {
            list.add(this.func_178643_a(i));
        }
        return list;
    }
    
    public ISpectatorMenuObject func_178645_b() {
        return this.func_178643_a(this.field_178660_i);
    }
    
    public ISpectatorMenuView func_178650_c() {
        return this.field_178659_h;
    }
    
    public void func_178644_b(final int p_178644_1_) {
        final ISpectatorMenuObject ispectatormenuobject = this.func_178643_a(p_178644_1_);
        if (ispectatormenuobject != SpectatorMenu.field_178657_a) {
            if (this.field_178660_i == p_178644_1_ && ispectatormenuobject.func_178662_A_()) {
                ispectatormenuobject.func_178661_a(this);
            }
            else {
                this.field_178660_i = p_178644_1_;
            }
        }
    }
    
    public void func_178641_d() {
        this.field_178651_f.func_175257_a(this);
    }
    
    public int func_178648_e() {
        return this.field_178660_i;
    }
    
    public void func_178647_a(final ISpectatorMenuView p_178647_1_) {
        this.field_178652_g.add(this.func_178646_f());
        this.field_178659_h = p_178647_1_;
        this.field_178660_i = -1;
        this.field_178658_j = 0;
    }
    
    public SpectatorDetails func_178646_f() {
        return new SpectatorDetails(this.field_178659_h, this.func_178642_a(), this.field_178660_i);
    }
    
    static /* synthetic */ void access$0(final SpectatorMenu spectatorMenu, final int field_178658_j) {
        spectatorMenu.field_178658_j = field_178658_j;
    }
    
    static class EndSpectatorObject implements ISpectatorMenuObject
    {
        private EndSpectatorObject() {
        }
        
        @Override
        public void func_178661_a(final SpectatorMenu menu) {
            menu.func_178641_d();
        }
        
        @Override
        public IChatComponent getSpectatorName() {
            return new ChatComponentText("Close menu");
        }
        
        @Override
        public void func_178663_a(final float p_178663_1_, final int alpha) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 128.0f, 0.0f, 16, 16, 256.0f, 256.0f);
        }
        
        @Override
        public boolean func_178662_A_() {
            return true;
        }
    }
    
    static class MoveMenuObject implements ISpectatorMenuObject
    {
        private final int field_178666_a;
        private final boolean field_178665_b;
        
        public MoveMenuObject(final int p_i45495_1_, final boolean p_i45495_2_) {
            this.field_178666_a = p_i45495_1_;
            this.field_178665_b = p_i45495_2_;
        }
        
        @Override
        public void func_178661_a(final SpectatorMenu menu) {
            SpectatorMenu.access$0(menu, this.field_178666_a);
        }
        
        @Override
        public IChatComponent getSpectatorName() {
            return (this.field_178666_a < 0) ? new ChatComponentText("Previous Page") : new ChatComponentText("Next Page");
        }
        
        @Override
        public void func_178663_a(final float p_178663_1_, final int alpha) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
            if (this.field_178666_a < 0) {
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 144.0f, 0.0f, 16, 16, 256.0f, 256.0f);
            }
            else {
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 160.0f, 0.0f, 16, 16, 256.0f, 256.0f);
            }
        }
        
        @Override
        public boolean func_178662_A_() {
            return this.field_178665_b;
        }
    }
}
