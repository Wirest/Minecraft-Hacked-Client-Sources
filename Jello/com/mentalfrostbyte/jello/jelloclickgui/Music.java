package com.mentalfrostbyte.jello.jelloclickgui;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringEscapeUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureImpl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mentalfrostbyte.jello.hud.Notification;
import com.mentalfrostbyte.jello.hud.NotificationManager;
import com.mentalfrostbyte.jello.main.Jello;
import com.mentalfrostbyte.jello.main.Module;
import com.mentalfrostbyte.jello.music.image.Resources;
import com.mentalfrostbyte.jello.music.music.ChannelObj;
import com.mentalfrostbyte.jello.music.music.Player;
import com.mentalfrostbyte.jello.tabgui.TabGUI.Cat;
import com.mentalfrostbyte.jello.ttf.GLUtils;
import com.mentalfrostbyte.jello.util.BlurUtil;
import com.mentalfrostbyte.jello.util.FontUtil;
import com.mentalfrostbyte.jello.util.ScaleUtils;
import com.mentalfrostbyte.jello.util.Stencil;
import com.youtube.Channel;
import com.youtube.Item;
import com.youtube.Snippet;
import com.youtube.WebUtils;
import com.youtube.playlist.Playlist;
import com.youtube.search.Search;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.JelloTextField;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Music {

	public static float volumeControl = 1;
	public float x;
	public float y;
	public boolean dragging;
	public float startX;
	public float startY;
	public float lastX;
	public float lastY;
	

	public float scrollY;
	
	public float animatedScrollY;
	public float lastScrollY;
	
	public boolean scrolling;
	public float scrollStartY;
	public float scrollLastY;
	
	public float scroll;
	
	public float scrollTicks;
	public float lastScrollTicks;
	
	public boolean isPlaying;
	
	public List<Channel> channels = Lists.newArrayList();
	public String api_key = "AIzaSyCer_ztDdAmZJqLSX1FsNFkb_t1v29A5Ls";
	public String electroPose = "UCpO0OSNAFLRUpGrNz-bJJHA";
	
	public Channel selectedChannel;
	
	public float scrollTrans;
	public float lastScrollTrans;
	
	public Item selectedSong;
	public int currentSongLength;
	public long currentPosition;
	
	public boolean searchScreen;
	
	public JelloTextField searchBar;
	public Search currentSearch;
	
	public Music(float x, float y) {
		this.x = x;
		this.y = y;

		List<ChannelObj> channels = Arrays.asList(new ChannelObj("Trap Nation","UCa10nxShhzNrCE1o2ZOPztg"), 
				new ChannelObj("Electro Posé","UCpO0OSNAFLRUpGrNz-bJJHA"), 
				new ChannelObj("Chill Nation","UCM9KEEuzacwVlkt9JfJad7g"), 
				new ChannelObj("VEVO","UC2pmfLm7iq6Ov1UwYrWYkZA"), 
				new ChannelObj("MrSuicideSheep","UC5nc_ZtjKW1htCVZVRxlQAQ"), 
				new ChannelObj("Trap City","UC65afEgL62PGFWXY7n6CUbA"), 
				new ChannelObj("CloudKid","UCSa8IUd1uEjlREMa21I3ZPQ"));
		
		for(ChannelObj ch : channels){
			Gson playlist = new Gson();
			Playlist pl = playlist.fromJson(WebUtils.visitSite("https://www.googleapis.com/youtube/v3/channels?id="+ch.id+"&key="+api_key+"&part=contentDetails"), Playlist.class);
			
			
			Gson gson = new Gson();
			Channel request = gson.fromJson(WebUtils.visitSite("https://www.googleapis.com/youtube/v3/playlistItems?part=snippet%2CcontentDetails&maxResults=50&playlistId="+ pl.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads() +"&key="+api_key), Channel.class);
			for(Item v : request.getItems()){
				//if(v.image == null){
					//v.image = Resources.downloadTexture("https://img.youtube.com/vi/"+ v.getSnippet().getResourceId().getVideoId() +"/0.jpg");
					//}
			}
			//selectedChannel = request;
			this.channels.add(request);
		}
		
		
	}
	
	public void makeSearch(String query){
		Gson gson = new Gson();
		Search request = gson.fromJson(WebUtils.visitSite("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=" + query.replace(" ", "%20") + "&type=video&key="+api_key), Search.class);
		this.currentSearch = request;
	}
	
	
	public Channel getChannel(String name){
		for(Channel c : channels){
			if(c.getItems().get(0).getSnippet().getChannelTitle().equals(name)){
				return c;
			}
		}
		return null;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	
	public void mouseReleased(int mouseX, int mouseY, int state){
		dragging = false;
		scrolling = false;
		if(searchBar != null && searchScreen)
			searchBar.mouseReleased(mouseX, mouseY, state);
	}
	 public boolean isHoveringText(String text, float x2, float y2, int mouseX, int mouseY){
	    	return mouseX >= x2 && mouseY >= y2 && mouseX <= FontUtil.jelloFontDuration.getStringWidth(text) + x2 && mouseY <= FontUtil.jelloFontDuration.getHeight() + y2;
	    }
	public void mouseClicked(int mouseX, int mouseY, int mouseButton, JelloGui parent){
		//Collections.reverse(channels.get(0).getItems());
		if(searchBar != null && searchScreen)
			searchBar.mouseClicked(mouseX, mouseY, mouseButton);
		
		if(mouseButton == 1){
			if(this.isHoveringCoords(x, y, 125, 35, mouseX, mouseY)){
				searchScreen = true;
				selectedChannel = null;
			}
		}
		if(mouseButton == 0){
		if(this.isHoveringCoords(x, y, 125, 35, mouseX, mouseY)){
			
			//Player.play("https://w35.convertmp3.io/download/get/?id=de4e6HptLJw&r=VvW5uO0sM2lr1oATxlDlLWHnp5jhYkOG&t=Laxcity+-+Ambitions+%28Flyboy+Remix%29");
			startX = mouseX;
    		startY = mouseY;
    		lastX = x;
    		lastY = y;
    		dragging = true;
		}
		List<String> channels = Arrays.asList("Trap Nation", "ElectroPosé", "Chill Nation", "Vevo", "MrSuicideSheep", "Trap City", "CloudKid");
		for(int render = 0; render < 2; render++){
			int channelCount = 0;
			for(String c : channels){
				GlStateManager.color(1, 1, 1, 1);
				if(this.isHoveringText(c, (float) (x - 9.5f/2f + 0.5f + 125/2f - 0.5f - FontUtil.jelloFontDuration.getStringWidth(c)/2f), y - 3.5f + 93/2f + 0.5f + 20*channelCount - 2, mouseX, mouseY)){
					if(this.getChannel(c) != null){
						selectedChannel = this.getChannel(c);
						searchScreen = false;
						this.currentSearch = null;
						this.searchBar.setText("");
					}
				}
			channelCount++;
			}
		}
		
		if(this.selectedSong != null){
			if(Player.paused){
				if(this.isHoveringCoords(x- 16 + 265, y + 262, 17.5f, 19f, mouseX, mouseY)){
					Player.resume();

				}
				}else{
					if(this.isHoveringCoords(x- 16 + 265, y + 262, 17.5f, 19f, mouseX, mouseY)){
						Player.pause();

					}
				}
		}
		
		if(searchScreen && this.currentSearch != null){
			int xCount = 0;
			int yCount = 0;
			if(searchScreen && this.currentSearch != null){
			for(com.youtube.search.Item v : this.currentSearch.getItems()){
				//if(v.getSnippet().getTitle().contains(" - ")){
					if(xCount < 2){
					xCount++;
				}else{
					yCount++;
					xCount = 0;
				}
				//}
			}
			}
			
			float locationX = 71;
			float locationY = 53/2f - scroll*(yCount*105f - 105);
			xCount = 0;
			yCount = 0;
			if(searchScreen && this.currentSearch != null){
			for(com.youtube.search.Item v : this.currentSearch.getItems()){
				v.lastHover = v.hover;
				//if(v.getSnippet().getTitle().contains(" - ")){
					if(v.image != null){
	            		if(this.isHoveringCoords(x - 4.5f + 125, y - 4.5f, 275,250, mouseX, mouseY) && this.isHoveringCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, Jello.jgui.mouseX, Jello.jgui.mouseY)){
	            			Item i = new Item();
	            			i.setId(v.getId().getVideoId());
	            			i.image = v.image;
	            			i.blurred = v.blurred;
	            			i.hover = v.hover;
	            			i.lastHover = v.lastHover;
	            			i.setKind(v.getKind());
	            			i.setEtag(v.getEtag());
	            			Snippet s = new Snippet();
	            			s.setChannelId(v.getSnippet().getChannelId());
	            			s.setChannelTitle(v.getSnippet().getChannelTitle());
	            			s.setDescription(v.getSnippet().getDescription());
	            			s.setPublishedAt(v.getSnippet().getPublishedAt());
	            			s.setTitle(v.getSnippet().getTitle());
	            			i.setSnippet(s);
	            			this.selectedSong = i;
	            			Player.stop();
	            			WebUtils w = new WebUtils();
	            			w.playMusicLink(v.getId().getVideoId());
	            			NotificationManager.notify("Now Playing", v.getSnippet().getTitle(), 8000, this.selectedSong.image);
	                		
	            		}
					}
				if(xCount < 2){
					xCount++;
				}else{
					yCount++;
					xCount = 0;
				}
				//}
				}
			}
				float newX = x + 298 - 3.5f;
				float newY = y - 32 + 3.5f;
				 float height = (50/((yCount-1)))*10 + 129 - 2.5f;

				    float scrollBarHeight = 20;
				    float percent = (scrollY)/(492/2f - scrollBarHeight);
				    if(percent > 1){
				    	percent = 1;
				    }
				    float dist = 210.5f*percent;
				    
				    float startY = dist - (scrollBarHeight-30)*percent;
				    
				    
				    float endY = -118 + 0.5f + 3 + scrollBarHeight + dist - (scrollBarHeight-30)*percent;
				    
			    if(this.isHoveringCoords(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f + startY, 5.5f, scrollBarHeight + 2.5f+ 2.5f, mouseX, mouseY)){
	    		scrollStartY = mouseY;
	    		scrollLastY = scrollY;
	    		scrolling = true;
			}
			}
		
		if(selectedChannel != null){
		int xCount = 0;
		int yCount = 0;
		if(selectedChannel != null){
		for(Item v : this.selectedChannel.getItems()){
			if(v.getSnippet().getTitle().contains(" - ")){
				if(xCount < 2){
				xCount++;
			}else{
				yCount++;
				xCount = 0;
			}
			}
		}
		}
		
		float locationX = 71;
		float locationY = 53/2f - scroll*(yCount*105f - 105);
		xCount = 0;
		yCount = 0;
		if(selectedChannel != null){
		for(Item v : this.selectedChannel.getItems()){
			v.lastHover = v.hover;
			if(v.getSnippet().getTitle().contains(" - ")){
				if(v.image != null){
            		if(this.isHoveringCoords(x - 4.5f + 125, y - 4.5f, 275,250, mouseX, mouseY) && this.isHoveringCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, Jello.jgui.mouseX, Jello.jgui.mouseY)){
            			this.selectedSong = v;
            			Player.stop();
            			WebUtils w = new WebUtils();
            			w.playMusicLink(v.getSnippet().getResourceId().getVideoId());
            			NotificationManager.notify("Now Playing", v.getSnippet().getTitle(), 8000, this.selectedSong.image);
            		}
				}
			if(xCount < 2){
				xCount++;
			}else{
				yCount++;
				xCount = 0;
			}
			}
			}
		}
			float newX = x + 298 - 3.5f;
			float newY = y - 32 + 3.5f;
			 float height = (50/((yCount-1)))*10 + 129 - 2.5f;

			    float scrollBarHeight = 20;
			    float percent = (scrollY)/(492/2f - scrollBarHeight);
			    if(percent > 1){
			    	percent = 1;
			    }
			    float dist = 210.5f*percent;
			    
			    float startY = dist - (scrollBarHeight-30)*percent;
			    
			    
			    float endY = -118 + 0.5f + 3 + scrollBarHeight + dist - (scrollBarHeight-30)*percent;
			    
		    if(this.isHoveringCoords(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f + startY, 5.5f, scrollBarHeight + 2.5f+ 2.5f, mouseX, mouseY)){
    		scrollStartY = mouseY;
    		scrollLastY = scrollY;
    		scrolling = true;
		}
		}
		}
		
	}
	public void keyTyped(char typedChar, int keyCode) {
		if(searchBar != null && searchScreen && !searchBar.getText().isEmpty() && keyCode == 28){
			this.makeSearch(searchBar.getText());
		}
		if(searchBar != null && searchScreen)
			searchBar.textboxKeyTyped(typedChar, keyCode);
	}
	
	public void onTick(){
		if(searchBar != null && searchScreen)
			searchBar.onTick();
		
		lastScrollTicks = scrollTicks;
		if(scrollTicks > 0){
			scrollTicks--;
		}
		lastScrollTrans = scrollTrans;
		if(scroll > 0){
			if(scrollTrans < 1){
			scrollTrans += 0.5f;
			}
		}else{
			if(scrollTrans > 0){
				scrollTrans -= 0.5f;
			}
		}
		
		lastScrollY = animatedScrollY;
		if(!scrolling){
		animatedScrollY += (((scrollY)-animatedScrollY)/(1.5f))+(scrollY == 0 ? -0.1 : 0.1);
		}
		if(scrollY == 0 && animatedScrollY < 0.25f){
			//animatedScrollY = 0;
			//animatedScrollY = scrollY;
		}
		if(animatedScrollY < 0){
			animatedScrollY = 0;
		}
		/*if(scrollY > animatedScrollY){
			animatedScrollY++;
			if(scrollY > animatedScrollY){
				animatedScrollY++;
			}
		}else if(scrollY < animatedScrollY){
			animatedScrollY--;
			if(scrollY < animatedScrollY){
				animatedScrollY--;
				
			}
		}*/
		
		if(scrollTrans > 1){
			scrollTrans = 1;
		}
		if(scrollTrans < 0){
			scrollTrans = 0;
		}
		
		if(searchScreen && this.currentSearch != null){
			int xCount = 0;
			int yCount = 0;
			if(searchScreen && this.currentSearch != null){
			for(com.youtube.search.Item v : this.currentSearch.getItems()){
				//if(v.getSnippet().getTitle().contains(" - ")){
					if(xCount < 2){
					xCount++;
				}else{
					yCount++;
					xCount = 0;
				}
				//}
			}
			}
			
			float locationX = 71;
			float locationY = 53/2f - scroll*(yCount*105f - 105);
			xCount = 0;
			yCount = 0;
			if(searchScreen && this.currentSearch != null){
			for(com.youtube.search.Item v : this.currentSearch.getItems()){
				v.lastHover = v.hover;
				//if(v.getSnippet().getTitle().contains(" - ")){
					if(v.image != null){
	            		if(this.isHoveringCoords(x - 4.5f + 125, y - 4.5f, 275,250, Jello.jgui.mouseX, Jello.jgui.mouseY) && this.isHoveringCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, Jello.jgui.mouseX, Jello.jgui.mouseY)){
	            			if(v.hover < 1){
	            				v.hover += 0.5;
	            			}
	            		}else{
	            			if(v.hover > 0){
	            				v.hover -= 0.5;
	            			}
	            		}
	            		if(v.hover > 1){
	            			v.hover = 1;
	            		}
	            		if(v.hover < 0){
	            			v.hover = 0;
	            		}
					}
				if(xCount < 2){
					xCount++;
				}else{
					yCount++;
					xCount = 0;
				}
				//}
			}
			}
			}
		if(selectedChannel != null){
		int xCount = 0;
		int yCount = 0;
		if(selectedChannel != null){
		for(Item v : this.selectedChannel.getItems()){
			if(v.getSnippet().getTitle().contains(" - ")){
				if(xCount < 2){
				xCount++;
			}else{
				yCount++;
				xCount = 0;
			}
			}
		}
		}
		
		float locationX = 71;
		float locationY = 53/2f - scroll*(yCount*105f - 105);
		xCount = 0;
		yCount = 0;
		if(selectedChannel != null){
		for(Item v : this.selectedChannel.getItems()){
			v.lastHover = v.hover;
			if(v.getSnippet().getTitle().contains(" - ")){
				if(v.image != null){
            		if(this.isHoveringCoords(x - 4.5f + 125, y - 4.5f, 275,250, Jello.jgui.mouseX, Jello.jgui.mouseY) && this.isHoveringCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, Jello.jgui.mouseX, Jello.jgui.mouseY)){
            			if(v.hover < 1){
            				v.hover += 0.5;
            			}
            		}else{
            			if(v.hover > 0){
            				v.hover -= 0.5;
            			}
            		}
            		if(v.hover > 1){
            			v.hover = 1;
            		}
            		if(v.hover < 0){
            			v.hover = 0;
            		}
				}
			if(xCount < 2){
				xCount++;
			}else{
				yCount++;
				xCount = 0;
			}
			}
		}
		}
		}
		
	}
	public float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
	}
	public void drawPanel(int mouseX, int mouseY, float opacity, boolean hideShadow, float leftIntro, float rightIntro){
		GlStateManager.disableAlpha();
		if(dragging){
    		x = lastX + mouseX - startX;
    		y = lastY + mouseY - startY;
    		if(currentSearch == null){
    		searchBar.xPosition = x + 121 + 15.5f;
			searchBar.yPosition = y - 9 + 20 - 1.5f - scroll*((1)*105f - 105);
    		}
    	}
		
		GlStateManager.color(1, 1, 1, opacity);
		
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		int white = new Color(1,1,1, opacity).getRGB();
		
		if(this.selectedSong != null){
			GlStateManager.color(1, 1, 1, 1);
		GlStateManager.color(1, 1, 1, opacity);
		
			//this.drawTexturedRect(sr.getScaledWidth() - 31/2f - 9, sr.getScaledHeight() - 34/2f - 9, 31/2f, 34/2f, "ambient play", sr);
		}
		
		this.drawTexturedRect(x - 16, y - 16, 423f, 323f, "jello music background", sr);
		if(searchScreen){
			GlStateManager.color(1, 1, 1, opacity);
			//searchBar.xPosition = x + 121 + 15.5f;
			//searchBar.yPosition = y - 9 + 20 - 1.5f - scroll*(5*105f - 105);
			//searchBar.width = 50;
			Stencil.write(false);
			Gui.drawFloatRect(x - 4.5f + 125, y - 4.5f, x + 395.5f, y + 248.5f, -1);
			Stencil.erase(true);
			GlStateManager.disableAlpha();
	    	GlStateManager.enableBlend();
	    	GL11.glEnable(3042);
	    	GL11.glColor4f(1, 1, 1, 1);
			this.searchBar.drawTextBox(mouseX, mouseY);
			Stencil.dispose();
		}
		if(selectedSong != null){
			//String title = //"Bored";//
				//	StringEscapeUtils.unescapeHtml4(selectedSong.getSnippet().getTitle().split(" - ")[1]);
			//String artist = //"Billie Eilish";//
			//		StringEscapeUtils.unescapeHtml4(selectedSong.getSnippet().getTitle().split(" - ")[0]);
			String title = //"Bored";//
					selectedSong.getSnippet().getTitle().contains(" - ") ? StringEscapeUtils.unescapeHtml4(selectedSong.getSnippet().getTitle().split(" - ")[1]) : StringEscapeUtils.unescapeHtml4(selectedSong.getSnippet().getTitle());
			String artist = //"Billie Eilish";//
					selectedSong.getSnippet().getTitle().contains(" - ") ? StringEscapeUtils.unescapeHtml4(selectedSong.getSnippet().getTitle().split(" - ")[0]) : StringEscapeUtils.unescapeHtml4(selectedSong.getSnippet().getChannelTitle());
			
			GlStateManager.color(1, 1, 1, 1);
			TextureImpl.bindNone();
			GlStateManager.color(1, 1, 1, 1);
			GlStateManager.color(opacity, opacity, opacity, opacity);
			//if(opacity >= 0.8){
			selectedSong.image.rectTextureMaskedBanner(this.x + 91/2f - 11.5f - 4.5f - 68/2f, this.y + 453/2f - 11.5f - 4.5f + 76/2f, 400, 47, 0, 0.001f);
			//}
			Gui.drawFloatRect(x- 16 + 9 + 2.5f, y- 16 + 561/2f - 16, x- 16 + 12 + 250/2f + 274.5f , y- 16 + 561/2f + 28.5f , new Color(0, 0, 0, 0.4f*opacity).getRGB());
			Gui.drawFloatRect(x- 16 + 9 + 2.5f, y- 16 + 561/2f + 28.5f, x- 16 + 12 + 250/2f , y- 16 + 561/2f + 28.5f + 2.5f, new Color(0, 0, 0, 0.4f*opacity).getRGB());
			
			float percent = smoothTrans(Jello.jgui.percent, Jello.jgui.lastPercent);
    		float percent2 = smoothTrans(Jello.jgui.percent2, Jello.jgui.lastPercent2);
    		
    		float newScale = Minecraft.getMinecraft().currentScreen == null ? smoothTrans(Jello.jgui.outro, Jello.jgui.lastOutro) : percent > 0.98 ? percent : percent2 <= 1 ? percent2 : 1;
			float[] coords = ScaleUtils.getScaledCoords(this.x + 91/2f - 11.5f - 4.5f - 68/2f, this.y + 453/2f - 11.5f - 4.5f + 76/2f + 0.5f, 400, 47, newScale, sr);
			//if(opacity >= 0.8){
			if(newScale == 1){
			BlurUtil.blurAreaBoarder(this.x + 91/2f - 11.5f - 4.5f - 68/2f, this.y + 453/2f - 11.5f - 4.5f + 76/2f + 0.5f, 400, 47, 30/*1 + (14*smoothHover)*/, opacity, 0, 1);
			}else{
				if(Minecraft.getMinecraft().currentScreen == null){
			BlurUtil.blurAreaBoarder(coords[0],coords[1] - 1f, coords[2], coords[3]+0.5f, 30/*1 + (14*smoothHover)*/,  opacity,0, 1);
				}else{
					BlurUtil.blurAreaBoarder(coords[0] - 0.5f,coords[1]-1f, coords[2] + 1.5f, coords[3] + 1f, 30/*1 + (14*smoothHover)*/, opacity, 0, 1);
					
				}
				}
			//}
			FontUtil.jelloFontSmall.drawNoBSCenteredString(artist, x + 264/2f  +2 - 166/2f + 13/2f, y + 572/2f - 7/2f + 1, white);
		GlStateManager.color(1, 1, 1, 1);
		FontUtil.jelloFontSmall.drawNoBSCenteredString(title, x + 264/2f  +2 - 166/2f + 13/2f, y + 572/2f - 7/2f + 1 - 10, white);
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.color(1, 1, 1, opacity);
		
		this.drawTexturedRect(x - 16, y - 16, 423f, 323f, "music icon", sr);
		TextureImpl.bindNone();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.color(1, 1, 1, opacity);
		selectedSong.image.rectTextureMasked2(this.x + 91/2f - 11.5f - 4.5f, this.y + 453/2f - 11.5f - 4.5f, 57, 57, 0, 0.001f);
		
		}
		
		float xpos = 804/2f - 16;
		float ypos = 520/2f - 4.5f;
		float volume = this.volumeControl;
		//if(selectedSong != null){
		Gui.drawFloatRect(x + xpos, y + ypos, x + xpos + 2, y + ypos + 20, new Color(0f,0f,0f, .2f*opacity).getRGB());
		Gui.drawFloatRect(x + xpos, y + ypos - (-1+volume)*20, x + xpos + 2, y + ypos + 20, new Color(1f,1f,1f, .5f*opacity).getRGB());
		//}
		if(Mouse.isButtonDown(0) && this.isMouseHoveringRect2(x + xpos, y + ypos, x + xpos + 2, y + ypos + 21, mouseX, mouseY)){
		this.volumeControl = Math.min(20, Math.max(0,((y + ypos + 20)-mouseY)))/20f;
		Player.setVolume(volumeControl*50);
		}
		String time = "0:00";
		String total = "0:00";
		float barPercent = 0;
		if(selectedSong != null && this.currentSongLength != 0){
		if(Player.isPlaying){
 			currentPosition = Player.player.getPosition();
 		}
		final int MINUTES_IN_AN_HOUR = 60;
	    final int SECONDS_IN_A_MINUTE = 60;

	    int seconds = currentSongLength % SECONDS_IN_A_MINUTE;
	    int totalMinutes = currentSongLength / SECONDS_IN_A_MINUTE;
	    int minutes = totalMinutes % MINUTES_IN_AN_HOUR;
	    int hours = totalMinutes / MINUTES_IN_AN_HOUR;
	    total = totalMinutes+":"+seconds;
		
    	 int progress = 20;
    	 barPercent = (currentPosition/1000f)/this.currentSongLength;
			
    	 if (currentPosition >= 3600000) {
				String format = "%2d:%02d:%02d";
				time = String.format(format,
						TimeUnit.MILLISECONDS.toHours(currentPosition),
					    TimeUnit.MILLISECONDS.toMinutes(currentPosition) % TimeUnit.HOURS.toMinutes(1),
					    TimeUnit.MILLISECONDS.toSeconds(currentPosition) % TimeUnit.MINUTES.toSeconds(1));
			} else if (currentPosition >= 60000) {
				String format = "%2d:%02d";
				time = String.format(format,
						TimeUnit.MILLISECONDS.toMinutes(currentPosition) % TimeUnit.HOURS.toMinutes(1),
					    TimeUnit.MILLISECONDS.toSeconds(currentPosition) % TimeUnit.MINUTES.toSeconds(1));
			} else if (currentPosition >= 1000) {
				String format = "0:%02d";
				time = String.format(format,
					    TimeUnit.MILLISECONDS.toSeconds(currentPosition) % TimeUnit.MINUTES.toSeconds(1));
			}
		}
		
		float durationPercent = barPercent;
		//if(selectedSong != null){
		Gui.drawFloatRect(x- 16 + 9 + 250/2f + 3, y- 16 + 561/2f + 28.5f, x- 16 + 9 + 250/2f + 277.5f, y- 16 + 561/2f + 28.5f + 2.5f, new Color(selectedSong == null ? 0 :1, selectedSong == null ? 0 :1, selectedSong == null ? 0 :1, 0.35f*opacity).getRGB());
		Gui.drawFloatRect(x- 16 + 9 + 250/2f + 3, y- 16 + 561/2f + 28.5f, x- 16 + 9 + 250/2f + 3 + 274.5f*durationPercent  , y- 16 + 561/2f + 28.5f + 2.5f, new Color(1, 1, 1, 0.65f*opacity).getRGB());
		//}
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.color(1, 1, 1, opacity);
		//if(selectedSong != null){
		this.drawTexturedRect(x- 16 + 9 + 250/2f, y- 16 + 561/2f + 28.5f - 2.5f, 555/2f, 5, "durationshadow", sr);
		//}
		
		
		this.drawTexturedRect(x- 16 + 207.5f, y + 264.5f, 22.5f, 15, "rewind", sr);
		if(!Player.paused && selectedSong != null){
			this.drawTexturedRect(x- 16 + 265, y + 262, 17.5f, 19f, "pause", sr);
			}else{
			this.drawTexturedRect(x- 16 + 265, y + 262.5f, 37/2f, 36/2f, "play", sr);
			}
		this.drawTexturedRect(x- 16 + 644/2f, y- 16 + 561/2f, 22.5f, 15, "fastforward", sr);
		//if(selectedSong != null){
		GlStateManager.color(1, 1, 1, 1);
		FontUtil.jelloFontSmall.drawNoBSCenteredString(time, x + 264/2f  +2 , y + 572/2f - 7/2f, white);
		GlStateManager.color(1, 1, 1, 1);
		FontUtil.jelloFontSmall.drawNoBSCenteredString(total, x + 264/2f  +2 + 494/2f, y + 572/2f - 7/2f, white);
		//}
		List<String> channels = Arrays.asList("Trap Nation", "Electro Posé", "Chill Nation", "VEVO", "MrSuicideSheep", "Trap City", "CloudKid");
		for(int render = 0; render < 2; render++){
			int channelCount = 0;
			for(String c : channels){
				GlStateManager.color(1, 1, 1, 1);
			FontUtil.jelloFontDuration.drawNoBSCenteredString(c, x - 9.5f/2f + 0.5f + 125/2f - 0.5f, y - 3.5f + 93/2f + 0.5f + 20*channelCount, white);
			channelCount++;
			}
		}
		if(searchScreen && this.currentSearch != null){
			int xCount = 0;
			int yCount = 0;
			for(com.youtube.search.Item v : this.currentSearch.getItems()){
				//if(v.getSnippet().getTitle().contains(" - ")){
					if(xCount < 2){
					xCount++;
				}else{
					yCount++;
					xCount = 0;
				}
				//}
			}
			
			float locationX = 71;
			float locationY = 53/2f - scroll*(yCount*105f - 105) + 7;
			xCount = 0;
			yCount = 0;
			BlurUtil.blurAll(1);
			Stencil.write(false);
			Gui.drawFloatRect(x - 4.5f + 125, y - 4.5f, x + 395.5f, y + 248.5f, -1);
			Stencil.erase(true);
			
				
				for(com.youtube.search.Item v : this.currentSearch.getItems()){
					//if(v.getSnippet().getTitle().contains(" - ")){
						if((100 + yCount*105f + locationY - 172/2f) <= 250 && (100 + yCount*105f + locationY - 172/2f) >= -106){
						String title = //"Bored";//
								v.getSnippet().getTitle().contains(" - ") ? StringEscapeUtils.unescapeHtml4(v.getSnippet().getTitle().split(" - ")[1]) : StringEscapeUtils.unescapeHtml4(v.getSnippet().getTitle());
						String artist = //"Billie Eilish";//
								v.getSnippet().getTitle().contains(" - ") ? StringEscapeUtils.unescapeHtml4(v.getSnippet().getTitle().split(" - ")[0]) : StringEscapeUtils.unescapeHtml4(v.getSnippet().getChannelTitle());
						GlStateManager.color(1, 1, 1, 1);
	            		GlStateManager.color(1, 1, 1, opacity);
						drawTexturedRect(this.x + 100 + xCount*86.5f + locationX - 77/2f - 11.5f, this.y + 100 + yCount*105f + locationY - 172/2f - 11.5f, 100, 100, "thumbnailsquare", sr);
						//GlStateManager.enableBlend();
						//GlStateManager.color(1, 1, 1, 1);
						if(v.image != null){
		            		float smoothHover = smoothTrans(v.hover, v.lastHover);
		            		//if(opacity > 0.5){
		            		try{
		            		}catch(Exception e){
		            			
		            		}
		            		if(v.image.texture != null){
		            			TextureImpl.bindNone();
		            			}
			            		GlStateManager.color(1, 1, 1, opacity);
		            		if(smoothHover != 0){
		            		ScaleUtils.preScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (float) (1.00 + 0.05*smoothHover), sr);
		            		}
		            		
		            		v.image.rectTextureMasked(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, opacity, 0.001f);
		            		
		            		if(smoothHover != 0){
		            			v.image.rectTextureMasked(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, opacity, 0);
		            			float toSubtract = Math.max(29,100.5f + yCount*105f + locationY - 172/2f)-(100.5f + yCount*105f + locationY - 172/2f);
		            			if(toSubtract < 80){
		            				float percent1 = smoothTrans(Jello.jgui.percent, Jello.jgui.lastPercent);
		            	    		float percent2 = smoothTrans(Jello.jgui.percent2, Jello.jgui.lastPercent2);
		            	    		
		            	    		float newScale = Minecraft.getMinecraft().currentScreen == null ? smoothTrans(Jello.jgui.outro, Jello.jgui.lastOutro) : percent1 > 0.98 ? percent1 : percent2 <= 1 ? percent2 : 1;
		            				float[] coords = ScaleUtils.getScaledCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f - 2*smoothHover, this.y + Math.max(29,100.5f + yCount*105f + locationY - 172/2f) - 2*smoothHover - 0.25f, 81 - 4*(1-smoothHover) + 0.5f, (81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract, newScale, sr);
		            				//if(opacity >= 0.8){
		            				if(newScale == 1){
		            				BlurUtil.blurAreaBoarder(this.x + 100 + xCount*86.5f + locationX - 77/2f - 2*smoothHover, this.y + Math.max(29,100.5f + yCount*105f + locationY - 172/2f) - 2*smoothHover - 0.25f, 81 - 4*(1-smoothHover) + 0.5f, (81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract, 15, 0, 1);
		            				}else{
		            					if(Minecraft.getMinecraft().currentScreen == null){
		            				BlurUtil.blurAreaBoarder(coords[0],coords[1] - 1f, coords[2], coords[3]+0.5f, 15, 0, 1);
		            					}else{
		            						BlurUtil.blurAreaBoarder(coords[0] - 0.5f,coords[1]-1f, coords[2] + 1.5f, coords[3] + 1f, 15, 0, 1);
		            						
		            					}
		            					}
		            				//BlurUtil.blurAreaBoarder(this.x + 100 + xCount*86.5f + locationX - 77/2f - 2*smoothHover, this.y + Math.max(29,100.5f + yCount*105f + locationY - 172/2f) - 2*smoothHover - 0.25f, 81 - 4*(1-smoothHover) + 0.5f, (81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract, 15, 0, 1);
		            			
		            			}
		            			if(v.image.texture != null){
		            			TextureImpl.bindNone();
		            			}
		            			v.image.rectTextureMasked(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, opacity, smoothHover);
		            			ScaleUtils.postScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (float) (1.00 + 0.05*smoothHover), sr);
			            		
			            		GlStateManager.enableBlend();
			            		GlStateManager.disableAlpha();
			            		
			            		ScaleUtils.preScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (0.5f+ smoothHover/2f), sr);
			            		if(opacity > 0.5f){
			            		GlStateManager.color(1, 1, 1, 1f*smoothHover);
			            		}else{
			            			GlStateManager.color(1, 1, 1, 1);
			                		GlStateManager.color(1, 1, 1, opacity);
			            		}
			            		if(Mouse.isButtonDown(0)){
			            			//this.drawTexturedRect(sr.getScaledWidth() - 31/2f - 9, sr.getScaledHeight() - 34/2f - 9, 31/2f, 34/2f, "ambient play", sr);
			            	    	
			            			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/ambient play.png"));
			            			GL11.glTexParameteri(3553, 10240, 9729);
			                        GL11.glTexParameteri(3553, 10241, 9729);
			            			
			                        Gui.INSTANCE.drawModalRectWithCustomSizedTexture(this.x + 100 + xCount*86.5f + locationX - 77/2f - 11.5f + 50 - (31/4f)/1.2f, this.y + 100 + yCount*105f + locationY - 172/2f - 11.5f + 50 - (34/4f)/1.2f, 0, 0, 31/2f/1.2f, 34/2f/1.2f, 31/2f/1.2f, 34/2f/1.2f);
			            			GL11.glTexParameteri(3553, 10240, 9728);
			                        GL11.glTexParameteri(3553, 10241, 9728);
				            		}else{
			            		drawTexturedRect(this.x + 100.25f + xCount*86.5f + locationX - 77/2f - 11.5f + 50 - 31/4f, this.y + 100 + yCount*105f + locationY - 172/2f - 11.5f + 50 - 34/4f, 31/2f, 34/2f, "ambient play", sr);
				            		}
			            		ScaleUtils.postScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (0.5f+ smoothHover/2f), sr);
			            			
		            		}
		            	}else{
		            		if(v.image == null || v.image.texture == null){
		    					v.image = Resources.downloadTexture("https://img.youtube.com/vi/"+ v.getId().getVideoId() +"/0.jpg");
		    					}
		            	}
						GlStateManager.color(1, 1, 1, 1);
						FontUtil.jelloFontMusic.drawNoBSCenteredString(title, this.x + 100 + xCount*86.5f + locationX, this.y + 100 + yCount*105f + locationY, white);
						GlStateManager.color(1, 1, 1, 1);
						FontUtil.jelloFontMusic.drawNoBSCenteredString(artist, this.x + 100 + xCount*86.5f + locationX, this.y + 100 + yCount*105f + 13/2f + locationY, white);
						}
					if(xCount < 2){
						xCount++;
					}else{
						yCount++;
						xCount = 0;
					}
					//}
				}
			
			
			Stencil.dispose();
		
			float newX = x + 298 - 3.5f;
			float newY = y - 32 + 3.5f;
			float smoothScroll = smoothTrans(scrollTicks, lastScrollTicks);
		    
			GlStateManager.enableBlend();
		    GlStateManager.color(1, 1, 1, Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f, 0, 0,  5.5f, 5.5f/2f, 5.5f, 5.5f);
		    GlStateManager.color(1, 1, 1, 1);
	        Gui.drawFloatRect(newX + 100 - 2.5f, newY + 25 + 2.5f + 5.5f/2f, newX + 100 - 2.5f - 5.5f, newY + 25 + 225 - 2.5f - 2.5f + 25.5f, new Color(0,0,0,Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0))/4).getRGB());
	        
	        GlStateManager.color(1, 1, 1, Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
	        GlStateManager.disableAlpha();
		    GlStateManager.enableBlend();
		    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 225 - 2.5f - 2.5f + 29 - 3.5f, 0, 5.5f/2f,  5.5f, 5.5f/2f, 5.5f, 5.5f);
		    GlStateManager.color(1, 1, 1, 1);
		    
		    
		    float height = (50/((yCount-1)))*10 + 129 - 2.5f;

		    float scrollBarHeight = 20;
		    float percent = (smoothTrans(animatedScrollY, lastScrollY))/(492/2f - scrollBarHeight);
		    if(percent > 1){
		    	percent = 1;
		    }
		    if(scrollY > 226.5){
		    	scrollY = 226.5f;
		    }
		    float dist = 210.5f*percent;
		    
		    float startY = dist - (scrollBarHeight-30)*percent;
		    
		    
		    float endY = -118 + 0.5f + 3 + scrollBarHeight + dist - (scrollBarHeight-30)*percent;
		    if(this.isHoveringCoords(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f, 5.5f, 245, mouseX, mouseY)){
		    	if(scrollTicks == 0){
		    	this.scrollTicks = 30;
		    	lastScrollTicks = 30;
	    		}else if(scrollTicks <= 20){
	    			scrollTicks = 20;
	    	    	lastScrollTicks = 20;
	    		}
		    }
		    if (Mouse.hasWheel() && this.isHoveringCoords(x - 4.5f + 125, y - 4.5f, 275,250, mouseX, mouseY) /*&& locationY > 100*/) {
		 	   
	            final int wheel = Jello.jgui.mouseWheel;
	            if (wheel < 0) {
	            	searchBar.setFocused(false);
	            	if(scrollY < 245.5){
	                    this.scrollY += 10f;
	                	}
	                if (this.scrollY < 0) {
	                    this.scrollY = 0;
	                }
	                if(scrollY > 245.5){
	                	scrollY = 245.5f;
	                }
	                if(scrollTicks <= 20 && scrollTicks > 0){
	                	scrollTicks = 20;
	                	lastScrollTicks =20;
	                }else if(scrollTicks ==0){
	                scrollTicks = 30;
	            	lastScrollTicks = 30;
	                }
	            }
	            else if (wheel > 0) {
	            	searchBar.setFocused(false);
	                this.scrollY -= 10f;
	                if (this.scrollY < 0) {
	                    this.scrollY = 0;
	                }
	                if(scrollY > 245.5){
	            //   	scroll = (count-1)*15 - 110;
	                }
	                if(scrollTicks <= 20 && scrollTicks > 0){
	                	scrollTicks = 20;
	                	lastScrollTicks = 20;
	                }else if(scrollTicks ==0){
	                	lastScrollTicks = 30;
	                scrollTicks = 30;
	                }
	            }
	        }
		    scroll = percent;
		    if(scrolling){
	    		scrollY = scrollLastY + (mouseY) - scrollStartY ;
	    		if(scrollTicks == 0){
	    	    	this.scrollTicks = 30;
	    	    	lastScrollTicks = 30;
	        		}else if(scrollTicks <= 20){
	        			scrollTicks = 20;
	        	    	lastScrollTicks = 20;
	        		}
	    		
	    		if(scrollY < 0){
	    			scrollY = 0;
	    		}
	    		if(scrollY > 245.5){
	    			scrollY = 245.5f;
	    		}
	    		animatedScrollY = scrollY;
	    		lastScrollY = scrollY;
	    	}
		    searchBar.xPosition = x + 121 + 15.5f;
			searchBar.yPosition = y - 9 + 20 - 1.5f - scroll*((yCount)*105f - 105);
		    GlStateManager.color(1, 1, 1, Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
		    
			
			
			Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f + startY, 0, 0,  5.5f, 5.5f/2f, 5.5f, 5.5f);
		    GlStateManager.color(1, 1, 1, 1);
	        
		    Gui.drawFloatRect(newX + 100 - 2.5f, newY + 25 + 2.5f + 5.5f/2f + startY, newX + 100 - 2.5f - 5.5f, newY + 25 + 125 - 2.5f - 2.5f + endY, new Color(0,0,0,Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0))/4).getRGB());
	        
	        GlStateManager.color(1, 1, 1, Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
	        GlStateManager.disableAlpha();
		    GlStateManager.enableBlend();
		    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
		    
		    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 125 - 2.5f - 2.5f + endY, 0, 5.5f/2f,  5.5f, 5.5f/2f, 5.5f, 5.5f);
			
		    float scrollTr = smoothTrans(scrollTrans, lastScrollTrans);
		    
		   /* if(scrollTr == 1){
		    //BlurUtil.blurAll(1);
		    	float percent1 = smoothTrans(Jello.jgui.percent, Jello.jgui.lastPercent);
	    		float percent2 = smoothTrans(Jello.jgui.percent2, Jello.jgui.lastPercent2);
	    		
	    		float newScale = Minecraft.getMinecraft().currentScreen == null ? smoothTrans(Jello.jgui.outro, Jello.jgui.lastOutro) : percent1 > 0.98 ? percent1 : percent2 <= 1 ? percent2 : 1;
				//Jello.addChatMessage(newScale  + "");
				float[] coords = ScaleUtils.getScaledCoords(x - 4.5f + 125, y - 4.5f + 0.5f, 275, 32, newScale, sr);
				//if(opacity >= 0.8){
				if(newScale == 1){
				BlurUtil.blurAreaBoarder(x - 4.5f + 125, y - 4.5f + 0.5f, 275, 32, 60, 0, 1);
				}else{
					if(Minecraft.getMinecraft().currentScreen == null){
				BlurUtil.blurAreaBoarder(coords[0],coords[1] - 1f, coords[2], coords[3]+0.5f, 60, 0, 1);
					}else{
						BlurUtil.blurAreaBoarder(coords[0] - 0.5f,coords[1]-1f, coords[2] + 1.5f, coords[3] + 1f, 60, 0, 1);
						
					}
					}
		   // BlurUtil.blurAreaBoarder(x - 4.5f + 125, y - 4.5f + 0.5f, 275, 32, 60, 0, 1);
		    }*/
		    GlStateManager.color(1, 1, 1, 1);
			//FontUtil.jelloFontRegularBig.drawNoBSCenteredString("ElectroPosé", x + 125 + 275/2f - 8, y + 37/2f - 5, white);
		    /*if(scrollTr == 1){
		    FontUtil.jelloFontAddAlt.drawNoBSCenteredString(currentSearch.getItems().get(0).getSnippet().getChannelTitle(), x + 125 + 275/2f - 8 + 0.5f, y + 37/2f - 5 - 7*(scrollTr), white);
		    }else{
		    	FontUtil.jelloFontRegularBig.drawNoBSCenteredString(currentSearch.getItems().get(0).getSnippet().getChannelTitle(), x + 125 + 275/2f - 8, y + 37/2f - 5 - 7*(scrollTr), white);
		    }*/
		    
			}
		if(selectedChannel != null){
		int xCount = 0;
		int yCount = 0;
		for(Item v : this.selectedChannel.getItems()){
			if(v.getSnippet().getTitle().contains(" - ")){
				if(xCount < 2){
				xCount++;
			}else{
				yCount++;
				xCount = 0;
			}
			}
		}
		
		float locationX = 71;
		float locationY = 53/2f - scroll*(yCount*105f - 105);
		xCount = 0;
		yCount = 0;
		BlurUtil.blurAll(1);
		Stencil.write(false);
		Gui.drawFloatRect(x - 4.5f + 125, y - 4.5f, x + 395.5f, y + 248.5f, -1);
		Stencil.erase(true);
		
			
			for(Item v : this.selectedChannel.getItems()){
				if(v.getSnippet().getTitle().contains(" - ")){
					if((100 + yCount*105f + locationY - 172/2f) <= 250 && (100 + yCount*105f + locationY - 172/2f) >= -106){
					String title = //"Bored";//
							StringEscapeUtils.unescapeHtml4(v.getSnippet().getTitle().split(" - ")[1]);
					String artist = //"Billie Eilish";//
							StringEscapeUtils.unescapeHtml4(v.getSnippet().getTitle().split(" - ")[0]);
					GlStateManager.color(1, 1, 1, 1);
            		GlStateManager.color(1, 1, 1, opacity);
					drawTexturedRect(this.x + 100 + xCount*86.5f + locationX - 77/2f - 11.5f, this.y + 100 + yCount*105f + locationY - 172/2f - 11.5f, 100, 100, "thumbnailsquare", sr);
					//GlStateManager.enableBlend();
					//GlStateManager.color(1, 1, 1, 1);
					if(v.image != null){
	            		float smoothHover = smoothTrans(v.hover, v.lastHover);
	            		//if(opacity > 0.5){
	            		try{
	            			//Jello.addChatMessage(v.image.texture+"");
	            		}catch(Exception e){
	            			
	            		}
	            		if(v.image.texture != null){
	            			TextureImpl.bindNone();
	            			}
		            		GlStateManager.color(1, 1, 1, opacity);
	            		if(smoothHover != 0){
	            		ScaleUtils.preScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (float) (1.00 + 0.05*smoothHover), sr);
	            		}
	            		
	            		v.image.rectTextureMasked(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, opacity, 0.001f);
	            		/*if(this.isHoveringCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, Jello.jgui.mouseX, Jello.jgui.mouseY)){
	                		
	            		}*/
	            		if(smoothHover != 0){
	            			v.image.rectTextureMasked(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, opacity, 0);
	            			float toSubtract = Math.max(29,100.5f + yCount*105f + locationY - 172/2f)-(100.5f + yCount*105f + locationY - 172/2f);
	            			if(toSubtract < 80){
	            				float percent1 = smoothTrans(Jello.jgui.percent, Jello.jgui.lastPercent);
	            	    		float percent2 = smoothTrans(Jello.jgui.percent2, Jello.jgui.lastPercent2);
	            	    		
	            	    		float newScale = Minecraft.getMinecraft().currentScreen == null ? smoothTrans(Jello.jgui.outro, Jello.jgui.lastOutro) : percent1 > 0.98 ? percent1 : percent2 <= 1 ? percent2 : 1;
	            				//Jello.addChatMessage(newScale  + "");
	            				float[] coords = ScaleUtils.getScaledCoords(this.x + 100 + xCount*86.5f + locationX - 77/2f - 2*smoothHover, this.y + Math.max(29,100.5f + yCount*105f + locationY - 172/2f) - 2*smoothHover - 0.25f, 81 - 4*(1-smoothHover) + 0.5f, (81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract, newScale, sr);
	            				//if(opacity >= 0.8){
	            				float height = ((81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract);
	            				if(height > 0){
	            				//Jello.addChatMessage(((81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract) + " ");
	            				if(newScale == 1){
	            				BlurUtil.blurAreaBoarder(this.x + 100 + xCount*86.5f + locationX - 77/2f - 2*smoothHover, this.y + Math.max(29,100.5f + yCount*105f + locationY - 172/2f) - 2*smoothHover - 0.25f, 81 - 4*(1-smoothHover) + 0.5f, (81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract, 15, opacity, 0, 1);
	            				}else{
	            					if(Minecraft.getMinecraft().currentScreen == null){
	            				BlurUtil.blurAreaBoarder(coords[0],coords[1] - 1f, coords[2], coords[3]+0.5f, 15, opacity, 0, 1);
	            					}else{
	            						BlurUtil.blurAreaBoarder(coords[0] - 0.5f,coords[1]-1f, coords[2] + 1.5f, coords[3] + 1f, 15, opacity, 0, 1);
	            						
	            					}
	            					}
	            				}
	            				//BlurUtil.blurAreaBoarder(this.x + 100 + xCount*86.5f + locationX - 77/2f - 2*smoothHover, this.y + Math.max(29,100.5f + yCount*105f + locationY - 172/2f) - 2*smoothHover - 0.25f, 81 - 4*(1-smoothHover) + 0.5f, (81 - 4*(1-smoothHover) - (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249) > 0 ? (((100.5f + yCount*105f + locationY - 172/2f - 2*smoothHover - 0.25f)+(81 - 4*(1-smoothHover)) - 249)) : -0)) - toSubtract, 15, 0, 1);
	            			
	            			}
	            			if(v.image.texture != null){
	            			TextureImpl.bindNone();
	            			}
	            			v.image.rectTextureMasked(this.x + 100 + xCount*86.5f + locationX - 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f, 77, 77, opacity, smoothHover);
	            			ScaleUtils.postScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (float) (1.00 + 0.05*smoothHover), sr);
		            		
		            		GlStateManager.enableBlend();
		            		GlStateManager.disableAlpha();
		            		
		            		ScaleUtils.preScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (0.5f+ smoothHover/2f), sr);
		            		if(opacity > 0.5f){
		            		GlStateManager.color(1, 1, 1, 1f*smoothHover);
		            		}else{
		            			GlStateManager.color(1, 1, 1, 1);
		                		GlStateManager.color(1, 1, 1, opacity);
		            		}
		            		if(Mouse.isButtonDown(0)){
		            			//this.drawTexturedRect(sr.getScaledWidth() - 31/2f - 9, sr.getScaledHeight() - 34/2f - 9, 31/2f, 34/2f, "ambient play", sr);
		            	    	
		            			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/ambient play.png"));
		            			GL11.glTexParameteri(3553, 10240, 9729);
		                        GL11.glTexParameteri(3553, 10241, 9729);
		            			
		                        Gui.INSTANCE.drawModalRectWithCustomSizedTexture(this.x + 100 + xCount*86.5f + locationX - 77/2f - 11.5f + 50 - (31/4f)/1.2f, this.y + 100 + yCount*105f + locationY - 172/2f - 11.5f + 50 - (34/4f)/1.2f, 0, 0, 31/2f/1.2f, 34/2f/1.2f, 31/2f/1.2f, 34/2f/1.2f);
		            			GL11.glTexParameteri(3553, 10240, 9728);
		                        GL11.glTexParameteri(3553, 10241, 9728);
			            		}else{
		            		drawTexturedRect(this.x + 100.25f + xCount*86.5f + locationX - 77/2f - 11.5f + 50 - 31/4f, this.y + 100 + yCount*105f + locationY - 172/2f - 11.5f + 50 - 34/4f, 31/2f, 34/2f, "ambient play", sr);
			            		}
		            		ScaleUtils.postScale(this.x + 100 + xCount*86.5f + locationX - 77/2f + 77/2f, this.y + 100 + yCount*105f + locationY - 172/2f + 77/2f, (0.5f+ smoothHover/2f), sr);
		            			
	            		}
	            	}else{
	            		if(v.image == null || v.image.texture == null){
	    					v.image = Resources.downloadTexture("https://img.youtube.com/vi/"+ v.getSnippet().getResourceId().getVideoId() +"/0.jpg");
	    					//Jello.addChatMessage("Download " + v.image.texture);
	    					}
	            	}
					GlStateManager.color(1, 1, 1, 1);
					FontUtil.jelloFontMusic.drawNoBSCenteredString(title, this.x + 100 + xCount*86.5f + locationX, this.y + 100 + yCount*105f + locationY, white);
					GlStateManager.color(1, 1, 1, 1);
					FontUtil.jelloFontMusic.drawNoBSCenteredString(artist, this.x + 100 + xCount*86.5f + locationX, this.y + 100 + yCount*105f + 13/2f + locationY, white);
					}
				if(xCount < 2){
					xCount++;
				}else{
					yCount++;
					xCount = 0;
				}
				}
			}
		
		
		Stencil.dispose();
	
		float newX = x + 298 - 3.5f;
		float newY = y - 32 + 3.5f;
		float smoothScroll = smoothTrans(scrollTicks, lastScrollTicks);
	    
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, 1);
	    GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f, 0, 0,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    GlStateManager.color(1, 1, 1, 1);
        Gui.drawFloatRect(newX + 100 - 2.5f, newY + 25 + 2.5f + 5.5f/2f, newX + 100 - 2.5f - 5.5f, newY + 25 + 225 - 2.5f - 2.5f + 25.5f, new Color(0,0,0,opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0))/4).getRGB());
        
        GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
        GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 225 - 2.5f - 2.5f + 29 - 3.5f, 0, 5.5f/2f,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    GlStateManager.color(1, 1, 1, 1);
	    //Jello.addChatMessage(category.name + " + "+(((count-1)*smoothScroll)/50)*25+"");
	    
	    /*
	     * float height = (50/((yCount-1)))*22.9f - 2.5f;
	    float percent = (scroll/((yCount-1)*15 - 110))*100 + 50;
	    float startY = 0 + percent - height*(percent/100);
	    float endY = -100  + percent + height - height*(percent/100);
	    
	     */
	    
	    float height = (50/((yCount-1)))*10 + 129 - 2.5f;

	    float scrollBarHeight = 20;
	    float percent = (smoothTrans(animatedScrollY, lastScrollY))/(492/2f - scrollBarHeight);//(scrollY)/(492/2f - scrollBarHeight);
	    if(percent > 1){
	    	percent = 1;
	    }
	    if(scrollY > 226.5){
	    	scrollY = 226.5f;
	    }
	    float dist = 210.5f*percent;
	    
	    float startY = dist - (scrollBarHeight-30)*percent;
	    
	    
	    float endY = -118 + 0.5f + 3 + scrollBarHeight + dist - (scrollBarHeight-30)*percent;
	    if(this.isHoveringCoords(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f, 5.5f, 245, mouseX, mouseY)){
    		//Jello.addChatMessage("hover");
	    	if(scrollTicks == 0){
	    	this.scrollTicks = 30;
	    	lastScrollTicks = 30;
    		}else if(scrollTicks <= 20){
    			scrollTicks = 20;
    	    	lastScrollTicks = 20;
    		}
	    }
	    if (Mouse.hasWheel() && this.isHoveringCoords(x - 4.5f + 125, y - 4.5f, 275,250, mouseX, mouseY) /*&& locationY > 100*/) {
	 	   
            final int wheel = Jello.jgui.mouseWheel;
            if (wheel < 0) {
            	if(scrollY < 245.5){
                    this.scrollY += 10f;
                	}
                if (this.scrollY < 0) {
                    this.scrollY = 0;
                }
                if(scrollY > 245.5){
                	scrollY = 245.5f;
                }
                if(scrollTicks <= 20 && scrollTicks > 0){
                	scrollTicks = 20;
                	lastScrollTicks =20;
                }else if(scrollTicks ==0){
                scrollTicks = 30;
            	lastScrollTicks = 30;
                }
            }
            else if (wheel > 0) {
                this.scrollY -= 10f;
                if (this.scrollY < 0) {
                    this.scrollY = 0;
                }
                if(scrollY > 245.5){
            //   	scroll = (count-1)*15 - 110;
                }
                if(scrollTicks <= 20 && scrollTicks > 0){
                	scrollTicks = 20;
                	lastScrollTicks = 20;
                }else if(scrollTicks ==0){
                	lastScrollTicks = 30;
                scrollTicks = 30;
                }
            }
        }
	    scroll = percent;
	    if(scrolling){
    		scrollY = scrollLastY + (mouseY) - scrollStartY ;
    		if(scrollTicks == 0){
    	    	this.scrollTicks = 30;
    	    	lastScrollTicks = 30;
        		}else if(scrollTicks <= 20){
        			scrollTicks = 20;
        	    	lastScrollTicks = 20;
        		}
    		
    		if(scrollY < 0){
    			scrollY = 0;
    		}
    		if(scrollY > 245.5){
    			scrollY = 245.5f;
    		}
    		animatedScrollY = scrollY;
    		lastScrollY = scrollY;
    	}
	    GlStateManager.color(1, 1, 1, 1);
	    GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    
		
		
		Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 2.5f + startY, 0, 0,  5.5f, 5.5f/2f, 5.5f, 5.5f);
	    GlStateManager.color(1, 1, 1, 1);
        
	    Gui.drawFloatRect(newX + 100 - 2.5f, newY + 25 + 2.5f + 5.5f/2f + startY, newX + 100 - 2.5f - 5.5f, newY + 25 + 125 - 2.5f - 2.5f + endY, new Color(0,0,0,opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0))/4).getRGB());
        
        GlStateManager.color(1, 1, 1, opacity*Math.min(1, Math.max(0,smoothScroll > 20 ? 1-((smoothScroll-20)/10f) : smoothScroll > 0 && smoothScroll < 10 ? ((smoothScroll)/10f) : smoothScroll >= 10 && smoothScroll <= 20 ? 1 : 0)));
        GlStateManager.disableAlpha();
	    GlStateManager.enableBlend();
	    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/scroll ball.png"));
	    
	    Gui.INSTANCE.drawModalRectWithCustomSizedTexture(newX + 100 - 2.5f - 5.5f, newY + 25 + 125 - 2.5f - 2.5f + endY, 0, 5.5f/2f,  5.5f, 5.5f/2f, 5.5f, 5.5f);
		
	    float scrTr = Math.min(1, Math.max(0, smoothTrans(animatedScrollY, lastScrollY)/10f));
		 float scrollTr = scrTr;//smoothTrans(scrollTrans, lastScrollTrans);
	    	//System.out.println(scrTr + "");
	    //if(scrollTr == 1){
	    //BlurUtil.blurAll(1);
	    	float percent1 = smoothTrans(Jello.jgui.percent, Jello.jgui.lastPercent);
    		float percent2 = smoothTrans(Jello.jgui.percent2, Jello.jgui.lastPercent2);
    		
    		float newScale = Minecraft.getMinecraft().currentScreen == null ? smoothTrans(Jello.jgui.outro, Jello.jgui.lastOutro) : percent1 > 0.98 ? percent1 : percent2 <= 1 ? percent2 : 1;
			//Jello.addChatMessage(newScale  + "");
			float[] coords = ScaleUtils.getScaledCoords(x - 4.5f + 125, y - 4.5f + 0.5f, 275, 32, newScale, sr);
			//if(opacity >= 0.8){
			if(newScale == 1){
			BlurUtil.blurAreaBoarder(x - 4.5f + 125, y - 4.5f + 0.5f, 275, 32, 60, scrollTr*opacity, 0, 1);
			}else{
				if(Minecraft.getMinecraft().currentScreen == null){
			BlurUtil.blurAreaBoarder(coords[0],coords[1] - 1f, coords[2], coords[3]+0.5f, 60, scrollTr*opacity, 0, 1);
				}else{
					BlurUtil.blurAreaBoarder(coords[0] - 0.5f,coords[1]-1f, coords[2] + 1.5f, coords[3] + 1f, 60, scrollTr*opacity, 0, 1);
					
				}
				}
	   // BlurUtil.blurAreaBoarder(x - 4.5f + 125, y - 4.5f + 0.5f, 275, 32, 60, 0, 1);
	    //}
	    GlStateManager.color(1, 1, 1, 1);
		//FontUtil.jelloFontRegularBig.drawNoBSCenteredString("ElectroPosé", x + 125 + 275/2f - 8, y + 37/2f - 5, white);
	    //if(scrollTr == 1){
	    FontUtil.jelloFontAddAlt.drawNoBSCenteredString(selectedChannel.getItems().get(0).getSnippet().getChannelTitle(), x + 125 + 275/2f - 8, y + 37/2f - 5 - 7*(scrollTr), new Color(1f,1f,1f, scrollTr*opacity).getRGB());
	    //}else{

	    GlStateManager.color(1, 1, 1, 1);
	    	FontUtil.jelloFontRegularBig.drawNoBSCenteredString(selectedChannel.getItems().get(0).getSnippet().getChannelTitle(), x + 125 + 275/2f - 8, y + 37/2f - 5 - 7*(scrollTr), new Color(1f,1f,1f, (1-scrollTr)*opacity).getRGB());
	    //}
		}
	    GlStateManager.color(1, 1, 1, 1);
	    GlStateManager.color(1, 1, 1, opacity);
	    this.drawTexturedRect(x - 16, y - 16, 423f, 323f, "music shadow", sr);
		
	    /*float scaleAmount = 2f;//((float)mouseY/sr.getScaledHeight())*2f;
	    int pixel = mouseX;
	    Jello.addChatMessage(""+(mouseX-sr.getScaledWidth()/2f));
	    float x1 = 150 + (mouseX-sr.getScaledWidth()/2f);
	    float y1 = 150 + (mouseY-sr.getScaledHeight()/2f);
	    float x2 = 200 + (mouseX-sr.getScaledWidth()/2f) ;
	    float y2 = 200 + (mouseY-sr.getScaledHeight()/2f);
	    float sfX = (((-sr.getScaledWidth()/2)*scaleAmount) + sr.getScaledWidth()/2f);
	    float sfY = (((-sr.getScaledHeight()/2)*scaleAmount) + sr.getScaledHeight()/2f);
	    float x1S = ((sr.getScaledWidth()/2)-x1)/(sr.getScaledWidth()/2f);
	    float x2S = ((sr.getScaledWidth()/2)-x2)/(sr.getScaledWidth()/2f);
	    float y1S = ((sr.getScaledHeight()/2)-y1)/(sr.getScaledHeight()/2f);
	    float y2S = ((sr.getScaledHeight()/2)-y2)/(sr.getScaledHeight()/2f);*/
	    // Gui.drawFloatRect(x1 + sfX*x1S, y1 + sfY*y1S, x2 + sfX*x2S, y2 + sfY*y2S, -1);
	     
	     /*GlStateManager.translate(sr.getScaledWidth()/2f, sr.getScaledHeight()/2f, 0);
	     GlStateManager.scale(scaleAmount, scaleAmount, 1);
	     GlStateManager.translate(-sr.getScaledWidth()/2f, -sr.getScaledHeight()/2f, 0);
	     Gui.drawFloatRect(x1, y1, x2, y2, 0xff000000);
	     GlStateManager.translate(sr.getScaledWidth()/2f, sr.getScaledHeight()/2f, 0);
	     GlStateManager.scale(1/scaleAmount, 1/scaleAmount, 1);
	     GlStateManager.translate(-sr.getScaledWidth()/2f, -sr.getScaledHeight()/2f, 0);*/
	     //Jello.addChatMessage("scale: " + scaleAmount + " posx: "+(((-sr.getScaledWidth()/2)*scaleAmount) + sr.getScaledWidth()/2f));
	    //Jello.addChatMessage(((sr.getScaledWidth()/2)-x1)/(sr.getScaledWidth()/2f) +"");
	}
	public void drawTexturedRect(float x, float y, float width, float height, String image, ScaledResolution sr) {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("Jello/"+image+".png"));
        Gui.INSTANCE.drawModalRectWithCustomSizedTexture(x,  y, 0, 0, width, height, width, height);
    }
	public void initGui(){
		searchBar = new JelloTextField(1, Minecraft.getMinecraft().fontRendererObj, x + 121 + 15.5f, y - 9 + 20 - 1.5f, 258 - 15, 20);
   	 searchBar.setMaxStringLength(32);
   	 searchBar.setPlaceholder("Search...");
        
	}
	public boolean isMouseHoveringRect2(float x, float y, float x2, float y2, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }
	
	public boolean isHoveringCoords(float x, float y, float width, float height, int mouseX, int mouseY){
		return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height-0.5f;
	}
	
}
