package moonx.ohare.client.module.impl.visuals;

import java.awt.Color;

import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.combat.AntiBot;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.ColorValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;


public class Chams extends Module {
    public ColorValue visible = new ColorValue("Visible", new Color(0xff0000).getRGB());
    public ColorValue hidden = new ColorValue("Hidden", new Color(0xff00ff).getRGB());
    public ColorValue visiblefriend = new ColorValue("Visible Friend", new Color(0x3030ff).getRGB());
    public ColorValue hiddenfriend = new ColorValue("Hidden Friend", new Color(0xff2060).getRGB());
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);
    public BooleanValue showArmor = new BooleanValue("ShowArmor", true);
    public BooleanValue colored = new BooleanValue("Colored", true);
    public BooleanValue hands = new BooleanValue("Hands", false);
    public BooleanValue rainbow = new BooleanValue("Raindow", false);
    public Chams() {
        super("Chams", Category.VISUALS, new Color(0xA54DFF).getRGB());
        setDescription("Wallhacks");
    }

    public boolean isValid(EntityLivingBase entity) {
        return isValidType(entity) && !AntiBot.getBots().contains(entity) &&entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }
}
