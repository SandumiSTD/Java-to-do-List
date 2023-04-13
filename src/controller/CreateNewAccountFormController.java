package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import sun.security.pkcs11.Secmod;

import java.io.IOException;
import java.sql.*;

/**
 * @auther : R.P.Sandumi Tharika Dilransamie
 * @since : 2/7/2023
 **/
public class CreateNewAccountFormController {
    public Label lblID;

    public TextField txtUserName;
    public TextField txtEmail;
    public PasswordField txtNewPassword;
    public PasswordField txtConfirmPassword;
    public Button btnRegister;
    public Label lblPasswordNotMatch1;
    public Label lblPasswordNotMatch2;
    public AnchorPane root;

    public void initialize(){
        txtUserName.setDisable(true);
        txtEmail.setDisable(true);
        txtNewPassword.setDisable(true);
        txtConfirmPassword.setDisable(true);
        btnRegister.setDisable(true);
        lblPasswordNotMatch1.setVisible(false);
        lblPasswordNotMatch2.setVisible(false);
    }

    public void btnAddNewUserOnAction(ActionEvent actionEvent) {

        autoGenerateID();

        txtUserName.setDisable(false);
        txtEmail.setDisable(false);
        txtNewPassword.setDisable(false);
        txtConfirmPassword.setDisable(false);
        btnRegister.setDisable(false);

    }

    public void autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select user_id from user order by user_id desc limit 1");

            boolean isExist = resultSet.next();

            if(isExist){

                String oldId = resultSet.getString(1);
                //System.out.println(oldId);

                int length = oldId.length();
                String id = oldId.substring(1, length);
                //System.out.println(id);

                int intId = Integer.parseInt(id);
                //System.out.println(intId);

                intId = intId + 1;
                //System.out.println(intId);

                //lblID.setText("U00" + intId);
                if(intId < 10){
                    lblID.setText("U00" + intId);
                }else if(intId < 100){
                    lblID.setText("U0" + intId);
                }else{
                    lblID.setText("U" + intId);
                }

            }else{
                lblID.setText("U001");
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnRegisterOnAction(ActionEvent actionEvent) {

        String newPassword = txtNewPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();

        if(newPassword.equals(confirmPassword)) {
            txtNewPassword.setStyle("-fx-border-color: transparent");
            txtConfirmPassword.setStyle("-fx-border-color: transparent");

            lblPasswordNotMatch1.setVisible(false);
            lblPasswordNotMatch2.setVisible(false);

            register();

        }else{
            txtNewPassword.setStyle("-fx-border-color:red");
            txtConfirmPassword.setStyle("-fx-border-color:red");

            lblPasswordNotMatch1.setVisible(true);
            lblPasswordNotMatch2.setVisible(true);

            txtNewPassword.requestFocus();
        }
    }

    public void register(){
        String id = lblID.getText();
        String username = txtUserName.getText();
        String email = txtEmail.getText();
        String password = txtConfirmPassword.getText();

        Connection connection = DBConnection.getInstance().getConnection();


        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into user values(?,?,?,?)");
            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,username);
            preparedStatement.setObject(3,email);
            preparedStatement.setObject(4,password);

            int i = preparedStatement.executeUpdate();

            if(i != 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Success...").showAndWait();

                Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
                Scene scene = new Scene(parent);

                Stage primaryStage = (Stage) root.getScene().getWindow();

                primaryStage.setScene(scene);
                primaryStage.setTitle("Login Form");
                primaryStage.centerOnScreen();

            }else{
                new Alert(Alert.AlertType.ERROR,"Something Went Wrong...").showAndWait();
            }


        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
