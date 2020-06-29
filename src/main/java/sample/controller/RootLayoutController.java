package sample.controller;

import org.controlsfx.dialog.Dialogs;

import java.io.File;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import sample.MainApp;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/13
 */
public class RootLayoutController {

    @FXML
    private ProgressBar mProgressBar;
    @FXML
    private Button      startButton;
    @FXML
    private Button      cancelButton;
    @FXML
    private Slider      slider;
    @FXML
    private HBox        mHBox;

    private MainApp mMainApp;

    public void setMainApp(MainApp mainApp) {
        mMainApp = mainApp;
    }

    @FXML
    private void handleNew() {
        mMainApp.getPersonObservableList().clear();
        mMainApp.setPersonFilePath(null);
    }

    @FXML
    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML files(*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(mMainApp.getPrimaryStage());

        if (file != null) {
            mMainApp.loadPersonDataFromFile(file);
        }
    }

    @FXML
    private void handleSave() {
        File file = mMainApp.getPersonFilePath();
        if (file != null) {
            mMainApp.savePersonDataToFile(file);
        } else {
            handleSaveAS();
        }
    }

    @FXML
    private void handleSaveAS() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("XML files(*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(filter);

        File file = fileChooser.showSaveDialog(mMainApp.getPrimaryStage());
        if (file != null) {
            if (!file.getPath().endsWith("xml")) {
                file = new File(file.getPath() + ".xml");
            }
            mMainApp.savePersonDataToFile(file);
        }
    }

    @FXML
    private void handleBirthdayStatistics() {
        mMainApp.showBirthdayStatistics();
    }

    @FXML
    private void handleAbout() {
//        Dialogs.create()
//                .title("AddressApp")
//                .masthead("About")
//                .message("Author: Chenz")
//                .showInformation();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mMainApp.getPrimaryStage());
        alert.setTitle("AddressApp");
        alert.setHeaderText("About");
        alert.setContentText("Author: Chenz");
        alert.showAndWait();


    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }

    private Task mTask;

    @FXML
    private void handleStart() {
        startButton.setDisable(true);
        mProgressBar.setProgress(0);
        cancelButton.setDisable(false);
        mTask = creatWork();
        mProgressBar.progressProperty().unbind();
        mProgressBar.progressProperty().bind(mTask.progressProperty());
        mTask.messageProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
        new Thread(mTask).start();
    }

    @FXML
    private void handleCancel() {
        startButton.setDisable(false);
        cancelButton.setDisable(true);
        mTask.cancel(true);
        mProgressBar.progressProperty().unbind();
        mProgressBar.setProgress(0);
        System.out.println("cancel");
    }

    public Task creatWork() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(2000);
                    updateMessage("2000 ms");
                    updateProgress(i + 1, 100);
                }
                return true;
            }
        };
    }

    @FXML
    private void initialize() {

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mProgressBar.setProgress(newValue.doubleValue() / 100);
            System.out.println(newValue.doubleValue() / 100);

        });

        ToggleGroup group = new ToggleGroup();
        ToggleButton button1 = new ToggleButton("High");
        button1.setToggleGroup(group);
        ToggleButton button2 = new ToggleButton("Medium");
        button1.setToggleGroup(group);
        button1.setSelected(true);
        ToggleButton button3 = new ToggleButton("Low");
        button1.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue==null){
                System.out.println("default value");
            }else {
                System.out.println(group.getSelectedToggle());
            }
        });

        mHBox.getChildren().addAll(button1,button2,button3);
    }

    @FXML
    private void handleTouch() {

    }
}
