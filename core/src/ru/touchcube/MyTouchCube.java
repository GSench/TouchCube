package ru.touchcube;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

public class MyTouchCube extends ApplicationAdapter implements ApplicationListener, GestureListener {

    //Orthographic Camera
    OrthographicCamera cam;

    //Input Controllers
    InputMultiplexer inp; CameraInputController camController; GestureDetector gd, gdZoom;

    //Batchs
    DecalBatch decalBt;
    SpriteBatch buttons;

    //Cube textures
    TextureRegion texture, textureC;

    //Mods
    boolean deleteMode=false, paintMode = false;

    //Palette
    PaintInterface paint;

    //Zoom attrs
    float lastDistance=0;
    int numOfFingers = 0;

    //Centre attrs
    float deltaPosX=0, deltaPosY=0, deltaPosZ=0;
    boolean isDragged=false;

    //Camera position
    float xCamPos, yCamPos, zCamPos;

    //Cube count & size params
    int cubeSize = 2, maxDistance = 128;

    //Screen size
    float w, h;

    //Decals' arrays
    ArrayList<ArrayList<Face>> decals=new ArrayList<ArrayList<Face>>(), decLoading=new ArrayList<ArrayList<Face>>();
    ArrayList<Face> blockD0 = new ArrayList<Face>(), blockD1 = new ArrayList<Face>(), blockD2 = new ArrayList<Face>(),
            blockD3 = new ArrayList<Face>(), blockD4 = new ArrayList<Face>(), blockD5 = new ArrayList<Face>();

    //Blocks' arrays
    ArrayList<String> blocks = new ArrayList<String>();
    ArrayList<String> blocksLoading = new ArrayList<String>();

    //Android API connecter
    ActionResolver actionResolver;

    //Public constructor
    public MyTouchCube(ActionResolver actionResolver){
        this.actionResolver=actionResolver;
    }

    //Finger touches' handing
    public InputProcessor inpForTouches = new InputProcessor() {

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            isDragged=false;
            numOfFingers--;
            checkFingers();
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            numOfFingers++;
            checkFingers();
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean keyUp(int keycode) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean keyTyped(char character) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean keyDown(int keycode) {
            // TODO Auto-generated method stub
            return false;
        }
    };

    /**
     * Create method
     */
    @Override
    public void create() {

        //Getting screen size
        w= Gdx.graphics.getWidth();
        h= Gdx.graphics.getHeight();

        //Camera description
        cam = new OrthographicCamera(Gdx.graphics.getWidth()/60, Gdx.graphics.getHeight()/60);
        float camPosition = cubeSize*maxDistance;
        float camFarDistance = (float) (Math.sqrt(3)*camPosition*2);
        xCamPos=camPosition;
        yCamPos=camPosition;
        zCamPos=camPosition;
        cam.position.set(camPosition, camPosition, camPosition);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = camFarDistance;
        cam.update();

        //Handing touches
        inp = new InputMultiplexer();
        gdZoom = new GestureDetector(gdZoomListener);
        gd=new GestureDetector(this);
        camController = new CameraInputController(cam);
        inp.addProcessor(inpForTouches);
        inp.addProcessor(gdZoom);
        inp.addProcessor(camController);
        inp.addProcessor(gd);
        Gdx.input.setInputProcessor(inp);

        //Sprite Batch for buttons
        buttons = new SpriteBatch();

        //Decal Batch for cubes
        decalBt=new DecalBatch(new CameraGroupStrategy(cam));

        //Cube textures setting
        Texture textures = new Texture("data/textures.png");
        texture = new TextureRegion(textures, 992, 944, 16, 16);
        textureC = new TextureRegion(textures, 1008, 944, 16, 16);

        //Decals setting
        decals.add(blockD0);
        decals.add(blockD1);
        decals.add(blockD2);
        decals.add(blockD3);
        decals.add(blockD4);
        decals.add(blockD5);
        for(int i=0; i<6; i++) decLoading.add(new ArrayList<Face>());

        //Palette setting
        paint = new PaintInterface(buttons);

        //loading latest workspace
        loadFromTemp();
    }

