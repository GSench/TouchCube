package ru.touchcube.presentation.model;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.model.V3F;
import ru.touchcube.domain.utils.Pair;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.presentation.utils.MaterialManager;

// Implementation of CubeDrawing.
// This class receives events from interactor and provides methods of drawing cube using LibGDX
// Instances of this class must be created in LibGDX view

public class CubeDrawer extends CubeDrawing {

    public static final int CUBE_SIZE = 2;
    private Model model;
    private ModelInstance modelInstance;
    private ModelBuilder modelBuilder;
    private MaterialManager materialManager;
    private function_get<V3F> getCenter;

    public CubeDrawer(Cube cube,
                      ModelBuilder modelBuilder,
                      MaterialManager materialManager,
                      function_get<V3F> getCenter) {
        super(cube);
        this.modelBuilder=modelBuilder;
        this.materialManager=materialManager;
        this.getCenter=getCenter;
    }

    public ModelInstance getModelInstance() {
        return modelInstance;
    }

    @Override
    public void onDelete() {
        model.dispose();
    }

    @Override
    public void onCreate() {
        Material material = materialManager.forColor(getCube().getColor());
        model = modelBuilder.createBox(CUBE_SIZE, CUBE_SIZE, CUBE_SIZE, material, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        V3F center = getCenter.get();
        float x = getCube().getPosition().x()*CUBE_SIZE-center.x();
        float y = getCube().getPosition().y()*CUBE_SIZE-center.y();
        float z = getCube().getPosition().z()*CUBE_SIZE-center.z();
        modelInstance = new ModelInstance(model, x, y, z);
    }

    @Override
    public void onColorChanged() {
        for(int m=0;m<modelInstance.materials.size;m++) {
            Material mat = modelInstance.materials.get(m);
            materialManager.toColor(mat, getCube().getColor());
        }
    }

    public Pair<float[], float[]> getTrianglesOfSide(int side){
        float triangle1[] = new float[9];
        float triangle2[] = new float[9];

        /**
        float[] verticesOfDecal=sides[side].getVertices();
        //vertices: {x1, y1, z1, ?, ?, ?, x2, y2, z2, ?, ?, ?, x3, y3, z3, ?, ?, ?, x4, y4, z4, ?, ?, ?}
        // if we look on the face side of decal's rectangle,
        // the corners will be ordered in this way:
        // 1 2
        // 3 4

        // I use triangle 123:
        // 1 2
        // 3
        triangle1[0]=verticesOfDecal[0]; //1
        triangle1[1]=verticesOfDecal[1];
        triangle1[2]=verticesOfDecal[2];
        triangle1[3]=verticesOfDecal[6]; //2
        triangle1[4]=verticesOfDecal[7];
        triangle1[5]=verticesOfDecal[8];
        triangle1[6]=verticesOfDecal[12]; //3
        triangle1[7]=verticesOfDecal[13];
        triangle1[8]=verticesOfDecal[14];

        //And triangle 234:
        //   2
        // 3 4
        triangle2[0]=verticesOfDecal[6]; //2
        triangle2[1]=verticesOfDecal[7];
        triangle2[2]=verticesOfDecal[8];
        triangle2[3]=verticesOfDecal[12]; //3
        triangle2[4]=verticesOfDecal[13];
        triangle2[5]=verticesOfDecal[14];
        triangle2[6]=verticesOfDecal[18]; //4
        triangle2[7]=verticesOfDecal[19];
        triangle2[8]=verticesOfDecal[20];

        return new Pair<float[], float[]>(triangle1, triangle2);*/
        return new Pair<float[], float[]>(triangle1, triangle2);
    }

}
