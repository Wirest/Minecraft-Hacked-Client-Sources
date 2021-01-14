package com.mentalfrostbyte.jello.util;

import com.mentalfrostbyte.jello.ttf.GLUtils;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class BezierCurve {

	public double A;
	public double B;
	public double C;
	public double D;
	
	public float percent = 100;
	public long oldTime = System.currentTimeMillis();
	
	public BezierCurve(double point1, double point2, double point3, double point4) {
		this.A = point1;
		this.B = point2;
		this.C = point3;
		this.D = point4;
	}
	
	public double interpolateCubic(double percent){
		return 1-(A*Math.pow(1-percent,3)+3*B*Math.pow(1-percent,2)*percent+3*C*(1-percent)*Math.pow(percent,2)+D*Math.pow(percent,3))+(A*(1-percent));
	}
	
	public double interpolateQuadratic(double x){
		 return A * Math.pow(1-x,2) + B * 2 * (1-x) * x + C * Math.pow(x,2);
	}
	
	public double interpolate(double x){
		return (1-(B + 0.5*x*(C-A + x*(2*A-5*B+4*C-D + x*(3*(B-C)+D-A))))*4)*(1/.6);
	}
	
	/*public double interpolateScale(double p){
		return 1-((A*(Math.pow(1-p, 3))) + ((3*B)*(Math.pow(1-p, 2)*p)) + ((3*C)*(1-p)*(Math.pow(p, 2))) + (D*(Math.pow(p, 3))))+(A*(1-p));
	}*/
	
	public void drawCurve(){
		boolean editorMode = false;
		/*A = .35;
		B = .1;
		C = .35;
		D = 1;*/
		//.12,.62,.24,1.24
		A = editorMode ? (double)GLUtils.getMouseX()/(double)GLUtils.getScreenWidth(): .35;//(double)GLUtils.getMouseX()/(double)GLUtils.getScreenWidth();
		B = .1;
		C = .25;
		D = editorMode ? (double)GLUtils.getMouseY()/(double)GLUtils.getScreenHeight() : 1;//(double)GLUtils.getMouseY()/(double)GLUtils.getScreenHeight();
		if(editorMode)
			FontUtil.jelloFontAddAlt.drawString("X: " + Math.round(A * 100.0) / 100.0 + " Y: " + Math.round(D * 100.0) / 100.0, GLUtils.getMouseX(), GLUtils.getMouseY(), 0xff000000);
		
		GlStateManager.color(1, 1, 1, 1);
		float lastY = 0;
		for(float x = 0; x < 1.05; x += 0.05){
        	float y = (float) interpolate(x);//1-(float) (((A*(Math.pow(1-x, 3))) + ((3*B)*(Math.pow(1-x, 2)*x)) + ((3*C)*(1-x)*(Math.pow(x, 2))) + (D*(Math.pow(x, 3))))+(A*(1-x)));
        	if(lastY !=0)
        	RenderingUtil.drawVLine((float)(50 + x*50), (float)(200 + -y*50), (float)(50 + (x)*50) -  2.5f, (float)(200 + -lastY*50), 1, 0xff000000);
        	
        	lastY = y;
        }
		float percent = (((System.currentTimeMillis()-oldTime)/100) % 100f)/100;
		float y = (float) interpolate(percent);//1-(float) ((A*(Math.pow(1-percent, 3))) + ((3*B)*(Math.pow(1-percent, 2)*percent)) + ((3*C)*(1-percent)*(Math.pow(percent, 2))) + (D*(Math.pow(percent, 3)))+(A*(1-percent)));
		Gui.drawFloatRect(50 + percent*50 - 1, 200 + -y*50 - 1, 51 + percent*50, 200 + -y*50 + 1, 0xff000000);
		FontUtil.jelloFontAddAlt.drawCenteredString((int)(percent*100)+"% " + Math.round(y * 100.0) / 100.0, 50 + percent*50 - 1, 200 + -y*50 + 3, 0);
	}
	
	public double get(boolean backwards, int iterations){
		//.25f,.1f,.25f,1f
		//double point75 =  (point1*0.3d);
		//(float) Math.abs(((stepAndInterpolate(backwards, iterations)-point75)-point75-point2)*(1/(point75+point2)))
		//return (float) Math.abs(((stepAndInterpolate(backwards, iterations)-0.075)-0.175)*(1/0.175));
		double thisthing = Math.max(0, Math.abs(((stepAndInterpolate(backwards, iterations)))));//-point75)-point75-point2)*(1/(point75+(point2+0.03)))));
		return thisthing;
	}
	
	public double stepAndInterpolate(boolean backwards, int iterations){
		if(backwards){
			for(int x = 0; x < iterations; x++){
				if(percent < 100){
					percent++;
					}
			}
		}else{
			for(int x = 0; x < iterations; x++){
				
				if(percent > 0){
					percent--;
				}
		}
		}
		return interpolate(percent/100d);
	}
	
}
