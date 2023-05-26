package components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static DBConnector connector;

    private DBConnector(){}

    public static DBConnector getInstance(){
        if(connector == null){
            connector = new DBConnector();
        }
        return connector;
    }



    public Connection getConnection(){
        try{
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/kape", "root", "root");
        } catch (SQLException e){
            throw new RuntimeException(e.getCause());
        }
    }
}
