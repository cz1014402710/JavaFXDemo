package sample.controller;

import org.controlsfx.dialog.Dialogs;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import sample.MainApp;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/13
 */
public class RootLayoutController {

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
}
