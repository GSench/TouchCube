package ru.touchcube.domain.utils;

/**
 * Created by grish on 21.08.2018.
 */

public class Color {

    int ri;
    int gi;
    int bi;
    int ai;
    boolean noColorI;

    public Color(int ri, int gi, int bi, int ai, boolean noColorI) {
        this.ri = ri;
        this.gi = gi;
        this.bi = bi;
        this.ai = ai;
        this.noColorI = noColorI;
    }

    public int r(){
        return ri;
    }
    public int g(){
        return gi;
    }
    public int b(){
        return bi;
    }
    public int a(){
        return ai;
    }
    public boolean noColor(){
        return noColorI;
    }
}
