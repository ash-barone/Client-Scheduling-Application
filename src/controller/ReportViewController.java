package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReportViewController {

    @FXML
    private Button backBtn;

    @FXML
    private TextArea reportTxtArea;

    /**
     *
     * @param event the event of clicking on the back button to navigate back to main menu
     * @throws Exception
     */
    @FXML
    void onActionBackToMainMenu(ActionEvent event) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event the event of clicking on the report 1 button to show the first report
     */
    @FXML
    void onActionShowReport1(ActionEvent event) {

        //TODO the actual report lol
    }

    /**
     *
     * @param event the event of clicking on the report 2 button to show the second report
     */
    @FXML
    void onActionShowReport2(ActionEvent event) {

        //TODO the actual second report
    }

    /**
     *
     * @param event the event of clicking on the report 3 button to show the third report
     */
    @FXML
    void onActionShowReport3(ActionEvent event) {

        //TODO the third report
    }
}