    //Zoom handing
    GestureListener gdZoomListener = new GestureListener(){

        @Override
        public boolean touchDown(float x, float y, int pointer, int button) {
            return false;
        }

        @Override
        public boolean tap(float x, float y, int count, int button) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean longPress(float x, float y) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean fling(float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan(float x, float y, float deltaX, float deltaY) {
            if(deltaX>=1|deltaY>1|deltaX<-1|deltaY<-1) isDragged=true;
            xCamPos=cam.position.x;
            yCamPos=cam.position.y;
            zCamPos=cam.position.z;
            return false;
        }

        @Override
        public boolean panStop(float x, float y, int pointer, int button) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean zoom(float initialDistance, float distance) {
            return true;
        }

        @Override
        public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
                             Vector2 pointer1, Vector2 pointer2) {
            checkFingers();
            float initialDistance=0;
            float distance=0;
            float zoom=0;
            distance = Math.abs((new Vector2(pointer2.x-pointer1.x, pointer2.y-pointer1.y)).len());
            if (lastDistance==0){
                initialDistance = Math.abs(((new Vector2(initialPointer2.x-initialPointer1.x, initialPointer2.y-initialPointer1.y)).len()));
            } else {
                initialDistance = lastDistance;
            }
            zoom = distance-initialDistance;
            zoom/=16;
            if (!((cam.viewportHeight<8)&&(zoom>0))){
                lastDistance = distance;
                cam.viewportWidth-=zoom;
                cam.viewportHeight-=(zoom*h/w);
                cam.update();
            }
            checkFingers();
            return true;
        }

    };

    /**
     * Updating last distance for zoom
     */
    public void checkFingers(){
        if (numOfFingers < 2){
            lastDistance = 0;
        }
    }

    /**
     * rendering libgdx objects
     */
    @Override
    public void render() {

        //Camera update
        camController.update();

        //Zoom checking
        checkFingers();

        //Graphic setting
        Gdx.gl.glViewport(0, 0, (int)w, (int)h);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        //Drawing cubes
        addAll();
        decalBt.flush();

        //Drawing palette
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        buttons.begin();
        if(!deleteMode){
            paint.drawColorInterface();
        }

        //Checking for closing color picking interface
        if(paint.closeRGBWindow){
            Gdx.input.setInputProcessor(inp);
            paint.closeRGBWindow=false;
        }

        //The end of frame
        buttons.end();
    }

    /**
     * App finishing
     */
    @Override
    public void dispose() {
        decalBt.dispose();
        saveToTemp();
        paint.saveColors();
    }

