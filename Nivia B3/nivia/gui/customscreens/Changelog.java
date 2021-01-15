package nivia.gui.customscreens;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;
import nivia.Pandora;
import nivia.utils.Helper;
import nivia.utils.utils.RenderUtils;

import java.util.ArrayList;

public class Changelog extends GuiScreen {

    private static int y = 15;
    private static int x = 10;
    private ArrayList<ChangeBox> changes = new ArrayList<>();


    public void drawScreen(int par1, int par2, float par3)
    {
        int xx = y;
        CustomScreenUtils.drawMainBackGround();
        for(ChangeBox box : changes){
            box.drawChangeBox(xx, y, getMaxWidth(box) + 3, 200);
            xx += getMaxWidth(box) + 10;
        }
    }
    public void initGui()
    {
        buttonList.add(new GuiButton(0, width / 2 + 116, height - 24, 75, 20, "Cancel"));
        changes.clear();         
        

        
        
        changes.add(new ChangeBox(7));
        changes.add(new ChangeBox(8));
        changes.add(new ChangeBox(9));
        changes.add(new ChangeBox(10));
        addChanges();
    }
    public void addChanges() {
    	changes.forEach(c -> {
    		if(c.getVersion() == 3) {
                c.addChange(ChangeType.ADD, "Search");
               	 c.addChange(ChangeType.ADD, "Pulse Blink");
               	 c.addChange(ChangeType.ADD, "Teleport");
               	 c.addChange(ChangeType.ADD, "Custom Main Menu");
                    
                }  
                if(c.getVersion() == 4) {
                	 c.addChange(ChangeType.ADD, "ChestStealer");
                	 c.addChange(ChangeType.ADD, "Scaffold");
                	 c.addChange(ChangeType.ADD, "SmoothAim");
                	 c.addChange(ChangeType.ADD, "AutoClicker");
                	 c.addChange(ChangeType.ADD, "Sizes to tabgui: -tg small/big");
                	 c.addChange(ChangeType.ADD, "custom colors to tracers");
                	 c.addChange(ChangeType.ADD, "longhop");
                	 c.addChange(ChangeType.ADD, "Ghost & Misc categories");
                	 c.addChange(ChangeType.IMPROVE, "Tick and autopot");  
                } 
                if(c.getVersion() == 5) {
                	 c.addChange(ChangeType.ADD, "Fov & Teaming option on KA");
                	 c.addChange(ChangeType.ADD, "Latest Speed for new NCP");
                	 c.addChange(ChangeType.ADD, "Waypoints");
                	 c.addChange(ChangeType.ADD, "Outline ESP");
                	 c.addChange(ChangeType.ADD, "Chams Mod");
                	 c.addChange(ChangeType.ADD, "ChangeLog Screen");
                	 c.addChange(ChangeType.FIX, "SmoothAim values");
                } 
                if(c.getVersion() == 6) {
                c.addChange(ChangeType.ADD, "Update Checker");      
               	 c.addChange(ChangeType.ADD, "Nuker");         
               	 c.addChange(ChangeType.ADD, "Paralyze");
               	 c.addChange(ChangeType.ADD, "AntiHunger");
               	 c.addChange(ChangeType.ADD, "TriggerBot");
               	 c.addChange(ChangeType.ADD, "AntiBot on Aura");
               	 c.addChange(ChangeType.ADD, "Ticks Existed on Aura");
                 c.addChange(ChangeType.ADD, "GWEN-Hypixel-AAC Antibot");
                 c.addChange(ChangeType.ADD, "Sunrise & Spirit Themes");
                 c.addChange(ChangeType.ADD, "'Normal' ESP mode");
                 c.addChange(ChangeType.ADD, "Multiaura");
                 c.addChange(ChangeType.FIX, "step randomly stopping");             
                 c.addChange(ChangeType.FIX, "aura not attacking");
                 c.addChange(ChangeType.FIX, "firion kicking (mostly)");
                 c.addChange(ChangeType.IMPROVE, "tick + armorbreaker");
                 c.addChange(ChangeType.IMPROVE, "autopot performance");
               } 
                if(c.getVersion() == 7) {
                    c.addChange(ChangeType.ADD, "Phase Modes");      
                   	 c.addChange(ChangeType.ADD, "Rename Command");         
                   	 c.addChange(ChangeType.ADD, "Rainbow Arraylist");
                   	 c.addChange(ChangeType.ADD, "AutoTool");
                   	 c.addChange(ChangeType.ADD, "AutoACcept");
                   	 c.addChange(ChangeType.ADD, "AutoAccept");
                   	 c.addChange(ChangeType.ADD, "Fly");
                     c.addChange(ChangeType.ADD, "Timer");
                     c.addChange(ChangeType.ADD, "InventoryMove");
                     c.addChange(ChangeType.ADD, "Color * command");
                     c.addChange(ChangeType.ADD, "CPS for AutoClicker");
                     c.addChange(ChangeType.ADD, "LongHop own module");
                     c.addChange(ChangeType.ADD, "StorageESP own module");
                     c.addChange(ChangeType.IMPROVE, "autoarmor detection");
                   } 
                if(c.getVersion() == 8) {
                    c.addChange(ChangeType.ADD, "ClickGui");      
                   	 c.addChange(ChangeType.ADD, "Bypassing BHOP");         
                   	 c.addChange(ChangeType.ADD, "Color customize");
                   	 c.addChange(ChangeType.ADD, "AutoTool option");
                   	 c.addChange(ChangeType.ADD, "AutoACcept option");
                   	 c.addChange(ChangeType.ADD, "NameProtect");
                   	 c.addChange(ChangeType.ADD, "Projectiles");
                     c.addChange(ChangeType.ADD, "CivBreak");
                     c.addChange(ChangeType.ADD, "Refill");
                     c.addChange(ChangeType.ADD, "Crit Modes");
                     c.addChange(ChangeType.ADD, "Throwpot");
                     c.addChange(ChangeType.ADD, "PearlClip cmd");
                     c.addChange(ChangeType.ADD, "Sunrise stuff");
                     c.addChange(ChangeType.FIX, "Aura not targeting");
                     c.addChange(ChangeType.FIX, "Scaffold + jump");
                     c.addChange(ChangeType.FIX, "longhop mod");
                     c.addChange(ChangeType.FIX, "offline names");
                     c.addChange(ChangeType.FIX, "your face");
                     c.addChange(ChangeType.REMOVE, "herobrine");
                   }      
                if(c.getVersion() == 9) {
                	 c.addChange(ChangeType.ADD, "A new ClickGui");      
                   	 c.addChange(ChangeType.ADD, "BowAimbot");         
                   	 c.addChange(ChangeType.ADD, "Rename Command on guis");
                   	 c.addChange(ChangeType.ADD, "Invisibles Option for Tracers");
                   	 c.addChange(ChangeType.ADD, "1.5b Packet step");
                     c.addChange(ChangeType.ADD, "Another BlockHitAnim");
                     c.addChange(ChangeType.FIX, "Scaffold [For real now]");
                     c.addChange(ChangeType.FIX, "Antibot Hypix and Mineplex");

                   }      
                if(c.getVersion() == 10) {
                	c.addChange(ChangeType.ADD, "Dolphin");
                	c.addChange(ChangeType.ADD, "AntiAim");
                	c.addChange(ChangeType.ADD, "Speedmine Modes");
                	c.addChange(ChangeType.REMAKE, "TabGui Sliders");
                	c.addChange(ChangeType.REMAKE, "ClickGui");
                	c.addChange(ChangeType.REMAKE, "Main Menu");
                	c.addChange(ChangeType.REMAKE, "KillAura");

                  }  
    	});
    }

