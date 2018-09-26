package ru.touchcube.presentation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.utils.Pair;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.presentation.presenters_impl.WorldPresenterImpl;
import ru.touchcube.presentation.utils.MyGestureListener;
import ru.touchcube.presentation.utils.V3F;
import ru.touchcube.presentation.view.WorldView;

public class MyTouchCube extends ApplicationAdapter implements WorldView {

    //Orthographic Camera
    private OrthographicCamera cam;

    //Screen size
    private float w, h;

    //Cube count & size params
    private static final int maxDistance = 128;

    //Batchs
    private DecalBatch decalBt;

    //Cube textures
	private TextureRegion cubeTexture, cubeNCTexture;

	//Center
    private V3F center = new V3F(0,0,0);

	private function_get<TextureRegion> getCubeTexture = new function_get<TextureRegion>() {
        @Override
        public TextureRegion get() {
            if(cubeTexture==null) cubeTexture = new TextureRegion(new Texture("cube.png"));
            return cubeTexture;
        }
    };

    private function_get<TextureRegion> getCubeNCTexture = new function_get<TextureRegion>() {
        @Override
        public TextureRegion get() {
            if(cubeNCTexture==null) cubeNCTexture = new TextureRegion(new Texture("cube_nc.png"));
            return cubeNCTexture;
        }
    };

	private WorldPresenterImpl presenter;
	private function_get<Color> getCurrentColor;

	public MyTouchCube(function_get<Color> getCurrentColor){
	    this.getCurrentColor=getCurrentColor;
		presenter = new WorldPresenterImpl();
        presenter.setView(this);
	}
	
	@Override
	public void create () {
        //Getting screen size
        w= Gdx.graphics.getWidth();
        h= Gdx.graphics.getHeight();

        //Camera description
        cam = new OrthographicCamera(Gdx.graphics.getWidth()/60, Gdx.graphics.getHeight()/60);
        float camPosition = CubeDrawer.CUBE_SIZE*maxDistance;
        float camFarDistance = (float) (Math.sqrt(3)*camPosition*2);
        cam.position.set(camPosition, camPosition, camPosition);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = camFarDistance;
        cam.update();

        //Handing touches
        InputMultiplexer input = new InputMultiplexer();
        CameraInputController camController = new CameraInputController(cam);
        GestureDetector gestureDetector = new GestureDetector(gestureListener);
        input.addProcessor(gestureDetector);
        input.addProcessor(camController);
        Gdx.input.setInputProcessor(input);

        //Decal Batch for cubes
        decalBt=new DecalBatch(new CameraGroupStrategy(cam));

        presenter.start();
	}

	@Override
	public void render () {

	    if(!presenter.isRendering()) return;

        //Graphic setting
        Gdx.gl.glViewport(0, 0, (int)w, (int)h);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        //Drawing cubes
        for(CubeDrawing cubeDrawing: presenter.getCubes()){
            CubeDrawer cubeDrawer = (CubeDrawer) cubeDrawing;
            for(int i=0; i<6; i++){
                if(isDrawing(cubeDrawer, i)){
                    System.out.println("d"+i);
                    decalBt.add(cubeDrawer.getSides()[i]);
                }
            }

        }
        decalBt.flush();
	}

	private boolean isDrawing(CubeDrawer cube, int side) {
        return cube.getCube().getDrawSides()[side] && (
                        (side == 0 && cam.position.z < 0) ||
                        (side == 1 && cam.position.x > 0) ||
                        (side == 2 && cam.position.z > 0) ||
                        (side == 3 && cam.position.x < 0) ||
                        (side == 4 && cam.position.y < 0) ||
                        (side == 5 && cam.position.y > 0)
        );
    }
	
	@Override
	public void dispose () {
		decalBt.dispose();
	}

    @Override
    public CubeDrawing add(Cube newCube) {
        return new CubeDrawer(newCube,
                getCubeTexture,
                getCubeNCTexture,
                new function_get<V3F>() {
                    @Override
                    public V3F get() {
                        return center;
                    }
                });
	}