    /**
     * Creating cub in (x, y, z) with specified color or without color (standard one)
     * @param x x-position of cube in App axes (1-cube length)
     * @param y y-position of cube in App axes (1-cube length)
     * @param z z-position of cube in App axes (1-cube length)
     * @param c color of the box, mustn't be 0
     * @param noColor true if the box has standard color. In this case, c parameter becomes float instance of black color
     * @param decToLoad if box is loaded from array, her decals drawing is known. If this cube isn't loaded, use empty string ("")
     */
    public void createBox(int x, int y, int z, float c, boolean noColor, String decToLoad){
        if(c==0)c= Color.BLACK.toFloatBits();
        ArrayList<String> blockBuffer = null;
        ArrayList<ArrayList<Face>> decalBuffer=null;
        boolean loadDecals = !decToLoad.equals("");
        int k=cubeSize/2;
        float xReal=0, yReal=0, zReal=0, angleAlpha = 0, angleBeta = 0;
        Decal d [] = new Decal[6];
        boolean decs [] = new boolean [6];
        if(loadDecals){
            blockBuffer=blocksLoading;
            decalBuffer=decLoading;
            blockBuffer.add(x+"x"+y+"y"+z+"z"+c+"c"+"co"+noColor+"w"+decToLoad);
            for(int j=0; j<6; j++){
                if(decToLoad.substring(j, j+1).equals("0")) decs[j]=false;
                else decs[j]=true;
            }
        } else {
            blockBuffer=blocks;
            decalBuffer=decals;
            blockBuffer.add(x+"x"+y+"y"+z+"z"+c+"c"+"co"+noColor+"w111111");
        }
        int i;
        for (i=0; i<6; i++){
            d[i]= Decal.newDecal(2 * k, 2 * k, texture, true);
            switch (i){
                case 0:
                    xReal=x*2*k;
                    yReal=y*2*k;
                    zReal=z*2*k-k;
                    angleAlpha = 180;
                    angleBeta = 0;
                    break;
                case 1:
                    xReal=x*2*k+k;
                    yReal=y*2*k;
                    zReal=z*2*k;
                    angleAlpha = 90;
                    angleBeta = 0;
                    break;
                case 2:
                    xReal=x*2*k;
                    yReal=y*2*k;
                    zReal=z*2*k+k;
                    angleAlpha = 0;
                    angleBeta = 0;
                    break;
                case 3:
                    xReal=x*2*k-k;
                    yReal=y*2*k;
                    zReal=z*2*k;
                    angleAlpha = 270;
                    angleBeta = 0;
                    break;
                case 4:
                    xReal=x*2*k;
                    yReal=y*2*k-k;
                    zReal=z*2*k;
                    angleAlpha = 0;
                    angleBeta = 90;
                    break;
                case 5:
                    xReal=x*2*k;
                    yReal=y*2*k+k;
                    zReal=z*2*k;
                    angleAlpha = 0;
                    angleBeta = 270;
                    break;
            }
            d[i].setPosition(xReal-deltaPosX, yReal-deltaPosY, zReal-deltaPosZ);
            d[i].rotateY(angleAlpha);
            d[i].rotateX(angleBeta);
            if(!noColor){
                d[i].setTextureRegion(textureC);
                d[i].setColor(c);
            }
            Face f = new Face();
            f.decal=d[i];
            if(loadDecals)f.isDrawing=decs[i];
            decalBuffer.get(i).add(f);
        }
        if(!loadDecals)checkDecals(blockBuffer.size()-1);
    }

    public void deleteBox(int x, int y, int z, float f, boolean b, String dec){
        int index = blocks.indexOf(x+"x"+y+"y"+z+"z"+f+"c"+"co"+b+dec);
        checkDecals(index);
        blocks.remove(index);
        for (int i=0; i<6; i++){
            decals.get(i).remove(index);
        }
    }

    public Color FloatToColor(float color){
        buttons.setColor(color);
        Color ret = buttons.getColor();
        buttons.setColor(Color.WHITE);
        return ret;
    }

    public int reverse(int index){
        switch (index){
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 0;
            case 3:
                return 1;
            case 4:
                return 5;
            case 5:
                return 4;
        }
        return index;
    }

