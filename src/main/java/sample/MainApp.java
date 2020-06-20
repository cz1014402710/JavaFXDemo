package sample;

import org.controlsfx.dialog.Dialogs;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.model.Person;
import sample.model.PersonListWrapper;
import sample.controller.BirthdayStatisticsController;
import sample.controller.PersonEditDialogController;
import sample.controller.PersonOverviewController;
import sample.controller.RootLayoutController;

public class MainApp extends Application {

    private Stage      mPrimaryStage;
    private BorderPane mRootLayout;

    private ObservableList<Person> mPersonObservableList = FXCollections.observableArrayList();

    public MainApp() {
        mPersonObservableList.add(new Person("Hans", "Muster"));
        mPersonObservableList.add(new Person("Ruth", "Mueller"));
        mPersonObservableList.add(new Person("Heinz", "Kurz"));
        mPersonObservableList.add(new Person("Cornelia", "Meier"));
        mPersonObservableList.add(new Person("Werner", "Meyer"));
        mPersonObservableList.add(new Person("Lydia", "Kunz"));
        mPersonObservableList.add(new Person("Anna", "Best"));
        mPersonObservableList.add(new Person("Stefan", "Meier"));
        mPersonObservableList.add(new Person("Martin", "Mueller"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 600, 400));
//        primaryStage.setResizable(false);
//        primaryStage.show();

        mPrimaryStage = primaryStage;
        mPrimaryStage.setTitle("AddressApp");

        mPrimaryStage.getIcons().add(new Image(String.valueOf(MainApp.class.getResource("/images/iconfinder_Address_Book_86957.png"))));

        initRootLayout();
        showPersonOverview();
    }

    private void initRootLayout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            System.out.println(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            fxmlLoader.setLocation(MainApp.class.getResource("/fxml/RootLayout.fxml"));
            mRootLayout = fxmlLoader.load();

            Scene scene = new Scene(mRootLayout);
//            mPrimaryStage.setResizable(false);
//            mPrimaryStage.setFullScreen(false);
            mPrimaryStage.setScene(scene);

            RootLayoutController rootLayoutController = fxmlLoader.getController();
            rootLayoutController.setMainApp(this);

            mPrimaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showPersonOverview() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApp.class.getResource("/fxml/PersonOverview.fxml"));
            AnchorPane anchorPane = fxmlLoader.load();
            mRootLayout.setCenter(anchorPane);

            PersonOverviewController controller = fxmlLoader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(Person person) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/PersonEditDialog.fxml"));
            AnchorPane pane = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Person");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mPrimaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            PersonEditDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPerson(person);
            dialogStage.showAndWait();

            return controller.isOkClicked();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public File getPersonFilePath() {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        String filePath = preferences.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    public void setPersonFilePath(File file) {
        Preferences preferences = Preferences.userNodeForPackage(MainApp.class);
        if (file != null) {
            preferences.put("filePaht", file.getPath());

            mPrimaryStage.setTitle("AddressApp - " + file.getName());
        } else {
            preferences.remove("filePaht");

            mPrimaryStage.setTitle("AddressApp");
        }
    }

    public void loadPersonDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);
            mPersonObservableList.clear();
            mPersonObservableList.addAll(wrapper.getPersons());
            setPersonFilePath(file);
        } catch (JAXBException e) {
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not load data from file: " + file.getPath())
                    .showException(e);
        }
    }

    public void savePersonDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(mPersonObservableList);
            m.marshal(wrapper, file);
            setPersonFilePath(file);
        } catch (JAXBException e) {
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not save data to file: " + file.getPath())
                    .showException(e);
        }
    }

    public void showBirthdayStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/BirthdayStatistics.fxml"));
            AnchorPane pane = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Birthday Statistics");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(mPrimaryStage);
            Scene scene = new Scene(pane);
            dialogStage.setScene(scene);

            BirthdayStatisticsController controller = loader.getController();
            controller.setPersonData(mPersonObservableList);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return mPrimaryStage;
    }

    public ObservableList<Person> getPersonObservableList() {
        return mPersonObservableList;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
