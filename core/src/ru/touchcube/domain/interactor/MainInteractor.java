package ru.touchcube.domain.interactor;

/**
 * Created by grish on 01.09.2018.
 */

public class MainInteractor {

    /**
    private TouchCubeWorld worldService;
    private Palette paletteService;
    private CubeModelManager modelManagerService;

    private MainPresenter presenter;

    public MainInteractor(MainPresenter presenter, SystemInterface system, CubeModelStorage storage){
        this.presenter=presenter;
        paletteService = new Palette(system, presenter);
        worldService = new TouchCubeWorld(presenter, getCurrentColor, system);
        modelManagerService = new CubeModelManager(presenter, getCurrentModel, loadModel, storage, system);
    }

    public void start(){
        presenter.init();
        paletteService.init();
        modelManagerService.loadFromCash();
    }

    public ArrayList<CubeDrawing> getCubes() {
        return worldService.getCubes();
    }

    public void tapOnCube(CubeDrawing cubeDrawing, int side){
        worldService.tapOnCube(cubeDrawing, side);
    }

    public void longTapOnCube(CubeDrawing cubeDrawing, int side) {
        worldService.reloadCurrent();
    }

    public void onClearButtonPushed(){
        worldService.onClearButtonPushed();
    }

    public void onCentreButtonPushed(){
        worldService.reloadCurrent();
    }

    public void isPutMode(){
        worldService.isPutMode();
    }

    public void isPaintMode(){
        worldService.isPaintMode();
    }

    public void isDeleteMode(){
        worldService.isDeleteMode();
    }

    public void onColorClick(int pos){
        paletteService.onColorClick(pos);
    }

    public void onColorPicked(Color color){
        paletteService.onColorPicked(color);
    }

    public void onFilesButtonClicked(){
        modelManagerService.onFilesButtonClicked();
    }

    public void onSaveCurrentModel(String title){
        modelManagerService.onSaveCurrentModel(title);
    }

    public void onLoadModel(CubeModelFile file){
        modelManagerService.onLoadModel(file);
    }

    public void onDeleteModel(CubeModelFile file){
        modelManagerService.onDeleteModel(file);
    }

    public void onShareModel(CubeModelFile file){
        modelManagerService.onShareModel(file);
    }

    public void finish(){
        paletteService.exit();
        modelManagerService.exit();
    }

    private function<ArrayList<Cube>> loadModel = new function<ArrayList<Cube>>() {
        @Override
        public void run(ArrayList<Cube>[] params) {
            worldService.load(params[0]);
        }
    };

    private function_get<ArrayList<Cube>> getCurrentModel = new function_get<ArrayList<Cube>>() {
        @Override
        public ArrayList<Cube> get() {
            ArrayList<CubeDrawing> currentModel = worldService.getCubes();
            ArrayList<Cube> cubes = new ArrayList<Cube>(currentModel.size());
            for(CubeDrawing cubeDrawing: currentModel) cubes.add(cubeDrawing.getCube());
            return cubes;
        }
    };

    private function_get<Color> getCurrentColor = new function_get<Color>() {
        @Override
        public Color get() {
            return paletteService.getCurrentColor();
        }
    };

    public void onColorPickerClosed() {
        paletteService.onColorPickerClosed();
    }*/
}
