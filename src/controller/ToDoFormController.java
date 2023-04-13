package controller;

import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;
import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.nio.cs.ext.ISO2022_CN_CNS;
import sun.security.jgss.wrapper.GSSCredElement;
import tm.ToDoTM;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

/**
 * @auther : R.P.Sandumi Tharika Dilransamie
 * @since : 2/7/2023
 **/
public class ToDoFormController {
    public Label lblTitle;
    public Label lblID;
    public AnchorPane root;
    public Pane subroot;
    public TextField txtToDo;
    public ListView<ToDoTM> lstToDos;
    public TextField txtSelectedTodo;
    public Button btnUpdate;
    public Button btnDelete;
    String id;

    public void initialize(){
        lblTitle.setText("Hello " + LoginFormController.enteredUserName + " Welcome to To-Do List");
        lblID.setText(LoginFormController.enteredID);

        subroot.setVisible(false);

        txtSelectedTodo.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        loadList();

        lstToDos.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoTM>() {
            @Override
            public void changed(ObservableValue<? extends ToDoTM> observable, ToDoTM oldValue, ToDoTM newValue) {
                txtSelectedTodo.setDisable(false);
                btnDelete.setDisable(false);
                btnUpdate.setDisable(false);

                subroot.setVisible(false);

                ToDoTM selectedItem = lstToDos.getSelectionModel().getSelectedItem();
                if(selectedItem == null){
                    return;
                }
                txtSelectedTodo.setText(selectedItem.getDescription());

                id = selectedItem.getId();
            }
        });
    }


    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to Log Out...?", ButtonType.YES, ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){

            Parent parent = FXMLLoader.load(this.getClass().getResource("../view/LoginForm.fxml"));
            Scene scene = new Scene(parent);

            Stage primaryStage = (Stage) root.getScene().getWindow();

            primaryStage.setScene(scene);
            primaryStage.setTitle("Login Form");
            primaryStage.centerOnScreen();
        }

    }

    public void btnAddNewToDoOnAction(ActionEvent actionEvent) {
        subroot.setVisible(true);

        txtSelectedTodo.setDisable(true);
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);

        lstToDos.getSelectionModel().clearSelection();
    }

    public void btnAddToListOnAction(ActionEvent actionEvent) {

        String id = autoGenerateID();
        String description = txtToDo.getText();
        String user_id = lblID.getText();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into todo values (?,?,?)");

            preparedStatement.setObject(1,id);
            preparedStatement.setObject(2,description);
            preparedStatement.setObject(3,user_id);

            int i = preparedStatement.executeUpdate();

            txtToDo.clear();
            subroot.setVisible(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadList();

    }

    public String autoGenerateID(){
        Connection connection = DBConnection.getInstance().getConnection();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select id from todo order by id desc limit 1");

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
                    return "T00" + intId;
                }else if(intId < 100){
                    return "T0" + intId;
                }else{
                    return "T" + intId;
                }

            }else{
                return "T001";
            }


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void loadList(){
        ObservableList<ToDoTM> todos = lstToDos.getItems();
        todos.clear();

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from todo where user_id = ?");
            preparedStatement.setObject(1,LoginFormController.enteredID);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                String user_id = resultSet.getString(3);

                ToDoTM object = new ToDoTM(id,description,user_id);
                todos.add(object);
            }
            lstToDos.refresh();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this todo..?",ButtonType.YES,ButtonType.NO);
        Optional<ButtonType> buttonType = alert.showAndWait();

        if(buttonType.get().equals(ButtonType.YES)){
            Connection connection = DBConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement("delete from todo where id = ?");

                preparedStatement.setObject(1,id);
                preparedStatement.executeUpdate();
                loadList();

                txtSelectedTodo.clear();
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
                txtSelectedTodo.setDisable(true);


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {

        Connection connection = DBConnection.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update todo set description = ? where id = ?");
            preparedStatement.setObject(1,txtSelectedTodo.getText());
            preparedStatement.setObject(2,id);

            preparedStatement.executeUpdate();

            loadList();

            txtSelectedTodo.clear();
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            txtSelectedTodo.setDisable(true);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


}
