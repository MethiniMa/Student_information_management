package edu.au;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LongStringConverter;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML private TableView<student> table;
    @FXML private TableColumn<student,Integer> col_ssn;
    @FXML private TableColumn<student,String> col_firstname;
    @FXML private TableColumn<student,String> col_mi;
    @FXML private TableColumn<student,String> col_lastname;
    @FXML private TableColumn<student,Long> col_phone;
    @FXML private TableColumn<student,String> col_birth;
    @FXML private TableColumn<student,String> col_street;
    @FXML private TableColumn<student,Integer> col_zipcode;
    @FXML private TableColumn<student,String> col_dept;

    @FXML private TextField tf_fName;
    @FXML private TextField tf_Mi;
    @FXML private TextField tf_lName;
    @FXML private TextField tf_phone;
    @FXML private DatePicker tf_birth;
    @FXML private TextField tf_street;
    @FXML private TextField tf_zip;
    @FXML private TextField tf_dep;
    @FXML private TextField tf_deleteSSN;
    @FXML private PieChart piechart;
    @FXML private BarChart barchart;

    ObservableList<student> dataC = FXCollections.observableArrayList();
    Connection connection = null;
    ResultSet rs = null;
    PreparedStatement pst = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Table_update();
        col_firstname.setCellFactory(TextFieldTableCell.forTableColumn());
        col_mi.setCellFactory(TextFieldTableCell.forTableColumn());
        col_lastname.setCellFactory(TextFieldTableCell.forTableColumn());
        col_phone.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        col_birth.setCellFactory(TextFieldTableCell.forTableColumn());
        col_street.setCellFactory(TextFieldTableCell.forTableColumn());
        col_zipcode.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        col_dept.setCellFactory(TextFieldTableCell.forTableColumn());


        table.setEditable(true);
        table.getSelectionModel().setCellSelectionEnabled(true);
    }

    public void Adding (){
        connection = DBconnection.ConnectDB();
        String sql = "insert into student (firstName,mi,lastName,phone,birthDate,street,zipCode,deptID)values(?,?,?,?,?,?,?,? )";
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, tf_fName.getText());
            if(tf_fName.getText().length()>10){
                JOptionPane.showMessageDialog(null, "First name is too long");
            }else if(tf_fName.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "First name cannot be null");
            }
            pst.setString(2, tf_Mi.getText().toUpperCase());
            if (tf_Mi.getText().length()>1){
                JOptionPane.showMessageDialog(null, "Only 1 letter for the middle name");
            }
            pst.setString(3, tf_lName.getText());
            if (tf_lName.getText().length()>10){
                JOptionPane.showMessageDialog(null, "Last name is too long");
            }else if(tf_lName.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Last name cannot be null");
            }
            pst.setLong(4, Long.parseLong(tf_phone.getText()));

            pst.setString(5, tf_birth.getValue().toString());
            pst.setString(6, tf_street.getText());
            if(tf_street.getText().length()>50){
                JOptionPane.showMessageDialog(null, "Street name is too long");
            }else if(tf_street.getText().isEmpty()){
                JOptionPane.showMessageDialog(null, "Street cannot be empty");
            }
            pst.setInt(7, Integer.parseInt(tf_zip.getText()));

            pst.setString(8, tf_dep.getText());
            pst.execute();
            JOptionPane.showMessageDialog(null, "Add successful");


        } catch (Exception e) {
            if(tf_phone.getText().length()>11||tf_phone.getText().length()<7){
                JOptionPane.showMessageDialog(null, "Phone number is invalid");
            }else if(tf_zip.getText().length()>6||tf_zip.getText().length()==0){
                JOptionPane.showMessageDialog(null, "Zipcode is invalid");
            }else if(tf_birth.getValue().toString().length()==0){
                JOptionPane.showMessageDialog(null, "Birth date cannot be empty");
            }

        }
    }

    public void Delete_student(){
        connection = DBconnection.ConnectDB();
        String sql = "delete from student where ssn = ?";
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(tf_deleteSSN.getText()));
            pst.execute();
            JOptionPane.showMessageDialog(null, "Delete");

        } catch (Exception e) {
            if(tf_deleteSSN.getText().length()==0){
                JOptionPane.showMessageDialog(null, "SSN cannot be empty");
            }
        }
    }

    public void Table_update(){
        connection = DBconnection.ConnectDB();

        col_ssn.setCellValueFactory(new PropertyValueFactory<>("ssn"));
        col_firstname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        col_mi.setCellValueFactory(new PropertyValueFactory<>("mi"));
        col_lastname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        col_birth.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        col_street.setCellValueFactory(new PropertyValueFactory<>("street"));
        col_zipcode.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        col_dept.setCellValueFactory(new PropertyValueFactory<>("deptID"));

        dataC = DBconnection.getStudentInfo();
        table.setItems(dataC);

    }

    public void Chart_refresh(){
        int a=0, b=0, c=0, d=0;
        connection = DBconnection.ConnectDB();
        String sql = "SELECT DeptID FROM student";
        try {
            rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                if (rs.getString("deptID").equals("BIOL")) {
                    a++;
                }
                if (rs.getString("deptID").equals("CS")) {
                    b++;
                }
                if (rs.getString("deptID").equals("CHEM")) {
                    c++;
                }else if (rs.getString("deptID").equals("MATH")) {
                    d++;
                }
            }} catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<javafx.scene.chart.PieChart.Data> pieChartData =
                FXCollections.observableArrayList( new javafx.scene.chart.PieChart.Data("Biology",a),
                        new javafx.scene.chart.PieChart.Data("Computer Science",b),
                        new javafx.scene.chart.PieChart.Data("Chemistry",c),
                        new javafx.scene.chart.PieChart.Data("Math",d));

        piechart.setTitle("Student Department");
        piechart.setData(pieChartData);

        pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName()," : ",
                data.pieValueProperty())));

        XYChart.Series bardata = new XYChart.Series();

        bardata.setName("Number of Student");

        bardata.getData().add(new XYChart.Data("Biology", a));
        bardata.getData().add(new XYChart.Data("Computer Science", b));
        bardata.getData().add(new XYChart.Data("Math", c));
        bardata.getData().add(new XYChart.Data("Chemistry", d));

        barchart.getData().add(bardata);
    }
}

