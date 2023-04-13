package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @auther : R.P.Sandumi Tharika Dilransamie
 * @since : 2/7/2023
 **/
public class LoginFormController {
    public AnchorPane root;
    public TextField txtUserName;
    public PasswordField txtPassword;

    public static String enteredUserName;
    public static String enteredID;

    public void lblCreateNewAccountOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("../view/CreateNewAccountForm.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create New Account");
        primaryStage.centerOnScreen();

    }

    public void btnLoginOnAction(ActionEvent actionEvent) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select *from user where user_name = ? and password = ?");

            preparedStatement.setObject(1,userName);
            preparedStatement.setObject(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){

                enteredUserName = userName;
                enteredID = resultSet.getString(1);

                Parent parent =FXMLLoader.load(this.getClass().getResource("../view/ToDoForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) root.getScene().getWindow();

                primaryStage.setScene(scene);
                primaryStage.setTitle("To-Do List");
                primaryStage.centerOnScreen();

            }else{
                new Alert(Alert.AlertType.ERROR,"Invalid username or password").showAndWait();
                txtUserName.clear();
                txtPassword.clear();

                txtUserName.requestFocus();
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }
}































