package ru.touchcube.presentation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeDrawing;
import ru.touchcube.domain.utils.Pair;
import ru.touchcube.domain.utils.function_get;
import ru.touchcube.presentation.utils.V3F;

public class CubeDrawer extends CubeDrawing {

    public static final int CUBE_SIZE = 2;
    private Decal[] sides;
    private function_get<TextureRegion> getCubeTexture;
    private function_get<TextureRegion> getCubeNCTexture;
    private function_get<V3F> getCenter;

    public CubeDrawer(Cube cube,
                      function_get<TextureRegion> getCubeTexture,
                      function_get<TextureRegion> getCubeNCTexture,
                      function_get<V3F> getCenter) {
        super(cube);
        sides = new Decal[6];
        this.getCubeTexture=getCubeTexture;
        this.getCubeNCTexture=getCubeNCTexture;
        this.getCenter=getCenter;
    }

    public Decal[] getSides() {
        return sides;
    }

    @Override
    public void onDelete() {
        sides=null;
    }

    @Override
    public void onCreate() {

        int s=CUBE_SIZE;
        TextureRegion texture = getCubeNCTexture.get();
        TextureRegion textureC = getCubeTexture.get();
        int xReal = 0, yReal = 0, zReal = 0, angleAlpha = 0, angleBeta = 0;
        int x = getCube().getPosition().x();
        int y = getCube().getPosition().y();
        int z = getCube().getPosition().z();

        for (int i=0; i<6; i++) {
            sides[i] = Decal.newDecal(s, s, getCube().getColor().noColor()?texture:textureC, true);
            switch (i) {
                case 0:
                    xReal = x * s;
                    yReal = y * s;
                    zReal = z * s - s/2;
                    angleAlpha = 180;
                    angleBeta = 0;
                    break;
                case 1:
                    xReal = x * s + s/2;
                    yReal = y * s;
                    zReal = z * s;
                    angleAlpha = 90;
                    angleBeta = 0;
                    break;
                case 2:
                    xReal = x * s;
                    yReal = y * s;
                    zReal = z * s + s/2;
                    angleAlpha = 0;
                    angleBeta = 0;
                    break;
                case 3:
                    xReal = x * s - s/2;
                    yReal = y * s;
                    zReal = z * s;
                    angleAlpha = 270;
                    angleBeta = 0;
                    break;
                case 4:
                    xReal = x * s;
                    yReal = y * s - s/2;
                    zReal = z * s;
                    angleAlpha = 0;
                    angleBeta = 90;
                    break;
                case 5:
                    xReal = x * s;
                    yReal = y * s + s/2;
                    zReal = z * s;
                    angleAlpha = 0;
                    angleBeta = 270;
                    break;
            }
            sides[i].setPosition(xReal - getCenter.get().x(), yReal - getCenter.get().y(), zReal - getCenter.get().z());
            sides[i].rotateY(angleAlpha);
            sides[i].rotateX(angleBeta);
            if (!getCube().getColor().noColor()){
                float color = Color.toFloatBits(
                        getCube().getColor().r(),
                        getCube().getColor().g(),
                        getCube().getColor().b(),
                        getCube().getColor().a());
                sides[i].setColor(color);
            }
        }
    }

    @Override
    public void onColorChanged() {
        for(Decal side: sides){
            if(getCube().getColor().noColor()){
                side.setTextureRegion(getCubeNCTexture.get());
                side.setColor(Color.WHITE);
            } else {
                side.setTextureRegion(getCubeTexture.get());
                float color = Color.toFloatBits(
                        getCube().getColor().r(),
                        getCube().getColor().g(),
                        getCube().getColor().b(),
                        getCube().getColor().a());
                side.setColor(color);
            }
        }
    }

    public Pair<float[], float[]> getTrianglesOfSide(int side){
        float triangle1[] = new float[9];
        float triangle2[] = new float[9];

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

        return new Pair<float[], float[]>(triangle1, triangle2);
    }

}
