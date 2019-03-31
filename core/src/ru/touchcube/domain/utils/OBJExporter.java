package ru.touchcube.domain.utils;

import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.V3;

/**
 * Created by grish on 08.12.2018.
 */

public class OBJExporter {

    private static final String OBJ_HEADER = "# TouchCube v1.2 OBJ File\n";
    private static final String MTL_HEADER = "# TouchCube v1.2 MTL File\n";

    public static Pair<String, String> exportObjAndMtl(ArrayList<Cube> cubes, String mtlFilename) {
        
        StringBuilder obj = new StringBuilder();
        StringBuilder mtl = new StringBuilder();
        
        obj.append(OBJ_HEADER);
        obj.append("mtllib ").append(mtlFilename).append("\n");

        int j;
        ArrayList<String> colors = new ArrayList<String>();

        for (int i=0; i<cubes.size(); i++){

            Cube cube = cubes.get(i);

            V3 p = cube.getPosition();
            obj.append("o Cube.").append(i).append("\n");
            obj.append("v ").append(p.x() - 0.5).append(" ").append(p.y() - 0.5).append(" ").append(p.z() + 0.5).append("\n");
            obj.append("v ").append(p.x() - 0.5).append(" ").append(p.y() + 0.5).append(" ").append(p.z() + 0.5).append("\n");
            obj.append("v ").append(p.x() + 0.5).append(" ").append(p.y() + 0.5).append(" ").append(p.z() + 0.5).append("\n");
            obj.append("v ").append(p.x() + 0.5).append(" ").append(p.y() - 0.5).append(" ").append(p.z() + 0.5).append("\n");
            obj.append("v ").append(p.x() - 0.5).append(" ").append(p.y() - 0.5).append(" ").append(p.z() - 0.5).append("\n");
            obj.append("v ").append(p.x() - 0.5).append(" ").append(p.y() + 0.5).append(" ").append(p.z() - 0.5).append("\n");
            obj.append("v ").append(p.x() + 0.5).append(" ").append(p.y() + 0.5).append(" ").append(p.z() - 0.5).append("\n");
            obj.append("v ").append(p.x() + 0.5).append(" ").append(p.y() - 0.5).append(" ").append(p.z() - 0.5).append("\n");

            String colorStr = cube.getColor().toString();
            if(!cube.getColor().noColor()){
                if(!colors.contains(colorStr)) colors.add(colorStr);
                obj.append("usemtl ").append(colorStr).append("\n");
            }
            obj.append("s off\n");

            obj.append("f ").append(i*8 + 5).append(" ").append(i*8 + 1).append(" ").append(i*8 + 2).append(" ").append(i*8 + 6).append("\n");
            obj.append("f ").append(i*8 + 7).append(" ").append(i*8 + 3).append(" ").append(i*8 + 2).append(" ").append(i*8 + 6).append("\n");
            obj.append("f ").append(i*8 + 8).append(" ").append(i*8 + 4).append(" ").append(i*8 + 3).append(" ").append(i*8 + 7).append("\n");
            obj.append("f ").append(i*8 + 5).append(" ").append(i*8 + 1).append(" ").append(i*8 + 4).append(" ").append(i*8 + 8).append("\n");
            obj.append("f ").append(i*8 + 5).append(" ").append(i*8 + 6).append(" ").append(i*8 + 7).append(" ").append(i*8 + 8).append("\n");
            obj.append("f ").append(i*8 + 1).append(" ").append(i*8 + 2).append(" ").append(i*8 + 3).append(" ").append(i*8 + 4).append("\n");
            
            obj.append("\n");
        }

        if(colors.size()==0) return new Pair<String, String>(obj.toString(), null);

        mtl.append(MTL_HEADER);

        for(String colorStr: colors){
            Color c = new Color(colorStr);
            mtl.append("newmtl ").append(colorStr).append("\n"); //material name
            mtl.append("Ns 100\n"); //specular exponent
            mtl.append("Ka 1 1 1\n"); //ambient color
            mtl.append("Kd ").append(c.r() / 255f).append(" ").append(c.g() / 255f).append(" ").append(c.b() / 255f).append("\n"); //diffuse color
            mtl.append("Ks 0 0 0\n"); //specular reflectivity
            mtl.append("Ke 0 0 0\n"); //emissive coeficient
            mtl.append("Ni 1\n"); //optical density
            mtl.append("d ").append(c.a() / 255f).append("\n"); //transparency
            mtl.append("illum 2\n"); //various material lighting and shading effects (2: Highlight on)
            mtl.append("\n");
        }

        return new Pair<String, String>(obj.toString(), mtl.toString());
    }

}
