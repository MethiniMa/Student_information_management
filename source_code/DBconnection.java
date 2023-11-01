package edu.au;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import javax.swing.*;
import java.sql.*;

public class DBconnection {

    public static Connection ConnectDB(){
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project","root", "mxy9090");
            return connection;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }

    }
    public static ObservableList<student> getStudentInfo(){
        Connection connection = ConnectDB();
        ObservableList<student>data = FXCollections.observableArrayList();
        String sql = "SELECT * FROM student";
        try{
            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()){
                data.add(new student(rs.getInt("ssn"),rs.getString("firstName"),rs.getString("mi")
                        ,rs.getString("lastName"),rs.getLong("phone"), rs.getString("birthDate")
                        ,rs.getString("street"),rs.getInt("zipCode"),rs.getString("deptID")));

            }
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
        return data;

    }

}



