package ru.touchcube.domain.utils;

import java.io.IOException;
import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.V3;

/**
 * Created by grish on 08.12.2018.
 */

public class OBJExporter {

    private static final String OBJ_HEADER = "# TouchCube v? OBJ File\n"; //TODO version
    private static final String MTL_HEADER = "# TouchCube v? MTL File\n"; //TODO version

    public static Pair<String, String> exportObjAndMtl(ArrayList<Cube> cubes, String mtlFilename) throws IOException {
        
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
            obj.append("v ").append(p.z() - 0.5).append(" ").append(p.x() - 0.5).append(" ").append(p.y() + 0.5).append("\n");
            obj.append("v ").append(p.z() - 0.5).append(" ").append(p.x() + 0.5).append(" ").append(p.y() + 0.5).append("\n");
            obj.append("v ").append(p.z() + 0.5).append(" ").append(p.x() + 0.5).append(" ").append(p.y() + 0.5).append("\n");
            obj.append("v ").append(p.z() + 0.5).append(" ").append(p.x() - 0.5).append(" ").append(p.y() + 0.5).append("\n");
            obj.append("v ").append(p.z() - 0.5).append(" ").append(p.x() - 0.5).append(" ").append(p.y() - 0.5).append("\n");
            obj.append("v ").append(p.z() - 0.5).append(" ").append(p.x() + 0.5).append(" ").append(p.y() - 0.5).append("\n");
            obj.append("v ").append(p.z() + 0.5).append(" ").append(p.x() + 0.5).append(" ").append(p.y() - 0.5).append("\n");
            obj.append("v ").append(p.z() + 0.5).append(" ").append(p.x() - 0.5).append(" ").append(p.y() - 0.5).append("\n");

            String colorStr = cube.getColor().toString();
            if(!colors.contains(colorStr)) colors.add(colorStr);
            obj.append("usemtl ").append(colorStr).append("\n");
            obj.append("s off\n");

            obj.append("f ").append(i*8 + 5).append(" ").append(i*8 + 1).append(" ").append(i*8 + 2).append(" ").append(i*8 + 6).append("\n");
            obj.append("f ").append(i*8 + 7).append(" ").append(i*8 + 3).append(" ").append(i*8 + 2).append(" ").append(i*8 + 6).append("\n");
            obj.append("f ").append(i*8 + 8).append(" ").append(i*8 + 4).append(" ").append(i*8 + 3).append(" ").append(i*8 + 7).append("\n");
            obj.append("f ").append(i*8 + 5).append(" ").append(i*8 + 1).append(" ").append(i*8 + 4).append(" ").append(i*8 + 8).append("\n");
            obj.append("f ").append(i*8 + 5).append(" ").append(i*8 + 6).append(" ").append(i*8 + 7).append(" ").append(i*8 + 8).append("\n");
            obj.append("f ").append(i*8 + 1).append(" ").append(i*8 + 2).append(" ").append(i*8 + 3).append(" ").append(i*8 + 4).append("\n");
            
            obj.append("\n");
        }

        mtl.append(MTL_HEADER);

        for(String colorStr: colors){
            Color c = new Color(colorStr);
            mtl.append("newmtl ").append(colorStr).append("\n"); //material name
            mtl.append(c.noColor()? "Ns 0\n" : "Ns 100\n"); //specular exponent
            mtl.append(c.noColor()? "Ka 0 0.41 0.59\n" : "Ka 1 1 1\n"); //ambient color
            mtl.append(c.noColor()? "Kd 0.08 0.08 0.08\n" : "Kd "+c.r()/255f+" "+c.g()/255f+" "+c.b()/255f+"\n"); //diffuse color
            mtl.append("Ks 0 0 0\n"); //specular reflectivity
            mtl.append("Ke 0 0 0\n"); //emissive coeficient
            mtl.append("Ni 1\n"); //optical density
            mtl.append(c.noColor()? "d 1\n" : "d "+c.a()/255f+"\n"); //transparency
            mtl.append("illum 2\n"); //various material lighting and shading effects (2: Highlight on)
            mtl.append("\n");
        }

        return new Pair<String, String>(obj.toString(), mtl.toString());
    }

}