    public void checkDecals(int index){
        int x = koordFromArray(blocks, index, 1);
        int y = koordFromArray(blocks, index, 2);
        int z = koordFromArray(blocks, index, 3);
        String cl [] = new String[]{x+"x"+y+"y"+(z-1), (x+1)+"x"+y+"y"+z, x+"x"+y+"y"+(z+1), (x-1)+"x"+y+"y"+z, x+"x"+(y-1)+"y"+z, x+"x"+(y+1)+"y"+z };
        Color c = FloatToColor(colorFromArray(blocks, index));
        boolean noColor = colorfullFromArray(blocks, index);
        float a = c.a;
        for(int i = 0; i<blocks.size(); i++){
            String koords = (blocks.get(i)).substring(0, (blocks.get(i)).indexOf("z"));
            float col = colorFromArray(blocks, i);
            boolean nc = colorfullFromArray(blocks, i);
            Color co = FloatToColor(col);
            int j=0;
            while(j<6){
                if(koords.equals(cl[j])){
                    if(!deleteMode){
                        if(((co.a<0.9)&(!nc))&((a>0.9)|(noColor))){
                            ((decals.get(reverse(j))).get(i)).isDrawing=false;
                            ((decals.get(j)).get(index)).isDrawing=true;
                            putDecDrawToArray(i, reverse(j), 0);
                            putDecDrawToArray(index, j, 1);
                        }
                        else if(((co.a>0.9)|(nc))&((a<0.9)&(!noColor))){
                            ((decals.get(j)).get(index)).isDrawing=false;
                            ((decals.get(reverse(j))).get(i)).isDrawing=true;
                            putDecDrawToArray(index, j, 0);
                            putDecDrawToArray(i, reverse(j), 1);
                        }
                        else if(((co.a>0.9)|(nc))&((a>0.9)|(noColor))){
                            ((decals.get(reverse(j))).get(i)).isDrawing=false;
                            ((decals.get(j)).get(index)).isDrawing=false;
                            putDecDrawToArray(i, reverse(j), 0);
                            putDecDrawToArray(index, j, 0);
                        }
                        else if(((co.a<0.9)&(!nc))&((a<0.9)&(!noColor))){
                            ((decals.get(reverse(j))).get(i)).isDrawing=true;
                            ((decals.get(j)).get(index)).isDrawing=true;
                            putDecDrawToArray(i, reverse(j), 1);
                            putDecDrawToArray(index, j, 1);
                        }
                    } else{
                        ((decals.get(reverse(j))).get(i)).isDrawing=true;
                        putDecDrawToArray(i, reverse(j), 1);
                    }
                }
                j++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void loadAll(final ArrayList<String> ars){
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                blocks.clear();
                for(ArrayList<Face> ds: decals){
                    ds.clear();
                }
                if (ars!=null){
                    for (int i=0; i<ars.size(); i++){
                        int w = indexOfSymbol(ars.get(i), "w");
                        createBox(koordFromArray(ars, i, 1), koordFromArray(ars, i, 2), koordFromArray(ars, i, 3), colorFromArray(ars, i), colorfullFromArray(ars, i), ars.get(i).substring(w+1));
                    }
                    blocks=(ArrayList<String>) blocksLoading.clone();
                    decals=(ArrayList<ArrayList<Face>>) decLoading.clone();
                    blocksLoading=null;
                    for(@SuppressWarnings("unused") ArrayList<Face> ds: decLoading)ds=null;
                    blocksLoading=new ArrayList<String>();
                    for(@SuppressWarnings("unused") ArrayList<Face> ds: decLoading) ds=(new ArrayList<Face>());
                    saveToTemp();
                } else {
                    deltaPosX=0;
                    deltaPosY=0;
                    deltaPosZ=0;
                    createBox(0, 0, 0, 0, true, "");
                }
            }
        });
    }

    public int indexOfSymbol(String str, String what){
        for(int i=0; i<str.length(); i++)
            if(str.substring(i, i+1).equals(what))
                return i;
        return -1;
    }

    public boolean colorfullFromArray(ArrayList<String> ars, int i) {
        String r = ars.get(i);
        boolean q = Boolean.parseBoolean(r.substring(r.indexOf("co")+2, r.indexOf("w")));
        return q;
    }

    public float colorFromArray(ArrayList<String> ars, int i){
        String presentKoord = ars.get(i);
        return Float.parseFloat(presentKoord.substring(presentKoord.indexOf("z")+1, presentKoord.indexOf("c")));
    }

    public void putDecDrawToArray(int index, int number, int put){
        String tmp = blocks.get(index).substring(0, blocks.get(index).indexOf("w"));
        String get = blocks.get(index).substring(blocks.get(index).indexOf("w")+1);
        String one = "", two="";
        if(number!=0) one = get.substring(0, number);
        if(number!=5) two = get.substring(number+1);
        blocks.remove(index);
        blocks.add(index, tmp+"w"+one+put+two);
    }

