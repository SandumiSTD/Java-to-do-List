/**
 * @auther : R.P.Sandumi Tharika Dilransamie
 * @since : 2/7/2023
 **/

import db.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        DBConnection object = DBConnection.getInstance();
        Connection connection = object.getConnection();
        System.out.println(connection);

        Parent parent = FXMLLoader.load(this.getClass().getResource("view/LoginForm.fxml"));
        Scene scene = new Scene(parent);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Login Form");
        primaryStage.centerOnScreen();
        primaryStage.show();

    }
}
