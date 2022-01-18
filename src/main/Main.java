package main;

import DBAccess.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    /**
     * start begins at the login screen
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        // testing load screen System.out.println("pls");
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginScreen.fxml"));
        stage.setTitle("Login Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * connection to db starts and ends here
     * @param args
     */
    public static void main(String[] args) throws SQLException {
        //start connection
        JDBC.openConnection();

        //launch args
        launch(args);

        //close database
        JDBC.closeConnection();

    }
}