    public void addAll(){
        boolean enableDrawing = false;
        for(ArrayList<Face> ds: decals){
            switch(decals.indexOf(ds)){
                case 0:
                    if(zCamPos<0){
                        enableDrawing = true;
                    }
                    break;
                case 1:
                    if(xCamPos>0){
                        enableDrawing = true;
                    }
                    break;
                case 2:
                    if(zCamPos>0){
                        enableDrawing = true;
                    }
                    break;
                case 3:
                    if(xCamPos<0){
                        enableDrawing = true;
                    }
                    break;
                case 4:
                    if(yCamPos<0){
                        enableDrawing = true;
                    }
                    break;
                case 5:
                    if(yCamPos>0){
                        enableDrawing = true;
                    }
                    break;
            }
            if(enableDrawing)
                for (Face f: ds)
                    if(f.isDrawing)decalBt.add(f.decal);
            enableDrawing = false;
        }
    }

    public int koordFromArray(ArrayList<String> ar, int poz, int k){
        String presentKoord = ar.get(poz);
        int ret=0;
        switch (k){
            case 1:
                ret = Integer.parseInt(presentKoord.substring(0, presentKoord.indexOf("x")));
                break;
            case 2:
                ret = Integer.parseInt(presentKoord.substring(presentKoord.indexOf("x")+1, presentKoord.indexOf("y")));
                break;
            case 3:
                ret = Integer.parseInt(presentKoord.substring(presentKoord.indexOf("y")+1, presentKoord.indexOf("z")));
                break;
        }
        return ret;
    }

    public void centralize(){
        if (blocks.size()>0){
            saveToTemp();
            int k =cubeSize;
            float maxX=koordFromArray(blocks, 0, 1)*k;
            float minX=koordFromArray(blocks, 0, 1)*k;
            float maxY=koordFromArray(blocks, 0, 2)*k;
            float minY=koordFromArray(blocks, 0, 2)*k;
            float maxZ=koordFromArray(blocks, 0, 3)*k;
            float minZ=koordFromArray(blocks, 0, 3)*k;
            for (int i=0; i<blocks.size(); i++) {
                if (koordFromArray(blocks, i, 1)*k<minX){
                    minX=koordFromArray(blocks, i, 1)*k;
                }
                if (koordFromArray(blocks, i, 1)*k>maxX){
                    maxX=koordFromArray(blocks, i, 1)*k;
                }
                if (koordFromArray(blocks, i, 2)*k<minY){
                    minY=koordFromArray(blocks, i, 2)*k;
                }
                if (koordFromArray(blocks, i, 2)*k>maxY){
                    maxY=koordFromArray(blocks, i, 2)*k;
                }
                if (koordFromArray(blocks, i, 3)*k<minZ){
                    minZ=koordFromArray(blocks, i, 3)*k;
                }
                if (koordFromArray(blocks, i, 3)*k>maxZ){
                    maxZ=koordFromArray(blocks, i, 3)*k;
                }
            }
            deltaPosX=(maxX+minX)/2;
            deltaPosY=(maxY+minY)/2;
            deltaPosZ=(maxZ+minZ)/2;
            loadFromTemp();
        }
    }

    public void saveToTemp(){
        Preferences prefs = Gdx.app.getPreferences("TEMP");
        prefs.clear();
        int i;
        for (i=0; i<blocks.size(); i++){
            prefs.putString("b"+i, blocks.get(i));
        }
        prefs.putInteger("n", blocks.size());
        prefs.flush();
    }

    public void loadFromTemp(){
        ArrayList<String> retSt=new ArrayList<String>();
        Preferences prefs = Gdx.app.getPreferences("TEMP");
        int num=prefs.getInteger("n");
        for (int i=0; i<num; i++){
            retSt.add(prefs.getString("b"+i));
        }
        prefs.flush();
        loadAll(retSt);
    }

    public void changeDeleteMode(int on){
        switch (on){
            case 1:
                deleteMode=false;
                paintMode=false;
                break;
            case 2:
                deleteMode=false;
                paintMode=true;
                break;
            case 3:
                deleteMode=true;
                paintMode=false;
                break;
        }
    }

    public Vector3 minus(Vector3 a, Vector3 b){
        return new Vector3(a.x-b.x, a.y-b.y, a.z-b.z);
    }