    @Override
    public Color getCurrentColor() {
        return getCurrentColor.get();
    }

    public void isPutMode() {
        presenter.isPutMode();
    }

    public void isPaintMode() {
        presenter.isPaintMode();
    }

    public void isDeleteMode() {
        presenter.isDeleteMode();
    }

    public void onCentreButtonPushed() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                presenter.onCentreButtonPushed();
            }
        });
    }

    public void onClearButtonPushed() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                presenter.onClearButtonPushed();
            }
        });
    }

    public ArrayList<Cube> getCurrentModel() {
        ArrayList<CubeDrawing> currentModel = presenter.getCubes();
        ArrayList<Cube> cubes = new ArrayList<Cube>(currentModel.size());
        for(CubeDrawing cubeDrawing: currentModel) cubes.add(cubeDrawing.getCube());
        return cubes;
    }

    public void loadModel(final ArrayList<Cube> model) {
	    Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                presenter.loadModel(model);
            }
        });
    }

    private MyGestureListener gestureListener = new MyGestureListener(){
        private float prevInitialDistance = -1;
        private float prevCamZoom = 1;
        @Override
        public boolean zoom(float initialDistance, float distance) {
            if(prevInitialDistance!=initialDistance){
                prevInitialDistance=initialDistance;
                prevCamZoom=cam.zoom;
            }
            cam.zoom=prevCamZoom*(initialDistance/distance);
            return false;
        }
        @Override
        public boolean tap(float x, float y, int count, int button) {
            Pair<CubeDrawing, Integer> intersected = intersectCube(x,y);
            if(intersected!=null){
                presenter.tapOnCube(intersected.f, intersected.s);
                System.out.println("Tapped on " + intersected.f.getCube().getPosition().toString()+" side "+intersected.s);
            }
            return false;
        }
    };

    private Pair<CubeDrawing, Integer> intersectCube(float xTouch, float yTouch){
        CubeDrawing touchedCube=null;
        int touchedSide = -1;

        Ray tapping = cam.getPickRay(xTouch, yTouch);

        Vector3 intersection, intersection1, intersection2, shortestIntersection = null;
        for (CubeDrawing cube : presenter.getCubes()){
            CubeDrawer cubeDrawer = (CubeDrawer) cube;
            for (int i=0; i<6; i++){
                //Warning: if decal wasn't added to DecalBatch in render method, its coordinates are indefinite
                //So cubeDrawer.getTrianglesOfSide(i) will work incorrect (see next)
                //So we must skip such decals
                if(!isDrawing(cubeDrawer, i)) continue;
                intersection = new Vector3(0,0,0);
                intersection1 = new Vector3(0,0,0);
                intersection2 = new Vector3(0,0,0);

                Pair<float[], float[]> triangles = cubeDrawer.getTrianglesOfSide(i);

                if (Intersector.intersectRayTriangles(tapping, triangles.f, intersection1)||
                        Intersector.intersectRayTriangles(tapping, triangles.s, intersection2)) {

                    if (intersection1.len()!=0) intersection.set(intersection1);
                    if (intersection2.len()!=0) intersection.set(intersection2);

                    if (shortestIntersection==null){
                        shortestIntersection=new Vector3();
                        shortestIntersection.set(intersection);
                        touchedCube=cube;
                        touchedSide=i;
                    } else {
                        Vector3 tapSubIntersect = new Vector3(0,0,0).set(tapping.origin).sub(intersection);
                        Vector3 tapSubShortest = new Vector3(0,0,0).set(tapping.origin).sub(shortestIntersection);
                        if (tapSubIntersect.len()<tapSubShortest.len()){
                            shortestIntersection.set(intersection);
                            touchedCube=cube;
                            touchedSide=i;
                        }
                    }
                }
            }
        }
        if(touchedCube==null) return null;
        return new Pair<CubeDrawing, Integer>(touchedCube, touchedSide);
    }

}
