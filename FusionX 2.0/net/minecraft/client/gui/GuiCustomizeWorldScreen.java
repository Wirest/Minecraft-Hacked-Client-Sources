package net.minecraft.client.gui;

import com.google.common.base.Predicate;
import com.google.common.primitives.Floats;
import java.io.IOException;
import java.util.Random;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

public class GuiCustomizeWorldScreen extends GuiScreen implements GuiSlider.FormatHelper, GuiPageButtonList.GuiResponder
{
    private GuiCreateWorld field_175343_i;
    protected String field_175341_a = "Customize World Settings";
    protected String field_175333_f = "Page 1 of 3";
    protected String field_175335_g = "Basic Settings";
    protected String[] field_175342_h = new String[4];
    private GuiPageButtonList field_175349_r;
    private GuiButton field_175348_s;
    private GuiButton field_175347_t;
    private GuiButton field_175346_u;
    private GuiButton field_175345_v;
    private GuiButton field_175344_w;
    private GuiButton field_175352_x;
    private GuiButton field_175351_y;
    private GuiButton field_175350_z;
    private boolean field_175338_A = false;
    private int field_175339_B = 0;
    private boolean field_175340_C = false;
    private Predicate field_175332_D = new Predicate()
    {
        private static final String __OBFID = "CL_00001933";
        public boolean func_178956_a(String p_178956_1_)
        {
            Float var2 = Floats.tryParse(p_178956_1_);
            return p_178956_1_.length() == 0 || var2 != null && Floats.isFinite(var2.floatValue()) && var2.floatValue() >= 0.0F;
        }
        public boolean apply(Object p_apply_1_)
        {
            return this.func_178956_a((String)p_apply_1_);
        }
    };
    private ChunkProviderSettings.Factory field_175334_E = new ChunkProviderSettings.Factory();
    private ChunkProviderSettings.Factory field_175336_F;
    private Random field_175337_G = new Random();
    private static final String __OBFID = "CL_00001934";

    public GuiCustomizeWorldScreen(GuiScreen p_i45521_1_, String p_i45521_2_)
    {
        this.field_175343_i = (GuiCreateWorld)p_i45521_1_;
        this.func_175324_a(p_i45521_2_);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        this.field_175341_a = I18n.format("options.customizeTitle", new Object[0]);
        this.buttonList.clear();
        this.buttonList.add(this.field_175345_v = new GuiButton(302, 20, 5, 80, 20, I18n.format("createWorld.customize.custom.prev", new Object[0])));
        this.buttonList.add(this.field_175344_w = new GuiButton(303, this.width - 100, 5, 80, 20, I18n.format("createWorld.customize.custom.next", new Object[0])));
        this.buttonList.add(this.field_175346_u = new GuiButton(304, this.width / 2 - 187, this.height - 27, 90, 20, I18n.format("createWorld.customize.custom.defaults", new Object[0])));
        this.buttonList.add(this.field_175347_t = new GuiButton(301, this.width / 2 - 92, this.height - 27, 90, 20, I18n.format("createWorld.customize.custom.randomize", new Object[0])));
        this.buttonList.add(this.field_175350_z = new GuiButton(305, this.width / 2 + 3, this.height - 27, 90, 20, I18n.format("createWorld.customize.custom.presets", new Object[0])));
        this.buttonList.add(this.field_175348_s = new GuiButton(300, this.width / 2 + 98, this.height - 27, 90, 20, I18n.format("gui.done", new Object[0])));
        this.field_175352_x = new GuiButton(306, this.width / 2 - 55, 160, 50, 20, I18n.format("gui.yes", new Object[0]));
        this.field_175352_x.visible = false;
        this.buttonList.add(this.field_175352_x);
        this.field_175351_y = new GuiButton(307, this.width / 2 + 5, 160, 50, 20, I18n.format("gui.no", new Object[0]));
        this.field_175351_y.visible = false;
        this.buttonList.add(this.field_175351_y);
        this.func_175325_f();
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        this.field_175349_r.func_178039_p();
    }

