package sample

import javafx.application.Application
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.stage.Modality
import javafx.stage.Stage
import org.controlsfx.dialog.Dialogs
import sample.controller.BirthdayStatisticsController
import sample.controller.PersonEditDialogController
import sample.controller.PersonOverviewController
import sample.controller.RootLayoutController
import sample.model.Person
import sample.model.PersonListWrapper
import java.io.File
import java.io.IOException
import java.util.prefs.Preferences
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller

class MainApp : Application() {
    var primaryStage: Stage? = null
        private set
    private var mRootLayout: BorderPane? = null
    val personObservableList: ObservableList<Person> = FXCollections.observableArrayList()
    override fun start(primaryStage: Stage) {

//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/sample.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 600, 400));
//        primaryStage.setResizable(false);
//        primaryStage.show();
        this.primaryStage = primaryStage
        primaryStage.title = "AddressApp"
        primaryStage.icons.add(Image(MainApp::class.java.getResource("/images/iconfinder_Address_Book_86957.png").toString()))
        initRootLayout()
        showPersonOverview()
    }

    private fun initRootLayout() {
        try {
            val fxmlLoader = FXMLLoader()
            println(MainApp::class.java.getResource("/fxml/RootLayout.fxml"))
            fxmlLoader.location = MainApp::class.java.getResource("/fxml/RootLayout.fxml")
            mRootLayout = fxmlLoader.load()
            val scene = Scene(mRootLayout)
            //            mPrimaryStage.setResizable(false);
//            mPrimaryStage.setFullScreen(false);
            primaryStage!!.scene = scene
            val rootLayoutController: RootLayoutController = fxmlLoader.getController()
            rootLayoutController.setMainApp(this)
            primaryStage!!.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showPersonOverview() {
        try {
            val fxmlLoader = FXMLLoader()
            fxmlLoader.location = MainApp::class.java.getResource("/fxml/PersonOverview.fxml")
            val anchorPane = fxmlLoader.load<AnchorPane>()
            mRootLayout!!.center = anchorPane
            val controller: PersonOverviewController = fxmlLoader.getController()
            controller.setMainApp(this)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun showPersonEditDialog(person: Person?): Boolean {
        return try {
            val loader = FXMLLoader()
            loader.location = MainApp::class.java.getResource("/fxml/PersonEditDialog.fxml")
            val pane = loader.load<AnchorPane>()
            val dialogStage = Stage()
            dialogStage.title = "Edit Person"
            dialogStage.initModality(Modality.WINDOW_MODAL)
            dialogStage.initOwner(primaryStage)
            val scene = Scene(pane)
            dialogStage.scene = scene
            val controller: PersonEditDialogController = loader.getController()
            controller.setDialogStage(dialogStage)
            controller.setPerson(person)
            dialogStage.showAndWait()
            controller.isOkClicked
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    var personFilePath: File?
        get() {
            val preferences = Preferences.userNodeForPackage(MainApp::class.java)
            val filePath = preferences["filePath", null]
            return filePath?.let { File(it) }
        }
        set(file) {
            val preferences = Preferences.userNodeForPackage(MainApp::class.java)
            if (file != null) {
                preferences.put("filePaht", file.path)
                primaryStage!!.title = "AddressApp - " + file.name
            } else {
                preferences.remove("filePaht")
                primaryStage!!.title = "AddressApp"
            }
        }

    fun loadPersonDataFromFile(file: File) {
        try {
            val context = JAXBContext.newInstance(PersonListWrapper::class.java)
            val um = context.createUnmarshaller()
            val wrapper = um.unmarshal(file) as PersonListWrapper
            personObservableList.clear()
            personObservableList.addAll(wrapper.persons)
            personFilePath = file
        } catch (e: JAXBException) {
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not load data from file: " + file.path)
                    .showException(e)
        }
    }

    fun savePersonDataToFile(file: File) {
        try {
            val context = JAXBContext.newInstance(PersonListWrapper::class.java)
            val m = context.createMarshaller()
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
            val wrapper = PersonListWrapper()
            wrapper.persons = personObservableList
            m.marshal(wrapper, file)
            personFilePath = file
        } catch (e: JAXBException) {
            Dialogs.create()
                    .title("Error")
                    .masthead("Could not save data to file: " + file.path)
                    .showException(e)
        }
    }

    fun showBirthdayStatistics() {
        try {
            val loader = FXMLLoader()
            loader.location = MainApp::class.java.getResource("/fxml/BirthdayStatistics.fxml")
            val pane = loader.load<AnchorPane>()
            val dialogStage = Stage()
            dialogStage.title = "Birthday Statistics"
            dialogStage.initModality(Modality.WINDOW_MODAL)
            dialogStage.initOwner(primaryStage)
            val scene = Scene(pane)
            dialogStage.scene = scene
            val controller: BirthdayStatisticsController = loader.getController()
            controller.setPersonData(personObservableList)
            dialogStage.show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(MainApp::class.java,*args)
        }
    }

    init {
        personObservableList.add(Person("Hans", "Muster"))
        personObservableList.add(Person("Ruth", "Mueller"))
        personObservableList.add(Person("Heinz", "Kurz"))
        personObservableList.add(Person("Cornelia", "Meier"))
        personObservableList.add(Person("Werner", "Meyer"))
        personObservableList.add(Person("Lydia", "Kunz"))
        personObservableList.add(Person("Anna", "Best"))
        personObservableList.add(Person("Stefan", "Meier"))
        personObservableList.add(Person("Martin", "Mueller"))
    }
}