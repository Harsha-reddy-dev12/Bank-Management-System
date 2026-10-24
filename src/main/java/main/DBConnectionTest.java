package main;

import util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionTest {
    public static void main(String[] args) {
        try(Connection connection=DBConnection.getConnection()){
            System.out.println("Database connection successfull");
        }
        catch(SQLException e){
            System.out.println("Data base connection failed");
            System.out.println(e.getMessage());
        }
    }
}
