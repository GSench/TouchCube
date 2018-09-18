package ru.touchcube.domain.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.V3;

public class CubeModelFileDescriptor {

    private static int BYTE_SIZE = 17;

    public static ArrayList<Cube> decode(byte[] encoded){
        ByteBuffer buffer = ByteBuffer.wrap(encoded);
        byte braceByte = "^".getBytes(Charset.forName("UTF-8"))[0];
        for (byte anEncoded : encoded) if (buffer.get() == braceByte) break;

        int size = buffer.remaining()/BYTE_SIZE;
        ArrayList<Cube> cubes = new ArrayList<Cube>(size);
        for(int i=0; i<size; i++) cubes.add(cubeFromBytes(buffer));
        return cubes;
    }

    public static byte[] encode(ArrayList<Cube> decoded){
        byte[] header = "Touch Cube model encoded in binary format. Cubes' data starts after caret symbol byte ^".getBytes(Charset.forName("UTF-8"));
        ByteBuffer buffer = ByteBuffer.allocate(header.length+decoded.size()*BYTE_SIZE);
        buffer.put(header);
        for(Cube cube: decoded) putCube(cube, buffer);
        return buffer.array();
    }

    private static void putCube(Cube cube, ByteBuffer buffer){
        byte drawSidesByte = 0;
        for(int i=0; i<6; i++)
            drawSidesByte = (byte) ((drawSidesByte<<1)|(cube.getDrawSides()[i]?1:0));
        drawSidesByte = (byte) ((drawSidesByte<<1)|(cube.getColor().noColor()?1:0));
        //drawSidesByte is byte: 0ssssssn ,
        // where s = 1 if cube's side is drawing, 0 otherwise; ssssss is drawSides array from 0 to 5;
        // n = 1 if noColor is true, 0 otherwise;
        buffer
                .put(drawSidesByte) //1b
                .putInt(cube.getPosition().x()) //4b
                .putInt(cube.getPosition().y()) //4b
                .putInt(cube.getPosition().z()) //4b
                .put(((byte)(cube.getColor().r()-128))) //1b
                .put(((byte)(cube.getColor().g()-128))) //1b
                .put(((byte)(cube.getColor().b()-128))) //1b
                .put(((byte)(cube.getColor().a()-128))) //1b
        ; //17 bytes totally
    }

    private static Cube cubeFromBytes(ByteBuffer buffer){
        if(buffer.remaining()<BYTE_SIZE) return null;
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
