package ru.touchcube.domain;

/**
 * Created by grish on 21.08.2018.
 */

public class TouchCubeUtils {

    public static int reverseSide(int index){
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


}
