package ru.touchcube.domain.services;

import java.util.ArrayList;

import ru.touchcube.domain.SystemInterface;
import ru.touchcube.domain.interactor.CubeModelManagerPresenter;
import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.CubeModelFile;
import ru.touchcube.domain.model.CubeModelStorage;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.utils.CubeModelFileDescriptor;
import ru.touchcube.domain.utils.function;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public class CubeModelManager {

    private static final String CASH = "cash.cu";

    private static final String EXTENSION = "cu";

    private CubeModelManagerPresenter presenter;
    private CubeModelStorage storage;
    private SystemInterface system;

    public CubeModelManager(CubeModelManagerPresenter presenter, CubeModelStorage storage, SystemInterface system) {
        this.presenter = presenter;
        this.system = system;
        this.storage=storage;
    }

    public void onFilesButtonClicked() {
        presenter.openModelList(storage.getList());
    }

    public void onSaveCurrentModel(final String title){
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
            presenter.onNameError(title);
            return;
        }
        presenter.onStartSaving(title);
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                function<Void> result = new function<Void>() {
                    @Override
                    public void run(Void... params) {
                        presenter.onSaved(title);}
                };
                try {
                    CubeModelFile file = storage.createNew(title+"."+EXTENSION);
                    file.write(CubeModelFileDescriptor.encode(presenter.getCurrentModel()));
                } catch (Exception e) {
                    e.printStackTrace();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {
                            presenter.onSavingError(title);}
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
                        public void run(Void... params) {
                            presenter.loadModel(decoded);}
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {
                            presenter.onLoadError(file.getModelName());}
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
                        public void run(Void... params) {
                            presenter.updateModelList(storage.getList());}
                    };
                } catch (Exception e) {
                    e.printStackTrace();
                    result = new function<Void>() {
                        @Override
                        public void run(Void... params) {
                            presenter.onDeleteError(file.getModelName());}
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
        saveToCash(presenter.getCurrentModel());
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
                        public void run(Void... params) { presenter.loadModel(decoded); }
                    });
                } else {
                    final ArrayList<Cube> decoded = CubeModelFileDescriptor.decode(modelEncoded);
                    system.doOnForeground(new function<Void>() {
                        @Override
                        public void run(Void... params) { presenter.loadModel(decoded); }
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
