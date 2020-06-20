package sample.controller;


import org.controlsfx.dialog.DialogStyle;
import org.controlsfx.dialog.Dialogs;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sample.MainApp;
import sample.model.Person;
import sample.util.DateUtil;

/**
 * description: <一句话功能简述>
 *
 * @author Chenz
 * @date 2020/6/12
 */
public class PersonOverviewController {

    @FXML
    private TableView<Person>           mPersonTableView;
    @FXML
    private TableColumn<Person, String> mFirstNameColumn;
    @FXML
    private TableColumn<Person, String> mLastNameColumn;

    @FXML
    private Label mFirstNameLabel;
    @FXML
    private Label mLastNameLabel;
    @FXML
    private Label mStreetLabel;
    @FXML
    private Label mPostalCodeLabel;
    @FXML
    private Label mCityLabel;
    @FXML
    private Label mBirthdayLabel;

    private MainApp mMainApp;

    public PersonOverviewController() {
    }

    @FXML
    private void initialize() {
        mFirstNameColumn.setCellValueFactory(param -> param.getValue().firstNameProperty());
        mLastNameColumn.setCellValueFactory(param -> param.getValue().lastNameProperty());

        showPersonDetails(null);

        mPersonTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    @FXML
    private void handleNewPerson() {

        Person person = new Person("", "");
        boolean isOkClicked = mMainApp.showPersonEditDialog(person);
        if (isOkClicked){
            mMainApp.getPersonObservableList().add(person);
        }

    }

    @FXML
    private void handleEditPerson() {
       Person person = mPersonTableView.getSelectionModel().getSelectedItem();
       if (person != null){
           boolean isOkClicked = mMainApp.showPersonEditDialog(person);
           if (isOkClicked){
               showPersonDetails(person);
           }
       }else {
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.initOwner(mMainApp.getPrimaryStage());
           alert.setTitle("No Selection");
           alert.setHeaderText("No Person Selected");
           alert.setContentText("Please select a person in the table.");
           alert.showAndWait();
       }
    }

    @FXML
    private void handleDeletePerson() {
        int selectedIndex = mPersonTableView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            mPersonTableView.getItems().remove(selectedIndex);
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(mMainApp.getPrimaryStage());
            alert.setTitle("No Selection");
            alert.setHeaderText("No Person Selected");
            alert.setContentText("Please select a person in the table.");
            alert.showAndWait();

//          Dialogs dialogs =  Dialogs.create()
//                    .owner(mMainApp.getPrimaryStage())
//                    .style(DialogStyle.NATIVE)
//                    .title("No Selection")
//                    .masthead("No Person Selected")
//                    .message("Please select a person in the table !");
//          dialogs.showWarning();

        }
    }


    public void setMainApp(MainApp mainApp) {
        mMainApp = mainApp;

        mPersonTableView.setItems(mainApp.getPersonObservableList());
    }

    private void showPersonDetails(Person person) {
        if (person != null) {
            mFirstNameLabel.setText(person.getFirstName());
            mLastNameLabel.setText(person.getLastName());
            mStreetLabel.setText(person.getStreet());
            mPostalCodeLabel.setText(person.getPostalCode());
            mCityLabel.setText(person.getCity());
            mBirthdayLabel.setText(DateUtil.fomat(person.getBirthday()));
        } else {
            mFirstNameLabel.setText("");
            mLastNameLabel.setText("");
            mStreetLabel.setText("");
            mPostalCodeLabel.setText("");
            mCityLabel.setText("");
            mBirthdayLabel.setText("");
        }
    }
}