    public String intersectCube(float x, float y){
        int currentBox=0;
        int currentFace = 0;
        Ray tapping = cam.getPickRay(x, y);
        float verticesOfDecal[]=new float[24];
        float triangle1[] = new float[9];
        float triangle2[] = new float[9];
        Vector3 intersection = new Vector3(0,0,0);
        Vector3 intersection1 = new Vector3(0,0,0);
        Vector3 intersection2 = new Vector3(0,0,0);
        Vector3 shortestIntersection = null;
        boolean intersectionOccured = false;
        for (ArrayList<Face> face : decals){
            for (Face f: face){
                Decal d = f.decal;
                intersection=new Vector3(0,0,0);
                intersection1 = new Vector3(0,0,0);
                intersection2 = new Vector3(0,0,0);
                verticesOfDecal=d.getVertices();
                triangle1[0]=verticesOfDecal[0];
                triangle1[1]=verticesOfDecal[1];
                triangle1[2]=verticesOfDecal[2];
                triangle1[3]=verticesOfDecal[6];
                triangle1[4]=verticesOfDecal[7];
                triangle1[5]=verticesOfDecal[8];
                triangle1[6]=verticesOfDecal[12];
                triangle1[7]=verticesOfDecal[13];
                triangle1[8]=verticesOfDecal[14];

                triangle2[0]=verticesOfDecal[6];
                triangle2[1]=verticesOfDecal[7];
                triangle2[2]=verticesOfDecal[8];
                triangle2[3]=verticesOfDecal[12];
                triangle2[4]=verticesOfDecal[13];
                triangle2[5]=verticesOfDecal[14];
                triangle2[6]=verticesOfDecal[18];
                triangle2[7]=verticesOfDecal[19];
                triangle2[8]=verticesOfDecal[20];

                if ((Intersector.intersectRayTriangles(tapping, triangle1, intersection1))||
                        (Intersector.intersectRayTriangles(tapping, triangle2, intersection2)))
                {
                    intersectionOccured=true;
                    if (!(intersection1.len()==0)){
                        intersection.set(intersection1);
                    }
                    if (!(intersection2.len()==0)){
                        intersection.set(intersection2);
                    }

                    if (shortestIntersection==null){
                        shortestIntersection=new Vector3();
                        shortestIntersection.set(intersection);
                        currentBox=face.indexOf(f);
                        currentFace=decals.indexOf(face);
                    } else {
                        if (minus(tapping.origin, intersection).len()<minus(tapping.origin, shortestIntersection).len()){
                            shortestIntersection.set(intersection);
                            currentBox=face.indexOf(f);
                            currentFace=decals.indexOf(face);
                        }
                    }
                }
            }
        }
        return intersectionOccured+"b"+currentBox+"q"+currentFace;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        paint.px.dispose();
        paint.pxBlack.dispose();
        paint.pxBrit.dispose();
        paint.black.dispose();
        paint.brit.dispose();
        paint.rgb.dispose();
    }

    @Override
    public void resume() {
        paint=new PaintInterface(buttons);
    }

