// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import com.google.common.base.Objects;
import com.google.common.base.Predicates;
import com.google.common.base.Predicate;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IntHashMap;
import java.util.List;

public class GuiPageButtonList extends GuiListExtended
{
    private final List<GuiEntry> field_178074_u;
    private final IntHashMap<Gui> field_178073_v;
    private final List<GuiTextField> field_178072_w;
    private final GuiListEntry[][] field_178078_x;
    private int field_178077_y;
    private GuiResponder field_178076_z;
    private Gui field_178075_A;
    
    public GuiPageButtonList(final Minecraft mcIn, final int widthIn, final int heightIn, final int topIn, final int bottomIn, final int slotHeightIn, final GuiResponder p_i45536_7_, final GuiListEntry[]... p_i45536_8_) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.field_178074_u = (List<GuiEntry>)Lists.newArrayList();
        this.field_178073_v = new IntHashMap<Gui>();
        this.field_178072_w = (List<GuiTextField>)Lists.newArrayList();
        this.field_178076_z = p_i45536_7_;
        this.field_178078_x = p_i45536_8_;
        this.field_148163_i = false;
        this.func_178069_s();
        this.func_178055_t();
    }
    
    private void func_178069_s() {
        GuiListEntry[][] field_178078_x;
        for (int length = (field_178078_x = this.field_178078_x).length, j = 0; j < length; ++j) {
            final GuiListEntry[] aguipagebuttonlist$guilistentry = field_178078_x[j];
            for (int i = 0; i < aguipagebuttonlist$guilistentry.length; i += 2) {
                final GuiListEntry guipagebuttonlist$guilistentry = aguipagebuttonlist$guilistentry[i];
                final GuiListEntry guipagebuttonlist$guilistentry2 = (i < aguipagebuttonlist$guilistentry.length - 1) ? aguipagebuttonlist$guilistentry[i + 1] : null;
                final Gui gui = this.func_178058_a(guipagebuttonlist$guilistentry, 0, guipagebuttonlist$guilistentry2 == null);
                final Gui gui2 = this.func_178058_a(guipagebuttonlist$guilistentry2, 160, guipagebuttonlist$guilistentry == null);
                final GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui2);
                this.field_178074_u.add(guipagebuttonlist$guientry);
                if (guipagebuttonlist$guilistentry != null && gui != null) {
                    this.field_178073_v.addKey(guipagebuttonlist$guilistentry.func_178935_b(), gui);
                    if (gui instanceof GuiTextField) {
                        this.field_178072_w.add((GuiTextField)gui);
                    }
                }
                if (guipagebuttonlist$guilistentry2 != null && gui2 != null) {
                    this.field_178073_v.addKey(guipagebuttonlist$guilistentry2.func_178935_b(), gui2);
                    if (gui2 instanceof GuiTextField) {
                        this.field_178072_w.add((GuiTextField)gui2);
                    }
                }
            }
        }
    }
    
    private void func_178055_t() {
        this.field_178074_u.clear();
        for (int i = 0; i < this.field_178078_x[this.field_178077_y].length; i += 2) {
            final GuiListEntry guipagebuttonlist$guilistentry = this.field_178078_x[this.field_178077_y][i];
            final GuiListEntry guipagebuttonlist$guilistentry2 = (i < this.field_178078_x[this.field_178077_y].length - 1) ? this.field_178078_x[this.field_178077_y][i + 1] : null;
            final Gui gui = this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b());
            final Gui gui2 = (guipagebuttonlist$guilistentry2 != null) ? this.field_178073_v.lookup(guipagebuttonlist$guilistentry2.func_178935_b()) : null;
            final GuiEntry guipagebuttonlist$guientry = new GuiEntry(gui, gui2);
            this.field_178074_u.add(guipagebuttonlist$guientry);
        }
    }
    
    public void func_181156_c(final int p_181156_1_) {
        if (p_181156_1_ != this.field_178077_y) {
            final int i = this.field_178077_y;
            this.field_178077_y = p_181156_1_;
            this.func_178055_t();
            this.func_178060_e(i, p_181156_1_);
            this.amountScrolled = 0.0f;
        }
    }
    
    public int func_178059_e() {
        return this.field_178077_y;
    }
    
    public int func_178057_f() {
        return this.field_178078_x.length;
    }
    
    public Gui func_178056_g() {
        return this.field_178075_A;
    }
    
    public void func_178071_h() {
        if (this.field_178077_y > 0) {
            this.func_181156_c(this.field_178077_y - 1);
        }
    }
    
    public void func_178064_i() {
        if (this.field_178077_y < this.field_178078_x.length - 1) {
            this.func_181156_c(this.field_178077_y + 1);
        }
    }
    
    public Gui func_178061_c(final int p_178061_1_) {
        return this.field_178073_v.lookup(p_178061_1_);
    }
    
    private void func_178060_e(final int p_178060_1_, final int p_178060_2_) {
        GuiListEntry[] array;
        for (int length = (array = this.field_178078_x[p_178060_1_]).length, i = 0; i < length; ++i) {
            final GuiListEntry guipagebuttonlist$guilistentry = array[i];
            if (guipagebuttonlist$guilistentry != null) {
                this.func_178066_a(this.field_178073_v.lookup(guipagebuttonlist$guilistentry.func_178935_b()), false);
            }
        }
        GuiListEntry[] array2;
        for (int length2 = (array2 = this.field_178078_x[p_178060_2_]).length, j = 0; j < length2; ++j) {
            final GuiListEntry guipagebuttonlist$guilistentry2 = array2[j];
            if (guipagebuttonlist$guilistentry2 != null) {
                this.func_178066_a(this.field_178073_v.lookup(guipagebuttonlist$guilistentry2.func_178935_b()), true);
            }
        }
    }
    
    private void func_178066_a(final Gui p_178066_1_, final boolean p_178066_2_) {
        if (p_178066_1_ instanceof GuiButton) {
            ((GuiButton)p_178066_1_).visible = p_178066_2_;
        }
        else if (p_178066_1_ instanceof GuiTextField) {
            ((GuiTextField)p_178066_1_).setVisible(p_178066_2_);
        }
        else if (p_178066_1_ instanceof GuiLabel) {
            ((GuiLabel)p_178066_1_).visible = p_178066_2_;
        }
    }
    
    private Gui func_178058_a(final GuiListEntry p_178058_1_, final int p_178058_2_, final boolean p_178058_3_) {
        return (p_178058_1_ instanceof GuiSlideEntry) ? this.func_178067_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiSlideEntry)p_178058_1_) : ((p_178058_1_ instanceof GuiButtonEntry) ? this.func_178065_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiButtonEntry)p_178058_1_) : ((p_178058_1_ instanceof EditBoxEntry) ? this.func_178068_a(this.width / 2 - 155 + p_178058_2_, 0, (EditBoxEntry)p_178058_1_) : ((p_178058_1_ instanceof GuiLabelEntry) ? this.func_178063_a(this.width / 2 - 155 + p_178058_2_, 0, (GuiLabelEntry)p_178058_1_, p_178058_3_) : null)));
    }
    
    public void func_181155_a(final boolean p_181155_1_) {
        for (final GuiEntry guipagebuttonlist$guientry : this.field_178074_u) {
            if (guipagebuttonlist$guientry.field_178029_b instanceof GuiButton) {
                ((GuiButton)guipagebuttonlist$guientry.field_178029_b).enabled = p_181155_1_;
            }
            if (guipagebuttonlist$guientry.field_178030_c instanceof GuiButton) {
                ((GuiButton)guipagebuttonlist$guientry.field_178030_c).enabled = p_181155_1_;
            }
        }
    }
    
    @Override
    public boolean mouseClicked(final int mouseX, final int mouseY, final int mouseEvent) {
        final boolean flag = super.mouseClicked(mouseX, mouseY, mouseEvent);
        final int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
        if (i >= 0) {
            final GuiEntry guipagebuttonlist$guientry = this.getListEntry(i);
            if (this.field_178075_A != guipagebuttonlist$guientry.field_178028_d && this.field_178075_A != null && this.field_178075_A instanceof GuiTextField) {
                ((GuiTextField)this.field_178075_A).setFocused(false);
            }
            this.field_178075_A = guipagebuttonlist$guientry.field_178028_d;
        }
        return flag;
    }
    
    private GuiSlider func_178067_a(final int p_178067_1_, final int p_178067_2_, final GuiSlideEntry p_178067_3_) {
        final GuiSlider guislider = new GuiSlider(this.field_178076_z, p_178067_3_.func_178935_b(), p_178067_1_, p_178067_2_, p_178067_3_.func_178936_c(), p_178067_3_.func_178943_e(), p_178067_3_.func_178944_f(), p_178067_3_.func_178942_g(), p_178067_3_.func_178945_a());
        guislider.visible = p_178067_3_.func_178934_d();
        return guislider;
    }
    
    private GuiListButton func_178065_a(final int p_178065_1_, final int p_178065_2_, final GuiButtonEntry p_178065_3_) {
        final GuiListButton guilistbutton = new GuiListButton(this.field_178076_z, p_178065_3_.func_178935_b(), p_178065_1_, p_178065_2_, p_178065_3_.func_178936_c(), p_178065_3_.func_178940_a());
        guilistbutton.visible = p_178065_3_.func_178934_d();
        return guilistbutton;
    }
    
    private GuiTextField func_178068_a(final int p_178068_1_, final int p_178068_2_, final EditBoxEntry p_178068_3_) {
        final GuiTextField guitextfield = new GuiTextField(p_178068_3_.func_178935_b(), this.mc.fontRendererObj, p_178068_1_, p_178068_2_, 150, 20);
        guitextfield.setText(p_178068_3_.func_178936_c());
        guitextfield.func_175207_a(this.field_178076_z);
        guitextfield.setVisible(p_178068_3_.func_178934_d());
        guitextfield.func_175205_a(p_178068_3_.func_178950_a());
        return guitextfield;
    }
    
    private GuiLabel func_178063_a(final int p_178063_1_, final int p_178063_2_, final GuiLabelEntry p_178063_3_, final boolean p_178063_4_) {
        GuiLabel guilabel;
        if (p_178063_4_) {
            guilabel = new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, this.width - p_178063_1_ * 2, 20, -1);
        }
        else {
            guilabel = new GuiLabel(this.mc.fontRendererObj, p_178063_3_.func_178935_b(), p_178063_1_, p_178063_2_, 150, 20, -1);
        }
        guilabel.visible = p_178063_3_.func_178934_d();
        guilabel.func_175202_a(p_178063_3_.func_178936_c());
        guilabel.setCentered();
        return guilabel;
    }
    
    public void func_178062_a(final char p_178062_1_, final int p_178062_2_) {
        if (this.field_178075_A instanceof GuiTextField) {
            GuiTextField guitextfield = (GuiTextField)this.field_178075_A;
            if (!GuiScreen.isKeyComboCtrlV(p_178062_2_)) {
                if (p_178062_2_ == 15) {
                    guitextfield.setFocused(false);
                    int k = this.field_178072_w.indexOf(this.field_178075_A);
                    if (GuiScreen.isShiftKeyDown()) {
                        if (k == 0) {
                            k = this.field_178072_w.size() - 1;
                        }
                        else {
                            --k;
                        }
                    }
                    else if (k == this.field_178072_w.size() - 1) {
                        k = 0;
                    }
                    else {
                        ++k;
                    }
                    this.field_178075_A = this.field_178072_w.get(k);
                    guitextfield = (GuiTextField)this.field_178075_A;
                    guitextfield.setFocused(true);
                    final int l = guitextfield.yPosition + this.slotHeight;
                    final int i1 = guitextfield.yPosition;
                    if (l > this.bottom) {
                        this.amountScrolled += l - this.bottom;
                    }
                    else if (i1 < this.top) {
                        this.amountScrolled = (float)i1;
                    }
                }
                else {
                    guitextfield.textboxKeyTyped(p_178062_1_, p_178062_2_);
                }
            }
            else {
                final String s = GuiScreen.getClipboardString();
                final String[] astring = s.split(";");
                int m;
                final int j = m = this.field_178072_w.indexOf(this.field_178075_A);
                String[] array;
                for (int length = (array = astring).length, n = 0; n < length; ++n) {
                    final String s2 = array[n];
                    this.field_178072_w.get(m).setText(s2);
                    if (m == this.field_178072_w.size() - 1) {
                        m = 0;
                    }
                    else {
                        ++m;
                    }
                    if (m == j) {
                        break;
                    }
                }
            }
        }
    }
    
    @Override
    public GuiEntry getListEntry(final int index) {
        return this.field_178074_u.get(index);
    }
    
    public int getSize() {
        return this.field_178074_u.size();
    }
    
    @Override
    public int getListWidth() {
        return 400;
    }
    
    @Override
    protected int getScrollBarX() {
        return super.getScrollBarX() + 32;
    }
    
    public static class EditBoxEntry extends GuiListEntry
    {
        private final Predicate<String> field_178951_a;
        
        public EditBoxEntry(final int p_i45534_1_, final String p_i45534_2_, final boolean p_i45534_3_, final Predicate<String> p_i45534_4_) {
            super(p_i45534_1_, p_i45534_2_, p_i45534_3_);
            this.field_178951_a = Objects.firstNonNull(p_i45534_4_, Predicates.alwaysTrue());
        }
        
        public Predicate<String> func_178950_a() {
            return this.field_178951_a;
        }
    }
    
    public static class GuiButtonEntry extends GuiListEntry
    {
        private final boolean field_178941_a;
        
        public GuiButtonEntry(final int p_i45535_1_, final String p_i45535_2_, final boolean p_i45535_3_, final boolean p_i45535_4_) {
            super(p_i45535_1_, p_i45535_2_, p_i45535_3_);
            this.field_178941_a = p_i45535_4_;
        }
        
        public boolean func_178940_a() {
            return this.field_178941_a;
        }
    }
    
    public static class GuiEntry implements IGuiListEntry
    {
        private final Minecraft field_178031_a;
        private final Gui field_178029_b;
        private final Gui field_178030_c;
        private Gui field_178028_d;
        
        public GuiEntry(final Gui p_i45533_1_, final Gui p_i45533_2_) {
            this.field_178031_a = Minecraft.getMinecraft();
            this.field_178029_b = p_i45533_1_;
            this.field_178030_c = p_i45533_2_;
        }
        
        public Gui func_178022_a() {
            return this.field_178029_b;
        }
        
        public Gui func_178021_b() {
            return this.field_178030_c;
        }
        
        @Override
        public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
            this.func_178017_a(this.field_178029_b, y, mouseX, mouseY, false);
            this.func_178017_a(this.field_178030_c, y, mouseX, mouseY, false);
        }
        
        private void func_178017_a(final Gui p_178017_1_, final int p_178017_2_, final int p_178017_3_, final int p_178017_4_, final boolean p_178017_5_) {
            if (p_178017_1_ != null) {
                if (p_178017_1_ instanceof GuiButton) {
                    this.func_178024_a((GuiButton)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                }
                else if (p_178017_1_ instanceof GuiTextField) {
                    this.func_178027_a((GuiTextField)p_178017_1_, p_178017_2_, p_178017_5_);
                }
                else if (p_178017_1_ instanceof GuiLabel) {
                    this.func_178025_a((GuiLabel)p_178017_1_, p_178017_2_, p_178017_3_, p_178017_4_, p_178017_5_);
                }
            }
        }
        
        private void func_178024_a(final GuiButton p_178024_1_, final int p_178024_2_, final int p_178024_3_, final int p_178024_4_, final boolean p_178024_5_) {
            p_178024_1_.yPosition = p_178024_2_;
            if (!p_178024_5_) {
                p_178024_1_.drawButton(this.field_178031_a, p_178024_3_, p_178024_4_);
            }
        }
        
        private void func_178027_a(final GuiTextField p_178027_1_, final int p_178027_2_, final boolean p_178027_3_) {
            p_178027_1_.yPosition = p_178027_2_;
            if (!p_178027_3_) {
                p_178027_1_.drawTextBox();
            }
        }
        
        private void func_178025_a(final GuiLabel p_178025_1_, final int p_178025_2_, final int p_178025_3_, final int p_178025_4_, final boolean p_178025_5_) {
            p_178025_1_.field_146174_h = p_178025_2_;
            if (!p_178025_5_) {
                p_178025_1_.drawLabel(this.field_178031_a, p_178025_3_, p_178025_4_);
            }
        }
        
        @Override
        public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
            this.func_178017_a(this.field_178029_b, p_178011_3_, 0, 0, true);
            this.func_178017_a(this.field_178030_c, p_178011_3_, 0, 0, true);
        }
        
        @Override
        public boolean mousePressed(final int slotIndex, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
            final boolean flag = this.func_178026_a(this.field_178029_b, p_148278_2_, p_148278_3_, p_148278_4_);
            final boolean flag2 = this.func_178026_a(this.field_178030_c, p_148278_2_, p_148278_3_, p_148278_4_);
            return flag || flag2;
        }
        
        private boolean func_178026_a(final Gui p_178026_1_, final int p_178026_2_, final int p_178026_3_, final int p_178026_4_) {
            if (p_178026_1_ == null) {
                return false;
            }
            if (p_178026_1_ instanceof GuiButton) {
                return this.func_178023_a((GuiButton)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            if (p_178026_1_ instanceof GuiTextField) {
                this.func_178018_a((GuiTextField)p_178026_1_, p_178026_2_, p_178026_3_, p_178026_4_);
            }
            return false;
        }
        
        private boolean func_178023_a(final GuiButton p_178023_1_, final int p_178023_2_, final int p_178023_3_, final int p_178023_4_) {
            final boolean flag = p_178023_1_.mousePressed(this.field_178031_a, p_178023_2_, p_178023_3_);
            if (flag) {
                this.field_178028_d = p_178023_1_;
            }
            return flag;
        }
        
        private void func_178018_a(final GuiTextField p_178018_1_, final int p_178018_2_, final int p_178018_3_, final int p_178018_4_) {
            p_178018_1_.mouseClicked(p_178018_2_, p_178018_3_, p_178018_4_);
            if (p_178018_1_.isFocused()) {
                this.field_178028_d = p_178018_1_;
            }
        }
        
        @Override
        public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
            this.func_178016_b(this.field_178029_b, x, y, mouseEvent);
            this.func_178016_b(this.field_178030_c, x, y, mouseEvent);
        }
        
        private void func_178016_b(final Gui p_178016_1_, final int p_178016_2_, final int p_178016_3_, final int p_178016_4_) {
            if (p_178016_1_ != null && p_178016_1_ instanceof GuiButton) {
                this.func_178019_b((GuiButton)p_178016_1_, p_178016_2_, p_178016_3_, p_178016_4_);
            }
        }
        
        private void func_178019_b(final GuiButton p_178019_1_, final int p_178019_2_, final int p_178019_3_, final int p_178019_4_) {
            p_178019_1_.mouseReleased(p_178019_2_, p_178019_3_);
        }
    }
    
    public static class GuiLabelEntry extends GuiListEntry
    {
        public GuiLabelEntry(final int p_i45532_1_, final String p_i45532_2_, final boolean p_i45532_3_) {
            super(p_i45532_1_, p_i45532_2_, p_i45532_3_);
        }
    }
    
    public static class GuiListEntry
    {
        private final int field_178939_a;
        private final String field_178937_b;
        private final boolean field_178938_c;
        
        public GuiListEntry(final int p_i45531_1_, final String p_i45531_2_, final boolean p_i45531_3_) {
            this.field_178939_a = p_i45531_1_;
            this.field_178937_b = p_i45531_2_;
            this.field_178938_c = p_i45531_3_;
        }
        
        public int func_178935_b() {
            return this.field_178939_a;
        }
        
        public String func_178936_c() {
            return this.field_178937_b;
        }
        
        public boolean func_178934_d() {
            return this.field_178938_c;
        }
    }
    
    public static class GuiSlideEntry extends GuiListEntry
    {
        private final GuiSlider.FormatHelper field_178949_a;
        private final float field_178947_b;
        private final float field_178948_c;
        private final float field_178946_d;
        
        public GuiSlideEntry(final int p_i45530_1_, final String p_i45530_2_, final boolean p_i45530_3_, final GuiSlider.FormatHelper p_i45530_4_, final float p_i45530_5_, final float p_i45530_6_, final float p_i45530_7_) {
            super(p_i45530_1_, p_i45530_2_, p_i45530_3_);
            this.field_178949_a = p_i45530_4_;
            this.field_178947_b = p_i45530_5_;
            this.field_178948_c = p_i45530_6_;
            this.field_178946_d = p_i45530_7_;
        }
        
        public GuiSlider.FormatHelper func_178945_a() {
            return this.field_178949_a;
        }
        
        public float func_178943_e() {
            return this.field_178947_b;
        }
        
        public float func_178944_f() {
            return this.field_178948_c;
        }
        
        public float func_178942_g() {
            return this.field_178946_d;
        }
    }
    
    public interface GuiResponder
    {
        void func_175321_a(final int p0, final boolean p1);
        
        void onTick(final int p0, final float p1);
        
        void func_175319_a(final int p0, final String p1);
    }
}
