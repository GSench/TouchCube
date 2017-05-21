package ru.touchcube;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class PaintInterface {
	SpriteBatch batch;
	TextureRegion color;
	TextureRegion noColor;
	TextureRegion choise;
	TextureRegion RGBWindow;
	TextureRegion alfaBackground;
	TextureRegion colorRGB;
	TextureRegion alfaBackground2;
	TextureRegion borders;
	Texture rgb;
	Texture brit;
	TextureRegion alpha;
	Texture black;
	Pixmap px;
	Pixmap pxBrit;
	Pixmap pxBlack;
	boolean rgbmod = false;
	boolean closeRGBWindow=false;
	Color pallitraC [] = new Color[]{ Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN, Color.YELLOW };
	Color pallitraRealC [] = new Color[]{ Color.RED, Color.ORANGE, Color.BLUE, Color.GREEN, Color.YELLOW };
	int greyDP [] = new int[]{ 255, 255, 255, 255, 255 };
	int alphaCP [] = new int[]{ 255, 255, 255, 255, 255 };
	int currentColor=0;
	Color paintColor=pallitraC[0];
	Color colorFromRgb=pallitraC[0];
	Color realColor=colorFromRgb;
	int greyDelta=255;
	int alphaC=255;
	float h;
	float w;
	float a;
	int pxW;
	
	public PaintInterface(SpriteBatch b){
		batch=b;
		Texture texture = new Texture("data/textures.png");
		color = new TextureRegion(texture, 1008, 944, 16, 16);
		noColor = new TextureRegion(texture, 992, 944, 16, 16);
		choise = new TextureRegion(texture, 640, 0, 64, 32);
		RGBWindow = new TextureRegion(texture, 0, 0, 640, 720);
		alfaBackground = new TextureRegion(texture, 720, 896, 64, 64);
		colorRGB = new TextureRegion(texture, 768, 960, 256, 64);
		alfaBackground2 = new TextureRegion(texture, 512, 960, 256, 64);
		borders = new TextureRegion(texture, 256, 960, 256, 64);
		alpha=new TextureRegion(texture, 0, 960, 256, 64);
		h= Gdx.graphics.getHeight();
		w= Gdx.graphics.getWidth();
		if(h>w){
			a=w;
			w=h;
			h=a;
		}
		a=(float) (0.1*h);
		pxW=(int)(w/3.75);
		px = new Pixmap(512, 128, Format.RGBA8888);
		for (int x = 0; x < px.getWidth(); x++) {
		    int rgb = HSBtoRGBA8888(x / (float)px.getWidth(), 1f, 1f);
		    px.setColor(rgb);
		    px.drawRectangle(x, 0, 1, px.getHeight());
		}
		rgb=new Texture(px);
		
		pxBrit = new Pixmap(512, 128, Format.RGBA8888);
		int f=2;
		int x=0;
		for (int j = 0; j < 256; j++) {
			int c = (j << 24) | (j << 16) | (j << 8) | 0x000000ff;
			pxBrit.setColor(c);
			pxBrit.drawRectangle(x, 0, f, pxBrit.getHeight());
			x+=f;
		}
		brit=new Texture(pxBrit);
		
		pxBlack=new Pixmap(16, 128, Format.RGBA8888);
		pxBlack.setColor(Color.BLACK);
		for(int l=0; l<16; l++){
			pxBlack.drawRectangle(l, 0, 1, 128);
		}
		black=new Texture(pxBlack);
		
		loadColors();
	}
	
	public GestureDetector getRGB = new GestureDetector(new GestureListener(){

		@Override
		public boolean touchDown(float x, float y, int pointer, int button) {
			getRGB(x, y);
			getBlack(x, y);
			getGreyC(x, y);
			getAlpha(x, y);
			return false;
		}

		@Override
		public boolean tap(float x, float y, int count, int button) {
			paintTapListener(x, y);
			return false;
		}

		@Override
		public boolean longPress(float x, float y) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean fling(float velocityX, float velocityY, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean pan(float x, float y, float deltaX, float deltaY) {
			getRGB(x, y);
			getBlack(x, y);
			getGreyC(x, y);
			getAlpha(x, y);
			return false;
		}

		@Override
		public boolean panStop(float x, float y, int pointer, int button) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean zoom(float initialDistance, float distance) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
				Vector2 pointer1, Vector2 pointer2) {
			// TODO Auto-generated method stub
			return false;
		}
		
	});
	
	public void getRGB(float x, float y){
		if((x>=w*2/3-w*0.125+w/60)&(x<=w*2/3-w*0.125+w/60+pxW)&(y>=h*0.5-h/36-h*5/36+h/12)&(y<=h*0.5-h/36+h/12)){
			x=(float) (x-(w*2/3-w*0.125+w/60));
			for (int i = 0; i < pxW; i++) {
				if(i==(int)x){
				colorFromRgb = new Color(HSBtoRGBA8888(x / (float)pxW, 1f, 1f));
				realColor=colorFromRgb;
				setColor();
				}
			}
		}
	}
	
	public void getGreyC(float x, float y){
		if((x>=w*2/3-w*0.125+w/60)&(x<=w*2/3-w*0.125+w/60+pxW)&(y>=h-(h/3+h/36+h*5/36-h/12))&(y<=h-(h/3+h/36-h/12))){
			x=(float) (x-(w*2/3-w*0.125+w/60));
			float f=(float) (pxW*0.00195313);
			for (int j = 0; j < 511; j++) {
				if((x>=j*f)&(x<j*f+f)){
					greyDelta=j;
					setColor();
				}
			}
		}
	}
	
	public void getAlpha(float x, float y){
		if((x>=w*2/3-w*0.125+w/60)&(x<=w*2/3-w*0.125+w/60+pxW)&(y>=h-(h/6+h/36+h*5/36-h/12))&(y<=h-(h/6+h/36-h/12))){
			x=(float) (x-(w*2/3-w*0.125+w/60));
			float f=(float) (pxW*0.00390625);
			for (int j = 0; j < 256; j++) {
				if((x>=j*f)&(x<j*f+f)){
					alphaC=j;
					setColor();
				}
			}
		}
	}
	
	public void getBlack(float x, float y){
		if((x>=w*2/3-w*0.125+w/60+pxW)&(x<=w*2/3-w*0.125+w/60+pxW+w/120)&(y>=h*0.5-h/36-h*5/36+h/12)&(y<=h*0.5-h/36+h/12)){
			colorFromRgb= Color.BLACK;
			realColor= Color.BLACK;
			setColor();
		}
	}
	
	public void setColor(){
		int delt = 255-greyDelta;
		Color t= new Color(realColor);
		if(delt>0){
			delt=255-delt;
			int d = Color.toIntBits(255, delt, delt, delt);
			Color de = new Color(d);
			t.mul(de);
		} else if(delt<0){
			delt=Math.abs(delt);
			int d = Color.toIntBits(255, delt, delt, delt);
			Color de = new Color(d);
			int r=delt;
			int g=delt;
			int b=delt;
			if(de.r+t.r>=1){
				r=0;
			}
			if(de.g+t.g>=1){
				g=0;
			}
			if(de.b+t.b>=1){
				b=0;
			}
			d = Color.toIntBits(255, b, g, r);
			de = new Color(d);
			float rt = t.r+de.r;
			float gt = t.g+de.g;
			float bt = t.b+de.b;
			if(r==0) rt = 1;
			if(g==0) gt = 1;
			if(b==0) bt = 1;
			t=new Color(rt, gt, bt, 1);
		}
		colorFromRgb=t;
		int a = Color.toIntBits(alphaC, 0, 0, 0);
		Color def = new Color(a);
		colorFromRgb = new Color(colorFromRgb.r, colorFromRgb.g, colorFromRgb.b, def.a);
		for(int i=0; i<5; i++){
			if(i+1==currentColor){
				pallitraC[i]=colorFromRgb;
				pallitraRealC[i]=realColor;
				greyDP[i]=greyDelta;
				alphaCP[i]=alphaC;
			}
		}
		paintColor=colorFromRgb;
	}
	
	public void drawColorInterface(){
		batch.setColor(Color.WHITE);
		for(int i=2; i<8; i++){
			batch.draw(alfaBackground, w-a, (float) (0.1*h*i), a, a);
		}
		batch.draw(noColor, w-a, (float) (0.7*h), a, a);
		batch.setColor(pallitraC[0]);
		batch.draw(color, w-a, (float) (0.6*h), a, a);
		batch.setColor(pallitraC[1]);
		batch.draw(color, w-a, (float) (0.5*h), a, a);
		batch.setColor(pallitraC[2]);
		batch.draw(color, w-a, (float) (0.4*h), a, a);
		batch.setColor(pallitraC[3]);
		batch.draw(color, w-a, (float) (0.3*h), a, a);
		batch.setColor(pallitraC[4]);
		batch.draw(color, w-a, (float) (0.2*h), a, a);
		batch.setColor(Color.WHITE);
		drawChoise();
		drawRGB();
		
	}
	
	private void drawRGB() {
		if(rgbmod){

			batch.draw(RGBWindow, (float) (w*2/3-w*0.125), h/6-h/12, w/3, h*2/3);
			batch.draw(alfaBackground2, (float) (w*2/3-w*0.125+w/60), (float)(h/6+h*0.5+h/36-h/12), pxW, (float)(h/9));
			batch.setColor(colorFromRgb);
			batch.draw(colorRGB, (float) (w*2/3-w*0.125+w/60), (float)(h/6+h*0.5+h/36-h/12), pxW, (float)(h/9));
			batch.setColor(Color.WHITE);
			batch.draw(rgb, (float)(w*2/3-w*0.125+w/60), (float)(h*0.5+h/36-h/12), pxW, (float)(h*5/36));
			batch.draw(black, (float)(w*2/3-w*0.125+w/60+pxW), (float)(h*0.5+h/36-h/12), (float)(w/120), (float)(h*5/36));
			batch.draw(brit, (float)(w*2/3-w*0.125+w/60), (float)(h/3+h/36-h/12), pxW, (float)(h*5/36));
			batch.draw(alpha, (float)(w*2/3-w*0.125+w/60), (float)(h/6+h/36-h/12), pxW, (float)(h*5/36));
			
			batch.draw(borders, (float) (w*2/3-w*0.125+w/60-4), (float)(h/6+h*0.5+h/36-1-h/12), pxW+8, (float)(h/9+2));
			batch.draw(borders, (float)(w*2/3-w*0.125+w/60-4), (float)(h*0.5+h/36-1-h/12), pxW+24, (float)(h*5/36+2));
			batch.draw(borders, (float)(w*2/3-w*0.125+w/60-4), (float)(h/3+h/36-1-h/12), pxW+8, (float)(h*5/36+2));
			batch.draw(borders, (float)(w*2/3-w*0.125+w/60-4), (float)(h/6+h/36-1-h/12), pxW+8, (float)(h*5/36+2));
			
		}
	}

	public void drawChoise(){
		switch(currentColor){
		case 0:
			batch.draw(choise, w-a-a, (float) (0.7*h), 2*a, a);
			break;
		case 1:
			batch.draw(choise, w-a-a, (float) (0.6*h), 2*a, a);
			break;
		case 2:
			batch.draw(choise, w-a-a, (float) (0.5*h), 2*a, a);
			break;
		case 3:
			batch.draw(choise, w-a-a, (float) (0.4*h), 2*a, a);
			break;
		case 4:
			batch.draw(choise, w-a-a, (float) (0.3*h), 2*a, a);
			break;
		case 5:
			batch.draw(choise, w-a-a, (float) (0.2*h), 2*a, a);
			break;
		}
	}
	
	public void paintTapListener(float x, float y){
        if((y>0.2*h)&(y<0.3*h)&(x>(w-a))){
			paintColor.add(0, 0, 0, 0);
			currentColor=0;
			onButtonShowRGB();
        } else if(x>(w-a)){
        	for(int i=0; i<5; i++){
        		if((y>0.1*h*(i+3))&(y<0.1*h*(i+4))){
        			if(currentColor==i+1){
        				onButtonShowRGB();
        			} else {
        			paintColor=pallitraC[i];
        			currentColor=i+1;
        			colorFromRgb=pallitraC[i];
        			realColor=pallitraRealC[i];
        			greyDelta=greyDP[i];
        			alphaC=alphaCP[i];
        			}
        		}
        	}
		} else if(!((x>w*2/3-w*0.125)&(x<w*0.875)&(y>h/6+h/12)&(y<h*5/6+h/12))){
			onButtonShowRGB();
		}
	}

	public void onButtonShowRGB() {
		if(rgbmod){
			closeRGBWindow=true;
			rgbmod = false;
		} else if(currentColor!=0) {
			rgbmod = true;
			Gdx.input.setInputProcessor(getRGB);
		}
	}
	
	public void saveColors(){
		Preferences prefs = Gdx.app.getPreferences("COLORS");
		Preferences prefs1 = Gdx.app.getPreferences("NOFIRSTCALL");
		prefs1.putBoolean("NOONE", true);
		prefs1.flush();
		prefs.clear();
		for(int i=0; i<5; i++){
			prefs.putFloat("C"+i, pallitraC[i].toFloatBits());
			prefs.putFloat("CR"+i, pallitraRealC[i].toFloatBits());
			prefs.putInteger("CG"+i, greyDP[i]);
			prefs.putInteger("CA"+i, alphaCP[i]);

		}
		prefs.flush();
	}
	
	public void loadColors(){
		Preferences prefs = Gdx.app.getPreferences("COLORS");
		Preferences prefs1 = Gdx.app.getPreferences("NOFIRSTCALL");
		if(prefs1.getBoolean("NOONE")){
			for(int i=0; i<5; i++){
				pallitraC[i]= new Color(floatToColor(prefs.getFloat("C"+i)));
				pallitraRealC[i]= new Color(floatToColor(prefs.getFloat("CR"+i)));
				greyDP[i]=prefs.getInteger("CG"+i);
				alphaCP[i]=prefs.getInteger("CA"+i);
			}
		}
		prefs.flush();
		prefs1.flush();
	}
	
	public Color floatToColor(float color){
		batch.setColor(color);
		Color ret = batch.getColor();
		batch.setColor(Color.WHITE);
		return ret;
	}
	
	public static int HSBtoRGBA8888(float hue, float saturation, float brightness) {
		int r = 0, g = 0, b = 0;
		if (saturation == 0) {
			r = g = b = (int)(brightness * 255.0f + 0.5f);
		} else {
			float h = (hue - (float)Math.floor(hue)) * 6.0f;
			float f = h - (float) Math.floor(h);
			float p = brightness * (1.0f - saturation);
			float q = brightness * (1.0f - saturation * f);
			float t = brightness * (1.0f - (saturation * (1.0f - f)));
			switch ((int)h) {
			case 0:
				r = (int)(brightness * 255.0f + 0.5f);
				g = (int)(t * 255.0f + 0.5f);
				b = (int)(p * 255.0f + 0.5f);
				break;
			case 1:
				r = (int)(q * 255.0f + 0.5f);
				g = (int)(brightness * 255.0f + 0.5f);
				b = (int)(p * 255.0f + 0.5f);
				break;
			case 2:
				r = (int)(p * 255.0f + 0.5f);
				g = (int)(brightness * 255.0f + 0.5f);
				b = (int)(t * 255.0f + 0.5f);
				break;
			case 3:
				r = (int)(p * 255.0f + 0.5f);
				g = (int)(q * 255.0f + 0.5f);
				b = (int)(brightness * 255.0f + 0.5f);
				break;
			case 4:
				r = (int)(t * 255.0f + 0.5f);
				g = (int)(p * 255.0f + 0.5f);
				b = (int)(brightness * 255.0f + 0.5f);
				break;
			case 5:
				r = (int)(brightness * 255.0f + 0.5f);
				g = (int)(p * 255.0f + 0.5f);
				b = (int)(q * 255.0f + 0.5f);
				break;
			}
		}
		return (r << 24) | (g << 16) | (b << 8) | 0x000000ff;
	}
}
	