package ru.touchcube.domain.utils;

import ru.touchcube.domain.model.Cube;

/**
 * Created by grish on 21.08.2018.
 */

public class TouchCubeUtils {

    public static int reverseSide(int side){
        switch (side){
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
        return side;
    }

    public static V3 getNearCubeCoordinates(Cube c, int side){
        V3 v = c.getPosition();
        switch (side){
            case 0:
                return new V3(v.x(), v.y(), v.z()-1);
            case 1:
                return new V3(v.x()+1, v.y(), v.z());
            case 2:
                return new V3(v.x(), v.y(), v.z()+1);
            case 3:
                return new V3(v.x()-1, v.y(), v.z());
            case 4:
                return new V3(v.x(), v.y()-1, v.z());
            case 5:
                return new V3(v.x(), v.y()+1, v.z());
        }
        return v;
    }

    public static boolean pointsAreNear(V3 first, V3 second){
        return Math.abs(first.x()+first.y()+first.z()-second.x()-second.y()-second.z())<=1;
    }

}
