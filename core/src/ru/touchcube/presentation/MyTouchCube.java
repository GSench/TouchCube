package ru.touchcube.presentation;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.presentation.presenters_impl.MainPresenterImpl;
import ru.touchcube.presentation.utils.V3F;

public class MyTouchCube extends ApplicationAdapter {

    //Orthographic Camera
    OrthographicCamera cam;

    //Input Controllers
    CameraInputController camController;

    //Screen size
    float w, h;

    //Cube count & size params
    int maxDistance = 128;

    //Camera position
    float xCamPos, yCamPos, zCamPos;

    //Batchs
    DecalBatch decalBt;

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

	private MainPresenterImpl presenter;

	public MyTouchCube(MainPresenterImpl presenter){
		this.presenter=presenter;
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
        xCamPos=camPosition;
        yCamPos=camPosition;
        zCamPos=camPosition;
        cam.position.set(camPosition, camPosition, camPosition);
        cam.lookAt(0,0,0);
        cam.near = 0.1f;
        cam.far = camFarDistance;
        cam.update();

        //Handing touches
        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        //Decal Batch for cubes
        decalBt=new DecalBatch(new CameraGroupStrategy(cam));
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
            for(Decal side: cubeDrawer.getSides())
                decalBt.add(side);
        }
        decalBt.flush();
	}
	
	@Override
	public void dispose () {
		decalBt.dispose();
	}

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
}
