package ru.touchcube.domain.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.V3;

// Class with static instruments of converting array of Cubes to byte[] and versa

public class CubeModelFileDescriptor {

    private static int BYTE_SIZE = 17;

    public static ArrayList<Cube> decode(byte[] encoded) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(encoded);
        byte braceByte = "^".getBytes(Charset.forName("UTF-8"))[0];

        //checking if model data contains starting byte
        int i;
        for(i=0; i<encoded.length; i++) if(encoded[i]==braceByte) break;
        if(i==encoded.length) throw new Exception();

        for (byte anEncoded : encoded) if (buffer.get() == braceByte) break;

        int size = buffer.remaining()/BYTE_SIZE;
        ArrayList<Cube> cubes = new ArrayList<Cube>(size);
        for(i=0; i<size; i++) cubes.add(cubeFromBytes(buffer));
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

    //old version support
    public static ArrayList<Cube> decodeVer1(byte[] encoded) throws Exception {
        String read = decrypt(encoded);
        ArrayList<Cube> ret = new ArrayList<Cube>();
        String sub, cubeStr = null;
        for (int i=0; i<read.length(); i++){
            if (read.substring(i, i+1).equals("b")){
                sub = read.substring(i);
                if (sub.indexOf("b", 3)<0){
                    cubeStr = sub.substring(1);
                } else {
                    cubeStr = sub.substring(1, sub.indexOf("b", 3));
                }
                boolean noColor = Boolean.parseBoolean(cubeStr.substring(cubeStr.indexOf("co")+2, cubeStr.indexOf("w")));
                float colorF = Float.parseFloat(cubeStr.substring(cubeStr.indexOf("z")+1, cubeStr.indexOf("c")));
                com.badlogic.gdx.graphics.Color cL = new com.badlogic.gdx.graphics.Color();
                com.badlogic.gdx.graphics.Color.abgr8888ToColor(cL, colorF);
                Color color = new Color((int)(cL.r*255), (int)(cL.g*255), (int)(cL.b*255), (int)(cL.a*255), noColor);

                int x = Integer.parseInt(cubeStr.substring(0, cubeStr.indexOf("x")));
                int y = Integer.parseInt(cubeStr.substring(cubeStr.indexOf("x")+1, cubeStr.indexOf("y")));
                int z = Integer.parseInt(cubeStr.substring(cubeStr.indexOf("y")+1, cubeStr.indexOf("z")));
                V3 pos = new V3(x, y, z);

                String faces = cubeStr.substring(cubeStr.indexOf("w")+1);
                boolean[] facesToDraw = new boolean[6];
                for(int j=0; j<6; j++) facesToDraw[j] = !faces.substring(j, j + 1).equals("0");

                Cube cube = new Cube(pos, color);
                cube.setDrawSides(facesToDraw);

                ret.add(cube);
            }
        }
        return ret;
    }

    private static String decrypt(byte[] bytes) throws Exception {
        byte[] keyStrict = "пиу пиу умри взломщик".getBytes("UTF-8");
        keyStrict = Arrays.copyOf(keyStrict, 24);
        SecretKey key = new SecretKeySpec(keyStrict, "AES");
        Cipher dcipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        dcipher.init(Cipher.DECRYPT_MODE, key);
        String str = new String(bytes, "UTF-8");
        byte[] dec = MyBase64.decode(str);
        byte[] doFinal=dcipher.doFinal(dec);
        return new String(doFinal, "UTF-8");
    }

}
