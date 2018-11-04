package ru.touchcube.presentation.view_impl;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import java.util.ArrayList;
import java.util.Iterator;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.model.V3F;
import ru.touchcube.domain.utils.Pair;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.presentation.model.CubeDrawer;
import ru.touchcube.presentation.presenters_impl.WorldPresenterImpl;
import ru.touchcube.presentation.utils.MaterialManager;
import ru.touchcube.presentation.utils.MyGestureListener;
import ru.touchcube.presentation.view.WorldView;

// This class is the View for TouchCubeWorld.
// It draws cubes in 3D space using LibGDX.
// It must be created in LibGDX backend (OS) and launched in LibGDX way.
// It runs in separate thread.
// It can create CubeDrawers.

public class MyTouchCube extends ApplicationAdapter implements WorldView {

    //Camera
    private Camera cam;

    //Camera mode
    private static final int ORTHOGRAPHY = 1;
    private static final int PERSPECTIVE = 2;
    private int cameraMode = ORTHOGRAPHY;

    //Input processor
    private InputMultiplexer input;

    //Screen size
    private float w, h;

    //Cube count & size params
    private static final int maxDistance = 128;

    //Batchs
    private ModelBuilder modelBuilder;
    private ModelBatch modelBatch;
    private Environment environment;

    //Cube textures
	private MaterialManager materialManager;

	//Center
    private V3F center = new V3F(0,0,0);

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

        //Camera init
        setOrthographycCam();

        //Handing touches
        input = new InputMultiplexer();
        Gdx.input.setInputProcessor(input);
        initInputProcessor();

        //Decal batch for cubes
        modelBatch = new ModelBatch();
        modelBuilder = new ModelBuilder();
        materialManager = new MaterialManager();
        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));
        initBatch();

        presenter.start();
	}

	private void initInputProcessor(){
	    CameraInputController camController = new CameraInputController(cam);
        GestureDetector gestureDetector = new GestureDetector(gestureListener);
        input.clear();
        input.addProcessor(gestureDetector);
        input.addProcessor(camController);
    }

    private void initBatch(){

    }

    public void setOrthography(){
	    Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setOrthographycCam();
                initInputProcessor();
                initBatch();
            }
        });
    }

    public void setPerspective(){
	    Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                setPerspectiveCam();
                initInputProcessor();
                initBatch();
            }
        });
    }

    private void setOrthographycCam(){
        cameraMode=ORTHOGRAPHY;

        //Orthographic camera
        cam = new OrthographicCamera(w/60, h/60);
        float camPosition = CubeDrawer.CUBE_SIZE*maxDistance;
        float camFarDistance = (float) (Math.sqrt(3)*camPosition*2);
        cam.position.set(camPosition, camPosition, camPosition);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = camFarDistance;
        cam.update();
    }

    private void setPerspectiveCam(){
        cameraMode=PERSPECTIVE;

        //Perspective camera
        cam = new PerspectiveCamera(67, w, h);
        float camPosition = CubeDrawer.CUBE_SIZE*5;
        float camFarDistance = (float) (Math.sqrt(3)*camPosition*100);
        cam.position.set(camPosition, camPosition, camPosition);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = camFarDistance;
        cam.update();
    }

    private Iterable<ModelInstance> array = new Iterable<ModelInstance>() {
        @Override
        public Iterator<ModelInstance> iterator() {
            return new Iterator<ModelInstance>() {
                private int pos=0;
                @Override
                public boolean hasNext() {
                    return presenter.getCubes().size()-pos!=0;
                }

                @Override
                public ModelInstance next() {
                    return ((CubeDrawer)(presenter.getCubes().get(pos++))).getModelInstance();
                }
            };
        }
    };

	@Override
	public void render () {

	    if(!presenter.isRendering()) return;

        //Graphic setting
        Gdx.gl.glViewport(0, 0, (int)w, (int)h);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        //Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        //Drawing cubes
        modelBatch.begin(cam);
        modelBatch.render(array, environment);
        modelBatch.end();
	}

	private boolean isDrawing(CubeDrawer cube, int side) {
        return cube.getCube().getDrawSides()[side] && ((
                        (side == 0 && cam.position.z < 0) ||
                        (side == 1 && cam.position.x > 0) ||
                        (side == 2 && cam.position.z > 0) ||
                        (side == 3 && cam.position.x < 0) ||
                        (side == 4 && cam.position.y < 0) ||
                        (side == 5 && cam.position.y > 0)
        ) || isPerspective());
    }
	
	@Override
	public void dispose () {
		modelBatch.dispose();
	}

    @Override
    public CubeDrawing add(Cube newCube) {
        return new CubeDrawer(newCube,
                modelBuilder,
                materialManager,
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

    public boolean isOrthography(){
	    return cameraMode==ORTHOGRAPHY;
    }

    public boolean isPerspective(){
        return cameraMode==PERSPECTIVE;
    }

    public void onCentreButtonPushed() {
        Gdx.app.postRunnable(new Runnable() {
            @Override
            public void run() {
                centerModel();
                presenter.reloadCurrentModel();
            }
        });
    }

    private void centerModel(){
	    if(presenter.getCubes().size()==0) return;
        V3 pos = presenter.getCubes().get(0).getCube().getPosition();
	    float
                minX = pos.x(), maxX = pos.x(),
                minY = pos.y(), maxY = pos.y(),
                minZ = pos.z(), maxZ = pos.z();
	    for(CubeDrawing cube: presenter.getCubes()){
	        pos = cube.getCube().getPosition();
	        if(pos.x()<minX) minX=pos.x();
	        if(pos.x()>maxX) maxX=pos.x();
            if(pos.y()<minY) minY=pos.y();
            if(pos.y()>maxY) maxY=pos.y();
            if(pos.z()<minZ) minZ=pos.z();
            if(pos.z()>maxZ) maxZ=pos.z();
        }
        center = new V3F(((maxX+minX)/2)*CubeDrawer.CUBE_SIZE, ((maxY+minY)/2)*CubeDrawer.CUBE_SIZE,((maxZ+minZ)/2)*CubeDrawer.CUBE_SIZE);
    }

    private void centerCube(CubeDrawing cube){
	    V3 pos = cube.getCube().getPosition();
	    center = new V3F(pos.x()*CubeDrawer.CUBE_SIZE, pos.y()*CubeDrawer.CUBE_SIZE, pos.z()*CubeDrawer.CUBE_SIZE);
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
            if(cam instanceof OrthographicCamera){
                OrthographicCamera cam = (OrthographicCamera) MyTouchCube.this.cam;
                if(prevInitialDistance!=initialDistance){
                    prevInitialDistance=initialDistance;
                    prevCamZoom=cam.zoom;
                }
                cam.zoom=prevCamZoom*(initialDistance/distance);
            } else {
                prevInitialDistance = -1;
                prevCamZoom = 1;
            }
            return false;
        }
        @Override
        public boolean tap(float x, float y, int count, int button) {
            Pair<CubeDrawing, Integer> intersected = intersectCube(x,y);
            if(intersected!=null){
                presenter.tapOnCube(intersected.f, intersected.s);
                Gdx.input.vibrate(30);
            }
            else {
                if(presenter.getCubes().size()==0) center = new V3F(0,0,0);
                presenter.tapOnBackground();
            }
            return false;
        }
        @Override
        public boolean longPress(float x, float y) {
            Pair<CubeDrawing, Integer> intersected = intersectCube(x,y);
            if(intersected!=null){
                centerCube(intersected.f);
                presenter.longTapOnCube(intersected.f, intersected.s);
                Gdx.input.vibrate(100);
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
