package ru.touchcube.presentation.utils;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;

import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.utils.Pair;

/**
 * Created by grish on 04.11.2018.
 */

public class MaterialManager {

    private ArrayList<Pair<Color, Material>> materials;

    public MaterialManager(){
        materials = new ArrayList<Pair<Color, Material>>();
    }

    public Material forColor(Color color){
        for(Pair<Color, Material> materialColor: materials){
            if(materialColor.f.toString().equals(color.toString())) return materialColor.s;
        }
        Material material = createNewForColor(color);
        materials.add(new Pair<Color, Material>(color, material));
        System.out.println("Material added: "+color.toString());
        System.out.println("Material count: "+materials.size());
        return material;
    }

    public void toColor(Material mat, Color color) {

    }

    private Material createNewForColor(Color color){
        if(color.noColor()){
            return new Material(ColorAttribute.createDiffuse(com.badlogic.gdx.graphics.Color.CHARTREUSE));
        } else {
            return new Material(ColorAttribute.createDiffuse(
                    color.r()/255,
                    color.g()/255,
                    color.b()/255,
                    color.a()/255
            ));
        }
    }
}
