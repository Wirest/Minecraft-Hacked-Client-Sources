package info.sigmaclient.management.agora;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import info.sigmaclient.Client;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRenderHUD;
import info.sigmaclient.event.impl.EventTick;
import info.sigmaclient.management.agora.component.AgoraChatMessage;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;




public class AgoraModule extends Module {

	private String oldMode = "Minecraft";
    public AgoraModule(ModuleData data) {
        super(data);
    }

    @Override
    public void onEnable() {
    	GuiChat.mode = oldMode;
    	this.setHidden(true);
    }
    @Override
    public void onDisable() {
    	oldMode = GuiChat.mode;
    	GuiChat.mode = "Minecraft";
    }

    @Override
    @RegisterEvent(events = {EventRenderHUD.class})
    public void onEvent(Event event) {
    	if(event instanceof EventRenderHUD){
    		this.setHidden(true);
    		EventRenderHUD er = (EventRenderHUD)event;
    		ScaledResolution res = er.getResolution();
    		if(!GuiChat.mode.equalsIgnoreCase("Agora"))
    			return;
    		Queue<AgoraChatMessage> messages = GuiAgoraIngame.selectedChannel.getMessages();
            ArrayList<AgoraChatMessage>list = new ArrayList<>(messages);
            Collections.reverse(list);
            float scale = 1;//mc.gameSettings.chatScale;
            double ch = 0;
            int s = res.getScaleFactor();
            int width =MathHelper.floor_float(mc.gameSettings.chatWidth * (float) (320- 40) + (float) 40);
            int x2 = MathHelper.ceiling_float_int((float) (width / scale));
            int x1 = 2;
            int y = res.getScaledHeight() - 40, size = list.size();
            int cWheel = GuiAgoraIngame.cWheel;
            TTFFontRenderer font = Client.fm.getFont("SFR 8");
            TTFFontRenderer fontb = Client.fm.getFont("SFB 8");
            GL11.glPushMatrix(); 	
            GlStateManager.translate(0, cWheel, 0.0F);
            GL11.glScissor(0,30*s, (int)(x2) * s, (int)(res.getScaledHeight()-147) * s);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            //TODO SCALE LE CHAT EN FONCTION DU SCALE DU CHAT MC
         //   GL11.glScaled(scale, scale, 1);
          
            for(int i = 0; i< size; i++){
            	AgoraChatMessage mess = list.get(i);
            	//TODO : LE USERNAME EN COULEUR
            	String message =  mess.getUsername() + " : " + mess.getMessage();
            	boolean shouldShow = true; //TODO : SI LE MESSAGE A ETE POSTé YA MOINS DE 10S OU QUE LE GuiAgoraInGame EST OPEN
            	String[] splt = message.split(" ");
            	String[] lines = new String[2000];
        		int off = 0;
        		float xOff = x1;
        		for(int j = 0; j < splt.length; j++){
        			xOff += font.getWidth(splt[j] + " ");
        			if(xOff > x2 && j != 2){
        				off ++;
        				xOff = x1;
        				j--;
        			}else{
        				String add = lines[off] == null ? splt[j] + " " : lines[off] + splt[j] + " ";
            			lines[off] = add;
        			}
        		}
        		for(int j = lines.length-2 ; j >= 0 ; j--){

        			if(lines[j] != null){
        				long age = mess.getAge();
        				if((age < 11000 || mc.currentScreen instanceof GuiAgoraIngame)){
        					
            				GL11.glPushMatrix();
            				GlStateManager.color(0, 0, 0,0);
            				Gui.drawRect(x1, y-1, x2, y+9, Colors.getColor(0, 100));
            				GL11.glPopMatrix();
            				String render = lines[j];
            				String userName = null;
            				float offset = x1+1;
            				if(j == 0){
            					userName = lines[j].split(" ")[0];
            					lines[j] = lines[j].replaceFirst(userName, "");
            					offset += fontb.getWidth(userName);
            				}
            				int c = -1;
            				if(mess.getRank() != null)
            					c = mess.getRank().getColor();
            				if(userName != null){
            					fontb.drawStringWithShadow(userName, x1 +1.3F, y+2, 0xff000000 | c);
            					font.drawStringWithShadow(lines[j], offset, y+2, Colors.getColor(255,255));
            				}else{
            					font.drawStringWithShadow(lines[j], x1 + 2, y+2, Colors.getColor(255,255));
            				}
                			y-= 10;
        				}
        			}
        		}
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GL11.glPopMatrix();

    	}
    }
    
    
}
