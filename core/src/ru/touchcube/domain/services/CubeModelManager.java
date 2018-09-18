package ru.touchcube.domain.services;

import java.util.ArrayList;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.interactor.CubeModelManagerFace;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.utils.CubeModelFileDescriptor;
import ru.touchcube.domain.utils.function;
import ru.touchcube.domain.utils.function_get;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public class CubeModelManager {

    private static final String CASH = "cash.cu";

    private static final String EXTENSION = "cu";

    private CubeModelManagerFace face;
    private CubeModelStorage storage;
    private SystemInterface system;
    private function_get<ArrayList<Cube>> currentModel;
    private function<ArrayList<Cube>> loadModel;

    public CubeModelManager(CubeModelManagerFace face, function_get<ArrayList<Cube>> currentModel, function<ArrayList<Cube>> loadModel, CubeModelStorage storage, SystemInterface system) {
        this.face = face;
        this.currentModel=currentModel;
        this.loadModel = loadModel;
        this.system = system;
        this.storage=storage;
    }

    public void onFilesButtonClicked() {
        face.openModelList(storage.getList());
    }

    public void onSaveModel(final String title){
        if(
                title.equals("")||
                title.contains(".")||
                title.contains("*")||
                title.contains("\\")||
                title.contains("/")||
                title.contains(":")||
                title.contains("?")||
                title.contains("|")||
                title.contains("\"")){
            face.onNameError(title);
            return;
        }
        face.onStartSaving(title);
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                function<Void> result = new function<Void>() {
                    @Override
                    public void run(Void... params) {
                        face.onSaved(title);}
                };
                try {
                    CubeModelFile file = storage.createNew(title+"."+EXTENSION);
                    file.write(CubeModelFileDescriptor.encode(currentModel.get()));
                } catch (Exception e) {
                    e.printStackTrace();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {
                            face.onSavingError(title);}
                    };
                }
                system.doOnForeground(result);
            }
        });
    }

    public void onLoadModel(final CubeModelFile file){
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                function<Void> result;
                try {
                    final ArrayList<Cube> decoded = CubeModelFileDescriptor.decode(file.read());
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {loadModel.run(decoded);}
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {face.onLoadError(file.getModelName());}
                    };
                }
                system.doOnForeground(result);
            }
        });
    }

    public void onDeleteModel(final CubeModelFile file){
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                function<Void> result;
                try {
                    file.delete();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {face.updateModelList(storage.getList());}
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {face.onDeleteError(file.getModelName());}
                    };
                }
                system.doOnForeground(result);
            }
        });
    }

    public void onShareModel(CubeModelFile file){
        file.share();
    }

    public void exit(){
        saveToCash(currentModel.get());
    }

    public void loadFromCash(){
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                byte[] modelEncoded = system.loadCashFile(CASH, null);
                if(modelEncoded==null||modelEncoded.length==0){
                    final ArrayList<Cube> decoded = new ArrayList<Cube>();
                    decoded.add(new Cube(new V3(0,0,0), new Color(0,0,0,1,true)));
                    system.doOnForeground(new function<Void>() {
                        @Override
                        public void run(Void... params) { loadModel.run(decoded); }
                    });
                } else {
                    final ArrayList<Cube> decoded = CubeModelFileDescriptor.decode(modelEncoded);
                    system.doOnForeground(new function<Void>() {
                        @Override
                        public void run(Void... params) { loadModel.run(decoded); }
                    });
                }
            }
        });
    }

    private void saveToCash(final ArrayList<Cube> model){
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                system.saveToCashFile(CASH, CubeModelFileDescriptor.encode(model));
            }
        });
    }

}