    public void tapOnBox(float x, float y){
        String ret = intersectCube(x, y);
        int currentBox=Integer.parseInt(ret.substring(ret.indexOf("b")+1, ret.indexOf("q")));
        int currentFace=Integer.parseInt(ret.substring(ret.indexOf("q")+1));
        boolean inters = Boolean.parseBoolean(ret.substring(0, ret.indexOf("b")));
        if (inters){
            int xNew=0;
            int yNew=0;
            int zNew=0;
            switch (currentFace){
                case 0:
                    xNew = koordFromArray(blocks, currentBox, 1);
                    yNew = koordFromArray(blocks, currentBox, 2);
                    zNew = koordFromArray(blocks, currentBox, 3)-1;
                    break;
                case 1:
                    xNew = koordFromArray(blocks, currentBox, 1)+1;
                    yNew = koordFromArray(blocks, currentBox, 2);
                    zNew = koordFromArray(blocks, currentBox, 3);
                    break;
                case 2:
                    xNew = koordFromArray(blocks, currentBox, 1);
                    yNew = koordFromArray(blocks, currentBox, 2);
                    zNew = koordFromArray(blocks, currentBox, 3)+1;
                    break;
                case 3:
                    xNew = koordFromArray(blocks, currentBox, 1)-1;
                    yNew = koordFromArray(blocks, currentBox, 2);
                    zNew = koordFromArray(blocks, currentBox, 3);
                    break;
                case 4:
                    xNew = koordFromArray(blocks, currentBox, 1);
                    yNew = koordFromArray(blocks, currentBox, 2)-1;
                    zNew = koordFromArray(blocks, currentBox, 3);
                    break;
                case 5:
                    xNew = koordFromArray(blocks, currentBox, 1);
                    yNew = koordFromArray(blocks, currentBox, 2)+1;
                    zNew = koordFromArray(blocks, currentBox, 3);
                    break;
            }
            if (deleteMode){
                deleteBox(koordFromArray(blocks, currentBox, 1), koordFromArray(blocks, currentBox, 2), koordFromArray(blocks, currentBox, 3), colorFromArray(blocks, currentBox), colorfullFromArray(blocks, currentBox), blocks.get(currentBox).substring(blocks.get(currentBox).indexOf("w")));
            } else {
                if(paintMode){
                    if(paint.currentColor==0){
                        String s=blocks.get(currentBox).substring(0, blocks.get(currentBox).indexOf("z")+1)+paint.paintColor.toFloatBits()+"c"+"co"+true+"w111111";
                        blocks.remove(currentBox);
                        blocks.add(currentBox, s);
                        for(ArrayList<Face> dec: decals){
                            ((dec.get(currentBox)).decal).setTextureRegion(texture);
                            ((dec.get(currentBox)).decal).setColor(Color.WHITE);
                        }
                    } else {
                        String s=blocks.get(currentBox).substring(0, blocks.get(currentBox).indexOf("z")+1)+paint.paintColor.toFloatBits()+"c"+"co"+false+"w111111";
                        blocks.remove(currentBox);
                        blocks.add(currentBox, s);
                        for(ArrayList<Face> dec: decals){
                            ((dec.get(currentBox)).decal).setTextureRegion(textureC);
                            ((dec.get(currentBox)).decal).setColor(paint.paintColor);
                        }
                    }
                    checkDecals(currentBox);
                } else {
                    Color col = new Color(paint.paintColor);
                    if(paint.currentColor==0)
                        if(xNew>maxDistance||xNew<-maxDistance||yNew>maxDistance||yNew<-maxDistance||zNew>maxDistance||zNew<-maxDistance)
                            actionResolver.showToast("range rendering achived");
                        else createBox(xNew, yNew, zNew, 0, true, "");
                    else
                    if(xNew>maxDistance||xNew<-maxDistance||yNew>maxDistance||yNew<-maxDistance||zNew>maxDistance||zNew<-maxDistance)
                        actionResolver.showToast("range rendering achived");
                    else createBox(xNew, yNew, zNew, col.toFloatBits(), false, "");
                    Gdx.input.vibrate(19);
                }
            }
        }
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if((x>=(float)(w-0.1*h))&&(y<=(float)(h*0.9))&&(y>=(float)(h*0.2))&&(!deleteMode)){
            paint.paintTapListener(x, y);
        } else if (blocks.isEmpty()){
            createBox(0, 0, 0, 0, true, "");
        } else {
            tapOnBox(x, y);
        }
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        if(!isDragged){
            int k = cubeSize;
            String ret = intersectCube(x, y);
            int cube=Integer.parseInt(ret.substring(ret.indexOf("b")+1, ret.indexOf("q")));
            boolean inters = Boolean.parseBoolean(ret.substring(0, ret.indexOf("b")));
            if(inters){
                deltaPosX=k*koordFromArray(blocks, cube, 1);
                deltaPosY=k*koordFromArray(blocks, cube, 2);
                deltaPosZ=k*koordFromArray(blocks, cube, 3);
                saveToTemp();
                loadFromTemp();
                Gdx.input.vibrate(70);
            }
        }
        return true;
    }
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
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
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }
}