    public int getMaxWidth(ChangeBox changebox) {
        int maxWidth = 0;
        for (Change c : changebox.schanges) {
            if (Helper.get2DUtils().getStringWidth(changebox.getChangeText(c)) > maxWidth)
                maxWidth = Helper.get2DUtils().getStringWidth(changebox.getChangeText(c), 10);
        }
        return maxWidth;
    }

    public class ChangeBox {

        private ArrayList<Change> schanges = new ArrayList<>();
        private double version;

        public ChangeBox(double version) {
            this.version = version;
        }

        public double getVersion() {
            return version;
        }

        public void drawChangeBox(int xx, int y, int width, int height) {
            Helper.get2DUtils().drawRect(xx - 1, y - 1, xx + width - 1, y + getHeight() + 1, Helper.colorUtils().RGBtoHEX(25, 25, 25, 255));
            Helper.get2DUtils().drawRect(xx, y, xx + width - 2, y + getHeight(), Helper.colorUtils().RGBtoHEX(35, 35, 35, 255));
            Helper.get2DUtils().drawCentredStringWithShadow(Pandora.getClientName() + " " + EnumChatFormatting.GOLD +  "b" + (int)getVersion(), xx + width / 2, y + 2, 0xFFFFFF);
            Helper.get2DUtils().drawRect(xx, y - 1 + RenderUtils.comfortaa.getHeight(), xx + width - 2, y + RenderUtils.comfortaa.getHeight(), Helper.colorUtils().RGBtoHEX(0, 0, 0, 255));
            int startY = y + RenderUtils.comfortaa.getHeight() + 3;
            for(Change c : schanges) {
                GL11.glPushMatrix();
                Helper.get2DUtils().drawStringWithShadow(getChangeText(c), xx + 3, startY, 0xFFFFFF, RenderUtils.futuristic);
                GL11.glPopMatrix();
                startY += RenderUtils.comfortaa.getHeight() - 1;
            }
        }
        public String getChangeText(Change c) {
            String prefix;
            switch (c.type) {
                case FIX:
                    prefix = EnumChatFormatting.AQUA + "~ Fixed " + EnumChatFormatting.WHITE;
                    break;
                case ADD:
                    prefix =  EnumChatFormatting.GREEN + "+ Added " + EnumChatFormatting.WHITE;
                    break;
                case REMOVE:
                    prefix =  EnumChatFormatting.RED + "- Removed " + EnumChatFormatting.WHITE;
                    break;
                case REMAKE:
                    prefix =  EnumChatFormatting.DARK_PURPLE + "= Remade " + EnumChatFormatting.WHITE;
                    break;
                case IMPROVE:
                    prefix =  EnumChatFormatting.GOLD + "> Improved " + EnumChatFormatting.WHITE;
                    break;
                case BROKE:
                    prefix =  EnumChatFormatting.DARK_RED + ">> Broke " + EnumChatFormatting.WHITE;
                    break;
                default:
                    prefix = "";
            }
            return prefix + c.change;
        }

        public void addChange(ChangeType type, String c) {
            Change change = new Change(type, c);
            if(!schanges.contains(change))
                schanges.add(change);
        }

        public int getHeight() {
            int height = RenderUtils.futuristic.getHeight();
            for(int i = 0; i < schanges.size(); i++) {
                height += RenderUtils.comfortaa.getHeight() - 1;
            }
            return height;
        }
    }
    public class Change {
        private ChangeType type;
        private String change;

        public Change(ChangeType type, String change) {
            this.type = type;
            this.change = change;
        }
    }

    private enum ChangeType {
        FIX, ADD, REMOVE, REMAKE, IMPROVE, BROKE
    }

}
