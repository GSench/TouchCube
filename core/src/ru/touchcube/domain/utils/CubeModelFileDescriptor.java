package ru.touchcube.domain.utils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.V3;

public class CubeModelFileDescriptor {

    private static int BYTE_SIZE = 17;

    public static ArrayList<Cube> decode(byte[] encoded){
        //TODO
        int size = encoded.length/BYTE_SIZE;
        ArrayList<Cube> cubes = new ArrayList<Cube>(size);
        for(int i=0; i<size; i++)
            cubes.add(cubeFromBytes(Arrays.copyOfRange(encoded, i*BYTE_SIZE, (i+1)*BYTE_SIZE)));
        return cubes;
    }

    public static byte[] encode(ArrayList<Cube> decoded){
        //TODO
        ByteBuffer buffer = ByteBuffer.allocate(decoded.size()*BYTE_SIZE);
        for(Cube cube: decoded) buffer.put(cubeToBytes(cube));
        return buffer.array();
    }

    private static byte[] cubeToBytes(Cube cube){
        byte drawSidesByte = 0;
        for(int i=0; i<6; i++)
            drawSidesByte = (byte) ((drawSidesByte<<1)|(cube.getDrawSides()[i]?1:0));
        drawSidesByte = (byte) ((drawSidesByte<<1)|(cube.getColor().noColor()?1:0));
        return ByteBuffer.allocate(BYTE_SIZE)
                .put(drawSidesByte)
                .putInt(cube.getPosition().x())
                .putInt(cube.getPosition().y())
                .putInt(cube.getPosition().z())
                .put(((byte)(cube.getColor().r()-128)))
                .put(((byte)(cube.getColor().g()-128)))
                .put(((byte)(cube.getColor().b()-128)))
                .put(((byte)(cube.getColor().a()-128)))
                .array();
    }

    private static Cube cubeFromBytes(byte[] bytes){
        if(bytes.length!=BYTE_SIZE) return null;
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        byte drawSidesByte = buffer.get();
        boolean noColor = (drawSidesByte & 1) == 1;
        boolean[] drawSidesArray = new boolean[6];
        for(int i=5; i>=0; i--){
            drawSidesByte = (byte) (drawSidesByte>>1);
            drawSidesArray[i] = (drawSidesByte & 1) == 1;
        }
        Cube ret = new Cube(
                new V3(
                        buffer.getInt(),
                        buffer.getInt(),
                        buffer.getInt()),
                new Color(
                        ((int)buffer.get())+128,
                        ((int)buffer.get())+128,
                        ((int)buffer.get())+128,
                        ((int)buffer.get())+128,
                        noColor)
        );
        ret.setDrawSides(drawSidesArray);
        return ret;
    }

}
