package ru.touchcube.domain.model;

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

    public Color(String str){
        ri = Integer.parseInt(str.substring(0,2), 16);
        gi = Integer.parseInt(str.substring(2,4), 16);
        bi = Integer.parseInt(str.substring(4,6), 16);
        ai = Integer.parseInt(str.substring(6,8), 16);
        noColorI = str.substring(8).equals("1");
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

    @Override
    public String toString(){
        return String.format("%02X", ri)+String.format("%02X", gi)+String.format("%02X", bi)+String.format("%02X", ai)+(noColorI?"1":"0");
    }
}
