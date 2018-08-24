package ru.touchcube.domain;

import java.util.ArrayList;

import ru.touchcube.domain.model.Color;
import ru.touchcube.domain.model.Cube;
import ru.touchcube.domain.model.V3;
import ru.touchcube.domain.presenter.CubeModelManagerPresenter;
import ru.touchcube.domain.utils.function;

/**
 * Created by Григорий Сенченок on 24.08.2018.
 */

public class CubeModelManager {

    private static final String CASH = "cash";

    private CubeModelManagerPresenter presenter;
    private SystemInterface system;

    public CubeModelManager(CubeModelManagerPresenter presenter, SystemInterface system) {
        this.presenter = presenter;
        this.system = system;
    }

    public void init(){
        loadFromCash();
    }

    public void exit(){
        saveToCash(presenter.getCurrentModel());
    }

    private void loadFromCash(){
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                byte[] modelEncoded = system.loadCashFile(CASH, null);
                if(modelEncoded==null){
                    final ArrayList<Cube> decoded = new ArrayList<Cube>();
                    decoded.add(new Cube(new V3(0,0,0), new Color(0,0,0,1,true)));
                    system.doOnForeground(new function<Void>() {
                        @Override
                        public void run(Void... params) {
                            presenter.loadModel(decoded);
                        }
                    });
                } else {
                    final ArrayList<Cube> decoded = decode(modelEncoded);
                    system.doOnForeground(new function<Void>() {
                        @Override
                        public void run(Void... params) {
                            presenter.loadModel(decoded);
                        }
                    });
                }
            }
        });
    }

    private void saveToCash(final ArrayList<Cube> model){
        system.doOnBackground(new function<Void>() {
            @Override
            public void run(Void... params) {
                system.saveToCashFile(CASH, encode(model));
            }
        });
    }

    private ArrayList<Cube> decode(byte[] encoded){
        //TODO
        return null;
    }

    private byte[] encode(ArrayList<Cube> decoded){
        //TODO
        return null;
    }

}