    private void func_175325_f()
    {
        GuiPageButtonList.GuiListEntry[] var1 = new GuiPageButtonList.GuiListEntry[] {new GuiPageButtonList.GuiSlideEntry(160, I18n.format("createWorld.customize.custom.seaLevel", new Object[0]), true, this, 1.0F, 255.0F, (float)this.field_175336_F.field_177929_r), new GuiPageButtonList.GuiButtonEntry(148, I18n.format("createWorld.customize.custom.useCaves", new Object[0]), true, this.field_175336_F.field_177927_s), new GuiPageButtonList.GuiButtonEntry(150, I18n.format("createWorld.customize.custom.useStrongholds", new Object[0]), true, this.field_175336_F.field_177921_v), new GuiPageButtonList.GuiButtonEntry(151, I18n.format("createWorld.customize.custom.useVillages", new Object[0]), true, this.field_175336_F.field_177919_w), new GuiPageButtonList.GuiButtonEntry(152, I18n.format("createWorld.customize.custom.useMineShafts", new Object[0]), true, this.field_175336_F.field_177944_x), new GuiPageButtonList.GuiButtonEntry(153, I18n.format("createWorld.customize.custom.useTemples", new Object[0]), true, this.field_175336_F.field_177942_y), new GuiPageButtonList.GuiButtonEntry(210, I18n.format("createWorld.customize.custom.useMonuments", new Object[0]), true, this.field_175336_F.field_177940_z), new GuiPageButtonList.GuiButtonEntry(154, I18n.format("createWorld.customize.custom.useRavines", new Object[0]), true, this.field_175336_F.field_177870_A), new GuiPageButtonList.GuiButtonEntry(149, I18n.format("createWorld.customize.custom.useDungeons", new Object[0]), true, this.field_175336_F.field_177925_t), new GuiPageButtonList.GuiSlideEntry(157, I18n.format("createWorld.customize.custom.dungeonChance", new Object[0]), true, this, 1.0F, 100.0F, (float)this.field_175336_F.field_177923_u), new GuiPageButtonList.GuiButtonEntry(155, I18n.format("createWorld.customize.custom.useWaterLakes", new Object[0]), true, this.field_175336_F.field_177871_B), new GuiPageButtonList.GuiSlideEntry(158, I18n.format("createWorld.customize.custom.waterLakeChance", new Object[0]), true, this, 1.0F, 100.0F, (float)this.field_175336_F.field_177872_C), new GuiPageButtonList.GuiButtonEntry(156, I18n.format("createWorld.customize.custom.useLavaLakes", new Object[0]), true, this.field_175336_F.field_177866_D), new GuiPageButtonList.GuiSlideEntry(159, I18n.format("createWorld.customize.custom.lavaLakeChance", new Object[0]), true, this, 10.0F, 100.0F, (float)this.field_175336_F.field_177867_E), new GuiPageButtonList.GuiButtonEntry(161, I18n.format("createWorld.customize.custom.useLavaOceans", new Object[0]), true, this.field_175336_F.field_177868_F), new GuiPageButtonList.GuiSlideEntry(162, I18n.format("createWorld.customize.custom.fixedBiome", new Object[0]), true, this, -1.0F, 37.0F, (float)this.field_175336_F.field_177869_G), new GuiPageButtonList.GuiSlideEntry(163, I18n.format("createWorld.customize.custom.biomeSize", new Object[0]), true, this, 1.0F, 8.0F, (float)this.field_175336_F.field_177877_H), new GuiPageButtonList.GuiSlideEntry(164, I18n.format("createWorld.customize.custom.riverSize", new Object[0]), true, this, 1.0F, 5.0F, (float)this.field_175336_F.field_177878_I)};
        GuiPageButtonList.GuiListEntry[] var2 = new GuiPageButtonList.GuiListEntry[] {new GuiPageButtonList.GuiLabelEntry(416, I18n.format("tile.dirt.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(165, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177879_J), new GuiPageButtonList.GuiSlideEntry(166, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177880_K), new GuiPageButtonList.GuiSlideEntry(167, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177873_L), new GuiPageButtonList.GuiSlideEntry(168, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177874_M), new GuiPageButtonList.GuiLabelEntry(417, I18n.format("tile.gravel.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(169, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177875_N), new GuiPageButtonList.GuiSlideEntry(170, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177876_O), new GuiPageButtonList.GuiSlideEntry(171, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177886_P), new GuiPageButtonList.GuiSlideEntry(172, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177885_Q), new GuiPageButtonList.GuiLabelEntry(418, I18n.format("tile.stone.granite.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(173, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177888_R), new GuiPageButtonList.GuiSlideEntry(174, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177887_S), new GuiPageButtonList.GuiSlideEntry(175, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177882_T), new GuiPageButtonList.GuiSlideEntry(176, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177881_U), new GuiPageButtonList.GuiLabelEntry(419, I18n.format("tile.stone.diorite.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(177, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177884_V), new GuiPageButtonList.GuiSlideEntry(178, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177883_W), new GuiPageButtonList.GuiSlideEntry(179, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177891_X), new GuiPageButtonList.GuiSlideEntry(180, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177890_Y), new GuiPageButtonList.GuiLabelEntry(420, I18n.format("tile.stone.andesite.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(181, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177892_Z), new GuiPageButtonList.GuiSlideEntry(182, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177936_aa), new GuiPageButtonList.GuiSlideEntry(183, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177937_ab), new GuiPageButtonList.GuiSlideEntry(184, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177934_ac), new GuiPageButtonList.GuiLabelEntry(421, I18n.format("tile.oreCoal.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(185, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177935_ad), new GuiPageButtonList.GuiSlideEntry(186, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177941_ae), new GuiPageButtonList.GuiSlideEntry(187, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177943_af), new GuiPageButtonList.GuiSlideEntry(189, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177938_ag), new GuiPageButtonList.GuiLabelEntry(422, I18n.format("tile.oreIron.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(190, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177939_ah), new GuiPageButtonList.GuiSlideEntry(191, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177922_ai), new GuiPageButtonList.GuiSlideEntry(192, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177924_aj), new GuiPageButtonList.GuiSlideEntry(193, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177918_ak), new GuiPageButtonList.GuiLabelEntry(423, I18n.format("tile.oreGold.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(194, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177920_al), new GuiPageButtonList.GuiSlideEntry(195, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177930_am), new GuiPageButtonList.GuiSlideEntry(196, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177932_an), new GuiPageButtonList.GuiSlideEntry(197, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177926_ao), new GuiPageButtonList.GuiLabelEntry(424, I18n.format("tile.oreRedstone.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(198, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177928_ap), new GuiPageButtonList.GuiSlideEntry(199, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177908_aq), new GuiPageButtonList.GuiSlideEntry(200, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177906_ar), new GuiPageButtonList.GuiSlideEntry(201, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177904_as), new GuiPageButtonList.GuiLabelEntry(425, I18n.format("tile.oreDiamond.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(202, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177902_at), new GuiPageButtonList.GuiSlideEntry(203, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177916_au), new GuiPageButtonList.GuiSlideEntry(204, I18n.format("createWorld.customize.custom.minHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177914_av), new GuiPageButtonList.GuiSlideEntry(205, I18n.format("createWorld.customize.custom.maxHeight", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177912_aw), new GuiPageButtonList.GuiLabelEntry(426, I18n.format("tile.oreLapis.name", new Object[0]), false), null, new GuiPageButtonList.GuiSlideEntry(206, I18n.format("createWorld.customize.custom.size", new Object[0]), false, this, 1.0F, 50.0F, (float)this.field_175336_F.field_177910_ax), new GuiPageButtonList.GuiSlideEntry(207, I18n.format("createWorld.customize.custom.count", new Object[0]), false, this, 0.0F, 40.0F, (float)this.field_175336_F.field_177897_ay), new GuiPageButtonList.GuiSlideEntry(208, I18n.format("createWorld.customize.custom.center", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177895_az), new GuiPageButtonList.GuiSlideEntry(209, I18n.format("createWorld.customize.custom.spread", new Object[0]), false, this, 0.0F, 255.0F, (float)this.field_175336_F.field_177889_aA)};
        GuiPageButtonList.GuiListEntry[] var3 = new GuiPageButtonList.GuiListEntry[] {new GuiPageButtonList.GuiSlideEntry(100, I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.field_177917_i), new GuiPageButtonList.GuiSlideEntry(101, I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.field_177911_j), new GuiPageButtonList.GuiSlideEntry(102, I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.field_177913_k), new GuiPageButtonList.GuiSlideEntry(103, I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]), false, this, 1.0F, 2000.0F, this.field_175336_F.field_177893_f), new GuiPageButtonList.GuiSlideEntry(104, I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]), false, this, 1.0F, 2000.0F, this.field_175336_F.field_177894_g), new GuiPageButtonList.GuiSlideEntry(105, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]), false, this, 0.01F, 20.0F, this.field_175336_F.field_177915_h), new GuiPageButtonList.GuiSlideEntry(106, I18n.format("createWorld.customize.custom.baseSize", new Object[0]), false, this, 1.0F, 25.0F, this.field_175336_F.field_177907_l), new GuiPageButtonList.GuiSlideEntry(107, I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]), false, this, 1.0F, 6000.0F, this.field_175336_F.field_177899_b), new GuiPageButtonList.GuiSlideEntry(108, I18n.format("createWorld.customize.custom.heightScale", new Object[0]), false, this, 1.0F, 6000.0F, this.field_175336_F.field_177900_c), new GuiPageButtonList.GuiSlideEntry(109, I18n.format("createWorld.customize.custom.stretchY", new Object[0]), false, this, 0.01F, 50.0F, this.field_175336_F.field_177909_m), new GuiPageButtonList.GuiSlideEntry(110, I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.field_177896_d), new GuiPageButtonList.GuiSlideEntry(111, I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]), false, this, 1.0F, 5000.0F, this.field_175336_F.field_177898_e), new GuiPageButtonList.GuiSlideEntry(112, I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]), false, this, 1.0F, 20.0F, this.field_175336_F.field_177903_n), new GuiPageButtonList.GuiSlideEntry(113, I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]), false, this, 0.0F, 20.0F, this.field_175336_F.field_177905_o), new GuiPageButtonList.GuiSlideEntry(114, I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]), false, this, 1.0F, 20.0F, this.field_175336_F.field_177933_p), new GuiPageButtonList.GuiSlideEntry(115, I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]), false, this, 0.0F, 20.0F, this.field_175336_F.field_177931_q)};
        GuiPageButtonList.GuiListEntry[] var4 = new GuiPageButtonList.GuiListEntry[] {new GuiPageButtonList.GuiLabelEntry(400, I18n.format("createWorld.customize.custom.mainNoiseScaleX", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(132, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177917_i)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(401, I18n.format("createWorld.customize.custom.mainNoiseScaleY", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(133, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177911_j)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(402, I18n.format("createWorld.customize.custom.mainNoiseScaleZ", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(134, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177913_k)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(403, I18n.format("createWorld.customize.custom.depthNoiseScaleX", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(135, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177893_f)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(404, I18n.format("createWorld.customize.custom.depthNoiseScaleZ", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(136, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177894_g)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(405, I18n.format("createWorld.customize.custom.depthNoiseScaleExponent", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(137, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177915_h)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(406, I18n.format("createWorld.customize.custom.baseSize", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(138, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177907_l)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(407, I18n.format("createWorld.customize.custom.coordinateScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(139, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177899_b)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(408, I18n.format("createWorld.customize.custom.heightScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(140, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177900_c)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(409, I18n.format("createWorld.customize.custom.stretchY", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(141, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177909_m)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(410, I18n.format("createWorld.customize.custom.upperLimitScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(142, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177896_d)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(411, I18n.format("createWorld.customize.custom.lowerLimitScale", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(143, String.format("%5.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177898_e)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(412, I18n.format("createWorld.customize.custom.biomeDepthWeight", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(144, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177903_n)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(413, I18n.format("createWorld.customize.custom.biomeDepthOffset", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(145, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177905_o)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(414, I18n.format("createWorld.customize.custom.biomeScaleWeight", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(146, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177933_p)}), false, this.field_175332_D), new GuiPageButtonList.GuiLabelEntry(415, I18n.format("createWorld.customize.custom.biomeScaleOffset", new Object[0]) + ":", false), new GuiPageButtonList.EditBoxEntry(147, String.format("%2.3f", new Object[]{Float.valueOf(this.field_175336_F.field_177931_q)}), false, this.field_175332_D)};
        this.field_175349_r = new GuiPageButtonList(this.mc, this.width, this.height, 32, this.height - 32, 25, this, new GuiPageButtonList.GuiListEntry[][] {var1, var2, var3, var4});

        for (int var5 = 0; var5 < 4; ++var5)
        {
            this.field_175342_h[var5] = I18n.format("createWorld.customize.custom.page" + var5, new Object[0]);
        }

        this.func_175328_i();
    }

    public String func_175323_a()
    {
        return this.field_175336_F.toString().replace("\n", "");
    }

    public void func_175324_a(String p_175324_1_)
    {
        if (p_175324_1_ != null && p_175324_1_.length() != 0)
        {
            this.field_175336_F = ChunkProviderSettings.Factory.func_177865_a(p_175324_1_);
        }
        else
        {
            this.field_175336_F = new ChunkProviderSettings.Factory();
        }
    }

    public void func_175319_a(int p_175319_1_, String p_175319_2_)
    {
        float var3 = 0.0F;

        try
        {
            var3 = Float.parseFloat(p_175319_2_);
        }
        catch (NumberFormatException var5)
        {
            ;
        }

        float var4 = 0.0F;

        switch (p_175319_1_)
        {
            case 132:
                var4 = this.field_175336_F.field_177917_i = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
                break;

            case 133:
                var4 = this.field_175336_F.field_177911_j = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
                break;

            case 134:
                var4 = this.field_175336_F.field_177913_k = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
                break;

            case 135:
                var4 = this.field_175336_F.field_177893_f = MathHelper.clamp_float(var3, 1.0F, 2000.0F);
                break;

            case 136:
                var4 = this.field_175336_F.field_177894_g = MathHelper.clamp_float(var3, 1.0F, 2000.0F);
                break;

            case 137:
                var4 = this.field_175336_F.field_177915_h = MathHelper.clamp_float(var3, 0.01F, 20.0F);
                break;

            case 138:
                var4 = this.field_175336_F.field_177907_l = MathHelper.clamp_float(var3, 1.0F, 25.0F);
                break;

            case 139:
                var4 = this.field_175336_F.field_177899_b = MathHelper.clamp_float(var3, 1.0F, 6000.0F);
                break;

            case 140:
                var4 = this.field_175336_F.field_177900_c = MathHelper.clamp_float(var3, 1.0F, 6000.0F);
                break;

            case 141:
                var4 = this.field_175336_F.field_177909_m = MathHelper.clamp_float(var3, 0.01F, 50.0F);
                break;

            case 142:
                var4 = this.field_175336_F.field_177896_d = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
                break;

            case 143:
                var4 = this.field_175336_F.field_177898_e = MathHelper.clamp_float(var3, 1.0F, 5000.0F);
                break;

            case 144:
                var4 = this.field_175336_F.field_177903_n = MathHelper.clamp_float(var3, 1.0F, 20.0F);
                break;

            case 145:
                var4 = this.field_175336_F.field_177905_o = MathHelper.clamp_float(var3, 0.0F, 20.0F);
                break;

            case 146:
                var4 = this.field_175336_F.field_177933_p = MathHelper.clamp_float(var3, 1.0F, 20.0F);
                break;

            case 147:
                var4 = this.field_175336_F.field_177931_q = MathHelper.clamp_float(var3, 0.0F, 20.0F);
        }

        if (var4 != var3 && var3 != 0.0F)
        {
            ((GuiTextField)this.field_175349_r.func_178061_c(p_175319_1_)).setText(this.func_175330_b(p_175319_1_, var4));
        }

        ((GuiSlider)this.field_175349_r.func_178061_c(p_175319_1_ - 132 + 100)).func_175218_a(var4, false);

        if (!this.field_175336_F.equals(this.field_175334_E))
        {
            this.field_175338_A = true;
        }
    }

    public String func_175318_a(int p_175318_1_, String p_175318_2_, float p_175318_3_)
    {
        return p_175318_2_ + ": " + this.func_175330_b(p_175318_1_, p_175318_3_);
    }

    private String func_175330_b(int p_175330_1_, float p_175330_2_)
    {
        switch (p_175330_1_)
        {
            case 100:
            case 101:
            case 102:
            case 103:
            case 104:
            case 107:
            case 108:
            case 110:
            case 111:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 139:
            case 140:
            case 142:
            case 143:
                return String.format("%5.3f", new Object[] {Float.valueOf(p_175330_2_)});

            case 105:
            case 106:
            case 109:
            case 112:
            case 113:
            case 114:
            case 115:
            case 137:
            case 138:
            case 141:
            case 144:
            case 145:
            case 146:
            case 147:
                return String.format("%2.3f", new Object[] {Float.valueOf(p_175330_2_)});

            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 157:
            case 158:
            case 159:
            case 160:
            case 161:
            default:
                return String.format("%d", new Object[] {Integer.valueOf((int)p_175330_2_)});

            case 162:
                if (p_175330_2_ < 0.0F)
                {
                    return I18n.format("gui.all", new Object[0]);
                }
                else
                {
                    BiomeGenBase var3;

                    if ((int)p_175330_2_ >= BiomeGenBase.hell.biomeID)
                    {
                        var3 = BiomeGenBase.getBiomeGenArray()[(int)p_175330_2_ + 2];
                        return var3 != null ? var3.biomeName : "?";
                    }
                    else
                    {
                        var3 = BiomeGenBase.getBiomeGenArray()[(int)p_175330_2_];
                        return var3 != null ? var3.biomeName : "?";
                    }
                }
        }
    }

    public void func_175321_a(int p_175321_1_, boolean p_175321_2_)
    {
        switch (p_175321_1_)
        {
            case 148:
                this.field_175336_F.field_177927_s = p_175321_2_;
                break;

            case 149:
                this.field_175336_F.field_177925_t = p_175321_2_;
                break;

            case 150:
                this.field_175336_F.field_177921_v = p_175321_2_;
                break;

            case 151:
                this.field_175336_F.field_177919_w = p_175321_2_;
                break;

            case 152:
                this.field_175336_F.field_177944_x = p_175321_2_;
                break;

            case 153:
                this.field_175336_F.field_177942_y = p_175321_2_;
                break;

            case 154:
                this.field_175336_F.field_177870_A = p_175321_2_;
                break;

            case 155:
                this.field_175336_F.field_177871_B = p_175321_2_;
                break;

            case 156:
                this.field_175336_F.field_177866_D = p_175321_2_;
                break;

            case 161:
                this.field_175336_F.field_177868_F = p_175321_2_;
                break;

            case 210:
                this.field_175336_F.field_177940_z = p_175321_2_;
        }

        if (!this.field_175336_F.equals(this.field_175334_E))
        {
            this.field_175338_A = true;
        }
    }

    public void func_175320_a(int p_175320_1_, float p_175320_2_)
    {
        switch (p_175320_1_)
        {
            case 100:
                this.field_175336_F.field_177917_i = p_175320_2_;
                break;

            case 101:
                this.field_175336_F.field_177911_j = p_175320_2_;
                break;

            case 102:
                this.field_175336_F.field_177913_k = p_175320_2_;
                break;

            case 103:
                this.field_175336_F.field_177893_f = p_175320_2_;
                break;

            case 104:
                this.field_175336_F.field_177894_g = p_175320_2_;
                break;

            case 105:
                this.field_175336_F.field_177915_h = p_175320_2_;
                break;

            case 106:
                this.field_175336_F.field_177907_l = p_175320_2_;
                break;

            case 107:
                this.field_175336_F.field_177899_b = p_175320_2_;
                break;

            case 108:
                this.field_175336_F.field_177900_c = p_175320_2_;
                break;

            case 109:
                this.field_175336_F.field_177909_m = p_175320_2_;
                break;

            case 110:
                this.field_175336_F.field_177896_d = p_175320_2_;
                break;

            case 111:
                this.field_175336_F.field_177898_e = p_175320_2_;
                break;

            case 112:
                this.field_175336_F.field_177903_n = p_175320_2_;
                break;

            case 113:
                this.field_175336_F.field_177905_o = p_175320_2_;
                break;

            case 114:
                this.field_175336_F.field_177933_p = p_175320_2_;
                break;

            case 115:
                this.field_175336_F.field_177931_q = p_175320_2_;

            case 116:
            case 117:
            case 118:
            case 119:
            case 120:
            case 121:
            case 122:
            case 123:
            case 124:
            case 125:
            case 126:
            case 127:
            case 128:
            case 129:
            case 130:
            case 131:
            case 132:
            case 133:
            case 134:
            case 135:
            case 136:
            case 137:
            case 138:
            case 139:
            case 140:
            case 141:
            case 142:
            case 143:
            case 144:
            case 145:
            case 146:
            case 147:
            case 148:
            case 149:
            case 150:
            case 151:
            case 152:
            case 153:
            case 154:
            case 155:
            case 156:
            case 161:
            case 188:
            default:
                break;

            case 157:
                this.field_175336_F.field_177923_u = (int)p_175320_2_;
                break;

            case 158:
                this.field_175336_F.field_177872_C = (int)p_175320_2_;
                break;

            case 159:
                this.field_175336_F.field_177867_E = (int)p_175320_2_;
                break;

            case 160:
                this.field_175336_F.field_177929_r = (int)p_175320_2_;
                break;

            case 162:
                this.field_175336_F.field_177869_G = (int)p_175320_2_;
                break;

            case 163:
                this.field_175336_F.field_177877_H = (int)p_175320_2_;
                break;

            case 164:
                this.field_175336_F.field_177878_I = (int)p_175320_2_;
                break;

            case 165:
                this.field_175336_F.field_177879_J = (int)p_175320_2_;
                break;

            case 166:
                this.field_175336_F.field_177880_K = (int)p_175320_2_;
                break;

            case 167:
                this.field_175336_F.field_177873_L = (int)p_175320_2_;
                break;

            case 168:
                this.field_175336_F.field_177874_M = (int)p_175320_2_;
                break;

            case 169:
                this.field_175336_F.field_177875_N = (int)p_175320_2_;
                break;

            case 170:
                this.field_175336_F.field_177876_O = (int)p_175320_2_;
                break;

            case 171:
                this.field_175336_F.field_177886_P = (int)p_175320_2_;
                break;

            case 172:
                this.field_175336_F.field_177885_Q = (int)p_175320_2_;
                break;

            case 173:
                this.field_175336_F.field_177888_R = (int)p_175320_2_;
                break;

            case 174:
                this.field_175336_F.field_177887_S = (int)p_175320_2_;
                break;

            case 175:
                this.field_175336_F.field_177882_T = (int)p_175320_2_;
                break;

            case 176:
                this.field_175336_F.field_177881_U = (int)p_175320_2_;
                break;

            case 177:
                this.field_175336_F.field_177884_V = (int)p_175320_2_;
                break;

            case 178:
                this.field_175336_F.field_177883_W = (int)p_175320_2_;
                break;

            case 179:
                this.field_175336_F.field_177891_X = (int)p_175320_2_;
                break;

            case 180:
                this.field_175336_F.field_177890_Y = (int)p_175320_2_;
                break;

            case 181:
                this.field_175336_F.field_177892_Z = (int)p_175320_2_;
                break;

            case 182:
                this.field_175336_F.field_177936_aa = (int)p_175320_2_;
                break;

            case 183:
                this.field_175336_F.field_177937_ab = (int)p_175320_2_;
                break;

            case 184:
                this.field_175336_F.field_177934_ac = (int)p_175320_2_;
                break;

            case 185:
                this.field_175336_F.field_177935_ad = (int)p_175320_2_;
                break;

            case 186:
                this.field_175336_F.field_177941_ae = (int)p_175320_2_;
                break;

            case 187:
                this.field_175336_F.field_177943_af = (int)p_175320_2_;
                break;

            case 189:
                this.field_175336_F.field_177938_ag = (int)p_175320_2_;
                break;

            case 190:
                this.field_175336_F.field_177939_ah = (int)p_175320_2_;
                break;

            case 191:
                this.field_175336_F.field_177922_ai = (int)p_175320_2_;
                break;

            case 192:
                this.field_175336_F.field_177924_aj = (int)p_175320_2_;
                break;

            case 193:
                this.field_175336_F.field_177918_ak = (int)p_175320_2_;
                break;

            case 194:
                this.field_175336_F.field_177920_al = (int)p_175320_2_;
                break;

            case 195:
                this.field_175336_F.field_177930_am = (int)p_175320_2_;
                break;

            case 196:
                this.field_175336_F.field_177932_an = (int)p_175320_2_;
                break;

            case 197:
                this.field_175336_F.field_177926_ao = (int)p_175320_2_;
                break;

            case 198:
                this.field_175336_F.field_177928_ap = (int)p_175320_2_;
                break;

            case 199:
                this.field_175336_F.field_177908_aq = (int)p_175320_2_;
                break;

            case 200:
                this.field_175336_F.field_177906_ar = (int)p_175320_2_;
                break;

            case 201:
                this.field_175336_F.field_177904_as = (int)p_175320_2_;
                break;

            case 202:
                this.field_175336_F.field_177902_at = (int)p_175320_2_;
                break;

            case 203:
                this.field_175336_F.field_177916_au = (int)p_175320_2_;
                break;

            case 204:
                this.field_175336_F.field_177914_av = (int)p_175320_2_;
                break;

            case 205:
                this.field_175336_F.field_177912_aw = (int)p_175320_2_;
                break;

            case 206:
                this.field_175336_F.field_177910_ax = (int)p_175320_2_;
                break;

            case 207:
                this.field_175336_F.field_177897_ay = (int)p_175320_2_;
                break;

            case 208:
                this.field_175336_F.field_177895_az = (int)p_175320_2_;
                break;

            case 209:
                this.field_175336_F.field_177889_aA = (int)p_175320_2_;
        }

        if (p_175320_1_ >= 100 && p_175320_1_ < 116)
        {
            Gui var3 = this.field_175349_r.func_178061_c(p_175320_1_ - 100 + 132);

            if (var3 != null)
            {
                ((GuiTextField)var3).setText(this.func_175330_b(p_175320_1_, p_175320_2_));
            }
        }

        if (!this.field_175336_F.equals(this.field_175334_E))
        {
            this.field_175338_A = true;
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 300:
                    this.field_175343_i.field_146334_a = this.field_175336_F.toString();
                    this.mc.displayGuiScreen(this.field_175343_i);
                    break;

                case 301:
                    for (int var2 = 0; var2 < this.field_175349_r.getSize(); ++var2)
                    {
                        GuiPageButtonList.GuiEntry var3 = this.field_175349_r.func_178070_d(var2);
                        Gui var4 = var3.func_178022_a();

                        if (var4 instanceof GuiButton)
                        {
                            GuiButton var5 = (GuiButton)var4;

                            if (var5 instanceof GuiSlider)
                            {
                                float var6 = ((GuiSlider)var5).func_175217_d() * (0.75F + this.field_175337_G.nextFloat() * 0.5F) + (this.field_175337_G.nextFloat() * 0.1F - 0.05F);
                                ((GuiSlider)var5).func_175219_a(MathHelper.clamp_float(var6, 0.0F, 1.0F));
                            }
                            else if (var5 instanceof GuiListButton)
                            {
                                ((GuiListButton)var5).func_175212_b(this.field_175337_G.nextBoolean());
                            }
                        }

                        Gui var8 = var3.func_178021_b();

                        if (var8 instanceof GuiButton)
                        {
                            GuiButton var9 = (GuiButton)var8;

                            if (var9 instanceof GuiSlider)
                            {
                                float var7 = ((GuiSlider)var9).func_175217_d() * (0.75F + this.field_175337_G.nextFloat() * 0.5F) + (this.field_175337_G.nextFloat() * 0.1F - 0.05F);
                                ((GuiSlider)var9).func_175219_a(MathHelper.clamp_float(var7, 0.0F, 1.0F));
                            }
                            else if (var9 instanceof GuiListButton)
                            {
                                ((GuiListButton)var9).func_175212_b(this.field_175337_G.nextBoolean());
                            }
                        }
                    }

                    return;

                case 302:
                    this.field_175349_r.func_178071_h();
                    this.func_175328_i();
                    break;

                case 303:
                    this.field_175349_r.func_178064_i();
                    this.func_175328_i();
                    break;

                case 304:
                    if (this.field_175338_A)
                    {
                        this.func_175322_b(304);
                    }

                    break;

                case 305:
                    this.mc.displayGuiScreen(new GuiScreenCustomizePresets(this));
                    break;

                case 306:
                    this.func_175331_h();
                    break;

                case 307:
                    this.field_175339_B = 0;
                    this.func_175331_h();
            }
        }
    }

    private void func_175326_g()
    {
        this.field_175336_F.func_177863_a();
        this.func_175325_f();
    }

    private void func_175322_b(int p_175322_1_)
    {
        this.field_175339_B = p_175322_1_;
        this.func_175329_a(true);
    }

    private void func_175331_h() throws IOException
    {
        switch (this.field_175339_B)
        {
            case 300:
                this.actionPerformed((GuiListButton)this.field_175349_r.func_178061_c(300));
                break;

            case 304:
                this.func_175326_g();
        }

        this.field_175339_B = 0;
        this.field_175340_C = true;
        this.func_175329_a(false);
    }

    private void func_175329_a(boolean p_175329_1_)
    {
        this.field_175352_x.visible = p_175329_1_;
        this.field_175351_y.visible = p_175329_1_;
        this.field_175347_t.enabled = !p_175329_1_;
        this.field_175348_s.enabled = !p_175329_1_;
        this.field_175345_v.enabled = !p_175329_1_;
        this.field_175344_w.enabled = !p_175329_1_;
        this.field_175346_u.enabled = !p_175329_1_;
        this.field_175350_z.enabled = !p_175329_1_;
    }

    private void func_175328_i()
    {
        this.field_175345_v.enabled = this.field_175349_r.func_178059_e() != 0;
        this.field_175344_w.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
        this.field_175333_f = I18n.format("book.pageIndicator", new Object[] {Integer.valueOf(this.field_175349_r.func_178059_e() + 1), Integer.valueOf(this.field_175349_r.func_178057_f())});
        this.field_175335_g = this.field_175342_h[this.field_175349_r.func_178059_e()];
        this.field_175347_t.enabled = this.field_175349_r.func_178059_e() != this.field_175349_r.func_178057_f() - 1;
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);

        if (this.field_175339_B == 0)
        {
            switch (keyCode)
            {
                case 200:
                    this.func_175327_a(1.0F);
                    break;

                case 208:
                    this.func_175327_a(-1.0F);
                    break;

                default:
                    this.field_175349_r.func_178062_a(typedChar, keyCode);
            }
        }
    }

    private void func_175327_a(float p_175327_1_)
    {
        Gui var2 = this.field_175349_r.func_178056_g();

        if (var2 instanceof GuiTextField)
        {
            float var3 = p_175327_1_;

            if (GuiScreen.isShiftKeyDown())
            {
                var3 = p_175327_1_ * 0.1F;

                if (GuiScreen.isCtrlKeyDown())
                {
                    var3 *= 0.1F;
                }
            }
            else if (GuiScreen.isCtrlKeyDown())
            {
                var3 = p_175327_1_ * 10.0F;

                if (GuiScreen.func_175283_s())
                {
                    var3 *= 10.0F;
                }
            }

            GuiTextField var4 = (GuiTextField)var2;
            Float var5 = Floats.tryParse(var4.getText());

            if (var5 != null)
            {
                var5 = Float.valueOf(var5.floatValue() + var3);
                int var6 = var4.func_175206_d();
                String var7 = this.func_175330_b(var4.func_175206_d(), var5.floatValue());
                var4.setText(var7);
                this.func_175319_a(var6, var7);
            }
        }
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (this.field_175339_B == 0 && !this.field_175340_C)
        {
            this.field_175349_r.func_148179_a(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        if (this.field_175340_C)
        {
            this.field_175340_C = false;
        }
        else if (this.field_175339_B == 0)
        {
            this.field_175349_r.func_148181_b(mouseX, mouseY, state);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.field_175349_r.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.field_175341_a, this.width / 2, 2, 16777215);
        this.drawCenteredString(this.fontRendererObj, this.field_175333_f, this.width / 2, 12, 16777215);
        this.drawCenteredString(this.fontRendererObj, this.field_175335_g, this.width / 2, 22, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);

        if (this.field_175339_B != 0)
        {
            drawRect(0, 0, this.width, this.height, Integer.MIN_VALUE);
            this.drawHorizontalLine(this.width / 2 - 91, this.width / 2 + 90, 99, -2039584);
            this.drawHorizontalLine(this.width / 2 - 91, this.width / 2 + 90, 185, -6250336);
            this.drawVerticalLine(this.width / 2 - 91, 99, 185, -2039584);
            this.drawVerticalLine(this.width / 2 + 90, 99, 185, -6250336);
            float var4 = 85.0F;
            float var5 = 180.0F;
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator var6 = Tessellator.getInstance();
            WorldRenderer var7 = var6.getWorldRenderer();
            this.mc.getTextureManager().bindTexture(optionsBackground);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float var8 = 32.0F;
            var7.startDrawingQuads();
            var7.func_178991_c(4210752);
            var7.addVertexWithUV((double)(this.width / 2 - 90), 185.0D, 0.0D, 0.0D, 2.65625D);
            var7.addVertexWithUV((double)(this.width / 2 + 90), 185.0D, 0.0D, 5.625D, 2.65625D);
            var7.addVertexWithUV((double)(this.width / 2 + 90), 100.0D, 0.0D, 5.625D, 0.0D);
            var7.addVertexWithUV((double)(this.width / 2 - 90), 100.0D, 0.0D, 0.0D, 0.0D);
            var6.draw();
            this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirmTitle", new Object[0]), this.width / 2, 105, 16777215);
            this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm1", new Object[0]), this.width / 2, 125, 16777215);
            this.drawCenteredString(this.fontRendererObj, I18n.format("createWorld.customize.custom.confirm2", new Object[0]), this.width / 2, 135, 16777215);
            this.field_175352_x.drawButton(this.mc, mouseX, mouseY);
            this.field_175351_y.drawButton(this.mc, mouseX, mouseY);
        }
    }
}
