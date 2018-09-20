package ru.touchcube;

import android.app.Activity;
import android.content.Context;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.utils.function;


/**
 * Created by grish on 01.05.2017.
 */

public class AndroidInterface implements SystemInterface {

    private Activity act;
    public static final String SPREF = "preferences";

    public AndroidInterface(Activity act){
        this.act = act;
    }

    @Override
    public void doOnBackground(final function<Void> background) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                background.run();
            }
        }).start();

    }

    @Override
    public void doOnForeground(final function<Void> background) {
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                background.run();
            }
        });
    }

    /**
    @Override
    public Pair<byte[], HttpParams> httpGet(URL url, HttpParams params) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        if(params!=null){
            for(Pair<String, String> header: params.getHeaders()){
                urlConnection.setRequestProperty(header.f, header.s);
            }
        }
        urlConnection.setRequestMethod("GET");
        try {
            //Opening input stream
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            //getting response parameters
            int responseCode = urlConnection.getResponseCode();
            Iterator<Map.Entry<String, List<String>>> headers = urlConnection.getHeaderFields().entrySet().iterator();

            //proceeding response parameters
            HttpParams response = new HttpParams();
            ArrayList<Pair<String, String>> headersParam = new ArrayList<>();
            while (headers.hasNext()){
                Map.Entry<String, List<String>> entry = headers.next();
                headersParam.add(new Pair<>(entry.getKey(), entry.getValue().get(0)));
            }
            response.setHeaders(headersParam);
            response.setResultCode(responseCode);

            //reading input stream
            int available = in.available();
            if(available==0)
                return new Pair<>(readByByte(in), response);
            else
                return new Pair<>(IOUtils.readFully(in, in.available()), response);
        } finally {
            urlConnection.disconnect();
        }
    }
    private byte[] readByByte(InputStream in) throws IOException {
        ArrayList<Byte> byteChain = new ArrayList<>();
        while (true){
            try {
                byteChain.add(IOUtils.readFully(in, 1)[0]);
            } catch (EOFException e){
                break;
            }
        }
        byte[] ret = new byte[byteChain.size()];
        for(int i=0; i<byteChain.size(); i++) ret[i]=byteChain.get(i);
        return ret;
    }

    @Override
    public String[] getSavedStringArray(String title, String[] def) {
        Set<String> defaultArr;
        if(def==null) defaultArr = null;
        else defaultArr = new HashSet<>(Arrays.asList(def));
        Set<String> stringSet = act.getSharedPreferences(SPREF, Context.MODE_PRIVATE).getStringSet(title, defaultArr);
        return stringSet!=null ? stringSet.toArray(new String[stringSet.size()]) : def;
    }

    @Override
    public void saveStringArray(String title, String[] array) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .putStringSet(title, new HashSet<>(Arrays.asList(array)))
                .commit();
    }*/

    @Override
    public byte[] loadCashFile(String filename, byte[] def) {
        return new byte[0];
    }

    @Override
    public void saveToCashFile(String filename, byte[] bytes) {

    }

    @Override
    public String getSavedString(String title, String def) {
        return act.getSharedPreferences(SPREF, Context.MODE_PRIVATE).getString(title, def);
    }

    @Override
    public void saveString(String title, String string) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .putString(title, string)
                .commit();
    }

    /**
    @Override
    public int getSavedInt(String title, int def) {
        return act.getSharedPreferences(SPREF, Context.MODE_PRIVATE).getInt(title, def);
    }

    @Override
    public void saveInt(String title, int i) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .putInt(title, i)
                .commit();
    }*/

    /**
    @Override
    public void removeSaved(String str) {
        act.getSharedPreferences(SPREF, Context.MODE_PRIVATE)
                .edit()
                .remove(str)
                .commit();
    }

    @Override
    public String md5(String s) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(s.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }*/

}
